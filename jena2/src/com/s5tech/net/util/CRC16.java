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

/**
 * 16-Bit CRC checksum
 */
public final class CRC16 {

	private int crc;

	public CRC16() {
		reset();
	}

	public void reset() {
		crc = 0xFFFF;
	}

	public int getValue() {
		return crc;
	}

	public void add(byte b) {
		int msb = (crc & 0xFF);
		int lsb = ((crc >> 8) & 0xFF);
		lsb ^= b; // x^1
		lsb &= 0xFF;
		lsb ^= ((lsb >> 4) & 0xFF); // ???
		lsb &= 0xFF;
		msb ^= ((lsb << 4) & 0xFF); // x^12
		msb &= 0xFF;
		msb ^= ((lsb >> 3) & 0xFF); // x^5
		msb &= 0xFF;
		lsb ^= ((lsb << 5) & 0xFF); // x^5
		lsb &= 0xFF;

		crc = ((int) (msb & 0xFF) << 8) | (lsb & 0xFF);
	}

	public static int computeCrc16(byte[] input, int offset, int length) {

		int i = offset;

		// Avoid ArrayOutOfBoundsException
		if (length > input.length)
			length = input.length;

		CRC16 crc = new CRC16();
		
		while (0 < length--) {
			crc.add(input[i++]);
		}
		
		return crc.getValue();
	}

}