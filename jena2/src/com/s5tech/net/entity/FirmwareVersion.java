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
 
package com.s5tech.net.entity;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.s5tech.net.type.ISerializable;
import com.s5tech.net.util.Tools;

@SuppressWarnings("serial")
public class FirmwareVersion implements ISerializable, Comparable<FirmwareVersion>, Serializable {

	private int major;
	private int minor;
	private int maintenance;
	private boolean debug;
	private int build;
	private boolean updated;
	private String string;

	public FirmwareVersion() {
		this(0,0,0,false,0);
	}
	
	public FirmwareVersion(int major, int minor, int maintenance) {
		this(major,minor,maintenance,false,0);
	}

	public FirmwareVersion(int major, int minor, int maintenance, boolean debug) {
		this(major,minor,maintenance,debug,0);
	}

	public FirmwareVersion(int major, int minor, int maintenance, boolean debug, int buildNumber) {
		setMajor(major);
		setMinor(minor);
		setMaintenance(maintenance);
		this.debug = debug;
		build = buildNumber;
		updated = true;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		updated = true;
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		updated = true;
		this.minor = minor;
	}

	public int getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(int maintenance) {
		updated = true;
		this.maintenance = maintenance;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		updated = true;
	}

	public int getBuild() {
		return build;
	}
	
	public void setBuild(int build) {
		this.build = build;
	}

	public int compareTo(FirmwareVersion o) {
		return compareTo(o, false);
	}

	/**
	 */
	public int compareTo(FirmwareVersion o, boolean ignoreBuildNumber) {
		if(o == null) return 1;
		
		if(major > o.major) return 1;
		if(major < o.major) return -1;
		if(minor > o.minor) return 1;
		if(minor < o.minor) return -1;
		if(maintenance > o.maintenance) return 1;
		if(maintenance < o.maintenance) return -1;
		
		if(debug != o.debug) return debug ? -1 : 1;
		
		if(!ignoreBuildNumber && build != o.build) return build > o.build ? 1 : -1;
		
		return 0;
	}
	
	@Override
	public synchronized String toString() {
		if(updated) {
			string = major + "." + minor + "." + maintenance + (debug ? "D" : "R") + build;
			updated = false;
		}
		return string;
	}

	@Override
	public int hashCode() {
		return -287623120;		
	};
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FirmwareVersion)) return false;
		FirmwareVersion fw = FirmwareVersion.class.cast(obj);
		return major == fw.major && 
					minor == fw.minor && 
					maintenance == fw.maintenance && 
					(debug == fw.debug && build == fw.build);
	}
	
	public int length() {
		return 4;
	}

	public int write(ByteBuffer dest) {
		dest.put((byte)major);
		dest.put((byte)minor);
		dest.put((byte)maintenance);
		byte tmp = (byte)(debug ? 0x80 : 0);
		tmp |= (build & 0x7F);
		dest.put(tmp);
		return length();
	}

	public boolean read(ByteBuffer src) {
		return read(src, length());
	}

	public boolean read(ByteBuffer src, int length) {
		major = Tools.uByteToInt(src.get());
		minor = Tools.uByteToInt(src.get());
		maintenance = Tools.uByteToInt(src.get());
		
		byte b = src.get();
		debug = (b & 0x80) > 0;
		build = (b & 0x7F);
		
		return true;
	}
}
