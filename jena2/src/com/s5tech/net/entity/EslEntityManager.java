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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.coord.EslNetworkCoordinatorProxy;
import com.s5tech.net.util.ISystemKeys;

public class EslEntityManager {
	
	private static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
	
	private static IEslEntityManager instance_ = null;
	
	public static synchronized final IEslEntityManager instance() {
				
		if (instance_ == null) {
			
			try {
				if (System.getProperty("persistence", "").equalsIgnoreCase("rw"))
				{
					if (log.isTraceEnabled()) 
						log.trace("using full access read/write Esl Entiry Manager");
					instance_ = (IEslEntityManager) new JdbcReadWriteEslEntityManager();
				}
				else 
				{
					instance_ = (IEslEntityManager) new EslNetworkCoordinatorProxy();
				}
			}
			catch (Throwable t) 
			{
				log.error("cannot create Esl Entity Manager {}", t);
			}
		}
		return instance_;
	}

		
}
