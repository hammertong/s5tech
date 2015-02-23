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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class ShutdownApplication {

	public static void exec(String[] args) {
		
		int port = 9000;
		String host = "127.0.0.1";
		File conf = new File ("./conf/s5.conf");
		
		if (args != null) 
		{
			for (int i = 0; i < args.length; i ++)
			{
				if (args[i].equalsIgnoreCase("-p"))
				{
					port = Integer.parseInt(args[++i]);
				}
				else if (args[i].equalsIgnoreCase("-h"))
				{
					host = args[++i];
				}
				else if (args[i].toLowerCase().startsWith("-c"))
				{
					conf = new File(args[++i]);
				}	
			}
		}
			
		if (conf.exists()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(conf));
				for ( ;;) {
					String l = in.readLine();
					if (l == null) break;
					l = l.replace('\t',  ' ').trim();
					if (l.length() == 0) continue;
					if (l.startsWith("#")) continue;
					if (!l.startsWith("shutdown.port")) continue;
					String [] p = l.split("=");
					if (p[0].trim().equals("shutdown.port") && p.length > 1)
					{
						try {
							port = Integer.parseInt(p[1].trim());
							System.out.println("Getting shutdown configuration from " 
									+ conf.getAbsolutePath() + ": TCP port is " + port);	
						}
						catch (Throwable t) {
							t.printStackTrace();
						}
					}
					break;
				}
				in.close();
			}
			catch (Throwable t) {
				t.printStackTrace();
			}				
		}
					
		try {	
			Socket s = new Socket(InetAddress.getByName(host), port);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintStream out = new PrintStream(s.getOutputStream());
			System.out.println("Client connected.");
			System.out.println(in.readLine());
			out.println("shutdown");
			s.close();
			System.exit(0);
		}
		catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
		
	}

}
