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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.castor.core.util.Base64Decoder;
import org.castor.core.util.Base64Encoder;

/**
 * 
 * <pre>
 * Usage: 
 *    java EslFirmwareXmlCreator &lt; binfile &gt; xmlfile
 * </pre>
 * @author moscardino
 *
 */
public class EslFirmwareXmlCreator {
	
	public void writeXml(InputStream in, PrintStream out, String msgid) throws IOException
	{
		boolean startNow = true;
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		for ( ;; ) {
			int c = in.read();
			if (c == -1) break;
			bo.write((byte) c);	
		}
		
		byte[] data = bo.toByteArray();
		
		char[] base64 = Base64Encoder.encode (data);
		
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<message xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
		out.println("\t\txsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" ");
		out.println("\t\tmsgId=\"" + msgid + "\" ");
		out.println("\t\tmsgCommand=\"EslFirmwareUpdate\" ");
		out.println("\t\txmlns=\"http://s5tech.com/network\">");
		out.println("\t<eslList all=\"true\" />");
		out.print("\t<firmware startNow=\"");
		out.print(startNow);
		out.println("\">");
		
		int i = 0;			
		for ( ;; ) {
			if (i >= base64.length) break;
			if (i % 64 == 0) { 
				out.println();
				out.print("\t\t");
			}
			out.print(base64[i++]);				
		}			
		out.println();
		
		out.println("\t</firmware>");
		out.println("</message>");
		
		try 
		{
			byte [] check = Base64Decoder.decode(new String(base64));
			for (i = 0; i < data.length; i ++)
			{
				if (data[i] != check[i]) 
					throw new IOException("byte non corrispondente dopo la conversione");
			}
		}
		catch (Throwable ct) {
			throw new IOException("check base64 conversion failure: " + ct.getMessage());
		}
	}
	
}
