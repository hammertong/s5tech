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
 
package com.s5tech.net.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;

public class FtpFileServer {

	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	private FtpServer server = null;
	
	public void start() {
					
		try {
			
			FtpServerFactory serverFactory = new FtpServerFactory();
			
			int port = Integer.parseInt(System.getProperty("ftpserver.port", "21"));
			boolean sslEnabled = Boolean.parseBoolean(System.getProperty("ftpserver.enableSSL", "false")); 
			
			if (port != 21 || sslEnabled) {
				
				ListenerFactory factory = new ListenerFactory();
				factory.setPort(port);
				
				SslConfigurationFactory ssl = new SslConfigurationFactory();
				
				ssl.setKeystoreFile (new File(System.getProperty ("javax.net.ssl.keyStore", "./security/keystore")));
				ssl.setKeystorePassword (System.getProperty ("javax.net.ssl.keyStorePassword", "changeit"));
				ssl.setKeystoreType(System.getProperty ("javax.net.ssl.keyStoreType", "jks"));
				
				// set the SSL configuration for the listener
				factory.setSslConfiguration(ssl.createSslConfiguration());
				factory.setImplicitSsl(true);
				// replace the default listener
				
				serverFactory.addListener("default", factory.createListener());								
			}
			
			File usersFile = new File(System.getProperty ("ftpserver.authfile", "security/ftpauth"));
			
			PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
			userManagerFactory.setFile(usersFile);
			
			// check FTP home directory, create it if not present ...
			Properties p = new Properties();
			p.load(new FileInputStream(usersFile));
			for (Object okey : p.keySet()) {
				if (okey.toString().endsWith(".homedirectory")) {
					File dir = new File(p.getProperty(okey.toString()));
					if (!dir.exists()) {
						if (log.isTraceEnabled()) log.trace("creating ftp-home dir " + dir.getPath() + " ...");
						dir.mkdirs(); 
					}
				}
			}
			
			serverFactory.setUserManager(userManagerFactory.createUserManager());
			
			server = serverFactory.createServer();         
			server.start();			
			
			if (log.isTraceEnabled()) log.trace("FTP service listening on port " + port + " ...");
			
		}
		catch (Throwable t) {
			log.error("starting ftp service ... - ", t);			
		}
	}
	
	public void suspend()
	{
		if (server == null) return;
		try {
			server.suspend();
		}
		catch (Throwable t) {
			log.error("suspending server  - ", t);
		}		
	}
	
	public void resume()
	{
		if (server == null) return;
		try {
			server.resume();
		}
		catch (Throwable t) {
			log.error("resuming server  - ", t);
		}		
	}
	
	public void stop ()
	{
		if (server == null) return;
		try {
			server.stop();
		}
		catch (Throwable t) {
			log.error("stopping server  - ", t);
		}		
	}
		
	/*
	public static void main(String [] args) throws NoSuchAlgorithmException
	{		
		String password = "admin:s5tech123!";
		byte[] a = java.security.MessageDigest.getInstance("MD5").digest(password.getBytes());
		for (byte b : a) {			
			String x = Integer.toHexString((b & 0xff));
			if (x.length() == 1) x = "0" + x; 
			System.err.print(x.toUpperCase());
		}
		System.err.println();		
	}
	*/
	
}
