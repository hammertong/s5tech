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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

public class CsvToXml {

	public static void exec(String[] args) {

		String infile = null;
		String outfile = null;
		String root = "data";
		String separator = ";";
		
		for (int i = 0; i < args.length; i ++) 
		{
			if (args[i].equals("-i")) {
				infile = args[++i];
			}
			else if (args[i].equals("-o")) {
				outfile = args[++i];
			}
			else if (args[i].equals("-r")) {
				root = args[++i];
			}
			else if (args[i].equals("-s")) {
				String s = args[++i];
				if (s.equals("\\t")) separator = "\t";
				else if (s.equals("\\b")) separator = " ";
				else separator = s;
			}				
		}
		
		if (infile == null || outfile == null) {
			System.err.println("input file and output file must be specified!");
			System.exit(1);
		}
		
		try {
		
			BufferedReader in = new BufferedReader(new FileReader(infile));
			PrintStream out = new PrintStream(outfile);
			
			String line = in.readLine();
			String [] columns = line.split(separator);
			
			out.print("<");
			out.println(root);
			out.println(">");
			
			for ( ;;) {
				
				line = in.readLine();
				
				if (line == null) break;
				
				String [] fields = line.split(separator); 
				
				for (int i = 0; i < columns.length; i ++) {
					
					out.print("\t");
					
					out.print("<");
					out.println(columns[i]);
					out.print(">");
					
					out.print(fields[i]);
					
					out.print("</");
					out.println(columns[i]);
					out.println(">");
				}				
				
			}
			
			out.print("</");
			out.println(root);
			out.println(">");
			
			in.close();
			out.close();
			
			System.exit(0);
		
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			
			File f = new File(outfile);
			if (f.exists()) 
			{
				System.err.println("removing output file " + outfile + "... ");
				f.delete();
			}
			
			System.exit(1);
		}
		

	}

}
