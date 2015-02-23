package org.apache.jsp.xslt;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class alerts_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_message_key_nobody;

  private org.apache.jasper.runtime.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_bean_message_key_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_bean_message_key_nobody.release();
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
      response.setContentType("text/xml");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n<xsl:template match=\"/\">\r\n\r\n<xsl:choose>\r\n<xsl:when test=\"count(list//Alerts) > 0\">\r\n\r\n\t<table cellspacing=\"0\" cellpadding=\"0\" class=\"square\" style=\"width: 100%; font-size: 12px;\">\r\n   \r\n\t\t<tr class=\"header\" style=\"font-weight: bolder; height: 32px;\">\t\t\r\n\t\t\t<td style=\"width: 30px;\"><img style=\"width: 32px; height: 32px; opacity: 0.0;\" src=\"images/expand.png\"></img></td>\r\n\t\t\t<td align=\"center\" style=\"width: 30px;\">");
      if (_jspx_meth_bean_message_0(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td>\r\n\t\t\t\t");
      if (_jspx_meth_bean_message_1(_jspx_page_context))
        return;
      out.write("\t\t\t\t\r\n\t\t\t</td>\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_2(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_3(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t</tr>\r\n\t\t\r\n\t\t<xsl:for-each select=\"list/Alerts\">\t\r\n\t    <tr valign=\"middle\">\t\t\t\r\n\t\t\t<xsl:attribute name=\"class\">\r\n\t\t\t\t<xsl:choose>\r\n\t\t\t\t\t<xsl:when test=\"(position() mod 2) != 1\">\r\n\t\t\t\t\t\t<xsl:text>even</xsl:text>\r\n\t\t\t\t\t</xsl:when>\r\n\t\t\t\t\t<xsl:otherwise>\r\n\t\t\t\t\t\t<xsl:text>odd</xsl:text>\r\n\t\t\t\t\t</xsl:otherwise>\r\n\t\t\t\t</xsl:choose>\r\n\t\t\t</xsl:attribute>\t\t\t\r\n\t\t\t<td class=\"header\" onclick=\"showmesg(this)\" style=\"cursor: pointer; width: 50px;\">\t\t\t\t\r\n\t\t\t\t<xsl:attribute name=\"id\">\r\n\t\t\t\t\t<xsl:value-of select=\"id\" />\t\t\t\r\n\t\t\t\t</xsl:attribute>\t\t\t\r\n\t\t\t\t<img class=\"trans\" style=\"width: 32px; height: 32px;\" src=\"images/expand.png\"></img>\r\n\t\t\t</td>\r\n\t\t\t<td align=\"center\" style=\"width: 30px: font-weight: bolder;\">\r\n\t\t\t\t<xsl:value-of select=\"position()\"/>\t\t\t\t\r\n\t\t\t</td>\r\n\t\t\t<td>\r\n\t\t\t\t<xsl:value-of select=\"alertType\" />\t\t\t\r\n\t\t\t</td>\t\t\t  \r\n\t\t\t<td>\r\n\t\t\t\t<xsl:value-of select=\"title\" />\t\t\t\r\n\t\t\t</td>\t\t\t  \t\t\t\r\n\t\t\t<td>\r\n\t\t\t\t<xsl:value-of select=\"lastUpdate\" />\r\n\t\t\t</td>\t\t\t\t\r\n\t    </tr>\r\n\t\t<tr>\r\n\t\t\t<xsl:attribute name=\"class\">\r\n\t\t\t\t<xsl:choose>\r\n");
      out.write("\t\t\t\t\t<xsl:when test=\"(position() mod 2) != 1\">\r\n\t\t\t\t\t\t<xsl:text>even</xsl:text>\r\n\t\t\t\t\t</xsl:when>\r\n\t\t\t\t\t<xsl:otherwise>\r\n\t\t\t\t\t\t<xsl:text>odd</xsl:text>\r\n\t\t\t\t\t</xsl:otherwise>\r\n\t\t\t\t</xsl:choose>\r\n\t\t\t</xsl:attribute>\r\n\t\t\t<td class=\"header\"></td>\r\n\t\t\t<td></td>\r\n\t\t\t<td colspan=\"3\" style=\"padding-bottom: 15px;\">\r\n\t\t\t\t<div style=\"display: none;\">\r\n\t\t\t\t\t<xsl:attribute name=\"id\">\r\n\t\t\t\t\t\t<xsl:value-of select=\"concat('div', id)\" />\r\n\t\t\t\t\t</xsl:attribute>\r\n\t\t\t\t\t!load\r\n\t\t\t\t</div>\r\n\t\t\t</td>\r\n\t\t</tr>\r\n\t\t</xsl:for-each>\r\n\t\r\n\t\t<tr class=\"header\" style=\"font-weight: bolder; height: 32px;\">\r\n\t\t\t<td style=\"width: 30px;\"><img style=\"width: 32px; height: 32px; opacity: 0.0;\" src=\"images/expand.png\"></img></td>\r\n\t\t\t<td colspan=\"4\">\r\n\t\t\t\t");
      if (_jspx_meth_bean_message_4(_jspx_page_context))
        return;
      out.write(" <b><xsl:value-of select=\"count(list//Alerts)\" /></b> \t\t\t\r\n\t\t\t</td>\t   \t\r\n\t\t</tr>\r\n\t\r\n\t</table>  \r\n\t\t\r\n</xsl:when>\r\n<xsl:otherwise>\t\r\n\r\n\t<b>");
      if (_jspx_meth_bean_message_5(_jspx_page_context))
        return;
      out.write("</b>\r\n\t\r\n</xsl:otherwise>\r\n</xsl:choose>\r\n  \r\n</xsl:template>\r\n</xsl:stylesheet>\r\n");
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

  private boolean _jspx_meth_bean_message_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_0 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_0.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_0.setParent(null);
    _jspx_th_bean_message_0.setKey("xslt.nr");
    int _jspx_eval_bean_message_0 = _jspx_th_bean_message_0.doStartTag();
    if (_jspx_th_bean_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_0);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_0);
    return false;
  }

  private boolean _jspx_meth_bean_message_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_1 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_1.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_1.setParent(null);
    _jspx_th_bean_message_1.setKey("xslt.alerts.alertType");
    int _jspx_eval_bean_message_1 = _jspx_th_bean_message_1.doStartTag();
    if (_jspx_th_bean_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_1);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_1);
    return false;
  }

  private boolean _jspx_meth_bean_message_2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_2 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_2.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_2.setParent(null);
    _jspx_th_bean_message_2.setKey("xslt.alerts.title");
    int _jspx_eval_bean_message_2 = _jspx_th_bean_message_2.doStartTag();
    if (_jspx_th_bean_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_2);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_2);
    return false;
  }

  private boolean _jspx_meth_bean_message_3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_3 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_3.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_3.setParent(null);
    _jspx_th_bean_message_3.setKey("xslt.alerts.lastUpdate");
    int _jspx_eval_bean_message_3 = _jspx_th_bean_message_3.doStartTag();
    if (_jspx_th_bean_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_3);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_3);
    return false;
  }

  private boolean _jspx_meth_bean_message_4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_4 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_4.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_4.setParent(null);
    _jspx_th_bean_message_4.setKey("xslt.total");
    int _jspx_eval_bean_message_4 = _jspx_th_bean_message_4.doStartTag();
    if (_jspx_th_bean_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_4);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_4);
    return false;
  }

  private boolean _jspx_meth_bean_message_5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_5 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_5.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_5.setParent(null);
    _jspx_th_bean_message_5.setKey("xslt.alerts.noupdates");
    int _jspx_eval_bean_message_5 = _jspx_th_bean_message_5.doStartTag();
    if (_jspx_th_bean_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
    return false;
  }
}
