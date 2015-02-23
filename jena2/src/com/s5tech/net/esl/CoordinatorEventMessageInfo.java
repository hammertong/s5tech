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

import com.s5tech.net.type.Channel;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.xml.types.CoordinatorEventType;
import com.s5tech.net.xml.types.MessageCommand;

public class CoordinatorEventMessageInfo extends MessageInfo<EUI64Address>{

	private static final long serialVersionUID = -4582546212153842049L;
	
	private String port = null;
	private Channel channelNo = null;
	private CoordinatorEventType type;
	
	public CoordinatorEventMessageInfo() {
		super (false, null, null, MessageCommand.COORDINATOREVENT);
	}

	public CoordinatorEventMessageInfo(String msgId, EUI64Address mac, Date creationTime) {
		super (false, msgId, mac, MessageCommand.COORDINATOREVENT, creationTime);
	}

	public CoordinatorEventMessageInfo(String msgId, EUI64Address mac) {
		super (false, msgId, mac, MessageCommand.COORDINATOREVENT);
	}
	
	public CoordinatorEventMessageInfo(EUI64Address mac) {
		super (false, "", mac, MessageCommand.COORDINATOREVENT);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Channel getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(Channel channelNo) {
		this.channelNo = channelNo;		
	}

	public CoordinatorEventType getType() {
		return type;
	}

	public void setType(CoordinatorEventType type) {
		this.type = type;
	}
	
	
}
