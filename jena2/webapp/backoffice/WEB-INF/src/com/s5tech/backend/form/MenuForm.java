package com.s5tech.backend.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

@SuppressWarnings("serial")
public class MenuForm extends ActionForm {

	private String method = null;
	private String submit = null;

	public MenuForm() {
		super();
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		method = "";
		submit = "";
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
}
