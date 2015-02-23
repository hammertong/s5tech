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
 
package com.s5tech.net.util;

import javax.naming.InitialContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * A ConnectionFactory object encapsulates a set of  connection configuration parameters 
  * that has been defined by s5.conf. A client uses it to create a connection with a JDBC 
  * provider. ConnectionFactory is a <b>Singleton</b>.
  * <ol>
  * 	<li>Added support: Generic JNDI DataSource</li>
  * 	<li>Removed Suport: Apache JDBC pooling</li>
  * </ol>
  * 
  */
public class ConnectionFactory {
	
	private static ConnectionFactory instance = null;
	
	/** default configuration file, overriden by the same properties prepended by <i>jdbc.</i> in file s5.conf */
	public static final String CONFIG_FILE 	= "/connection-factory.properties";
	
	public static String SYSPREFIX = "jdbc.";
	
	public static final String DATASOURCE_KEY	= "datasource";
	public static final String DRIVER_KEY 		= "driver";
	public static final String URL_KEY 			= "url";
	public static final String USER_KEY 		= "username";
	public static final String PASS_KEY 		= "password";
	public static final String PROPS_KEY 		= "settings";
	public static final String SHARED_KEY		= "shared";

	private InitialContext ic = null;
	private DataSource ds = null;
	
	private Connection shared = null;

	//private UserTransaction ut = null;
	
	private Properties config = null;
	
	private static String initDb = "";

