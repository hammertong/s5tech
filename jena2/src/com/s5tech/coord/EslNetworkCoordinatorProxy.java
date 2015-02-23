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
 
package com.s5tech.coord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslInstallationKey;
import com.s5tech.net.entity.GenericEntityManager;
import com.s5tech.net.entity.IEslEntityManager;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.util.ConnectionFactory;
import com.s5tech.net.util.Tools;

@SuppressWarnings("serial")
public class EslNetworkCoordinatorProxy extends
		GenericEntityManager<EUI64Address, EslDeviceInfo> implements
		IEslEntityManager 
{
	//protected Map<NetworkAddress, EUI64Address> nwkMap;	
	
	public static Object datasyncMutex = "";
	
	protected HashMap<Integer, String> eslTypes;
	
	private Vector<Integer> dotMatrixEslTypes = null; 
	
	public EslNetworkCoordinatorProxy() throws SQLException {		
		
		eslTypes = new HashMap<>();
		
		synchronized (datasyncMutex) 
		{
			Connection connection = null;
			Statement st = null;
			ResultSet rs = null;
			
			dotMatrixEslTypes = new Vector<>();
			
			try 
			{
				connection = ConnectionFactory.getInstance().createConnection();
				if (connection == null) throw new SQLException("unable to create jdbc connection");
				connection.setAutoCommit(true);
				st = connection.createStatement();
				
				//String sql = "select networkEquivalentType, eslType from eslTypes";				
				String sql = "select e.networkEquivalentType, e.eslType, d.isDotMatrix "; 
				sql += "from eslTypes e ";
				sql += "left join displayTypes d on d.displayType = e.displayType"; 

				rs = st.executeQuery(sql);					
				
				while (rs.next()) {				
					eslTypes.put(rs.getInt(1), rs.getString(2));
					int n = rs.getInt(3);
					if (n == 1) {
						dotMatrixEslTypes.add(rs.getInt(1));	
					}
				}				
					
			}
			catch (Throwable t) {
				log.error("error loading esl types - {}", t);
			}
			finally {
				try { if (rs != null) rs.close(); } catch (Throwable t) {}
				try { if (st != null) st.close(); } catch (Throwable t) {}
				try { if (connection != null) connection.close(); } catch (Throwable t) {}
				rs = null; 
				st = null;
				connection = null;
			}
			if (log.isTraceEnabled())
				log.trace("loaded {} esl types, dotmatrix: {}/" + eslTypes.size(), 
						eslTypes.size(), (dotMatrixEslTypes.size() > 0 ? dotMatrixEslTypes.size() : "...none!"));
		}
		
		load();		
	}
	
	public Map<EUI64Address, EslDeviceInfo> getEsls() {
		return getAll();
	}
	
	public void load() {
		
		synchronized (datasyncMutex) {
		
			Connection connection = null;
			Statement st = null;
			ResultSet rs = null;
			
			clear();
			//nwkMap.clear();
			int count = 0;
			
			try {
				
				connection = ConnectionFactory.getInstance().createConnection();
				if (connection == null) throw new SQLException("unable to create jdbc connection");
				connection.setAutoCommit(true);				

				st = connection.createStatement();		
				rs = st.executeQuery(
						"select e.esl, e.eslShortAddress, e.coordinatorMac, " +
						"e.alarmMode, t.networkEquivalentType, e.installationKey " +
						"from esls e, eslTypes t where e.eslType = t.eslType");
					
				while (rs.next()) {
				
					try {
						
						NetworkAddress nwk = null;
						EUI64Address key = new EUI64Address(
								Tools.hexStringToByteArray (rs.getString("esl")));
						
						EslDeviceInfo value = new EslDeviceInfo(key);						
												
						if (rs.getString("eslShortAddress") != null) {
							int n = 0;
							try {
								n = Integer.parseInt(rs.getString("eslShortAddress"));
								nwk = new NetworkAddress(n);
								//nwkMap.put(nwk, value.getMac());					
								value.setNetworkAddress(nwk);
							}
							catch (Throwable t) {
								log.error("error getting short address of esl {} from db: {}", key, t.getMessage());
							}
						}
						
						if (rs.getString("coordinatorMac") != null
								&& rs.getString("coordinatorMac").trim().length() != 0
								&& !rs.getString("coordinatorMac").trim().toLowerCase().equals("n/a")) {
							try {
								EUI64Address caddr = new EUI64Address(rs.getString("coordinatorMac"));
								value.setCoordinatorMac(caddr);
							}
							catch (Throwable t){}								
						}
						
						value.setType(rs.getInt("networkEquivalentType"));
						
						if (rs.getString("alarmMode") != null) {
							boolean b = false;
							try {					
								b = Boolean.parseBoolean(rs.getString("alarmMode"));
								value.setAlarmEnabled(b);
							}
							catch (Throwable t) {}					
						}
						
						if (rs.getString("installationKey") != null) {
							try {
								value.setInstallationKey(
									new EslInstallationKey(
											rs.getString("installationKey").getBytes()));
							}
							catch (Throwable t) {}
						}
		
						count ++;
						put(key, value);
					}
					catch (Throwable t) {
						
						log.error("error getting esl from db: {}", t.getMessage());
						log.error("{}", t);
						
					}
						
				}
							
			}
			catch (Throwable t) {
				
				log.error(t.getMessage());
				
			}
			finally {
				try { if (rs != null) rs.close(); } catch (Throwable t) {}
				try { if (st != null) st.close(); } catch (Throwable t) {}
				try { if (connection != null) connection.close(); } catch (Throwable t) {}
				rs = null; 
				st = null;
				connection = null;
			}
			
			log.info("loaded " + (count > 0 ? count : "NO") + " esls from database");
			
		}
		
	}
		
	public EslDeviceInfo getEslInfoByMac(EUI64Address eslMac) {
		return get(eslMac);		
	}
	
	@Override
	public boolean isDotMatrix(int eslType) {		
		return dotMatrixEslTypes.contains(eslType);		
	}
		
//	@Override
//	public EslDeviceInfo getEslInfoByEslShortAddress(NetworkAddress nwk, EUI64Address coordinatorMac) 
//	{
//		EUI64Address a = nwkMap.get(nwk);
//		return a != null ? get(a) : null;
//	}
	
//	@Override
//	public Set<NetworkAddress> getEslShortAddressList(EUI64Address coordinatorMac) {	
//		return nwkMap.keySet();				
//	}
		
//	@Override
//	public void onJoin(EUI64Address address, NetworkAddress shortAddress,
//			EUI64Address coordinatorMac, boolean isRejoin) {		
//		
//		if (get(address) == null) return;
//		
//		synchronized (datasyncMutex) 
//		{
//			get(address).setCoordinatorMac(coordinatorMac);				
//		}		
//	}
		
//	@Override
//	public void removeAllEsls() {	
//		
//		try {
//			synchronized (datasyncMutex) {
//				if (log.isInfoEnabled()) log.info("Removing ALL Esls from database!");
//				clear();
//				nwkMap.clear();
//			}
//		}
//		catch (Throwable t) {		
//			log.error("Remove All Esls error {}", t);
//		}
//		
//	}	
	
	@Override
	public void removeEsls(Collection<EUI64Address> eslMac) {				
		
		if (eslMac == null || eslMac.size() == 0) return;
		
		int removed = 0;
		
		try {
		
			synchronized (datasyncMutex)  {
				for (EUI64Address addr : eslMac) {				
					if (get(addr) != null) {
						//nwkMap.remove(get(addr).getNetworkAddress());
						remove(addr);
						removed ++;						
					}
				}						
			}
			
		}
		catch (Throwable t) {
			
			log.error("Remove Esl List error {}", t);	
			
		}
		
		if (log.isInfoEnabled()) log.info((removed > 0 ? "Removed " + removed + " Esls.": "No esls removed"));
		
	}
	
	@Override
	public int lookupNetworkEquivalentType(String eslType)
	{
		try {			
			for (Integer n : eslTypes.keySet())
			{
				String e = eslTypes.get(n);
				if (e.equalsIgnoreCase(eslType)) return n;
			}
			return Integer.parseInt(eslType);			
		}
		catch (Throwable t) {
			log.error("cannot lookup network equivalent esltype > {} - reason: {}", eslType, t);
			return 0;
		}
		
	}
	
	@Override
	public void addEsls(Collection<EslDeviceInfo> esl) {
		
		if (esl == null || esl.size() == 0) return;
		
		int added = 0;
		int updated = 0;
		int records_with_errors = 0;
		
		try {
		
			synchronized (datasyncMutex) {
				
				for (EslDeviceInfo e : esl) {				
					
					if (get(e.getMac()) == null) 
					{
						try 
						{
							if (e.getNetworkAddress() == null) 
								throw new Exception("Missing Esl short address");
							if (e.getNetworkAddress().intValue() < NetworkAddress.FIRST_NWK_ADDRESS || e.getNetworkAddress().intValue() > NetworkAddress.LAST_NWK_ADDRESS)
								throw new Exception("Esl short address provided out of range");
							if (e.getNetworkAddress().intValue() == NetworkAddress.RESERVED1_ADDRESS)
								throw new Exception("Esl short address provided is reserved, cannot be assigned to Esl this address " + e.getNetworkAddress().toString());
							//if (e.getType() == 0) 
							//	throw new Exception("Missing Esl type");							
							//if (nwkMap.containsKey(e.getNetworkAddress()))
							//	throw new Exception("Esl " + e.getMac() + " with short address previously assigned to different Esl " + nwkMap.get(e.getNetworkAddress()).toString());	
							//nwkMap.put(e.getNetworkAddress(), e.getMac());
							
							put(e.getMac(), e);				
							
							added ++;
							
						}
						catch (Throwable ex)
						{
							records_with_errors ++;
							log.error("Wrong Add Esl list record for Esl {}: {}", e.getMac(), ex.getMessage());
							if (log.isTraceEnabled()) log.trace ("{}", ex);
						}					
						
					}
					else 
					{
						if (log.isTraceEnabled())
							log.trace("Esl {} present in database, updating device info data ...", e.getMac());
						
						get(e.getMac()).setType(e.getType());
						get(e.getMac()).setAlarmEnabled(e.isAlarmEnabled());
						get(e.getMac()).setInstallationKey(e.getInstallationKey());
						if (get(e.getMac()).getNetworkAddress().compareTo(e.getNetworkAddress()) != 0)
						{
							log.error ("Wrong record for Esl {}: short address provided is different from that previously assigned to this esl, " +
									   "new short address ignored. To change the short address first remove the esl and then re-add the esl)", 
									   e.getMac().toString());
						}
						
						updated ++;
						
					}
					
				}	
							
			}
			
		}
		catch (Throwable t) 
		{
			log.error("Add Esl List error {}", t);			
		}
		
		if (log.isInfoEnabled())
			log.info ((added > 0 ? "Added " + added + " esls. " : "No esls added. ") 
					+ (updated > 0 ? "Updated " + updated + " esls. ": "")
					+ (records_with_errors > 0 ? "" + records_with_errors + " records with error. " : "")
					);
				
	}
	
	@Override
	public void shutdown() {
		
		if (log.isTraceEnabled()) 
			log.trace("shutting down jdbc esl entity manager ...");
		
	}

	@Override
	public boolean isAllowed(EUI64Address eslmac) {
		return getAll().containsKey(eslmac); 
	}

	@Override
	public void update(EslDeviceInfo device) {
		//qui nono faccio niente ..., sincrinizzo alla fine
	}
	
	@Override
	public void updateAssociation(EUI64Address coordinator, Collection<EUI64Address> esllist) {
		
		synchronized (datasyncMutex) {
				
			int chunk_size = 500;
					
			Connection connection = null;
			Statement update = null;			
			int count = 0;
			
			try {				
				
				if ((connection = ConnectionFactory.getInstance().createConnection()) == null) 
					throw new SQLException("unable to create jdbc connection");
				
				connection.setAutoCommit(false);				
				
				StringBuffer sql = new StringBuffer("UPDATE esls SET coordinatorMac = '" + coordinator.toString() + "' WHERE esl in (");
				update = connection.createStatement();
				
				for (EUI64Address esl : esllist) {
					
					if (count > 0 && count % chunk_size == 0) 
					{
						sql.append(')');
						//if (log.isTraceEnabled()) 
						//	log.trace("saving {} esls associations to {} ... - SQL > " + sql.toString(), chunk_size, coordinator.toString());
						update.executeUpdate(sql.toString());
						sql = new StringBuffer("UPDATE esls SET coordinatorMac = '" + coordinator.toString() + "' WHERE esl in (");
					}
					
					if (count % chunk_size > 0) sql.append(','); 
					sql.append('\'').append(esl.toString()).append('\'');
					count ++;
					
				}
				
				if (count > 0) {
					sql.append(')');
					//if (log.isTraceEnabled()) 
					//	log.trace("saving {} esls associations to {} ... - SQL > " + sql.toString(), chunk_size, coordinator.toString());
					update.executeUpdate(sql.toString());
				}
								
				if (log.isTraceEnabled()) 
					log.trace("commit esls to coordinator association transaction ...");
				
				connection.commit();
				
				log.info ("synchronized {} esls associated to coordinator {}", count, coordinator);			
				
			}
			catch (Throwable t) {
				log.error ("synchronizing esls to coordinators associations - {}", t);
				log.trace ("rolling back transaction ...");
				if (connection != null) try { connection.rollback(); } catch (Throwable rt) { log.error("rolling back -  {}", rt);}
			}
			finally {
				if (update != null) try { update.close(); } catch (Throwable ignored) {}
				if (connection != null) try { connection.close(); } catch (Throwable ignored) {}
			}
							
		}
		
	}
	
}
