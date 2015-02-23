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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FILTER:<br><br>
 * esl&lt;=MAC>,event&lt;=evtpattern&gt;,context&lt;=net|app&gt;,coordinator&lt;=MAC&gt;
 * <br> 
 * <pre>
 * evtpattern examples: 
 * - join
 * - price:unknown
 * - join:request
 * </pre> 
 * @author GANDALF
 *
 */
public class ApplicationLogEventsDumper extends ApplicationLogEventsDefaultHandler {
	
	DateFormat tf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	private char sep = ' ';
	
	private String denyCtx_ = null;
	
	private String showEvt_ = null;
	
	private void print (StringBuffer b, String v, int len, boolean rightAlign)
	{
		if (v == null) v = "";
		int l = len;
		l -= v.length();
		if (l < 0) {
			v = v.substring(0, len);
			l = 0;
		}		
		if (rightAlign) {
			while (l-- > 0) {
				b.append(' ');
			}
			b.append(v);	
		}
		else {
			b.append(v);
			while (l-- > 0) {
				b.append(' ');
			}
		}
		b.append(sep);
	}
	
	private synchronized boolean validateCtx(String ctx)
	{
		if (denyCtx_ == null) {			
			if (filter != null && filter.containsKey("esl")) {
				denyCtx_ = "APP,NET";
			}
			else if (filter != null && filter.containsKey("context") 
					&& filter.getProperty("context").equalsIgnoreCase("net")) {
				denyCtx_ = "APP,ESL";
			}
			else if (filter != null && filter.containsKey("context") 
					&& filter.getProperty("context").equalsIgnoreCase("app")) {
				denyCtx_ = "NET,ESL";
			}
			else {
				denyCtx_ = "";	
			}
		}
		return (denyCtx_.indexOf(ctx) < 0);
	}
	
	private synchronized boolean validateEvt(String evt)
	{
		if (showEvt_ == null) {
			if (filter != null && filter.containsKey("event")) {
				showEvt_ = filter.getProperty("event").toLowerCase();
			}			
			else {
				showEvt_ = "";	
			}
		}
		return (showEvt_.length() == 0 || evt.toLowerCase().indexOf(showEvt_) >= 0);
	}
	
	private void print (Date timestamp, String ctx, String evt, String s1, String s2, String s3, int i1, int i2)
	{		
		if (out == null) return;
		if (!validateCtx(ctx)) return;
		if (!validateEvt(evt)) return;
		StringBuffer b = new StringBuffer();
		print (b, tf.format (timestamp), 24, false);
		print (b, ctx, 3, false);
		print (b, evt, 20, false);
		print (b, s1, 16, false);
		print (b, s2, 16, false);	
		print (b, s3,  6, false);	
		if (i1 >= 0) print (b, String.valueOf(i1), 6, true);		
		if (i2 >= 0) print (b, String.valueOf(i2), 6, true);
		out.println(b.toString());
		out.flush();
	}
	
	private void print (Date timestamp, String ctx, String evt, String s1, String s2, String s3, int i1)
	{		
		print (timestamp, ctx, evt, s1, s2, s3, i1, -1);
	}
	
	private void print (Date timestamp, String ctx, String evt, String s1, String s2, String s3)
	{		
		print (timestamp, ctx, evt, s1, s2, s3, -1, -1);
	}
	
	private void print (Date timestamp, String ctx, String evt, String s1, String s2)
	{		
		if (out == null) return;
		if (!validateCtx(ctx)) return;
		if (!validateEvt(evt)) return;
		StringBuffer b = new StringBuffer();
		print (b, tf.format (timestamp), 24, false);
		print (b, ctx, 3, false);
		print (b, evt, 20, false);
		print (b, s1, 16, false);
		b.append(s2 == null ? "" : s2);
		b.append(sep);		
		out.println(b.toString());
		out.flush();
	}
	
	@Override
	public void onEslValidateRequest(Date timestamp, String esl,
			String coordinator) {		
		//
		// TODO: check for pending priceupdate, statistics, leave, kill and chtojoin 
		//		
		print (timestamp, "ESL", "JOIN:REQUEST", coordinator, esl);
	}

	@Override
	public void onEslJoin(Date timestamp, String esl, String networkAddress, String coordinator) {		
		print (timestamp, "ESL", "JOIN:AUTHORIZED", coordinator, esl, networkAddress);
	}

	@Override
	public void onEslJoinIgnored(Date timestamp, String esl, String coordinator) {
		print (timestamp, "ESL", "JOIN:IGNORED", coordinator, esl);				
	}

	@Override
	public void onEslUnauthorizedAttempt(Date timestamp, String esl, String coordinator) {		
		print (timestamp, "ESL", "JOIN:UNAUTHORIZED", coordinator, esl);
	}

	@Override
	public void onEslAcknowledge(Date timestamp, String esl, String coordinator, boolean isAfterJoin) {		
		if (isAfterJoin) {			
			print (timestamp, "ESL", "JOIN:ACKNOWLEDGE", coordinator, esl);
		}
		else {
			print (timestamp, "ESL", "PRICE:ACKNOWLEDGE", coordinator, esl);
		}		
	}
	
