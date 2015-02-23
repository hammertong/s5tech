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
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.s5tech.net.esl.MessageInfo;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;
import com.s5tech.net.xml.Message;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


public class JMSServerConnectorProxy implements IServerConnector, IServerReceiver {

	private static final String TOPIC_PREFIX = "topic://";
	private static final String QUEUE_PREFIX = "queue://";
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	private static final Logger logXmlTransmits = LoggerFactory.getLogger(ISystemKeys.XMLSERVER + ".TO");
	private static final Logger logXmlReception = LoggerFactory.getLogger(ISystemKeys.XMLSERVER + ".FROM");
	
	private static final int MAX_UPQUEUE_SIZE = 500;
	
	private Queue<Message> messageSubscriberQueue;
	private Queue<MessageInfo<?>> upQueue;
	private IServerMessageReceiver serverMessageReceiver;
	
	private Thread consumerThread = null;
		
	private String url = null;
	
	String upDestination = null;
	boolean isUpTopic = false;
	
	String downDestination = null;
	boolean isDownTopic = false;
			
	public JMSServerConnectorProxy() {
		
		url = System.getProperty("server.url", "vm://S5NET");
		
		upDestination = System.getProperty("server.upqueue", "dynamicQueues/UP");
		downDestination = System.getProperty("server.downqueue", "dynamicQueues/DOWN");
		
		if (upDestination.startsWith(TOPIC_PREFIX))
		{
			isUpTopic = true;
			upDestination = upDestination.substring(TOPIC_PREFIX.length());			
		}
		else if (upDestination.startsWith(QUEUE_PREFIX))
		{
			upDestination = upDestination.substring(QUEUE_PREFIX.length());
		}
		
		if (downDestination.startsWith(TOPIC_PREFIX))
		{
			isDownTopic = true;
			downDestination = downDestination.substring(TOPIC_PREFIX.length());			
		}
		else if (downDestination.startsWith(QUEUE_PREFIX))
		{
			downDestination = downDestination.substring(QUEUE_PREFIX.length());
		}	
		
		if (log.isTraceEnabled())
			log.trace("connecting to JMS url " + url + ", destinations: "
					+ "upstream => " + (isUpTopic ? TOPIC_PREFIX : QUEUE_PREFIX) + upDestination + ", "
					+ "downstream => " + (isDownTopic ? TOPIC_PREFIX : QUEUE_PREFIX) + downDestination + ", ");
		
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
					log.warn("Up Queue trimmed: removed " + count + " messages");
				
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
	
	public void setServerMessageReceiver(
			IServerMessageReceiver serverMessageReceiver) {
		this.serverMessageReceiver = serverMessageReceiver;
	}

	public synchronized boolean isConnected() {
		return (consumerThread != null && consumerThread.isAlive());		
	}

	public synchronized boolean connect() 
	{		
		try {
			
			consumerThread = new Thread (new Runnable() {
				
				@Override
				public void run() {					
					
					Connection connection = null;
					Session session = null;
					MessageConsumer consumer = null;
					
					try {
						
						ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
						
						connection = factory.createConnection();
		                connection.start();

		                connection.setExceptionListener(new ExceptionListener() {							
							@Override
							public void onException(JMSException e) {
								log.warn("downstream listener exception {}", e.getMessage());
								if (log.isTraceEnabled()) log.trace("---------- stacktrace\n{}", e);
							}
						});

		                // Create a Session
		                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		                // Create the destination (Topic or Queue)
		                Destination destination = (isDownTopic ? session.createTopic(downDestination) 
		                		: session.createQueue(downDestination));

		                // Create a MessageConsumer from the Session to the Topic or Queue
		                consumer = session.createConsumer(destination);

						log.info("consumer connected to jms downstream destination");
						
						for ( ;; ) 
						{	
							javax.jms.Message message = consumer.receive();							
			                if (message instanceof TextMessage) {
			                    try 
								{
									onMessage((TextMessage) message);
								}
								catch (Throwable t) 
								{
									TextMessage msg = (TextMessage) message;
									log.error("exception parsing downstream message - {}", t.getMessage());
									if (log.isTraceEnabled()) 
										log.trace("--------- message caused exception:\n{} --------- stacktrace:\n{}", 
												(msg.getText() != null ? msg.getText() : "null"), t);
								}
			                }
			                else {
			                	
			                	log.warn("invlid object received from JMS downstream channel !! -> " + 
			                			(message == null ? "message is null" : message.toString()));
			                	
			                }							
						}
						
					}
					catch (Throwable t) {
						log.error("downstream consumer exception: {}", t.getMessage());
						if (log.isTraceEnabled()) 
							log.trace("--------- exception stacktrace:\n{}", t);
					}
					finally 
					{
						if (consumer != null) try { consumer.close(); } catch (Throwable ignored) {}
						if (session != null) try { session.close(); } catch (Throwable ignored) {}
						if (connection != null) try { connection.close(); } catch (Throwable ignored) {}
					}
					
					log.warn("JMS Downstream Destination Consumer Exit!");
										
				}
				
			}, "JMS Downstream Destination Consumer");
			
			consumerThread.setDaemon(true);
			consumerThread.start();
			
			log.info ("JMS receiver client started on {}", url);
			
			return true;
		}
		catch (Throwable t) {
			
			log.error("error starting JMS receiver - {}", t);
			disconnect();
			
			return false;
		}		
	}

	
	public synchronized void disconnect() {		
		consumerThread.interrupt();		
		log.info("JMS client disconnected from {}", url);
	}
	
	public void onMessage(TextMessage arg0) {
		
		String textMessage = null;
		
		try {
			textMessage = arg0.getText();
		}
		catch (Throwable t) {
			log.error("error retrieving text from jms message - {}", t.getMessage());
			return;
		}
		
		if (textMessage == null) 
		{
			log.warn("received jms message with null text");
			return;
		}
		
		byte [] data = textMessage.getBytes();
				
		// Checking for UTF8 BOM and, if present, 'manually' remove it.
		int offset = data.length > 3 
					&& data[0] == (byte)0xEF 
					&& data[1] == (byte)0xBB 
					&& data[2] == (byte)0xBF ? 3 : 0;

		//dovuto ad un problema con il producer .NET (vedi ActiveMQBrokerConnector.cs in BackendTools):
		offset = (offset > 0 ? offset : (data[0] == (byte)'?' ? 1 : 0)); 
		
		if(logXmlReception.isTraceEnabled())
			logXmlReception.trace(new String(data, offset, data.length - offset));
		
		Object obj = MessageUtils.fromXml(data, offset, data.length - offset);
		
		if(obj != null && obj instanceof Message) {
			messageSubscriberQueue.add(Message.class.cast(obj));
		}
	}
	
	public void transmit(MessageInfo<?> body) {
		log.trace("Queued a(n) {} message", body.getCmd());
		upQueue.add(body);
	}
	
	public synchronized boolean transmitDirect(MessageInfo<?> msg) 
	{
		try 
		{
			byte[] body = MessageUtils.toXml(msg);
			String textMessage =  new String(body);
			
			// Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = (isUpTopic ? session.createTopic(upDestination) : session.createQueue(upDestination));

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
						
			TextMessage t = session.createTextMessage();
		    t.setText(textMessage);
		    producer.send(t);
			
			if(logXmlTransmits.isTraceEnabled()) {
				logXmlTransmits.trace(textMessage);
			}
			
			return true;
		} 
		catch (Exception e) 
		{
			log.error("error publishing " + (isUpTopic ? "topic": "queue") + " text message - {}", e.getMessage());
			if (log.isTraceEnabled()) log.trace("exception stacktrace:\n{}", e);
			
			return false;
		}
	}
		
}

