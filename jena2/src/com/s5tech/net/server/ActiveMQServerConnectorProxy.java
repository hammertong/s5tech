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
import java.net.URI;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.esl.MessageInfo;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;
import com.s5tech.net.xml.Message;


public class ActiveMQServerConnectorProxy implements IServerConnector, IServerReceiver {

	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	private static final Logger logXmlTransmits = LoggerFactory.getLogger(ISystemKeys.XMLSERVER + ".TO");
	private static final Logger logXmlReception = LoggerFactory.getLogger(ISystemKeys.XMLSERVER + ".FROM");
	
	private static final int MAX_UPQUEUE_SIZE = 500;
	
	private Queue<Message> messageSubscriberQueue;
	private Queue<MessageInfo<?>> upQueue;
	private IServerMessageReceiver serverMessageReceiver;
	
	private Thread consumerThread = null;
	
	QueueConnection cp = null;
	QueueSession sp = null;
	QueueConnection cc = null;
	QueueSession sc = null;
	MessageProducer p = null;
	MessageConsumer c = null;
	
	private String url = null;
	String amqUpQueueName = null;
	String amqDownQueueName = null;
			
	public ActiveMQServerConnectorProxy() {
		
		url = System.getProperty("server.url", "vm://S5NET");
		amqUpQueueName = System.getProperty("server.upqueue", "dynamicQueues/UP");
		amqDownQueueName = System.getProperty("server.downqueue", "dynamicQueues/DOWN");
		
		if (log.isTraceEnabled())
			log.trace("connecting to activemq url " + url + ", queues: {}, {}", 
					amqUpQueueName, amqDownQueueName);
		
		upQueue = new ActiveQueue<MessageInfo<?>>(new IActiveQueueSubscriber<MessageInfo<?>>() {

			public void elementFromQueue(MessageInfo<?> element) {
				
				// Kinda busy wait - sorry, but this needs to be implemented quickly
				while(!isConnected()) {
					Tools.doWait("", 5000);
				}
 
				transmitDirect(element);

				// Queue trimming ...
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
					
					try {
						ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory (new URI(url));
						cc = factory.createQueueConnection();
						sc = cc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);			
						c = sc.createConsumer (new ActiveMQQueue(amqDownQueueName));
						cc.start();		
						log.info("down queue consumer connected");
						for ( ;; ) 
						{							
							TextMessage msg = (TextMessage) c.receiveNoWait();
							if (msg != null)
							{
								try 
								{
									onMessage(msg);
								}
								catch (Throwable t) 
								{
									log.error("error parsing down queue message - {}", t.getMessage());
									if (log.isTraceEnabled()) log.trace("received message: {}", 
											(msg.getText() != null ? msg.getText() : "null"));
								}
							}
							else 
							{
								Thread.sleep(500);
							}
						}						
					}
					catch (Throwable t) {
						log.error("down queue consumer error: {}", t);
					}
					finally 
					{
						try { if (c != null) c.close(); } catch (Throwable ignored) {}
						try { if (sc != null) sc.close(); } catch (Throwable ignored) {}
						try { if (cc != null) cc.stop(); cc.close(); } catch (Throwable ignored) {}
						
						if (p != null) {
							if (log.isTraceEnabled()) log.trace("stopping producer session ...");
							synchronized (p) {
								try { p.close(); } catch (Throwable ignored) {}
								try { if (sp != null) sp.close(); } catch (Throwable ignored) {}
								try { if (cp != null) cp.stop(); cp.close(); } catch (Throwable ignored) {}								
							}
							if (log.isTraceEnabled()) log.trace("producer session stopped");
							p = null;
							sp = null;
							cp = null;
						}						
					}
					
					log.info("down queue consumer exit");
					
					c = null;
					sc = null;
					cc = null;
										
				}
			}, "ActiveMQ Downqueue Consumer");
			
			consumerThread.setDaemon(true);
			consumerThread.start();
			
			log.info ("activemq client started at {}", url);
			
			return true;
		}
		catch (Throwable t) {
			
			log.error("error starting activemq connector - {}", t);
			disconnect();
			
			return false;
		}		
	}

	
	public synchronized void disconnect() {
		
		consumerThread.interrupt();
		
		log.info("activemq client disconnected from {}", url);
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

		//FIXED JAN 2013 due to a .net client bug @see ActiveMQBrokerConnector.cs in BackendTools
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
			try 
			{
				if (p == null) 
				{
					ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory (new URI(url));
					cp = factory.createQueueConnection();
					sp = cp.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);			
					p = sp.createProducer (new ActiveMQQueue(amqUpQueueName));
				}
			}
			catch (Throwable t) 
			{
				log.error("cannot create up queue producer {}", t);
				return false;
			}
			
			byte[] body = MessageUtils.toXml(msg);
			String textMessage =  new String(body);
			
			TextMessage t = sp.createTextMessage();
		    t.setText(textMessage);
		    p.send(t);
			
			if(logXmlTransmits.isTraceEnabled()) {
				logXmlTransmits.trace(textMessage);
			}
			
			return true;
		} 
		catch (Exception e) 
		{
			log.error("error transmitting message - {}", e.getMessage());
			if (log.isTraceEnabled()) log.trace("stacktrace: {}", e);
			return false;
		}
	}
		
}

