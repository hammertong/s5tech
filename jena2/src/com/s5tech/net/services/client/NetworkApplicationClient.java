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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.s5tech.net.services.xml.XmlMessageParser;
import com.s5tech.net.services.xml.XmlValidator;

public class NetworkApplicationClient {
	
	private static boolean publish (String provider, String url, String queue, String message)
	{
		try 
		{
			if (provider == null || provider.equalsIgnoreCase("activemq") 
					|| provider == null || provider.length() == 0)
			{
				ActiveMQProducer p = new ActiveMQProducer();
				if (url != null) p.setUrl(url);
				if (queue != null) p.setQueueName(queue);
				p.open();				
				p.send(message.toString());
				p.close();
				return true;
			}
			else if (provider.toLowerCase().startsWith("rabbit"))
			{
				RabbitPublisherConnectionClose p = new RabbitPublisherConnectionClose();
				return p.publish(message.getBytes());
			}
			else 
			{
				throw new Exception("publisher provider not implemented: " + provider); 
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}
	
	private static String formatProperties(Properties p)
	{
		String head = null;
		StringBuffer b = new StringBuffer();

		if (p == null || p.size() == 0) return "EMPTY";
		
		for (Object o : p.keySet())
		{			
			String key = o.toString();
			//System.err.println("*** " + key + " ...");	
			
			if (key.startsWith("/message@xml") 
					|| key.startsWith("/message@xsi")) {
				continue;
			}
			
			if (p.getProperty(key)
					.replace('\n', ' ')
					.replace('\r', ' ')
					.replace('\t', ' ').trim().length() == 0) {
				continue;
			}
			
			if (key.equals("/message@msgCommand")) {
				head = "MSG " + p.getProperty(key).toUpperCase();
			}
			else if (key.equals("/ackMessage@msgCommand")) {
				head = "ACK " + p.getProperty(key).toUpperCase();
			} 
			else {
				int n = key.lastIndexOf('/');
				int m = key.lastIndexOf('@');
				String k = key.substring(m > 0 ? m + 1 : n + 1);
				b.append(' ').append(k.toLowerCase()).append('=').append(p.getProperty(key));
			}
		}
		
		if (head == null) return "HEADER-N/A";
		
		return head + b.toString();		
	}
	
	public static void exec (String args[])
	{
		String action = "publish";
		String provider = null;
		String url = null;
		String qn = null;
		String infile = null;
		
		int consumeTimeoutMs = 0;
		boolean stopSend = false;

		boolean authorizeAll  = false;
		
		XmlValidator validator = null;
		
		try {
			
			for (int i = 0; i < args.length; i ++) 
			{
				if (args[i].equals("-u")) {
					url = args[++i];
				}
				else if (args[i].equals("-p")) {
					provider = args[++i];
				}
				else if (args[i].equals("-q")) {
					qn = args[++i];
				}
				else if (args[i].equals("-a")) {
					action = args[++i];
				}
				else if (args[i].equals("-i")) {
					infile = args[++i];
				}
				else if (args[i].equals("-A")) {
					authorizeAll = true;
				}
				else if (args[i].equals("-t")) {
					consumeTimeoutMs = Integer.parseInt(args[++i]);
				}
				else if (args[i].equals("-v")) {
					String schema = args[++i];
					if (!(new File(schema).exists())) {
						System.err.println("xml schema file " + schema
								+ " not found ... exit!");
						System.exit(1);			
					}	
					validator = new XmlValidator(schema);
				}
				else if (args[i].equals("-n")) {
					stopSend = true;
				}
//				else {
//					System.err.println("invalid option " + args[i]);
//					System.exit(1);
//				}
			}
						
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			System.err.println("invalid command line ... exit!");
			System.exit(1);
			
		}
		
		if (infile != null && !(new File(infile).exists())) {
			System.err.println("input file " + infile
					+ " not found ... exit!");
			System.exit(1);			
		}
		
		try 
		{		
			if (action.equalsIgnoreCase("publish")) {
				
				StringBuffer message = new StringBuffer();
				
				BufferedReader in = new BufferedReader (
						new InputStreamReader(
								new FileInputStream(infile)));
				
				for (;;) {
					String line = in.readLine();
					if (line == null) break;
					message.append(line);
				}
				
				in.close();
				
				if (validator != null) {					
					if (validator.validate(message.toString())) {
						System.err.println("validation ok!" 
									+ (validator.getWarnings() > 0 ? 
											" (" + validator.getErrors() + " warnings)" 
											: ""));
					}
					else {
						System.err.println("validation error!!! (" 
								+ validator.getErrors() 
								+ " errors)");
						System.exit(1);
					}
				}
			    
				if (stopSend) System.exit(0);
				
				publish (provider, url, qn, message.toString());
				
			}
			else if (action.equalsIgnoreCase("receive")) {
				
				ActiveMQConsumer ac = null;
								
				if (consumeTimeoutMs > 0) {
					System.out.println("set consumer worker timeout = " 
							+ consumeTimeoutMs + " ms");
				}
				
				ac = new ActiveMQConsumer();
				if (url != null) ac.setUrl(url);
				if (qn != null) ac.setQueueName(qn);
				ac.open();				
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				XmlMessageParser parser = new XmlMessageParser();
				
				int count = 0;
				
				for ( ;; ) {
				
					String message = ac.receive();
					
					if (message == null || message.length() == 0) {
						System.err.println("receiver impl. error: returned empty message!!!");
						continue;
					}
					
					boolean isValid = (validator == null || validator.validate(message));
					
					count ++;
					
					Properties props = null;
					
					try {
						props = (isValid ? parser.parse (message) : null);	
					}
					catch (Throwable ex) {
						ex.printStackTrace();
					}
					
					if (authorizeAll 
							&& props.getProperty("/message@msgCommand")
								.equalsIgnoreCase("EslEvent")
							&& props.getProperty("/message/eslEvent@type")
								.equalsIgnoreCase("UnauthorizedJoinAttempt")) {						
						String esl = props.getProperty("/message/eslEvent@eslMac");
						
						StringBuffer addmsg = new StringBuffer() 
								.append("<?xml version='1.0' encoding='UTF-8'?>")
								.append("<message msgId=\"1234\" msgCommand=\"AddEslList\" ")
								.append("xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" ")
								.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://s5tech.com/network\">")
								.append("<eslList>")
								.append("<esl mac=\"")
								.append(esl)
								.append("\" type=\"5\" installationKey=\"WFhYWFlZWVk=\" />")
								.append("</eslList>")
								.append("</message>");
												
						publish (provider, url, qn, addmsg.toString());
						
					}
					
					System.out.println(
							String.format("RECV %s %6d %s %s", 
									df.format(new Date()), 
									count, 
									(isValid ? "VALID" : "ERROR"), 
									formatProperties(props)));

					if (consumeTimeoutMs > 0) { 
						System.err.println ("sleeping " + consumeTimeoutMs + " ms before next reception ...");
						Thread.sleep(consumeTimeoutMs);
					}
					
				}
				
			}
			else if (action.equalsIgnoreCase("count")) {
				
				ActiveMQQueueCounter cnt = new ActiveMQQueueCounter();				
				System.out.println("url::" + url + "\tqueue::" + qn + "\tmessages => " + cnt.count(url, qn));
				
			}
			else {
				
				throw new Exception("invalid action specified: " + action);
			}
			
			System.exit(0);
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			System.exit(1);			
		}
		
	}

}
