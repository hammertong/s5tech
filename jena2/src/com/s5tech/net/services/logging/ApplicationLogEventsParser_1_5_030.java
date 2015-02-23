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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;

public class ApplicationLogEventsParser_1_5_030 extends ALogFileFinder {
	
	private IApplicationLogEventsHandler ehandler = null;
	
	private String logDir;
	
	private Date from;
	
	private boolean all;
	
	private Properties filter = null;
	
	public static int bufferInfoTimeout;
	
	protected HashMap<String, HashMap<String, String>> sentMap = new HashMap<String, HashMap<String,String>>();
	
	public ApplicationLogEventsParser_1_5_030()
	{
		filter = null;
		from = null;
		all = true;
		logDir = "./logs";
		bufferInfoTimeout = 15760 + 1000;
	}
	
	public Properties getFilter() {
		return filter;
	}

	public void setFilter(Properties filter) {
		this.filter = filter;
	}

	public String getLogDir() {
		return logDir;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}
	
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public boolean isAll() {
		return all;
	}
	
	public static int getBufferInfoTimeout() {
		return bufferInfoTimeout;
	}

	public static void setBufferInfoTimeout(int bufferInfoTimeout) {
		ApplicationLogEventsParser_1_5_030.bufferInfoTimeout = bufferInfoTimeout;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public void SetEventsHandler(IApplicationLogEventsHandler ehandler)
	{
		this.ehandler = ehandler;
	}
	
	protected boolean filterValidate(String key, String value)
	{
		boolean hasFilter = (filter != null && filter.containsKey(key));
		if (!hasFilter) {
			return true;
		}
		else {
			return (value != null 
					&& value.equalsIgnoreCase(filter.getProperty(key)));
		}		
	}
	
	public void parseDir()
	{
		BufferedReader in = null;
		
		if (ehandler == null) return;
	
		HashMap<String, String> portmap = new HashMap<String, String>();
		HashMap<String, Date> aliveBuffers = new HashMap<String, Date>();

		HashMap<String, HashMap<String, Integer>> filledVirtualSlotsMap = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, HashMap<String, Date>> durationTimeslotsMap = new HashMap<String, HashMap<String, Date>>();
		
		ArrayList<String> rcvmap = new ArrayList<>();
				
		try
		{		
			Date lastTimestamp = null;
			String [] files = null;
			
			if (all) {
				files = getOrderedLogFilesList (
						logDir, 
						System.getProperty("applog_prefix", "application"), 
						System.getProperty("applog_ext", ".log"));
			}
			else {
				files = new String[] {
					logDir 
						+ System.getProperty("applog_prefix", "application") 
						+ System.getProperty("applog_ext", ".log")
				};
			}
			
			for (String filename : files) {
				
				in = new BufferedReader (
							new InputStreamReader(
									new FileInputStream(filename)));
				int linecount = 0;
		
				for ( ; ; ) 
				{
					String  l = in.readLine();
					if (l == null) break;
					linecount ++;
					if (!l.startsWith("2")) continue;
					//if (l.startsWith(" ") || l.startsWith("\t") || l.startsWith("\r") || l.startsWith("\n")) continue;
					
					try {
						
						String timestamp = l.substring(0, 23);
						
						Date d = null;
						
						try {
							d = timef.parse(timestamp);
						}
						catch( Throwable t) {
							System.err.println("not parsable date at line " + linecount + " of file " + filename);
							d = null;
						}
						
						if (d == null) continue; 
						if (from != null && d.before(from)) continue; 
						
						//
						// update map coordinator <-> port
						//
						
						String coordinator = null;
						
						if (l.indexOf("APPLICATION.COORD(") > 0) 
						{
							if (l.indexOf("[Coord inQueue@") > 0) {
								int j1 = l.indexOf("[Coord inQueue@") + "[Coord inQueue@".length();
								int j2 = l.indexOf(']', j1);
								String port = l.substring(j1, j2);
								int k1 = l.indexOf("APPLICATION.COORD(") + "APPLICATION.COORD(".length();
								int k2 = l.indexOf(')', k1);
								String address = l.substring(k1, k2);
								try {
									long a = Long.parseLong(address.toLowerCase(), 16);
									if (a == 0) throw new Exception ("invalid address (0x0000000000000000)");
								}
								catch (Throwable t) {
									address = null;
								}
								if (address != null) {
									coordinator = address;
									portmap.put(port, coordinator);
								}
							}
						}
						
						//
						// lookup current coordinator
						//
						
						if (coordinator == null) {
							for (String k : portmap.keySet()) {
								if (l.indexOf(k) > 0) {
									coordinator = portmap.get(k);
									break;
								}
							}
						}
						
						if (!filterValidate("coordinator", coordinator)) continue;
						
						//
						// begin firing events from parser ... 
						//

						lastTimestamp = d;
						
						if (l.indexOf("tarting S5TNET-core ") > 0 && l.indexOf("ersion ") > 0) {
							int j1 = l.indexOf("ersion ");
							String version = l.substring(j1);
							ehandler.onApplicationStart(d, version);
							aliveBuffers.clear();
							continue;
						}
						else if (l.indexOf("hutting down application") > 0) {
							ehandler.onApplicationShutdown(d);
							aliveBuffers.clear();
							continue;
						}		
						
						if (lastTimestamp != null && (d.getTime() - lastTimestamp.getTime()) > bufferInfoTimeout) {
							ehandler.onApplicationLoggerInactivity(lastTimestamp);						
						}
						
						if (l.indexOf("creating ") < 0 && l.indexOf("coordinator") > 0 && 
							(l.indexOf("timeout") > 0 || l.indexOf("offline") > 0)) {
						
							// ... coordinator offline (timeout) at port 00124B000106AEEE
							// ... coordinator online: port COM11, mac address 00124B000106AEEE, channel 17
							// ... coordinator timeout (*), port 
								
							if (coordinator == null) {
								int n = l.indexOf(" port ");
								n += 6;
								String port = l.substring(n).trim();
								n = port.indexOf(" ");
								if (n >= 0) {
									port.substring(0, n);
								}														
								coordinator = (portmap.containsKey(port) ? portmap.get(port) : port);								
							}	
								
							String fport = null;
							for (String p : portmap.keySet()) {
								if (portmap.get(p).equals(coordinator)) {
									fport = p;
									break;
								}
							}							
							
							ehandler.onCoordinatorOffline(d, coordinator, fport);							
							continue;
							
						}						
						
						if (l.indexOf("received from server EslPriceUpdate") > 0) {
							//2013-09-12T23:46:33.966 [Server Down Queue] TRACE APPLICATION - received from server EslPriceUpdate - ESLs: 00124B0001C65539 , payload activation date: Thu Sep 12 23:46:30 CEST 2013 (secs from 2000 = 432337590) Xml Activation Date: Thu Sep 12 21:46:30 CEST 2013, price is ACTIVE: current system time => Thu Sep 12 23:46:33 CEST 2013 [1379022393934 ms] received activation time => Thu Sep 12 23:46:30 CEST 2013 [1379022390000 ms]
							
							int n = l.indexOf(" - ESLs: ");
							n += 9;
							
							int m = l.indexOf(",", n);
							String [] macs = l.substring(n, m).trim().split(" ");
							for (String mac : macs) {
								if (mac.trim().length() == 0) continue;
								if (filterValidate("esl", mac)) {
									ehandler.onPriceUpdateReceived(d, mac.trim());
								}
							}
							continue;
						}
						if (l.indexOf("Received message with command: EslStatusRequest") > 0) {
							
							ehandler.onStatusRequestReceived(d);
							
						}
						
						//
						// if cannot lookup coordinator, the following events are meaningless
						//
						
						if (coordinator == null) continue;
						
						if (l.indexOf("Send attempt #") > 0 && (l.indexOf("for active price") > 0 || l.indexOf("for pending price") > 0)) {
							//2013-09-12T23:46:33.981 [EslNetwork DownQueue Dispatcher] TRACE APPLICATION.ESL(00124B0001C65539) - Send attempt #1 for active price. (hash: 1831189247)
							int n = l.indexOf("APPLICATION.ESL(");
							n += 16;
							String esl = l.substring(n, n + 16);
							
							if (!filterValidate("esl", esl)) continue;
							
							n = l.indexOf("Send attempt #");
							n += 14;
							n = Integer.parseInt(l.substring(n, n + 1));
							ehandler.onSubmitPriceUpdate(d, coordinator, esl, n);
						}
											
						//
						// timed out buffer info notifications
						//
						
						if (aliveBuffers.size() > 0)
						{
							ArrayList<String> toBeRemoved = new ArrayList<String>();
							
							for (String koord : aliveBuffers.keySet())  
							{
								long t = aliveBuffers.get(koord).getTime();
								t += 16000;
								
								if (t < d.getTime()) {
								
									//System.out.println("TOUT removing " + koord 
									//	+ " : from " + aliveBuffers.get(koord).toString() + " to now : " + d.toString() + " => " + ((int)(d.getTime() - t)));
								
									String fport = null;
									for (String p : portmap.keySet()) {
										if (portmap.get(p).equals(coordinator)) {
											fport = p;
											break;
										}
									}
									
									ehandler.onCoordinatorBufferTimeout(d, coordinator, fport);
									toBeRemoved.add(coordinator);
								}
							}
							
							for (String krem : toBeRemoved) 
							{
								aliveBuffers.remove(krem);
							}
						}
																								
						if (l.indexOf("REQUEST VALIDATE FOR ") > 0) {
							int j1 = l.indexOf("REQUEST VALIDATE FOR ") + "REQUEST VALIDATE FOR ".length();
							int j2 = j1 + 16;
							String esl = l.substring(j1, j2);
							if (esl.equals("0000000000000000")) {
								ehandler.onCoordinatorSpam(d, coordinator, "VALIDATE-REQUEST:0x00");
							}
							else {
								if (filterValidate("esl", esl)) {
									ehandler.onEslValidateRequest (d, esl, coordinator);
								}
							}
						}
						else if (l.indexOf("REJOIN ESL ") > 0 || l.indexOf("JOIN ESL ") > 0){
							int j1 = l.indexOf("JOIN ESL ") + "JOIN ESL ".length();
							int j2 = j1 + 16;
							String esl = l.substring(j1, j2);
							j1 = l.indexOf("short address ");
							j1 += 14;
							String nwk = "0x" + l.substring(j1);
							if (filterValidate("esl", esl)) {
								ehandler.onEslJoin (d, esl, nwk, coordinator);
							}
						}
						else if (l.indexOf("ignored device authorization request from ") > 0) {
							int j1 = l.indexOf("ignored device authorization request from ") + "ignored device authorization request from ".length();
							int j2 = j1 + 16;
							String esl = l.substring(j1, j2);
							if (filterValidate("esl", esl)) {
								ehandler.onEslJoinIgnored (d, esl, coordinator);
							}
						}
						else if (l.indexOf("ESL not authorized! (MAC: ") > 0) {
							int j1 = l.indexOf("ESL not authorized! (MAC: ") + "ESL not authorized! (MAC: ".length();
							int j2 = j1 + 16;
							String esl = l.substring(j1, j2);
							if (filterValidate("esl", esl)) {
								ehandler.onEslUnauthorizedAttempt (d, esl, coordinator);
							}
						}
						else if (l.indexOf("Got EslMessage from NetworkAddress ") > 0) {
							int j1 = l.indexOf("APPLICATION.ESL(") + "APPLICATION.ESL(".length();
							//int j2 = l.indexOf(')', j1);
							//String esl = l.substring(j1, j2);	
							j1 = l.indexOf("dress 0x");
							j1 += 8;
							String networkAddress = "0x" + l.substring(j1, j1 + 4);
							rcvmap.add(coordinator + networkAddress);							
							//ehandler.updateAssociation (esl, networkAddress, coordinator);
						}
						else if (l.indexOf("Got Esl ACKNOWLEDGE") > 0) {							
							int j1 = l.indexOf("APPLICATION.ESL(") + "APPLICATION.ESL(".length();
							int j2 = l.indexOf(')', j1);
							String esl = l.substring(j1, j2);
							if (filterValidate("esl", esl)) {							
								ehandler.onEslAcknowledge (d, esl, coordinator, (l.indexOf("join") > 0));
							}
						}	
						else if (l.indexOf("relaying 'priceunknown'") > 0 ) {
							int j1 = l.indexOf("APPLICATION.ESL(") + "APPLICATION.ESL(".length();
							int j2 = l.indexOf(')', j1);
							String esl = l.substring(j1, j2);		
							if (filterValidate("esl", esl)) {
								ehandler.onEslEvaluatePrice(d, esl, coordinator, "PUNK");
							}
						}
						else if (l.indexOf("Evaluating hash codes:") > 0 && l.indexOf("EslNetwork DownQueue Dispatcher") < 0) {
							int j1 = l.indexOf("APPLICATION.ESL(") + "APPLICATION.ESL(".length();
							int j2 = l.indexOf(')', j1);
							String esl = l.substring(j1, j2);
							
							if (!filterValidate("esl", esl)) continue;
							
							String localActiveHash = null;
							String localPendingHash = null;
							String eslActiveHash = null;
							String eslPendingHash = null;
							
							for (String pz : l.substring(l.indexOf("Evaluating hash codes: ") + 23).split(",")) {
								pz = pz.trim();
								if (pz.startsWith("local active: ")) {
									localActiveHash = pz.substring("local active: ".length()).trim();
								}
								else if (pz.startsWith("esl active: ")) {
									eslActiveHash = pz.substring("esl active: ".length()).trim();
								}
								else if (pz.startsWith("local pending: ")) {
									localPendingHash = pz.substring("local pending: ".length()).trim();
								}
								else if (pz.startsWith("esl pending: ")) {
									eslPendingHash = pz.substring("esl pending: ".length()).trim();
								}								
							}
							
							boolean no_pending = localPendingHash.equals("-1") || localPendingHash.equals("0");
							
							if (localActiveHash.equals(eslActiveHash) 
									&& (no_pending || localPendingHash.equals(eslPendingHash))) {
								ehandler.onEslEvaluatePrice(d, esl, coordinator, "P-OK");	
							}
							else if (!localActiveHash.equals(eslActiveHash)) {
								ehandler.onEslEvaluatePrice(d, esl, coordinator, "P-WA");
							}
							else if (!no_pending){
								ehandler.onEslEvaluatePrice(d, esl, coordinator, "P-WP");
							}
							
						}	
						else if (l.indexOf("Got Esl EXTENDED_STATUS") > 0) {
							
							int j1 = l.indexOf("APPLICATION.ESL(") + "APPLICATION.ESL(".length();
							int j2 = l.indexOf(')', j1);
							String esl = l.substring(j1, j2);
								
							if (filterValidate("esl", esl)) {
								ehandler.onEslStatus (d, esl, coordinator);
							}
							
						}
						else if (l.indexOf("Got Esl STATISTICS") > 0) {
							
							int j1 = l.indexOf("APPLICATION.ESL(") + "APPLICATION.ESL(".length();
							int j2 = l.indexOf(')', j1);
							String esl = l.substring(j1, j2);
							
							if (filterValidate("esl", esl)) {
								ehandler.onEslStatistics (d, esl, coordinator);							
							}
							
						}
						else if (l.indexOf("Free buffers: ") > 0) {
							
							String s = l.substring(l.indexOf("Free buffers: ") + "Free buffers: ".length(), l.indexOf("Timeslots: "));
							s = s.replace('.',  ',').replace(':', ',');
							//int g;
							
							//String [] tk = s.split(",");							
//							int free_bcast = Integer.parseInt(tk[1].trim());
//							int free_cmd = Integer.parseInt(tk[3].trim());
//							int free_status = Integer.parseInt(tk[5].trim());
							
							HashMap<String, Integer> timeslots = null;
							// timeslots format:
							// 0D0C:1|08F7:1|093C:1|0C84:1|01DA:1|01DB:1|0A68:1|022E:1|0398:1|088E:1|0CC4:1|0000:2|0000:2|0A4E:1
							String [] arr = l.substring(l.indexOf("Timeslots: ") + 11).split("\\|");
							//System.err.println("--> " + l.substring(l.indexOf("Timeslots: ") + 11) + ", " + arr.length + " ... " + arr[0]);
							
							for (String a : arr) {
								String [] kv = a.split(":");
								if (kv[0].equals("0000")) continue;
								if (timeslots == null) timeslots = new HashMap<String, Integer>();
								timeslots.put("0x" + kv[0], Integer.parseInt(kv[1]));
							}
							
							ArrayList<String> tbRemove = new ArrayList<String>();							
							HashMap<String, Date> durationTimeslots = durationTimeslotsMap.get(coordinator);
							HashMap<String, Integer> filledVirtualSlots = filledVirtualSlotsMap.get(coordinator);
							
							if (durationTimeslots == null)
							{
								durationTimeslots = new HashMap<String, Date>();
								durationTimeslotsMap.put(coordinator, durationTimeslots);
							}
							
							if (filledVirtualSlots == null)
							{
								filledVirtualSlots = new HashMap<String, Integer>();
								filledVirtualSlotsMap.put(coordinator, filledVirtualSlots);
							}
							
							if (durationTimeslots != null && durationTimeslots.size() > 0)
							{									
								for (String nwk : durationTimeslots.keySet()) {
									
									if (timeslots == null || !timeslots.containsKey(nwk)) {
										
										if (!rcvmap.remove(coordinator + nwk)) {
											
											String _esl = sentMap.get(coordinator) != null ? sentMap.get(coordinator).get(nwk) : null;
											
											if (filterValidate("esl", _esl)) 
											{
												ehandler.onBufferFree (
														d, 
														coordinator, 
														nwk, 
														_esl,
														d.getTime() - durationTimeslots.get(nwk).getTime());												
											}
										}
										
										tbRemove.add(nwk);
									}
								}
								
								for (String nwk : tbRemove) {
									durationTimeslots.remove(nwk);
									filledVirtualSlots.remove(nwk);
								}
								
								tbRemove.clear();
							}							
							
							if (filledVirtualSlots != null && filledVirtualSlots.size() > 0)
							{
								for (String nwk : filledVirtualSlots.keySet()) {
									
									if (timeslots == null || !timeslots.containsKey(nwk)) {
										
										String _esl = sentMap.get(coordinator) != null ? sentMap.get(coordinator).get(nwk) : null;
										
										if (filterValidate("esl", _esl)) 
										{
											ehandler.onBufferFlooding (
													d, 
													coordinator, 
													nwk,
													_esl);
										}
										
										tbRemove.add(nwk);
									}
									
									if (!durationTimeslots.containsKey(nwk)) {
										durationTimeslots.put(nwk, d);
									}
								}
															
								for (String nwk : tbRemove) {
									filledVirtualSlots.remove(nwk);
								}
							}
							
							aliveBuffers.put(coordinator, d);
							
						}							
						else if (l.indexOf("ent statstic requests ") > 0) {
							
							int n = l.indexOf("ent statstic requests ");
							n += 22;							
							String esl = l.substring(n, n + 16);

							if (filterValidate("esl", esl)) {
								ehandler.onSubmitStatistics(d, coordinator, esl);
							}
							
						}	
						else if (l.indexOf("ent channel ") > 0 && l.indexOf("to join to ") > 0) {
							
							//ent channel {} to join to {}							
							int n = l.indexOf("ent channel ");
							n += 12;
							int channel = Integer.parseInt(l.substring(n, n + 2));
							n = l.indexOf("to join to ");
							n += 11;
							
							String esl = l.substring(n, n + 16);				

							if (filterValidate("esl", esl)) {
								ehandler.onSubmitChannelToJoin(d, coordinator, esl, channel);
							}
							
						}	
						else if (l.indexOf("nqueued Kill message for network address ") > 0) {
							int n = l.indexOf("address ");
							n += 8;
							String networkAddress = "0x" + l.substring(n, n + 4);
							
							String esl = (l.length() > n + 10 && l.charAt(n + 4) == '=' ? l.substring(n + 5, n + 21) : "NETADDR:::" + networkAddress);
							
							if (filterValidate("esl", esl)) {
								ehandler.onSubmitKill(d, coordinator, networkAddress, esl);							
							}
						}
						else if (l.indexOf("nqueued Leave message for network address ") > 0) {
							
							int n = l.indexOf("address ");
							n += 8;
							String networkAddress = "0x" + l.substring(n, n + 4);
							String esl = (l.length() > n + 10 && l.charAt(n + 4) == '=' ? l.substring(n + 5, n + 21) : "NETADDR:::" + networkAddress);
							
							if (filterValidate("esl", esl)) {
								ehandler.onSubmitLeave(d, coordinator, networkAddress, esl);
							}
						}							
						else if (l.indexOf(" - To ") > 0 && l.indexOf("sending ") > 0) {
							
							int n = l.indexOf(" - To ");
							n += 6;							
							String nwk = "0x" + l.substring(n, n + 4);
							
							if (nwk.equals("0xFFFF")) continue;
							
							//
							// lookup ESL only available in rel. >= 1.6.07
							//
							String esl = (l.charAt(n + 4) == '=' ? l.substring(n + 5, n + 21) : "NETADDR:::" + nwk);
							
							HashMap<String, String> map = sentMap.get(coordinator);
							if (map == null) map = new HashMap<>(); 
							map.put(nwk, esl);
							
							//now updated association ....
							
							n = l.indexOf(" sending ");
							n += 9;
							int remaining = Integer.parseInt(l.substring(n, l.indexOf(" ", n + 1)));
							
							n = l.indexOf("(max ");
							n += 5;
							int maxcount = Integer.parseInt(l.substring(n, n + 1));
							
							int sent = (remaining > 1 && maxcount > 1 ? 2 : 1); 
							remaining -= sent;
							
							HashMap<String, Integer> filledVirtualSlots = filledVirtualSlotsMap.get(coordinator);
							
							if (filledVirtualSlots == null) {
								filledVirtualSlots = new HashMap<String, Integer>();
								filledVirtualSlotsMap.put(coordinator, filledVirtualSlots);
							}
							
							n = (filledVirtualSlots.containsKey(nwk) ? filledVirtualSlots.get(nwk) : 0);
							n += sent;
							
							filledVirtualSlots.put(nwk, n);
							
							if (filterValidate("esl", esl)) {
								ehandler.onBufferFilled (d, coordinator, nwk, esl, sent, remaining);
							}
							
						}
						else if (l.indexOf("gnored device authorization from zero address") > 0) {							
							if (filter != null 
									&& filter.containsKey("esl")) { 
								continue;
							}							
							ehandler.onCoordinatorSpam(d, coordinator, "AUTHORIZE-FAIL:0x00");
						}
						else if (l.indexOf("eceived EslMessage not associated to any known Esl") > 0) {
							if (filter != null 
									&& filter.containsKey("esl")) { 
								continue;
							}
							ehandler.onCoordinatorSpam(d, coordinator, "WRONG-ESLMESSAGE:" + l.substring(l.indexOf("PDU > ") + 6));
						}
						else if (l.indexOf("ctive ESL Network channel ") > 0) {
							if (filter != null 
									&& filter.containsKey("esl")) { 
								continue;
							}
							String fport = null;
							for (String p : portmap.keySet()) {
								if (portmap.get(p).equals(coordinator)) {
									fport = p;
									break;
								}
							}					
							ehandler.onCoordinatorOnline(d, coordinator, fport, 
									Integer.parseInt(l.substring(l.indexOf("channel ") + 8).trim()));
						}	
						else if (l.indexOf("Sent status requests for ") > 0) {
							
							if (filter != null 
									&& filter.containsKey("esl")) { 
								continue;
							}
							
							int idx = l.indexOf("groups: ");
							idx += 8;
							
							for (String s : l.substring(idx).split(",")) {								
								ehandler.onSubmitStatus (d, coordinator, "0x" + s.trim());
							}
							
						}
						
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				in.close();
				
			}

		}
		catch (Throwable t)
		{
			t.printStackTrace();			
		}
		finally 
		{
			try { if (in != null) in.close(); } catch (Throwable emmejoervinoembe) {}
		}		
		
	}

}
