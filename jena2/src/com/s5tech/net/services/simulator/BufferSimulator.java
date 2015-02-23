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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

import com.s5tech.net.services.simulator.data.EslDataBase;
import com.s5tech.net.services.simulator.data.EslInfo;

public class BufferSimulator implements Runnable {

	private Logger log; 
	
	//private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	private int port;
	private String macAddress = "*"; 
	private int channel = 11;
	
	int BEACON_TIMEOUT = 15670;
	
	boolean timeslot_disabled = false;
	
	boolean timeslot_randomized_occupation = true;
	
	boolean asyncwriter = false;
	
	PriorityBlockingQueue<OutgoingPacket> outgoing = null;
	BufferedOutputStream gbout = null;
	
	private String bindAddress = null;
	
	private Thread wtransmitter = null;	
	
	private int goOfflineSecondsFromStart = 0;
	
	private boolean ackEnabled = true;
	
	private int timeslot_occ_delay_ms = 8000;
	
	private long emesgCounter = 0;
	private long floodCounter = 0;
	
	private IBufferListener listener = null;	
	
	boolean autostart_join = true;
	
	private Vector<String> endlist = new Vector<>();
	
	private Boolean connected = new Boolean(false);
	
	private byte [] binfo = new byte[] {
			0x00, 0x06,
			0x02,
			0x0F,
			0x0F,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02,
			0x00, 0x00, 0x02 };
		
	class OutgoingPacket implements Comparable<OutgoingPacket>{

		byte[] data = null;
		
		public OutgoingPacket(byte[] data) 
		{
			this.data = data;
		}
		
		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}

