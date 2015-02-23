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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.type.AbstractSerializableFrame;
import com.s5tech.net.type.ISerializable;
import com.s5tech.net.type.SerializeFactory;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.ISystemKeys;

public class EslTransportFrame extends AbstractSerializableFrame implements Comparable<EslTransportFrame> {

	public static int MAX_LENGTH = 93;
	public static int MAX_FULL_PDU_LENGTH = 80;
	public static int MAX_PDU_FRAGMENT_LENGTH = 76;
	
	public static final int MIN_SEQUENCE_NUMBER = 1;
	public static final int MAX_SEQUENCE_NUMBER = 15;
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	private byte protocolVersion;
	private int totalNumberOfPackages;
	private int currentPackageNumber;
	private int sequenceNumber;
	private static int nextSequenceNumber;
	private static final Set<Integer> reservedSequenceIds = new HashSet<Integer>();

    static {
    	
    	try {
    		
    		if (System.getProperty("EslTransportFrame.MAX_PDU_FRAGMENT_LENGTH", "").length() > 0) {
    		
    			int n = Integer.parseInt(System.getProperty("EslTransportFrame.MAX_PDU_FRAGMENT_LENGTH"));
    			
    			if (n > 0 && n != 76)
    			{
	    			MAX_PDU_FRAGMENT_LENGTH = n;
	    			MAX_FULL_PDU_LENGTH = MAX_PDU_FRAGMENT_LENGTH + 4;
	        		MAX_LENGTH = MAX_PDU_FRAGMENT_LENGTH + 4 + 13;	        		 
	        		
	        		log.info("using custom MAX_PDU_FRAGMENT_LENGTH: " + MAX_FULL_PDU_LENGTH);
	        		log.info("using custom MAX_FULL_PDU_LENGTH: " + MAX_PDU_FRAGMENT_LENGTH);
	        		log.info("using custom MAX_LENGTH: " + MAX_LENGTH);	        		
    			}
        		
    		}
    	}
    	catch (Throwable t) {
    		
    		log.error("error setting custom MAX PDU FRAGMENT LENGTH ... {}", t);
    						
    	}
    		
    	    
    	
    }
	
	synchronized private static boolean isValidSequenceNumber(int number) {
		return number >= MIN_SEQUENCE_NUMBER && number <= MAX_SEQUENCE_NUMBER;
	}

	synchronized static int acquireNextSequenceNumber() {
		if(nextSequenceNumber++ >= MAX_SEQUENCE_NUMBER) nextSequenceNumber = MIN_SEQUENCE_NUMBER;
		if(reservedSequenceIds.contains(nextSequenceNumber))
			nextSequenceNumber = acquireNextSequenceNumber();
		return nextSequenceNumber;
	}

	/**
	 * Attempts to acquire and reserver the next sequence number
	 * @return the reserved sequence number. -1 on failure
	 */
	synchronized public static int acquireAndReserveSequenceNumber() {
		int num = acquireNextSequenceNumber();
		return tryReserveSequenceNumber(num) ? num : -1;
	}
	
	/**
	 * only allows reservation of {@value #MAX_SEQUENCE_NUMBER}-{@value #MIN_SEQUENCE_NUMBER}-1 sequence numbers
	 * @param value
	 * @return
	 */
	synchronized public static boolean tryReserveSequenceNumber(int value) {
		if(reservedSequenceIds.contains(value) || 
				value < MIN_SEQUENCE_NUMBER || 
				value > MAX_SEQUENCE_NUMBER || 
				(reservedSequenceIds.size() > (MAX_SEQUENCE_NUMBER-MIN_SEQUENCE_NUMBER-1))) return false;
		return reservedSequenceIds.add(value);
	}

	synchronized public static boolean releaseSequenceNumber(int value) {
		return reservedSequenceIds.remove(value);
	}
	
	public EslTransportFrame() {
		this(acquireNextSequenceNumber(), 1,1,null,0);
	}

	public EslTransportFrame(int sequenceNumber, int totalNumberOfPackages, int currentPackageNumber, ByteBuffer data, int length) {
		this.sequenceNumber = sequenceNumber;
		this.totalNumberOfPackages = totalNumberOfPackages;
		this.currentPackageNumber = currentPackageNumber;
		setPdu(data);
	}

