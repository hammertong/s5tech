package com.s5tech.backend.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.view.UserView;

public class HomeAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		HttpSession session = request.getSession();
		
		//
		// set locale if required
		//
		
		String requestedLocale = request.getParameter("locale");		
		if (requestedLocale != null && requestedLocale.length() > 0) {
			session.setAttribute(Globals.LOCALE_KEY, new Locale(requestedLocale));			
		}
		
		//
		// set locale to DEFAULT_LOCALE if not done yet
		//
		
		if (session.getAttribute(Globals.LOCALE_KEY) == null) {
			session.setAttribute(
					Globals.LOCALE_KEY, 
					new Locale(IConstants.DEFAULT_LOCALE));
		}
		
		UserView userView = UserView.class.cast(
				request.getSession().getAttribute(IConstants.USER_VIEW_KEY));		
		
		
		//
		// create roles and context based forward
		//
		
		String forward = null;		
				
		userView.setCurrentContext (request.getParameter("context"));
		
		if (userView.isAdministrator() &&
				userView.getCurrentContext().equals("networkDiagnostics")) {			
			forward = "System";			
		}
		else {
			if (userView.isInRole("priceChangeFunction")) {				
				forward = "Pricer"; 
			}
			else if (userView.isInRole("reportPriceChange")) {				
				forward = "Daily"; 
			}
			else if (userView.isAdministrator()) {				
				forward = "Alerts";					
			}
			else {
				forward = "Product";
			}			
		}
		
		return mapping.findForward (forward);
		
	}

}
