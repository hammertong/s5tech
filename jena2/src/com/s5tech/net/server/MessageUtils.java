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
 
/**
 * 
 */
package com.s5tech.net.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TimeZone;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.s5tech.net.entity.EslDeviceInfo;
import com.s5tech.net.entity.EslEntityManager;
import com.s5tech.net.entity.EslInstallationKey;
import com.s5tech.net.esl.CoordinatorEventMessageInfo;
import com.s5tech.net.esl.EslEventMessageInfo;
import com.s5tech.net.esl.EslStatisticsMessageInfo;
import com.s5tech.net.esl.EslStatusMessageInfo;
import com.s5tech.net.esl.HubEventMessageInfo;
import com.s5tech.net.esl.MessageInfo;
import com.s5tech.net.esl.EslStatusMessageInfo.CoordinatorsInRange;
import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;
import com.s5tech.net.util.XmlUtils;
import com.s5tech.net.xml.AckMessage;
import com.s5tech.net.xml.EslItem;
import com.s5tech.net.xml.EslList;
import com.s5tech.net.xml.Message;
import com.s5tech.net.xml.types.CommandResult;
import com.s5tech.net.xml.types.MessageCommand;

/**
 * @author S5Tech Development Team
 * Created: Apr 7, 2010
 *
 */
public class MessageUtils {

	private static Logger log;
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;

	static {
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
		marshaller = XmlUtils.newMarshaller();
		marshaller.setValidation(false);
		unmarshaller = XmlUtils.newUnmarshaller();
	}
	
	private static int msgId;
	
	synchronized public static int nextMsgId() {
		msgId++;
		if(msgId == Integer.MAX_VALUE) msgId = 1;
		return msgId;
	}
	
	/**
	 * Create an {@link AckMessage} from a (received) {@link Message} object.
	 * @param msg
	 * @return
	 */
	public static AckMessage createAck(Message msg, CommandResult result, String description) {
		if(msg == null) return null;
		AckMessage ack = new AckMessage();
		ack.setMsgCommand(msg.getMsgCommand());
		ack.setMsgId(msg.getMsgId());
		ack.setCreationTime(new Date());
		ack.setDescription(description);
		ack.setResult(result);
		return ack;
	}

	/**
	 * Creates a new {@link Message} object.
	 * Sets the creation date and increments and sets the next message id
	 * @param command The {@link MessageCommand} command to set in the created object
	 * @return a object ready for command-specific content
	 */
	synchronized public static Message createMessage(MessageCommand command) {
		Message msg = new Message();
		msg.setCreationTime(new java.util.Date());
		msg.setMsgId(String.valueOf(nextMsgId()));
		msg.setMsgCommand(command);
		return msg;
	}
	
	public static boolean isDateNow(String date) {
		return date != null && "now".equals(date);
	}
	
	static DateFormat dateFormatter;
	
