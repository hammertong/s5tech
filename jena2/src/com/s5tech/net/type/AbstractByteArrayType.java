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
// Decompiler options: packimports(3) 
// Source File Name:   AbstractByteArrayType.java

package com.s5tech.net.type;

import com.s5tech.net.util.Tools;

import java.io.Serializable;
import java.nio.ByteBuffer;

public abstract class AbstractByteArrayType
    implements ISerializable, Serializable
{

    public AbstractByteArrayType()
    {
        hash = 0;
    }

    public AbstractByteArrayType(byte value[])
    {
        setValue(value);
    }

    public boolean read(ByteBuffer src, int length)
    {
        if(length < length())
        {
            return false;
        } else
        {
            value = new byte[length()];
            src.get(value);
            return true;
        }
    }

    public int write(ByteBuffer dest)
    {
        if(value == null)
        {
            return 0;
        } else
        {
            dest.put(value);
            return length();
        }
    }

    public void setValue(byte value[])
    {
        setValue(value, 0, false);
    }

    public void setValue(byte source[], int offset)
    {
        setValue(source, offset, false);
    }

    public void setValue(byte value[], int offset, boolean reverse)
    {
        if(value == null || value.length < offset + length())
            throw new IllegalArgumentException((new StringBuilder()).append("The length of the data must be ").append(length()).append(" bytes").toString());
        System.arraycopy(value, offset, getValue(), 0, length());
        if(reverse)
            Tools.reverseByteArray(getValue());
        calcHash();
    }

    public byte[] getValue()
    {
        if(value == null)
            value = new byte[length()];
        return value;
    }

    public long getLongValue(boolean msb)
    {
        if(value == null)
            return 0L;
        else
            return Tools.getLongFromBytes(value, 0, value.length, msb);
    }

    public boolean equals(Object obj)
    {
        return obj != null && AbstractByteArrayType.class.isAssignableFrom(obj.getClass()) 
        	&& Tools.equalArrays(getValue(), ((AbstractByteArrayType)AbstractByteArrayType.class.cast(obj)).getValue());
    }

    private void calcHash()
    {
        if(value != null)
        {
            long h = Tools.getLongFromBytes(value, 0, length(), true);
            hash = (int)(h > 0x7fffffffL ? (h >> 32) + (h & 0xffffffffL) : h);
        } else
        {
            super.hashCode();
        }
    }

    public int hashCode()
    {
        if(hash == 0)
            calcHash();
        return hash;
    }

    public String toString()
    {
        return Tools.toHexString(value);
    }

    private static final long serialVersionUID = 0x6df4b78b3dbbdff6L;
    private byte value[];
    private int hash;
}
