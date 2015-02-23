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

import java.io.IOException;
import java.nio.ByteBuffer;

import com.s5tech.net.msg.ITransmissionListener;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.Tools;

/**
 * Provides reading and writing of a frame from/to a stream
 * @author S5Tech Development Team
 */
public abstract class AbstractSerializableFrame implements ISerializable {

	/**
	 * Write the header to the output stream
	 * @param dest
	 * @return
	 */
	abstract protected int writeHead(ByteBuffer dest);
	/**
	 * Write tail to the output stream
	 * @param dest
	 * @return
	 */
	abstract protected int writeTail(ByteBuffer dest);
	/**
	 * read head from the input stream
	 * @param dest
	 * @return
	 */
	abstract protected int readHead(ByteBuffer src);
	/**
	 * read head from the input stream
	 * @param src
	 * @return
	 */
	abstract protected int readTail(ByteBuffer src);

	/**
	 * This method reads the pdu from the input stream while considering the length.
	 * Must return null on error
	 * @param src
	 * @return
	 */
	protected ByteBuffer createAndReadPdu(ByteBuffer src, int length) {
		return ByteBufferUtils.create(src, length);
	}
	
	/**
	 * Calls {@link #createAndReadPdu(ByteBuffer, int)} with src.remaining() as the length
	 * @param src
	 * @return
	 */
	protected ByteBuffer createAndReadPdu(ByteBuffer src) {
		return createAndReadPdu(src, src.remaining());
	}
	
	/**
	 * Get the max allowed length of the pdu.
	 * The setPdu methods will throw an {@link IllegalArgumentException} if the length of the supplied data is larger
	 * @return
	 */
	abstract public int getMaxPduLength();
	
	abstract protected int getLengthOfHeadAndTail();
	
	public final int length() {
		return getLengthOfHeadAndTail() + (pdu == null ? 0 : pdu.length());
	}
	
	private ISerializable pdu;

	public <T extends ISerializable> T getPduAs(Class<? super T> clazz) {
		if(pdu == null || clazz == null) return null;
		// This cast satisfies javac.
		return (T)SerializeFactory.read(getPduAsBuffer(), clazz);
	}
	
	public ByteBuffer getPduAsBuffer() {
		ByteBuffer buffer = ByteBufferUtils.allocate(pdu.length());
		pdu.write(buffer);
		buffer.flip();
		return buffer;
	}
	
	public ISerializable getPdu() {
		return pdu;
	}
	public void setPdu(ByteBuffer pdu) {
		setPdu(wrap(pdu));
	}
	public void setPdu(ByteBuffer pdu, int length) {
		setPdu(wrap(pdu, length));
	}
	public void setPdu(ISerializable pdu) {
		if(pdu != null && pdu.length() > getMaxPduLength())
			throw new IllegalArgumentException("The length of the pdu cannot exceed " + getMaxPduLength() + " bytes! Was: " + pdu.length());
		this.pdu = pdu;
		if(pdu instanceof AbstractSerializableFrame) {
			AbstractSerializableFrame.class.cast(pdu);
		}
	}
	public void setPdu(byte[] pdu) {
		setPdu(wrap(pdu, 0, pdu.length));
	}
	public byte[] getPduArray() {
		return pdu == null ? new byte[0] : getPduAsBuffer().array();
	}

	/**
	 * Write the contents of the frame to an {@link ByteBuffer}
	 * @param dest
	 */
	final public int write(ByteBuffer dest) {
		int i = writeHead(dest);
		int tmp;
		if(i >= 0) {
			if(pdu != null) {
				i += pdu.write(dest);
			}
			if(i >= 0) {
				tmp = writeTail(dest);
				if(tmp < 0) return tmp;
				i += tmp;
			}
		}
		return i;
	}
	
	/**
	 * Read the object from a {@link ByteBuffer}
	 * @param src
	 * @return
	 * @throws IOException 
	 */
	final public boolean read(ByteBuffer src, int length) {
		int i = readHead(src);
		if(i >= 0) {
			pdu = wrap(src, length - i);
			if(pdu != null) {
				int t = readTail(src);
				if(t < 0 ) i = -1;
				else i += length + t;
			} else {
				i = -1;
			}
		}
		return i > 0;
	}

	public static ISerializable wrap(ByteBuffer data) {
		return data == null ? null : wrap(data.array(), data.position(), data.remaining());
	}

	public static ISerializable wrap(ByteBuffer data, int length) {
		return data == null ? null : wrap(data.array(), data.position(), length);
	}

	public static ISerializable wrap(final byte[] data, final int offset, final int length) {
		if(data == null) return null;
		return new ISerializable() {
			
			private ByteBuffer buffer = ByteBufferUtils.wrap(data, offset, length);
			private int len = length;
			
			public int write(ByteBuffer dest) {
				if(buffer != null)
					dest.put(buffer.array(), buffer.position(), len);
				return len;
			}
			
			public boolean read(ByteBuffer src, int len) {
				buffer = ByteBufferUtils.wrap(src.array(), src.position(), len);
				this.len = len;
				return buffer != null;
			}
			
			public int length() {
				return len;
			}
		};
	}

	private ITransmissionListener<AbstractSerializableFrame> transmissionListener;

	public ITransmissionListener<AbstractSerializableFrame> getTransmissionListener() {
		return transmissionListener;
	}
	
	public void setTransmissionListener(
			ITransmissionListener<AbstractSerializableFrame> transmissionListener) {
		this.transmissionListener = transmissionListener;
	}
	
	public boolean invokeTransmissionListener() {
		if(transmissionListener == null) return false;
		transmissionListener.onTransmission(this);
		return true;
	}
	
	public boolean hasTransmissionListener() {
		return transmissionListener != null;
	}
	
	@Override
	public String toString() {
		return Tools.toString(this);
	}
}