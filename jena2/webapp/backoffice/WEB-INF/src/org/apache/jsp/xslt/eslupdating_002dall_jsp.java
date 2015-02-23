package org.apache.jsp.xslt;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class eslupdating_002dall_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n<xsl:template match=\"/\">\r\n\r\n<xsl:choose>\r\n<xsl:when test=\"count(list//EslUpdates) > 0\">\r\n\r\n\t<table cellspacing=\"0\" cellpadding=\"5\" class=\"square\" width=\"100%\">\r\n\t\r\n\t\t<tr class=\"header\">\t\t\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_0(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_1(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td colspan=\"2\">");
      if (_jspx_meth_bean_message_2(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_3(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_4(_jspx_page_context))
        return;
      out.write("</td>\t\t\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_5(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_6(_jspx_page_context))
        return;
      out.write("</td>\t\t\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_7(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t\t<td>");
      if (_jspx_meth_bean_message_8(_jspx_page_context))
        return;
      out.write("</td>\r\n\t\t</tr>\r\n\t\t\r\n\t\t<xsl:for-each select=\"list/EslUpdates\">\t\r\n\t    <tr>\r\n\t\t\t<xsl:attribute name=\"class\">\r\n\t\t\t\t<xsl:choose>\r\n\t\t\t\t\t<xsl:when test=\"(position() mod 2) != 1\">\r\n\t\t\t\t\t\t<xsl:text>even</xsl:text>\r\n\t\t\t\t\t</xsl:when>\r\n\t\t\t\t\t<xsl:otherwise>\r\n\t\t\t\t\t\t<xsl:text>odd</xsl:text>\r\n\t\t\t\t\t</xsl:otherwise>\r\n\t\t\t\t</xsl:choose>\r\n\t\t\t</xsl:attribute>\t\r\n\t\t\t<td>\r\n\t\t\t\t<b>\r\n\t\t\t\t\t<xsl:value-of select=\"position()\"/>\r\n\t\t\t\t</b>\r\n\t\t\t</td>\r\n\t\t\t<td>\r\n\t\t\t\t<xsl:value-of select=\"esl\" />\t\t\t\r\n\t\t\t</td>\t\t\t  \r\n\t\t\t<td>\r\n\t\t\t\t<xsl:choose>\r\n\t\t\t\t\t<xsl:when test=\"acknoledgeDate != ''\">\r\n\t\t\t\t\t\t<img src=\"images/led-green.png\" class=\"led\"></img>\t\t\t\t\t\t\r\n\t\t\t\t\t</xsl:when>\r\n\t\t\t\t\t<xsl:otherwise>\t  \r\n\t\t\t\t\t\t<img src=\"images/led-red.png\" class=\"led\"></img>\r\n\t\t\t\t\t</xsl:otherwise>\r\n\t\t\t\t</xsl:choose>\t\t\t\t\r\n\t\t\t</td>\t\t\t\t\r\n\t\t\t<td>\r\n\t\t\t\t<xsl:choose>\r\n\t\t\t\t\t<xsl:when test=\"acknoledgeDate != ''\">\r\n\t\t\t\t\t\t");
      if (_jspx_meth_bean_message_9(_jspx_page_context))
        return;
      out.write("\r\n\t\t\t\t\t</xsl:when>\r\n\t\t\t\t\t<xsl:otherwise>\t  \r\n\t\t\t\t\t\t");
      if (_jspx_meth_bean_message_10(_jspx_page_context))
        return;
      out.write("\r\n\t\t\t\t\t</xsl:otherwise>\r\n\t\t\t\t</xsl:choose>\t\r\n\t\t\t</td>\t\r\n\t\t\t<td>\r\n\t\t\t\t<input type=\"text\" style=\"background: transparent; border: 0px; width: 100%; font-size: 10px;\">\r\n\t\t\t\t\t<xsl:attribute name=\"value\">\r\n\t\t\t\t\t\t<xsl:value-of select=\"publishingDate\" />\t\t\t\r\n\t\t\t\t\t</xsl:attribute>\r\n\t\t\t\t</input>\t\t\t\t\r\n\t\t\t</td>\t\t\t  \r\n\t\t\t<td>\r\n\t\t\t\t<xsl:value-of select=\"sentPrice\" />\t\t\t\r\n\t\t\t</td>\t\r\n\t\t\t<td>\r\n\t\t\t\t<xsl:value-of select=\"sentPricePerUnit\" />\t\t\t\r\n\t\t\t</td>\t\r\n\t\t\t<td>\r\n\t\t\t\t<xsl:value-of select=\"product\" />\t\t\t\r\n\t\t\t</td>\t\r\n\t\t\t<td>\t\t\t\r\n\t\t\t\t<input type=\"text\" style=\"background: transparent; border: 0px; width: 100%;\">\t\t\t\t\t\r\n\t\t\t\t\t<xsl:attribute name=\"value\">\r\n\t\t\t\t\t\t<xsl:value-of select=\"productDescription\" />\t\t\t\t\t\t\t\t\r\n\t\t\t\t\t</xsl:attribute>\r\n\t\t\t\t</input>\r\n\t\t\t</td>\t\r\n\t\t\t<td>\t\t\t\t\r\n\t\t\t\t<xsl:choose>\r\n\t\t\t\t\t<xsl:when test=\"starts-with(updateUser, 'HSID:')\">\r\n\t\t\t\t\t\t<!--\r\n\t\t\t\t\t\t");
      if (_jspx_meth_bean_message_11(_jspx_page_context))
        return;
      out.write("\r\n\t\t\t\t\t\t-->\r\n\t\t\t\t\t\t<xsl:value-of select=\"substring(updateUser, 6)\" /> <i>(HTTP)</i>\r\n\t\t\t\t\t</xsl:when>\r\n\t\t\t\t\t<xsl:otherwise>\t  \r\n\t\t\t\t\t\t<xsl:value-of select=\"updateUser\" />\r\n\t\t\t\t\t</xsl:otherwise>\r\n\t\t\t\t</xsl:choose>\t\r\n\t\t\t</td>\t\r\n\t    </tr>\r\n\t\t</xsl:for-each>\r\n\t\t\r\n\t\t<tr class=\"header\">\r\n\t\t\t<td colspan=\"10\">\r\n\t\t\t\t");
      if (_jspx_meth_bean_message_12(_jspx_page_context))
        return;
      out.write(" <b><xsl:value-of select=\"count(list//EslUpdates)\" /></b> \t\t\t\r\n\t\t\t</td>\t   \t\r\n\t\t</tr>\r\n\t\t\r\n\t</table>\r\n\t\t\r\n</xsl:when>\r\n<xsl:otherwise>\t\t\r\n\t<b>");
      if (_jspx_meth_bean_message_13(_jspx_page_context))
        return;
      out.write("</b>\r\n</xsl:otherwise>\r\n</xsl:choose>\r\n\r\n</xsl:template>\r\n</xsl:stylesheet>\r\n");
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
    _jspx_th_bean_message_1.setKey("xslt.eslupdating.mac");
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
    _jspx_th_bean_message_2.setKey("xslt.eslupdating.status");
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
    _jspx_th_bean_message_3.setKey("xslt.eslupdating.pubtime");
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
    _jspx_th_bean_message_4.setKey("xslt.eslupdating.sentprice");
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
    _jspx_th_bean_message_5.setKey("xslt.eslupdating.sentppu");
    int _jspx_eval_bean_message_5 = _jspx_th_bean_message_5.doStartTag();
    if (_jspx_th_bean_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_5);
    return false;
  }

  private boolean _jspx_meth_bean_message_6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_6 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_6.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_6.setParent(null);
    _jspx_th_bean_message_6.setKey("xslt.eslupdating.code");
    int _jspx_eval_bean_message_6 = _jspx_th_bean_message_6.doStartTag();
    if (_jspx_th_bean_message_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_6);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_6);
    return false;
  }

  private boolean _jspx_meth_bean_message_7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_7 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_7.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_7.setParent(null);
    _jspx_th_bean_message_7.setKey("xslt.eslupdating.desc");
    int _jspx_eval_bean_message_7 = _jspx_th_bean_message_7.doStartTag();
    if (_jspx_th_bean_message_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_7);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_7);
    return false;
  }

  private boolean _jspx_meth_bean_message_8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_8 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_8.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_8.setParent(null);
    _jspx_th_bean_message_8.setKey("xslt.eslupdating.user");
    int _jspx_eval_bean_message_8 = _jspx_th_bean_message_8.doStartTag();
    if (_jspx_th_bean_message_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_8);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_8);
    return false;
  }

  private boolean _jspx_meth_bean_message_9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_9 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_9.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_9.setParent(null);
    _jspx_th_bean_message_9.setKey("xslt.eslupdating.received");
    int _jspx_eval_bean_message_9 = _jspx_th_bean_message_9.doStartTag();
    if (_jspx_th_bean_message_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_9);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_9);
    return false;
  }

  private boolean _jspx_meth_bean_message_10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_10 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_10.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_10.setParent(null);
    _jspx_th_bean_message_10.setKey("xslt.eslupdating.waiting");
    int _jspx_eval_bean_message_10 = _jspx_th_bean_message_10.doStartTag();
    if (_jspx_th_bean_message_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_10);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_10);
    return false;
  }

  private boolean _jspx_meth_bean_message_11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_11 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_11.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_11.setParent(null);
    _jspx_th_bean_message_11.setKey("xslt.eslupdating.user.web");
    int _jspx_eval_bean_message_11 = _jspx_th_bean_message_11.doStartTag();
    if (_jspx_th_bean_message_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_11);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_11);
    return false;
  }

  private boolean _jspx_meth_bean_message_12(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_12 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_12.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_12.setParent(null);
    _jspx_th_bean_message_12.setKey("xslt.total");
    int _jspx_eval_bean_message_12 = _jspx_th_bean_message_12.doStartTag();
    if (_jspx_th_bean_message_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_12);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_12);
    return false;
  }

  private boolean _jspx_meth_bean_message_13(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_13 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_13.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_13.setParent(null);
    _jspx_th_bean_message_13.setKey("xslt.eslupdating.noupdates");
    int _jspx_eval_bean_message_13 = _jspx_th_bean_message_13.doStartTag();
    if (_jspx_th_bean_message_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_13);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_13);
    return false;
  }
}
