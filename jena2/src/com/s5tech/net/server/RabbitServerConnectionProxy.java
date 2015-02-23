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
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Properties;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConnectionParameters;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.s5tech.net.esl.MessageInfo;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;
import com.s5tech.net.util.Tools.DecodeResult;
import com.s5tech.net.xml.Message;

/**
 * This class proxies server communication by wrapping the MQ and server message apis
 * @author S5Tech Development Team
 * Created: Apr 7, 2010
 *
 */
public class RabbitServerConnectionProxy implements Consumer, ReturnListener, IServerConnector, 
	IServerReceiver {
	
	public static final String DEFAULT_SERVER_PORT="default";
	public static final long DEFAULT_SERVER_RECONNECT_INTERVAL=10000;

	//private static final String PROPERTIES_KEY = "server";
	private static final int MAX_BODY_SIZE = (int) Math.pow(2, 21); // 2MB
	// TODO Qualify this value - 100 is just an arbitrary value based on that the upstream messages are quite small
	private static final int MAX_UPQUEUE_SIZE = 500;

	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	private static final Logger logXmlTransmits = LoggerFactory.getLogger(ISystemKeys.XMLSERVER + ".TO");
	//private static final Logger logXmlTransmitPerformance = LoggerFactory.getLogger(ILoggers.XMLSERVER + ".PERF.TO");
	private static final Logger logXmlReception = LoggerFactory.getLogger(ISystemKeys.XMLSERVER + ".FROM");
	//private static final Logger logXmlReceivePerformance = LoggerFactory.getLogger(ILoggers.XMLSERVER + ".PERF.FROM");;
	
	
	
	private Connection conn;
	private Channel channel;
	private Timer reconnectTimer;
	private boolean disconnecting;

	private String serverHost;
	private int serverPort;
	private String username;
	private String password;
	private String exchangeName;
	private boolean useSsl;
	private File certFile;
	private String certPassword;
	private File keyStore;
	private String keyStorePassword;
	private String virtualHost;
	private String forcedVirtualHost;
	private String downQueueName;
	private String downQueueRoutingKey;
	private String upQueueName;
	private String upQueueRoutingKey;
	private Queue<Message> messageSubscriberQueue;
	private Queue<MessageInfo<?>> upQueue;
	private IServerMessageReceiver serverMessageReceiver;
	private long reconnectIntervalMillis;

	public RabbitServerConnectionProxy() {
		this(null);
	}

	public RabbitServerConnectionProxy(IServerMessageReceiver serverMessageReceiver) {
				
		this.serverMessageReceiver = serverMessageReceiver;

		upQueue = new ActiveQueue<MessageInfo<?>>(new IActiveQueueSubscriber<MessageInfo<?>>() {

			public void elementFromQueue(MessageInfo<?> element) {
				// Kinda busy wait - sorry, but this needs to be implemented quickly
				while(!isConnected()) {
					Tools.doWait("", 5000);
				}

				transmitDirect(element);

				// Queue trimming
				int count = 0;
				while(upQueue.size() > MAX_UPQUEUE_SIZE) {
					upQueue.poll();
					count++;
				}
				if(count > 0)
					log.warn("Queue trimmed; removed " + count + " messages");
			}
			
		}, "Server Up Queue", true, Thread.MAX_PRIORITY);

		messageSubscriberQueue = new ActiveQueue<Message>(new IActiveQueueSubscriber<Message>() {

			public void elementFromQueue(Message message) {
				deliverMessage(message);
			}
		}, "Server Down Queue");
	}

	private void deliverMessage(Message m) {
		if(serverMessageReceiver != null)
			serverMessageReceiver.onMessage(m);
	}

	public void setForcedVirtualHost(String vHost) {
		
	}
	
	/**
	 * Validate the server related settings from {@link HubSettings}.
	 * Note that this is just a sanity check; it is not possible to see if the values are in fact correct.
	 * @return true if the settings looks ok, false otherwise
	 */
	public boolean getAndCheckSettings() {
	
		Properties p = new Properties();
		try {
			p.load(getClass().getResourceAsStream("/rabbitmq.properties"));
		}
		catch(Throwable t) {
			log.error("rabbitmq.properties not found in classpath");
			return false;
		}
				
		serverHost = p.getProperty(SERVER_HOST);
		String portStr = p.getProperty(SERVER_PORT);
		if(portStr != null) {
			DecodeResult<Integer> portRes = Tools.tryParseInt(portStr, 10);
			serverPort = portRes.success ? portRes.value : -1; // Treat all NaN's as default port
		} else {
			serverPort = -1;
		}
		username = p.getProperty(SERVER_USERNAME);
		password = p.getProperty(SERVER_PASSWORD);
		exchangeName = p.getProperty(SERVER_EXCHANGE_NAME, "amq.direct");
		downQueueName = p.getProperty(SERVER_DOWNQUEUE_NAME);
		downQueueRoutingKey = p.getProperty(SERVER_DOWNQUEUE_ROUTING_KEY);
		upQueueName = p.getProperty(SERVER_UPQUEUE_NAME);
		upQueueRoutingKey = p.getProperty(SERVER_UPQUEUE_ROUTING_KEY);
		DecodeResult<Integer> ms = Tools.tryParseInt(p.getProperty(SERVER_RECONNECT_SECS, "0"), 10);
		reconnectIntervalMillis = ms.success ? ms.value*1000 : 0;
		virtualHost = p.getProperty(SERVER_VIRTUAL_HOST, "/");
		
		useSsl = Boolean.valueOf(p.getProperty(SERVER_USE_SSL, "false"));
		certFile = new File(Tools.getBasedir() + p.getProperty(CLIENT_CERTFILE, "keycert.p12"));
		certPassword = p.getProperty(CLIENT_CERT_PASSWORD, "");
		keyStore = new File(Tools.getBasedir() + p.getProperty(CLIENT_KEYSTORE_FILE, "./security/keystore"));
		keyStorePassword = p.getProperty(CLIENT_KEYSTORE_PASSWORD, "changeit");

		boolean ok = true;
		
		if(useSsl && (!certFile.exists() || Tools.isNullOrEmpty(certPassword) || !keyStore.exists() || Tools.isNullOrEmpty(keyStorePassword))) {
			ok = false;
			log.error("UseSSL=true but certificate password or fileName/file is missing");
		} else {
			ok = Tools.allNotNull(serverHost, username, password, exchangeName, downQueueName, downQueueRoutingKey, upQueueName, upQueueRoutingKey);
			if(!ok) log.error("One or more properties not set correctly!");
		}
		
		return ok;
	}

	/**
	 * @param serverMessageReceiver the serverMessageReceiver to set
	 */
	public void setServerMessageReceiver(
			IServerMessageReceiver serverMessageReceiver) {
		this.serverMessageReceiver = serverMessageReceiver;
	}

	/**
	 * Check if the connection to the server is up
	 * @return
	 */
	public synchronized boolean isConnected() {
		return conn != null && channel != null;
	}

	public boolean connect() {
		return connect(false);
	}

	public synchronized boolean connect(boolean enableReconnect) {

		disconnecting = false;
		
		//if(!PlatformFacade.instance().isEth0Connected() || !getAndCheckSettings()) {
		//	if(enableReconnect) startReconnect();
		//	return false;
		//}
		
		if(!getAndCheckSettings()) {			
			return false;
		}
		
		ConnectionParameters parms = new ConnectionParameters();
		parms.setPassword(password);
		parms.setUsername(username);
		if(forcedVirtualHost != null)
			parms.setVirtualHost(forcedVirtualHost);
		else
			parms.setVirtualHost(virtualHost);
		
		if(reconnectIntervalMillis > 1000)
			parms.setRequestedHeartbeat((int)(reconnectIntervalMillis/1000));
		
		ConnectionFactory factory = new ConnectionFactory(parms);

		if(useSsl) {
			try {
				useSsl = false;
				
				if(certFile.exists()) {
					if(certPassword == null) certPassword = "";
					if(keyStorePassword == null) keyStorePassword = "";
					
					char[] keyPassphrase = certPassword.toCharArray();
					KeyStore ks = KeyStore.getInstance("PKCS12");
					ks.load(new FileInputStream(certFile), keyPassphrase.length == 0 ? null : keyPassphrase);
					
					KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
					kmf.init(ks, keyPassphrase);

					char[] trustPassphrase = keyStorePassword.toCharArray();
					KeyStore tks = KeyStore.getInstance("JKS");
					tks.load(new FileInputStream(keyStore), trustPassphrase);

					TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
					tmf.init(tks);

					SSLContext c = SSLContext.getInstance("SSLv3");

					c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
					factory.useSslProtocol(c);
				} else {
					factory.useSslProtocol();
				}
				useSsl = true;
				log.info("Connecting using SSL");
//			} catch (KeyManagementException e) {
//				//TODO handle key mgmt exceptions
//				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//				//TODO handle no such algorithm exceptions
//			} catch (KeyStoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (UnrecoverableKeyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (CertificateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (Throwable e) {
				log.error("A(n) " + e.getClass().getSimpleName() + " error occured while attemting to connect to the server. Message: " + e.getMessage());
				log.debug("",e);
			}
		} else {
			log.info("Connecting without SSL");
		}
				
		try {
			if(serverPort > -1)
				conn = factory.newConnection(serverHost, serverPort);
			else
				conn = factory.newConnection(serverHost);

			channel = conn.createChannel();
			//channel.exchangeDeclare(exchangeName, "direct");
			channel.queueDeclare(downQueueName);
			channel.queueBind(downQueueName, "amq.direct", downQueueRoutingKey);

			channel.queueDeclare(upQueueName);
			channel.queueBind(upQueueName, "amq.direct", upQueueRoutingKey);
			
			channel.setReturnListener(this);

			channel.basicConsume(downQueueName, this);

//			int messageCount = downDeclareOk.getMessageCount();
//			if(messageCount > 0) {
//				log.info("Getting " + messageCount + " message " + (messageCount == 1 ? "" : "s") + " from the server.");
//			}
//			while(messageCount > 0) {
//				channel.basicRecoverAsync(false);
//				GetResponse response = channel.basicGet(downQueueName, true);
//				messageCount = response.getMessageCount();
//				handleDelivery("",
//						response.getEnvelope(), 
//						response.getProps(), 
//						response.getBody());
//			}
			
		} catch (Throwable e) {
			log.error("A(n) " + e.getClass().getSimpleName() + " error occured while attempting to connect to the server. Message: " + e.getMessage());
			log.debug("",e);
			conn = null;
		}

		if(conn == null) {
			if(enableReconnect) startReconnect();
		} else {
			log.info("Connected to server: " + conn.getHost() + ":" + conn.getPort());
		}
		
		return conn != null;
	}

	public synchronized void disconnect() {
		disconnecting = true;
		if(reconnectTimer != null)
			reconnectTimer.cancel();
		log.info("Disconnecting");
		if(channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException e) {
				log.debug("An I/O error occured while closing the channel: {}", e.getMessage());
				log.trace("",e);
			}
		}
		if(conn != null && conn.isOpen()) {
			try {
				conn.close();
			} catch (IOException e) {
				log.debug("An I/O error occured whil closing the channel: {}", e.getMessage());
				log.trace("",e);
			}
		}
		channel = null;
		conn = null;
	}
	
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub
		log.debug("Cancel OK called with tag: {}", consumerTag);
	}

	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub
		log.debug("Consume OK called with tag: {}", consumerTag);
	}

	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties props, byte[] body) throws IOException {

		// TODO Is it possible to only do this when messages are delivered to the other hubs?
		channel.basicAck(envelope.getDeliveryTag(), false);

		if (log.isTraceEnabled()) log.trace("Received message with a body of {} bytes", body.length);

		// This is implemented to avoid memory issues
		if(body.length > MAX_BODY_SIZE) {
			log.error("Message larger than max size: " + body.length + "MAX: " + MAX_BODY_SIZE);
			return;
		}
		
		// Checking for UTF8 BOM and, if present, 'manually' remove it.
		int offset = body.length > 3 
					&& body[0] == (byte)0xEF 
					&& body[1] == (byte)0xBB 
					&& body[2] == (byte)0xBF ? 3 : 0;

		if(logXmlReception.isTraceEnabled())
			logXmlReception.trace(new String(body, offset, body.length - offset));
		
		//long start = logXmlReceivePerformance.isTraceEnabled() ? System.currentTimeMillis() : 0;
		
		Object obj = MessageUtils.fromXml(body, offset, body.length - offset);
		
		//if(start > 0)
		//	logXmlReceivePerformance.trace("XML parsed in {} ms", (System.currentTimeMillis()-start));
		
		if(obj != null && obj instanceof Message) {
			messageSubscriberQueue.add(Message.class.cast(obj));
		}
	}

	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {
		log.debug("Shutdown signal called with tag: {}", consumerTag);
		log.trace("",sig);
		if(!disconnecting && reconnectIntervalMillis > 0)
			startReconnect();
	}

	public void handleBasicReturn(int replyCode, String replyText, String exchange,
			String routingKey, BasicProperties props, byte[] body) throws IOException {
		//TODO anything to be done here?
		if(log.isDebugEnabled())
			log.debug("Basic return: Reply code: " + replyCode + ", Reply text: " + replyText + ", Exchange: " + exchange + ", Routing key: " + routingKey);
	}

	private synchronized void startReconnect() {
		startReconnect(1);
	}

	/**
	 * Start the reconnect timer
	 * Optionally try a number of times, until success
	 * @param retries the number of times to retry starting the reconnect timer
	 */
	private synchronized void startReconnect(int retries) {
		if(disconnecting) return;
		log.info("Starting reconnect sequence");
		if(reconnectTimer != null)
			reconnectTimer.cancel();
		
		reconnectTimer = new Timer("Server reconnect", true);

		long interval  = reconnectIntervalMillis > 0 ? reconnectIntervalMillis : DEFAULT_SERVER_RECONNECT_INTERVAL;
		
		try {
			reconnectTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(disconnecting || connect(false)) cancel();
				}
			}, interval, interval);
		} catch(IllegalStateException e) {
			log.error("A(n) " + e.getClass().getSimpleName() + " error occurred in startReconnect. Message: " + e.getMessage());
			log.debug("",e);
			if(retries > 0)
				startReconnect(--retries);
		}
	}

	public void transmit(MessageInfo<?> body) {
		log.trace("Queued a(n) {} message", body.getCmd());
		upQueue.add(body);
	}
	
	public boolean transmitDirect(MessageInfo<?> msg) {
		if(channel == null || msg == null) return false;
		BasicProperties props = new BasicProperties();
		try {
			//long time = logXmlTransmitPerformance.isTraceEnabled() ? System.currentTimeMillis() : 0;
			byte[] body = MessageUtils.toXml(msg);
			if(logXmlTransmits.isTraceEnabled()) {
				logXmlTransmits.trace(new String(body));
			}
			channel.basicPublish(exchangeName, upQueueRoutingKey, props, body);
			//if(time > 0) {
			//	time = System.currentTimeMillis()-time;
			//	logXmlTransmitPerformance.trace(msg.getCmd() + " sent in " + time + " ms");
			//}
			return true;
		} catch (Exception e) {
			log.error("error transmitting message - {}", e.getMessage());
			if (log.isTraceEnabled()) log.trace("stacktrace: {}", e);
			return false;
		}
	}
	
	public static final String CLIENT_CERTFILE						="client.certificate.file";
	public static final String CLIENT_CERT_PASSWORD					="client.certificate.password";
	public static final String CLIENT_KEYSTORE_FILE					="client.keystore.file";
	public static final String CLIENT_KEYSTORE_PASSWORD				="client.keystore.password";

	public static final String SERVER_HOST							="host";
	public static final String SERVER_PORT							="port";
	public static final String SERVER_USERNAME						="username";
	public static final String SERVER_PASSWORD						="password";
	public static final String SERVER_USE_SSL						="usessl";
	public static final String SERVER_RECONNECT_SECS				="reconnectintervalsecs";
	public static final String SERVER_VIRTUAL_HOST					="rabbitmq.virtual.host";
	public static final String SERVER_DOWNQUEUE_NAME				="rabbitmq.downqueue.name";
	public static final String SERVER_DOWNQUEUE_ROUTING_KEY			="rabbitmq.downqueue.routingkey";
	public static final String SERVER_UPQUEUE_NAME					="rabbitmq.upqueue.name";
	public static final String SERVER_UPQUEUE_ROUTING_KEY			="rabbitmq.upqueue.name";
	public static final String SERVER_EXCHANGE_NAME					="rabbitmq.exchange.name";
	
	

}
