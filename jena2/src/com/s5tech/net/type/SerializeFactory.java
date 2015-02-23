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
 
package com.s5tech.net.type;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.ISystemKeys;

public class SerializeFactory {

	private static Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	public static final int DEFAULT_ALLOCATION_SIZE = 100;
	
	private static int allocationSize;
	
	public static int getAllocationSize() {
		if(allocationSize == 0) allocationSize = DEFAULT_ALLOCATION_SIZE;
		return allocationSize;
	}

	public static void setAllocationSize(int allocationSize) {
		SerializeFactory.allocationSize = allocationSize;
	}

	public static <T extends ISerializable> T read(ByteBuffer src, Class<? super T> clazz) {
		// This cast satisfies javac.
		return (T)read(src, src.remaining(), clazz);
	}
	
	/**
	 * Reads an {@link ISerializable} object from a {@link ByteBuffer}.
	 * @param <T>
	 * @param src the byte buffer to read from, beginning at the current position
	 * @param clazz the expected class of the serialised data to read
	 * @return the data object. Null on any error
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ISerializable> T read(ByteBuffer src, int length, Class<? super T> clazz) {
		if(src == null || clazz == null) return null;
		T res = null;
		try {
			res = (T) clazz.newInstance();
			if(!res.read(src, length)) res = null;
		} catch (Exception e) {
			log.trace("Unable to read a(n) " + clazz.getName() + " object from the byte buffer: " + e.getMessage());
			res = null;
		}
		return res;
	}

	public static ByteBuffer writeToNewBuffer(ISerializable obj) {
		return writeToNewBuffer(obj, obj.length());
	}
		
	public static ByteBuffer writeToNewBuffer(ISerializable obj, int bufferLength) {
		ByteBuffer buffer = ByteBufferUtils.allocate(bufferLength);
		obj.write(buffer);
		buffer.rewind();
		return buffer;
	}

	public static byte[] toArray(ISerializable msg) {
		return writeToNewBuffer(msg).array();
	}

}
