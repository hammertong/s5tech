package com.s5tech.backend.dao;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class GenericDao {
	
	private static final Log log = LogFactory.getLog(GenericDao.class);
		
	private Document config = null;
	
	private DataSource datasource = null;
		
	public GenericDao()
	{
		if (log.isTraceEnabled()) 
			log.trace("initializing DAO ...");
	}
	
	public GenericDao(InputStream in)
	{
		this();
		load(in);
	}
	
	public boolean load (InputStream in)
	{
		long start = (log.isTraceEnabled() ? System.currentTimeMillis() : 0);
		
		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance( );
	        factory.setValidating(false);
	        //factory.setNamespaceAware(false);
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        config = builder.parse(in);	        
	        return true;
		}
		catch (Throwable t) 
		{
			log.error("creating generic DAO - ", t);
			return false;
		}	
		finally
		{
			if (log.isTraceEnabled()) log.trace("DAO performance --------> load ==> " 
					+ (System.currentTimeMillis() - start) + " ms");
		}
		
	}
	
	public Connection getConnection() throws ClassNotFoundException, DOMException, SQLException
	{	
    	String url = null;
    	String username = null;
    	String password = null;
		Properties jdbcprops = new Properties();
        NodeList list = config.getElementsByTagName("jdbc").item(0).getChildNodes();    
        
        for (int i = 0; i < list.getLength(); i ++) 
        {
        	String name = list.item(i).getNodeName();
        	if (name.startsWith("#")) continue;
        	
        	String text = (list.item(i).getTextContent() != null ?        			
        			list.item(i).getTextContent()
	        			.replace('\r', ' ')
	        			.replace('\n', ' ')
	        			.replace('\t', ' ')
	        			.trim()
        			: "");
        	
        	if (text == null) throw new SQLException(
        			"no value defined for jdbc property '" + name + "'");
        	
        	if (name.equals("datasource") && datasource == null) 
        	{
        		try {
        			InitialContext ctx = new InitialContext();
        			datasource = (DataSource) ctx.lookup (text);
        		}
        		catch (NamingException nx) {
        			datasource = null;
        			log.error("cannot lookup datasource " + text, nx);
        		}
        	}        	
        	else if (name.equals("pool") && datasource == null) 
        	{
        		try {
        			
        			String poolclazz = list.item(i).getAttributes().getNamedItem("class").getNodeValue();
        			
        			log.trace("creating new datasource pool " + poolclazz);
        			
        			Class<?> clazz = Class.forName(poolclazz);
        			Object pool = clazz.newInstance();        			
        			NodeList sublist = list.item(i).getChildNodes();
        			
        			Method [] mlist = clazz.getMethods();
        			        			
        			for (int n = 0; n < sublist.getLength(); n ++) {
        				
        				String property = sublist.item(n).getNodeName();
        				String value = sublist.item(n).getTextContent()
        	        			.replace('\r', ' ')
        	        			.replace('\n', ' ')
        	        			.replace('\t', ' ')
        	        			.trim();
        					
    					String methodMatch = "set" + property; 
	        			
	        			for (int k = 0; k < mlist.length; k ++)
	        			{	       
	        				Method method = mlist[k];
	        				
	        				if (method.getName().equalsIgnoreCase(methodMatch)) {
	        					
	        					if (Modifier.isStatic(method.getModifiers())) continue;
	        					if (method.getParameterTypes().length != 1) continue; 
	        					
	        					Class<?> arg = method.getParameterTypes()[0];
	        					
	        					try {
		        					if (arg == String.class) {
		        						method.invoke(pool, value);
		        					}
		        					else if (arg == boolean.class) {
		        						method.invoke(pool, Boolean.parseBoolean(value));
		        					}
		        					else if (arg == int.class) {
		        						method.invoke(pool, Integer.parseInt(value));
		        					}
		        					else if (arg == long.class) {
		        						method.invoke(pool, Long.parseLong(value));
		        					}
		        					else if (arg == double.class) {
		        						method.invoke(pool, Double.parseDouble(value));
		        					}
	        					}
	        					catch (Throwable invokExcp) {	
	        						
	        						log.error("cannot invoke " + poolclazz 
	        								+ "." + method.getName() + "() - ", invokExcp);
	        						
	        					}
	        				}	        				
	        				
	        			}    	        		
        				
        			}
        			 
        			datasource = DataSource.class.cast(pool);
        			
        			if (list.item(i).getAttributes().getNamedItem("test") != null) {
        				
        				String sql = list.item(i).getAttributes().getNamedItem("test").getNodeValue();
        				
        				if (sql != null) {
        					
        					Connection c = null;
        		        	PreparedStatement ps = null;
        		        	ResultSet rs = null;        		        	
        		        	try{
        		        		c = datasource.getConnection();
        		        	    ps = c.prepareStatement(sql);
        		        	    rs = ps.executeQuery();
        		        	    if (rs.next()){}
        		        	    log.trace("datasouce test ok!");
        		        	}catch(Throwable t){
        		        	    log.error("datasource test failed! - ", t);
        		        	}finally{
        		        		if (rs != null) try { rs.close(); } catch (Throwable ignored) {}
        		        		if (ps != null) try { ps.close(); } catch (Throwable ignored) {}
        		        		if (c != null) try { c.close(); } catch (Throwable ignored) {}        		        			        		
        		        	}
        					
        				}
        				
        			}
        			
        			log.trace("connection pool " + poolclazz + " created!");
        			
        		}
        		catch (Throwable t) {
        			datasource = null;
        			log.error("connection pool creation failure", t);
        		}
        	}
        	
        	if (datasource != null) {
        		return datasource.getConnection();
        	}
        	
        	else if (name.equals("driver")) 
        	{
        		if (datasource != null)  Class.forName(text);
        	}
        	else if (name.equals("url")) 
        	{
        		url = text;
        	}
        	else
        	{        		
        		if (name.equals("username")) {
        			username = text;
        		}
        		else if (name.equals("password")) {
        			password = text;
        		}
        		
        		jdbcprops.setProperty(name, text);
        	}
        }
        
        if (datasource != null && username != null && password != null) {
        	return datasource.getConnection(username, password);
        }
        else if (datasource != null) {
        	return datasource.getConnection();
        }
        else {
        	return DriverManager.getConnection (url, jdbcprops);
        }
	    
	}
		
	public List<Object> executeNamedQuery(String name, Object... params)
			throws Exception
	{
		long start = (log.isTraceEnabled() ? System.currentTimeMillis() : 0);
		
		Class<?> clazz = null; 
		NodeList list = config.getElementsByTagName("query"); 
		
		String sql = null;
        
		boolean replaced = false;
		
		for (int i = 0; i < list.getLength(); i ++) 
        {
			clazz = null;
			NamedNodeMap map = list.item(i).getAttributes();			
			if (!name.equalsIgnoreCase(map.getNamedItem("name").getNodeValue())) continue;			
			sql = list.item(i).getTextContent().replace('\t', ' ').trim();
			clazz = (map.getNamedItem("class") != null ? Class.forName(map.getNamedItem("class").getNodeValue()) : null);
			replaced = (map.getNamedItem("replaced") != null && map.getNamedItem("replaced").getNodeValue().equals("true"));
			break;
        }
		
        if (sql == null) throw new Exception("named query " + name + " not found");
        
        if (replaced && params != null && params.length > 0) {
        	int paramcount = 0;
        	for (Object value : params) 
            {
        		paramcount ++;
        		sql = sql.replaceAll("\\{" + paramcount + "\\}", value.toString());
            }        	
        	params = null;
        }   
        
//        if (log.isTraceEnabled()) log.trace("DAO performance --------> query(1)::looked-up ==> " 
//				+ (System.currentTimeMillis() - start) + " ms");
    	
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultset = null;
        
        try 
        {
        	connection = getConnection();
        	
//        	if (log.isTraceEnabled()) log.trace("DAO performance --------> query(2)::connected ==> " 
//					+ (System.currentTimeMillis() - start) + " ms");
        	
        	statement = connection.prepareStatement(sql);
        	
        	int paramcount = 0;
            
        	if (params != null && params.length > 0)
        	{
        		//statement.clearParameters();
        		
	        	for (Object value : params) 
	            {        		    			
	        		if (value == null) {
	        			statement.setNull(++paramcount, Types.NULL);
	        		}	        		
	        		else if (value instanceof String) {
	    				statement.setString(++paramcount, String.class.cast(value)); 
	    			}
	    			else if (value instanceof Short) {
	    				statement.setShort(++paramcount, Short.class.cast(value)); 
	    			}
	    			else if (value instanceof Integer) {
	    				statement.setInt(++paramcount, Integer.class.cast(value)); 
	    			}
	    			else if (value instanceof Long) {
	    				statement.setLong(++paramcount, Long.class.cast(value)); 
	    			}
	    			else if (value instanceof Float) {
	    				statement.setFloat(++paramcount, Float.class.cast(value)); 
	    			}
	    			else if (value instanceof Double) {
	    				statement.setDouble(++paramcount, Double.class.cast(value)); 
	    			}
	    			else if (value instanceof Boolean) {
	    				statement.setBoolean(++paramcount, Boolean.class.cast(value)); 
	    			}
	    			else if (value instanceof byte[]) {
	    				statement.setBytes(++paramcount, byte[].class.cast(value)); 
	    			}
	    			else if (value instanceof Date) {
	    				statement.setTimestamp(++paramcount, new Timestamp(Date.class.cast(value).getTime())); 
	    			}
	    			else {
	    				statement.setString(++paramcount, value.toString()); 
	    			}        			
	            }
        	}
        	
        	if (log.isTraceEnabled()) traceCommand(sql, params);
        	
//        	if (log.isTraceEnabled()) log.trace("DAO performance --------> query(3)::st-ready ==> " 
//					+ (System.currentTimeMillis() - start) + " ms");
        	
        	resultset = statement.executeQuery();
        	
//        	if (log.isTraceEnabled()) log.trace("DAO performance --------> query(4)::executed ==> " 
//					+ (System.currentTimeMillis() - start) + " ms");
        	
        	Map<String, Method> setterMap = new HashMap<>();
        	ResultSetMetaData md = resultset.getMetaData();
        	
        	if (clazz != null)
        	{
	        	for (int i = 1; i <= md.getColumnCount(); i ++) {
	    			String columnName = md.getColumnName(i);
	        		for (Method m : clazz.getDeclaredMethods()) {
	        			if (m.getName().startsWith("set")) {
	        				if (Modifier.isStatic(m.getModifiers())) continue;
	        				if (m.getParameterTypes() == null || m.getParameterTypes().length != 1) continue;
	        				String s = m.getName().substring(3);
	        				if (s.length() == 0) continue;
	        				if (s.equalsIgnoreCase(columnName)) {
	        					setterMap.put(columnName, m);
	        					break;
	        				}
	        			}
	        		}
	        	}
        	}
	        	
        	List<Object> returnvalue = new ArrayList<>();
        	
//        	if (log.isTraceEnabled()) log.trace("DAO performance --------> query(5)::mapped ==> " 
//					+ (System.currentTimeMillis() - start) + " ms");
        	
        	while (resultset.next()) 
        	{
//        		rowcount ++;        		
//        		if (from > rowcount) continue;
//        		if (limit > 0 && from + limit < rowcount) break;

        		if (clazz == null) {        			
        			//if no binding specified, returns set of first fields
        			String value = resultset.getString(1);
        			if (value != null) returnvalue.add(value);        		
        			continue;
        		}
        		
        		Object bean = clazz.newInstance();
        		returnvalue.add(bean);

        		for (int i = 1; i <= md.getColumnCount(); i ++) 
        		{
        			String columnName = md.getColumnName(i);
        			
        			Method setter = setterMap.get(columnName);
        			
        			if (setter == null) continue;
        			
        			int itype = md.getColumnType(i); 
        			
        			// alcuni check per wrappare al setter più vicino
        			
        			boolean convertBoolean = false;
        			Class<?> setterarg = setter.getParameterTypes()[0];	
        			
        			if (setterarg == Boolean.class 
        					|| setterarg == boolean.class) {
        				if (itype != Types.BIT 
        					&& itype != Types.BOOLEAN) {
        					convertBoolean = true;
        				}
        			}
        			
        			if (setterarg == String.class) { 
    					if (itype != Types.VARCHAR 
    							&& itype != Types.NVARCHAR) {
    						itype = Types.VARCHAR;
    					}
        			}
        			
        			switch (itype)
        			{
        			case Types.NULL:
						//nothing to do ...
						break;
					
					case Types.VARCHAR:
					case Types.CHAR:
					{
						String value = resultset.getString(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
						
					case Types.LONGNVARCHAR:
					case Types.NCHAR:
					case Types.NVARCHAR:
					{
						String value = resultset.getNString(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
																										
					case Types.BIGINT:
					{
						Long value = resultset.getLong(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
						
					case Types.INTEGER:
					{	
						Object value = resultset.getInt(i);
						if (value != null) {
							if (convertBoolean) {
								if (Integer.class.cast(value).intValue() == 0) {
									setter.invoke(bean, false);
								}
								else {
									setter.invoke(bean, true);
								}
							}
							else {
								setter.invoke(bean, Integer.class.cast(value));
							}
						}
					}
					break;
						
					case Types.SMALLINT:
					case Types.TINYINT:
					{
						Object value = resultset.getShort(i);
						if (value != null) {
							if (convertBoolean) {
								if (Integer.class.cast(value).intValue() == 0) {
									setter.invoke(bean, false);
								}
								else {
									setter.invoke(bean, true);
								}
							}
							else {
								setter.invoke(bean, value);
							}
						}
						if (value != null) setter.invoke(bean, Short.class.cast(value));
					}
					break;
				
					case Types.DECIMAL:
					{
						if (resultset.getBigDecimal(i) != null) {
							setter.invoke(bean, resultset.getBigDecimal(i).doubleValue());						
						}
					}					
					break;
						
					case Types.DOUBLE:
					{
						Double value = resultset.getDouble(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
					
					case Types.FLOAT:
					{
						Float value = resultset.getFloat(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
						
					case Types.NUMERIC:
					case Types.REAL:
					{
						Float value = resultset.getFloat(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
						
					case Types.BIT:
					case Types.BOOLEAN:
					{
						Boolean value = resultset.getBoolean(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
					
					case Types.DATE:
					{
						Date value = resultset.getDate(i);
						if (value != null) setter.invoke(bean, value);
					}
					break;
						
					case Types.TIME:
					{
						Date value = resultset.getTime(i);
						if (value != null) setter.invoke(bean, new Date(value.getTime()));
					}
					break;
						
					case Types.TIMESTAMP:
					{
						Timestamp value = resultset.getTimestamp(i);
						if (value != null) setter.invoke(bean, new Date(value.getTime()));
					}
					break;										
						
					case Types.BLOB:
					case Types.BINARY:
					case Types.CLOB:
					case Types.NCLOB:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
					{
						byte[] value = resultset.getBytes(i);
						if (value != null) setter.invoke(bean, value);
					}
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
					{
						if (resultset.getObject(i) != null) {
							setter.invoke(bean, resultset.getObject(i).toString());
						}
					}
					break;
					
					default:
        			
        			}
        		}
	        	
        	}
        	
        	return returnvalue;
        	
        }
        catch (Throwable t) 
        {
        	log.error("running query - ", t);
        	return null;
        	
        }
        finally {
        	if (resultset != null) try { resultset.close(); } catch (Throwable ignored) {}
        	if (statement != null) try { statement.close(); } catch (Throwable ignored) {}
        	if (connection != null) try { connection.close(); } catch (Throwable ignored) {}
        	
        	if (log.isTraceEnabled()) log.trace("DAO performance --------> query ==> " 
					+ (System.currentTimeMillis() - start) + " ms");
        }
        
	}
	
	public int executeNamedCommand(String name, Object... params)
			throws Exception
	{
		long start = (log.isTraceEnabled() ? System.currentTimeMillis() : 0);
		
		NodeList list = config.getElementsByTagName("command"); 
		
		String sql = null;
        
		boolean replaced = false;
		
		for (int i = 0; i < list.getLength(); i ++) 
        {
			NamedNodeMap map = list.item(i).getAttributes();			
			if (!name.equalsIgnoreCase(map.getNamedItem("name").getNodeValue())) continue;			
			sql = list.item(i).getTextContent().replace('\t', ' ').trim();
			replaced = (map.getNamedItem("replaced") != null && map.getNamedItem("replaced").getNodeValue().equals("true"));
			break;
        }
		
        if (sql == null) throw new Exception("named command " + name + " not found");
        
        if (replaced && params != null && params.length > 0) {
        	int paramcount = 0;
        	for (Object value : params) 
            {
        		paramcount ++;
        		sql = sql.replaceAll("\\{" + paramcount + "\\}", value.toString());
            }        	
        	params = null;
        }
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try 
        {
        	connection = getConnection();
        	
        	statement = connection.prepareStatement(sql);
        
        	int paramcount = 0;
            
        	if (params != null && params.length > 0)
        	{
        		//statement.clearParameters();
        		
	        	for (Object value : params) 
	            {       	        		
	        		if (value == null) {
	        			statement.setNull(++paramcount, Types.NULL);
	        		}	        		
	        		else if (value instanceof String) {
	    				statement.setString(++paramcount, String.class.cast(value)); 
	    			}
	    			else if (value instanceof Short) {
	    				statement.setShort(++paramcount, Short.class.cast(value)); 
	    			}
	    			else if (value instanceof Integer) {
	    				statement.setInt(++paramcount, Integer.class.cast(value)); 
	    			}
	    			else if (value instanceof Long) {
	    				statement.setLong(++paramcount, Long.class.cast(value)); 
	    			}
	    			else if (value instanceof Float) {
	    				statement.setFloat(++paramcount, Float.class.cast(value)); 
	    			}
	    			else if (value instanceof Double) {
	    				statement.setDouble(++paramcount, Double.class.cast(value)); 
	    			}
	    			else if (value instanceof Boolean) {
	    				statement.setBoolean(++paramcount, Boolean.class.cast(value)); 
	    			}
	    			else if (value instanceof byte[]) {
	    				statement.setBytes(++paramcount, byte[].class.cast(value)); 
	    			}
	    			else if (value instanceof Date) {
	    				statement.setTimestamp(++paramcount, new Timestamp(Date.class.cast(value).getTime())); 
	    			}
	    			else {
	    				statement.setString(++paramcount, value.toString()); 
	    			}        			
	            }
        	}
        	
        	if (log.isTraceEnabled()) traceCommand(sql, params);
        	
        	return statement.executeUpdate();
        	
        }
        catch (Throwable t) 
        {        	
        	log.error ("running command - ", t);  
        	return -1;
        }
        finally {
        	if (statement != null) try { statement.close(); } catch (Throwable ignored) {}
        	if (connection != null) try { connection.close(); } catch (Throwable ignored) {}
        	if (log.isTraceEnabled()) log.trace("DAO performance --------> command ==> " 
					+ (System.currentTimeMillis() - start) + " ms");
        }
        
	}
	
	
	public int executeStoredProc(String name, Object... params) 
			throws Exception
	{		    
		
		long start = (log.isTraceEnabled() ? System.currentTimeMillis() : 0);
		
        Connection connection = null;
        CallableStatement statement = null;
        
        String sql = null;
        
        boolean replaced = false;
        
        NodeList list = config.getElementsByTagName("storedprocedure"); 
        
		for (int i = 0; i < list.getLength(); i ++) 
        {
			NamedNodeMap map = list.item(i).getAttributes();			
			if (!name.equalsIgnoreCase(map.getNamedItem("name").getNodeValue())) continue;			
			sql = list.item(i).getTextContent().replace('\t', ' ').trim();
			replaced = (map.getNamedItem("replaced") != null && map.getNamedItem("replaced").getNodeValue().equals("true"));
			break;
        }
		
        if (sql == null) throw new Exception("named stored procedure '" + name + "' not found");
        
        if (replaced && params != null && params.length > 0) {
        	int paramcount = 0;
        	for (Object value : params) 
            {
        		paramcount ++;
        		sql = sql.replaceAll("\\{" + paramcount + "\\}", value.toString());
            }        	
        	params = null;
        }
        
        try 
        {
        	connection = getConnection();
        	
        	String sqlcall = "{ ? = call " + sql + " }";
        	
        	statement = connection.prepareCall(sqlcall);
        	statement.registerOutParameter(1, Types.INTEGER);
        	
        	int paramcount = 1;
            
        	if (params != null && params.length > 0)
        	{
        		//statement.clearParameters();
        		
	        	for (Object value : params) 
	            {       	        		
	        		if (value == null) {
	        			statement.setNull(++paramcount, Types.NULL);
	        		}	        		
	        		else if (value instanceof String) {
	    				statement.setString(++paramcount, String.class.cast(value)); 
	    			}
	    			else if (value instanceof Short) {
	    				statement.setShort(++paramcount, Short.class.cast(value)); 
	    			}
	    			else if (value instanceof Integer) {
	    				statement.setInt(++paramcount, Integer.class.cast(value)); 
	    			}
	    			else if (value instanceof Long) {
	    				statement.setLong(++paramcount, Long.class.cast(value)); 
	    			}
	    			else if (value instanceof Float) {
	    				statement.setFloat(++paramcount, Float.class.cast(value)); 
	    			}
	    			else if (value instanceof Double) {
	    				statement.setDouble(++paramcount, Double.class.cast(value)); 
	    			}
	    			else if (value instanceof Boolean) {
	    				statement.setBoolean(++paramcount, Boolean.class.cast(value)); 
	    			}
	    			else if (value instanceof byte[]) {
	    				statement.setBytes(++paramcount, byte[].class.cast(value)); 
	    			}
	    			else if (value instanceof Date) {
	    				statement.setTimestamp(++paramcount, new Timestamp(Date.class.cast(value).getTime())); 
	    			}
	    			else {
	    				statement.setString(++paramcount, value.toString()); 
	    			}        			
	            }
        	}
        	
        	if (log.isTraceEnabled()) traceCommand(sql, params);
        		
        	statement.execute();
        	
        	return statement.getInt(1);
        	
        }
        catch (Throwable t) 
        {
        	log.error ("running stored proc. -", t);
        	return -1;
        }
        finally {
        	if (statement != null) try { statement.close(); } catch (Throwable ignored) {}
        	if (connection != null) try { connection.close(); } catch (Throwable ignored) {}
        	if (log.isTraceEnabled()) log.trace("DAO performance --------> storedproc ==> " 
					+ (System.currentTimeMillis() - start) + " ms");
        }
        
	}
	
	
	private void traceCommand(String sql, Object[] params) {
		StringBuilder defparam = new StringBuilder("SQL > ")
			.append(sql
					.replace('\r', ' ')
					.replace('\n', ' ')
					.replace('\t', ' ')
					.trim()
				);
		if (params != null && params.length > 0) {
			defparam.append(" --// PARAMS => ");
			for (Object value : params) {
				if (value == null) {
					defparam.append("[NULL] ");
				}
				else {
					defparam.append("[").append(value.getClass().getName())
							.append(": ").append(value.toString()).append("] ");
				}
			}
		}
		log.trace(defparam.toString());
	}
	
		
	/**
	 * test method
	 * @param args
	 * @throws Exception
	 */
	public static void main (String [] args) throws Exception {
		
		GenericDao dao = new GenericDao();
		int n = dao.executeStoredProc("assesl join", "01", "00124B000100000F", "test");
		System.err.println(" *** returned ->  " + n);
		
	}
	
}
