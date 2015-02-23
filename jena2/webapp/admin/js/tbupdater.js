var display_data_target_id = 'displaydata';
var display_time_target_id = 'displayTime';
var tableAlternateColor = '#e8e8f8';
var update_timeout_ms = 5000;
var update_xml_url = '#';
var update_xslt_url = '#';
var update_noresultsDescription = '';
var postrender_callback = null;

var isIE = (window.navigator.userAgent.indexOf("MSIE ") >= 0 || window.navigator.userAgent.indexOf('.NET') > 0);

function loadXMLDoc(dname)
{
	if (!isIE)
	{
		xhttp=new XMLHttpRequest();	  
	}
	else
	{
		xhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhttp.open("GET",dname,false);
	xhttp.send("");
	return xhttp.responseXML;
}

function loadXSLTDoc(dname)
{
	if (!isIE)
	{
		xhttp=new XMLHttpRequest();	  
		xhttp.open("GET",dname,false);
		xhttp.send("");
		return xhttp.responseXML;
	}
	else
	{
		xslt=new ActiveXObject("Msxml2.FreeThreadedDOMDocument.3.0");
		xslt.async=false;
		xslt.load(dname);
		return xslt;
	}	
}

function dataRendering(o)
{
	var i, j;
	var tb = o.children[0].children[0];
	var n = tb.children.length;
	if (n == 2) {
		document.getElementById(display_data_target_id).innerHTML = '<b>' + update_noresultsDescription + '</b>'
	}
	else {
		for (i = 0; i < n - 1; i ++)
		{
			if (i == 0) continue;
			tr = tb.children[i];
			if (i % 2 == 1) {
				for (j = 0; j < tr.children.length; j ++) {
					if (j == 0) tr.children[j].style.background = tableAlternateColor;
					else if (j == tr.children.length -1) tr.children[j].style.background = tableAlternateColor;
					else tr.children[j].style.background = tableAlternateColor;				
				}
			}
		}
	}
}

function displayResult()
{
	xml=loadXMLDoc(update_xml_url);
	xsl=loadXSLTDoc(update_xslt_url);
	// code for IE
	if (isIE)
	{	 
	  ex=xml.transformNode(xsl);
	  document.getElementById(display_data_target_id).innerHTML = ex;
	}
	// code for Mozilla, Firefox, Opera, etc.
	else if (document.implementation && document.implementation.createDocument)
	{
	  xsltProcessor=new XSLTProcessor();
	  xsltProcessor.importStylesheet(xsl);
	  resultDocument = xsltProcessor.transformToFragment(xml,document);
	  document.getElementById(display_data_target_id).innerHTML = "";	  
	  document.getElementById(display_data_target_id).appendChild(resultDocument);
	}
	document.getElementById(display_time_target_id).innerHTML = 'ultimo aggiornamento: ' + new Date();
	dataRendering (document.getElementById(display_data_target_id));
	if (postrender_callback != null) postrender_callback ();
	setTimeout("displayResult()", update_timeout_ms);
}

function displayResultNoUpdate()
{
	xml=loadXMLDoc(update_xml_url);
	xsl=loadXSLTDoc(update_xslt_url);
	// code for IE
	if (isIE)
	{
	  ex=xml.transformNode(xsl);
	  document.getElementById(display_data_target_id).innerHTML = ex;
	}
	// code for Mozilla, Firefox, Opera, etc.
	else if (document.implementation && document.implementation.createDocument)
	{
	  xsltProcessor=new XSLTProcessor();
	  xsltProcessor.importStylesheet(xsl);
	  resultDocument = xsltProcessor.transformToFragment(xml,document);
	  document.getElementById(display_data_target_id).innerHTML = "";	  
	  document.getElementById(display_data_target_id).appendChild(resultDocument);
	}
	dataRendering (document.getElementById(display_data_target_id));
	if (postrender_callback != null) postrender_callback();
}

function startTbUpdater(xml_url, xslt_url, noresultsDescription, postrender_func)
{
	update_xml_url = xml_url;
	update_xslt_url = xslt_url;
	update_noresultsDescription = noresultsDescription;
	if (postrender_func != null) postrender_callback = postrender_func;
	displayResult();	
}

function tbUpdate(xml_url, xslt_url, noresultsDescription, postrender_func)
{
	update_xml_url = xml_url;
	update_xslt_url = xslt_url;
	update_noresultsDescription = noresultsDescription;
	if (postrender_func != null) postrender_callback = postrender_func;
	displayResultNoUpdate();	
}

