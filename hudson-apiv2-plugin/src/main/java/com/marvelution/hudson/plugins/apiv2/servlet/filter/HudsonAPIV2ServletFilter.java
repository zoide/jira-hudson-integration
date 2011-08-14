/*
 * Licensed to Marvelution under one or more contributor license 
 * agreements.  See the NOTICE file distributed with this work 
 * for additional information regarding copyright ownership.
 * Marvelution licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.marvelution.hudson.plugins.apiv2.servlet.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.apache.wink.server.internal.servlet.RestFilter;
import org.apache.wink.server.internal.servlet.RestServlet;

import com.marvelution.hudson.plugins.apiv2.resources.impl.BaseRestResource;
import com.marvelution.hudson.plugins.apiv2.servlet.HudsonAPIV2ServletConfig;

/**
 * Custom implementation of the {@link RestFilter}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 * 
 * @since 1.0.0
 */
public class HudsonAPIV2ServletFilter extends RestFilter {

	private static final Logger LOGGER = Logger.getLogger(HudsonAPIV2ServletFilter.class.getName());

	private RestServlet restServlet;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			final HudsonAPIV2FilterConfig restFilterConfig = new HudsonAPIV2FilterConfig(filterConfig);
			restServlet = new HudsonRestServletForFilter(restFilterConfig);
			restServlet.init(new HudsonAPIV2ServletConfig(restFilterConfig));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to create HudsonRestFilterConfig", e);
			throw new ServletException("Failed to create HudsonRestFilterConfig", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		LOGGER.log(Level.FINE, "Destroying RestFilter {}", this);
		restServlet.destroy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
					ServletException {
		// The Wink RestFilter can only handle HttpServletRequests so make sure we have one
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			// Put the original HttpServletRequest in the HttpServletRequestWrapper
			final HttpServletRequestWrapper servletRequest =
				new HttpServletRequestWrapper((HttpServletRequest) request);
			// Get the requestUri without the context path and the leading slash
			String requestUri = servletRequest.getPathInfo();
			if (StringUtils.isNotBlank(requestUri) && requestUri.startsWith("/")) {
				requestUri = requestUri.substring(1);
			}
			LOGGER.log(Level.FINE, "Got a request from URI: " + requestUri);
			// Make sure it is a REST call
			if (StringUtils.isNotBlank(requestUri) && requestUri.startsWith(BaseRestResource.BASE_REST_URI)) {
				LOGGER.log(Level.FINE, "Got a REST request, forwarding it to the Wink RestFilter");
				FilteredHttpServletResponse servletResponse =
					new FilteredHttpServletResponse((HttpServletResponse) response);
				restServlet.service(servletRequest, servletResponse);
				if ((!(servletResponse.isCommitted())) && (servletResponse.getStatusCode() == 404)) {
					LOGGER.log(Level.FINE, "Filter " + this.getClass().getName()
						+ " did not match a resource so letting request continue on FilterChain");
					servletResponse.setStatus(200);
					filterChain.doFilter(request, response);
				}
			// Otherwise forward the request to the next Filter in the chain
			} else {
				LOGGER.log(Level.FINE, "No REST request, forwarding request to the next ServletFilter");
				filterChain.doFilter(request, response);
			}
		// If we don't have a HttpServletRequest and HttpServletResponse then forward to the next Filter in the chain
		} else {
			LOGGER.log(Level.FINE,
				"No HttpServletRequest and HttpServletResponse, forwarding request to the next ServletFilter");
			filterChain.doFilter(request, response);
		}
	}

	/**
	 * Custom {@link RestFilter.RestServletForFilter} implementation that overrides the getProperties() method of the RestServlet.
	 * This is required because resource loading from the class-path in Hudson is broken for plugins
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
	 * 
	 * @since 1.0.0
	 */
	protected static class HudsonRestServletForFilter extends RestServletForFilter {

		private static final long serialVersionUID = 1L;
	
		/**
		 * Constructor
		 * 
		 * @param filterConfig the {@link FilterConfig} implementation
		 */
		public HudsonRestServletForFilter(FilterConfig filterConfig) {
			super(filterConfig);
		}
	
	}

	/**
	 * Custom FilteredHttpServletResponse provided by the {@link RestFilter} class
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
	 * 
	 * @since 1.0.0
	 */
	private static class FilteredHttpServletResponse extends HttpServletResponseWrapper {

		private static final Logger LOGGER = Logger.getLogger(FilteredHttpServletResponse.class.getName());

		private int statusCode;

		/**
		 * Constructor
		 * 
		 * @param response the base {@link HttpServletResponse} that is wrapped
		 */
		public FilteredHttpServletResponse(HttpServletResponse response) {
			super(response);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setStatus(int statusCode) {
			super.setStatus(statusCode);
			this.statusCode = statusCode;
			LOGGER.log(Level.FINE, "FilteredHttpServletResponse set status code to " + Integer.valueOf(statusCode));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setStatus(int statusCode, String msg) {
			super.setStatus(statusCode, msg);
			this.statusCode = statusCode;
			LOGGER.log(Level.FINE, "FilteredHttpServletResponse set status code to " + Integer.valueOf(statusCode));
		}

		/**
		 * Get the status code
		 * 
		 * @return status code value
		 */
		int getStatusCode() {
			return this.statusCode;
		}

	}

}
