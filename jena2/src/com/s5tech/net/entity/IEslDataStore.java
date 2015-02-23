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

import java.util.List;
import java.util.Map;

import com.s5tech.data.JdbcConnectionFactory;
import com.s5tech.net.type.EUI64Address;

/**
 * This interface contains the methods that must be available on a esl data store
 * @author S5Tech Development Team
 *
 */
public interface IEslDataStore {

	public JdbcConnectionFactory getPricesForEsl(EUI64Address mac);
	
	public void setActivePriceForEsl(EUI64Address esl, EslPriceData price);
	public void setPendingPriceForEsl(EUI64Address esl, EslPriceData price);
	public void removeActivePriceForEsl(EUI64Address esl);
	
	public void removePendingPriceForEsl(EUI64Address esl);
	public Map<EUI64Address,JdbcConnectionFactory> getPriceList();
	public long getLatestPriceId();
	
	public List<EUI64Address> getEnqueuedEslMessagesList();
	public void setPriceUpdateEnqueued(EUI64Address esl);
	public void setEslMessageSent(EUI64Address esl);
	
	public void shutdown();
		
	
}