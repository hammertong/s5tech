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
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


public abstract class ALogFileFinder {

	protected static DateFormat timef = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	protected static String [] getOrderedLogFilesList(String logDir, String prefix, String suffix)
	{
		Vector<String> fList = new Vector<String>();
		String fname;
		
		if (!logDir.endsWith("/")) logDir = logDir + "/";
		
		String bklogDir = logDir;
		
		File bf = new File(logDir + "backup");
		if (bf.exists() && bf.isDirectory()) bklogDir = logDir + "backup/";
			
		
		int i = 0;
		for ( ;; ) {
			if (i == 0) {
				fname = logDir + prefix + suffix;
			}
			else {
				fname = bklogDir + prefix + "." + i + suffix;
			}
			if (!(new File(fname).exists())) break;
			fList.add(i, fname);
			i = i + 1;
		}
		
		String [] ret = new String [fList.size()];
		
		for (i = 0; i < fList.size(); i ++) {
			ret[i] = fList.elementAt(fList.size() - i - 1);
		}
		
		return ret;
		
	}
	
	public static Date getFirstDateEntry(String logDir, String prefix, String suffix)
	{
		try {
			String [] l = getOrderedLogFilesList(logDir, prefix, suffix);
			String filename = l[0];
			BufferedReader in = new BufferedReader(new FileReader(filename));
			for ( ;;) {
				String txt = in.readLine();
				if (txt == null) break;
				String [] t = txt.split("[ )]"); 
				try {
					Date d = timef.parse(t[0]);
					return d;
				}
				catch (Throwable ignored) {}
			}
			in.close();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	public static void fmtprint (PrintStream out, Object value, int digits, boolean isRightAligned)
	{
		StringBuffer b = new StringBuffer(value == null ? "null" : value.toString());
		if (digits > b.length())
		{
			while (b.length() < digits)
			{
				if (isRightAligned)
				{
					b.insert(0, ' ');
				}
				else 
				{
					b.append(' ');
				}
			}
			out.print(b.toString());
		}
		else if (digits < b.length())
		{
			if (isRightAligned)
			{
				out.print(b.toString().substring(b.length() - digits));
			}
			else 
			{
				out.print(b.toString().substring(0, digits));
			}
		}
		
	}
	
	public static String padleft(int value, int padNum, char padChar)
	{
		String s = String.valueOf(value);
		while (s.length() < padNum) s = padChar + s;
		return s;
	}
	
	public static String padleft(String value, int padNum, char padChar)
	{
		String s = (value == null ? "" : value);
		while (s.length() < padNum) s = padChar + s;
		return s;
	}
	
}
