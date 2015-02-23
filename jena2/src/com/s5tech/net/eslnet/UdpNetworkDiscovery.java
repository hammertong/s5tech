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
 
package com.s5tech.net.eslnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.s5tech.net.services.webapp.IAdminServices;

/**
 * 
 * Classe originariamente usata da webapp come client remoto
 * per la creazione di una facade della netwrok application.
 * 
 * @author GANDALF
 *
 */
public class UdpNetworkDiscovery 
		implements IAdminServices {
	
	private static final Log log = LogFactory.getLog(UdpNetworkDiscovery.class);
	
	private Socket s = null;
	
	private BufferedReader in = null;
	
	private PrintStream out = null;
	
	String proto = "tcp";
	String host = "127.0.0.1";
	int port = 9000;
	
	String server_id = null;
	String version = null;
	
	public UdpNetworkDiscovery(String url)
	{		
		if (url.length() > 0) {
			String [] p = url.split("[\\:\\?\\/\\&]");
			proto = p[0].trim().toLowerCase();
			if (p.length > 3) host = p[3].trim();
			if (p.length > 4) try { port = Integer.parseInt(p[4]);} catch (Throwable t) {}
			//if (p.length > 5) try { timeout = Integer.parseInt(p[5]);} catch (Throwable t) {}
		}
	}
		
	public void open() throws Exception
	{	
		log.info("connecting to " + proto + "://" + host + ":" + port + " ...");
	
		if (proto.equalsIgnoreCase("tcp")) {
			s = SocketFactory.getDefault().createSocket(host, port);				
		}
		else if (proto.equalsIgnoreCase("ssl")) {
			s = SSLSocketFactory.getDefault().createSocket(host, port);
		}
		else {
			throw new Exception("invalid protocol specified: " + proto);				
		}
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintStream(s.getOutputStream());
		server_id = in.readLine();
	}
	
	private synchronized void sendline(String cmd) throws Exception
	{
		if (s == null || s.isClosed() || !s.isConnected()) {
			open();
		}
		out.println(cmd);
		out.flush();		
	}
	
	public boolean isConnected()
	{
		return (s != null && !s.isClosed() && s.isConnected());
	}
	
	public boolean shellOutputFindString(String cmd, String findString)
	{
		Process p = null;
		BufferedReader in = null;
		try {			
			p = Runtime.getRuntime().exec(cmd);
			in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));			
			for ( ;; ) {
				String l = in.readLine();
				if (l == null) break;
				if (l.equalsIgnoreCase(findString)) return true;
			}
			return false;
		}
		catch (Throwable t) {
			t.printStackTrace();			
			return false;
		}
		finally {
			//try { p.waitFor(); } catch (Throwable eccezzziuunale) {}
			if (in != null) try {in.close(); } catch (Throwable ignored) {}
		}				
	}
	
	private synchronized String receiveData() throws Exception
	{
		StringBuffer b = new StringBuffer();
		
		String l = in.readLine();	
		int code = Integer.parseInt(l.substring(0, 3));
		
		switch (code)
		{
		case 200:
			b.append(l);
			break;
		case 202:
			for ( ;;) {
				l = in.readLine();
				if (l == null) throw new Exception("remote error: connection closed");
				if (l.length() == 0) break;
				b.append(l).append('\n');
			}
			break;
		default:
			throw new Exception("remote error: " + l);
		}

		return b.toString();
	}
	
	public synchronized void close()
	{
		if (s == null || s.isClosed() || !s.isConnected()) return;
		try { in.close(); in= null; } catch (Throwable ignored) {}
		try { out.close(); out = null; } catch (Throwable ignored) {}
		try { s.close(); s = null; } catch (Throwable ignored) {}
	}
	
	public String executeCommand(String cmd) {
		String r = null;
		try {
			sendline(cmd);
			r = receiveData();
		}
		catch (Throwable t) {
			try {
				log.warn("remote service connection lost? try to reconnect...");
				close();
				open();
				sendline(cmd);
				r = receiveData();
			}
			catch(Throwable ex) {
				ex.printStackTrace();				
			}
		}		
		return (r != null ? r : "");
	}
		
	public synchronized String getVersion() {
		if (version == null) {
			version = executeCommand("net ver"); 
		}
		return version;
	}
	
	public String[] getCoordinators(String params) {
		return executeCommand("net ls" 
				+ (params != null ? " " + params.trim() : "")).split("\n");
	}

	
	public String[] getCoordinatorsQueues() {
		return executeCommand("net que").split("\n");
	}
	
	public String[] getHubs() {
		return executeCommand("net hub").split("\n");
	}

	
	public String[] getEsls(String params) {
		return executeCommand("net esl" 
				+ (params != null ? " " + params.trim() : "")).split("\n");
	}
	
	
	public String[] getFirmwareInfo() {
		return executeCommand("net fir").split("\n");
	}

	
	public String[] getFirmwareUpgradeInfo() {
		return executeCommand("net upg").split("\n");
	}

	
	public String setCoordinatorChannel(String params) {
		return executeCommand("net chn" 
				+ (params != null ? " " + params.trim() : ""));
	}

	
	public String setCoordinatorReset(String params) {
		return executeCommand("net res" 
				+ (params != null ? " " + params.trim() : ""));
	}

	
	public String setCoordinatorTime(String params) {
		return executeCommand("net tim" 
				+ (params != null ? " " + params.trim() : ""));
	}
	
	public String getPrice(String esl, boolean pending) {
		return executeCommand("net pri " + esl + (pending ? " pending" : ""));
	}
	
	public String getParameters() {
		return executeCommand("net par");
	}
	
	public String[] getTasks() {
		return executeCommand("net tsk").split("\n");
	}
	
	public String getSystemInfo() {
		return executeCommand("net sys");
	}
	
	public String retryPriceUpdate(String params) {
		return executeCommand("net res" 
				+ (params != null ? " " + params.trim() : ""));
	}

}
