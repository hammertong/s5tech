function doRequest (data)
{
	if (data.length == 0) {
		alert('Error: cannot send empty message!');
		return;			
	}
	
	if (data.indexOf('<?xml ') < 0) {
		alert('Error: cannot send message without xml header!');
		return;			
	}
	
	if (window.XMLHttpRequest)
	{
	  xhttp=new XMLHttpRequest();
	}
	else
	{
	  xhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xhttp.open("POST",'../publish',false);
	xhttp.send(data);
	
	//return xhttp.responseXML;
}

function formatDate()
{
	return formatDateSpec(null);
}

function formatDateSpec(dd)
{	
	var x = (dd == null ? new Date() : dd);
		
	var y = x.getFullYear();
	var m = x.getMonth() + 1;
	var d = x.getDate();
	
	var H = x.getHours();
	var M = x.getMinutes();
	var S = x.getSeconds();
	
	var s = '';
	
	s += y;
	
	s += '-';
	
	if (m < 10) s += '0';
	s += m;
	
	s += '-';
	
	if (d < 10) s += '0';
	s += d;
	
	s += "T";
	
	if (H < 10) s += '0';
	s += H;
	
	s += ':';
	
	if (M < 10) s += '0';
	s += M;
	
	s += ':';
	
	if (S < 10) s += '0';
	s += S;
	
	return s;
}

function sendNightMode(durationSecs, activationTime)
{
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="1" msgCommand="EslEnterNightMode" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += '	<eslList all="true" />';
	xml += '	<time activation="' + activationTime + '" durationSecs="' + durationSecs + '"/>';
	xml += '</message>';
	
	try {
		doRequest (xml);
		return true;
	}
	catch (e) { return false; }
	
}

function sendActiveServicePage(durationSecs)
{
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="1" msgCommand="EslSetActiveServicePage" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += '	<eslList all="true" />';
	xml += '	<time durationSecs="' + durationSecs + '"/>';
	xml += '</message>';
	
	try {
		doRequest (xml);
		return true;
	}
	catch (e) { return false; }
		
}

function sendStatusRequest (maxbuf, secsToWait, mac)
{
	if (maxbuf < 0 || maxbuf > 15) maxbuf = 7;
	if (secsToWait > 180) secsToWait = 180;   

	var body = '';
	
	if (mac == null) { 
		//this is a global status request
		body += '	<eslList all="true" />';
		body += '	<eslStatusRequest maxBuf="' + maxbuf + '" secsToWait="' + secsToWait + '" />';
	}
	else {
		body += '	<eslList>';			
		body += '		<mac>' + mac + '</mac>';		
		body += '	</eslList>';	
	}
	
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="1" msgCommand="EslStatusRequest" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += body;
	xml += '</message>';
	
	try {
		doRequest (xml);
		return true;
	}
	catch (e) { return false; }
	
}

function sendGlobalStatusRequest (maxbuf, secsToWait)
{
	sendStatusRequest(maxbuf, secsToWait, null);
}

function sendLeave(mac) 
{
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="1" msgCommand="EslLeave" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += '	<eslList>';			
	xml += '		<mac>' + mac + '</mac>';		
	xml += '	</eslList>';	
	xml += '</message>';
	
	try {
		doRequest (xml);
		return true;
	}
	catch (e) { return false; }

}

function sendKill(mac) 
{
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="1" msgCommand="EslKill" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += '	<eslList>';			
	xml += '		<mac>' + mac + '</mac>';		
	xml += '	</eslList>';	
	xml += '</message>';
	
	try {
		doRequest (xml);
		return true;
	}
	catch (e) { return false; }

}

function sendStatisticsRequest(mac) 
{
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="1" msgCommand="EslStatisticsRequest" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += '	<eslList>';			
	xml += '		<mac>' + mac + '</mac>';		
	xml += '	</eslList>';	
	xml += '</message>';
	
	try {
		doRequest (xml);
		return true;
	}
	catch (e) { return false; }

}

function sendSetChannelToJoinRequest (channel, secsToWait, mac) 
{
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="1" msgCommand="EslSetChannelToJoin" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += '	<eslList>';			
	xml += '		<mac>' + mac + '</mac>';		
	xml += '	</eslList>';	
	xml += '	<eslChannelToJoin channel="' + channel + '" secsToWait="' + secsToWait + '" />';
	xml += '</message>';
	
	try {
		doRequest (xml);
		return true;
	}
	catch (e) { return false; }

}

function sendPriceUpdate(mac, msgid, activation, hash, b64data, callback) 
{
	var xml = "<?xml version='1.0' encoding='UTF-8'?>";
	xml += '<message msgId="' + msgid + '" msgCommand="EslPriceUpdate" ';
	xml += '		xsi:schemaLocation="http://s5tech.com/network schema.xsd" ';
	xml += '		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ';
	xml += '		xmlns="http://s5tech.com/network"> ';
	xml += '	<eslList>';			
	xml += '		<mac>' + mac + '</mac>';		
	xml += '	</eslList>';
	xml += '	<eslPriceData activationTime="' + activation + '" hashCode="' + hash + '">';
	xml += '	' + b64data;
	xml += '	</eslPriceData>'; 
	xml += '</message>';	
	try {	
		doRequest (xml);//, callback, true);
		return true;
	}
	catch (e) { return false; }
}



