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
 
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst space safe 
// Source File Name:   SocketSerialPortProxy.java

package com.s5tech.net.serial;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;

// Referenced classes of package com.s5tech.net.desktop.serial:
//			ISerialPortProxy

public class SSLSocketSerialPortProxy
	implements ISerialPortProxy
{

	public final static int DEFAULT_TCP_PORT = 55550;

	final static int IPTOS_LOWCOST = 0x02;
	final static int IPTOS_RELIABILITY = 0x04;
	final static int IPTOS_THROUGHPUT = 0x08;
	final static int IPTOS_LOWDELAY = 0x10;
	
	private String proto;
	private String name;
	private Socket sock;
	private String host;
	private int port;
	private Logger log;

	public SSLSocketSerialPortProxy(String serialPortName)
	{
		init (serialPortName);
	}

	private void init(String serialPortName) 
	{
		Exception ex = null;
		
		try
		{
			
			port = DEFAULT_TCP_PORT;
			
			String [] v = serialPortName.split("[:\\/\\?&]");
			int count = 0;
			for (String s : v) {
				if (s.length() == 0) continue;
				if (count == 0) {
					proto = s;							
				}
				else if (count == 1) {
					host = s;
				}
				else if (count == 2) {
					try {
						port = Integer.parseInt(s);
					}
					catch(Throwable __T) { 
						log.error("failure parsing TCP port number - {}", serialPortName);
					}
				}
				count ++;
			}
			
			serialPortName = proto + "://" + host + ":" + port;
			
		}
		catch (Exception e)
		{
			ex = e;
		}
		if (ex != null || host == null || "".equals(((Object) (host))) || port < 0)
		{
			throw new IllegalArgumentException("valid serial port url must be specified! (e.g: tcp://localhost:54321)", ((Throwable) (ex)));
		}
		else
		{
			name = serialPortName;
			log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + "." + name);			
			return;
		}
	}

	public boolean connect()
	{
		if (sock == null) init(name);
		boolean res = false;
		try
		{
			if (sock != null && sock.isConnected())
			{
				res = true;
			}
			else
			{
				//SocketFactory socketFactory = proto.equals("ssl") ? SSLSocketFactory.getDefault(): SocketFactory.getDefault();
				//SocketFactory socketFactory = proto.equals("ssl") ? SSLContext.getInstance("SSL").getSocketFactory() : SocketFactory.getDefault();
				
				//
				// Initialize SSL Trust manager
				//				
				SocketFactory socketFactory = null;
				
				InetAddress addr = InetAddress.getByName (host);
				
				if (proto.equals("ssl")) 
				{
			        try {
			        	
			        	String keystore = System.getProperty("javax.net.ssl.keyStore", "./security/keystore");			        	
			        	String keystorePass = System.getProperty("javax.net.ssl.keyStorePassword", "changeit");
						String kstype = System.getProperty("javax.net.ssl.keyStoreType", "");
			        	
						char[] passphrase = keystorePass.toCharArray();
						KeyManagerFactory kmf = KeyManagerFactory.getInstance ("SunX509");
						
						if (kstype.length() == 0) {
							kstype = keystore.toLowerCase().endsWith(".p12") || keystore.toLowerCase().endsWith(".pkcs12")? "pkcs12": "jks";
						}

						KeyStore ks = KeyStore.getInstance (kstype);
						ks.load (new FileInputStream(keystore), passphrase);
						kmf.init (ks, passphrase);			
						
			        	SSLContext sc;
			        	sc = SSLContext.getInstance("SSL");
						sc.init (
								kmf.getKeyManagers(),								
								new X509TrustManager[] {
									new X509TrustManager() {
										@Override
										public X509Certificate[] getAcceptedIssuers() {
											//return null;
											return new X509Certificate[0];
										}
										@Override
										public void checkServerTrusted(X509Certificate[] arg0, String arg1)
												throws CertificateException {
										}
										@Override
										public void checkClientTrusted(X509Certificate[] arg0, String arg1)
												throws CertificateException {
										}
									} 
								}
							, new java.security.SecureRandom());
						
						socketFactory = sc.getSocketFactory();
						
					} catch (Exception e) {
						log.error("Cannot initialize SSL Socket Factory - reason : {}", e);
					}
					
					sock = socketFactory.createSocket(addr, port);
					SSLSocket s = (SSLSocket) sock;
					if (s.getSession().getPeerHost() == null) 
						throw new Exception("Cannot Create SSL Socket, unable to retrieve remote peer (SSL handshake failure??)");
					
				}
				else 
				{
					socketFactory = SocketFactory.getDefault();
					sock = socketFactory.createSocket(addr, port);
				}
				
				try {
					if (System.getProperty("sock.keep.alive") != null) {
						sock.setKeepAlive(true);
					}
				}
				catch (Throwable t) {
					log.error("cannot set socket keep alive option - {}", t);
				}
				
				try {
					if (System.getProperty("sock.linger") != null) {
						sock.setSoLinger(true, Integer.parseInt(System.getProperty("sock.linger")));
					}
					else 
					{
						sock.setSoLinger(false, 0);
					}
				}
				catch (Throwable t) {
					log.error("cannot set socket linger option = {} - {}", System.getProperty("sock.linger"), t);
				}				
				
				try {
					if (System.getProperty("sock.performance.preferences") != null) {
						//Note: only since java >= 5
						int [] pp = new int[] { 2, 2, 2 };
						String []x = System.getProperty("sock.performance.preferences").split("[ ,]"); 
						for (int i = 0; i < x.length; i ++) {
							if (x[i].toLowerCase().indexOf("connect") >= 0) {
								pp[0] = i;
							}
							else if (x[i].toLowerCase().indexOf("latency") >= 0) {
								pp[1] = i;
							}
							else if (x[i].toLowerCase().indexOf("bandwith") >= 0) {
								pp[2] = i;
							}
						}					
						sock.setPerformancePreferences(pp[0], pp[1], pp[2]);
					}
				}
				catch (Throwable t) {
					log.error("cannot set socket performance preferences - {}", t);
				}
				
				try {
					if (System.getProperty("sock.receive.buffersize") != null) {
						sock.setReceiveBufferSize(Integer.parseInt(System.getProperty("sock.receive.buffersize")));
					}
				}
				catch (Throwable t) {
					log.error("cannot set socket receive buffer - {}", t);
				}
				
				try {
					if (System.getProperty("sock.timeout") != null) {
						sock.setSoTimeout(Integer.parseInt(System.getProperty("sock.timeout"))); //0 for infinite
					}
				}
				catch (Throwable t) {
					log.error("cannot set socket timeout - {}", t);
				}
								
				try {
					//disable Nagle's Alg (Nagle's Alg dovrebbe ottimizzare le performance quando si usano piccoli pacchetti)			
					if (System.getProperty("sock.tcp.nodelay") != null) {
						sock.setTcpNoDelay(true);
					}
				}
				catch (Throwable t) {
					log.error("cannot set socket TCP no-delay option - {}", t);
				}
				
				try {
					if (System.getProperty("sock.traffic.class") != null) {
						String []x = System.getProperty("sock.performance.preferences").split("[ ,]"); 
						int tc = 0;
						for (int i = 0; i < x.length; i ++) {
							if (x[i].toLowerCase().indexOf("lowc") >= 0) {
								tc |= IPTOS_LOWCOST;
							}
							else if (x[i].toLowerCase().indexOf("rel") >= 0) {
								tc |= IPTOS_RELIABILITY;
							}
							else if (x[i].toLowerCase().indexOf("thr") >= 0) {
								tc |= IPTOS_THROUGHPUT;
							}
							else if (x[i].toLowerCase().indexOf("lowd") >= 0) {
								tc |= IPTOS_LOWDELAY;
							}
						}	
						sock.setTrafficClass(tc);						
					}
				}
				catch (Throwable t) {
					log.error("cannot set socket traffic class - {}", t);
				}
								
				res = true;
			}
		}
		catch (Exception e)
		{
			log.error("connect exception - {}", ((Throwable) (e)));
			sock = null; //needed by ssl handshake errors
		}
		return res;
	}

	public InputStream getInputStream()
	{
		InputStream i = null;
		try
		{
			i = sock.getInputStream();
		}
		catch (Exception e)
		{
			sock = null;
			log.error("cannot get input stream - {}", ((Throwable) (e)));
		}
		return i;
	}

	public OutputStream getOutputStream()
	{
		OutputStream o = null;
		try
		{
			o = sock.getOutputStream();
		}
		catch (Exception e)
		{
			sock = null;
			log.error("cannot get output stream - {}", ((Throwable) (e)));
		}
		return o;
	}

	public String getSerialPortName()
	{
		return name;
	}

	public boolean portExists()
	{
		//
		// TODO: portExists just returns true - can this be done better?
		//
		//log.debug("portExists just returns true - can this be done better?");
		return true;
	}

	public void setSerialPortName(String serialPortName)
	{
		name = serialPortName;
	}

	public InetAddress getRemoteAddress()
	{
		return sock != null ? sock.getInetAddress() : null;
	}

	public int getRemotePort()
	{
		return sock != null ? sock.getPort() : -1;
	}

	@Override
	public void close() {
		if (sock != null) try {sock.close(); } catch (Throwable t) {}
		sock = null;		
	}
	
}

