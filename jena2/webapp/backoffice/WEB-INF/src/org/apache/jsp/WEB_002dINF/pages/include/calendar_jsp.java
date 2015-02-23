package org.apache.jsp.WEB_002dINF.pages.include;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class calendar_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/plain");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n\r\n<style>\r\n\r\ntable.calendar {\r\n\tfont-family: Arial;\r\n\tfont-size: 12px;\r\n\tbackground: #ffffff;\r\n\tborder: 1px solid #ffffff;\r\n\tcursor: default;\r\n}\r\n\r\ntd.calendar {\r\n\tbackground: #ffffff;\r\n\tborder: 1px solid #ffffff;\r\n}\r\n\r\ntd.monthname {\r\n\ttext-align: center;\r\n\tfont-weight: bolder;\r\n\tcolor: #0000AA;\r\n}\r\n\r\ntd.dayname {\r\n\ttext-align: right;\r\n\theight: 25px;\r\n}\r\n\r\ntr.calendarheader {\r\n\tbackground: #ffffff;\r\n\theight: 25px;\r\n}\r\n\r\ntr.calendarfooter {\r\n\tbackground: #ffffff;\r\n\theight: 30px;\r\n}\r\n\r\ntd.cout {\r\n\tbackground: #ffffff;\r\n\tborder: 1px solid #ffffff;\r\n\tcolor: #000000;\r\n\twidth: 25px; \r\n}\r\n\r\ntd.cover {\r\n\tbackground: #e0e0ff;\r\n\tborder: 1px solid #9090ff;\r\n\tcolor: #9090ff;\r\n\twidth: 25px;\r\n}\r\n\r\ntd.current {\r\n\tbackground: #d0d0ff;\r\n\tborder: 1px solid #303088;\r\n\tcolor: #505088;\r\n\twidth: 25px;\r\n}\r\n\r\na.arrow:link\r\n{\r\n\ttext-decoration: none;\r\n\tcolor: #000000;\r\n}\r\n\r\na.arrow:hover\r\n{\r\n\tcolor: #8080ff;\r\n}\r\n\r\na.arrow:visited\r\n{\r\n\tcolor: #000000;\r\n}\r\n\r\n.time {\r\n\tcolor: #0000AA;\r\n\tborder: 0px #ffffff solid;\r\n\twidth: 20px;\r\n\ttext-align: center;\r\n");
      out.write("}\r\n\r\n</style>\r\n\r\n<script>\r\n\r\nvar selectedDate = new Date();\r\n\r\nvar month_desc = new Array (\r\n\t'");
      if (_jspx_meth_bean_message_0(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_1(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_2(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_3(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_4(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_5(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_6(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_7(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_8(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_9(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_10(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_11(_jspx_page_context))
        return;
      out.write("'\r\n);\r\n\r\nvar week_desc = new Array (\r\n\t'");
      if (_jspx_meth_bean_message_12(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_13(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_14(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_15(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_16(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_17(_jspx_page_context))
        return;
      out.write("',\r\n\t'");
      if (_jspx_meth_bean_message_18(_jspx_page_context))
        return;
      out.write("'\r\n);\r\n\r\nfunction dayToPos(w)\r\n{\r\n\treturn (w > 0 ? w-- : 6);\r\n}\r\n\r\nfunction dayCount(month, year)\r\n{\r\n\tswitch(month)\r\n\t{\r\n\t\tcase 3:\r\n\t\tcase 5:\r\n\t\tcase 8:\r\n\t\tcase 10:\r\n\t\t\treturn 30;\r\n\t\t\tbreak;\r\n\t\tcase 1:\r\n\t\t\treturn (year % 4 == 0 ? 29 : 28);\r\n\t\t\tbreak;\r\n\t\tdefault:\r\n\t\t\treturn 31;\r\n\t\t\tbreak;\r\n\t}\r\n}\r\n\r\nfunction moveLeft()\r\n{\r\n\tvar year = selectedDate.getFullYear();\r\n\tvar month = selectedDate.getMonth();\r\n\tmonth --;\r\n\tif (month < 0) {\r\n\t\tmonth = 11;\r\n\t\tyear --;\r\n\t}\r\n\tselectedDate = new Date(year, month, 1, selectedDate.getHours(), selectedDate.getMinutes(), 0);\r\n\tcalendar_init();\r\n}\r\n\r\nfunction moveRight()\r\n{\r\n\tvar year = selectedDate.getFullYear();\r\n\tvar month = selectedDate.getMonth();\r\n\tmonth ++;\r\n\tif (month > 11) {\r\n\t\tmonth = 0;\r\n\t\tyear ++;\r\n\t}\r\n\tselectedDate = new Date(year, month, 1, selectedDate.getHours(), selectedDate.getMinutes(), 0);\r\n\tcalendar_init();\r\n}\r\n\r\nfunction shiftime(action) \r\n{\r\n\tvar h = parseInt(document.getElementById(\"hours\").value);\r\n\tvar m = parseInt(document.getElementById(\"minutes\").value);\r\n");
      out.write("\t\r\n\tswitch (action)\r\n\t{\r\n\t\tcase 1:\r\n\t\t\th ++;\r\n\t\t\tif (h > 23) h = 0;\r\n\t\t\tbreak;\r\n\t\tcase 2:\r\n\t\t\th --;\r\n\t\t\tif (h < 0) h = 23;\r\n\t\t\tbreak;\r\n\t\tcase 3:\r\n\t\t\tm += 10;\r\n\t\t\tif (m > 59) m = 0;\t\r\n\t\t\tbreak;\r\n\t\tcase 4:\r\n\t\t\tm -= 10;\r\n\t\t\tif (m < 0) m = 50;\t\t\t\r\n\t\t\tbreak;\t\t\t\r\n\t}\r\n\t\r\n\tif (h < 10) h = '0' + h;\r\n\tif (m < 10) m = '0' + m;\r\n\t\r\n\tdocument.getElementById(\"hours\").value = h;\r\n\tdocument.getElementById(\"minutes\").value = m;\r\n\t\r\n}\r\n\r\nfunction cdeout(o)\r\n{\r\n\tif (o.innerHTML == '&nbsp;') return;\r\n\tif (o.className == 'current') return;\r\n\to.className = 'cout';\r\n}\r\n\r\nfunction cdeover(o)\r\n{\r\n\tif (o.innerHTML == '&nbsp;') return;\r\n\tif (o.className == 'current') return;\r\n\to.className = 'cover';\r\n}\r\n\r\nfunction formatDate(date)\r\n{\r\n\tvar y = date.getFullYear();\r\n\t\t\t\r\n\tvar m = date.getMonth();\r\n\tm ++;\r\n\tif (m < 10) m = '0' + m;\r\n\t\r\n\tvar d = date.getDate();\r\n\tif (d < 10) d = '0' + d;\r\n\t\r\n\tvar hh = date.getHours();\r\n\tif (hh < 10) hh = '0' + hh;\r\n\t\r\n\tvar mm = date.getMinutes();\r\n\tif (mm < 10) mm = '0' + mm;\r\n\t\r\n\t//var ss = date.getSeconds();\r\n");
      out.write("\t//if (ss < 10) ss = '0' + ss;\r\n\t\r\n\tss = '00';\r\n\t\r\n\treturn(y + '-' + m + '-' + d + 'T' + hh + ':' + mm + ':' + ss);\r\n}\r\n\r\nfunction doclick(o)\r\n{\r\n\tif (o.innerHTML == '&nbsp;') return;\r\n\tif (o.className == 'current') return;\r\n\t\r\n\tselectedDate.setDate(parseInt(o.innerHTML));\r\n\t\r\n\t//TBD\r\n\talert('WARNING: undefined calendar target control - selected date: ' + formatDate(selectedDate));\r\n\t\r\n}\r\n\r\nfunction calendar_init(target) \r\n{\r\n\tvar html = '';\r\n\thtml += '<table class=\"calendar\" cellspacing=\"0\" cellpadding=\"1\" id=\"calendar\">';\r\n\thtml += '\t<tr class=\"calendarheader\">';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\"><a class=\"arrow\" href=\"javascript:moveLeft();\">&#9668;</a></td>';\r\n\thtml += '\t\t<td align=\"center\" valign=\"middle\" colspan=\"5\" class=\"monthname\">';\r\n\thtml += '\t\t\t<span id=\"monthname\"></span>&nbsp;<span id=\"year\"></span>';\r\n\thtml += '\t\t</td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\"><a class=\"arrow\" href=\"javascript:moveRight();\">&#9658;</a></td>';\r\n\thtml += '\t</tr>';\r\n\thtml += '\t<tr>';\r\n");
      out.write("\thtml += '\t\t<td class=\"dayname\"></td>';\r\n\thtml += '\t\t<td class=\"dayname\"></td>';\r\n\thtml += '\t\t<td class=\"dayname\"></td>';\r\n\thtml += '\t\t<td class=\"dayname\"></td>';\r\n\thtml += '\t\t<td class=\"dayname\"></td>';\r\n\thtml += '\t\t<td class=\"dayname\"></td>';\r\n\thtml += '\t\t<td class=\"dayname\"></td>';\r\n\thtml += '\t</tr>';\r\n\thtml += '\t<tr>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n");
      out.write("\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t</tr>';\r\n\thtml += '\t<tr>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n");
      out.write("\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t</tr>';\r\n\thtml += '\t<tr>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n");
      out.write("\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t</tr>';\r\n\thtml += '\t<tr>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n");
      out.write("\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t</tr>';\r\n\thtml += '\t<tr>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n");
      out.write("\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\" onclick=\"doclick(this)\" onmouseover=\"cdeover(this)\" onmouseout=\"cdeout(this)\"></td>';\r\n\thtml += '\t</tr>';\r\n\thtml += '\t<tr class=\"calendarfooter\">';\r\n\thtml += '\t\t<td align=\"right\" valign=\"middle\" class=\"cout\"></td>';\r\n\thtml += '\t\t<td align=\"center\" valign=\"middle\" class=\"cout\" colspan=\"5\">';\r\n\thtml += '\t\t\t<a class=\"arrow\" href=\"javascript:shiftime(1)\">&#x25B2;</a>';\r\n\thtml += '\t\t\t<a class=\"arrow\" href=\"javascript:shiftime(2)\">&#x25BC;</a>';\r\n\thtml += '\t\t\t<input type=\"text\" maxlength=\"2\" id=\"hours\" class=\"time\" value=\"00\" readonly=\"true\"/>:<input type=\"text\" maxlength=\"2\" id=\"minutes\" class=\"time\" value=\"00\"  readonly=\"true\"/>';\r\n\thtml += '\t\t\t<a class=\"arrow\" href=\"javascript:shiftime(3)\">&#x25B2;</a>';\r\n\thtml += '\t\t\t<a class=\"arrow\" href=\"javascript:shiftime(4)\">&#x25BC;</a>';\r\n\thtml += '\t\t</td>';\r\n\thtml += '\t\t<td align=\"left\" valign=\"middle\" class=\"cout\"></td>';\r\n");
      out.write("\thtml += '\t</tr>';\r\n\thtml += '</table>';\r\n\t\r\n\tdocument.getElementById(target).innerHTML = html;\t\r\n\t\r\n\t//\r\n\t// load calendar data\r\n\t//\r\n\t\r\n\tvar d  = selectedDate;\r\n\tvar dow = d.getDay();\r\n\tvar day = d.getDate();\r\n\tvar month = d.getMonth();\r\n\tvar year = d.getFullYear();\r\n\t\r\n\tdocument.getElementById(\"monthname\").innerHTML = month_desc[month];\r\n\tdocument.getElementById(\"year\").innerHTML = year;\r\n\t\r\n\tvar h = d.getHours();\r\n\tif (h < 10) h = '0' + h;\r\n\tvar m = d.getMinutes();\r\n\twhile (m % 10 != 0) m --;\r\n\tif (m < 10) m = '0' + m;\r\n\t\r\n\tdocument.getElementById(\"hours\").value = h;\r\n\tdocument.getElementById(\"minutes\").value = m;\r\n\t\r\n\tvar tb = document.getElementById(\"calendar\").children[0];\r\n\t\r\n\t//popola le descrizioni del giorno della settimana\r\n\tfor (var j = 0; j < 7; j ++)\r\n\t{\r\n\t\ttb.children[1].children[j].innerHTML = week_desc[j];\r\n\t}\r\n\t\r\n\t//cerca che giorno della settimana e' il primo del mese \r\n\tvar w = dow;\r\n\tfor (var i = day; i > 0; i --) {\r\n\t\tw --;\r\n\t\tif (w == -1) w = 6;\r\n\t}\r\n\t\r\n\tvar n = 0;\r\n\tvar vpos = dayToPos(w);\r\n");
      out.write("\r\n\tvar day_limit = dayCount(month, year);\r\n\t\t\r\n\t//popola tabella con i giorni\r\n\tfor (var i = 0; i < 5; i ++)\r\n\t{\r\n\t\tfor (var j = 0; j < 7; j ++)\r\n\t\t{\r\n\t\t\ttb.children[2 + i].children[j].className = 'cout';\r\n\t\t\t\r\n\t\t\tif (n == 0 && j == vpos) n = 1;  \r\n\t\t\t\r\n\t\t\tif (n > 0 && n <= day_limit) {\r\n\t\t\t\ttb.children[2 + i].children[j].innerHTML = n;\t\r\n\t\t\t\tif (n == day) {\r\n\t\t\t\t\ttb.children[2 + i].children[j].className = 'current';\r\n\t\t\t\t}\r\n\t\t\t\tn ++;\r\n\t\t\t}\t\r\n\t\t\telse {\r\n\t\t\t\ttb.children[2 + i].children[j].innerHTML = '&nbsp;';\r\n\t\t\t}\t\t\t\r\n\t\t}\r\n\t}\r\n\t\r\n}\r\n\r\n</script>\r\n\r\n");
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
    _jspx_th_bean_message_0.setKey("calendar.jan");
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
    _jspx_th_bean_message_1.setKey("calendar.feb");
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
    _jspx_th_bean_message_2.setKey("calendar.mar");
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
    _jspx_th_bean_message_3.setKey("calendar.apr");
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
    _jspx_th_bean_message_4.setKey("calendar.may");
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
    _jspx_th_bean_message_5.setKey("calendar.jun");
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
    _jspx_th_bean_message_6.setKey("calendar.jul");
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
    _jspx_th_bean_message_7.setKey("calendar.aug");
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
    _jspx_th_bean_message_8.setKey("calendar.sep");
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
    _jspx_th_bean_message_9.setKey("calendar.oct");
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
    _jspx_th_bean_message_10.setKey("calendar.nov");
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
    _jspx_th_bean_message_11.setKey("calendar.dec");
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
    _jspx_th_bean_message_12.setKey("calendar.mo");
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
    _jspx_th_bean_message_13.setKey("calendar.tu");
    int _jspx_eval_bean_message_13 = _jspx_th_bean_message_13.doStartTag();
    if (_jspx_th_bean_message_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_13);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_13);
    return false;
  }

  private boolean _jspx_meth_bean_message_14(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_14 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_14.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_14.setParent(null);
    _jspx_th_bean_message_14.setKey("calendar.we");
    int _jspx_eval_bean_message_14 = _jspx_th_bean_message_14.doStartTag();
    if (_jspx_th_bean_message_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_14);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_14);
    return false;
  }

  private boolean _jspx_meth_bean_message_15(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_15 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_15.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_15.setParent(null);
    _jspx_th_bean_message_15.setKey("calendar.th");
    int _jspx_eval_bean_message_15 = _jspx_th_bean_message_15.doStartTag();
    if (_jspx_th_bean_message_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_15);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_15);
    return false;
  }

  private boolean _jspx_meth_bean_message_16(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_16 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_16.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_16.setParent(null);
    _jspx_th_bean_message_16.setKey("calendar.fr");
    int _jspx_eval_bean_message_16 = _jspx_th_bean_message_16.doStartTag();
    if (_jspx_th_bean_message_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_16);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_16);
    return false;
  }

  private boolean _jspx_meth_bean_message_17(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_17 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_17.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_17.setParent(null);
    _jspx_th_bean_message_17.setKey("calendar.sa");
    int _jspx_eval_bean_message_17 = _jspx_th_bean_message_17.doStartTag();
    if (_jspx_th_bean_message_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_17);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_17);
    return false;
  }

  private boolean _jspx_meth_bean_message_18(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.struts.taglib.bean.MessageTag _jspx_th_bean_message_18 = (org.apache.struts.taglib.bean.MessageTag) _jspx_tagPool_bean_message_key_nobody.get(org.apache.struts.taglib.bean.MessageTag.class);
    _jspx_th_bean_message_18.setPageContext(_jspx_page_context);
    _jspx_th_bean_message_18.setParent(null);
    _jspx_th_bean_message_18.setKey("calendar.su");
    int _jspx_eval_bean_message_18 = _jspx_th_bean_message_18.doStartTag();
    if (_jspx_th_bean_message_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_18);
      return true;
    }
    _jspx_tagPool_bean_message_key_nobody.reuse(_jspx_th_bean_message_18);
    return false;
  }
}
