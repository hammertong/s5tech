/**********************************************************************************
 *
 *           S5TECH(c) NETWORK APPLICATION DOCUMENTATION AND LICENSE
 *                        Version 1.6, September 2014
 *                          http://www.s5tech.com/
 *
 * Permission to copy, modify, and distribute this software and its documentation,
 * with or without modification, for any purpose and without fee or royalty is
 * hereby granted.
 * 
 * THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 * NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 * THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 * PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 * 
 * COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 * CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 * 
 * The name and trademarks of  copyright holders may NOT be used in advertising or
 * publicity pertaining to the software without specific, written prior permission.
 * Title to copyright in this software and any associated documentation will at
 * all times remain with copyright holders.
 * 
 * FOR INFORMATION ABOUT OBTAINING, INSTALLING AND RUNNING THIS SOFTWARE WRITE AN
 * EMAIL TO assist@s5tech.com
 * 
 * S5Tech Development Team 2015-01-15
 * S5Tech S.P.A, Via Caboto 10, 20100 Legnano - Italy
 * 		
 *********************************************************************************/
 
package com.s5tech.net.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.castor.core.util.Base64Decoder;
import org.castor.core.util.Base64Encoder;

import com.s5tech.data.JdbcConnectionFactory;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.util.ConnectionFactory;
import com.s5tech.net.util.Tools;

/**
 * TODO: 
 * al momento pendingUpdatId viene usato per la persistenza:
 * 		1) viene impostato a -1 quando il price update (active o pending) viene messo sul buffer di uscita 
 * 		2) ritorna a numero long integer tempo corrente in ms da 1970 quando viene scritto su seriale
 * 		   (in situazioni regolari corrisponde a quando esce dal buffer)
 * 
 * @author x-man
 *
 */
