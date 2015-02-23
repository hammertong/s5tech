package com.s5tech.backend.util;

import javax.servlet.http.HttpServletRequest;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.view.UserView;

public class RequestTools {
	
	public static String formatSessionId(HttpServletRequest request) {
		if (request.getSession() == null) return "";		
		String k = new StringBuffer(IConstants.UPDATE_USER_PREFIX)
				.append(((UserView) request.getSession().getAttribute(IConstants.USER_VIEW_KEY)).getId()
						).toString();		
		return (k.length() > 25 ? k.substring(0, 25): k);		
	}
	
	public static boolean isInRole (String permissionName, HttpServletRequest request) 
	{
		if (request.getSession(false) == null) return false;
		Object o = request.getSession().getAttribute(IConstants.USER_VIEW_KEY);
		if (o == null) return false;
		UserView userview = UserView.class.cast(o);
		return userview.isInRole(permissionName);
	}	
	
	public static boolean isLoggedIn( HttpServletRequest request ){
	    if ( request.getSession(false) == null ||
	         (request.getSession().getAttribute( IConstants.USER_VIEW_KEY ) == null)){
	      return false;
	    } else{
	      return true;
	    }
	}
	
}
