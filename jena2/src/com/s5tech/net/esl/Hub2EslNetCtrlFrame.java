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

import java.nio.ByteBuffer;

import com.s5tech.net.type.AbstractSerializableFrame;
import com.s5tech.net.type.ISerializable;
import com.s5tech.net.util.Tools;

/**
 * This class marshalls and unmarshalls an esl network manager control protocol frame.
 * It currently ignores the protocol version
 * The priority is used in the outbound queue, to enable faster delivery of validate response frames
 * @author S5Tech Development Team
 *
 */
public class Hub2EslNetCtrlFrame extends AbstractSerializableFrame implements Comparable<Hub2EslNetCtrlFrame> {

	public static final int MAX_LENGTH = 252;
	public static final int MAX_PDU_LENGTH = 250;
	public static final int PRI_LOW = 1;
	public static final int PRI_NORMAL = 0;
	public static final int PRI_HIGH = -1;
	public static final int PRI_HIGHEST = -2;

	private byte protocolVersion;
	private Hub2EslNetCtrlCommand command;
	private int priority;

	public Hub2EslNetCtrlFrame() {
		this(null,null, PRI_NORMAL);
	}
	
	public Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand command) {
		this(command, null, PRI_NORMAL);
	}
	
	public Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand command, ISerializable pdu) {
		this(command, pdu, PRI_NORMAL);
	}
	
	public Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand command, ISerializable pdu, int priority) {
		this.command = command;
		this.priority = priority;
		setPdu(pdu);
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}
	
	@Override
	protected int getLengthOfHeadAndTail() {
		return 2;
	}

	@Override
	public int getMaxPduLength() {
		return MAX_PDU_LENGTH;
	}

	@Override
	protected int readHead(ByteBuffer src) {
		if(src == null) return -1;
		protocolVersion = src.get();
		command = Hub2EslNetCtrlCommand.typeOf(src.get());
		return 2;
	}

	@Override
	protected int readTail(ByteBuffer src) {
		return 0;
	}

	@Override
	protected int writeHead(ByteBuffer dest) {
		dest.put(protocolVersion);
		dest.put(command == null ? Hub2EslNetCtrlCommand.RESERVED.value() : command.value());
		return 2;
	}

	@Override
	protected int writeTail(ByteBuffer dest) {
		return 0;
	}
	
	public byte getProtocolVersion() {
		return protocolVersion;
	}
	public void setProtocolVersion(byte protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	public Hub2EslNetCtrlCommand getCommand() {
		return command;
	}
	public void setCommand(Hub2EslNetCtrlCommand command) {
		this.command = command;
	}
	
	public int compareTo(Hub2EslNetCtrlFrame o) {
		if(o == null || priority > o.priority) return 1;
		return priority == o.priority ? 0 : -1;
	}
	
	@Override
	public String toString() {
		return "Protocol ver.:" + protocolVersion + ", CMD: " + (command == null ? "none" : command) + ", Data: " + (getPdu() == null ? "none" : Tools.toHexString(getPduArray()));
	}
}
