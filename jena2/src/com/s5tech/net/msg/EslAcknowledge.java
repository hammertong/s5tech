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

import java.io.Serializable;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.type.ISerializable;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;


public class EslAcknowledge implements ISerializable, Serializable, IEslPriceHashcodeInfo {

	private static final long serialVersionUID = -4368749679027978899L;

	public static final int MIN_LENGTH = 9;
    
    private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
    
    private long receivedTime = 0L;
    
    private int deviceType;
    private boolean nightModeOn = false;
	private long hashCodeActivePrice;
	private long hashCodePendingPrice;
	
	public int length() {
		return MIN_LENGTH;
	}

	public boolean read(ByteBuffer src, int length) {
		
		try 
		{			
			byte b = src.get();	
					
			deviceType = b & 0xff; //signed to unsigned
			deviceType &= ~0x80;  
			deviceType &= ~0x40;
				
			nightModeOn = ((b & 0x40) != 0);
				
			hashCodeActivePrice = ByteBufferUtils.readUInt32(src);
			hashCodePendingPrice = ByteBufferUtils.readUInt32(src);
			return true;
		}
		catch (Throwable t) {
			log.error("Cannot read ESLAcknowledge: {}", t);
			return false;
		}
	}

	public int write(ByteBuffer dest) {

		byte b = (byte)(deviceType & 0xff);
		b |= 0x80;
		if (nightModeOn) b |= 0x40;
		
		dest.put((byte) b);		
		ByteBufferUtils.writeUInt32(hashCodeActivePrice, dest);
		ByteBufferUtils.writeUInt32(hashCodePendingPrice, dest);
		
		return MIN_LENGTH;
	}
	
	public long getHashCodeActivePrice() {
		return hashCodeActivePrice;
	}

	public long getHashCodePendingPrice() {
		return hashCodePendingPrice;
	}
	
	public boolean isNightModeOn() {
		return nightModeOn;
	}
	
	public void setHashCodeActivePrice(long hashCodeActivePrice) {
		this.hashCodeActivePrice = hashCodeActivePrice;
	}

	public void setHashCodePendingPrice(long hashCodePendingPrice) {
		this.hashCodePendingPrice = hashCodePendingPrice;
	}

	public void setNightModeOn(boolean nightModeOn) {
		this.nightModeOn = nightModeOn;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	
	public int getDeviceType() {
		return deviceType;
	}

	public boolean isChanged(EslAcknowledge previous) {
		return previous == null ||
				hashCodeActivePrice != previous.hashCodeActivePrice ||
				hashCodePendingPrice != previous.hashCodePendingPrice ||
				nightModeOn != previous.nightModeOn;
	}
	
	@Override	
	public String toString() {
		return Tools.toString(this);
	}
	
	public long getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(long rtime) {
		this.receivedTime = rtime;
	}

}
