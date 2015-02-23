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
 
package com.s5tech.net.util;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.type.AbstractByteArrayType;


/**
 * Helper class which contains a number of generic methods.
 * 
 * @author $Author: mka $
 * @version $Revision: 1.2 $, $Date: 2010/10/15 10:17:14 $
 */
public class Tools {

	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	public static String getBasedir() {		
		try {
			return new File (".").getCanonicalPath() + "/";
		}
		catch (Throwable t) {
			log.error("unable to retrieve current directory path");
			return "./";
		}		
	}	
	
	public static long get20000101BasedTime() {
		return get20000101BasedTime(System.currentTimeMillis());
	}
	
	/**
	 * Basically subtracts 946684800 seconds from the supplied value
	 * @param currentTimeMillis
	 * @return the number of seconds elapsed since Jan. 1st. 2000 UTC.
	 */
	public static long get20000101BasedTime(long currentTimeMillis) {
		if(currentTimeMillis <= 0)
			currentTimeMillis = System.currentTimeMillis();
		return (currentTimeMillis/1000L) - 946684800L;
	}
	
	public static long getTimeFrom20000101BasedTime(long timestamp) {
		return (timestamp + 946684800L) * 1000L;
	}
	
	/**
	 * Convert a stack trace to a string consisting of one line per {@link StackTraceElement}
	 * @param t The {@link Throwable} containing the stacktrace to convert.
	 * @return The converted stacktrace
	 */
	public static String getStacktraceString(Throwable t) {
		StringBuffer res = new StringBuffer();

		if (t != null) {
			StackTraceElement[] st = t.getStackTrace();
			for (StackTraceElement s : st)
				res.append(s.toString()).append(EOL);
		}

		return res.toString();
	}

	/**
	 * Return the first non-null object in the given list.
	 * @param objects A list of potentially null objects
	 * @return The first object that isn't a null-reference, or null if all nulls.
	 */
	public static Object firstNotNull(Object... objects) {

		Object res = null;
		for (Object o : objects) {
			if (o != null) {
				res = o;
				break;
			}
		}
		return res;
	}

	/**
	 * Return a {@link String} with a specific with.
	 * @param str The string to convert.
	 * @param width The with of the output string
	 * @param rightpad The char to pad the string width, if the desired output length is longer than the input length
	 * @return The fixed-width {@link String}
	 */
	public static String makefixedWidth(String str, int width, char rightpad) {

		char[] chs = new char[width];

		int len = width >= str.length() ? str.length() : width;

		Arrays.fill(chs, rightpad);
		System.arraycopy(str.toCharArray(), 0, chs, 0, len);

		return String.copyValueOf(chs);
	}

