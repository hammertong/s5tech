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

import java.net.URI;
import java.text.DecimalFormat;
import java.util.Enumeration;

import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

public class ActiveMQQueueCounter {


	public int count(String url, String queueName)
	{
		int count = 0;
		
		QueueConnection connection = null;
		QueueSession session = null;
		QueueBrowser browser = null;
		
		try 
		{			
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory (new URI(url));
			connection = factory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);			
			browser = session.createBrowser(new ActiveMQQueue(queueName));
			@SuppressWarnings("rawtypes")
			Enumeration e = browser.getEnumeration();
			while (e.hasMoreElements()) {
				e.nextElement();
				count ++;
			}
		}
		catch (Throwable t) 
		{
			t.printStackTrace();
		}
		finally 
		{
			if (browser != null) try { browser.close(); } catch (Throwable ignored){}
			if (session != null) try  { session.close(); } catch (Throwable ignored) {}
			if (connection != null) try  { connection.stop(); } catch (Throwable ignored) {}
			if (connection != null) try  { connection.close(); } catch (Throwable ignored) {}	
		}
		return count;
	}

}
