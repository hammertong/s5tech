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
import java.util.Collection;

import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.xml.types.MessageCommand;

public class EslCommand implements Serializable {
	
	private static final long serialVersionUID = -1604783474006573038L;
	
	private MessageCommand command;
	private String refId;
	private boolean allEsls;
	private Collection<EUI64Address> esls;
	private Serializable data;
	
	public EslCommand() {
	}
	
	public EslCommand(MessageCommand command, String refId) {
		this(command,refId,null,null);
	}

	public EslCommand(MessageCommand command, String refId,
			Collection<EUI64Address> esls) {
		this(command,refId,esls,null);
	}

	public EslCommand(MessageCommand command, String refId,
			Collection<EUI64Address> esls, Serializable data) {
		super();
		this.command = command;
		this.refId = refId;
		this.esls = esls;
		this.data = data;
	}

	public MessageCommand getCommand() {
		return command;
	}

	public void setCommand(MessageCommand command) {
		this.command = command;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Collection<EUI64Address> getEsls() {
		return esls;
	}

	public void setEsls(Collection<EUI64Address> esls) {
		this.esls = esls;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

	public boolean isAllEsls() {
		return allEsls;
	}
	
	public void setAllEsls(boolean allEsls) {
		this.allEsls = allEsls;
	}
	
}
