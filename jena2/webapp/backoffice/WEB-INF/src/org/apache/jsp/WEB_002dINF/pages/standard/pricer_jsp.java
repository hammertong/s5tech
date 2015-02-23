package org.apache.jsp.WEB_002dINF.pages.standard;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Iterator;
import java.util.HashMap;
import java.lang.reflect.Field;
import com.s5tech.backend.IConstants;
import com.s5tech.backend.view.UserView;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionErrors;
import com.s5tech.backend.IConstants;
import com.s5tech.backend.view.UserView;
import com.s5tech.backend.IConstants;

public final class pricer_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(3);
    _jspx_dependants.add("/WEB-INF/pages/standard/../include/header.jsp");
    _jspx_dependants.add("/WEB-INF/pages/standard/../include/../menu/standard.jsp");
    _jspx_dependants.add("/WEB-INF/pages/standard/../include/footer.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_html;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_message_key_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_present_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_message_key_arg1_arg0_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_message_key_arg0_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_img_styleClass_srcKey_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_form_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_submit_style_property;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_submit_styleId_style_property;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notPresent_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_form_styleId_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_define_type_property_name_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_hidden_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_equal_value_property_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_write_property_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_text_style_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_messagesPresent;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_errors_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_radio_value_property_onclick_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_text_styleId_style_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notEqual_value_property_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notEmpty_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_write_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody;

  private org.apache.jasper.runtime.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_html_html = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_message_key_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_present_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_message_key_arg1_arg0_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_message_key_arg0_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_img_styleClass_srcKey_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_form_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_submit_style_property = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_submit_styleId_style_property = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notPresent_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_form_styleId_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_define_type_property_name_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_hidden_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_equal_value_property_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_write_property_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_text_style_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_messagesPresent = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_errors_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_radio_value_property_onclick_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_text_styleId_style_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notEqual_value_property_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notEmpty_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_write_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_html_html.release();
    _jspx_tagPool_bean_message_key_nobody.release();
    _jspx_tagPool_logic_present_name.release();
    _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody.release();
    _jspx_tagPool_bean_message_key_arg1_arg0_nobody.release();
    _jspx_tagPool_bean_message_key_arg0_nobody.release();
    _jspx_tagPool_html_link_href.release();
    _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.release();
    _jspx_tagPool_html_img_styleClass_srcKey_nobody.release();
    _jspx_tagPool_html_form_action.release();
    _jspx_tagPool_html_submit_style_property.release();
    _jspx_tagPool_html_submit_styleId_style_property.release();
    _jspx_tagPool_logic_notPresent_name.release();
    _jspx_tagPool_html_form_styleId_action.release();
    _jspx_tagPool_bean_define_type_property_name_id_nobody.release();
    _jspx_tagPool_html_hidden_property_nobody.release();
    _jspx_tagPool_logic_equal_value_property_name.release();
    _jspx_tagPool_bean_write_property_name_nobody.release();
    _jspx_tagPool_html_text_style_property_nobody.release();
    _jspx_tagPool_logic_messagesPresent.release();
    _jspx_tagPool_html_errors_nobody.release();
    _jspx_tagPool_html_radio_value_property_onclick_nobody.release();
    _jspx_tagPool_html_text_styleId_style_property_nobody.release();
    _jspx_tagPool_logic_notEqual_value_property_name.release();
    _jspx_tagPool_logic_notEmpty_name.release();
    _jspx_tagPool_bean_write_name_nobody.release();
    _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.release();
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

      out.write("\r\n\r\n\r\n<!DOCTYPE html>\r\n");
      //  html:html
      org.apache.struts.taglib.html.HtmlTag _jspx_th_html_html_0 = (org.apache.struts.taglib.html.HtmlTag) _jspx_tagPool_html_html.get(org.apache.struts.taglib.html.HtmlTag.class);
      _jspx_th_html_html_0.setPageContext(_jspx_page_context);
      _jspx_th_html_html_0.setParent(null);
      int _jspx_eval_html_html_0 = _jspx_th_html_html_0.doStartTag();
      if (_jspx_eval_html_html_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n<head>\r\n\r\n  <base href=\"");
          out.print( request.getContextPath() );
          out.write("/\">\r\n  <title>");
          if (_jspx_meth_bean_message_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</title>\r\n  <link rel=\"stylesheet\" href=\"stylesheets/login_style_ie.css\" type=\"text/css\">\r\n  <script type=\"text/javascript\" src=\"js/tbupdater.js\"></script>\r\n  <script type=\"text/javascript\">\r\n  <!--\r\n\tvar items = 10;\r\n\tvar hours = 24;\r\n\tvar timeout = 5000;\r\n\t\r\n\tvar urldata = 'backend?query=eslupdates-by-current-user&unique=esl&currentuser=true';\r\n\tvar urltemplate = 'xslt/eslupdating.jsp';\r\n\t\r\n\tfunction updateHours(value) {\r\n\t\thours = value;\r\n\t\tupdatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);\t\t\r\n\t}\r\n\t\r\n\tfunction updateItems(value) {\r\n\t\titems = value;\r\n\t\tupdatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);\r\n\t}\r\n\t\r\n\tfunction updateTimeout(value) {\r\n\t\ttimeout = value;\r\n\t\tupdatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);\r\n\t}\t\r\n\t\r\n\tfunction schedule()\r\n\t{\r\n\t\tupdatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);\r\n\t\tsetTimeout(\"schedule()\", timeout);\t\r\n\t}\r\n  // -->\t\r\n  </script>\r\n  \r\n</head>\r\n\r\n<body onload=\"schedule()\">\r\n");
          out.write("\r\n");
          out.write("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");

	UserView userView = null;	
	String statusbar = null;		

          out.write("\r\n\r\n<!-- begin header section -->\r\n");
          //  logic:present
          org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_0 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
          _jspx_th_logic_present_0.setPageContext(_jspx_page_context);
          _jspx_th_logic_present_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_present_0.setName( IConstants.USER_VIEW_KEY );
          int _jspx_eval_logic_present_0 = _jspx_th_logic_present_0.doStartTag();
          if (_jspx_eval_logic_present_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\t\r\n");

	userView = ((UserView)session.getAttribute(IConstants.USER_VIEW_KEY));
	
	//
	// questo mecanismo viene ripreso nel footer per gestire il focus su campo con errore
	//
	Object omap = request.getAttribute(Globals.ERROR_KEY);
	if (omap != null) {
		ActionMessages map = ActionMessages.class.cast(omap);
		if (map.size() > 0) {
			Field field = ActionMessages.class.getDeclaredField("messages");
			field.setAccessible(true);
			Iterator it = HashMap.class.cast(field.get(map)).keySet().iterator();
			while (it.hasNext()) {
				String focus = it.next().toString();
				//System.err.println("------> JSP errors key => " + focus);					
				if (focus.startsWith(ActionErrors.GLOBAL_MESSAGE)) continue;
				if (focus.length() == 0) continue;
				request.setAttribute("focus", focus);
				break;
			}
			field.setAccessible(false);
		}
	}
	
	statusbar = (request.getAttribute("statusbar") != null ? request.getAttribute("statusbar").toString() :  "");
	

              out.write("\r\n\r\n<script type=\"text/javascript\">\r\nfunction swapout(td)\r\n{\r\n\tif (td.className && td.className == 'hnavsel') return;\r\n\ttd.className = 'hnav'; \t\r\n}\r\nfunction swapover(td)\r\n{\r\n\tif (td.className && td.className == 'hnavsel') return;\r\n\ttd.className = 'hnavover'; \t\r\n}\r\nfunction selectmenu(el, url)\r\n{\t\t\r\n\tif (el.className == 'hnavsel') return;\r\n\tif (window.navigator.userAgent.indexOf(\"MSIE \") >= 0 \r\n\t\t\t|| window.navigator.userAgent.indexOf('.NET') > 0) {\t\t\r\n\t\twindow.location.href = '");
              out.print( request.getContextPath() );
              out.write("' + '/' + url;\r\n\t}\r\n\telse {\r\n\t\twindow.location.replace(url);\r\n\t}\r\n}\r\n</script>\r\n\r\n");
              int evalDoAfterBody = _jspx_th_logic_present_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_present_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_0);
            return;
          }
          _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_0);
          out.write("\r\n\r\n<center>\r\n\r\n<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\" width=\"100%\" class=\"rounded filled\">\r\n\r\n\t<tr valign=\"top\" class=\"header\" style=\"height: 50px\">\r\n\t\t<td align=\"left\" valign=\"middle\" style=\"width: 120px;\">\r\n\t\t\t");
          if (_jspx_meth_html_img_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\r\n\t\t</td>\t\r\n\t\t<td align=\"right\" valign=\"middle\">\r\n\t\t\t&nbsp;\r\n\t\t\t");
          //  logic:present
          org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_1 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
          _jspx_th_logic_present_1.setPageContext(_jspx_page_context);
          _jspx_th_logic_present_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_present_1.setName( IConstants.USER_VIEW_KEY );
          int _jspx_eval_logic_present_1 = _jspx_th_logic_present_1.doStartTag();
          if (_jspx_eval_logic_present_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\t\r\n\t\t\t<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td align=\"left\" style=\"color: #ccdfe1;\">\r\n\t\t\t\t\t\t");
              //  bean:message
              org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_1 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg1_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
              _jspx_th_bean_message_1.setPageContext(_jspx_page_context);
              _jspx_th_bean_message_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
              _jspx_th_bean_message_1.setKey("header.userloggedin");
              _jspx_th_bean_message_1.setArg0( userView.getFirstName() );
              _jspx_th_bean_message_1.setArg1("");
              int _jspx_eval_bean_message_1 = _jspx_th_bean_message_1.doStartTag();
              if (_jspx_th_bean_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_tagPool_bean_message_key_arg1_arg0_nobody.reuse(_jspx_th_bean_message_1);
                return;
              }
              _jspx_tagPool_bean_message_key_arg1_arg0_nobody.reuse(_jspx_th_bean_message_1);
              out.write("\r\n\t\t\t\t\t\t<br>\r\n\t\t\t\t\t\t");
              //  bean:message
              org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_2 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
              _jspx_th_bean_message_2.setPageContext(_jspx_page_context);
              _jspx_th_bean_message_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
              _jspx_th_bean_message_2.setKey("header.userrole");
              _jspx_th_bean_message_2.setArg0( userView.getRole() );
              int _jspx_eval_bean_message_2 = _jspx_th_bean_message_2.doStartTag();
              if (_jspx_th_bean_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_2);
                return;
              }
              _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_2);
              out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td align=\"center\" style=\"width: 50px;\"> \r\n\t\t\t\t\t\t");
              if (_jspx_meth_html_link_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t<br>\r\n\t\t\t\t\t\t<span style=\"font-size: 10px; opacity: 0.6;\">\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t</span>\r\n\t\t\t\t\t</td>      \r\n\t\t\t\t\t<td align=\"center\" style=\"width: 50px;\"> \r\n\t\t\t\t\t\t");
              if (_jspx_meth_html_link_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t<br>\r\n\t\t\t\t\t\t<span style=\"font-size: 10px; opacity: 0.6;\">\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t</span>\r\n\t\t\t\t\t</td>   \r\n\t\t\t\t</tr>\r\n\t\t\t</table>\r\n\t\t\t");
              int evalDoAfterBody = _jspx_th_logic_present_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_present_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_1);
            return;
          }
          _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_1);
          out.write("\t\t\t\t\r\n\t\t</td>\r\n\t</tr>  \r\n\t\r\n\t");
          //  logic:present
          org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_2 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
          _jspx_th_logic_present_2.setPageContext(_jspx_page_context);
          _jspx_th_logic_present_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_present_2.setName( IConstants.USER_VIEW_KEY );
          int _jspx_eval_logic_present_2 = _jspx_th_logic_present_2.doStartTag();
          if (_jspx_eval_logic_present_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\t\r\n\t\r\n\t<tr class=\"hnavbar\" style=\"padding: 0px; margin: 0px;\">\r\n\t\t<td colspan=\"2\" style=\"padding: 0px; margin: 0px;\"> \r\n\t\t\t<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"padding: 0px; margin: 0px;\">\r\n\t\t\t\t<tr valign=\"middle\" class=\"hnavbar\" style=\"padding: 0px; margin: 0px;\">\t\t\t\t\t\t\t\t\r\n\t\t\t\t\t<td onclick=\"selectmenu(this, 'action/home')\" \r\n\t\t\t\t\t\tonmouseover=\"swapover(this)\" \r\n\t\t\t\t\t\tonmouseout=\"swapout(this)\" \t\t\t\t\t\t\r\n\t\t\t\t\t\tclass=\"");
              out.print( (userView.getCurrentContext().length() == 0 ? "hnavsel" : "hnav") );
              out.write("\"  \t\t\t\t\t\t\r\n\t\t\t\t\t\tstyle=\"width: 210px;\">\r\n\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_2, _jspx_page_context))
                return;
              out.write("\t\t\r\n\t\t\t\t\t</td>\t\t\t\t\t\t\t\t\t\t\r\n\t\t\t\t\t<td class=\"hnavnotab\" style=\"padding: 0px; margin: 0px;\">\r\n\t\t\t\t\t\t&nbsp;\r\n\t\t\t\t\t</td>\t\t\t\t\t\r\n\t\t\t\t\t<td align=\"right\" class=\"hnavnotab\" style=\"width: 180px; padding: 0px; margin: 0px; padding-right: 10px;\">\t\t\r\n\t\t\t\t\t\t<a href=\"action/home?locale=it_IT\">");
              if (_jspx_meth_html_img_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_2, _jspx_page_context))
                return;
              out.write("</a>&nbsp;\r\n\t\t\t\t\t\t<a href=\"action/home?locale=en_US\">");
              if (_jspx_meth_html_img_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_2, _jspx_page_context))
                return;
              out.write("</a>&nbsp;\r\n\t\t\t\t\t\t<a href=\"action/home?locale=fr_FR\">");
              if (_jspx_meth_html_img_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_2, _jspx_page_context))
                return;
              out.write("</a>&nbsp;\t\t\t\t\t\t\r\n\t\t\t\t\t</td>\t \t\t\t\t\t\t\t\t\t\r\n\t\t\t\t</tr>\r\n\t\t\t</table>\r\n\t\t</td>\r\n\t</tr>\r\n\t\r\n\t<tr valign=\"top\">\r\n\t\t<td style=\"padding-top: 10px; width: 200px;\" rowspan=\"2\">\t\t\t\r\n\t\t\t");
              out.write("\r\n\r\n\r\n\r\n\r\n\r\n<!-- begin header section -->\r\n");
              //  logic:present
              org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_3 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
              _jspx_th_logic_present_3.setPageContext(_jspx_page_context);
              _jspx_th_logic_present_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_2);
              _jspx_th_logic_present_3.setName( IConstants.USER_VIEW_KEY );
              int _jspx_eval_logic_present_3 = _jspx_th_logic_present_3.doStartTag();
              if (_jspx_eval_logic_present_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\t\r\n\r\n");

	UserView menuView = ((UserView)session.getAttribute(IConstants.USER_VIEW_KEY));

                  out.write("\t\r\n\t\r\n");
                  //  html:form
                  org.apache.struts.taglib.html.FormTag _jspx_th_html_form_0 = (org.apache.struts.taglib.html.FormTag) _jspx_tagPool_html_form_action.get(org.apache.struts.taglib.html.FormTag.class);
                  _jspx_th_html_form_0.setPageContext(_jspx_page_context);
                  _jspx_th_html_form_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_3);
                  _jspx_th_html_form_0.setAction("menu");
                  int _jspx_eval_html_form_0 = _jspx_th_html_form_0.doStartTag();
                  if (_jspx_eval_html_form_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\r\n\r\n<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\" width=\"200px\">\r\n\t\r\n\t");
 if (menuView.isInRole("priceChangeFunction")) { 
                      out.write("\r\n\t<tr>\r\n  \t\t<td align=\"center\" valign=\"middle\">\r\n\t\t\t");
                      if (_jspx_meth_html_submit_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_0, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t</td>\r\n\t</tr>\t\t\r\n\t");
 } 
                      out.write("\r\n\t\r\n\t");
 if (menuView.isAdministrator()) { 
                      out.write("\t\r\n\t<tr>\r\n  \t\t<td align=\"center\" valign=\"middle\">\r\n\t\t\t");
                      if (_jspx_meth_html_submit_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_0, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t</td>\r\n\t</tr>\r\n\t");
 } 
                      out.write("\r\n\r\n\t");
 if (menuView.isInRole("reportPriceChange")) { 
                      out.write("\t\r\n\t<tr>\r\n  \t\t<td align=\"center\" valign=\"middle\">\r\n\t\t\t");
                      if (_jspx_meth_html_submit_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_0, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t</td>\r\n\t</tr>\r\n\t");
 } 
                      out.write("\r\n\t\r\n\t<tr>\r\n  \t\t<td align=\"center\" valign=\"middle\">\r\n\t\t\t");
                      if (_jspx_meth_html_submit_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_0, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t</td>\r\n\t</tr>\t\r\n\t\r\n\t<tr>\r\n  \t\t<td align=\"center\" valign=\"middle\">\r\n\t\t\t");
                      if (_jspx_meth_html_submit_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_0, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t</td>\r\n\t</tr>\r\n\t<tr>\r\n  \t\t<td align=\"center\" valign=\"middle\">\r\n\t\t\t");
                      if (_jspx_meth_html_submit_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_0, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t</td>\r\n\t</tr>\t\r\n\t<tr>\r\n  \t\t<td align=\"center\" valign=\"middle\">\r\n\t\t\t");
                      if (_jspx_meth_html_submit_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_0, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t</td>\r\n\t</tr>\t\r\n\t<tr>\r\n\t\t<td align=\"center\" valign=\"middle\">&nbsp;</td>\r\n\t</tr>    \r\n</table>\r\n\r\n");
                      int evalDoAfterBody = _jspx_th_html_form_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_html_form_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _jspx_tagPool_html_form_action.reuse(_jspx_th_html_form_0);
                    return;
                  }
                  _jspx_tagPool_html_form_action.reuse(_jspx_th_html_form_0);
                  out.write("\r\n\r\n");
                  int evalDoAfterBody = _jspx_th_logic_present_3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_logic_present_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_3);
                return;
              }
              _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_3);
              out.write('\r');
              out.write('\n');
              out.write("\t\t\t\r\n\t\t</td>\t\r\n\t\t<td style=\"padding-left: 20px; padding-right: 20px; padding-top: 10px; padding-bottom: 10px;\">\t\r\n\t\t\r\n\t");
              int evalDoAfterBody = _jspx_th_logic_present_2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_present_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_2);
            return;
          }
          _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_2);
          out.write("\t\r\n\r\n\t");
          //  logic:notPresent
          org.apache.struts.taglib.logic.NotPresentTag _jspx_th_logic_notPresent_0 = (org.apache.struts.taglib.logic.NotPresentTag) _jspx_tagPool_logic_notPresent_name.get(org.apache.struts.taglib.logic.NotPresentTag.class);
          _jspx_th_logic_notPresent_0.setPageContext(_jspx_page_context);
          _jspx_th_logic_notPresent_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_notPresent_0.setName( IConstants.USER_VIEW_KEY );
          int _jspx_eval_logic_notPresent_0 = _jspx_th_logic_notPresent_0.doStartTag();
          if (_jspx_eval_logic_notPresent_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n\t</table>\t \r\n\t");
              int evalDoAfterBody = _jspx_th_logic_notPresent_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_notPresent_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_logic_notPresent_name.reuse(_jspx_th_logic_notPresent_0);
            return;
          }
          _jspx_tagPool_logic_notPresent_name.reuse(_jspx_th_logic_notPresent_0);
          out.write("\t\r\n\r\n<!-- end header section -->\r\n\r\n");
          out.write("\r\n\t\r\n");
          //  html:form
          org.apache.struts.taglib.html.FormTag _jspx_th_html_form_1 = (org.apache.struts.taglib.html.FormTag) _jspx_tagPool_html_form_styleId_action.get(org.apache.struts.taglib.html.FormTag.class);
          _jspx_th_html_form_1.setPageContext(_jspx_page_context);
          _jspx_th_html_form_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_html_form_1.setAction("pricer");
          _jspx_th_html_form_1.setStyleId("pricerForm");
          int _jspx_eval_html_form_1 = _jspx_th_html_form_1.doStartTag();
          if (_jspx_eval_html_form_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\r');
              out.write('\n');
              //  bean:define
              org.apache.struts.taglib.bean.DefineTag _jspx_th_bean_define_0 = (org.apache.struts.taglib.bean.DefineTag) _jspx_tagPool_bean_define_type_property_name_id_nobody.get(org.apache.struts.taglib.bean.DefineTag.class);
              _jspx_th_bean_define_0.setPageContext(_jspx_page_context);
              _jspx_th_bean_define_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
              _jspx_th_bean_define_0.setName("pricerForm");
              _jspx_th_bean_define_0.setProperty("step");
              _jspx_th_bean_define_0.setId("step");
              _jspx_th_bean_define_0.setType("Integer");
              int _jspx_eval_bean_define_0 = _jspx_th_bean_define_0.doStartTag();
              if (_jspx_th_bean_define_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_tagPool_bean_define_type_property_name_id_nobody.reuse(_jspx_th_bean_define_0);
                return;
              }
              _jspx_tagPool_bean_define_type_property_name_id_nobody.reuse(_jspx_th_bean_define_0);
              Integer step = null;
              step = (Integer) _jspx_page_context.findAttribute("step");
              out.write("  \r\n\r\n");
              if (_jspx_meth_html_hidden_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write('\r');
              out.write('\n');
              if (_jspx_meth_html_hidden_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\r\n<h1>");
              //  bean:message
              org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_13 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg1_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
              _jspx_th_bean_message_13.setPageContext(_jspx_page_context);
              _jspx_th_bean_message_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
              _jspx_th_bean_message_13.setKey("pricer.head");
              _jspx_th_bean_message_13.setArg0( step.toString() );
              _jspx_th_bean_message_13.setArg1("4");
              int _jspx_eval_bean_message_13 = _jspx_th_bean_message_13.doStartTag();
              if (_jspx_th_bean_message_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_tagPool_bean_message_key_arg1_arg0_nobody.reuse(_jspx_th_bean_message_13);
                return;
              }
              _jspx_tagPool_bean_message_key_arg1_arg0_nobody.reuse(_jspx_th_bean_message_13);
              out.write("</h1>\r\n\r\n<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width: 400px; padding-bottom: 30px;\">\r\n\t\r\n\t<tr style=\"height: 30px;\">\r\n\t\t<td colspan=\"2\">\r\n\t\t\t");
              if (_jspx_meth_logic_equal_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t");
              if (_jspx_meth_logic_equal_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\t\t\t");
              if (_jspx_meth_logic_equal_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\t\t\t");
              if (_jspx_meth_logic_equal_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\t\t</td>\r\n\t</tr>\r\n\t\r\n\t<tr valign=\"top\" id=\"row_step1\" style=\"display: none; height: 70px;\">\r\n  \t\t<td style=\"width: 200px;\">\r\n\t\t\t");
              if (_jspx_meth_bean_message_18((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\t\t\t\r\n\t\t</td>\r\n\t\t<td style=\"width: 200px;\">\r\n\t\t\t");
              if (_jspx_meth_html_text_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t");
              if (_jspx_meth_logic_messagesPresent_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\t\t</td>\r\n\t</tr>\r\n\t\r\n\t<tr valign=\"top\" id=\"row_step2\" style=\"display: none; height: 70px;\">\r\n  \t\t<td style=\"width: 200px;\">\t\t\t\r\n\t\t\t");
              if (_jspx_meth_bean_message_19((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\t\t\r\n\t\t</td>\r\n\t\t<td style=\"width: 200px;\">\r\n\t\t\t");
              if (_jspx_meth_html_text_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t");
              if (_jspx_meth_logic_messagesPresent_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\t\t</td>\r\n\t</tr>\r\n\t\t\t\r\n\t<tr valign=\"top\" id=\"row_step3\" style=\"display: none; height: 70px;\">\r\n  \t\t<td colspan=\"2\">\r\n\t\t\t<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\t\t\t\t\r\n\t\t\t\t<tr valign=\"top\" style=\"height: 35px;\">\r\n\t\t\t\t\t<td valign=\"top\" style=\"width: 200px;\">\r\n\t\t\t\t\t\t<label>\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_html_radio_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write(" \r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_20((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t</label>\r\n\t\t\t\t\t</td>\t\t\t\t\t\t\r\n\t\t\t\t\t<td valign=\"top\" style=\"width: 200px;\">\r\n\t\t\t\t\t\t<div id=\"discount\" style=\"display: none\">\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_html_text_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write(" \r\n\t\t\t\t\t\t\t<span style=\"font-size: 10px;\">");
              if (_jspx_meth_bean_message_21((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("</span>\r\n\t\t\t\t\t\t</div>\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td valign=\"top\" style=\"width: 200px;\">\r\n\t\t\t\t\t\t<label>\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_html_radio_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write(" \r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_22((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t</label>\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t\t<tr valign=\"top\" style=\"height: 35px;\">\r\n\t\t\t\t\t<td valign=\"top\" style=\"width: 200px;\">\r\n\t\t\t\t\t\t<label>\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_html_radio_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write(" \t\t\t\t\t\t\t\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_23((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t</label>\r\n\t\t\t\t\t</td>\t\t\t\t\t\t\r\n\t\t\t\t\t<td valign=\"top\" style=\"width: 200px;\">\r\n\t\t\t\t\t\t<div id=\"points\" style=\"display: none\">\t\t\t\t\t\t\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_html_text_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write(" \r\n\t\t\t\t\t\t\t<span style=\"font-size: 10px;\">");
              if (_jspx_meth_bean_message_24((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("</span>\r\n\t\t\t\t\t\t</div>\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td valign=\"top\" style=\"width: 200px;\">\r\n\t\t\t\t\t\t<label>\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_html_radio_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write(" \r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_25((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t</label>\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\t\t\t\t\r\n\t\t\t</table>\r\n\t\t\t");
              if (_jspx_meth_logic_messagesPresent_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\t\t</td>\r\n\t</tr>\r\n\t\r\n\t<tr id=\"row_step4\" style=\"height: 70px; display: none;\">\r\n\t\t<td colspan=\"2\">\r\n\t\t\t");
              if (_jspx_meth_logic_present_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t<br>\t\t\t\r\n\t\t</td>\r\n\t</tr>\r\n\t\r\n\t<tr style=\"height: 30px;\">\r\n  \t\t<td colspan=\"2\" align=\"left\">\r\n\t\t");
              if (_jspx_meth_logic_equal_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\t\t");
              //  logic:notEqual
              org.apache.struts.taglib.logic.NotEqualTag _jspx_th_logic_notEqual_0 = (org.apache.struts.taglib.logic.NotEqualTag) _jspx_tagPool_logic_notEqual_value_property_name.get(org.apache.struts.taglib.logic.NotEqualTag.class);
              _jspx_th_logic_notEqual_0.setPageContext(_jspx_page_context);
              _jspx_th_logic_notEqual_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
              _jspx_th_logic_notEqual_0.setName("pricerForm");
              _jspx_th_logic_notEqual_0.setProperty("step");
              _jspx_th_logic_notEqual_0.setValue("4");
              int _jspx_eval_logic_notEqual_0 = _jspx_th_logic_notEqual_0.doStartTag();
              if (_jspx_eval_logic_notEqual_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n\t\t\t");
                  //  html:submit
                  org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_8 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_styleId_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
                  _jspx_th_html_submit_8.setPageContext(_jspx_page_context);
                  _jspx_th_html_submit_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_notEqual_0);
                  _jspx_th_html_submit_8.setProperty("method");
                  _jspx_th_html_submit_8.setStyleId("next");
                  _jspx_th_html_submit_8.setStyle( (step.intValue() > 1 ? "width: 120px; position: relative; left: 140px;" : "width: 120px;") );
                  int _jspx_eval_html_submit_8 = _jspx_th_html_submit_8.doStartTag();
                  if (_jspx_eval_html_submit_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_html_submit_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_html_submit_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_html_submit_8.doInitBody();
                    }
                    do {
                      out.write("      \r\n\t\t\t\t");
                      if (_jspx_meth_bean_message_36((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_8, _jspx_page_context))
                        return;
                      out.write("\r\n\t\t\t");
                      int evalDoAfterBody = _jspx_th_html_submit_8.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_html_submit_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_html_submit_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _jspx_tagPool_html_submit_styleId_style_property.reuse(_jspx_th_html_submit_8);
                    return;
                  }
                  _jspx_tagPool_html_submit_styleId_style_property.reuse(_jspx_th_html_submit_8);
                  out.write("\t\t\t\t\t\t\r\n\t\t");
                  int evalDoAfterBody = _jspx_th_logic_notEqual_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_logic_notEqual_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_tagPool_logic_notEqual_value_property_name.reuse(_jspx_th_logic_notEqual_0);
                return;
              }
              _jspx_tagPool_logic_notEqual_value_property_name.reuse(_jspx_th_logic_notEqual_0);
              out.write("\r\n\t\t");
              if (_jspx_meth_logic_notEqual_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\t\t\r\n\t\t</td>\t\t\r\n\t</tr>\r\n\t\r\n</table>\r\n\r\n<script type=\"text/javascript\">\r\n<!--\r\n\r\n\tdocument.getElementById('row_step");
              out.print( step.intValue() );
              out.write("').style.display = 'block';\r\n\t\r\n");
              if (_jspx_meth_logic_equal_9((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\r\n\r\n");
              if (_jspx_meth_logic_equal_10((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\r\n");
              if (_jspx_meth_logic_equal_11((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\r\n");
              if (_jspx_meth_logic_equal_12((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_form_1, _jspx_page_context))
                return;
              out.write("\t\r\n\r\n// -->\r\n</script>\r\n\r\n");
              int evalDoAfterBody = _jspx_th_html_form_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_html_form_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_html_form_styleId_action.reuse(_jspx_th_html_form_1);
            return;
          }
          _jspx_tagPool_html_form_styleId_action.reuse(_jspx_th_html_form_1);
          out.write("\r\n\r\n<hr>\r\n<h3>");
          if (_jspx_meth_bean_message_38((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</h3>\r\n<div style=\"cursor: default; font-size: 10px;\">\r\n\t<!-- ");
          if (_jspx_meth_bean_message_39((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write(" -->\r\n\t<select onchange=\"updateHours(this.value)\">\r\n\t\t<option value=\"1\">");
          if (_jspx_meth_bean_message_40((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\r\n\t\t<option value=\"6\">");
          if (_jspx_meth_bean_message_41((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\r\n\t\t<option value=\"24\" selected=\"selected\">");
          if (_jspx_meth_bean_message_42((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\r\n\t\t<option value=\"168\">");
          if (_jspx_meth_bean_message_43((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\r\n\t</select>\t\t\r\n\t<!-- ");
          if (_jspx_meth_bean_message_44((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\t-->\r\n\t<select onchange=\"updateItems(this.value)\">\r\n\t\t<option value=\"1\">1</option>\r\n\t\t<option value=\"5\">5</option>\r\n\t\t<option value=\"10\" selected=\"selected\">10</option>\r\n\t\t<option value=\"20\">20</option>\r\n\t\t<option value=\"1000\">");
          if (_jspx_meth_bean_message_45((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\t\t\t\r\n\t</select>\r\n\t<!-- ");
          if (_jspx_meth_bean_message_46((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write(" -->\r\n\t<select onchange=\"updateTimeout(this.value)\">\r\n\t\t<option value=\"1000\">");
          if (_jspx_meth_bean_message_47((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\r\n\t\t<option value=\"5000\" selected=\"selected\">");
          if (_jspx_meth_bean_message_48((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\r\n\t\t<option value=\"10000\">");
          if (_jspx_meth_bean_message_49((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\t\r\n\t\t<option value=\"30000\">");
          if (_jspx_meth_bean_message_50((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("</option>\t\t\r\n\t</select>\r\n</div>\r\n\r\n");
          out.write("\r\n<!-- begin footer section -->\t\r\n");
          //  logic:present
          org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_5 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
          _jspx_th_logic_present_5.setPageContext(_jspx_page_context);
          _jspx_th_logic_present_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_present_5.setName( IConstants.USER_VIEW_KEY );
          int _jspx_eval_logic_present_5 = _jspx_th_logic_present_5.doStartTag();
          if (_jspx_eval_logic_present_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\t\r\n\t\t</td>\r\n\t</tr>\t\r\n\t<tr valign=\"top\">\t\t\t\r\n\t\t<td style=\"padding-top: 0px; padding-left: 20px; padding-right: 20px; padding-bottom: 20px;\" id=\"bottomview\">\t\t\t\r\n\t\t\t&nbsp;\r\n\t\t</td>\r\n\t</tr>\t\r\n\t<tr valign=\"middle\">\t\t\t\r\n\t\t<td colspan=\"2\" class=\"statusbar\">\t\t\t\t\r\n\t\t\t<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td id=\"statusbar\">\t\t\t\t\t\t\r\n\t\t\t\t\t\t");
              if (_jspx_meth_logic_notEmpty_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_5, _jspx_page_context))
                return;
              out.write("\r\n\t\t\t\t\t\t<div style=\"display: none;\" id=\"statusbar.lastupdate\">\r\n\t\t\t\t\t\t\t");
              if (_jspx_meth_bean_message_51((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_5, _jspx_page_context))
                return;
              out.write("\t\t\t\r\n\t\t\t\t\t\t</div>\r\n\t\t\t\t\t</td>\t\t\t\t\t\r\n\t\t\t\t\t<td id=\"version\" style=\"width: 200px;\" align=\"right\">\t\r\n\t\t\t\t\t\tRunning Network Version 1.6.00\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td id=\"version.led\" style=\"width: 20px;\" align=\"center\">\t\r\n\t\t\t\t\t\t<img src=\"images/led-green.png\" class=\"trans led\"></img>\t\t\t\t\t\t\r\n\t\t\t\t\t</td>\t\t\t\t\t\r\n\t\t\t\t\t<td id=\"backend\" style=\"width: 100px;\" align=\"right\">\t\t\t\t\t\t\r\n\t\t\t\t\t\tBackend runnning\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td id=\"backend.led\" style=\"width: 20px;\" align=\"center\">\t\r\n\t\t\t\t\t\t<img src=\"images/led-green.png\" class=\"trans led\"></img>\t\t\t\t\t\t\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t\t\r\n\t\t\t</table>\r\n\t\t</td>\r\n\t</tr>\t\r\n</table>\r\n");
              int evalDoAfterBody = _jspx_th_logic_present_5.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_present_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_5);
            return;
          }
          _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_5);
          out.write("\r\n\r\n<br>\r\n<br>\r\n\r\n<div id=\"footer\" align=\"center\">\r\n\t<div class=\"copyright\">\r\n\t\t");
          //  logic:present
          org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_6 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
          _jspx_th_logic_present_6.setPageContext(_jspx_page_context);
          _jspx_th_logic_present_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_present_6.setName( IConstants.USER_VIEW_KEY );
          int _jspx_eval_logic_present_6 = _jspx_th_logic_present_6.doStartTag();
          if (_jspx_eval_logic_present_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n\t\t\t");
              if (_jspx_meth_bean_message_52((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_6, _jspx_page_context))
                return;
              out.write("\t\r\n\t\t\t<br>\r\n\t\t");
              int evalDoAfterBody = _jspx_th_logic_present_6.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_present_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_6);
            return;
          }
          _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_6);
          out.write("\t\t\t\t\r\n\t\t");
          if (_jspx_meth_bean_message_53((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\t\r\n\t\t<br>\r\n\t</div>\r\n\t<br>\r\n\t<div>\r\n\t\t<a href=\"https://twitter.com/S5Tech\" target=\"_blank\" style=\"color: white;\">\r\n\t\t\t");
          if (_jspx_meth_html_img_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\r\n\t\t</a>\r\n\t\t<a href=\"https://www.linkedin.com/company/900485\" target=\"_blank\" style=\"color: white;\">\r\n\t\t\t");
          if (_jspx_meth_html_img_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\r\n\t\t</a>\r\n\t\t<a href=\"https://angel.co/s5tech\" target=\"_blank\" style=\"color: white;\">\r\n\t\t\t");
          if (_jspx_meth_html_img_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\r\n\t\t</a>\t\t\r\n\t</div>\r\n</div>\r\n\r\n</center>\r\n\r\n");
          out.write("\r\n\r\n");
          if (_jspx_meth_logic_present_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\r\n\r\n");
          if (_jspx_meth_logic_present_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\r\n\r\n<!--end footer section -->\r\n");
          out.write("\r\n\r\n</body>\r\n\r\n");
          int evalDoAfterBody = _jspx_th_html_html_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_html_html_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _jspx_tagPool_html_html.reuse(_jspx_th_html_html_0);
        return;
      }
      _jspx_tagPool_html_html.reuse(_jspx_th_html_html_0);
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

  private boolean _jspx_meth_bean_message_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_0 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_0.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_0.setKey("pricer.title");
    int _jspx_eval_bean_message_0 = _jspx_th_bean_message_0.doStartTag();
    if (_jspx_th_bean_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_0);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_0);
    return false;
  }

  private boolean _jspx_meth_html_img_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_0 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_0.setPageContext(_jspx_page_context);
    _jspx_th_html_img_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_html_img_0.setSrcKey("image.logo");
    _jspx_th_html_img_0.setHeight("46px");
    _jspx_th_html_img_0.setAltKey("image.logo.alt");
    _jspx_th_html_img_0.setBorder("0");
    int _jspx_eval_html_img_0 = _jspx_th_html_img_0.doStartTag();
    if (_jspx_th_html_img_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody.reuse(_jspx_th_html_img_0);
      return true;
    }
    _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody.reuse(_jspx_th_html_img_0);
    return false;
  }

  private boolean _jspx_meth_html_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:link
    org.apache.struts.taglib.html.LinkTag _jspx_th_html_link_0 = (org.apache.struts.taglib.html.LinkTag) _jspx_tagPool_html_link_href.get(org.apache.struts.taglib.html.LinkTag.class);
    _jspx_th_html_link_0.setPageContext(_jspx_page_context);
    _jspx_th_html_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
    _jspx_th_html_link_0.setHref("action/home");
    int _jspx_eval_html_link_0 = _jspx_th_html_link_0.doStartTag();
    if (_jspx_eval_html_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_link_0.doInitBody();
      }
      do {
        out.write("\r\n\t\t\t\t\t\t\t");
        if (_jspx_meth_html_img_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_link_0, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_html_link_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_link_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_link_href.reuse(_jspx_th_html_link_0);
      return true;
    }
    _jspx_tagPool_html_link_href.reuse(_jspx_th_html_link_0);
    return false;
  }

  private boolean _jspx_meth_html_img_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_link_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_1 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_1.setPageContext(_jspx_page_context);
    _jspx_th_html_img_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_link_0);
    _jspx_th_html_img_1.setSrcKey("image.home");
    _jspx_th_html_img_1.setWidth("46px");
    _jspx_th_html_img_1.setHeight("46px");
    _jspx_th_html_img_1.setAltKey("image.home.alt");
    _jspx_th_html_img_1.setStyleClass("trans ico");
    _jspx_th_html_img_1.setBorder("0");
    int _jspx_eval_html_img_1 = _jspx_th_html_img_1.doStartTag();
    if (_jspx_th_html_img_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.reuse(_jspx_th_html_img_1);
      return true;
    }
    _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.reuse(_jspx_th_html_img_1);
    return false;
  }

  private boolean _jspx_meth_bean_message_3(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_3 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_3.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
    _jspx_th_bean_message_3.setKey("global.home");
    int _jspx_eval_bean_message_3 = _jspx_th_bean_message_3.doStartTag();
    if (_jspx_th_bean_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_3);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_3);
    return false;
  }

  private boolean _jspx_meth_html_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:link
    org.apache.struts.taglib.html.LinkTag _jspx_th_html_link_1 = (org.apache.struts.taglib.html.LinkTag) _jspx_tagPool_html_link_href.get(org.apache.struts.taglib.html.LinkTag.class);
    _jspx_th_html_link_1.setPageContext(_jspx_page_context);
    _jspx_th_html_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
    _jspx_th_html_link_1.setHref("action/logout");
    int _jspx_eval_html_link_1 = _jspx_th_html_link_1.doStartTag();
    if (_jspx_eval_html_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_link_1.doInitBody();
      }
      do {
        out.write("\r\n\t\t\t\t\t\t\t");
        if (_jspx_meth_html_img_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_link_1, _jspx_page_context))
          return true;
        out.write("\t\t\r\n\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_html_link_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_link_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_link_href.reuse(_jspx_th_html_link_1);
      return true;
    }
    _jspx_tagPool_html_link_href.reuse(_jspx_th_html_link_1);
    return false;
  }

  private boolean _jspx_meth_html_img_2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_link_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_2 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_2.setPageContext(_jspx_page_context);
    _jspx_th_html_img_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_link_1);
    _jspx_th_html_img_2.setSrcKey("image.logout");
    _jspx_th_html_img_2.setWidth("46px");
    _jspx_th_html_img_2.setHeight("46px");
    _jspx_th_html_img_2.setAltKey("image.logout.alt");
    _jspx_th_html_img_2.setStyleClass("trans ico");
    _jspx_th_html_img_2.setBorder("0");
    int _jspx_eval_html_img_2 = _jspx_th_html_img_2.doStartTag();
    if (_jspx_th_html_img_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.reuse(_jspx_th_html_img_2);
      return true;
    }
    _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.reuse(_jspx_th_html_img_2);
    return false;
  }

  private boolean _jspx_meth_bean_message_4(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_4 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_4.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
    _jspx_th_bean_message_4.setKey("global.logout");
    int _jspx_eval_bean_message_4 = _jspx_th_bean_message_4.doStartTag();
    if (_jspx_th_bean_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_4);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_4);
    return false;
  }

  private boolean _jspx_meth_bean_message_5(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_5 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_5.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_2);
    _jspx_th_bean_message_5.setKey("hnav.standard");
    int _jspx_eval_bean_message_5 = _jspx_th_bean_message_5.doStartTag();
    if (_jspx_th_bean_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
    return false;
  }

  private boolean _jspx_meth_html_img_3(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_3 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_styleClass_srcKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_3.setPageContext(_jspx_page_context);
    _jspx_th_html_img_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_2);
    _jspx_th_html_img_3.setSrcKey("image.lang.it");
    _jspx_th_html_img_3.setStyleClass("trans flag");
    int _jspx_eval_html_img_3 = _jspx_th_html_img_3.doStartTag();
    if (_jspx_th_html_img_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_styleClass_srcKey_nobody.reuse(_jspx_th_html_img_3);
      return true;
    }
    _jspx_tagPool_html_img_styleClass_srcKey_nobody.reuse(_jspx_th_html_img_3);
    return false;
  }

  private boolean _jspx_meth_html_img_4(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_4 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_styleClass_srcKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_4.setPageContext(_jspx_page_context);
    _jspx_th_html_img_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_2);
    _jspx_th_html_img_4.setSrcKey("image.lang.en");
    _jspx_th_html_img_4.setStyleClass("trans flag");
    int _jspx_eval_html_img_4 = _jspx_th_html_img_4.doStartTag();
    if (_jspx_th_html_img_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_styleClass_srcKey_nobody.reuse(_jspx_th_html_img_4);
      return true;
    }
    _jspx_tagPool_html_img_styleClass_srcKey_nobody.reuse(_jspx_th_html_img_4);
    return false;
  }

  private boolean _jspx_meth_html_img_5(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_5 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_styleClass_srcKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_5.setPageContext(_jspx_page_context);
    _jspx_th_html_img_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_2);
    _jspx_th_html_img_5.setSrcKey("image.lang.fr");
    _jspx_th_html_img_5.setStyleClass("trans flag");
    int _jspx_eval_html_img_5 = _jspx_th_html_img_5.doStartTag();
    if (_jspx_th_html_img_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_styleClass_srcKey_nobody.reuse(_jspx_th_html_img_5);
      return true;
    }
    _jspx_tagPool_html_img_styleClass_srcKey_nobody.reuse(_jspx_th_html_img_5);
    return false;
  }

  private boolean _jspx_meth_html_submit_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_0 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_0.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_submit_0.setProperty("method");
    _jspx_th_html_submit_0.setStyle("width: 100%; height: 30px;");
    int _jspx_eval_html_submit_0 = _jspx_th_html_submit_0.doStartTag();
    if (_jspx_eval_html_submit_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_0.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_0, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_0);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_0);
    return false;
  }

  private boolean _jspx_meth_bean_message_6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_6 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_6.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_0);
    _jspx_th_bean_message_6.setKey("menu.changeprice");
    int _jspx_eval_bean_message_6 = _jspx_th_bean_message_6.doStartTag();
    if (_jspx_th_bean_message_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_6);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_6);
    return false;
  }

  private boolean _jspx_meth_html_submit_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_1 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_1.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_submit_1.setProperty("method");
    _jspx_th_html_submit_1.setStyle("width: 100%; height: 30px;");
    int _jspx_eval_html_submit_1 = _jspx_th_html_submit_1.doStartTag();
    if (_jspx_eval_html_submit_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_1.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_1, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_1);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_1);
    return false;
  }

  private boolean _jspx_meth_bean_message_7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_7 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_7.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_1);
    _jspx_th_bean_message_7.setKey("menu.alerts");
    int _jspx_eval_bean_message_7 = _jspx_th_bean_message_7.doStartTag();
    if (_jspx_th_bean_message_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_7);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_7);
    return false;
  }

  private boolean _jspx_meth_html_submit_2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_2 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_2.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_submit_2.setProperty("method");
    _jspx_th_html_submit_2.setStyle("width: 100%; height: 30px;");
    int _jspx_eval_html_submit_2 = _jspx_th_html_submit_2.doStartTag();
    if (_jspx_eval_html_submit_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_2.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_2, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_2);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_2);
    return false;
  }

  private boolean _jspx_meth_bean_message_8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_8 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_8.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_2);
    _jspx_th_bean_message_8.setKey("menu.dailyreports");
    int _jspx_eval_bean_message_8 = _jspx_th_bean_message_8.doStartTag();
    if (_jspx_th_bean_message_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_8);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_8);
    return false;
  }

  private boolean _jspx_meth_html_submit_3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_3 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_styleId_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_3.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_submit_3.setProperty("method");
    _jspx_th_html_submit_3.setStyle("width: 100%; height: 30px;");
    _jspx_th_html_submit_3.setStyleId("navButtonProduct");
    int _jspx_eval_html_submit_3 = _jspx_th_html_submit_3.doStartTag();
    if (_jspx_eval_html_submit_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_3.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_9((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_3, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_styleId_style_property.reuse(_jspx_th_html_submit_3);
      return true;
    }
    _jspx_tagPool_html_submit_styleId_style_property.reuse(_jspx_th_html_submit_3);
    return false;
  }

  private boolean _jspx_meth_bean_message_9(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_9 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_9.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_3);
    _jspx_th_bean_message_9.setKey("menu.details");
    int _jspx_eval_bean_message_9 = _jspx_th_bean_message_9.doStartTag();
    if (_jspx_th_bean_message_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_9);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_9);
    return false;
  }

  private boolean _jspx_meth_html_submit_4(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_4 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_4.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_submit_4.setProperty("method");
    _jspx_th_html_submit_4.setStyle("width: 100%; height: 30px;");
    int _jspx_eval_html_submit_4 = _jspx_th_html_submit_4.doStartTag();
    if (_jspx_eval_html_submit_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_4.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_10((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_4);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_4);
    return false;
  }

  private boolean _jspx_meth_bean_message_10(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_10 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_10.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_4);
    _jspx_th_bean_message_10.setKey("menu.productassociation");
    int _jspx_eval_bean_message_10 = _jspx_th_bean_message_10.doStartTag();
    if (_jspx_th_bean_message_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_10);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_10);
    return false;
  }

  private boolean _jspx_meth_html_submit_5(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_5 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_5.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_submit_5.setProperty("method");
    _jspx_th_html_submit_5.setStyle("width: 100%; height: 30px;");
    int _jspx_eval_html_submit_5 = _jspx_th_html_submit_5.doStartTag();
    if (_jspx_eval_html_submit_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_5.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_11((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_5, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_5);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_5);
    return false;
  }

  private boolean _jspx_meth_bean_message_11(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_11 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_11.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_5);
    _jspx_th_bean_message_11.setKey("menu.deleteassociation");
    int _jspx_eval_bean_message_11 = _jspx_th_bean_message_11.doStartTag();
    if (_jspx_th_bean_message_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_11);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_11);
    return false;
  }

  private boolean _jspx_meth_html_submit_6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_6 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_6.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_submit_6.setProperty("method");
    _jspx_th_html_submit_6.setStyle("width: 100%; height: 30px;");
    int _jspx_eval_html_submit_6 = _jspx_th_html_submit_6.doStartTag();
    if (_jspx_eval_html_submit_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_6.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_12((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_6, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_6);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_6);
    return false;
  }

  private boolean _jspx_meth_bean_message_12(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_12 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_12.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_6);
    _jspx_th_bean_message_12.setKey("menu.sticker");
    int _jspx_eval_bean_message_12 = _jspx_th_bean_message_12.doStartTag();
    if (_jspx_th_bean_message_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_12);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_12);
    return false;
  }

  private boolean _jspx_meth_html_hidden_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_0 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
    _jspx_th_html_hidden_0.setPageContext(_jspx_page_context);
    _jspx_th_html_hidden_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_hidden_0.setProperty("step");
    int _jspx_eval_html_hidden_0 = _jspx_th_html_hidden_0.doStartTag();
    if (_jspx_th_html_hidden_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_hidden_property_nobody.reuse(_jspx_th_html_hidden_0);
      return true;
    }
    _jspx_tagPool_html_hidden_property_nobody.reuse(_jspx_th_html_hidden_0);
    return false;
  }

  private boolean _jspx_meth_html_hidden_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_1 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
    _jspx_th_html_hidden_1.setPageContext(_jspx_page_context);
    _jspx_th_html_hidden_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_hidden_1.setProperty("pricePerUnit");
    int _jspx_eval_html_hidden_1 = _jspx_th_html_hidden_1.doStartTag();
    if (_jspx_th_html_hidden_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_hidden_property_nobody.reuse(_jspx_th_html_hidden_1);
      return true;
    }
    _jspx_tagPool_html_hidden_property_nobody.reuse(_jspx_th_html_hidden_1);
    return false;
  }

  private boolean _jspx_meth_logic_equal_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_0 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_0.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_0.setName("pricerForm");
    _jspx_th_logic_equal_0.setProperty("step");
    _jspx_th_logic_equal_0.setValue("1");
    int _jspx_eval_logic_equal_0 = _jspx_th_logic_equal_0.doStartTag();
    if (_jspx_eval_logic_equal_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t");
        if (_jspx_meth_bean_message_14((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_0, _jspx_page_context))
          return true;
        out.write("\t\t \t\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_0);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_0);
    return false;
  }

  private boolean _jspx_meth_bean_message_14(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_14 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_14.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_0);
    _jspx_th_bean_message_14.setKey("pricer.step.1");
    int _jspx_eval_bean_message_14 = _jspx_th_bean_message_14.doStartTag();
    if (_jspx_th_bean_message_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_14);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_14);
    return false;
  }

  private boolean _jspx_meth_logic_equal_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_1 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_1.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_1.setName("pricerForm");
    _jspx_th_logic_equal_1.setProperty("step");
    _jspx_th_logic_equal_1.setValue("2");
    int _jspx_eval_logic_equal_1 = _jspx_th_logic_equal_1.doStartTag();
    if (_jspx_eval_logic_equal_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t");
        if (_jspx_meth_bean_message_15((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_1, _jspx_page_context))
          return true;
        out.write("\t\t\t\r\n\t\t\t\t");
        if (_jspx_meth_bean_write_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_1, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_1);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_1);
    return false;
  }

  private boolean _jspx_meth_bean_message_15(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_15 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_15.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_1);
    _jspx_th_bean_message_15.setKey("pricer.step.2");
    int _jspx_eval_bean_message_15 = _jspx_th_bean_message_15.doStartTag();
    if (_jspx_th_bean_message_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_15);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_15);
    return false;
  }

  private boolean _jspx_meth_bean_write_0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_0 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_0.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_1);
    _jspx_th_bean_write_0.setName("product");
    _jspx_th_bean_write_0.setProperty("productDescription");
    int _jspx_eval_bean_write_0 = _jspx_th_bean_write_0.doStartTag();
    if (_jspx_th_bean_write_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_0);
      return true;
    }
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_0);
    return false;
  }

  private boolean _jspx_meth_logic_equal_2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_2 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_2.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_2.setName("pricerForm");
    _jspx_th_logic_equal_2.setProperty("step");
    _jspx_th_logic_equal_2.setValue("3");
    int _jspx_eval_logic_equal_2 = _jspx_th_logic_equal_2.doStartTag();
    if (_jspx_eval_logic_equal_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t");
        if (_jspx_meth_bean_message_16((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_2, _jspx_page_context))
          return true;
        out.write("\t\t\t\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_2);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_2);
    return false;
  }

  private boolean _jspx_meth_bean_message_16(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_16 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_16.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_2);
    _jspx_th_bean_message_16.setKey("pricer.step.3");
    int _jspx_eval_bean_message_16 = _jspx_th_bean_message_16.doStartTag();
    if (_jspx_th_bean_message_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_16);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_16);
    return false;
  }

  private boolean _jspx_meth_logic_equal_3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_3 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_3.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_3.setName("pricerForm");
    _jspx_th_logic_equal_3.setProperty("step");
    _jspx_th_logic_equal_3.setValue("4");
    int _jspx_eval_logic_equal_3 = _jspx_th_logic_equal_3.doStartTag();
    if (_jspx_eval_logic_equal_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t");
        if (_jspx_meth_bean_message_17((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_3, _jspx_page_context))
          return true;
        out.write("\t\t\t\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_3);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_3);
    return false;
  }

  private boolean _jspx_meth_bean_message_17(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_17 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_17.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_3);
    _jspx_th_bean_message_17.setKey("pricer.step.4");
    int _jspx_eval_bean_message_17 = _jspx_th_bean_message_17.doStartTag();
    if (_jspx_th_bean_message_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_17);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_17);
    return false;
  }

  private boolean _jspx_meth_bean_message_18(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_18 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_18.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_18.setKey("pricer.step.1.insert");
    int _jspx_eval_bean_message_18 = _jspx_th_bean_message_18.doStartTag();
    if (_jspx_th_bean_message_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_18);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_18);
    return false;
  }

  private boolean _jspx_meth_html_text_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_0 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_style_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_0.setPageContext(_jspx_page_context);
    _jspx_th_html_text_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_text_0.setProperty("barcode");
    _jspx_th_html_text_0.setStyle("width: 100px; height: 16px;");
    int _jspx_eval_html_text_0 = _jspx_th_html_text_0.doStartTag();
    if (_jspx_th_html_text_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_text_style_property_nobody.reuse(_jspx_th_html_text_0);
      return true;
    }
    _jspx_tagPool_html_text_style_property_nobody.reuse(_jspx_th_html_text_0);
    return false;
  }

  private boolean _jspx_meth_logic_messagesPresent_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:messagesPresent
    org.apache.struts.taglib.logic.MessagesPresentTag _jspx_th_logic_messagesPresent_0 = (org.apache.struts.taglib.logic.MessagesPresentTag) _jspx_tagPool_logic_messagesPresent.get(org.apache.struts.taglib.logic.MessagesPresentTag.class);
    _jspx_th_logic_messagesPresent_0.setPageContext(_jspx_page_context);
    _jspx_th_logic_messagesPresent_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    int _jspx_eval_logic_messagesPresent_0 = _jspx_th_logic_messagesPresent_0.doStartTag();
    if (_jspx_eval_logic_messagesPresent_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t<div style=\"font-size: 10px; font-weight: bolder; color: red;\">\r\n\t\t\t\t");
        if (_jspx_meth_html_errors_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_messagesPresent_0, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t</div>\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_messagesPresent_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_messagesPresent_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_messagesPresent.reuse(_jspx_th_logic_messagesPresent_0);
      return true;
    }
    _jspx_tagPool_logic_messagesPresent.reuse(_jspx_th_logic_messagesPresent_0);
    return false;
  }

  private boolean _jspx_meth_html_errors_0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_messagesPresent_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.struts.taglib.html.ErrorsTag _jspx_th_html_errors_0 = (org.apache.struts.taglib.html.ErrorsTag) _jspx_tagPool_html_errors_nobody.get(org.apache.struts.taglib.html.ErrorsTag.class);
    _jspx_th_html_errors_0.setPageContext(_jspx_page_context);
    _jspx_th_html_errors_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_messagesPresent_0);
    int _jspx_eval_html_errors_0 = _jspx_th_html_errors_0.doStartTag();
    if (_jspx_th_html_errors_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_errors_nobody.reuse(_jspx_th_html_errors_0);
      return true;
    }
    _jspx_tagPool_html_errors_nobody.reuse(_jspx_th_html_errors_0);
    return false;
  }

  private boolean _jspx_meth_bean_message_19(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_19 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_19.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_19.setKey("pricer.step.2.insert");
    int _jspx_eval_bean_message_19 = _jspx_th_bean_message_19.doStartTag();
    if (_jspx_th_bean_message_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_19);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_19);
    return false;
  }

  private boolean _jspx_meth_html_text_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_1 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_style_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_1.setPageContext(_jspx_page_context);
    _jspx_th_html_text_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_text_1.setProperty("newprice");
    _jspx_th_html_text_1.setStyle("width: 100px; height: 16px;");
    int _jspx_eval_html_text_1 = _jspx_th_html_text_1.doStartTag();
    if (_jspx_th_html_text_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_text_style_property_nobody.reuse(_jspx_th_html_text_1);
      return true;
    }
    _jspx_tagPool_html_text_style_property_nobody.reuse(_jspx_th_html_text_1);
    return false;
  }

  private boolean _jspx_meth_logic_messagesPresent_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:messagesPresent
    org.apache.struts.taglib.logic.MessagesPresentTag _jspx_th_logic_messagesPresent_1 = (org.apache.struts.taglib.logic.MessagesPresentTag) _jspx_tagPool_logic_messagesPresent.get(org.apache.struts.taglib.logic.MessagesPresentTag.class);
    _jspx_th_logic_messagesPresent_1.setPageContext(_jspx_page_context);
    _jspx_th_logic_messagesPresent_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    int _jspx_eval_logic_messagesPresent_1 = _jspx_th_logic_messagesPresent_1.doStartTag();
    if (_jspx_eval_logic_messagesPresent_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t<div style=\"font-size: 10px; font-weight: bolder; color: red;\">\r\n\t\t\t\t");
        if (_jspx_meth_html_errors_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_messagesPresent_1, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t</div>\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_messagesPresent_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_messagesPresent_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_messagesPresent.reuse(_jspx_th_logic_messagesPresent_1);
      return true;
    }
    _jspx_tagPool_logic_messagesPresent.reuse(_jspx_th_logic_messagesPresent_1);
    return false;
  }

  private boolean _jspx_meth_html_errors_1(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_messagesPresent_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.struts.taglib.html.ErrorsTag _jspx_th_html_errors_1 = (org.apache.struts.taglib.html.ErrorsTag) _jspx_tagPool_html_errors_nobody.get(org.apache.struts.taglib.html.ErrorsTag.class);
    _jspx_th_html_errors_1.setPageContext(_jspx_page_context);
    _jspx_th_html_errors_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_messagesPresent_1);
    int _jspx_eval_html_errors_1 = _jspx_th_html_errors_1.doStartTag();
    if (_jspx_th_html_errors_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_errors_nobody.reuse(_jspx_th_html_errors_1);
      return true;
    }
    _jspx_tagPool_html_errors_nobody.reuse(_jspx_th_html_errors_1);
    return false;
  }

  private boolean _jspx_meth_html_radio_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:radio
    org.apache.struts.taglib.html.RadioTag _jspx_th_html_radio_0 = (org.apache.struts.taglib.html.RadioTag) _jspx_tagPool_html_radio_value_property_onclick_nobody.get(org.apache.struts.taglib.html.RadioTag.class);
    _jspx_th_html_radio_0.setPageContext(_jspx_page_context);
    _jspx_th_html_radio_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_radio_0.setProperty("options");
    _jspx_th_html_radio_0.setValue("discount");
    _jspx_th_html_radio_0.setOnclick("checkradios()");
    int _jspx_eval_html_radio_0 = _jspx_th_html_radio_0.doStartTag();
    if (_jspx_th_html_radio_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_0);
      return true;
    }
    _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_0);
    return false;
  }

  private boolean _jspx_meth_bean_message_20(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_20 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_20.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_20.setKey("pricer.step.3.discount");
    int _jspx_eval_bean_message_20 = _jspx_th_bean_message_20.doStartTag();
    if (_jspx_th_bean_message_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_20);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_20);
    return false;
  }

  private boolean _jspx_meth_html_text_2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_2 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_styleId_style_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_2.setPageContext(_jspx_page_context);
    _jspx_th_html_text_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_text_2.setProperty("discount");
    _jspx_th_html_text_2.setStyleId("discountText");
    _jspx_th_html_text_2.setStyle("width: 70px; height: 16px; text-align: right;");
    int _jspx_eval_html_text_2 = _jspx_th_html_text_2.doStartTag();
    if (_jspx_th_html_text_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_text_styleId_style_property_nobody.reuse(_jspx_th_html_text_2);
      return true;
    }
    _jspx_tagPool_html_text_styleId_style_property_nobody.reuse(_jspx_th_html_text_2);
    return false;
  }

  private boolean _jspx_meth_bean_message_21(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_21 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_21.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_21.setKey("pricer.step.3.%");
    int _jspx_eval_bean_message_21 = _jspx_th_bean_message_21.doStartTag();
    if (_jspx_th_bean_message_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_21);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_21);
    return false;
  }

  private boolean _jspx_meth_html_radio_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:radio
    org.apache.struts.taglib.html.RadioTag _jspx_th_html_radio_1 = (org.apache.struts.taglib.html.RadioTag) _jspx_tagPool_html_radio_value_property_onclick_nobody.get(org.apache.struts.taglib.html.RadioTag.class);
    _jspx_th_html_radio_1.setPageContext(_jspx_page_context);
    _jspx_th_html_radio_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_radio_1.setProperty("options");
    _jspx_th_html_radio_1.setValue("offer");
    _jspx_th_html_radio_1.setOnclick("checkradios()");
    int _jspx_eval_html_radio_1 = _jspx_th_html_radio_1.doStartTag();
    if (_jspx_th_html_radio_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_1);
      return true;
    }
    _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_1);
    return false;
  }

  private boolean _jspx_meth_bean_message_22(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_22 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_22.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_22.setKey("pricer.step.3.offer");
    int _jspx_eval_bean_message_22 = _jspx_th_bean_message_22.doStartTag();
    if (_jspx_th_bean_message_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_22);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_22);
    return false;
  }

  private boolean _jspx_meth_html_radio_2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:radio
    org.apache.struts.taglib.html.RadioTag _jspx_th_html_radio_2 = (org.apache.struts.taglib.html.RadioTag) _jspx_tagPool_html_radio_value_property_onclick_nobody.get(org.apache.struts.taglib.html.RadioTag.class);
    _jspx_th_html_radio_2.setPageContext(_jspx_page_context);
    _jspx_th_html_radio_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_radio_2.setProperty("options");
    _jspx_th_html_radio_2.setValue("points");
    _jspx_th_html_radio_2.setOnclick("checkradios()");
    int _jspx_eval_html_radio_2 = _jspx_th_html_radio_2.doStartTag();
    if (_jspx_th_html_radio_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_2);
      return true;
    }
    _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_2);
    return false;
  }

  private boolean _jspx_meth_bean_message_23(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_23 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_23.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_23.setKey("pricer.step.3.points");
    int _jspx_eval_bean_message_23 = _jspx_th_bean_message_23.doStartTag();
    if (_jspx_th_bean_message_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_23);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_23);
    return false;
  }

  private boolean _jspx_meth_html_text_3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_3 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_styleId_style_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_3.setPageContext(_jspx_page_context);
    _jspx_th_html_text_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_text_3.setProperty("points");
    _jspx_th_html_text_3.setStyleId("pointsText");
    _jspx_th_html_text_3.setStyle("width: 70px; height: 16px; text-align: right;");
    int _jspx_eval_html_text_3 = _jspx_th_html_text_3.doStartTag();
    if (_jspx_th_html_text_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_text_styleId_style_property_nobody.reuse(_jspx_th_html_text_3);
      return true;
    }
    _jspx_tagPool_html_text_styleId_style_property_nobody.reuse(_jspx_th_html_text_3);
    return false;
  }

  private boolean _jspx_meth_bean_message_24(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_24 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_24.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_24.setKey("pricer.step.3.pt");
    int _jspx_eval_bean_message_24 = _jspx_th_bean_message_24.doStartTag();
    if (_jspx_th_bean_message_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_24);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_24);
    return false;
  }

  private boolean _jspx_meth_html_radio_3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:radio
    org.apache.struts.taglib.html.RadioTag _jspx_th_html_radio_3 = (org.apache.struts.taglib.html.RadioTag) _jspx_tagPool_html_radio_value_property_onclick_nobody.get(org.apache.struts.taglib.html.RadioTag.class);
    _jspx_th_html_radio_3.setPageContext(_jspx_page_context);
    _jspx_th_html_radio_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_html_radio_3.setProperty("options");
    _jspx_th_html_radio_3.setValue("3x2");
    _jspx_th_html_radio_3.setOnclick("checkradios()");
    int _jspx_eval_html_radio_3 = _jspx_th_html_radio_3.doStartTag();
    if (_jspx_th_html_radio_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_3);
      return true;
    }
    _jspx_tagPool_html_radio_value_property_onclick_nobody.reuse(_jspx_th_html_radio_3);
    return false;
  }

  private boolean _jspx_meth_bean_message_25(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_25 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_25.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_bean_message_25.setKey("pricer.step.3.3x2");
    int _jspx_eval_bean_message_25 = _jspx_th_bean_message_25.doStartTag();
    if (_jspx_th_bean_message_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_25);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_25);
    return false;
  }

  private boolean _jspx_meth_logic_messagesPresent_2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:messagesPresent
    org.apache.struts.taglib.logic.MessagesPresentTag _jspx_th_logic_messagesPresent_2 = (org.apache.struts.taglib.logic.MessagesPresentTag) _jspx_tagPool_logic_messagesPresent.get(org.apache.struts.taglib.logic.MessagesPresentTag.class);
    _jspx_th_logic_messagesPresent_2.setPageContext(_jspx_page_context);
    _jspx_th_logic_messagesPresent_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    int _jspx_eval_logic_messagesPresent_2 = _jspx_th_logic_messagesPresent_2.doStartTag();
    if (_jspx_eval_logic_messagesPresent_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t<div style=\"font-size: 10px; font-weight: bolder; color: red;\">\r\n\t\t\t\t");
        if (_jspx_meth_html_errors_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_messagesPresent_2, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t</div>\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_messagesPresent_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_messagesPresent_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_messagesPresent.reuse(_jspx_th_logic_messagesPresent_2);
      return true;
    }
    _jspx_tagPool_logic_messagesPresent.reuse(_jspx_th_logic_messagesPresent_2);
    return false;
  }

  private boolean _jspx_meth_html_errors_2(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_messagesPresent_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.struts.taglib.html.ErrorsTag _jspx_th_html_errors_2 = (org.apache.struts.taglib.html.ErrorsTag) _jspx_tagPool_html_errors_nobody.get(org.apache.struts.taglib.html.ErrorsTag.class);
    _jspx_th_html_errors_2.setPageContext(_jspx_page_context);
    _jspx_th_html_errors_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_messagesPresent_2);
    int _jspx_eval_html_errors_2 = _jspx_th_html_errors_2.doStartTag();
    if (_jspx_th_html_errors_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_errors_nobody.reuse(_jspx_th_html_errors_2);
      return true;
    }
    _jspx_tagPool_html_errors_nobody.reuse(_jspx_th_html_errors_2);
    return false;
  }

  private boolean _jspx_meth_logic_present_4(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:present
    org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_4 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
    _jspx_th_logic_present_4.setPageContext(_jspx_page_context);
    _jspx_th_logic_present_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_present_4.setName("product");
    int _jspx_eval_logic_present_4 = _jspx_th_logic_present_4.doStartTag();
    if (_jspx_eval_logic_present_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t<table class=\"rounded\" style=\"width: 100%;\" id=\"publish\">\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_26((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\t\t\t\t\t\t\r\n\t\t\t\t\t<td align=\"right\">\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_write_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\t\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_27((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\t\t\t\t\t\t\r\n\t\t\t\t\t<td align=\"right\">\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_write_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\t\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_28((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td align=\"right\">\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_write_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\t\t\t\t\t\r\n\t\t\t\t");
        if (_jspx_meth_logic_equal_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\t\t\t\t\t\r\n\t\t\t\t");
        if (_jspx_meth_logic_equal_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\t\t\t\t\t\r\n\t\t\t\t");
        if (_jspx_meth_logic_equal_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t");
        if (_jspx_meth_logic_equal_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t</table>\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_present_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_present_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_4);
      return true;
    }
    _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_4);
    return false;
  }

  private boolean _jspx_meth_bean_message_26(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_26 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_26.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_bean_message_26.setKey("pricer.step.4.newprice");
    int _jspx_eval_bean_message_26 = _jspx_th_bean_message_26.doStartTag();
    if (_jspx_th_bean_message_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_26);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_26);
    return false;
  }

  private boolean _jspx_meth_bean_write_1(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_1 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_1.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_bean_write_1.setName("product");
    _jspx_th_bean_write_1.setProperty("productDescription");
    int _jspx_eval_bean_write_1 = _jspx_th_bean_write_1.doStartTag();
    if (_jspx_th_bean_write_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_1);
      return true;
    }
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_1);
    return false;
  }

  private boolean _jspx_meth_bean_message_27(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_27 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_27.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_bean_message_27.setKey("pricer.step.4.newprice");
    int _jspx_eval_bean_message_27 = _jspx_th_bean_message_27.doStartTag();
    if (_jspx_th_bean_message_27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_27);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_27);
    return false;
  }

  private boolean _jspx_meth_bean_write_2(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_2 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_2.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_bean_write_2.setName("pricerForm");
    _jspx_th_bean_write_2.setProperty("newprice");
    int _jspx_eval_bean_write_2 = _jspx_th_bean_write_2.doStartTag();
    if (_jspx_th_bean_write_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_2);
      return true;
    }
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_2);
    return false;
  }

  private boolean _jspx_meth_bean_message_28(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_28 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_28.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_bean_message_28.setKey("pricer.step.4.newppu");
    int _jspx_eval_bean_message_28 = _jspx_th_bean_message_28.doStartTag();
    if (_jspx_th_bean_message_28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_28);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_28);
    return false;
  }

  private boolean _jspx_meth_bean_write_3(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_3 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_3.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_bean_write_3.setName("pricerForm");
    _jspx_th_bean_write_3.setProperty("pricePerUnit");
    int _jspx_eval_bean_write_3 = _jspx_th_bean_write_3.doStartTag();
    if (_jspx_th_bean_write_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_3);
      return true;
    }
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_3);
    return false;
  }

  private boolean _jspx_meth_logic_equal_4(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_4 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_4.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_logic_equal_4.setName("pricerForm");
    _jspx_th_logic_equal_4.setProperty("options");
    _jspx_th_logic_equal_4.setValue("discount");
    int _jspx_eval_logic_equal_4 = _jspx_th_logic_equal_4.doStartTag();
    if (_jspx_eval_logic_equal_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_29((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td align=\"right\">\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_write_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_30((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_4, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_4);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_4);
    return false;
  }

  private boolean _jspx_meth_bean_message_29(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_29 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_29.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_4);
    _jspx_th_bean_message_29.setKey("pricer.step.3.discount");
    int _jspx_eval_bean_message_29 = _jspx_th_bean_message_29.doStartTag();
    if (_jspx_th_bean_message_29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_29);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_29);
    return false;
  }

  private boolean _jspx_meth_bean_write_4(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_4 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_4.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_4);
    _jspx_th_bean_write_4.setName("pricerForm");
    _jspx_th_bean_write_4.setProperty("discount");
    int _jspx_eval_bean_write_4 = _jspx_th_bean_write_4.doStartTag();
    if (_jspx_th_bean_write_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_4);
      return true;
    }
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_4);
    return false;
  }

  private boolean _jspx_meth_bean_message_30(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_30 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_30.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_4);
    _jspx_th_bean_message_30.setKey("pricer.step.3.%");
    int _jspx_eval_bean_message_30 = _jspx_th_bean_message_30.doStartTag();
    if (_jspx_th_bean_message_30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_30);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_30);
    return false;
  }

  private boolean _jspx_meth_logic_equal_5(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_5 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_5.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_logic_equal_5.setName("pricerForm");
    _jspx_th_logic_equal_5.setProperty("options");
    _jspx_th_logic_equal_5.setValue("points");
    int _jspx_eval_logic_equal_5 = _jspx_th_logic_equal_5.doStartTag();
    if (_jspx_eval_logic_equal_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t<tr>\t\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_31((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_5, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td align=\"right\">\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_write_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_5, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_32((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_5, _jspx_page_context))
          return true;
        out.write("\t\t\t\t\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_5);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_5);
    return false;
  }

  private boolean _jspx_meth_bean_message_31(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_31 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_31.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_5);
    _jspx_th_bean_message_31.setKey("pricer.step.3.points");
    int _jspx_eval_bean_message_31 = _jspx_th_bean_message_31.doStartTag();
    if (_jspx_th_bean_message_31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_31);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_31);
    return false;
  }

  private boolean _jspx_meth_bean_write_5(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_5 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_5.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_5);
    _jspx_th_bean_write_5.setName("pricerForm");
    _jspx_th_bean_write_5.setProperty("points");
    int _jspx_eval_bean_write_5 = _jspx_th_bean_write_5.doStartTag();
    if (_jspx_th_bean_write_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_5);
      return true;
    }
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_5);
    return false;
  }

  private boolean _jspx_meth_bean_message_32(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_32 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_32.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_5);
    _jspx_th_bean_message_32.setKey("pricer.step.3.pt");
    int _jspx_eval_bean_message_32 = _jspx_th_bean_message_32.doStartTag();
    if (_jspx_th_bean_message_32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_32);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_32);
    return false;
  }

  private boolean _jspx_meth_logic_equal_6(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_6 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_6.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_logic_equal_6.setName("pricerForm");
    _jspx_th_logic_equal_6.setProperty("options");
    _jspx_th_logic_equal_6.setValue("offer");
    int _jspx_eval_logic_equal_6 = _jspx_th_logic_equal_6.doStartTag();
    if (_jspx_eval_logic_equal_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t<tr>\t\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_33((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_6, _jspx_page_context))
          return true;
        out.write("\t\t\t\t\t\t\t\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t&nbsp;\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\t\t\t\t\t\t\r\n\t\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_6);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_6);
    return false;
  }

  private boolean _jspx_meth_bean_message_33(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_33 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_33.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_6);
    _jspx_th_bean_message_33.setKey("pricer.step.3.offer");
    int _jspx_eval_bean_message_33 = _jspx_th_bean_message_33.doStartTag();
    if (_jspx_th_bean_message_33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_33);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_33);
    return false;
  }

  private boolean _jspx_meth_logic_equal_7(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_7 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_7.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_4);
    _jspx_th_logic_equal_7.setName("pricerForm");
    _jspx_th_logic_equal_7.setProperty("options");
    _jspx_th_logic_equal_7.setValue("3x2");
    int _jspx_eval_logic_equal_7 = _jspx_th_logic_equal_7.doStartTag();
    if (_jspx_eval_logic_equal_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t<tr>\t\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t");
        if (_jspx_meth_bean_message_34((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_7, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td>\r\n\t\t\t\t\t\t&nbsp;\r\n\t\t\t\t\t</td>\r\n\t\t\t\t</tr>\r\n\t\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_7);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_7);
    return false;
  }

  private boolean _jspx_meth_bean_message_34(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_34 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_34.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_7);
    _jspx_th_bean_message_34.setKey("pricer.step.3.3x2");
    int _jspx_eval_bean_message_34 = _jspx_th_bean_message_34.doStartTag();
    if (_jspx_th_bean_message_34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_34);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_34);
    return false;
  }

  private boolean _jspx_meth_logic_equal_8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_8 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_8.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_8.setName("pricerForm");
    _jspx_th_logic_equal_8.setProperty("step");
    _jspx_th_logic_equal_8.setValue("4");
    int _jspx_eval_logic_equal_8 = _jspx_th_logic_equal_8.doStartTag();
    if (_jspx_eval_logic_equal_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t");
        if (_jspx_meth_html_submit_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_equal_8, _jspx_page_context))
          return true;
        out.write("\t\t\t\t\t\t\r\n\t\t");
        int evalDoAfterBody = _jspx_th_logic_equal_8.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_8);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_8);
    return false;
  }

  private boolean _jspx_meth_html_submit_7(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_equal_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_7 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_7.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_equal_8);
    _jspx_th_html_submit_7.setProperty("method");
    _jspx_th_html_submit_7.setStyle("width: 120px; position: relative; left: 140px; font-weight: bolder;");
    int _jspx_eval_html_submit_7 = _jspx_th_html_submit_7.doStartTag();
    if (_jspx_eval_html_submit_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_7.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_35((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_7, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_7);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_7);
    return false;
  }

  private boolean _jspx_meth_bean_message_35(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_35 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_35.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_7);
    _jspx_th_bean_message_35.setKey("pricer.publish");
    int _jspx_eval_bean_message_35 = _jspx_th_bean_message_35.doStartTag();
    if (_jspx_th_bean_message_35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_35);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_35);
    return false;
  }

  private boolean _jspx_meth_bean_message_36(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_36 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_36.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_8);
    _jspx_th_bean_message_36.setKey("pricer.next");
    int _jspx_eval_bean_message_36 = _jspx_th_bean_message_36.doStartTag();
    if (_jspx_th_bean_message_36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_36);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_36);
    return false;
  }

  private boolean _jspx_meth_logic_notEqual_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:notEqual
    org.apache.struts.taglib.logic.NotEqualTag _jspx_th_logic_notEqual_1 = (org.apache.struts.taglib.logic.NotEqualTag) _jspx_tagPool_logic_notEqual_value_property_name.get(org.apache.struts.taglib.logic.NotEqualTag.class);
    _jspx_th_logic_notEqual_1.setPageContext(_jspx_page_context);
    _jspx_th_logic_notEqual_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_notEqual_1.setName("pricerForm");
    _jspx_th_logic_notEqual_1.setProperty("step");
    _jspx_th_logic_notEqual_1.setValue("1");
    int _jspx_eval_logic_notEqual_1 = _jspx_th_logic_notEqual_1.doStartTag();
    if (_jspx_eval_logic_notEqual_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\t\t\t\r\n\t\t\t");
        if (_jspx_meth_html_submit_9((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_notEqual_1, _jspx_page_context))
          return true;
        out.write("\t\t\t\r\n\t\t");
        int evalDoAfterBody = _jspx_th_logic_notEqual_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_notEqual_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_notEqual_value_property_name.reuse(_jspx_th_logic_notEqual_1);
      return true;
    }
    _jspx_tagPool_logic_notEqual_value_property_name.reuse(_jspx_th_logic_notEqual_1);
    return false;
  }

  private boolean _jspx_meth_html_submit_9(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_notEqual_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.struts.taglib.html.SubmitTag _jspx_th_html_submit_9 = (org.apache.struts.taglib.html.SubmitTag) _jspx_tagPool_html_submit_style_property.get(org.apache.struts.taglib.html.SubmitTag.class);
    _jspx_th_html_submit_9.setPageContext(_jspx_page_context);
    _jspx_th_html_submit_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_notEqual_1);
    _jspx_th_html_submit_9.setProperty("method");
    _jspx_th_html_submit_9.setStyle("width: 120px; position: relative; left: -120px;");
    int _jspx_eval_html_submit_9 = _jspx_th_html_submit_9.doStartTag();
    if (_jspx_eval_html_submit_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_submit_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_submit_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_submit_9.doInitBody();
      }
      do {
        out.write("      \r\n\t\t\t\t");
        if (_jspx_meth_bean_message_37((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_9, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t");
        int evalDoAfterBody = _jspx_th_html_submit_9.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_submit_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_submit_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_9);
      return true;
    }
    _jspx_tagPool_html_submit_style_property.reuse(_jspx_th_html_submit_9);
    return false;
  }

  private boolean _jspx_meth_bean_message_37(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_37 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_37.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_9);
    _jspx_th_bean_message_37.setKey("pricer.previous");
    int _jspx_eval_bean_message_37 = _jspx_th_bean_message_37.doStartTag();
    if (_jspx_th_bean_message_37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_37);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_37);
    return false;
  }

  private boolean _jspx_meth_logic_equal_9(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_9 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_9.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_9.setName("pricerForm");
    _jspx_th_logic_equal_9.setProperty("step");
    _jspx_th_logic_equal_9.setValue("1");
    int _jspx_eval_logic_equal_9 = _jspx_th_logic_equal_9.doStartTag();
    if (_jspx_eval_logic_equal_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\tvar frm = document.getElementById('pricerForm');\r\n\tfrm.barcode.focus();\t\r\n");
        int evalDoAfterBody = _jspx_th_logic_equal_9.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_9);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_9);
    return false;
  }

  private boolean _jspx_meth_logic_equal_10(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_10 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_10.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_10.setName("pricerForm");
    _jspx_th_logic_equal_10.setProperty("step");
    _jspx_th_logic_equal_10.setValue("2");
    int _jspx_eval_logic_equal_10 = _jspx_th_logic_equal_10.doStartTag();
    if (_jspx_eval_logic_equal_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\tvar frm = document.getElementById('pricerForm');\r\n\tfrm.newprice.focus();\r\n");
        int evalDoAfterBody = _jspx_th_logic_equal_10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_10);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_10);
    return false;
  }

  private boolean _jspx_meth_logic_equal_11(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_11 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_11.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_11.setName("pricerForm");
    _jspx_th_logic_equal_11.setProperty("step");
    _jspx_th_logic_equal_11.setValue("3");
    int _jspx_eval_logic_equal_11 = _jspx_th_logic_equal_11.doStartTag();
    if (_jspx_eval_logic_equal_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\tvar prevcheck = null;\t\r\n\tfunction checkradios()\r\n\t{\t\r\n\t\tvar frm = document.getElementById('pricerForm');\r\n\t\t// reset options \r\n\t\tdocument.getElementById('discount').style.display = 'none';\r\n\t\tdocument.getElementById('points').style.display = 'none';\r\n\t\tif (frm.options.value == prevcheck) {\t\t\t\r\n\t\t\t var radios = document.getElementsByTagName(\"input\");\r\n\t\t\t for (var i = 0; i < radios.length; i ++) {\r\n\t\t\t\tif (radios[i].type \r\n\t\t\t\t\t\t&& radios[i].type == \"radio\" \r\n\t\t\t\t\t\t&& radios[i].name == 'options') {\r\n\t\t\t\t\tradios[i].checked = false;\r\n\t\t\t\t}\r\n\t\t\t }\r\n\t\t\t frm.options.value = '';\r\n\t\t}\r\n\t\t// display if needed\r\n\t\tswitch (frm.options.value) {\t\t\t\r\n\t\t\tcase 'discount':\r\n\t\t\t\tdocument.getElementById('discount').style.display = 'block';\r\n\t\t\t\tdocument.getElementById('discountText').focus();\r\n\t\t\t\tbreak;\r\n\t\t\tcase 'points':\r\n\t\t\t\tdocument.getElementById('points').style.display = 'block';\r\n\t\t\t\tdocument.getElementById('pointsText').focus();\r\n\t\t\t\tbreak;\r\n\t\t}\t\t\r\n\t\tprevcheck = frm.options.value;\t\t\r\n\t}\t\r\n\tcheckradios();\r\n\tdocument.getElementById('next').focus();\t\t\r\n");
        int evalDoAfterBody = _jspx_th_logic_equal_11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_11);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_11);
    return false;
  }

  private boolean _jspx_meth_logic_equal_12(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:equal
    org.apache.struts.taglib.logic.EqualTag _jspx_th_logic_equal_12 = (org.apache.struts.taglib.logic.EqualTag) _jspx_tagPool_logic_equal_value_property_name.get(org.apache.struts.taglib.logic.EqualTag.class);
    _jspx_th_logic_equal_12.setPageContext(_jspx_page_context);
    _jspx_th_logic_equal_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_1);
    _jspx_th_logic_equal_12.setName("pricerForm");
    _jspx_th_logic_equal_12.setProperty("step");
    _jspx_th_logic_equal_12.setValue("4");
    int _jspx_eval_logic_equal_12 = _jspx_th_logic_equal_12.doStartTag();
    if (_jspx_eval_logic_equal_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\t\r\n\t// blinking button\r\n\tfunction doBlink() {\r\n\t\tvar el = document.getElementById('publish');\t\t\r\n\t\tvar op = (el.style != null ? el.style.opacity : null);\r\n\t\tel.style.opacity = (!op || op == '1' ?  '0.5' : '1');\t\t\t\t\r\n\t\tsetTimeout(\"doBlink()\", 500);\r\n\t}\t\r\n\tdoBlink();\r\n");
        int evalDoAfterBody = _jspx_th_logic_equal_12.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_equal_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_12);
      return true;
    }
    _jspx_tagPool_logic_equal_value_property_name.reuse(_jspx_th_logic_equal_12);
    return false;
  }

  private boolean _jspx_meth_bean_message_38(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_38 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_38.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_38.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_38.setKey("daily.title");
    int _jspx_eval_bean_message_38 = _jspx_th_bean_message_38.doStartTag();
    if (_jspx_th_bean_message_38.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_38);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_38);
    return false;
  }

  private boolean _jspx_meth_bean_message_39(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_39 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_39.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_39.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_39.setKey("xslt.selecttime");
    int _jspx_eval_bean_message_39 = _jspx_th_bean_message_39.doStartTag();
    if (_jspx_th_bean_message_39.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_39);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_39);
    return false;
  }

  private boolean _jspx_meth_bean_message_40(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_40 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_40.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_40.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_40.setKey("combo.lasthour");
    int _jspx_eval_bean_message_40 = _jspx_th_bean_message_40.doStartTag();
    if (_jspx_th_bean_message_40.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_40);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_40);
    return false;
  }

  private boolean _jspx_meth_bean_message_41(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_41 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_41.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_41.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_41.setKey("combo.lastxhours");
    _jspx_th_bean_message_41.setArg0("6");
    int _jspx_eval_bean_message_41 = _jspx_th_bean_message_41.doStartTag();
    if (_jspx_th_bean_message_41.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_41);
      return true;
    }
    _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_41);
    return false;
  }

  private boolean _jspx_meth_bean_message_42(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_42 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_42.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_42.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_42.setKey("combo.lastday");
    int _jspx_eval_bean_message_42 = _jspx_th_bean_message_42.doStartTag();
    if (_jspx_th_bean_message_42.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_42);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_42);
    return false;
  }

  private boolean _jspx_meth_bean_message_43(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_43 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_43.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_43.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_43.setKey("combo.lastweek");
    int _jspx_eval_bean_message_43 = _jspx_th_bean_message_43.doStartTag();
    if (_jspx_th_bean_message_43.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_43);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_43);
    return false;
  }

  private boolean _jspx_meth_bean_message_44(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_44 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_44.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_44.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_44.setKey("xslt.selectmaxitems");
    int _jspx_eval_bean_message_44 = _jspx_th_bean_message_44.doStartTag();
    if (_jspx_th_bean_message_44.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_44);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_44);
    return false;
  }

  private boolean _jspx_meth_bean_message_45(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_45 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_45.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_45.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_45.setKey("combo.unlimited");
    int _jspx_eval_bean_message_45 = _jspx_th_bean_message_45.doStartTag();
    if (_jspx_th_bean_message_45.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_45);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_45);
    return false;
  }

  private boolean _jspx_meth_bean_message_46(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_46 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_46.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_46.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_46.setKey("xslt.refreshupdate");
    int _jspx_eval_bean_message_46 = _jspx_th_bean_message_46.doStartTag();
    if (_jspx_th_bean_message_46.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_46);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_46);
    return false;
  }

  private boolean _jspx_meth_bean_message_47(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_47 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_47.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_47.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_47.setKey("combo.everysec");
    int _jspx_eval_bean_message_47 = _jspx_th_bean_message_47.doStartTag();
    if (_jspx_th_bean_message_47.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_47);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_47);
    return false;
  }

  private boolean _jspx_meth_bean_message_48(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_48 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_48.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_48.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_48.setKey("combo.everyxsecs");
    _jspx_th_bean_message_48.setArg0("5");
    int _jspx_eval_bean_message_48 = _jspx_th_bean_message_48.doStartTag();
    if (_jspx_th_bean_message_48.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_48);
      return true;
    }
    _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_48);
    return false;
  }

  private boolean _jspx_meth_bean_message_49(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_49 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_49.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_49.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_49.setKey("combo.everyxsecs");
    _jspx_th_bean_message_49.setArg0("10");
    int _jspx_eval_bean_message_49 = _jspx_th_bean_message_49.doStartTag();
    if (_jspx_th_bean_message_49.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_49);
      return true;
    }
    _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_49);
    return false;
  }

  private boolean _jspx_meth_bean_message_50(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_50 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_50.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_50.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_50.setKey("combo.everyxsecs");
    _jspx_th_bean_message_50.setArg0("30");
    int _jspx_eval_bean_message_50 = _jspx_th_bean_message_50.doStartTag();
    if (_jspx_th_bean_message_50.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_50);
      return true;
    }
    _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_50);
    return false;
  }

  private boolean _jspx_meth_logic_notEmpty_0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:notEmpty
    org.apache.struts.taglib.logic.NotEmptyTag _jspx_th_logic_notEmpty_0 = (org.apache.struts.taglib.logic.NotEmptyTag) _jspx_tagPool_logic_notEmpty_name.get(org.apache.struts.taglib.logic.NotEmptyTag.class);
    _jspx_th_logic_notEmpty_0.setPageContext(_jspx_page_context);
    _jspx_th_logic_notEmpty_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_5);
    _jspx_th_logic_notEmpty_0.setName("statusbar");
    int _jspx_eval_logic_notEmpty_0 = _jspx_th_logic_notEmpty_0.doStartTag();
    if (_jspx_eval_logic_notEmpty_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n\t\t\t\t\t\t\t");
        if (_jspx_meth_bean_write_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_notEmpty_0, _jspx_page_context))
          return true;
        out.write("\r\n\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_notEmpty_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_notEmpty_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_notEmpty_name.reuse(_jspx_th_logic_notEmpty_0);
      return true;
    }
    _jspx_tagPool_logic_notEmpty_name.reuse(_jspx_th_logic_notEmpty_0);
    return false;
  }

  private boolean _jspx_meth_bean_write_6(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_notEmpty_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_6 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_6.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_notEmpty_0);
    _jspx_th_bean_write_6.setName("statusbar");
    int _jspx_eval_bean_write_6 = _jspx_th_bean_write_6.doStartTag();
    if (_jspx_th_bean_write_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_name_nobody.reuse(_jspx_th_bean_write_6);
      return true;
    }
    _jspx_tagPool_bean_write_name_nobody.reuse(_jspx_th_bean_write_6);
    return false;
  }

  private boolean _jspx_meth_bean_message_51(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_51 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_51.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_51.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_5);
    _jspx_th_bean_message_51.setKey("pricer.lastupdate");
    int _jspx_eval_bean_message_51 = _jspx_th_bean_message_51.doStartTag();
    if (_jspx_th_bean_message_51.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_51);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_51);
    return false;
  }

  private boolean _jspx_meth_bean_message_52(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_52 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_52.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_52.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_6);
    _jspx_th_bean_message_52.setKey("footer.customeragreement");
    int _jspx_eval_bean_message_52 = _jspx_th_bean_message_52.doStartTag();
    if (_jspx_th_bean_message_52.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_52);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_52);
    return false;
  }

  private boolean _jspx_meth_bean_message_53(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_53 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_53.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_53.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_bean_message_53.setKey("footer.copyrigth");
    int _jspx_eval_bean_message_53 = _jspx_th_bean_message_53.doStartTag();
    if (_jspx_th_bean_message_53.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_53);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_53);
    return false;
  }

  private boolean _jspx_meth_html_img_6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_6 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_6.setPageContext(_jspx_page_context);
    _jspx_th_html_img_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_html_img_6.setSrcKey("image.twitter.logo");
    _jspx_th_html_img_6.setAltKey("image.twitter.logo.alt");
    _jspx_th_html_img_6.setStyleClass("trans ico");
    int _jspx_eval_html_img_6 = _jspx_th_html_img_6.doStartTag();
    if (_jspx_th_html_img_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.reuse(_jspx_th_html_img_6);
      return true;
    }
    _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.reuse(_jspx_th_html_img_6);
    return false;
  }

  private boolean _jspx_meth_html_img_7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_7 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_7.setPageContext(_jspx_page_context);
    _jspx_th_html_img_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_html_img_7.setSrcKey("image.linkedin.logo");
    _jspx_th_html_img_7.setAltKey("image.linkedin.logo.alt");
    _jspx_th_html_img_7.setStyleClass("trans ico");
    int _jspx_eval_html_img_7 = _jspx_th_html_img_7.doStartTag();
    if (_jspx_th_html_img_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.reuse(_jspx_th_html_img_7);
      return true;
    }
    _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.reuse(_jspx_th_html_img_7);
    return false;
  }

  private boolean _jspx_meth_html_img_8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_8 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_8.setPageContext(_jspx_page_context);
    _jspx_th_html_img_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_html_img_8.setSrcKey("image.angels.logo");
    _jspx_th_html_img_8.setAltKey("image.angels.logo.alt");
    _jspx_th_html_img_8.setStyleClass("trans ico");
    int _jspx_eval_html_img_8 = _jspx_th_html_img_8.doStartTag();
    if (_jspx_th_html_img_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.reuse(_jspx_th_html_img_8);
      return true;
    }
    _jspx_tagPool_html_img_styleClass_srcKey_altKey_nobody.reuse(_jspx_th_html_img_8);
    return false;
  }

  private boolean _jspx_meth_logic_present_7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:present
    org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_7 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
    _jspx_th_logic_present_7.setPageContext(_jspx_page_context);
    _jspx_th_logic_present_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_logic_present_7.setName("focus");
    int _jspx_eval_logic_present_7 = _jspx_th_logic_present_7.doStartTag();
    if (_jspx_eval_logic_present_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n<script type=\"text/javascript\">\r\n<!--\r\nfor (var i = 0; i < document.forms.length; i ++) {\r\n\tif (document.forms[i].");
        if (_jspx_meth_bean_write_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_7, _jspx_page_context))
          return true;
        out.write(") {\t\t\r\n\t\tvar field = document.forms[i].");
        if (_jspx_meth_bean_write_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_7, _jspx_page_context))
          return true;
        out.write(";\r\n\t\tfield.style.background = '#ffcccc';\r\n\t\tfield.style.color = '#ee0000';\r\n\t\tif (field.type == 'text') {\r\n\t\t\tfield.setSelectionRange(0, field.value.length);\r\n\t\t}\r\n\t\tfield.focus();\t\t\t\t\t\r\n\t\tbreak;\r\n\t}\r\n}\r\n// -->\r\n</script>\r\n");
        int evalDoAfterBody = _jspx_th_logic_present_7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_present_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_7);
      return true;
    }
    _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_7);
    return false;
  }

  private boolean _jspx_meth_bean_write_7(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_7 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_7.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_7);
    _jspx_th_bean_write_7.setName("focus");
    int _jspx_eval_bean_write_7 = _jspx_th_bean_write_7.doStartTag();
    if (_jspx_th_bean_write_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_name_nobody.reuse(_jspx_th_bean_write_7);
      return true;
    }
    _jspx_tagPool_bean_write_name_nobody.reuse(_jspx_th_bean_write_7);
    return false;
  }

  private boolean _jspx_meth_bean_write_8(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_8 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_8.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_7);
    _jspx_th_bean_write_8.setName("focus");
    int _jspx_eval_bean_write_8 = _jspx_th_bean_write_8.doStartTag();
    if (_jspx_th_bean_write_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_write_name_nobody.reuse(_jspx_th_bean_write_8);
      return true;
    }
    _jspx_tagPool_bean_write_name_nobody.reuse(_jspx_th_bean_write_8);
    return false;
  }

  private boolean _jspx_meth_logic_present_8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:present
    org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_8 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
    _jspx_th_logic_present_8.setPageContext(_jspx_page_context);
    _jspx_th_logic_present_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_logic_present_8.setName("view3d");
    int _jspx_eval_logic_present_8 = _jspx_th_logic_present_8.doStartTag();
    if (_jspx_eval_logic_present_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n</div>\r\n<script type=\"text/javascript\">\r\n\t\r\n\t// Create an instance of Meny\r\n\tvar meny = Meny.create({\r\n\t\t// The element that will be animated in from off screen\r\n\t\tmenuElement: document.querySelector( '.meny' ),\r\n\r\n\t\t// The contents that gets pushed aside while Meny is active\r\n\t\tcontentsElement: document.querySelector( '.contents' ),\r\n\r\n\t\t// [optional] The alignment of the menu (top/right/bottom/left)\r\n\t\tposition: Meny.getQuery().p || 'left',\r\n\r\n\t\t// [optional] The height of the menu (when using top/bottom position)\r\n\t\theight: 200,\r\n\r\n\t\t// [optional] The width of the menu (when using left/right position)\r\n\t\twidth: 260,\r\n\r\n\t\t// [optional] Distance from mouse (in pixels) when menu should open\r\n\t\tthreshold: 40,\r\n\r\n\t\t// [optional] Use mouse movement to automatically open/close\r\n\t\tmouse: true,\r\n\r\n\t\t// [optional] Use touch swipe events to open/close\r\n\t\ttouch: true\r\n\t});\r\n\r\n\t// API Methods:\r\n\t// meny.open();\r\n\t// meny.close();\r\n\t// meny.isOpen();\r\n\r\n\t// Events:\r\n\t// meny.addEventListener( 'open', function(){ console.log( 'open' ); } );\r\n");
        out.write("\t// meny.addEventListener( 'close', function(){ console.log( 'close' ); } );\r\n\r\n\t// Embed an iframe if a URL is passed in\r\n\tif( Meny.getQuery().u && Meny.getQuery().u.match( /^http/gi ) ) {\r\n\t\tvar contents = document.querySelector( '.contents' );\r\n\t\tcontents.style.padding = '0px';\r\n\t\tcontents.innerHTML = '<div class=\"cover\"></div><iframe src=\"'+ Meny.getQuery().u +'\" style=\"width: 100%; height: 100%; border: 0; position: absolute;\"></iframe>';\r\n\t}\r\n\t\r\n</script>\r\n");
        int evalDoAfterBody = _jspx_th_logic_present_8.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_present_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_8);
      return true;
    }
    _jspx_tagPool_logic_present_name.reuse(_jspx_th_logic_present_8);
    return false;
  }
}
