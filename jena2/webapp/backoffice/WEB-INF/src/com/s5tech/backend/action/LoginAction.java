package com.s5tech.backend.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.dao.GenericDao;
import com.s5tech.backend.form.LoginForm;
import com.s5tech.backend.service.IAuthentication;
import com.s5tech.backend.service.SecurityService;
import com.s5tech.backend.view.UserView;


public class LoginAction extends Action {
	
   
   public ActionForward execute( ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {

      UserView userView = null;
      
      String accessNbr = ((LoginForm)form).getAccessNumber();
      String pinNbr = ((LoginForm)form).getPinNumber();
      
      IAuthentication service = new SecurityService(
    		  GenericDao.class.cast(
				getServlet().getServletContext()
				.getAttribute(IConstants.DATA_ACCESS_SERVICES_KEY)));
  	
   	  userView = service.login(accessNbr, pinNbr);
      
      HttpSession session = request.getSession(false);
      
      if (session != null) session.invalidate();
      
      session = request.getSession(true);
      session.setAttribute(IConstants.USER_VIEW_KEY, userView);      
      
      return mapping.findForward("Home");      
      
  }
}
