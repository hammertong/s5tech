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
 
package com.s5tech.net.services.logging;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

public class LogAnalyzer {

	String xmlschema = "schema.xsd";
	String from = null;
	String action = null; 
	String logdir = "./logs"; 
	String filter = null;
	
	public LogAnalyzer()
	{	
	}
	
	public LogAnalyzer(String action)
	{
		this.action = action;
	}
	
	public LogAnalyzer(String logdir, String xmlschema)
	{
		this.action = "validate";
		this.logdir = logdir;
		this.xmlschema = xmlschema;
	}

	public LogAnalyzer(String action, String logdir, String from)
	{
		this.action = action;
		this.logdir = logdir;
		this.from = from;
	}
	
	public String getXmlschema() {
		return xmlschema;
	}

	public void setXmlschema(String xmlschema) {
		this.xmlschema = xmlschema;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLogdir() {
		return logdir;
	}

	public void setLogdir(String logdir) {
		this.logdir = logdir;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void run(OutputStream stream)
	{
		try {
			
			Date datefrom = null;			
			
			if (from != null) 
			{
				DateFormat timef = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				datefrom = timef.parse(from);
			}
				
			if (action != null && action.equalsIgnoreCase("zip"))
			{
				ZipLog parser = new ZipLog();
				if (datefrom != null)  parser.setFrom(datefrom);
				parser.setOut(stream);
				parser.setLogdir(logdir);
				parser.run();
				if (stream != null) try { stream.close(); } catch (Throwable ignored) {}
			}
			else if (action != null && action.equalsIgnoreCase("validate"))
			{
				XmlLogValidator parser = new XmlLogValidator();	
				if (datefrom != null)  parser.setFrom(datefrom);
				parser.setSchemafile(xmlschema);
				PrintStream o = (stream != null ? new PrintStream(stream) : System.out);
				parser.setOut(o);
				parser.setLogdir(logdir);				
				parser.run();
				if (o != null) try { o.close(); } catch (Throwable ignored) {}	
			}	
			else if (action != null && action.equalsIgnoreCase("xml"))
			{
				XmlLogScanner parser = new XmlLogScanner();	
				if (datefrom != null)  parser.setFrom(datefrom);				
				PrintStream o = (stream != null ? new PrintStream(stream) : System.out);
				parser.setOut(o);
				parser.setLogdir(logdir);				
				parser.setFilter(filter);
				parser.run();
				if (o != null) try { o.close(); } catch (Throwable ignored) {}	
			}
			else if (action != null && action.equalsIgnoreCase("unauth"))
			{
				UnauthorizedEslsParser parser = new UnauthorizedEslsParser();
				if (datefrom != null) parser.setFrom(datefrom); 
				PrintStream o = (stream != null ? new PrintStream(stream) : System.out);
				parser.setOut(o);
				parser.setLogdir(logdir);
				parser.run();
				if (o != null) try { o.close(); } catch (Throwable ignored) {}	
			}
			else if (action != null && action.equalsIgnoreCase("view"))
			{
				LogViewer viewer = (logdir == null ? new LogViewer() : new LogViewer(logdir));
				viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				viewer.setSize(800, 500);
				viewer.setTitle("Network Application Logs Viewer");
				viewer.setVisible(true);	
				synchronized (viewer) {
					viewer.wait();
				}
			}
			else
			{
				ApplicationLogEventsDumper parser = new ApplicationLogEventsDumper();
				if (datefrom != null)  parser.setFrom(datefrom);
				PrintStream o = (stream != null ? new PrintStream(stream) : null); 
				parser.setLogDir(logdir);
				parser.setFilter(filter);
				parser.setOut(System.out);
				parser.run();				
				
				if (o != null) try { o.close(); } catch (Throwable ignored) {}	
			}
			
						
			System.exit(0);
			
		}
		catch(Throwable t) {
		
			t.printStackTrace();
		
		}
	}
	
	
	public static void exec(String[] args) {

		boolean startgui = false;

		LogAnalyzer m = new LogAnalyzer();
		String outputfile = null;
		
		for (int i = 0; i < args.length; i ++) 
		{
			if (args[i].equals("-G"))
			{
				startgui = true;
			}			
			else if (args[i].equals("-o"))
			{
				outputfile = args[++i];				
			}
			else if (args[i].equals("-x"))
			{
				m.xmlschema = args[++i];				
			}
			else if (args[i].equals("-f"))
			{
				m.from = args[++i];				
			}
			else if (args[i].equals("-a"))
			{
				m.action = args[++i];				
			}
			else if (args[i].equals("-l"))
			{
				m.logdir = args[++i];				
			}
			else if (args[i].equals("-r"))
			{
				m.filter = args[++i];				
			}					
		}
		
		if (startgui)
		{
			LogViewer l = new LogViewer();
			l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			l.setSize(800, 500);
			l.setTitle("S5Tech Network Application Logs Viewer");
			l.setVisible(true);				
			Object o = "";
			synchronized (o) {
				try {
					o.wait();
				}
				catch (Throwable t) {
					System.exit(0);
				}
			}
		}
		else {
			try {
				m.run ((outputfile != null ? new FileOutputStream(outputfile) : System.out));
				System.exit(0);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
	}

}
