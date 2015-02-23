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
 
package com.s5tech.net.services.client;

//import java.io.File;
//import java.io.FileInputStream;
import java.net.URI;

import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQPublisherConnectionClose {

	protected static final Logger log = LoggerFactory.getLogger("NET");
	
	public static final String DEFAULT_URL = "tcp://127.0.0.1:61616";
	public static final String DEFAULT_QUEUE = "dynamicQueues/DOWN";
	
	protected String url = null;
	protected String queueName = null;
	
	public ActiveMQPublisherConnectionClose(String url, String queueName)
	{
		this.url = url;
		this.queueName = queueName;
	}
	
	public ActiveMQPublisherConnectionClose()
	{
		url = DEFAULT_URL;
		queueName = DEFAULT_QUEUE;
	}
		
	public void publish(byte [] data) {
		publish(new String(data));	
	}
	
	public void publish(String data) {

		QueueConnection cp = null;
		QueueSession qs = null;
		MessageProducer p = null;
		
		try 
		{			
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory (new URI(url));
			cp = factory.createQueueConnection();
			qs = cp.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			p = qs.createProducer (new ActiveMQQueue(queueName));
				
			TextMessage t = qs.createTextMessage();
		    t.setText(data);
		    p.send(t);
	    
		}
		catch (Throwable t) {
			
			log.error("transmission error (publish()) - {}", t); 
			
		}
		finally {
			if (p != null) try  { p.close(); } catch (Throwable ignored) {}
			if (qs != null) try  { qs.close(); } catch (Throwable ignored) {}
			if (cp != null) try  { cp.stop(); } catch (Throwable ignored) {}
			if (cp != null) try  { cp.close(); } catch (Throwable ignored) {}	
		}	    
		
	}
		
}
