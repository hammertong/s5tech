<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<h3><bean:message key="global.sessiontimeout"/></h3>
<a href="<%= request.getContextPath() + "/login.jsp" %>">
	<bean:message key="global.returnlogin"/>
</a>
