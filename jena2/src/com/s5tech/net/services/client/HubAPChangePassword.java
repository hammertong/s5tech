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
 
package com.s5tech.net.services.client;

import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class HubAPChangePassword implements Runnable {

	private static final int UDP_PACKET_SIZE = 1024;
	
	private static final DateFormat tf = new SimpleDateFormat("yyyyMMddHHmmss");

	private int port = 55559;

	private long timeout = 60000;
	
	private String password;

	private Vector<String> macList = new Vector<String>();
	
	private String remoteAddress = null;
	
	private String hubroot = "root";
	
	private String hubPass = "s5tech321!";
	
	private int sshport = 22;
		
	private void listen()
	{
		PrintStream o =	System.err;
		//long exitTime = System.currentTimeMillis();
		//exitTime += timeout;
		
		byte [] buffer = new byte [UDP_PACKET_SIZE];
		DatagramSocket server = null;		
		
		try {

			server = new DatagramSocket(port); 
			server.setBroadcast(true);
			
			for ( ;; ) {
				
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				server.receive(packet);				
				String data = new String(packet.getData(), 0, packet.getLength());										
				String address = packet.getAddress().getHostAddress();
				String hubmac = null;

				try {
					
					for (String p : data.split(";"))
					{
						String [] kv = p.split("=");
						if (kv[0].equals("ID"))
						{
							hubmac = kv[1].replaceAll("\\:", "");
						}
					}
					
					if (hubmac == null) throw new Exception (
							"wrong data packet (cannot parse hub mac address) from " + address);
					
					if (macList.contains(hubmac)) continue;					
					
					o.println ("trying to change password for hub " 
							+ address 
							+ " at ip address " 
							+ hubmac 
							+ " ...");

					macList.add (hubmac);
					sshChangePassword (address);					

				}
				catch (Throwable e) {
					
					o.println("error: " + e.getMessage());
					o.println("packet discarded, DATA > " + data);
					
				}
				
			}
			
		}
		catch (Throwable t) 
		{
			o.println("fatal error in listener ...");
			t.printStackTrace();
		}	
		
	}
	
	
	private void sshChangePassword(String ip) 
	{		
		System.out.println("changing the password ...!!!");
		System.out.println("HUB IP => " + ip);
		//System.out.println("PASSWD => " + password);
		
		SSHRemoteCommand rem = new SSHRemoteCommand();
		rem.execute(hubroot, hubPass, ip, sshport, 
				"cat /etc/s5.config > /etc/s5.config." + tf.format(new Date()));
		rem.execute(hubroot, hubPass, ip, sshport, 
				"sed -e '/AP_PASS/d' < /etc/s5.config > xxx && echo AP_\"PASS="
				+ password + "\" >> xxx && rm -f /etc/s5.config && mv xxx /etc/s5.config");
		if (rem.getExitCode() == 0) rem.execute(hubroot, hubPass, ip, sshport, 
				"reboot");
		
		System.out.println("EXITCODE = " + rem.getExitCode());
	}
	
	public void run() 
	{
		if (remoteAddress != null) {
			System.err.println("setting new password on hub with ip address " + remoteAddress + " ...");
			sshChangePassword(remoteAddress);
		}
		else {
			System.err.println("listening on UDP port " + port + " hub broadcasts ...");
			System.err.println("listening for " + (timeout / 1000) + " seconds and setting new password for them ...");
			listen();
		}
	}
	
	
	public static void exec(String[] args) 
	{
		
		HubAPChangePassword h = new HubAPChangePassword();
		
		try 
		{
			for (int i = 0; i < args.length; i ++) {			
				if (args[i].equals("-h")) {
					h.remoteAddress = args[++i];
				}
				else if (args[i].equals("-p")) {
					h.port = Integer.parseInt (args[++i]);
				}
				else if (args[i].equals("-w")) {
					h.hubPass = args[++i];
				}
				else if (args[i].equals("-r")) {
					h.hubroot = args[++i];
				}
				else if (args[i].equals("-s")) {
					h.sshport = Integer.parseInt(args[++i]);
				}
				else if (args[i].equals("-t")) {
					h.timeout = 1000 * Integer.parseInt (args[++i]);
				}
				else if (args[i].equals("-W")) { //new AP password
					h.password = args[i];
				}				
			}
			
			if (h.password == null) {
				System.exit(1);
			}
			
			h.run();
			System.exit(0);
			
		}
		catch (Throwable t) {
		
			t.printStackTrace();
			System.exit(1);
			
		}
		
		
	}
	
	
	
	

}
