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

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConnectionParameters;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RabbitPublisherConnectionClose {

	String configfile = "./conf/rabbitmq.properties";

	public RabbitPublisherConnectionClose()
	{
	}
	
	public RabbitPublisherConnectionClose(String configfile)
	{
		this.configfile = configfile;
	}
			
	public boolean publish(byte [] data) throws Exception{
		
		Connection conn = null;
		Channel channel;
		
		try {
		
			Properties p = new Properties();
			p.load (new FileInputStream(new File(configfile)));
					
			String password = p.getProperty("password");
			String username = p.getProperty("username"); 
			String downQueueName = p.getProperty("rabbitmq.downqueue.name");
					
			String virtualHost = p.getProperty("rabbitmq.virtual.host");
			String serverHost = p.getProperty("host");
			int serverPort = Integer.parseInt(p.getProperty("port", "5672"));
			
			String downQueueRoutingKey = p.getProperty("rabbitmq.downqueue.name");
			String exchangeName = p.getProperty("rabbitmq.exchange.name", "amq.direct");
					
			if (System.getProperty("server") != null) serverHost = System.getProperty("server"); 
					
			ConnectionParameters parms = new ConnectionParameters();
			parms.setPassword(password);
			parms.setUsername(username);
			parms.setVirtualHost(virtualHost);
			
			ConnectionFactory factory = new ConnectionFactory(parms);
			
			conn = factory.newConnection(serverHost, serverPort);
			channel = conn.createChannel();
			channel.queueDeclare(downQueueName);
			channel.queueBind(downQueueName, "amq.direct", downQueueRoutingKey);
			
			BasicProperties props = new BasicProperties();
			channel.basicPublish(exchangeName, downQueueRoutingKey, props, data);
		
			return true;
		}
		catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		finally {
			if (conn != null) try { conn.close(); } catch (Throwable ignred) {}
		}
			
			
	}
	
	

	public boolean publishFile(String xmlfile) {
	
		File xmlsource = null;
		
		try 
		{	
			xmlsource = new File(xmlfile); 
			
			if (!xmlsource.exists() || !xmlsource.canRead() || !xmlsource.isFile())
				throw new Exception("cannot read xmlfile: " + xmlsource.getAbsolutePath());	
			
			int filelen = (int)xmlsource.length();
			byte [] data = new byte[filelen];
			FileInputStream in = new FileInputStream(xmlsource);
			in.read(data, 0, filelen);
			
			RabbitPublisherConnectionClose p = new RabbitPublisherConnectionClose();
			
			boolean ret = p.publish (data);
			
			in.close();
			
			return ret;
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			return false;
			
		}		
		
	}

}
