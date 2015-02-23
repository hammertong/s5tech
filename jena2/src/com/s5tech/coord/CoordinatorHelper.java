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
 
package com.s5tech.coord;

import java.util.Collection;

import com.s5tech.net.entity.EslPriceData;
import com.s5tech.net.esl.EslCommand;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.xml.types.MessageCommand;

/**
 * A price update with esl list and price data
 * This is mapped from the corresponding xml.
 * @author S5Tech Development Team
 *
 */

public class CoordinatorHelper extends EslCommand {

	private static final long serialVersionUID = 6892122086201236970L;
	
	public CoordinatorHelper() {
		super();
		setCommand(MessageCommand.ESLPRICEUPDATE);
	}

	public CoordinatorHelper(String refId,
			Collection<EUI64Address> esls, EslPriceData data) {
		super(MessageCommand.ESLPRICEUPDATE, refId, esls, data);
		data.setRefId(refId);
	}

	public CoordinatorHelper(String refId,
			Collection<EUI64Address> esls) {
		super(MessageCommand.ESLPRICEUPDATE, refId, esls);
	}
	
	public EslPriceData getPrice() {
		if(getData() != null && getData() instanceof EslPriceData)
		return EslPriceData.class.cast(getData());
		return null;
	}

	@Override
	public void setRefId(String refId) {
		super.setRefId(refId);
		if(getData() != null && getData() instanceof EslPriceData)
			EslPriceData.class.cast(getData()).setRefId(refId);
	}
	
	public void setPrice(EslPriceData price) {
		price.setRefId(getRefId());
		setData(price);
	}
	
}
