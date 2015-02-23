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

import java.util.HashMap;
import java.util.Map;

public enum EslApplicationCommand {

/*
Description                   Id   Size                      Description/Value(s)
Reserved                      0x00
EslData                       0x01 See Table 3               Binary format (See Table 3)   Kommentar [laj1]: All data
                                                                                           contained in Table 3 is in server
EslStatus                     0x02 1 byte                    Battery level
                                                                                           binary format
                                   1 byte                    LQI for the associated HUB
                                   1 byte                    Temperature
                                   1 byte                    Firmware version
                                   2 bytes                   Remaining hours
                                   4 bytes                   Hash code for Active price
                                                             data
                                   4 bytes                   Hash code for Pending price
                                                             data
                                   1 bit                     0 – No Rail alarm
                                                             1 – Rail alarm
                                   1 bit                     0 – Night mode off
                                                             1 – Night mode on             Kommentar [FP2]: The upstream
                                                                                           xml message EslStatus to the
                                   1 bit                     0 – Storage mode off
                                                                                           server do not convey this
                                                             1 – Storage mode on
                                                                                           information. Should we add this
                                   1 bit                     reserved                      information to the xml message?
NB!                                4 bit                     Num of alternate hubs in
                                                                                           Kommentar [laj3]: Yes
ESL must send a status before                                range
going into storage mode.           0 – Num of alternate hubs
                                   in rangede
                                   {
                                     8 bytes                 EUI64 of hub in range
                                     1 byte                  LQI
                                   }                                                       Kommentar [FP4]: This command
                                                                                           should include the activationTime
SetNightMode                  0x03 1 bit                     0 – Night mode off
                                                                                           and duration as well. Both
                                                             1 – Night mode on
                                                                                           activationTime and duration are
                                   7 bit                     Reserved                      already included in the
          EslStatus                         4 bytes                   Activation time in sec. Since downstream xml message
                                                                                           EslEnterNightMode which is
                                                             01012000
                                                                                           coming from the server.
                                   4 bytes                   Duration time in sec. Since
                                                                                           Kommentar [laj5R4]: updated
                                                             01012000
SetStorageMode                0x04 -                                                       Kommentar [FP6]: My
N_KeyUpdate                   0x05 8 bytes                   EUI-64 IEEE network key       understanding is that this is a
                                                                                           downstream message sent only
S_KeyUpdate                   0x06 8 bytes                   EUI-64 IEEE store key
                                                                                           once in the ESL lifetime. Please
UpdateFirmware                0x07 TBD                                                     confirm.
	
	*/
	
	RESERVED(0),
	DATA(0x01),
	STATUS(0x02),
	SET_NIGHT_MODE(0x03),
	SET_STORAGE_MODE(0x04),
	KEY_UPDATE_N(0x05),
	KEY_UPDATE_S(0x06),
	UPDATE_FIRMWARE(0x07),
	SET_TIME(0x08),
	SET_CHANNEL_TO_JOIN(0x09),
	SET_ACTIVE_SERVICE_PAGE(0x0A),
	READ_STATISTICS(0x0B),
	UNKNOWN(0xFF);

	static private Map<Byte,EslApplicationCommand> map;
	private byte value;

	private EslApplicationCommand(int value) {
		this.value = (byte) value;		
	}
	
	synchronized public static EslApplicationCommand typeOf(byte value) {
		if(map == null) {
			map = new HashMap<Byte, EslApplicationCommand>();
			for (EslApplicationCommand cmd : values())
				map.put(cmd.value, cmd);
		}
		EslApplicationCommand res = map.get(value);
		if(res == null) res = UNKNOWN;
		return res;
	}

	public byte value() {
		return value;
	}	
	
}
