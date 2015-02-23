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
 
package com.s5tech.net.services.simulator.data;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EslDataBase {
	
	static final Logger _log = Logger.getLogger("DATABASE");
			
	public List<EslInfo> loadEsls(String macAddress)
	{	
		ArrayList<EslInfo> list = new ArrayList<>();
		if (macAddress == null || macAddress.equals("*")) return list; 
		
		Connection c = null;
		Statement s = null;
		ResultSet r = null;		
		
		try {		
			
			String driver = System.getProperty("jdbc.driver", "");
			String url = System.getProperty("jdbc.url", "");
			String username = System.getProperty("jdbc.username", "");
			String password = System.getProperty("jdbc.password", "");	
			
			if (System.getProperty("jdbcprops") == null && driver.length() == 0) 
				System.setProperty("jdbcprops", "jdbc.properties");  

			if (System.getProperty("jdbcprops") != null)
			{
				Properties p = new Properties();
				p.load(new FileInputStream(System.getProperty("jdbcprops", "./jdbc.properties")));
				driver = p.getProperty("jdbc.driver");
				url = p.getProperty("jdbc.url");
				username = p.getProperty("jdbc.username");
				password = p.getProperty("jdbc.password");				
			}
			
			
			Class.forName(driver);
			c = DriverManager.getConnection (url.trim(), username.trim(), password.trim());
			//conn.setAutoCommit (false);				
			s = c.createStatement();
			
			String sql = "SELECT e.esl, e.eslShortAddress, e.eslType, e.coordinatorMac, p.activeHash, p.pendingHash " +
					"FROM esls e " +
					"LEFT OUTER JOIN pricesList p " +					
					"ON e.esl = p.esl " +
					(macAddress == null || macAddress.equals("*") ? "" : "WHERE coordinatorMac = '" + macAddress + "' ") +
					"ORDER BY 1 ASC";
			
			r = s.executeQuery(sql);
			String old_mac = null;
			while (r.next()) {
				String mac = r.getString(1).trim();
				if (old_mac != null && old_mac.equals(mac)) continue;
				old_mac = mac;
				EslInfo info = new EslInfo();
				info.mac_id = mac;
				info.mac_address = new byte[8];
				info.coordinator = r.getString(4);
				for (int i = 0; i < 8; i ++) 
				{						
					info.mac_address[i] = (byte) Integer.parseInt(info.mac_id.substring(i * 2, i * 2 + 2).toLowerCase(), 16);
				}
				info.short_address = r.getInt(2);
				info.esltype = (r.getString(3).equalsIgnoreCase("ESL70") ? (byte)0x06 : (byte)0x05);
				info.activeHash = (r.getLong(5));
				info.pendingHash = (r.getLong(6));	
				list.add(info);
			}				
			_log.info("loaded " + list.size() + " esls associated with coordinator " + macAddress + " from database");				
		}
		catch (Throwable t) {
			_log.log(Level.SEVERE, "loading ESLs", t);
		}
		finally {
			if (r != null) try { r.close(); } catch (Throwable ignored) {}
			if (s != null) try { s.close(); } catch (Throwable ignored) {}
			if (c != null) try { c.close(); } catch (Throwable ignored) {}
		}			
		return list;
	}
	
	public List<String> loadEslsFromQuery(String sql)
	{	
		ArrayList<String> list = new ArrayList<>();
		if (sql == null) return list; 
		
		Connection c = null;
		Statement s = null;
		ResultSet r = null;		
		
		try {		
			
			String driver = System.getProperty("jdbc.driver", "");
			String url = System.getProperty("jdbc.url", "");
			String username = System.getProperty("jdbc.username", "");
			String password = System.getProperty("jdbc.password", "");	
			
			if (System.getProperty("jdbcprops") == null && driver.length() == 0) 
				System.setProperty("jdbcprops", "jdbc.properties");  

			if (System.getProperty("jdbcprops") != null)
			{
				Properties p = new Properties();
				p.load(new FileInputStream(System.getProperty("jdbcprops", "./jdbc.properties")));
				driver = p.getProperty("jdbc.driver");
				url = p.getProperty("jdbc.url");
				username = p.getProperty("jdbc.username");
				password = p.getProperty("jdbc.password");				
			}
			
			
			Class.forName(driver);
			c = DriverManager.getConnection (url.trim(), username.trim(), password.trim());
			//conn.setAutoCommit (false);				
			s = c.createStatement();
			
			r = s.executeQuery(sql);
			while (r.next()) {
				list.add(r.getString("esl").trim());				
			}				
			_log.info("loaded " + list.size() + " esls from sql query > " + sql);				
		}
		catch (Throwable t) {
			_log.log(Level.SEVERE, "loading ESLs", t);
		}
		finally {
			if (r != null) try { r.close(); } catch (Throwable ignored) {}
			if (s != null) try { s.close(); } catch (Throwable ignored) {}
			if (c != null) try { c.close(); } catch (Throwable ignored) {}
		}			
		return list;
	}
	
	public EslInfo getEslInfo(String eslmac)
	{
		
		Connection c = null;
		Statement s = null;
		ResultSet r = null;		
		
		try {		
			
			String driver = System.getProperty("jdbc.driver", "");
			String url = System.getProperty("jdbc.url", "");
			String username = System.getProperty("jdbc.username", "");
			String password = System.getProperty("jdbc.password", "");	
			
			if (System.getProperty("jdbcprops") == null && driver.length() == 0) 
				System.setProperty("jdbcprops", "jdbc.properties");  

			if (System.getProperty("jdbcprops") != null)
			{
				Properties p = new Properties();
				p.load(new FileInputStream(System.getProperty("jdbcprops", "./jdbc.properties")));
				driver = p.getProperty("jdbc.driver");
				url = p.getProperty("jdbc.url");
				username = p.getProperty("jdbc.username");
				password = p.getProperty("jdbc.password");				
			}
			
			
			Class.forName(driver);
			c = DriverManager.getConnection (url.trim(), username.trim(), password.trim());
			//conn.setAutoCommit (false);				
			s = c.createStatement();
			
			String sql = "SELECT e.esl, e.eslShortAddress, e.eslType, e.coordinatorMac, p.activeHash, p.pendingHash " +
					"FROM esls e " +
					"LEFT OUTER JOIN pricesList p " +					
					"ON e.esl = p.esl " +
					"WHERE e.esl = '" + eslmac + "' ";
					
			r = s.executeQuery(sql);
			String old_mac = null;
			while (r.next()) {
				String mac = r.getString(1).trim();
				if (old_mac != null && old_mac.equals(mac)) continue;
				old_mac = mac;
				EslInfo info = new EslInfo();
				info.mac_id = mac;
				info.mac_address = new byte[8];
				info.coordinator = r.getString(4);
				for (int i = 0; i < 8; i ++) 
				{						
					info.mac_address[i] = (byte) Integer.parseInt(info.mac_id.substring(i * 2, i * 2 + 2).toLowerCase(), 16);
				}
				info.short_address = r.getInt(2);
				info.esltype = (r.getString(3).equalsIgnoreCase("ESL70") ? (byte)0x06 : (byte)0x05);
				info.activeHash = (r.getLong(5));
				info.pendingHash = (r.getLong(6));	
				return info;
			}			
			
			return null;
			
		}
		catch (Throwable t) {
			_log.log(Level.SEVERE, "loading ESL info from mac " + eslmac, t);
			return null;
		}
		finally {
			if (r != null) try { r.close(); } catch (Throwable ignored) {}
			if (s != null) try { s.close(); } catch (Throwable ignored) {}
			if (c != null) try { c.close(); } catch (Throwable ignored) {}
		}			
	}
	
	
	/*public void merge(String [] coords) {		
		//
		// TODO ... 
		//
	}*/

}
