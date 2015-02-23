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

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XmlValidator implements XmlNames {
	
	String schemaFile = null;
	XmlErrorHandler handler = null;
	
	public XmlValidator(String schemaFile) {
		this.schemaFile = schemaFile;
		handler = new XmlErrorHandler(true);
	}
	
	public boolean validate(String xmlMessage) throws ParserConfigurationException, SAXException 
	{
		InputSource source = new InputSource(new StringReader(xmlMessage));
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    spf.setValidating(true);
	    
	    SAXParser saxParser = spf.newSAXParser();
	    saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
	    saxParser.setProperty(JAXP_SCHEMA_SOURCE, new File(schemaFile));
	    
	    XMLReader xmlReader = saxParser.getXMLReader();	    
	    
	    xmlReader.setErrorHandler(handler);
	    handler.reset();
	    
	    try {
	    	xmlReader.parse(source);
	    	return (handler.getErrors() == 0);
	    }
	    catch (Throwable t) {
	    	System.err.println("validator parser exception > " + t.getMessage());
	    	return false;
	    }	    
	}
	
	public int getErrors()
	{
		return handler.getErrors();
	}
	
	public int getWarnings()
	{
		return handler.getWarnings();
	}

}
