package org.apache.jsp.WEB_002dINF.pages.include;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.s5tech.backend.IConstants;

public final class footer_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n<!-- begin footer section -->\t\r\n<logic:present name=\"");
      out.print( IConstants.USER_VIEW_KEY );
      out.write("\">\t\r\n\t\t</td>\r\n\t</tr>\t\r\n\t<tr valign=\"top\">\t\t\t\r\n\t\t<td style=\"padding-top: 0px; padding-left: 20px; padding-right: 20px; padding-bottom: 20px;\" id=\"bottomview\">\t\t\t\r\n\t\t\t&nbsp;\r\n\t\t</td>\r\n\t</tr>\t\r\n\t<tr valign=\"middle\">\t\t\t\r\n\t\t<td colspan=\"2\" class=\"statusbar\">\t\t\t\t\r\n\t\t\t<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td id=\"statusbar\">\t\t\t\t\t\t\r\n\t\t\t\t\t\t<logic:notEmpty name=\"statusbar\">\r\n\t\t\t\t\t\t\t<bean:write name=\"statusbar\" />\r\n\t\t\t\t\t\t</logic:notEmpty>\r\n\t\t\t\t\t\t<div style=\"display: none;\" id=\"statusbar.lastupdate\">\r\n\t\t\t\t\t\t\t<bean:message key=\"pricer.lastupdate\" />\t\t\t\r\n\t\t\t\t\t\t</div>\r\n\t\t\t\t\t</td>\t\t\t\t\t\r\n\t\t\t\t\t<td id=\"version\" style=\"width: 200px;\" align=\"right\">\t\r\n\t\t\t\t\t\tRunning Network Version 1.6.00\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td id=\"version.led\" style=\"width: 20px;\" align=\"center\">\t\r\n\t\t\t\t\t\t<img src=\"images/led-green.png\" class=\"trans led\"></img>\t\t\t\t\t\t\r\n\t\t\t\t\t</td>\t\t\t\t\t\r\n\t\t\t\t\t<td id=\"backend\" style=\"width: 100px;\" align=\"right\">\t\t\t\t\t\t\r\n\t\t\t\t\t\tBackend runnning\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td id=\"backend.led\" style=\"width: 20px;\" align=\"center\">\t\r\n");
      out.write("\t\t\t\t\t\t<img src=\"images/led-green.png\" class=\"trans led\"></img>\t\t\t\t\t\t\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t\t\r\n\t\t\t</table>\r\n\t\t</td>\r\n\t</tr>\t\r\n</table>\r\n</logic:present>\r\n\r\n<br>\r\n<br>\r\n\r\n<div id=\"footer\" align=\"center\">\r\n\t<div class=\"copyright\">\r\n\t\t<logic:present name=\"");
      out.print( IConstants.USER_VIEW_KEY );
      out.write("\">\r\n\t\t\t<bean:message key=\"footer.customeragreement\"/>\t\r\n\t\t\t<br>\r\n\t\t</logic:present>\t\t\t\t\r\n\t\t<bean:message key=\"footer.copyrigth\"/>\t\r\n\t\t<br>\r\n\t</div>\r\n\t<br>\r\n\t<div>\r\n\t\t<a href=\"https://twitter.com/S5Tech\" target=\"_blank\" style=\"color: white;\">\r\n\t\t\t<html:img srcKey=\"image.twitter.logo\" altKey=\"image.twitter.logo.alt\" styleClass=\"trans ico\"/>\r\n\t\t</a>\r\n\t\t<a href=\"https://www.linkedin.com/company/900485\" target=\"_blank\" style=\"color: white;\">\r\n\t\t\t<html:img srcKey=\"image.linkedin.logo\" altKey=\"image.linkedin.logo.alt\" styleClass=\"trans ico\"/>\r\n\t\t</a>\r\n\t\t<a href=\"https://angel.co/s5tech\" target=\"_blank\" style=\"color: white;\">\r\n\t\t\t<html:img srcKey=\"image.angels.logo\" altKey=\"image.angels.logo.alt\" styleClass=\"trans ico\"/>\r\n\t\t</a>\t\t\r\n\t</div>\r\n</div>\r\n\r\n</center>\r\n\r\n");
      out.write("\r\n\r\n<logic:present name=\"focus\">\r\n<script type=\"text/javascript\">\r\n<!--\r\nfor (var i = 0; i < document.forms.length; i ++) {\r\n\tif (document.forms[i].<bean:write name=\"focus\" />) {\t\t\r\n\t\tvar field = document.forms[i].<bean:write name=\"focus\" />;\r\n\t\tfield.style.background = '#ffcccc';\r\n\t\tfield.style.color = '#ee0000';\r\n\t\tif (field.type == 'text') {\r\n\t\t\tfield.setSelectionRange(0, field.value.length);\r\n\t\t}\r\n\t\tfield.focus();\t\t\t\t\t\r\n\t\tbreak;\r\n\t}\r\n}\r\n// -->\r\n</script>\r\n</logic:present>\r\n\r\n<logic:present name=\"view3d\">\r\n</div>\r\n<script type=\"text/javascript\">\r\n\t\r\n\t// Create an instance of Meny\r\n\tvar meny = Meny.create({\r\n\t\t// The element that will be animated in from off screen\r\n\t\tmenuElement: document.querySelector( '.meny' ),\r\n\r\n\t\t// The contents that gets pushed aside while Meny is active\r\n\t\tcontentsElement: document.querySelector( '.contents' ),\r\n\r\n\t\t// [optional] The alignment of the menu (top/right/bottom/left)\r\n\t\tposition: Meny.getQuery().p || 'left',\r\n\r\n\t\t// [optional] The height of the menu (when using top/bottom position)\r\n");
      out.write("\t\theight: 200,\r\n\r\n\t\t// [optional] The width of the menu (when using left/right position)\r\n\t\twidth: 260,\r\n\r\n\t\t// [optional] Distance from mouse (in pixels) when menu should open\r\n\t\tthreshold: 40,\r\n\r\n\t\t// [optional] Use mouse movement to automatically open/close\r\n\t\tmouse: true,\r\n\r\n\t\t// [optional] Use touch swipe events to open/close\r\n\t\ttouch: true\r\n\t});\r\n\r\n\t// API Methods:\r\n\t// meny.open();\r\n\t// meny.close();\r\n\t// meny.isOpen();\r\n\r\n\t// Events:\r\n\t// meny.addEventListener( 'open', function(){ console.log( 'open' ); } );\r\n\t// meny.addEventListener( 'close', function(){ console.log( 'close' ); } );\r\n\r\n\t// Embed an iframe if a URL is passed in\r\n\tif( Meny.getQuery().u && Meny.getQuery().u.match( /^http/gi ) ) {\r\n\t\tvar contents = document.querySelector( '.contents' );\r\n\t\tcontents.style.padding = '0px';\r\n\t\tcontents.innerHTML = '<div class=\"cover\"></div><iframe src=\"'+ Meny.getQuery().u +'\" style=\"width: 100%; height: 100%; border: 0; position: absolute;\"></iframe>';\r\n\t}\r\n\t\r\n</script>\r\n</logic:present>\r\n\r\n<!--end footer section -->\r\n");
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
