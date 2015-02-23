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

import java.io.Serializable;

import com.s5tech.net.type.EUI48Address;
import com.s5tech.net.util.Tools;

public class ApplicationMessage implements Serializable {

	private static final long serialVersionUID = 1187282855307839177L;
	
	private ApplicationMessageType type;
	private Serializable body;
	private EUI48Address host;
	
	public ApplicationMessage() {
	}

	public ApplicationMessage(ApplicationMessageType type, Serializable body) {
		this(null,type,body);
	}

	public ApplicationMessage(EUI48Address host, ApplicationMessageType type, Serializable body) {
		this.host = host;
		this.type = type;
		this.body = body;
	}

	public ApplicationMessageType getType() {
		return type;
	}

	public void setType(ApplicationMessageType type) {
		this.type = type;
	}

	public Serializable getBody() {
		return body;
	}

	public void setBody(Serializable body) {
		this.body = body;
	}
	
	public <T extends Serializable> T getBody(Class<? extends T> clazz) {
		if(clazz == null || body == null) return null;
		if(clazz.isAssignableFrom(body.getClass()))
			return clazz.cast(body);
		else
			return null;
	}

	/**
	 * Get the host that the message is to or from.
	 * All messages arriving in application router from the network, will get the mac of the originating hub.
	 * @return
	 */
	public EUI48Address getHost() {
		return host;
	}
	
	public void setHost(EUI48Address host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return "host=" + host + ", type=" + type + ", body=" + Tools.toString(body);
	}

}
