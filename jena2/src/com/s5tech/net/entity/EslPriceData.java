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
 
package com.s5tech.net.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * A price with id, hash and activation time
 * @author S5Tech Development Team
 *
 */
public class EslPriceData implements Serializable {
	
	private static final long serialVersionUID = 5539282364041969190L;

	public static final long UNSET_HASH = -999;

	private String refId;
	private long hashWhenApplied;
	private Date activationTime;
	private byte[] data;
	private long receivedAt;
	
	public EslPriceData() {
		hashWhenApplied = UNSET_HASH;
	}

	public EslPriceData(String refId) {
		this.refId = refId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String id) {
		this.refId = id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getHashWhenApplied() {
		return hashWhenApplied;
	}

	public void setHashWhenApplied(long hashWhenApplied) {
		this.hashWhenApplied = hashWhenApplied;
	}

	public Date getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}

	public long getReceivedAt() {
		return receivedAt;
	}

	public void setUpdateId(long receivedAt) {
		this.receivedAt = receivedAt;
	}
}
