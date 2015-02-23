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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.coord.CoordinatorHelper;
import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.entity.EslPriceData;
import com.s5tech.net.entity.FirmwareVersion;
import com.s5tech.net.entity.IEslDataStore;
import com.s5tech.net.entity.IEslEntityManager;
import com.s5tech.net.esl.ApplicationMessage;
import com.s5tech.net.esl.EslCommand;
import com.s5tech.net.esl.EslSignalInfo;
import com.s5tech.net.esl.Hub2EslNetCtrlCommand;
import com.s5tech.net.esl.Hub2EslNetCtrlFrame;
import com.s5tech.net.firmware.DeviceType;
import com.s5tech.net.firmware.FirmwareInfo;
import com.s5tech.net.firmware.FirmwareLibrary;
import com.s5tech.net.firmware.IFirmwareUpdateSubscriber;
import com.s5tech.net.msg.EslApplicationCommand;
import com.s5tech.net.msg.EslApplicationFrame;
import com.s5tech.net.msg.EslCapabilityInformation;
import com.s5tech.net.msg.EslLeaveMessage;
import com.s5tech.net.msg.EslMessage;
import com.s5tech.net.msg.EslProxy;
import com.s5tech.net.msg.EslSecurityFrame;
import com.s5tech.net.msg.EslStatistics;
import com.s5tech.net.msg.EslStatus;
import com.s5tech.net.msg.EslTransportFrame;
import com.s5tech.net.msg.IEslListener;
import com.s5tech.net.msg.ITransmissionListener;
import com.s5tech.net.serial.SerialPortFacade;
import com.s5tech.net.serial.SerialPortFacade.ISerialListener;
import com.s5tech.net.server.IServerProxy;
import com.s5tech.net.server.ServerAdaptor;
import com.s5tech.net.services.logging.ApplicationServicesListener;
import com.s5tech.net.type.BufferInformation;
import com.s5tech.net.type.Channel;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.ISerializable;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.type.Percent;
import com.s5tech.net.type.SerializeFactory;
import com.s5tech.net.type.TimeData;
import com.s5tech.net.type.TimeslotBuffer;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;
import com.s5tech.net.util.Tools.DecodeResult;
import com.s5tech.net.xml.EslStatusRequestInfo;
import com.s5tech.net.xml.types.CommandResult;
import com.s5tech.net.xml.types.EslEventType;
import com.s5tech.net.xml.types.MessageCommand;

