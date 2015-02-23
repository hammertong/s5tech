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

import com.s5tech.net.type.ISerializable;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialPortFacade
{
	public static interface ISerialListener
	{

		public abstract void onData(ByteBuffer bytebuffer);

		public abstract void onOnline();

		public abstract void onOffline();
	}
	
	private static Logger applog = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	private static Logger log = LoggerFactory.getLogger(ISystemKeys.SERIALFRAMING);
	
	private int reconnectTimerTimeout = -1;
	
	private String serialPort;
	private ISerialPortProxy port;
	private Thread proc;
	private InputStream in;
	private OutputStream out;
	private AtomicBoolean online;
	private ISerialListener listener;
	
	boolean wasOnline = false;
	long onlineCount = 0;
	long offlineCount = 0;
	long offlineTime = 0;
	long onlineTime = 0;

	public SerialPortFacade(String serialPortName)
	{
		this(((ISerialPortProxy) (null)), serialPortName);
	}

	public SerialPortFacade(ISerialPortProxy port)
	{
		this(port, ((String) (null)));
	}

	public SerialPortFacade(ISerialPortProxy port, String serialPortName)
	{
		serialPort = serialPortName;
		this.port = port;
		online = new AtomicBoolean(false);
	}

	public synchronized boolean start()
	{
		if (proc != null) 
		{
			if (in == null)
			{
				try {
					proc.interrupt();
					close();
				}
				catch (Exception e) {
					log.error("error stopping reader {} ...", e);
				}
			}
			else {
				return false;
			}
		}
		
		if (serialPort == null && port == null)
		{
			throw new IllegalStateException("Serial port not set!");
		} 
		else
		{
			proc = new Thread(new Runnable() {				
					public void run()
					{
						proc();	
						stop();
					}			
				}
			);
			proc.setDaemon(true);
			proc.setName("Serial reader " + serialPort);
			
			//TODO: search another thread with the same name!!!
			
			
			proc.start();
			boolean ok = connect();
			online.set(!ok);
			setOnlineStatus(ok);
			return true;
		}
	}

	public synchronized void stop()
	{
		if (proc != null)
		{
			log.debug("Stopping port {}", ((Object) (serialPort)));
			proc.interrupt();
			close();
			//proc = null;
		}
	}

	private void close()
	{
		if (in == null && out == null) return;
		
		if (log.isTraceEnabled()) 
			log.trace("Attempting to close and release in and output streams " 
					+ serialPort);
		
		try
		{
			if (in != null) in.close();
		}
		catch (IOException e)
		{
			log.error("IOException while closing input stream {}: {}", serialPort, ((Object) (e.getMessage())));
		}
		
		try
		{
			if (out != null) out.close();
		}
		catch (IOException e)
		{
			log.error("IOException while closing output stream {}: {}", serialPort, ((Object) (e.getMessage())));
		}
		
		in = null;
		out = null;
		
		try { port.close(); } catch (Throwable ignored) {}
	
	}

	public synchronized boolean transmit(byte data[])
	{
		return transmit(new SerialFrame(data));
	}

	public synchronized boolean transmit(ByteBuffer data)
	{
		return transmit(new SerialFrame(data));
	}

	public synchronized boolean transmit(ISerializable data)
	{
		return transmit(new SerialFrame(data));
	}

	private synchronized boolean transmit(SerialFrame frame)
	{
		if (frame == null)
			throw new IllegalArgumentException("the parameter \"frame\" must be set!");
		if (!online.get() || out == null)
			return false;
		if (log.isTraceEnabled())
			log.trace("SEND > " + serialPort + " - " + frame.toString());
		boolean res = false;
		try
		{
			frame.write(out);
			out.flush();
			res = true;
		}
		catch (Exception e)
		{
			log.error("An error occured while writing to the serial port {}: {}", serialPort, e.getMessage());
			if (log.isTraceEnabled()) log.trace("{}", ((Throwable) (e)));
			setOnlineStatus(false);
		}
		return res;
	}

	public synchronized boolean isRunning()
	{
		//return proc != null;
		return proc != null && in != null;
	}

	public void setListener(ISerialListener listener)
	{
		this.listener = listener;
	}

	public void removeListener()
	{
		listener = null;
	}

	public String getSerialPortName()
	{
		return serialPort;
	}

	public boolean isOnline()
	{
		return online.get();
	}

	private void setOnlineStatus(boolean online)
	{
		if (this.online.get() == online) return;
		
		this.online.set(online);
		
		if (online) {
			if (!wasOnline) {
				onlineCount ++;
				applog.info("serial/virtual port {} online", port.getSerialPortName());
				onlineTime = System.currentTimeMillis();
			}
		}
		else {
			if (wasOnline) {
				offlineCount ++;
				applog.info("serial/virtual port {} offline", port.getSerialPortName());
				offlineTime = System.currentTimeMillis();
			}
		}
		
		wasOnline = online;
		
		if (listener != null)
		{
			if (online) {
				listener.onOnline();
			}
			else {
				listener.onOffline();				
			}
		}
		
		if (online)
		{
			return;
		} 
		else
		{
			close();
			if (reconnectTimerTimeout > 0) 
			(new Timer("Serial port reconnect " + serialPort, true)).schedule(((TimerTask) (new TimerTask() {
					public void run()
					{
						if (proc == null) cancel();
						boolean c = connect();
						setOnlineStatus(c);
						if (c) cancel();
					}
				}
			)), 1000L, reconnectTimerTimeout);
			return;
		}
	}

	private boolean connect()
	{
		boolean res = false;
		if (port == null) {
			port = createPlatformSpecificPort(serialPort);
		}
		if (port == null) {
			log.error("Unable to create serial port proxy {} for this platform!", serialPort);
			LoggerFactory.getLogger("APPLICATION").error("Unable to create serial port proxy {} for this platform!", serialPort);
		}
		else if (!port.portExists()) {
			log.error((new StringBuilder()).append("The specified port (").append(serialPort).append(") does not exist!").toString());
			LoggerFactory.getLogger("APPLICATION").error((new StringBuilder()).append("!!! ERROR !!! Port ").append(serialPort).append(" NOT FOUND !!!").toString());
		}
		else if (!port.connect()) {
			log.error((new StringBuilder()).append("Unable to connect to the serial port: ").append(serialPort).toString());
			LoggerFactory.getLogger("APPLICATION").error((new StringBuilder()).append("Unable to connect to the serial port: ").append(serialPort).toString());
		} 
		else
		{
			in = port.getInputStream();
			if (in != null && !(in instanceof BufferedInputStream))
				in = ((InputStream) (new BufferedInputStream(in, 765)));
			out = in == null ? null : port.getOutputStream();
			if (out == null)
			{
				log.error("Unable to get the in and output streams for the serial port {}", serialPort);
				LoggerFactory.getLogger("APPLICATION").error("Unable to get the in and output streams for the serial port {}", serialPort);
			} 
			else
			{
				//++
				//if (!serialPort.startsWith("tcp://") && !serialPort.startsWith("ssl://")) {
					if (!(out instanceof BufferedOutputStream))
						out = ((OutputStream) (new BufferedOutputStream(out)));
				//}
				res = true;
			}
		}
		return res;
	}

	private void proc()
	{
		Object waitObj = "";
		int avail = 0;
		
		int serialReaderConnectTimeout = 10000;
		
		try {
			serialReaderConnectTimeout = Integer.parseInt(System.getProperty("serialReaderConnectTimeout", "10000"));
		} catch(Throwable nnex) { 
			log.trace ("cannot parse serialReaderConnectTimeout property - {}", nnex); 
		}
				
		if (reconnectTimerTimeout > 0 && serialReaderConnectTimeout >= 0) {
			log.trace("serial reader configured with reconnect timeout, initial connect timeout will be ignored!");
			serialReaderConnectTimeout = -1;		
		}
		
		boolean stanby =  (serialReaderConnectTimeout < 0);
				
		log.info("starting serial reader using "
				+ (stanby ? "persistent serial reader conection with " 
						+ (reconnectTimerTimeout > 0 ? "reconnect timer of " + reconnectTimerTimeout + "ms" : "NO reconnect time !!!") 
						: " initial connection timeout of " + serialReaderConnectTimeout + "ms")); 
				
		try {
		
			do
			{
				if (proc == null) break;
				
				if (stanby) { 
					for (; !isOnline(); Tools.doWait(waitObj, 1000L));
				}
				else {					
					long sstart = System.currentTimeMillis();
					for (; !isOnline() && (System.currentTimeMillis() - sstart < serialReaderConnectTimeout); Tools.doWait(waitObj, 100L));
				}
				
				if (!isOnline()) {
					log.error("cannot start serial reader @" + port.getSerialPortName() + ": timeout exceeded while connecting!");
					break;
				}
				
				SerialFrame frame = new SerialFrame();
				
				try
				{
					
					if (stanby) { 
						for (; in != null && avail == in.available() && proc != null; Tools.doWait(waitObj, 100L));
					}
					
					while (in.read() != 2) ;
					
					in.mark(258);
					frame.skipStartOfFrame();
					int read = frame.read(in);
					if (read > 0)
					{
						if (log.isTraceEnabled())
							log.trace("RECV < " + serialPort + " - " + frame.toString());						
						if (listener != null)
							listener.onData(frame.getData());
					} 
					else
					{
						in.reset();
						if (!frame.isFcsMismatch())
							avail = in.available();
					}
				}
				catch (Exception e)
				{
					if (proc != null)
					{
						log.error("An error occured while communicating with the serial port {}: {}", serialPort, ((Object) (e.getMessage())));
						log.debug("", ((Throwable) (e)));
					}
					setOnlineStatus(false);
				}
				
				
			} while (true);
		
		}
		catch (Throwable t) {
			
			log.error("serial reader exit error !!! - {}", t);
			
		}	
		
		log.warn("exit from serial reader @" + serialPort);
	}
	
		
		
	public static ISerialPortProxy createPlatformSpecificPort(String serialPortName)
	{
		ISerialPortProxy port = null;
		
		if (serialPortName != null)
			
			try {
			
				if (serialPortName.startsWith("tcp://")) {
					//port = ((ISerialPortProxy) (new SocketSerialPortProxy2(serialPortName)));
					port = ((ISerialPortProxy) (new SocketSerialPortProxy(serialPortName)));
				}
				else if (serialPortName.startsWith("ssl://")) {
					port = ((ISerialPortProxy) (new SSLSocketSerialPortProxy(serialPortName)));				
				}
				else if (serialPortName.startsWith("rxtx://")) {
					port = ((ISerialPortProxy) (new RxTxSerialPortProxy(serialPortName.substring(7))));				
				}
				else if (serialPortName.startsWith("file://")) {
					port = ((ISerialPortProxy) (new UnixSerialPortProxy(serialPortName.substring(7))));				
				}
				else if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
					port = ((ISerialPortProxy) (new RxTxSerialPortProxy(serialPortName)));
				}
				else if (System.getProperty("os.name").toLowerCase().indexOf("osx") >= 0) {
					port = ((ISerialPortProxy) (new RxTxSerialPortProxy(serialPortName)));
				}				
				else {
					port = ((ISerialPortProxy) (new UnixSerialPortProxy(serialPortName)));
				}
				
			}
		catch (Throwable t) {
			
			port = null;
			log.error("cannot instantiate port proxy at  {} - reason {}", serialPortName, t);
			
		}
		
		//log.debug("Created serial port proxy: {}", port != null ? ((Object) (((Object) (port)).getClass())) : "[none]");
		
		return port;
	}

	public void setReconnectTimeoutMs(int reconnectTimeout) {
		this.reconnectTimerTimeout = reconnectTimeout;
	}
	
	public int getReconnectTimeoutMs() {
		return this.reconnectTimerTimeout;
	}
	
	public long getOnlineCount() {
		return onlineCount;
	}

	public long getOfflineCount() {
		return offlineCount;
	}
	
	public long getOfflineTime()
	{
		return offlineTime;
	}
	
	public long getOnlineTime()
	{
		return onlineTime;
	}
	
}

