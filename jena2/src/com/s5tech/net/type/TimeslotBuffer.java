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

import java.nio.ByteBuffer;

import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.Tools;

/**
 * Contains information about a time slot buffer in the coordinator
 * @author S5Tech Development Team
 *
 */
public class TimeslotBuffer implements ISerializable {

	public static final int LENGTH = 3;
	
	private NetworkAddress address = null;
	private int freeSlotsInDataQueue = 0;

	public TimeslotBuffer() {
	}

	public TimeslotBuffer(NetworkAddress address, int freeSlotsInDataQueue) {
		this.address = address;
		this.freeSlotsInDataQueue = freeSlotsInDataQueue;
	}

	/**
	 * Check if the buffer is assigned or not.
	 * @return true if no or empty address is for the timeslot
	 */
	public boolean isUnassigned() {
		return address == null || address.intValue() == 0;
	}
	
	public NetworkAddress getAddress() {
		return address;
	}
	
	public int getFreeSlotsInDataQueue() {
		return freeSlotsInDataQueue;
	}
	
	public void setFreeSlotsInDataQueue(int n) {
		freeSlotsInDataQueue = (n < 0 ? 0 : (n > 2 ? 2 : n));
	}
	
	public int length() {
		return LENGTH;
	}

	public boolean read(ByteBuffer src, int length) {
		if(src == null || src.remaining() < LENGTH) return false;
		address = SerializeFactory.read(src, NetworkAddress.class);
		freeSlotsInDataQueue = Tools.uByteToInt(src.get());
		return true;
	}

	public int write(ByteBuffer dest) {
		if(address != null) address.write(dest);
		else ByteBufferUtils.writeUInt16(0, dest);
		dest.put((byte)freeSlotsInDataQueue);
		return LENGTH;
	}
	
	@Override
	public String toString() {
		return (address == null ? "0000" : address.toString()) + ":" + freeSlotsInDataQueue;
	}

	public void setAddress(NetworkAddress addr) {
		address = addr;
	}

	public void removeFreeSlotsInDataQueue(int sent) {
		freeSlotsInDataQueue -= sent;
		if(freeSlotsInDataQueue < 0) freeSlotsInDataQueue = 0;
	}

}
