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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.entity.FirmwareVersion;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.ISerializable;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

/**
 * 
 * @author S5Tech Development Team
 * 
 */
public class EslStatus implements ISerializable, Serializable, IEslPriceHashcodeInfo {

	private static final long serialVersionUID = -4368749679027978096L;

	public static final int MIN_LENGTH = 18;
    
    private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

    private long receivedTime = 0L;
    
    private int deviceType;
	private FirmwareVersion firmwareVersion;
	private FirmwareVersion bootloaderFirmwareVersion;
	private int batteryLevel;
	private int linkQuality;
	private int temperature;
	private int lifetimeHours;
	private long hashCodeActivePrice;
	private long hashCodePendingPrice;
	private boolean railAlarmOn;
	private boolean nightModeOn;
	private Map<EUI64Address,Integer> alternativeCoordsInRange;
		
	private EUI64Address esl = null; 
	
	public EUI64Address getEsl() {
		return esl;
	}

	public void setEsl(EUI64Address esl) {
		this.esl = esl;
	}

	public int length() {
		return MIN_LENGTH + (alternativeCoordsInRange == null ? 0 : alternativeCoordsInRange.size());
	}

	public boolean read(ByteBuffer src, int length) {
		
		try {
		
			byte b;		
			
			b = src.get();
			
			deviceType = b & 0xff; //signed to unsigned
			deviceType &= ~0x80;  
			deviceType &= ~0x40;
				
			firmwareVersion = new FirmwareVersion();
			firmwareVersion.read(src);
				
			if(firmwareVersion.compareTo(new FirmwareVersion(3, 0, 7, true, 0)) == 0) {
				hashCodeActivePrice = ByteBufferUtils.readUInt32(src);
				hashCodePendingPrice = ByteBufferUtils.readUInt32(src);
				if (alternativeCoordsInRange == null) 
					alternativeCoordsInRange = new HashMap<EUI64Address, Integer>();
				return true;
			}
			
			bootloaderFirmwareVersion = new FirmwareVersion();
			if(firmwareVersion.compareTo(new FirmwareVersion(1, 2, 7, true, 3)) >= 0) {
				bootloaderFirmwareVersion.read(src);
			}
			
			if(firmwareVersion.compareTo(new FirmwareVersion(2, 0, 0, true, 3)) >= 0) {
				esl = new EUI64Address(src);
			}
				
			batteryLevel = Tools.uByteToInt(src.get());
			linkQuality = Tools.uByteToInt(src.get());
			temperature = Tools.uByteToInt(src.get());
			
			lifetimeHours = ByteBufferUtils.readUInt16(src);
			hashCodeActivePrice = ByteBufferUtils.readUInt32(src);
			hashCodePendingPrice = ByteBufferUtils.readUInt32(src);
			
			byte tmp  = src.get();
			
			railAlarmOn = (tmp & 0x80) != 0;
			nightModeOn = (tmp & 0x40) != 0;
			tmp &= 0x0F;
				
			alternativeCoordsInRange = new HashMap<EUI64Address, Integer>();
			
			while(tmp > 0) {
				//Check that the data for the list is actually present
				if(src.remaining() < EUI64Address.LENGTH + 1) {
					log.warn("message indicates " 
							+ (tmp + alternativeCoordsInRange.size()) 
							+ " alternative coordinators in range, but only contains data for " 
							+ alternativeCoordsInRange.size());
					break;
				}
				alternativeCoordsInRange.put(new EUI64Address(src), Tools.uByteToInt(src.get()));
				tmp--;
			}
			
			return true;
			
		}
		catch (Throwable t) 
		{
			log.error("Cannot read EslStatus - {}", t);
			return false;
		}
		
	}

