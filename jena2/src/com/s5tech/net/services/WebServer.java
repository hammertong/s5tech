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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Properties;

import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
	
	private static final Logger log = LoggerFactory.getLogger ("WEBACCESS");
	
	private int http_port = 9090;
	private int https_port = 0;
	private StringBuffer deployBuffer = new StringBuffer();
		
	private boolean needClientAuth = false;
	
	private HandlerCollection handlerCollection;
	
	private Server server = null;
	
	public WebServer()
	{		
		handlerCollection = new HandlerCollection();
	}
	
	String firstContext = null;
	
	public synchronized void addContextHandler(AbstractHandler handler, String contextPath) {		
		ContextHandler context = new ContextHandler();
		context.setContextPath(contextPath);
		context.setResourceBase(".");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.setHandler(handler);
		handlerCollection.addHandler(context);
		if (firstContext == null) firstContext = contextPath; 
	}
		
	/**
	 * <pre>
	 * Examples: 
	 * getHead("http://localhost:8080")
	 * getHead("https://localhost:8443")
	 * </pre>
	 * @param url
	 * @return false <i>if no http or https are running at url specified</i>
	 */
	public boolean getHead(String url)
	{
		String server = null;
		
		try 
		{
			String[] pz = url.split("[\\:\\/\\?]");
			String proto = (pz.length > 0 ? pz[0] : "http");
			String host = (pz.length > 3 ? pz[3] : "127.0.0.1");
			int port = (pz.length > 4 ? Integer.parseInt(pz[4]) : 8080);
	
			Socket s = null;
			
			if (proto.equalsIgnoreCase("https")) {
				s = SSLSocketFactory.getDefault().createSocket(host, port);
			}
			else {
				s = new Socket(host, port);
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintStream out = new PrintStream (s.getOutputStream());
			
			out.println("HEAD / HTTP/1.0");
			out.println();			
			for ( ;;) {
				String t = null;
				t = in.readLine();
				if (t == null || t.length() == 0) break;
				//System.out.println("HEAD > " + t);
				if (t.startsWith("Server:")) {
					server = t.substring(7).trim();					
				}
			}			
			s.close();
			
			System.out.println("Detected " 
					+ (server != null ? proto.toUpperCase() 
					+ " server: " + server : " no server!"));
			
			return (server != null);
			
		}
		catch (Throwable t) {
			return false;			
		}		
	}
	
	public void parseArgs(String [] args)
	{
		for (int i = 0; args != null && i < args.length; i ++) {
			if (args[i].equals("--http")) {
				http_port = Integer.parseInt(args[++i]);
			}
			else if (args[i].equals("--https")) {
				https_port = Integer.parseInt(args[++i]);
			}
			else if (args[i].equals("--deploy")) {
				deployBuffer.append(args[++i]).append(' ');
			}
			else if (args[i].toLowerCase().startsWith("--needclientauth")) {
				needClientAuth = true;
			}
			else if (args[i].equals("--head")) {
				String url = args[++i];
				System.out.println("trying " + url + " ...");
				boolean result = getHead(url);
				System.exit((result ? 0 : 1));
			}			
		}		
	}
	
	public void start() 	
	{	
		try {
			
			if (server != null && server.isRunning()) return;
			
			server = new Server();			
			
			if (http_port > 0) {
				SocketConnector connector = new SocketConnector();
				connector.setMaxIdleTime(1000 * 60 * 60);
			    connector.setSoLingerTime(-1);
			    connector.setPort(http_port);
			    server.addConnector(connector);
			    log.info("added HTTP listener port " + http_port);				   
			}
			
		    if (https_port > 0) 
		    {
		    	SslSocketConnector sslconnector = new SslSocketConnector();
			    
		    	sslconnector.setKeystore (System.getProperty ("javax.net.ssl.keyStore", "./security/keystore"));
		    	sslconnector.setKeyPassword (System.getProperty ("javax.net.ssl.keyStorePassword", "changeit"));
		    	sslconnector.setKeystoreType (System.getProperty ("javax.net.ssl.keyStoreType", "jks"));				
		    	sslconnector.setTruststore (System.getProperty ("javax.net.ssl.trustStore", "./security/keystore"));
		    	sslconnector.setTrustPassword (System.getProperty ("javax.net.ssl.trustStorePassword", "changeit"));
		    	sslconnector.setTruststoreType (System.getProperty ("javax.net.ssl.trustStoreType", "jks"));
			    
			    sslconnector.setMaxIdleTime(1000 * 60 * 60);
			    sslconnector.setSoLingerTime(-1);
			    sslconnector.setPort(https_port);
			    
			    sslconnector.setNeedClientAuth(needClientAuth);
			    
			    log.info("added HTTPS secure listener port " + https_port + " " +
			    		(needClientAuth ? "(client authentication required!)": ""));
			    
			    server.addConnector(sslconnector);
		    }
		    
			//
			// deploy war if specified
			//
			String[] deployWars = deployBuffer.toString().split(" ");
			
			for (String war : deployWars) 
			{
				if (war.length() == 0) continue;
				String [] def = war.split(":");
				String ctxpath = def[0];
				if (!ctxpath.startsWith("/")) ctxpath = "/" + ctxpath;
				String warpath = def[1];
				log.info("deploying web application " + ctxpath + " from war file " + warpath);
				WebAppContext webapp = new WebAppContext();
			    webapp.setContextPath(ctxpath);				   
			    if (warpath.endsWith(".war")) {
				    webapp.setWar(warpath);		
			    }
			    else {
			    	webapp.setDescriptor(warpath + "/WEB-INF/web.xml");
			    	webapp.setResourceBase(warpath);    	
			    	webapp.setParentLoaderPriority(true);    	
			    }
			
			    Properties p = new Properties();
			    p.put("org.mortbay.jetty.servlet.Default.dirAllowed", "false");

			    webapp.setInitParams(p);
			    
			    			    			    
			    handlerCollection.addHandler(webapp);
			}

		    //add root default redirector
		    if (firstContext == null || firstContext.trim().length() == 0) { firstContext = "/admin"; }

			handlerCollection.addHandler(
		    		new AbstractHandler() {					
						@Override
						public void handle(String arg0, HttpServletRequest arg1,
								HttpServletResponse arg2, int arg3) throws IOException,
								ServletException {
							try {
								if (arg1.getRequestURI().equals("/")) {
									arg2.sendRedirect(firstContext);
									((Request)arg1).setHandled(true);											
								}
							}
							catch (Throwable eccexxignurant) {}
						}
			});

			
			server.setHandler(handlerCollection);
			server.setSendServerVersion(false);
			server.start();
			
			log.info("web server listening on port/s:" 
					+ (http_port > 0 ? " HTTP " + http_port : "") 
					+ (https_port > 0 ? " HTTPS " + https_port : "")
					+ " ...");
			
			//from other thread...: server.stop();
			
		}
		catch (Throwable t) {

			log.error("web server exit - ", t);
			
		}
		
	}
	
	public void join()
	{
		if (server == null) return;
		try {
			server.join();
		}
		catch (Throwable t) {
			log.error("joining server  - ", t);
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

}
