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

import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

public abstract class ApplicationLogEventsDefaultHandler 
		implements IApplicationLogEventsHandler {

	protected Date from = null;
	protected String logDir = "./logs";
	
	protected PrintStream out = System.out;
	
	protected Properties filter = null;
	
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public String getLogDir() {
		return logDir;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}
		
	public void setFilter(String criteria) {
		
		if (criteria == null) return;
		
		String[] px = null;
		
		if (criteria.indexOf(',') >= 0) {
			px = criteria.split(",");
		}
		else if (criteria.indexOf(';') >= 0) {
			px = criteria.split(";");
		}
		else {
			px = new String[] { criteria };			
		}
		
		if (px == null) return;
		
		for (String p : px) {
			String [] tk = p.split("=");			
			if (tk.length < 2) continue;
			if (filter == null) filter = new Properties();			
			filter.put(tk[0].replace('\t', ' ').trim().toLowerCase(), tk[1].replace('\t', ' ').trim());
		}		
	}
	
	public void run()
	{
		try {		
			ApplicationLogEventsParser_1_5_030 p = new ApplicationLogEventsParser_1_5_030();
			p.setLogDir(logDir);
			p.setFrom(from);
			p.setFilter(filter);
			p.SetEventsHandler(this);
			p.parseDir();			
		}
		catch (Throwable t) {			
			t.printStackTrace();		
		}		
	}

//	@Override
//	public void updateAssociation(String esl, String networkAddress, String coordinator)
//	{
//		nwkmap.put(networkAddress, esl);
//		macmap.put(esl, networkAddress);		
//	}
	
	
	// dummy methods....
	
	@Override
	public void onApplicationStart(Date timestamp, String version) {
	}

	@Override
	public void onApplicationShutdown(Date timestamp) {
	}

	@Override
	public void onApplicationLoggerInactivity(Date timestamp) {
	}

	@Override
	public void onCoordinatorSpam(Date timestamp, String coordinator,
			String description) {
	}

	@Override
	public void onCoordinatorOnline(Date timestamp, String coordinator,
			String port, int channel) {
	}

	@Override
	public void onCoordinatorOffline(Date timestamp, String coordinator,
			String port) {
	}

	@Override
	public void onCoordinatorBufferTimeout(Date timestamp, String coordinator,
			String port) {
	}

	@Override
	public void onEslValidateRequest(Date timestamp, String esl,
			String coordinator) {
	}

	@Override
	public void onEslJoin(Date timestamp, String esl, String networkAddress,
			String coordinator) {
	}

	@Override
	public void onEslJoinIgnored(Date timestamp, String esl, String coordinator) {
	}

	@Override
	public void onEslUnauthorizedAttempt(Date timestamp, String esl,
			String coordinator) {
	}

	@Override
	public void onPriceUpdateReceived(Date timestamp, String esl) {
	}

	@Override
	public void onSubmitPriceUpdate(Date timestamp, String coordinator,
			String esl, int try_nr) {
	}

	@Override
	public void onSubmitStatistics(Date timestamp, String coordinator,
			String esl) {
	}

	@Override
	public void onSubmitChannelToJoin(Date timestamp, String coordinator,
			String esl, int channel) {
	}

	@Override
	public void onSubmitKill(Date timestamp, String coordinator,
			String networkAddress, String esl) {
	}

	@Override
	public void onSubmitLeave(Date timestamp, String coordinator,
			String networkAddress, String esl) {
	}

	@Override
	public void onSubmitStatus(Date timestamp, String coordinator,
			String groupAddress) {
	}

	@Override
	public void onEslEvaluatePrice(Date timestamp, String esl,
			String coordinator, String evalResult) {
	}

	@Override
	public void onBufferFilled(Date timestamp, String coordinator,
			String networkAddress, String esl, int sent, int remaining) {
	}

	@Override
	public void onBufferFree(Date timestamp, String coordinator,
			String networkAddress, String esl, long duration) {
	}

	@Override
	public void onBufferFlooding(Date timestamp, String coordinator,
			String networkAddress, String esl) {
	}

	@Override
	public void onEslAcknowledge(Date timestamp, String esl,
			String coordinator, boolean isAfterJoin) {
	}

	@Override
	public void onEslStatus(Date timestamp, String esl, String coordinator) {
	}

	@Override
	public void onEslStatistics(Date timestamp, String esl, String coordinator) {
	}
	
	
}