		@Override
		public int compareTo(OutgoingPacket o) {
			return 0;
		}
		
	}
		
	private int join_start_delay = 5000; 	// initial startup delay for join task
	private int join_delay = 5; 			// delay ms between two consucutive validate requests of different esls
	private int rejoin_delay = 8000;        // minimum delay between consucutive join of the same esl
	
	private static final int JOIN_EXPIRATION_TIMEOUT_MS = 400;
	
	private int join_size = 0; // less than 0xffff, set join_size = 0 to disable join task
	private Thread joiner = null;
	
	private int join_loops = 1;
	
	int gb_loop_nr = 0;
	double gb_fail_ = 0;
	int gb_res_avg_ = 0;
	int gb_last_join_et = 0;
	
	boolean merge_esls = false;
	
	List<EslInfo> esls = null;
	private TreeMap<Integer, EslInfo> nwkmap;
	private TreeMap<String, EslInfo> macmap;
	
	private static EslDataBase db = new EslDataBase();
	
	public BufferSimulator(
			int port, 
			String macAddress, 
			int channel) {
		
		this(port, null, macAddress, channel, 0, 0, 0, 0, 0, 0, 0, 0, true);
	}
	
	public BufferSimulator(
			int port, 
			String macAddress, 
			int channel, 
			int join_size, 
			int join_start_delay, 
			int join_delay, 
			int rejoin_delay) {
		
		this(port, 
				null, 
				macAddress, 
				channel, 
				join_size, 
				join_start_delay, 
				join_delay, 
				rejoin_delay, 
				1, 0, 0, 0,
				true);
	}
	
	public BufferSimulator(
			int port, 
			String bindAddress, 
			String macAddress, 
			int channel, 
			int join_size, 
			int join_start_delay, 
			int join_delay, 
			int rejoin_delay, 
			int rejoin_loops, 
			int goOfflineSecondsFromStart,
			int timeslot_occ_delay_ms,
			int beacon_timeout, boolean autostart_join) {
		
		this.join_size = join_size;
		this.join_delay = join_delay;
		this.join_start_delay = join_start_delay;
		this.rejoin_delay = rejoin_delay;
		this.join_loops = rejoin_loops;
		this.bindAddress = bindAddress;
		this.port = port;		
		this.macAddress = macAddress;
		this.goOfflineSecondsFromStart = goOfflineSecondsFromStart;
		this.channel = channel;
		this.timeslot_occ_delay_ms = (timeslot_occ_delay_ms == 0 ? 8000 : timeslot_occ_delay_ms);		
		this.BEACON_TIMEOUT = (beacon_timeout == 0 ? 15670 : beacon_timeout);
		this.autostart_join = autostart_join;
		
		log = Logger.getLogger("SIMULATOR." + macAddress + "." + bindAddress + ":" + port);
		
		esls = db.loadEsls(macAddress);
		nwkmap = new TreeMap<Integer, EslInfo>();
		macmap = new TreeMap<String, EslInfo>();for (EslInfo e : esls) {
			macmap.put(e.mac_id, e);
			nwkmap.put(e.short_address, e);
		}

	}
	
	public IBufferListener getListener() {
		return listener;
	}

	public void setListener(IBufferListener listener) {
		this.listener = listener;
	}
	
	private void startJoin(List<String> macs)
	{	
		final List<EslInfo> joinList = new ArrayList<>();
		
		if (join_size <= 0) return;
		
		if (joiner != null && joiner.isAlive()) {
			log.warning("join thread already started @tcp://" + bindAddress +":" 
					+ port + " (MAC "  
					+ macAddress + ") ... ignored start request!");
			return;
		}
		
		if (macs == null) {
			for (EslInfo e : esls)
			{
				joinList.add(e);
			}
		}
		else {
			for (String m : macs) {
				EslInfo e = macmap.get(m);
				if (e == null) {
					log.warning("impossible ESL rejoin " + m 
							+ ": not associated to this controller (" + macAddress + ")!");
					continue;
				}
				joinList.add(e);				
			}
		}
		
		joiner = new Thread (new Runnable() {
			
			@Override
			public void run() {
				
				int total_attempts = 0;
				double res_avg = 0;
				long last_join = 0;
				long start_vreq = 0;

				byte[] validate_req = new byte[11];
				
				if (log.isLoggable(Level.ALL))
					log.finest("STARTING JOIN TASK for " + join_size + " esl/s ... delay " 
								+ join_delay 
								+ ", start delay " 
								+ join_start_delay 
								+ ", loops " 
								+ join_loops);
				
				for (int j = 0; j < join_loops; j ++)
				{
					total_attempts = 0;							
					start_vreq = 0;
					
					try {												
						
						if (log.isLoggable(Level.ALL))
							log.finest("START JOIN ROUND nr. " + ((int)(j + 1)) + " ...");	
						
						Thread.sleep(join_start_delay);	
						
						for (EslInfo e : joinList) {	
							e.reset();													
						}
						
						while  (true) 
						{
							int total = 0;
							int count_not_joined = 0;
							long start = System.currentTimeMillis();
							
							for (EslInfo e : joinList) {																					
								synchronized (e) {
//									boolean ok = (e.coordinator != null && macAddress.equals("*"));
//									if (!ok) ok = (e.coordinator != null && e.coordinator.equals(macAddress));
//									if (!ok) continue;
									total ++;
									if (total > join_size) break;
									if (e.join_time > 0) continue;
									count_not_joined ++;
									validate_req[0] = (byte) 0x00;					
									validate_req[1] = (byte) 0x01;
									validate_req[10] = (byte) 0x00; 			// capability info
									e.vreq_time = System.currentTimeMillis();
									e.vreq_attempts ++;
									for (int k = 0; k < 8; k ++) {
										validate_req[9 - k] = e.mac_address[k];
									}
									if (start_vreq == 0) start_vreq = System.currentTimeMillis();
									write (validate_req);
									total_attempts ++;
								}
								if (join_delay > 0) Thread.sleep(join_delay);
							}
							
							if (count_not_joined == 0) break;
							long wait = start + rejoin_delay - System.currentTimeMillis();
							if (wait > 0) Thread.sleep(wait);
						}
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
					
					try 
					{	
						/*
						String suffix = "_" + macAddress;
						suffix += "_NR" + String.format("%05d",((int)(j + 1)));
						suffix += "_SZ" + String.format("%05d", join_size);
						suffix += (bindAddress == null || bindAddress.equals("*") ? "" : "." + bindAddress);
																		
						String fname = "./results/JOIN" + suffix + ".txt";												
						
						PrintStream fout = new PrintStream(fname);
						
						fout.println("simulator address: " + "tcp://" + bindAddress + ":" + port + " " + macAddress + " " + channel);
						fout.println("rejoin round nr.:  " + String.format("%6d", ((int)(j + 1))));
						fout.println("rejoin esls size:  " + String.format("%6d", join_size));
						fout.println("esl delay ms:      " + String.format("%6d", join_delay));
						fout.println("round delay ms:    " + String.format("%6d", rejoin_delay));
						*/
						
						res_avg = 0;
						
						last_join = 0;
						for (EslInfo e : joinList) {
//							boolean ok = (e.coordinator != null && macAddress.equals("*"));
//							if (!ok) ok = (e.coordinator != null && e.coordinator.equals(macAddress));
//							if (!ok) continue;
							if (e.join_time <= 0) continue;
							res_avg += ((double)(e.join_time - e.vreq_time));	
							if (e.join_time > last_join) last_join = e.join_time;
						}
						
						double fail = (double) join_size;
						fail /= total_attempts;
						fail = 1 - fail;
						fail *= 100;

						res_avg /= join_size;

						/*
						fout.println("failure %:         " + String.format("%6d", ((int) fail)));
						fout.println("avg.response ms:   " + String.format("%6d", ((int)(res_avg))));
						fout.println("total e.time ms:   " + String.format("%6d", (last_join - start_vreq)));
						
						fout.println();
						
						int total = 0;
						
						for (EslInfo e : EslInfo.macmap.values()) {
							boolean ok = (e.coordinator != null && macAddress.equals("*"));
							if (!ok) ok = (e.coordinator != null && e.coordinator.equals(macAddress));
							if (!ok) continue;
							if (e.join_time <= 0) continue;
							total ++;
							if (total > join_size) break;
							fout.print(e.mac_id);
							fout.print('\t');
							fout.print(e.vreq_attempts);
							fout.print('\t');
							fout.print((int)(e.join_time - e.vreq_time));
							fout.print('\t');
							fout.print(df.format(new Date(e.join_time)));
							fout.print('\t');
							fout.println();						
						}
						
						fout.close();	
						*/
						
						if (listener != null)
						{
							listener.onJoinCompleted (
									macAddress, 
									((int)(j + 1)),
									((int) fail),
									((int)(res_avg)),
									(int)(last_join - start_vreq)
									);
						}
						
						gb_loop_nr = j + 1;
						gb_fail_ = fail;
						gb_res_avg_ = (int)res_avg;
						gb_last_join_et = (int)(last_join - start_vreq);
						
						//log.info("TOTAL REJOIN results of {} esls dumped to file " + fname, join_size);
					
					}										
					catch (Throwable t) 
					{
						t.printStackTrace();											
					}											
					
				}
				
				if (listener != null)
					listener.onExit(macAddress); 
				
				if (log.isLoggable(Level.ALL))
					log.finest("JOIN THREAD exit!");
				
			}
		}, Thread.currentThread().getName() + " -> NR." + join_loops + " REJOIN TASK");
		
		joiner.setDaemon(true);
		joiner.start();	
		
	}
	
	ServerSocket server = null;
	Socket client = null;
	
	public void run() {
		
		try {
			
			int count = 0;
			
			if (bindAddress != null) 
			{
				server = new ServerSocket(port, 1, InetAddress.getByName(bindAddress));
			}
			else 
			{
				server = new ServerSocket(port);
			}			

			//
			// preliminary loop to check if there are esls 
			// associated to this coordinator mac
			//
			if (join_size > 0)
			{
//				synchronized (esls) {
//					for (EslInfo e : esls) {	
//						boolean ok = (e.coordinator != null && macAddress.equals("*"));
//						if (!ok) ok = (e.coordinator != null && e.coordinator.equals(macAddress));
//						if (!ok) continue;
//						if (join_size > count) count ++;
//					}					
//				}
				
				count = (join_size > esls.size() ? esls.size() : join_size);
				
				if (esls.size() > 0) 	
				{	
					if (join_size > esls.size())
					{
						log.info("resized join from " 
									+ join_size + " to " 
									+ count 
									+ " (no sufficient esls associated with this coordinator mac " 
									+ macAddress + ")");													
					}
				}
				else 
				{
					log.warning("JOIN THREAD IGNORED! No registered esls "
								+ "associated with this coordinator " 
								+ macAddress);
				}
				
				join_size = count;
			}

			if (log.isLoggable(Level.ALL))
				log.finest ("LISTENER ACTIVE ON PORT " + port + " ..." 
						+ "\n\ttimeslot delay: " + timeslot_occ_delay_ms + " ms" 	
						+ "\n\tbeacon timeout: " + BEACON_TIMEOUT + " ms" 	
						+ (join_size > 0 ? 		
							"\n\tauto start:     " + autostart_join +
							"\n\tjoin size:      " + join_size +
							"\n\tstart delay:    " + join_start_delay + "ms" +
							"\n\tesl delay:      " + join_delay + "ms" +
							"\n\tnr.of loops:    " + join_loops +
							"\n\tloop delay:     " + rejoin_delay + "ms" +
							"\n\tgoes offline:   " + (goOfflineSecondsFromStart > 0 ? 
									"in " + goOfflineSecondsFromStart + " seconds!" : "never") 
							: "\n\tno rejoin set!"));
			else log.info("LISTENER ACTIVE ON PORT " + port + " ...");
			
			for ( ;; ) {
			
				if (listener != null) 
					listener.onListening(macAddress);
				
				Socket s = server.accept();		
				
				client = s;
			
				try {	
					
					final OutputStream out = s.getOutputStream();
					final InputStream in = s.getInputStream();

					log.info(
							"STARTING NEW SIMULATOR SESSION: CLIENT " 
							+ s.getRemoteSocketAddress().toString() + " ...");

					if (asyncwriter) {
						startWriter(new BufferedOutputStream(out));					
					}
					else {
						gbout = new BufferedOutputStream(out);
					}
					
					if (listener != null) 
						listener.onStart(
								"tcp://" + bindAddress + ":" + port, 
								macAddress, 
								channel,
								join_size,
								join_delay,
								rejoin_delay);

					if (autostart_join && join_size > 0)
					{
						rejoin();
					}
										
					if (goOfflineSecondsFromStart > 0) 
					{
						final Socket s_ = s;
						Timer offlineStarter = new Timer(Thread.currentThread().getName() + " -> OFFLINE TRIGGER", true);
						TimerTask timeSetterTask = new TimerTask() {
							@Override
							public void run() {
								try {
									log.info("GOING OFFLINE NOW !!!!!!!!!!!.");
									s_.close();
									cancel();
								}
								catch(Throwable r) { r.printStackTrace(); }
								
								log.info("TIMER OFFLINE EXIT.");
															}
						};
						offlineStarter.schedule(timeSetterTask, goOfflineSecondsFromStart * 1000, 1000);						
					}
										
					runReader(in);
					
					if (wtransmitter != null && wtransmitter.isAlive()) 
						try { wtransmitter.interrupt(); }
						catch (Throwable ignored) {}
						
					if (joiner != null && joiner.isAlive()) 
						try { joiner.interrupt(); }
						catch (Throwable ignored) {}
											
				}
				catch (Throwable t1) {					
					log.log(Level.SEVERE, "cannot accept @tcp://" + bindAddress + ":" + port, t1);
				}				
				finally {
					if (s != null) try { s.close(); } catch (Throwable ignored) {}					
				}
			}			
		}
		catch (Throwable t) {
			log.log(Level.SEVERE, "cannot bind address tcp://" + bindAddress + ":" + port, t);		
		}
		finally {
			if (server != null) try { server.close(); } catch (Throwable ignored) {}	
		}
		
	}
		
	
	private static byte[] insertLongAsBytes(long value, byte[] array,
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
	
	/* esl ack message payload */
	byte [] ackTemplate = new byte[] {
			
			0x00, 0x05,
			0x00,
			0x00, 0x00,                                     // [3-4] short network address
			0x03, 0x00, 0x00, 0x02,
			0x06,                                           // [9] esl type | 0x80
			0x00, 0x00, 0x00, 0x00,                         // [31-34] hash active
			0x00, 0x00, 0x00, 0x00,                         // [35-37] hash pending
			
	};
	
	public void emptyBuffer(short slot_nr) {	
		
		try {		
			
			//
			// empty buffer's timeslot number 'slot_nr'
			//
			
			synchronized (binfo) {
				
				int i = (5 + slot_nr * 3);	
				
				ackTemplate[4] = binfo[i + 1];
				ackTemplate[3] = binfo[i];
				
				int shortAddress = binfo[i + 1] & 0xff;				
				shortAddress <<= 8;
				shortAddress += binfo[i] & 0xff; 
				
				String hex = Integer.toHexString(shortAddress);
				while (hex.length() < 4) hex = "0" + hex;
				
				if (log.isLoggable(Level.ALL))
					log.finest ("FREEING SLOT NR." + slot_nr + " address 0x" + hex.toUpperCase());	
				
				binfo[i] = 0;
				binfo[i + 1] = 0;
				binfo[i + 2] ++;
				StringBuffer buff = new StringBuffer();
				{
					for (int j = 5; j < 5 + 14 * 3; j += 3) {
						String h = Integer.toHexString(binfo[j] & 0xff);
						if (h.length() < 2) h = "0" + h;
						h = Integer.toHexString(binfo[j + 1] & 0xff) + h;
						if (h.length() < 4) h = "0" + h;
						buff.append(h).append(":").append(binfo[j + 2]).append("|");
					}	
					
					if (log.isLoggable(Level.ALL))
						log.finest ("BUFFER-FREE> " + buff.toString());
				}
				
				//
				// Send new buffer information
				//
				write(binfo);
				
				//
				// TBD: send ack here?
				//
				synchronized (endlist) 
				{
					if (ackEnabled && endlist.contains(hex)) 
					{
						endlist.remove(hex);
						EslInfo e =  nwkmap.get(shortAddress);
						//insertLongAsBytes(1474731479L, ackTemplate, 10, 4, false);
						insertLongAsBytes((e != null ? e.activeHash : 1), ackTemplate, 10, 4, false);
						insertLongAsBytes((e != null ? e.pendingHash : 0), ackTemplate, 14, 4, false);
						ackTemplate[9] = (e!= null ? e.esltype : 0x06);
						ackTemplate[9] |= 0x80;					
						write(ackTemplate);					
					}
				}
				
			}		
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
			
	public void startWriter(BufferedOutputStream out_) {		 
		outgoing = new PriorityBlockingQueue<OutgoingPacket>();
		if (out_ == null) return;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedOutputStream out_ = gbout;
				for ( ;;) {
					try {
						OutgoingPacket p = outgoing.take();
						byte [] b = p.getData();
						if (b == null) continue;
						int length = b.length;
						if (length > 0) {
							int crc = length;
							for (int i = 0; i < length; i ++) {
								crc ^= b[i];
							}
							synchronized (out_) {
								out_.write(0x02);
								out_.write(length);
								out_.write(b, 0, length);
								out_.write(crc);
								out_.flush();
							}
						}
					}
					catch (InterruptedException ignoredie) {}
					catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		});
		t.setDaemon(true);
		t.setName(Thread.currentThread().getName() + " -> WRITER");
		t.start();
	}
	
	public void write(byte [] data) {
		if (data == null) return;
		if (asyncwriter) {
			OutgoingPacket p = new OutgoingPacket(data);
			outgoing.put(p);
		}
		else {
			try {
				int length = data.length;
				if (length > 0) {
					int crc = length;
					for (int i = 0; i < length; i ++) {
						crc ^= data[i];
					}
					synchronized (gbout) {
						gbout.write(0x02);
						gbout.write(length);
						gbout.write(data);
						gbout.write(crc);
						gbout.flush();
					}
				}
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	public void runReader(InputStream in) {
		
		if (log.isLoggable(Level.ALL))
			log.finest("READER STARTED");
		
		Random rnd = new Random();
		
		try {
			
			byte [] b = new byte[256];
			
			connected = true;
			
			for ( ;;) {
				
				int c = 0;
				int length = 0;

				if ((c = in.read()) == -1) 
					throw new IOException("input stream closed");
			
				if (c != 0x02) continue;

				if ((length = in.read()) == -1) 
					throw new IOException("input stream closed");
				
				if (length <= 0) continue;				
				 
				if ((in.read(b, 0, length) != length)) continue;
									
				int x = (length & 0xff);
				for (int i = 0; i < length; i ++) {
					x ^= (b[i] & 0xff);
				}

				int crc = in.read();
				if (crc == -1) throw new IOException("input stream closed");
				
				crc &= 0xff;
				
				if (crc != x) {
					log.warning("crc error!");
					continue;
				}
								
				switch(b[1]) {
				
				case 0X02:
					
					String mac = "";					
					for (int i = 9; i >= 2; i --) {
						String hs = Integer.toHexString(b[i] & 0xff);
						if (hs.length() < 2) hs = "0" + hs;						
						mac += hs;
					}
					
					String nwk = Integer.toHexString(b[11] & 0xff);
					if (nwk.length() < 2) nwk = "0" + nwk;
					nwk += Integer.toHexString(b[10] & 0xff);
					if (nwk.length() < 4) nwk = "0" + nwk;
					
					mac = mac.toUpperCase();
					nwk = nwk.toUpperCase();
					
					if (nwk.equals("FFFF")) {
						if (log.isLoggable(Level.ALL))
							log.finest("DEVICE " + mac + " NOT AUTHORIZED BY NETWORK APPLICATION !!!: ");
						break;
					}
					
					//if (join_size > 0)
					//{
						EslInfo e = macmap.get(mac);					
						long ct = System.currentTimeMillis();
						if (ct - e.vreq_time > JOIN_EXPIRATION_TIMEOUT_MS) 
						{
							if (log.isLoggable(Level.ALL))
								log.finest("DEVICE " + mac + " AUTHORIZATION EXPIRED !!! " + (ct - e.vreq_time) + " ms");							
						}
						else 
						{
							if (log.isLoggable(Level.ALL))
								log.finest("DEVICE " + mac + " JOINED");
							
							synchronized (e) {
								
								e.join_time = ct;
								
								//++ patch for testing short assignment ...
								if (merge_esls) {
									int prevshort = e.short_address;
									e.short_address = b[10];
									e.short_address *=  0xff;
									e.short_address += b[11];
									if (!e.coordinator.equals (macAddress)) {
										if (log.isLoggable(Level.FINEST)) {
											log.finest ("esl " + e.mac_id + " jumped from coordinator " +
													e.coordinator + " => " + macAddress + " ESLMAC:" +
													e.mac_id);
										}
									}
									else {
										if (prevshort != e.short_address) {
											log.severe("short address was not recycled !!! from 0x" +
													Integer.toHexString(prevshort) + " => 0x" +
													Integer.toHexString(e.short_address) + 
													" ESLMAC:" +
													e.mac_id);	
										}
									}
									e.coordinator = macAddress;
								}
								//--
								
							}
						}
					//}
									
					byte [] ackjoin = new byte[] { 
							0x00, 0x05,
							0x00,
							b[10], b[11],                                   // [3-4] short network address
							0x03, 0x00, 0x00, 0x02,
							(byte) (0x06 | 0x80),                           // [9] esl type
							0x00, 0x00, 0x00, 0x00,                         // [31-34] hash active
							0x00, 0x00, 0x00, 0x00,                         // [35-37] hash pending
							};
						
					write (ackjoin);
					
					if (log.isLoggable(Level.ALL))
						log.finest("ACKNOLEDGE SENT NETWORK ADDRESS -> " + nwk);
										
					break;
					
				case 0X05: 		
					
					final byte blow = b[3];
					final byte bhigh = b[4];					
					String hex = Integer.toHexString(blow & 0xff);
					if (hex.length() < 2) hex = "0" + hex;
					hex = Integer.toHexString(bhigh & 0xff) + hex;
					if (hex.length() < 4) hex = "0" + hex;
					
					if (hex.toLowerCase().equals("ffff")) {
						
						//TBD ....
						break;
						
					}
					else 
					{
						if (b[8] == (byte)0x0B) 
						{
							if (log.isLoggable(Level.ALL))
								log.finest("STATISTICS for NETWORK ADDRESS 0x" + hex);
						}
						else {						
							
							//getting activation time from eslmessage payload  ....
							//0005 08 4B02 00030001 0105 32D60814 1000C84900003F0D030058888888DFFEFFF0
							//                      .... [11] <- here 4 bytes timestamp in secs from 2000
							//
							
							if (log.isLoggable(Level.INFO)) {
								String s = "";
								int sc = 0;
								for (byte B : b) {
									String hh = Integer.toHexString(((int) B) & 0xff).toUpperCase();
									if (hh.length() < 2) hh = "0" + hh;
									s += ((sc % 32 == 0) ? "\n" : ":");
									s += hh;
									sc ++;
								}
								log.info("PRICE_UPDATE for NETWORK ADDRESS 0x" + hex + " > " + s);
							}							
						}
					}
					
					if (timeslot_disabled) break;
					
					//
					// fill slots with received short address destination ...
					//
					short slot_nr = 0;
					
					synchronized (binfo) {
						
						for (int i = 5; i < 5 + 14 * 3; i += 3) {							
							
							if (binfo[i] == 0 && binfo[i + 1] == 0) {
								
								binfo[i] = blow; 
								binfo[i + 1] = bhigh;
								binfo[i + 2] --;
								
								emesgCounter ++;
								
								if (log.isLoggable(Level.ALL))
									log.finest("FILLED SLOT NR." + slot_nr + " FOR ADDRESS 0x" + hex.toUpperCase());
								
								final short nr = slot_nr;
								final Random trnd = rnd;
								
								boolean transmission_enabled = ((b[6] & 0x10) == 0x10);
								
								if (transmission_enabled) {
									if (b[7] == b[9] && b[8] == b[10]) 
										endlist.add(hex);
								}
								else {
									endlist.add(hex);
								}
								
								Thread t = new Thread(new Runnable() {									
									
									@Override
									public void run() {
										
										try {		
											
											if (timeslot_randomized_occupation) 
											{
												int ms = 1000;
												ms += trnd.nextInt(timeslot_occ_delay_ms);
												Thread.sleep(ms);
											}
											else 
											{
												Thread.sleep(timeslot_occ_delay_ms);
											}
											
											BufferSimulator.this.emptyBuffer(nr);
											
										}
										catch (Throwable e) {
											
											e.printStackTrace();
											
										}
										
									}
								}, Thread.currentThread().getName() + " -> TIMER TIMESLOT [" + nr + "]");
								t.setDaemon(true);
								t.start();
								break;
								
							}
							else {
								
								slot_nr ++;
							
							}
						}	
						
						if (slot_nr >= 14) {
							
							StringBuffer buff = new StringBuffer();
							
							for (int i = 5; i < 5 + 14 * 3; i += 3) {
								
								String h = Integer.toHexString(binfo[i] & 0xff);
								if (h.length() < 2) h = "0" + h;
								h = Integer.toHexString(binfo[i + 1] & 0xff) + h;
								if (h.length() < 4) h = "0" + h;
								buff.append("0x").append(h).append(":").append(binfo[i + 2]).append("|");
								
							}
							
							floodCounter ++;
							
							if (log.isLoggable(Level.WARNING))
								log.warning("COORDINATOR FLOODING !!! ALL slots full !!! > " + buff.toString().toUpperCase()); 
							
						}
						
					}
					
				break;
					
				case 0X07: 
					if (log.isLoggable(Level.ALL))
						log.finest("REQ_IEEE_EUI received");
					byte [] address_response = new byte [10];
					address_response[0] = (byte)0x00;					
					address_response[1] = (byte)0x08;
					/* coordinator mac address */
					String mm = "0012000000000001";
					if (!macAddress.equals("*")) mm = macAddress.toLowerCase();
					address_response[9] = (byte)Integer.parseInt(mm.substring (0, 2), 16);
					address_response[8] = (byte)Integer.parseInt(mm.substring (2, 4), 16);
					address_response[7] = (byte)Integer.parseInt(mm.substring (4, 6), 16);
					address_response[6] = (byte)Integer.parseInt(mm.substring (6, 8), 16);
					address_response[5] = (byte)Integer.parseInt(mm.substring (8, 10), 16);
					address_response[4] = (byte)Integer.parseInt(mm.substring (10, 12), 16);
					address_response[3] = (byte)Integer.parseInt(mm.substring (12, 14), 16);
					address_response[2] = (byte)Integer.parseInt(mm.substring (14, 16), 16);
					write (address_response);
					break;
					
				case 0x09: 	
					if (log.isLoggable(Level.ALL))
						log.finest("REQ_ACTIVE_CHANNEL received");
					byte [] channel_response = new byte [3];
					channel_response[0] = (byte)0x00;
					channel_response[1] = (byte)0x0A;
					/* coordinator channel */
					channel_response[2] = (byte) (channel > 26 || channel < 11 ? 11: channel);
					write (channel_response);
					break;
					
				case 0x0B: 
					if (log.isLoggable(Level.ALL))
						log.finest ("REQ_SET_CHANNEL_MASK received (NOT MANAGED)");
					break;
					
				case 0x0E: 
					if (log.isLoggable(Level.ALL))
						log.finest("REQ_FIRMWARE_VERSION received");
					byte [] fw_response = new byte [6];
					fw_response[0] = (byte)0x00;
					fw_response[1] = (byte)0x0F;
					/* coordinator fw version*/
					fw_response[2] = (byte)0x01;
					fw_response[3] = (byte)0x02;
					fw_response[4] = (byte)0x09;
					fw_response[5] = (byte)0x00;		
					write (fw_response);
					break;
					
				case 0X10: 
					
					if (log.isLoggable(Level.ALL))
						log.finest("REQ_NUM_FREE_BUFFERS received");
					
					if (wtransmitter != null && wtransmitter.isAlive()) 
						try { wtransmitter.interrupt(); }
						catch (Throwable ignored) {}
					
					wtransmitter = new Thread(new Runnable() {						
						
						@Override
						public void run() {
							
								if (log.isLoggable(Level.INFO))
								{
									log.info("BUFFER TIMEOUT TRANSMITTER STARTED");									
								}
								
								try {			
									for ( ;;) {
										
										synchronized (binfo) {
											
											StringBuffer buff = new StringBuffer();
											{
												for (int i = 5; i < 5 + 14 * 3; i += 3) {
													String h = Integer.toHexString (binfo[i] & 0xff);
													if (h.length() < 2) h = "0" + h;
													h = Integer.toHexString (binfo[i + 1] & 0xff) + h;
													if (h.length() < 4) h = "0" + h;
													buff.append(h).append(":").append(binfo[i + 2]).append("|");
												}	
												
												if (log.isLoggable(Level.ALL))
												{
													log.finest("BUFFER-TIMEOUT > " + buff.toString());
												}
												
												if (listener != null) 
													listener.onUpdateTransmissions(macAddress, emesgCounter, floodCounter);												
											}
											write(binfo);
										}	
										
										Thread.sleep(BEACON_TIMEOUT);
									
									}
								}
								catch (Throwable ignored) {} 								
								
								log.warning("BUFFER TIMEOUT TRANSMITTER EXIT !!!");		
								
						}
						
					}, Thread.currentThread().getName() + "-> TIMEOUT BUFFER TRANSMITTER");
					
					wtransmitter.setDaemon(true);
					wtransmitter.start();
					
					break;
					
				case 0x11: 		
					long setTime, setTimeHere;
					setTime = (long)(b[2] & 0xff);
					setTime += (((long)(b[3] & 0xff)) * 0xff);
					setTime += (((long)(b[4] & 0xff)) * 0xffff);
					setTime += (((long)(b[5] & 0xff)) * 0xffffff);					
					setTimeHere = (System.currentTimeMillis() / 1000);	
					if (log.isLoggable(Level.INFO))
						log.info ("SET_TIME received => " + new Date((setTime + 946684800L) * 1000)
							+ " - here: " + new Date(setTimeHere * 1000));
					break;	
				
				default:
					if (log.isLoggable(Level.WARNING))
						log.warning("Received command not managed => " + b[1]);
					
				}
			}
		}
		catch (Throwable ignored) {}
		
		connected = false;								
		
	}
		
	public long getEslMessageFloodCounter()
	{
		return floodCounter;
	}
	
	public long getEslMessageSentCounter()
	{
		return emesgCounter;
	}

	public String getMacAddress()
	{
		return macAddress;
	}
	
	public int getJoinSize()
	{
		return join_size;
	}
	
	public int getJoinDelay()
	{
		return join_delay;
	}
	
	public int getLoopDelay()
	{
		return rejoin_delay;
	}
	
	public int getLoopNr()
	{
		return gb_loop_nr;
	}

	public double getJoinFailures()
	{
		return gb_fail_;
	}
	
	public int getJoinResponseAverage()
	{
		return gb_res_avg_ ;
	}
	
	public int getLastJoinLoopElapsedTime()
	{
		return gb_last_join_et ;
	}
	
	public void stop() 
	{
		if (server != null) try { server.close(); } catch (Throwable t) {}
		if (client != null) try { client.close(); } catch (Throwable t) {}
	}
	
	public void rejoin() throws Exception
	{	
		if (joiner != null && joiner.isAlive()) throw new Exception("cannot start join: already running");		
		join_loops = 1;				
		startJoin(null);		
	}
	
	public void rejoin(List<String> macs) throws Exception
	{
		if (joiner != null && joiner.isAlive()) throw new Exception("cannot start join: already running");
		join_loops = 1;		
		join_size = macs.size();
		startJoin(macs);		
	}
	
	public void addEsls(List<String> macs)
	{
		synchronized (esls) 
		{		
			int count = 0;
			for (String mac : macs) {
				if (!macmap.containsKey(mac)) {
					EslInfo esl = db.getEslInfo(mac);
					if (esl == null) continue;
					esls.add(esl);
					nwkmap.put(esl.short_address, esl);
					macmap.put(esl.mac_id, esl);
					esl.reset();
					count ++;
				}					
			}				
			log.info("added " + count + " of " + macs.size() + " esls");
		}
	}
	
	public void removeEsls(List<String> macs)
	{
		synchronized (esls) 
		{		
			int count = 0;
			for (String mac : macs) {
				if (macmap.containsKey(mac)) {
					EslInfo e = macmap.remove(mac);
					nwkmap.remove(e.short_address);
					esls.remove(e);
					count ++;
				}
			}
			log.info("removed " + count + " of " + macs.size() + " esls");
		}
	}
	
	public void waitForRejoinCompleted()
	{
		if (joiner == null) return;
		if (!joiner.isAlive()) return;
		try {
			joiner.join();
		}
		catch (Throwable ignored) {}
	}
	
	public boolean isConnected() {
		return connected;
	}
	
//	
//	public void removeJoin(String mac) {
//		synchronized (esls) 
//		{	
//			if (macmap.containsKey(mac)) {
//				EslInfo e = macmap.remove(mac);
//				nwkmap.remove(e.short_address);
//				esls.remove(e);
//			}
//		}
//	}
	
}
