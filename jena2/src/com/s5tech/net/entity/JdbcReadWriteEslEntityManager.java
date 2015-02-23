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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;

import com.s5tech.coord.EslNetworkCoordinatorProxy;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.util.ConnectionFactory;
import com.s5tech.net.util.Tools;

@SuppressWarnings("serial")
public class JdbcReadWriteEslEntityManager extends
		EslNetworkCoordinatorProxy
{ 
	
	private static final String DEFAULT_ADD_ESLTYPE = "ESL50";
	
	public JdbcReadWriteEslEntityManager() throws SQLException {		
		super();				
	}	
	
	private void execSQL(String sql, Object [] args)
	{
		Connection c = null;		
		PreparedStatement p = null;		
		try {
			
			if (log.isTraceEnabled()) {
				StringBuffer s = new StringBuffer();	
				for (Object o : args)
				{
					s.append(o == null ? "null" : "[" + o.toString()).append("] ");				
				}
				log.trace("execute SQL > {} -- Parameters > {}", 
						sql,
						s.length() == 0 ? "none" : s.toString());
			}
			
			c = ConnectionFactory.getInstance().createConnection();
			p = c.prepareStatement(sql);			
			if (args != null)
			{
				for (int i = 0; i < args.length; i ++)
				{
					Object o = args[i];
					
					if (o == null) 
					{
						p.setNull(1 + i, Types.VARCHAR);
					}
					else if (o instanceof String) 
					{
						p.setString(1 + i, String.class.cast(o));
					}					
					else if (o instanceof Integer) 
					{
						p.setInt(1 + i, Integer.class.cast(o));
					}
					else if (o instanceof Long) 
					{
						p.setLong(1 + i, Long.class.cast(o));
					}
					else if (o instanceof Date) 
					{
						p.setTimestamp(1 + i, new Timestamp(((Date) o).getTime()));
					}
					else  
					{
						p.setString(1 + i, o.toString());
					}					
				}
			}	
			
			p.executeUpdate();
			
		}
		catch (Throwable t) 
		{			
			log.error("Entity manager SQL error - {}", t);			
		}
		finally {
			if (p != null) try {p.close(); } catch (Throwable ignored) {}
			if (c != null) try {c.close(); } catch (Throwable ignored) {}
		}
	}
		
//	@Override
//	public void removeAllEsls() {	
//		try {
//			synchronized (datasyncMutex) {
//				if (log.isInfoEnabled()) log.info("Removing ALL Esls from database!");
//				clear();
//				nwkMap.clear();
//				execSQL ("delete from esls");				
//			}
//		}
//		catch (Throwable t) {
//			log.error("Remove All Esls error {}", t);	
//		}		
//	}	
	
	@Override
	public void removeEsls(Collection<EUI64Address> eslMac) {				
		
		int removed = 0;
		if (eslMac == null || eslMac.size() == 0) return;
				
		try {
			
			synchronized (datasyncMutex)  {
				for (EUI64Address addr : eslMac) {				
					if (get(addr) != null) {
						//nwkMap.remove(get(addr).getNetworkAddress());
						remove(addr);
						execSQL ("delete from esls where esl = ?", new Object [] {addr});						
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
	public void addEsls(Collection<EslDeviceInfo> esl) {
		if (esl == null || esl.size() == 0) return;
		int added = 0;
		int updated = 0;
		int records_with_errors = 0;
		try {
			
			synchronized (datasyncMutex) {
				
				for (EslDeviceInfo e : esl) {
					
					if (log.isTraceEnabled()) log.trace("Adding device {} > {}", e.getMac(), Tools.toStringObj(e));
					
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
//							if (e.getType() == null) 
//								throw new Exception("Missing Esl type");
//							if (e.getType().trim().length() == 0) 
//								throw new Exception("Missing Esl type");
							
//							if (nwkMap.containsKey(e.getNetworkAddress()))
//								throw new Exception("Esl " + e.getMac() + " with short address previously assigned to different Esl " + nwkMap.get(e.getNetworkAddress()).toString());	
//							nwkMap.put(e.getNetworkAddress(), e.getMac());
							
							put(e.getMac(), e);	
							
							//
							// update database
							//
							
							
							String etype = eslTypes.get(e.getType());																				
							execSQL ("insert into esls (esl, eslShortAddress, eslType, installationKey, alarmMode) values (?, ?, ?, ?, ?)", 
									new Object[] 
										{
											e.getMac().toString(),
											e.getNetworkAddress().intValue(),
											(etype == null ? DEFAULT_ADD_ESLTYPE : etype),
											e.getInstallationKey(),
											(e.isAlarmEnabled() ? 1 : 0)
										});

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
						if (log.isTraceEnabled()) {
							log.trace ("esl {} already registered, updating device info data ...", e.getMac());
						}
						get(e.getMac()).setType(e.getType());
						get(e.getMac()).setAlarmEnabled(e.isAlarmEnabled());
						get(e.getMac()).setInstallationKey(e.getInstallationKey());
						if (e.getNetworkAddress() != null
								&& e.getNetworkAddress().intValue() != 0
								&& get(e.getMac()).getNetworkAddress().compareTo(e.getNetworkAddress()) != 0)
						{
							log.warn ("esl {} already registered with another short address, new short address will be ignored, " +
									   "remove and re-add the esl if you want change the short address!", 
									   e.getMac().toString());
						}
						
						String etype = eslTypes.get(e.getType());
						execSQL ("update esls set eslType = ?, installationKey = ?, alarmMode = ? where esl = ?", 
								new Object[] 
									{
										etype == null ? DEFAULT_ADD_ESLTYPE : etype,
										e.getInstallationKey(),
										(e.isAlarmEnabled() ? 1 : 0),
										e.getMac().toString(),
									});

						
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
    public boolean isAllowed(EUI64Address eslmac) {
        
		EslDeviceInfo esl = getAll().get(eslmac);
        return (esl != null);
        
    }

	@Override
	public void shutdown() {
		if (log.isTraceEnabled()) log.trace("shutting down jdbc read/write esl entity manager");
	}
	
	@Override
	public void update(EslDeviceInfo esl) 
	{		
		synchronized (datasyncMutex) 
		{	
			execSQL(
					"update esls set coordinatorMac = ?, eslShortAddress = ? where esl = ?", 
					new Object[] {
						esl.getCoordinatorMac(), 
						esl.getNetworkAddress().intValue(), 
						esl.getMac()});
		}		
	}
	
	@Override
	public void updateAssociation(EUI64Address coordinator, Collection<EUI64Address> esllist) {
		// nothing to do here....
	}
	
}
