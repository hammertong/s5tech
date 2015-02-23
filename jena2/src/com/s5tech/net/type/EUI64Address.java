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
 
/**
 * 
 */
package com.s5tech.net.type;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.s5tech.net.util.Tools;

/**
 * Represents a 8 byte mac addresss
 * @author S5Tech Development Team
 */
public class EUI64Address extends AbstractByteArrayType implements Comparable<EUI64Address> {

	private static final long serialVersionUID = -3941031045053236316L;
	public static final int LENGTH = 8;
	public static final EUI64Address ALL;
	public boolean all;
	
	static {
		byte[] adr = new byte[LENGTH];
		Arrays.fill(adr, (byte) 0xFF);
		ALL = new EUI64Address(adr);
	}
	
	public EUI64Address() {
	}	
	
	public EUI64Address(byte[] value) {
		setValue(value, 0, false);
	}
	
	public EUI64Address(byte[] value, int offset) {
		setValue(value, offset, false);
	}

	public EUI64Address(byte[] value, int offset, boolean reverse) {
		setValue(value, offset, reverse);
	}

	public EUI64Address(ByteBuffer src) {
		read(src, LENGTH);
	}
	
	public EUI64Address(String value) throws Exception{
		if (value == null) return;
		setValue(Tools.hexStringToByteArray(value.trim()), 0, false);		
	}
	
	
	/* (non-Javadoc)
	 * @see com.s5tech.net.desktop.entity.AbstractByteArrayType#length()
	 */
	public int length() {
		return LENGTH;
	}

	public boolean read(ByteBuffer src, int length) {
		int nextPos = src.position()+LENGTH;
		setValue(src.array(), src.position(), true);
		boolean res = getValue() != null;
		if(res) src.position(nextPos);
		return res;
	}

	public int write(ByteBuffer dest) {
		byte[] val = getValue();
		for(int i=LENGTH-1 ; i >= 0 ; i--)
			dest.put(val[i]);
		return LENGTH;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		return (ALL == this && ALL == obj) || super.equals(obj);
	}

	public long getLongValue() {
		return super.getLongValue(false);
	}
	
	/**
	 * Compares this instance to another, by comparing the long values.
	 * The speed of this method may be improved by caching the long value, but this double the memory usage of this object type.
	 */
	public int compareTo(EUI64Address o) {
		if(o == null) return 1;
		else if(o == this) return 0;
		long a = getLongValue();
		long b = o.getLongValue();
		if(a == b) return 0;
		if(a > b) return 1;
		return -1;
	}
	
}
