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
 
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst space safe 
// Source File Name:   SerialFrame.java

package com.s5tech.net.serial;

import com.s5tech.net.type.ISerializable;
import com.s5tech.net.type.SerializeFactory;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

import java.io.*;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialFrame
{

	public static final byte START_OF_FRAME = 2;
	public static final int MAX_PDU_LENGTH = 252;
	public static final int MAX_LENGTH = 255;
	private int fcs;
	private int length;
	private ByteBuffer data;
	private boolean skipStartOfFrame;
	private boolean fcsMismatch;
	private static Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

	public SerialFrame()
	{
		fcs = -1;
		length = -1;
	}

	public SerialFrame(byte data[])
	{
		setData(ByteBufferUtils.wrap(data));
	}

	public SerialFrame(ByteBuffer data)
	{
		setData(data);
	}

	public SerialFrame(ISerializable data)
	{
		setData(data);
	}

	public void skipStartOfFrame()
	{
		skipStartOfFrame = true;
	}

	public boolean isFcsMismatch()
	{
		return fcsMismatch;
	}

	public int length()
	{
		return length;
	}

	public int read(InputStream src)
		throws IOException
	{
		if (!skipStartOfFrame && src.read() != 2)
			return -1;
		length = src.read();
		if (length < 0)
			return -1;
		data = ByteBufferUtils.allocate(length);
		if (length != src.read(data.array()))
			return -1;
		int calcedFCS = calcFCS();
		fcs = src.read();
		if (fcs < 0 || fcs != calcedFCS)
		{
			if (log.isDebugEnabled())
				log.debug((new StringBuilder()).append("FCS error. Calculated: ").append(Tools.toHexByte(calcedFCS)).append("(").append(calcedFCS).append("). Read: ").append(Tools.toHexByte(fcs)).append(" (").append(fcs).append(")").toString());
			fcsMismatch = true;
			return -1;
		} else
		{
			return length + 3;
		}
	}

	public int write(OutputStream dest)
		throws IOException
	{
		dest.write(2);
		dest.write(length);
		if (length > 0)
			dest.write(data.array(), data.position(), data.remaining());
		dest.write(calcFCS());
		return length + 3;
	}

	public int calcFCS()
	{
		byte val = (byte)length;
		if (data == null)
			return -1;
		data.mark();
		for (int i = 0; i < length; i++)
			val ^= data.get();

		data.reset();
		return val < 0 ? val + 256 : val;
	}

	public void setData(byte data[])
	{
		setData(ByteBufferUtils.wrap(data));
	}

	public void setData(ISerializable pdu)
	{
		setData(SerializeFactory.writeToNewBuffer(pdu));
	}

	public void setData(ByteBuffer pdu)
	{
		if (pdu.remaining() > 252)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("The length of the pdu cannot exceed 252 bytes! Was: ").append(pdu.remaining()).toString());
		} else
		{
			data = pdu;
			length = pdu.remaining();
			setFcs();
			return;
		}
	}

	public ByteBuffer getData()
	{
		return data;
	}

	public void setFcs()
	{
		fcs = calcFCS();
	}

	public int getFcs()
	{
		if (fcs <= 0)
			setFcs();
		return fcs;
	}

	public int getLength()
	{
		return length;
	}

	public String toString()
	{
		return (new StringBuilder()).append("LEN:").append(length).append(", FCS: 0x").append(Tools.toHexByte(getFcs())).append(", Data: ").append(data != null ? Tools.toHexString(data.array(), data.position(), data.remaining()) : "none").toString();
	}

}
