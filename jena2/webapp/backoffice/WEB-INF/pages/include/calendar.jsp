<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="text/plain" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<style>

table.calendar {
	font-family: Arial;
	font-size: 12px;
	background: #ffffff;
	border: 1px solid #ffffff;
	cursor: default;
}

td.calendar {
	background: #ffffff;
	border: 1px solid #ffffff;
}

td.monthname {
	text-align: center;
	font-weight: bolder;
	color: #0000AA;
}

td.dayname {
	text-align: right;
	height: 25px;
}

tr.calendarheader {
	background: #ffffff;
	height: 25px;
}

tr.calendarfooter {
	background: #ffffff;
	height: 30px;
}

td.cout {
	background: #ffffff;
	border: 1px solid #ffffff;
	color: #000000;
	width: 25px; 
}

td.cover {
	background: #e0e0ff;
	border: 1px solid #9090ff;
	color: #9090ff;
	width: 25px;
}

td.current {
	background: #d0d0ff;
	border: 1px solid #303088;
	color: #505088;
	width: 25px;
}

a.arrow:link
{
	text-decoration: none;
	color: #000000;
}

a.arrow:hover
{
	color: #8080ff;
}

a.arrow:visited
{
	color: #000000;
}

.time {
	color: #0000AA;
	border: 0px #ffffff solid;
	width: 20px;
	text-align: center;
}

</style>

<script>

var selectedDate = new Date();

var month_desc = new Array (
	'<bean:message key="calendar.jan" />',
	'<bean:message key="calendar.feb" />',
	'<bean:message key="calendar.mar" />',
	'<bean:message key="calendar.apr" />',
	'<bean:message key="calendar.may" />',
	'<bean:message key="calendar.jun" />',
	'<bean:message key="calendar.jul" />',
	'<bean:message key="calendar.aug" />',
	'<bean:message key="calendar.sep" />',
	'<bean:message key="calendar.oct" />',
	'<bean:message key="calendar.nov" />',
	'<bean:message key="calendar.dec" />'
);

var week_desc = new Array (
	'<bean:message key="calendar.mo" />',
	'<bean:message key="calendar.tu" />',
	'<bean:message key="calendar.we" />',
	'<bean:message key="calendar.th" />',
	'<bean:message key="calendar.fr" />',
	'<bean:message key="calendar.sa" />',
	'<bean:message key="calendar.su" />'
);

function dayToPos(w)
{
	return (w > 0 ? w-- : 6);
}

function dayCount(month, year)
{
	switch(month)
	{
		case 3:
		case 5:
		case 8:
		case 10:
			return 30;
			break;
		case 1:
			return (year % 4 == 0 ? 29 : 28);
			break;
		default:
			return 31;
			break;
	}
}

function moveLeft()
{
	var year = selectedDate.getFullYear();
	var month = selectedDate.getMonth();
	month --;
	if (month < 0) {
		month = 11;
		year --;
	}
	selectedDate = new Date(year, month, 1, selectedDate.getHours(), selectedDate.getMinutes(), 0);
	calendar_init();
}

function moveRight()
{
	var year = selectedDate.getFullYear();
	var month = selectedDate.getMonth();
	month ++;
	if (month > 11) {
		month = 0;
		year ++;
	}
	selectedDate = new Date(year, month, 1, selectedDate.getHours(), selectedDate.getMinutes(), 0);
	calendar_init();
}

function shiftime(action) 
{
	var h = parseInt(document.getElementById("hours").value);
	var m = parseInt(document.getElementById("minutes").value);
	
	switch (action)
	{
		case 1:
			h ++;
			if (h > 23) h = 0;
			break;
		case 2:
			h --;
			if (h < 0) h = 23;
			break;
		case 3:
			m += 10;
			if (m > 59) m = 0;	
			break;
		case 4:
			m -= 10;
			if (m < 0) m = 50;			
			break;			
	}
	
	if (h < 10) h = '0' + h;
	if (m < 10) m = '0' + m;
	
	document.getElementById("hours").value = h;
	document.getElementById("minutes").value = m;
	
}

function cdeout(o)
{
	if (o.innerHTML == '&nbsp;') return;
	if (o.className == 'current') return;
	o.className = 'cout';
}

function cdeover(o)
{
	if (o.innerHTML == '&nbsp;') return;
	if (o.className == 'current') return;
	o.className = 'cover';
}

function formatDate(date)
{
	var y = date.getFullYear();
			
	var m = date.getMonth();
	m ++;
	if (m < 10) m = '0' + m;
	
	var d = date.getDate();
	if (d < 10) d = '0' + d;
	
	var hh = date.getHours();
	if (hh < 10) hh = '0' + hh;
	
	var mm = date.getMinutes();
	if (mm < 10) mm = '0' + mm;
	
	//var ss = date.getSeconds();
	//if (ss < 10) ss = '0' + ss;
	
	ss = '00';
	
	return(y + '-' + m + '-' + d + 'T' + hh + ':' + mm + ':' + ss);
}

