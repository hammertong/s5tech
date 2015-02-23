package com.s5tech.backend.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

@SuppressWarnings("serial")
public class ProductSearchForm extends ActionForm {

	private String product = null;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {

		ActionErrors errors = new ActionErrors();
		if (product == null || product.length() == 0) {
			ActionMessage newError = new ActionMessage("product.error.fillproduct");
			errors.add("product", newError);
		}
		return errors;

	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		resetFields();
	}

	protected void resetFields() {
		this.product = "";
	}

	public String getProduct() {
		return (product == null ? "" : product.trim());
	}

	public void setProduct(String product) {
		this.product = product;
	}

}
