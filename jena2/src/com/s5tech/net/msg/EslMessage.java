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
 
package com.s5tech.net.msg;

import java.nio.ByteBuffer;

import com.s5tech.net.esl.Hub2EslNetCtrlFrame;
import com.s5tech.net.type.AbstractSerializableFrame;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.type.SerializeFactory;
import com.s5tech.net.util.Tools;

/**
 * Read and writes messages to and from ESLs
 * @author S5Tech Development Team
 */
public class EslMessage extends AbstractSerializableFrame {

	//public static final int MAX_PDU_LENGTH = 102;
	public static int MAX_PDU_LENGTH = EslTransportFrame.MAX_LENGTH + 9;

	private NetworkAddress networkAddress;
	private EUI64Address macAddress;
	private byte version;
	private boolean macAckRequired;
	private ITransmissionListener<EslMessage> listener;
	
	private int priority;// = Hub2EslNetCtrlFrame.PRI_NORMAL;
	
	public EslMessage() {
		this(null);
	}

	/**
	 * 
	 * @param listener callback invoked on serialisation. This is called synchronously; make it quick
	 */
	public EslMessage(ITransmissionListener<EslMessage> listener) {
		macAckRequired = true;
		priority = Hub2EslNetCtrlFrame.PRI_NORMAL;
		this.listener = listener;		
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean isLowPriority()
	{
		return priority == Hub2EslNetCtrlFrame.PRI_LOW;
	}

	@Override
	protected int getLengthOfHeadAndTail() {
		return 1 + (macAddress == null ? 2 : 8);
	}
	
	@Override
	protected int readHead(ByteBuffer src) {
		byte opts = src.get();
		version  = (byte) (opts&0xF4);
		boolean macAddrUsed = (opts&0x10) > 0;
		macAckRequired = (opts&0x08) > 0;
		if(macAddrUsed) macAddress = SerializeFactory.read(src, EUI64Address.class);
		else networkAddress = SerializeFactory.read(src, NetworkAddress.class);
		return 1 + (macAddress == null ? 2 : 8);
	}
	
	@Override
	protected int readTail(ByteBuffer src) {
		return 0;
	}
	
	@Override
	protected int writeHead(ByteBuffer dest) {
		if(macAddress == null && networkAddress == null)
			networkAddress = new NetworkAddress(0);
		
		byte opts = (byte)version;
		if(macAddress != null)  opts |= 0x10;
		if(macAckRequired && !NetworkAddress.BROADCAST_ADDRESS.equals(networkAddress)) opts |= 0x08;
		dest.put(opts);
		if(macAddress != null) 	macAddress.write(dest);
		else networkAddress.write(dest);
		return 1 + (macAddress == null ? 2 : 8);
	}
	
	@Override
	protected int writeTail(ByteBuffer dest) {
		return 0;
	}

	@Override
	public int getMaxPduLength() {
		return MAX_PDU_LENGTH;
	}

	public void transmitted() {
		if(listener != null) listener.onTransmission(this);
	}
	
	public NetworkAddress getNetworkAddress() {
		return networkAddress;
	}

	public void setNetworkAddress(NetworkAddress networkAddress) {
		this.networkAddress = networkAddress;
	}

	public EUI64Address getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(EUI64Address macAddress) {
		this.macAddress = macAddress;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public boolean isMacAckRequired() {
		return macAckRequired;
	}

	public void setMacAckRequired(boolean macAckRequired) {
		this.macAckRequired = macAckRequired;
	}
	
	@Override
	public String toString() {
		return Tools.toString(this);
	}
	
//	@Override
//	public String headToString() {
//		return "networkAddress=" + networkAddress + ",macAddress=" + macAddress + ",version=" + version + ",macAckRequired=" + macAckRequired;
//	}
}
