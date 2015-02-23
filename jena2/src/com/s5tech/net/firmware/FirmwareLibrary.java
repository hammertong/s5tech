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
 
package com.s5tech.net.firmware;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.activemq.util.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.entity.FirmwareVersion;
import com.s5tech.net.util.ActiveQueue;
import com.s5tech.net.util.IActiveQueueSubscriber;
import com.s5tech.net.util.ISystemKeys;
import com.s5tech.net.util.Tools;

/**
 * Maintains a map of firmware images found in the deviceFirmware folder in the storage location.
 * The folder is kept clean, meaning that invalid files are deleted, and only the latest version of the firmware for each device type is kept at all times.
 * 
 * @author S5Tech Development Team
 *
 */
public final class FirmwareLibrary {

	public static final String FIRMWARE_FOLDER = System.getProperty("firmware.dir", "./run/data/firmware");
	
	private static FirmwareLibrary _instance;
	private static final Object lock = "";

	/**
	 * For testing. If this is set prior to the first invocation of {@link #instance()}, this folder is used instead of {@link PlatformFacade#getStorageLocation()}
	 */
	public static File base = null;
	
	public static FirmwareLibrary instance() {
		synchronized (lock) {
			if(_instance == null) _instance = new FirmwareLibrary();
		}
		return _instance;
	}

	private final Logger log;
	private final Timer fileMonitor;
	private File libraryFolder;
	private Map<Integer,FirmwareInfo> library;
	private Map<Integer,IFirmwareUpdateSubscriber> subscribers;
	private ActiveQueue<FirmwareInfo> newFirmwareQueue;
	private long prevLoadedAt;
	
	private FirmwareLibrary() {

		log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);
		fileMonitor = new Timer("Firmware monitor", true);

		library = new HashMap<Integer, FirmwareInfo>();
		subscribers = new HashMap<Integer, IFirmwareUpdateSubscriber>();
		
		// Initialize
		libraryFolder = new File(Tools.getBasedir() + FIRMWARE_FOLDER);
		
		log.info("FW Library searching for new firmware files in: {}", Tools.getBasedir() + FIRMWARE_FOLDER);
		
		if(libraryFolder.exists()) {
			if(!libraryFolder.isDirectory()) {
				log.error(libraryFolder.getAbsolutePath() + " exists, but is a file! Firmware library not starting!");
				return;
			}
		} 
		else {
			boolean res = false;
			try {
				res = libraryFolder.mkdirs();
			} catch(SecurityException e) {
				log.debug("cannot create firmware {}: {}", FIRMWARE_FOLDER, e);
			}
			if(!res) {
				log.error("Unable to create folder " + libraryFolder.getAbsolutePath() + "! Firmware library not starting!");
				return;
			}
		}

		log.info("Using folder " + libraryFolder);

		newFirmwareQueue = new ActiveQueue<FirmwareInfo>(new IActiveQueueSubscriber<FirmwareInfo>() {
			public void elementFromQueue(FirmwareInfo element) {
				IFirmwareUpdateSubscriber subscriber = subscribers.get(element.getDeviceType());
				if(subscriber != null) subscriber.onNewFirmware(element);
			}
		}, "New firmware queue");
		newFirmwareQueue.setPriority(Thread.MIN_PRIORITY);
		
