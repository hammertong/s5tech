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

import java.util.Date;

import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.xml.types.MessageCommand;

public class EslStatisticsMessageInfo extends EslMessageInfo {

	private static final long serialVersionUID = 2866129968064563704L;
	
	private int nColdReset;
	private int nHotReset;
	private int nPushReset;
	private int nOtaReset;
	private int nAssertReset;
	private int nPushSleep;
	private int nNetSleep;
	private int nScanSleep;
	private int nPowerupSleep;
	private int nStatusRetry;
	private int nScan;
	private long time;
	
	private int nJoinWDT;
	
	
	public EslStatisticsMessageInfo() {
		super();
	}

	public EslStatisticsMessageInfo(String msgId, EUI64Address mac,
			Date creationTime) {
		super(false, msgId, mac, MessageCommand.ESLSTATISTICS, creationTime);
	}

	public EslStatisticsMessageInfo(String msgId, EUI64Address mac) {
		super(false, msgId, mac, MessageCommand.ESLSTATISTICS);
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getnJoinWDT() {
		return nJoinWDT;
	}

	public void setnJoinWDT(int nJoinWDT) {
		this.nJoinWDT = nJoinWDT;
	}
	
}
