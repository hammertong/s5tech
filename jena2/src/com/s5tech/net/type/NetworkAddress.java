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
import java.nio.ByteBuffer;

import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.Tools;

/**
 * This class is able to represent a network (short) address for an ESL.
 * It is able to serialize itself using the {@link ISerializable} interface.
 * @author S5Tech Development Team
 *
 */
public class NetworkAddress implements Serializable, ISerializable, Comparable<NetworkAddress> {

	private static final long serialVersionUID = -7126867983349220520L;
	
	public static final NetworkAddress BROADCAST_ADDRESS = new NetworkAddress(0xFFFF);
	
	public static final int LENGTH = 2;
	private int address;
	
	public static final int FIRST_NWK_ADDRESS = 0x0001;
	public static final int LAST_NWK_ADDRESS = 0xFFFE;
	public static final int RESERVED1_ADDRESS = 0xBBBA;

	/**
	 * Must exists to be able to read from ByteBuffer using {@link SerializeFactory}
	 */
	public NetworkAddress() {}
	
	public NetworkAddress(byte[] address) {
		this(address,0);
	}

	public NetworkAddress(byte[] address, int offset) {
		this.address = address == null ? 0 : Tools.getIntFromBytes(address, offset, LENGTH, true);
	}
	
	public NetworkAddress(int value) {
		address = value;
	}

	public byte[] getAddress() {
		return Tools.intToBytes(address, LENGTH, true);
	}
	
	public int length() {
		return LENGTH;
	}

	public int compareTo(NetworkAddress o) {
		if(o == null) return 1;
		int res = address-o.address;
		if(res == 0) return 0;
		return res > 0 ? 1 : -1;
	}
	
	public int intValue() {
		return address;
	}

	public boolean read(ByteBuffer src, int length) {
		address = ByteBufferUtils.readUInt16(src);
		return true;
	}

	public int write(ByteBuffer dest) {
		ByteBufferUtils.writeUInt16(address, dest);
		return LENGTH;
	}

	public boolean isBroadcast() {
		return BROADCAST_ADDRESS.address == address;
	}
	
	@Override
	public int hashCode() {
		return address;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof NetworkAddress && address == NetworkAddress.class.cast(obj).address;
	}

	@Override
	public String toString() {
		return Tools.toHexString(getAddress());
	}
	
	public void increment() 
	{
		address ++;
	}
}
