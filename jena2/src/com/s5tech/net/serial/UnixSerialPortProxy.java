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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;

public class UnixSerialPortProxy implements ISerialPortProxy {

	private Logger log;
	public File serialPort;
	
	public UnixSerialPortProxy() {
		this(null);
	}
	
	public UnixSerialPortProxy(String serialPortName) {
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + "." + (serialPortName == null? "SERIALPORT_NOTSET" : serialPortName));	
		setSerialPortName(serialPortName);
	}

	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop.serial.ISerialPortProxy#portExists()
	 */
	public boolean portExists() {
		return serialPort != null && serialPort.exists();
	}
	
	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop..serial.ISerialPortProxy#connect()
	 */
	public boolean connect() {
		return portExists() && serialPort.canRead() && serialPort.canWrite();
	}

	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop..serial.ISerialPortProxy#getSerialPortName()
	 */
	public String getSerialPortName() {
		return serialPort == null ? "" : serialPort.getName();
	}

	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop..serial.ISerialPortProxy#setSerialPortName(java.lang.String)
	 */
	public void setSerialPortName(String serialPortName) {
		serialPort = new File(serialPortName);
	}
	
	public InputStream getInputStream() {
		if(!portExists()) return null;
		InputStream is;
		try {
			final FileInputStream inputStream = new FileInputStream(serialPort);
			is = inputStream;
		} catch (FileNotFoundException e) {
			log.error("Unable to acquire input stream; serial port " + serialPort + " not found!");
			is = null;
		}
		return is;
	}
	
	public OutputStream getOutputStream() {
		if(!portExists()) return null;
		OutputStream os;
		try {
			FileOutputStream outputStream = new FileOutputStream(serialPort);
			os = outputStream;
		} catch (FileNotFoundException e) {
			log.error("Unable to acquire output stream; serial port " + serialPort + " not found!");
			os = null;
		}
		return os;
	}

	@Override
	public void close() {
		//nothing to do here ?
	}
	
}
