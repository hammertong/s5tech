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
 
package com.s5tech.net.eslnet;

import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.entity.FirmwareVersion;
import com.s5tech.net.firmware.FirmwareInfo;
import com.s5tech.net.firmware.FirmwareLibrary;
import com.s5tech.net.msg.EslApplicationCommand;
import com.s5tech.net.msg.EslApplicationFrame;
import com.s5tech.net.msg.EslMessage;
import com.s5tech.net.msg.EslTransportFrame;
import com.s5tech.net.msg.ITransmissionListener;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

/**
 * This class is responsible handling transmission of firmware update messages.
 * 
 * @author S5Tech Development Team
 *
 */
public class EslFirmwareUpdater {

	private AtomicBoolean updating;
	private IEslNetwork network;
	private FirmwareLibrary library;
	private MyTransmissionListener transmissionListener;
	private ActiveQueue<Map.Entry<Integer,FirmwareVersion>> firmwareUpdateQueue;
	private Map<Integer,Integer> reservedSequenceIdsForDeviceTypes;
 	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
 	private int count = 0; 
 	private int total = 3;
 	
 	private FirmwareInfo updatingFw = null;
 	private Date lastOpTime = null;
 	private boolean upgradeOk = false;
 	
	public EslFirmwareUpdater(IEslNetwork network) {
		
		try {
			total = Integer.parseInt(System.getProperty("firmware.transmission", "3"));
 		}
 		catch (Throwable ignored) { total = 3; }
 						
		updating = new AtomicBoolean(false);

		reservedSequenceIdsForDeviceTypes = new HashMap<Integer, Integer>();
		
		library = FirmwareLibrary.instance();
		this.network = network;

		firmwareUpdateQueue = 
			new ActiveQueue<Map.Entry<Integer,FirmwareVersion>>(new IActiveQueueSubscriber<Map.Entry<Integer,FirmwareVersion>>() 
				{
					public void elementFromQueue(Entry<Integer, FirmwareVersion> element) 
					{
						int sentPackets = 0;
						upgradeOk = false;
						lastOpTime = new Date();
						count = 1;
						while(count <= total) {
							if (update(element.getKey(), element.getValue())) {
								log.info("Firmware upgrade transmission " + count + " of " + total);
								// Wait until all messages are sent
								log.trace("Waiting for transmission on all messages"); // TODO should this have a max value? If so, what about cleanup in lower outbound queues?
								if(transmissionListener != null) {
									while(transmissionListener != null && !transmissionListener.isDone()) {
										if((transmissionListener.getCount() > (sentPackets + 10)) || 
											(sentPackets == 0 && transmissionListener.getCount() > 0)) {
											sentPackets = transmissionListener.getCount();
											//updateRuntimeInfo("transmitting");
										}
										Tools.doWait("", 5000);
									}
								}
								if (count >= total) break;
								else count++;
								
							} else {
								log.trace("Update procedure failed!");
								break;
							}
						}
						log.info("Firmware transmitted " + count + " of " + total + " times");
						//status.sentPackets = transmissionListener.count;
						if(count == total) {
							log.trace("Wait one minute to allow reception of the last transmitted messages on all ESLs");
							// Wait one minute to allow the ESLs to receive the last messages
							Tools.doWait("", 60000);
							upgradeOk = true;
						} else {
							upgradeOk = false;
						}
						
						log.info("Update procedure completed (" + (upgradeOk ? "SUCCESS" : "FAILURE" ) + "), re-enabling firmware version comparision");
						
						lastOpTime = new Date();
						
						log.trace("Update procedure complete; reenabling firmware version comparision");
						updating.set(false);
						
					}
					
				}, "ESL Firmware updater");
		
	}

