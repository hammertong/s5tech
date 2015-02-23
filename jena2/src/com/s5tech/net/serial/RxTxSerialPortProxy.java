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
 
/*
 * This class is neither used, nor tested, but is left for inspiration
 * if someone decides to run the hubapp on windows/osx some day
 */

package com.s5tech.net.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;

public class RxTxSerialPortProxy implements ISerialPortProxy {

	private Logger log;
	private SerialPort port;
	private String portName;
	
	public RxTxSerialPortProxy() {
		this(null);
	}
	
	public RxTxSerialPortProxy(String serialPortName) {
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + "." + (serialPortName == null? "SERIALPORT_NOTSET" : serialPortName));		
		setSerialPortName(serialPortName);
	}

	private CommPortIdentifier getPortIdentifier() {
		if(portName == null) return null;

		Enumeration<?> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		
		CommPortIdentifier ci = null;
		
		while(portIdentifiers.hasMoreElements()) {
			ci = CommPortIdentifier.class.cast(portIdentifiers.nextElement());
			if(ci == null) break;
			if(CommPortIdentifier.PORT_SERIAL == ci.getPortType()) {
				if(portName.equals(ci.getName())) {
					break;
				}
			}
			ci = null;
		}
		return ci;
		
	}
	
	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop.serial.ISerialPortProxy#portExists()
	 */
	public boolean portExists() {
		return getPortIdentifier() != null;
	}
	
	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop.serial.ISerialPortProxy#connect()
	 */
	public boolean connect() {
		
		CommPortIdentifier portIdentifier = getPortIdentifier();
		
		if(portIdentifier == null) return false;

		try {
			port = (SerialPort) portIdentifier.open("HubApplication", 50);
			port.setSerialPortParams(115200, 8, 1, 0);
			if (log.isTraceEnabled()) log.trace("Running serial port with baudrate = " + port.getBaudRate());
			return true;
		} catch(PortInUseException e) {
			log.error("Port already in use by " + e.currentOwner);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop.serial.ISerialPortProxy#getSerialPortName()
	 */
	public String getSerialPortName() {
		return portName;
	}

	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop.serial.ISerialPortProxy#setSerialPortName(java.lang.String)
	 */
	public void setSerialPortName(String serialPortName) {
		portName = serialPortName;
	}
	
	public InputStream getInputStream() {
		if(!portExists()) return null;
		InputStream is;
		try {
			is = port.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			is = null;
		}
		return is;
	}
	
	public OutputStream getOutputStream() {
		if(!portExists()) return null;
		OutputStream os;
		try {
			os = port.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			os = null;
		}
		return os;
	}

	@Override
	public void close() {
		
		if (port != null) try { port.close(); } catch (Throwable t) {}
		port = null;
		
	}
	
	/*
	public static void main (String [] args) {
		
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.print(portIdentifier.getName()  +  " - ");
                        
            switch ( portIdentifier.getPortType() )
            {
                case CommPortIdentifier.PORT_I2C:
                	System.out.println("I2C");
                	break;
                case CommPortIdentifier.PORT_PARALLEL:
                	System.out.println("Parallel");
                	break;
                case CommPortIdentifier.PORT_RAW:
                	System.out.println("Raw");
                	break;
                case CommPortIdentifier.PORT_RS485:
                	System.out.println("RS485");
                	break;
                case CommPortIdentifier.PORT_SERIAL:
                	System.out.println("Serial");
                	break;
                default:
                	System.out.println("unknown type");
            }
            
        }  
	
	}
	*/
	
	
}
