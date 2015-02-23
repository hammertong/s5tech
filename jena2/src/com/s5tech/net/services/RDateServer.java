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

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;

public class RDateServer implements Runnable {	
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	private int port;
	
	static final long LDATE_1_1_1970 = 2208988800L;
	
	public RDateServer()
	{
		port = 37;
		try {
			port = Integer.parseInt(System.getProperty ("rdateserver.port", "37"));
		}
		catch (Throwable t) {}
	}
	
	public RDateServer(int port)
	{
		this.port = port;
	}

	@Override
	public void run() {
		
		try 
		{
			ServerSocket server = ServerSocketFactory
					.getDefault()
					.createServerSocket (port);//, 0, InetAddress.getByName("0.0.0.0"));	
			
			log.info("RDate server active and listening on port {} ...", port);
			
			for ( ;; ) 
			{
				Socket socket = server.accept();
	
				try 
				{
					String address = socket.getRemoteSocketAddress().toString();
					if (log.isTraceEnabled())
						log.trace ("RDate setting time @ {} ...", address);
										
					long l = LDATE_1_1_1970;
					l += (System.currentTimeMillis() / 1000);
					
					byte [] array = new byte [4];
					int shiftCount = 3 * 8;
					int shiftCountIncrement = -8;
		
					for (int cnt = 0; cnt < 4; cnt++) {
						array[cnt] = (byte) ((l >> shiftCount) & 0xff);
						shiftCount += shiftCountIncrement;
					}

					OutputStream o = socket.getOutputStream();
					o.write(array);
					o.flush();					
					
				}
				catch (Throwable t) 
				{
					log.error("RDate error: {}", t);
				}
				finally 
				{
					if (socket != null) try { socket.close(); } catch (Throwable ignored) {}									
				}
			}
		}
		catch (Throwable t) 
		{
			log.error("RDATE Server critical error - Exit! Reason: {}", t);
		}
		
	}		
	
//	public static void main (String [] args) {
//		
//		RDateServer r = new RDateServer();
//		r.run();
//		
//	}

}

