<%@ page contentType="application/json" %>
<%@ page import="org.apache.struts.action.ActionForm" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.lang.reflect.Field" %>
<%
	Enumeration e = request.getAttributeNames();
	while (e.hasMoreElements()) {		
		String name = e.nextElement().toString();
		Object o = request.getAttribute(name);
		if (o instanceof ActionForm) {
			out.write("{ ");
			out.write("\n\t\"formBean\": \"" + name + "\", "); 			
			for (Field field : o.getClass().getDeclaredFields()) {				
				out.write("\n\t");
				out.write("\"");
				out.write(field.getName());
				out.write("\": ");
				out.write("\"");
				field.setAccessible(true);
				Object x = field.get(o);
				out.write(
					(x == null ? "" : x.toString()
						    .replace('\t', ' ')
							.replace('\n', ' ')
							.replace('\r', ' ')
							.replace('"', '\'')
							.trim())
							);
				field.setAccessible(false);				
				out.write("\"");
			}
			out.write("\n}");
			break;
		}
	}	
	
%>
