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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.InetAddress;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.castor.core.util.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.data.JdbcConnectionFactory;
import com.s5tech.net.desktop.S5TechDesktopApp;
import com.s5tech.net.entity.EslDataStore;
import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.entity.EslPriceData;
import com.s5tech.net.entity.IEslDataStore;
import com.s5tech.net.entity.IEslEntityManager;
import com.s5tech.net.eslnet.EslFirmwareUpdater;
import com.s5tech.net.eslnet.IEslNetwork;
import com.s5tech.net.firmware.FirmwareLibrary;
import com.s5tech.net.services.logging.ApplicationServicesListener;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.HubInfo;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

public class RemoteControlServices {
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

	private String EOL = "\n";
	private DateFormat timef = null;
	private ApplicationServicesListener networkManager = null;	
	private IEslEntityManager entityManager = null;
	
	public static RemoteControlServices instance_ = null;
		
	private RemoteControlServices()
	{
		log.info("creating network services facade ...");
		networkManager = ApplicationServicesListener.instance();
		entityManager = EslEntityManager.instance();
		if (timef == null) timef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.trace("network services initializer started");
	}
	
	public static synchronized RemoteControlServices getInstance()
	{
		if (instance_ == null) {
			instance_ = new RemoteControlServices();
		}
		return instance_;
	}
	
	
	public String getEOL() {
		return EOL;
	}

	public void setEOL(String eol) {
		EOL = eol;
	}

	public String getVersion()
	{
		String type = null;
		String title = null;
		String version = null;
		
		try {
			if (S5TechDesktopApp.class.getPackage() != null &&
					S5TechDesktopApp.class.getPackage().getImplementationTitle() != null) {
				type = "R";
				title = S5TechDesktopApp.class.getPackage().getImplementationTitle();
				version = S5TechDesktopApp.class.getPackage().getImplementationVersion();		
			}	
		}
		catch (Throwable ignored) {}
		
		if (title != null && version != null) 
			return "result=" + title + " " + type + " " + version; 
		
		for (int i = 0; i < 3; i ++) 
		{
			String basedir = "./";
			if (i > 0) {
				String resName = "/" + getClass().getName().replace('.', '/') + ".class";
				URL url = getClass().getResource(resName);
				basedir = url.getFile();
				basedir = basedir.substring(0, basedir.length() - resName.length());
				if (i == 1) {
					basedir = basedir + "/";
				}
				else if (i == 2) {
					basedir = basedir + "/../";
				}				
			}
			try {
				File rc = null;
				float max = 0;
				for (File f : new File(basedir).listFiles()) {
					if (f.getName().indexOf("build.number.") >= 0) {
						float c = Float.parseFloat(
								f.getName().substring(
								f.getName().indexOf("build.number.") + 13));
						if (c > max) {
							max = c;
							rc = f;
						}
					}
				}			
				if (rc != null){					
					type = "B";
					title = "Network Application";
					Properties p = new Properties();					
					InputStream in = new FileInputStream(rc);
					p.load(in);
					version = p.getProperty("build.number");
					while (version.length() < 4) { version = "0" + version; }
					version = String.valueOf(max).trim() + "." + version;
					in.close();					
				}	
			}
			catch (Throwable ignored) { ignored.printStackTrace(); }
			
			if (title != null && version != null) 
				return "result=" + title + " " + type + " " + version; 
			
			try {
				File f = new File(basedir + "VERSION");
				if (f.exists()){
					type = "V";
					title = "Network Application";
					BufferedReader in = new BufferedReader(new FileReader(f));
					version = in.readLine().trim();
					in.close();
				}	
			}
			catch (Throwable ignored) {}
			
			if (title != null && version != null) 
				return "result=" + title + " " + version + "-" + type; 
			
		}
		
		return "result=Network Application Unversioned";
			
	}
	
