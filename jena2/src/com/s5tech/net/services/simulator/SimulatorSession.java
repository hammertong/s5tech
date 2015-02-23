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
 
package com.s5tech.net.services.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.s5tech.net.services.simulator.data.EslDataBase;

public class SimulatorSession {

	private static final Logger log = Logger.getLogger("SIMULATOR");

	public static final String DEFAULT_CONFIG = "./simulator.properties";

	private static final int DEFAULT_PORT = 55550;
	private static final int DEFAULT_CHANNEL = 12;
	private static final int DEFAULT_JOIN_DELAY = 50;
	private static final int DEFAULT_JOIN_SIZE = 0;
	private static final int DEFAULT_REJOIN_DELAY = 1000;

	private String configurationFile = null;
	private IBufferListener listener = null;
	
	private HashMap<String, BufferSimulator> simlist = new HashMap<>();
	private HashMap<String, Thread> threadlist = new HashMap<>();

	public SimulatorSession() {
		this(null, null);
	}

	public SimulatorSession(String configurationFile) {
		this(configurationFile, null);
	}

	public SimulatorSession(String configurationFile, IBufferListener listener) {
		this.configurationFile = (configurationFile == null ? DEFAULT_CONFIG
				: configurationFile);
		this.listener = listener;
	}

	public IBufferListener getListener() {
		return listener;
	}

	public void setListener(IBufferListener listener) {
		this.listener = listener;
	}

	public static class XMLConfigurationHandler extends DefaultHandler {

		private StringBuffer buffer = null;
		private Properties props = null;		
		private String prefix = ""; 
		private int ncount = 0;		
		
		public Properties getConfigurationProperties() {
			return props;
		}

		public void startElement(String namespaceURI, String lName,
				String qName, Attributes attrs) throws SAXException {

			buffer = new StringBuffer();
			
			String eName = lName; // Element name
			
			if ("".equals(eName)) eName = qName;

			//System.err.println("start *** " + eName);
			
			if (eName.equals("session"))
			{
				props = new Properties();				
			}
			else if (eName.equals("node"))
			{
				ncount ++;
				prefix = "SIM." + ncount + ".";
			}
			else if (eName.equals("node-default"))
			{
				prefix = "DEFSIM.";
			}
			else if (eName.equals("database"))
			{
				prefix = "jdbc.";
			}
			else if (eName.equals("join-test"))
			{
				prefix += "join.";
				
				for (int i = 0; i < attrs.getLength(); i++) {
					if (attrs.getQName(i).equals("autostart")) 
					{
						if (attrs.getValue(i).toLowerCase().equals("yes") 
								|| attrs.getValue(i).toLowerCase().equals("true"))
						{
							props.put ("DEFSIM.autostart_join", "true");
						}
						else 
						{
							props.put ("DEFSIM.autostart_join", "false");
						}
					}
				}
			}			
			else if (eName.equals("broadcast")) 
			{
				for (int i = 0; i < attrs.getLength(); i++) {
					if (attrs.getQName(i).equals("on")) 
					{
						if (attrs.getValue(i).toLowerCase().equals("yes") 
								|| attrs.getValue(i).toLowerCase().equals("true"))
						props.put ("BROADCAST.on", "true");
						prefix = "BROADCAST.";
					}
					else 
					{
						props.put ("BROADCAST.on", "false");
					}
				}
			}
			
		}

		public void endElement(
					String namespaceURI, 
					String sName, 
					String qName)
				throws SAXException {
			
			if (qName.equals("session") 
					|| qName.equals("node") 
					|| qName.equals("node-default")
					|| qName.equals("broadcast"))
			{
				prefix = "";
			}
			else if (qName.equals("join-test"))
			{
				prefix = prefix.substring(0, prefix.length() - "join.".length());
			}	
			else if (qName.equals("database"))
			{
				prefix = "";
			}
			else if (prefix.equals("jdbc."))
			{
				System.setProperty(prefix + qName.replace('-', '_'), 
						buffer.toString()
						.replace('\n', ' ')
						.replace('\r', ' ')
						.replace('\t', ' ').trim());
			}
			else
			{
				props.put(
						prefix + qName.replace('-', '_'), 
						buffer.toString()
								.replace('\n', ' ')
								.replace('\r', ' ')
								.replace('\t', ' ').trim());
				
			}			
		}

		public void characters(char buf[], int offset, int len)
				throws SAXException {
			for (int i = 0; i < len; i ++)
			{
				buffer.append(buf[i + offset]);
			}
		}

	}

