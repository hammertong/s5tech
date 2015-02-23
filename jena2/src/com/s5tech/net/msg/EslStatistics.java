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
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.type.ISerializable;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

/**
 * 
 * @author S5Tech Development Team
 * 
 */
public class EslStatistics implements ISerializable {

    public static final int LENGTH = 17;
    
    private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

    protected long receivedTime = 0L;
    
    /*
    typedef struct
	{
	        uint8  deviceType;
	        uint8  nColdReset;
	        uint8  nHotReset;
	        uint8  nPushReset;
	        uint8  nOtaReset;
	        uint8  nAssertReset;
	        uint8  nPushSleep;
	        uint8  nNetSleep;
	        uint8  nScanSleep;
	        uint8  nPowerupSleep;
	        uint16 nStatusRetry;
	        uint16 nScan;
	        uint32 timestamp;
	} stat_send_t;
	*/
    
    protected int  deviceType;
    protected int  nColdReset;
    protected int  nHotReset;
    protected int  nPushReset;
    protected int  nOtaReset;
    protected int  nAssertReset;
    protected int  nPushSleep;
    protected int  nNetSleep;
    protected int  nScanSleep;
    protected int  nPowerupSleep;
    protected int  nStatusRetry;
    protected int  nScan;
    
    /**
     * Esl timestamp in number of seconds from midnight of 1/1/2000
     */
    protected long timestamp;
    
    /**
     * Equivalent time of timestamp processed as java date 
     */
    protected Date eslTime = null;
	    
	public int length() {
		return LENGTH;
	}

	public boolean read(ByteBuffer src, int length) {
	
		try 
		{
			byte b = src.get();	
			if (b != -1) throw new Exception("wrong first byte i ElsStatistics (should be 0xFF");
			
			byte[] counters = new byte[9];
			src.get (counters, 0, 9);
			
			nColdReset 		= counters[0] & 0xff;
		    nHotReset 		= counters[1] & 0xff;
		    nPushReset 		= counters[2] & 0xff;
		    nOtaReset 		= counters[3] & 0xff;
		    nAssertReset 	= counters[4] & 0xff;
		    nPushSleep 		= counters[5] & 0xff;
		    nNetSleep 		= counters[6] & 0xff;
		    nScanSleep 		= counters[7] & 0xff;
		    nPowerupSleep 	= counters[8] & 0xff;	    
		    nStatusRetry 	= ByteBufferUtils.readUInt16(src);
		    nScan 			= ByteBufferUtils.readUInt16(src);
	
			timestamp = ByteBufferUtils.readUInt32(src);
			eslTime = new Date((timestamp * 1000) + 946684800000L);
	
			return true;
		}
		catch(Throwable t) {
			
			log.error("Cannot read EslStatistics - {}", t);
			return false;
			
		}
	}

	public int write(ByteBuffer dest) {

		dest.put((byte) 0xff);
		dest.put((byte) deviceType);
		dest.put((byte) nColdReset);
		dest.put((byte) nHotReset);
		dest.put((byte) nPushReset);
		dest.put((byte) nOtaReset);
		dest.put((byte) nAssertReset);
		dest.put((byte) nPushSleep);
		dest.put((byte) nNetSleep);
		dest.put((byte) nScanSleep);
		dest.put((byte) nPowerupSleep);
		ByteBufferUtils.writeUInt16(nStatusRetry, dest);
		ByteBufferUtils.writeUInt16(nScan, dest);
		
		ByteBufferUtils.writeUInt32(timestamp, dest); 
		
		return LENGTH;
	}
	
	public boolean isChanged(EslStatistics previous) {
		return previous == null || timestamp != previous.timestamp;
	}
	
	@Override
	public String toString() {
		return Tools.toString(this);
	}

	public long getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(long receivedTime) {
		this.receivedTime = receivedTime;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getnColdReset() {
		return nColdReset;
	}

	public void setnColdReset(int nColdReset) {
		this.nColdReset = nColdReset;
	}

	public int getnHotReset() {
		return nHotReset;
	}

	public void setnHotReset(int nHotReset) {
		this.nHotReset = nHotReset;
	}

	public int getnPushReset() {
		return nPushReset;
	}

	public void setnPushReset(int nPushReset) {
		this.nPushReset = nPushReset;
	}

	public int getnOtaReset() {
		return nOtaReset;
	}

	public void setnOtaReset(int nOtaReset) {
		this.nOtaReset = nOtaReset;
	}

	public int getnAssertReset() {
		return nAssertReset;
	}

	public void setnAssertReset(int nAssertReset) {
		this.nAssertReset = nAssertReset;
	}

	public int getnPushSleep() {
		return nPushSleep;
	}

	public void setnPushSleep(int nPushSleep) {
		this.nPushSleep = nPushSleep;
	}

	public int getnNetSleep() {
		return nNetSleep;
	}

	public void setnNetSleep(int nNetSleep) {
		this.nNetSleep = nNetSleep;
	}

	public int getnScanSleep() {
		return nScanSleep;
	}

	public void setnScanSleep(int nScanSleep) {
		this.nScanSleep = nScanSleep;
	}

	public int getnPowerupSleep() {
		return nPowerupSleep;
	}

	public void setnPowerupSleep(int nPowerupSleep) {
		this.nPowerupSleep = nPowerupSleep;
	}

	public int getnStatusRetry() {
		return nStatusRetry;
	}

	public void setnStatusRetry(int nStatusRetry) {
		this.nStatusRetry = nStatusRetry;
	}

	public int getnScan() {
		return nScan;
	}

	public void setnScan(int nScan) {
		this.nScan = nScan;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Date getEslTime() {
		return eslTime;
	}

	public void setEslTime(Date eslTime) {
		this.eslTime = eslTime;
	}

	
			
}
