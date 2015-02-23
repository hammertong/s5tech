/*****************************************************************
 *
 * Funzioni per la trasformazione XSLT/XML in javascript
 * 
 *****************************************************************/

var isIE = (window.navigator.userAgent.indexOf("MSIE ") >= 0 
		|| window.navigator.userAgent.indexOf('.NET') > 0);

var xhttp = null;
var xmldata = null;
var template_url = null;
var target = null;
var last_update_prefix = null;
var xhttp_callback = null;

var xmldata_url = null;
var xml_labels = null;
var add_labels = false;

var postrender_callback = null;
		
function loadUrl(url, callback, asText) 
{
	/* request busy */
	if (xhttp && xhttp.readyState > 0 && xhttp.readyState < 4) return false; 
	
	try {
	
		/* new request */
		if (!isIE) {
			xhttp = new XMLHttpRequest();	  
		}
		else {
			xhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xhttp_callback = callback;
		
		/* set callback procedure */
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 
					&& xhttp.status == 200) {					
				if (xhttp_callback) {	
					xhttp_callback((asText ? xhttp.responseText : xhttp.responseXML));				
				}
			}
		} 
		
		/* do the request */
		xhttp.open("POST", url, true);
		if (!asText) xhttp.setRequestHeader('Content-type', 'text/xml');
		xhttp.send("");	
		
		return true;
		
	}	
	catch (e) {
		
		return false;
		
	}
	
}

function mergedocs(template)
{
	if (isIE) {	 
		var ex = xmldata.transformNode(xsltIE);
		document.getElementById(target).innerHTML = ex;
	}	
	else if (document.implementation && document.implementation.createDocument) {
		xsltProcessor=new XSLTProcessor();
		xsltProcessor.importStylesheet(template);
		resultDocument = xsltProcessor.transformToFragment (xmldata, document);
		document.getElementById(target).innerHTML = "";	  
		document.getElementById(target).appendChild(resultDocument);
	}	
	
	var d = new Date();
	
	if (last_update_prefix == null) {
		last_update_prefix = document.getElementById('statusbar.lastupdate') ? 
			document.getElementById('statusbar.lastupdate').innerHTML : 'last update';		
	}
	
	var msg = last_update_prefix;
	msg += ' ';		
	msg += (d.getHours() < 10 ? '0' + d.getHours() : d.getHours());
	msg += ':';
	msg += (d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes());
	msg+= ':';
	msg += (d.getSeconds() < 10 ? '0' + d.getSeconds() : d.getSeconds());		
	
	if (document.getElementById('statusbar')) {
		document.getElementById('statusbar').innerHTML = msg;
	}
	else {
		window.status = msg;
	}
	
	if (postrender_callback) postrender_callback();
	
}

function loadXSLT(response) 
{	
	//
	// innesto nell'xml le diciture internazionali (xml_labels)
	// precedentemente caricate, se richiesto
	//
	if (add_labels) {		
		add_labels = false;
		var i18n = response.createElement("messages");						
		var x = xml_labels.getElementsByTagName('item');
		for (var i=0;i<x.length;i++) {		
			var newel=response.createElement(x[i].getAttribute('key'));
			var newtext=response.createTextNode(x[i].getAttribute('value'));
			newel.appendChild(newtext);
			i18n.appendChild(newel);		
		} 		
		response.childNodes[0].appendChild(i18n);		
	}	
	
	//
	// salvo l'xml passo al caricamento del template 
	//
	xmldata = response;		
	loadUrl(template_url, mergedocs, false);		
}

function loadXML(response) {
	xml_labels = response;
	loadUrl(xmldata_url, loadXSLT, false);
}

function updatexsl(dst_target, xml_url, xslt_url, labels_prefix, callbackfunc)
{
	target = dst_target;
	template_url = xslt_url;		
	postrender_callback = callbackfunc;	
	add_labels = (labels_prefix != null);
	if (!xml_labels && labels_prefix && labels_prefix.length > 0) {		
		var m = xslt_url.indexOf('xslt/');
		var baseurl = (m > 0 ? xslt_url.substr(0, m) + '/': '');
		m += 5;
		var n = xslt_url.indexOf('.xsl');
		var labels_url = baseurl + 'action/messages?prefix=' + labels_prefix;				
		xmldata_url = xml_url;		
		loadUrl(labels_url, loadXML, false);
	}
	else {
		loadUrl(xml_url, loadXSLT, false);
	}
}
