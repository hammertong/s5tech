package com.s5tech.backend;

public interface IConstants {

	//session atttribute keys
	public static final String USER_VIEW_KEY = "USER_VIEW";
	
	//struts global forward
	public static final String SYSTEM_FAILURE_KEY = "SystemFailure";
	public static final String SESSION_TIME_OUT_KEY = "SessionTimeOut";
	
	//struts page forward
	public static final String SUCCESS_KEY = "Success";
	public static final String FAILURE_KEY = "Failure";
	
	//servlet context attributes
	public static final String DATA_ACCESS_SERVICES_KEY = "data-access";
	
	//generic use constants
	public static final String UPDATE_USER_PREFIX = "HSID:";	
	
	//defaults 
	public static final String DEFAULT_LOCALE = System.getProperty("com.s5tech.backend.DEFAULT_LOCALE", "it_IT"); 
		
}
