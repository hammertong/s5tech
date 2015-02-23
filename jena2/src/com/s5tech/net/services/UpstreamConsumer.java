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
 
package com.s5tech.net.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Properties;

import com.s5tech.net.entity.EslDataStore;
import com.s5tech.net.services.client.ActiveMQConsumer;
import com.s5tech.net.services.client.ActiveMQPublisherConnectionClose;
import com.s5tech.net.services.display.LCDPayload;
import com.s5tech.net.type.EUI64Address;

import org.castor.core.util.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpstreamConsumer implements Runnable {

	private static final Logger log = LoggerFactory.getLogger("APPLICATION.UPSTREAMCONSUMER");
	
	private ActiveMQPublisherConnectionClose publisher = null;

	private String xml_statusreq = null;
	private String xml_priceupdate = null;
	private LCDPayload lcd = null;
	private Properties displayProperties = null;
	
	LinkedList<String> statuslist = new LinkedList<String>();
	LinkedList<String> alreadystatuslist = new LinkedList<String>();
	LinkedList<String> priceupdate = new LinkedList<String>();
	
	Object synclist = "";
		
	int nextid = 0;	

	private static String amqurl = System.getProperty("UpstreamConsumer.amqurl", "vm://S5NET");
	private static String amqup = System.getProperty("UpstreamConsumer.amqup", "dynamicQueues/UP");
	private static String amqdown = System.getProperty("UpstreamConsumer.amqdown", "dynamicQueues/DOWN");
	private static boolean gbreq = false;
		
	/** xml status tag used to display price - default is battery level */
	private static String xmlkey = System.getProperty("UpstreamConsumer.pricekey", "batteryLevel");
	private static boolean displayfw = Boolean.parseBoolean(System.getProperty("UpstreamConsumer.fwversion", "true"));
	
	public UpstreamConsumer() {
		
		gbreq = System.getProperty("UpstreamConsumer.statusreq", "").equalsIgnoreCase("global");
		
		log.info("initializing upstream consumer plugin with {} status requester ...", (gbreq ? "global" : "group"));				
		
		try {
			
			lcd = new LCDPayload();
			
			displayProperties = new Properties();
			displayProperties.load(getClass().getResourceAsStream(
					"/com/s5tech/net/services/display/EslPayloadEmpty.properties"));
			
			BufferedReader in1 = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream(
					"/com/s5tech/net/services/xml/EslStatusRequest.xml")));
			
			xml_statusreq = "";
			for ( ;;) {
				String t = in1.readLine();
				if (t == null) break;
				xml_statusreq += t;
				xml_statusreq += "\n";
			}
			in1.close();
			
			BufferedReader in2 = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(
					"/com/s5tech/net/services/xml/EslPriceUpdate.xml")));
			
			xml_priceupdate = "";
			for ( ;;) {
				String t = in2.readLine();
				if (t == null) break;
				xml_priceupdate += t;
				xml_priceupdate += "\n";
			}
			in2.close();
			
			nextid = 0;
			
			if (gbreq) {
				Thread t = new Thread (new Runnable() {
					
					@Override
					public void run() {
						
						try {
							String globalstatusreq = "";
							globalstatusreq += "<?xml version='1.0' encoding='UTF-8'?>";
							globalstatusreq += "<message msgId=\"12\" msgCommand=\"EslStatusRequest\" "; 
							globalstatusreq += "xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://s5tech.com/network\"> ";
							globalstatusreq += "<eslList all=\"true\" />";							
							globalstatusreq += "</message>";
							log.info("status requester started ...");
							for ( ;; ) {
								Thread.sleep(30000);
								log.info("sending global status request ...");
								publisher.publish(globalstatusreq);	
							}
						}
						catch (Throwable t) {
							t.printStackTrace();
						}
					}
				}, "status requester");
				
				t.setDaemon(true);
				t.start();
			}
				
		}
		catch (Throwable t) {
			
			log.error("initializer error - reason {}", t);
			
		}
	}

	private String findParam(String key, String message) {
		String value = null;
		try {			
			int n = message.indexOf(key + "=\"");
			if (n > 0) {
				n += key.length();
				n += 2;
				value = message.substring(n, message.indexOf('"', n));				
//System.err.println("-------------> " + message.substring(n, message.indexOf('"', n)));
//System.err.println("-----------> " + message.substring(n, message.indexOf('"', n - 1)));
//System.err.println("---------> " + message.substring(n, message.indexOf('"', n - 2)));				
				return value.trim();
			}
			else {
				n = message.indexOf("<" + key + ">");
				if (n > 0) {
					n += key.length();
					n += 2;
					value = message.substring(n, message.indexOf('<', n));
					return value.trim();
				}				
			}
			
		} catch (Throwable t) {
			log.error("cannot parse param {} from xml - {}", key, t);
		}
		return null;
	}
		
	public void onMessage(String message) throws Exception {
		
		//log.info(":: RECEIVED MESSAGE > {}", message);
		
		try {
			
			if (message == null) {
				log.warn("received null message!");
				return;
			}

			String cmd = findParam("msgCommand", message);

			if (message.indexOf("<message ") >= 0) {
				
				if (cmd.equals("EslEvent")) {
					
					String type = findParam("type", message);
					String esl = findParam("eslMac", message);					
					
					if ((type.equals("Joined")) || (type.equals("Rejoined"))) {						
						
						String ccmac = findParam("coordinatorMac", message);
						log.info("ESL {} joined to {}", esl, ccmac);
						
						if (xmlkey.equalsIgnoreCase("shortaddress")) return; //cortocircuitato da netctl
						
						//if (statuslist.contains(esl)) {
							log.trace("removing esl {} from status request list and cleaning datastore ...", esl);
							synchronized (synclist) {
								try { statuslist.remove(esl); } catch (Throwable ignorred) {}	
								try { alreadystatuslist.remove(esl);} catch (Throwable ignorred) {}
								try { priceupdate.remove(esl);} catch (Throwable ignorred) {}
							}							
						//}
						
						EslDataStore.instance().removeActivePriceForEsl(new EUI64Address(esl));
						
					} 
					else if (type.equals("PriceUnknown")) {						
						
//						String esltype = findParam("eslType", message);
//						
//						if (xmlkey.equalsIgnoreCase("shortaddress")) return; //cortocircuitato da netctl
//
//						if ("56".indexOf(esltype) < 0) {
//							log.warn("esl {} is not in subset ESL50/70 type is {}, skipping status request ...!",
//									esl, esltype);
//							return;
//						}
//
//						if (alreadystatuslist.contains(esl)) {
//							log.trace("status request already requested for esl " + esl);
//							return;
//						}
//						
////						if (statusrecvlist.contains(esl)) {
////							log.trace("status request already received for esl " + esl);
////							return;
////						}
//						
//						log.info("received PriceUnknown for ESL {} type {}, sending status request to this esl ... ", 
//								esl, esltype);
//						
//						synchronized (synclist) {
//							statuslist.add(esl);
//							alreadystatuslist.add(esl);
//						}
//												
////						String xml = xml_statusreq
////								.replaceFirst("@msgid", id)
////								.replaceFirst("@mac", esl);
////						publisher.publish(xml);						
						
						
					} 
					else if (type.equals("UnauthorizedJoinAttempt")) {
						log.info("received UnauthorizedJoinAttempt for ESL {}", esl);
					} 
					else if (type.equals("Scanprobe")) {
						log.info("received Scanprobe for ESL {}", esl);
					} 
					else {
						log.error("type not managed parsed in eslEvent {}", type);
					}
					
				} 
				else if (cmd.equals("EslStatus")) {					
					
					String id = String.valueOf(++nextid);
					String esl = findParam("mac", message);
					
					String blevel = findParam(xmlkey, message);
					
					String eslType = findParam("deviceType", message);
					String activationtime = "2000-01-01T00:00:00";
					
//					synchronized (synclist) {
//						if (!statusrecvlist.contains(esl))
//							statusrecvlist.add(esl);
//					} 
					
					if ("56".indexOf(eslType) < 0) {
						log.warn("esl {} is not in subset ESL50/70 type is {}, skipping price update ...!",
								esl, eslType);
						return;
					}
					
					if (priceupdate.contains(esl)) {
						log.trace("price update already sent to {} ... skipping!", esl);						
						return;
					}					
					
					if (displayfw) {
						String fwversion = findParam("firmwareVersion", message);
						if (fwversion == null || fwversion.length() == 0) {
							log.error("cannot read firmare upgrade from status message of esl "
									+ esl + "  ...");
						}
						else {
							String [] pp = fwversion.split("\\.");
							displayProperties.put("hexdigit1", pp[0]);
							displayProperties.put("hexdigit2", pp[1]);
							displayProperties.put("hexdigit3", pp[2].substring(0, 1));						
							displayProperties.put("smallprice", pp[2].substring(2) + ".00"); 
							if (pp[2].indexOf('D') > 0) {
								displayProperties.put("d", "1");
								displayProperties.put("a", "0");
							}
							else {
								displayProperties.put("d", "0");
								displayProperties.put("a", "1");
							}						
						}
					}
					
					try {
						if (eslType.equals("5") && Integer.parseInt(blevel) > 199) blevel = "199";
					}
					catch (Throwable t) {
						log.error("parsing " + blevel + " {} in esl typw 5: {}", blevel, t);
					}
					
					displayProperties.put("bigprice", blevel + ".00");					
					displayProperties.put("esltype", eslType);					
					
					log.warn("received status for ESL {} type {}, sending battery level price " 
							+ displayProperties.get("bigprice") 
							+ " price update ...", 
								esl, 
								displayProperties.get("esltype"));
					
					byte [] data = lcd.createPrice(displayProperties, null);					
					byte [] hash = lcd.calculateEslHash(data);
					
					String base64data =	new String(Base64Encoder.encode(data));
					String hashcodest = String.valueOf(getLowEndianUInt(hash, 0, 4)).trim();
										
					String xml = xml_priceupdate
								.replaceFirst("@mac", esl)
								.replaceFirst("@msgid", id)
								.replaceFirst("@data", base64data)
								.replaceFirst("@activationtime", activationtime)
								.replaceFirst("@hash", hashcodest);		
					
					publisher.publish(xml);
					
					synchronized (synclist) {
						priceupdate.add(esl);
					}
					
				}
				else {
					log.info("Received message of type {}", cmd);
				}
			} else if (message.indexOf("<ackMessage ") >= 0) {
				
				log.info("received ack message of type {}", cmd);
				
				if (cmd.equalsIgnoreCase("EslPriceUpdate")) {
					String esl = findParam("mac", message);
					String result = findParam("result", message);					
					String id = findParam("msgId", message);
					log.info("received priceupdate acknowledge {} for ESL {}, MSGID=> " 
							+ id,
							result, esl
							);
					
					synchronized (synclist) {
						statuslist.remove(esl);
						//priceupdate.remove(esl);							
					}
					
					//EslDataStore.instance().removeActivePriceForEsl(new EUI64Address(esl));
					
				}
				
				
			}

		} catch (Throwable t) {
			log.error("processing incoming message {}\nreason: {}",
					message, t);
					
		}
	}
	
	public long getLowEndianUInt(final byte[] data, int offset, int len) {
		long result = 0;
		for (int j = offset + len - 1; j >= 0; j--) {
			result <<= 8;
			result |= 0xff & data[j];
		}
		return result;
	}
	
	@Override
	public void run() {
		ActiveMQConsumer consumer = null;
		try {
			Thread.sleep(5000);
			log.info("starting ...");
			publisher = new ActiveMQPublisherConnectionClose(amqurl, amqdown);			
			consumer = new ActiveMQConsumer();
			consumer.setUrl(amqurl);
			consumer.setQueueName(amqup);
			if (!consumer.open()) throw new Exception("cannot connect to broker!");			
			log.info("AUTOUPDATER WITH " + xmlkey.toUpperCase() 
					+ " SET CONNECTED! DISPLAY FIRMWARE VERSION: " 
						+ String.valueOf(displayfw).toUpperCase());
			Thread st = new Thread(new Runnable() {				
				@Override
				public void run() {					
					try {
						for ( ;;) {
							Thread.sleep(15700);
							int count = 0;
							String elist = "";
							synchronized (synclist) {
								while (statuslist.size() > 0 && count < 7) {
									String esl = null;
									try {
										esl = statuslist.removeFirst();
									}									
									catch (Throwable iii) {
										esl = null;
									}
									if (esl != null) {
										elist += "<mac>";
										elist += esl;
										elist += "</mac>";
										count ++;
									}
								}
								if (count > 0) {	
									log.info("sending status request for {} esls ...", count);
									String xml = xml_statusreq
											.replaceFirst("@msgid", String.valueOf(++nextid))
											.replaceFirst("@maclist", elist);
									publisher.publish(xml);
								}
							}							
						}
					}
					catch (Throwable t) {
						log.error("exit status transmitter !!! - {}", t);						
					} 
				} 
			}, "upstream consumer - status transmitter");
			st.setDaemon(true);
			st.start();
			
			while (true) {				
				String msg = null;
				try {
					msg = consumer.receive();
					onMessage(msg);
				}
				catch (Throwable xx) {
					log.error("received message exception - MSG > {}\r\nEXCEPTION : {} ", msg, xx);
				}
			}
		}
		catch (Throwable t) {
			log.error("consumer EXIT !!! ERROR - {}", t);
		}
		finally {
			if (consumer != null) try { consumer._close(); } catch (Throwable ignored) {}
		}
	}

}