	public String getCoordinators(String params)
	{
		boolean hasQueues = (getParam("queues", params) != null && getParam("queues", params).equalsIgnoreCase("true"));		
		boolean channelsList = (getParam("channelslist", params) != null && getParam("channelslist", params).equalsIgnoreCase("true"));		
		SimpleDateFormat dateformat = (getParam("dateformat", params) != null ? new SimpleDateFormat(getParam("dateformat", params).replace('_', ' ')) : null);					
		
		StringBuffer buf = new StringBuffer("port url;channel;esls;mac address;online;online count;offline count;last online time;last offline time;");		
		if (hasQueues) buf.append("msg;lowmsg;bcast;status;");		
		if (channelsList) buf.append("channelslist");		
		buf.append(EOL);
		
		try 
		{	
			Set<String> ports = networkManager.getAll().keySet();
			if (ports == null || ports.size() == 0) return null;
			for (String p : ports)
	    	{
	    		IEslNetwork net =  networkManager.getAll().get(p);
	    		buf.append(p).append(';')
	    				.append(net.getActiveChannel()).append(';')
	    				.append(net.getAssociatedEsls()).append(';')
	    				.append(net.getCoordinatorMac()).append(';')
	    				.append(net.isOnline()).append(';')
	    				.append(net.getOnlineCount()).append(';')
			    		.append(net.getOfflineCount()).append(';')
	    				.append((dateformat != null ? (net.getOnlineTime() <= 0 ? 
	    						"never" : 
	    							dateformat.format(new Date(net.getOnlineTime()))) : net.getOnlineTime())
	    						).append(';')
			    		.append((dateformat != null ? (net.getOfflineTime() <= 0 ? 
			    				"never" : 
			    					dateformat.format(new Date(net.getOfflineTime()))) : net.getOfflineTime())
			    				).append(';');
	    		if (hasQueues) {
	    			buf.append(net.getOutboundSize()).append(';')
	    					.append(net.getOutboundLowPrioritySize()).append(';')
	    					.append(net.getOutboundBroadcastSize()).append(';')
	    					.append(net.getPendingStatusSize()).append(';');
	    		}
	    		if (channelsList) {
	    			buf.append(net.getChannelList()).append(';');    					
	    		}
	    		buf.append(EOL);
	    	}			
		}
		catch (Throwable t) {
			log.error("cannot get coordinators info - reason {}", t);			
		}
		return buf.toString();
	}
	
	public String getCoordinatorsQueues()
	{
		StringBuffer buf = new StringBuffer(
				"port url;channel;mac address;online;esl;esl low;bcast;outgoing;status").append(EOL);				
		try 
		{			
			Set<String> ports = networkManager.getAll().keySet();
			if (ports == null || ports.size() == 0) return null;			
			for (String p : ports)
	    	{
	    		IEslNetwork net =  networkManager.getAll().get(p);    		
	    		buf.append(p).append(';')
	    				.append(net.getActiveChannel()).append(';')
	    				.append(net.getCoordinatorMac()).append(';')
	    				.append(net.isOnline()).append(';')
	    				.append(net.getOutboundSize()).append(';')
	    				.append(net.getOutboundLowPrioritySize()).append(';')
	    				.append(net.getOutboundBroadcastSize()).append(';')
	    				.append(net.getOutQueueSize()).append(';')
	    				.append(net.getPendingStatusSize())
	    				.append(EOL);
	    	}			
		}
		catch (Throwable t) {
			log.error("cannot get coordinators info - reason {}", t);			
		}
		return buf.toString();
	}
	
	public String getHubs()
	{
		StringBuffer b = new StringBuffer("mac address;ip adddress;ports;version;socket").append(EOL);		
		try 
		{
			for (HubInfo hub : ApplicationServicesListener.instance().getHubsList()) {
				b.append(hub.getMac()).append(";")
						.append(hub.getIpAddress()).append(";")
						.append(hub.getPorts()).append(";")
						.append(hub.getVersion()).append(";")
						.append(hub.getProtocol())
						.append(EOL);
			}			
		}
		catch (Throwable t) {
			log.error("cannot get hub list - reason {}", t);			
		}
		return b.toString();
	}
	
