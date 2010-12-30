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
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * Custom {@link FilterConfig} implementation special for the {@link HudsonAPIV2ServletFilter}.
 * This is required because Hudson only supports adding a default ServletFilter without any configuration
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 * 
 * @see VERSION
 */
public class HudsonAPIV2FilterConfig implements FilterConfig {

	private final FilterConfig parentFilterConfig;

	private final Properties initParams;

	/**
	 * Constructor
	 * 
	 * @param parentFilterConfig the original {@link FilterConfig} provided by Hudson at startup
	 * @throws IOException in case the Application properties cannot be loaded
	 */
	public HudsonAPIV2FilterConfig(FilterConfig parentFilterConfig) throws IOException {
		this.parentFilterConfig = parentFilterConfig;
		initParams = new Properties();
		initParams.load(HudsonAPIV2FilterConfig.class.getResourceAsStream("HudsonAPIV2FilterConfig.properties"));
		Enumeration<?> elements = parentFilterConfig.getInitParameterNames();
		while (elements.hasMoreElements()) {
			String paramName = elements.nextElement().toString();
			if (!initParams.containsKey(paramName)) {
				initParams.setProperty(paramName, parentFilterConfig.getInitParameter(paramName));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getFilterName() {
		return parentFilterConfig.getFilterName();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getInitParameter(String paramName) {
		return initParams.getProperty(paramName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumeration<?> getInitParameterNames() {
		return initParams.elements();
	}

	/**
	 * {@inheritDoc}
	 */
	public ServletContext getServletContext() {
		return parentFilterConfig.getServletContext();
	}
	
}