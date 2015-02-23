<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html:html>
<head>

  <base href="<%= request.getContextPath() %>/">
  <title><bean:message key="pricer.title"/></title>
  <link rel="stylesheet" href="stylesheets/login_style_ie.css" type="text/css">
  <script type="text/javascript" src="js/tbupdater.js"></script>
  <script type="text/javascript">
  <!--
	var items = 10;
	var hours = 24;
	var timeout = 5000;
	
	var urldata = 'backend?query=eslupdates-by-current-user&unique=esl&currentuser=true';
	var urltemplate = 'xslt/eslupdating.jsp';
	
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
	
<html:form action="pricer" styleId="pricerForm">
<bean:define name="pricerForm" property="step" id="step" type="Integer" />  

<html:hidden property="step" />
<html:hidden property="pricePerUnit" />
		
<h1><bean:message key="pricer.head" arg0="<%= step.toString() %>" arg1="4"/></h1>

<table border="0" cellspacing="0" cellpadding="0" style="width: 400px; padding-bottom: 30px;">
	
	<tr style="height: 30px;">
		<td colspan="2">
			<logic:equal name="pricerForm" property="step" value="1">
				<bean:message key="pricer.step.1"/>		 	
			</logic:equal>
			<logic:equal name="pricerForm" property="step" value="2">
				<bean:message key="pricer.step.2"/>			
				<bean:write name="product" property="productDescription" />
			</logic:equal>	
			<logic:equal name="pricerForm" property="step" value="3">
				<bean:message key="pricer.step.3"/>			
			</logic:equal>	
			<logic:equal name="pricerForm" property="step" value="4">
				<bean:message key="pricer.step.4"/>			
			</logic:equal>	
		</td>
	</tr>
	
	<tr valign="top" id="row_step1" style="display: none; height: 70px;">
  		<td style="width: 200px;">
			<bean:message key="pricer.step.1.insert"/>				
		</td>
		<td style="width: 200px;">
			<html:text property="barcode" style="width: 100px; height: 16px;"/>
			<logic:messagesPresent>
				<div style="font-size: 10px; font-weight: bolder; color: red;">
				<html:errors/>
				</div>
			</logic:messagesPresent>	
		</td>
	</tr>
	
	<tr valign="top" id="row_step2" style="display: none; height: 70px;">
  		<td style="width: 200px;">			
			<bean:message key="pricer.step.2.insert" />			
		</td>
		<td style="width: 200px;">
			<html:text property="newprice" style="width: 100px; height: 16px;" />
			<logic:messagesPresent>
				<div style="font-size: 10px; font-weight: bolder; color: red;">
				<html:errors/>
				</div>
			</logic:messagesPresent>	
		</td>
	</tr>
			
	<tr valign="top" id="row_step3" style="display: none; height: 70px;">
  		<td colspan="2">
			<table border="0" cellspacing="0" cellpadding="0">				
				<tr valign="top" style="height: 35px;">
					<td valign="top" style="width: 200px;">
						<label>
							<html:radio property="options" value="discount" onclick="checkradios()"/> 
							<bean:message key="pricer.step.3.discount" />
						</label>
					</td>						
					<td valign="top" style="width: 200px;">
						<div id="discount" style="display: none">
							<html:text property="discount" styleId="discountText" style="width: 70px; height: 16px; text-align: right;"/> 
							<span style="font-size: 10px;"><bean:message key="pricer.step.3.%"/></span>
						</div>
					</td>
					<td valign="top" style="width: 200px;">
						<label>
							<html:radio property="options" value="offer" onclick="checkradios()"/> 
							<bean:message key="pricer.step.3.offer" />
						</label>
					</td>
				</tr>
				<tr valign="top" style="height: 35px;">
					<td valign="top" style="width: 200px;">
						<label>
							<html:radio property="options" value="points" onclick="checkradios()"/> 							
							<bean:message key="pricer.step.3.points"/>
						</label>
					</td>						
					<td valign="top" style="width: 200px;">
						<div id="points" style="display: none">						
							<html:text property="points" styleId="pointsText" style="width: 70px; height: 16px; text-align: right;"/> 
							<span style="font-size: 10px;"><bean:message key="pricer.step.3.pt"/></span>
						</div>
					</td>
					<td valign="top" style="width: 200px;">
						<label>
							<html:radio property="options" value="3x2" onclick="checkradios()"/> 
							<bean:message key="pricer.step.3.3x2" />
						</label>
					</td>
				</tr>				
			</table>
			<logic:messagesPresent>
				<div style="font-size: 10px; font-weight: bolder; color: red;">
				<html:errors/>
				</div>
			</logic:messagesPresent>	
		</td>
	</tr>
	
	<tr id="row_step4" style="height: 70px; display: none;">
		<td colspan="2">
			<logic:present name="product">
			<table class="rounded" style="width: 100%;" id="publish">
				<tr>
					<td>
						<bean:message key="pricer.step.4.newprice" />
					</td>						
					<td align="right">
						<bean:write name="product" property="productDescription" />
					</td>
				</tr>	
				<tr>
					<td>
						<bean:message key="pricer.step.4.newprice" />
					</td>						
					<td align="right">
						<bean:write name="pricerForm" property="newprice" />
					</td>
				</tr>	
				<tr>
					<td>
						<bean:message key="pricer.step.4.newppu" />
					</td>
					<td align="right">
						<bean:write name="pricerForm" property="pricePerUnit" />
					</td>
				</tr>					
				<logic:equal name="pricerForm" property="options" value="discount">
				<tr>
					<td>
						<bean:message key="pricer.step.3.discount" />
					</td>
					<td align="right">
						<bean:write name="pricerForm" property="discount" />
						<bean:message key="pricer.step.3.%"/>
					</td>
				</tr>
				</logic:equal>					
				<logic:equal name="pricerForm" property="options" value="points">
				<tr>	
					<td>
						<bean:message key="pricer.step.3.points" />
					</td>
					<td align="right">
						<bean:write name="pricerForm" property="points" />
						<bean:message key="pricer.step.3.pt"/>				
					</td>
				</tr>
				</logic:equal>					
				<logic:equal name="pricerForm" property="options" value="offer">
				<tr>	
					<td>
						<bean:message key="pricer.step.3.offer" />							
					</td>
					<td>
						&nbsp;
					</td>
				</tr>						
				</logic:equal>
				<logic:equal name="pricerForm" property="options" value="3x2">
				<tr>	
					<td>
						<bean:message key="pricer.step.3.3x2" />
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				</logic:equal>
			</table>
			</logic:present>
			<br>			
		</td>
	</tr>
	
	<tr style="height: 30px;">
  		<td colspan="2" align="left">
		<logic:equal name="pricerForm" property="step" value="4">
			<html:submit property="method" style="width: 120px; position: relative; left: 140px; font-weight: bolder;">      
				<bean:message key="pricer.publish"/>
			</html:submit>						
		</logic:equal>
		<logic:notEqual name="pricerForm" property="step" value="4">
			<html:submit property="method" styleId="next" style="<%= (step.intValue() > 1 ? "width: 120px; position: relative; left: 140px;" : "width: 120px;") %>">      
				<bean:message key="pricer.next"/>
			</html:submit>						
		</logic:notEqual>
		<logic:notEqual name="pricerForm" property="step" value="1">			
			<html:submit property="method" style="width: 120px; position: relative; left: -120px;">      
				<bean:message key="pricer.previous"/>
			</html:submit>			
		</logic:notEqual>			
		</td>		
	</tr>
	
