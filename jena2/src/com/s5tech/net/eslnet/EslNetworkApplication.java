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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.transport.tcp.SslTransport;
import org.castor.core.util.Base64Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.desktop.S5TechDesktopApp;
import com.s5tech.net.entity.EslDataStore;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.esl.MessageInfo;
import com.s5tech.net.server.ActiveMQServerConnectorProxy;
import com.s5tech.net.server.IServerConnector;
import com.s5tech.net.server.IServerProxy;
import com.s5tech.net.server.IServerReceiver;
import com.s5tech.net.server.JMSServerConnectorProxy;
import com.s5tech.net.server.RabbitServerConnectionProxy;
import com.s5tech.net.server.ServerAdaptor;
import com.s5tech.net.server.ServerFacade;
import com.s5tech.net.server.ServerMessageDispatcher;
import com.s5tech.net.server.UDPNetworkDiscovery;
import com.s5tech.net.services.FtpFileServer;
import com.s5tech.net.services.RDateServer;
import com.s5tech.net.services.RemoteControlListener;
import com.s5tech.net.services.UpstreamConsumer;
import com.s5tech.net.services.WebServer;
import com.s5tech.net.services.client.HubAPChangePassword;
import com.s5tech.net.services.client.NetworkApplicationClient;
import com.s5tech.net.services.client.SSHRemoteCommand;
import com.s5tech.net.services.client.ShutdownApplication;
import com.s5tech.net.services.display.GeneratePngDisplay;
import com.s5tech.net.services.display.LCDPayload;
import com.s5tech.net.services.display.Payload;
import com.s5tech.net.services.logging.ApplicationServicesListener;
import com.s5tech.net.services.logging.LogAnalyzer;
import com.s5tech.net.services.simulator.Main;
import com.s5tech.net.services.webapp.AdminHandler;
import com.s5tech.net.services.xml.CsvToXml;
import com.s5tech.net.services.xml.XmlMessageBuilder;
import com.s5tech.net.util.Encoder;
import com.s5tech.net.util.ISystemKeys;

/**
 * Static initializer passing arguments with options.<br>
 * If otions are empty, the Network Application will be launched<br><br> 
 * Available options:<br><br>
 * <pre>
 * encode 
 *         [&lt;S5 input-file output-file&gt;]      convert input to output with symmetric local encoder
 *             | [&lt;hash&gt; &lt;base64-payload&gt;]    create hash code from base64 byte array data
 *             | [&lt;[MD5|...]&gt; &lt;password&gt;]     digest with specified alg. (e.g. MD5) given password
 *                 NOTE: for both 2nd and 3rd mode output is given in hexadecimal upper case digits                
 * 
 * ssh 
 *         -h &lt;10.1.1.100&gt; 
 *         -u &lt;root&gt; 
 *         -w &lt;s5tech123!&gt; 
 *         -c &lt;"ps ax | grep socat | grep -v grep"&gt;  
 * 
 * shutdown 
 *         -p &lt;9000&gt; 
 *         -h &lt;127.0.0.1&gt; 
 *         -c &lt;./conf/s5.conf&gt; 
 *         
 * client 
 *         -u &lt;tcp://127.0.0.1:61616&gt; -q &lt;dynamicQueues/UP&gt; -p &lt;activemq|rabbit&gt; 
 *         -a &lt;publish | receive | count&gt; 
 *         -i &lt;inputfile&gt;  (only publish)
 *         -v &lt;schema.xsd&gt; (publish & receive)
 *         -A (only receive, authorize all, switch on unauthorized events on network app)
 *         -n (only publish, don't send message, combined with -v to validate only xml file)
 *         -t &lt;timeout in ms&gt; (only receive) timeout between messages consumption used to 
 *                 emulate latency backend upstream elaboration
 * 
 * hubpwd 
 *         -h &lt;hostname&gt; if not specified, listen for all broadcast notifications see -p
 *         -p &lt;broadcast port&gt;
 *         -w &lt;root password&gt;
 *         -W &lt;new AP password&gt;
 *     
 * logscan
 *         -G start logviewer graphic user interface
 *         -o &lt;filename&gt; output 
 *         -x &lt;schema.xsd&gt; 
 *         -f &lt;date&gt; YYYY-MM-DDTHH:mm:ss 
 *         -l &lt;directory&gt; ./logs
 *         -a &lt;action&gt; dump (d), validate, unauth, zip, view
 *         -r &lt;filter&gt; format is mac=&lt;eslmac&gt;,coordinator=&lt;mac&gt;... 
 * 
 * csv2xml    
 *         -i &lt;input csv file&gt;    
 *         -o &lt;output xml file&gt;
 *         -r &lt;xml root element&gt; default: data
 *         -s &lt;separator&gt; \t for tab, \b for space 
 * 
 * xmlbuild
 *         -m &lt;mac list&gt; with separator ',' (alternative to -x option)
 *         -x &lt;input xml file&gt; data file (alternative to -x option)
 *         -o &lt;output file&gt;
 *         -t &lt;xslt template&gt; prepend 'file://' to use filesystem xslt file
 *         -p &lt;key=value&gt; can be reiterrated ... (optional parameters)
 * 
 * payload
 *         -a activation time in format yyyy-MM-dd'T'HH:mm:ss
 *                 default is null that means zero (01/01/2000) 
 *                 specifying '-a now' use current time
 *         -e &lt;epaper pngfile&gt;
 *         -p &lt;key=value&gt; multiple parameters option
 *         -f &lt;properties file&gt; for LCD 7SEGMENT ESL (see -s for an example)
 *         -s show an example of properties file and exit    
 *         -d &lt;base64data&gt; decode LCD 7SEGMENT ESL data in base64 format
 *         -o &lt;xml|text&gt; output format when build output ESL message (default is text)
 *         -c &lt;filepath[#startpos]&gt; calculate hash code from binary file from 
 *                 optional start position startpos (default is BOF)
 *         -x &lt;left&gt; position for png file, default and the only running is 0
 *         -t &lt;top&gt; position for png file, default and the only running is 0
 *         -P &lt;number&gt; of pages only for epaper (for LCD 7SEG use -p or -f option)
 *             
 * pngcreate
 *         -w &lt;width&gt; in pixels
 *         -h &lt;height&gt; in pixels
 *         -s &lt;mxn&gt; (e.g.: 200x96) same of above two options
 *         -i &lt;input&gt; text file for png definition (display.txt) see -s for example
 *         -o &lt;output&gt; pngfile
 *         -S show example
 *         
 * simulator
 *         -c or --conf &lt;filename&gt;            configuration .xml or .properties file (default is simulator.properties)
 *         -j or --jdbc &lt;filename&gt;            jdbc properties configuration file (ignored if xml configuration set)
 *         -l or --logger-config &lt;filename&gt;   jul logger configuration
 *         -o or --output-file &lt;filename&gt;     file csv where to save results
 * 
 * noapp
 *         initialize extensions and stop before running ESL network application
 *  
 * </pre>
 * @param args
 */
