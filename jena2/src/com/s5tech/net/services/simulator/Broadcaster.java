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
 
package com.s5tech.net.services.simulator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

public class Broadcaster implements Runnable {

	static final Logger log = Logger.getLogger("BROADCAST");
	
	String msg = "ID=001B000000BB;COORDS=0\n"; 
	String ip = "127.0.0.1";
	int port = 55559;
	int timeout = 30000;
	
	public Broadcaster() 
	{
	}
	
	public Broadcaster(String ip, int port, int timeout, String msg) 
	{
		this.ip = ip;
		this.port = port;
		this.msg = msg;
		this.timeout = timeout;
	}
		
	public void run() {
		
		DatagramSocket sock = null;
		
		try {
			
			byte[] buffer = msg.getBytes();
			int len = buffer.length;
			sock = new DatagramSocket();
			sock.setBroadcast(true);
			InetAddress host = InetAddress.getByName(ip);
			DatagramPacket packet = new DatagramPacket (buffer, len, host, port);
		    packet.setLength(len);
		    
		    log.info ("Running broadcaster @" + ip + ":" + port);
		    
			for ( ;; ) 
			{
				
				sock.send(packet);
				//System.out.println(new Date().toString() + " sent to " + ip + ":" + port + " > " + msg.replace('\n', ' '));  
				Thread.sleep(timeout);
			}
	
		}
		catch (Exception e) {
		
			e.printStackTrace();
		
		}
		finally {
		
			if (sock != null) try { sock.close(); } catch (Exception e) {}
	
		}

	}

}
