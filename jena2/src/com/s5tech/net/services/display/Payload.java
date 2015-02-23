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
 
package com.s5tech.net.services.display;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import org.castor.core.util.Base64Decoder;

public class Payload {

	public static void exec(String[] args) 
	{
		String activationTime = null;
		String epaperFile = null;
		String decodeData = null;
		String outformat = null;
		Properties props = null;
		
		int x = 0;
		int y = 0;
		int pages = 0;
		
		String hashscanfile = null;
		
		try {
			
			for (int i = 0; i < args.length; i ++) 
			{				
				if (args[i].equals("-a"))
				{
					activationTime = args[++i];
				}
				else if (args[i].equals("-e"))
				{
					epaperFile = args[++i];
				}
				else if (args[i].equals("-p"))
				{
					if (props == null) props = new Properties();
					String [] kv = args[++i].split("=");
					props.put(kv[0], kv[1]);
				}
				else if (args[i].equals("-f"))
				{
					if (props == null) props = new Properties();
					try {
						FileInputStream in = new FileInputStream(args[++i]);
						props.load(in);;
						in.close();
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
				}
				else if (args[i].equals("-d"))
				{
					decodeData = args[++i];
				}
				else if (args[i].equals("-o"))
				{
					outformat = args[++i];
				}
				else if (args[i].equals("-c"))
				{
					hashscanfile = args[++i];
				}
				else if (args[i].equals("-x"))
				{
					x = Integer.parseInt(args[++i]);
				}
				else if (args[i].equals("-y"))
				{
					y = Integer.parseInt(args[++i]);
				}
				else if (args[i].equals("-P"))
				{
					pages = Integer.parseInt(args[++i]);
				}
				else if (args[i].equals("-s"))
				{
					InputStream in_ = Payload.class.getResourceAsStream(
							"/com/s5tech/net/services/display/EslPayloadExample.properties");			
					BufferedReader in = new BufferedReader(new InputStreamReader(in_));
					for ( ;;) {
						String l = in.readLine();
						if (l == null) break;
						System.out.println(l);					
					}
					System.out.println();					
					System.exit(0);
				}			
				
			}
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			System.exit(1);
			
		}
		

		try {
			
			if (epaperFile != null) 
			{
				File f = new File(epaperFile);			
				byte [] filedata = new byte[(int)f.length()];
				FileInputStream in = new FileInputStream(f);
				in.read(filedata);
				in.close();
				
				if (f.length() > 2 * Short.MAX_VALUE - 1) throw new Exception("file too long");
				
				EPaperPayload p =  new EPaperPayload();	
				
				byte [] data = (x > 0 || y > 0 || pages > 0 ?
						p.createPrice(filedata, activationTime, pages, x, y) : 
							p.createPrice(filedata, activationTime)); 
								
				byte [] hash = null;
				//try { hash = p.calculateEslHash(filedata); } 
				//catch (Throwable t) { hash = null; t.printStackTrace(); }			
				try { hash = p.calculateEslHash(data); } 
				catch (Throwable t) { hash = null; t.printStackTrace(); }			
				p.printData(data, hash, 2, outformat);
								
				System.exit(0);				
			}			
			else 
			{				
				if (decodeData != null)
				{
					LCDPayload p =  new LCDPayload();
					
					byte[] data = Base64Decoder.decode(decodeData);
					Properties decoded = p.parsePrice(data);					
					byte [] hash = p.calculateEslHash(data);
					
					long l = p.getLongFromBytes(data, 2, 4, false);
					l += APayloadCalculator.OFFSET_20000101_MS;					
					String at = APayloadCalculator.timeFormat.format(new Date(l));

					System.out.print("activationtime: ");
					System.out.println(at);
					System.out.print("hashcode:       ");
					System.out.println(p.getLowEndianUInt(hash, 0, 4));
					System.out.println("decoded:");

					for (Object k : decoded.keySet()) 
					{
						String key = k.toString();
						System.out.print(key);
						System.out.print(':');
						for (int i = 0; i < (20 - key.length()); i ++) {
							System.out.print(' ');	
						}
						System.out.println(decoded.get(k));
					}					
					
					System.exit(0);
					
				}
				else if (hashscanfile != null)
				{
					int i = hashscanfile.indexOf('#');
					int startpos = (i > 0 ? Integer.parseInt(hashscanfile.substring(i + 1)) : 0);					
					String filename = (i > 0 ? hashscanfile.substring(0, i): hashscanfile);
					
					System.out.println ("scan hash code of binary data of file " + filename + " from byte " + startpos);
					
					File f = new File (filename);
					if (!f.exists()) throw new Exception ("file not found! " + filename);
					byte [] data = new byte[(int)f.length() - startpos];
					
					FileInputStream stream = null;
					
					try {
						stream = new FileInputStream(filename);
						if (startpos > 0) {
							byte [] ignored= new byte[startpos];
							stream.read(ignored, 0, startpos);
						}
						
						for (int j = 0; j < data.length; j ++) {
							int c = stream.read();
							if (c == -1) throw new Exception("unexpected end of file!");
							data[j] = (byte) c;						
						}
					}
					catch (Throwable ignored) {}
					finally {
						if (stream != null) try { stream.close(); } catch (Throwable igrored) {}
					}
					
					EPaperPayload p =  new EPaperPayload();	
					byte [] hash = null;
					try { hash = p.calculateEslHash(data); } 
					catch (Throwable t) { hash = null; t.printStackTrace(); }			
					p.printData(data, hash, 2, null);	
					
					System.exit(0);
					
				}
				else 
				{
					if (props == null || props.size() == 0)
					{
						System.err.println("No parameters given!");
						System.exit(1);
					}
					
					LCDPayload p =  new LCDPayload();					
					byte [] data = p.createPrice(props, activationTime);
					byte [] hash = p.calculateEslHash(data);
					
					p.printData(data, hash, 2, outformat);
					
					System.exit(0);
				}
			}
						
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			System.exit(1);
		}
		
		System.exit(2);
		
	}

}