public class EslNetworkApplication {

	private static Logger log; 
	
	/** the broker client connector */
	private static IServerConnector serverConnector = null;	
	
	private static Properties p = null;	
	
	/** a built-in broker server for data exchange with backEnd, enabled by configuration */   
	private static BrokerService broker = null;
	
	/** main process, invoked after static configurator */
	public void run() {		
		
		try 
		{
			
			String connectorImpl = System.getProperty("server.connector", "activemq");
			
			if (connectorImpl.length() > 0) {
								
				try 
				{
					if (connectorImpl.trim().equalsIgnoreCase("rabbitmq")) {
						log.info("initializing rabbitmq server connector");
						serverConnector = (IServerConnector) new RabbitServerConnectionProxy();
					}
					else if (connectorImpl.trim().equalsIgnoreCase("activemq")) {
						log.info("initializing activemq server connector");
						serverConnector = (IServerConnector) new ActiveMQServerConnectorProxy();
					}
					else if (connectorImpl.trim().equalsIgnoreCase("jms")) {
						log.info("initializing JMS server connector");
						serverConnector = (IServerConnector) new JMSServerConnectorProxy();
					}					
					else {
						log.info("initializing server connector " + connectorImpl);						
						serverConnector = (IServerConnector) Class.forName(connectorImpl).newInstance();
					}
				}
				catch (Throwable t) {
					log.error("cannot initialize server proxy - ", t);
					return;
				}
				
				if (serverConnector instanceof IServerReceiver) {
					((IServerReceiver) serverConnector).setServerMessageReceiver(ServerMessageDispatcher.instance());	
				}

				if (serverConnector.connect()) log.info ("connected to server ...");				
				
			}
			
			ServerFacade.instance().setServerProxy(new IServerProxy() 
			{				
				public boolean sendMessage(MessageInfo<?> msg)
		        {
					if (serverConnector == null || msg == null)
		            {
						log.info("Error: try to send message without broker connection - " 
								+ (msg == null ? "(null)": msg.toString()));
		                return false;
		            }
					else
		            {
		            	try {
		            		if (serverConnector != null) serverConnector.transmit(msg);
		            		return true;
		            	}
		            	catch (Throwable t) 
		            	{
		            		t.printStackTrace();
		            		return false;		            		
		            	}
		            }
		        }

				public boolean isConnected() {					
					return (serverConnector != null ? serverConnector.isConnected() : false );						
				}
				
			});
			
			ServerAdaptor.wrap(ServerFacade.instance(), "server connector");

			ApplicationServicesListener manager = ApplicationServicesListener.instance();
			//if (manager.isTimeoutEnabled()) addObserver (manager);
			
			EslEntityManager.instance(); 
			EslDataStore.instance();

			IEslNetwork eslnet = null;
			int count  = 0;

			synchronized (System.getProperties()) {
				
				for (Object okey : System.getProperties().keySet()) {
					
					String key = (String)okey ;
					//while (it.hasNext()) {
					//	String key = (String) it.next();
					if (key.startsWith("network.")) 
					{
						int reconnectMs = -1;
						String channelList = "";
						String [] v = System.getProperty(key).split("[\\?&]");
						String portAddress = v[0].trim();
						
						for (int i = 1; i < v.length; i ++) {
							if (v[i].startsWith("channels=")) {
								channelList = v[i].substring("channels=".length()).trim();	
							}
							else if (v[i].startsWith("reconnectTimeout=")) {
								try {
									reconnectMs = Integer.parseInt(v[i].substring("reconnectTimeout=".length()).trim());	
									if (reconnectMs > 0) reconnectMs *= 1000;
								}
								catch (Throwable ignored) {}
							}
						}	
						
						try 
						{
							eslnet =  manager.createNetwork(portAddress);
							if (channelList.length() > 0) eslnet.setChannelList(channelList, false);		
							if (reconnectMs > 0) {
								log.info("Setting reconnect timeout {} ms at {}", reconnectMs, portAddress);
								eslnet.setReconnectTimeoutMs(reconnectMs);
							}
							else {
								log.warn("Starting static network at {} without reconnectTimeout parameter set, " +
										"this will create problems after disconnection!", 
										portAddress);
							}
							eslnet.connect();						
							log.info("Static esl network started: " + key
									+ " url = " + portAddress + 
									(channelList.length() == 0 ? ", using current active channel" : ", allowed channels: " + channelList));
							count ++;
						}
						catch (Throwable t) 
						{
							log.error("Error: cannot connect to static esl network " 
									+ key + " - ", t); 
							
						}
					}
				}
			}
			
			if (log.isInfoEnabled())
			{
				if (count == 0) 
				{
					log.info("No static esl networks configured.");
				}
				else 
				{
					log.info("Started " + count + " static esl networks.");
				}
			}

			if (System.getProperty("discovery", "").equalsIgnoreCase("true")) 
			{	
				Thread t = new Thread(
						new UDPNetworkDiscovery((manager.isTimeoutEnabled() ? manager : null)),
						"UDP Network Discovery"
				);
				t.setDaemon(true);
				t.start();				
			}

			//
			// Enter main loop
			//			

			count = 0;
			
			for ( ;; )
			{	
				
				Thread.sleep(5000);
				
				if (manager.isTimeoutEnabled()) manager.checkTimeouts();
				if (serverConnector != null 
						&& !serverConnector.isConnected() 
						&& !serverConnector.connect()) log.warn("Unable to connect server!");
								
				if (count ++ > 24) {
					System.gc();
					count = 0;
				}				
			}
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}		
		
		log.warn("Exit network application!");
				
	}	
	
	
	/**
	 * Static initializer passing arguments with options.<br>
	 * If otions are empty, the Network Application will be launched<br><br> 
	 * Available options:<br><br>
	 * <pre>
	 * encode 
     *         [&lt;S5 input-file output-file&gt;]      convert input to output with symmetric local encoder
     *             | [&lt;hash&gt; &lt;base64-payload&gt;]    create hash code from base64 byte array data
     *             | [&lt;[MD5|...]&gt; &lt;password&gt;]     digest with specified alg. (e.g. MD5) given password
     *                 NOTE: for both 2nd and 3rd mode output is given in hexadecimal upper case digits                
     * 
     * ssh 
     *         -h &lt;10.1.1.100&gt; 
     *         -u &lt;root&gt; 
     *         -w &lt;s5tech123!&gt; 
     *         -c &lt;"ps ax | grep socat | grep -v grep"&gt;  
     * 
     * shutdown 
     *         -p &lt;9000&gt; 
     *         -h &lt;127.0.0.1&gt; 
     *         -c &lt;./conf/s5.conf&gt; 
     *         
     * client 
     *         -u &lt;tcp://127.0.0.1:61616&gt; -q &lt;dynamicQueues/UP&gt; -p &lt;activemq|rabbit&gt; 
     *         -a &lt;publish | receive | count&gt; 
     *         -i &lt;inputfile&gt;  (only publish)
     *         -v &lt;schema.xsd&gt; (publish & receive)
     *         -A (only receive, authorize all, switch on unauthorized events on network app)
     *         -n (only publish, don't send message, combined with -v to validate only xml file)
     *         -t &lt;timeout in ms&gt; (only receive) timeout between messages consumption used to 
     *                 emulate latency backend upstream elaboration
     * 
     * hubpwd 
     *         -h &lt;hostname&gt; if not specified, listen for all broadcast notifications see -p
     *         -p &lt;broadcast port&gt;
     *         -w &lt;root password&gt;
     *         -W &lt;new AP password&gt;
     *     
     * logscan
     *         -G start logviewer graphic user interface
     *         -o &lt;filename&gt; output 
     *         -x &lt;schema.xsd&gt; 
     *         -f &lt;date&gt; YYYY-MM-DDTHH:mm:ss 
     *         -l &lt;directory&gt; ./logs
     *         -a &lt;action&gt; dump (d), validate, unauth, zip, view
     *         -r &lt;filter&gt; format is mac=&lt;eslmac&gt;,coordinator=&lt;mac&gt;... 
     * 
     * csv2xml    
     *         -i &lt;input csv file&gt;    
     *         -o &lt;output xml file&gt;
     *         -r &lt;xml root element&gt; default: data
     *         -s &lt;separator&gt; \t for tab, \b for space 
     * 
     * xmlbuild
     *         -m &lt;mac list&gt; with separator ',' (alternative to -x option)
     *         -x &lt;input xml file&gt; data file (alternative to -x option)
     *         -o &lt;output file&gt;
     *         -t &lt;xslt template&gt; prepend 'file://' to use filesystem xslt file
     *         -p &lt;key=value&gt; can be reiterrated ... (optional parameters)
     * 
     * payload
     *         -a activation time in format yyyy-MM-dd'T'HH:mm:ss
     *                 default is null that means zero (01/01/2000) 
     *                 specifying '-a now' use current time
     *         -e &lt;epaper pngfile&gt;
     *         -p &lt;key=value&gt; multiple parameters option
     *         -f &lt;properties file&gt; for LCD 7SEGMENT ESL (see -s for an example)
     *         -s show an example of properties file and exit    
     *         -d &lt;base64data&gt; decode LCD 7SEGMENT ESL data in base64 format
     *         -o &lt;xml|text&gt; output format when build output ESL message (default is text)
     *         -c &lt;filepath[#startpos]&gt; calculate hash code from binary file from 
     *                 optional start position startpos (default is BOF)
     *         -x &lt;left&gt; position for png file, default and the only running is 0
     *         -t &lt;top&gt; position for png file, default and the only running is 0
     *         -P &lt;number&gt; of pages only for epaper (for LCD 7SEG use -p or -f option)
     *             
     * pngcreate
     *         -w &lt;width&gt; in pixels
     *         -h &lt;height&gt; in pixels
     *         -s &lt;mxn&gt; (e.g.: 200x96) same of above two options
     *         -i &lt;input&gt; text file for png definition (display.txt) see -s for example
     *         -o &lt;output&gt; pngfile
     *         -S show example
     *         
     * simulator
     *         -c or --conf &lt;filename&gt;            configuration .xml or .properties file (default is simulator.properties)
     *         -j or --jdbc &lt;filename&gt;            jdbc properties configuration file (ignored if xml configuration set)
     *         -l or --logger-config &lt;filename&gt;   jul logger configuration
     *         -o or --output-file &lt;filename&gt;     file csv where to save results
     * 
     * noapp
     *         initialize extensions and stop before running ESL network application
     *  
	 * </pre>
	 * @param args
	 */
	@SuppressWarnings(value = { "all" })
	public static void init(String[] args) {
		
		Boolean noNetworkApp = new Boolean(false);
		boolean deleteOnExit = false;
		
		try {
			
			if (args != null && args.length > 0) {
				if (args[0].toLowerCase().startsWith("conf")) {
					System.setProperty("com.s5tech.net.config", args[1]);
					String [] nargs = (args.length <= 2 ? null : new String[args.length - 2]);
					if (nargs != null) {
						for (int _i = 0; _i < nargs.length; _i++) {
							nargs[_i] = args[_i + 2];
						}
					}				
					args = nargs;
				}
			}
			
			Encoder e = new Encoder();
				
			if (args != null && args.length > 0) {
			
				boolean exit = true;
				
				try {
			
					if (args[0].equalsIgnoreCase("convert"))
					{
						if (args[1].equalsIgnoreCase("s5")) {					
							InputStream i = null;
							OutputStream o = null;							
							try {
								File f1, f2;
								f1 = new File(args[2]);
								if (!f1.exists()) throw new IOException("file " + f1.getAbsolutePath() + " not found!");
								f2 = new File(args[3]);
								i = new FileInputStream(f1);
								o = new FileOutputStream(f2);					
								e.run (i, o, true);				
							}
							catch (Throwable t) 
							{ 
								t.printStackTrace(); 
								System.exit(1);
							}
							finally 
							{
								if (i != null) try { i.close(); } catch (Throwable ignored) {}
								if (o != null) try { o.close(); } catch (Throwable ignored) {}
							}							
						}
						else if (args[1].equalsIgnoreCase("hash")) {					
							try {
								LCDPayload lcd = new LCDPayload();
								byte[] hash = lcd.calculateEslHash(Base64Decoder.decode(args[2]));
								System.err.print("0x");
								for (byte b : hash) {			
									String x = Integer.toHexString((b & 0xff));
									if (x.length() == 1) x = "0" + x; 
									System.err.print(x.toUpperCase());
								}
								System.err.println();
							}
							catch (Throwable t) 
							{ 
								t.printStackTrace(); 
								System.exit(1);
							}														
						}
						else 
						{
							String provider = args[1];							
							byte[] a = java.security.MessageDigest.getInstance(args[1]).digest(args[2].getBytes());
							for (byte b : a) {			
								String x = Integer.toHexString((b & 0xff));
								if (x.length() == 1) x = "0" + x; 
								System.err.print(x.toUpperCase());
							}
							System.err.println();	
						}						
					}
					else if (args[0].equalsIgnoreCase("ssh"))
					{
						SSHRemoteCommand.exec(args);
					}
					else if (args[0].equalsIgnoreCase("shutdown"))
					{
						ShutdownApplication.exec(args);
					}
					else if (args[0].equalsIgnoreCase("client"))
					{
						NetworkApplicationClient.exec(args);
					}
					else if (args[0].equalsIgnoreCase("hubpwd"))
					{
						HubAPChangePassword.exec(args);
					}
					else if (args[0].equalsIgnoreCase("logscan"))
					{
						LogAnalyzer.exec(args);
					}
					else if (args[0].equalsIgnoreCase("csv2xml"))
					{
						CsvToXml.exec(args);
					}
					else if (args[0].equalsIgnoreCase("xmlbuild"))
					{
						XmlMessageBuilder.exec(args);
					}
					else if (args[0].equalsIgnoreCase("payload"))
					{
						Payload.exec(args);
					}
					else if (args[0].equalsIgnoreCase("pngcreate"))
					{
						GeneratePngDisplay.exec(args);
					}
					else if (args[0].equalsIgnoreCase("noapp"))
					{
						exit = false;
						noNetworkApp = true;						
					}
					else if (args[0].toLowerCase().startsWith("sim"))
					{
						Main.exec(args, 1);						
					}
					else 
					{
						exit = false;
						System.err.println("WARNING: WRONG OPTION '" + args[0] + "' ...!");
						System.err.println("See README.txt for command line instructions.");
					}

				}
				catch(Throwable t) {
				
					t.printStackTrace();
					System.err.println("See README.txt for command line instructions.");
					System.exit(1);
					
				}				
				
				if (exit) System.exit(-1);
				
			}
			
			//
			// inizializzo opzioni valorizzate di default che posso disattivare da system.properties
			// 
			
			System.setProperty ("enableleave", "true");
			System.setProperty ("activemq.server", "true"); 
			System.setProperty ("timeoutCoordinator", "35");
			System.setProperty ("timeoutHub", "65");
			System.setProperty ("skipEslFirmwareCheck", "true");
			
			//
			// inizializzo configurazione
			// 
						
			InputStream is = null;
			p = new Properties();
			
			File f = null;
			
			File fconf = new File(System.getProperty("com.s5tech.net.config", "./conf/s5.conf"));
			
			String includebasePath = fconf.getParent();
			
			if (includebasePath.contains("/")) includebasePath += "/";
			else includebasePath += "\\";
						
			//due to fucking obfuscator problem....
			BufferedReader inbak = null;
									
			if (fconf.exists()) 
			{
				int pluginId = 0;
				int networkId = 0;
				
				BufferedReader in = null;
				
				if (e.isEncoded(fconf.getAbsolutePath())) {					
					System.err.println("loading settings from encoded conf file " + fconf.getAbsolutePath());
					FileInputStream _in = new FileInputStream(fconf);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					e.run(_in, out, false);
					in = new BufferedReader (new InputStreamReader (new ByteArrayInputStream(out.toByteArray())));
					out.close();
					in.close();					
				}
				else {
					System.err.println("loading settings from plainf conf file " + fconf.getAbsolutePath());
					in = new BufferedReader(new FileReader(fconf));
				}
				
				f = File.createTempFile("s5conf", ".properties");
				f.deleteOnExit();
				
				PrintStream o = new PrintStream(f);
				
				for ( ;; ) {
					
					String l = in.readLine();
					
					if (l == null) {
						if (inbak == null) break;
						else {
							try { in.close(); } catch (Throwable ignored) {};
							in = inbak;
							inbak = null;
							l = in.readLine();
						}
						if (l == null) break;
					}
					
					l = l.replace('\t', ' ');
					l = l.trim();
					if (l.length() == 0) continue;
					if (l.startsWith("#")) continue;
					
					if (l.startsWith("include ")) {
						inbak = in;
						String path = "";
						try {
							path = l.substring("include ".length()).trim();
							if (!path.contains(":") && !path.startsWith("/"))
							{
								path = includebasePath + path;
							}
							System.out.println("including file " + path + " ...");
							in = new BufferedReader(new FileReader(path));
						}
						catch (Throwable fopen_ex) {
							System.err.println("cannot open included file " + path + " - " + fopen_ex.getMessage());
							in = inbak;
							inbak = null;
						} 
					}
					else if (l.startsWith("set ")) {
						o.print(l.substring(4));
						if (l.indexOf('=') < 0) {
							o.print(" = true");
						}
						o.println();
					}
					else if (l.startsWith("unset ")) {
						o.println(l.substring(6) + " = false");
					}
					else if (l.startsWith("network ")) {
						networkId ++;
						o.println("network." + networkId + " = " + l.substring(8));
					}
					else if (l.startsWith("plugin ")) {
						pluginId ++;
						o.println("plugin." + pluginId + " = " + l.substring(7));
					}
					else if (l.startsWith("reservation ")) {
						String [] x = l.split(" ");
						String k = "reservation";
						String v = null; 
						for (int i = 1; i < x.length; i ++) {
							if (i < x.length - 1 && (
									x[i].equalsIgnoreCase("coordinator")
									|| x[i].equalsIgnoreCase("mac"))) {
								k += ".mac." + x[++i];
							}
							else if (i < x.length - 1 && x[i].equalsIgnoreCase("ip")) {
								k += ".ip." + x[++i];
							}
							else if (i < x.length - 1 && (
									x[i].equalsIgnoreCase("hubmac")
									|| x[i].equalsIgnoreCase("hub"))) {
								k += ".hub." + x[++i];
							}
							else if (i < x.length - 1 && x[i].equalsIgnoreCase("channels")) {
								v = x[++i];
							}
						}
						if (v != null) {
							o.println(k + " = " + v);
						}
					}
					else {
						System.err.println(
								"invalid command line in configuration file "
								+ fconf.getAbsolutePath() + " > " + l);
					}
				}
				o.close();
				in.close();
			}
			else
			{
				f = new File("./conf/system.properties");
			}
			
			if (f.exists()) {
				
				if (e.isEncoded(f.getAbsolutePath())) {
					
					System.err.println("loading settings from encoded file " + f.getAbsolutePath());
					
					FileInputStream in = new FileInputStream(f);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					e.run(in, out, false);
					is = new ByteArrayInputStream(out.toByteArray());
					out.close();
					in.close();
					
				}
				else {
					
					System.out.println("loading settings from plain file " + f.getAbsolutePath());
					is = new FileInputStream(f);
					
				}
				
			}
			else {
				is = S5TechDesktopApp.class.getResourceAsStream("/system.properties");
			}
			
			if (is != null) {
				p.load(is);
				for (Object i : p.keySet()) {
					String key = (String) i;
					if (System.getProperty (key) == null 
							&& p.getProperty(key).trim().length() > 0 
									&& !p.getProperty(key).trim().toLowerCase().equals("false")
									&& !p.getProperty(key).trim().toLowerCase().startsWith("disable")
									&& !p.getProperty(key).trim().toLowerCase().equals("no")) {
						System.setProperty (key, p.getProperty(key));
					}					
					else {
						if (p.getProperty(key).trim().length() > 0 
								&& !p.getProperty(key).trim().toLowerCase().equals("false")
								&& !p.getProperty(key).trim().toLowerCase().startsWith("disable")
								&& !p.getProperty(key).trim().toLowerCase().equals("no")) { 
							System.out.println ("ignore config setting previously defined with java -D option: " +
									key + " = " + System.getProperty(key));
						}
						else {
							//disabled by system.properties
							System.clearProperty(key);
						}
					}					
				}
				
				try {
					if (deleteOnExit) {
						fconf.delete();			
						f.delete();
					}
					else if (fconf.exists()) {						
						f.delete();	
					}					
				}
				catch (Throwable xxx) {}
								
			}
			else {
				System.err.println ("WARNING: no config file found! (default is system.properties)");
			}
						
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		
		//
		// initialize default options if not defined in system.properties 
		//
		
		if (System.getProperty("sock.keep.alive") == null) 
			System.setProperty ("sock.keep.alive", "true");
		
		if (System.getProperty("status.request.maxbuf") == null) 
			System.setProperty("status.request.maxbuf", "7");
		
		if (System.getProperty("log.dir") == null) 
			System.setProperty("log.dir", "logs");
		
		if (System.getProperty("log.archivedir") == null) 
			System.setProperty("log.archivedir", "logs/backup");
		
		if (System.getProperty("logback.configurationFile") == null) 
			System.setProperty("logback.configurationFile", "file:conf/logback-s5tech.xml");

		if (System.getProperty("log.rollingsize") == null) 
			System.setProperty("log.rollingsize", "13");
		
		if (System.getProperty("log.filesize") == null) 
			System.setProperty("log.filesize", "10MB");
		
		if (System.getProperty("timesetter.interval") == null) 
			System.setProperty("timesetter.interval", "7200");

		if (System.getProperty("firmware.transmission") == null) 
			System.setProperty("firmware.transmission", "3");
		
		if (System.getProperty("maxPriceUpdateRetry") == null) 
			System.setProperty("maxPriceUpdateRetry", "3");
		
		if (System.getProperty("server.connector") == null) 
			System.setProperty("server.connector", "activemq");
		
		if (System.getProperty("server.url") == null) 
			System.setProperty("server.url", "vm://S5NET");
		
		if (System.getProperty("server.upqueue") == null) 
			System.setProperty("server.upqueue", "dynamicQueues/UP");
		
		if (System.getProperty("server.downqueue") == null) 
			System.setProperty("server.downqueue", "dynamicQueues/DOWN");
		
		if (System.getProperty("activemq.connector") == null) 
			System.setProperty("activemq.connector", "tcp://localhost:61616");
		
		if (System.getProperty("javax.net.ssl.keyStore") == null) {
			System.setProperty ("javax.net.ssl.keyStore", "./security/keystore");
			System.setProperty ("javax.net.ssl.keyStorePassword", "changeit");
			System.setProperty ("javax.net.ssl.keyStoreType", "jks");				
			System.setProperty ("javax.net.ssl.trustStore", "./security/keystore");
			System.setProperty ("javax.net.ssl.trustStorePassword", "changeit");
			System.setProperty ("javax.net.ssl.trustStoreType", "jks");
		}		
		
		if (!noNetworkApp) {
			noNetworkApp = System.getProperty(
					"noNetworkApp", "").equalsIgnoreCase("true");			
		}

		//
		// Initialize Logging framework
		//
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);		
		
		//
		// Initialize ActiveMQ broker if required
		//		
		if (System.getProperty("activemq.server") != null) 
		{					
			try 
			{	
				
				log.info("starting broker service with active connector on {} ...", 
						System.getProperty("activemq.connector", "tcp://localhost:61616"));
				
				broker = new BrokerService();
				
				if (System.getProperty("activemq.persistent") != null) 
				{
					broker.setPersistent(true);
					String dataDirectory = System.getProperty("activemq.persistence.dir", "./run/data/activemq");
					broker.setDataDirectory (dataDirectory);
					log.info("using activemq persistence, data directory: {}", dataDirectory);
				}
				else 
				{
					broker.setPersistent(false);					
				}
				
				broker.setUseJmx(System.getProperty("activemq.usejmx") != null);									
				broker.setBrokerName("S5NET");
				broker.addConnector(System.getProperty("activemq.connector", "tcp://localhost:61616"));
				
				broker.setUseShutdownHook(true);
				broker.start();
				
				while (!broker.isStarted()) { Thread.sleep(100); }
				
				log.info("broker service running.");				
									
			}
			catch (Throwable t) 
			{
				log.error("error starting broker: " + t.getMessage());
				return;
			}		
		}
		
		//
		// Shutdown Hook, ut here shutdown instances
		//
		if (!noNetworkApp) {
			Runtime.getRuntime().addShutdownHook(
					new Thread(new Runnable() {			
						@Override
						public void run() {	
							log.info("shutting down application ...");
							ApplicationServicesListener.instance().shutDown();
							EslDataStore.instance().shutdown();
							EslEntityManager.instance().shutdown();								
						}
					}, "Shutdown-Hook")
				);
		}
			
		if (log.isInfoEnabled())
		{
			log.info("*****************************************************************************");
			log.info("*");
			if (S5TechDesktopApp.class.getPackage().getImplementationTitle() != null) 
			{				
				log.info("* Starting {} Application - Version {} ...", 
						S5TechDesktopApp.class.getPackage().getImplementationTitle(),
						S5TechDesktopApp.class.getPackage().getImplementationVersion());
			}
			else 
			{
				log.info("* Starting Unversioned ESL Network Application ..."); 
			}
			log.info("*");
			log.info("*****************************************************************************");
		}
		
		//
		// Initialize and start plugins
		//
		try 
		{
			Properties plugins = new Properties();			
			for (Object okey : System.getProperties().keySet()) {											
				if (okey.toString().startsWith("plugin."))
					plugins.put(okey, System.getProperty(okey.toString()));				
			}
			
			for (Object okey : plugins.keySet()) {			
				
				String value = System.getProperty(okey.toString()).trim();
				
				try 
				{	
					int pcount = 0;
					
					if (value.equalsIgnoreCase("com.s5tech.net.plugin.WebServer")) {
						
						String wroot = System.getProperty("webapp.root", "./webapp");
						if (!wroot.endsWith("/")) wroot += "/";
												
						WebServer jetty = new WebServer();
						
						String adminPath = System.getProperty("webapp.admin.path", "/admin");							
						String adminRoot = System.getProperty("webapp.admin.documentRoot", wroot + "admin/");
						if (!adminRoot.endsWith("/")) adminRoot += "/";
						String adminIcon = System.getProperty("webapp.admin.favicon", "images/s5.ico");
						if (new File(adminRoot).exists()) {
							jetty.addContextHandler(
									new AdminHandler(
											adminRoot, 
											adminPath, 
											adminIcon) , 
									adminPath);
						}
						
						StringBuffer wargs = new StringBuffer(System.getProperty("webserver.commandLine", "--http 9090"));
						
						if (new File(wroot).exists()) {								
							for (String f : new File(wroot).list()) {
								if (f.toLowerCase().endsWith(".war")) {
									String fname = f.substring(0, f.length() - 4); 
									wargs.append(" --deploy ").append("/").append(fname).append(":").append(wroot + f);
								}
								else {
									String fname = f;
									if (adminRoot.indexOf("/" + fname) >= 0) continue;
									fname = wroot + fname;
									File dir = new File(fname);
									if (dir.exists() && dir.isDirectory()) {
										wargs.append(" --deploy ").append("/").append(f).append(":").append(fname);
									}
								}
							}								
						}						
						jetty.parseArgs(wargs.toString().split(" "));						
						jetty.start();	
						
					}
					else if (value.equalsIgnoreCase("com.s5tech.net.plugin.FtpServer")) {						
						FtpFileServer mina = new FtpFileServer();						
						mina.start();						
					}					
					else if (value.equalsIgnoreCase("com.s5tech.net.plugin.RDateServer")) {
						Thread t = new Thread(new RDateServer(), "Time Server Extension");
						t.setDaemon(true);
						t.start();							
					}
					else if (value.equalsIgnoreCase("com.s5tech.net.plugin.ShutdownSocket")
							|| value.equalsIgnoreCase("com.s5tech.net.plugin.RemoteServices")) {
						Thread t = new Thread(new RemoteControlListener(), "Remote Control Listener");
						t.setDaemon(true);
						t.start();							
					}
					else if (value.equalsIgnoreCase("com.s5tech.net.plugin.UpstreamConsumer")) {
						Thread t = new Thread(new UpstreamConsumer(), "Upstream Consumer");
						t.setDaemon(true);
						t.start();							
					}
					else {
						pcount ++;
						Object o = Class.forName(value).newInstance();
						if (o instanceof Runnable) {
							Runnable plugin = Runnable.class.cast(o);
							Thread t = new Thread(plugin, "plugin-" + pcount + ":" + value);
							t.setDaemon(true);
							t.start();
						}
						else {			
							Method start = null;
							try {
								start = o.getClass().getMethod("start");
							} catch (Throwable ignored) {}
							if (start != null) start.invoke(o);
						}
						log.info("started plugin " + o.getClass().getSimpleName());
					}
					
				}
				catch (Throwable t) 
				{
					log.error("Failure initializing plugin {} - reason: {}", value, t);
				}		
			}
		}
		catch (Throwable inipluginEx) {
			
			log.error("failure in init plugin section - {}", inipluginEx);
			
		}
		
		if (noNetworkApp) {
			try {
				for ( ;;) {
					Thread.sleep(5000);
				}
			}
			catch(Throwable ignored) {}
			return;
		}
			
		//
		// Run appplication
		//
		new EslNetworkApplication().run();
		
	}
	
	
}
