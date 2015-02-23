package com.s5tech.backend.servlet.helpers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.s5tech.backend.util.Base64Encoder;
import com.s5tech.backend.view.ViewFormatter;

public class OutputFormatter {
	
	private int mode = 2;
	
	public OutputFormatter(HttpServletRequest request)
	{
		if (request == null) {
			mode = 3;
			return; 
		}
		
		if (request.getParameter("format") != null && request.getParameter("format").equalsIgnoreCase("json")
				|| (request.getHeader("Content-Type") != null && request.getHeader("Content-Type").indexOf("json") >= 0)) {
			mode = 1; 
		}
		if (request.getParameter("format") != null && request.getParameter("format").equalsIgnoreCase("xml")
				|| (request.getHeader("Content-Type") != null && request.getHeader("Content-Type").indexOf("xml") >= 0)) { 
			mode = 2;
		}
		else {
			mode = 3;
		}		
	}
	
	public String getContentType()
	{
		switch (mode) 
		{
		case 1:
			return ("application/json");			
		case 2:
			return ("text/xml");			
		case 3:
		default:
			return ("text/plain");
		}		
	}
	
	public String formatList(List<?> lst) throws IOException {

		int count = 0;		
		StringBuffer buffer = new StringBuffer();
	
		switch (mode) 
		{
		case 1:
			buffer.append("[ ");
			break;
		case 2:
			buffer.append("<list>");
			break;
		case 3:
			break;
		default:
			throw new IOException("invalid serialization mode: " + mode);
		}		
		
		Iterator<?> it = lst.iterator();
		
		while (it.hasNext()) {
			count ++;
			buffer.append(format(it.next()));
			switch (mode) 
			{
			case 1:
				if (count < lst.size()) buffer.append(", ");
				break;
			case 2:				
				break;
			case 3:
				buffer.append("\n");
				break;
			}	
		}
		
		switch (mode) 
		{
		case 1:
			buffer.append(" ]");
			break;
		case 2:
			buffer.append("</list>");
			break;
		case 3:
			break;
		}
		
		return buffer.toString();
	}
	
	public String formatPOJO (Object o) throws IOException
	{

		StringBuffer buffer = new StringBuffer();
		
		Method[] m = o.getClass().getMethods();
			
		String objectname = o.getClass().getName();
		if (objectname.lastIndexOf('.') > 0) 
			objectname = objectname.substring(
					objectname.lastIndexOf('.') + 1);
		
		switch (mode) 
		{
		case 1:
			buffer.append("{ ");
			break;
		case 2:
			buffer.append("<").append(objectname).append(">");
			break;
		case 3:
			break;
		default:
			throw new IOException("invalid serialization mode: " + mode);
		}		
		
		for (int i = 0; i < m.length; i ++) 
		{
			Method method = m[i];
			if (Modifier.isStatic(method.getModifiers())) continue;
			if (Modifier.isTransient(method.getModifiers())) continue;
			String name = method.getName();
			if (!name.startsWith("get") || name.equals("get")) continue;
			if (method.getParameterTypes() != null && method.getParameterTypes().length > 0) continue;
			if (method.getReturnType().equals(Void.TYPE)) continue;
			if (method.getReturnType() == Class.class) continue;
			
			String field = name.substring(3, 4).toLowerCase() + name.substring(4);   
			
			switch (mode) 
			{
			case 1:
				buffer.append("\"").append(field).append("\": \"");
				break;
			case 2:
				buffer.append("<").append(field).append(">");
				break;
			}
			
			try 
			{
				String value;
				Object returned = method.invoke(o);
				
				if (returned == null) {
					value = "";
				}					
				else if (returned instanceof String) {
					value = String.class.cast(returned);
				}
				else if (returned instanceof byte[]) {
					value = new String(Base64Encoder.encode(byte[].class.cast(returned)));
				}
				else if (returned instanceof char[]) {
					value = new String(char[].class.cast(returned));
				}
				else {
					value = new ViewFormatter().format(returned);
				}
				
				switch (mode) 
				{
				case 1:
					buffer.append(value
							.replaceAll("\\\"", "'")
							.replace('\t', ' ')
							.replace('\n', ' ')
							.replace('\r', ' ')
							.trim());
					break;
				case 2:
					buffer.append(value
							.replaceAll("\\&", "&amp;")
							.replaceAll("\\<", "&lt;")
							.replaceAll("\\>", "&gt;")
							.replace('\t', ' ')
							.replace('\n', ' ')
							.replace('\r', ' ')
							.trim());
					break;
				case 3:
					buffer.append(value
							.replace('\t', ' ')
							.replace('\n', ' ')
							.replace('\r', ' ')
							.trim())
							.append('\t'); //separator always tab					
					break;
				}
				
			}
			catch (Throwable ignored) {}
						
			switch (mode) 
			{
			case 1:
				buffer.append("\"");
				if (i < m.length -1) buffer.append(", ");
				break;
			case 2:
				buffer.append("</").append(field).append(">");
				break;	
			case 3:					
				break;
			}
			
		}
		
		switch (mode) 
		{
		case 1:
			buffer.append("} ");
			break;
		case 2:
			buffer.append("</").append(objectname).append(">");
			break;				
		}
		
		return buffer.toString();
	}
	
	public String format(Object o) throws IOException {

		if (o == null) return "";
		
		if (o instanceof String) return String.class.cast(o);
		
		if (o instanceof List) return formatList(List.class.cast(o));
		
		return formatPOJO(o);		
		
	}

}
