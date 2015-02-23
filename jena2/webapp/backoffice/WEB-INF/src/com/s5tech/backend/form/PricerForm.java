package com.s5tech.backend.form;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

@SuppressWarnings("serial")
public class PricerForm extends ActionForm {

	private int step = 0;

	private String barcode;
	private String newprice;
	private String options;
	private String discount;
	private String points;
	private String pricePerUnit;

	public PricerForm() {
		super();
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {

		ActionErrors errors = new ActionErrors();
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		step = 1;
		barcode = "";
		newprice = "";
		options = "";
		discount = "";
		points = "";
	}
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = (barcode != null ? barcode.replace('\t',  ' ').trim() : "");
	}

	public String getNewprice() {
		return newprice;
	}

	public void setNewprice(String newprice) {
		this.newprice = (newprice != null ? newprice.replace('\t',  ' ').trim() : "");
	}

	public String getOptions() {
		return (options == null ? "" : options.trim());
	}

	public void setOptions(String options) {
		this.options = options;
	}
	
	public String getDiscount() {
		return (discount == null ? "" : discount.trim());
	}

	public void setDiscount(String discount) {
		this.discount = (discount != null ? discount.replace('\t',  ' ').trim() : "");
	}

	public String getPoints() {
		return (points == null ? "" : points.trim());
	}

	public void setPoints(String points) {
		this.points = (points != null ? points.replace('\t',  ' ').trim() : "");
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	
}
