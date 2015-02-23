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
 
package com.s5tech.net.services.logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.entity.EslDataStore;
import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.entity.IEslDataStore;
import com.s5tech.net.entity.IEslEntityManager;
import com.s5tech.net.esl.CoordinatorEventMessageInfo;
import com.s5tech.net.esl.EslCommand;
import com.s5tech.net.esl.EslStatusRequestAckInfo;
import com.s5tech.net.esl.FirmwareUpdateAckInfo;
import com.s5tech.net.esl.HubEventMessageInfo;
import com.s5tech.net.esl.MessageInfo;
import com.s5tech.net.eslnet.EslNetworkController;
import com.s5tech.net.eslnet.IEslNetwork;
import com.s5tech.net.server.IServerProxy;
import com.s5tech.net.server.ServerFacade;
import com.s5tech.net.type.Channel;
import com.s5tech.net.type.EUI48Address;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.HubInfo;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.xml.types.CommandResult;
import com.s5tech.net.xml.types.CoordinatorEventType;
import com.s5tech.net.xml.types.HubEventType;
import com.s5tech.net.xml.types.MessageCommand;

public class ApplicationServicesListener extends Observable implements Observer {

	class TimeoutInfo 
	{	
		protected String key;
		protected long lastKeepAlive;		
	}
	
	class TimeoutHubInfo extends TimeoutInfo
	{		
		String ipAddress;
		String protocol = null;
		String version;
		String ports = null;
		
		public TimeoutHubInfo(String mac, String ip, String proto, String version, String ports, long timestamp)
		{
			key = mac;
			ipAddress = ip;
			protocol = proto;
			this.version = version;
			this.ports = ports;
			lastKeepAlive = timestamp;
		}
	}
	
	class TimeoutCoordinatorInfo extends TimeoutInfo
	{
		int channel = 0;		
		String address = null;
		boolean reloaded = false;
		
		public TimeoutCoordinatorInfo(String port, long timestamp)
		{
			key = port;
			lastKeepAlive = timestamp;
		}
		
		public TimeoutCoordinatorInfo(String port, long timestamp, String address, int channel)
		{
			key = port;
			lastKeepAlive = timestamp;
			this.channel = channel;
			this.address = address;
		}
	}
		
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

	private static ApplicationServicesListener _instance = null;

	private Map<String, IEslNetwork> networks = null;

	private ActiveQueue<EslCommand> downQueue = null;
	private ActiveQueue<EslDeviceInfo> joinQueue = null;

	private IServerProxy server = null;

	private int timeoutCoordinators = -1;
	private int timeoutHubs = -1;
	
	private static Object oSync = new Object();
	
	private HashMap<String, TimeoutHubInfo> timeoutHubsMap = new HashMap<String, TimeoutHubInfo>();
	private HashMap<String, TimeoutCoordinatorInfo> timeoutCoordsMap = new HashMap<String, TimeoutCoordinatorInfo>();
		
	private String mapFile = null;
	
	synchronized public static final ApplicationServicesListener instance() {
		if (_instance == null) {
			_instance = new ApplicationServicesListener();
		}
		return _instance;
	}
	
