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
 
package com.s5tech.net.services.logging;

import java.util.Date;

public interface IApplicationLogEventsHandler {
			
	public void onApplicationStart(Date timestamp, String version);
	public void onApplicationShutdown(Date timestamp);
	public void onApplicationLoggerInactivity(Date timestamp);

	public void onCoordinatorSpam (Date timestamp, String coordinator, String description);
	public void onCoordinatorOnline (Date timestamp, String coordinator, String port, int channel);
	public void onCoordinatorOffline (Date timestamp, String coordinator, String port);	
	public void onCoordinatorBufferTimeout (Date timestamp, String coordinator, String port);

	//public void updateAssociation(String esl, String networkAddress, String coordinator);
	
	public void onEslValidateRequest (Date timestamp, String esl, String coordinator);
	public void onEslJoin (Date timestamp, String esl, String networkAddress, String coordinator);
	public void onEslJoinIgnored (Date timestamp, String esl, String coordinator);
	public void onEslUnauthorizedAttempt (Date timestamp, String esl, String coordinator);
	
	public void onPriceUpdateReceived(Date timestamp, String esl);
	public void onStatusRequestReceived(Date timestamp);
	
	public void onSubmitPriceUpdate(Date timestamp, String coordinator, String esl, int try_nr);
	public void onSubmitStatistics(Date timestamp, String coordinator, String esl);
	public void onSubmitChannelToJoin(Date timestamp, String coordinator, String esl, int channel);
	public void onSubmitKill(Date timestamp, String coordinator, String networkAddress, String esl);
	public void onSubmitLeave(Date timestamp, String coordinator, String networkAddress, String esl);
	public void onSubmitStatus (Date timestamp, String coordinator, String groupAddress); 
	
	public void onEslEvaluatePrice (Date timestamp, String esl, String coordinator, String evalResult);
	
	public void onBufferFilled (Date timestamp, String coordinator, String networkAddress, String esl, int sent, int remaining);
	public void onBufferFree (Date timestamp, String coordinator, String networkAddress, String esl, long duration);
	public void onBufferFlooding (Date timestamp, String coordinator, String networkAddress, String esl);
	
	//
	// seguenti metodo vengono sempre preceduti da un update association nwk+coord <-> mac 
	//
	public void onEslAcknowledge (Date timestamp, String esl, String coordinator, boolean isAfterJoin);
	public void onEslStatus (Date timestamp, String esl, String coordinator);
	public void onEslStatistics (Date timestamp, String esl, String coordinator);
	
	
}
