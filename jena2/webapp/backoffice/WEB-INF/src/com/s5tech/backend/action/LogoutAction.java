package com.s5tech.backend.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

public class LogoutAction extends Action {
 public ActionForward execute( ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)
    throws Exception {

    // Invalidate the User's session if one exists
    HttpSession session = request.getSession(false);
    if(session != null) {
      session.invalidate();
    }
    return mapping.findForward("Success");
  }
}
