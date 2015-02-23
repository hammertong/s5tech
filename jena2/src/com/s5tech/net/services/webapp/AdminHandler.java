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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.castor.core.util.Base64Decoder;
import org.castor.core.util.Base64Encoder;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.services.client.ActiveMQPublisherConnectionClose;
import com.s5tech.net.services.client.EslFirmwareXmlCreator;
import com.s5tech.net.services.display.LCDPayload;
import com.s5tech.net.services.logging.ALogFileFinder;
import com.s5tech.net.services.logging.ApplicationLogEventsDumper;
import com.s5tech.net.services.logging.UnauthorizedEslsParser;
import com.s5tech.net.services.logging.XmlLogValidator;
import com.s5tech.net.services.logging.ZipLog;
import com.s5tech.net.util.ConnectionFactory;
import com.s5tech.net.util.Encoder;

public class AdminHandler extends AbstractHandler {
	
		private static final Logger log = LoggerFactory.getLogger ("WEBACCESS.ADMIN");
		
		private FormatTools fmt = new FormatTools();

		IAdminServices netsrv = null;

		private Long msgId = new Long(1);	
		
		byte[] favicon = null;
		
		private String documentRoot;
		private String adminPath;
		
		private boolean flat = false;
		
		public AdminHandler(String documentRoot, String contextPath, String webicon)
		{
			this.documentRoot = documentRoot;
			this.adminPath = contextPath;
			
			if (!documentRoot.endsWith("/")) documentRoot += "/";
			
			if (log.isInfoEnabled())
				log.info("deploying admin services " + contextPath + ", location: " + documentRoot +" ...");
			
			netsrv = AdminServicesFactory.instance().createService(
					System.getProperty ("webapp.admin.services.url", "local"));
			
			flat = !(netsrv instanceof AdminLocalServices);
			
			if (!webicon.startsWith("/") && webicon.indexOf(':') < 0) webicon = documentRoot + webicon;
			
			try {				
				File f = new File(webicon);
				if (f.exists()) {
					favicon = new byte [(int)f.length()];
					FileInputStream in = new FileInputStream(f);
					in.read(favicon);
					in.close();
					log.info("web controller has loaded favorite icon from {} ...", webicon);
				}
			}
			catch (Throwable t) {				
				log.warn("cannot load favorite iconf file from: " 
						+ webicon + " - reason: " 
						+ t.getMessage());				
			}
		}
		
		
		@SuppressWarnings("rawtypes")
		@Override
		public void handle(
				String path, 
				HttpServletRequest request,
				HttpServletResponse response, 
				int dispatch) throws IOException,
				ServletException {
			
		try 
		{	
			if (log.isTraceEnabled()) 
				log.trace("HTTP access from {} - {} > " + request.getRequestURI(), request.getRemoteAddr(), request.getMethod());
										
			String basepath = documentRoot;
			
			if (!new File(basepath).isDirectory()) throw new Exception ("document root not found: " + basepath);
			
			if (!Authenticator.instance().authorize(request, response)) return; 
									
			//System.err.println(" >>>>>> " + path);
			if (path == null || path.length() <= 1) {
				response.sendRedirect(adminPath + "/files/index.html");
				return;
			}				
									
			String rct = request.getContentType();
			if (rct == null) rct = "text/xml";
			if (rct.indexOf("json") >= 0) rct = "application/json"; 
			
			if (favicon != null && path.startsWith("/favicon.ico"))
			{	
				response.setContentType("image/vnd.microsoft.icon");
				response.setHeader("Cache-Control", "max-age=64800");
				response.setContentLength((int) favicon.length);
				OutputStream out = response.getOutputStream();							
				out.write(favicon);
				out.flush();
				out.close();														
			}	
			else if (path.startsWith("/files/"))
			{
				String localpath = basepath + path.substring(7);
				File f = new File(localpath);
				
				if (f.isFile() && f.exists())
				{
					if (path.endsWith(".html") || path.endsWith(".htm"))
					{
						response.setContentType("text/html");
					}
					else if (path.endsWith(".css"))										
					{
						response.setContentType("text/css");
					}
					else if (path.endsWith(".csv"))										
					{
						response.setContentType("text/csv");
					}
					else if (path.endsWith(".txt"))										
					{
						response.setContentType("text/plain");
					}
					else if (path.endsWith(".js"))										
					{
						response.setContentType("application/javascript");
					}
					else if (path.endsWith(".xml"))
					{
						response.setContentType("text/xml");	
					}
					else if (path.endsWith(".xsl") || path.endsWith(".xslt"))
					{
						response.setContentType("application/xslt+xml");	
					}
					else if (path.endsWith(".png"))
					{
						response.setContentType("image/png");	
					}
					else if (path.endsWith(".gif"))
					{
						response.setContentType("image/gif");	
					}
					else if (path.endsWith(".jpg"))
					{
						response.setContentType("image/jpeg");	
					}
					else if (path.endsWith(".zip"))
					{
						response.setContentType("application/zip");	
					}
					else if (path.endsWith(".svg"))
					{
						response.setContentType("image/svg+xml");	
					}
					else
					{
						response.setContentType("application/octet-stream");	
					}
					
					response.setContentLength((int) f.length());
					OutputStream out = response.getOutputStream();
					FileInputStream in = new FileInputStream(f);
					for ( ;; ) {
						int c = in.read();
						if (c == -1) break;
						out.write(c);
					}
					out.flush();
					out.close();
					in.close();
					
				}
				else 
				{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}							
			}
			else if (path.startsWith("/network/"))
			{	
		        String service = (path.indexOf('?') >= 0 ? path.substring(9, path.indexOf('?')) : path.substring(9));
		        
		        StringBuffer params = new StringBuffer();
		        
		        try {
			        Enumeration e = request.getParameterNames();
			        while (e.hasMoreElements())
			        {
			        	String k = (String) e.nextElement();
			        	if (params.length() > 0) params.append(' ');
			        	params.append(k).append('=').append(request.getParameter(k).toString());
			        }
		        }
		        catch (Throwable ttt) {}
		        
		        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		        for ( ;;)
		        {
		        	String t = in.readLine();
		        	if (t == null)  break;
		        	if (params.length() > 0) params.append('\n');
		        	params.append(t);
		        }

		        String data = null;
		        
		        synchronized (netsrv) {
				
			        if (service.equalsIgnoreCase("version"))			        
			        {
			        	data = fmt.formatResponse("version", netsrv.getVersion(), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("threads"))
			        {
			        	data = fmt.formatResponse("threads", netsrv.getTasks(), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("coordinators"))
			        {
			        	data = fmt.formatResponse("coordinators", netsrv.getCoordinators(params.toString()), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("queues"))
			        {
			        	data = fmt.formatResponse("queues", netsrv.getCoordinatorsQueues(), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("hubs"))
			        {	
			        	data = fmt.formatResponse("hubs", netsrv.getHubs(), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("esls"))
			        {
			        	data = fmt.formatResponse("esls", netsrv.getEsls(params.toString()), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("system"))
			        {
			        	data = fmt.formatResponse("system", netsrv.getSystemInfo(), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("firmwares"))
			        {
			        	data = fmt.formatResponse("firmwares", netsrv.getFirmwareInfo(), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("upgrading"))
			        {
			        	data = fmt.formatResponse("upgrading", netsrv.getFirmwareUpgradeInfo(), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("setchannels"))
			        {
			        	data = fmt.formatResponse("setchannels", netsrv.setCoordinatorChannel(params.toString()), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("settime"))
			        {
			        	data = fmt.formatResponse("setchannels", netsrv.setCoordinatorTime(params.toString()), rct, flat);
			        }
			        else if (service.equalsIgnoreCase("props"))
			        {							        	
			        	data = fmt.formatResponse("props", netsrv.getParameters(), rct, flat);
			        }	
			        else if (service.equalsIgnoreCase("retryupdate"))
			        {							        	
			        	data = fmt.formatResponse("updated", netsrv.retryPriceUpdate((params == null ? null : params.toString())), rct, flat);
			        }						        
			        else 
			        {
			        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			        }
			        
			        if (data != null)
			        {
			        	response.setContentLength((int) data.length());
			        	response.setContentType(rct);
						OutputStream out = response.getOutputStream();
						out.write(data.getBytes());
						out.flush();
						out.close();
			        }
			        else 
			        {
			        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			        }
			        
		        }
		        
			}						
			else if (path.startsWith("/upload/"))
			{
				if (path.startsWith("/upload/publish"))
				{								
					
					String url = System.getProperty ("server.url", "vm://S5NET");
					String queue = System.getProperty ("server.downqueue", "dynamicQueues/DOWN");
					ActiveMQPublisherConnectionClose p = new ActiveMQPublisherConnectionClose(url, queue);
					
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					
					for (Object item : items)
					{
						FileItem file = (FileItem) item;
						StringBuffer b = new StringBuffer();
						if (file.getSize() > 0)
						{
							BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()));
							for ( ;;) {
								String l = in.readLine();
								if (l == null) break;
								b.append(l).append("\n");										
							}
							p.publish (b.toString().getBytes());
						}
					}
					
				}
				else if (path.startsWith("/upload/firmware"))
				{
					
					String msgid = request.getParameter("msgid");
					if (msgid == null || msgid.length() == 0) msgid = "1234";
					
					String url = System.getProperty ("server.url", "vm://S5NET");
					String queue = System.getProperty ("server.downqueue", "dynamicQueues/DOWN");
					ActiveMQPublisherConnectionClose p = new ActiveMQPublisherConnectionClose(url, queue);
					
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					
					for (Object item : items)
					{
						FileItem file = (FileItem) item;
						if (file.getSize() > 0)
						{
							byte[] data = new byte[(int)file.getSize()];
							file.getInputStream().read(data);
							
							EslFirmwareXmlCreator efc = new EslFirmwareXmlCreator();
							ByteArrayOutputStream b = new ByteArrayOutputStream();
							PrintStream out = new PrintStream(b);
							
							efc.writeXml(file.getInputStream(), out, msgid);
							
							p.publish (b.toByteArray());
						}
					}
				}								
				else 
				{																
					throw new Exception ("Invalid file upload path: " + path);
				}
				
				if (request.getParameter("destinationPage") != null 
						&& request.getParameter("destinationPage").length() > 0) 
				{
					response.sendRedirect(request.getParameter("destinationPage"));
				}
				else
				{
					String rdata = fmt.formatResponse("response", "result=accepted", rct, false);
					response.setContentType(rct);
					response.setContentLength((int) rdata.length());
					OutputStream out = response.getOutputStream();
					out.write(rdata.getBytes());
					out.flush();
					out.close();
				}
											
			}						
			else if (path.startsWith("/priceupdate"))
			{							
				int n = request.getContentLength();
				if (n <= 0) throw new Exception ("cannot create payload without posting data");
				
				Properties payloadProperties = new Properties();
				payloadProperties.load(request.getInputStream());
				
				String esl = payloadProperties.getProperty("esl");
				if (esl != null) new Exception ("esl not specified in posted data");
				
				LCDPayload payloadCalculator = new LCDPayload();
				byte [] payloadData = payloadCalculator.createPrice(
						payloadProperties, payloadProperties.getProperty("activationTime"));
				byte [] hash = payloadCalculator.calculateEslHash(payloadData);
				StringBuffer xmlData = new StringBuffer();
				
				synchronized (msgId) {
					xmlData = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>\n")
							.append("<message msgId=\"").append(msgId).append("\" msgCommand=\"EslPriceUpdate\" ") 
							.append("xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" ")
							.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://s5tech.com/network\">\n")
							.append("<eslList>\n")
							.append("\t<mac>").append(esl).append("</mac>\n")
							.append("</eslList>\n")
							.append("<eslPriceData activationTime=\"")
							.append(payloadProperties.getProperty ("activationTime"))
							.append("\" hashCode=\"")
							.append(payloadCalculator.getLowEndianUInt(hash, 0, 4))
							.append("\">").append('\n')
							.append(Base64Encoder.encode(payloadData)).append('\n')
							.append("</eslPriceData>").append('\n')
							.append("</message>\n");
					msgId ++;
				}	
				
				String url = System.getProperty ("server.url", "vm://S5NET");
				String queue = System.getProperty ("server.downqueue", "dynamicQueues/DOWN");
				ActiveMQPublisherConnectionClose p = new ActiveMQPublisherConnectionClose(url, queue);
				p.publish (xmlData.toString());							
											
				String rdata = fmt.formatResponse("response", "result=accepted", rct, false);
				response.setContentType(rct);
				response.setContentLength((int) rdata.length());
				OutputStream out = response.getOutputStream();
				out.write(rdata.getBytes());
				out.flush();
				out.close();
				
			}
			else if (path.startsWith("/publish"))
			{							
				int n = request.getContentLength();
				if (n <= 0) throw new Exception ("cannot create payload without posting data");
				
				byte [] data = new byte[n];
				
				request.getInputStream().read(data);
				
				String url = System.getProperty ("server.url", "vm://S5NET");
				String queue = System.getProperty ("server.downqueue", "dynamicQueues/DOWN");
				ActiveMQPublisherConnectionClose p = new ActiveMQPublisherConnectionClose(url, queue);
				
				p.publish (data);
				
				String rdata = fmt.formatResponse("response", "result=accepted", rct, false);
				response.setContentType(rct);
				response.setContentLength((int) rdata.length());
				OutputStream out = response.getOutputStream();
				out.write(rdata.getBytes());
				out.flush();
				out.close();
				
			}
			else if (path.startsWith("/config"))
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
				
				String action = (path.length() > 8 ? path.substring(8) : "get");
				
				//System.err.println(action);
				
				if (action.equals("put"))
				{
					boolean restart = false;
					int n = request.getContentLength();
					if (n <= 0) throw new Exception ("error: no data posted! requested path: " + path);		
					
					String hh = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new Date());
					
					
					File d = new File("./conf");
					if (!d.exists()) d.mkdirs();
					
					String u = "Anonymous";
					String ub = "N/A";
					
					Enumeration hit = request.getHeaderNames();
					
					while (hit.hasMoreElements()) {
						String k = hit.nextElement().toString();
						if (k.equals("Authorization")) {
							u = request.getHeader(k);
							if (u.indexOf("Basic ") == 0) {
								u = new String (
										Base64Decoder.decode(u.substring("Basic ".length())), 
										(request.getCharacterEncoding() != null ? request.getCharacterEncoding() : "UTF-8"));
								int _kl = u.indexOf(':');
								if (_kl > 0) u = u.substring(0, _kl);
							}
						}
						else if (k.equals("User-Agent")) {
							ub = request.getHeader(k);
						}
						//System.err.println(" *** " + k + " :==> " + request.getHeader(k));
					}					
					
					if (log.isTraceEnabled()) {
						log.trace("uploading configuration from " + u + "@" + request.getRemoteHost() + " ...");
					}
					
					// TODO: 
					// e = new Encoder()
					// printstream su byte array, poi passarlo come input stream 
					// e new FileOutputStream(system.properties) a e.run(in, out, true)
					//
												
					//System.err.println("<!-- BEGIN CONFIGURATION -->");
					Properties config = new Properties();
					for (Object ok : System.getProperties().keySet()) {
						config.put(ok.toString(), System.getProperty(ok.toString()));						
					}
					
					for ( ;;) 
					{
						String line = in.readLine();
						if (line == null) break;
						String [] fields = URLDecoder.decode(line, "UTF-8").split("&");
						
						for (String field: fields) {
							
							int k = field.indexOf('=');
							if (k < 0) continue;
							
							String key = field.substring(0, k).trim();
							String value = field.substring(k + 1).trim();
							
							if (value.equalsIgnoreCase("on")) value = "true";
							else if (value.equalsIgnoreCase("yes")) value = "true";
							else if (value.equalsIgnoreCase("true")) value = "true";
							else if (value.equals("off")) value = "false";
							else if (value.equalsIgnoreCase("no")) value = "false";
							else if (value.equalsIgnoreCase("false")) value = "false";
																
							//System.err.println (key  + " --> " + value);	
							
							if (key.equals("submit.config")) {
								File fconf = new File(System.getProperty ("config", "./conf/s5.conf"));
								if (fconf.exists()) {
									try {				
										log.warn("system configuration changed by " + u +
												" @" + request.getRemoteAddr() + "! ... backing up old system configuration from " 
												+ fconf.getAbsolutePath() + " to " + "./conf/s5.conf.bak." + hh);
										fconf.renameTo(new File("./conf/s5.conf.bak." + hh));
									}
									catch(Throwable t) {
										t.printStackTrace();
									}
								}
							}
							
							if (key.equals("config.restart")) {
								restart = value.equalsIgnoreCase("true");
								if (restart) {
									log.warn("requested restart by " + u + "@" + request.getRemoteHost());												
								}
							}
							else {
								value = value.trim();
								config.put(key, value);								
							}										
						}
					}
					
					in.close();
					
					PrintStream save = new PrintStream("./conf/system.properties");
					
					save.println("# ///////////////////////////////////////////////////////////");
					save.println("# ");
					save.println("# CONFIGURATION CREATED BY WEB CONSOLE ");
					save.println("# User: " + u);
					save.println("# Agnt: " + ub);
					save.println("# Host: " + request.getRemoteAddr());
					save.println("# Time: " + hh.replace('T', ' '));
					save.println("# ");
					save.println("# ///////////////////////////////////////////////////////////");
					save.println();
					
					for (Object okey : config.keySet()) {
						String key = okey.toString();
						if (key.startsWith("java.")
								|| key.startsWith("sun.")
								|| key.startsWith("user.")
								|| key.startsWith("awt.")
								|| key.startsWith("file.")
								|| key.startsWith("os.")
								|| key.startsWith("path.")
								|| key.startsWith("submit.")
								|| key.equals("line.separator")) continue;
						
						String value = config.getProperty(key);
						if (value.length() > 0) {
							save.print(key);
							save.print(" = ");
							save.println (value
									.replaceAll("\\$\\{amp\\}", "\\&")
									.replaceAll("\\$\\{bsl\\}", "\\\\\\\\")
									.replaceAll("\\$\\{smc\\}", ";").trim());
						}
						else if (key.equals("server.connector")) {
							save.print(key);
							save.println(" =");
						}
					}
					
					if (save != null) {
						save.close();
					}
					
					//System.err.println("<!-- END CONFIGURATION -->");
					
					//String rdata = fmt.formatResponse("response", "result=accepted", rct);
					//response.setContentType(rct);
					//response.setContentLength((int) rdata.length());
					//OutputStream out = response.getOutputStream();
					//out.write(rdata.getBytes());
					//out.flush();
					//out.close();
					response.sendRedirect(adminPath + "/files/config.html");
					
//						if (restart) {
//							
//							try {
//
//								// java binary
//								String java = System.getProperty ("java.home") + "/bin/java";
//
//								// vm arguments
//								List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
//								StringBuffer vmArgsOneLine = new StringBuffer();
//								for (String arg : vmArguments) {
//
//									// if it's the agent argument : we ignore it otherwise the
//									// address of the old application and the new one will be in conflict
//									if (!arg.contains("-agentlib")) {
//										vmArgsOneLine.append(arg);
//										vmArgsOneLine.append(" ");
//									}
//
//								}
//								
//								// init the command to execute, add the vm args
//								final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
//								
//								// program main and program arguments
//								String[] mainCommand = System.getProperty ("sun.java.command").split(" ");
//								
//								// program main is a jar
//								if (mainCommand[0].endsWith(".jar")) {
//									// if it's a jar, add -jar mainJar
//									cmd.append("-jar " + new File(mainCommand[0]).getPath());
//								} 
//								else 
//								{
//									// else it's a .class, add the classpath and mainClass
//									cmd.append("-cp \"" + System.getProperty ("java.class.path") + "\" " + mainCommand[0]);
//								}
//								
//								for (int i = 1; i < mainCommand.length; i++) {
//									cmd.append(" ");
//									cmd.append(mainCommand[i]);
//								}
//
//								log.warn ("restarting system...: command > {}", cmd.toString());
//
//								/*
//								// execute the command in a shutdown hook, to be sure that all the
//								// resources have been disposed before restarting the application
//								Runtime.getRuntime().addShutdownHook(new Thread() {
//									@Override
//									public void run() {
//										try {
//											Runtime.getRuntime().exec(cmd.toString());
//										} catch (IOException e) {
//											e.printStackTrace();
//										}
//									}
//								});
//								
//								// execute some custom code before restarting
//								//
//								// TODO: se non intercetta l'hook di spegnimento, inserire qui le procedure ....
//								//
//								
//								// exit
//								System.exit(0);
//								*/
//								
//							} 
//							catch (Exception e) {
//
//								// something went wrong
//								throw new IOException("Error while trying to restart the application", e);
//
//							}
//							
//						}
					
				}
				else if (action.equals("get"))
				{
					File f = new File("./conf/system.properties");
					
					if (f.exists()) 
					{									
						StringBuffer buff = new StringBuffer();
						
						InputStream pin = null;
						
						Encoder e = new Encoder();
						
						if (e.isEncoded(f.getAbsolutePath())) {
							FileInputStream _in = new FileInputStream(f);
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							e.run(_in, out, false);
							pin = new ByteArrayInputStream(out.toByteArray());
							out.close();										
						}
						else {
							pin = new FileInputStream(f);
						}
						
						Properties p = new Properties();
						p.load(pin);
						
						try {
							long l1 = f.lastModified();
							long l2 = ManagementFactory.getRuntimeMXBean().getStartTime();
							p.put("isnewconfig", String.valueOf((l1 > l2)));
						}
						catch (Throwable t) {
							p.put("isnewconfig", "unavailable");
						}
						
						for (Object ok : p.keySet()) {
							buff.append(ok.toString())
									.append("=")
									.append(p.getProperty(ok.toString()).replace(';', '|'))
									.append(";");										
						}
						
						String data = fmt.formatResponse("configuration", buff.toString(), rct, false);
						response.setContentLength((int) data.length());
						response.setContentType(rct);
						OutputStream out = response.getOutputStream();
						out.write(data.getBytes());
						out.flush();
						out.close();									
					}
					else 
					{
						String data = fmt.formatResponse("configuration", netsrv.getParameters(), rct, false);
						response.setContentLength((int) data.length());
						response.setContentType(rct);
						OutputStream out = response.getOutputStream();
						out.write(data.getBytes());
						out.flush();
						out.close();
					}	
					
				}
				else if (action.equals("jdbctest"))
				{
					String jdbcDriver = null;
					String jdbcUrl = null;
					String username = null;
					String password = null;
					
					int n = request.getContentLength();
					if (n <= 0) throw new Exception ("error: no data posted! requested path: " + path);		
					
					for ( ;;) 
					{
						String line = in.readLine();
						if (line == null) break;
						String [] fields = URLDecoder.decode(line, "UTF-8").split("&");
						
						for (String field: fields) {
							
							int k = field.indexOf('=');
							if (k < 0) continue;
							
							String key = field.substring(0, k).trim();
							String value = field.substring(k + 1)
									.replaceAll("\\$\\{amp\\}", "\\&")
									.replaceAll("\\$\\{bsl\\}", "\\\\").trim()
									.replaceAll("\\$\\{smc\\}", ";").trim();
							
							if (key.equalsIgnoreCase("jdbc.driver")) {
								jdbcDriver = value;
							}
							else if (key.equalsIgnoreCase("jdbc.url")) {
								jdbcUrl = value;
							}
							else if (key.equalsIgnoreCase("jdbc.username")) {
								username = (value.equalsIgnoreCase("null") ? "" : value);
							}
							else if (key.equalsIgnoreCase("jdbc.password")) {
								password = (value.equalsIgnoreCase("null") ? "" : value);
							}
							
						}
						
					}
					
					if (log.isTraceEnabled()) {
						log.trace("testing jdbc connection from host " + request.getRemoteHost() + ": "
								+ "driver = " + jdbcDriver 
								+ ", url = " + jdbcUrl 
								+ ", username = " + username
								+ ", password = *****");
					}
					
					String testResult = null;
					Connection conn = null;
													
					try {
						Class.forName (jdbcDriver);	
						if (username == null || username.length() == 0) {
							conn = DriverManager.getConnection (jdbcUrl.trim());
						}
						else {
							conn = DriverManager.getConnection (jdbcUrl.trim(), username.trim(), (password == null ? "" : password.trim()));
						}
						testResult = "ok";
					}
					catch (Throwable exx) {
						log.error("test jdbc connection error ... {}", exx);
						testResult = "error: " + exx.getMessage();
					}
					finally {
						if (conn != null) try { conn.close();} catch (Throwable ignored) {}
					}
					
					String rdata = fmt.formatResponse("response", "result=" + testResult, rct, false);
					response.setContentType(rct);
					response.setContentLength((int) rdata.length());
					OutputStream out = response.getOutputStream();
					out.write(rdata.getBytes());
					out.flush();
					out.close();
					
				}
				else 
				{
					log.error("invalid requested path: " + path);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}							
				
			}
			else if (path.startsWith("/sqlquery/"))
			{
				Connection connection = null;
				Statement st = null;
				ResultSet rs = null;	
				
				String data = null;
				
				try 
				{
					StringBuffer b = new StringBuffer();				
					String sql = path.substring("/sqlquery/".length());
					connection = ConnectionFactory.getInstance().createConnection();
					st = connection.createStatement();
					rs = st.executeQuery(sql);
					
					if (log.isDebugEnabled())
						log.debug("controller executing sql query > " + sql);
					
					ResultSetMetaData md = rs.getMetaData();
					int n = md.getColumnCount();
													
					while (rs.next())
					{	
						for (int i = 1; i <= n; i ++) 
						{				
							String name = md.getColumnName(i);
							
							if (name == null) name = "column" + i;
							if (name.trim().length() == 0) name = "column" + i;
							
							b.append(name);
							b.append("=");
							
							String value = null;
							
							try {
							
								switch (md.getColumnType(i))
								{
									case Types.NULL:
										value = "null";
										break;
									
									case Types.VARCHAR:
									case Types.CHAR:
										value = rs.getString(i);
										break;
										
									case Types.LONGNVARCHAR:
									case Types.NCHAR:
									case Types.NVARCHAR:
										value = rs.getNString(i);
										break;
																														
									case Types.BIGINT:
										value = String.valueOf(rs.getLong(i)).trim();
										break;
										
									case Types.INTEGER:
										value = String.valueOf(rs.getInt(i)).trim();
										break;
										
									case Types.SMALLINT:
									case Types.TINYINT:
										value = String.valueOf(rs.getShort(i)).trim();
										break;
								
									case Types.DECIMAL:
										value = rs.getBigDecimal(i).toString();
										break;
										
									case Types.DOUBLE:
										value = String.valueOf(rs.getDouble(i));
										break;
									case Types.FLOAT:
										value = String.valueOf(rs.getFloat(i));
										break;
									case Types.NUMERIC:
									case Types.REAL:
										value = String.valueOf(rs.getFloat(i));
										break;
										
									case Types.BIT:
									case Types.BOOLEAN:
										value = String.valueOf(rs.getBoolean(i));
										break;
									
									case Types.DATE:
										value = rs.getDate(i).toString();
										break;
										
									case Types.TIME:
										value = rs.getTime(i).toString();
										break;
										
									case Types.TIMESTAMP:
										value = rs.getTimestamp(i).toString();
										break;										
										
									case Types.BLOB:
									case Types.BINARY:
									case Types.CLOB:
									case Types.NCLOB:
									case Types.VARBINARY:
									case Types.LONGVARBINARY:
										value = new StringBuffer().append(Base64Encoder.encode(rs.getBytes(i))).toString();
										break;
										
									case Types.ARRAY:
									case Types.DATALINK:
									case Types.DISTINCT:
									case Types.JAVA_OBJECT:
									case Types.OTHER:
									case Types.REF:
									case Types.ROWID:
									case Types.SQLXML:
									case Types.STRUCT:	
										value = rs.getObject(i).toString();
										break;
									
									default:
										value = "N/A";										
								}
							}
							catch(Throwable tt) {
								
								value = "error";
								
							}
							
							if (value == null) value = "N/P";
							
							value = value.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').replace(';', ' ').trim();
							b.append(value).append(';');
							
						}
						
						b.append("\n");
						
					}	
					
					data = fmt.formatResponse("result", b.toString().split("\n"), rct, false);								
					
				}
				catch(Throwable t)
				{								
					t.printStackTrace();
				}
				finally 
				{
					if (rs != null) try { rs.close(); } catch(Throwable sqle) {};				
					if (st != null) try { st.close(); } catch(Throwable sqle) {};				
					if (connection != null) try { connection.close(); } catch(Throwable sqle) {};				
				}			
											
				if (data != null)
				{
					response.setContentLength((int) data.length());
					response.setContentType(rct);
					OutputStream out = response.getOutputStream();
					out.write(data.getBytes());
					out.flush();
					out.close();
				}
				else 
				{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
				
			}
			else if (path.startsWith("/logger/"))
			{	
				String contentType = null;
				
				if (path.startsWith("/logger/config"))
				{
					String fname = System.getProperty(
							"logback.configurationFile", 
							"file:conf/logback-s5tech.xml");
					
					if (fname.indexOf(':') > 0) {
						fname = fname.substring(fname.indexOf(':') + 1);
					}
					
					File cfgfile = new File(fname);
					
					int filelen = (int) cfgfile.length();
					byte[] data = new byte[filelen];
					
					FileInputStream in = new FileInputStream(cfgfile);
					in.read(data);
					in.close();
													
					response.setContentLength(filelen);
					response.setContentType(rct);
					
					OutputStream out = response.getOutputStream();
					out.write(data);
					out.flush();
					out.close();
					
				}
				else 
				{
					String from = (request.getParameter("from") != null && request.getParameter("from").length() > 0 ?
							request.getParameter("from") : null);
					String xmlschema = (request.getParameter("xmlschema") != null && request.getParameter("xmlschema").length() > 0 ?
							request.getParameter("xmlschema") : "./xml/schema.xsd");
					String logdir = (request.getParameter("logdir") != null && request.getParameter("logdir").length() > 0 ?
							request.getParameter("logdir") : System.getProperty ("log.dir", "./logs"));							
					String filter = (request.getParameter("filter") != null && request.getParameter("filter").length() > 0 ?
							request.getParameter("filter") : null);	
					
					try {
						
						DateFormat timef = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						
						ByteArrayOutputStream stream = new ByteArrayOutputStream();	
					
						String fileprefix = "";
						String fileext = ".txt";
						
						Date datefrom = null;
						Date dateto = new Date();
						
						if (from != null) 
						{
							datefrom = timef.parse(from);
							fileprefix = "-" + timef.format(datefrom)
									.replace(':', 'x')
									.replace('-', 'x')
									.replace('T', 'x')
									.replaceAll("x", "");
						}
							
						if (path.startsWith("/logger/download"))
						{
							fileprefix = "applicationlog" + fileprefix;
							fileext = ".zip";
							contentType = "application/zip";
							ZipLog parser = new ZipLog();
							if (datefrom != null)  parser.setFrom(datefrom);
							parser.setOut(stream);
							parser.setLogdir(logdir);
							parser.run();
							if (stream != null) try { stream.close(); } catch (Throwable ignored) {}
						}
						else if (path.startsWith("/logger/event"))
						{
							fileprefix = "eslevents" + fileprefix;
							fileext = ".txt";
							contentType = "text/plain";
							ApplicationLogEventsDumper parser = new ApplicationLogEventsDumper();
							if (datefrom != null)  parser.setFrom(datefrom);
							if (filter != null) parser.setFilter(filter);
							PrintStream o = (stream != null ? new PrintStream(stream) : null); 
							parser.setOut(o);
							parser.setLogDir(logdir);
							parser.run();
							if (o != null) try { o.close(); } catch (Throwable ignored) {}	
						}
						else if (path.startsWith("/logger/validate") || path.startsWith("/logger/xml"))
						{
							fileprefix = "xmlvalidation" + fileprefix;
							fileext = ".txt";
							contentType = "text/plain";
							XmlLogValidator parser = new XmlLogValidator();
							parser.setSchemafile(xmlschema);
							PrintStream o = (stream != null ? new PrintStream(stream) : null);
							parser.setOut(o);
							parser.setLogdir(logdir);
							parser.run();
							if (o != null) try { o.close(); } catch (Throwable ignored) {}	
						}
						else if (path.startsWith("/logger/unauth"))
						{
							fileprefix = "unauthorized-esllist" + fileprefix;
							fileext = ".txt";				
							contentType = "text/plain";
							UnauthorizedEslsParser parser = new UnauthorizedEslsParser();
							if (datefrom != null)  parser.setFrom(datefrom);
							PrintStream o = (stream != null ? new PrintStream(stream) : null); 
							parser.setOut(o);
							parser.setLogdir(logdir);
							parser.run();
							if (o != null) try { o.close(); } catch (Throwable ignored) {}	
						}	
						else if (path.startsWith("/logger/timelimit"))
						{
							fileprefix = null;									
							String logtype = request.getParameter("logtype");
							if (logtype == null || logtype.length() == 0) logtype = "application";	
							if (logtype.startsWith("xml")) logtype = "xmlserver";
							if (!logtype.equals("application") && !logtype.equals("xmlserver"))
								throw new Exception("invalid log type specified: " + logtype + " - " + path);									
							stream.write(fmt.formatResponse("response", 
									timef.format(ALogFileFinder.getFirstDateEntry(logdir, logtype, ".log")), 
									rct, false).getBytes());									
						}
						else
						{
							throw new Exception("invalid /logger/ action path: " + path);
						}
						
						byte []b = stream.toByteArray();							
						
						response.setContentType(contentType);
						response.setContentLength((int) b.length);
						
						if (fileprefix != null)
						{
							response.setHeader( "Content-Disposition", "attachment;filename=" 
										+ fileprefix + "-"
										+ timef.format(dateto) 
											.replace(':', 'x')
											.replace('-', 'x')
											.replace('T', 'x')
											.replaceAll("x", "") 
										+ fileext);
						}
						
						OutputStream out = response.getOutputStream();
						out.write(b);
						out.flush();
						out.close();
						
					}
					catch (Throwable e) 
					{
						log.error("error requesting path {} > {}", path, e);								
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					
				}
				
			}
			else 
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			
		}					
		catch (Throwable t)
		{
			log.error("HTTP server error: {}\n{}", t.getMessage(), t);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);						
		}
		finally 
		{
			((Request)request).setHandled(true);
		}
		
	}
	
}