	/**
	 * Convert a list to a String containing the list elements
	 * @param list The list to convert
	 * @param separator The string to separate the list items with
	 * @return A string containing the elements, separated by the specified separator
	 */
	public static String listToString(Iterable<?> list, String separator) {
		StringBuilder sb = new StringBuilder();

		if (list != null && separator != null) {
			for (Object obj : list)
				sb.append(toString(obj)).append(separator);
			if(sb.length() > separator.length())
				sb.delete(sb.length() - separator.length(), sb.length());
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param arr
	 * @param delimiter
	 * @return
	 */
	public static String arrayToString(Object[] arr, String delimiter) {
		if(arr == null || arr.length == 0) return "";
		if(delimiter == null) delimiter = " ";
		StringBuilder sb = new StringBuilder(arr[0].toString());
		for(int i=1 ; i < arr.length ; i++)
			sb.append(delimiter).append(arr[i]);
		return sb.toString();
	}

	/**
	 * Checks that all parameters are not null
	 * @param objs
	 * @return
	 */
	public static boolean allNotNull(Object... objs) {
		for(Object o : objs)
			if(o == null) return false;
		return true;
	}
	
	/**
	 * Check whether a {@link String} reference is null or empty
	 * @param string The {@link String} to check.
	 * @return true if the string reference is null or the String is empty.
	 */
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * A generic result class containing a success fiels and a value field
	 * @param <T> The type to contain in the value field
	 */
	public static class DecodeResult<T> {
		public boolean success;
		public T value;
	}

	/**
	 * Attempt to parse the specified string as an int.
	 * @param intValue The string to parse, may begin with 0x if radix 16
	 * @param radix the radix to use when parsing the string
	 * @return a result class with success == true on success, the value field is undefined on error
	 */
	public static DecodeResult<Long> tryParseLong(String longValue, int radix) {
		DecodeResult<Long> res = new DecodeResult<Long>();
		res.success = false;
		if (longValue != null && longValue != "") {
			if (radix == 16)
				longValue = longValue.replaceFirst("0x", "");
			try {
				res.value = Long.parseLong(longValue, radix);
				res.success = true;
			} catch (NumberFormatException e) {
				res.success = false;
				// Suppress NumberFormatExceptions
			}
		}
		return res;
	}
	
	public static DecodeResult<Integer> tryParseInt(String intValue, int radix) {
		DecodeResult<Integer> res = new DecodeResult<Integer>();
		DecodeResult<Long> parseLong = tryParseLong(intValue, radix);
		res.success = parseLong.success;
		if(parseLong.value != null)
			res.value = (int)parseLong.value.longValue();
		return res;
	}
	

	/**
	 * The current line separator of the system.
	 * Obtained by getting the system property "line.separator"
	 */
	public static String EOL = System.getProperty("line.separator");
	
	/**
	 * Converts an integer to hex representation
	 * @param value the integer to convert
	 * @param bytecount The number of bytes to use for representing the value 
	 * @param bigEndian if true, the value will be represented as MSB
	 * @return a string with value represented as hex
	 */
	public static String toHexString(int value, int bytecount, boolean bigEndian) {
		byte[] bytes = new byte[bytecount];
		insertIntAsBytes(value, bytes, 0, bytecount, bigEndian);
		return toHexString(bytes);
	}

	/**
	 * Convert a byte array to hex representation
	 * @param arr The array to convert
	 * @return a String containing the hex values
	 */
	public static String toHexString(byte[] arr) {
		return arr == null ? "" : toHexString(arr, 0, arr.length, "");
	}

	/**
	 * Convert a byte array to hex representation
	 * @param arr The array to convert
	 * @param delimiter a string to insert between the hex value of each byte
	 * @return a String containing the hex values separated by the delimiter
	 */
	public static String toHexString(byte[] arr, String delimiter) {
		return arr == null ? "" : toHexString(arr, 0, arr.length, delimiter);
	}

	/**
	 * Convert part of a byte array to hex representation
	 * @param arr The array to convert
	 * @param start The index to begin reading at
	 * @param length The amount of bytes to read
	 * @return a String containing the hex values
	 */
	public static String toHexString(byte[] arr, int start, int length) {
		return toHexString(arr, start, length, "");
	}

	/**
	 * Convert part of a byte array to hex representation
	 * @param arr The array to convert
	 * @param start The index to begin reading at
	 * @return a String containing the hex values
	 */
	public static String toHexString(byte[] arr, int start) {
		return toHexString(arr, start, arr.length - start, "");
	}

	/**
	 * Convert part of a byte array to hex representation
	 * @param arr The array to convert
	 * @param start The index to begin reading at
	 * @param length The amount of bytes to read
	 * @param delimiter a string to insert between the hex value of each byte
	 * @return a String containing the hex values separated by the delimiter
	 */
	public static String toHexString(byte[] arr, int start, int length,
			String delimiter) {
		if(arr == null) return "";
		StringBuffer sb = new StringBuffer(length * (2 + delimiter.length())
				- delimiter.length());
		length = length + start;
		for (int i = start; i < length; i++)
			appendToHexString(arr[i], sb).append(delimiter);
		return sb.substring(0, sb.length() - delimiter.length());
	}

	/**
	 * Append the hex representation of a byte to a {@link StringBuffer}
	 * @param b The byte to append
	 * @param buffer The {@link StringBuffer} to append the byte to
	 * @return The {@link StringBuffer} in order to allow chaining of methods
	 */
	public static StringBuffer appendToHexString(byte b, StringBuffer buffer) {
		buffer.append(hexChar[(b & 0xf0) >>> 4]);
		buffer.append(hexChar[b & 0x0f]);
		return buffer;
	}

	/**
	 * Get the hex representation of a single value
	 * @param b The integer value to represent as hex
	 * @return the hex representation of the specified integer
	 */
	public static String toHexByte(int b) {
		return appendToHexString((byte)b, new StringBuffer(2)).toString();
	}

	/******************************************************************************************
	 * Table to convert a nibble to a hex char.
	 *****************************************************************************************/
	static public char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	

	/**
	 * Wait for the specified amount of time, or until the objects notify is
	 * called
	 * 
	 * @param timeoutMillis
	 * @return true if the wait wasn't interrupted
	 */
	public static boolean doWait(Object obj, long timeoutMillis) {
		boolean res = false;
		if (obj != null)
			try {
				synchronized (obj) {
					obj.wait(timeoutMillis);
				}
				res = true;
			} catch (InterruptedException e) {
			}
		return res;
	}

	/**
	 * Wait for the specified amount of time, or until the objects notify is
	 * called
	 * 
	 * @param timeout
	 */
	public static void doActivate(Object obj) {
		if (obj != null)
			synchronized (obj) {
				obj.notifyAll();
			}
	}

	/**
	 * Converts a hex representation of a byte to a byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte fromHexString(String value) {
		if (value.length() > 1)
			return (byte) Integer.parseInt(value.substring(0, 2), 16);
		else
			return 0;
	}

	/**
	 * Convert a string containing only hex values to a byte array
	 * If the length of the string is not even, the last char will not be used.
	 * @param hexString the string containing hex values
	 * @return the resulting byte array. Null if an error occurs during the process.
	 */
	public static byte[] hexStringToByteArray(String hexString) {
		return hexStringToByteArray(hexString, false);
	}

	/**
	 * Convert a string containing hex values to a byte array
	 * If the length of the string is not even, the last char will not be used.
	 * @param hexString the string containing hex values
	 * @param autoSkipInvalidChars if true, all non-hex character will be skipped.
	 * @return the resulting byte array. Null if an error occurs during the process.
	 */
	public static byte[] hexStringToByteArray(String hexString,
			boolean autoSkipInvalidChars) {
		if (hexString == null || hexString == "")
			return null;

		hexString = hexString.toUpperCase();
		if (hexString.startsWith("0X"))
			hexString = hexString.substring(2);

		List<Byte> out = new ArrayList<Byte>();

		try {
			for (int cnt = 0; cnt < hexString.length(); cnt++) {
				if (autoSkipInvalidChars) {
					if(!isValidHexChar(hexString.charAt(cnt)) || cnt == hexString.length()-1 || !isValidHexChar(hexString.charAt(cnt+1))) {
						continue;
					}
				}
				// Expect two character for each byte
				if (cnt > hexString.length() - 2)
					break;
				out.add((byte) Integer.parseInt(hexString.substring(cnt,
						cnt + 2), 16));
				cnt++;
			}
		} catch (NumberFormatException e) {
			out = null;
			e.printStackTrace();
		}

		byte[] output = null;

		if (out != null) {
			output = new byte[out.size()];
			int cnt = 0;
			for (Byte b : out)
				output[cnt++] = b.byteValue();
		}

		return output;
	}

	private static boolean isValidHexChar(char c) {
		c = Character.toUpperCase(c);
		return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
	}
	
	/**
	 * Insert an integer value into a byte array
	 * @param value The value to insert
	 * @param array the array to insert the value into
	 * @param offset the position to insert the value at
	 * @param bytecount the number of byte to be used for representing the value
	 * @param bigEndian if true the value will be represented with the most significant byte first (MSB) other wise as LSB
	 * @return the specified byte array, to allow chaining of method calls
	 */
	public static byte[] insertIntAsBytes(int value, byte[] array, int offset,
			int bytecount, boolean bigEndian) {
		return insertLongAsBytes(value, array, offset, bytecount, bigEndian);
	}

	/**
	 * Insert a long value into a byte array
	 * @param value The value to insert
	 * @param array the array to insert the value into
	 * @param offset the position to insert the value at
	 * @param bytecount the number of byte to be used for representing the value
	 * @param bigEndian if true the value will be represented with the most significant byte first (MSB) other wise as LSB
	 * @return the specified byte array, to allow chaining of method calls
	 */
	public static byte[] insertLongAsBytes(long value, byte[] array,
			int offset, int bytecount, boolean bigEndian) {
		// Validate the input values
		if (offset + bytecount > array.length)
			return array;

		int shiftCount;
		int shiftCountIncrement;

		if (bigEndian) {
			shiftCount = (bytecount - 1) * 8;
			shiftCountIncrement = -8;
		} else {
			shiftCount = 0;
			shiftCountIncrement = 8;
		}

		for (int cnt = offset; cnt < bytecount + offset; cnt++) {
			array[cnt] = (byte) ((value >> shiftCount) & 0xff);
			shiftCount += shiftCountIncrement;
		}
		return array;
	}

	/**
	 * Insert the specified int into a new array with the length bytecount
	 * @param value The value to convert
	 * @param bytecount The number of bytes to use for representing the value 
	 * @param bigEndian if true, the value will be represented as MSB
	 * @return A byte array containing a representation of the specified value.
	 */
	public static byte[] intToBytes(int value, int bytecount, boolean bigEndian) {
		return insertIntAsBytes(value, new byte[bytecount], 0, bytecount,
				bigEndian);
	}

	/**
	 * Insert the specified long into a new array with the length bytecount
	 * @param value The value to convert
	 * @param bytecount The number of bytes to use for representing the value 
	 * @param bigEndian if true, the value will be represented as MSB
	 * @return A byte array containing a representation of the specified value.
	 */
	public static byte[] longToBytes(long value, int bytecount, boolean bigEndian) {
		return insertLongAsBytes(value, new byte[bytecount], 0, bytecount, bigEndian);
	}
	
	/**
	 * Convert a binary string (eg. "10010010") to a byte (0x92)
	 * @param str The string containing the binary value
	 * @return a byte read from the specified string
	 */
	public static byte binaryStringToByte(String str) {
		return (byte) Integer.parseInt(str, 2);
	}

	/**
	 * Convert a byte to an integer value, as if the byte is unsigned
	 * @param b The byte to convert
	 * @return The integer value corresponding to the unsigned value of the specified byte
	 */
	public static int uByteToInt(byte b) {
		return b >= 0 ? b : (b + 256);
	}

	/**
	 * Convert a byte to a long value, as if the byte is unsigned
	 * @param b The byte to convert
	 * @return The long value corresponding to the unsigned value of the specified byte
	 */
	public static long uByteToLong(byte b) {
		return b >= 0 ? b : (b + 256L);
	}

	/**
	 * Converts a byte to a string of 1's and 0's
	 * @param value The value to convert
	 * @return a string containing the binary representation of the specified byte
	 */
	public static String toBinaryString(byte value) {
		StringBuilder sb = new StringBuilder(8);
		return toBinaryString(value, sb, false).toString();
	}

	/**
	 * Converts a byte array to a string of 1's and 0's
	 * @param bytes The array to convert
	 * @return a string containing the binary representation of the specified bytes
	 */
	public static String toBinaryString(byte[] bytes) {
		return toBinaryString(bytes, false);
	}

	/**
	 * Converts a byte array to a string of 1's and 0's
	 * @param bytes The array to convert
	 * @param reverse insert the bits of each byte in reverse order
	 * @return a string containing the binary representation of the specified bytes
	 */
	public static String toBinaryString(byte[] bytes, boolean reverse) {

		if (bytes == null || bytes.length == 0)
			return "";

		StringBuilder sb = new StringBuilder(bytes.length * 8);

		for (byte b : bytes) {
			toBinaryString(b, sb, reverse);
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param value
	 * @param sb
	 * @param reverse
	 * @return
	 */
	private static StringBuilder toBinaryString(byte value, StringBuilder sb,
			boolean reverse) {

		if (reverse) {
			for (int cnt = 0; cnt < 8; cnt++) {
				if (((value >> cnt) & 1) != 0)
					sb.append("1");
				else
					sb.append("0");
			}
		} else {
			for (int cnt = 7; cnt >= 0; cnt--) {
				if (((value >> cnt) & 1) != 0)
					sb.append("1");
				else
					sb.append("0");
			}
		}

		return sb;
	}

	/**
	 * Read an integer value from the specified byte array 
	 * @param array The array to read the value from
	 * @param offset The position to begin reading at
	 * @param length the number of bytes to read.
	 * @param bigEndian read the value as MSB, or alternatively as LSB
	 * @return
	 */
	public static int getIntFromBytes(byte[] array, int offset, int length,
			boolean bigEndian) {
		return (int) getLongFromBytes(array, offset, length, bigEndian);
	}

	/**
	 * Read a long value from the specified byte array 
	 * @param array The array to read the value from
	 * @param offset The position to begin reading at
	 * @param length the number of bytes to read.
	 * @param bigEndian read the value as MSB, or alternatively as LSB
	 * @return
	 */
	public static long getLongFromBytes(byte[] array, int offset, int length,
			boolean bigEndian) {
		if (array.length < (offset + length))
			return offset;

		long result = 0;
		long shiftCount;
		long shiftCountIncrement;

		if (bigEndian) {
			shiftCount = ((length - 1) * 8);
			shiftCountIncrement = -8;
		} else {
			shiftCount = 0;
			shiftCountIncrement = 8;
		}

		for (int cnt = offset; cnt < (offset + length); cnt++) {
			result += uByteToLong(array[cnt]) << shiftCount;
			shiftCount += shiftCountIncrement;
		}

		return result;
	}

	/**
	 * Converts a boolean to a "yes" or "no" string
	 * @param value the value to interpret.
	 * @return yes if the value was true, "no" if it was false.
	 */
	public static String booleanToYesNo(boolean value) {
		if (value)
			return "yes";
		else
			return "no";
	}

	/**
	 * Reads bytes as chars in the supplied array, until 0x00 or 0xff or end of array is met.
	 * @param data the buffer to read the string from.
	 * @param offset The position to begin reading at.
	 * @return The resulting string
	 */
	public static String getTerminatedStringFromBytes(byte[] data, int offset) {
		if (data == null || offset < 0 || offset >= data.length)
			return "";

		StringBuilder sb = new StringBuilder();
		int ch;
		// Read until the null termination is reached.
		// Also stop at 0xff, which indicates non-written flash
		while (offset < data.length && (ch = uByteToInt(data[offset++])) > 0x00
				&& ch < 0xff) {
			sb.append((char) ch);
		}

		if (sb.length() > 0)
			return sb.toString();

		return "";
	}

	/**
	 * Insert a String into a byte array. Nothing fancier than a cast from char
	 * to byte is done.
	 * 
	 * @param value The string to insert
	 * @param array The array to insert the string into
	 * @param offset The position to begin inserting the string at.
	 * @param endIndex
	 *            The index to stop at. If this is greater than offset +
	 *            value.length(), 0-padding is applied
	 * @return The specified byte array
	 */
	public static byte[] insertStringAsBytes(String value, byte[] array,
			int offset, int endIndex) {
		if (value == null || array == null || "".equals(value))
			return array;

		char[] chars = value.toCharArray();

		for (int cnt = 0; cnt < chars.length && offset < array.length; cnt++)
			array[offset++] = (byte) chars[cnt];

		// Pad with zeros if needed
		while (offset < endIndex && offset < array.length)
			array[offset++] = 0;

		return array;
	}

	/**
	 * Insert a String into a byte array. Nothing fancier than a cast from char
	 * to byte is done.
	 * 
	 * @param value The string to insert
	 * @param array The array to insert the string into
	 * @param offset The position to begin inserting the string at.
	 * @return The specified byte array
	 */
	public static byte[] insertStringAsBytes(String value, byte[] array,
			int offset) {
		return insertStringAsBytes(value, array, offset, 0);
	}

	/**
	 * Convert the specified string to a byte array of the same length.
	 * The conversion uses ASCII-8.
	 * @param value The string to convert to bytes
	 * @return the resulting byte array
	 */
	public static byte[] stringToBytes(String value) {
		if (value == null || "".equals(value))
			return new byte[0];

		return insertStringAsBytes(value, new byte[value.length()], 0);
	}

	/**
	 * Convert the specified string to a byte array of the same length.
	 * The conversion uses ASCII-8.
	 * @param value The string to convert to bytes
	 * @param byteCount the size of the resulting byte array
	 * @return the resulting byte array
	 */
	public static byte[] stringToBytes(String value, int byteCount) {

		if (value == null || value == "" || byteCount == 0)
			return new byte[0];

		byte[] res = new byte[byteCount];
		if (value.length() < byteCount)
			Arrays.fill(res, (byte) 0);

		return insertStringAsBytes(value, res, 0);
	}

	/**
	 * Compare two byte arrays by checking of null, comparing the lengths and the content.
	 * @param arr1 The first array to compare
	 * @param arr2 The second array to compare
	 * @return true of the arrays are equal.
	 */
	public static boolean equalArrays(byte[] arr1, byte[] arr2) {
		if (arr1 == null || arr2 == null || arr1.length != arr2.length)
			return false;
		for (int cnt = 0; cnt < arr1.length; cnt++)
			if (arr1[cnt] != arr2[cnt])
				return false;
		return true;
	}

	/**
	 * XORs a number of bytes and returns the result
	 * @param array the array to read the bytes from
	 * @param offset the position of the first byte
	 * @param endIdx the position of the last byte
	 * @return the resulting XOR value
	 */
	public static byte calculateXORChecksum(byte[] array, int offset, int endIdx) {
		byte chk = array[offset++];
		while (offset <= endIdx)
			chk ^= array[offset++];
		return chk;
	}

	private static Random rnd;

	/**
	 * Inserts random bytes into a byte array
	 * @param buffer The byte array to insert the bytes into.
	 * @param start The position to insert the bytes at
	 * @param length The number of bytes to insert.
	 */
	public static void insertRandomBytes(byte[] buffer, int start, int length) {
		System.arraycopy(getRandomBytes(length), 0, buffer, start, length);
	}

	/**
	 * Creates random bytes
	 * @param count The size of the resulting byte array
	 * @return A byte array containing random values.
	 */
	public static byte[] getRandomBytes(int count) {
		if (rnd == null)
			rnd = new Random(System.currentTimeMillis());
		byte[] b = new byte[count];
		rnd.nextBytes(b);
		return b;
	}

	/**
	 * Get a random long value
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return The resulting value
	 */
	public static long random(long min, long max) {
		double tmp;
		do {
			tmp = Math.random() * max;
		} while (tmp < min);
		return (long) tmp;
	}

	/**
	 * Return an object that will invoke {@link Tools#toString(Object)} on an object when it's own toString method is invoked.
	 * @param o
	 * @return
	 */
	public static Object toStringObj(final Object o) {
		return new Object() {
			@Override
			public String toString() {
				return Tools.toString(o);
			}
		};
	}
	
	/**
	 * Converts various objects to a string representation. Defaults to
	 * org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(Object,
	 * ToStringStyle.SHORT_PREFIX_STYLE)
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		String res;
		if(obj == null) {
			return "null";
		} else if((obj instanceof String) || (obj instanceof AbstractByteArrayType)) {
			return obj.toString();
		} else if((obj instanceof ByteBuffer)) {
			ByteBuffer b = ByteBuffer.class.cast(obj);
			if(b.hasArray()) {
				res = toHexString(b.array(), b.position(), b.remaining());
			} else {
				res = ToStringBuilder.reflectionToString(b);
			}
		} else if (obj.getClass().isArray() && Byte.TYPE.equals(obj.getClass().getComponentType())) {
			res = toHexString((byte[]) obj);
		} else if (obj instanceof Collection<?>) {
			res = listToString((Collection<?>) obj, ",");
		} else {
			res = ToStringBuilder.reflectionToString(obj,
					ToStringStyle.SHORT_PREFIX_STYLE);
		}
		return res;
	}

	/**
	 * Ensures that a string isn't longer than allowed by cutting it if it's the case
	 * @param string The string to be evaluated
	 * @param maxLength The maximum length of the resulting string
	 * @param cutBeginning if true, the beginning of the supplied string is removed if it's too long. If false the end is removed. 
	 * @return A string with a length <= maxLength
	 */
	public static String ensureMaxStringLength(String string, int maxLength, boolean cutBeginning) {
		if(string == null || maxLength < 0)
			return string;
		
		if(string.length() <= maxLength)
			return string;
		
		return cutBeginning ? string.substring(string.length()-maxLength) : string.substring(0, maxLength); 
	}

	
	/**
	 * @param str	A string with a type of chars
	 * @return		Same string but with all non numeric chars removed
	 */
	public static String removeNonNumericChars(String str) {
		return str.replaceAll("[^0-9]", "");
	}

	/**
	 * Reverses an array
	 * 
	 * @param arrayToReverse the array to reverse
	 */
	public static void reverseByteArray(byte[] arrayToReverse) {
		int left = 0; // index of leftmost element
		int right = arrayToReverse.length - 1; // index of rightmost element

		while (left < right) {
			// exchange the left and right elements
			byte temp = arrayToReverse[left];
			arrayToReverse[left] = arrayToReverse[right];
			arrayToReverse[right] = temp;
			// move the bounds toward the center
			left++;
			right--;
		}
	}
}
