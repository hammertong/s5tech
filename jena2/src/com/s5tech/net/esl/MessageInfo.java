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
import java.util.Date;
import java.util.LinkedList;

import com.s5tech.net.type.AbstractByteArrayType;
import com.s5tech.net.xml.types.CommandResult;
import com.s5tech.net.xml.types.MessageCommand;

/**
 * Contains information about a message
 * @author S5Tech Development Team
 *
 */
public abstract class MessageInfo<T extends AbstractByteArrayType> implements Serializable {

	private static final long serialVersionUID = -580849350469880132L;
	private String msgId;
	private T mac;
	private MessageCommand cmd;
	private Date creationTime;
	private LinkedList<Object> content;
	private boolean ack;
	private String description;
	private CommandResult result;

	public MessageInfo() {
		this(false, null,null,null, new Date());
	}
	
	public MessageInfo(boolean isAck, String msgId, T mac,
			MessageCommand cmd) {
		this(isAck, msgId,mac,cmd, new Date());
	}

	public MessageInfo(boolean isAck, String msgId, T mac,
			MessageCommand cmd, Date creationTime) {
		super();
		this.ack = isAck;
		this.msgId = msgId;
		this.mac = mac;
		this.cmd = cmd;
		this.creationTime = creationTime;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public T getMac() {
		return mac;
	}

	public void setMac(T mac) {
		this.mac = mac;
	}

	public MessageCommand getCmd() {
		return cmd;
	}

	public void setCmd(MessageCommand cmd) {
		this.cmd = cmd;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public LinkedList<Object> getContent() {
		ensureContent();
		return content;
	}

	private void ensureContent() {
		if(content == null)
			content = new LinkedList<Object>();		
	}
	
	public void setContent(LinkedList<Object> content) {
		this.content = content;
	}

	public void addContent(LinkedList<Object> content) {
		ensureContent();
		this.content.addAll(content);
	}
	
	public void addContent(Object content) {
		ensureContent();
		this.content.add(content);
	}

	public boolean isAck() {
		return ack;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CommandResult getResult() {
		return result;
	}

	public void setResult(CommandResult result) {
		this.result = result;
	}
	
	/**
	 * Create and return an ack message for this message
	 * @return the new ack message object
	 */
	public MessageInfo<T> getAck() {
		return getAck(null,null);
	}
	
	/**
	 * Create and return an ack message for this message.
	 * The class of the returned object is the same as of the object on which this method is invoked
	 * @param result The result code for the ack
	 * @param description the description of the result
	 * @return the new ack message object
	 */
	@SuppressWarnings("unchecked")
	public MessageInfo<T> getAck(CommandResult result, String description) {
		MessageInfo<T> res;
		try {
			res = getClass().newInstance();
			res.ack = true;
			res.setMsgId(msgId);
			res.setMac(mac);
			res.setCmd(cmd);
			res.setResult(result);
			res.setDescription(description);
		} catch (Exception e) {
			res = null;
			e.printStackTrace();
		}
		return res;
	}
	
}