	public int write(ByteBuffer dest) {

		int len = MIN_LENGTH;
		
		dest.put((byte) deviceType);
		getFirmwareVersion().write(dest);
		dest.put((byte) batteryLevel);
		dest.put((byte) linkQuality);
		dest.put((byte) temperature);
		ByteBufferUtils.writeUInt16(lifetimeHours, dest);
		ByteBufferUtils.writeUInt32(hashCodeActivePrice, dest);
		ByteBufferUtils.writeUInt32(hashCodePendingPrice, dest);
		
		byte tmp = 0;
		if(railAlarmOn) tmp |= 0x80;
		if(nightModeOn) tmp |= 0x40;
		dest.put(tmp);
						
		for (EUI64Address a : alternativeCoordsInRange.keySet())
		{
			a.write(dest);
			dest.put((byte) (alternativeCoordsInRange.get(a).intValue() & 0xff));
			len += 9;
		}
		
		return len;
	}
	
	public int getBatteryLevel() {
		return batteryLevel;
	}

	public int getLinkQuality() {
		return linkQuality;
	}

	public int getTemperature() {
		return temperature;
	}

	public FirmwareVersion getFirmwareVersion() {
		if(firmwareVersion == null) firmwareVersion = new FirmwareVersion(0, 0, 0, true, 0);
		return firmwareVersion;
	}

	public FirmwareVersion getBootloaderFirmwareVersion() {
		if(bootloaderFirmwareVersion == null) bootloaderFirmwareVersion = new FirmwareVersion(0, 0, 0, true, 0);
		return bootloaderFirmwareVersion;
	}
	
	public int getLifetimeHours() {
		return lifetimeHours;
	}

	public long getHashCodeActivePrice() {
		return hashCodeActivePrice;
	}

	public long getHashCodePendingPrice() {
		return hashCodePendingPrice;
	}

	public boolean isRailAlarmOn() {
		return railAlarmOn;
	}

	public boolean isNightModeOn() {
		return nightModeOn;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public void setLinkQuality(int linkQuality) {
		this.linkQuality = linkQuality;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public void setFirmwareVersion(FirmwareVersion firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	
	public void setBootloaderFirmwareVersion(
			FirmwareVersion bootloaderFirmwareVersion) {
		this.bootloaderFirmwareVersion = bootloaderFirmwareVersion;
	}

	public void setRemainingHours(int remainingHours) {
		this.lifetimeHours = remainingHours;
	}

	public void setHashCodeActivePrice(long hashCodeActivePrice) {
		this.hashCodeActivePrice = hashCodeActivePrice;
	}

	public void setHashCodePendingPrice(long hashCodePendingPrice) {
		this.hashCodePendingPrice = hashCodePendingPrice;
	}

	public void setRailAlarmOn(boolean railAlarmOn) {
		this.railAlarmOn = railAlarmOn;
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
	
	public void setAlternativeCoordinatorsInRange(
			Map<EUI64Address, Integer> alternativeCoordsInRange) {
		this.alternativeCoordsInRange = alternativeCoordsInRange;
	}

	/**
	 * Returns an unmodifiable map containing the alternative hubs in range as keys, and the link quality as value.
	 * @return
	 */
	public Map<EUI64Address, Integer> getAlternativeCoordinatorsInRange() {
		return Collections.unmodifiableMap(alternativeCoordsInRange);
	}

	/**
	 * Returns true if one of these values differ between this and the previous status.
	 * <ol>
	 * <li>firmware version</li>
	 * <li>rail alarm on</li>
	 * <li>night mode on</li>
	 * <li>battery level</li>
	 * <li>hash code for active price</li>
	 * <li>hash code for pending price</li>
	 * </ol>
	 */
	public boolean isChanged(EslStatus previous) {
		return previous == null ||
				firmwareVersion != previous.firmwareVersion ||
				hashCodeActivePrice != previous.hashCodeActivePrice ||
				hashCodePendingPrice != previous.hashCodePendingPrice ||
				railAlarmOn != previous.railAlarmOn ||
				nightModeOn != previous.nightModeOn ||
				batteryLevel != previous.batteryLevel;
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
