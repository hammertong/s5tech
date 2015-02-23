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
import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 
 * Class used to run remote commands on hub via SSH client
 * @author GANDALF
 *
 */
public class SSHRemoteCommand {
	
	protected StringBuffer buffer = null;
	protected int exitCode = -1;
	
	public void execute (String user, String password, String hostname, int sshPort, String command) 
	{

		try {
			
			buffer = new StringBuffer();
			exitCode = -1;
			
			JSch jsch = new JSch();
			Session session = jsch.getSession (user, hostname, sshPort);
			
			Properties config = new Properties();  
			config.put ("StrictHostKeyChecking", "no"); 
			session.setConfig(config); 
			
			session.setPassword(password);
			session.connect();

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// channel.setInputStream(System.in);
			channel.setInputStream(null);
			// channel.setOutputStream(System.out);

			// FileOutputStream fos=new FileOutputStream("/tmp/stderr");
			// ((ChannelExec)channel).setErrStream(fos);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					//System.out.print (new String(tmp, 0, i));
					buffer.append(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					exitCode = channel.getExitStatus();
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	
	public String getResponse()
	{
		return (buffer == null ? null : buffer.toString()); 
	}
	
	
	public int getExitCode()
	{
		return exitCode;
	}
		
	
	public static void exec(String[] arg) 
	{		
	
		String user = "root";
		String password = "s5tech321!";
		String hostname = "10.1.1.100";
		int port = 22;
		String command = "ps ax | grep socat | grep -v grep";
		
		for (int i = 0; i < arg.length; i ++)
		{
			if (arg[i].equals("-u")) {
				user = arg[++i];
			}
			else if (arg[i].equals("-w")) {
				password = arg[++i];
			}
			else if (arg[i].equals("-p")) {
				port = Integer.parseInt(arg[++i]);	
			}
			else if (arg[i].equals("-h")) {
				hostname = arg[++i];
			}
			else if (arg[i].equals("-c")) {
				command = arg[++i];
			}			
		}
		
		System.err.println("Connecting to " + hostname + ":" + port + " ...");
		SSHRemoteCommand rem = new SSHRemoteCommand();
		rem.execute(user, password, hostname, port, command);
		System.err.println("COMMAND:   " + command);
		System.err.println("EXIT CODE: " + rem.getExitCode());
		System.out.println(rem.getResponse());
		
		System.exit(rem.getExitCode());
		
	}		
	
}