	public String setCoordinatorChannel(String params)
	{	
		String mac = getParam("mac", params);
		String port = getParam("port", params);
		String channel = getParam("channel", params);		
		String allowedchannels = getParam("allowedchannels", params);
		
		if (log.isTraceEnabled()) {
			log.trace("set coordinator channels invoked > {}", params);
		}
		
		if (allowedchannels == null) {
			log.error("allowed channels list not specified, exit from set coordinator channels ");
			return "result=fail";
		}
				
		int fail = 0;
		int success = 0;
		
		for (String p : networkManager.getAll().keySet())
    	{    		
			try 
			{
				IEslNetwork net =  networkManager.getAll().get(p);
	    		if (port != null) {
	    			if (!port.equalsIgnoreCase(p)) continue;
	    		}
	    		if (mac != null) {
	    			if (net.getCoordinatorMac() == null 
	    					|| net.getCoordinatorMac().toString().indexOf(mac) < 0) continue;
	    		}
	    		if (channel != null) {
	    			int n = Integer.parseInt(getParam("channel", params));
	    			if (net.getActiveChannel() == null 
	    					|| net.getActiveChannel().getValue() != n) continue;
	    		}
	    		if (log.isTraceEnabled()) {
	    			log.trace("controller setting allowed channels {} to coordinator {} ...", 
	    					allowedchannels, p);
	    		}
	    		
	    		net.setChannelList(allowedchannels, true);
	    		success ++;
	    		
			}
			catch (Throwable t) {
				
				log.error("setting coordinator channel - ",  t);
				fail ++;
			}
    	}
		
    	return new StringBuffer("success=")
    			.append(success).append(EOL)
    			.append("fail=").append(fail).append(EOL)
    			.toString();
    		
	}
	
	public String setCoordinatorReset(String params)
	{
		String mac = getParam("mac", params);
		String port = getParam("port", params);
		String channel = getParam("channel", params);
				
		int fail = 0;
		int success = 0;
		
		for (String p : networkManager.getAll().keySet())
    	{    	
			try 
			{
	    		IEslNetwork net =  networkManager.getAll().get(p);
	    		if (port != null) {
	    			if (!port.equalsIgnoreCase(p)) continue;
	    		}
	    		if (mac != null) {
	    			if (net.getCoordinatorMac() == null 
	    					|| net.getCoordinatorMac().toString().indexOf(mac) < 0) continue;
	    		}
	    		if (channel != null) {
	    			int n = Integer.parseInt(getParam("channel", params));
	    			if (net.getActiveChannel() == null 
	    					|| net.getActiveChannel().getValue() != n) continue;
	    		}    		
	    		log.warn("!!! resetting coordinator {} !!!...", p);
	    		net.reset();
	    		success ++;
			}
			catch (Throwable t) {
				log.error(" - ", t);
				fail ++;
			}    	
    	}
		
		return new StringBuffer("success=")
				.append(success).append(EOL)
				.append("fail=").append(fail).append(EOL)
				.toString();

	}
	
