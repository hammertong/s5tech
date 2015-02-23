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
 
package com.s5tech.net.firmware;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.entity.FirmwareVersion;
import com.s5tech.net.util.ByteBufferUtils;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

public class FirmwareInfo implements Serializable {

	private static final long serialVersionUID = 7713563652219633933L;

	public static final int HEADER_LENGTH = 18;
	
	private static Logger log;
	
	private FirmwareVersion version;
	private int deviceType;
	private int manufacturerId;
	private boolean valid;
	private File file;
	

	public FirmwareInfo() {
		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
		version = new FirmwareVersion();
	}

	public FirmwareVersion getVersion() {
		return version;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public int getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public boolean isValid() {
		return valid;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return "device type: 0x" + Tools.toHexByte(deviceType) +
					", version: " + version +
					", mfct id: " + manufacturerId +
					", file: " + (file == null ? "none" : file.getName());
	}
	
	public String toUnformattedString() {
		return Tools.toHexByte(deviceType) + " " 
				+ version 
				+ " " + manufacturerId 
				+ " " + (file == null ? "none" : file.getName());
	}
	
	/**
	 * Load device firmware from an input stream.
	 * Parses the header as specified in the ESL Protocol Specification, and performs some simple validations, such as specified length == actual length.
	 * @param fi The object to populate
	 * @param is the input stream to read from
	 * @return true on success. Otherwise failure
	 * @throws IOException
	 */
	static public boolean load(FirmwareInfo fi, InputStream is) throws IOException {
		
		boolean noCheckLength = (System.getProperty("firmware.nocheck") != null);

		if(fi == null) throw new IllegalArgumentException("Device firmware info must be set!!");
		
		byte[] arr = new byte[HEADER_LENGTH];
		is.read(arr);

		ByteBuffer bb = ByteBufferUtils.wrap(arr);
		
		int tmp = bb.get();
		if(tmp != 0x03) {
			fi.valid = false;
			log.warn("Wrong image type: 0x" + Tools.toHexByte(tmp));
			return false;
		}
		fi.version.read(bb);
		fi.manufacturerId = Tools.uByteToInt(bb.get());
		fi.deviceType = Tools.uByteToInt(bb.get());

		bb.get(); // skip page start
		// Read the size of the file
		int fileSize = ByteBufferUtils.readUInt16(bb) * 2; // The size is measured in words
		int dataSize = fileSize-HEADER_LENGTH;
		bb.position(bb.position()+4); // Skip magic a+b and crc16 of content
		// Read the crc of the file data
		ByteBufferUtils.readUInt16(bb);

		int count = 0;
		while(is.available() > 0) {
			is.read();
			count++;
		}
		
		if (noCheckLength) fi.valid = true;
		else fi.valid = count == dataSize;

		log.trace(Tools.toString(fi));

		return true;
	}
}
