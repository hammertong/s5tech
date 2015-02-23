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

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.xml.sax.SAXException;

import java.io.*;
import java.util.Date;


public class XmlLogValidator extends ALogFileFinder {
	
	private String schemafile = "schema.xsd";
	
	private String logdir = "./logs";
	
	private PrintStream out = System.out;
	
	private Date from = null;
	
	public void run()
	{
		int count = 0;
        int errors = 0;
        
		try 
		{		
			Source schemaFile = new StreamSource(new File(schemafile));
	        
	        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = schemaFactory.newSchema(schemaFile);
	        Validator validator = schema.newValidator();

	        for (String f : getOrderedLogFilesList(logdir, "xmlserver", ".log"))
	        {
	        	BufferedReader reader = new BufferedReader(new FileReader(f));
	        
	        	int linecount = 0;
	        	String line = null;
	        	
	        	for (;;) 
	        	{
	        		String desc;
	        		
		        	if (line == null) {
		        		line = reader.readLine();
		        		linecount ++;		        	
		        	}
		        	
		        	if (line == null) break;
		        	
		        	if (line.indexOf("[XMLSERVER.FROM]") >= 0)
		        	{
		        		desc = "DOWNSTREAM";
		        	}
		        	else if (line.indexOf("[XMLSERVER.TO]") >= 0)
		        	{
		        		desc = "UPSTREAM";
		        	}
		        	else
		        	{
		        		continue;
		        	}
		        	
		        	String time = line.split(" ")[0];		   
		        	if (from != null && timef.parse(time).before(from)) continue;
		        	StringBuffer w = new StringBuffer();
		        	
		        	for (;; ) 
		        	{
		        		line = reader.readLine();
		        		if (line == null) break;
		        		linecount ++;
		        		if (line.indexOf("[XMLSERVER.FROM]") >= 0 || line.indexOf("[XMLSERVER.TO]") >= 0)
			        		break;
		        		w.append(line).append('\n');
		        	}
		        	
		        	ByteArrayInputStream in = new ByteArrayInputStream(w.toString().getBytes());
	
		        	try{
			        	count ++;
			            Source xmlFile = new StreamSource(in);
			            validator.validate(xmlFile);
			        }
			        catch (SAXException e) 
			        {
			        	errors ++;
			        	out.print(time);
			            out.print(";");
			            out.print(desc);
			            out.print(";");
			            out.print(f);
			            out.print(";");
			            out.print(linecount);
			            out.print(";");
			            out.print(e.getLocalizedMessage());
			            out.println();
			        }
		        	
		        	in.close();		        	
	        	}
	        	
	        	reader.close();
	        }
	        
		}
		catch (Throwable t) 
		{
			t.printStackTrace();	
		}
        
        System.err.println("TOTAL -> parsed " + count + " xml mesages -> " + errors + " with errors");
				
	}
	
	public String getSchemafile() {
		return schemafile;
	}

	public void setSchemafile(String schemafile) {
		this.schemafile = schemafile;
	}

	public String getLogdir() {
		return logdir;
	}

	public void setLogdir(String logdir) {
		this.logdir = logdir;
	}
	
	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}
	
}
