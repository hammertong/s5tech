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

import java.nio.ByteBuffer;

import com.s5tech.net.type.AbstractSerializableFrame;
import com.s5tech.net.util.ByteBufferUtils;

/**
 * Security frame implementation
 * TODO actual security - currently only 'NONE' is supported
 * @author S5Tech Development Team
 *
 */
public class EslSecurityFrame extends AbstractSerializableFrame {

	private byte protocolVersion;
	private SecurityMode securityMode;
	private long frameCounter;
	private long mic;
	
	public EslSecurityFrame() {
		securityMode = SecurityMode.NONE;
	}
	
	@Override
	public int getMaxPduLength() {
		return EslTransportFrame.MAX_LENGTH;
	}
	@Override
	protected int readHead(ByteBuffer src) {
		byte securityControl = src.get();
		
		protocolVersion = (byte) (securityControl >> 5);
		securityMode = SecurityMode.typeOf((byte)((securityControl >> 3) & 0x03)); 

		if(SecurityMode.AES.equals(securityMode)) {
			frameCounter = ByteBufferUtils.readUInt32(src);
			return 5;
		} else {
			return 1;
		}
	}
	@Override
	protected int readTail(ByteBuffer src) {
		if(SecurityMode.NONE.equals(securityMode)) return 0;
		mic = ByteBufferUtils.readUInt32(src);
		return 4;
	}
	@Override
	protected int writeHead(ByteBuffer dest) {
		byte securityControl = (byte) (protocolVersion << 5);

		securityControl |= securityMode.value() << 3;

		dest.put(securityControl);
		
		if(SecurityMode.AES.equals(securityMode)) {
			ByteBufferUtils.writeUInt32(frameCounter, dest);
			return 5;
		} else {
			return 1;
		}
	}
	@Override
	protected int writeTail(ByteBuffer dest) {
		if(SecurityMode.AES.equals(securityMode)) {
			ByteBufferUtils.writeUInt32(mic, dest);
			return 4;
		} else {
			return 0;
		}
	}

	@Override
	protected int getLengthOfHeadAndTail() {
		return 1 + (SecurityMode.NONE.equals(securityMode) ? 0 : 8);
	}

	public byte getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(byte protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public SecurityMode getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(SecurityMode securityMode) {
		this.securityMode = securityMode;
	}

	public long getFrameCounter() {
		return frameCounter;
	}

	public void setFrameCounter(long frameCounter) {
		this.frameCounter = frameCounter;
	}

	public long getMic() {
		return mic;
	}

	public void setMic(long mic) {
		this.mic = mic;
	}
	
	/*
	Name                      Field                 Size         Description/Value(s)
	Security control          Protocol version      3 bit
	                                                             0 - Security disabled
	                          Security / Encryption 2 bit
	                                                             Neither auxiliary Security
	                          type
	                                                             header nor MIC is present in
	                                                             the Enhanced Frame Header.
	                                                             1 - CCM 128 bit AES
	                                                             Auxiliary Security header and
	                                                             MIC are both present in the
	                                                             Enhanced Frame Header.
	                                                             As specified
	                                                             2 - Reserved
	                                                             3 - Reserved 
	                          Reserved              3 bit
	Auxiliary Security header Frame Counter         4 bytes
	Security PDU                                    0 - 93 bytes
	MIC                                             4 bytes

	*/
	
	
}
