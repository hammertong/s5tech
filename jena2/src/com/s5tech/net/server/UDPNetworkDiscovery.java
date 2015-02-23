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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.eslnet.IEslNetwork;
import com.s5tech.net.services.logging.ApplicationServicesListener;
import com.s5tech.net.util.ISystemKeys;

public class UDPNetworkDiscovery 
		extends Observable 
		implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	public static final int UDP_PACKET_SIZE = 1024;
	
	public static final int HUB_BROADCAST_DEFAULT_PORT = 55559;
	
	public static int hubBroadcastPort = HUB_BROADCAST_DEFAULT_PORT;
	
	private boolean timeoutEnabled = false;
	
	public UDPNetworkDiscovery(Observer observer)
	{
		timeoutEnabled = observer != null;
		if (timeoutEnabled) addObserver (observer);		
	}
	
	@Override
	public void run() {
		
		Object dsc = new Object();
		byte [] buffer = new byte [UDP_PACKET_SIZE];
		
		DatagramSocket server = null;								
		
		String allowedchannels = System.getProperty("discovery.allowedchannels", "").trim();
		try { hubBroadcastPort = Integer.parseInt(System.getProperty("hubBroadcastPort", "55559"));} catch (Throwable t) { log.error("wrong hubBroadcastPort speification - {}", t); }
		boolean policy_deny = System.getProperty("discovery.policy", "allow").equalsIgnoreCase("deny"); 
		String ip_deny_list = System.getProperty("discovery.ipdeny", "");
		String ip_allow_list = System.getProperty("discovery.ipallow", "");
		
		try {
			
			boolean hasRestrictions = (policy_deny || (!policy_deny && ip_deny_list.length() > 0));
			
			log.info("UDP broadcast listener active on port {}, ip policy {} - " 
					+ (hasRestrictions ? "Restrictions to IP address will be applied!" : "No restrictions to IP address"), hubBroadcastPort, (policy_deny ? "DENY" : "ALLOW"));
			if (policy_deny && ip_allow_list.length() == 0) log.warn("policy deny, but not allowed IPs list given !!! listener will not connect to any coordinator!!!");
				
			server = new DatagramSocket(hubBroadcastPort); 
			server.setBroadcast(true);
			
			String data = null;
			String address = null;
			
			for ( ;; ) {
				
				data = null;
				address = null;
				
				try {
				
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					server.receive(packet);		
					
					if (packet.getAddress() == null || packet.getAddress().getHostAddress() == null) 
						throw new Exception("received packet without address set");
					
					address = packet.getAddress().getHostAddress();
					
					//
					// IP policy here !!!
					//						
					if (hasRestrictions) 
					{ 													
						String ip_list = (policy_deny ? ip_allow_list : ip_deny_list);
						ip_list = ip_list.replace(',', ' ').replace('\t', ' ').trim();
						boolean found = false;
						for (String x : ip_list.split(" ")) 
						{
							x = x.trim();
							if (x.length() == 0) continue;
							if (x.endsWith(".*")) x = x.replaceAll("\\.\\*", "");
							found = (address.indexOf(x) >= 0);	
							if (found) break;
						}
						
						if ((policy_deny && !found) 			/* policy deny, found ip allowed */ 
								|| (!policy_deny && found)) {	/* policy allow, found ip denied */
							if (log.isTraceEnabled())
								log.trace("ignored TCP virtual coordinator identification from ip address {}", address);
							continue;
						}
					}																						
					//
					// end ip policy
					//
																
					address = packet.getAddress().getHostAddress();
					
					if (packet.getLength() <= 0) 
						throw new Exception("received zero length packet from " + address);
					
					data = new String(packet.getData(), 0, packet.getLength());			
					
					//patch for packets incoming from windows
					if (data.indexOf('\r') >= 0) {
						StringBuffer bbb = new StringBuffer();
						for (char ccc : data.toCharArray()) {
							if (ccc == '\r') continue;
							bbb.append(ccc);
						}
						data = bbb.toString().trim();
					}
					
					String hubmac = null;
					String [] ports = null;
					String proto = "tcp";
					
					if (data.indexOf(";") > 0)
					{
						for (String p : data.split(";"))
						{
							String [] kv = p.split("=");
							
							if (kv[0].equals("ID"))
							{
								hubmac = kv[1].replaceAll("\\:", "");
							}
							else if (kv[0].equals("COORDS") && kv.length > 1)
							{
								ports = kv[1].split("\\n");
							}
							else if (kv[0].equals("SOCKET"))
							{
								if (kv[1].trim().equals("SSL"))
									proto = "ssl";
							}													
						}
					}
					else {			
						if (log.isTraceEnabled())
							log.trace("hub brodcast id from {} without mac address (old data version?), " +
									"hub online/timeout events will be not notified",
									address);
						ports = data.split("\\n");											
					}
					
					// keep alive only for hub ver. 1.3
					//if (hubmac != null && EslNetworksManager.instance().isTimeoutHubEnabled()) 
					//	EslNetworksManager.instance().keepAliveHub(hubmac, address);
					
					//TODO: check timeout enabled?
					if (timeoutEnabled) setChanged();
					
					StringBuffer pb = new StringBuffer();
					if (ports == null || ports.length == 0)
					{
						pb.append("n/a");
					}
					else 
					{
						for (String p : ports) {
							pb.append("5555").append(p).append(' ');													
						}
					}
					
					if (timeoutEnabled) {
						notifyObservers (
								new StringBuffer("hub;alive;")
									.append((hubmac == null ? "?" : hubmac)).append(";")
									.append(address ).append(";")
									.append(proto).append(";")
									.append(pb.toString())
									.toString());
					}
					
					if (ports == null) {
						if (log.isInfoEnabled()) 
							log.info("WARNING! received udp datagram with no coordinators set from {} > {}",
								address, data.replace('\n', ' ').replace('\r', ' '));
						continue;
					}
					
					for (String token : ports) {
						
						String porturl = proto + "://" + address + ":5555" + token;											
						
						synchronized (dsc) 
						{
							if (!ApplicationServicesListener.instance().containsNetwork(porturl))
							{
								try 
								{
									IEslNetwork eslnet =  ApplicationServicesListener.instance().createNetwork(porturl);
									//if (!eslnet.isOnline()) {  }
									
									String ac = (hubmac == null ? "" : System.getProperty("reservation.hub." + hubmac, ""));
									
									if (ac.length() == 0) ac = System.getProperty("reservation.ip." + address, "");
									else if (log.isTraceEnabled()) 
										log.trace("setting reserved channels {} for hub {} on port " + porturl, ac, hubmac);
									
									if (ac.length() == 0) ac = allowedchannels;
									else if (log.isTraceEnabled()) 
										log.trace("setting reserved channels {} for ip {} on port " + porturl, ac, address);
									
									if (ac.length() > 0) eslnet.setChannelList(ac, false);
									
									if (!eslnet.connect()) throw new Exception("ESL NEtwork connection failure at " + porturl);						
									log.info("dynamic esl network started: url = " + porturl + ", using current active channel");													
									
								}
								catch (Throwable t) 
								{
									log.info("error: cannot connect to dynamic esl network " 
											+ porturl + "  - reason: " + t.getMessage()); 													
								}
							}
							else
							{
								IEslNetwork net = ApplicationServicesListener.instance().getAll().get(porturl);
								if (!net.isOnline())
								{
									if (log.isTraceEnabled()) 
										log.trace("trying to restore network controller connection on {}", porturl);
									//net.stop(); 
									//Thread.sleep(500);
									net.connect();
								}
							}
							
						}
						
					}
				}
				catch (Throwable t)
				{										
					log.error ("cannot process udp datagram, source address {}, data > {}",
							address == null ? "n/a" : address, 
									data == null ? "n/a" : data);
					log.error ("error details: {}", t);
				}
				
			}
		
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			
		}
		finally {
			
			if (server != null) try { server.close(); } catch (Throwable t) {}
			
		}
	}
}
	


