package com.s5tech.backend.action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.dao.GenericDao;
import com.s5tech.backend.form.PricerForm;
//import com.s5tech.backend.framework.LookupDispatchAction;
import com.s5tech.backend.model.Esl;
import com.s5tech.backend.model.Product;
import com.s5tech.backend.util.RequestTools;
import com.s5tech.backend.view.ViewFormatter;

public class PricerAction extends LookupDispatchAction {
	
	//TODO: get read them db ...?	
	private static int minPoints = 1;	
	private static int maxPoints = 999;	
	private static int minDiscount = 1;
	private static int maxDiscount = 99;	
	
	private static char decimalSeparator = ',';
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Map getKeyMethodMap() {
		HashMap<String, String> map = new HashMap<String, String>();		
		map.put("pricer.next", "next");		
		map.put("pricer.previous", "previous");	
		map.put("pricer.publish", "publish");		
		return map;
	}
	
	public ActionForward next(ActionMapping mapping, ActionForm frm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try 
		{
			HttpSession session = request.getSession();
			PricerForm form = PricerForm.class.cast(frm);
			int step = form.getStep();
			Product product = null;
			
			ActionMessages errors = new ActionMessages();
			
			GenericDao dao = GenericDao.class.cast(
					getServlet().getServletContext()
					.getAttribute(IConstants.DATA_ACCESS_SERVICES_KEY));
						
			List<?> records;
			
			switch (step) {
			
			case 1:		

				if (form.getBarcode().length() == 0) {
					ActionMessage newError =
					        new ActionMessage("pricer.error.fillbarcode", "barcode");
					errors.add ("barcode", newError);
					break;
				}
				
				records = dao.executeNamedQuery("find product", form.getBarcode());
				
				if (records == null || records.size() == 0) {					
					ActionMessage newError = new ActionMessage("pricer.error.notfound");
					errors.add ("barcode", newError);
					break;
				}	
				
				product = Product.class.cast(records.iterator().next());
				
				records = dao.executeNamedQuery("product associated", product.getProduct());				
				
				if (records == null || records.size() == 0) { 
					ActionMessage newError = new ActionMessage(
							"pricer.error.productnotassociated", product.getProductDescription());
					errors.add ("barcode", newError);
					break;
				}
				
				if (errors.size() == 0) {
					session.setAttribute("product", product);
				}
				
				break;
			
			case 2:
				
				if (form.getNewprice().length() == 0) {
					ActionMessage newError =
					        new ActionMessage("pricer.error.fillprice", "newprice");
					errors.add ("newprice", newError);
				}
				
				if (errors.size() == 0) {
					try {
						String sep = "\\" + decimalSeparator;
						String regexp = "\\d*(" + sep + "\\d|" + sep + "\\d\\d)*";
						Pattern pattern = Pattern.compile(regexp);
						Matcher matcher = pattern.matcher(form.getNewprice());
						if (!matcher.matches()) {
							throw new Exception("");
						}
					}
					catch (Throwable t) {
						ActionMessage newError =
						        new ActionMessage("pricer.error.priceformat", decimalSeparator);
						errors.add ("newprice", newError);
					}
				}	
				
				if (errors.size() == 0) {
					
					product = Product.class.cast(session.getAttribute("product"));
					
					String esltype = null;
					
					records = dao.executeNamedQuery("find esl by product", product);
					if (records != null && records.size() > 0) {			
						esltype = Esl.class.cast(records.iterator().next()).getEslType();	
					}
										
					if (esltype != null && esltype.equalsIgnoreCase("ESL50")) {
						float x = Float.parseFloat(form.getNewprice());
						if (x < 0 && x >= 200) {
							ViewFormatter f = new ViewFormatter();
							ActionMessage newError = new ActionMessage("pricer.error.price", 
									"0", f.format(199.99D));
							errors.add ("newprice", newError);
						}
					}
					else if (esltype != null && esltype.equalsIgnoreCase("ESL70")) {
						float x = Float.parseFloat(form.getNewprice());
						if (x < 0 && x >= 2000) {
							ViewFormatter f = new ViewFormatter();
							ActionMessage newError = new ActionMessage("pricer.error.price", 
									"0", f.format(1999.99D));
							errors.add ("newprice", newError);
						}
					}
				}
				
				break;
				
			case 3:
				
				if (form.getOptions().equals("discount")) {
					if (form.getDiscount().length() == 0) {
						form.setOptions("");	
					}
					else {
						try {
							int test = Integer.parseInt(form.getDiscount());
							if (test < minDiscount || test > maxDiscount) {
								ActionMessage newError =
								        new ActionMessage("pricer.error.discountrange", minDiscount, maxDiscount);
								errors.add ("discount", newError);
							}
						}
						catch (Throwable t) {
							ActionMessage newError =
							        new ActionMessage("pricer.error.discountformat", "discount");
							errors.add ("discount", newError);					
						}
					}
				}
				else if (form.getOptions().equals("points")) {
					if (form.getPoints().length() == 0) {
						form.setOptions("");	
					}
					else {
						try {
							int test = Integer.parseInt(form.getPoints());
							if (test < minPoints || test > maxPoints) {
								ActionMessage newError =
								        new ActionMessage("pricer.error.pointsrange", minPoints, maxPoints);
								errors.add ("points", newError);
							}
						}
						catch (Throwable t) {
							ActionMessage newError =
							        new ActionMessage("pricer.error.pointsformat", "points");
							errors.add ("points", newError);					
						}
					}
				}
				
				product = Product.class.cast(session.getAttribute("product"));
				
				double p = (product.getActualPrice() != null ? product.getActualPrice() : 0);
				double ppu = (product.getActualPricePerUnit() != null ? product.getActualPricePerUnit() : 0);
				
				if (p > 0 & ppu > 0) {									
					float newppu = Float.parseFloat(form.getNewprice().replace(',', '.'));
					newppu *= ppu;
					newppu /= p;
					form.setPricePerUnit (String.format("%.2f", newppu));
				}
				else {
					form.setPricePerUnit ("0");
				}
								
				break;
			
			case 4:	
				//only display summary ...
				break;
			
			}
			
			if (errors.size() > 0) {
				// found errors, retry with prompt errors ... 
				form.setStep(step);
				saveErrors(request, errors);				
				return new ActionForward(mapping.getInput());				
			}
			else {
				// everithing ok go to next step ...
				if (step < 4) step ++;
				form.setStep(step);
			}			
									
		}
		catch (Throwable t) {
			
			t.printStackTrace();			
			ActionMessages errors = new ActionMessages();			
			ActionMessage newError = new ActionMessage("global.error.backend", (t.getMessage() != null ? t.getMessage() : ""));
			errors.add (ActionErrors.GLOBAL_MESSAGE, newError);			
			saveErrors(request, errors);				
			return new ActionForward(mapping.getInput());	
			//throw new ServletException("Pricer exception: " + t.getMessage());
			
		}		

		return mapping.findForward("Success");
		
	}

