package com.s5tech.backend.service;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

import com.s5tech.backend.dao.GenericDao;
import com.s5tech.backend.model.Setting;
import com.s5tech.backend.model.User;
import com.s5tech.backend.util.Base64Encoder;
import com.s5tech.backend.view.UserView;

public class SecurityService implements IAuthentication {
	
	GenericDao dao = null;
	
	public SecurityService(GenericDao dao)
	{
		this.dao = dao;
	}
	
	public UserView login(
			String accessNumber, 
			String pin)
			throws InvalidLoginException {
		
		try {
						
			List<?> data = dao.executeNamedQuery("login", accessNumber);			
			if (data == null || data.size() == 0) throw new Exception("userid not found");			
			
			User user = User.class.cast(data.iterator().next());
			
			byte[] bytesOfMessage = pin.getBytes("ASCII");
			MessageDigest md5 = MessageDigest.getInstance("MD5");			
			byte [] d = md5.digest(bytesOfMessage);			
						
			String hashFromDB = user.getPassword();
			String hashLogin = new String(Base64Encoder.encode(d));
			
		    if (!hashFromDB.equals(hashLogin)) throw new Exception("login failure");
	   	
			UserView userView = new UserView(user.getName(), user.getSurname());
			userView.setRole(user.getRole());
			userView.setId(user.getUserId());
		
			//
			// Set permissions here ...
			//			
			if (user.getRole().equals("SuperAdmin")
					|| user.getRole().equals("Administrator")) {
				List<?> ls = dao.executeNamedQuery("permissions from installationsettings");				
				Iterator<?> i = ls.iterator();
				while (i.hasNext()) {
					Setting s = Setting.class.cast(i.next());
					if (s.getValue() != null && s.getValue().equalsIgnoreCase("true")) {
						userView.addPermission(s.getSetting());
					}
					else if (s.getValue() != null && s.getValue().equalsIgnoreCase("super")
							&& user.getRole().equals("SuperAdmin")) {
						userView.addPermission(s.getSetting());
					}
				}				
			}
			
			return userView;
		
		}
		catch (Throwable t) {			
			
			String msg = "Invalid Login Attempt by " + accessNumber + ":" + pin;
			throw new InvalidLoginException(msg);
		}
		
	}
	
}
