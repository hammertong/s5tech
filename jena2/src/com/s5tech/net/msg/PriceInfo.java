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
 
package com.s5tech.net.msg;

import java.io.Serializable;

public class PriceInfo implements Serializable
{
	private static final long serialVersionUID = -6462685376162404862L;

	public static final long NO_HASH = -1L;
	
	long hash;
	int txCounter;
	boolean deliveryFailed;
	int maxTxCount;

	public boolean hasHash()
	{
		return hash != -1L;
	}

	public void resetHash()
	{
		hash = -1L;
	}

	public long getHash()
	{
		return hash;
	}

	public void setHash(long hash)
	{
		this.hash = hash;
	}

	public void resetTxCounter()
	{
		txCounter = 0;
		deliveryFailed = false;
	}

	public boolean txCount()
	{
		if (hash == 0L && txCounter > 0 || txCounter >= maxTxCount)
		{
			deliveryFailed = true;
			return false;
		} else
		{
			txCounter++;
			return true;
		}
	}

	public boolean isDeliveryFailed()
	{
		return deliveryFailed;
	}



	public PriceInfo()
	{
		this(3);
	}

	public PriceInfo(int maxPriceDeliveryAttempts)
	{
		maxTxCount = maxPriceDeliveryAttempts;
		resetHash();
	}
}
