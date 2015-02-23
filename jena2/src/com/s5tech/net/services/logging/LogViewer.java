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
 
package com.s5tech.net.services.logging;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class LogViewer extends JFrame {

	private static final String COLOR_PAUSED = "#880000";
	private static final String COLOR_RUNNING = "#000000";
	private static final String COLOR_BACKUP = "808080";
	private static final String PLAY_SUFFIX = "(paused)";
	private static final String PAUSE_SUFFIX = "(running)";
	private static final long serialVersionUID = 0L;
	private static final int TAIL_TIMEOUT = 200;
	
	interface ILogMessageFilter {
		public boolean matches(String message);
	}
	
	private String logdir = "./logs";
	
	public String getLogdir() {
		return logdir;
	}

	public void setLogdir(String logdir) {
		this.logdir = logdir;
	}

	private long maxfilesiz = (10 * 1024 * 1024);
		
	class LogViewAppletMonitor implements Runnable
	{

		private String logfilepath = null;

		private TextArea t = null;
		private ILogMessageFilter filter = null;
		
		private int readstat = 0;
		
		public LogViewAppletMonitor(String logfilepath, ILogMessageFilter filter, TextArea t)
		{
			this.logfilepath = logfilepath;
			this.filter = filter;
			this.t = t;
		}
		
		public synchronized void pause()
		{
			readstat = 3;
		}
		
		public synchronized void play()
		{
			readstat = 1;
		}
		
		@Override
		public void run() {			
			BufferedReader reader = null;					
			try 
			{
				boolean start = true;				
				String line = null;
				StringRingBuffer buffer = new StringRingBuffer(50);
				
				reader = null;
				
				maxfilesiz -= 8192;
				
				for ( ;; ) {
					
				    if (line == null) {
				    	
				    	if (readstat == 1) 
				    	{
				    		String bufferedline;
				    		readstat = 2;
				    		while ((bufferedline = buffer.read()) != null) {
				    			synchronized (t) {
						    		t.append(bufferedline);
							        t.append("\n");	
								}
				    		}
				    	}
				    	
				        Thread.sleep(TAIL_TIMEOUT);
				        
			        	try {
			        		boolean newfile = false;
			        		File fle = new File(logfilepath);
			        		Color p = null;			        		
		        			while (fle.length() >= maxfilesiz) {		        				
		        				p = t.getForeground();
		        				t.setForeground(Color.decode(COLOR_BACKUP));
		        				newfile = true;
		        				if (reader != null) try { reader.close(); } catch (Throwable t) {}		        				
		        				Thread.sleep(1000);
		        				System.err.println("waiting for tail (backup should be done in this time) ...");
		        				t.append("!!! LOGGER IS BACKING UP FILES !!! PLEASE WAIT !!!\n");
		        				fle = new File(logfilepath);		        				
		        			}			       
		        			if (start || newfile) {
		        				if (newfile && p != null) {
		        					t.setForeground(p);
		        				}
		        				if (start) readstat = 1;
		        				start = false;
		        				reader = new BufferedReader(new FileReader(logfilepath));
		        				System.err.println("tailing file " +  logfilepath + " ...");
		        			}
				        }
				        catch (Throwable t) {
				        	t.printStackTrace();
				        }	
			        	
				    }
				    else {
				    	
				    	if (filter != null && !filter.matches(line)) continue;
				    	
			    		if (readstat == 2)
			    		{
						    synchronized (t) {
					    		t.append(line);
						        t.append("\n");	
							}
			    		}
			    		else {
			    			buffer.write(line);
			    		}
			    		
				    }
				    
				    line = reader.readLine();			
				    
				}
			}
			catch (InterruptedException ie) {
				//exit from applet ...
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			finally {
				if (reader != null) try { reader.close(); } catch (Throwable ignored) {}
			}
		}
		
	}
		
	public void init()
	{	
				
		String p = System.getenv("ProgramFiles");
		String px86 = p + " (x86)";
		
		if (!new File(logdir).exists()) {
			
			File f = new File(p + "\\S5Technology\\S5Tech Backend Services\\Network\\logs");
			if (f.exists() && f.isDirectory()) {
				logdir = p + "\\S5Technology\\S5Tech Backend Services\\Network\\logs";
			}	
		
			File fx86 = new File(px86 + "\\S5Technology\\S5Tech Backend Services\\Network\\logs");
			if (fx86.exists() && fx86.isDirectory()) {
				logdir = px86 + "\\S5Technology\\S5Tech Backend Services\\Network\\logs";
			}
					
			if (System.getProperty("logdir", "").length() > 0) {
				System.err.println("override default log dir -> " + logdir + " -> with -> " + System.getProperty("logdir"));
				logdir = System.getProperty("logdir");
			}
			
		}
		
		System.err.println("using log dir: " + logdir);
		System.err.println("max file size set to " + maxfilesiz);
		
		setBackground(Color.decode("#e0e0e0"));
		
		JTabbedPane tab = new JTabbedPane();
		//tab.setBounds(0, 0, 1400, 700);
		tab.setPreferredSize(new Dimension(1000, 500));
		tab.setAlignmentX(LEFT_ALIGNMENT);
		
		TextArea t_app = new TextArea();		
		//t_app.setBounds(0, 0, 1400, 700);
		t_app.setFont(new Font("Courier", 1, 12));
		t_app.setEditable(false);	
		t_app.setForeground(Color.decode(COLOR_RUNNING));
		tab.add("APPLICATION " + PAUSE_SUFFIX, t_app);
		
		TextArea t_xml = new TextArea();		
		//t_xml.setBounds(0, 0, 1400, 700);
		t_xml.setFont(new Font("Courier", 1, 12));
		t_xml.setEditable(false);
		t_xml.setForeground(Color.decode(COLOR_RUNNING));
		tab.add("XML SERVER " + PAUSE_SUFFIX, t_xml);
		
		TextArea t_ser = new TextArea();		
		//t_ser.setBounds(0, 0, 1400, 700);
		t_ser.setFont(new Font("Courier", 1, 12));
		t_ser.setEditable(false);
		t_ser.setForeground(Color.decode(COLOR_RUNNING));
		tab.add("SERIALFRAMING" + PAUSE_SUFFIX, t_ser);
		
		TextArea t_scan = new TextArea();		
		//t_scan.setBounds(0, 0, 1400, 700);
		t_scan.setFont(new Font("Courier", 1, 12));
		t_scan.setEditable(false);
		t_scan.setForeground(Color.decode(COLOR_RUNNING));
		tab.add("SCANPROBES " + PAUSE_SUFFIX, t_scan);
				
		/*
		TextArea t_prup = new TextArea();		
		//t_prup.setBounds(0, 0, 1400, 700);
		t_prup.setFont(new Font("Courier", 1, 12));
		t_prup.setEditable(false);
		t_prup.setForeground(Color.decode(COLOR_RUNNING));
		tab.add("PRICE UPDATES" + PAUSE_SUFFIX, t_prup);
		*/
		
		TextArea t_hist = new TextArea();		
		//t_hist.setBounds(0, 0, 1400, 700);
		t_hist.setFont(new Font("Courier", 1, 12));
		t_hist.setEditable(false);
		t_hist.setForeground(Color.decode(COLOR_RUNNING));
		tab.add("HISTORY " + PAUSE_SUFFIX, t_hist);
		
		final LogViewAppletMonitor [] monitors = new LogViewAppletMonitor []  {
				new LogViewAppletMonitor(logdir + "/application.log", null, t_app),
				new LogViewAppletMonitor(logdir + "/xmlserver.log", null, t_xml),
				new LogViewAppletMonitor(logdir + "/serialframing.log", null, t_ser),
				new LogViewAppletMonitor(logdir + "/scanprobes.log", null, t_scan),
				//new LogViewAppletMonitor(logdir + "/priceperformance.log", null, t_prup),
				new LogViewAppletMonitor(logdir + "/../run/data/history.log", null, t_hist)
		};
		
		final JTabbedPane ftab = tab;
		tab.addMouseListener(new MouseListener() {	
			int prevIndex = 0;			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				int n= ftab.getSelectedIndex();
				if (n != prevIndex) {
					prevIndex = n;
					return;
				}
				prevIndex = n;
				String s = ftab.getTitleAt(n);
				if (s.endsWith(PAUSE_SUFFIX)) 
				{
					ftab.getSelectedComponent().setForeground(Color.decode(COLOR_PAUSED));
					s = s.substring(0, s.length() - PAUSE_SUFFIX.length()) + PLAY_SUFFIX;
					monitors[n].pause();
				}
				else 
				{
					ftab.getSelectedComponent().setForeground(Color.decode(COLOR_RUNNING));
					s = s.substring(0, s.length() - PLAY_SUFFIX.length()) + PAUSE_SUFFIX;
					monitors[n].play();
				}
				ftab.setTitleAt(n, s);
			}
		});
				
		add(tab, BorderLayout.CENTER);
						
		for (LogViewAppletMonitor monitor : monitors)
		{
			Thread t = new Thread(monitor);
			t.setDaemon(true);
			t.start();
		}
				
	}
	
	public LogViewer()
	{
		super();
		init();
	}
	
	public LogViewer(String ldir)
	{
		super();
		setLogdir(ldir);
		init();
	}
	
}
