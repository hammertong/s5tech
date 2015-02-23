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

import java.util.Date;
import java.util.Properties;

public class LCDPayload 
		extends APayloadCalculator {
	
	/**
	 * 
	 *  <b>Payload structure</b>
	 * 
	 *  <pre>
	 *  
		1 byte 			ESL type 

		4 bytes			Activation time in sec. Since 01012000
		
		4 bit 			Number of pages
		4 bit			Reserved
		
		0 – Num of pages
		{
			4 bit		Page visible in duration of x sec.
			4 bit		Reserved: bit 1 on ==> is Service Page
			X bytes     ESL data structures (see below)
		}
	 *  </pre>
	 *   
	 *  <b>C ESL data structure</b>
	    <pre>
	    
	 	typedef struct
		{		           
		  uint32 bigPrice;        // price on Area 1 -> 6 digits on large display, 5 digits on medium display (large <= 9999.99 [999999], medium <= 999.99 [99999])
		  uint32 smallPrice;      // price on Area 4 (<= 1999.99 [19999])
		  uint8  discount;        // discount on Area 2 (0 - 99)
		  uint8  datePoints[3];   // date or points on Area 2 (6 nibble, 1 con logica di accensione)
		  
		  struct
		  {
		    uint8 bigEuro     : 1;
		    uint8 N           : 1;
		    uint8 star        : 1;
		    uint8 PZ          : 1;
		    uint8 sconto      : 1;
		    uint8 offerta     : 1;
		    uint8 punti       : 1;
		    uint8 hand        : 1;
		    uint8 battery     : 1;
		    uint8 percent     : 1;
		    uint8 radio       : 1;
		    uint8 promo       : 1;
		    uint8 finoal      : 1;
		    uint8 X           : 1;
		    uint8 dot         : 1;
		    uint8 sottoCosto  : 1; // this icon is not present on medium display
		    uint8 prezzo      : 1;
		    uint8 base        : 1;
		    uint8 moon        : 1;
		    uint8 round       : 1;
		    uint8 A           : 1;
		    uint8 B           : 1;
		    uint8 C           : 1;
		    uint8 D           : 1;    
		    uint8 euro        : 1;
		    uint8 LT          : 1;
		    uint8 MT          : 1;
		    uint8 KG          : 1;  
		    uint8 reserved    : 4; 
		  } units;
		} AL_LCDx0_Tag_t;
		
		//MEDIUM  5 
		//LARGE   6
		</pre>
		
	 * @return
	 * 
	 */
	public byte [] createPrice (Properties displayProperties, String activationTime) 
			throws Exception
	{
		
		byte [] hea = new byte [] {
		  (byte)0x01,                                         
		  (byte)0x06,                                         // Esl Type
		  (byte)0x32, (byte)0xd6, (byte)0x08, (byte)0x14,     // Activation time from 01012000
		  (byte)0x10,			                              // 4 bit: Number of pages + 4 bit : 1 on => OneToTheLeft
		};
				
		byte etype = (byte) Integer.parseInt (displayProperties.getProperty ("esltype", "6"));	
		hea[1] = etype;
		
		long ltime = 0;

		if (activationTime != null) 
		{			
			if (activationTime.equalsIgnoreCase("now"))
			{
				ltime = System.currentTimeMillis();
				ltime -= 946684800000L;
				ltime /= 1000;
			}
			else 
			{
				ltime = timeFormat.parse(activationTime).getTime();
				ltime -= 946684800000L;
				ltime /= 1000;
			}
		}
		else 
		{
			ltime = 0;
		}
		
		/**
		 * 
		 * Override activation time with int value given 
		 * as argument in seconds from 01/01/2000
		 * 
		 */
		if (displayProperties.getProperty ("nActivationTime") != null)
		{
			ltime = (int) Long.parseLong((displayProperties.getProperty ("nActivationTime")));
		}
				
		insertLongAsBytes(ltime, hea, 2, 4, false);			

		//
		// TODO: set nr of pages in left semibyte hea[6] (1-3)
		//
		int pages = Integer.parseInt(displayProperties.getProperty ("pages", "1"));
		if (pages == 2) {
			hea[6] = (byte)0x20;
		}
		else if (pages == 3) {
			hea[6] = (byte)0x30;
		}
		
		if (displayProperties.getProperty("onetotheleft", "0").equals("1")) hea[6] |= (byte)0x08; //TODO: could be |= 0x01
		
		byte [] display = new byte [17 * pages];
				
		for (int i = 0; i < pages; i ++)
		{
			String prefix = (i > 0 ? "page" + (i + 1) + "." : "");
			int start = 17 * i; 
			
			// 4 bit: Visible time + 4 bit : 1 on => Set As Service Page (only last page of 2/3)
			byte _bt = Byte.parseByte(displayProperties.getProperty(prefix + "visibletime", "0"));
			if (_bt != 0) _bt <<= 4;
			
			if (i == pages - 1 && displayProperties.getProperty("servicepage", "0").equals("1")) _bt |= 0x08;

			display[start + 0] = _bt; 	
			
			// price & small price
			int bigprice = (int) (Double.parseDouble (displayProperties.getProperty(prefix + "bigprice", (etype == 6 ? "1888.88" : "188.88"))) * 100);
			int smallprice = (int) (Double.parseDouble (displayProperties.getProperty(prefix + "smallprice", "1999.99")) * 100);
			
			insertLongAsBytes(bigprice, display, 1 + start, 4, false);
			insertLongAsBytes(smallprice, display, 5 + start, 4, false);
			
			// discount
			display[start + 9]  = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdiscount", "58"), 16); //FF to erase it, 58 is 88 in decimal
			
			// datePoints
			display[start + 10] = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdigit1", "88"), 16); // FF to erase it
			display[start + 11] = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdigit2", "88"), 16); // FF to erase it
			display[start + 12] = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdigit3", "88"), 16); // FF to erase it
	
			// icons
			StringBuffer sb = new StringBuffer();
			sb.append(displayProperties.getProperty(prefix + "bigeuro", "1").trim());    
			sb.append(displayProperties.getProperty(prefix + "n", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "star", "0").trim());     //used for beacon lost
			sb.append(displayProperties.getProperty(prefix + "pz", "1").trim()); 
			sb.append(displayProperties.getProperty(prefix + "sconto", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "offerta", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "punti", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "hand", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "battery", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "percent", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "radio", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "promo", "1").trim()); 
			sb.append(displayProperties.getProperty(prefix + "finoal", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "x", "1").trim());    
			sb.append(displayProperties.getProperty(prefix + "dot", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "sottocosto", (etype == 6 ? "1" : "0")).trim());
			sb.append(displayProperties.getProperty(prefix + "prezzo", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "base", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "moon", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "round", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "a", "1").trim());      
			sb.append(displayProperties.getProperty(prefix + "b", "1").trim());      
			sb.append(displayProperties.getProperty(prefix + "c", "1").trim());      
			sb.append(displayProperties.getProperty(prefix + "d", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "euro", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "lt", "1").trim());     
			sb.append(displayProperties.getProperty(prefix + "mt", "1").trim());        
			sb.append(displayProperties.getProperty(prefix + "kg", "1").trim()); 
			sb.append("0000");
			
			display[start + 13] = (byte) (Integer.parseInt(sb.toString().substring(0, 8), 2) & 0x000000FF);
			display[start + 14] = (byte) (Integer.parseInt(sb.toString().substring(8, 16), 2) & 0x000000FF);
			display[start + 15] = (byte) (Integer.parseInt(sb.toString().substring(16,24), 2) & 0x000000FF);
			display[start + 16] = (byte) (Integer.parseInt(sb.toString().substring(24), 2) & 0x000000FF);
		
		}
		
		byte [] data = new byte [hea.length + display.length];
				
		int count = 0;

		for (byte b : hea) 
		{
			data[count] = b;  
			count ++;
		}
		
		for (byte b : display) 
		{
			data[count] = b;
			count ++;
		}
		
		return data;
	}
	
	
	public Properties parsePrice (byte [] data) throws Exception
	{
		
		Properties displayProperties = new Properties();
				
		displayProperties.setProperty ("esltype", String.valueOf((int) (data[1] & 0xff)));	
		long n = getLongFromBytes(data, 2, 4, false);
		displayProperties.setProperty ("nActivationTime", String.valueOf(n));
		
		n *= 1000;
		n += OFFSET_20000101_MS;		
		displayProperties.setProperty ("activationTime", timeFormat.format(new Date(n)));
				
		int pages = 0;
		
		if ((data[6] & ((byte)0x10)) == (byte)0x10) {
			 pages = 1;
		}
		else if ((data[6] & ((byte)0x20)) == (byte)0x20) {
			pages = 2;
		}
		else if  ((data[6] & ((byte)0x30)) == (byte)0x30) {
			pages = 3;
		}
		else {
			throw new Exception ("wrong number of pages");
		}
		
		displayProperties.setProperty ("pages", String.valueOf(pages));
		
		if ((data[6] & (byte)0x08) == (byte)0x08) displayProperties.setProperty("onetotheleft", "1");
		else displayProperties.setProperty("onetotheleft", "0");

		for (int i = 0; i < pages; i ++)
		{
			String prefix = (i > 0 ? "page" + (i + 1) + "." : "");
			int start = 7 + 17 * i; 
			
			// 4 bit: Visible time + 4 bit : 1 on => Set As Service Page (only last page of 2/3)
			displayProperties.setProperty(prefix + "visibletime", String.valueOf((int)(data[start] & (0x10))));
			
			byte _bt = data[start];
			_bt <<= 4;			
			if ((_bt & 0x08) == (byte) 0x08) {
				displayProperties.setProperty("servicepage", "1");
			}
			else {
				displayProperties.setProperty("servicepage", "0");
			}
			
			String bigprice = String.valueOf((int) getLongFromBytes (data, start + 1, 4, false)).trim();
			String smallprice = String.valueOf((int) getLongFromBytes (data, start + 5, 4, false)).trim();
			
			displayProperties.setProperty(prefix + "nBigprice", bigprice);
			displayProperties.setProperty(prefix + "nSmallprice", smallprice);
			
			if (bigprice.length() < 2) bigprice = "0.0" + bigprice;
			else if (bigprice.length() < 3) bigprice = "0." + bigprice;
			else bigprice = bigprice.substring(0, bigprice.length() - 2) + "." + bigprice.substring(bigprice.length() - 2);
			
			if (smallprice.length() < 2) smallprice = "0.0" + smallprice;
			else if (smallprice.length() < 3) smallprice = "0." + smallprice;
			else smallprice = smallprice.substring(0, smallprice.length() - 2) + "." + smallprice.substring(smallprice.length() - 2);
			
			displayProperties.setProperty(prefix + "bigprice", bigprice);
			displayProperties.setProperty(prefix + "smallprice", smallprice);
			
			/*
			  
			TODO: not parsed yet ....
			
			// discount
			display[start + 9]  = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdiscount", "58"), 16); //FF to erase it, 58 is 88 in decimal
			
			// datePoints
			display[start + 10] = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdigit1", "88"), 16); // FF to erase it
			display[start + 11] = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdigit2", "88"), 16); // FF to erase it
			display[start + 12] = (byte) Integer.parseInt(displayProperties.getProperty(prefix + "hexdigit3", "88"), 16); // FF to erase it
	
			// icons
			StringBuffer sb = new StringBuffer();
			sb.append(displayProperties.getProperty(prefix + "bigeuro", "1").trim());    
			sb.append(displayProperties.getProperty(prefix + "n", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "star", "0").trim());     //used for beacon lost
			sb.append(displayProperties.getProperty(prefix + "pz", "1").trim()); 
			sb.append(displayProperties.getProperty(prefix + "sconto", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "offerta", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "punti", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "hand", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "battery", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "percent", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "radio", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "promo", "1").trim()); 
			sb.append(displayProperties.getProperty(prefix + "finoal", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "x", "1").trim());    
			sb.append(displayProperties.getProperty(prefix + "dot", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "sottocosto", (etype == 6 ? "1" : "0")).trim());
			sb.append(displayProperties.getProperty(prefix + "prezzo", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "base", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "moon", "1").trim());  
			sb.append(displayProperties.getProperty(prefix + "round", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "a", "1").trim());      
			sb.append(displayProperties.getProperty(prefix + "b", "1").trim());      
			sb.append(displayProperties.getProperty(prefix + "c", "1").trim());      
			sb.append(displayProperties.getProperty(prefix + "d", "1").trim());   
			sb.append(displayProperties.getProperty(prefix + "euro", "1").trim());
			sb.append(displayProperties.getProperty(prefix + "lt", "1").trim());     
			sb.append(displayProperties.getProperty(prefix + "mt", "1").trim());        
			sb.append(displayProperties.getProperty(prefix + "kg", "1").trim()); 
			sb.append("0000");
			
			display[start + 13] = (byte) (Integer.parseInt(sb.toString().substring(0, 8), 2) & 0x000000FF);
			display[start + 14] = (byte) (Integer.parseInt(sb.toString().substring(8, 16), 2) & 0x000000FF);
			display[start + 15] = (byte) (Integer.parseInt(sb.toString().substring(16,24), 2) & 0x000000FF);
			display[start + 16] = (byte) (Integer.parseInt(sb.toString().substring(24), 2) & 0x000000FF);
			
			*/
			
		}
		
		return displayProperties;
		
	}
	
}
