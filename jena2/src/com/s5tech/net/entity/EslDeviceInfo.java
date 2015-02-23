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
 
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst space safe 
// Source File Name:   EslDeviceInfo.java

package com.s5tech.net.entity;

import com.s5tech.net.type.EUI64Address;
import com.s5tech.net.type.EslInfo;
import com.s5tech.net.type.NetworkAddress;
import com.s5tech.net.util.Tools;

// Referenced classes of package com.s5tech.net.desktop.entity:
//			EslInstallationKey

public class EslDeviceInfo extends EslInfo
{
	private static final long serialVersionUID = 0xdd2cc1c29bc13df1L;
	private int type;
	private EslInstallationKey installationKey;
	private boolean alarmEnabled;
	
	private NetworkAddress networkAddress;  
	
	private transient int securityCounterIn;
	private transient int securityCounterOut;

	private EUI64Address coordinatorMac;
	
	
	public EslDeviceInfo()
	{
	}

	public EslDeviceInfo(EUI64Address mac)
	{
		this(mac, 0, ((EslInstallationKey) (null)));
	}

	public EslDeviceInfo(EUI64Address mac, int type, EslInstallationKey installationKey)
	{
		super(mac);
		this.type = type;
		this.installationKey = installationKey;
	}

	public EslInstallationKey getInstallationKey()
	{
		return installationKey;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public void setInstallationKey(EslInstallationKey installationKey)
	{
		this.installationKey = installationKey;
	}

	public void setNetworkAddress(NetworkAddress networkAddress)
	{
		this.networkAddress = networkAddress;
	}

	public NetworkAddress getNetworkAddress()
	{
		return networkAddress;
	}

	public boolean isAlarmEnabled()
	{
		return alarmEnabled;
	}

	public void setAlarmEnabled(boolean alarmEnabled)
	{
		this.alarmEnabled = alarmEnabled;
	}

	public int getSecurityCounterIn()
	{
		return securityCounterIn;
	}

	public void setSecurityCounterIn(int securityCounterIn)
	{
		this.securityCounterIn = securityCounterIn;
	}

	public int getSecurityCounterOut()
	{
		return securityCounterOut;
	}

	public void setSecurityCounterOut(int securityCounterOut)
	{
		this.securityCounterOut = securityCounterOut;
	}

	public String toString()
	{
		return Tools.toString(((Object) (this)));
	}

	public EUI64Address getCoordinatorMac() {
		return coordinatorMac;
	}

	public void setCoordinatorMac(EUI64Address coordinatorMac) {
		this.coordinatorMac = coordinatorMac;
	}

	
	
	
	
}
