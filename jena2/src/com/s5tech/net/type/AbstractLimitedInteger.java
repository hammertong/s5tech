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
 
package com.s5tech.net.type;

/**
 * @author S5Tech Development Team
 */
public abstract class AbstractLimitedInteger implements Comparable<AbstractLimitedInteger> {

	private int value;
	private int highestValue;
	private int lowestValue;

	/**
	 * 
	 */
	protected AbstractLimitedInteger(int lowestValue, int highestValue) {
		if(lowestValue > highestValue)
			throw new IllegalArgumentException("lowestValue cannot be a larger number than highestValue");
		this.lowestValue = lowestValue;
		this.highestValue = highestValue;
		value = lowestValue;
	}
	
	/**
	 * @param value the value to set
	 */
	final public void setValue(int value) {
		if(value > highestValue) value = highestValue;
		else if(value < lowestValue) value = lowestValue;
		this.value = value;
	}
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !getClass().equals(obj.getClass()))
			return false;
		return value == AbstractLimitedInteger.class.cast(obj).value;
	}
	
	@Override
	public int hashCode() {
		return value;
	}
	
	public int compareTo(AbstractLimitedInteger arg0) {
		if(arg0 == null) return 1;
		return value == arg0.value ? 0 : value > arg0.value ? 1 : -1;
	}
}
