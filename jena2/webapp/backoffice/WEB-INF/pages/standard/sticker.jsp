<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html:html>
<head>
  <base href="<%= request.getContextPath() %>/">
  <title><bean:message key="sticker.title"/></title>
  <link rel="stylesheet" href="stylesheets/login_style_ie.css" type="text/css">    
</head>

<body>

<%@include file="../include/header.jsp"%>

<html:form action="sticker">

<h1><bean:message key="sticker.title"/></h1>

<table border="0" cellspacing="0" cellpadding="5" width="400px;">
	
	<tr>
		<td class="error" colspan="3">
			<html:errors/>
		</td>
	</tr>
	
	<tr>
		<td align="left">
			<bean:message key="sticker.product"/> 
		</td>
		<td align="right">			
			<html:text property="product" style="width: 120px; height: 16px;"/>
		</td>
		<td align="right">
			<html:submit property="method" style="width: 70px;">      
				<bean:message key="sticker.print"/>
			</html:submit>		
		</td>
	</tr>
	
	<%
	String stickersupdated = (request.getSession().getAttribute("stickersupdated") != null ?
			request.getSession().getAttribute("stickersupdated").toString() : "");	
	%>
	
	<tr>
		<td align="left" style="width: 230px">
			<bean:message key="sticker.nrstickersupdated" 
					arg0="<%= stickersupdated %>" />
		</td>
		<td align="right">
			<html:submit property="method" style="width: 100px;" styleId="stickersupdated">
				<bean:message key="sticker.stickersupdated" />						
			</html:submit>
		</td>
		<td align="right">
			<html:submit property="method" style="width: 70px;" styleId="empty">      
				<bean:message key="sticker.empty"/>
			</html:submit>
		</td>	
	</tr>
	
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>    
	
</table>

<%@include file="../include/footer.jsp"%>

</html:form>

<logic:present name="stickersupdated" scope="session">
<script type="text/javascript">
	//disable update stickers button if none found in session
	<logic:equal name="stickersupdated" scope="session" value="0">
	document.getElementById('empty').disabled = true;
	document.getElementById('stickersupdated').disabled = true;
	</logic:equal>		
</script>
</logic:present>

</body>

</html:html>
