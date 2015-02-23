package com.s5tech.backend.action;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.taglib.tiles.GetAttributeTag;

import com.s5tech.backend.form.StickerForm;

//import com.s5tech.backend.framework.LookupDispatchAction;

public class StickerLookupDispatchAction extends LookupDispatchAction {

	@SuppressWarnings("rawtypes")
	@Override
	protected Map getKeyMethodMap() {
		
		HashMap<String, String> map = new HashMap<String, String>();		
		map.put("sticker.stickersupdated", "stickersupdated");		
		map.put("sticker.print", "print");		
		map.put("sticker.empty", "empty");
				
		return map;
	}
	
	public ActionForward print (
			ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
				throws IOException, ServletException {		
		String forward = request.getParameter("forward");
		
		//TODO ...
		
		StickerForm f = StickerForm.class.cast(form);				
		System.err.println("***************** printing product ... " + f.getProduct());		
		return (forward != null ? mapping.findForward(forward) : new ActionForward(mapping.getInput()));
	}
	
	public ActionForward empty (
			ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
					throws IOException, ServletException {
		
		//TODO ...
		HttpSession session = request.getSession();
		session.setAttribute("stickersupdated", "0");
		return new ActionForward(mapping.getInput());  
	}
	
	public ActionForward stickersupdated (
			ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
					throws IOException, ServletException {
		//TODO ...
		return new ActionForward(mapping.getInput()); 
	}

}