	private ApplicationServicesListener () {

		server = ServerFacade.instance();
		networks = new HashMap<String, IEslNetwork>();

		downQueue = new ActiveQueue<EslCommand>("EslNetwork DownQueue Dispatcher");
		joinQueue = new ActiveQueue<EslDeviceInfo>("EslNetwork JoinQueue Dispatcher");
		
		if (System.getProperty("timeoutCoordinator") != null && System.getProperty("timeoutCoordinator").length() > 0)
			try { timeoutCoordinators = Integer.parseInt(System.getProperty("timeoutCoordinator")); } catch (Throwable ignored) {}
		
		if (System.getProperty("timeoutHub") != null && System.getProperty("timeoutHub").length() > 0)
			try { timeoutHubs = Integer.parseInt(System.getProperty("timeoutHub")); } catch (Throwable ignored) {}
		
		if (timeoutCoordinators > 0 && timeoutCoordinators < 15) timeoutCoordinators = 15;
		if (timeoutHubs > 0 && timeoutHubs < 30) timeoutHubs = 30;
		
		if (timeoutHubs > 0) timeoutHubs *= 1000;
		if (timeoutCoordinators > 0) timeoutCoordinators *= 1000;
		
		if (isTimeoutEnabled())
		{
			log.info("creating new Esl Network Manager: hub timeout {} ms,  coordinator timeout {} ms", timeoutHubs, timeoutCoordinators);
		}
		else 
		{
			log.warn("creating new Esl Network Manager: WARNING: no timeout managed!", timeoutHubs, timeoutCoordinators);
		}
				
		if (isTimeoutEnabled()) 
		{
			synchronized (oSync) 
			{
				mapFile = System.getProperty ("keepAliveFile", "./run/data/keepalive.txt");
				File f = new File(mapFile);
				
				//if (f.exists() && f.canRead() && f.isFile() && f.length() > 0)
				if (f.exists() && f.isFile() && f.length() > 0)
				{
					
					BufferedReader in = null;
					
					try 
					{
						int linecount = 0;
						
						in = new BufferedReader(new FileReader(f));
						
						for ( ;; )
						{
							String l = in.readLine();
							linecount ++;
							if (l == null) break;
							l = l.trim();									
							if (l.length() == 0) continue;
							if (l.startsWith("#")) continue;
							
							try {
								String [] tk = l.split(";");
								if (tk[0].trim().equals("hub"))
								{
									String key = tk[1].trim();
									timeoutHubsMap.put (key, 
											new TimeoutHubInfo(
													tk[1].trim(), 
													tk[3].trim(),
													(tk.length > 5 ? tk[5] : "n/a"),
													(tk.length > 6 ? tk[6] : "n/a"),
													(tk.length > 4 ? tk[4] : "n/a"),
													System.currentTimeMillis()));
								}
								else if (tk[0].trim().equals("net"))
								{
									String key = tk[1].trim();
									TimeoutCoordinatorInfo info = new TimeoutCoordinatorInfo(
													tk[1].trim(), 
													System.currentTimeMillis(),
													tk[3].trim(),
													Integer.parseInt(tk[4].trim()));
									info.reloaded = true;
									timeoutCoordsMap.put (key, info);									
								}
								else
								{
									throw new Exception("invalid 1st token");
								}
							}
							catch (Throwable t) 
							{
								if (log.isTraceEnabled()) {
									log.trace ("error reading line {} of file" 
											+ mapFile + " - reason: {}", linecount, t.toString());
								}
							}
							
						}
						
					}
					catch (Throwable t) 
					{
						log.error("exception loading maps from path {}: {}", mapFile, t);
					}	
					finally 
					{
						if (in != null) try { in.close(); } catch (Throwable t) {}
					}
						
					if (log.isTraceEnabled() && timeoutCoordsMap.size() == 0 && timeoutHubsMap.size() == 0) 
					{
						log.trace("no hubs and no coordinators loaded from keepalive file");
					}
						
					
					if (log.isInfoEnabled())
					{
						if (timeoutHubsMap.size() > 0) log.info("loaded {} hub/s from {}", timeoutHubsMap.size(), mapFile);
						if (timeoutCoordsMap.size() > 0) log.info("loaded {} coordinator/s from {}", timeoutCoordsMap.size(), mapFile);
					}
					
				}
			
				//else if (f.isFile() && log.isWarnEnabled())
				//{					
				//	log.warn("invalid path specification for maps: " +
				//			 "{} (not a file?, cannot read ? ...)", mapFile);
				//}
			
				addObserver(this);	
			}			
		}
		
		if (log.isTraceEnabled()) log.trace("Esl Network Manager created");
		
	}

