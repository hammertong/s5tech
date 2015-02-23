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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlErrorHandler implements ErrorHandler {

	boolean verbose = false;
	
	private int err_count = 0;
	private int warn_count = 0;;
		
	public XmlErrorHandler()
	{
		reset();
		verbose = false;
	}
	
	public XmlErrorHandler(boolean verbose)
	{
		reset();
		this.verbose = verbose;
	}
	
	public void reset()
	{
		err_count = 0;
		warn_count = 0;
	}
	
	@Override
	public void warning(SAXParseException exception) throws SAXException {
		if (verbose) System.err.println("VALIDATOR WARNING > " + exception.toString());
		warn_count ++;
	}
	
	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		if (verbose) System.err.println("VALIDATOR FATAL ERROR > " + exception.toString());
		err_count ++;
	}
	
	@Override
	public void error(SAXParseException exception) throws SAXException {
		if (verbose) System.err.println("VALIDATOR ERROR > " + exception.toString());
		err_count ++;
	}
	
	public int getErrors()
	{
		return err_count;
	}
	
	public int getWarnings()
	{
		return warn_count;
	}
	
}
