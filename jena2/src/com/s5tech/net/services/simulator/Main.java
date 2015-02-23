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
 
package com.s5tech.net.services.simulator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static String CSV_OUTPUT = null; 

	private static final String DEFAULT_LOGGING_PROPS = "logging.properties";
	
	public static void exec (String [] args, int args_offset) throws IOException {		

		if (System.getProperty("java.util.logging.config.file", "").length() == 0) {		
			if (new File(DEFAULT_LOGGING_PROPS).exists()) {
				System.setProperty("java.util.logging.config.file", DEFAULT_LOGGING_PROPS);
			}		
		}				
		
		String config = null;
		
		for (int i = args_offset; args != null && i < args.length; i ++) {
			if (args[i].equals("-o") || args[i].equals("--output-file")) {
				CSV_OUTPUT = args[++i];				
			}
			else if (args[i].equals("-c") || args[i].toLowerCase().startsWith("--conf")) {
				config = args[++i];				
			}
			else if (args[i].equals("-j") || args[i].toLowerCase().startsWith("--jdbc")) {
				System.setProperty("jdbcprops", args[++i]);
			}
			else if (args[i].equals("-l") || args[i].toLowerCase().startsWith("--logger-config")) {
				System.setProperty("java.util.logging.config.file", args[++i]);
			}			
		}
				
		final File csvfile = (CSV_OUTPUT == null ? null : new File(CSV_OUTPUT));
		
		System.err.println("logger configuration file is " + System.getProperty("java.util.logging.config.file", "not set!!!"));
		System.err.println("logger mode is " + (
				Logger.getLogger("SIMULATOR").isLoggable(Level.ALL) ? "verbose" : "silent"));
		
		if (config == null)
		{
			if (new File("simulator.xml").exists())
			{
				config = "simulator.xml";
			}
			else if (config == null && new File("simulator.properties").exists())
			{
				config = "simulator.properties";
			}
		}
		
		if (config != null) 
			System.out.println ("simulator configuration file " + config + " ...");
		
		if (csvfile != null)
		{		
			FileWriter fw = new FileWriter(csvfile, false);
			fw.write("mac\tsize\tesl-d\tround-d\tround-nr\tfailperc\tavgres\tetime\r\n");
			fw.close();
		}
		
		SimulatorSession session = new SimulatorSession(config);
		
		session.setListener (
				
				new IBufferListener() {
						
					String port;
					String coordinatorMac;
					int joinsize;
					int joindelay; 
					int rejoindelay;
				
					@Override
					public void onStart(
							String port, 
							String coordinatorMac,
							int channel,
							int joinsize, 
							int joindelay, 
							int rejoindelay)
							{
						this.port = port;
						this.coordinatorMac = coordinatorMac;								
						this.joinsize = joinsize;
						this.joindelay = joindelay; 
						this.rejoindelay = rejoindelay;
						
						System.err.println(
								"simulator start at " + port 
								+ " - mac: " + coordinatorMac 
								+ " - channel: " + channel);
						
					}
				
					@Override
					public void onJoinCompleted(
							String coordinatorMac,
							int rejoin_loop,
							int timedout_joins, 
							int response_average, 
							int total_elapsed_time) {

						if (timedout_joins < 0) return;
						
						double x = joinsize;
						x += timedout_joins;
						x = joinsize / x;
						x *= 100.;
						
						System.err.println(
								"join completed: MAC " 
										+ coordinatorMac 
										+ " - loop nr. : " + rejoin_loop
										+ " - rejoins : " + joinsize
										+ " - success: " + String.format("%.2f", x) + " %"); 
						
						if (csvfile == null) return;
						
						synchronized (csvfile) {

							try
							{
								FileWriter o = new FileWriter(csvfile, true);
								o.write(coordinatorMac);
								o.write('\t');
								o.write(String.format("%6d", joinsize));
								o.write('\t');
								o.write(String.format("%6d", joindelay));
								o.write('\t');
								o.write(String.format("%6d", rejoindelay));
								o.write('\t');
								o.write(String.format("%6d", rejoin_loop));
								o.write('\t');
								o.write(String.format("%6d", timedout_joins));
								o.write('\t');
								o.write(String.format("%6d", response_average));
								o.write('\t');
								o.write(String.format("%6d", total_elapsed_time));													
								o.write("\r\n");										
								o.close();
							}
							catch (Throwable neveroccur) {}

						}
						
					}

					long prevTotal = 0;
					
					@Override
					public void onUpdateTransmissions(
							String coordinatorMac,
							long totalEslMessagesTransmitted,
							long floodingEslMessageTransmitted) {

						if (totalEslMessagesTransmitted == 0) return;
						if (totalEslMessagesTransmitted <= prevTotal) return;
						
						double x = 0.;
						x = totalEslMessagesTransmitted;
						x -= floodingEslMessageTransmitted;
						x /= totalEslMessagesTransmitted;
						x *= 100.;
						
						System.err.println(
								"updating transmitted esl messages: MAC " 
										+ coordinatorMac 
										+ " - recvd: " + totalEslMessagesTransmitted
										+ " - flood: " + floodingEslMessageTransmitted
										+ " - success: " + String.format("%.2f", x) + " %"); 
										
						prevTotal = totalEslMessagesTransmitted;
					}

					@Override
					public void onExit(String coordinatorMac) {
						System.err.println(
								"simulator exit at " + port 
								+ " - mac received: " + coordinatorMac 
								+ " - mac saved: " + this.coordinatorMac);
					}

					@Override
					public void onListening(String coordinatorMac) {
						// TODO Auto-generated method stub
						
					}
											
				});
			
		try {
			
			session.start();
			
			for (;;) {									

				Thread.sleep (5000);
			}
			
		}
		catch (Throwable ignored) {}
					
	}

}
