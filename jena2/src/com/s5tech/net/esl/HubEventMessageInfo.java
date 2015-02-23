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
 
package com.s5tech.net.esl;

import java.util.Date;

import com.s5tech.net.type.EUI48Address;
import com.s5tech.net.xml.types.HubEventType;
import com.s5tech.net.xml.types.MessageCommand;

public class HubEventMessageInfo extends MessageInfo<EUI48Address>{

	private static final long serialVersionUID = -4582546212153842049L;
	
	private HubEventType type = null;
	private String ipAddress = null;
	
	
	public HubEventMessageInfo() {
		super (false, null, null, MessageCommand.HUBEVENT);
	}

	public HubEventMessageInfo(String msgId, EUI48Address mac, Date creationTime) {
		super (false, msgId, mac, MessageCommand.HUBEVENT, creationTime);
	}

	public HubEventMessageInfo(String msgId, EUI48Address mac) {
		super (false, msgId, mac, MessageCommand.HUBEVENT);
	}
	
	public HubEventMessageInfo(EUI48Address mac) {
		super (false, "", mac, MessageCommand.HUBEVENT);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public HubEventType getType() {
		return type;
	}

	public void setType(HubEventType type) {
		this.type = type;
	}	
	
	
}