	public void checkTimeouts() {
		
		try
		{
			long t = System.currentTimeMillis();
			
			synchronized (oSync) 
			{
				//
				// due to a problem of synchronization with Observer pattern
				//
				
				ArrayList<TimeoutHubInfo> hubs = new ArrayList<ApplicationServicesListener.TimeoutHubInfo>();
				ArrayList<TimeoutCoordinatorInfo> coords = new ArrayList<ApplicationServicesListener.TimeoutCoordinatorInfo>();
				
				for (String key : timeoutHubsMap.keySet())
				{								
					hubs.add(timeoutHubsMap.get(key));
				}
				
				for (String key : timeoutCoordsMap.keySet())
				{								
					coords.add(timeoutCoordsMap.get(key));
				}
				
				//
				// end sync problem
				//
				
				for (ApplicationServicesListener.TimeoutHubInfo o : hubs)
				{								
					if (t - o.lastKeepAlive > timeoutHubs) 
					{
						setChanged();							
						notifyObservers("hub;timeout;" + o.key);			
					}
				}
				
				for (ApplicationServicesListener.TimeoutCoordinatorInfo o : coords)
				{
					if (t - o.lastKeepAlive > timeoutCoordinators) 
					{
						setChanged();						
						notifyObservers("net;timeout;" + o.key);
					}
				}
				
			}
			
		}
		catch (Throwable e) {
			log.error("exception caught in timeout check {}", e);
		}		
	}
	
	public synchronized void shutDown() 
	{		
		try 
		{			
			long start = System.currentTimeMillis();	
			
			if (log.isTraceEnabled() && networks != null && networks.size() > 0)
			{									
				log.trace("shutting down {} network controllers ...", networks.size());
				synchronized (networks) {
					for (IEslNetwork n : networks.values())
					{
						n.shutdown();
					}
				}
			}
							
			if (isTimeoutEnabled())
			{
				saveMaps();
				if (log.isTraceEnabled() && timeoutHubsMap.size() > 0) 
					log.trace("saved {} hub/s in {}", timeoutHubsMap.size(), mapFile);				
				if (log.isTraceEnabled() && timeoutCoordsMap.size() > 0) 
					log.trace("saved {} coordinator/s in {}", timeoutCoordsMap.size(), mapFile);
			}
			
			DecimalFormat df = new DecimalFormat("#.###");
			String s = df.format(((double)((double)(System.currentTimeMillis() - start) / 1000)));
			if (s.equals("0")) s = "less than 1 ms";
			else s = s + " secs.";
			log.info("network shutdown completed in {}.", s);

		} 
		catch (Throwable t) 
		{			
			log.error("shutdown exception - {}", t);
		}
	}

	public Set<HubInfo> getHubsList() {
		HashSet<HubInfo> set = new HashSet<>();
		try 
		{	
			for (String mac : timeoutHubsMap.keySet())
			{				
				TimeoutHubInfo nfo  = timeoutHubsMap.get(mac);
				HubInfo hub = new HubInfo(new EUI48Address(mac));
				hub.setIpAddress ((nfo.ipAddress != null ? nfo.ipAddress : "0.0.0.0"));
				hub.setPorts((nfo.ports != null ? nfo.ports : ""));
				hub.setVersion((nfo.version != null ? nfo.version : "0.00"));
				hub.setProtocol((nfo.protocol != null ? nfo.protocol : ""));				
				set.add(hub);
			}			
		}
		catch (Throwable t)
		{
			log.error("getHubsList() - ", t);
		}
		return set;
	}
	
	public synchronized void sendToAllNetworks(EslCommand cmd) {
		try {
			downQueue.add(cmd);
		} catch (Throwable t) {
			log.error("sendToAllNetworks() - ", t.getMessage());
		}
	}

	public synchronized void notifyJoin(EslDeviceInfo esl) {
		joinQueue.add(esl);
	}

