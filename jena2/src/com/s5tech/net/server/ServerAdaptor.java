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
 
package com.s5tech.net.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.esl.CoordinatorEventMessageInfo;
import com.s5tech.net.esl.EslEventMessageInfo;
import com.s5tech.net.esl.EslMessageInfo;
import com.s5tech.net.esl.EslStatisticsMessageInfo;
import com.s5tech.net.esl.EslStatusMessageInfo;
import com.s5tech.net.esl.HubEventMessageInfo;
import com.s5tech.net.esl.MessageInfo;
import com.s5tech.net.eslnet.IEslNetwork;
import com.s5tech.net.msg.EslStatistics;
import com.s5tech.net.msg.EslStatistics320;
import com.s5tech.net.msg.EslStatus;
import com.s5tech.net.type.Channel;
import com.s5tech.net.type.EUI48Address;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.Percent;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.XmlUtils;
import com.s5tech.net.xml.types.CommandResult;
import com.s5tech.net.xml.types.CoordinatorEventType;
import com.s5tech.net.xml.types.EslEventType;
import com.s5tech.net.xml.types.HubEventType;
import com.s5tech.net.xml.types.MessageCommand;
import com.s5tech.net.xml.types.StateType;

/**
 * This class provide convenient methods for sending various messages to the server.
 * I wraps a {@link IServerProxy} object, to which it sends all messages
 * @author S5Tech Development Team
 *
 */
public class ServerAdaptor implements IServerProxy {

	private IServerProxy serverProxy;
	private ActiveQueue<MessageInfo<?>> upQueue;
	private static Logger log;
	private boolean upstreamDisabled = false;
	
	private String name = null;
	
	private ServerAdaptor(IServerProxy serverProxy, String name) {
		this(serverProxy, true, name);
	}
	
	private ServerAdaptor(IServerProxy serverProxy, boolean async, String name) {
		this.name = name;
		this.serverProxy = serverProxy;
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
		upstreamDisabled = (System.getProperty("upstream.disabled") != null);
		if (upstreamDisabled) {
			log.warn("UPSTREAM DISABLED! All Esls network" +
					" incoming messages will not sent to backend!");
			return;
		}		
		if(async) makeAsync();		
	}
	
	public void makeAsync() {
		if(upQueue == null) {
			upQueue = new ActiveQueue<MessageInfo<?>>(new IActiveQueueSubscriber<MessageInfo<?>>() {
				public void elementFromQueue(MessageInfo<?> element) {
					serverProxy.sendMessage(element);
				}
			}, name + " async. transmitter");
		}
	}
	
	public boolean isAsync() {
		return upQueue != null;
	}	
	

	public boolean sendEslPriceAcknowledge(EUI64Address macAddress, long hashCode, CommandResult result, String refId) {
		MessageInfo<EUI64Address> msg = new EslMessageInfo(true, refId , macAddress, MessageCommand.ESLPRICEUPDATE);
		msg.setResult(result);
		msg.setDescription("Acknowledge of a price update for ESL " + macAddress);
		msg.addContent(XmlUtils.createTextElement("hash", String.valueOf(hashCode)));
		return sendMessage(msg);
	}

	/**
	 * Delivers an esl event to the server.
	 * @param esl
	 * @param event
	 * @return
	 */
	public boolean eslEvent(EUI64Address esl, EslEventType event) {
		return eslEvent(esl, event, null, null);
	}
	
	public boolean eslEvent(EUI64Address esl, EslEventType event, EUI64Address coordinatorMac)
	{
		return eslEvent(esl, event, coordinatorMac, null);
	}
			
	
	/*
	 * TODO: introdurre coda + mappa e processo subscriber di consumazione per invio a blocchi
	 * 		 per alleviare problemi di join massive dovute a cadute di reti ESL
	 * 		 
	 */
	public boolean eslEvent(EUI64Address esl, EslEventType event, EUI64Address coordinatorMac, String attributes) {
		EslEventMessageInfo msg = new EslEventMessageInfo("", esl);
		//msg.setHubMac(PlatformFacade.instance().getEth0Mac());
		msg.setType(event);
		if (attributes != null) msg.setAttributes(attributes);
		if (coordinatorMac != null) msg.setCoordinatorMac(coordinatorMac);
		return sendMessage(msg);
	}
	
	/**
	 * Delivers an esl status to the server
	 * @param eslMac
	 * @param status
	 * @param network
	 * @return
	 */
	public boolean eslStatus(EUI64Address eslMac, EslStatus status, IEslNetwork network) {
		return sendMessage(createEslStatus(eslMac, status, network));
	}
	
	public boolean sendEslStatistics(EUI64Address eslMac, EslStatistics statistics)
	{
		return sendMessage(createEslStatistics(eslMac, statistics));
	}	
	