	@Override
	public void onSubmitStatus(Date timestamp, String coordinator, String groupAddress) {
		print (timestamp, "ESL", "STATUS:SUBMIT", coordinator, groupAddress);
	}
	
	@Override
	public void onEslStatus(Date timestamp, String esl, String coordinator) {		
		print (timestamp, "ESL", "STATUS", coordinator, esl);
	}
	
	@Override
	public void onPriceUpdateReceived(Date timestamp, String esl) {		
		print (timestamp, "ESL", "PRICE:RECEIVED", "SERVERDISPATCHER", esl);		
	}

	@Override
	public void onSubmitPriceUpdate(
			Date timestamp, 
			String coordinator,
			String esl, 
			int submit_nr) {
		print (timestamp, "ESL", "PRICE:SUBMIT", 
				((coordinator == null || coordinator.length() == 0) ? "SERVERDISPATCHER" : coordinator), 
					esl, String.valueOf(submit_nr));			
	}
		
	@Override
	public void onBufferFilled (Date timestamp, String coordinator, String networkAddress, String esl, int sent, int remaining) {
		print (timestamp, "ESL", "SENT", coordinator, esl, networkAddress, sent, remaining);	
	}
	
	@Override
	public void onBufferFree (Date timestamp, String coordinator, String networkAddress, String esl, long duration) {
		print (timestamp, "ESL", "FREE", coordinator, esl, networkAddress, (int)duration );
	}
	
	@Override
	public void onBufferFlooding (Date timestamp, String coordinator, String networkAddress, String esl) {
		print (timestamp, "ESL", "FLOOD", coordinator, esl, networkAddress);
	}
	
	@Override
	public void onSubmitStatistics(Date timestamp, String coordinator, String esl) {
		print (timestamp, "ESL", "STATISTICS:SUBMIT", 
				((coordinator == null || coordinator.length() == 0) ? "SERVERDISPATCHER" : coordinator), esl);
	}
	
	@Override
	public void onEslStatistics(Date timestamp, String esl, String coordinator) {	
		print (timestamp, "ESL", "STATISTICS:RECEIVED", coordinator, esl);
	}
	
	@Override
	public void onSubmitChannelToJoin(Date timestamp, String coordinator, String esl, int channel) {
		print (timestamp, "ESL", "CHANNELTOJOIN:SUBMIT", 
				((coordinator == null || coordinator.length() == 0) ? "SERVERDISPATCHER" : coordinator), 
				esl, String.valueOf(channel));
	}

	@Override
	public void onSubmitKill(Date timestamp, String coordinator, String networkAddress, String esl) {
		print (timestamp, "ESL", "KILL:SUBMIT", 
				((coordinator == null || coordinator.length() == 0) ? "SERVERDISPATCHER" : coordinator), 
				esl);		
	}

	@Override
	public void onSubmitLeave(Date timestamp, String coordinator,
			String networkAddress, String esl) {
		print (timestamp, "ESL", "LEAVE:SUBMIT", 
				((coordinator == null || coordinator.length() == 0) ? "SERVERDISPATCHER" : coordinator), 
				esl);
	}

	@Override
	public void onEslEvaluatePrice(Date timestamp, String esl, String coordinator, String evalResult) {
		String what = null;
		if (evalResult.equals("PUNK")) {
			what = "PRICE:UNKOWN";
		}
		else if (evalResult.equals("P-OK")) {
			what = "PRICE:OK";
		}
		else if (evalResult.equals("P-WA")) {
			what = "PRICE:WRONG-ACTIVE";
		}
		else if (evalResult.equals("P-WP")) {
			what = "PRICE:WRONG-PENDING";
		}
		print (timestamp, "ESL", what, coordinator, esl);
	}
	
	@Override
	public void onApplicationStart(Date timestamp, String version) {
		print (timestamp, "APP", "START", null, null);
	}

	@Override
	public void onApplicationShutdown(Date timestamp) {		
		print (timestamp, "APP", "STOP", null, null);	
	}

	@Override
	public void onApplicationLoggerInactivity(Date timestamp) {
		print (timestamp, "LOG", "INACTIVE", null, null);
	}

	@Override
	public void onCoordinatorSpam(Date timestamp, String coordinator, String desc) {
		print (timestamp, "LOG", "SPAM", coordinator, desc);
	}
	
	@Override
	public void onCoordinatorOffline (Date timestamp, String coordinator, String port) {
		print (timestamp, "NET", "OFFLINE", coordinator, port);
	}
	
	@Override
	public void onCoordinatorOnline (Date timestamp, String coordinator, String port, int channel) {
		while (port.length() < 30) { port += " "; }
		print (timestamp, "NET", "ONLINE", coordinator, port + String.valueOf(channel));
	}
	
	@Override
	public void onCoordinatorBufferTimeout (Date timestamp, String coordinator, String port) {
		print (timestamp, "NET", "TIMEOUT", coordinator, port);
	}

	@Override
	public void onStatusRequestReceived(Date timestamp) {
		print (timestamp, "ESL", "STATUSREQUEST", "SERVERDISPATCHER", "");		
	}
	
}
