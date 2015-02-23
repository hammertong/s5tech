package com.s5tech.backend.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

@SuppressWarnings("serial")
public class AssociateEslForm extends ActionForm {

	private String product;
	private String esl;	

	public AssociateEslForm() {
		super();
		resetFields();
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {

		ActionErrors errors = new ActionErrors();

		if (product == null || product.length() == 0) 
		{			
			ActionMessage newError = new ActionMessage("assesl.error.insert");
			errors.add ("product", newError);
		}
		
		if (esl == null || esl.length() == 0) 
		{	
			ActionMessage newError = new ActionMessage("assesl.error.insertesl");
			errors.add ("esl", newError);
			req.setAttribute("focus", "esl");
		}
		else 
		{			
			String esl = getEsl();
			boolean invalidate = (esl.length() != 16);
			
			if (!invalidate) {				
				for (int i = 0; i < esl.length(); i ++) {
					if ("0123456789ABCDEF".indexOf(esl.charAt(i)) < 0) {
						invalidate = true;
						break;					
					}
				}
			}
			
			if (invalidate) {				
				ActionMessage newError = new ActionMessage("assesl.error.eslnotvalid");
				errors.add ("esl", newError);
			}			
		}
		
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		resetFields();
	}

	protected void resetFields() {
		product = "";
		esl = "";
	}

	public String getProduct() {
		return (product == null ? "" : product.trim());
	}

	public void setProduct(String product) {
		this.product = (product != null ? product.trim() : null);;
	}

	public String getEsl() {
		return (esl == null ? "" : esl.trim().toUpperCase());
	}

	public void setEsl(String esl) {
		this.esl = (esl != null ? esl.trim() : null);
	}

}
