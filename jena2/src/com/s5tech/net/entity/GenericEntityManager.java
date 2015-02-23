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

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s5tech.net.util.ISystemKeys;


/**
 * This is a generic class for managing entities in a map
 * If autosort is enabled, all keys must implement the {@link Comparable} interface
 * @author S5Tech Development Team
 */
public class GenericEntityManager<K extends Serializable,V extends Serializable> implements Serializable {

	private static final long serialVersionUID = -688402324007321762L;
	
	protected Map<K,V> entities;
	
	protected static final Logger log = LoggerFactory.getLogger(ISystemKeys.APPLICATION);

	public GenericEntityManager() {
		this(false);		
	}

	/**
	 * 
	 * @param autoSort if true, the keys in the map will automatically be sorted. See {@link SortedMap}
	 */
	public GenericEntityManager(boolean autoSort) {
		if(autoSort)
			entities = new TreeMap<K, V>();
		else
			entities = new Hashtable<K, V>();
	}

	/**
	 * Indicates automatic sorting of the keys of the map
	 * @return
	 */
	public boolean isAutoSorted() {
		return false;
	}
	
	/**
	 * Adds an entity
	 * @param key the key of the entity
	 * @param value the value of the entity
	 * @return the previous value, if any, for the specified key
	 */
	public V put(K key, V value) {
		return entities.put(key, value);
	}

	/**
	 * Get an entity
	 * @param key
	 * @return
	 */
	public V get(K key) {
		return entities.get(key);
	}
	
	/**
	 * Removes an entity identified by its key
	 * @param key the key for the entity to remove
	 * @return the value, if any, for the specified key
	 */
	public V remove(K key) {
		return entities.remove(key);
	}

	/**
	 * Removes multiple entities.
	 * @param keys the key for the entities to remove
	 */
	public void remove(Collection<K> keys) {
		if(keys == null) return;
		for(K key : keys)
			entities.remove(key);
	}
	
	/**
	 * Check for existence of a specific key
	 * @param key
	 * @return
	 */
	public boolean contains(K key) {
		return entities.containsKey(key);
	}
	
	/**
	 * Get all entities. Currently this returns the actual map backing this entity manager
	 * @return all entities, indexed by their key
	 */
	public Map<K, V> getAll() {
		return entities;
	}

	public void clear() {
		entities.clear();
	}
	
	public int size() {
		return entities.size();
	}
	
	/**
	 * 
	 * @param entities
	 */
	protected void addAll(Map<K, V> entities) {
		this.entities.putAll(entities);
	}
				
	
}
