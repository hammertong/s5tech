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
 
package com.s5tech.net.serial;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;

// Referenced classes of package com.s5tech.net.desktop.serial:
//			ISerialPortProxy

public class SocketSerialPortProxy
	implements ISerialPortProxy
{

	private String name;
	private Socket sock;
	private String host;
	private int port;
	private Logger log;

	public SocketSerialPortProxy(String serialPortName)
	{
		init (serialPortName);
	}

	private void init(String serialPortName) 
	{
		
		Exception ex = null;
		
		try
		{
			if (serialPortName.lastIndexOf(':') <= "tcp:".length()) serialPortName += ":55550";
			int colon = serialPortName.lastIndexOf(":");
			host = serialPortName.substring(serialPortName.lastIndexOf("/") + 1, colon);
			port = Integer.decode(serialPortName.substring(colon + 1)).intValue();
		}
		catch (Exception e)
		{
			ex = e;
		}
		
		if (ex != null || host == null || "".equals(((Object) (host))) || port < 0)
		{
			throw new IllegalArgumentException(
					"valid serial port url must be specified! (e.g: tcp://localhost:54321)", 
					((Throwable) (ex)));
		} 
		else
		{
			name = serialPortName;
			log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + "." + name);			
			return;
		}
	}	

	//public SocketSerialPortProxy(Socket socket)
	//{
	//	log = LoggerFactory.getLogger(ILoggers.APPLICATION + "." + name);
	//	sock = socket;
	//	name = ((Object) (sock.getRemoteSocketAddress())).toString();
	//}

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
				if (sock != null) try { sock.close(); } catch (Throwable xxx) {}
				sock = new Socket(host, port);
				
				sock.setKeepAlive(true);
				
				//sock.setTcpNoDelay(false);
				//sock.setTrafficClass(0x10); //low_latency = 0x10
				//sock.setPerformancePreferences(0, 1, 0); //set low latency betweeen processing input and output
				//sock.setSoTimeout(0);
				//sock.setReceiveBufferSize(255); //default should be 65K
				//sock.setSendBufferSize(90);
				
				sock.setSoLinger(false, 0);
				
				res = true;
			}
		}
		catch (Exception e)
		{
			log.error("connect exception - {}", ((Throwable) (e)));
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


