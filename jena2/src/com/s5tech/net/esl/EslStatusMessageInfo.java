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
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.xml.types.MessageCommand;
import com.s5tech.net.xml.types.StateType;

public class EslStatusMessageInfo extends EslMessageInfo {

	private static final long serialVersionUID = 2866129968064563703L;

	public class CoordinatorsInRange implements Serializable {

		private static final long serialVersionUID = -8806207814524732511L;
		
		public EUI64Address mac;
		public int signalLevel;
		public CoordinatorsInRange(EUI64Address mac, int signalLevel) {
			this.mac = mac;
			this.signalLevel = signalLevel;
		}
	}
	
	private long hashCodeActivePrice;
	private long hashCodePendingPrice;
	private long batteryLevel;
	private long txPower;
	private EUI64Address macAssociatedCoordinator;
	private int temperature;
	private String firmwareVersion;
	private long lifetimeHours;
	private String channel;
	private boolean railDetected;
	private boolean nightMode;
	private StateType state;
	private Vector<CoordinatorsInRange> coordinatorsInRange;
	private int deviceType;

	public EslStatusMessageInfo() {
		super();
	}

	public EslStatusMessageInfo(String msgId, EUI64Address mac,
			Date creationTime) {
		super(false, msgId, mac, MessageCommand.ESLSTATUS, creationTime);
	}

	public EslStatusMessageInfo(String msgId, EUI64Address mac) {
		super(false, msgId, mac, MessageCommand.ESLSTATUS);
	}

	public long getHashCodeActivePrice() {
		return hashCodeActivePrice;
	}

	public void setHashCodeActivePrice(long hashCodeActivePrice) {
		this.hashCodeActivePrice = hashCodeActivePrice;
	}

	public long getHashCodePendingPrice() {
		return hashCodePendingPrice;
	}

	public void setHashCodePendingPrice(long hashCodePendingPrice) {
		this.hashCodePendingPrice = hashCodePendingPrice;
	}

	public long getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(long batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public long getTxPower() {
		return txPower;
	}

	public void setTxPower(long txPower) {
		this.txPower = txPower;
	}

	public EUI64Address getMacAssociatedCoordinator() {
		return macAssociatedCoordinator;
	}

	public void setMacAssociatedCoordinator(EUI64Address macAssociatedCoordinator) {
		this.macAssociatedCoordinator = macAssociatedCoordinator;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public long getLifetimeHours() {
		return lifetimeHours;
	}

	public void setLifetimeHours(long lifetimeHours) {
		this.lifetimeHours = lifetimeHours;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public boolean isRailDetected() {
		return railDetected;
	}

	public void setRailDetected(boolean railDetected) {
		this.railDetected = railDetected;
	}

	public boolean isNightMode() {
		return nightMode;
	}

	public void setNightMode(boolean nightMode) {
		this.nightMode = nightMode;
	}

	public StateType getState() {
		return state;
	}

	public void setState(StateType state) {
		this.state = state;
	}

	public Collection<CoordinatorsInRange> getCoordinatorsInRangeOfEsl() {
		return coordinatorsInRange;
	}

	public void addCoordinatorInRangeOfEsl(EUI64Address mac, int signalLevel) {
		if(coordinatorsInRange == null)
			coordinatorsInRange = new Vector<EslStatusMessageInfo.CoordinatorsInRange>();
		coordinatorsInRange.add(new CoordinatorsInRange(mac, signalLevel));
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	
	
	
}
