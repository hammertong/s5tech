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
 
package com.s5tech.net.server;

import com.s5tech.net.esl.ApplicationMessage;
import com.s5tech.net.esl.ApplicationMessageType;
import com.s5tech.net.esl.IApplicationMessageListener;
import com.s5tech.net.esl.MessageInfo;

/**
 * This class encapsulates the connection to the server by forwarding messages to an interchangeable {@link IServerProxy}.
 * The server proxy is set by the {@link MasterHubManager} when master status changes.
 * @author S5Tech Development Team
 *
 */
public class ServerFacade implements IServerProxy, IApplicationMessageListener {

	private static ServerFacade _instance = null;
	
	private IServerProxy serverProxy;
	//private ActiveQueue<MessageInfo<?>> upQueue;

	public static ServerFacade instance() {
		if(_instance == null)
			_instance = new ServerFacade();
		return _instance;
	}

	private ServerFacade() {
	}

	/**
	 * Set the server proxy to send the the messages for the server to.
	 * @param serverProxy
	 */
	public void setServerProxy(IServerProxy serverProxy) {
		this.serverProxy = serverProxy;
	}
	
	/**
	 * Gateway connection listener of Application Messages Network Queue 
	 * @see com.s5tech.net.esl.IApplicationMessageListener#onMessage(com.s5tech.net.esl.ApplicationMessage)
	 */
	public void onMessage(ApplicationMessage msg) {
		if(serverProxy == null || msg == null || !ApplicationMessageType.SERVER_MESSAGE.equals(msg.getType())) return;
		if(msg.getBody() instanceof MessageInfo<?>) {
			sendMessage(MessageInfo.class.cast(msg.getBody()));
		}
	}

	/**
	 * Gateway connection producer of Application Messages Network Queue 
	 * @see com.s5tech.net.desktop.server.IServerProxy#sendMessage(com.s5tech.net.desktop.msg.MessageInfo)
	 */
	public boolean sendMessage(MessageInfo<?> msg) {
		boolean res = isConnected() && serverProxy.sendMessage(msg);
		return res;
	}
	
	public boolean isConnected() {
		return serverProxy != null && serverProxy.isConnected();
	}
}