function doclick(o)
{
	if (o.innerHTML == '&nbsp;') return;
	if (o.className == 'current') return;
	
	selectedDate.setDate(parseInt(o.innerHTML));
	
	//TBD
	alert('WARNING: undefined calendar target control - selected date: ' + formatDate(selectedDate));
	
}

function calendar_init(target) 
{
	var html = '';
	html += '<table class="calendar" cellspacing="0" cellpadding="1" id="calendar">';
	html += '	<tr class="calendarheader">';
	html += '		<td align="right" valign="middle" class="cout"><a class="arrow" href="javascript:moveLeft();">&#9668;</a></td>';
	html += '		<td align="center" valign="middle" colspan="5" class="monthname">';
	html += '			<span id="monthname"></span>&nbsp;<span id="year"></span>';
	html += '		</td>';
	html += '		<td align="right" valign="middle" class="cout"><a class="arrow" href="javascript:moveRight();">&#9658;</a></td>';
	html += '	</tr>';
	html += '	<tr>';
	html += '		<td class="dayname"></td>';
	html += '		<td class="dayname"></td>';
	html += '		<td class="dayname"></td>';
	html += '		<td class="dayname"></td>';
	html += '		<td class="dayname"></td>';
	html += '		<td class="dayname"></td>';
	html += '		<td class="dayname"></td>';
	html += '	</tr>';
	html += '	<tr>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '	</tr>';
	html += '	<tr>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '	</tr>';
	html += '	<tr>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '	</tr>';
	html += '	<tr>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '	</tr>';
	html += '	<tr>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '		<td align="right" valign="middle" class="cout" onclick="doclick(this)" onmouseover="cdeover(this)" onmouseout="cdeout(this)"></td>';
	html += '	</tr>';
	html += '	<tr class="calendarfooter">';
	html += '		<td align="right" valign="middle" class="cout"></td>';
	html += '		<td align="center" valign="middle" class="cout" colspan="5">';
	html += '			<a class="arrow" href="javascript:shiftime(1)">&#x25B2;</a>';
	html += '			<a class="arrow" href="javascript:shiftime(2)">&#x25BC;</a>';
	html += '			<input type="text" maxlength="2" id="hours" class="time" value="00" readonly="true"/>:<input type="text" maxlength="2" id="minutes" class="time" value="00"  readonly="true"/>';
	html += '			<a class="arrow" href="javascript:shiftime(3)">&#x25B2;</a>';
	html += '			<a class="arrow" href="javascript:shiftime(4)">&#x25BC;</a>';
	html += '		</td>';
	html += '		<td align="left" valign="middle" class="cout"></td>';
	html += '	</tr>';
	html += '</table>';
	
	document.getElementById(target).innerHTML = html;	
	
	//
	// load calendar data
	//
	
	var d  = selectedDate;
	var dow = d.getDay();
	var day = d.getDate();
	var month = d.getMonth();
	var year = d.getFullYear();
	
	document.getElementById("monthname").innerHTML = month_desc[month];
	document.getElementById("year").innerHTML = year;
	
	var h = d.getHours();
	if (h < 10) h = '0' + h;
	var m = d.getMinutes();
	while (m % 10 != 0) m --;
	if (m < 10) m = '0' + m;
	
	document.getElementById("hours").value = h;
	document.getElementById("minutes").value = m;
	
	var tb = document.getElementById("calendar").children[0];
	
	//popola le descrizioni del giorno della settimana
	for (var j = 0; j < 7; j ++)
	{
		tb.children[1].children[j].innerHTML = week_desc[j];
	}
	
	//cerca che giorno della settimana e' il primo del mese 
	var w = dow;
	for (var i = day; i > 0; i --) {
		w --;
		if (w == -1) w = 6;
	}
	
	var n = 0;
	var vpos = dayToPos(w);

	var day_limit = dayCount(month, year);
		
	//popola tabella con i giorni
	for (var i = 0; i < 5; i ++)
	{
		for (var j = 0; j < 7; j ++)
		{
			tb.children[2 + i].children[j].className = 'cout';
			
			if (n == 0 && j == vpos) n = 1;  
			
			if (n > 0 && n <= day_limit) {
				tb.children[2 + i].children[j].innerHTML = n;	
				if (n == day) {
					tb.children[2 + i].children[j].className = 'current';
				}
				n ++;
			}	
			else {
				tb.children[2 + i].children[j].innerHTML = '&nbsp;';
			}			
		}
	}
	
}

</script>

