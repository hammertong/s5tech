package com.s5tech.backend.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.dao.GenericDao;
import com.s5tech.backend.servlet.helpers.OutputFormatter;
import com.s5tech.backend.util.RequestTools;

@SuppressWarnings("serial")
public class BackendServicesServlet extends HttpServlet {

	private static final Log log = LogFactory.getLog(HttpServlet.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String data = null;
		OutputFormatter output = new OutputFormatter(request);
		
		try {
			
			String query = request.getParameter("query");
			
			if (query != null) {
					
				List params = new ArrayList<>();				
				
				String [] requestParams = (request.getParameter("params") != null ? 
						request.getParameter("params").split(",") : new String[] {});
				
				if (requestParams != null) {					
					for (String param : requestParams) {
						if (param.startsWith("int:")) {
							params.add(Integer.parseInt(param.substring(4)));	
						}
						else if (param.startsWith("float:")) {
							params.add(Double.parseDouble(param.substring(4)));	
						}
						else {
							params.add(param);
						} 
					}					
				}
				
				if (request.getParameter("currentuser") != null) {
					params.add(RequestTools.formatSessionId(request));
				}
				
				GenericDao dao = GenericDao.class.cast(getServletContext()
						.getAttribute(IConstants.DATA_ACCESS_SERVICES_KEY));
				
				List<?> list = dao.executeNamedQuery (query, params.toArray());
				
				if (list == null) throw new Exception("no returned values!");
				
				String unique = request.getParameter("unique");
				
				if (unique != null && unique.length() > 0) {
					String getterName = "get" + unique.substring(0, 1).toUpperCase() + unique.substring(1);
					Method getter = null;
					ArrayList<Object> newlist =  new ArrayList<>();
					ArrayList<Object> uniquelist =  new ArrayList<>();
					for (int i = 0; i < list.size(); i ++) {
						Object o = list.get(i);						
						if (getter == null) {
							getter = o.getClass()
									.getMethod(getterName);
						}
						Object el = getter.invoke(o).toString();						
						if (!uniquelist.contains(el)) {
							uniquelist.add(el);
							newlist.add(o);
						}
					}
					list = newlist;
				}								
				
				data = output.format(list);				
				response.setStatus(HttpServletResponse.SC_OK);
				
			}
			else  {				
				
				data = output.format("error = 400");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			
		}
		catch (Throwable t) {			
			
			log.error("server error - ", t);				
			data = output.format("error = 500\ndescription = " + (t.getMessage() != null ? t.getMessage() : ""));
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			
		}
		
		try {
			
			if (data != null && data.length() > 0) {
				response.setContentLength(data.length());
				response.setContentType(output.getContentType());
	        	OutputStream out = response.getOutputStream();
				out.write(data.toString().getBytes());
				out.flush();
				out.close();
			}
		}
		catch (Throwable t) {
			log.error("flushing output buffer " + request.getRemoteHost(), t);
		}

	}

}
