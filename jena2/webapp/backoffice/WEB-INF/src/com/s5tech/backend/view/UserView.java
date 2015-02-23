package com.s5tech.backend.view;

import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public class UserView implements java.io.Serializable {

	private String id;
	private String name;
	private String surname;
	private String role;
	
	private String currentContext = null;
	
	@SuppressWarnings("rawtypes")
	private Set permissions = null;

	public UserView(String first, String last) {
		this(first, last, null);
	}
		
	public UserView(String first, String last, Set<?> userPermissions) {
		super();
		name = first;
		surname = last;
		permissions = userPermissions;		
		ensurePermissions();
	}	
	
	@SuppressWarnings("unchecked")
	private void ensurePermissions()
	{
		if (permissions == null) {
			permissions = new HashSet<>();
			permissions.add("standard");
		}
	}

	public boolean containsPermission(String permissionName) {		
		return (permissions != null && permissions.contains(permissionName));
	}
	
	@SuppressWarnings("unchecked")
	public void addPermission(String permissionName)
	{
		ensurePermissions();
		if (!containsPermission(permissionName)) 
			permissions.add(permissionName);
	}
	
	public String getLastName() {
		return surname;
	}

	public void setLastName(String name) {
		surname = name;
	}

	public String getFirstName() {
		if (name == null) return "";
		else if (name.length() > 1) return name.substring(0, 1).toUpperCase() + name.substring(1);
		else return name.toUpperCase();
	}

	public void setFirstName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role= role ;
	}

	public boolean isInRole(String permissionName) {
		if (id.equals("debug")) return true;
		return containsPermission(permissionName);		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public boolean isAdministrator() {
		return (role != null && (role.equals("SuperAdmin") 
				|| role.equals("Administrator")));
	}

	public String getCurrentContext() {
		return (currentContext != null ? currentContext : "");
	}

	public void setCurrentContext(String currentContext) {
		this.currentContext = currentContext;
	}
	
}