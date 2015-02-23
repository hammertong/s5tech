<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html:html>
<head>

<base href="<%= request.getContextPath() %>/">
<title><bean:message key="alerts.title"/></title>
<link rel="stylesheet" href="stylesheets/login_style_ie.css" type="text/css">
<script type="text/javascript" src="js/tbupdater.js"></script>
<script type="text/javascript">
  <!--
	var items = 10;
	var hours = 24;
	var timeout = 30000;
	
	var urldata = 'backend?query=alerts';
	var urltemplate = 'xslt/alerts.jsp';
	
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
		//setTimeout("schedule()", timeout);			
	}
	
	function populateMesg(text)
	{
		if (!text) return;
		
		var div = currentDiv;
		
		if (text.length > 100) {
			div.style.height = '120px';	
			div.style.overflowY = 'scroll';				
		}
		else {
			div.style.height = '30px';				
		}		
		
		div.innerHTML = text;
	}
	
	function showmesg(td)
	{	
		var div = document.getElementById('div' + td.id);
		
		if (div.innerHTML.indexOf('!load') >= 0) {			
			if (!loadUrl('backend?query=alerts-message&format=text&params=' + td.id, populateMesg, true)) return;
			currentDiv = div;			
			currentDiv.innerHTML = '<img style="width: 32px; height: 32px;" src="images/loader.gif"></img>';
		}
		
		if (div.style.display && div.style.display == 'block') {
			td.innerHTML = '<img class="trans" style="width: 32px; height: 32px;" src="images/expand.png"></img>';
			div.style.display = 'none';
		}
		else {
			td.innerHTML = '<img class="trans" style="width: 32px; height: 32px;" src="images/collapse.png"></img>';
			div.style.display = 'block';
		}				
	}
	
  // -->	
</script>
  
<style>
TR.S5 { color: #039; font-weight: bolder; text-decoration: underline; }
TR.S5Fatal { color: #dd0000; }
TR.S5Error { color: #aa0000; }
TR.S5Warn { color: #660000; }
TR.S5Warning { color: #660000; }
TR.S5Success { color: #666; }
TR.S5Info { color: #039; }
TR.S5Trace { color: gray; }
TR.S5Debug { color: gray; }
</style>
  
</head>

<body onload="schedule()">

<%@include file="../include/header.jsp"%>
	
<h1><bean:message key="alerts.title" /></h1>
	
<div style="cursor: default;">
	<!-- <bean:message key="xslt.selecttime"/> -->	
	<select onchange="updateHours(this.value)" class="combo">
		<option value="1"><bean:message key="combo.lasthour" /></option>
		<option value="6"><bean:message key="combo.lastxhours" arg0="6" /></option>
		<option value="24" selected="selected"><bean:message key="combo.lastday" /></option>
		<option value="168"><bean:message key="combo.lastweek" /></option>
		<option value="720"><bean:message key="combo.lastmonth" /></option>
		<option value="8760"><bean:message key="combo.lastyear" /></option>
	</select>		
	<!-- <bean:message key="xslt.selectmaxitems"/>	-->
	<select onchange="updateItems(this.value)" class="combo">
		<option value="1">1</option>
		<option value="5">5</option>
		<option value="10" selected="selected">10</option>
		<option value="20">20</option>
		<option value="1000"><bean:message key="combo.unlimited" /></option>			
	</select>
	<!--
	<bean:message key="xslt.refreshupdate"/>	
	<select onchange="updateTimeout(this.value)" class="combo">
		<option value="1000"><bean:message key="combo.everysec" /></option>
		<option value="5000"><bean:message key="combo.everyxsecs" arg0="5"/></option>
		<option value="10000"><bean:message key="combo.everyxsecs" arg0="10"/></option>	
		<option value="30000" selected="selected"><bean:message key="combo.everyxsecs" arg0="30"/></option>		
	</select>
	-->
</div>

<%@include file="../include/footer.jsp"%>

</body>

</html:html>