	public synchronized void addJoinSubscriber(
			IActiveQueueSubscriber<EslDeviceInfo> subscriber) {
		joinQueue.addSubscriber(subscriber);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	synchronized public IEslNetwork createNetwork(String portUrl) {
		
		try {
			String clazzname = System.getProperty("controller.impl", EslNetworkController.class.getName());
			Class clazz = Class.forName(clazzname);
			IEslNetwork ctrl = null;
			try {
				Constructor<IEslNetwork> ctor = clazz.getConstructor(
						String.class, 
						IEslEntityManager.class, 
						IEslDataStore.class, 
						IServerProxy.class);
				ctrl = ctor.newInstance(
						portUrl,
						EslEntityManager.instance(), 
						EslDataStore.instance(), 
						ServerFacade.instance());
			}
			catch (Throwable ttt) {				
				ctrl = null;
			}
			if (ctrl == null) ctrl = new EslNetworkController(
					portUrl,
					EslEntityManager.instance(), 
					EslDataStore.instance(), 
					ServerFacade.instance());
			networks.put(portUrl, ctrl);
			downQueue.addSubscriber(ctrl);		
			return ctrl;
		}
		catch (Throwable t) {
			log.error("cannot create network controller - {}", t);
			return null;
		}
	}

	public Map<String, IEslNetwork> getAll() {
		return networks;
	}

	public boolean containsNetwork(String portUrl) {
		return (networks.get(portUrl) != null);
	}
		
	public void sendFirmwareAcknowledge(CommandResult result, String refId, String description) {
		MessageInfo<EUI64Address> msg = new FirmwareUpdateAckInfo(refId);
		msg.setResult(result);
		msg.setDescription(description);
		server.sendMessage(msg);				
	}
	
	public void sendStatusRequestAcknowledge(CommandResult result, String refId, String description) {
		MessageInfo<EUI64Address> msg = new EslStatusRequestAckInfo(refId);
		msg.setResult(result);
		msg.setDescription(description);
		server.sendMessage(msg);
	}
	
	public boolean isTimeoutEnabled() 
	{
		return (timeoutCoordinators > 0 || timeoutHubs > 0);
	}
	
	@Override
	public void update (Observable arg0, Object arg1) {
		
		//log.trace (" ******************* observable {} - object {}", arg0, arg1);
		
		boolean changed = false;
		
		try 
		{
			String recv = (String) arg1;
			String []s = recv.split(";");
			
			synchronized (oSync) 
			{
				if (s[0].equals("hub"))
				{
					String mac = s[2];
					
					if (s[1].equals("alive")) 
					{	
						String ip = s[3];
						String proto = s.length > 4 ? s[4] : "tcp";
						String ports = s.length > 5 ? s[5] : "n/a";
						
						if (!timeoutHubsMap.containsKey(mac))
						{	
							TimeoutHubInfo h = new TimeoutHubInfo (mac, ip, proto, 
									(ip.equalsIgnoreCase(mac) ? "1.2" : "1.3"), 
											ports,
											System.currentTimeMillis());
							
							timeoutHubsMap.put (mac, h);
							changed = true;
							if (mac.equals(ip)) return; //old hub notifications contains ip address in place of mac
							
							HubEventMessageInfo msg = new HubEventMessageInfo (new EUI48Address(mac));
							msg.setCmd(MessageCommand.HUBEVENT);
							msg.setIpAddress(ip);
							msg.setType(HubEventType.ONLINE);
							server.sendMessage(msg);
							
							if (log.isInfoEnabled())
								log.info("hub {} online (1) with ip {}", mac, ip);
						}
						else 
						{
							timeoutHubsMap.get(mac).lastKeepAlive = System.currentTimeMillis();
							timeoutHubsMap.get(mac).ports = ports;							
						}						
					}
					else if (s[1].equals("timeout")) 
					{
						if (timeoutHubsMap.get(mac) == null) 
						{
							if (log.isWarnEnabled())
								log.warn("hub identification mismatch !! received " +
										 "timeout for mac {} not present in table", mac);							
							return;
						}
						
						if (timeoutHubsMap.get(mac).ipAddress == null || 
								timeoutHubsMap.get(mac).ipAddress.equals(mac)) 
						{
							//old hub notifications contains ip address instead of mac
							timeoutHubsMap.remove(mac);
							if (log.isTraceEnabled())
									log.trace (
											"hub offline at address {}, this will be not to " +
											"server notified because was an old version without mac address set", 
											mac);
						}
						
						timeoutHubsMap.remove(mac);
						changed = true;
						HubEventMessageInfo msg = new HubEventMessageInfo (new EUI48Address(s[2]));
						msg.setCmd(MessageCommand.HUBEVENT);						
						msg.setType(HubEventType.TIMEOUT);												
						server.sendMessage(msg);
						
						if (log.isInfoEnabled())
							log.info("hub {} timeout", mac);
					}
					else 
					{
						throw new Exception ("invalid token (nr.2), msg > " + recv);
					}
				}
				else if (s[0].equals("net"))
				{
					String port = s[2];
					
					if (s[1].equals("alive")) 
					{
						TimeoutCoordinatorInfo info = timeoutCoordsMap.get(port);
						
						if (info == null) 
						{
							int channel = 0;							
							
							if (!s[3].equalsIgnoreCase("null") && !s[4].equalsIgnoreCase("null"))
							{
								try { channel = Integer.parseInt(s[3]); } catch (Throwable t) { channel = 0; } 
								info = new TimeoutCoordinatorInfo(port, System.currentTimeMillis(), s[4], channel);
								timeoutCoordsMap.put (port, info);
								changed = true;
								
								CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo (new EUI64Address(s[4]));
								msg.setChannelNo(new Channel(channel));
								msg.setPort(port);
								msg.setCmd(MessageCommand.COORDINATOREVENT);
								msg.setType(CoordinatorEventType.ONLINE);
								
								server.sendMessage(msg);
								
								if (log.isInfoEnabled())
									log.info("coordinator {} online (1), channel {}, port " + port, s[4], channel);
							}
							else 
							{
								if (log.isWarnEnabled()) 
								{
									log.warn ("coordinator keep-alive event mismatch: event format should be " +
											  "'net;alive;port;channel;address', some needed " +
											  "parameters are missing > {}", recv);
								}
							}
						}
						else
						{
							boolean coordinatorChanged = false;
							
							info.lastKeepAlive = System.currentTimeMillis();
							
							if (info.channel == 0 && !s[3].equalsIgnoreCase("null")) 
							{
								try { 
									info.channel = Integer.parseInt(s[3]);
									coordinatorChanged = true;
									if (log.isTraceEnabled())
										log.trace (
												"updated coordinator's channel nr. to {} from keep-alive at port {}", 
												info.channel, port);
								} catch (Throwable t) { info.channel = 0; }
							}
							
							if (info.address == null && !s[4].equalsIgnoreCase("null")) 
							{
								try { 
									info.address = s[4];
									coordinatorChanged = true;
									if (log.isTraceEnabled())
										log.trace (
												"updated coordinator's mac address to {} from keep-alive at port {}", 
												info.address, port);
								} catch (Throwable t) { info.channel = 0; }
							}
							
							changed = coordinatorChanged;
							
							if (changed && info.channel > 0 && info.address != null && !info.reloaded)
							{								
								CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo (new EUI64Address(info.address));
								msg.setChannelNo(new Channel(info.channel));
								msg.setPort(port);
								msg.setCmd(MessageCommand.COORDINATOREVENT);
								msg.setType(CoordinatorEventType.ONLINE);
								
								server.sendMessage(msg);
								
								if (log.isInfoEnabled())
									log.info("coordinator {} online (2), channel {}, port " + port, info.address, info.channel);
								
							}
							
						}
						
					}
					else if (s[1].equals("channel")) 
					{
						
						TimeoutCoordinatorInfo info = timeoutCoordsMap.get(port);
						
						if (!s[3].equalsIgnoreCase("null"))
						{	
							int channel = 0;							
							try { channel = Integer.parseInt(s[3]); } catch (Throwable t) { channel = 0; }
							
							if (channel > 0) 
							{			
								if (info == null) 
								{
									info = new TimeoutCoordinatorInfo(port, System.currentTimeMillis());
									info.channel = channel;
									timeoutCoordsMap.put(port, info);
									changed = true;
								}
								else 
								{	
									if (info.channel == 0)
									{
										info.channel = channel;
										changed = true;
										if (info.address != null && !info.reloaded)
										{
											CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo (new EUI64Address(info.address));
											msg.setChannelNo(new Channel(info.channel));									
											msg.setPort(port);
											msg.setCmd(MessageCommand.COORDINATOREVENT);
											msg.setType(CoordinatorEventType.ONLINE);
											
											server.sendMessage(msg);

											if (log.isInfoEnabled())
												log.info("coordinator {} online (3), channel {}, port " + port, info.address, info.channel);
											
										}
										else 
										{
											if (log.isTraceEnabled())
											{
												log.trace("received channel {} at port {} with address missing ()" +
														 "should be received before channel", info.channel, port);
											}
										}
									}
									else 
									{
										if (info.channel != channel)
										{
											info.channel = channel;
											
											CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo (new EUI64Address(info.address));
											msg.setChannelNo(new Channel(info.channel));								
											msg.setPort(port);
											msg.setCmd(MessageCommand.COORDINATOREVENT);
											msg.setType(CoordinatorEventType.CHANNELCHANGED);
											
											server.sendMessage(msg);
												
											if (log.isInfoEnabled())
												log.info("coordinator {} channel changed, channel {}, port " + port, info.address, info.channel);
											
										}
									}									
								}
							}
							else 
							{
								if (log.isWarnEnabled()) 
								{
									log.warn ("coordinator channel mismatch (1): format is " +
											  "'net;alive;port;channel', wrong channel value > {}", recv);
								}	
							}
						}
						else 
						{
							if (log.isWarnEnabled()) 
							{
								log.warn ("coordinator channel mismatch (2): format is " +
										  "'net;alive;port;channel', wrong channel value > {}", recv);
							}
						}
						
					}
					else if (s[1].equals("address")) 
					{
						TimeoutCoordinatorInfo info = timeoutCoordsMap.get(port);
						
						if (!s[3].equalsIgnoreCase("null"))
						{
							String address = s[3];
							
							if (info == null) 
							{
								info = new TimeoutCoordinatorInfo(port, System.currentTimeMillis());
								info.address = address;
								timeoutCoordsMap.put(port, info);
								changed = true;
							}
							else 
							{								
								if (info.address != null && !info.address.equals(address))
								{							
									if (log.isWarnEnabled())
										log.warn("coordinator mismatch! address change detected at port {} - " +
												 "new coordinator mac address is {} " +
												 "old coordinator addres was " + info.address, address, info.address);
								}																
								
								if (info.channel > 0 && !info.reloaded) 
								{
									CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo (new EUI64Address(info.address));
									msg.setChannelNo(new Channel(info.channel));									
									msg.setPort(port);
									msg.setCmd(MessageCommand.COORDINATOREVENT);
									msg.setType(CoordinatorEventType.ONLINE);
									
									server.sendMessage(msg);

									if (log.isInfoEnabled())
										log.info("coordinator {} online (4), channel {}, port " + port, info.address, info.channel);
									
									
								}
//								else 
//								{
//									if (log.isTraceEnabled())
//										log.trace("notify not managed of coordinator channel " 
//												+ info.channel + " and address " + info.address + " will not delivered to server");
//								}
							}
						}
						else
						{
							if (log.isTraceEnabled()) 
							{
								log.trace ("coordinator address mismatch: format is " +
										  "'net;alive;port;address', wrong address value > {}", recv);
							}
						}
					}
					else if (s[1].equals("timeout")) 
					{
						TimeoutCoordinatorInfo info = timeoutCoordsMap.get(port);
						
						if (info == null) 
						{							
							if (log.isWarnEnabled())
								log.warn("coordinator keepalive mismatch: received timeout for " +
										 "port {} that is not present in table", port);							
						}
						else 
						{
							timeoutCoordsMap.remove(port);							
							changed = true;
							
							//stop network controller
							IEslNetwork net = getAll().get(port);
							
							if (net != null) 
							{
								CoordinatorEventMessageInfo msg = null;
								
								if (net.getCoordinatorMac() == null)
								{
									msg = new CoordinatorEventMessageInfo ();	
									if (log.isWarnEnabled())
										log.warn("warning: coordinator timeout mismatch (2): received timeout for " +
												 "port {} associated to uninitialized network controller " +
												 "(mac address missing)", port);
								}
								else 
								{
									msg = new CoordinatorEventMessageInfo (net.getCoordinatorMac());
								}
								
								msg.setPort(port);
								msg.setCmd(MessageCommand.COORDINATOREVENT);
								msg.setType(CoordinatorEventType.TIMEOUT);	
								
								server.sendMessage(msg);								

								if (log.isInfoEnabled())
									log.info("coordinator timeout (1), port " + port);
								
								//if (net.getReconnectTimeoutMs() <= 0 && (System.getProperty("persistentSerialReader") == null)) 
								//{ 
								//	log.info("coordinator timeout, destroying network controller at " + port + " ...");
								//	destroyNetworkController(port);
								//}
								//else 
								//{
									log.info("coordinator timeout - port " + port + " ...");
									net.stop();		
								//}
								
							}
							else 
							{
								if (info.reloaded)
								{
									//
									// questa condizione corrisponde alla situazione in cui il coordinator 
									// e' stato caricato dalla tabella di keep-alive ma non e' mai stato 
									// inizializzato il controller relativo (virtualmente esistente, fisicamente inattivo)
									//
									CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo (
											new EUI64Address(info.address));

									msg.setPort(port);
									msg.setCmd(MessageCommand.COORDINATOREVENT);
									msg.setType(CoordinatorEventType.TIMEOUT);	
									
									server.sendMessage(msg);

									if (log.isInfoEnabled())
										log.info("coordinator timeout (2), port " + port);
									
								}
								else 
								{
									if (log.isWarnEnabled())
										log.warn("warning: coordinator timeout mismatch (3): received timeout for " +
												 "port {} that is not present in network controllers list", port);
								}
							}
							
						}
					}					
					else if (s[1].equals("off")) 
					{
						
						IEslNetwork net = getAll().get(port);
						
						if (net != null) 
						{
							//if (net.getReconnectTimeoutMs() <= 0 && (System.getProperty("persistentSerialReader") == null)) 
							//{ 
							//	log.info("coordinator offline, destroying network controller at " + port + " ...");
							//	destroyNetworkController(port);
							//}
							//else 
							//{
								log.info("coordinator offline - port " + port + " ...");
								net.stop();		
							//}
						}
						
						TimeoutCoordinatorInfo info = timeoutCoordsMap.get(port);
						
						if (info == null) 
						{							
							if (log.isWarnEnabled())
								log.warn("coordinator keepalive mismatch: received offline for " +
										 "port {} that is not present in table", port);							
						}
						else 
						{
							timeoutCoordsMap.remove(port);							
							changed = true;
							
							CoordinatorEventMessageInfo msg = new CoordinatorEventMessageInfo ();
							msg.setPort(port);
							msg.setCmd(MessageCommand.COORDINATOREVENT);
							msg.setType(CoordinatorEventType.OFFLINE);	
							
							server.sendMessage(msg);

							if (log.isInfoEnabled())
								log.info("coordinator offline, port " + port);
															
						}
					}
					else 
					{
						throw new Exception ("invalid token in keepalive event (nr.2), msg > " + recv);
					}
				}
				else 
				{
					throw new Exception ("invalid token in keepalive event (nr.1), msg > " + recv);
				}		
			}
		}
		catch(Throwable t) 
		{
			log.error("observer error, keepalive event elaboration failure: {}", t);
		}
		finally 
		{
			if (changed) 
			{
				saveMaps(); 
			}
 		}
		
	}
	
	private void saveMaps() 
	{	
		PrintStream o = null;
		if (!isTimeoutEnabled()) return;
		try 
		{
			synchronized (oSync) 
			{
				
				mapFile = System.getProperty ("keepAliveFile", "./run/data/keepalive.txt");
				
				try
				{
					o = new PrintStream(mapFile);
					
					for (String mac : timeoutHubsMap.keySet())
					{
						o.print("hub;");
						o.print(mac);
						o.print(';');
						o.print(timeoutHubsMap.get(mac).lastKeepAlive);
						o.print(';');
						o.print(timeoutHubsMap.get(mac).ipAddress);
						o.print(';');
						o.print(timeoutHubsMap.get(mac).ports);
						o.print(';');
						o.print(timeoutHubsMap.get(mac).protocol);
						o.print(';');
						o.print(timeoutHubsMap.get(mac).version);
						o.print('\n');
					}
					
					for (String port : timeoutCoordsMap.keySet())
					{
						o.print("net;");
						o.print(port);
						o.print(';');
						o.print(timeoutCoordsMap.get(port).lastKeepAlive);
						o.print(';');
						o.print(timeoutCoordsMap.get(port).address);
						o.print(';');
						o.print(timeoutCoordsMap.get(port).channel);
						o.print('\n');
					}
					
				}
				catch (Throwable t) 
				{
					log.error("exception saving maps to path {}: {}", mapFile, t);
				}
			}
		}
		catch (Throwable t)
		{
			log.error("error saving hub & coordinators maps - {}", t);
		}
		finally 
		{
			if (o != null) try { o.close(); } catch (Throwable ignored) {}
		}	
	}
	
	
}

