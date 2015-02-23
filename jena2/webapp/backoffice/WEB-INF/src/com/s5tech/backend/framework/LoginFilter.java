package com.s5tech.backend.framework;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.s5tech.backend.util.RequestTools;

public class LoginFilter implements Filter {
	
	private static final Log log = LogFactory.getLog(LoginFilter.class);
	
	private static final String EXCLUDE_PATTERNS_KEY = "excludePatterns";

	private static final String LOGIN_PAGE_KEY = "loginPage";

	private String loginPage = null;
	
	private Pattern excludePatterns = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException 
	{
		if (filterConfig != null 
				&& filterConfig.getInitParameter(LOGIN_PAGE_KEY) != null
				&& filterConfig.getInitParameter(EXCLUDE_PATTERNS_KEY) != null) 
		{
			loginPage = filterConfig.getInitParameter(LOGIN_PAGE_KEY);
			if (!loginPage.startsWith("/")) loginPage = "/" + loginPage;
			excludePatterns = Pattern.compile(filterConfig.getInitParameter(EXCLUDE_PATTERNS_KEY));			
		}
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException 
	{
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;				
			if (!RequestTools.isLoggedIn(request)) {
				if (excludePatterns == null 
						|| !excludePatterns.matcher(request.getRequestURI()).matches()) {
					if (log.isDebugEnabled()) {
						log.debug("unauthorized request: " + request.getRemoteHost() 
								+ " not logged-in! > " + request.getRequestURI());
						//System.err.println("unauthorized requested (" + request.getRemoteHost() 
						//		+ " not logged-in) " + request.getRequestURI());
					}
					response.sendRedirect(request.getContextPath() + loginPage);
					return;
				}					
			}
		}
		catch (Throwable t) {			
			log.error("request processor filter exception - ", t);
			return;
		}
		
		chain.doFilter(req, res);
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
	
}