	public String setCoordinatorTime(String filter)
	{
		String mac = getParam("mac", filter);
		String port = getParam("port", filter);
		String channel = getParam("channel", filter);
				
		int fail = 0;
		int success = 0;
		
		for (String p : networkManager.getAll().keySet())
    	{   
			try {
			
	    		IEslNetwork net =  networkManager.getAll().get(p);
	
	    		if (port != null) {
	    			if (!port.equalsIgnoreCase(p)) continue;
	    		}
	
	    		if (mac != null) {
	    			if (net.getCoordinatorMac() == null 
	    					|| net.getCoordinatorMac().toString().indexOf(mac) < 0) continue;
	    		}
	
	    		if (channel != null) {
	    			int n = Integer.parseInt(getParam("channel", filter));
	    			if (net.getActiveChannel() == null 
	    					|| net.getActiveChannel().getValue() != n) continue;
	    		}
	    		
	    		if (log.isTraceEnabled())
	    			log.trace("controller setting time to coordinator {} ...", p);
	    		
	    		net.setTime();	    		
	    		success ++;
	    		
			}
			catch (Throwable t) {
				
				log.error("setting coordinator time - ", t);				
				fail ++;
				
			}
    	
    	}
		
		return new StringBuffer("success=")
				.append(success).append(EOL)
				.append("fail=").append(fail).append(EOL)
				.toString();
	}
	
	
	public String getEsls (String filter)
	{
		String mac = getParam("mac", filter);
		String coordinatorMac= getParam("coordinatorMac", filter);
		String eslType = getParam("eslType", filter);
		
		if (mac != null) mac = mac.toLowerCase();
		if (coordinatorMac != null) coordinatorMac = coordinatorMac.toLowerCase();
		if (eslType != null) eslType = eslType.toLowerCase();
		
		int shortAddress = getParam("shortAddress", filter) == null ? -1 : (getParam("shortAddress", filter).equalsIgnoreCase("null") ? 0 : Integer.parseInt(getParam("shortAddress", filter)));
		int from = getParam("from", filter) == null ? -1 : Integer.parseInt(getParam("from", filter));
		int limit = getParam("limit", filter) == null ? -1 : Integer.parseInt(getParam("limit", filter));
		
		int count = 0;
		
		StringBuffer b = new StringBuffer()
				.append("nr;mac;shortAddress;eslType;coordinatorMac;channel").append(EOL);
			
		try {
			
			HashMap<EUI64Address, String> channelMap = new HashMap<>();
			
			for (String p : networkManager.getAll().keySet())
	    	{   
				try {				
		    		IEslNetwork net =  networkManager.getAll().get(p);
		    		if (net.isOnline() 
		    				&& net.getCoordinatorMac() != null 
		    				&& net.getActiveChannel() != null) {
		    			channelMap.put(
		    					net.getCoordinatorMac(), 
		    					net.getActiveChannel().toString());
		    		}
				}
				catch (Throwable t) {
					log.error("saving channels map - ", t);
				}
	    	}
			
			Map<EUI64Address, EslDeviceInfo> esls = entityManager.getEsls();
			
			for (EUI64Address addr : esls.keySet())
			{		
				if (mac != null) {
					if (addr.toString().toLowerCase().indexOf(mac) < 0) continue;
				}
				
				EslDeviceInfo esl = esls.get(addr);
				
				if (coordinatorMac != null) {
					if (coordinatorMac.equalsIgnoreCase("null"))
					{
						if (esl.getCoordinatorMac() != null) continue;
					}
					else {
						if (esl.getCoordinatorMac() == null) continue;
						if (esl.getCoordinatorMac().toString().toLowerCase().indexOf(coordinatorMac) < 0) continue;
					}
				}
				
				if (eslType != null) {
					if (eslType.equalsIgnoreCase("null"))
					{
						if (esl.getType() != 0) continue;
					}
					else
					{
						int ntype = EslEntityManager.instance().lookupNetworkEquivalentType(eslType);					
						if (esl.getType() != ntype) continue;
					}
				}
				
				if (shortAddress == 0) {
					if (esl.getNetworkAddress() != null 
							&& esl.getNetworkAddress().intValue() > 0x0000 
							&& esl.getNetworkAddress().intValue() < 0xffff) continue;
				}
				else if (shortAddress > 0) {
					if (esl.getNetworkAddress() == null) continue;
					if (esl.getNetworkAddress().intValue() != shortAddress) continue;
				}
			
				count ++;
				if (from > 0 && from > count) continue;			
				if (limit > 0 && count >= from + limit) break;
				
				b.append(count).append(";")
						.append(addr != null ? addr.toString() : "").append(";")
						.append(esl.getNetworkAddress() != null ? esl.getNetworkAddress().toString() : "").append(";")
						.append(esl.getType()).append(";")
						.append(esl.getCoordinatorMac() != null ? esl.getCoordinatorMac() : "").append(";");
				
				b.append((
						esl.getCoordinatorMac() != null ? 
						(channelMap.get(esl.getCoordinatorMac()) != null ? 
								channelMap.get(esl.getCoordinatorMac()) : ""):
						"")
						).append(";");
							
				b.append(EOL);
				
			}
			
		}
		catch (Throwable t) {		
			
			log.error("getting esls - ", t);
			
		}
		
		return b.toString();
		
	}
	
	
	public String getPrice (String filter)
	{
			
		try {
			
			EUI64Address mac = null;
			
			if (filter == null || filter.length() == 0) throw new Exception("cannot get price without query");
			
			boolean ispending = (filter.toLowerCase().indexOf("pending") >= 0);
			
			for (String s : filter.replace('\t', ' ').trim().split(" ")) {				
				if (s.toLowerCase().indexOf("pending") >= 0) {
					ispending = true ;					
				}
				else {
					mac = new EUI64Address(s.toUpperCase());
				}
			}
			
			if (mac == null) throw new Exception("cannot get price without mac address");
			
			IEslDataStore store = EslDataStore.instance(); 
			JdbcConnectionFactory ds = store.getPricesForEsl(mac);
			
			if (ds == null) throw new Exception("price not set for ESL " + mac.toString());
			
			EslPriceData p = (ispending ? ds.getPendingPrice() : ds.getActivePrice());
			
			StringBuffer b = new StringBuffer();
			
			if (p != null && p.getData() != null) {
				b.append("type       = ").append(ispending ? "pending" : "active").append(EOL);
				b.append("activation = ").append(timef.format(p.getActivationTime())).append(EOL);
				b.append("hashcode   = ").append(p.getHashWhenApplied()).append(EOL);
				b.append("msgid      = ").append(p.getRefId()).append(EOL);
				b.append("received   = ").append(p.getReceivedAt()).append(EOL);
				b.append("data       = ").append(Base64Encoder.encode(p.getData())).append(EOL);
				b.append(EOL);
			}
			
			return b.toString();
			
		}
		catch (Throwable t) {		
			
			log.error("getting price - ", t);
			
		}
		
		return "";
		
	}
	
	
	public String getFirmwareUpgradeInfo()
	{
		StringBuffer buff = new StringBuffer()
				.append("status;last optime;device type;fw version;transmission number;")
				.append("total transmissions;packet number;total packets;cnannel;mac").append(EOL);
		
		try 
		{
			for (String p : networkManager.getAll().keySet())
	    	{    		
	    		IEslNetwork net =  networkManager.getAll().get(p);
	    		
				EslFirmwareUpdater fwUpdater = net.getFirmwareUpdater();
				
				if (fwUpdater == null) { 
					continue;
				}
				else if (fwUpdater.getLastOperationTime() == null) {
					continue;
				}
				else {	
					String d = Tools.toHexByte(fwUpdater.getUpdatingDeviceType());
					d = "0x" + (d.length() == 1 ? "0" : "") + d;				
					buff.append((fwUpdater.isUpdating() ? "upgrading" : (fwUpdater.isUpgradeOk() ? "success" : "failure"))).append(";")						
							.append(timef.format(fwUpdater.getLastOperationTime()).replace(' ', 'T')).append(";")
							.append(d).append(";")
							.append(fwUpdater.getVersion()).append(";")
							.append(fwUpdater.getCount()).append(";")
							.append(fwUpdater.getTotal()).append(";")
							.append(fwUpdater.getTransmissionCount()).append(";")
							.append(fwUpdater.getTransmissionSize()).append(";")
							.append(net.getActiveChannel()).append(";")
							.append(EOL);
				}			
	    	}
		}
		catch (Throwable t) {
			log.error("firmware upgrade ... ", t);
		}

		return buff.toString();

	}
	
