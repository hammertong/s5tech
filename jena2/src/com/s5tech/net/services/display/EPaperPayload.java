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

public class EPaperPayload 
		extends APayloadCalculator {
	
	public byte [] createPrice (byte [] pngFile, String activationTime) throws Exception
	{
		return createPrice(pngFile, activationTime, 0, 0, 0);
	}
	
	public byte [] createPrice (byte [] pngFile, String activationTime, int pages, int x, int y) throws Exception
	{	
		/*
		 * Note Esl Type = 0x04 sembra essere quello che dicono le specifiche
		 * impostandolo a 4 dopo l'invio del prezzo l'ESL grafica si resetta.
		 * 
		 * Impostandolo a 2 l'ESL risponde con un ack privo di hash code 
		 * a fine trasmissione (= 0 su hash e pending) e non visualizzando niente, 
		 * ma NON si resetta.
		 * 
		 * Non si e' provato con valori diversi da 2 e 4 
		 * 
		 * Considerazioni:
		 * Probabilmente 4 � il tipo giusto, ragione per cui l'esl grafica si
		 * resetta dopo avere mal processato il pacchetto dati inviato che 
		 * potrebbe non essere corretto. Nell'altro caso semplicemente rifiuta 
		 * il dato probabilmente senza neanche tentare di processarlo. 
		 * 
		 * Si tratta di pacchetti di tipo ESL_DATA, un png di 2634 bytes
		 * viene splittato in 35-36 pacchetti
		 * 
		 * MAC di Epaper trovati:
		 * 
		 * - 00124B000118C477
		 * 
		 */
		byte [] hea = new byte [] {
		  (byte)0x02,                                         // 0x01 for LCD, 0x02 for EPaper
		  (byte)0x04,                                         // Esl Type
		  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,     // Activation time from 01012000
		  (byte)0x01,			                              // Number of pages
		  (byte)0x00,			                              // X position
		  (byte)0x00,			                              // X position
		  (byte)0x00,			                              // Y position
		  (byte)0x00,			                              // Y position
		  (byte)0x00,			                              // Length of pngFile
		  (byte)0x00,			                              // Length of pngFile
		};

		if (activationTime != null) 
		{		
			long ltime = 0;
			
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
			
			insertLongAsBytes (ltime, hea, 2, 4, false);	
			
		}
		
		if (pages > 0) 
		{
			System.err.println("setting pages = " + pages);
			hea[6] = (byte) pages;
		}
		
		if (x > 0) 
		{
			System.err.println("setting X position = " + x);
			insertLongAsBytes (x, hea, 7, 2, false);	
		}
		
		if (y > 0) 
		{
			System.err.println("setting Y position = " + y);
			insertLongAsBytes (y, hea, 9, 2, false);	
		}
		
		insertLongAsBytes (pngFile.length, hea, 11, 2, false);

		int count = 0;
		byte [] data = new byte [hea.length + pngFile.length];

		for (byte b : hea) 
		{
			data[count] = b;  
			count ++;
		}
		
		for (byte b : pngFile) 
		{
			data[count] = b;
			count ++;
		}
		
		return data;
	}
	
}
