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

package com.marvelution.jira.plugins.hudson.model;

import org.apache.commons.lang.StringUtils;

import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * {@link HudsonStatusPortlet} Result model
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonViewStatusPortletResult extends HudsonStatusPortletResult {

	private String viewName;

	private String viewDescription;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param viewName the name of the Hudson View
	 */
	public HudsonViewStatusPortletResult(HudsonServer server, String viewName) {
		super(server);
		this.viewName = viewName;
	}

	/**
	 * Gets the Hudson View name
	 * 
	 * @return the Hudson View name
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Sets the Hudson View name
	 * 
	 * @param viewName the Hudson View name
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * Gets the Hudson View Description
	 * 
	 * @return the Hudson View Description
	 */
	public String getViewDescription() {
		return viewDescription;
	}

	/**
	 * Sets the Hudson View Description
	 * 
	 * @param viewDescription the Hudson View Description
	 */
	public void setViewDescription(String viewDescription) {
		this.viewDescription = viewDescription;
	}

	/**
	 * Gets if a Description is set
	 * 
	 * @return <code>true</code> if View Description is set, <code>false</code> otherwise
	 */
	public boolean hasViewDescription() {
		return StringUtils.isNotEmpty(viewDescription);
	}

}