	private boolean update(Integer deviceType, FirmwareVersion firmwareVersion) {
		
		if(firmwareVersion == null || library.getFirmwareForDeviceType(deviceType) == null) return false;
		
		if (network == null) {
			log.error("No network for transmission! Skipping update procedure");
			return false;
		}

		FirmwareInfo firmwareInfo = library.getFirmwareForDeviceType(deviceType);
		
		ByteBuffer buffer = FirmwareLibrary.instance().loadFirmware(firmwareInfo);

		log.info("Starting update of firmware version " + firmwareInfo.getVersion() + " for device type " + firmwareInfo.getDeviceType());

		if(buffer == null) {
			log.warn("Error loading firmware file for device type " + firmwareInfo.getDeviceType() + ", firmware version " + firmwareInfo.getVersion() + "!!");
			return false;
		}
		
		updatingFw = firmwareInfo;
		
		//
		// TODO: solo in network controller dovrebbe conoscere 
		//		 la creazione degli EslApplicationFrame e la relativa frammentazione 
		//       per questioni di tempo lo si fa qui in via eccezionale, per la comodita' 
		//       di usare il transmission listener che deve conoscere in anticipo 
		//       il numero di pacchetti creati
		//
		//		 La seguente parte di codice dovrebbe essere contenuta nel netowrk 
		//		 controller
		//

		EslApplicationFrame appFrame = new EslApplicationFrame();
		appFrame.setCommand(EslApplicationCommand.UPDATE_FIRMWARE);
		appFrame.setPdu(buffer);

		int seqNum;
		final List<EslTransportFrame> list;		
				
		if (reservedSequenceIdsForDeviceTypes.containsKey(deviceType)) 
		{
			seqNum = reservedSequenceIdsForDeviceTypes.get(deviceType);
			log.info("Using prevously reserved sequence number " + seqNum + " for device type " + deviceType);
		} 
		else 
		{
			if (EslTransportFrame.tryReserveSequenceNumber(deviceType)) 
			{
				seqNum = deviceType;
			}
			else 
			{
				seqNum = EslTransportFrame.acquireAndReserveSequenceNumber();
				if(seqNum < 0) return false;
			}
			reservedSequenceIdsForDeviceTypes.put(deviceType, seqNum);
			log.info("Reserving sequence number " + seqNum + " for device type " + deviceType);
		}

		list = EslTransportFrame.split(appFrame, seqNum);
		
		if(list == null || list.size() <= 0) {
			log.warn("Unable to split the firmware data, aborting update");
			return false;
		}
		
		log.debug("Firmware split into {} transport messages", list.size());

		transmissionListener = new MyTransmissionListener(list.size());
		
//		boolean res = true;		
//		for (EslTransportFrame tf : list) {
//			EslSecurityFrame sf = new EslSecurityFrame();
//			sf.setPdu(tf);
//			EslMessage em = new EslMessage(transmissionListener);
//			em.setNetworkAddress(NetworkAddress.BROADCAST_ADDRESS);
//			em.setPdu(sf);
//			res &= network.sendEslMessage(em);
//		}
//		return res;
		
		return network.sendEslApplicationMessage(list, transmissionListener, NetworkAddress.BROADCAST_ADDRESS);
				
	}

	synchronized public void eval(int deviceType, FirmwareVersion firmwareVersion) {
		
		if(deviceType <= 0 || firmwareVersion == null) return;
		
		Set<Integer> knownDeviceTypes = library.getKnownDeviceTypes();

		if(knownDeviceTypes.size() == 0 || !knownDeviceTypes.contains(deviceType)) {
			//log.trace("No suitable firmware in firmware library");
			return;
		}

		if(updating.get()) {
			log.trace("Update already in progress; skipping evaluation");
			return;
		}
		
		FirmwareVersion libFw = library.getFirmwareVersionForDeviceType(deviceType);
		if(libFw == null) {
			log.trace("No firmware found for device type {}.", deviceType);
			return;
		}

		if(libFw.compareTo(firmwareVersion) <= 0) {
			log.trace("No new firmware found for device type {}.", deviceType);
			return;
		}
		
		log.info("Updating device type " + deviceType + " from " + firmwareVersion + " to " + libFw);
		startUpdate (deviceType, firmwareVersion);
		
	}

	
	public void startUpdate(int deviceType, FirmwareVersion firmwareVersion)
	{	
		if (updating.get()) {
			log.warn("firmware upgrade already in progress... transmission aborted!");
			return;
		}
		updating.set(true);			
		firmwareUpdateQueue.add(new AbstractMap.SimpleEntry<Integer,FirmwareVersion>(deviceType, firmwareVersion));
	}
	
	private class MyTransmissionListener implements ITransmissionListener<EslMessage> {

		private int size;
		private int _count;
		
		public MyTransmissionListener(int size) {
			this.size = size;
		}

		public void onTransmission(EslMessage msg) {
			_count++;
			if(_count >= size) {
				log.info("All messages sent!");
			}
		}
		
		public boolean isDone() {
			return _count >= size;
		}
		
		public int getCount()
		{
			return _count;
		}
		
		public int getSize()
		{
			return size;
		}
	}
		
	public int getUpdatingDeviceType()
	{
		return (updatingFw == null ? 0: updatingFw.getDeviceType());
	}
	
	public String getVersion()
	{
		return (updatingFw == null ? "n/a" : updatingFw.getVersion().toString());
	}
	
	public int getCount()
	{
		return count;
	}
	
	public int getTotal()
	{
		return total;
	}
	
	public int getTransmissionSize()
	{
		return transmissionListener == null ? 0 : transmissionListener.getSize();
	}
	
	public int getTransmissionCount()
	{
		return transmissionListener == null ? 0 : transmissionListener.getCount();
	}
	
	public boolean isUpdating()
	{
		return updating.get();
	}
	
	public boolean isUpgradeOk()
	{
		return upgradeOk;
	}
	
	public Date getLastOperationTime()
	{
		return lastOpTime;
	}
 	 	
	public void stop()
	{
		firmwareUpdateQueue.kill();
	}
}
