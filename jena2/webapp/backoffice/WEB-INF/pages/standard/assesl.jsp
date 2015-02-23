<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html:html>
<head>
  <base href="<%= request.getContextPath() %>/">
  <title><bean:message key="assesl.title"/></title>
  <link rel="stylesheet" href="stylesheets/login_style_ie.css" type="text/css">    
</head>
<body>

<%@include file="../include/header.jsp"%>

<html:form action="assesl" focus="product">

<h1><bean:message key="assesl.title"/></h1>

<table border="0" cellspacing="0" cellpadding="5" width="300px;">

	<tr>
  		<td>
			<span style="font-weight: normal;">
				<bean:message key="assesl.product"/>
			</span>			
		</td>
		<td align="right">
			<html:text property="product" style="width: 120px; height: 16px;"/>						
		</td>
	</tr>
	
	<tr>
  		<td>
			<span style="font-weight: normal;">
				<bean:message key="assesl.esl"/>
			</span>			
		</td>
		<td align="right">
			<html:text property="esl" style="width: 120px; height: 16px;"/>			
		</td>
	</tr>
	
	<tr>
		<td class="error" colspan="2">
			<html:errors/>
		</td>
	</tr>	
	
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>    
	
	<tr>
  		<td>&nbsp;</td>
		<td align="right">
			<html:submit style="width: 120px;">      
				<bean:message key="assesl.associate"/>
			</html:submit>
		</td>		
	</tr>
	
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>    
	
</table>

</html:form>

<%@include file="../include/footer.jsp"%>

</body>

</html:html>