public class EslNetworkController extends Observable
	implements IEslNetwork, Runnable	
{
	
	/** oordinator write timeout in ms */
	public static final int MIN_FRAME_INTERVAL_MILLIS = 50;

	/** coordinator's set-time interval in ms */
	private long SET_TIME_INTERVAL_MILLIS = 7200000; // Every 2 hours

	/** the serial port proxy for this controller */
	private SerialPortFacade port;
	
	/** specialized logger for scanprobes (disabled by default) */
	private static final Logger logScanprobes = LoggerFactory.getLogger (ISystemKeys.SCANPROBES);
	
	/** the coordinator MAC address associated to this controller */
	private EUI64Address address;
	
	/** coordinator firmware version on this controler */
	private FirmwareVersion firmwareVersion;
	
	/** mark as online when reading data from coordinator */
	private boolean serialPortOnline;
	
	/** semaphore used to block serial reader processing when USB port goes offline */
	private final Object onLineWaitObject = "";
	
	/** the coordinator IEEE 802.15.4 current channel */
	private Channel activeChannel;
	
	/** the list of permitted IEEE 802.15.4 channels on this controller */
	private Set<Channel> allowedChannels_;
	
	/** the default start-up list of permitted IEEE 802.15.4 channels on this controller */
	private Set<Channel> defaultAllowedChannels;
	
	/** the serial listener implementation of this controller */
	private ISerialListener mySerialListener;
	
	/** queue for exchanging incoming network data between serial reader and the netowrk controller */
	private ActiveQueue<Hub2EslNetCtrlFrame> inQueue;
	
	/** 
	 * priority based queue used by coordinators' outgoing frames writer thread 
	 * see the javadoc of Hub2EslNetCtrlFrame to see more info about priority 
	 * @see tOutQueue */
	private PriorityBlockingQueue<Hub2EslNetCtrlFrame> outQueue;
	
	/** the high priority list of outgoing esl messages (price updates and esls broadcast updates) */
	private LinkedHashMap<NetworkAddress, LinkedList<EslMessage>> outbound;
	
	/** the low priority list of outgoing esl messages */
	private LinkedHashMap<NetworkAddress, LinkedList<EslMessage>> outboundLowPriority;
		
	/** outgoing status request queue */
	private LinkedList<NetworkAddress> pendingStatus;
	
	/** coordinator's buffer information shared between buffer transmitter and the reader task running on mySerialListener */  
	private BufferInformation bufferInfo = new BufferInformation();
	
	/** the coordinator's buffer transmitter implementation use by each ESlProxy @see co.s5tech.net.esl.EslProxy */
	private Thread eslMessageTransmitter;
	
	/** the coordinator's buffer semaphore */ 
	private Semaphore bufferSemaphore;
	
	/** pointer to last firmware updated in the network */
	private FirmwareInfo latestFirmware;
	
	/** time setter @see SET_TIME_INTERVAL_MILLIS */
	private Timer timeSetter;
	
	/** task of time setter @see SET_TIME_INTERVAL_MILLIS */
	private TimerTask timeSetterTask;
	
	/** coordinators' outgoing frames writer task @see com.s5tech.net.Hub2EslNetCtrlFrame */ 
	private Thread tOutQueue;
	
	/** the parent network manager of this controller */
	private ApplicationServicesListener networkManager = null;
	
	/** if equals true, improves time correction */
	boolean autocorrectTime;
	
	/** control used to balance the initial set of commands that has been sent to the coordinator */
	boolean first_init_done = false;
	
	/** maximum number of join requests that can be set in the flush queue buffer, 
	 * set to -1 means no control on flush queue buffer (default) */
	private int outqueueThreshold = -1;
	
	/** minimum coordinator firmware versione that supports upgrade */
	private FirmwareVersion minimumForUpgrade;
	
	// high level controller 
	
	/** the default allowed channels list */
	public static final String DEFAULT_ALLOWED_CHANNELS	= "all";
		
	/** The number of times to send certain broadcast messages */
	public  static final int BROADCAST_SPAM_COUNT = 3;
	
	/** specialized logger for this controller */
	private Logger log; 

	/** server transmitter interface */ 
	private ServerAdaptor server;
	
	/** the esl entity manager reference */ 
	private IEslEntityManager eslEntityManager;

	/** the esl proxies list */
	private Map<EUI64Address, EslProxy> proxyMap = new TreeMap<EUI64Address, EslProxy>();
	
	/** the datastore entity manager */ 
	private IEslDataStore dataStore;
	
	/** listener of the esls */
	private IEslListener eslListener;
	
	/** firmware updater task */
	private EslFirmwareUpdater fwUpdater;
	
	/** allowed channels list  */ 
	private Set<Channel> allowedChannels;
	
	/** nightmode shared structure */ 
	private TimeData nightModeData;
	
	/** enable sending unauthorized join attempts to server */
	private boolean sendUnauthorizedJoinAttemptMessages = false;
	
	/** shutdown signal flag */
	private AtomicBoolean shutdown = new AtomicBoolean(false);
	
	/** status request task */ 
	private Thread statusRequestProc = null;	
	
	/** singel status request map */
	private TreeSet<NetworkAddress> statusRequestAddressList = null;
	
	/** allow(true)/disallow(false) sending leave and kill commands to esls */
	private boolean enableLeave = false;

	/** action to take when removing esl with <i>RemoveEslList</i> Command: <ol><li>0 = no action</li><li>1 = send leave to esl</li><li>2 = send kill to esl</li></ol> */
	private short removeAction = 0;
	
	/** if set to false, skip transmission when esl offline, returning to server an ack fail */
	private boolean keepMessagesWhenOffline = false;

	/** flag enabling the persistent outgoing buffer of price updates after stop & go */
	private boolean persistentPriceUpdates = false;
	
	/** network address map for looking-up mac addres */ 
	private Map<NetworkAddress, EUI64Address> nwkMap;	
	
	/** use the local address assignemnt (disabled by default, server authorization is strongest) */
	private boolean local_auth = false;
	
	/** flag enabling authorized access to all esls in the network */
	private boolean authorizeAll = false;
	
	/** used to keep trace of las network address assignment */ 
	NetworkAddress networkAddressCounter = null;
	
	/** maximum nuber of esls joinable on this controller */
	public int MAX_REJOIN = 0xfffc; 
	
	/** lock used to set exclusive access flushing outgoing esls messages */
	private ReentrantLock outboundLock = new ReentrantLock();

	
	/** low level log for serial i/o USB communication */
	Logger iolog = null; 
	
	/**
	 * Network controller initializer
	 * 	
	 * @param portUrl_ the serial port url (USB or virtual hub tcp/ssl bridge)
	 * @param eslEntityManager 
	 * @param dataStore
	 * @param serverProxy
	 */
	public EslNetworkController(
			String portUrl_,
			IEslEntityManager eslEntityManager, 
			IEslDataStore dataStore,
			IServerProxy gateway) throws IllegalArgumentException {
		
		if (eslEntityManager == null || dataStore == null || gateway == null)
			throw new IllegalArgumentException("invalid value set for network controller initializer!");
		
		this.eslEntityManager = eslEntityManager;
		this.dataStore = dataStore;		
		
		try {			
			SET_TIME_INTERVAL_MILLIS = Integer.parseInt(System.getProperty("timesetter.interval", "7200"));
			SET_TIME_INTERVAL_MILLIS *= 1000;
		}
		catch (Throwable ignored) {}
		
		initialize_coordinator(portUrl_);
		
		this.server = ServerAdaptor.wrap(gateway, "upstream " + getPortName());
		
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + ".ESLNT(" + getPortName() + ")");
		
		nwkMap = new TreeMap<NetworkAddress, EUI64Address>();
		
		keepMessagesWhenOffline = (System.getProperty("keepMessagesWhenOffline") != null);
		
		local_auth = (
				System.getProperty("authorization", "").equalsIgnoreCase("local")
				|| System.getProperty("authorizeAll", "").equalsIgnoreCase("true"));
		
		if (log.isInfoEnabled()) {
			log.info("using {} ESL authorization", (local_auth ? "LOCAL" : "SERVER-CENTRALIZED"));
		}
		
		try {
			
			String _x = System.getProperty("removeAction", "leave").trim().toLowerCase();
			
			if (_x.equals("leave")) {
				removeAction = 0;
			}
			else if (_x.equals("kill")) {
				removeAction = 1;
			}
			else if (_x.equals("none")) {
				removeAction = 2;
			}
			
		}
		catch (Throwable ignored) {}
		
		sendUnauthorizedJoinAttemptMessages = (
				System.getProperty ("eslevent.unauthorized") != null);
		
		if (sendUnauthorizedJoinAttemptMessages) 
				log.info("ESL Network will send unauthorized join attempts");
		
		enableLeave = (System.getProperty("enableleave") != null);
		
		if (enableLeave) log.info("ESL Network controller set with Esls Leave/Kill Enabled!");
		
		persistentPriceUpdates = (System.getProperty("persistentPriceUpdate", "").equals("true"));
		
		if (log.isTraceEnabled()) 
			log.trace("Using  " 
					+ (persistentPriceUpdates ? "PERSISTENT priceupdates BUFFER :-)" : "VOLATILE priceupdates BUFFER ... :-("));
		
		authorizeAll = System.getProperty("authorizeAll", "false").toLowerCase().equals("true");
	    if (authorizeAll) log.warn("authorize all set to true, authorizing all ESLs!!! Dynamically assigning network addresses to all unknown ESLs!!!");
		
	    if (!local_auth && authorizeAll) local_auth = true;
		
	    if (System.getProperty("join.max", "").length() > 0) {
	    	try {
	    		MAX_REJOIN = Integer.parseInt(System.getProperty("join.max"));
	    		if (log.isTraceEnabled()) 
	    			log.trace("setting to {} maximum number of esls joined to this controller", MAX_REJOIN);
	    	}
	    	catch (Throwable xxx) {}
	    }
	    
		ApplicationServicesListener.instance().addJoinSubscriber(new IActiveQueueSubscriber<EslDeviceInfo>() {
			
			@Override
			public void elementFromQueue(EslDeviceInfo element) {
				
				if (element == null) return;
				if (element.getCoordinatorMac() == null) return;
				
				if (getAddress() == null) return;				
				
				EUI64Address mac = element.getMac();
				if (mac == null) return;
				
				if (getAddress().equals(element.getCoordinatorMac())) return;
				
				synchronized (proxyMap) {
					EslProxy p = proxyMap.get(mac);
					if (p != null) {
						if (log.isTraceEnabled()) log.trace("esl {} lost: joined to other coordinator, removing proxy ...", mac);
						if (p.getNetworkAddress() != null) {
							nwkMap.remove(p.getNetworkAddress());
							emptyBuffer(p.getNetworkAddress(), true);	//rimuovo dalla coda eventuali price update
						}
						proxyMap.remove(mac);												
					}
				}				
			}
		});
				
		// FIXME correct this after tests: set this as parameter
		fwUpdater = new EslFirmwareUpdater(this);

	}
	
	public void loadAssociatedEslsFromDatabase(EUI64Address address)
	{
		nwkMap.clear();
		proxyMap.clear();
		
		int max = 0;
		
		for(EslDeviceInfo s : eslEntityManager.getEsls().values()) {
			
			if(s.getNetworkAddress() != null 
					&& s.getCoordinatorMac() != null 
					&& s.getNetworkAddress().intValue() != 0
					&& !s.getNetworkAddress().isBroadcast()
					&& s.getCoordinatorMac().equals(address)) {				
				createEslProxy(s);	
				nwkMap.put(s.getNetworkAddress(), s.getMac());	
				if (max < s.getNetworkAddress().intValue()) max = s.getNetworkAddress().intValue();				 
			}
		}
		
		networkAddressCounter = (max > 0  ? new NetworkAddress(max) : new NetworkAddress(NetworkAddress.FIRST_NWK_ADDRESS - 1));
		
		//TBD: load data (outbound ...etc ...) :  ....
				
		log.info("Loaded " + (proxyMap.size() > 0 ? proxyMap.size() : "no") + " previously associated ESLs @{}", getPortName());
	}
	
	@SuppressWarnings("unchecked")
	public boolean sendApplicationFrame(EslApplicationFrame msg, NetworkAddress dest) {
		List<EslMessage> eMsgs = EslProxy.wrapApplicationFrame(msg, dest);
		for(EslMessage m : eMsgs) {
			if(!sendEslMessage(m, dest)) {
				return false;
			}
		}
		return true;
	}
	
	
	private EslProxy createEslProxy(EslDeviceInfo devInfo) {
		
		if (devInfo == null) {
			log.error("cannot create esl proxy without esl device info");
			return null;
		}
		
		if (devInfo.getCoordinatorMac() == null 
				|| !devInfo.getCoordinatorMac().equals(getAddress())) 
			return null;
		
		if (devInfo.getNetworkAddress() == null 
				|| devInfo.getNetworkAddress().intValue() <= 0) return null;
				
		EslProxy ep = null;
	
		final IEslNetwork _nc = this;

		synchronized (proxyMap) {
		
			ep = proxyMap.get(devInfo.getMac());
		
			if (ep == null) {
			
				ep = new EslProxy(devInfo, dataStore, this);
				
				if (eslListener == null) {
					
					eslListener = new IEslListener() {
						
						@Override
						public void onAlarm(EslProxy eslProxy, EslStatus status) {
							server.eslEvent(eslProxy.getMacAddress(), EslEventType.RAILALARM);
						}
						
						@Override
						public void onPriceUnknown(EslProxy eslProxy) {
							if ((sendUnauthorizedJoinAttemptMessages || authorizeAll)
									&& eslProxy.getAcknowledge() != null 
									&& eslProxy.getAcknowledge().getDeviceType() > 0) {
								server.eslEvent(eslProxy.getMacAddress(), EslEventType.PRICEUNKNOWN, null,
										"eslType=\"" 
												+ String.valueOf(eslProxy.getAcknowledge().getDeviceType()) 
												+ "\"");
							}
							else {
								server.eslEvent(eslProxy.getMacAddress(), EslEventType.PRICEUNKNOWN);
							}
						}
						
						@Override
						public void onStatusChanged(EslProxy eslProxy, EslStatus status) {
							server.eslStatus(eslProxy.getMacAddress(), status, _nc);
						}
						
						@Override
						public void onPriceUpdated(EslProxy eslProxy, long hashCode, String refId, boolean success) {
							server.sendEslPriceAcknowledge(eslProxy.getMacAddress(), hashCode, success ? CommandResult.SUCCESS : CommandResult.FAILURE, refId);										
						}
	
						@Override
						public void onStatistics(EslProxy eslproxy,
								EslStatistics statistics) {							
							server.sendEslStatistics(eslproxy.getMacAddress(), statistics);
						}
												
					};
					
				}
				
				ep.setListener(eslListener);
				
				proxyMap.put(devInfo.getMac(), ep);
				
				//update proxy map ...
				nwkMap.put(devInfo.getNetworkAddress(), devInfo.getMac());
				
			}
			
			ep.setDeviceInfo(devInfo);
	
		}
			
		return ep;
		
	}

	private synchronized NetworkAddress authorizeEsl(EUI64Address macAddr) {
		
		if(shutdown.get()) return NetworkAddress.BROADCAST_ADDRESS;
		
		NetworkAddress networkAddress = null;
			
		EslDeviceInfo eslInfo = eslEntityManager.getEslInfoByMac(macAddr);
		EslProxy eslProxy = proxyMap.get(macAddr);
		
		boolean managedYet = (eslInfo != null && eslProxy != null);
		
		if (eslInfo == null && !authorizeAll) 
		{
			if (sendUnauthorizedJoinAttemptMessages) { server.eslEvent(macAddr, EslEventType.UNAUTHORIZEDJOINATTEMPT); }
			log.info("ESL not authorized! (MAC: " + macAddr + ")");			
			return NetworkAddress.BROADCAST_ADDRESS;
		}
		
		if (!managedYet && nwkMap.size() > MAX_REJOIN) {
			log.error("ESL not authorized! (MAC: " + macAddr + "): too many ESLs associated to this coordinator {} !!!", nwkMap.size());
			if (sendUnauthorizedJoinAttemptMessages) { server.eslEvent(macAddr, EslEventType.UNAUTHORIZEDJOINATTEMPT); }
			return NetworkAddress.BROADCAST_ADDRESS;
		}
				
		if (!managedYet && local_auth)
		{	
			networkAddressCounter.increment();
			if (networkAddressCounter.intValue() == NetworkAddress.RESERVED1_ADDRESS) {
				networkAddressCounter.increment();
			}
			if (networkAddressCounter.intValue() > NetworkAddress.LAST_NWK_ADDRESS) {
				networkAddressCounter = new NetworkAddress(NetworkAddress.FIRST_NWK_ADDRESS);
			}
        		            	
            if (networkAddressCounter != null)
            {
            	if (log.isTraceEnabled()) {
            		log.trace ("new network address given to ESL {} -> 0x{}", 
            				macAddr, networkAddressCounter);
            	}
            	
            	NetworkAddress newnet = new NetworkAddress(networkAddressCounter.intValue());
            	
        	    if (eslInfo == null && authorizeAll) {
				
        	    	eslInfo = new EslDeviceInfo(macAddr);
        			eslInfo.setNetworkAddress(newnet);
        	    	
        	    	if (log.isTraceEnabled()) {
                		log.trace ("adding ESL {} to database ...", 
                				eslInfo.getMac());
                	}
        	    	
        	    	Collection<EslDeviceInfo> esls = new ArrayList<EslDeviceInfo>();
        	        esls.add(eslInfo);
        	        
        	        eslEntityManager.addEsls(esls);
        	        
        	    }
        	    else if (eslInfo != null) { 
        	    
        	    	eslInfo.setNetworkAddress(newnet);
        	    	
        	    }
            	else {
            	
            		log.error("execution not allowed here !!! CODE 89109W");      //this should be dead code, why compiler doesn't warn !!!????          		
            		return NetworkAddress.BROADCAST_ADDRESS;
            		
            	}
            	
            }
            else 
            {	                	
        		log.warn ("cannot assign network address to ESL {} !!! ", 
        				eslInfo.getMac()); 
        		return NetworkAddress.BROADCAST_ADDRESS;
            }
			
		}
		     
		networkAddress = eslInfo.getNetworkAddress();
		
		// some checks on network address validity ...
		
		if (networkAddress == null) {
			log.error("ESL not authorized! (MAC: " + macAddr + "): registered without short address!!!");
			if (sendUnauthorizedJoinAttemptMessages) { server.eslEvent(macAddr, EslEventType.UNAUTHORIZEDJOINATTEMPT); }
			return NetworkAddress.BROADCAST_ADDRESS;
		}					
		else if (networkAddress.intValue() == NetworkAddress.RESERVED1_ADDRESS) {
			log.error("ESL not authorized! (MAC: " + macAddr + "): invalid short address assigned: 0xBBBA (network reserved)");
			if (sendUnauthorizedJoinAttemptMessages) { server.eslEvent(macAddr, EslEventType.UNAUTHORIZEDJOINATTEMPT); }
			return NetworkAddress.BROADCAST_ADDRESS;
		}
		else if (networkAddress.intValue() > NetworkAddress.LAST_NWK_ADDRESS) {
			log.error("ESL not authorized! (MAC: " + macAddr + "): greater than last assignable 0x{}", NetworkAddress.LAST_NWK_ADDRESS);
			if (sendUnauthorizedJoinAttemptMessages) { server.eslEvent(macAddr, EslEventType.UNAUTHORIZEDJOINATTEMPT); }
			return NetworkAddress.BROADCAST_ADDRESS;
		}
		else if (networkAddress.intValue() < NetworkAddress.FIRST_NWK_ADDRESS) {
			log.error("ESL not authorized! (MAC: " + macAddr + "): minor than first assignable 0x{}", NetworkAddress.FIRST_NWK_ADDRESS);
			if (sendUnauthorizedJoinAttemptMessages) { server.eslEvent(macAddr, EslEventType.UNAUTHORIZEDJOINATTEMPT); }
			return NetworkAddress.BROADCAST_ADDRESS;
		}					
		
		// everithing ok, proceed 
		
		eslInfo.setNetworkAddress(networkAddress);
		
		boolean rejoin = (eslInfo.getCoordinatorMac() != null);
		
		eslInfo.setCoordinatorMac(getAddress()); 	
		
		createEslProxy(eslInfo).justJoined();

		EslEntityManager.instance().update(eslInfo);

		server.eslEvent(macAddr, (rejoin ? EslEventType.REJOINED : EslEventType.JOINED), getAddress());			
		
		if (log.isTraceEnabled()) 
		{
			log.trace (
					(rejoin ? "REJOIN ESL {} short address {}"
							: "JOIN ESL {} short address {}")  
							, macAddr, networkAddress);
		}				
		
		ApplicationServicesListener.instance().notifyJoin(eslInfo);
		
		return eslInfo.getNetworkAddress();
			
	}



	public void onMessage(EslMessage msg) {
		
		if(shutdown.get()) return;
		
		if(msg == null) return;
		NetworkAddress nwk = msg.getNetworkAddress();
		
		EslProxy esl = null;
		
		if(nwk != null) 
		{
			EslDeviceInfo d = EslEntityManager.instance().getEslInfoByMac(nwkMap.get(nwk));
			
			if (d == null) {
				log.error("Received EslMessage not associated to this coordinator {}, Esl NetworkAddress {}", 
						getAddress(), nwk.isBroadcast() ? "0xFFFF (BROADCAST!)" : "0x" + nwk.toString());
			}
			
			if ( d != null) esl = createEslProxy(d);
		} 
		else if (msg.getMacAddress() != null)
		{
			EslDeviceInfo d = EslEntityManager.instance().getEslInfoByMac(msg.getMacAddress());
			if (d == null) {
				log.error(
						"received EslMessage with Mac Address missing in database, Mac Address is {}", 
						msg.getMacAddress());
			}
			if ( d != null) esl = createEslProxy(d);
		}

		if(esl != null) {
			//if (esl.isJustJoined())	joinAckCont ++;
			esl.onMessage(msg);
//			EslStatus s = esl.getStatus();
//			if(s != null && fwUpdater != null)
//				fwUpdater.eval(s.getDeviceType(), s.getFirmwareVersion());
//// TODO this should be readded, but not before a thorough investigation of why this happens.
//// A hint may be to check if joined events are sent (to the server)..
////			if(networkManager.isOnline()) {
////				networkManager.reset();
////			}
////			cleanUpNetwork();
		} else {
			log.error("Received EslMessage not associated to any known Esl joined on this coordinator {}, PDU > {}", 
					getAddress(),
					Tools.toHexString(msg.getPduArray()));
			if(nwk != null && !nwk.isBroadcast()) {
				log.warn("Enqueueing leave command to ESL {}", nwk);
				sendEslLeave(nwk, false);
			}
		}
	}
	
	@Override
	public void evalFirmware(int deviceType, FirmwareVersion fwVersion)
	{
		fwUpdater.eval(deviceType, fwVersion);
	}
	
	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop.eslnet.IEslMessageTransmitter#sendEslMessage(com.s5tech.net.desktop.eslnet.NetworkAddress, com.s5tech.net.desktop.serialize.ISerializable)
	 */
	public boolean sendEslMessage(NetworkAddress esl, ISerializable data) {
		if (esl == null || esl.intValue() <= 0) {
			log.error("cannot send message to esl short address null or <= 0");
			return false;
		}
		EslMessage msg = new EslMessage();
		msg.setPdu(data);
		msg.setNetworkAddress(esl);
		return sendEslMessage(msg);
	}

	public boolean sendEslMessage(EslMessage msg, NetworkAddress addr) {
		if (addr == null || addr.intValue() <= 0) {
			log.error("cannot send message to esl short address null or <= 0");
			return false;
		}
		msg.setNetworkAddress(addr);
		return sendEslMessage(msg);
	}
	
	public boolean sendEslMessage(EslMessage msg) {
		if(shutdown.get() || msg == null || msg.getNetworkAddress() == null) return false;
		if (persistentPriceUpdates && msg.getNetworkAddress() != null && !msg.isLowPriority() && !msg.getNetworkAddress().isBroadcast()) {
			EslDeviceInfo d = eslEntityManager.getEslInfoByMac(nwkMap.get(msg.getNetworkAddress()));
			if (d == null || d.getMac() == null) 
				log.error("trying to set price update enqueued ... ooops cannot lookup esl from short address!!!");
			else dataStore.setPriceUpdateEnqueued(d.getMac());
		}
		return transmit(msg);
	}
	
	public boolean sendEslMessage(EUI64Address esl, ISerializable data) {		
		EslDeviceInfo eslinfo = EslEntityManager.instance().getEslInfoByMac(esl);		
		if(data == null || esl == null || eslinfo == null) return false;
		return sendEslMessage(eslinfo.getNetworkAddress(), data);
	}
	
	public boolean broadCastEslMessage(ISerializable data) {
		return sendEslMessage(NetworkAddress.BROADCAST_ADDRESS, data);
	}

	public boolean broadCastEslMessage(EslMessage msg) {
		msg.setNetworkAddress(NetworkAddress.BROADCAST_ADDRESS);
		return sendEslMessage(msg);
	}

	/**
	 * Creates a list of group addresses for a set of addresses.
	 * The group mask is the first three nipples of the network address. Ie: address 0x1234 belongs to network group 0x123(0)
	 * @param addresses
	 * @return
	 */
	static public Set<NetworkAddress> createGroupAddressList(Set<NetworkAddress> addresses) {
		
		Set<NetworkAddress> res = new HashSet<NetworkAddress>();
		for(NetworkAddress a : addresses) {
			NetworkAddress group = new NetworkAddress(a.intValue() & 0xFFF0);
			if(!res.contains(group)) res.add(group);
		}
		return res;
	}
	
	public void onMessage(ApplicationMessage msg) {
		
		if(msg == null || shutdown.get()) return;

		switch(msg.getType()) {
		
		case SET_STORE_KEY:
			/*
			 * 
			 * Set Store key, not implemented in ESL Firmware 
			 * 			 
			StoreKey sk = msg.getBody(StoreKey.class);
			if(sk != null) {
				if(storeKey == null || !storeKey.equals(sk)) {
					storeKey = sk;
					Persister.save(this);
				}
			}
			*/
			log.warn("Store key not managed");
			break;

		case ESL_MESSAGE:
			
			EslCommand eCmd = msg.getBody(EslCommand.class);
			if(eCmd != null) {
				handleEslCommand(eCmd);
			} else {
				log.warn("Wrong message body for application message type ESL_MESSAGE: ");
			}
			break;
			
		default:
			log.warn("Wrong application message: " + msg.getType());
		}
		
	}		

	
	private void handleEslCommand(EslCommand eCmd) {
		
		switch(eCmd.getCommand()) {
		
		case ESLPRICEUPDATE:
			
			if (eCmd.getEsls() == null) {
				log.warn("ESL Price update without esl list: command ignored");
				break;				
			}
			
			if(!(eCmd instanceof CoordinatorHelper)) {
				log.warn("ESL message contains wrong data type: " 
						+ eCmd.getData() == null ? "[none]" : eCmd.getData().getClass().getSimpleName());
				break;
			}

			CoordinatorHelper pu = CoordinatorHelper.class.cast(eCmd);
			EslPriceData price = pu.getPrice();
			
			if(price == null) {
				log.error("Got an empty price update!??");
				break;
			}
			
			{	
				Collection<EUI64Address> ls = (eCmd.isAllEsls() ? proxyMap.keySet() : eCmd.getEsls());				
				
				for(EUI64Address eMac : ls) {
					
					EslProxy ep = createEslProxy(EslEntityManager.instance().getEslInfoByMac(eMac));		
					
					if (ep != null) { // The alternative is not an error; 
									  // there may be a price update for an esl not joined on this coordinator
						
						if (log.isTraceEnabled())
						{
							log.trace(new StringBuffer("Got price for ESL ")
									.append(ep.getMacAddress()) 
									.append(" (hash: ") 
									.append(pu.getPrice().getHashWhenApplied()) 
									.append(") activation time: ").append(pu.getPrice().getActivationTime())
									.toString());
						}
						
						if (!keepMessagesWhenOffline && !isOnline()) 
						{
							log.warn("coordinator offline, cannot send price to esl {} / ShortAddr:{}", eMac, ep.getNetworkAddress());
							server.sendEslPriceAcknowledge(
									eMac, 
									pu.getPrice().getHashWhenApplied(), 
									CommandResult.NOROUTE,
									eCmd.getRefId());
						}
						else 
						{
							try 
							{
								ep.priceUpdate();
							}
							catch (Throwable t) 
							{
								log.error("Error enqueuing price update for ESL {} - reason : {}", ep.getMacAddress(), t);
							}
 						}
						
					}
					
				}
				
			}
		
			break;
			
		case ESLENTERNIGHTMODE:

			TimeData td = null;
			if(eCmd.getData() instanceof TimeData) {
				td = TimeData.class.cast(eCmd.getData());
				if(td != null) {
					nightModeData = td;					
				}
			}
			
			if (td == null) {
				log.error("Esl enter night mode command without time data, command ignored");
				return;
			}
			
			if(eCmd.isAllEsls())
			{
				sendSetNightMode(null);
			}
			else 
			{
				if(!eCmd.isAllEsls())
					log.error("ESLENTERNIGHTMODE without 'all' esls set, message not valid");	
			}		
			break;
			
		case ESLKILL:
		case ESLLEAVE:			
			
			if (eCmd.getEsls() == null) {
				log.warn("ESL kill without esl list: command ignored");
				break;
			}
			
			{
				Collection<EUI64Address> ls = (eCmd.isAllEsls() ? proxyMap.keySet() : eCmd.getEsls());
				Iterator<EUI64Address> it = ls.iterator();
				while (it.hasNext()) {	
					EUI64Address addr = it.next();
					if (proxyMap.get(addr) != null) {
						sendEslLeave(addr, (eCmd.getCommand() == MessageCommand.ESLKILL));
					}
				}
			}
			break;
			
		case ESLFIRMWAREUPDATE:			
			if (eCmd.isAllEsls()) 
			{
				FirmwareInfo fw = FirmwareInfo.class.cast(eCmd.getData());
				if (log.isTraceEnabled()) log.trace("Got ESL firmware {}, starting update ...", Tools.toStringObj(fw));
				fwUpdater.startUpdate(fw.getDeviceType(), fw.getVersion());				
			}
			break;
			
		case ESLSETALARMMODE:			
			log.warn("ESLSETALARMMODE no more supported");
			break;
			
		case ESLSTATISTICSREQUEST:			
			{
				Collection<EUI64Address> ls = (eCmd.isAllEsls() ? proxyMap.keySet() : eCmd.getEsls());
				Iterator<EUI64Address> it = ls.iterator();
				while (it.hasNext()) {	
					EUI64Address addr = it.next();
					sendStatisticsRequest(addr);
				}
			}
			break;
			
		case ESLSETCHANNELTOJOIN:			
			{
				Collection<EUI64Address> ls = (eCmd.isAllEsls() ? proxyMap.keySet() : eCmd.getEsls());
				Iterator<EUI64Address> it = ls.iterator();
				String [] p = ((String) eCmd.getData()).split(" ");
				int newChannel = Integer.parseInt(p[0]);
				int secondsToWait = Integer.parseInt(p[1]);				
				while (it.hasNext()) {	
					EUI64Address addr = it.next();
					sendSetChannelToJoin (addr, newChannel, secondsToWait);
				}
			}
			break;
			
		case ESLSETACTIVESERVICEPAGE:
			
			if(!eCmd.isAllEsls())
			{
				log.error("ESLSETACTIVESERVICEPAGE without 'all' esls set, message not valid");	
			}
			else {
				if(eCmd.getData() instanceof TimeData) {
					TimeData td1 = TimeData.class.cast(eCmd.getData());
					if(td1 != null) {
						sendSetActiveServicePage(null, td1.getDurationSecs());
					}
				}
				else {
					log.error("ESLSETACTIVESERVICEPAGE without time data set, message not valid");	
				}
			}
			break;
			
		case ESLSTATUSREQUEST:
			
			if (statusRequestProc == null || !statusRequestProc.isAlive()) 
			{		
				statusRequestAddressList = new TreeSet<NetworkAddress>();					
				
				if (eCmd.isAllEsls()) {
					Iterator<EslProxy> it = proxyMap.values().iterator();
					while (it.hasNext()) {
						EslProxy proxy = it.next();
						NetworkAddress na = (proxy == null ? null : proxy.getNetworkAddress());
						if (na != null) {
							statusRequestAddressList.add(na);							
						}
					}
				}
				else {
					Iterator<EUI64Address> it = eCmd.getEsls().iterator();
					while (it.hasNext()) {
						EUI64Address esl = it.next();
						EslProxy proxy = proxyMap.get(esl);
						NetworkAddress na = proxy != null ? proxy.getNetworkAddress() : null;
						if (proxy != null && na != null) {
							statusRequestAddressList.add(na);							
						}
					}
				}
				
				if (statusRequestAddressList.size() > 0) {
	 										
					int n = 15;						
					int secsToWait = 0;
					
					try { n = Integer.parseInt(System.getProperty("status.request.maxbuf", "15"));}
					catch (Throwable t) {}
					
					if (eCmd.getData() != null) {
						EslStatusRequestInfo rinfo = EslStatusRequestInfo.class.cast(eCmd.getData());	
						if (rinfo.hasMaxBuf()) n = (int)rinfo.getMaxBuf();	
						try { if (rinfo.hasSecsToWait()) secsToWait = (int)rinfo.getSecsToWait(); } catch (Throwable xxxx){}
					}
					
					if (n > 15 || n <= 0) n = 15;
					
					if (log.isInfoEnabled())
						log.info("Received Status Request for {} Esls, using {} of 15 maximum usable slots for pending status requests"  
								, eCmd.isAllEsls() ? "All" : statusRequestAddressList.size(), n);
					
					final int maxbuf = n;
					final int msTowait = secsToWait > 0 ? (secsToWait < 600 ? secsToWait * 1000 : 600 * 1000) : 0; 
					
					statusRequestProc = new Thread( new Runnable() {
						
						@Override
						public void run() {	
							
							if (!isOnline()) {
								log.warn("cannot start status request: coordinator offline");
								return;
							}
							
							long start = System.currentTimeMillis();
							while (numberOfPendingStatusRequests() != 0) {
								if (System.currentTimeMillis() -  start > 60000) {
									log.error("status request refused: previous pending status request already in progress");
									return;
								}
							}
							
							if (statusRequestAddressList == null || statusRequestAddressList.size() == 0) {
								log.warn("status request ignored: no ESLs in status request list");									
								return;
							}
							
							try {

								synchronized (statusRequestAddressList) {
									
									Set<NetworkAddress> groups = createGroupAddressList(statusRequestAddressList);						
									
									if (log.isTraceEnabled()) {
										log.trace(getPortName() + " Got ESLs in {} groups with {} managed esls",									
												(groups == null ? 0 : groups.size()), 
												(statusRequestAddressList == null || statusRequestAddressList.size() == 0 
														? "N/A": statusRequestAddressList.size()));
										if (groups != null && groups.size() > 0) {								
											StringBuffer buff = new StringBuffer();
											Iterator<NetworkAddress> it = groups.iterator();
											while (it.hasNext()) { buff.append(it.next()).append(" "); } 
											log.trace("{} sending status request to {} ...", getPortName(), buff.toString());
										}
									}
									
									if (groups != null && groups.size() > 0) {
										requestStatus(groups, maxbuf, msTowait);											
									}
								}

							}
							catch (Throwable t) {
								
								log.error("exception during status request: {}", t);
								
							}
							finally {
								
								//exit from status request but request will be scheduled in the future
								statusRequestAddressList = null;
								
							}
							
							//Wait request processing termination...
							
							try {
								while (getPendingStatusSize() > 0) {
									Thread.sleep(1000);
								}
								Thread.sleep(20000); //wait for all status returned after last request;
							}
							catch (Throwable t) {
								log.error("Error waiting Status request termination - {}", t);
							}
							
						}
					}, "StatusRequest@" + getPortName());		

					statusRequestProc.setDaemon(true);
					statusRequestProc.start();
					
				}
				else {
					
					log.info("no active Esls in list, status request ignored.", statusRequestAddressList.size());
				
				}
				
			}
			else 
			{
				log.warn ("status request already in progress, request rejected");
			}	
			break;
			
		case ADDESLLIST:			
			//
			// nothing to be done, because esls are not joined yet 
			//			
			break;
			
		case REMOVEESLLIST:			
			
			if(!eCmd.isAllEsls() && (eCmd.getEsls() == null || eCmd.getEsls().size() == 0)) {
				log.warn("remove esls without esl list: command ignored");
				return;				
			}
			
			if (eCmd.isAllEsls() && (
					EslEntityManager.instance().getEsls() == null ||
					EslEntityManager.instance().getEsls().size() == 0)) {
				log.warn("remove all esls, but no esls in database: command ignored");
				return;
			}
			
			synchronized (proxyMap) {
			
				Collection<EUI64Address> ls = (eCmd.isAllEsls() ?
						EslEntityManager.instance().getEsls().keySet()
						: eCmd.getEsls());
				{
					Iterator<EUI64Address> it = ls.iterator();
					
					while (it.hasNext()) {	
						
						EUI64Address addr = it.next();
						EslProxy p = proxyMap.get(addr);
						
						if (p != null) {
							
							if (removeAction == 0) {
								sendEslLeave(addr, false); //leave the esl
							}
							else if (removeAction == 1) {
								sendEslLeave(addr, true); //kill the esl
							}							
							
							proxyMap.remove(addr);		
							nwkMap.remove(p.getNetworkAddress());
														
						}
					}
				}
				
			}
				
			break;
					
		default:
			
			log.warn("ESL message type not handled here: " + eCmd.getCommand());
		
		}
		
//		Handlering af alarm mode, night mode, status report interval log timeout
		
	}
	
	
	@Override
	public boolean waitForStatusRequestEnd()
	{
		if (statusRequestProc == null) return false;
		try {
			if (!statusRequestProc.isAlive()) return true;
			statusRequestProc.join();
			return true;
		}
		catch (Throwable t) {
			log.error("error waiting for status request termination - {}", t);
			return false;
		}
	}
	
	
	@Override
	public boolean waitForFirmwareUpgradeEnd()
	{
		if (fwUpdater == null) return false;
		try {			
			while (fwUpdater.isUpdating()) {
				Thread.sleep(1000);
			}
			return fwUpdater.isUpgradeOk();
		}
		catch (Throwable t) {
			log.error("error waiting for firmware upgrade termination - {}", t);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private void sendSetNightMode(NetworkAddress esl) {
		
		if (proxyMap == null) return;		
		if (nightModeData == null) return;
		
		Date act = nightModeData.getActivationTime();
		
		if (act == null) act = new Date();
		ByteBuffer data = ByteBufferUtils.allocate(9);
		data.put((byte) 0x80);
		
		long actTime = act.getTime();
		actTime /= 1000L;
		actTime -= 946684800L;
				
		long dsecs = nightModeData.getDurationSecs();
		
		ByteBufferUtils.writeUInt32(actTime, data);
		ByteBufferUtils.writeUInt32(dsecs, data);
		data.flip();
		
		EslApplicationFrame eApp = new EslApplicationFrame();
		eApp.setCommand(EslApplicationCommand.SET_NIGHT_MODE);
		eApp.setPdu(data);
		
		List<EslMessage> eslMessageList = EslProxy.wrapApplicationFrame (eApp, esl);
		
		if(eslMessageList.size() > 0) {
			
			EslMessage eslMessage = eslMessageList.get(0);
			
			if (esl == null) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				SimpleDateFormat sdfutc0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		        sdfutc0.setTimeZone(TimeZone.getTimeZone("UTC-0"));
		        
				if (log.isInfoEnabled()) {
					log.info (new StringBuffer("broadcasting NIGHTMODE to all esls: ")
							.append(", LOCAL ACTIVATION TIME ")
							.append(sdf.format(act))
							.append(" (calculated ")
							.append(sdf.format(new Date((actTime + 946684800L) * 1000)))
							.append(" == ")
							.append(actTime).append("s from 01/01/2000")
							.append(", ESL ACTIVATION TIME ").append(sdfutc0.format(act))							
							.append(", LOCAL EXIT NIGHTMODE TIME ").append(sdf.format(new Date((actTime + 946684800L + dsecs) * 1000)))
							.append(", DURATION ").append(dsecs).append("s ")
							.toString());							
				}
				
				// Send it more than once to increase the possibility of the ELSs receiving the message
				for(int i=0 ; i < BROADCAST_SPAM_COUNT ; i++) {
					broadCastEslMessage(eslMessage);
				}
				
			}
			
		} else {
			
			log.error("No ESL messages generated from night mode application frame!?!?");
		
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void sendSetActiveServicePage(NetworkAddress esl, long durationSecs) {
		
		ByteBuffer data = ByteBufferUtils.allocate(4);
		
		long act = System.currentTimeMillis();
		act /= 1000L;
		act -= 946684800L;		
			
		long exitFromServicePageTime = act;
		exitFromServicePageTime += durationSecs;
		
		ByteBufferUtils.writeUInt32(exitFromServicePageTime, data);		
		data.flip();
		
		EslApplicationFrame eApp = new EslApplicationFrame();
		eApp.setCommand(EslApplicationCommand.SET_ACTIVE_SERVICE_PAGE);
		eApp.setPdu(data);
		
		List<EslMessage> eslMessageList = EslProxy.wrapApplicationFrame (eApp, esl);
		
		if(eslMessageList.size() > 0) {
			
			EslMessage eslMessage = eslMessageList.get(0);
			
			if(esl == null) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				SimpleDateFormat sdfutc0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		        sdfutc0.setTimeZone(TimeZone.getTimeZone("UTC-0"));
		        
				if (log.isInfoEnabled()) {
					log.info (new StringBuffer("broadcasting SERVICEPAGE to all esls: ")
							.append("LOCAL ACTIVATION TIME ")
							.append(sdf.format(new Date((act + 946684800L) * 1000)))
							.append(" (").append(act).append("s from 01/01/2000")
							.append(", ESL ACTIVATION TIME ").append(sdfutc0.format(new Date((act + 946684800L) * 1000)))							
							.append(", LOCAL EXIT SERVICE PAGE TIME ").append(sdf.format(new Date((exitFromServicePageTime + 946684800L) * 1000)))
							.append(", DURATION ").append(durationSecs).append("s ")
							.toString());							
				}
				
				// Send it more than once to increase the possibility of the ELSs receiving the message
				for(int i=0 ; i < BROADCAST_SPAM_COUNT ; i++) {
					broadCastEslMessage(eslMessage);
				}				
			} 
			
		} else {
			log.error("No ESL messages generated from night mode application frame!?!?");
		}
	}
	
	
	public EUI64Address getCoordinatorMac() {
		return getAddress();
	}
	
	public Set<EslSignalInfo> getAssociatedEslsSignal() {
		TreeSet<EslSignalInfo> res = new TreeSet<EslSignalInfo>();
		for (EslDeviceInfo dev : EslEntityManager.instance().getEsls().values()) {
			EslProxy esl = createEslProxy(dev);
			EslSignalInfo esi = new EslSignalInfo();
			esi.setMac(esl.getMacAddress());
			esi.setSignalLevel(new Percent(esl.getStatus() == null ? 0 : esl.getStatus().getLinkQuality()));
			res.add(esi);
		}
		return res;
	}	

	public void setChannelList(String chList, boolean send) {
		
		if(chList == null) {
			chList = DEFAULT_ALLOWED_CHANNELS;
			log.warn("given null allowed channels list, Using default channel list: " + DEFAULT_ALLOWED_CHANNELS);
		}
		
		allowedChannels = parseChannelList(chList);

		//StringBuffer sb = new StringBuffer();
		//for(Channel c : allowedChannels)
		//	sb.append(c + " ");
		//log.info("Channel list: {}", sb.toString());
		setDefaultAllowedChannels (allowedChannels);
		if (send) sendAllowedChannels();
	}

	public static Set<Channel> parseChannelList(String chList) {

		Set<Channel> res;
		if(chList.toLowerCase().indexOf("all") >= 0) {
			res = new HashSet<Channel>();
			for(int i=0; i<15; i++)
				res.add(new Channel(i+11));
		} else {
			String[] sChs = chList.split("[, ]");
			res = new HashSet<Channel>();
			for(int i=0; i<sChs.length && i<15 ; i++) {
				DecodeResult<Integer> val = Tools.tryParseInt(sChs[i].trim(), 10);
				if(val.success && val.value >= Channel.MIN && val.value <= Channel.MAX) {
					res.add(new Channel(val.value));
				}
			}
		}
		return res;
	}
	
	
	private void sendEslLeave(EUI64Address address, boolean isKill) 
	{	
		EslProxy ep = proxyMap.get(address); //createEslProxy(EslEntityManager.instance().getEslInfoByMac(address));
		if (ep == null || ep.getNetworkAddress() == null) return; //esl not managed here
		sendEslLeave(ep.getNetworkAddress(), isKill);
	}
		
	private void sendEslLeave (NetworkAddress addr, boolean isKill) {
		if (!enableLeave)
		{
			if (log.isInfoEnabled())
				log.info("Ignored {} (not enabled) to hort address {}", isKill ? "Kill" : "Leave", addr);
			return;
		}
		sendEslMessage(new EslLeaveMessage(addr, isKill), addr);		
		if (log.isTraceEnabled()) log.trace("Enqueued {} message for network address {}="
				+ (nwkMap.get(addr) != null ? nwkMap.get(addr).toString() : "NETADDR:::0x" + addr), (isKill ? "Kill" : "Leave"), addr);		
	}
	
		
	private void sendSetChannelToJoin(EUI64Address address, long channel, long secsToWait) {
		
		EslProxy ep = createEslProxy(EslEntityManager.instance().getEslInfoByMac(address));
		if (ep == null || ep.getNetworkAddress() == null) return; //esl not managed here
				
		ByteBuffer buff = ByteBufferUtils.allocate(5);
		ByteBufferUtils.writeUnsignedInteger(channel, buff, 1, false);
		ByteBufferUtils.writeUInt32(secsToWait, buff);
		buff.flip();
		
		EslApplicationFrame frame = new EslApplicationFrame();		
		frame.setCommand(EslApplicationCommand.SET_CHANNEL_TO_JOIN);
		frame.setPdu(buff);
		
		sendApplicationFrame(frame, ep.getNetworkAddress());
		
		if (log.isTraceEnabled())
		log.trace("Sent channel {} to join to {}", channel, address);
	}
	
	
	public void sendStatisticsRequest(EUI64Address address) {
		
		EslProxy ep = createEslProxy(EslEntityManager.instance().getEslInfoByMac(address));
		if (ep == null || ep.getNetworkAddress() == null) return; //esl not managed here
				
		ByteBuffer buff = ByteBufferUtils.allocate(5);
		ByteBufferUtils.writeUnsignedInteger(0, buff, 1, false);
		ByteBufferUtils.writeUInt32(0, buff);
		buff.flip();
		
		EslApplicationFrame frame = new EslApplicationFrame();		
		frame.setCommand(EslApplicationCommand.READ_STATISTICS);
		frame.setPdu(buff);
		
		sendApplicationFrame(frame, ep.getNetworkAddress());
		
		if (log.isTraceEnabled())
		log.trace("Sent statstic requests {} ", address);
		
	}
		
	
	@Override
	public void elementFromQueue(EslCommand cmd) {
		handleEslCommand(cmd);
	}
	
	@Override
	public String toString() 
	{
		StringBuffer buff = new StringBuffer();
		buff.append(" controller@").append(getPortName())									
				.append(" coordinator: ").append(getAddress());
		return buff.toString();
	}

	@Override
	public int getAssociatedEsls() {		
		return proxyMap.size();
	}

	@Override
	public EslProxy getEslProxy(EUI64Address mac) {
		return proxyMap.get(mac);
	}

	/**
	 * String format:<br>
	 * <pre>
	 * [None|Upgrading|Success|Failure] timestamp device version count total transmissionCount transmissionSize 
	 * </pre>
	 */
	@Override
	public EslFirmwareUpdater getFirmwareUpdater() {
		return fwUpdater;
	}

	@Override
	public void setReconnectTimeoutMs(int ms) {
		setReconnectoTimeoutMs(ms);		
	}
	
	@Override
	public int getReconnectTimeoutMs() {
		return getReconnectoTimeoutMs();		
	}

	@Override
	public Set<EUI64Address> getAssociatedSet() {
		return proxyMap.keySet();
	}

	@Override
	public void onScanprobe(EUI64Address esl) {
		server.eslEvent(esl, EslEventType.SCANPROBE, getAddress());			
	}
	
	@Override
	public String getChannelList() {
		StringBuffer b = new StringBuffer();
		for (Channel c : allowedChannels) {
			if (b.length() > 0) b.append(',');
			b.append(c.getValue());			
		}
		return b.toString();
	}

	@Override
	public EUI64Address getAssociatedAddress(NetworkAddress nwk) {
		try {
			return nwkMap.get(nwk);
		}
		catch (Throwable t) {
			return null;
		}
	}

	@Override
	public int restorePriceUpdates() {
		int count = 0;
		try {			
			if (log.isTraceEnabled()) log.trace("searching for previously sent EslPriceUpdate without acknowledge reply ...");
			for (EUI64Address e : dataStore.getEnqueuedEslMessagesList())
			{
				EslProxy ep = getEslProxy(e);
				if (ep != null && isOutboundMissing(ep.getNetworkAddress())) {
					if (log.isTraceEnabled()) log.trace("resubmitting price update for ESL {} ...", e);
					ep.priceUpdate();
					count ++;
				}
			}
			if (log.isInfoEnabled() && count > 0) log.info("resubmitted {} price updates - coordinator address: " 
					+ getAddress() 
					+ " - channel: " 
					+ getActiveChannel(), count);
		}
		catch (Throwable t) {
			log.error("exception restoring price updates {}", t);			
		}
		return count;
	}

	@Override
	public void setPriceUpdateSent(NetworkAddress nwk) {
		if (!persistentPriceUpdates) return;
		try {
			if (nwk == null || nwk.isBroadcast()) return;
			EslDeviceInfo d = eslEntityManager.getEslInfoByMac(nwkMap.get(nwk));
			if (d == null) throw new Exception("cannot lookup esl from short address");
			if (dataStore != null) dataStore.setEslMessageSent(d.getMac());
		}
		catch (Throwable t) {
			log.error("error marking esl message sent for short address {} - {} ", nwk, t);
		}
	}

	@Override
	public void shutdown() {
		try 
		{
			if (log.isTraceEnabled()) log.trace("shutting down network controller ...!");
			shutdown.set(true);			
			Collection<EUI64Address> esllist = (proxyMap != null ? proxyMap.keySet() : null);
			if (getAddress() != null && esllist != null && esllist.size() > 0)
			{
				eslEntityManager.updateAssociation(getAddress(), esllist);
			}						
		}
		catch (Throwable t) {
			log.error("error shutting down network controller - {}", t);
		}
	}

	@Override
	public void emptyBuffer(NetworkAddress nwk, boolean highPriorityBuffer) {
		if (nwk == null) return;
		try {
			emptyBuffer_(nwk, highPriorityBuffer);
		}
		catch (Throwable t) {
			log.error("flushing netwrok controller buffer for address {} - {}", nwk.toString(), t);
		}		
	}

	@Override
	public boolean sendEslApplicationMessage(
			List<EslTransportFrame> frames, 
			ITransmissionListener<EslMessage> listener,
			NetworkAddress destination) {
		return transmitEslApplicationMessage(frames, listener, destination);
	}
	
	// low level controller 
	
	/**
	 * The underlying main coordinator proxy of a network controller,
	 * invoked by network controlle constructor  
	 * @param serialPortFacade must be set    
	 * @param eslEntityManager
	 *            only needed for test: {@link EslEntityManager} will be used if
	 *            this is null
	 */
	private void initialize_coordinator (String serialPortName_) {
		
		port = new SerialPortFacade(serialPortName_);		
		iolog = LoggerFactory.getLogger(ISystemKeys.APPLICATION + ".COORD(INIT)@" + port.getSerialPortName());
				
		outQueue = new PriorityBlockingQueue<Hub2EslNetCtrlFrame>();
		
		networkManager = ApplicationServicesListener.instance();
		addObserver(networkManager);
		
		pendingStatus = new LinkedList<NetworkAddress>();
		
		int jmaxdelay = -1;
		try { jmaxdelay = Integer.parseInt(System.getProperty("join.max.delay", "-1")); }
		catch (Throwable t) { if (iolog.isDebugEnabled()) iolog.debug("cannot parse join.max.delay: {}", t); }
		if (jmaxdelay > 0 && jmaxdelay < MIN_FRAME_INTERVAL_MILLIS) jmaxdelay = MIN_FRAME_INTERVAL_MILLIS;
		outqueueThreshold = (jmaxdelay > 0 ? jmaxdelay / MIN_FRAME_INTERVAL_MILLIS : 0);
		
		autocorrectTime = System.getProperty("autocorrectTime", "").equals("true");
		
		if (iolog.isInfoEnabled()) iolog.info("creating standard coordinator proxy"); 
				
		if (outqueueThreshold > 0) {
			if (iolog.isInfoEnabled()) 
				iolog.info("setting join.max.delay to {} ms (=> outqueue max size = {}) to avoid outqueue flooding caused by total rejoins", 
					jmaxdelay, outqueueThreshold);	
		}
		else {
			iolog.warn("join.max.delay not set, possible deadlocks in massive total rejoin...! :-(");	
		}
		
		if (iolog.isTraceEnabled() && autocorrectTime) 
			iolog.trace("enforcing time correction ...");		
		
		outbound = new LinkedHashMap<NetworkAddress, LinkedList<EslMessage>>();
		outboundLowPriority = new LinkedHashMap<NetworkAddress, LinkedList<EslMessage>>();
		
		bufferSemaphore = new Semaphore(1);
		bufferSemaphore.drainPermits();
		eslMessageTransmitter = new Thread(this, "Buffer transmitter " + port.getSerialPortName());
		eslMessageTransmitter.setDaemon(true);
		eslMessageTransmitter.start();
		
		// initialize serial io writer task 
		tOutQueue = new Thread(new Runnable() {
			
			long prevTransmit = 0;
			
			public void run() {
								
				Hub2EslNetCtrlFrame frame = null;
				
				try {
					
					while(true) 
					{
						frame = outQueue.take();
						
						if(frame != null) {
							
							// Wait for the serial port to be online again
							if(!serialPortOnline) {
								
								Tools.doWait(onLineWaitObject, 0);
								
								// Remove all firmware data and reset command messages after a reset..
								Set<Hub2EslNetCtrlFrame> rm = new HashSet<Hub2EslNetCtrlFrame>();
									
								for(Hub2EslNetCtrlFrame msg : outQueue) 
								{
									if(Hub2EslNetCtrlCommand.FIRMWARE_DATA.equals(msg.getCommand()) || Hub2EslNetCtrlCommand.RESET.equals(msg.getCommand())) {
										rm.add(msg);
									}
								}
								
								if(rm.size() > 0) 
								{
									iolog.trace("Removing {} messages from outqueue", rm.size());
									outQueue.removeAll(rm);
								}
							
								if(Hub2EslNetCtrlCommand.FIRMWARE_DATA.equals(frame.getCommand()) 
										|| Hub2EslNetCtrlCommand.RESET.equals(frame.getCommand())) 
								{
									frame = null;
									continue;
								}
							}
							
							long delay = MIN_FRAME_INTERVAL_MILLIS - (System.currentTimeMillis() - prevTransmit);
							
							if (delay > 0) Tools.doWait("", delay);
							if (!transmitDirect(frame)) {
								mySerialListener.onOffline();
								setChanged();
								notifyObservers("net;off;" + port.getSerialPortName());
							}
							
							prevTransmit = System.currentTimeMillis();
						}
						
					}
					
				} 
				catch (InterruptedException e) 
				{
					iolog.warn("out queue transmitter interrupted, shutting down thread ... {}", (e.getMessage() == null ? "" : e.getMessage()));
				}
				
				iolog.trace("exit from out queue thread at " + port.getSerialPortName());
				
			}
			
		}, 
		"Coord outqueue@" + port.getSerialPortName());
		
		tOutQueue.setDaemon(true);
		tOutQueue.start();
		
		inQueue = new ActiveQueue<Hub2EslNetCtrlFrame>(
				new IActiveQueueSubscriber<Hub2EslNetCtrlFrame>() {
					public void elementFromQueue(Hub2EslNetCtrlFrame frame) {
						onFrame(frame);
					}
				}, "Coord inQueue@" + port.getSerialPortName());
		
		latestFirmware = FirmwareLibrary.instance().getFirmwareForDeviceType(DeviceType.COORDINATOR);
		
		FirmwareLibrary.instance().addSubscriber(DeviceType.COORDINATOR, new IFirmwareUpdateSubscriber() {
			public void onNewFirmware(FirmwareInfo fw) {
				if(latestFirmware == null || (fw != null && fw.getVersion().compareTo(latestFirmware.getVersion()) > 0)) {
					//TODO prepare and send firmware
				}
			}
		});
		
	}
	
	/**
	 *  This method is responsible for sending ESL messages by evaluating the buffer information received from the coord
	 *  It's important that the semaphore used is only released when new bufferinfo is received from the 
	 */
	public void run() {

		int count, sent, maxbuf = 15;
		
		while(true) {
			
			try {
			
				
				// wait for semaphore to be released
				bufferSemaphore.acquire();
				
				if (bufferInfo == null) continue;
				
				BufferInformation binfo = new BufferInformation();
				
				synchronized (bufferInfo) {
					binfo.copyFrom(bufferInfo);
				}
				
				LinkedHashMap<NetworkAddress, LinkedList<EslMessage>> map = null;
				
				for (int _i = 0; _i < 2; _i++)
				{
					if (_i == 0) map = outbound;
					else if (_i == 1) map = outboundLowPriority;

					if(!map.isEmpty()) {
						
						if(!iolog.isTraceEnabled())
							iolog.trace("Evaluating free buffers for {}: {}", 
									(_i == 0 ? "HIGH priority outbound messages" : "LOW priority outbound messages"), 
											binfo);
						
						// broadcast...
						
						count = binfo.getFreeSlotsInBroadcastDataQueue();
						sent  = sendEslMessages(map, NetworkAddress.BROADCAST_ADDRESS, count);
						if(sent > 0) {
							iolog.trace("Sent {} broadcast messages", sent);
							binfo.setFreeSlotsInBroadcastDataQueue (count - sent);
						}
	
						// esl destination previously assigned ...
						Set<NetworkAddress> sentTo = new HashSet<NetworkAddress>();
						// Find the assigned buffers and send to those

						for(TimeslotBuffer b : binfo.getBuffers()) {
							if(!b.isUnassigned()) {
								
								sent = sendEslMessages(map, b.getAddress(), b.getFreeSlotsInDataQueue());
								b.removeFreeSlotsInDataQueue(sent);
								sentTo.add(b.getAddress());
								
								//rimuovo l'item dalla stop & go persistence price list su DB
								if (_i == 0
											&& sent > 0 
											&& b.getAddress() != null
											&& !map.containsKey(b.getAddress())) {
										setPriceUpdateSent(b.getAddress());
								}									
																
							}
						}
						
						// esl destination not assigned yet ...												
						
						for(TimeslotBuffer b : binfo.getBuffers()) {
							
							if(b.isUnassigned()) {
								
								NetworkAddress addr = null;
								// Find a network address not sent to
								
								outboundLock.lock();
								try {
									for(NetworkAddress a : map.keySet()) {
										if(NetworkAddress.BROADCAST_ADDRESS.equals(a)) continue; // Don't send broadcasts here
										if(!sentTo.contains(a)) {
											addr = a;
											break;
										}
									}
								}
								catch (Throwable ignored){}								
								outboundLock.unlock();
								
								if(addr != null) { // Found messages for an ESL
									sent = sendEslMessages(map, addr, b.getFreeSlotsInDataQueue());									
									b.setAddress(addr);
									b.removeFreeSlotsInDataQueue(sent);
									sentTo.add(b.getAddress());
									
									//rimuovo l'item dalla stop & go persistence price list su DB
									if (_i == 0
												&& sent > 0 
												&& b.getAddress() != null
												&& !map.containsKey(b.getAddress())) {
											setPriceUpdateSent(b.getAddress());
									}	
									
								}
							}
						}
						
					}
					
				}
				
				
				if(!pendingStatus.isEmpty()) {
					
					try {
						maxbuf = Integer.parseInt(System.getProperty("status.request.maxbuf", "15"));				
					}
					catch (Throwable t) {
						maxbuf = 15;
					}
					
					if (maxbuf > 15 || maxbuf == 0) maxbuf = 15;
						
					if (maxbuf > binfo.getFreeSlotsInPendingStatusCmdQueue())
						maxbuf = binfo.getFreeSlotsInPendingStatusCmdQueue();
					
					sendStatusPollingRequests (maxbuf);
					
					if (pendingStatus.isEmpty()) 
					{
						if (iolog.isTraceEnabled()) 
							iolog.trace("status request terminated, no more pending status.");
					}
				}
					
				//}
				
			} catch (InterruptedException e) {
				iolog.warn("Buffer transmitter interrupted, thread shutdown ... {}", e.getMessage() == null ? "" : e.getMessage());
				// If interrupted, assume that the thread should be shut down
				break;
			}
		}
		
	}

	/**
	 * Send the messages to a specific ESL, or the first maxCount messages if list.size() > maxCount
	 * 
	 * @param list the list of messages to send
	 * @param maxCount the maximum number of messages to send
	 * @return the number of messages actually sent
	 */
	private int sendEslMessages(LinkedHashMap<NetworkAddress, LinkedList<EslMessage>> map, NetworkAddress addr, int maxCount) {

		if(addr == null || !map.containsKey(addr) || maxCount <= 0) return 0;

		int count = 0;

		outboundLock.lock();
		
		try {
			
			LinkedList<EslMessage> list = map.get(addr);
			
			if (list == null) return 0;
			
			if (iolog.isTraceEnabled() && addr != null) 
			{
				String a = addr.toString();
				a += "=";
				a += (addr.isBroadcast() ? "BROADCAST" : 
					(getAssociatedAddress(addr) != null ? 
							getAssociatedAddress(addr).toString() : "N/F:0x" + addr.toString() + ":LOST?"));
				iolog.trace("To " + a + ": sending " + list.size() + " messages (max " + maxCount + ")");
			}
			
			synchronized (list) {
				while(count < maxCount && !list.isEmpty()) {
					
					EslMessage eslMessage = list.poll();
				
					if (eslMessage instanceof EslLeaveMessage) 
					{
						outQueue.add(((EslLeaveMessage) eslMessage).getFrame());
					}
					else 
					{
						outQueue.add (new Hub2EslNetCtrlFrame (Hub2EslNetCtrlCommand.ESL_MESSAGE, eslMessage, eslMessage.getPriority()));
					}
				
					eslMessage.transmitted();
					count++;
				}
			}
			
			if (list.isEmpty()) 
			{ 
				map.remove(addr); // If all messages sent remove the ESL from the outbound list
			} 
			else if (iolog.isTraceEnabled()) 
			{
				iolog.trace("Remaining messages for " + addr + ": " + list.size());
			}
		}
		catch (Throwable err) {
			iolog.error("ESL transport layer exception - {}", err);
		}
		finally {
			outboundLock.unlock();
		}
		return count;
	}
	
	private void fetchControllerInfo() {
		fetchAddress();
		fetchChannel();
		fetchFirmwareVersion();
		fetchBufferInfo();
	}
	
	public void stop()
	{
		try {
			if (mySerialListener != null) mySerialListener.onOffline();
			port.stop();
		}
		catch (Throwable ignored) {}
	}
	
	public boolean connect() {
		
		boolean ok = false;
		allowedChannels_ = defaultAllowedChannels;
		
		first_init_done = false;
		address = null;
		activeChannel = null;
		
		if (iolog.isTraceEnabled()) iolog.trace("coordinator proxy reconnecting ...");
		
		if (mySerialListener == null) {

			activeChannel = null;
			firmwareVersion = null;
			address = null;

			iolog.info("Creating serial port listener");
			
			mySerialListener = new ISerialListener() {

				public void onOnline() {
					iolog.info("Serial port online");
					inQueue.clear();
					serialPortOnline = true;
					log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + ".COORD(" + address + ")");
					
					// Notify everyone waiting for online state
					Tools.doActivate(onLineWaitObject);
					
					//needed?
					fetchControllerInfo();
					sendAllowedChannels();
					
				}

				public void onOffline() {
					inQueue.clear();
					serialPortOnline = false;					
					log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + ".COORD(OFFLINE)@" + port.getSerialPortName());					
				}

				public void onData(ByteBuffer data) {
					if (!serialPortOnline)
						onOnline();

					Hub2EslNetCtrlFrame hFrame = SerializeFactory.read(data, Hub2EslNetCtrlFrame.class);
					if (hFrame != null) {
						//log2.debug("********* [" + hFrame.toString() + "]");
						inQueue.add(hFrame);
					}						
					else
						iolog.warn("Unable to deserialize coord ctrl frame from serial data: {}", Tools.toString(data));
				}
			};
			
			port.setListener(mySerialListener);
		}

		if (!port.isRunning()) {
			ok = port.start();
		}
		else if (port.isOnline()) {
			mySerialListener.onOnline();
			ok = true;
		}

		//enabled = ok;
		return ok;
		
	}

	private void checkFirstInitailization()
	{
		if (!first_init_done && activeChannel != null && allowedChannels_.contains(activeChannel) && address != null)
		{
			
			first_init_done = true;
			
			if (autocorrectTime) 
			{ 
				if (iolog.isTraceEnabled()) iolog.trace("coordinator channel ok! setting time ...");
				setTime(); 
			}
			
			if (timeSetter == null) 
			{
				iolog.info("Starting time setter. Transmission interval: " + SET_TIME_INTERVAL_MILLIS / 1000L + " secs");
				timeSetter = new Timer("Coord time setter " + port.getSerialPortName(), true);
				timeSetterTask = new TimerTask() {
					@Override
					public void run() {
						if (isOnline()) setTime();
					}
				};								
				timeSetterTask.scheduledExecutionTime();
			}
			
			if (System.getProperty("persistentPriceUpdate", "").equals("true")) {
				restorePriceUpdates();
			}
		}	
	}
	
	private void onFrame(Hub2EslNetCtrlFrame frame) {

		Hub2EslNetCtrlCommand cmd = frame.getCommand();
		
		if (cmd == null) {
			iolog.warn("No Hub2EslNetCtrlCommand set!!");
			return;
		}
		
		switch (cmd) {
		
		case IEEE_EUI:
			
			if (frame.getPdu().length() >= EUI64Address.LENGTH) {
				
				EUI64Address addr = frame.getPduAs(EUI64Address.class);
				
				if (addr == null) {
					
					iolog.error("Unable to read an IEEE EUI from the Hub2EslNetCtrl frame: {}", frame);
					
				} else {
					
					boolean toLoad = (address == null);
					
					if (!addr.equals(address)) {
						
						if (address != null) {
							iolog.warn("Previous and current EUI mismatch. Old: "
									+ address + ", New: " + addr);
							// TODO should we act on this?
							toLoad = true;
						}
						address = addr;
						
						log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + ".COORD(" + address + ")");						
						
						setChanged();
						notifyObservers("net;address;" + port.getSerialPortName() + ";" + address);	 
						
						String ac = System.getProperty("reservation.mac." + address.toString());
						if (ac != null) {
							this.allowedChannels_ = EslNetworkController.parseChannelList(ac);
						}
						else {
							this.allowedChannels_ = defaultAllowedChannels;
						}
						
						checkFirstInitailization();
						
					}
					
					iolog.info("Registered ESL Network Coordinator with IEEE EUI: {}", address);
					if (toLoad) loadAssociatedEslsFromDatabase(address);					
				}
				
			}			
			break;
			
		case ACTIVE_CHANNEL:
			
			byte[] pduArray = frame.getPduArray();
			
			if (pduArray != null && pduArray.length > 0) {
				
				int cInt = Tools.uByteToInt(pduArray[0]);
				Channel ch = new Channel(cInt);
				
				if (cInt == 0) {
					
					//log2.warn("waiting for new allowed coordinator channel ...");
					activeChannel = null;
					
				} else {
					
					activeChannel = ch;
					iolog.info("Active ESL Network channel {}", activeChannel);
					
					// Resend the list of allowed channels if the active channel
					// is not in the list.
					if (activeChannel == null
							|| (allowedChannels_ != null && !allowedChannels_
									.contains(activeChannel))) {
						
						iolog.warn("The active channel is not is the list of allowed channels; resending channel mask");
						sendAllowedChannels();
						
					}
					else
					{
						
						//TODO check timeout enabled ?
						setChanged();
						notifyObservers("net;channel;" + port.getSerialPortName() + ";" + activeChannel);	
						
						//network.onOnline(address, port.getSerialPortName(), activeChannel);
						// The time is set here, as we are sure that the coord is online, and the time is not useful until there's actually a network.
						
						checkFirstInitailization();										
						
					}
					
				}
				
			} else {
				iolog.warn("Invalid data: " + Tools.toHexString(pduArray));
			}
			break;
			
		case CHANNEL_MASK:
			iolog.debug("Channel mask: " + Tools.toString(frame.getPduArray()));
			break;
			
		case FIRMWARE_VERSION:
			ByteBuffer b = frame.getPduAsBuffer();
			if (b == null || b.remaining() < 4) {
				iolog.warn(
						"Too few bytes for coordinator firmware version: {}", 
						(b != null ? Tools.toHexString(b.array(), b.position(), b.remaining()): "null packet!"));
				break;
			}
			
			firmwareVersion = new FirmwareVersion();
			firmwareVersion.read(b, firmwareVersion.length());			
			
			if (iolog.isTraceEnabled())
				iolog.trace("Coordinator Firmware Version {}", firmwareVersion.toString());
				
			break;	
			
		case ESL_MESSAGE:
			
			if (address == null) return; //coordinator offline
			
			EslMessage msg = frame.getPduAs(EslMessage.class);
			
			//if (log2.isTraceEnabled()) log2.trace("got esl message frame > {}", frame.toString());
			
			if (msg == null) {
				iolog.error("Unable to read an EslMessage from the Hub2EslNetCtrl frame: " + frame);
				break;
			}

			onMessage(msg);			
			break;
			
		case NUM_FREE_BUFFERS:
			
			BufferInformation bInfo = frame.getPduAs(BufferInformation.class);
			if (bInfo == null) {
				iolog.error("Unable to read the number of free buffers from the Hub2EslNetCtrl frame: {}", frame);
				break;
			}
			
			if(iolog.isTraceEnabled()) 
				iolog.trace("Free buffers: {}",bInfo.toString());
			
			synchronized (bufferInfo) {
				bufferInfo.copyFrom(bInfo);
			}
			
			if (bufferSemaphore.hasQueuedThreads()) {	
				bufferSemaphore.release();				
			}
			
			if (address != null && activeChannel != null) 
			{
				setChanged();
				notifyObservers("net;alive;" + port.getSerialPortName() + ";" + activeChannel + ";" + address);	 
				checkFirstInitailization();
			}
			
			break;
			
		case VALIDATE_DEVICE_AUTHORIZATION:
			
			if (address == null) return;
			
			if (frame.getPdu().length() >= EUI64Address.LENGTH + 1) {
				
				EUI64Address addr = frame.getPduAs(EUI64Address.class);
				if (addr == null) {
					iolog.error("Unable to read an IEEE EUI from the Hub2EslNetCtrl frame: {}", frame);
					break;
				}
				
				if (addr.getLongValue() == 0 && iolog.isTraceEnabled()) {
					// ignored spam from coordinator
					iolog.trace("ignored device authorization from zero address (channel jamming?)");
					break;
				}
				
				EslCapabilityInformation cInfo = frame.getPduAs(EslCapabilityInformation.class);
				if (cInfo == null) {
					iolog.error("Unable to read the ESL capability information from the Hub2EslNetCtrl frame: {}", frame);
					break;
				}

				Hub2EslNetCtrlFrame reply = new Hub2EslNetCtrlFrame(
						Hub2EslNetCtrlCommand.DEVICE_AUTHORIZED);
				reply.setPriority(Hub2EslNetCtrlFrame.PRI_HIGH);
				ByteBuffer data = ByteBufferUtils.allocate(10);
				addr.write(data);
				
				if (iolog.isTraceEnabled()) 
					iolog.trace("REQUEST VALIDATE FOR {}", addr);
				
				NetworkAddress nwkAddr = authorizeEsl(addr);
						
				if (nwkAddr != null) { // To avoid NPE (probably due to spam from coordinator)
					nwkAddr.write(data);
					data.flip();
					reply.setPdu(data);
					transmit(reply);
				}
				
			}
			break;
			
		case SCAN_PROBE_NOTIFY:

			if (address == null) return;	
			
			if (frame.getPdu().length() >= EUI64Address.LENGTH) {
				
				EUI64Address mac = (EUI64Address) frame.getPduAs(EUI64Address.class);
				if (logScanprobes.isTraceEnabled() && mac != null && mac.getLongValue() != 0)
					logScanprobes.trace(
							new StringBuffer()
								.append((address == null ? "n/a" : address.toString())).append(';')
								.append((activeChannel == null ? "n/a" : activeChannel.toString())).append(';')
								.append((mac == null ? "n/a" : mac.toString())).toString());				
			}
			break;
			
		default:
			iolog.debug("Unsupported Hub2EslNetCtrlCommand command: " + cmd);
		}
	}
	
	public int getPendingStatusSize() 
	{
		return (pendingStatus == null ? 0 : pendingStatus.size());
	}
	
	
	private void sendStatusPollingRequests(int maxCount) {

		if(pendingStatus.isEmpty() || maxCount <= 0) return;
		
		List<String> trace = null;
		int count = 0;
		
		synchronized (pendingStatus) {
			
			while(!pendingStatus.isEmpty() && count < maxCount) 
			{
				count++;
				
				NetworkAddress group = pendingStatus.poll();
				if(iolog.isTraceEnabled()) {
					if(trace == null) trace = new ArrayList<String>();
					trace.add(group.toString());
				}
				
				transmit(new Hub2EslNetCtrlFrame(
						Hub2EslNetCtrlCommand.PENDING_STATUS, 
						group, 
						Hub2EslNetCtrlFrame.PRI_LOW));
				
			}
		}
		
		if(count > 0) {
			if(iolog.isTraceEnabled()) 
				iolog.trace("Sent status requests for {} groups: {}", 
						count, 
						Tools.listToString(trace, ","));
		}
		
	}


	public void requestStatus(Collection<NetworkAddress> groups) {
		if(groups == null) return;
		synchronized (pendingStatus) {
			for(NetworkAddress n : groups) {
				if(!pendingStatus.contains(n))
					pendingStatus.add(n);
			}
		}
		iolog.trace("Requesting status for {} address groups", groups == null ? 0 : groups.size());
	}

	
	public void setReconnectoTimeoutMs(int timeoutMs)
	{
		port.setReconnectTimeoutMs(timeoutMs);
	}
	
	public int getReconnectoTimeoutMs()
	{
		return port.getReconnectTimeoutMs();
	}
	
	public boolean isOnline() {	
		if (!serialPortOnline) return false;
		if (bufferInfo == null && serialPortOnline) return true; //initializing ? ...
		return serialPortOnline && address != null;
	}

	
	public void requestStatus(Collection<NetworkAddress> groups, int maxbuf, int msToWait) {
		if(groups == null) return;
		if (msToWait < 0) msToWait = 0;
		synchronized (pendingStatus) {
			for(NetworkAddress n : groups) {
				if(!pendingStatus.contains(n))
					pendingStatus.add(n);
			}
		}
		if (iolog.isTraceEnabled())
			iolog.trace("Requesting status for {} address groups " +
				"with buffer size set to {} slots" + (
						msToWait > 0 ? " waiting at leas " 
						+ msToWait + " ms between requests" : "") , 
						groups == null ? 0 : groups.size(), maxbuf);
	}
	
	public EUI64Address getAddress() {
		//if (address == null)
		//	fetchAddress();
		return address;
	}

	/**
	 * Adds the esl message to the outbound queue if more space is available
	 * @param eMsg
	 * @return true on success, false if no more space available in the queue or eMsg or it's network 
	 * @throws IllegalArgumentException if eMsg or its address or pdu is null
	 */
	public boolean transmit(EslMessage eMsg) {
		
		if(eMsg == null)
			throw new IllegalArgumentException("The ESL message must not be null");
		
		if(eMsg.getNetworkAddress() == null) {
			throw new IllegalArgumentException("No network address set in the ESL message");
		}			
		
		if(eMsg.getPdu() == null)
		{
			if (eMsg instanceof EslLeaveMessage) {
				if (((EslLeaveMessage)eMsg).getFrame() == null)
					throw new IllegalArgumentException("No Frame set in the ESL Leave message");
			}
			else 
				throw new IllegalArgumentException("No PDU set in the ESL message");
		}
		
		boolean res;

		if (eMsg.isLowPriority())
		{
			synchronized (outboundLowPriority) {
				LinkedList<EslMessage> list = outboundLowPriority.get(eMsg.getNetworkAddress());
				if(list == null) {
					list = new LinkedList<EslMessage>();
					outboundLowPriority.put(eMsg.getNetworkAddress(), list);
				}
				res = list.add(eMsg);
			}
		}
		else
		{
			synchronized (outbound) {
				LinkedList<EslMessage> list = outbound.get(eMsg.getNetworkAddress());
				if(list == null) {
					list = new LinkedList<EslMessage>();
					outbound.put(eMsg.getNetworkAddress(), list);
				}
				res = list.add(eMsg);
			}	
		}		
		
		if (res) bufferSemaphore.release();			
		
		return res;
	}

	private void transmit(Hub2EslNetCtrlFrame frame) {
		outQueue.add(frame);
	}

	private synchronized boolean transmitDirect(ISerializable data) {
		boolean res = port.transmit(data);
		if (!res) iolog.warn("Unable to transmit frame. Port status: "
					+ (port.isOnline() ? "online" : "offline"));
		return res;
	}
	
	private synchronized boolean isUpgradeSupported() {
		if(minimumForUpgrade == null)
			minimumForUpgrade = new FirmwareVersion(1, 1, 6);
		return firmwareVersion != null && firmwareVersion.compareTo(minimumForUpgrade) >= 0;
	}
	
	/**
	 * Firmware update is only allowed on firmware version 1.1.6 or newer.
	 * @param frames
	 * @return
	 */
	public boolean sendFirmwareUpdate(List<? extends ISerializable> frames) {
		if(!isOnline() || !isUpgradeSupported()) return false;
		for(ISerializable s : frames) {
			transmit(new Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand.FIRMWARE_DATA, s));
		}
		return true;
	}

	/**
	 * Sets the time on the coordinator to now()
	 * Not evaluating the transmission result, assuming that if it fails it 
	 * will be followed by a reconnect of the coordinator (and thus another set time transmission)
	 */
	public void setTime() {
		long ts = (System.currentTimeMillis()/1000L) - 946684800L;
		ByteBuffer time = ByteBufferUtils.allocate(4);
		ByteBufferUtils.writeUInt32(ts, time);
		time.flip();
		Hub2EslNetCtrlFrame frame = new Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand.SET_TIME);
		frame.setPriority(Hub2EslNetCtrlFrame.PRI_HIGHEST);
		frame.setPdu(time);
		transmit(frame);
		if (iolog.isInfoEnabled()) {
			iolog.info("Time sent to coordinator: {} (secs from 01-01-2000: {}) port " + port.getSerialPortName(), 
					new Date((ts + 946684800L) * 1000L), 
					ts);
		}
	}
	
	/**
	 * Only public for tool/test purposes..
	 */
	public void fetchAddress() {
		transmit(new Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand.REQ_IEEE_EUI));
	}

	/**
	 * Fetches buffer info
	 */
	public void fetchBufferInfo() {
		transmit(new Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand.REQ_NUM_FREE_BUFFERS));
	}

	public BufferInformation getBufferInformation() {
		if (bufferInfo == null)
			fetchBufferInfo();
		return bufferInfo;
	}

	public Channel getActiveChannel() {
		if (activeChannel == null)
			fetchChannel();
		return activeChannel;
	}

	public void fetchChannel() {
		transmit(new Hub2EslNetCtrlFrame(
				Hub2EslNetCtrlCommand.REQ_ACTIVE_CHANNEL));
	}

	public void fetchFirmwareVersion() {
		transmit(new Hub2EslNetCtrlFrame(
				Hub2EslNetCtrlCommand.REQ_FIRMWARE_VERSION));
	}

	public void sendAllowedChannels() {

		if (allowedChannels_ == null || !serialPortOnline)
			return;

		long mask = 0;

		for (Channel ch : allowedChannels_) {
			mask |= (1 >> (ch.getValue()));
		}

		if (iolog.isTraceEnabled())
		{
			
			StringBuffer b = new StringBuffer();
			
			if (mask == 0) {
				b.append("NONE");			
			}
			else {
				for (Channel c : allowedChannels_) {
					if (b.length() > 0) b.append(", ");
					b.append(c.toString());
				}			
			}
			
			iolog.trace("Setting allowed channels for the coordinator: "
					+ (mask == 0 ? "none" : b.toString()));

		}
		
		ByteBuffer channelMask = ByteBufferUtils.allocate(4);
		ByteBufferUtils.writeUInt32(mask, channelMask, false);
		channelMask.position(0);

		Hub2EslNetCtrlFrame ctrlFrame = new Hub2EslNetCtrlFrame(
				Hub2EslNetCtrlCommand.REQ_SET_CHANNEL_MASK);
		ctrlFrame.setPdu(channelMask);
		transmit(ctrlFrame);
	}

	public void setDefaultAllowedChannels(Set<Channel> channels) {
		this.allowedChannels_ = channels;
		//	sendAllowedChannels();
		defaultAllowedChannels = channels;
	}

	public void reset() {
		iolog.info("**** SENDING RESET TO COORDINATOR! ****");
		transmit(new Hub2EslNetCtrlFrame(Hub2EslNetCtrlCommand.RESET));
		inQueue.clear();
	}

	public int numberOfPendingStatusRequests() {
		return pendingStatus.size();
	}
	
	public String getPortName() 
	{
		return port.getSerialPortName();		
	}
	
	public long getOfflineCount()
	{
		return port.getOfflineCount();
	}
	
	public long getOnlineCount()
	{
		return port.getOnlineCount();
	}
	
	public long getOfflineTime()
	{
		return port.getOfflineTime();
	}
	
	public long getOnlineTime()
	{
		return port.getOnlineTime();
	}
	
	public int getOutboundSize()
	{
		return outbound.size() - 
				(outbound.get(NetworkAddress.BROADCAST_ADDRESS) != null ? 1 : 0);
	}

	public boolean isOutboundMissing (NetworkAddress nwk) 
	{
		return(outbound.get(nwk) == null);
	}
	
	public int getOutboundLowPrioritySize()
	{
		return outboundLowPriority.size() -
				(outboundLowPriority.get(NetworkAddress.BROADCAST_ADDRESS) != null ? 1 : 0);
	}
	
	public int getOutboundBroadcastSize()
	{
		return outbound.get(NetworkAddress.BROADCAST_ADDRESS) == null ? 
				0 : outbound.get(NetworkAddress.BROADCAST_ADDRESS).size();
	}
	
	public int getOutQueueSize()
	{
		return outQueue.size();
	}
	
	private void emptyBuffer_(NetworkAddress address, boolean highPriority)
	{
		if (address == null) return;
		
		if (highPriority) {
			synchronized (outbound) {
				if (outbound.containsKey(address)) {
					if (iolog.isTraceEnabled()) {
						iolog.trace (
								"flushing price update for esl address 0x{} ...", 
								address.toString());
					}
					outbound.remove(address);
				}
			}
		}
		else {
			synchronized (outboundLowPriority) {
				if (outboundLowPriority.containsKey(address)) {
					if (iolog.isTraceEnabled()) {
						iolog.trace (
								"flushing low priority command for esl address 0x{} ...", 
								address.toString());
					}
					outboundLowPriority.remove(address);
				}
			}
		}
	}
	
	public boolean transmitEslApplicationMessage(
			List<EslTransportFrame> frames, 
			ITransmissionListener<EslMessage> listener, 
			NetworkAddress destination) {
		
		if (frames == null || frames.size() == 0) return false;
		if (destination == null) return false;

		boolean res = true;
		
		try {
			
			synchronized (outbound) {
				
				LinkedList<EslMessage> list = outbound.get (address);
				
				if (list == null) {
					list = new LinkedList<EslMessage>();
					outbound.put(destination, list);
				}				
			
				for (EslTransportFrame tf : frames) {
					EslSecurityFrame sf = new EslSecurityFrame();
					sf.setPdu(tf);
					EslMessage em = (listener != null ? new EslMessage(listener) : new EslMessage());
					em.setNetworkAddress(destination);
					em.setPdu(sf);
					res &= list.add(em);		
				}
				
			}
			
			return res;
		}
		catch (Throwable t) {
			
			iolog.error("submitting high priority transport frames block - {}", t);
			return false;
			
		}
	}	
	
}
