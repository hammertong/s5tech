package com.s5tech.backend.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.s5tech.backend.IConstants;
import com.s5tech.backend.dao.GenericDao;

public class ServicesListener implements ServletContextListener {
	
	private static final Log log = LogFactory.getLog(ServicesListener.class);
		
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		String parameter = null;
		
		final ServletContext servletContext = arg0.getServletContext();
		
		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(servletContext.getResourceAsStream("/WEB-INF/struts-config.xml"));
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//message-resources[@parameter]");
			NamedNodeMap map = ((NodeList) expr.evaluate(doc, XPathConstants.NODESET)).item(0).getAttributes();
			
			parameter = map.getNamedItem("parameter").getNodeValue();
			if (parameter == null) throw new Exception ("parameter attribute of 'message-resources' tag not found in struts-config.xml");
			
		}
		catch (Throwable t) 
		{
			log.error("failure looking up messages' parameter in struts-config - ", t);		
		}
		
		
		String daopath = null;
		
		try {
			File file = new File(servletContext.getRealPath("WEB-INF/dao.xml").replace('\\', '/'));
			if (file.exists()) daopath = file.getPath();
			InputStream in = (file.exists() ? new FileInputStream(file): servletContext.getResourceAsStream("/dao.xml"));	
			GenericDao dao = new GenericDao(in);
			dao.getConnection().close();
			servletContext.setAttribute(IConstants.DATA_ACCESS_SERVICES_KEY, dao);
		}
		catch (Throwable t) {
			log.error("failure loading dao - ", t);
		}
		
		final String parameter_ = parameter;
		final String daopath_ = daopath;
				
		Thread t = new Thread (
				
				new Runnable() {					
					
					@Override
					public void run() {			
						
						String filepattern = null;
						HashMap<String, Long> mtimeMap = new HashMap<String, Long> ();
						
						try 
						{	
							
							if (parameter_ != null) 
							{
								String resourcepattern= "/" + parameter_.replace('.', '/') + ".properties";							
								filepattern = servletContext.getRealPath("WEB-INF/classes" + resourcepattern).replace('\\', '/');								
								
								if (filepattern == null) 
									throw new Exception (
											"messages resource bundle not in accessible filesystem (jar packaged?)");							 
								
								if (filepattern.lastIndexOf('/') >= 0) 
									filepattern = filepattern.substring(0, filepattern.lastIndexOf('/')); 
								
								if (log.isTraceEnabled())
									log.trace ("monitoring '" + parameter_ + "' resource bundle filesystem changes ...");
							}							
						}
						catch (Throwable t) {
							
							log.error("loading configured I18N resources bundle, cannot monitor changes - ", t);
							filepattern = null;
							
						}
						
						
						try {
							
							
							if (daopath_ != null) {								
								if (log.isTraceEnabled())
									log.trace ("monitoring data access configuration file " + daopath_ + " ...");
							}	
							
							while (daopath_ != null && filepattern != null) {
								
								Thread.sleep(5000);
								
								//
								// look for data access configuration changes
								//
								if (daopath_ != null) 
								{
									File file = new File(daopath_);
									
									if (mtimeMap.get(file.getName()) == null) 
									{
										mtimeMap.put(file.getName(), file.lastModified());
									}
									else if (mtimeMap.get(file.getName()) != file.lastModified()) 
									{		
										log.info("data access configuration modified! reloading ...");
										
										try 
										{
											GenericDao dao = new GenericDao(new FileInputStream(file));											
											dao.getConnection().close();
											servletContext.setAttribute(IConstants.DATA_ACCESS_SERVICES_KEY, dao);
										}
										catch (Throwable t) 
										{
											log.error("failure reloading data access - ", t);
										}
									}
								}
								
								//
								// look for struts' message resources changes
								//
								if (parameter_ != null) 
								{
									for (File file : new File(filepattern).listFiles()) 
									{
										if (file.getName().indexOf(parameter_) < 0) continue;
										
										if (mtimeMap.get(file.getName()) == null) 
										{
											mtimeMap.put(file.getName(), file.lastModified());
										}
										else if (mtimeMap.get(file.getName()) != file.lastModified()) 
										{										
											log.info("messages resource bundle modified! reloading ...");
											
											try 
											{
												mtimeMap.put(file.getName(), file.lastModified());
		
												if (servletContext.getAttribute(Globals.MODULE_KEY) == null) 
													throw new Exception(Globals.MODULE_KEY + " missing in servlet context ?");
												
												ModuleConfig config = ModuleConfig.class.cast(servletContext.getAttribute(Globals.MODULE_KEY));
												
												for (MessageResourcesConfig m : config.findMessageResourcesConfigs()) {
													servletContext.removeAttribute(m.getKey() + config.getPrefix());  
									                String factory = m.getFactory();  
									                MessageResourcesFactory.setFactoryClass(factory);  
									                MessageResourcesFactory factoryObject = MessageResourcesFactory.createFactory();  
									                MessageResources resources = factoryObject.createResources(m.getParameter());  
									                resources.setReturnNull(m.getNull());  
									                servletContext.setAttribute(m.getKey() + config.getPrefix(), resources);
												}
											}
											catch (Throwable t) 
											{
												log.error("failure reloading message resources - ", t);
											}
																				
											break;
										}									
									}			
								}
							}
						}
						catch (Throwable t) 
						{
							t.printStackTrace();
						}
					}
				}, 
				"MessagesBundle FileMonitor"
			);
		t.setDaemon(true);
		t.start();		
		
	}
	
	
	

}
