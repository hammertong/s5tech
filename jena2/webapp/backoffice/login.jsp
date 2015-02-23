<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="<%= IConstants.USER_VIEW_KEY %>">  
  <logic:redirect href="action/home"/>    
</logic:present>
<!DOCTYPE html>
<html:html>
<head>  
  <base href="<%= request.getContextPath() %>/">
  <title><bean:message key="login.title"/></title>
  <link rel="stylesheet" href="stylesheets/login_style_ie.css" type="text/css">
  <!-- Favicons ================================================== -->
  <link rel="shortcut icon" href="images/favicon.png">
  <link rel="apple-touch-icon" href="images/apple-touch-icon.png">
  <link rel="apple-touch-icon" sizes="72x72" href="images/apple-touch-icon-72x72.png">
  <link rel="apple-touch-icon" sizes="114x114" href="images/apple-touch-icon-114x114.png">  
</head>

<body>

<%@include file="WEB-INF/pages/include/header.jsp"%>

<br><br><center>

<html:form action="login" focus="accessNumber">

<table class="rounded filled" style="width: 300px; padding: 10px;">
  <tr>
    <td colspan="2">
      &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="2" class="error">
      <html:errors/>
    </td>
  </tr>
  <tr>
    <td><bean:message key="login.accessnumber"/></td>
    <td><html:text property="accessNumber" size="14" maxlength="32"/>
  </tr>
  <tr>    
  <td><bean:message key="login.pinnumber"/></td>
  <td><html:password property="pinNumber" size="14" maxlength="32"/></td>
  </tr>
  <tr>
    <td colspan="2" align="center">
      <br>
      <html:submit>      
        <bean:message key="login.button"/>
      </html:submit>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      &nbsp;
    </td>
  </tr>    
</table>

</html:form>

</center><br><br>

<%@include file="WEB-INF/pages/include/footer.jsp"%>

</body>

</html:html>
