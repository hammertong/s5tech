<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="text/xml" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<messages>
<logic:iterate name="messages" id="message">	
	<item <% out.write(" key=\""); %>
		<bean:write name="message"/>
		<% out.write("\" value=\""); %>
		<bean:message key="<%= message.toString() %>" />
		<% out.write("\" />"); %>
</logic:iterate>
</messages>
