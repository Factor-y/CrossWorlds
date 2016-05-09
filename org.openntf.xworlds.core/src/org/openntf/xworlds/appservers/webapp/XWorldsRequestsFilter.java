package org.openntf.xworlds.appservers.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xworlds.appservers.lifecycle.XWorldsManagedThread;
import org.openntf.xworlds.appservers.webapp.config.XWorldsApplicationConfigurator;

/**
 * Servlet Filter implementation class XWorldsRequestsFilter
 */

public class XWorldsRequestsFilter implements Filter {

//	static final XLogger log = XLoggerFactory.getXLogger(XWorldsRequestsFilter.class);
	
    /**
     * Default constructor. 
     */
    public XWorldsRequestsFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		try {
			
			XWorldsManagedThread.setupAsDominoThread((HttpServletRequest) request);
			XWorldsApplicationConfigurator configurator = (XWorldsApplicationConfigurator) request.getServletContext().getAttribute(XWorldsApplicationConfigurator.APPCONTEXT_ATTRS_CWAPPCONFIG);
			if (configurator != null) {
				configurator.setupRequest((HttpServletRequest)request, (HttpServletResponse)response);
			}
			
			chain.doFilter(request, response);
			
		} finally {
			XWorldsManagedThread.shutdownDominoThread();
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
