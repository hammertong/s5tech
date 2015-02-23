<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html:html>
<head>

  <base href="<%= request.getContextPath() %>/">
  <title><bean:message key="daily.title"/></title>
  <link rel="stylesheet" href="stylesheets/login_style_ie.css" type="text/css">
  <script type="text/javascript" src="js/tbupdater.js"></script>
  <script type="text/javascript">
  <!--
	var items = 10;
	var hours = 24;
	var timeout = 5000;
	
	var urldata = 'backend?query=eslupdates&unique=esl';
	var urltemplate = 'xslt/eslupdating-all.jsp';
	
	function updateHours(value) {
		hours = value;
		updatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);		
	}
	
	function updateItems(value) {
		items = value;
		updatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);
	}
	
	function updateTimeout(value) {
		timeout = value;
		updatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);
	}	
	
	function schedule()
	{
		updatexsl('bottomview', urldata + '&params=' + items + ',' + hours, urltemplate);
		setTimeout("schedule()", timeout);	
	}
  // -->	
  </script>
  
</head>

<body onload="schedule()">

<%@include file="../include/header.jsp"%>
	
<h1><bean:message key="daily.title" /></h1>
	
<div style="cursor: default; font-size: 10px;">
	<!-- <bean:message key="xslt.selecttime"/> -->	
	<select onchange="updateHours(this.value)">
		<option value="1"><bean:message key="combo.lasthour" /></option>
		<option value="6"><bean:message key="combo.lastxhours" arg0="6" /></option>
		<option value="24" selected="selected"><bean:message key="combo.lastday" /></option>
		<option value="168"><bean:message key="combo.lastweek" /></option>
	</select>		
	<!-- <bean:message key="xslt.selectmaxitems"/>	-->
	<select onchange="updateItems(this.value)">
		<option value="1">1</option>
		<option value="5">5</option>
		<option value="10" selected="selected">10</option>
		<option value="20">20</option>
		<option value="1000"><bean:message key="combo.unlimited" /></option>			
	</select>
	<!-- <bean:message key="xslt.refreshupdate"/> -->	
	<select onchange="updateTimeout(this.value)">
		<option value="1000"><bean:message key="combo.everysec" /></option>
		<option value="5000" selected="selected"><bean:message key="combo.everyxsecs" arg0="5"/></option>
		<option value="10000"><bean:message key="combo.everyxsecs" arg0="10"/></option>	
		<option value="30000"><bean:message key="combo.everyxsecs" arg0="30"/></option>		
	</select>
</div>

<%@include file="../include/footer.jsp"%>

</body>

</html:html>
