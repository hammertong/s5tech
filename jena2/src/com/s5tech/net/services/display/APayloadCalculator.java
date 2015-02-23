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
 
package com.s5tech.net.services.display;

import java.io.ByteArrayInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.castor.core.util.Base64Encoder;

public abstract class APayloadCalculator 
{	
	
	protected static DateFormat timeFormat = null;
	
	protected static final long OFFSET_20000101_MS = 946684800000L;
	
	static 
	{
		timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT-0"));
	}
	
	static final int MIN_PADDING = 3;
	static final int BLOCK_SIZE = 16;
	static final int HASH_LENGHT = 4; // By convention is the number of bytes starting from the end of the resulting hash.
	
	public long getLowEndianUInt(final byte[] data, int offset, int len) {
		long result = 0;
		for (int j = offset + len - 1; j >= 0; j--) {
			result <<= 8;
			result |= 0xff & data[j];
		}
		return result;
	}
	
	protected long getLongFromBytes(byte[] array, int offset, int length,
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
	
	protected long uByteToLong(byte b) {
		return b >= 0 ? b : (b + 256L);
	}
		
	protected byte[] insertLongAsBytes(long value, byte[] array,
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
	
	public byte[] calculateEslHash(byte[] pData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{	
		byte[] mmoHash = matyasMeyerOseas(pData.length, pData);
        byte[] finalHash = new byte[HASH_LENGHT];
        for (int indx = 0; indx <= HASH_LENGHT - 1; indx++)
            finalHash[indx] = mmoHash[indx];
        return finalHash;	
	}
	
	protected byte[] matyasMeyerOseas(int length, byte[] pData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        int i;
        int bitLength = length * 8;
        int lenLastBlock = (length % BLOCK_SIZE); // The remainder of division.
        int numBaseBlocks = length / BLOCK_SIZE;
        int numTmpBlocks = (lenLastBlock + MIN_PADDING > BLOCK_SIZE ? 2 : 1); // always 1 or 2
        byte[] tmpData = new byte[2 * BLOCK_SIZE];
       
        Arrays.fill(tmpData, (byte)0x00); // Must be 0x00 filled.
        ByteArrayInputStream ms = new ByteArrayInputStream(pData, length - lenLastBlock, lenLastBlock);
                
        ms.read(tmpData, 0, lenLastBlock);
        i = MIN_PADDING;
        while (--i > 0)
        {
            tmpData[(numTmpBlocks * BLOCK_SIZE) - i] = (byte)((bitLength >> ((i - 1) * 8)) & 0xFF);
        }
        tmpData[lenLastBlock] = (byte) 0x80; // Or?

        byte[] pResult = new byte[BLOCK_SIZE];
        Arrays.fill(pResult, (byte)0x00); 
        numBaseBlocks += numTmpBlocks;
        i = 0;
        byte[] dataToEncrypt = null;
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        			 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        
        do
        {
            if (numBaseBlocks == numTmpBlocks) 
            {
                dataToEncrypt = Arrays.copyOf(tmpData, BLOCK_SIZE);
            }
            else 
            {
                dataToEncrypt = Arrays.copyOfRange(pData, i * BLOCK_SIZE, i * BLOCK_SIZE + BLOCK_SIZE + 1);
            }
            
            SecretKeySpec keySpec = new SecretKeySpec(pResult, "AES");            
            IvParameterSpec ivSpec = new IvParameterSpec((i == 0 ? iv: cipher.getIV()));
    		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    		
    		pResult = cipher.update(dataToEncrypt);

            for (int indx = 0; indx < BLOCK_SIZE; indx++)
                pResult[indx] ^= dataToEncrypt[indx];

            i++;
        } while (--numBaseBlocks > 0);

        return pResult;
    }
	
	public void printData(byte[] data, byte[] hashcode, int activationTimePosition, String format)
	{
		final int SPLIT_B64_SIZE = 64;
		
		long l = getLongFromBytes(data, activationTimePosition, 4, false);
		l *= 1000;
		l += OFFSET_20000101_MS;
				
		String activationTime = timeFormat.format(new Date(l));
		
		if (format != null && format.equalsIgnoreCase("xml"))
		{
			System.out.print("<activationtime>");
			System.out.print(activationTime);
			System.out.println("</activationtime>");
			System.out.print("<hashcode>");
			System.out.print(hashcode == null ? 0 : getLowEndianUInt(hashcode, 0, 4));
			System.out.println("</hashcode>");
			
			System.out.println("<base64data>");
			String b64 = new String(Base64Encoder.encode(data));
			int remaining = b64.length();
			int startindex = 0;
			while (remaining > 0) {
				System.out.println (b64.substring(
						startindex, 
						startindex + (remaining > SPLIT_B64_SIZE ? SPLIT_B64_SIZE : remaining)));				
				remaining -= SPLIT_B64_SIZE;
				startindex += SPLIT_B64_SIZE;
			}
			System.out.println("</base64data>");
		}		
		else 
		{
			System.out.print("activationtime: ");
			System.out.println(activationTime);
			System.out.print("hashcode:       ");
			System.out.println(hashcode == null ? 0 : getLowEndianUInt(hashcode, 0, 4));
			System.out.println("base64data:");
			
			String b64 = new String(Base64Encoder.encode(data));
			int remaining = b64.length();
			int startindex = 0;
			while (remaining > 0) {
				System.out.println (b64.substring(
						startindex, 
						startindex + (remaining > SPLIT_B64_SIZE ? SPLIT_B64_SIZE : remaining)));				
				remaining -= SPLIT_B64_SIZE;
				startindex += SPLIT_B64_SIZE;
			}
		}
		
	}
	

}
