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
 
package com.s5tech.net.services.webapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.castor.core.util.Base64Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Authenticator {
	
	private static Authenticator instance_ = null;
	
	private static final Logger log = LoggerFactory.getLogger ("WEBACCESS");
	
	private String auths = null;
		
	private String webadmin = null;
	
	private Authenticator()
	{
		webadmin = System.getProperty ("webadmin", "");
		
		//
		// load authorizations file (if not found NO authorization required)
		// file format all lines in:
		//
		// user:password
		// character # is a prefix for comment
		//
		 
		String webfile = System.getProperty ("webserver.authfile", "security/webauth");
		
		try {				
			File f = new File(webfile);
			if (f.exists()) {
				StringBuffer b = new StringBuffer();
				BufferedReader in = new BufferedReader(new FileReader(f));
				for (;;) {
					String t = in.readLine();
					if (t == null) break;
					t = t.trim();
					if (t.length() == 0) continue;
					if (t.startsWith("#")) continue;
					b.append(t).append('\n');
				}					
				if (b.length() > 0) auths = b.toString();
				in.close();
				log.info("web controller has loaded users from {} ...", webfile);
			}
		}
		catch (Throwable t) {		
			log.warn("cannot load authorization file from: " 
					+ webfile + " - reason: " 
					+ t.getMessage());				
		}
		//
		// end auth
		//
		
	}

	public static synchronized Authenticator instance()
	{
		if (instance_ == null) {
			instance_ = new Authenticator();
		}
		return instance_;
	}
	
	public boolean authorize(
			HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException
	{
		
		if (auths != null)
		{	
			//Security here..... 
			//HTTP Headers: 
			//  WWW-Authenticate: Basic realm="insert realm"
			//  Authorization: Basic QWxhZGluOnNlc2FtIG9wZW4=														
			
			//response.setHeader("WWW-Authenticate", "Basic realm=\"insert realm\"");
			response.setHeader("WWW-Authenticate", "Basic realm=\"Authorization needed @s5tech.com\"");
						
			if (request.getHeader("Authorization") == null)
			{
				//Authorization required
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			else 
			{
				String pass = request.getHeader("Authorization");
				if (pass.startsWith("Basic "))
				{								
					byte[] data = Base64Decoder.decode(pass.substring("Basic ".length()));
					String x = new String (
							data, 
							(request.getCharacterEncoding() != null ? request.getCharacterEncoding() : "UTF-8"));
					//Iterator<String> i = Charset.availableCharsets().keySet().iterator();
					//while (i.hasNext()) {
					//	String k = i.next();
					//	System.out.println(" >>>> " + k + " ==> " + new String (data, k));
					//}
					if (auths.indexOf(x + "\n") < 0) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return false; //auth failed										
					}
				}
				else
				{
					//Auth Method not allowed
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}								
			}								
		}
		else if (webadmin.length() > 0) 
		{	
			response.setHeader("WWW-Authenticate", "Basic realm=\"insert realm\"");
			
			if (request.getHeader("Authorization") == null)
			{
				//Authorization required
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			else 
			{
				String pass = request.getHeader("Authorization");
				if (pass.startsWith("Basic "))
				{								
					byte[] data = Base64Decoder.decode(pass.substring("Basic ".length()));
					String x = new String (
							data, 
							(request.getCharacterEncoding() != null ? request.getCharacterEncoding() : "UTF-8"));
					//Iterator<String> i = Charset.availableCharsets().keySet().iterator();
					//while (i.hasNext()) {
					//	String k = i.next();
					//	System.out.println(" >>>> " + k + " ==> " + new String (data, k));
					//}
					if (webadmin.indexOf(x) < 0) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return false; //auth failed										
					}									
				}
				else
				{
					//Auth Method not allowed
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}								
			}			
		}
		
		return true;
		
	}
	
	
}
