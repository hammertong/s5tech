<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.s5tech.backend.IConstants" %>
<%@ page import="com.s5tech.backend.view.UserView" %>

<!-- begin header section -->
<logic:present name="<%= IConstants.USER_VIEW_KEY %>">	

<%
	UserView menuView = ((UserView)session.getAttribute(IConstants.USER_VIEW_KEY));
%>	
	
<html:form action="menu">

<table border="0" cellspacing="0" cellpadding="5" width="200px">
	
	<% if (menuView.isInRole("priceChangeFunction")) { %>
	<tr>
  		<td align="center" valign="middle">
			<html:submit property="method" style="width: 100%; height: 30px;">      
				<bean:message key="menu.changeprice"/>
			</html:submit>
		</td>
	</tr>		
	<% } %>
	
	<% if (menuView.isAdministrator()) { %>	
	<tr>
  		<td align="center" valign="middle">
			<html:submit property="method" style="width: 100%; height: 30px;">      
				<bean:message key="menu.alerts"/>
			</html:submit>
		</td>
	</tr>
	<% } %>

	<% if (menuView.isInRole("reportPriceChange")) { %>	
	<tr>
  		<td align="center" valign="middle">
			<html:submit property="method" style="width: 100%; height: 30px;">      
				<bean:message key="menu.dailyreports"/>
			</html:submit>
		</td>
	</tr>
	<% } %>
	
	<tr>
  		<td align="center" valign="middle">
			<html:submit property="method" style="width: 100%; height: 30px;" styleId="navButtonProduct">      
				<bean:message key="menu.details"/>
			</html:submit>
		</td>
	</tr>	
	
	<tr>
  		<td align="center" valign="middle">
			<html:submit property="method" style="width: 100%; height: 30px;">      
				<bean:message key="menu.productassociation"/>
			</html:submit>
		</td>
	</tr>
	<tr>
  		<td align="center" valign="middle">
			<html:submit property="method" style="width: 100%; height: 30px;">      
				<bean:message key="menu.deleteassociation"/>
			</html:submit>
		</td>
	</tr>	
	<tr>
  		<td align="center" valign="middle">
			<html:submit property="method" style="width: 100%; height: 30px;">      
				<bean:message key="menu.sticker"/>
			</html:submit>
		</td>
	</tr>	
	<tr>
		<td align="center" valign="middle">&nbsp;</td>
	</tr>    
</table>

</html:form>

</logic:present>
