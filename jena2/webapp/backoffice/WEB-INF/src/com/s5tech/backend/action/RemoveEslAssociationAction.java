package com.s5tech.backend.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.dao.GenericDao;
import com.s5tech.backend.form.RemoveEslAssociationForm;

public class RemoveEslAssociationAction extends Action {
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm frm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 
	{		
		try {
			
			RemoveEslAssociationForm form = RemoveEslAssociationForm.class.cast(frm);

			GenericDao dao = GenericDao.class.cast(
					getServlet().getServletContext()
					.getAttribute(IConstants.DATA_ACCESS_SERVICES_KEY));
			
			List<?> esls = dao.executeNamedQuery("esl exists", form.getEsl());			
			if (esls == null || esls.size() == 0) {
				ActionMessages errors = new ActionMessages();				
				ActionMessage newError = new ActionMessage("remesl.error.eslnotfound");
				errors.add ("esl", newError);						
				saveErrors(request, errors);				
				return new ActionForward(mapping.getInput());
			}		
			
			if (dao.executeNamedCommand("remesl detach", form.getEsl()) <= 0) {				
				ActionMessages errors = new ActionMessages();			
				ActionMessage newError = new ActionMessage("remesl.failed");
				errors.add (ActionErrors.GLOBAL_MESSAGE, newError);			
				saveErrors(request, errors);				
				return new ActionForward(mapping.getInput());
			}
			
			form.reset(null, null);
			return mapping.findForward("Success");
			
		}
		catch (Throwable t) {
			
			ActionMessages errors = new ActionMessages();			
			ActionMessage newError = new ActionMessage("global.error.backend", (t.getMessage() != null ? t.getMessage() : ""));
			errors.add (ActionErrors.GLOBAL_MESSAGE, newError);			
			saveErrors(request, errors);				
			return new ActionForward(mapping.getInput());
			
		}			
		
	}
	
}
