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
 
package com.s5tech.net.services.webapp;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.desktop.S5TechDesktopApp;
import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.entity.IEslEntityManager;
import com.s5tech.net.eslnet.EslFirmwareUpdater;
import com.s5tech.net.eslnet.IEslNetwork;
import com.s5tech.net.firmware.FirmwareLibrary;
import com.s5tech.net.services.logging.ApplicationServicesListener;
import com.s5tech.net.type.Channel;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.HubInfo;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

public class AdminLocalServices 
		implements IAdminServices {
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

	private static DateFormat timef = null;
	
	private ApplicationServicesListener networkManager = null;	
	private IEslEntityManager entityManager = null;
	
	public AdminLocalServices()
	{		
		networkManager = ApplicationServicesListener.instance();
		entityManager = EslEntityManager.instance();
		timef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*
		Thread initializer = new Thread(
				new Runnable() {
					@Override
					public void run() {						
						try {							
							Thread.sleep(1000);
							log.info("creating new network services facade ...");
							networkManager = EslNetworksManager.instance();
							entityManager = EslEntityManager.instance();
							if (timef == null) timef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							ready = true;
							log.info("network services facade initialized");
						}
						//catch (InterruptedException ie) {
						//	log.trace("network services facade initializer interrupted");
						//}
						catch (Throwable t) {
							log.error("error creating network services facade : {}", t); 
						}						
					}					
				}, "Network services facade initializer");		
		
		initializer.setDaemon(true);
		initializer.start();	
		*/
		log.trace("network services initializer started");
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
	
	public String[] getCoordinators(String params)
	{
		try 
		{
			int count = 0;
			Set<String> ports = networkManager.getAll().keySet();
			if (ports == null || ports.size() == 0) return null;
			
			boolean hasQueues = (getParam("queues", params) != null 
					&& getParam("queues", params).equalsIgnoreCase("true"));
			
			SimpleDateFormat dateformat = (getParam("dateformat", params) != null ? 
					new SimpleDateFormat(getParam("dateformat", params).replace('_', ' ')) : null);
						
			boolean channelsList= (getParam("channelslist", params) != null 
					&& getParam("channelslist", params).equalsIgnoreCase("true"));
			
			String [] l = new String [ports.size()];
			for (String p : ports)
	    	{
	    		IEslNetwork net =  networkManager.getAll().get(p);
	    		StringBuffer buf = new StringBuffer()
	    				.append("port url=").append(p).append(';')
	    				.append("channel=").append(net.getActiveChannel()).append(';')
	    				.append("esls=").append(net.getAssociatedEsls()).append(';')
	    				.append("mac address=").append(net.getCoordinatorMac()).append(';')
	    				.append("online=").append(net.isOnline()).append(';')
	    				.append("online count=").append(net.getOnlineCount()).append(';')
			    		.append("offline count=").append(net.getOfflineCount()).append(';')
	    				.append("last online time=").append(
	    						(dateformat != null ? (net.getOnlineTime() <= 0 ? "never" : dateformat.format(new Date(net.getOnlineTime()))) : net.getOnlineTime())
	    						).append(';')
			    		.append("last offline time=").append(
			    				(dateformat != null ? (net.getOfflineTime() <= 0 ? "never" : dateformat.format(new Date(net.getOfflineTime()))) : net.getOfflineTime())
			    				).append(';');
	    		if (hasQueues) {
	    			buf.append("msg=").append(net.getOutboundSize()).append(';')
    					.append("lowmsg=").append(net.getOutboundLowPrioritySize()).append(';')
    					.append("bcast=").append(net.getOutboundBroadcastSize()).append(';')
    					.append("status=").append(net.getPendingStatusSize()).append(';');
	    		}
	    		if (channelsList) {
	    			buf.append("channelslist=").append(net.getChannelList()).append(';');    					
	    		}
	    		l[count ++] = buf.toString();
	    	}
			return l;
		}
		catch (Throwable t) {
			log.error("cannot get coordinators info - reason {}", t);			
		}
		return null;
	}
	
	public String[] getCoordinatorsQueues()
	{
		try 
		{
			int count = 0;
			Set<String> ports = networkManager.getAll().keySet();
			if (ports == null || ports.size() == 0) return null;
			String [] l = new String [ports.size()];
			for (String p : ports)
	    	{
	    		IEslNetwork net =  networkManager.getAll().get(p);    		
	    		l[count ++] =new StringBuffer()
	    				.append("port url=").append(p).append(';')
	    				.append("channel=").append(net.getActiveChannel()).append(';')
	    				.append("mac address=").append(net.getCoordinatorMac()).append(';')
	    				.append("online=").append(net.isOnline()).append(';')
	    				.append("esl=").append(net.getOutboundSize()).append(';')
	    				.append("esl low=").append(net.getOutboundLowPrioritySize()).append(';')
	    				.append("bcast=").append(net.getOutboundBroadcastSize()).append(';')
	    				.append("outgoing=").append(net.getOutQueueSize()).append(';')
	    				.append("status=").append(net.getPendingStatusSize()).append(';')
	    				.toString();
	    	}
			return l;
		}
		catch (Throwable t) {
			log.error("cannot get coordinators info - reason {}", t);			
		}
		return null;
	}
	
	public String[] getHubs()
	{
		try 
		{
			StringBuffer b = new StringBuffer();		
			try 
			{
				for (HubInfo hub : ApplicationServicesListener.instance().getHubsList()) {
					b.append("mac address=").append(hub.getMac()).append(";")
							.append("ip address=").append(hub.getIpAddress()).append(";")
							.append("ports=").append(hub.getPorts()).append(";")
							.append("version=").append(hub.getVersion()).append(";")
							.append("socket=").append(hub.getProtocol())
							.append('\n');
				}			
			}
			catch (Throwable t) {
				log.error("cannot get coordinators info - reason {}", t);			
			}
			return b.toString().split("\n");			
		}
		catch (Throwable t) {
			log.error("cannot get hub list - reason {}", t);			
		}
		return null;
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
    			int n = Integer.parseInt(getParam("channel", params));
    			if (net.getActiveChannel() == null 
    					|| net.getActiveChannel().getValue() != n) continue;
    		}
    		
    		log.info("controller setting allowed channels {} to coordinator {} ...", allowedchannels, p);
    		
    		net.setChannelList(allowedchannels, true);	
    	}
		
    	return "result=ok";
    		
	}
	
	public String setCoordinatorReset(String params)
	{
		String mac = getParam("mac", params);
		String port = getParam("port", params);
		String channel = getParam("channel", params);
				
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
    			int n = Integer.parseInt(getParam("channel", params));
    			if (net.getActiveChannel() == null 
    					|| net.getActiveChannel().getValue() != n) continue;
    		}
    		
    		log.info("!!! resetting coordinator @ {} !!!...", p);
    		net.reset();
    	
    	}
		
		return null;

	}
	
	public String setCoordinatorTime(String filter)
	{
		String mac = getParam("mac", filter);
		String port = getParam("port", filter);
		String channel = getParam("channel", filter);
				
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
    		
    		log.info("controller setting time to coordinator {} ...", p);
    		net.setTime();
    	
    	}
		
		return null;
	}
	
	
	public String[] getEsls (String filter)
	{
		String mac = getParam("mac", filter);
		String coordinatorMac= getParam("coordinatorMac", filter);
		String eslType = getParam("esltype", filter);
		
		if (mac != null) mac = mac.toLowerCase();
		if (coordinatorMac != null) coordinatorMac = coordinatorMac.toLowerCase();
		if (eslType != null) eslType = eslType.toLowerCase();
		
		int shortAddress = getParam("shortAddress", filter) == null ? -1 : (getParam("shortAddress", filter).equalsIgnoreCase("null") ? 0 : Integer.parseInt(getParam("shortAddress", filter)));
		int from = getParam("from", filter) == null ? -1 : Integer.parseInt(getParam("from", filter));
		int limit = getParam("limit", filter) == null ? -1 : Integer.parseInt(getParam("limit", filter));
		
		int count = 0;
		
		StringBuffer b = new StringBuffer();
		Map<EUI64Address, EslDeviceInfo> esls = entityManager.getEsls();
		
		Map<EUI64Address, Channel> channelsMap = new HashMap<EUI64Address, Channel>();
		for (String p : networkManager.getAll().keySet())
    	{    		
    		IEslNetwork net =  networkManager.getAll().get(p);
    		if (net.getCoordinatorMac() == null) continue;
    		if (net.getActiveChannel() == null) continue;
    		if (!channelsMap.containsKey(net.getCoordinatorMac())) channelsMap.put(net.getCoordinatorMac(), net.getActiveChannel());
    	}

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
			
			String channel = ((esl.getCoordinatorMac() != null && channelsMap.containsKey(esl.getCoordinatorMac())) ? 
					channelsMap.get(esl.getCoordinatorMac()).toString() : "");
			
			b.append("mac=").append(addr.toString()).append(";")
					.append("shortAddress=").append(esl.getNetworkAddress().toString()).append(";")
					.append("eslType=").append(esl.getType()).append(";")
					.append("coordinatorMac=").append(esl.getCoordinatorMac()).append(";")
					.append("channel=").append(channel).append(";")
					;
						
			b.append("\n");
			
		}
		
		if (b.length() > 0) {
			return b.toString().split("\n");
		}
		else {
			return null;
		}
	}
	
	public String[] getFirmwareUpgradeInfo()
	{
		int count = 0;
		
		for (String p : networkManager.getAll().keySet())
    	{    		
    		IEslNetwork net =  networkManager.getAll().get(p);
    		
			EslFirmwareUpdater fwUpdater = net.getFirmwareUpdater();
			
			if (fwUpdater == null) 
			{
				continue;
			}
			else if (fwUpdater.getLastOperationTime() == null) 
			{
				continue;
			}
			
			count ++;
	
    	}
		
		if (count == 0) return null;
				
		String l[] = new String [count];
		
		count  = 0;
		
		for (String p : networkManager.getAll().keySet())
    	{    		
    		IEslNetwork net =  networkManager.getAll().get(p);
    		
			EslFirmwareUpdater fwUpdater = net.getFirmwareUpdater();
			
			if (fwUpdater == null) 
			{
				continue;
			}
			else if (fwUpdater.getLastOperationTime() == null) 
			{
				continue;
			}
			else
			{
				
				String d = Tools.toHexByte(fwUpdater.getUpdatingDeviceType());
				d = "0x" + (d.length() == 1 ? "0" : "") + d;
				
				l[count] = new StringBuffer()
						.append("status=")
						.append(fwUpdater.isUpdating() ? "upgrading" : (fwUpdater.isUpgradeOk() ? "success" : "failure"))
						.append(";last optime=")
						.append(timef.format(fwUpdater.getLastOperationTime()).replace(' ', 'T'))
						.append(";device type=")
						.append(d)
						.append(";fw version=")
						.append(fwUpdater.getVersion())
						.append(";transmission number=")
						.append(fwUpdater.getCount())
						.append(";total transmissions=")
						.append(fwUpdater.getTotal())
						.append(";packet number=")
						.append(fwUpdater.getTransmissionCount())
						.append(";total packets=")
						.append(fwUpdater.getTransmissionSize())
						.append(";cnannel=")
						.append(net.getActiveChannel())
						.append(";mac=")
						.append(net.getCoordinatorMac())
						.toString();
				
				count ++;
			}
			
    	}

		return l;

	}
	
	public String[] getFirmwareInfo()
	{
		try 
		{
			Set<Integer> s = FirmwareLibrary.instance().getKnownDeviceTypes();
			if (s == null || s.size() == 0)
			{
				return null;
			}
			else 
			{	
				String [] l = new String [s.size()];
				int fcount = 0;
				for (int d : s)
				{
					String [] fi = FirmwareLibrary.instance().getFirmwareForDeviceType(d).toUnformattedString().split(" ");
					StringBuffer b = new StringBuffer("esltype=").append("0x");
					if (fi[0].length() == 1) b.append("0").append(fi[0]);
					b.append(";version=").append(fi[1]);
					b.append(";manufacturer id=").append(fi[2]);
					b.append(";file=").append(fi[3]);
					l[fcount ++] = b.toString();
				}								
				return l;
			}
		}
		catch (Throwable t) {
			log.error("error getting firmware libraries ... {}", t);
		}
		return null;
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
			
			return String.valueOf(count);
			
		}
		catch(Throwable t) {
			log.error("error retrying price update ... {}", t);
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
	
	public String getSystemInfo() {
		try 
		{
			
			StringBuffer b = new StringBuffer();
			String hostname = "unknown";
			
			try {
				hostname = InetAddress.getLocalHost().getHostName();
			}
			catch (Throwable ignoredxxx){ }
			
			b.append("host name=").append(hostname).append("\n");
			
			OperatingSystemMXBean ox = ManagementFactory.getOperatingSystemMXBean();
			RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
			
			b.append("os name=").append(ox.getName()).append("\n");
			b.append("os arch=").append(ox.getArch()).append("\n");
			b.append("os version=").append(ox.getVersion()).append("\n");
			b.append("jvm vendor=").append(mx.getVmVendor()).append("\n");
			b.append("jvm name=").append(mx.getVmName()).append("\n");
			b.append("jvm version=").append(mx.getVmVersion()).append("\n");
			b.append("application path=").append(new File(".").getAbsolutePath()).append("\n");
			b.append("start time=").append(timef.format(new Date(mx.getStartTime()))).append("\n");
			b.append("uptime=").append(mx.getUptime()).append("\n");
			
			long m = 0;
			
			b.append("processors=").append(ox.getAvailableProcessors()).append("\n");
			b.append("loadavg=").append((ox.getSystemLoadAverage() >= 0 ? ox.getSystemLoadAverage() : "n/a")).append("\n");
			b.append("threads=").append(ManagementFactory.getThreadMXBean().getThreadCount()).append("\n");
					
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
				
				b.append("\n");
				
			}
						
			return b.toString();
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}
	
	public String[] getTasks() {
		try {

			ThreadMXBean tx = ManagementFactory.getThreadMXBean();

			long[] lt = tx.getAllThreadIds();
			long[] dlt = tx.findDeadlockedThreads();
			long[] micro = new long[lt.length];
			long tot = 0;

			if (tx.isThreadCpuTimeSupported() && tx.isThreadCpuTimeEnabled()) {
				for (int i = 0; i < lt.length; i++) {
					micro[i] = tx.getThreadCpuTime(lt[i]); // cpu time is in
															// nanosec
					tot += micro[i];
				}
			}

			String[] l = new String[lt.length];

			for (int i = lt.length - 1; i >= 0; i--) {
				StringBuffer b = new StringBuffer();
				ThreadInfo ti = tx.getThreadInfo(lt[i]);

				b.append("id=").append(lt[i]);
				b.append(";state=").append(ti.getThreadState());

				b.append(";cpu=");
				if (tx.isThreadCpuTimeEnabled()) {
					b.append((int) ((double) micro[i] / (double) tot * 100))
							.append('%');
				} else {
					b.append(" n/a");
				}

				b.append(";deadlocked=");
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

				b.append(";name=");
				b.append(ti.getThreadName());

				b.append(";blockedtime=").append(ti.getBlockedTime());
				b.append(";waitedtime=").append(ti.getWaitedTime());
				b.append(";suspended=").append(ti.isSuspended());
				b.append(";lockowner=").append(ti.getLockOwnerName());

				b.append(";seqnumber=").append(((int) (i + 1)));
				b.append(";id=").append(lt[i]);

				l[i] = b.toString();
			}

			//
			// following code only used to order threads list by CPU usage ...
			//

			Comparator<String> ic = new Comparator<String>() {

				@Override
				public int compare(String s1, String s2) {

					if (s1 == null && s2 == null)
						return 0;
					else if (s1 == null)
						return -1;
					else if (s2 == null)
						return 1;

					int n1 = 0, n2 = 0;
					String s[] = new String[] { s1, s2 };

					for (int i = 0; i < 2; i++) {
						int n = 0;
						String[] pc = s[i].split(";");
						for (int k = 0; k < pc.length; k++) {
							if (pc[k].toLowerCase().startsWith("cpu")) {
								try {
									n = Integer.parseInt(pc[k]
											.substring(pc[k].indexOf('=') + 1)
											.replace('%', ' ').trim());
								} catch (Throwable ehx) {
									n = 0;
								}
							}
						}
						if (i == 0)
							n1 = n;
						else
							n2 = n;
					}

					return (n1 < n2 ? 1 : (n1 == n2 ? 0 : -1));
				}

			};

			Arrays.sort(l, ic);

			//
			// end of sort
			//

			return l;

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
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
	
}

