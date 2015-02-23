<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.lang.reflect.Field" %>

<%@ page import="com.s5tech.backend.IConstants" %>
<%@ page import="com.s5tech.backend.view.UserView" %>

<%@ page import="org.apache.struts.Globals" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
	UserView userView = null;	
	String statusbar = null;		
%>

<!-- begin header section -->
<logic:present name="<%= IConstants.USER_VIEW_KEY %>">	
<%
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
	
%>

<script type="text/javascript">
function swapout(td)
{
	if (td.className && td.className == 'hnavsel') return;
	td.className = 'hnav'; 	
}
function swapover(td)
{
	if (td.className && td.className == 'hnavsel') return;
	td.className = 'hnavover'; 	
}
function selectmenu(el, url)
{		
	if (el.className == 'hnavsel') return;
	if (window.navigator.userAgent.indexOf("MSIE ") >= 0 
			|| window.navigator.userAgent.indexOf('.NET') > 0) {		
		window.location.href = '<%= request.getContextPath() %>' + '/' + url;
	}
	else {
		window.location.replace(url);
	}
}
</script>

</logic:present>

<center>

<table border="0" cellspacing="0" cellpadding="5" width="100%" class="rounded filled">

	<tr valign="top" class="header" style="height: 50px">
		<td align="left" valign="middle" style="width: 120px;">
			<html:img srcKey="image.logo" height="46px" altKey="image.logo.alt" border="0"/>
		</td>	
		<td align="right" valign="middle">
			&nbsp;
			<logic:present name="<%= IConstants.USER_VIEW_KEY %>">	
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td align="left" style="color: #ccdfe1;">
						<bean:message key="header.userloggedin" 
								arg0="<%= userView.getFirstName() %>" 
								arg1="" />
						<br>
						<bean:message key="header.userrole" 
								arg0="<%= userView.getRole() %>" />
					</td>
					<td align="center" style="width: 50px;"> 
						<html:link href="action/home">
							<html:img srcKey="image.home" width="46px" height="46px" altKey="image.home.alt" styleClass="trans ico" border="0"/>
						</html:link>
						<br>
						<span style="font-size: 10px; opacity: 0.6;">
							<bean:message key="global.home"/>
						</span>
					</td>      
					<td align="center" style="width: 50px;"> 
						<html:link href="action/logout">
							<html:img srcKey="image.logout" width="46px" height="46px" altKey="image.logout.alt" styleClass="trans ico" border="0"/>		
						</html:link>
						<br>
						<span style="font-size: 10px; opacity: 0.6;">
							<bean:message key="global.logout"/>
						</span>
					</td>   
				</tr>
			</table>
			</logic:present>				
		</td>
	</tr>  
	
	<logic:present name="<%= IConstants.USER_VIEW_KEY %>">	
	
	<tr class="hnavbar" style="padding: 0px; margin: 0px;">
		<td colspan="2" style="padding: 0px; margin: 0px;"> 
			<table cellpadding="0" cellspacing="0" width="100%" style="padding: 0px; margin: 0px;">
				<tr valign="middle" class="hnavbar" style="padding: 0px; margin: 0px;">								
					<td onclick="selectmenu(this, 'action/home')" 
						onmouseover="swapover(this)" 
						onmouseout="swapout(this)" 						
						class="<%= (userView.getCurrentContext().length() == 0 ? "hnavsel" : "hnav") %>"  						
						style="width: 210px;">
						<bean:message key="hnav.standard"/>		
					</td>										
					<td class="hnavnotab" style="padding: 0px; margin: 0px;">
						&nbsp;
					</td>					
					<td align="right" class="hnavnotab" style="width: 180px; padding: 0px; margin: 0px; padding-right: 10px;">		
						<a href="action/home?locale=it_IT"><html:img srcKey="image.lang.it" styleClass="trans flag"/></a>&nbsp;
						<a href="action/home?locale=en_US"><html:img srcKey="image.lang.en" styleClass="trans flag"/></a>&nbsp;
						<a href="action/home?locale=fr_FR"><html:img srcKey="image.lang.fr" styleClass="trans flag"/></a>&nbsp;						
					</td>	 									
				</tr>
			</table>
		</td>
	</tr>
	
	<tr valign="top">
		<td style="padding-top: 10px; width: 200px;" rowspan="2">			
			<%@include file="../menu/standard.jsp"%>			
		</td>	
		<td style="padding-left: 20px; padding-right: 20px; padding-top: 10px; padding-bottom: 10px;">	
		
	</logic:present>	

	<logic:notPresent name="<%= IConstants.USER_VIEW_KEY %>">
	</table>	 
	</logic:notPresent>	

<!-- end header section -->