	public String getFirmwareInfo()
	{
		StringBuffer buff = new StringBuffer(
			"esltype;version;manufacturer id;file").append(EOL);
				
		try 
		{
			Set<Integer> s = FirmwareLibrary.instance().getKnownDeviceTypes();
			if (s != null && s.size() > 0) {
				for (int d : s) {
					String [] fi = FirmwareLibrary.instance().getFirmwareForDeviceType(d).toUnformattedString().split(" ");
					buff.append("0x");
					if (fi[0].length() == 1) buff.append("0").append(fi[0]);					
					buff.append(";").append(fi[1]).append(";").append(fi[2]).append(";").append(fi[3]).append(EOL);					
				}											
			}
		}
		catch (Throwable t) {
			log.error("getting firmware libraries ... ", t);
		}
		
		return buff.toString();
	}
	
	public String retryPriceUpdate(String filter)
	{
		try {
			
			String mac = getParam("mac", filter);
			String port = getParam("port", filter);
			String channel = getParam("channel", filter);
					
			int count = 0;
			
			for (String p : networkManager.getAll().keySet())
	    	{    		
	    		IEslNetwork net =  networkManager.getAll().get(p);

	    		if (port != null) {
	    			if (!port.equalsIgnoreCase(p)) continue;
	    		}

	    		if (mac != null) {
	    			if (net.getCoordinatorMac() == null 
	    					|| net.getCoordinatorMac().toString().indexOf(mac) < 0) continue;
	    		}

	    		if (channel != null) {
	    			int n = Integer.parseInt(getParam("channel", filter));
	    			if (net.getActiveChannel() == null 
	    					|| net.getActiveChannel().getValue() != n) continue;
	    		}
	    		
	    		log.info("controller resubmitting price updates {} ...", p);
	    		
	    		count += net.restorePriceUpdates();
	    	
	    	}
			
			return ("updated=" + String.valueOf(count) + EOL);
			
		}
		catch(Throwable t) {
			
			log.error("error retrying price update ... {}", t);
			return null;
			
		}
		
	}
	
