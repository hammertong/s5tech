/* Common AJAX features */
function mergeXmlDocuments (xmlUrl, xslUrl, data, target, parentDoc)
{	
	// code for IE
	try {
		
		if (window.ActiveXObject)
		{
			var XMLDocument = new ActiveXObject('MSXML2.DOMDocument.3.0');
			var XSLDocument = new ActiveXObject('MSXML2.DOMDocument.3.0');

			XMLDocument.validateOnParse = false;
			XMLDocument.async = false;
			var queryString = (data == null ? '': '?' + data.substring(1).replace(' ', '&'));
			XMLDocument.load (xmlUrl + queryString);
			if (XMLDocument.parseError.errorCode != 0) {
			   //manage error
			   return;
			}
			
			XSLDocument.validateOnParse = false;
			XSLDocument.async = false;
			XSLDocument.load (xslUrl);
			if (XSLDocument.parseError.errorCode != 0) {
			   //manager error
			   return;
			}
			
			document.getElementById("displaydata").innerHTML = XMLDocument.transformNode(XSLDocument);

		}	
		// code for Mozilla, Firefox, Opera, etc.
		else if (document.implementation && document.implementation.createDocument)
		{
			var xhttp = new XMLHttpRequest();
			
			xhttp.open("POST", xmlUrl, false);
			xhttp.setRequestHeader('Content-type','text/xml');
			xhttp.send(data);	
			var xml = xhttp.responseXML;
			
			xhttp = new XMLHttpRequest();
			xhttp.open("GET", xslUrl, false);
			xhttp.send(data);	
			var xsl = xhttp.responseXML;

			xsltProcessor = new XSLTProcessor();
			xsltProcessor.importStylesheet(xsl);
			resultDocument = xsltProcessor.transformToFragment (xml, (parentDoc ? parentDoc : document));
			target.innerHTML = "";	  
			target.appendChild(resultDocument);
		}
		else 
		{
			alert ("Service not supported, cannot render data.\rYour browser doesn't support XML/XSL transformation.");
			return false;
		}	
		return true;
	}
	catch (e) {
	
		target.innerHTML = '<pre style="font-family: ' +"'Courier New, Courier'; font-size: 12px;" + '"><br/>';
		target.innerHTML += "Error in XSLT/XML conversion:<ol>";
		if (e.description) target.innerHTML += "<li>description: " + e.description + "</li>";
		if (e.message) target.innerHTML += "<li>message: " + e.message + "</li>";
		if (e.number) target.innerHTML += "<li>error number: " + e.number + "</li>";
		target.innerHTML += "</ol></pre>";
		//alert (target.innerHTML);		
		return false;
	}
		
}

function loadJSon(url, data)
{	
	if (window.XMLHttpRequest)
	{
	  xhttp=new XMLHttpRequest();
	}
	else
	{
	  xhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}	
	xhttp.open("POST", url, false);
	xhttp.setRequestHeader('Content-type','text/json');
	xhttp.send(data);
	return eval('( ' + xhttp.responseText + ' )');
}

function getAbsolutePosition(control)
{
	var op = control;
	var x = 0;
	
	while (op) {
		x += op.offsetLeft;
		op = op.offsetParent;
	}

	op = control;
	var y = 0;
	while (op) {
		y += op.offsetTop;
		op = op.offsetParent;
	}

	var ret = new Array();	
	ret[0] = x;
	ret[1] = y;
	ret[2] = control.offsetWidth;
	ret[3] = control.offsetHeight;
	
	return ret;
	
}

function setAbsolutePosition(control, x, y, w, h)
{	
	control.style.left = x;	
	control.style.top = y;
	if (w) control.style.width = w;
	if (h) control.style.height = h;	
	control.style.visibility = 'visible';
	control.style.display = 'block';
}

function replaceAllText (text, textToFind, textToReplace)
{
	var s = text.replace (textToFind, textToReplace);
	while (s.indexOf(textToFind) > 0) { s = s.replace (textToFind, textToReplace); }
	return s;
}