	static {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public static Date parseDate(String date) {
		try {
			return date == null ? null : dateFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String formatDate(Date date) {
		return dateFormatter.format(date);
	}

	public static Object fromXml(ByteBuffer xml) {
		return fromXml(xml.array(), xml.position(), xml.remaining());
	}
	
	public static Object fromXml(byte[]  xml, int offset, int length) {
		Object obj;
		try {
			obj = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(xml, offset, length)));
		} catch (Exception e) {
			obj = null;
			log.error("A(n) " + e.getClass().getName() + " ( " + e.getMessage() + " ) occured while unmarshalling: " + xml);
			log.debug("", e);
		}
		return obj;
	}
	

	private static String eslStatusToXml(EslStatusMessageInfo msg) {
		StringBuffer x = new StringBuffer()
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <message xmlns=\"http://s5tech.com/network\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" mac=\"") 
				.append(msg.getMac()) 
				.append("\" msgId=\"") 
				.append(String.valueOf(Tools.isNullOrEmpty(msg.getMsgId()) ? MessageUtils.nextMsgId() : msg.getMsgId()))
				.append("\" msgCommand=\"EslStatus\" creationTime=\"") 
				.append(MessageUtils.formatDate(msg.getCreationTime())).append("\">")
				.append("<eslStatus mac=\"").append(msg.getMac()).append("\">")
				.append("<hashCodeActivePrice>").append(msg.getHashCodeActivePrice()).append("</hashCodeActivePrice>")
				.append("<hashCodePendingPrice>").append(msg.getHashCodePendingPrice()).append("</hashCodePendingPrice>")
				.append("<batteryLevel>").append(msg.getBatteryLevel()).append("</batteryLevel>")
				.append("<txPower>").append(msg.getTxPower()).append("</txPower>")
				.append("<macAssociatedCoordinator>").append(msg.getMacAssociatedCoordinator()).append("</macAssociatedCoordinator>")
				.append("<temperature>").append(msg.getTemperature()).append("</temperature>")
				.append("<firmwareVersion>").append(msg.getFirmwareVersion()).append("</firmwareVersion>")
				.append("<lifetimeHours>").append(msg.getLifetimeHours()).append("</lifetimeHours>")
				.append("<channel>").append(msg.getChannel()).append("</channel>")
				.append("<railDetected>").append(msg.isRailDetected()).append("</railDetected>")
				.append("<nightMode>").append(msg.isNightMode()).append("</nightMode>")
				.append("<state>").append(msg.getState().toString()).append("</state>")
				.append("<deviceType>").append(String.valueOf(msg.getDeviceType())).append("</deviceType>")
				.append("<coordinatorsInRange>");		
		if(msg.getCoordinatorsInRangeOfEsl() != null) {
			for (CoordinatorsInRange h : msg.getCoordinatorsInRangeOfEsl()) {
				x.append("<coordinator mac=\"").append(h.mac) 
						.append("\" signalLevel=\"").append(h.signalLevel)
						.append("\"/>");
			}
		}
		x.append("</coordinatorsInRange></eslStatus></message>");		
		return x.toString();
	}
	