		fileMonitor.schedule(new TimerTask() {
			@Override
			public void run() {
				loadLibrary();
			}
		}, 10000, 30000);

	}

	public void init() {
		loadLibrary();
	}
		
	public void addSubscriber(int deviceType, IFirmwareUpdateSubscriber subscriber) {
		subscribers.put(deviceType, subscriber);
	}
	
	public IFirmwareUpdateSubscriber removeSubscriber(int deviceType) {
		return subscribers.remove(deviceType);
	}
	
	/**
	 * Reads all .bin files in the library folder, and returns a list of files with valid firmware.
	 * The list only contains the firmware if newer than the current.
	 * Invalid files and obsolete firmware files are deleted
	 */
	synchronized private void loadLibrary() {
		
		// Remove firmware info if files removed
		
		Set<Integer> rm = new HashSet<Integer>();		
		for(FirmwareInfo fwi : library.values()) {
			if (fwi.getFile() == null) continue; //added with fw upload
			File f = new File(fwi.getFile().getAbsolutePath());
			if(!(f.exists())) {
				rm.add(fwi.getDeviceType());
			}
		}
		
		for(int dt : rm) {
			FirmwareInfo removed = library.remove(dt);
			log.info("Remove firmware info; removed from folder: {}", removed);
		}
		
		Map<Integer, FirmwareInfo> newFw = new HashMap<Integer, FirmwareInfo>();
		
		File[] files = libraryFolder.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file != null && 
							file.isFile() &&
							file.lastModified() > prevLoadedAt &&
							file.getName().endsWith(".bin");
			}
		});

		if(files.length > 0) {
			if (log.isTraceEnabled())
				log.trace("{} new files found in the library folder", files.length);
		}

		prevLoadedAt = System.currentTimeMillis();

		boolean deleting;
		int newFiles = 0;
		
		// Validate and put files into the library
		// Only read files which were modified since last call
		for(File f : files) {
			
			newFiles++;
			deleting = false;
			
			try {
				
				FirmwareInfo fw = new FirmwareInfo();
				
				if(FirmwareInfo.load(fw, new BufferedInputStream(new FileInputStream(f)))) 
				{
					fw.setFile(f);

					// Compare to the already read firmware version for the device type
					if(newFw.containsKey(fw.getDeviceType())) {
						fw = compareAndDeleteOldest(fw, newFw.get(fw.getDeviceType()));
					}

					// Compare to the library's current firmware version for the device type
					if(library.containsKey(fw.getDeviceType())) {
						fw = compareAndDeleteOldest(fw, library.get(fw.getDeviceType()));
						// If the current fw info is the newest, don't consider it an update
						if(library.containsValue(fw)) {
							fw = null;
						}
					}

					if(fw != null) {
						newFw.put(fw.getDeviceType(), fw);
						log.info("Current firmware: {}", Tools.toStringObj(fw));
					}

				} else {
					
					log.info("Removing invalid firmware file: {}", f.getAbsolutePath());
					deleting = true;
					f.delete();
					
				}
			} 
			catch (IOException e) {
				if(deleting)
					log.error("IO error (" + e.getMessage() + ") while attempting to delete obsolete firmware file: " + f.getAbsolutePath());
				else
					log.error("IO error (" + e.getMessage() + ") while attempting to load firmware file: " + f.getAbsolutePath());
				log.debug("",e);
			}
		}
		
		if(newFiles > 0) {
			log.info("Checked {} new file(s)", newFiles);
			log.info("Loaded {} valid firmware file(s)", newFw.size());
		}
		
		if(newFw != null && newFw.size() > 0) {
			// add the firmwares
			for(FirmwareInfo fw : newFw.values()) {
				library.put(fw.getDeviceType(), fw);
			}
			
			// call the subscribers
			for(FirmwareInfo fw : newFw.values()) {
				newFirmwareQueue.add(fw);
			}
		}

		String fwList = Tools.listToString(library.values(), "\n");
		if (log.isInfoEnabled() && (newFw != null && newFw.size() > 0))
			log.info("Known firmware " + (Tools.isNullOrEmpty(fwList) ? "none" : fwList));
	}
	
	
	/**
	 * Returns deviceType number of uploaded firmware data
	 * if error returns -1
	 */
	synchronized public FirmwareInfo updateLibrary(byte [] data) {
			
		try {
			
			FirmwareInfo fw = new FirmwareInfo();			
			if(FirmwareInfo.load (fw, new BufferedInputStream(new ByteArrayInputStream(data)))) 
			{
				// Compare to the library's current firmware version for the device type
				if (library.containsKey(fw.getDeviceType())) {
					fw = compareAndDeleteOldest(fw, library.get(fw.getDeviceType()));
					// If the current fw info is the newest, don't consider it an update
					if (library.containsValue(fw)) {
						fw = null;
					}
				}

				if(fw != null) {					
					log.info("Upgrading firmware: {}", Tools.toStringObj(fw));
					library.put(fw.getDeviceType(), fw);
					newFirmwareQueue.add(fw);
					return fw;
				}

			} else {
				
				log.info("Invalid firmware file: {}, cannot load");
				
			}
		} 
		catch (IOException e) {			
			
			log.error("IO error (" + e.getMessage() + ") while attempting to upgrade firmware from binary data - {}", e);
			
		}
		
		return null;
		
	}
	

	/**
	 * Compares firmware and deletes the file of the oldest
	 * @param fw1
	 * @param fw2
	 * @return
	 */
	private FirmwareInfo compareAndDeleteOldest(FirmwareInfo fw1, FirmwareInfo fw2) {
		if(fw1 == null || fw2 == null) {
			return fw1 != null ? fw1 : fw2;
		}
		FirmwareInfo old = fw1.getVersion().compareTo(fw2.getVersion()) >= 0 ? fw2 : fw1;
		if (old.getFile() != null)
		{
			log.info("Removing old firmware file: {}", old.getFile().getAbsolutePath());
			old.getFile().delete();
		}
		return old == fw1 ? fw2 : fw1;
	}
	
	public Set<Integer> getKnownDeviceTypes() {
		return new HashSet<Integer>(library.keySet());
	}
	
	public FirmwareInfo getFirmwareForDeviceType(int deviceType) {
		return library.get(deviceType);
	}
	
	public FirmwareVersion getFirmwareVersionForDeviceType(int deviceType) {
		FirmwareInfo fw = library.get(deviceType);
		return fw == null ? null : fw.getVersion();
	}

	public ByteBuffer loadFirmware(FirmwareInfo fw) {
		if(fw == null || !fw.isValid() || fw.getFile() == null) return null;

		ByteBuffer bb = null;
		try {
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fw.getFile()));
			ByteArrayOutputStream bo = new ByteArrayOutputStream(inputStream.available());
			while(inputStream.available() > 0) {
				bo.write(inputStream.read());
			}
			bb = ByteBuffer.wrap(bo.toByteArray());
		} catch (IOException e) {
			log.error("Error loading firmware file: {}", e.getMessage());
			log.debug("",e);
		}
		
		return bb;
	}
		
	
}
