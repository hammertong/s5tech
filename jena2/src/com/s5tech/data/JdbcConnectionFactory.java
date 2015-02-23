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
 
package com.s5tech.data;

import java.io.Serializable;

import com.s5tech.net.entity.EslPriceData;

public class JdbcConnectionFactory implements Serializable {

	private static final long serialVersionUID = -3625075125035228546L;
	private EslPriceData activePrice = null;
	private EslPriceData pendingPrice = null;

	public JdbcConnectionFactory() {
	}

	public JdbcConnectionFactory(EslPriceData activePrice, EslPriceData pendingPrice) {
		if (activePrice != null && activePrice.getActivationTime() != null && activePrice.getData() != null)  
			this.activePrice = activePrice;
		if (pendingPrice != null && pendingPrice.getActivationTime() != null && pendingPrice.getData() != null)
			this.pendingPrice = pendingPrice;
	}

	public EslPriceData getActivePrice() {
		return activePrice;
	}

	public void setActivePrice(EslPriceData activePrice) {
		if (activePrice != null && activePrice.getActivationTime() != null && activePrice.getData() != null)  
			this.activePrice = activePrice;
		else this.activePrice = null;
	}

	public EslPriceData getPendingPrice() {
		return pendingPrice;
	}

	public void setPendingPrice(EslPriceData pendingPrice) {
		if (pendingPrice != null && pendingPrice.getActivationTime() != null && pendingPrice.getData() != null)
			this.pendingPrice = pendingPrice;
		else this.pendingPrice = null;
	}
}
