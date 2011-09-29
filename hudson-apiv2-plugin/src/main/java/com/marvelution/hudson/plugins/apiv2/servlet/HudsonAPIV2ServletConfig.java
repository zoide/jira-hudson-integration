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

package com.marvelution.hudson.plugins.apiv2.servlet;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.marvelution.hudson.plugins.apiv2.servlet.filter.HudsonAPIV2FilterConfig;

/**
 * Custom {@link ServletConfig} for the RestServlet
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 * 
 * @see 1.0.0
 */
public class HudsonAPIV2ServletConfig implements ServletConfig {

	private HudsonAPIV2FilterConfig filterConfig;

	/**
	 * Constructor
	 * 
	 * @param filterConfig the {@link HudsonAPIV2FilterConfig}
	 */
	public HudsonAPIV2ServletConfig(HudsonAPIV2FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getInitParameter(String paramName) {
		return filterConfig.getInitParameter(paramName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumeration<?> getInitParameterNames() {
		return filterConfig.getInitParameterNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public ServletContext getServletContext() {
		return filterConfig.getServletContext();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getServletName() {
		return filterConfig.getFilterName();
	}

}
