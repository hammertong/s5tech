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
 
package com.s5tech.net.util;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;


public class XmlUtils {

	public static final String NAMESPACE = "http://s5tech.com/network";

	private static XMLContext ctx;

	static {
		ctx = new XMLContext();
		try {
			ctx.addPackage("com.s5tech.net.xml");
		} catch (ResolverException e) {
			e.printStackTrace();
		}
	}
	
	static public Marshaller newMarshaller() {
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setUseXSITypeAtRoot(true);
		marshaller.setSuppressXSIType(true);
		marshaller.setNamespaceMapping("", "http://s5tech.com/network");
		marshaller.setSchemaLocation("http://s5tech.com/network schema.xsd");		
		marshaller.setValidation(false);
		return marshaller;
	}
	
	static public Unmarshaller newUnmarshaller() {
		return ctx.createUnmarshaller();
	}
	
	/**
	 * Creates an element containing text
	 * @param elementName
	 * @param value
	 * @return
	 */
	public static AnyNode createTextElement(String elementName, String value) {
		if(value == null) value = "";
		AnyNode n = new AnyNode(AnyNode.ELEMENT, elementName, "", NAMESPACE, null);
		n.addAnyNode(new AnyNode(AnyNode.TEXT, null, null,null,value));
		return n;		
	}

}
