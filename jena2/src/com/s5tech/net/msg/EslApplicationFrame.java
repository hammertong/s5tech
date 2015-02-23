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

public class EslApplicationFrame extends AbstractSerializableFrame {

	private EslApplicationCommand command;
	private byte frameControl;
	
	public EslApplicationFrame() {
	}
	
	public EslApplicationCommand getCommand() {
		return command;
	}
	
	public void setCommand(EslApplicationCommand command) {
		this.command = command;
	}
	
	@Override
	public int getMaxPduLength() {
		return 65535*EslTransportFrame.MAX_PDU_FRAGMENT_LENGTH-1;
	}

	@Override
	protected int readHead(ByteBuffer src) {
		frameControl = src.get();
		command = EslApplicationCommand.typeOf(src.get());
		return 2;
	}

	@Override
	protected int readTail(ByteBuffer src) {
		return 0;
	}

	@Override
	protected int writeHead(ByteBuffer dest) {
		dest.put(frameControl);
		dest.put(command.value());
		return 1;
	}

	@Override
	protected int writeTail(ByteBuffer dest) {
		return 0;
	}

	@Override
	protected int getLengthOfHeadAndTail() {
		return 2;
	}

}
