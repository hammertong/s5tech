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

import java.util.List;
import java.util.Set;

import com.s5tech.net.entity.FirmwareVersion;
import com.s5tech.net.esl.EslCommand;
import com.s5tech.net.esl.EslSignalInfo;
import com.s5tech.net.esl.IApplicationMessageListener;
import com.s5tech.net.msg.EslMessage;
import com.s5tech.net.msg.EslProxy;
import com.s5tech.net.msg.EslTransportFrame;
import com.s5tech.net.msg.IEslMessageTransmitter;
import com.s5tech.net.msg.ITransmissionListener;
import com.s5tech.net.type.Channel;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.ISerializable;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.util.IActiveQueueSubscriber;

/**
 * 
 * Interface for a generic ESL network
 * @author S5Tech Development Team
 *
 */
public interface IEslNetwork extends 
		IActiveQueueSubscriber<EslCommand>,
		IApplicationMessageListener, 
		IEslMessageTransmitter 
{
	
	public void onMessage(EslMessage msg);
	
	public Channel getActiveChannel();
	public EUI64Address getCoordinatorMac();
	
	public Set<EUI64Address> getAssociatedSet();
	
	public boolean sendEslMessage(EslMessage msg);
	public boolean sendEslMessage(NetworkAddress nwkAddress, ISerializable message);
	
	public void loadAssociatedEslsFromDatabase(EUI64Address address); 
	
	public boolean connect();
	public void setChannelList(String chList, boolean send);
	public String getChannelList();
	public void setTime();
	public void reset();
	
	public boolean isOnline();
	
	public EslProxy getEslProxy(EUI64Address mac);
	
	public int getAssociatedEsls();
	public Set<EslSignalInfo> getAssociatedEslsSignal();
	
	public EslFirmwareUpdater getFirmwareUpdater();
	
	public void evalFirmware(int deviceType, FirmwareVersion fwVersion);
	public void stop();
	public void setReconnectTimeoutMs(int ms);
	public int getReconnectTimeoutMs();
	
	public void onScanprobe(EUI64Address esl);
	
	public boolean waitForStatusRequestEnd();
	public boolean waitForFirmwareUpgradeEnd();
	
	public EUI64Address getAssociatedAddress(NetworkAddress nwk);
		
	public int getOutboundSize();
	public int getOutboundLowPrioritySize();
	public int getOutboundBroadcastSize();
	public int getOutQueueSize();
	public int getPendingStatusSize(); 
	
	public long getOfflineCount();
	public long getOnlineCount();
	public long getOfflineTime();
	public long getOnlineTime();
	
	public int restorePriceUpdates();
	public void setPriceUpdateSent(NetworkAddress nwk);
	
	public void shutdown();
	
	public void emptyBuffer(NetworkAddress nwk, boolean highPriority);
	
	public boolean sendEslApplicationMessage(
			List<EslTransportFrame> frames, 
			ITransmissionListener<EslMessage> listener,
			NetworkAddress destination);
		
}
