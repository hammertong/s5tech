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
 
/**
 * 
 */
package com.s5tech.net.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class provides active queueing by continuously emptying the queue,
 * sending the element to multiple subscribers
 * 
 * @author $Author: S5Tech
 * @version $Revision: 1.7 $, $Date: 2010/11/26 10:29:05 $
 */
public class ActiveQueue<T> extends LinkedBlockingQueue<T>implements Runnable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

	private BlockingQueue<T> queue;
	private List<IActiveQueueSubscriber<T>> subscribers;
	private boolean killed;
	private boolean swallowThrowablesInRun;
	private String name;
	private IExceptionListener exceptionListener;
	private Thread proc;

	public ActiveQueue(String name) {
		this(null, name);
	}

	public ActiveQueue(IActiveQueueSubscriber<T> subscriber, String name) {
		this(subscriber, name, true);
	}

	public ActiveQueue(IActiveQueueSubscriber<T> subscriber, String name, boolean daemon) {
		this(subscriber, name, daemon, Thread.NORM_PRIORITY);
	}

	public ActiveQueue(IActiveQueueSubscriber<T> subscriber, String name, boolean daemon, int threadPriority) {
		subscribers = new ArrayList<IActiveQueueSubscriber<T>>();
		queue = new LinkedBlockingQueue<T>();
		this.name = name;
		swallowThrowablesInRun = (System.getProperty("logThrowablesInQueues") == null);
		addSubscriber(subscriber);
		proc = new Thread(this, name);
		proc.setDaemon(daemon);
		proc.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		kill();
		super.finalize();
	}

	public void setPriority(int newPriority) {
		proc.setPriority(newPriority);
	}

	public int getPriority() {
		return proc.getPriority();
	}

	public boolean isDaemon() {
		return proc.isDaemon();
	}
	
	public void setExceptionListener(IExceptionListener listener) {
		exceptionListener = listener;
	}

	public void swallowThrowablesInRun(boolean yes) {
		swallowThrowablesInRun = yes;
	}

	public void addSubscriber(IActiveQueueSubscriber<T> subscriber) {
		if (subscriber != null) {
			synchronized (subscribers) {
				subscribers.add(subscriber);
			}
		}
	}

	public boolean removeSubscriber(IActiveQueueSubscriber<T> subscriber) {
		synchronized (subscribers) {
			return subscribers.remove(subscriber);
		}
	}

	public void removeAllSubscribers() {
		synchronized (subscribers) {
			subscribers.clear();
		}
	}

	public boolean add(T obj) {
		boolean res = false;
		res = queue.offer(obj);
		return res;
	}

	public synchronized void start() {
		if (!proc.isAlive())
			proc.start();
	}

	public void kill() {
		killed = true;
		removeAllSubscribers();
		proc.interrupt();
	}

	public void run() {

		T t;

		try {
			while (!killed) {
				
				t = queue.take();

				// Got an object..
				if (subscribers != null) {
					synchronized (subscribers) {
						for (IActiveQueueSubscriber<T> s : subscribers) {
							if (s != null) {
								try {
									s.elementFromQueue(t);
								} catch (Throwable tx) {
									if(swallowThrowablesInRun) {
										log.warn("Throwable swallowed in ActiveQueue.run!!! {}", tx);
									} else if(exceptionListener != null) {
										exceptionListener.onException(tx);
									} else {
										log.error("Exception caught in ActiveQueue.run: {}", tx);
									}
								}
							}
						}
					}
				}
			}
		} catch (InterruptedException e) {
			log.debug("ActiveQueue \"" + name + "\" killed");
			killed = true;
		}
	}
}