	@Override
	protected int getLengthOfHeadAndTail() {
		return 1 + (isFragmentationEnabled() ? 4 : 0);
	}
	
	public byte getProtocolVersion() {
		return protocolVersion;
	}

	public boolean isFragmentationEnabled() {
		return totalNumberOfPackages > 1;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getTotalNumberOfPackages() {
		return totalNumberOfPackages;
	}

	public int getCurrentPackageNumber() {
		return currentPackageNumber;
	}

	@Override
	protected int readHead(ByteBuffer src) {
		byte frameControl  = src.get();
		
		if((frameControl&0x10) > 0) { // Check for fragmentation
			totalNumberOfPackages = ByteBufferUtils.readUInt16(src);
			currentPackageNumber = ByteBufferUtils.readUInt16(src);
		}
		sequenceNumber = frameControl & 0x0F;
		return 1 + (isFragmentationEnabled() ? 4 : 0);
	}
	
	@Override
	protected int readTail(ByteBuffer src) {
		return 0;
	}

	@Override
	protected int writeHead(ByteBuffer dest) {
		byte frameControl = isFragmentationEnabled() ? (byte)0x10 : 0;
		frameControl |= (sequenceNumber & 0x0F);
		dest.put(frameControl);
		if(isFragmentationEnabled()) {
			dest.putShort((short) totalNumberOfPackages);
			dest.putShort((short) currentPackageNumber);
		}
		return 1 + (isFragmentationEnabled() ? 4 : 0);
	}
	
	@Override
	protected int writeTail(ByteBuffer dest) {
		return 0;
	}
	
	@Override
	public int getMaxPduLength() {
		return MAX_LENGTH - (isFragmentationEnabled() ? 5 : 1);
	}

// The max pdu sizes were changed on 2010-08-26: 88 -> 76 , 92 -> 80
	
	public int compareTo(EslTransportFrame o) {
		if(o == null) return 1;
		if(o == null || sequenceNumber > o.sequenceNumber || currentPackageNumber > o.currentPackageNumber) return 1;
		return sequenceNumber < o.sequenceNumber || currentPackageNumber < o.currentPackageNumber ? -1 : 0;
	}

	public static List<EslTransportFrame> split(ISerializable data) {
		return split(data, MIN_SEQUENCE_NUMBER-1);
	}

	public static List<EslTransportFrame> split(ISerializable data, int sequenceNumber) {
		return split(SerializeFactory.writeToNewBuffer(data), sequenceNumber);
	}

	/**
	 * Split specific data into a number of transport frames
	 * @param data
	 * @return
	 */
	public static List<EslTransportFrame> split(ByteBuffer data) {
		return split(data, MIN_SEQUENCE_NUMBER-1);
	}
	
	/**
	 * 
	 * @param data
	 * @param sequenceNumber the sequence number of the messages. This value is the same for all created frames. If less than 1 or more than 15, the next available is used.
	 * @return
	 */
	public static List<EslTransportFrame> split(ByteBuffer data, int sequenceNumber) {
		List<EslTransportFrame> res = new ArrayList<EslTransportFrame>();
		if(!isValidSequenceNumber(sequenceNumber)) sequenceNumber = acquireNextSequenceNumber();
		if(data.remaining() <= MAX_FULL_PDU_LENGTH) {
			res.add(new EslTransportFrame(sequenceNumber, 1, 1, data, data.remaining()));
		} else {
			int total = data.remaining()/MAX_PDU_FRAGMENT_LENGTH+1;
			//fix... 
        	if (data.remaining() % MAX_PDU_FRAGMENT_LENGTH == 0) total --;
        	
			int count = 1;
			if(log.isTraceEnabled()) {
				log.trace("Creating " + total + " transport messages from data");
			}
			while(data.remaining() > MAX_PDU_FRAGMENT_LENGTH) {
				res.add(new EslTransportFrame(sequenceNumber, total, count++, ByteBufferUtils.create(data, MAX_PDU_FRAGMENT_LENGTH), MAX_PDU_FRAGMENT_LENGTH));
			}
			int rem = data.remaining();
			if(rem > 0)
				res.add(new EslTransportFrame(sequenceNumber, total, count, ByteBufferUtils.create(data,rem), rem));
		}
		return res;
	}
	
}
