package com.s5tech.backend.util;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.w3c.dom.DOMException;

import com.s5tech.backend.dao.GenericDao;

public class ModelViewGenerator 
{

	static final int MODE_GENERATE_JSP = 1;
	static final int MODE_GENERATE_KEYS = 2;
	static final int MODE_GENERATE_VIEWBEAN = 3;
	static final int MODE_GENERATE_MODELBEAN = 4;
	
	public static void main(String[] args) throws SecurityException, ClassNotFoundException, DOMException, SQLException {
		
		
		//String clazz = "com.s5tech.backend.model.Product";
		String clazz = "com.s5tech.backend.model.Alerts";
		
		String bean_name = "product";
		
		String key_prefix = "pdetail";
		
		//String modeltable = "alerts";
		String modeltable = "eslupdates";
		
		//int mode = MODE_GENERATE_JSP;
		//int mode = MODE_GENERATE_KEYS;		
		//int mode = MODE_GENERATE_VIEWBEAN;
		int mode = MODE_GENERATE_MODELBEAN;
		
		PrintStream out = System.out;
		
		if (mode == MODE_GENERATE_MODELBEAN) {

			Connection con = null;
			Statement st = null;				
			ResultSet rs = null;			
			
			try {
				con = new GenericDao(
						new FileInputStream("./web/WEB-INF/dao.xml")).getConnection();
				st = con.createStatement();				
				rs = st.executeQuery("select * from " + modeltable);			
				ResultSetMetaData m = rs.getMetaData();
				
				out.println("package com.s5tech.backend.model;");
				out.println();
				out.print("public class ");
				out.print(modeltable.substring(0, 1).toUpperCase() + modeltable.substring(1));
				out.println(" {");
				
				for (int i = 1; i <= m.getColumnCount(); i ++) {
					String property = m.getColumnName(i);
					int itype = m.getColumnType(i);
					String type = null;
					
					switch (itype)
        			{        			
					case Types.VARCHAR:
					case Types.CHAR:
					case Types.LONGNVARCHAR:
					case Types.NCHAR:
					case Types.NVARCHAR:
						type = "String";
						break;
																										
					case Types.BIGINT:
						type = "long";
						break;
						
					case Types.INTEGER:
						type = "int";
						break;
						
					case Types.SMALLINT:
					case Types.TINYINT:
						type = "short";
						break;
				
					case Types.DECIMAL:
					case Types.FLOAT:
					case Types.NUMERIC:
					case Types.REAL:
					case Types.DOUBLE:
						type = "double";		
						break;
					
						
					case Types.BIT:
					case Types.BOOLEAN:
						type = "boolean";		
						break;
					
					case Types.DATE:
					case Types.TIME:
					case Types.TIMESTAMP:
						type = "java.util.Date";
						break;
						
					case Types.BLOB:
					case Types.BINARY:
					case Types.CLOB:
					case Types.NCLOB:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
						type = "byte[]";
						break;
						
					case Types.ARRAY:
					case Types.DATALINK:
					case Types.DISTINCT:
					case Types.JAVA_OBJECT:
					case Types.OTHER:
					case Types.REF:
					case Types.ROWID:
					case Types.SQLXML:
					case Types.STRUCT:
						type = "String";
						break;
					
					default:
						type = "String";
        			}
        								
					out.println("private " + type + " "+ property + ";");					
					out.println("public " + type + " get" 
							+ property.substring(0, 1).toUpperCase() + property.substring(1) 
							+ "() { return this." + property + "; }");
					out.println("public void set" 
							+ property.substring(0, 1).toUpperCase() + property.substring(1) 
							+ "(" + type + " value) { this." + property + " = value; }");
					out.println();	
				}				
				out.println("}");
				out.println();
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			finally {
				if (rs != null) try { rs.close(); } catch (Throwable ignored) {}
				if (st != null) try { st.close(); } catch (Throwable ignored) {}
				if (con != null) try { con.close(); } catch (Throwable ignored) {}
			}
		}
		else {
		
			Field [] fields = Class.forName(clazz).getDeclaredFields();
	
			for (Field field : fields)
			{
				String property = field.getName();
				
				if (Modifier.isStatic(field.getModifiers())) continue;
				
				switch (mode) {
				
				case MODE_GENERATE_JSP:
					
					if (field.getType() == boolean.class)
					{
						out.println("<tr>");
						out.println("  	<td>");
						out.println("		<span style=\"font-weight: normal;\">");
						out.println("			<bean:message key=\"" + key_prefix + "." + property + "\" />");
						out.println("		</span>");
						out.println("	</td>");
						out.println("	<td align=\"right\">");
						out.println("		<logic:equal name=\"" + bean_name + "\" property=\"" + property + "\" value=\"true\">");
						out.println("			<span style=\"color: green; font-size: 30px;\">&bull;</span>");
						out.println("		</logic:equal>");
						out.println("		<logic:notEqual name=\"" + bean_name + "\" property=\"" + property + "\" value=\"true\">");
						out.println("			<span style=\"color: red; font-size: 30px;\">&bull;</span>");
						out.println("		</logic:notEqual>");
						out.println("	</td>");
						out.println("</tr>");
					}
					else
					{
						out.println("<tr>");
						out.println("	<td><span style=\"font-weight: normal;\">");
						out.println("		<bean:message key=\"" + key_prefix + "." + property + "\" /></span>");
						out.println("	</td>");
						out.println("	<td align=\"right\">");
						out.println("		<bean:write name=\"" + bean_name + "\" property=\"" + property + "\" />");
						out.println("	</td>");
						out.println("</tr>");
					}
					
					break;
				
				case MODE_GENERATE_KEYS:
					
					out.print(key_prefix);
					out.print('.');
					out.print(property);
					out.print(" = ");
					out.println();
					
					break;
				
				case MODE_GENERATE_VIEWBEAN:
					
					out.println("private String " + property + " = null;");
					out.println("public String get" 
							+ property.substring(0, 1).toUpperCase() + property.substring(1) 
							+ "() { return this." + property + "; }");
					out.println("public void set" 
							+ property.substring(0, 1).toUpperCase() + property.substring(1) 
							+ "(String value) { this." + property + " = (value != null ? value.trim() : null); }");
					out.println();
					
					break;
								
				}
	
			}
			
		}

	}

}
