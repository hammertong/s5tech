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
 
package com.s5tech.net.esl;

import java.util.HashMap;
import java.util.Map;

public enum Hub2EslNetCtrlCommand {

	RESERVED(0x00),
	VALIDATE_DEVICE_AUTHORIZATION(0x01),
	DEVICE_AUTHORIZED(0X02),
	DATA_PENDING(0X03),
	PENDING_STATUS(0X04),
	ESL_MESSAGE(0X05),
	NUM_FREE_BUFFERS(0X06),
	REQ_IEEE_EUI(0X07),
	IEEE_EUI(0x08),
	REQ_ACTIVE_CHANNEL(0x09),
	ACTIVE_CHANNEL(0x0A),
	REQ_SET_CHANNEL_MASK(0x0B),
	CHANNEL_MASK(0x0C),
	SCAN_PROBE_NOTIFY(0x0D),
	REQ_FIRMWARE_VERSION(0x0E),
	FIRMWARE_VERSION(0x0F),
	REQ_NUM_FREE_BUFFERS(0X10),
	SET_TIME(0x11),
	FIRMWARE_DATA(0x12),
	ESL_LEAVE(0xFD),
	RESET(0xFE),
	UNKNOWN(0xFF)
	;

	static private Map<Byte,Hub2EslNetCtrlCommand> map;

	private byte value;
	
	private Hub2EslNetCtrlCommand(int value) {
		this.value = (byte) value;
	}
	
	synchronized public static Hub2EslNetCtrlCommand typeOf(byte value) {
		if(map == null) {
			map = new HashMap<Byte, Hub2EslNetCtrlCommand>();
			for (Hub2EslNetCtrlCommand cmd : values())
				map.put(cmd.value, cmd);
		}
		Hub2EslNetCtrlCommand res = map.get(value);
		if(res == null) res = UNKNOWN;
		return res;
	}

	public byte value() {
		return value;
	}
	
	
	/*
	Reserved                    0x00
ValidateDeviceAuthorization 0x01
DeviceAuthorized            0x02 8 bytes                IEEE EUI-64
                                  1 byte                Capability information (802.15.4
                                                        specified, with the slight modification that
                                                        the security bit will have the additional
                                                        meaning that the device already has a
                                                        store key.)
DataPending                 0x03 2 bytes                ESL network address
                                  2 bytes               (unsigned int) Number of pending
                                                        messages
PendingStatus               0x06 2 bytes                ESL network address Group Mask (ex.
                                                        0x001A)
Message                     0x04 Options/Framecontrol
                                  {
                                    3 bit               Version
                                    1 bit               16/64 bit destaddr
                                    1 bit               MAC Ack (enable retransmission)
                                    3 bit               Reserved
                                  }
                                  2/8 bytes             ESL address
                                  0-102 bytes           PDU
                            0x05 1 byte
NumFreeBuffers                                          (unsigned int) number of free data-
                                                        message buffers
                                  1 byte                (unsigned int) number of free slots in the
                                                        pending data list
                                  1 byte                (unsigned int) number of free slots in the
                                                        pending status list
ReqHENM_IEEE_EUI            0x06                        Request HENM IEEE EUI
AckHENM_IEEE_EUI            0x07 8 bytes                IEEE EUI-64 address of HENM
	*/
	
	
}
