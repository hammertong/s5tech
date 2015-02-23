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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import org.exolab.castor.types.AnyNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.coord.CoordinatorHelper;
import com.s5tech.net.entity.EslDataStore;
import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.entity.EslInstallationKey;
import com.s5tech.net.entity.IEslDataStore;
import com.s5tech.net.entity.IEslEntityManager;
import com.s5tech.net.esl.EslCommand;
import com.s5tech.net.eslnet.IEslNetwork;
import com.s5tech.net.firmware.FirmwareInfo;
import com.s5tech.net.firmware.FirmwareLibrary;
import com.s5tech.net.services.logging.ApplicationServicesListener;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.type.TimeData;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;
import com.s5tech.net.xml.EslChannelToJoinInfo;
import com.s5tech.net.xml.EslItem;
import com.s5tech.net.xml.EslList;
import com.s5tech.net.xml.EslPriceData;
import com.s5tech.net.xml.EslStatusRequestInfo;
import com.s5tech.net.xml.Firmware;
import com.s5tech.net.xml.Message;
import com.s5tech.net.xml.TimeInfo;
import com.s5tech.net.xml.types.CommandResult;
import com.s5tech.net.xml.types.MessageCommand;

public class ServerMessageDispatcher 
		implements IServerMessageReceiver {

	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	private static ServerMessageDispatcher instance_;
	
	private Object syncFwUpdate = new Object();
	
	private IEslEntityManager entityManager;
	
	private IEslDataStore dataStore;
	
	private ApplicationServicesListener networkManager = null;
	
	private boolean ignoreXmlActivationTime = false;
	
	
	private ServerMessageDispatcher() {
		log.trace("initialize server message dispatcher ...");
		if (System.getProperty("ignoreXmlActivationTime") != null) {
			log.warn("Server message dispatcher running with activationTime xmltag ignore flag activated !!!");				
			ignoreXmlActivationTime = true;
		}
		networkManager = ApplicationServicesListener.instance();
		entityManager = EslEntityManager.instance();
		dataStore = EslDataStore.instance();
	}
	
	public synchronized static ServerMessageDispatcher instance()
	{
		if (instance_ == null) instance_ = new ServerMessageDispatcher();
		return instance_;
	}
	
	public synchronized void onMessage(Message msg) {
		
		if (log.isTraceEnabled())
			log.trace("Received message with command: " + msg.getMsgCommand());
		
		EslList esls = null;
		EslCommand eslCommand = null;
		TimeInfo timeInfo = null;
		
		boolean now = false;
		//Date act = null;
		TimeData td = null;
		
		switch(msg.getMsgCommand()) {
			
		case ESLPRICEUPDATE:
			
			{			
				EslPriceData priceData = null;
				long utcT, actT;
				boolean active;
				
				for (Object o : msg.getAnyObject()) {
					if(o instanceof EslList)
						esls = EslList.class.cast(o);
					else if(o instanceof EslPriceData)
						priceData = EslPriceData.class.cast(o);
				}
				
				if (esls == null) {
					log.error("Incomplete esl price update request; no esls list specified");
					break;
				}
				
				if (priceData == null || priceData.getContent() == null) {
					log.error("Incomplete esl price update request; no price specified");
					break;
				}
				
				com.s5tech.net.entity.EslPriceData price = new com.s5tech.net.entity.EslPriceData();
				
				price.setUpdateId(System.currentTimeMillis());						
							
				if (ignoreXmlActivationTime)
				{
					//prendo l'activation time dal payload invece dhe dal tag xml
					price.setActivationTime(new Date(Tools.getLongFromBytes(priceData.getContent(), 2, 4, false) * 1000 + 946684800000L));
				}
				else 
				{
					//prendo l'activation time dal tag xml come da predefinito
					price.setActivationTime(priceData.getActivationTime());
				}
				
				price.setData(priceData.getContent());
				price.setHashWhenApplied(priceData.getHashCode());
				price.setRefId(msg.getMsgId());
				
				utcT = System.currentTimeMillis();
				
				actT = price.getActivationTime().getTime();
				actT += TimeZone.getDefault().getOffset(System.currentTimeMillis());
				
				active = (utcT >= actT);
	
				Collection<EUI64Address> lsEsls = MessageUtils.getMacsFromList(esls);
				if (lsEsls == null || lsEsls.size() == 0)
				{
					log.error("Incomplete esl price update request; empty esls list");
					break;
				}
				
				//
				// Update Datastore
				//
				
				Vector<EUI64Address> removeList = new Vector<>();
				
				for (EUI64Address a : lsEsls) {		
					
					if (!entityManager.isAllowed(a)) {
						log.warn("price update for mac {} not authorized (esl removed or not added yet ?), skipping this mac ...", a);
						removeList.add(a);
						continue;
					}
					
					if (active) 
					{
						dataStore.setActivePriceForEsl(a, price);
						if (log.isTraceEnabled()) log.trace("updated active price for {} on datastore", a);						
					}
					else 
					{
						dataStore.setPendingPriceForEsl(a, price);					
						if (log.isTraceEnabled()) log.trace("updated pending price for {} on datastore", a);
					}		
					
					//
					// TODO:
					// POSSIBILE INSERIRE QUI UN LOOP PER CAPIRE SE L'ESL E' PRESA IN CARICO DA
					// UN CONTROLLER, E RIPORTARE UN WARNING SE NON E' ASSOCIATA A NESSUNO.
					// AL MOMENTO NON LO FACCIO PER QUESTIONI DI LATENCY E AUMENTO DI RISCHIO DI
					// DEADLOCK
					//
					
				}
				
				for (EUI64Address a : removeList) {					
					lsEsls.remove(a);					
				}
				
				if (lsEsls.size() == 0) {
					log.error("price update command discarded! no valid MAC address given in message body ...");
					break;
				}
				
				//
				// Trace data if activated by logger
				//
				
				if (log.isTraceEnabled()) {
					StringBuffer b = new StringBuffer("received from server EslPriceUpdate - ");				
					long payloadActime = Tools.getLongFromBytes(priceData.getContent(), 2, 4, false);
					Date payloadActimeToDate = new Date(payloadActime * 1000 + 946684800000L);
					Date xmlActivationDate = priceData.getActivationTime();								
					b.append("ESLs: ");
					for (EUI64Address a : lsEsls) {
						b.append(a);
						b.append(' ');
					}				
					b.append(", payload activation date: ").append(payloadActimeToDate);
					b.append(" (secs from 2000 = ").append(payloadActime);					
					b.append(") Xml Activation Date: ").append(xmlActivationDate);
					b.append (", price is ").append((active ? "ACTIVE": "PENDING")).append(": current system time => ")
								.append(new Date(utcT)).append(" [").append(utcT).append(" ms]") 
								.append(" received activation time => ")
								.append(new Date(actT)).append(" [").append(actT).append(" ms]");					
						
					log.trace(b.toString());
				}
				
				//
				// send data to network controllers
				//
				
				eslCommand = new CoordinatorHelper(msg.getMsgId(), lsEsls, price);
				eslCommand.setAllEsls(esls.isAll());
				
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
			
			}
				
			break;
			
		case ESLENTERNIGHTMODE:

			{
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} if(o instanceof TimeInfo) {
						timeInfo = TimeInfo.class.cast(o);
					}
				}
				
				if(esls == null) {
					log.warn("Incomplete esl enter night mode request; no esls specified");
					break;
				}
	
				if(timeInfo == null) {
					log.warn("Incomplete esl enter night mode request; no time specified");
					break;
				}
				
				now = MessageUtils.isDateNow(timeInfo.getActivation());
				Date act = now ? null : MessageUtils.parseDate(timeInfo.getActivation());
				td = new TimeData(act, timeInfo.getDurationSecs(), timeInfo.getIntervalSecs(), now);
				
				eslCommand = new EslCommand(MessageCommand.ESLENTERNIGHTMODE, msg.getMsgId(), MessageUtils.getMacsFromList(esls), td);				
				eslCommand.setAllEsls(esls.isAll());
									
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
				
			}
			
			break;
			
		case ESLSETACTIVESERVICEPAGE:
		
			{
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} if(o instanceof TimeInfo) {
						timeInfo = TimeInfo.class.cast(o);
					}
				}
				
				if(esls == null) {
					log.warn("Incomplete esl enter night mode request; no esls specified");
					break;
				}
	
				if(timeInfo == null) {
					log.warn("Incomplete esl enter night mode request; no time specified");
					break;
				}
				
				Date act = null;
				now = MessageUtils.isDateNow(timeInfo.getActivation());
				if (!now) {
					act = now ? null : MessageUtils.parseDate(timeInfo.getActivation());				
					now = (act == null);					
				}
				
				td = new TimeData(act, timeInfo.getDurationSecs(), timeInfo.getIntervalSecs(), now);
				
				eslCommand = new EslCommand(MessageCommand.ESLSETACTIVESERVICEPAGE, msg.getMsgId(), MessageUtils.getMacsFromList(esls), td);				
				eslCommand.setAllEsls(esls.isAll());
				
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
				
			}
			
			break;
			
		case ESLSETALARMMODE:
			
			{
				AnyNode alarmOnNode = null;
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} if (o instanceof AnyNode) {
						alarmOnNode = AnyNode.class.cast(o);
					}
				}
				if(esls == null) {
					log.warn("Incomplete esl set alarm mode request; no esls specified");
					break;
				}
				
				if(alarmOnNode == null 
						|| !"alarmEnabled".equals(alarmOnNode.getLocalName()) ||
						alarmOnNode.getFirstChild() == null) {
					log.warn("Incomplete esl set alarm mode request; no alarm mode specified");
					break;
				}
	
				boolean alarmOn = Boolean.getBoolean(alarmOnNode.getFirstChild().getStringValue());
				eslCommand = new EslCommand(MessageCommand.ESLSETALARMMODE, 
						msg.getMsgId(), MessageUtils.getMacsFromList(esls), alarmOn);
				eslCommand.setAllEsls(esls.isAll());
				
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
			}

			break;
			
		case ESLSTATUSREQUEST:
			
			{
			
				EslStatusRequestInfo rinfo = null;
	
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} 
					else if(o instanceof TimeInfo) {
						timeInfo = TimeInfo.class.cast(o);
					}
					else if(o instanceof EslStatusRequestInfo) {
						rinfo = EslStatusRequestInfo.class.cast(o);
					}
				}
				
				if(esls == null) {
					log.warn("Incomplete esl status request; no esls specified");
					break;
				}
				
				eslCommand = new EslCommand(
						MessageCommand.ESLSTATUSREQUEST, 
						msg.getMsgId(), MessageUtils.getMacsFromList(esls), rinfo);			
				eslCommand.setAllEsls(esls.isAll());
				
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
	
				final String mid = msg.getMsgId();
				final boolean isall = esls.isAll();
	
				Thread t = new Thread(new Runnable() {						
					@Override
					public void run() {
						boolean ret = true;
						int count = 0;
						try {
							Thread.sleep(1000);
							for (IEslNetwork n : networkManager.getAll().values()) {					
								ret &= n.waitForStatusRequestEnd();					
								count ++;
							}
							networkManager.sendStatusRequestAcknowledge(
									ret ? CommandResult.SUCCESS : CommandResult.FAILURE, 
									mid, 
									"Completed " + (count < 2 ? "network" : (count + " networks")) + " status request of "
									+ (isall ? "all esls" : "partial list of esls"));
							if (log.isInfoEnabled())
								log.info("Status request completed " + (ret ? "successfully" : "with ERRORS")  + " on all network controllers");
						}
						catch (Throwable t) {	
							log.error("Status request processsing Error: {}", t);
							networkManager.sendStatusRequestAcknowledge(
									CommandResult.FAILURE, 
											mid, 
											"Status request processsing error - " + t.getMessage());
						}					
					}
				}, "Wait-For-Status-Request-End");
				t.setDaemon(true);
				t.start();
				
			}
			
			break;
					
		case ESLSETCHANNELTOJOIN:
		case ESLSTATISTICSREQUEST:
			
			{
			
				StringBuffer sb = new StringBuffer();
				
				for(Object o : msg.getAnyObject()) {				
					if(o instanceof EslList) 
					{
						esls = EslList.class.cast(o);
					}				
					if(o instanceof EslChannelToJoinInfo) {
							
						EslChannelToJoinInfo info = EslChannelToJoinInfo.class.cast(o);
						sb.append(info.getChannel())
								.append(' ')
								.append(info.getSecsToWait());					
						break;
					}
				}
				
				if(esls == null) {
					log.warn("Incomplete esl list in " + (msg.getMsgCommand() == MessageCommand.ESLSETCHANNELTOJOIN ? 
							"channel to join": "statistics") + " request; no esls specified");
					break;
				}
				
				eslCommand = new EslCommand(
						msg.getMsgCommand(), 
						msg.getMsgId(), MessageUtils.getMacsFromList(esls));			
				eslCommand.setAllEsls(esls.isAll());			
				if (sb.length() > 0) eslCommand.setData(sb.toString());
				
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
				
			}
			
			break;
			
		case ESLKILL:
		case ESLLEAVE:
			
			{
			
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} 
				}
				
				if(esls == null) {
					log.warn("Incomplete esl kill request; no esls specified");
					break;
				}
				
				eslCommand = new EslCommand(
						msg.getMsgCommand(), 
						msg.getMsgId(), MessageUtils.getMacsFromList(esls));			
				eslCommand.setAllEsls(esls.isAll());
				
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
				
			}
				
			break;
		
		case ADDESLLIST:
			
			{
			
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} 
				}
				
				if(esls == null) {
					log.warn("Incomplete add esl request; no esls specified");
					break;
				}
				
				List<EslDeviceInfo> eslsToAdd = new LinkedList<EslDeviceInfo>();			
				for (EslItem item: esls.getEslItem()) {
					EslDeviceInfo esl = new EslDeviceInfo(new EUI64Address(item.getMac()));
					if (item.getEslShortAddress() > 0)
						esl.setNetworkAddress(new NetworkAddress((int)item.getEslShortAddress()));			
					esl.setType(EslEntityManager.instance().lookupNetworkEquivalentType(item.getType()));
					esl.setInstallationKey(new EslInstallationKey(item.getInstallationKey()));
					eslsToAdd.add(esl);
				}
				
				EslEntityManager.instance().addEsls(eslsToAdd);
				
				// this message will be not notified to esls networks 
				// because esls are supposed not joined yet, and managed 
				// only by entity manager
				
			}
			
			break;
			
		case REMOVEESLLIST:
			
			{			
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} 
				}
				
				if(esls == null) {
					log.warn("Incomplete remove esl request; no esls specified");
					break;
				}
				
				Collection<EUI64Address> eslsToRemove = MessageUtils.getMacsFromList(esls);
				
				EslEntityManager.instance().removeEsls(eslsToRemove);
				
				for (EUI64Address address : eslsToRemove)
				{
					EslDataStore.instance().removeActivePriceForEsl(address);
					EslDataStore.instance().removePendingPriceForEsl(address);
				}
				
				eslCommand = new EslCommand (
						MessageCommand.REMOVEESLLIST, 
						msg.getMsgId(), MessageUtils.getMacsFromList(esls));
				
				ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
			
			}
			
			break;
			
		case ESLFIRMWAREUPDATE:
			
			{			
				Firmware fwInfo = null;
				
				for(Object o : msg.getAnyObject()) {
					if(o instanceof EslList) {
						esls = EslList.class.cast(o);
					} 
					if(o instanceof Firmware) {
						fwInfo = Firmware.class.cast(o);
					}
				}
				
				if (esls == null || !esls.isAll()) {
					log.error("cannot run firmware upgrade without all esls set");
					networkManager.sendFirmwareAcknowledge(CommandResult.FAILURE, msg.getMsgId(), "Esl list not set or not valid");
					break;
				}
				
				if (fwInfo == null) {
					log.error("cannot run firmware upgrade without data set");
					networkManager.sendFirmwareAcknowledge(CommandResult.FAILURE, msg.getMsgId(), "Firmware data not present");
					break;
				}
				
				boolean _now = fwInfo.getStartNow();
				byte [] data = fwInfo.getContent();
				
				try {			
					
					FirmwareInfo fw = null;
					
					synchronized (syncFwUpdate) {					
						
						fw = FirmwareLibrary.instance().updateLibrary(data);
						
						if (fw != null) {
							File fwdir = new File(FirmwareLibrary.FIRMWARE_FOLDER);
							File fwFile = new File (fwdir.getAbsolutePath() 
									+ "/ESLFIRMWARE_DEV0X" 
									+ (fw.getDeviceType() < 16 ? "0" : "" ) + Integer.toString(fw.getDeviceType(), 16) 
									+ "_" 
									+ fw.getVersion() + ".bak");
							FileOutputStream out = new FileOutputStream(fwFile);
							out.write(data);
							out.close();	
							fw.setFile(fwFile);
						}
						else {
							log.error("Error loading firmware data, cannot start firmware upgrade");
						}
						
					}
					
					
					if (_now && fw != null) {
						if (log.isInfoEnabled())
							log.info("requested firmware upgrade starting now for device type {}, " +
									 "version {} : dispatching reqest to all networks...", 
									 fw.getDeviceType(), fw.getVersion());		
						
						eslCommand = new EslCommand(MessageCommand.ESLFIRMWAREUPDATE, msg.getMsgId(), null, fw);
						eslCommand.setAllEsls(true);			
						ApplicationServicesListener.instance().sendToAllNetworks(eslCommand);
											
						final String m2id = msg.getMsgId();
	
						Thread t2 = new Thread(new Runnable() {						
							@Override
							public void run() {
								boolean ret = true;
								int count = 0;
								try {
									Thread.sleep(1000);
									for (IEslNetwork n : networkManager.getAll().values()) {					
										ret &= n.waitForFirmwareUpgradeEnd();					
										count ++;
									}
									networkManager.sendFirmwareAcknowledge(
											ret ? CommandResult.SUCCESS : CommandResult.FAILURE, 
											m2id, 
											"Completed " + (count < 2 ? "network" : (count + " networks")) + " firmware upgrade");
								}
								catch (Throwable t) {	
									log.error("Firmware upgrade processsing Error: {}", t);
									networkManager.sendFirmwareAcknowledge(
											CommandResult.FAILURE, 
													m2id, 
													"Firmware upgrade processsing error - " + t.getMessage());
								}					
							}
						}, "Wait-For-Firmware-Upgrade-End");
						t2.setDaemon(true);
						t2.start();
						
					}
					else if (fw != null)
					{					
						if (log.isInfoEnabled())
							log.info("Updated Firmware library with device type {}", Tools.toStringObj(fw));
						networkManager.sendFirmwareAcknowledge(CommandResult.SUCCESS, msg.getMsgId(), "Firmware library upgraded");
					}
					else 
					{
						log.error("Firmware upgrade failure, data not valid");
						networkManager.sendFirmwareAcknowledge(CommandResult.FAILURE, msg.getMsgId(), "Invalid data");
					}
					
				}
				catch (Throwable x)
				{
					log.error("Exception upoading firmware file - {}", x);
					networkManager.sendFirmwareAcknowledge(CommandResult.FAILURE, msg.getMsgId(), "Dispatcher exception - " + x.getMessage());
					break;
				}
				
			}
			
			break;
						
		case SETSTOREKEY: 			
		case ESLEVENT:
		case ESLSTATUS:
		default:
			log.warn("Command not applicable here: " + msg.getMsgCommand() + ", Msg id: " + msg.getMsgId());
			
		}
				
	}

}
