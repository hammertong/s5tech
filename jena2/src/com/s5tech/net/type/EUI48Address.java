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
 * Represents a 6 byte mac addresss
 * @author S5Tech Development Team
 */
public class EUI48Address extends AbstractByteArrayType implements Comparable<EUI48Address> {

	private static final long serialVersionUID = -7787111482926096294L;
	public static final int LENGTH = 6;
	
	public static final EUI48Address ALL;
	
	static {
		byte[] all = new byte[LENGTH];
		Arrays.fill(all,(byte) 0xFF);
		ALL = new EUI48Address(all);
	}

	public EUI48Address() {
	}

	public EUI48Address(byte[] value) {
		setValue(value, 0, false);
	}
	
	public EUI48Address(byte[] value, int offset) {
		setValue(value, offset, false);
	}

	public EUI48Address(byte[] value, int offset, boolean reverse) {
		setValue(value, offset, reverse);
	}
	
	public EUI48Address(String value) throws Exception{
		if (value == null) return;
		setValue(Tools.hexStringToByteArray(value.trim()), 0, false);		
	}
	
	public boolean read(ByteBuffer src, int length) {
		if(ALL == this) return false;
		int pos = src.position();
		setValue(src.array(), src.position(), true);
		boolean res = getValue() != null;
		if(res) src.position(src.position()+pos);
		return res;
	}

	public int write(ByteBuffer dest) {
		byte[] val = getValue();
		for(int i=LENGTH-1 ; i >= 0 ; i--)
			dest.put(val[i]);
		return LENGTH;
	}
	
	public int length() {
		return LENGTH;
	}

	@Override
	public void setValue(byte[] source, int offset) {
		if(ALL == this) return;
		super.setValue(source, offset);
	}
	
	@Override
	public void setValue(byte[] value) {
		if(ALL == this) return;
		super.setValue(value);
	}
	
	@Override
	public void setValue(byte[] value, int offset, boolean reverse) {
		if(ALL == this) return;
		super.setValue(value, offset, reverse);
	}
	
	public long getLongValue() {
		return super.getLongValue(false);
	}
	
	/**
	 * Compares this instance to another, by comparing the long values.
	 * The speed of this method may be improved by caching the long value, but this double the memory usage of this object type.
	 */
	public int compareTo(EUI48Address o) {
		if(o == null) return 1;
		else if(o == this) return 0;
		long a = getLongValue();
		long b = o.getLongValue();
		if(a == b) return 0;
		if(a > b) return 1;
		return -1;
	}

}
