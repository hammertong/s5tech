package org.apache.jsp.WEB_002dINF.pages.include;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.struts.action.ActionForm;
import java.util.Enumeration;
import java.util.HashMap;
import java.lang.reflect.Field;

public final class json_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  private org.apache.jasper.runtime.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("application/json");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n\r\n\r\n\r\n\r\n");

	Enumeration e = request.getAttributeNames();
	while (e.hasMoreElements()) {		
		String name = e.nextElement().toString();
		Object o = request.getAttribute(name);
		if (o instanceof ActionForm) {
			out.write("{ ");
			out.write("\n\t\"formBean\": \"" + name + "\", "); 			
			for (Field field : o.getClass().getDeclaredFields()) {				
				out.write("\n\t");
				out.write("\"");
				out.write(field.getName());
				out.write("\": ");
				out.write("\"");
				field.setAccessible(true);
				Object x = field.get(o);
				out.write(
					(x == null ? "" : x.toString()
						    .replace('\t', ' ')
							.replace('\n', ' ')
							.replace('\r', ' ')
							.replace('"', '\'')
							.trim())
							);
				field.setAccessible(false);				
				out.write("\"");
			}
			out.write("\n}");
			break;
		}
	}	
	

      out.write('\r');
      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
