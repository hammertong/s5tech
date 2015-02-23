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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Provides various methods for instantiating {@link ByteBuffer}s
 * Ensures that all uses the same {@link ByteOrder}
 * @author S5Tech Development Team
 *
 */
public abstract class ByteBufferUtils {

	private ByteBufferUtils() {}

	private static ByteOrder order;
	
	public static ByteOrder getOrder() {
		return order;
	}

	public static void setOrder(ByteOrder order) {
		ByteBufferUtils.order = order;
	}

	public static final ByteBuffer allocate(int capacity) {
		ByteBuffer b = ByteBuffer.allocate(capacity);
		b.order(ByteOrder.LITTLE_ENDIAN);
		return b;
	}

	public static final ByteBuffer wrap(byte[] data, int offset, int length) {
		ByteBuffer b = ByteBuffer.wrap(data, offset, length);
		b.order(ByteOrder.LITTLE_ENDIAN);
		return b;
	}
	
	public static final ByteBuffer wrap(byte[] data) {
		return wrap(data, 0, data.length);
	}

	/**
	 * Creates a new ByteBuffer object and populates it with data from an existing ByteBuffer
	 * @param data the ByteBuffer to copy from
	 * @param length
	 * @return
	 */
	public static final ByteBuffer create(ByteBuffer data, int length) {
		byte[] d = new byte[length];
		data.get(d);
		return wrap(d);
	}

	public static final ByteBuffer create(ByteBuffer data) {
		return create(data, data.remaining());
	}

	public static int readUInt16(ByteBuffer buffer) {
		return readUInt16(buffer, false);
	}
	
	public static long readUInt32(ByteBuffer buffer) {
		return readUInt32(buffer, false);
	}

/*
	public static int readUShort(ByteBuffer buffer, boolean msb) {
		ByteOrder bo = buffer.order();
		buffer.order(msb ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
		int res = buffer.getShort();
		buffer.order(bo);
		return res >= 0 ? res : res+65536;
	}
*/	
	
	public static int readUInt16(ByteBuffer src, boolean msb) {
		return (int) readUnsignedInteger(src, 2, msb);
	}
	
	public static long readUInt32(ByteBuffer src, boolean msb) {
		return readUnsignedInteger(src, 4, msb);
	}
	
	public static long readUnsignedInteger(ByteBuffer src, int length, boolean msb) {

		if(src.remaining() < length) length = src.remaining();

		long val = 0;
		long shiftCount;
		long shiftCountIncrement;

		if (msb) {
			shiftCount = ((length - 1) * 8);
			shiftCountIncrement = -8;
		} else {
			shiftCount = 0;
			shiftCountIncrement = 8;
		}

		int endPos = src.position()+length;
		
		for (int pos = src.position(); pos < endPos; pos++) {
			byte b = src.get(pos);
			val += (b >= 0 ? b : (b + 256L)) << shiftCount;
			shiftCount += shiftCountIncrement;
		}
		
		src.position(endPos);

		return val;
	}
	
	public static void writeUInt8(int value, ByteBuffer dest) {
		writeUnsignedInteger(value, dest, 1, false);
	}

	public static void writeUInt16(int value, ByteBuffer dest) {
		writeUnsignedInteger(value, dest, 2, false);
	}
	
	public static void writeUInt32(long value, ByteBuffer dest) {
		writeUnsignedInteger(value, dest, 4, false);
	}

	public static void writeUInt16(int value, ByteBuffer dest, boolean msb) {
		writeUnsignedInteger(value, dest, 2, msb);
	}
	
	public static void writeUInt32(long value, ByteBuffer dest, boolean msb) {
		writeUnsignedInteger(value, dest, 4, msb);
	}
	
	public static final void writeUnsignedInteger(long value, ByteBuffer dest, int length, boolean msb) {
		
		if(dest.remaining() < length) length = dest.remaining();

		int shiftCount;
		int shiftCountIncrement;

		if(msb) {
			shiftCount = (length - 1) * 8;
			shiftCountIncrement = -8;
		} else {
			shiftCount = 0;
			shiftCountIncrement = 8;
		}

		int endPos = dest.position()+length;
		
		for (int pos = dest.position(); pos < endPos; pos++) {
			dest.put((byte) ((value >> shiftCount) & 0xff));
			shiftCount += shiftCountIncrement;
		}
	}
}
