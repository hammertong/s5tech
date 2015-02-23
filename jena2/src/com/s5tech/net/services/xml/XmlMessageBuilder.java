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
 
package com.s5tech.net.services.xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlMessageBuilder {

	static AtomicInteger serial = new AtomicInteger(0);
	
	Document document;
	DocumentBuilderFactory factory;
	TransformerFactory trFactory;
	
	public XmlMessageBuilder() {
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		trFactory = TransformerFactory.newInstance();
	}
	
	public String createEslMessageFromMacList(List<String> macs, String template, Properties extParams)
	{
		StringWriter w = new StringWriter();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<data>");
		buffer.append("<id>").append(serial.incrementAndGet()).append("</id>");
		for (String m : macs) 
		{
			buffer.append("<mac>").append(m).append("</mac>");
		}
		if (extParams != null) {
			for (Object o : extParams.keySet()) 
			{
				String key = o.toString();
				buffer
					.append('<').append(key).append('>')
					.append(extParams.getProperty(key))
					.append('<').append('/').append(key).append('>');
			}
		}
		buffer.append("</data>");
		build (template, buffer.toString(), w);
		return w.toString();
	}
	
	public String createEslMessageFromMacList(String[] macs, String template, Properties extParams)
	{
		StringWriter w = new StringWriter();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<data>");
		buffer.append("<id>").append(serial.incrementAndGet()).append("</id>");
		for (String m : macs) 
		{
			buffer.append("<mac>").append(m).append("</mac>");
		}
		if (extParams != null) {
			for (Object o : extParams.keySet()) 
			{
				String key = o.toString();
				buffer
					.append('<').append(key).append('>')
					.append(extParams.getProperty(key))
					.append('<').append('/').append(key).append('>');
			}
		}
		buffer.append("</data>");
		build (template, buffer.toString(), w);
		return w.toString();
	}

	public String createEslMessageFromXmlFile(String xmlfile, String template, Properties extParams) throws IOException
	{
		StringWriter w = new StringWriter();
		StringBuffer buffer = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(xmlfile));
		for ( ;;) {
			String l = in.readLine();
			if (l == null) break;
			buffer.append(l);			
		}
		in.close();
		build (template, buffer.toString(), w);
		return w.toString();
	}
	
	public void build (String stylesheet_path, String xmldata, Writer out)
	{	
		try 
		{
			InputStream is = null;
			
			if (stylesheet_path.startsWith("file://")) {
				is = new FileInputStream(stylesheet_path.substring(7));
			}
			else {
				is = getClass().getResourceAsStream(stylesheet_path);
			}
			
			if (is == null) throw new IOException("cannot read xsl template " 
					+ stylesheet_path);
			
			
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xmldata)));
            NodeList list = document.getElementsByTagName("data");
            Node node = list.item(0);
            
            StreamSource stylesource = new StreamSource(is); 
            Transformer transformer = trFactory.newTransformer(stylesource);
            
            DOMSource source = new DOMSource(node);
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
            
        }
		catch (Throwable t) {
			
			t.printStackTrace();
			
		}
		
	}
	
	public static void exec(String[] args) 
	{
		try 
		{
			Properties params = null;
			
			List<String> maclist = null;
			String xmlfile = null;
			String xslt = null;
			String outfile = null;
			
			for (int i = 0; i < args.length; i ++) {
				
				if (args[i].equals("-m")) 
				{
					maclist = new ArrayList<String>();
					
					for (String mac : args[++i].split(",")) 
					{
						if (mac.length() != 16) {
							System.err.println("mac specified not valid: " + mac);
							System.exit(1);
						}
						
						mac = mac.toUpperCase();
						
						for (int j = 0; j < 16; j ++) {
							if ("0123456789ABCDEF".indexOf(mac.charAt(i)) < 0) 
							{
								System.err.println("mac specified not valid: " + mac);
								System.exit(1);
							}
						}
						
						maclist.add(mac);	
					}
					
				}				
				else if (args[i].equals("-x")) {
					xmlfile = args[++i];
				}
				else if (args[i].equals("-o")) {
					outfile = args[++i];
				}
				else if (args[i].equals("-t")) {	
					xslt = args[++i];
					if (!xslt.toLowerCase().startsWith("file://")) {
						xslt = "file://" + xslt;
					}					
				}
				else if (args[i].equals("-p")) {
					if (params == null) params = new Properties();
					params.put(args[i], args[++i]);
				}
				
			}
			
			if (maclist == null && xmlfile == null) {
				System.err.println("neither xml data file and mac list specified!");
				System.exit(1);
			}
			
			if (xslt == null) {
				System.err.println("no xsl template specified!");
				System.exit(1);
			}
			
			PrintStream out = (outfile != null ? new PrintStream(outfile) : System.out);
			
			XmlMessageBuilder b = new XmlMessageBuilder();
			
			String message = null;
			
			if (maclist != null) 
			{
				message = b.createEslMessageFromMacList(maclist, xslt, params);
			}
			else 
			{
				message = b.createEslMessageFromXmlFile(xmlfile, xslt, params);
			}
			
			if (message == null) 
				System.err.println("cannot create message file with input data!");
			
			out.println(message);
			if (outfile != null) out.close(); 
			
			System.exit(0);
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			System.exit(1);
		}
		
	}

}
