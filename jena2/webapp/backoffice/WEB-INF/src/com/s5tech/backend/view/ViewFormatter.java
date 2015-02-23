package com.s5tech.backend.view;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewFormatter {
	
	DateFormat datef = null;
	NumberFormat nf = null;
	
	public ViewFormatter() {
		datef = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");
		nf = NumberFormat.getInstance();
	}
	
	public String format(Object value) {
		if (value == null) {
			return "--";
		}
		else if (value instanceof String) {
			return String.class.cast(value);
		}
		else if (value instanceof Date) {
			return format(Date.class.cast(value));
		}
		else if (value instanceof Double) {
			return format(Double.class.cast(value));
		}
		else if (value instanceof Boolean) {
			return format(Boolean.class.cast(value));
		}
		else {		
			return value.toString();
		}
	}
	
	public String format(Date value) {
		return (value != null ? datef.format(value) : "");
	}
	
	public String format(Double value) {
		return (value != null ? nf.format(value) : "");
	}
	
	public String format(Boolean value) {
		return (value != null ? Boolean.toString(value) : "");
	}
	
	public String format(boolean value) {
		return Boolean.toString(value);
	}
	
	public void setDateFormat(String dateFormat) {
		datef = new SimpleDateFormat(dateFormat);		
	}
	
	public int copyAllProperties(Object dst, Object src) {
		
		int count = 0;
		
		Method[] mlist = dst.getClass().getMethods();
		
		for (Method m : mlist) {			
			if (Modifier.isStatic(m.getModifiers())) continue;
			String name = m.getName();
			if (!name.startsWith("set")) continue;
			if (name.length() == 3) continue;
			String property = name.substring(3);
			property = property.substring(0, 1).toLowerCase() + property.substring(1);
			if (copyProperty(dst, property, src)) {
				count ++;				
			}
		}		
		
		return count;
		
	}
	
	public boolean copyProperty (Object dst, String property, Object src)
	{	
		try {
		
			String name = property.substring(0, 1).toUpperCase() + property.substring(1);
			
			Method getter = null;					
			Method setter = null;
			
			Class<?>[] possibleGetterTypes = new Class[] { 
					String.class, 
					Integer.class, 
					Boolean.class, 
					Date.class, 
					boolean.class};
			
			for (Class<?> clazz : possibleGetterTypes)
			{
				if (setter == null)
					try { 
						setter = dst.getClass().getDeclaredMethod("set" + name, String.class); 
					} catch (Throwable ignored) {}	
				if (getter == null)
					try { getter = src.getClass().getDeclaredMethod(
							(clazz != boolean.class ? "get" : "is") + name); 
					} 
					catch (Throwable ignored) {}
				if (getter != null && setter != null) break;
			}
			
			if (getter == null || setter == null) {				
				return false;
			}
			
			Object value = getter.invoke(src);
			//if (value != null) setter.invoke(dst, format(value));
			setter.invoke(dst, format(value));
			
			return true;
			
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			return false;
		}
		
	}
	
	
	
}
