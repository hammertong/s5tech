package com.s5tech.backend.action;

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
import com.s5tech.backend.form.AssociateEslForm;

public class AssociateEslAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm frm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionMessages errors = new ActionMessages();
		AssociateEslForm form = AssociateEslForm.class.cast(frm);
		
		try {
			
			GenericDao dao = GenericDao.class.cast(
					getServlet().getServletContext().getAttribute(IConstants.DATA_ACCESS_SERVICES_KEY));
			
			int exitcode = dao.executeStoredProc("assesl join",
						form.getEsl(),
						form.getProduct(),
						IConstants.UPDATE_USER_PREFIX + request.getSession().getId());
			
			switch (exitcode) {
			
			case 0:
				break;
			
			case 50001:
				errors.add ("product", new ActionMessage("assesl.error.notfound"));						
				saveErrors(request, errors);		
				break;
				
			case 50002:
				errors.add ("esl", new ActionMessage("assesl.error.eslnotfound"));						
				saveErrors(request, errors);	
				break;
				
			default:
				throw new Exception("unexpected SP exitcode: " + exitcode);				
			}
			
			if (errors.size() > 0) 
				return new ActionForward(mapping.getInput());
			
			if (dao.executeNamedCommand(
					"assesl multiplelabelprinting", 
					form.getProduct()) <= 0) {
				errors.add (ActionErrors.GLOBAL_MESSAGE, new ActionMessage("assesl.error.multiplelabelprinting"));						
				saveErrors(request, errors);	
			}
			
			if (errors.size() > 0) 
				return new ActionForward(mapping.getInput());
			
			//everithing ok, continue joining new producs ...
			
			form.reset(null, null);
			return mapping.findForward("Success");
			
		}
		catch (Throwable t) {
			
			ActionMessage newError = new ActionMessage("global.error.backend", (t.getMessage() != null ? t.getMessage() : ""));
			errors.add (ActionErrors.GLOBAL_MESSAGE, newError);			
			saveErrors(request, errors);				
			return new ActionForward(mapping.getInput());
			
		}		
		
	}
	
}