</table>

<script type="text/javascript">
<!--

	document.getElementById('row_step<%= step.intValue() %>').style.display = 'block';
	
<logic:equal name="pricerForm" property="step" value="1">
	var frm = document.getElementById('pricerForm');
	frm.barcode.focus();	
</logic:equal>

<logic:equal name="pricerForm" property="step" value="2">
	var frm = document.getElementById('pricerForm');
	frm.newprice.focus();
</logic:equal>	

<logic:equal name="pricerForm" property="step" value="3">
	var prevcheck = null;	
	function checkradios()
	{	
		var frm = document.getElementById('pricerForm');
		// reset options 
		document.getElementById('discount').style.display = 'none';
		document.getElementById('points').style.display = 'none';
		if (frm.options.value == prevcheck) {			
			 var radios = document.getElementsByTagName("input");
			 for (var i = 0; i < radios.length; i ++) {
				if (radios[i].type 
						&& radios[i].type == "radio" 
						&& radios[i].name == 'options') {
					radios[i].checked = false;
				}
			 }
			 frm.options.value = '';
		}
		// display if needed
		switch (frm.options.value) {			
			case 'discount':
				document.getElementById('discount').style.display = 'block';
				document.getElementById('discountText').focus();
				break;
			case 'points':
				document.getElementById('points').style.display = 'block';
				document.getElementById('pointsText').focus();
				break;
		}		
		prevcheck = frm.options.value;		
	}	
	checkradios();
	document.getElementById('next').focus();		
</logic:equal>	

<logic:equal name="pricerForm" property="step" value="4">	
	// blinking button
	function doBlink() {
		var el = document.getElementById('publish');		
		var op = (el.style != null ? el.style.opacity : null);
		el.style.opacity = (!op || op == '1' ?  '0.5' : '1');				
		setTimeout("doBlink()", 500);
	}	
	doBlink();
</logic:equal>	

// -->
</script>

</html:form>

<hr>
<h3><bean:message key="daily.title" /></h3>
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
