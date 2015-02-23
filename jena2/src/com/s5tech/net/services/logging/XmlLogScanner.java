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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import org.castor.core.util.Base64Decoder;

import com.s5tech.net.services.display.LCDPayload;

public class XmlLogScanner extends ALogFileFinder {
	
	private String logdir = "./logs";
	
	private PrintStream out = System.out;

	private Date from = null;	

	private String findParam(String key, String message) {
		String value = null;
		try {			
			int n = message.indexOf(key + "=\"");
			if (n > 0) {
				n += key.length();
				n += 2;
				value = message.substring(n, message.indexOf('"', n));				
//System.err.println("-------------> " + message.substring(n, message.indexOf('"', n)));
//System.err.println("-----------> " + message.substring(n, message.indexOf('"', n - 1)));
//System.err.println("---------> " + message.substring(n, message.indexOf('"', n - 2)));				
				return value.trim();
			}
			else {
				n = message.indexOf("<" + key + ">");
				if (n > 0) {
					n += key.length();
					n += 2;
					value = message.substring(n, message.indexOf('<', n));
					return value.trim();
				}			
				else {
					n = message.indexOf("<" + key + " ");
					if (n > 0) {
						n = message.indexOf(">", n + 1);						
						value = message.substring(n, message.indexOf('<', n));
						return value.trim().replace('\t', ' ').replace('\r', ' ').replace('\n', ' ').trim();
					}	
				}
			}
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
		
	
	//private HashMap<String, String> esldic = new HashMap<String, String>();
	
	private String fmt(String o, int len) {
		if (o == null) o = "";
		while (o.length() < len) {
			o = " " + o;
		}
		if (o.length() > len) o = o.substring(o.length() - len);
		return o;
	}
	
	String filterMac = null;
	String filterCC = null;
	
		
	public String getFilterMac() {
		return filterMac;
	}


	public void setFilterMac(String filterMac) {
		if (filterMac == null) return;
		this.filterMac = filterMac.toLowerCase();
	}


	public String getFilterCC() {
		return filterCC;
	}


	public void setFilterCC(String filterCC) {
		if (filterCC == null) return;
		this.filterCC = filterCC.toLowerCase();
	}
	
	public void setFilter(String criteria) {
		
		if (criteria == null) return;
		
		String[] px = null;
		
		if (criteria.indexOf(',') >= 0) {
			px = criteria.split(",");
		}
		else if (criteria.indexOf(';') >= 0) {
			px = criteria.split(";");
		}
		else {
			px = new String[] { criteria };			
		}
		
		if (px == null) return;
		
		for (String p : px) {
			String [] tk = p.split("=");			
			if (tk.length < 2) continue;
			if (tk[0].equalsIgnoreCase("mac")) setFilterMac(tk[1]);
			else if (tk[0].equalsIgnoreCase("esl")) setFilterMac(tk[1]);
			else if (tk[0].equalsIgnoreCase("coordinator")) setFilterCC(tk[1]);			
		}	
	}


	private String dump(String xml, boolean isUp)
	{		
		try {
			String cmd = findParam("msgCommand", xml);
			if (cmd == null) throw new Exception();
			if (!cmd.equals("EslPriceUpdate") && !cmd.equals("EslStatus")) return null;
			
			String esl = findParam("mac", xml);
			
			if (filterMac != null && esl.toLowerCase().indexOf(filterMac) < 0) return null; 
			
			if (cmd.equals("EslStatus")) {
				
				//String batteryLevels = esldic.get(esl);
				//if (batteryLevels == null) batteryLevels = "";
				//if (batteryLevels.length() > 0) batteryLevels = " " + batteryLevels;
				//batteryLevels = findParam("batteryLevel", xml) + batteryLevels;				
				//esldic.put(esl,  batteryLevels);
				
				String cc = findParam("macAssociatedCoordinator", xml);
				if (filterCC != null && cc.toLowerCase().indexOf(filterCC) < 0) return null;
				
				int n = 0;
			    int sumcc = 0;
			    for ( ;; ) {
			    	n = xml.indexOf("<coordinator ", n);
			    	if (n < 0) break;
			    	n ++;
			    	sumcc ++;			    	
			    }
				
			    String fwver = findParam("firmwareVersion", xml);
			    while (fwver.length() < 10) {fwver += " "; }
			    
				return "status " + esl + 
						" @" + cc + " " +
						fwver + " " +
						"batl " + fmt(findParam("batteryLevel", xml), 3) + " " +				
						"txpw " + fmt(findParam("txPower", xml), 3) + " " +						
						"temp " + fmt(findParam("temperature", xml), 3) + " " +						
						"life " + fmt(findParam("lifetimeHours", xml), 3) + " " +
						"type " + fmt(findParam("deviceType", xml), 3) + " " +
						"chan " + findParam("channel", xml) + " " +
						"@alt " + sumcc;						
				
			}
			else {
				if (isUp) {
					return "acknowledge " + esl + " " + findParam("result", xml);// + " batteryLevel: " + esldic.get(esl) + " "					
				}
				else {
					String price = findParam("eslPriceData", xml);
					LCDPayload p = new LCDPayload();
					Properties lp = p.parsePrice(Base64Decoder.decode(price));				
					price = lp.getProperty("bigprice");
					return "priceupdate " + esl + " " + price;// + " batteryLevel: " + esldic.get(esl) + " " 
							//+ (price.startsWith(esldic.get(esl).split(" ")[0] + ".") ? "OK" : "ERROR!!!");
				}
			}							
			
		}
		catch (Throwable exx) {
			exx.printStackTrace();
			return null;
		}						
	}
	
	public void run()
	{
		int count = 0;
        int errors = 0;
        
		try 
		{		
	        
	        for (String f : getOrderedLogFilesList(logdir, "xmlserver", ".log"))
	        {
	        	BufferedReader reader = new BufferedReader(new FileReader(f));
	        
	        	String line = null;
	        	
	        	for (;;) 
	        	{
	        		String desc;
	        		
		        	if (line == null) {
		        		line = reader.readLine();		    		        	
		        	}
		        	
		        	if (line == null) break;
		        	
		        	boolean isUp = false;
		        	if (line.indexOf("[XMLSERVER.FROM]") >= 0) {
		        		desc = "SENT > ";
		        	}
		        	else if (line.indexOf("[XMLSERVER.TO]") >= 0) {
		        		desc = "RECV < ";
		        		isUp = true;
		        	}
		        	else {
		        		continue;
		        	}
		        	
		        	String time = line.split(" ")[0];		
		        	if (from != null && timef.parse(time).before(from)) continue;
		        	
		        	StringBuffer w = new StringBuffer();
		        	
		        	for (;; ) {
		        		line = reader.readLine();
		        		if (line == null) break;
		        		if (line.indexOf("[XMLSERVER.FROM]") >= 0 || line.indexOf("[XMLSERVER.TO]") >= 0)
			        		break;
		        		w.append(line).append('\n');
		        	}
		        	
		        	try {
			        	count ++;	
			        	
			        	//out.println("'" + w.toString().replace('\n', ' ').replace('\r',  ' ').trim() + "'");
			            String msg = dump(w.toString(), isUp);
			            if (msg != null) out.println(time + " " + desc + msg);
			        }
			        catch (Exception e) {
			        	errors ++;
			        	e.printStackTrace();
			        }
		        			        	
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