	public void start() {

		Properties props = null;

		try {
			if (configurationFile.toLowerCase().endsWith(".xml")) {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				XMLConfigurationHandler handler = new XMLConfigurationHandler();
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse(new File(configurationFile), handler);
				props = handler.getConfigurationProperties();
			} else {
				FileInputStream in = new FileInputStream(configurationFile);
				props = new Properties();
				props.load(in);
			}
		} catch (Throwable t) {
			log.log(Level.SEVERE, "error loading configuration from " + configurationFile, t);
			props = null;
		}

		if (props == null) {
			
			if (configurationFile != null)
				log.warning("configuration file '" + configurationFile
					+ "' not found (or not readable?) ! ");
			else log.info("configuration file not set: using default single node simulator ...");
			
			props = new Properties();
			props.put("SIM.1.coordinator_mac", " 0012000077707007");
			props.put("SIM.1.channel", "12");
			props.put("SIM.1.bind_address", " 127.0.0.1");
			props.put("SIM.1.bind_port", " 55550");
		}

		Collection<String> keylist = new ArrayList<String>();

		for (Object o : props.keySet()) {
			if (!(o instanceof String))
				continue;
			String k = (String) o;
			if (k.startsWith("SIM.")) {
				String p = k.split("\\.")[1];
				if (keylist.contains(p))
					continue;
				keylist.add(p);
			}
		}

		int _deport = 55550;
		String _demac = "00FF000000000000";
		int _dechannel = 11;

		String _def_initial_delay = props.getProperty("DEFSIM.join.startup_delay", "1000");
		String _def_join_delay = props.getProperty("DEFSIM.join.delay", "1000");
		String _def_joinsize = props.getProperty("DEFSIM.join.maxsize", "0");
		String _def_rejoin_delay = props.getProperty("DEFSIM.join.restart_delay", "1000");
		String _def_rejoin_loops = props.getProperty("DEFSIM.join.loops", "0");
		String _def_offlinesecs = props.getProperty("DEFSIM.offline_secs", "-1");
		String _def_timeslot_delay = props.getProperty("DEFSIM.timeslot_wait", "16000");
		String _def_beacon_timeout = props.getProperty("DEFSIM.beacon_timeout", "15670");
		String _def_autostart = props.getProperty("DEFSIM.autostart_join", "true");
		
		String coords = "";

		int port = DEFAULT_PORT;
		int channel = DEFAULT_CHANNEL;
		int join_delay = DEFAULT_JOIN_DELAY;
		int joinsize = DEFAULT_JOIN_SIZE;
		int rejoin_delay = DEFAULT_REJOIN_DELAY;

		for (String k : keylist) {
			String prefix = "SIM." + k + ".";

			try {
				String mac = props.getProperty(prefix + "coordinator_mac", _demac);
				port = Integer.parseInt(props.getProperty(prefix + "bind_port",
						String.valueOf(_deport)));
				if (port == _deport)
					_deport++;
				channel = Integer.parseInt(props.getProperty(
						prefix + "channel", String.valueOf(_dechannel)));
				if (channel == _dechannel) {
					_dechannel++;
					if (_dechannel > 26)
						_dechannel = 11;
				}
				int initial_delay = Integer.parseInt(props.getProperty(prefix
						+ "join.startup_delay", _def_initial_delay));
				join_delay = Integer.parseInt(props.getProperty(prefix
						+ "join.delay", _def_join_delay));
				joinsize = Integer.parseInt(props.getProperty(prefix
						+ "join.maxsize", _def_joinsize));
				rejoin_delay = Integer.parseInt(props.getProperty(prefix
						+ "join.restart_delay", _def_rejoin_delay));
				String bind_address = props.getProperty(
						prefix + "bind_address", "127.0.0.1");
				int rejoin_loops = Integer.parseInt(props.getProperty(prefix
						+ "join.loops", _def_rejoin_loops));
				int offlinesecs = Integer.parseInt(props.getProperty(prefix
						+ "offline_secs", _def_offlinesecs));
				int timeslot_occ_delay = Integer.parseInt(props.getProperty(
						prefix + "timeslot_wait", _def_timeslot_delay));
				int beacon_timeout = Integer.parseInt(props.getProperty(prefix
						+ "beacon_timeout", _def_beacon_timeout));
				
				boolean join_automatic_start = Boolean.parseBoolean
						(props.getProperty(prefix
						+ "autostart_join", _def_autostart));

				if (port >= 55550 && port < 60000) {
					coords += String.valueOf((int) (port - 55550)).trim()
							+ "\n";
				}

				BufferSimulator sim = new BufferSimulator(port, bind_address,
						mac, channel, joinsize, initial_delay, join_delay,
						rejoin_delay, rejoin_loops, offlinesecs,
						timeslot_occ_delay, beacon_timeout, join_automatic_start);
				
				if (listener!= null) sim.setListener(listener);
				
				String name = "tcp://" + bind_address + ":" + port + " COORD("
						+ mac + ")";
				
				Thread t = new Thread(sim, name);
				t.setDaemon(true);
				t.start();
				
				synchronized (simlist) {
					threadlist.put(mac, t);
					simlist.put(mac, sim);	
				}
				
			} catch (Throwable t) {
				log.log(Level.SEVERE, "cannot initialize simulator " + prefix, t);
			}

		}

		if (props.getProperty("BROADCAST.on", "").equalsIgnoreCase("true")
				|| props.getProperty("BROADCAST.on", "")
						.equalsIgnoreCase("yes")) {
			if (coords.length() > 0) {
				String ccmac = props.getProperty("BROADCAST.mac",
						"00FE00000000");
				String baddr = props.getProperty("BROADCAST.ip", "127.0.0.1");
				int bport = Integer.parseInt(props.getProperty(
						"BROADCAST.port", "55559"));
				int timeout = Integer.parseInt(props.getProperty(
						"BROADCAST.timeoutsecs", "30"));
				timeout *= 1000;
				Broadcaster b = new Broadcaster(baddr, bport, timeout, "ID="
						+ ccmac + ";COORDS=" + coords);
				Thread bt = new Thread(b);
				bt.setDaemon(true);
				bt.start();
			}
		}
		
		
		if (props.getProperty("REJOIN.list", "").length() > 0) 
		{			
			final int timeout = Integer.parseInt(props.getProperty("REJOIN.timeout", "60000"));
			final List<String> partialRejoinEsls = new ArrayList<>();
								
			if (props.getProperty("REJOIN.list").toLowerCase().indexOf("select") >= 0) {
				partialRejoinEsls.addAll(
						new EslDataBase().loadEslsFromQuery(props.getProperty("REJOIN.list")));
			}
			else {
				for (String s : props.getProperty("REJOIN.list", "").split(",")) {
					s = s.replace('\t', ' ').trim().toUpperCase();
					if (s.length() != 16) continue;
					partialRejoinEsls.add(s);
				}
			}
			
			log.finest("Initializing STATIC REJOINER with " + partialRejoinEsls.size() 
					+ " ESLs, coordinator-jump timeout = " + timeout + "ms");
			
			Thread joiner = new Thread(
					
					new Runnable() {						
						
						@Override
						public void run() {
							
							try {
							
								while (true)
								{
								
									for (int i = 0; i < simlist.size(); i ++) {
										
										int count = 0;
										
										for (BufferSimulator s : simlist.values()) {									
											if (count == i) {
												s.addEsls(partialRejoinEsls);
											}
											else {
												s.removeEsls(partialRejoinEsls);
											}
											count ++;										
										}
										
										count = 0;
										
										for (BufferSimulator s : simlist.values()) {									
											if (count == i) {
												
												while (!s.isConnected()) {
													log.info("WAITING to be connected for coordinator " + s.getMacAddress() + " REJOIN ...");
													Thread.sleep(5000);
												}
												
												log.info("New REJOIN for coordinator " + s.getMacAddress() + " ...");
												for ( ;;)  {
													try {														
														s.rejoin(partialRejoinEsls);
														break;
													}
													catch (Throwable t) {
														log.warning("new rejoin start failure: waiting 5"
																+ " seconds, then try to restart ...");
														Thread.sleep(5000);													
													}
												}
												s.waitForRejoinCompleted();												
												break;
											}									
											count ++;										
										}	
										
										Thread.sleep(timeout);
			
									}
									
								}
								
							}
							catch (Throwable t) {
								
								t.printStackTrace();
								
							}
							
							log.severe("EXIT from Static REJOIN thread!");
							
						}
					}, "Static Joiner");
			
			joiner.setDaemon(true);
			joiner.start();
		}

	}
	
//	public void doRejoin()
//	{
//		synchronized (simlist) {
//			for (BufferSimulator s : simlist.values())
//			{
//				try { s.startJoin(); } catch (Throwable t) { t.printStackTrace(); }
//			}
//		}
//	}
	
	public void stop()
	{
		synchronized (simlist) {
			for (BufferSimulator s : simlist.values())
			{
				try { s.stop();	} catch (Throwable ignored) {}
			}
			for (Thread t : threadlist.values())
			{
				try { t.interrupt(); } catch (Throwable ignored) {}
			}			
		}
	}

}