	private static String eslStatisticsToXml(EslStatisticsMessageInfo msg) {
		StringBuffer x = new StringBuffer()
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <message xmlns=\"http://s5tech.com/network\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" mac=\"") 
				.append(msg.getMac()) 
				.append("\" msgId=\"") 
				.append(String.valueOf(Tools.isNullOrEmpty(msg.getMsgId()) ? MessageUtils.nextMsgId() : msg.getMsgId()))
				.append("\" msgCommand=\"EslStatistics\" creationTime=\"") 
				.append(MessageUtils.formatDate(msg.getCreationTime())).append("\">")				
				.append("<eslStatistics mac=\"").append(msg.getMac()).append("\">")
				.append("<nColdReset>").append(msg.getnColdReset()).append("</nColdReset>")
				.append("<nHotReset>").append(msg.getnHotReset()).append("</nHotReset>")
				.append("<nPushReset>").append(msg.getnPushReset()).append("</nPushReset>")
				.append("<nOtaReset>").append(msg.getnOtaReset()).append("</nOtaReset>")
				.append("<nAssertReset>").append(msg.getnAssertReset()).append("</nAssertReset>")
				.append("<nPushSleep>").append(msg.getnPushSleep()).append("</nPushSleep>")
				.append("<nNetSleep>").append(msg.getnNetSleep()).append("</nNetSleep>")
				.append("<nScanSleep>").append(msg.getnScanSleep()).append("</nScanSleep>")
				.append("<nPowerupSleep>").append(msg.getnPowerupSleep()).append("</nPowerupSleep>")
				.append("<nStatusRetry>").append(msg.getnStatusRetry()).append("</nStatusRetry>")
				.append("<nScan>").append(msg.getnScan()).append("</nScan>")
				.append("<time>").append(msg.getTime()).append("</time>");				
				if (msg.getnJoinWDT() >= 0) {
					x.append("<nJoinWDT>").append(msg.getnJoinWDT()).append("</nJoinWDT>");
				}						
				x.append("</eslStatistics></message>");		
		return x.toString();
	}
		
	private static String eslEventToXml(EslEventMessageInfo msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><message xmlns=\"http://s5tech.com/network\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" mac=\"") 
				.append(msg.getMac())
				.append("\" msgId=\"")
				.append(String.valueOf(Tools.isNullOrEmpty(msg.getMsgId()) ? MessageUtils.nextMsgId() : msg.getMsgId()))
				.append("\" msgCommand=\"EslEvent\" creationTime=\"") 
				.append(MessageUtils.formatDate(msg.getCreationTime()))
				.append("\"> <eslEvent eslMac=\"") 
				.append(msg.getMac())
				.append("\" type=\"") 
				.append(msg.getType().toString()) 
				.append("\"");
		if (msg.getCoordinatorMac() != null && msg.getCoordinatorMac().length() > 0) 
			sb.append(" coordinatorMac=\"").append(msg.getCoordinatorMac()).append("\"");
		if (msg.getAttributes() != null) sb.append(" ").append(msg.getAttributes());
		sb.append(" time=\"")
				.append(MessageUtils.formatDate(msg.getCreationTime())) 
				.append("\"/> </message>");
		return sb.toString();
	}
	
	
	
	private static String coordinatorEventToXml(CoordinatorEventMessageInfo msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<message xmlns=\"http://s5tech.com/network\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
				.append("xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" ")
				.append("mac=\"") 
				.append(msg.getMac())
				.append("\" msgId=\"")
				.append(String.valueOf(Tools.isNullOrEmpty(msg.getMsgId()) ? MessageUtils.nextMsgId() : msg.getMsgId()))
				.append("\" msgCommand=\"CoordinatorEvent\" creationTime=\"") 
				.append(MessageUtils.formatDate(msg.getCreationTime()))
				.append("\"> <coordinatorEvent coordinatorMac=\"") 
				.append(msg.getMac())
				.append("\" type=\"")
				.append(msg.getType().toString()) 
				.append("\"");
		if (msg.getPort() != null && msg.getPort().length() > 0) 
			sb.append(" port=\"").append(msg.getPort()).append("\"");
		if (msg.getChannelNo() != null && !msg.getChannelNo().isOffline()) 
			sb.append(" channel=\"").append(msg.getChannelNo()).append("\"");
		sb.append(" time=\"")
				.append(MessageUtils.formatDate(msg.getCreationTime())) 
				.append("\"/></message>");
		return sb.toString();
	}
		

	private static String hubEventToXml(HubEventMessageInfo msg) {		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<message xmlns=\"http://s5tech.com/network\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
				.append("xsi:schemaLocation=\"http://s5tech.com/network schema.xsd\" ")
				.append("mac=\"") 
				.append(msg.getMac())
				.append("\" msgId=\"")
				.append(String.valueOf(Tools.isNullOrEmpty(msg.getMsgId()) ? MessageUtils.nextMsgId() : msg.getMsgId()))
				.append("\" msgCommand=\"HubEvent\" creationTime=\"") 
				.append(MessageUtils.formatDate(msg.getCreationTime()))
				.append("\"> <hubEvent hubMac=\"") 
				.append(msg.getMac())
				.append("\" type=\"")
				.append(msg.getType().toString()) 
				.append("\"");
		if (msg.getIpAddress() != null && msg.getIpAddress().length() > 0) 
			sb.append(" ipAddress=\"").append(msg.getIpAddress()).append("\"");
		sb.append(" time=\"")
				.append(MessageUtils.formatDate(msg.getCreationTime())) 
				.append("\"/></message>");
		return sb.toString();
	}
	
		
	private static final ByteArrayOutputStream bo = new ByteArrayOutputStream();

	public synchronized static byte[] toXml(Object msg) {
		if(msg == null) return null;
		bo.reset();
		if(msg instanceof EslStatusMessageInfo) {
			return eslStatusToXml(EslStatusMessageInfo.class.cast(msg)).getBytes();
		} else if(msg instanceof EslEventMessageInfo) {
			return eslEventToXml(EslEventMessageInfo.class.cast(msg)).getBytes();
		} else if(msg instanceof EslStatisticsMessageInfo) {
			return eslStatisticsToXml(EslStatisticsMessageInfo.class.cast(msg)).getBytes();
		} else if(msg instanceof CoordinatorEventMessageInfo) {
			return coordinatorEventToXml(CoordinatorEventMessageInfo.class.cast(msg)).getBytes();
		} else if(msg instanceof HubEventMessageInfo) {
			return hubEventToXml(HubEventMessageInfo.class.cast(msg)).getBytes();
		} else if(msg instanceof MessageInfo) {
			MessageInfo<?> info = MessageInfo.class.cast(msg);
			AckMessage a = null;
			Message m = null;
			if(info.isAck()) {
				a = new AckMessage();
				a.setDescription(info.getDescription());
				a.setMsgId(info.getMsgId());
				a.setResult(info.getResult());
				if (info.getMac() != null) a.setMac(info.getMac().getValue());
				a.setMsgCommand(info.getCmd());
			} else {
				m = MessageUtils.createMessage(info.getCmd());
				m.setMac(info.getMac().getValue());
				m.setMsgCommand(info.getCmd());
			}
			LinkedList<Object> content = info.getContent();
			if(content != null && content.size() > 0) {
				if(a == null) m.setAnyObject(content.toArray());
				else a.setAnyObject(content.toArray());
			}
			return toXml(a != null ? a : m);
		}
		try {
			marshaller.setWriter(new OutputStreamWriter(bo /*, Charset.forName("UTF-8")*/));
			marshaller.marshal(msg);
		} catch (Exception e) {
			bo.reset();
			log.error("A(n) " + e.getClass().getName() + " occured while marshalling: {}", e.getMessage());
			log.debug("", e);
			e.printStackTrace();
		}
		return bo.toByteArray();
	}
	
	/*
		@SuppressWarnings("unchecked")
	public static <T extends IByteSerializable> T unmarshall(byte[] data, int offset, Class<? super T> type) {

		try {
			T res = (T) type.newInstance();
			return res.setFromBytes(data, offset) ? res: null;
		} catch (Exception e) {
			log.error("{} in unmarshall: {}", e.getClass().getSimpleName(), e.getMessage());
			log.debug("{}", e);
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	@SuppressWarnings("unchecked")
	public static <T> T getFromMessage(Message msg, Class<? super T> clazz) {
		if(msg != null && msg.getAnyObject() != null) {
			for(Object o : msg.getAnyObject()) {
				if(clazz.isAssignableFrom(o.getClass()))
					return (T)o;
			}
		}
		return null;
	}
	
	static Collection<EUI64Address> getMacsFromList(EslList esls) {
		Collection<EUI64Address> res = new HashSet<EUI64Address>();
		if(esls == null || esls.getMacCount() == 0) return res;
		for(byte[] b : esls.getMac()) {
			if(b.length >= EUI64Address.LENGTH)
				res.add(new EUI64Address(b));
		}
		return res;
	}
	
	static Collection<EslDeviceInfo> getEslsFromList(EslList esls) {
		Collection<EslDeviceInfo> res = new HashSet<EslDeviceInfo>();
		if(esls == null || esls.getEslItemCount() == 0) return res;
		for(EslItem e : esls.getEslItem()) {
			if(e.getMac() != null && e.getMac().length >= EUI64Address.LENGTH)
				res.add(
						new EslDeviceInfo(
								new EUI64Address(e.getMac()), 
								EslEntityManager.instance().lookupNetworkEquivalentType(e.getType()), 
								new EslInstallationKey(e.getInstallationKey())));
		}
		return res;
	}

	
}
