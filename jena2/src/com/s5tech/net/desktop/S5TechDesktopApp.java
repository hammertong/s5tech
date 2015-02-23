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
 
package com.s5tech.net.desktop;

import java.io.File;

import com.s5tech.net.eslnet.EslNetworkApplication;

/**
 * Network Application Main Class, launch the application<br><br>
 * Usage: <br>
 * 
 * java com.s5tech.net.desktop.S5TechDesktopApp <i>run network application</i><br> 
 * or alternatively to run in complementary modes...<br>
 * java com.s5tech.net.desktop.S5TechDesktopApp [config <configuration-file>] [options]<br>
 * <br>
 * default configuration files are in order:<br>
 * <b>./conf/system.properties</b> (properties format)<br>
 * <b>./conf/s5.conf</b> (conf format)<br>
 * <br>
 * alternative options:<br><br>
 * <pre>
 * encode 
 *         [&lt;S5 input-file output-file&gt;]      convert input to output with symmetric local encoder
 *             | [&lt;hash&gt; &lt;base64-payload&gt;]    create hash code from base64 byte array data
 *             | [&lt;[MD5|...]&gt; &lt;password&gt;]     digest with specified alg. (e.g. MD5) given password
 *                 NOTE: for both 2nd and 3rd mode output is given in hexadecimal upper case digits                
 * 
 * ssh 
 *         -h &lt;10.1.1.100&gt; 
 *         -u &lt;root&gt; 
 *         -w &lt;s5tech123!&gt; 
 *         -c &lt;"ps ax | grep socat | grep -v grep"&gt;  
 * 
 * shutdown 
 *         -p &lt;9000&gt; 
 *         -h &lt;127.0.0.1&gt; 
 *         -c &lt;./conf/s5.conf&gt; 
 *         
 * client 
 *         -u &lt;tcp://127.0.0.1:61616&gt; -q &lt;dynamicQueues/UP&gt; -p &lt;activemq|rabbit&gt; 
 *         -a &lt;publish | receive | count&gt; 
 *         -i &lt;inputfile&gt;  (only publish)
 *         -v &lt;schema.xsd&gt; (publish & receive)
 *         -A (only receive, authorize all, switch on unauthorized events on network app)
 *         -n (only publish, don't send message, combined with -v to validate only xml file)
 *         -t &lt;timeout in ms&gt; (only receive) timeout between messages consumption used to 
 *                 emulate latency backend upstream elaboration
 * 
 * hubpwd 
 *         -h &lt;hostname&gt; if not specified, listen for all broadcast notifications see -p
 *         -p &lt;broadcast port&gt;
 *         -w &lt;root password&gt;
 *         -W &lt;new AP password&gt;
 *     
 * logscan
 *         -G start logviewer graphic user interface
 *         -o &lt;filename&gt; output 
 *         -x &lt;schema.xsd&gt; 
 *         -f &lt;date&gt; YYYY-MM-DDTHH:mm:ss 
 *         -l &lt;directory&gt; ./logs
 *         -a &lt;action&gt; dump (d), validate, unauth, zip, view
 *         -r &lt;filter&gt; format is mac=&lt;eslmac&gt;,coordinator=&lt;mac&gt;... 
 * 
 * csv2xml    
 *         -i &lt;input csv file&gt;    
 *         -o &lt;output xml file&gt;
 *         -r &lt;xml root element&gt; default: data
 *         -s &lt;separator&gt; \t for tab, \b for space 
 * 
 * xmlbuild
 *         -m &lt;mac list&gt; with separator ',' (alternative to -x option)
 *         -x &lt;input xml file&gt; data file (alternative to -x option)
 *         -o &lt;output file&gt;
 *         -t &lt;xslt template&gt; prepend 'file://' to use filesystem xslt file
 *         -p &lt;key=value&gt; can be reiterrated ... (optional parameters)
 * 
 * payload
 *         -a activation time in format yyyy-MM-dd'T'HH:mm:ss
 *                 default is null that means zero (01/01/2000) 
 *                 specifying '-a now' use current time
 *         -e &lt;epaper pngfile&gt;
 *         -p &lt;key=value&gt; multiple parameters option
 *         -f &lt;properties file&gt; for LCD 7SEGMENT ESL (see -s for an example)
 *         -s show an example of properties file and exit    
 *         -d &lt;base64data&gt; decode LCD 7SEGMENT ESL data in base64 format
 *         -o &lt;xml|text&gt; output format when build output ESL message (default is text)
 *         -c &lt;filepath[#startpos]&gt; calculate hash code from binary file from 
 *                 optional start position startpos (default is BOF)
 *         -x &lt;left&gt; position for png file, default and the only running is 0
 *         -t &lt;top&gt; position for png file, default and the only running is 0
 *         -P &lt;number&gt; of pages only for epaper (for LCD 7SEG use -p or -f option)
 *             
 * pngcreate
 *         -w &lt;width&gt; in pixels
 *         -h &lt;height&gt; in pixels
 *         -s &lt;mxn&gt; (e.g.: 200x96) same of above two options
 *         -i &lt;input&gt; text file for png definition (display.txt) see -s for example
 *         -o &lt;output&gt; pngfile
 *         -S show example
 *         
 * simulator
 *         -c or --conf &lt;filename&gt;            configuration .xml or .properties file (default is simulator.properties)
 *         -j or --jdbc &lt;filename&gt;            jdbc properties configuration file (ignored if xml configuration set)
 *         -l or --logger-config &lt;filename&gt;   jul logger configuration
 *         -o or --output-file &lt;filename&gt;     file csv where to save results
 * 
 * noapp
 *         initialize extensions and stop before running ESL network application
 *  
 * </pre>
 * @author S5Tech
 *
 */
public class S5TechDesktopApp {
	
	public static void main(String[] args) {
	
		try {
			
			if (!new File("./logs").isDirectory()) {
				new File("./logs").mkdirs();
				Thread.sleep(500);
			}
			
			if (!new File("./run/data").isDirectory()) {
				new File("./run/data").mkdirs();
				Thread.sleep(500);
			}
			
		}
		catch (Throwable ignored) {}
		
		EslNetworkApplication.init(args);
		
	}
	
}
