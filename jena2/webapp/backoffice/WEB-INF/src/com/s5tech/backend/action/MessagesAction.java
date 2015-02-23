package com.s5tech.backend.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.PropertyMessageResources;

public class MessagesAction extends Action {
	
	private static final Log log = LogFactory.getLog(Action.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String prefix = request.getParameter("prefix");
		
		List  ls = new ArrayList<>();
		
		if (prefix != null && prefix.length() > 0) {
			try 
			{
				PropertyMessageResources p = (PropertyMessageResources) 
						request.getAttribute(Globals.MESSAGES_KEY);			
				Field f = PropertyMessageResources.class.getDeclaredField("messages");
				f.setAccessible(true);
				HashMap map = HashMap.class.cast(f.get(p));
				for (Object okey : map.keySet()) {
					String key = okey.toString();
					int n = key.indexOf('.');
					key = (n > 0 ? key.substring(n + 1) : key);
					if (key.startsWith(prefix)) {						
						ls.add(key);	
					}
				}			 
				f.setAccessible(false);
			}
			catch (Throwable t) 
			{
				log.error("reading message keys - ", t);
			}
		}
		
		request.setAttribute("messages", ls);
		return mapping.findForward("Messages");
		
	}
	
}
