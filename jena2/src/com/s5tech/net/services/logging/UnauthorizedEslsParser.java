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
import java.util.Iterator;
import java.util.Vector;


public class UnauthorizedEslsParser 
		extends ALogFileFinder implements Runnable {
	
	private static final String SEARCHSTR = "ESL not authorized";	
	
	private String logDir = "./logs";
	
	private PrintStream out = null;
	
	private Date scanFrom = null;
	
	private Date lastJoinTime = null;
		
	public Date getLastJoinTime() {
		return lastJoinTime;
	}

	public Date getScanFrom() {
		return scanFrom;
	}

	public void setFrom(Date scanFrom) {
		this.scanFrom = scanFrom;
	}

	public String getLogdir() {
		return logDir;
	}

	public void setLogdir(String logdir) {
		this.logDir = logdir;
	}

	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public void run() {
		
		String [] files = getOrderedLogFilesList(logDir, "application", ".log");
		
		Vector<String> unauthorizedEslsList = null;
				
		try {
		
			unauthorizedEslsList = new Vector<String>();
						
			for (int i = 0; i < files.length; i ++) {
				
				BufferedReader in = null;
				
				try {

					in = new BufferedReader(new FileReader(files[i]));
					
					int linecount = 0;
															
					for ( ;; ) {
						
						String txt = in.readLine();
						if (txt == null) break;
						
						linecount ++;
						
						try 
						{
							if (txt.indexOf(SEARCHSTR) > 0) {	
								
								String [] t = txt.split("[ )]"); 
								
								Date joinTime = timef.parse(t[0]);								
								if (scanFrom != null && !scanFrom.before(joinTime)) continue;
								lastJoinTime = joinTime;
		
								String eslmac = t[12].trim();
								if (eslmac.equals("0000000000000000")) continue;
								if (eslmac.toLowerCase().indexOf("limit") >= 0) continue;
								
								if (!unauthorizedEslsList.contains(eslmac)) unauthorizedEslsList.add(eslmac);
		
							}
						}
						catch (Throwable t) {
							
							System.err.println("cannot parse line " + linecount + ", reason: ");
							t.printStackTrace();
							
						}
						
					}	
					
					System.err.println("parsed " + linecount +  " lines");
					
				}
				catch (Throwable t) {
					
					t.printStackTrace();
					
				}
				finally {
					
					if (in != null) try { in.close(); } catch (Throwable t) {}
					
				}
				
			}
			
			System.err.println("collected " + unauthorizedEslsList.size() + " esls unauthorized");
			
			Iterator<String> it = unauthorizedEslsList.iterator();
			while (it.hasNext())
			{
				out.println(it.next());										
			}			
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			
		}
		finally {
			
		}
		
	}
	
}

