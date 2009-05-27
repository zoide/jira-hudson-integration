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

package com.marvelution.jira.plugins.hudson.portlets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.model.HudsonStatusPortletResult;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Jira {@link Portlet} for showing the statuses of all Hudson Jobs
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonStatusPortlet extends AbstractHudsonPorlet {

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext}
	 * @param userUtil the {@link UserUtil} implementation
	 * @param webResourceManager the {@link WebResourceManager} implementation
	 * @param permissionManager the {@link PermissionManager}
	 * @param applicationProperties the {@link ApplicationProperties}
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor}
	 * @param hudsonServerManager the {@link HudsonServerManager}
	 */
	public HudsonStatusPortlet(JiraAuthenticationContext authenticationContext, UserUtil userUtil,
								WebResourceManager webResourceManager, PermissionManager permissionManager,
								ApplicationProperties applicationProperties,
								HudsonServerAccessor hudsonServerAccessor, HudsonServerManager hudsonServerManager) {
		super(authenticationContext, userUtil, webResourceManager, permissionManager, applicationProperties,
			hudsonServerAccessor, hudsonServerManager);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getVelocityParams(PortletConfiguration portletConfiguration) {
		final Map<String, Object> params = super.getVelocityParams(portletConfiguration);
		if (hudsonServerManager.isHudsonConfigured()) {
			final List<HudsonStatusPortletResult> results = new ArrayList<HudsonStatusPortletResult>();
			for (HudsonServer server : hudsonServerManager.getServers()) {
				final HudsonStatusPortletResult result = new HudsonStatusPortletResult(server);
				try {
					final List<Job> jobs = hudsonServerAccessor.getProjects(server);
					result.setJobs(jobs);
				} catch (HudsonServerAccessorException e) {
					result.setError(getErrorText("hudson.error.cannot.connect"));
					LOGGER.error("Failed to connect to the Hudson server.", e);
				}
				results.add(result);
			}
			params.put("results", results);
		}
		return params;
	}

}
