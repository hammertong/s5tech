package com.s5tech.backend.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

@SuppressWarnings("serial")
public class StickerForm extends ActionForm {

	private String product = null;
	private String submit = null;

	public StickerForm() {
		super();
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		product = "";
	}

	public String getProduct() {
		return (product == null ? "" : product.trim());
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

}
