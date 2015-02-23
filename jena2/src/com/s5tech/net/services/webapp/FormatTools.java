/**********************************************************************************
 *
 *           S5TECH(c) NETWORK APPLICATION DOCUMENTATION AND LICENSE
 *                        Version 1.6, September 2014
 *                          http://www.s5tech.com/
 *
 * Permission to copy, modify, and distribute this software and its documentation,
 * with or without modification, for any purpose and without fee or royalty is
 * hereby granted.
 * 
 * THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 * NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 * THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 * PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 * 
 * COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 * CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 * 
 * The name and trademarks of  copyright holders may NOT be used in advertising or
 * publicity pertaining to the software without specific, written prior permission.
 * Title to copyright in this software and any associated documentation will at
 * all times remain with copyright holders.
 * 
 * FOR INFORMATION ABOUT OBTAINING, INSTALLING AND RUNNING THIS SOFTWARE WRITE AN
 * EMAIL TO assist@s5tech.com
 * 
 * S5Tech Development Team 2015-01-15
 * S5Tech S.P.A, Via Caboto 10, 20100 Legnano - Italy
 * 		
 *********************************************************************************/
 
package com.s5tech.net.services.webapp;

public class FormatTools {
	
	public String formatResponse(String rootElement, Object data, String rct, boolean flat)
	{		
		try {
			if (rct.equals("text/xml")) {
				return formatXml(rootElement, data, flat);
			}
			else if (rct.equals("application/json")) {
					return formatJson(rootElement, data, flat);
			}
			else if (rct.indexOf("text/plain") >= 0) {
				return formatText(data);
			}
			else { 
				// this is default
				return formatXml(rootElement, data, flat);
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
			return null;
		}		
	}
	
	
	private String formatXml(String rootElement, Object data, boolean flat)
	{
		if (!flat)
		{
			StringBuffer response =  new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>\n");
			response.append("<").append(rootElement).append(">\n");
			
			if (data != null) 
			{
				if (data instanceof String[])
				{
					String [] lines = String[].class.cast(data);
					for (String line : lines)
					{
						response.append("\t<entry>");
						for (String params : line.split(";"))
						{
							String [] kv = params.split("=");
							String key = kv[0].trim().replace(' ', '-');
							String value = (kv.length > 1 ? kv[1].trim() : "");
							response.append("\t\t<").append(key).append(">")
									.append(value).append("</").append(key).append(">\n");
						}
						response.append("\t</entry>");
					}
				}
				else if (data instanceof String)
				{
					String line = String.class.cast(data);				
					if (line.indexOf('\n') > 0) line = line.replace('\n', ';');
					if (line.indexOf('=') >= 0)
					{
						for (String params : line.split(";"))
						{
							String [] kv = params.split("=");
							String key = kv[0].trim().replace(' ', '-');
							String value = (kv.length > 1 ? kv[1].trim() : "");
							response.append("\t<").append(key).append(">")
									.append(value).append("</").append(key).append(">\n");
						}
					}
					else 
					{
						response.append("\t<result>")
							.append(line.trim()).append("</result>\n");
					}
				}
			}
			
			response.append("</").append(rootElement).append(">\n");
			
			return response.toString();		
		}
		else {
			
			StringBuffer response =  new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>\n");
			response.append("<").append(rootElement).append(">\n");
			
			if (data != null) 
			{
				if (data instanceof String[])
				{
					String [] lines = String[].class.cast(data);				
					String [] head = lines[0].split(";");				
					for (int i = 0; i < head.length; i ++) {
						head[i] = head[i].trim().replace(' ', '-'); 
					}
					
					for (int i = 1; i < lines.length; i ++) {
						String line = lines[i];
						response.append("\t<entry>");
						int count = 0;
						for (String value : line.split(";")) {
							if (count >= head.length) break;
							response.append("\t\t<").append(head[count]).append(">")
									.append(value
											.replace('<',  ' ')
											.replace('>', ' ')
											.replace('&', ' ')
											.replace('\t', ' ')
											.replace('\n', ' ')
											.replace('\r', ' ')
											.trim())
									.append("</").append(head[count]).append(">\n");
							count ++;
						}
						response.append("\t</entry>");
					}
				}
				else if (data instanceof String)
				{
					String line = String.class.cast(data);				
					if (line.indexOf('\n') > 0) line = line.replace('\n', ';');
					if (line.indexOf('=') >= 0)
					{
						for (String params : line.split(";"))
						{
							String [] kv = params.split("=");
							String key = kv[0].trim().replace(' ', '-');
							String value = (kv.length > 1 ? kv[1].trim() : "");
							response.append("\t<").append(key).append(">")
									.append(value).append("</").append(key).append(">\n");
						}
					}
					else 
					{
						response.append("\t<result>")
							.append(line.trim()).append("</result>\n");
					}
				}
			}
			
			response.append("</").append(rootElement).append(">\n");
			
			return response.toString();
			
		}
			
	}
	
	
	private String formatJson(String rootElement, Object data, boolean flat)
	{
		if (!flat)
		{			
			StringBuffer response =  new StringBuffer();
			
			response.append("{ \"").append(rootElement).append("\": ");
			
			if (data != null) 
			{
				if (data instanceof String[])
				{
					int linecount = 0;
					String [] lines = String[].class.cast(data);	
					response.append(" [");
					for (String line : lines)
					{
						if (linecount > 0) response.append(",");					
						response.append(" {");
						int count = 0;
						for (String params : line.split(";"))
						{
							String [] kv = params.split("=");
							String key = kv[0].trim().replace(' ', '_').replace('"', '_').replace('\'', '_');
							String value = (kv.length > 1 ? kv[1].trim().replace('"', ' ').replace('\'', ' ').replace('\r', ' ').replace('\n', ' ') : "");
							if (count > 0 ) response.append(", ");
							response.append("\"").append(key).append("\": \"").append(value).append("\"");
							count ++;
						}
						linecount ++;
						response.append("}");
					}
					response.append(" ]");
				}
				else if (data instanceof String)
				{
					String line = String.class.cast(data);				
					if (line.indexOf('\n') > 0) line = line.replace('\n', ';');				
					if (line.indexOf('=') >= 0)
					{
						int count = 0;
						response.append(" { ");
						for (String params : line.split(";"))
						{
							String [] kv = params.split("=");
							String key = kv[0].trim().replace(' ', '-').replace('\'', '_');
							String value = (kv.length > 1 ? kv[1].trim().replace('"', ' ').replace('\'', ' ').replace('\r', ' ').replace('\n', ' ') : "");
							if (count > 0 ) response.append(", ");
							response.append("\"").append(key).append("\": \"").append(value).append("\"");
							count ++;
						}
						response.append("} ");
					}
					else 
					{
						response.append("\"").append(line.trim().replace('"', ' ').replace('\'', ' ').replace('\r', ' ').replace('\n', ' ')).append("\"");
					}
				}
			}
			else
			{
				response.append("\"\""); 
			}
			
			response.append(" }");
			
			//Log.info("JSON <<\n{}\n>> JSON", response.toString());
			
			return response.toString();
			
		}
		else {
			
			StringBuffer response =  new StringBuffer();
			
			response.append("{ \"").append(rootElement).append("\": ");
			
			if (data != null) 
			{
				if (data instanceof String[])
				{
					int linecount = 0;
					String [] lines = String[].class.cast(data);
					String [] head = lines[0].split(";");				
					for (int i = 0; i < head.length; i ++) {
						head[i] = head[i].trim()
								.replace(' ', '_')
								.replace(':', '_')
								.replace(' ', '_')
								.replace('"', '_')
								.replace('\'', '_'); 
					}
					
					response.append(" [");
					for (int i = 1; i < lines.length; i ++)
					{
						String line = lines[i];
						if (linecount > 0) response.append(",");					
						response.append(" {");
						int count = 0;
						for (String value : line.split(";"))
						{	
							if (count > 0 ) response.append(", ");
							response.append("\"").append(head[count]).append("\": \"")
									.append(value
											.replace('"', ' ')
											.replace('\'', ' ')
											.replace('\r', ' ')
											.replace('\n', ' ')
											.replace('\t', ' ')										
											.trim())
									.append("\"");
							count ++;
						}
						linecount ++;
						response.append("}");
					}
					response.append(" ]");
				}
				else if (data instanceof String)
				{
					String line = String.class.cast(data);				
					if (line.indexOf('\n') > 0) line = line.replace('\n', ';');				
					if (line.indexOf('=') >= 0)
					{
						int count = 0;
						response.append(" { ");
						for (String params : line.split(";"))
						{
							String [] kv = params.split("=");
							String key = kv[0].trim().replace(' ', '-').replace('\'', '_');
							String value = (kv.length > 1 ? kv[1].trim().replace('"', ' ').replace('\'', ' ').replace('\r', ' ').replace('\n', ' ') : "");
							if (count > 0 ) response.append(", ");
							response.append("\"").append(key).append("\": \"").append(value).append("\"");
							count ++;
						}
						response.append("} ");
					}
					else 
					{
						response.append("\"").append(line.trim().replace('"', ' ').replace('\'', ' ').replace('\r', ' ').replace('\n', ' ')).append("\"");
					}
				}
			}
			else
			{
				response.append("\"\""); 
			}
			
			response.append(" }");
			
			//Log.info("JSON <<\n{}\n>> JSON", response.toString());
			
			return response.toString();
			
		}		
		
	}
	
	private String formatText(Object data)
	{
		StringBuffer response =  new StringBuffer();
		
		if (data != null) 
		{
			if (data instanceof String[])
			{
				String [] lines = String[].class.cast(data);
				for (String line : lines)
				{
					response.append(line).append('\n');
				}
			}
			else if (data instanceof String)
			{
				response.append(String.class.cast(data));
			}
		}
		
		return response.toString();
		
	}
	

}