	private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);
	
	/**
	 * Singleton constructor
	 * @throws IOException
	 */
	private ConnectionFactory() throws IOException {		
		try {			
			
			InputStream in = null;
			config = new Properties();
			
			if (System.getProperty("connectionFactory") != null &&
					new File(System.getProperty("connectionFactory")).exists()) {
				in = new FileInputStream(System.getProperty("connectionFactory"));
			}
			else if (new File("./conf/connection-factory.properties").exists()) {
				in = new FileInputStream("./conf/connection-factory.properties");
			}
			else if (new File("./connection-factory.properties").exists()) {
				in = new FileInputStream("./connection-factory.properties");
			}
			else {
				in = this.getClass().getResourceAsStream (CONFIG_FILE);	
			}
			
			if (in != null) config.load (in);			
			
		} catch (IOException e) {
			log.error("jdbc configuration error - cannot open from classpath the file {} - {}",
					CONFIG_FILE, e);
		}
		finally {	
			//
			// overrides connection-factory.properties configuration with 
			// system properties
			//
			String [] sk = new String [] {
					SYSPREFIX + DATASOURCE_KEY,
					SYSPREFIX + DRIVER_KEY,
					SYSPREFIX + URL_KEY,
					SYSPREFIX + USER_KEY,
					SYSPREFIX + PASS_KEY,
					SYSPREFIX + PROPS_KEY,
					SYSPREFIX + SHARED_KEY
			};
			
			for (String k : sk) {
				if (System.getProperty(k, "").length() > 0)	 {
					config.setProperty(k, System.getProperty(k));
				}
				else {
					config.remove(k);
				}
			}
			
		}
	}

	/**
	 * get the factory
	 * @return
	 * @throws IOException
	 */
	public static synchronized ConnectionFactory getInstance () throws IOException {
		if (instance == null) {
			instance = new ConnectionFactory();
		} 
		return instance;
	}

	/**
	 * create a new default database connection (prefixed by <i>jdbc.</i>)
	 * if the database doesn'exists, and you are using the hsqldb embedded database driver, try to create it  
	 * with the embedded package's schema file at <i>/com/s5tech/data/dbschema-text.sql</i>, you can redefine 
	 * the schema passing it from java command line with <i>-Dhsqldb.schema</i> option, starting with file if 
	 * you want to access to fileSystem resource (e.g.: <i>-Dhsqldb.schema=./myschema.sql</i>).   
	 * @return Connection (autocommit = true)
	 * @throws SQLException
	 */
	public Connection createConnection () throws SQLException {
		return createConnection (null);
	}
		
	/**
	 * create a new database connection prefixed by <i>factoryKey + '.'</i> in connection-factory.properties<br>
	 * if the database doesn'exists, and you are using the hsqldb embedded database driver, try to create it  
	 * with the embedded package's schema file at <i>/com/s5tech/data/dbschema-text.sql</i>, you can redefine 
	 * the schema passing it from java command line with <i>-Dhsqldb.schema</i> option, starting with file if 
	 * you want to access to fileSystem resource (e.g.: <i>-Dhsqldb.schema=./myschema.sql</i>).   
	 * @return Connection (autocommit = true)
	 * @throws SQLException
	 */
	public Connection createConnection (String factoryKey) throws SQLException {		
		
		Connection conn = null;
		String prefix = factoryKey == null ? SYSPREFIX: factoryKey + ".";
		
		for (int ip = 0; ip < 2; ip ++) {
				
			String datasource = config.getProperty (prefix + DATASOURCE_KEY);		
			String jdbcDriver = config.getProperty (prefix + DRIVER_KEY);
			String jdbcUrl = config.getProperty (prefix + URL_KEY);
			String username = config.getProperty (prefix + USER_KEY);
			String password = config.getProperty (prefix + PASS_KEY);
			
			boolean isShared = (config.getProperty (prefix + SHARED_KEY) == null ? 
					false:
					Boolean.parseBoolean(config.getProperty (prefix + SHARED_KEY)));
			
			if (isShared && shared != null && !shared.isClosed()) {
				return shared;
			}
			
			Properties props = null;
			
			if (datasource != null) {
				
				try {
					
					//log.debug("try to estabilish connection via datasource: " + datasource);
					if (ds == null) {
						ic = new InitialContext();
						ds = (DataSource)ic.lookup (datasource.trim());					
					}
					conn = ds.getConnection();
					//conn.setAutoCommit(false);
					
					// conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
					// conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					
					if (isShared) shared = conn;
					
					return conn;
					
				}
				catch (Throwable t ) {
					t.printStackTrace();
					conn = null;
				}
			}
			
			if (jdbcDriver != null){
				
				try {
					
					if (config.getProperty (prefix + PROPS_KEY) != null) {
						String p = config.getProperty (prefix + PROPS_KEY).trim();
						if (p.length() > 0) {
							props = new Properties();
							String [] kv = p.split(";");
							for (int i = 0; i < kv.length; i ++) {
								String [] kp = kv [i].split(":"); 
								props.put(kp[0].trim(), kp[1].trim());
							}				
						}
					}
					
					Class.forName (jdbcDriver);	
					
					if (jdbcUrl.startsWith("jdbc:hsqldb:file:")) 
					{
						synchronized (initDb)
						{
							File dir = new File(jdbcUrl.substring(17, jdbcUrl.lastIndexOf('/')).trim());
							
							if (!dir.isDirectory()) {
								
								try {
									
									if (!dir.mkdirs()) 
										throw new Exception("cannot create database directory " 
												+  dir.getAbsolutePath());
									
									conn = DriverManager.getConnection (jdbcUrl.trim(), username.trim(), (password == null ? "" : password.trim()));
									
									StringBuffer sql = new StringBuffer();
									
									String sc = System.getProperty("hsqldb.schema", "/com/s5tech/data/dbschema-text.sql");
							
									log.info("initializing database from schema " + sc 
											+ ", output path: " + dir.getAbsolutePath());							
									
									BufferedReader in = new BufferedReader(new InputStreamReader(
											(sc.startsWith("file://") ? 
													new FileInputStream(sc.substring(7).trim()):
														getClass().getResourceAsStream(sc))));
									
									Statement st = null;
									
									try {
										st = conn.createStatement();
										for ( ;; ) {
											String l = in.readLine();
											if (l == null) {
												if (sql.length() > 0) {
													st.executeUpdate(sql.toString());
												}
												break;
											}
											if (l.trim().length() == 0) continue;
											if (l.trim().startsWith("--")) continue;
											sql.append(l.trim()).append('\n');
											if (l.trim().endsWith(";")) {
												st.executeUpdate(sql.toString());	
												sql = new StringBuffer();
											}
										}				
									}
									catch (Throwable t) {
										throw t;
									}
									finally {
										if (in != null) try { in.close(); } catch (Throwable t) {}
										if (st != null) try { st.close(); } catch (Throwable t) {}									
									}
									
									if (conn != null) return conn;
																	
								}
								catch (Throwable err) {
									log.error("error initializing database... {}", err);
									throw err;
								}
							}
						}
					}
										
					if (username == null && props == null) {
//						System.err.println("establishing database connection to " 
//								+ jdbcDriver + " - " + jdbcUrl);
						if (conn != null) try{ conn.close(); } catch (Throwable ignored) {}
						conn = DriverManager.getConnection (jdbcUrl.trim());
					}
					else if (props != null && props.size() > 0) {
//						System.err.println("establishing database connection to " 
//								+ jdbcDriver + " - " + jdbcUrl + " - " + props.toString());
						if (conn != null) try{ conn.close(); } catch (Throwable ignored) {}
						conn = DriverManager.getConnection (jdbcUrl.trim(), props);
					}
					else {
//						System.err.println("establishing database connection to " 
//								+ jdbcDriver + " - " + jdbcUrl + " - " + username + " - " + (password == null ? "" : password));
						if (conn != null) try{ conn.close(); } catch (Throwable ignored) {}
						conn = DriverManager.getConnection (jdbcUrl.trim(), username.trim(), (password == null ? "" : password.trim()));
					}
					//conn.setAutoCommit (false);
				
					if (isShared) shared = conn;
					
					return conn;
					
				}
				catch (Throwable t ) {				
					throw new SQLException ("Cannot load jdbc " + jdbcDriver
							+ " @" + jdbcUrl + ": " + t.getMessage());
				}
			}
			else {
				if (prefix.length() > 0) {
					prefix = "";
				}
				else break;
			}
		}
		
		throw new SQLException (
				"Neither Datasource or jdbc driver set! Check " + CONFIG_FILE);
	}	
	
	public void setProperty(String key, String value)
	{
		config.setProperty(key, value);
	}
	
	public void removeProperty(String key)
	{
		config.remove(key);
	}
	
	
	/**
	 * Try to obtain and close database connection
	 * @return true if test success
	 */
	public static boolean testConnection () {
		boolean test = false;
		Connection conn = null;
		try {
			conn = ConnectionFactory.getInstance().createConnection();			
			test = true;
		}
		catch (Throwable t)  {}
		finally {
			if (conn != null) try { conn.close(); } catch (Throwable ignored) {}
		}
		return test;
	}
	
	/**
	 * Execute an sql update statement with default connection parameters
	 * @param sql
	 * @return
	 */
	public int executeSqlUpdate(String sql)
	{		
		Connection c = null;
		Statement s = null;
		try {
			c = createConnection();
			s = c.createStatement();
			return s.executeUpdate(sql);			
		}
		catch (Throwable t) {
			log.error("error executing update SQL = \"{}\" - {}", sql, t);		
			return -1;
		}
		finally {
			if (s != null) try { s.close(); } catch (Throwable ignored) {}
			if (c != null) try { c.close(); } catch (Throwable ignored) {}
		}		
	}
	
	/**
	 * Execute an sql query with default connection parameters
	 * @param sql
	 * @return String []<br>the records as array of strings with fields separated by ';' 
	 */	
	public String [] executeSqlQuery(String sql)
	{
		StringBuffer buff = new StringBuffer();
		
		Connection c = null;
		Statement s = null;
		ResultSet r = null;
		
		try {
			c = createConnection();
			s = c.createStatement();
			r = s.executeQuery(sql);
			
			int n = r.getMetaData().getColumnCount();
			for (int i = 1; i <= n; i ++){
				buff.append(r.getMetaData().getColumnName(i).replace(';', ' ')).append(';');				
			}			
			buff.append('\n');
			
			while (r.next()) {
				for (int i = 1; i <= n; i ++){
					buff.append(
							r.getString(i)
								.replace(';', ' ')
								.replace('\n', ' ')
								.replace('\r', ' ')
								).append(';');				
				}
				buff.append('\n');
			}			
			
		}
		catch (Throwable t) {
			log.error("error executing query SQL = \"{}\" - {}", sql, t);		
			return null;
		}
		finally {
			if (r != null) try { r.close(); } catch (Throwable ignored) {}
			if (s != null) try { s.close(); } catch (Throwable ignored) {}
			if (c != null) try { c.close(); } catch (Throwable ignored) {}
		}
		return (buff.length() > 0 ? buff.toString().split("\n") : null);
	}
	
}
