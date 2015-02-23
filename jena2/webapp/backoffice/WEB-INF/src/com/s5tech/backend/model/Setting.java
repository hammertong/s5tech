package com.s5tech.backend.model;

public class Setting {
	
	private String setting;
	private String value;
	
	public Setting () {}
	
	public String getSetting() {
		return setting;
	}
	public void setSetting(String setting) {
		this.setting = setting;
	}
	public String getValue() {
		return value;
	}	
	public void setValue(String value) {
		this.value = value;
	}	
	
	public void set (String setting, String value) { 
		this.setting = setting; 
		this.value = value; 
	}
	
}
