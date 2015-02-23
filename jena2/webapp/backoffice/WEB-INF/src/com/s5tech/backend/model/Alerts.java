package com.s5tech.backend.model;

public class Alerts {
	
	private int Id;

	public int getId() {
		return this.Id;
	}

	public void setId(int value) {
		this.Id = value;
	}

	private String alertType;

	public String getAlertType() {
		return this.alertType;
	}

	public void setAlertType(String value) {
		this.alertType = value;
	}

	private String title;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String value) {
		this.title = value;
	}

	private String message;

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String value) {
		this.message = value;
	}

	private java.util.Date lastUpdate;

	public java.util.Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(java.util.Date value) {
		this.lastUpdate = value;
	}

}
