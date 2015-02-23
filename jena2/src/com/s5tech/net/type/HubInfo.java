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
 
package com.s5tech.net.type;

import java.io.Serializable;


/**
 * An hub descriptor for Observable list contained in Network Manager
 * @author S5Tech Development Team
 */
@SuppressWarnings("serial")
public class HubInfo implements Serializable, Comparable<HubInfo> {

	private EUI48Address mac;
	
	public HubInfo() {
	}

	/**
	 * Sets the mac address of the Esl
	 * @param mac
	 */
	public HubInfo(EUI48Address mac) {
		this();
		this.mac = mac;
	}

	/**
	 * @return the mac
	 */
	public EUI48Address getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(EUI48Address mac) {
		this.mac = mac;
	}

	/**
	 * Compares the mac addresses of the object using {@link EUI48Address#equals(Object)}
	 */
	@Override
	public boolean equals(Object arg0) {
		return mac != null && mac.equals(arg0);
	}
	
	private String ipAddress;
	private String protocol = null;
	private String version;
	private String ports = null;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	@Override
	public int hashCode() {
		int h = 0;
		h += (ports == null ? 0 : ports.hashCode()); 
		h += (version == null ? 0 : version.hashCode()); 
		h += (mac == null ? 0 : mac.hashCode()); 
		return h;
	};
	
	@Override
	public int compareTo(HubInfo arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