	public boolean coordinatorEvent(CoordinatorEventType eventType, EUI64Address mac, String port, Channel channel) {
		CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo(mac);
		msg.setType(eventType);
		if (port != null) msg.setPort(port);
		if (channel != null && !channel.isOffline()) msg.setChannelNo(channel);		
		return sendMessage(msg);
	}
	
	public boolean hubEvent(HubEventType eventType, EUI48Address mac, String ipAddress) {
		HubEventMessageInfo msg = new HubEventMessageInfo(mac);
		msg.setType(eventType);
		if (ipAddress != null) msg.setIpAddress(ipAddress);		
		return sendMessage(msg);
	}
		
	public boolean sendMessage(MessageInfo<?> msg) {
		if (serverProxy == null) return true;
		if (upstreamDisabled) return true;
		if(upQueue == null)
			return serverProxy.sendMessage(msg);
		boolean res = upQueue.add(msg);
		if(!upQueue.isEmpty() && log.isTraceEnabled())
			log.trace("Added message to queue: " + res + ". Queue size: " + upQueue.size());
		return res;
	}

	public boolean isConnected() {
		return serverProxy != null && serverProxy.isConnected();
	}
		
	private EslStatusMessageInfo createEslStatus(EUI64Address eslMac, EslStatus status, IEslNetwork network) {
		
		EslStatusMessageInfo s = new EslStatusMessageInfo(null, eslMac);
		Channel channel = network.getActiveChannel();
		s.setHashCodeActivePrice(status.getHashCodeActivePrice());
		s.setHashCodePendingPrice(status.getHashCodePendingPrice());		
		if (status.getFirmwareVersion().getMajor() >= 4) {
			s.setBatteryLevel(status.getBatteryLevel());
		}
		else {
			s.setBatteryLevel(Percent.calcPercent(status.getBatteryLevel(), 80, 255));
		}		
		s.setChannel(channel == null ? "1" : String.valueOf(channel.getValue()));
		s.setFirmwareVersion(status.getFirmwareVersion().toString());
		s.setLifetimeHours(status.getLifetimeHours());
		if(s.getLifetimeHours() <= 0)
			s.setLifetimeHours(1);
		if(network != null && network.getCoordinatorMac() != null)
			s.setMacAssociatedCoordinator(network.getCoordinatorMac());
		//s.setMacAssociatedHubEth0(PlatformFacade.instance().getEth0Mac());
		s.setNightMode(status.isNightModeOn());
		s.setRailDetected(!status.isRailAlarmOn());
		//TODO state information not available
		s.setState(StateType.UNKNOWN);
		s.setTemperature(status.getTemperature());
		s.setTxPower(Percent.calcPercent(status.getLinkQuality(), 0, 255));		
		s.setDeviceType(status.getDeviceType());

		Map<EUI64Address, Integer> coordsInRange = status.getAlternativeCoordinatorsInRange();
		if(coordsInRange != null && coordsInRange.size() > 0) {
			for(Map.Entry<EUI64Address, Integer> pair : coordsInRange.entrySet()) {
				s.addCoordinatorInRangeOfEsl(pair.getKey(), Percent.calcPercent(pair.getValue(), 0, 255));
			}
		}
		return s;
	}
	
	
	private EslStatisticsMessageInfo createEslStatistics(EUI64Address eslMac, EslStatistics stats) {		
		EslStatisticsMessageInfo s = new EslStatisticsMessageInfo(null, eslMac);
		s.setMac(eslMac);
		s.setnAssertReset(stats.getnAssertReset());
		s.setnColdReset(stats.getnColdReset());
		s.setnHotReset(stats.getnHotReset());
		s.setnNetSleep(stats.getnNetSleep());
		s.setnOtaReset(stats.getnOtaReset());
		s.setnPowerupSleep(stats.getnPowerupSleep());
		s.setnPushReset(stats.getnPushReset());
		s.setnPushSleep(stats.getnPushSleep());
		s.setnScan(stats.getnScan());
		s.setnScanSleep(stats.getnScanSleep());
		s.setnStatusRetry(stats.getnStatusRetry());
		if (stats instanceof EslStatistics320)
		{
			s.setnJoinWDT((int)((EslStatistics320) stats).getnJoinWDT());
		}
		else 
		{
			s.setnJoinWDT(-1);
		}
		s.setTime(stats.getTimestamp());
		return s;
	}
	
	public void kill()
	{
		if (upQueue == null) return;
		try {
			upQueue.kill();
		}
		catch (Throwable ignored) {}
	}
	
	
	/**
	 * Wraps a {@link IServerProxy} object in a {@link ServerAdaptor} object.
	 * If the supplied server proxy is actually a {@link ServerAdaptor} instance, it will simply be returned
	 * @param serverProxy
	 * @return
	 */
	public static ServerAdaptor wrap(IServerProxy serverProxy, String name) {
		if(serverProxy == null) return null;
		if(serverProxy instanceof ServerAdaptor) return ServerAdaptor.class.cast(serverProxy);
		return new ServerAdaptor(serverProxy, name);
	}

}
