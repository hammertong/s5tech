package org.apache.jsp.WEB_002dINF.pages.include;

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

public final class header_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/WEB-INF/pages/include/../menu/standard.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_present_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_message_key_arg1_arg0_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_message_key_arg0_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_message_key_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_img_styleClass_srcKey_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_form_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_submit_style_property;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_submit_styleId_style_property;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notPresent_name;

  private org.apache.jasper.runtime.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_logic_present_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_message_key_arg1_arg0_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_message_key_arg0_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_message_key_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_img_styleClass_srcKey_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_form_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_submit_style_property = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_submit_styleId_style_property = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notPresent_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_logic_present_name.release();
    _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody.release();
    _jspx_tagPool_bean_message_key_arg1_arg0_nobody.release();
    _jspx_tagPool_bean_message_key_arg0_nobody.release();
    _jspx_tagPool_html_link_href.release();
    _jspx_tagPool_html_img_width_styleClass_srcKey_height_border_altKey_nobody.release();
    _jspx_tagPool_bean_message_key_nobody.release();
    _jspx_tagPool_html_img_styleClass_srcKey_nobody.release();
    _jspx_tagPool_html_form_action.release();
    _jspx_tagPool_html_submit_style_property.release();
    _jspx_tagPool_html_submit_styleId_style_property.release();
    _jspx_tagPool_logic_notPresent_name.release();
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

      out.write("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");

	UserView userView = null;	
	String statusbar = null;		

      out.write("\r\n\r\n<!-- begin header section -->\r\n");
      //  logic:present
      org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_0 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
      _jspx_th_logic_present_0.setPageContext(_jspx_page_context);
      _jspx_th_logic_present_0.setParent(null);
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
      if (_jspx_meth_html_img_0(_jspx_page_context))
        return;
      out.write("\r\n\t\t</td>\t\r\n\t\t<td align=\"right\" valign=\"middle\">\r\n\t\t\t&nbsp;\r\n\t\t\t");
      //  logic:present
      org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_1 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_name.get(org.apache.struts.taglib.logic.PresentTag.class);
      _jspx_th_logic_present_1.setPageContext(_jspx_page_context);
      _jspx_th_logic_present_1.setParent(null);
      _jspx_th_logic_present_1.setName( IConstants.USER_VIEW_KEY );
      int _jspx_eval_logic_present_1 = _jspx_th_logic_present_1.doStartTag();
      if (_jspx_eval_logic_present_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\t\r\n\t\t\t<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n\t\t\t\t<tr>\r\n\t\t\t\t\t<td align=\"left\" style=\"color: #ccdfe1;\">\r\n\t\t\t\t\t\t");
          //  bean:message
          org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_0 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg1_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
          _jspx_th_bean_message_0.setPageContext(_jspx_page_context);
          _jspx_th_bean_message_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
          _jspx_th_bean_message_0.setKey("header.userloggedin");
          _jspx_th_bean_message_0.setArg0( userView.getFirstName() );
          _jspx_th_bean_message_0.setArg1("");
          int _jspx_eval_bean_message_0 = _jspx_th_bean_message_0.doStartTag();
          if (_jspx_th_bean_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_bean_message_key_arg1_arg0_nobody.reuse(_jspx_th_bean_message_0);
            return;
          }
          _jspx_tagPool_bean_message_key_arg1_arg0_nobody.reuse(_jspx_th_bean_message_0);
          out.write("\r\n\t\t\t\t\t\t<br>\r\n\t\t\t\t\t\t");
          //  bean:message
          org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_1 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_arg0_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
          _jspx_th_bean_message_1.setPageContext(_jspx_page_context);
          _jspx_th_bean_message_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
          _jspx_th_bean_message_1.setKey("header.userrole");
          _jspx_th_bean_message_1.setArg0( userView.getRole() );
          int _jspx_eval_bean_message_1 = _jspx_th_bean_message_1.doStartTag();
          if (_jspx_th_bean_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_1);
            return;
          }
          _jspx_tagPool_bean_message_key_arg0_nobody.reuse(_jspx_th_bean_message_1);
          out.write("\r\n\t\t\t\t\t</td>\r\n\t\t\t\t\t<td align=\"center\" style=\"width: 50px;\"> \r\n\t\t\t\t\t\t");
          if (_jspx_meth_html_link_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
            return;
          out.write("\r\n\t\t\t\t\t\t<br>\r\n\t\t\t\t\t\t<span style=\"font-size: 10px; opacity: 0.6;\">\r\n\t\t\t\t\t\t\t");
          if (_jspx_meth_bean_message_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
            return;
          out.write("\r\n\t\t\t\t\t\t</span>\r\n\t\t\t\t\t</td>      \r\n\t\t\t\t\t<td align=\"center\" style=\"width: 50px;\"> \r\n\t\t\t\t\t\t");
          if (_jspx_meth_html_link_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
            return;
          out.write("\r\n\t\t\t\t\t\t<br>\r\n\t\t\t\t\t\t<span style=\"font-size: 10px; opacity: 0.6;\">\r\n\t\t\t\t\t\t\t");
          if (_jspx_meth_bean_message_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_1, _jspx_page_context))
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
      _jspx_th_logic_present_2.setParent(null);
      _jspx_th_logic_present_2.setName( IConstants.USER_VIEW_KEY );
      int _jspx_eval_logic_present_2 = _jspx_th_logic_present_2.doStartTag();
      if (_jspx_eval_logic_present_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\t\r\n\t\r\n\t<tr class=\"hnavbar\" style=\"padding: 0px; margin: 0px;\">\r\n\t\t<td colspan=\"2\" style=\"padding: 0px; margin: 0px;\"> \r\n\t\t\t<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"padding: 0px; margin: 0px;\">\r\n\t\t\t\t<tr valign=\"middle\" class=\"hnavbar\" style=\"padding: 0px; margin: 0px;\">\t\t\t\t\t\t\t\t\r\n\t\t\t\t\t<td onclick=\"selectmenu(this, 'action/home')\" \r\n\t\t\t\t\t\tonmouseover=\"swapover(this)\" \r\n\t\t\t\t\t\tonmouseout=\"swapout(this)\" \t\t\t\t\t\t\r\n\t\t\t\t\t\tclass=\"");
          out.print( (userView.getCurrentContext().length() == 0 ? "hnavsel" : "hnav") );
          out.write("\"  \t\t\t\t\t\t\r\n\t\t\t\t\t\tstyle=\"width: 210px;\">\r\n\t\t\t\t\t\t");
          if (_jspx_meth_bean_message_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_logic_present_2, _jspx_page_context))
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
      _jspx_th_logic_notPresent_0.setParent(null);
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

  private boolean _jspx_meth_html_img_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:img
    org.apache.struts.taglib.html.ImgTag _jspx_th_html_img_0 = (org.apache.struts.taglib.html.ImgTag) _jspx_tagPool_html_img_srcKey_height_border_altKey_nobody.get(org.apache.struts.taglib.html.ImgTag.class);
    _jspx_th_html_img_0.setPageContext(_jspx_page_context);
    _jspx_th_html_img_0.setParent(null);
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

  private boolean _jspx_meth_bean_message_2(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_2 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_2.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
    _jspx_th_bean_message_2.setKey("global.home");
    int _jspx_eval_bean_message_2 = _jspx_th_bean_message_2.doStartTag();
    if (_jspx_th_bean_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_2);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_2);
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

  private boolean _jspx_meth_bean_message_3(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_3 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_3.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
    _jspx_th_bean_message_3.setKey("global.logout");
    int _jspx_eval_bean_message_3 = _jspx_th_bean_message_3.doStartTag();
    if (_jspx_th_bean_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_3);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_3);
    return false;
  }

  private boolean _jspx_meth_bean_message_4(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_4 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_4.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_2);
    _jspx_th_bean_message_4.setKey("hnav.standard");
    int _jspx_eval_bean_message_4 = _jspx_th_bean_message_4.doStartTag();
    if (_jspx_th_bean_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_4);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_4);
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
        if (_jspx_meth_bean_message_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_0, _jspx_page_context))
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

  private boolean _jspx_meth_bean_message_5(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_5 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_5.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_0);
    _jspx_th_bean_message_5.setKey("menu.changeprice");
    int _jspx_eval_bean_message_5 = _jspx_th_bean_message_5.doStartTag();
    if (_jspx_th_bean_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
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
        if (_jspx_meth_bean_message_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_1, _jspx_page_context))
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

  private boolean _jspx_meth_bean_message_6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_6 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_6.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_1);
    _jspx_th_bean_message_6.setKey("menu.alerts");
    int _jspx_eval_bean_message_6 = _jspx_th_bean_message_6.doStartTag();
    if (_jspx_th_bean_message_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_6);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_6);
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
        if (_jspx_meth_bean_message_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_2, _jspx_page_context))
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

  private boolean _jspx_meth_bean_message_7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_7 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_7.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_2);
    _jspx_th_bean_message_7.setKey("menu.dailyreports");
    int _jspx_eval_bean_message_7 = _jspx_th_bean_message_7.doStartTag();
    if (_jspx_th_bean_message_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_7);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_7);
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
        if (_jspx_meth_bean_message_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_3, _jspx_page_context))
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

  private boolean _jspx_meth_bean_message_8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_8 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_8.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_3);
    _jspx_th_bean_message_8.setKey("menu.details");
    int _jspx_eval_bean_message_8 = _jspx_th_bean_message_8.doStartTag();
    if (_jspx_th_bean_message_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_8);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_8);
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
        if (_jspx_meth_bean_message_9((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_4, _jspx_page_context))
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

  private boolean _jspx_meth_bean_message_9(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_9 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_9.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_4);
    _jspx_th_bean_message_9.setKey("menu.productassociation");
    int _jspx_eval_bean_message_9 = _jspx_th_bean_message_9.doStartTag();
    if (_jspx_th_bean_message_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_9);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_9);
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
        if (_jspx_meth_bean_message_10((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_5, _jspx_page_context))
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

  private boolean _jspx_meth_bean_message_10(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_10 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_10.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_5);
    _jspx_th_bean_message_10.setKey("menu.deleteassociation");
    int _jspx_eval_bean_message_10 = _jspx_th_bean_message_10.doStartTag();
    if (_jspx_th_bean_message_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_10);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_10);
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
        if (_jspx_meth_bean_message_11((javax.servlet.jsp.tagext.JspTag) _jspx_th_html_submit_6, _jspx_page_context))
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

  private boolean _jspx_meth_bean_message_11(javax.servlet.jsp.tagext.JspTag _jspx_th_html_submit_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_11 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_11.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_submit_6);
    _jspx_th_bean_message_11.setKey("menu.sticker");
    int _jspx_eval_bean_message_11 = _jspx_th_bean_message_11.doStartTag();
    if (_jspx_th_bean_message_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_11);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_11);
    return false;
  }
}
