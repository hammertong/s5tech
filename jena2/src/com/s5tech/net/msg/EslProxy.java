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

import com.s5tech.data.JdbcConnectionFactory;
import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.entity.EslPriceData;
import com.s5tech.net.entity.IEslDataStore;
import com.s5tech.net.esl.Hub2EslNetCtrlFrame;
import com.s5tech.net.eslnet.IEslNetwork;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.type.SerializeFactory;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

import java.nio.ByteBuffer;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EslProxy
{
	//private static final Logger logpp = LoggerFactory.getLogger(ILoggers.PRICEPERFOMANCE);
		
	public static final int MAX_PRICE_DELIVERY_ATTEMPTS = 3;
	
	private EslDeviceInfo deviceInfo;
	
	private EslStatus status = null;
	private EslAcknowledge acknowledge = null;
	private EslStatistics statistics = null;
	
	private PriceInfo activePrice;
	private PriceInfo pendingPrice;
	private PriceInfo sentPrice;

	private boolean online;

	private boolean justJoined;
	private int ttdCounter;
	
	private boolean isDotmatrix = false;

	private Logger log;
	
	private IEslListener eslListener;
	private IEslDataStore dataStore;
	private IEslNetwork network;
	
	private static boolean autocorrectTime = false;
	private static boolean skipFwCheck = false;
	private static int maxPriceUpdateRetry = 3;
		
	static 
	{
		try { autocorrectTime = System.getProperty("autocorrectTime", "").equals("true"); }
		catch (Throwable ex) {}
		try { skipFwCheck = System.getProperty("skipEslFirmwareCheck", "").equals("true"); }
		catch (Throwable ex) {}
		try { maxPriceUpdateRetry = Integer.parseInt(System.getProperty("maxPriceUpdateRetry", "3")); }
		catch (Throwable ex) {}
				
		//build log message from loaded configuration
		
		StringBuffer x = new StringBuffer();
		
		x.append("ESLPROXY -> ")
				.append("price-resubmit: ")
				.append(maxPriceUpdateRetry)
				.append(", ");
		
		x.append("adjust-time: ")
				.append(autocorrectTime ? "ON" : "OFF")
				.append(", ");
		
		x.append("firmware-check: ")
				.append(skipFwCheck ? "OFF" : "ON")
				.append(", ");
		
		LoggerFactory.getLogger(ISystemKeys.APPLICATION).info(x.toString());
		
	}
	
	
	public EslProxy(EslDeviceInfo deviceInfo, IEslNetwork network)
	{
		this(deviceInfo, ((IEslDataStore) (null)), network);
	}
	
	public EslProxy(EslDeviceInfo deviceInfo, IEslDataStore dataStore, IEslNetwork network)
	{	
		if (deviceInfo == null)
		{
			throw new IllegalArgumentException("deviceInfo must be set!!");
		} 
		else
		{
			log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + ".ESL(" + deviceInfo.getMac() + ")");
			
			isDotmatrix = EslEntityManager.instance().isDotMatrix(deviceInfo.getType());  
			
			if (network == null) throw new IllegalArgumentException ("cannot create esl proxy: no network set");
			this.network = network;
			
			this.deviceInfo = deviceInfo;
			this.dataStore = dataStore;
			
			activePrice = new PriceInfo(maxPriceUpdateRetry);
			pendingPrice = new PriceInfo(maxPriceUpdateRetry);
			
			return;
		}
	}
	
	public void setDataStore(IEslDataStore dataStore)
	{
		this.dataStore = dataStore;
	}
	
	public void setDeviceInfo(EslDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;	
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION + ".ESL(" + deviceInfo.getMac() + ")");
	}

	public String toString() {
		return Tools.toString(((Object) (this)));
	}
	
	public IEslNetwork getNetwork() {
		return network;
	}

	public void setNetwork(IEslNetwork network) {
		this.network = network;
	}

	@SuppressWarnings("rawtypes")
	public boolean sendData(byte data[])
	{		
		if (network == null) return false;
	
		// ++patch 
		// per impedire l'ingolfamento di invio dei price update 
		// in caso di reinvio da server o resubmit da netapp causato 
		// da messaggio di status prematuro o messaggio di status con 
		// wrong hashcode
		//
		network.emptyBuffer(getNetworkAddress(), true);
		// --patch 
		
		EslApplicationFrame app = new EslApplicationFrame();
		app.setCommand(EslApplicationCommand.DATA);
		app.setPdu(data);
		
		List ems = wrapApplicationFrame(app, getNetworkAddress());
		for (Iterator i$ = ems.iterator(); i$.hasNext();)
		{
			EslMessage m = (EslMessage)i$.next();
			if (!network.sendEslMessage(m))
				return false;
		}

		return true;
	}

	//public static List wrapApplicationFrame(EslApplicationFrame msg, EslProxy esl)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List wrapApplicationFrame(EslApplicationFrame msg, NetworkAddress nwk)
	{
		List list = EslTransportFrame.split(((com.s5tech.net.type.ISerializable) (msg)));
		List listMsgs = ((List) (new ArrayList()));
		
		EslMessage em;
		for (Iterator i$ = list.iterator(); i$.hasNext(); listMsgs.add(((Object) (em))))
		{
			EslTransportFrame tf = (EslTransportFrame)i$.next();
			EslSecurityFrame secureFrame = new EslSecurityFrame();
			secureFrame.setPdu(((com.s5tech.net.type.ISerializable) (tf)));
			em = new EslMessage();
			em.setPdu(((com.s5tech.net.type.ISerializable) (secureFrame)));
			
			/* patch for esl statistics request low priority */
			if (msg.getCommand() == EslApplicationCommand.READ_STATISTICS) 
				em.setPriority(Hub2EslNetCtrlFrame.PRI_LOW);
			
			//if (esl != null)
			//	em.setNetworkAddress(esl.getNetworkAddress());
			if (nwk != null) em.setNetworkAddress(nwk);
			
		}

		return listMsgs;
	}

	public static EslApplicationFrame toApplicationFrame(EslMessage msg, EslProxy esl)
	{
		if (msg == null)
			return null;
		EslSecurityFrame securityFrame = (EslSecurityFrame)msg.getPduAs(EslSecurityFrame.class);
		if (securityFrame == null)
		{
			if (esl != null)
				esl.log.warn((new StringBuilder()).append("Unable to decode security frame from esl message data: ").append(Tools.toHexString(msg.getPduArray())).toString());
			return null;
		}
		EslTransportFrame transportFrame = securityFrame.getPduAs(EslTransportFrame.class);
		if (transportFrame == null)
		{
			if (esl != null)
				esl.log.warn((new StringBuilder()).append("Unable to decode transport frame from esl message data: ").append(Tools.toHexString(msg.getPduArray())).toString());
			return null;
		} else
		{
			return (EslApplicationFrame)transportFrame.getPduAs(EslApplicationFrame.class);
		}
	}
	
		
	
	public void onMessage(EslApplicationFrame msg) {
	
		switch(msg.getCommand()) {
		
		case STATUS:
						
			try 
			{
				ByteBuffer src = msg.getPduAsBuffer();
				
				src.mark();
				byte b = src.get();			
				
				src.rewind();
				
				if (b == -1) 
				{
					if (msg.getPduArray() != null &&  msg.getPduArray().length == 19) {
						statistics = SerializeFactory.read (src, EslStatistics320.class);
					}
					else {
						statistics = SerializeFactory.read (src, EslStatistics.class);	
					}
					statistics.setReceivedTime(System.currentTimeMillis());
					//if (logpp.isTraceEnabled() && !justJoined)  {
					//	logpp.trace("CHANNEL {} {} STATISTICS", network.getActiveChannel(), getMacAddress());
					//}
					if (log.isTraceEnabled()) {
						log.trace("Got Esl STATISTICS: {}", Tools.toString(statistics));
					}
					if (eslListener != null) eslListener.onStatistics (this, statistics);
				}
				else if ((b & 0x80) != 0)
				{
					acknowledge = SerializeFactory.read (src, EslAcknowledge.class);
					acknowledge.setReceivedTime(System.currentTimeMillis());
					if (log.isTraceEnabled()) {
						log.trace("Got Esl ACKNOWLEDGE {}: {}", (justJoined ? "after rejoin": "after price update"), Tools.toString(acknowledge));
					}
					if (deviceInfo.getCoordinatorMac() == null || 
							deviceInfo.getCoordinatorMac().compareTo(network.getCoordinatorMac()) != 0) {
						if (log.isTraceEnabled()) log.trace("esl jumped from coordinator {} to {}", 
								deviceInfo.getCoordinatorMac(), network.getCoordinatorMac());
						deviceInfo.setCoordinatorMac(network.getCoordinatorMac());
					}					
					//if (logpp.isTraceEnabled() && !justJoined)  {
					//	logpp.trace("CHANNEL {} {} ACK", network.getActiveChannel(), getMacAddress());
					//}					
					evalPrice (acknowledge, false);
				}
				else
				{
					status = SerializeFactory.read (src, EslStatus.class);
					status.setReceivedTime(System.currentTimeMillis());
					
					if (log.isTraceEnabled()) {
						log.trace("Got Esl EXTENDED_STATUS: {}", Tools.toString(status));
					}				
					
					evalPrice (status, false);
					
					if (eslListener != null) {
						eslListener.onStatusChanged(this, status);	
					}
					
					if (!skipFwCheck) {
						network.evalFirmware (status.getDeviceType(), status.getFirmwareVersion());
					}
					
				}
			}
			catch (Throwable t) 
			{
				log.error("cannot process incoming EslMessage - {}", t);
			}
			
			break;
			
		// ESL destined commands; should never appear here
		case DATA:
		case KEY_UPDATE_N:
		case KEY_UPDATE_S:
		case SET_NIGHT_MODE:
		case SET_STORAGE_MODE:
		case UPDATE_FIRMWARE:
			break;
			default:
		}
	}
	
	
	public void onMessage(EslMessage msg)
	{
		if (msg == null) return;
		ttdCounter = 0;
		EslApplicationFrame appMsg = toApplicationFrame(msg, this);
		if (log.isTraceEnabled() && appMsg != null)
			log.trace("Got EslMessage from {} of type {}", 
					(msg.getMacAddress() == null ? 
							(msg.getNetworkAddress() == null ? "UNKNWN SOURCE": "NetworkAddress 0x" + msg.getNetworkAddress().toString()) 
							: msg.getMacAddress()), 
					appMsg.getCommand().name());
		if (appMsg == null)
		{
			log.error((new StringBuilder()).append("Unable to read an application message from the ESL message data: ")
					.append(Tools.toHexString(msg.getPduArray())).toString());
			return;
		} 
		else
		{
			onMessage(appMsg);
			return;
		}
	}

	public int getTtdCounter()
	{
		return ttdCounter;
	}

	public int incrementTtdCounter()
	{
		return ++ttdCounter;
	}

	public NetworkAddress getNetworkAddress()
	{
		return deviceInfo.getNetworkAddress();
	}

	public void setNetworkAddress(NetworkAddress networkAddress)
	{
		deviceInfo.setNetworkAddress(networkAddress);
	}

	public EUI64Address getMacAddress()
	{
		return deviceInfo != null ? deviceInfo.getMac() : null;
	}

	public void setMacAddress(EUI64Address macAddress)
	{
		deviceInfo.setMac(macAddress);
	}

	public void setListener(IEslListener listener)
	{
		eslListener = listener;
	}

	public EslStatus getStatus()
	{
		return status;
	}
	
	private synchronized boolean evalPrice(IEslPriceHashcodeInfo hashInfo, boolean outgoing)
	{		
		boolean result = false;
		
		if (justJoined) // && priceInfo instanceof EslAcknowledge) //commentato per retro compatibilita ESL <= 3.0.6
		{	
			activePrice.resetTxCounter();
			pendingPrice.resetTxCounter();
			justJoined = false;			
		}
				
		JdbcConnectionFactory priceForEsl = dataStore != null ? dataStore.getPricesForEsl(getMacAddress()) : null;
		
		if (!outgoing) { 
			if (priceForEsl == null
						|| (isDotmatrix && hashInfo.getHashCodeActivePrice() == 0)
						//
						// TODO: the following two lines will be removed after dotmatrix firmware bug fix
						//
						|| (isDotmatrix && priceForEsl.getActivePrice() == null)
						|| (isDotmatrix && hashInfo.getHashCodeActivePrice() != priceForEsl.getActivePrice().getHashWhenApplied())) 
			{		
				if (log.isTraceEnabled())
					log.trace ("No prices registered for the ESL, relaying 'priceunknown' to server - reason: {} ...",
							(priceForEsl == null ? "missing in datastore" : "restarting dotmatrix type"));
				if (eslListener != null) eslListener.onPriceUnknown(this);
				return false;
			}
		}
		
		//
		// evaluate hash codes ...
		//
		
		EslPriceData pActive = priceForEsl.getActivePrice();
		EslPriceData pPending = priceForEsl.getPendingPrice();
		if (pActive != null)
		{
			if (activePrice.getHash() != pActive.getHashWhenApplied())
			{
				activePrice.resetTxCounter();
				activePrice.setHash(pActive.getHashWhenApplied());
				if (pPending != null && pPending.getActivationTime().before(pActive.getActivationTime()))
				{
					dataStore.removePendingPriceForEsl(getMacAddress());
					pPending = null;
				}
			}
		} 
		else
		{
			if (activePrice != null) activePrice.resetHash();
		}
		if (pPending != null)
		{
			if (pendingPrice.getHash() != pPending.getHashWhenApplied())
			{
				pendingPrice.resetTxCounter();
				pendingPrice.setHash(pPending.getHashWhenApplied());
			}
		} 
		else
		{
			pendingPrice.resetHash();
		}
		
		long eslHashActive = hashInfo != null ? hashInfo.getHashCodeActivePrice() : 0L;
		long eslHashPending = hashInfo != null ? hashInfo.getHashCodePendingPrice() : 0L;
		
		if (log.isTraceEnabled()) 
			log.trace((new StringBuilder())
					.append("Evaluating hash codes: local active: ")
					.append(activePrice.getHash())
					.append(", esl active: ")
					.append(eslHashActive)
					.append(", local pending: ")
					.append(pendingPrice.getHash())
					.append(", esl pending: ")
					.append(eslHashPending).toString());
		
		if (autocorrectTime 
				&& activePrice != null 
				&& eslHashPending != 0 
				&& activePrice.getHash() != eslHashActive 
				&& activePrice.getHash() == eslHashPending) {
			if (log.isTraceEnabled())
				log.trace("... ops active hash results in the future for this esl, setting time in this network ...");
			network.setTime(); //correzione tempo se il prezzo per il server e' active, ma sulla esl e' nel futuro
		}

		if (pendingPrice.hasHash() && eslHashActive == pendingPrice.getHash())
		{
			if (log.isTraceEnabled()) log.trace ("ESL has activated the pending price. Updating the data store.");
			dataStore.setActivePriceForEsl(getMacAddress(), pPending);
			dataStore.removePendingPriceForEsl(getMacAddress());
			activePrice.setHash(pendingPrice.getHash());
			pendingPrice.resetHash();
			pActive = pPending;
			pPending = null;
			if (sentPrice == pendingPrice) sentPrice = null;
		}
		
		if (sentPrice != pendingPrice && activePrice.hasHash())
		{
			if (sentPrice == null && eslHashActive == 0L)
				activePrice.resetTxCounter();
			
			if (activePrice.getHash() != eslHashActive || activePrice.hash == 0L)
			{
				if (!activePrice.isDeliveryFailed())
				{
					
					if (activePrice == sentPrice && activePrice.hash > 0L)
					{
						if (sentPrice.txCount())
						{
							result = sendPrice(pActive, activePrice);
						}
						else
						{
							onPriceUpdateResult(eslHashActive, pActive, false);
						}
					}
					else if (activePrice.hash > 0L || activePrice.txCounter < 1)
					{
						if (log.isTraceEnabled()) log.trace ("Wrong active price on the ESL.");
						activePrice.resetTxCounter();
						activePrice.txCount();
						result = sendPrice(pActive, activePrice);
					}
					// ++patch for active hash set to 0 (returns true immediately without comparison)
					else if (activePrice.hash == 0) {
						onPriceUpdateResult (eslHashActive, pActive, true);
						activePrice.resetTxCounter();
					}
					// --patch						
					
				} 
				else
				{
					log.warn("Active price delivery failed, not retrying");
				}
			} 
			else if (sentPrice == activePrice)
			{
				onPriceUpdateResult (eslHashActive, pActive, true);
			}
		}
		
		if (sentPrice != activePrice && pendingPrice.hasHash())
		{
			if (sentPrice == null && eslHashPending == 0L)
				pendingPrice.resetTxCounter();
			if (pendingPrice.getHash() != eslHashPending || pendingPrice.hash == 0L)
			{
				if (!pendingPrice.isDeliveryFailed())
				{
					if (sentPrice == pendingPrice && pendingPrice.hash > 0L)
					{
						if (sentPrice.txCount())
						{
							result = sendPrice(pPending, sentPrice);
						}
						else
						{
							onPriceUpdateResult(eslHashPending, pPending, false);
						}
					} 
					else if (pendingPrice.hash > 0L || pendingPrice.txCounter < 1)
					{
						if (log.isTraceEnabled()) log.trace ("Wrong pending price on the ESL. Sending price");
						pendingPrice.resetTxCounter();
						pendingPrice.txCount();
						result = sendPrice(pPending, pendingPrice);
					}
					// ++patch for pending hash set to 0 (returns true immediately without comparison)
					else if (pendingPrice.hash == 0) {
						onPriceUpdateResult (eslHashPending, pActive, true);
						pendingPrice.resetTxCounter();
					}
					// --patch
				}
				else
				{
					log.warn("Pending price delivery failed, not retrying");
				}
			} 
			else if (sentPrice == pendingPrice)
			{
				onPriceUpdateResult(eslHashPending, pPending, true);
			}
		}
		
		//
		// update hash codes of status if previously valued ...!
		// 
		if (this.status != null && hashInfo instanceof EslAcknowledge) { 
			this.status.setHashCodeActivePrice (hashInfo.getHashCodeActivePrice());
			this.status.setHashCodePendingPrice (hashInfo.getHashCodePendingPrice());
		}
		
		return result;
		
	}
		

	public boolean priceUpdate()
	{
		if (sentPrice != null)
		{
			sentPrice.resetTxCounter();
			sentPrice = null;
		}
		return evalPrice(status, true);
	}

	private void onPriceUpdateResult(long hash, EslPriceData price, boolean success)
	{
		if (sentPrice == null) return;
		if (success) sentPrice.resetTxCounter();
		sentPrice = null;
		if (log.isTraceEnabled()) log.trace("Price updated with success:{}", success);
		if (eslListener != null) eslListener.onPriceUpdated(this, hash, price.getRefId(), success);
	}
		
	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	private boolean sendPrice(EslPriceData price, PriceInfo priceSent)
	{
		sentPrice = priceSent;
		if (log.isTraceEnabled()) {
			log.trace((new StringBuilder())
					.append("Send attempt #")
					.append(sentPrice.txCounter)
					.append(" for ")
					.append(sentPrice != activePrice ? "pending" : "active")
					.append(" price. (hash: ")
					.append(price.getHashWhenApplied())
					.append(")")
					.toString());
		}
		boolean r = sendData(price.getData()); 
		if (!r) log.warn("Send price failed !!!");
		return r;
	}

	public void justJoined()
	{
		justJoined = true;
		status = null;
		statistics = null;
		acknowledge = null;
	}
	
	public boolean isJustJoined()
	{
		return justJoined;
	}
	
	public EslStatistics getStatistics()
	{
		return statistics;
	}
	
	public EslAcknowledge getAcknowledge()
	{
		return acknowledge;
	}
	
	public boolean hasStatus()
	{
		 return status != null;
	}
	
	public boolean hasStatistics()
	{
		return statistics != null;
	}

}
