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

import com.s5tech.net.util.Tools;

/**
 * This class contains information regarding the buffers of the coordinator.<br>
 * BufferInformation class supports NIO Buffering and is a java replacement of the TI Coordinator C struct 
 * 'NumFreeBuffers':
 *
 *	<pre> 	
 *<font color="blue">#define</font> MAX_TIMESLOTS 14
 *	
 *<font color="blue">typedef struct</font>
 *{
 *<font color="blue">  uint16</font> nwkAddr; <font color="green">// == 0x0000 if unassigned</font>
 *<font color="blue">  uint8</font> freeSlotsInDataQueue;
 *} SlotDataQueue_t;
 *		 
 *<font color="blue">typedef struct</font>
 *{		  
 *<font color="blue">  uint8</font> freeSlotsInBroadcastDataQueue;
 *<font color="blue">  uint8</font> freeSlotsInPendingDataCmdQueue;
 *<font color="blue">  uint8</font> freeSlotsInPendingStatusCmdQueue;
 *  SlotDataQueue_t SlotDataQueue[MAX_TIMESLOTS];
 *} NCL_NumOfFreeBuffers_t;
 * </pre>
 **/ 
public class BufferInformation implements ISerializable {

	/** number of timeslots for single mac-address destination */
	public static final int NUMBER_OF_TIMESLOTS = 14;
	
	/** override the AbstractByteBuffer LENGTH */
	public static final int LENGTH = 3 + NUMBER_OF_TIMESLOTS * TimeslotBuffer.LENGTH;
	 
	private int freeSlotsInBroadcastDataQueue = 0;
	private int freeSlotsInPendingDataCmdQueue = 0;
	private int freeSlotsInPendingStatusCmdQueue = 0;
	private TimeslotBuffer[] buffers;
	
	public BufferInformation() {
		buffers = new TimeslotBuffer[NUMBER_OF_TIMESLOTS];
	}
	
	public int length() {
		return LENGTH;
	}

	public boolean read(ByteBuffer src, int length) {
		if(length < LENGTH) return false;
		freeSlotsInBroadcastDataQueue = Tools.uByteToInt(src.get());
		freeSlotsInPendingDataCmdQueue = Tools.uByteToInt(src.get());
		freeSlotsInPendingStatusCmdQueue = Tools.uByteToInt(src.get());
		for(int i=0; i < NUMBER_OF_TIMESLOTS; i++) {
			buffers[i] = new TimeslotBuffer();
			buffers[i].read(src, src.remaining());
		}
		return true;
	}

	public int write(ByteBuffer dest) {
		return 0;
	}

	public int getFreeSlotsInBroadcastDataQueue() {
		return freeSlotsInBroadcastDataQueue;
	}

	public void setFreeSlotsInBroadcastDataQueue(int freeSlotsInBroadcastDataQueue) {
		this.freeSlotsInBroadcastDataQueue = freeSlotsInBroadcastDataQueue;
	}

	public int getFreeSlotsInPendingDataCmdQueue() {
		return freeSlotsInPendingDataCmdQueue;
	}

	public void setFreeSlotsInPendingDataCmdQueue(int freeSlotsInPendingDataCmdQueue) {
		this.freeSlotsInPendingDataCmdQueue = freeSlotsInPendingDataCmdQueue;
	}

	public int getFreeSlotsInPendingStatusCmdQueue() {
		return freeSlotsInPendingStatusCmdQueue;
	}

	public void setFreeSlotsInPendingStatusCmdQueue(
			int freeSlotsInPendingStatusCmdQueue) {
		this.freeSlotsInPendingStatusCmdQueue = freeSlotsInPendingStatusCmdQueue;
	}

	public TimeslotBuffer[] getBuffers() {
		return buffers;
	}
	
	/**
	 * override toString to 'easy understand' in log information 
	 */
	@Override
	public String toString() {
		return "broadcast: " + freeSlotsInBroadcastDataQueue + 
					", data cmd: " + freeSlotsInPendingDataCmdQueue +
					", status req: " + freeSlotsInPendingStatusCmdQueue +
					". Timeslots: " + Tools.arrayToString(buffers, "|"); 
	}
	
	/**
	 * clone this buffer
	 * @param binfo
	 */
	public void copyFrom(BufferInformation binfo) {		
		freeSlotsInBroadcastDataQueue = binfo.getFreeSlotsInBroadcastDataQueue();
		freeSlotsInPendingDataCmdQueue = binfo.getFreeSlotsInPendingDataCmdQueue();
		freeSlotsInPendingStatusCmdQueue = binfo.getFreeSlotsInPendingStatusCmdQueue();
		if (buffers == null) buffers = new TimeslotBuffer[NUMBER_OF_TIMESLOTS];
		TimeslotBuffer [] tb = binfo.getBuffers();
		for (int i = 0; i < NUMBER_OF_TIMESLOTS; i ++) {
			if (buffers[i] == null) buffers[i] 
					= new TimeslotBuffer(tb[i].getAddress(), tb[i].getFreeSlotsInDataQueue());
		}
		
	}
	
}
