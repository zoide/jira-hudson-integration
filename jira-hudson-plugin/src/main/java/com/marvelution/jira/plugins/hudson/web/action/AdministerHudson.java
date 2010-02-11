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

package com.marvelution.jira.plugins.hudson.web.action;

import java.util.Collection;

import com.atlassian.jira.security.PermissionManager;
import com.marvelution.jira.plugins.hudson.service.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link AbstractHudsonWebActionSupport} implementation for viewing configured HUdson Servers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AdministerHudson extends AbstractHudsonWebActionSupport {

	private static final long serialVersionUID = 1L;

	private final HudsonConfigurationManager hudsonConfigurationManager;

	/**
	 * Constructor
	 * 
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 * @param hudsonConfigurationManager the {@link HudsonConfigurationManager} implementation
	 */
	public AdministerHudson(PermissionManager permissionManager, HudsonServerManager hudsonServerManager,
							HudsonConfigurationManager hudsonConfigurationManager) {
		super(permissionManager, hudsonServerManager);
		this.hudsonConfigurationManager = hudsonConfigurationManager;
	}

	/**
	 * Get all the configured {@link HudsonServer} objects
	 * 
	 * @return {@link Collection} of {@link HudsonServer} objects
	 */
	public Collection<HudsonServer> getServers() {
		return hudsonServerManager.getServers();
	}

	/**
	 * Get the hideUnassociatedHudsonTabs setting
	 * 
	 * @return setting value
	 */
	public boolean isHideUnassociatedHudsonTabs() {
		return hudsonConfigurationManager.getBooleanProperty(HudsonConfigurationManager.HIDE_UNASSOCIATED_HUDSON_TAB);
	}

	/**
	 * Set the hideUnassociatedHudsonTabs setting
	 * 
	 * @param hideUnassociatedHudsonTabs the new setting value
	 */
	public void setHideUnassociatedHudsonTabs(boolean hideUnassociatedHudsonTabs) {
		hudsonConfigurationManager.setProperty(HudsonConfigurationManager.HIDE_UNASSOCIATED_HUDSON_TAB,
			hideUnassociatedHudsonTabs);
	}

	/**
	 * Get the filterHudsonBuilds setting
	 * 
	 * @return setting value
	 */
	public boolean isFilterHudsonBuilds() {
		return hudsonConfigurationManager.getBooleanProperty(HudsonConfigurationManager.FILTER_HUDSON_BUILDS);
	}

	/**
	 * Set the filterHudsonBuilds setting
	 * 
	 * @param filterHudsonBuilds the new setting value
	 */
	public void setFilterHudsonBuilds(boolean filterHudsonBuilds) {
		hudsonConfigurationManager.setProperty(HudsonConfigurationManager.FILTER_HUDSON_BUILDS, filterHudsonBuilds);
	}

}
