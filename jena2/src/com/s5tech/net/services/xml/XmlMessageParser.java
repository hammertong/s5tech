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

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlMessageParser extends DefaultHandler implements XmlNames {

	private SAXParser saxParser = null;

	private String currentPath = "";

	private Properties p = null;

	public XmlMessageParser() throws ParserConfigurationException,
			SAXException {
		super();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		saxParser = factory.newSAXParser();
		p = new Properties();
	}

	public void reset() {
		p.clear();
	}

	@Override
	public void startElement(String namespaceURI, String lName, String qName,
			Attributes attrs) throws SAXException {

		String eName = lName;

		if ("".equals(eName))
			eName = qName;

		currentPath += "/" + eName;

		if (p.containsKey(currentPath)) {
			int count = 1;
			while (p.containsKey(currentPath + ("#" + count))) {
				count++;
			}
			currentPath += ("#" + count);
		}

		p.put(currentPath, "");

		for (int i = 0; i < attrs.getLength(); i++) {
			p.put(currentPath + "@" + attrs.getLocalName(i), attrs.getValue(i));
		}

	}

	@Override
	public void endElement(String namespaceURI, String sName, String qName)
			throws SAXException {

		int n = currentPath.lastIndexOf('/');
		if (n >= 0)
			currentPath = currentPath.substring(0, n);
		else
			currentPath = "";

	}

	@Override
	public void characters(char buf[], int offset, int len) throws SAXException {

		Object o = p.get(currentPath);
		if (o == null)
			return;

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < len; i++) {
			buffer.append(buf[i + offset]);
		}
		buffer.append(o.toString());

		p.put(currentPath,
				buffer.toString().replace('\n', ' ').replace('\r', ' ')
						.replace('\t', ' ').trim());

	}

	public String getValue(String path) {
		return p.getProperty(path);
	}

	public Properties getProperties() {
		return p;
	}

	public Properties parse(String xml) throws IOException, SAXException {
		synchronized (p) {
			try {
				reset();
				saxParser.parse(new InputSource(new StringReader(xml)), this);
				return Properties.class.cast(p.clone());
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
		}
	}

}