public class JdbcEslDataStore 
		extends GenericEntityManager<EUI64Address, JdbcConnectionFactory> 
		implements IEslDataStore {

	private static final long serialVersionUID = 479343467889701413L;
	
	private Object mapLock = new Object();
		
	private long latestPriceId = -1;
	
	public JdbcEslDataStore() {
		try {
			load();			
		} catch (Exception e) {
			log.error("initializer error: {}", e.getMessage());
		}
	}
	
	public void setActivePriceForEsl(EUI64Address esl, EslPriceData price) {
		setPriceForEsl(esl, price, true);
	}
	
	public void setPendingPriceForEsl(EUI64Address esl, EslPriceData price) {
		setPriceForEsl(esl, price, false);
	}
	
	public void removeActivePriceForEsl(EUI64Address esl) {
		removePriceForEsl(esl, true);
	}
	
	public void removePendingPriceForEsl(EUI64Address esl) {
		removePriceForEsl(esl, false);		
	}

	private void removePriceForEsl(EUI64Address esl, boolean active) 
	{
		if (esl == null || get(esl) == null) return;		
	
		Connection connection = null;
		Statement st = null;
		
		try {
			
			connection = ConnectionFactory.getInstance().createConnection();
			if (connection != null)
			{
				connection.setAutoCommit(true);
				st = connection.createStatement();
			}
			
			synchronized (mapLock) 
			{
				JdbcConnectionFactory prices = get(esl);
				
				if(active) 
				{			
					//update map				
					prices.setActivePrice(null);
					
					//update database
					if (st != null) {
						st.execute (
							new StringBuffer("update pricesList ")
							.append("set activeUpdateId = NULL, ")
							.append("activeRefId = NULL, activePublishingDate = NULL, ")
							.append("activeHash = NULL, activePrice = NULL ")
							.append("where esl = '").append(esl.toString())
							.append("' and processed = 1")
							.toString());		
					}
				}
				else 
				{						
					//update map
					prices.setPendingPrice(null);
					
					//update database
					if (st != null) {
						st.execute (
							new StringBuffer("update pricesList ")
							.append("set pendingUpdateId = NULL, ")
							.append("pendingRefId = NULL, pendingPublishingDate = NULL, ")
							.append("pendingHash = NULL, pendingPrice = NULL ")
							.append("where esl = '").append(esl.toString())
							.append("' and processed = 1")
							.toString());		
					}
				}
				
				if (prices.getPendingPrice() == null && prices.getActivePrice() == null) 
				{
					//update map
					remove(esl);	
					
					//update database
					if (st != null) {
						st.execute (new StringBuffer()			
							.append("delete from pricesList ")
							.append("where esl = '").append(esl.toString())
							.append("' and processed = 1")
							.toString());
					}					
				}					
			}
			
			if (connection == null || st == null) throw new SQLException("failure establishing jdbc data access, cannot write to database!");
			
		}
		catch (Throwable e) 
		{
			log.error("error removing " 
					+ (active ? "active": "pending") + " price for esl {} from pricesList - reason: {}", 
					esl, e);	
		}
		finally 
		{
			try { if (st != null) st.close(); } catch (SQLException ignored) {} 
			try { if (connection != null) connection.close(); } catch (SQLException ignored) {} 
		}
		
	}
	
	private void setPriceForEsl(EUI64Address esl, EslPriceData price, boolean active)
	{
		
		if(esl == null || price == null || price.getData() == null)
		{
			log.error(
					"Invalid arguments for price update, on or more required values is missing. ESL : " 
							+ esl + ", Price: " 
							+ (price == null ? "null" : Tools.toHexString(price.getData())));
			return;
		}
		
		if (log.isTraceEnabled()) {
			log.trace("updating {} price for esl {} ...", (active ? "active" : "pending"), esl);
		}
		
		synchronized (mapLock) 
		{
			
			Connection connection = null;
			Statement st = null;
			PreparedStatement pst1 = null;
			PreparedStatement pst2 = null;
			PreparedStatement pst3 = null;
			
			try {
				
				connection = ConnectionFactory.getInstance().createConnection();

				if (connection != null)
				{
					connection.setAutoCommit(true);
					st = connection.createStatement();
				}
							
				//
				// alcuni check preliminari prima di salvare i dati
				//
				
				long now = System.currentTimeMillis();
				
				//
				// il vecchio pending e' diventato active 
				//			
				
				if (get(esl) != null && get(esl).getPendingPrice() != null)  {
					
					long atime =get(esl).getPendingPrice().getActivationTime().getTime();
					atime += TimeZone.getDefault().getOffset(new Date().getTime());	
							
					if (active && atime < now) {
						
						if (log.isTraceEnabled()) 
							log.trace ("Pending price registered for esl {} out of date, removing it ...", esl);
						
						// il prezzo inviato e' active, rimuovo il pending perche' e' scaduto,
						// il nuovo active inviato sovrascriverà il vecchio pending diventato active
						
						//update map
						get(esl).setPendingPrice(null);
						
						//update db
						if (st != null) {
							st.executeUpdate (
								new StringBuffer("update pricesList ")
								.append("set pendingUpdateId = NULL, ")
								.append("pendingRefId = NULL, pendingPublishingDate = NULL, ")
								.append("pendingHash = NULL, pendingPrice = NULL ")
								.append("where esl = '").append(esl.toString())
								.append("' and processed = 1")
								.toString());
						}
						
					}
					else {
						
						if (log.isTraceEnabled()) 
							log.trace("Pending price registered for esl {} is now active, saving the new pending price ...", esl);
						
						// il prezzo inviato e' pending, il vecchio pending diventa active
						EslPriceData p = get(esl).getPendingPrice();
						
						//update map
						get(esl).setActivePrice(p);
						
						//update db
						if (connection != null) {
							pst1 = connection.prepareStatement(new StringBuffer()
									.append("update pricesList ")
									.append("set activeUpdateId = ").append(p.getReceivedAt()).append(", ")
									.append("activeRefId = ").append(p.getRefId()).append(", ")
									.append("activePublishingDate = ?, ")
									.append("activeHash = ").append(p.getHashWhenApplied()).append(", ")
									.append("activePrice = '").append(Base64Encoder.encode(p.getData())).append("' ")
									.append("where esl = '").append(esl.toString()).append("' and processed = 1; ")
									.toString());				
							pst1.setTimestamp(1, new Timestamp(p.getActivationTime().getTime()));
							pst1.executeUpdate();		
							//pst.close();							
						}
						
					}
								
				}
				
				//
				// fine operazioni preliminari di check 
				// inizio operazioni di set price data
				//			
				
				if (price.getReceivedAt() > latestPriceId) latestPriceId = price.getReceivedAt();
				
				JdbcConnectionFactory priceSet = get(esl);
				
				if (priceSet == null) {
					
					//update map
					priceSet = new JdbcConnectionFactory();
					put(esl, priceSet);
					
					//update db
					if (st != null) {
						st.executeUpdate(new StringBuffer()
							.append("insert into pricesList (esl, processed) values ('")
							.append(esl.toString())
							.append("', 1); ").toString());
					}
						
				}
				
				if(active) {
					
					//update map
					priceSet.setActivePrice(price);
					
					//update db
					if (connection != null) {
						pst2 = connection.prepareStatement(new StringBuffer()
								.append("update pricesList ")
								.append("set activeUpdateId = ").append(price.getReceivedAt()).append(", ")
								.append("activeRefId = ").append(price.getRefId()).append(", ")
								.append("activePublishingDate = ?, ")
								.append("activeHash = ").append(price.getHashWhenApplied()).append(", ")
								.append("activePrice = '").append(Base64Encoder.encode(price.getData())).append("' ")
								.append("where esl = '").append(esl.toString()).append("' and processed = 1; ")
								.toString());				
						pst2.setTimestamp(1, new Timestamp(price.getActivationTime().getTime()));
						pst2.executeUpdate();
					}
					
				} 
				else 
				{

					//update map
					priceSet.setPendingPrice(price);

					//update db
					if (connection != null) {
						pst3 = connection.prepareStatement(new StringBuffer()
								.append("update pricesList ")
								.append("set pendingUpdateId = ").append(price.getReceivedAt()).append(", ")
								.append("pendingRefId = ").append(price.getRefId()).append(", ")
								.append("pendingPublishingDate = ?, ")
								.append("pendingHash = ").append(price.getHashWhenApplied()).append(", ")
								.append("pendingPrice = '").append(Base64Encoder.encode(price.getData())).append("' ")
								.append("where esl = '").append(esl.toString()).append("' and processed = 1; ")
								.toString());
						pst3.setTimestamp(1, new Timestamp(price.getActivationTime().getTime()));
						pst3.executeUpdate();
					}
					
				}	
				
				if (connection == null || st == null) 
					throw new SQLException ("failure establishing jdbc data access, cannot write to database!");
				
			}
			catch (Exception e) 
			{
				log.error("error updating " 
						+ (active ? "active": "pending") + " price for esl {} in pricesList - reason: {}", 
						esl, e);
			}
			finally 
			{
				try { if (st != null) st.close(); } catch (SQLException ignored) {} 
				try { if (pst1 != null) pst1.close(); } catch (SQLException ignored) {} 
				try { if (pst2 != null) pst2.close(); } catch (SQLException ignored) {} 
				try { if (pst3 != null) pst3.close(); } catch (SQLException ignored) {} 
				try { if (connection != null) connection.close(); } catch (SQLException ignored) {} 
			}
		}
	}

	public JdbcConnectionFactory getPricesForEsl(EUI64Address mac) {
			
		//
		// Alcuni check preliminari prima di salvare i dati
		//
		long now = System.currentTimeMillis();
		
		if (get(mac) != null && get(mac).getPendingPrice() != null)
		{
			long atime = get(mac).getPendingPrice().getActivationTime().getTime();
			atime += TimeZone.getDefault().getOffset(new Date().getTime());
					
			//
			// il pending e' diventato active: lo sostituisco all'active e lo rimuove dal pending
			//
			if (atime < now) {
				
				
				if (log.isTraceEnabled()) 
					log.trace ("Pending price registered for esl {} is now active, moving it to active price ...", mac);
	
				//
				//update map
				//
				EslPriceData p = get(mac).getPendingPrice();
				get(mac).setActivePrice(p);			
				get(mac).setPendingPrice(null);
				
				//update db			
				Connection connection = null;
				Statement st = null;
				PreparedStatement pst = null;
				
				try {
					
					connection = ConnectionFactory.getInstance().createConnection();
	
					if (connection != null)
					{
						connection.setAutoCommit(true);
						st = connection.createStatement();
					}
					
					// il vecchio pending diventa active
					
					if (connection != null) {
						pst = connection.prepareStatement(new StringBuffer()
								.append("update pricesList ")
								.append("set activeUpdateId = ").append(p.getReceivedAt()).append(", ")
								.append("activeRefId = ").append(p.getRefId()).append(", ")
								.append("activePublishingDate = ?, ")
								.append("activeHash = ").append(p.getHashWhenApplied()).append(", ")
								.append("activePrice = '").append(Base64Encoder.encode(p.getData())).append("' ")
								.append("where esl = '").append(mac.toString()).append("' and processed = 1; ")
								.toString());				
						pst.setTimestamp(1, new Timestamp(p.getActivationTime().getTime()));
						pst.execute();		
					}
					
					//il vecchio pending non è valido viene rimosso
					
					if (st != null) 
					{
						st.execute (new StringBuffer("update pricesList ")
							.append("set pendingUpdateId = NULL, ")
							.append("pendingRefId = NULL, pendingPublishingDate = NULL, ")
							.append("pendingHash = NULL, pendingPrice = NULL ")
							.append("where esl = '").append(mac.toString())
							.append("' and processed = 1")
							.toString());	
						st.close();
					}
									
					if (connection == null || st == null) 
						throw new SQLException ("failure establishing jdbc data access, cannot write to database!");
					
				}
				catch (Throwable t) {
					
					log.error("error updating active price with " +
							"outdated pending price update for esl {} in pricesList - reason: {}", mac, t);
					
				}
				finally {
	
					try { if (st != null && !st.isClosed()) st.close(); } catch (SQLException ignored) {} 
					try { if (pst != null && !pst.isClosed()) pst.close(); } catch (SQLException ignored) {} 
					try { if (connection != null) connection.close(); } catch (SQLException ignored) {} 
					
				}
				
				
			}
			
		}
						
		return get(mac);
	}
	
	public Map<EUI64Address, JdbcConnectionFactory> getPriceList() {
		return getAll();
	}
	
	public long getLatestPriceId() {
		return latestPriceId;
	}
	
	
	public void load() throws Exception {
		
		int count = 0;
		
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			
			clear();

			connection = ConnectionFactory.getInstance().createConnection();
			
			if (connection == null) 
				throw new SQLException("cannot create jdbc connection");
			
			connection.setAutoCommit(true);
			
			st = connection.createStatement();		
			
			rs = st.executeQuery("select * from pricesList where processed = 1");
			
			while (rs.next()) {
				
				EUI64Address key = new EUI64Address(Tools.hexStringToByteArray (rs.getString("esl")));
								
				EslPriceData activePrice = null;
				EslPriceData pendingPrice = null;
				
				// active
				activePrice = new EslPriceData();
				activePrice.setUpdateId(rs.getLong("activeUpdateId"));
				activePrice.setRefId(rs.getString("activeRefId"));
				activePrice.setActivationTime(rs.getDate("activePublishingDate"));
				activePrice.setHashWhenApplied(rs.getLong("activeHash"));
				if (rs.getString("activePrice") != null && rs.getString("activePrice").length() > 0) 
					activePrice.setData(Base64Decoder.decode(rs.getString("activePrice")));
			
				// pending
				pendingPrice = new EslPriceData();
				pendingPrice.setUpdateId(rs.getLong("pendingUpdateId"));
				pendingPrice.setRefId(rs.getString("pendingRefId"));
				pendingPrice.setActivationTime(rs.getDate("pendingPublishingDate"));
				pendingPrice.setHashWhenApplied(rs.getLong("pendingHash"));
				if (rs.getString("pendingPrice") != null && rs.getString("pendingPrice").length() > 0) 
					pendingPrice.setData(Base64Decoder.decode(rs.getString("pendingPrice")));
							
				JdbcConnectionFactory value = new JdbcConnectionFactory(activePrice, pendingPrice);
				
				put(key, value);
				count ++;
				
			}
			
		}
		catch (Throwable t) {
			log.error("error loading datastore records from db - {}", t);
			
		}
		finally {
			try { if (rs != null) rs.close(); } catch (Throwable t) {}
			try { if (st != null) st.close(); } catch (Throwable t) {}
			try { if (connection != null) connection.close(); } catch (Throwable t) {}
			rs = null; 
			st = null;
		}
		
		if (count > 0) log.info("Successfully loaded prices for " + count + " ESLs from database.");
		else log.info("No prices registered for ESLs in database.");
		
	}
	
	
	@Override
	public List<EUI64Address> getEnqueuedEslMessagesList() {
		
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		
		List<EUI64Address> list = new ArrayList<EUI64Address>(); 
		
		try {
			
			connection = ConnectionFactory.getInstance().createConnection();
			
			if (connection == null) 
				throw new SQLException("cannot create jdbc connection");
			
			connection.setAutoCommit(true);
			
			st = connection.createStatement();			
			
			rs = st.executeQuery("select esl from pricesList where pendingUpdateId = -1");	
			
			while (rs.next()) {
				try {
					list.add(new EUI64Address(rs.getString(1)));
				}
				catch (Throwable t) {
					if (log.isTraceEnabled())
						log.trace("wrong mac adddress in pricesList table: '" + rs.getString(1) + "'");
				}
			}
			
		}
		catch (Throwable t) {
			
			log.error("error loading unfinished price update records from db: {} - {}", t.getMessage(), t);
			
		}
		finally {
			try { if (rs != null) rs.close(); } catch (Throwable t) {}
			try { if (st != null) st.close(); } catch (Throwable t) {}
			try { if (connection != null) connection.close(); } catch (Throwable t) {}
			rs = null; 
			st = null;
		}
		
		return list;
		
	}
	
	@Override
	public void setPriceUpdateEnqueued(EUI64Address esl) {
		Connection connection = null;
		Statement st = null;
		if (esl == null) return;
		String sql = "update pricesList set pendingUpdateId = -1 where esl = '" + esl.toString() + "' and processed = 1";
		try {
			connection = ConnectionFactory.getInstance().createConnection();
			if (connection == null) throw new SQLException("cannot create jdbc connection");
			connection.setAutoCommit(true);
			st = connection.createStatement();			
			st.executeUpdate(sql);
		}
		catch (Throwable t) {
			log.error("error setting price update outgoing for esl {} - SQL > " + sql + "\n{}", esl, t);
		}
		finally {
			try { if (st != null) st.close(); } catch (Throwable t) {}
			try { if (connection != null) connection.close(); } catch (Throwable t) {}
			st = null;
		}
	}
		
	
	@Override
	public void setEslMessageSent(EUI64Address esl) {
		Connection connection = null;
		Statement st = null;
		long ct = System.currentTimeMillis();
		if (esl == null) return;
		String sql = "update pricesList set pendingUpdateId = " + ct + " where esl = '" + esl.toString() + "' and processed = 1";
		try {
			connection = ConnectionFactory.getInstance().createConnection();
			if (connection == null) throw new SQLException("cannot create jdbc connection");
			connection.setAutoCommit(true);
			st = connection.createStatement();			
			st.executeUpdate(sql);
		}
		catch (Throwable t) {
			log.error("error setting null acknowledgeDate of esl {} - SQL > " + sql + "\n{}", esl, t);
		}
		finally {
			try { if (st != null) st.close(); } catch (Throwable t) {}
			try { if (connection != null) connection.close(); } catch (Throwable t) {}
			st = null;
		}
	}
	
	@Override
	public void shutdown() {
		if (log.isTraceEnabled()) log.trace("shutting down jdbc esl data store");
	}


}