	public ActionForward previous(ActionMapping mapping, ActionForm frm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {		
			PricerForm form = PricerForm.class.cast(frm); 
			int step = form.getStep();
			if (step > 1) step --; 
			form.setStep(step);									
		}
		catch (Throwable t) {
			throw new ServletException("Pricer Action Error: " + t.getMessage());
		}		

		return mapping.findForward("Success");

	}
	
	public ActionForward publish(ActionMapping mapping, ActionForm frm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try {
			
			ActionMessages errors = new ActionMessages();
			PricerForm form = PricerForm.class.cast(frm);
			
			GenericDao dao = GenericDao.class.cast(
					getServlet().getServletContext()
					.getAttribute(IConstants.DATA_ACCESS_SERVICES_KEY));
						
			HttpSession session = request.getSession();
			
			Locale locale = Locale.class.cast(session.getAttribute(Globals.LOCALE_KEY));
			String culture = locale.getLanguage() + "-" + locale.getCountry().toUpperCase();
			if (!culture.equals("it-IT") && !culture.equals("en-US")) culture = "it-IT"; //default 
			
			Product p = Product.class.cast(session.getAttribute("product"));
			
			int exitcode = dao.executeStoredProc("pricer easy", 
					p.getProduct() , 
					form.getNewprice().replace(',', '.'), 
					form.getPricePerUnit().replace(',', '.'), 
					(form.getOptions().equals("discount") && form.getDiscount().length() > 0 ? form.getDiscount() : "0"),
					(form.getOptions().equals("offer") ? "-1" : "0"),
					(form.getOptions().equals("3x2") ? "-1" : "0"),
					"-1",
					(form.getOptions().equals("points") && form.getPoints().length() > 0 ? form.getPoints() : "0"),
					culture,
					RequestTools.formatSessionId(request));			
			
			switch (exitcode) {
			
			case 0:				
				break;
			case 50001:				
				errors.add ("barcode", new ActionMessage("pricer.error.notfound"));
				break;
			case 50002:
				errors.add ("barcode", new ActionMessage("pricer.error.eslnotfound"));				
				break;
			case 50003:
				ViewFormatter f1 = new ViewFormatter();				
				errors.add ("newprice",	new ActionMessage("pricer.error.price", "0", f1.format(999.99D)));
				break;
			case 50004:
				ViewFormatter f2 = new ViewFormatter();
				errors.add ("newprice",	new ActionMessage("pricer.error.price", "0", f2.format(1999.99D)));
				break;
			default:
				throw new Exception("unexpected SP exitcode: " + exitcode);
			}
			
			if(errors.size() > 0) {
				saveErrors(request, errors);				
				return new ActionForward(mapping.getInput());
			}
			
			form.reset(null, null);
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			
			ActionMessages errors = new ActionMessages();			
			ActionMessage newError = new ActionMessage("global.error.backend", (t.getMessage() != null ? t.getMessage() : ""));
			errors.add (ActionErrors.GLOBAL_MESSAGE, newError);			
			saveErrors(request, errors);				
			return new ActionForward(mapping.getInput());
			
			//throw new ServletException("Pricer Action Error: " + t.getMessage());
		}		
		
		return mapping.findForward("Home");
		
	}
	
}
