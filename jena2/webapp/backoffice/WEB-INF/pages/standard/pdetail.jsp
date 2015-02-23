<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html:html>
<head>
  <base href="<%= request.getContextPath() %>/">
  <title><bean:message key="pdetail.title"/></title>
  <link rel="stylesheet" href="stylesheets/login_style_ie.css" type="text/css">      
  <script>
	
	var xhttp = null;
	
	var isIE = (window.navigator.userAgent.indexOf("MSIE ") >= 0 
			|| window.navigator.userAgent.indexOf('.NET') > 0);	
	
	function doPrint() {
		var method = '<bean:message key="sticker.print" />';
		var txt = 'product=<bean:write name="product" property="product" />';
		var url = 'action/sticker?forward=FormatJSon&method=' + method + '&' + txt;		
		
		try {			
			xhttp = (isIE ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest());	  
			xhttp.onreadystatechange = function() {
				if (xhttp.readyState == 4 
					&& xhttp.status == 200) {	
						//	
						//TODO check from json xhttp.responseText if error ...
						//
						//document.getElementById("printed").src = "images/error.png";
						document.getElementById('navButtonProduct').click();
				}
			}			
			xhttp.open("POST", url, true);			
			xhttp.send(txt + "\n");							
		}	
		catch (e) {						
		}	
	}
	
  </script>  
</head>

<body>

<%@include file="../include/header.jsp"%>

<h1><bean:message key="pdetail.title"/></h1>

<table border="0" cellspacing="0" cellpadding="5" class="rounded" width="500px;" >

	<tr class="header">
		<td colspan="2" align="right">
			<table cellpadding="0" cellspacing="0" border="0" style="width: 60px;">
				<tr><td valign="middle" align="center">
					<img src="images/printer.png" class="trans" id="printed"
							style="width: 50px; height: 50px; cursor: pointer;" 
							onclick="doPrint()"></img>	
				</td></tr>
				<tr><td valign="middle" align="center">
					<span style="font-size: 10px; opacity: 0.6;">
						<bean:message key="pdetail.print"/>
					</span>					
				</td></tr>
			</table>
		</td>		
	</tr>	

	<logic:notEmpty name="product" property="productDescription"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.productDescription" /></span>
		</td>
		<td align="right" style="font-weight: bolder;">
			<bean:write name="product" property="productDescription" />
		</td>
	</tr>	
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="product"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.product" /></span>
		</td>
		<td align="right" id="pcode">
			<bean:write name="product" property="product" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="barCode"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.barCode" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="barCode" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="unitOfMeasure"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.unitOfMeasure" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="unitOfMeasure" />
		</td>
	</tr>
	</logic:notEmpty>	
	
	<logic:notEmpty name="product" property="productCode"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.productCode" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="productCode" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="publishTime"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.publishTime" /></span>
		</td>
		<td align="right" style="font-weight: bolder;">
			<bean:write name="product" property="publishTime" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="initpromo"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.initpromo" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="initpromo" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="endpromo"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.endpromo" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="endpromo" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="actualPrice"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.actualPrice" /></span>
		</td>
		<td align="right" style="font-weight: bolder;">
			<bean:write name="product" property="actualPrice" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="actualPricePerUnit"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.actualPricePerUnit" /></span>
		</td>
		<td align="right" style="font-weight: bolder;">
			<bean:write name="product" property="actualPricePerUnit" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="confirmedDate"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.confirmedDate" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="confirmedDate" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="previousPrice"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.previousPrice" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="previousPrice" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="previousPricePerUnit"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.previousPricePerUnit" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="previousPricePerUnit" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="receivedPrice"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.receivedPrice" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="receivedPrice" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="receivedPricePerUnit"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.receivedPricePerUnit" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="receivedPricePerUnit" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="tipoPromo"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.tipoPromo" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="tipoPromo" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="rotation"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.rotation" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="rotation" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="fidelityPrice"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.fidelityPrice" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="fidelityPrice" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="discountValue"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.discountValue" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="discountValue" />
		</td>
	</tr>
	</logic:notEmpty>
		
	<logic:notEmpty name="product" property="iconA"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconA" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconA" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconA" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconB"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconB" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconB" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconB" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconC"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconC" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconC" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconC" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconD"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconD" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconD" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconD" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconN"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconN" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconN" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconN" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconMoon"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconMoon" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconMoon" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconMoon" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconRefresh"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconRefresh" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconRefresh" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconRefresh" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconHand"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconHand" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconHand" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconHand" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconSottocosto"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.iconSottocosto" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="iconSottocosto" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="iconSottocosto" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="updated"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.isUpdated" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="updated" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="updated" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="alertPending"> 
	<tr>
		<td>
			<span style="font-weight: normal;">
				<bean:message key="pdetail.isAlertPending" />
			</span>
		</td>
		<td align="right">
			<logic:equal name="product" property="alertPending" value="true">
				<span style="color: green; font-size: 30px;">&bull;</span>
			</logic:equal>
			<logic:notEqual name="product" property="alertPending" value="true">
				<span style="color: red; font-size: 30px;">&bull;</span>
			</logic:notEqual>
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="iconPz"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.iconPz" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="iconPz" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="category"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.category" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="category" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="label1"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.label1" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="label1" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="label2"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.label2" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="label2" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="label3"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.label3" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="label3" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="warehouseKeeper"> 	
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.warehouseKeeper" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="warehouseKeeper" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="department"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.department" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="department" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="aisle"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.aisle" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="aisle" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="shelf"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.shelf" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="shelf" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="position"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.position" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="position" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="facing"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.facing" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="facing" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="fivedigits"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.fivedigits" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="fivedigits" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="discount"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.discount" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="discount" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="promotext"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.promotext" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="promotext" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="punti"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.punti" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="punti" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="tipoConsegnaMargin"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.tipoConsegnaMargin" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="tipoConsegnaMargin" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="imballo"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.imballo" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="imballo" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="alert"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.alert" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="alert" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="assortimento"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.assortimento" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="assortimento" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="modulo"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.modulo" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="modulo" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="giacenza"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.giacenza" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="giacenza" />
		</td>
	</tr>
	</logic:notEmpty>
	
	<logic:notEmpty name="product" property="baseprice"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.baseprice" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="baseprice" />
		</td>
	</tr>
	</logic:notEmpty>

	<logic:notEmpty name="product" property="updateUser"> 
	<tr>
		<td><span style="font-weight: normal;">
			<bean:message key="pdetail.updateUser" /></span>
		</td>
		<td align="right">
			<bean:write name="product" property="updateUser" />
		</td>
	</tr>
	</logic:notEmpty>
		
</table>

<br>
&nbsp;
<br>

<%@include file="../include/footer.jsp"%>

</body>

</html:html>
