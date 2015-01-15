package org.openntf.xworlds.appservers.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.openntf.xworlds.appservers.lifecycle.XWorldsManagedThread;

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
		
		XWorldsManagedThread.setupAsDominoThread();
		try {
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