	public String getParameters() 
	{
		StringBuffer b = new StringBuffer();						        	
    	for (Object o : System.getProperties().keySet())
    	{
    		try {
    			b.append(o.toString()).append("=")
    					.append(System.getProperty(o.toString())
    							.replace('\n', ' ').replace('\r', ' ')
    							.replace(';', '|')
    							.replace('<', '{')
    							.replace('>', '}'))
    					.append(';');
    		}						        		
    		catch (Throwable t) {}						        		
    	}
    	return b.toString();	
	}

	public String getTasks() {
		
		StringBuffer buffer = new StringBuffer(
				"id;state;cpu;deadlocked;name;blockedtime;waitedtime;suspended;lockowner;seqnumber");
		
		try {
			
			ThreadMXBean tx = ManagementFactory.getThreadMXBean();
			
			long [] lt = tx.getAllThreadIds();
			long [] dlt = tx.findDeadlockedThreads();
			long [] micro = new long[lt.length];
			long tot = 0;						
			
			if (tx.isThreadCpuTimeSupported() && tx.isThreadCpuTimeEnabled())
			{
				for (int i = 0; i < lt.length; i ++)
				{
					micro [i] = tx.getThreadCpuTime(lt[i]); //cpu time is in nanosec
					tot += micro [i];
				}
			}
			
			String []l = new String [lt.length];
			
			for (int i = lt.length - 1; i >= 0 ; i--)
			{
				StringBuffer b = new StringBuffer();
				ThreadInfo ti = tx.getThreadInfo(lt[i]);
				
				b.append(lt[i]).append(";");
				b.append(ti.getThreadState()).append(";");				
				b.append((tx.isThreadCpuTimeEnabled() ? 
						String.valueOf((int) ((double)micro[i] / (double) tot * 100))
						: "-1"))
						.append(";");
				
				String s = "false";
				if (dlt != null) {
					for (long lk : dlt) {
						if (lk == lt[i]) {
							s = "true";
							break;
						}
					}
				}
				b.append(s);				
				
				b.append(";").append(ti.getThreadName())				
						.append(";").append(ti.getBlockedTime())				
						.append(";").append(ti.getWaitedTime())
						.append(";").append(ti.isSuspended())	
						.append(";").append(ti.getLockOwnerName())
						.append(";").append(String.valueOf(((int)(i + 1))));			
				
				l[i] = b.toString();
			}
			
			//
        	// following code only used to order threads list by CPU usage ...
        	//
        	
        	Comparator<String> ic = new Comparator<String>() {			
        		@Override
        	    public int compare(String s1, String s2) {					
					if ((s1 == null || s1.length() == 0) 
							&& (s2 == null || s2.length() == 0)) {
						return 0;
					}
					else if (s1 == null || s1.length() == 0) {
						return -1;
					}
					else if (s2 == null || s2.length() == 0) {
						return 1;					
					}
					else {
						int n1 = 0, n2 = 0;
						String s[] = new String [] {s1, s2};					
						for (int i = 0; i < 2; i ++) 
						{
							int n = 0;
							String [] pc = s[i].split(";");
							try {
								n = Integer.parseInt(pc[2]);											
							}
							catch(Throwable ignored) {
								n = 0;
							}
							if (i == 0) n1 = n;
							else n2 = n;
						}						
						return (n1 < n2 ? 1 : (n1 == n2 ? 0 : -1));
					}
        	    }        		
			};
			
			Arrays.sort(l, ic);
			
			//
			// end of sort
			//
			
			for (String ll : l) {
				buffer.append(ll).append(EOL);
			}			
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		return buffer.toString();
	}
	
	public String getSystemInfo() {
		try 
		{
			
			StringBuffer b = new StringBuffer();
			String hostname = "unknown";
			
			try {
				hostname = InetAddress.getLocalHost().getHostName();
			}
			catch (Throwable ignoredxxx){ }
			
			b.append("host name=").append(hostname).append(EOL);
			
			OperatingSystemMXBean ox = ManagementFactory.getOperatingSystemMXBean();
			RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
			
			b.append("os name=").append(ox.getName()).append(EOL);
			b.append("os arch=").append(ox.getArch()).append(EOL);
			b.append("os version=").append(ox.getVersion()).append(EOL);
			b.append("jvm vendor=").append(mx.getVmVendor()).append(EOL);
			b.append("jvm name=").append(mx.getVmName()).append(EOL);
			b.append("jvm version=").append(mx.getVmVersion()).append(EOL);
			b.append("application path=").append(new File(".").getAbsolutePath()).append(EOL);
			b.append("start time=").append(timef.format(new Date(mx.getStartTime()))).append(EOL);
			b.append("uptime=").append(mx.getUptime()).append(EOL);
			
			long m = 0;
			
			b.append("processors=").append(ox.getAvailableProcessors()).append(EOL);
			b.append("loadavg=").append((ox.getSystemLoadAverage() >= 0 ? ox.getSystemLoadAverage() : "--")).append(EOL);
			b.append("threads=").append(ManagementFactory.getThreadMXBean().getThreadCount()).append(EOL);
					
			for (int __i_ = 0; __i_ < 3; __i_ ++)
			{
				if (__i_ == 0)
				{
					b.append("mem free=");
					m = Runtime.getRuntime().freeMemory();
				}
				else if (__i_ == 1)
				{
					b.append("mem max=");
					m = Runtime.getRuntime().maxMemory();
				}	
				else if (__i_ == 2)
				{
					b.append("mem total=");
					m = Runtime.getRuntime().totalMemory();
				}
			
				if (m < 1024)
				{
					b.append(m + " bytes");
				}
				else if (m < (1024 * 1024))
				{
					b.append((m / 1024) + " Kb");
				}
				else if (m < (1024 * 1024 * 1024))
				{
					b.append((m / (1024 * 1024)) + " Mb");
				}
				else
				{
					b.append((m / (1024 * 1024 * 1024)) + " Gb");
				}				
				
				b.append(EOL);
				
			}
						
			return b.toString();
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}
	
	
	private static String getParam(String key, String line) 
	{
		if (line == null) return null;
		String find = key + "=";
		if (!line.startsWith(find)) find = " " + find;
		if (line.indexOf(find) >= 0) {
			int start = line.indexOf(find);
			start += find.length();
			int end = line.indexOf(" ", start);
			if (end == -1) end = line.length();
			return line.substring(start, end);
		}
		else return null;
	}
	
		
}

