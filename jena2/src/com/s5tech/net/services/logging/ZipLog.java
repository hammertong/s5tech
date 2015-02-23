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
import java.io.OutputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipLog extends ALogFileFinder {
	
	private String logDir = "./logs";
	
	private OutputStream out = System.out;
	
	private Date from = null;
		
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date scanFrom) {
		this.from = scanFrom;
	}

	public String getLogdir() {
		return logDir;
	}

	public void setLogdir(String logdir) {
		this.logDir = logdir;
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}
	
	public void run()
	{
		boolean timeok = false;
		
		ZipOutputStream zout = null;
		
		try 
		{		
			zout = new ZipOutputStream(out);		
			
			ZipEntry entry = new ZipEntry("application.log");
			zout.putNextEntry(entry);
			
			for (String f : getOrderedLogFilesList(logDir, "application", ".log")) {
				
				BufferedReader in = null;
				
				try {
					
					in = new BufferedReader(new FileReader(f));
					
					for ( ;; ) 
					{
						String txt = in.readLine();
						
						if (txt == null) break;
						
						try 
						{
							String [] t = txt.split("[ )]"); 
							Date entryTime = timef.parse(t[0]);								
							if (from != null && !from.before(entryTime)) continue;
							if (!timeok) timeok = true;
						}
						catch (Throwable ignored) { /* may be stacktrace ... ? */ }	
						
						if (timeok) {
							byte[] d = txt.getBytes();
							zout.write(d, 0, d.length);
						}
					}	
					
				}
				catch (Throwable t) {
					
					t.printStackTrace();
					
				}
				finally {
					
					if (in != null) try { in.close(); } catch (Throwable t) {}
					
				}
			}		
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			
		}
		finally 
		{
			
			if (zout != null) {
				try { zout.closeEntry(); } catch (Throwable ignored) {}
				try { zout.close(); } catch (Throwable ignored) {}
			}

			if (out != null) try { out.close(); } catch (Throwable ignored) {}
						
		}
		
	}
	
}

