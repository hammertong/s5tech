package com.s5tech.backend.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.dao.GenericDao;
import com.s5tech.backend.form.ProductSearchForm;
import com.s5tech.backend.model.Product;
import com.s5tech.backend.view.ProductView;
import com.s5tech.backend.view.ViewFormatter;

public class ProductDetailAction extends Action {
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm frm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Product product = null;
		
		try {
			
			HttpSession session = request.getSession();
			ProductSearchForm form = ProductSearchForm.class.cast(frm);
			
			GenericDao dao = GenericDao.class.cast(
					getServlet().getServletContext()
					.getAttribute(IConstants.DATA_ACCESS_SERVICES_KEY));
						
			List<?> result = dao.executeNamedQuery("find product", form.getProduct());
			if (result != null && result.size() > 0) {
				product = Product.class.cast(result.iterator().next());
			}
			
			if (product == null) {
				ActionMessages errors = new ActionMessages();
				ActionMessage newError = new ActionMessage("product.error.notfound");
				errors.add ("product", newError);
				saveErrors(request, errors);				
				return new ActionForward(mapping.getInput());	
			}					
		
			ViewFormatter viewFormatter = new ViewFormatter();
			ProductView view = new ProductView();
			synchronized (dao) {				
				List<?> viewlist = dao.executeNamedQuery("product view");				
				Iterator<?> it = viewlist.iterator();				
				while (it.hasNext()) {
					String name = String.class.cast(it.next());
					if (!viewFormatter.copyProperty(view, name, product)) {
						System.err.println("*** cannot migrate " + name + " property to the view layer...!");
					}					
				}
			}
			
			session.setAttribute("product", view);
			return mapping.findForward("View");
			
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw new ServletException("Data Access Error: " + t.getMessage());
		}		
		
	}
	
}
