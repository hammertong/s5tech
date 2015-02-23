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
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;

public class RemoteControlListener implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	@Override
	public void run() {

		int port = 9000;
		//int timeout = 60000;
		ServerSocket server = null;

		final ArrayList<Socket> socks = new ArrayList<>();
				
		String proto = System.getProperty ("shutdown.proto", "tcp");
		String bindaddress = System.getProperty ("shutdown.bindaddress", "127.0.0.1");
		try { port = Integer.parseInt(System.getProperty ("shutdown.port", "9000"));} 
		catch (Throwable t) {}
		//try { timeout = Integer.parseInt(System.getProperty ("shutdown.sotimeout", "60000"));} 
		//catch (Throwable t) {}
		
		if (port < 0 || port > 65535) port = 9000;
		//if (timeout < 1000) timeout = 60000;
				
		String url = System.getProperty ("remote.services", "");
		if (url.length() > 0) {
			String [] p = url.split("[\\:\\?\\/\\&]");
			proto = p[0].trim().toLowerCase();
			if (p.length > 3) bindaddress = p[3].trim();
			if (p.length > 4) try { port = Integer.parseInt(p[4]);} catch (Throwable t) {}
			//if (p.length > 5) try { timeout = Integer.parseInt(p[5]);} catch (Throwable t) {}
		}
		
		try {
		
			//final Integer gb_timeout = new Integer(timeout);
			InetAddress binding = InetAddress.getByName(bindaddress);
			
			if (proto.equalsIgnoreCase("tcp")) {
				server = ServerSocketFactory.getDefault().createServerSocket(
						port, 
						0, 
						binding);				
			}
			else if (proto.equalsIgnoreCase("ssl")) {
				server = SSLServerSocketFactory.getDefault().createServerSocket(
						port, 
						0,
						binding);
			}
			else {
				throw new Exception("invalid protocol specified: " + proto);				
			}
			
			final RemoteControlServices services = RemoteControlServices.getInstance();
			
			if (log.isTraceEnabled())
				log.trace("remote services listener active on {} ...",
						proto + "://" + bindaddress + ":" + port);
								
			for ( ;; ) 
			{
				final Socket socket = server.accept();	
//				socket.setKeepAlive(true); 
//				socket.setSoTimeout(gb_timeout);
			
				synchronized (socks) {
					socks.add(socket);
				}				
				
				if (log.isTraceEnabled())
					log.trace("accepted connections from {} ...", 
							socket.getRemoteSocketAddress().toString());
								
				Thread worker = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						try {
							
							BufferedReader in = new BufferedReader(
									new InputStreamReader(socket.getInputStream()));
							PrintStream out = new PrintStream(socket.getOutputStream());
							out.print("ok - ");
							String version = null;
							synchronized (services) {
								version = services.getVersion();
							}
							if (version.indexOf('=') > 0) 
								version = version.substring(version.indexOf('=') + 1).trim();
							out.print(version);
							
							String eol = services.getEOL();							
							out.print(eol);
							out.flush();
							
							for ( ;; ) {
								
								try {
								
									String t = in.readLine();
									
									if (t == null) break;
									t = t.trim();
									
									if (t.equalsIgnoreCase("shutdown")) 
									{										
										out.print("200 ok");
										out.print("\r\n"); 	//keep it for .NET services compatibility (always CR/LF in MS Windows O.S. client)
										out.flush();									
										synchronized (socks) {
											for (Socket s : socks) {
												try { s.close(); } catch (Throwable ignored) {}
											}											
										}
										System.exit(0);											
									}
									else if (t.equalsIgnoreCase("whois")) 
									{
										out.print("202 ok");
										out.print(eol);
										synchronized (socks) {
											for (Socket s : socks) {
												out.print((s.isConnected() && ! s.isClosed() ? "OPEN   " : "CLOSED "));
												out.print(s.getRemoteSocketAddress().toString());
												if (s.getRemoteSocketAddress().equals(socket.getRemoteSocketAddress())) {
													out.print(" [y]");
												}
												out.print(eol);
											}											
										}
										out.print(eol);
										out.flush();	
									}
									else if (t.equalsIgnoreCase("timeout")) 
									{
										int timeout = Integer.parseInt(t.substring(t.indexOf(" ")).trim());
										socket.setSoTimeout(timeout);
										out.print("200 ok"); 	
										out.print(eol);
										out.flush();	
									}
									else if (t.startsWith("setcr")) 
									{										
										if (t.substring(5).trim().equalsIgnoreCase("on")) {
											services.setEOL("\r\n");
										}
										else {
											services.setEOL("\n");
										}
										eol = services.getEOL();
										out.print("200 ok"); 	
										out.print(eol);
										out.flush();	
									}
									else if (t.startsWith("net ")) 
									{	
										String params = null; 
										String s = null;
														
										String command = t.substring(4).replace('\t', ' ').trim();
										
										params = (command.indexOf(' ') > 0 ? 
												command.substring(command.indexOf(' ')).trim() 
												: null);
										
										synchronized (services) {								
										
											if (command.startsWith("ls")) {
												s = services.getCoordinators(params);
											}
											else if (command.startsWith("que")) {
												s = services.getCoordinatorsQueues();
											}
											else if (command.startsWith("esl")) {
												s = services.getEsls(params);
											}
											else if (command.startsWith("fir")) {
												s = services.getFirmwareInfo();
											}
											else if (command.startsWith("upg")) {
												s = services.getFirmwareUpgradeInfo();
											}
											else if (command.startsWith("hub")) {
												s = services.getHubs();
											}
											else if (command.startsWith("thr") 
													|| command.equalsIgnoreCase("tsk")) {
												s = services.getTasks();
											}
											else if (command.startsWith("par")) {
												s = services.getParameters();
											}
											else if (command.startsWith("sys")) {
												s = services.getSystemInfo();
											}
											else if (command.startsWith("ver")) {
												s = services.getVersion();
											}
											else if (command.startsWith("ret")) {
												s = services.retryPriceUpdate(params);												
											}
											else if (command.startsWith("chn")) {
												s = services.setCoordinatorChannel(params);												
											}
											else if (command.startsWith("res")) {
												s = services.setCoordinatorReset(params);												
											}
											else if (command.startsWith("tim")) {
												s = services.setCoordinatorTime(params);												
											}
											else if (command.startsWith("pri")) {
												s = services.getPrice(params);												
											}
											else {
												throw new Exception("invalid network command: " + command);
											}
										}
										
										if (s != null) {
											out.print ("202 ok");
											out.print(eol);
											out.print(s);
											if (!s.endsWith("\n")) out.print(eol);
											out.print(eol);
											out.flush ();
										}
										else {
											throw new Exception("invalid network command: no values returned");
										}										
									}
									else if (t.equalsIgnoreCase("exit")
											|| t.equalsIgnoreCase("close")
											|| t.equalsIgnoreCase("quit")) 
									{									
										break;
									}
									else {
										if (log.isTraceEnabled())
											log.trace("invalid remote service command @{} > {}", 
												socket.getRemoteSocketAddress().toString(),
												t);
										out.print("404 error - invalid command: " + t);
										out.print(eol);
										out.flush();
									}	
									
								}
								catch (Throwable ex) {
									
									if (ex instanceof SocketException) break;
									
									if (log.isErrorEnabled())
										log.error("remote service command exception @{} > {}", 
											socket.getRemoteSocketAddress().toString(),
											ex);
									
									out.print("500 error"); 	
									out.print((ex != null && ex.getMessage() != null ? " - " + ex.getMessage(): ""));
									out.print(eol);
									out.flush();
									
								}
								
							}
							
						}
						catch (Throwable t) {
							
							log.error("client exception {} ...");
							
						}
						finally {
						
							if (log.isTraceEnabled())
								log.trace("closing connections from {} ...", (
										socket != null && socket.getRemoteSocketAddress() != null ? 
												socket.getRemoteSocketAddress().toString(): "?"));
							
							if (socket != null) {
								try { socket.close(); } catch (Throwable ignored) {}
								synchronized(socks) {
									socks.remove(socket);
								}
							}
							
						}				
						
					}
				}, "worker-client@" + socket.getRemoteSocketAddress().toString());
				
				worker.setDaemon(true);
				worker.start();
				
			}
			
		}
		catch (Throwable t) {
			
			log.error("CANNOT START REMOTE LISTENER ON TCP PORT {} ... ABORTING JVM !!!", port);
			log.error("POSSIBLE ALTERNATIVE SOLUTIONS:\n" 
					+ "\tOPT. 1) CHANGE THE SHUTDOWN TCP PORT IN YOUR CONFIGURATION (e.g.: in s5.conf 'set shutdown.port = 9001')");
			log.error("\tOPT. 2) SHUTDOWN THE TASK THAT IS LISTENING ON THIS PORT (TCP {}) !!!", port);  
			
			System.exit(1);
			
		}
		finally {
			
			
			
		}
	}

}
