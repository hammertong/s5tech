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
 
package com.s5tech.net.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Encoder {

	private static final byte[] magic = new byte [] {
		(byte) 0xFE, (byte) 0x77, (byte) 0xFE, (byte) 0xDE, 
	};
	
	public boolean isEncoded(String filename)
	{
		FileInputStream _iX = null;
		
		try {			
			File fconf = new File(filename);
			if (fconf.length() > 4) {				
				_iX = new FileInputStream(fconf);
				byte [] _bX = new byte[4];
				_iX.read(_bX, 0, 4);
				return (_bX[0] == magic[0]
						&& _bX[1] == magic[1]
						&& _bX[2] == magic[2] 
						&& _bX[3] == magic[3]);					
			}
			else {
				return false;
			}
		}
		catch (Throwable xyz) {
			return false;
		}
		finally {
			if (_iX != null) try { _iX.close(); }  catch (Throwable ignored) {}
		}
	}
	
	public void run (InputStream in, OutputStream out, boolean encode) throws IOException
	{
		int count = 0;
		
		try 
		{
			byte [] head = new byte[4];
	
			if (encode) {
		
				for (int i = 0; i < magic.length; i ++) {
					out.write(magic[i]);
				}
				
				for (int i = 0; i < head.length; i ++ ){
					head[i] = (byte) ((int)(Math.random() * 255) & 0xff);
					out.write(head[i]);
				}
				
			}
			else {
				
				for (int i = 0; i < magic.length; i ++) {
					if ((byte) in.read() != magic[i]) throw new IOException("invalid header!");
				}
				
				in.read(head, 0, head.length);
			}
			
			for ( ;;) {
				
				int c = in.read();
				if (c == -1) break;
				byte b = (byte) c;
				
				b ^= head[count];
				out.write(b);
				
				count ++;
				count %= head.length;
				
			}
			
		}
		catch (Throwable t) {
			throw t;
		}
		
	}
	
}

