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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.marvelution.jira.plugins.hudson.model.HudsonStatusPortletResult;
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
	 * @param permissionManager the {@link PermissionManager}
	 * @param applicationProperties the {@link ApplicationProperties}
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor}
	 * @param hudsonServerManager the {@link HudsonServerManager}
	 */
	public HudsonStatusPortlet(JiraAuthenticationContext authenticationContext, PermissionManager permissionManager,
			ApplicationProperties applicationProperties, HudsonServerAccessor hudsonServerAccessor,
			HudsonServerManager hudsonServerManager) {
		super(authenticationContext, permissionManager, applicationProperties, hudsonServerAccessor,
			hudsonServerManager);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Map<String, Object> getVelocityParams(PortletConfiguration portletConfiguration) {
		final Map<String, Object> params = super.getVelocityParams(portletConfiguration);
		if (!hudsonServerManager.isHudsonConfigured()) {
			params.put("isHudsonConfigured", Boolean.FALSE);
			params.put("errorMessage", getErrorText("hudson.error.hudsonnotconfigured"));
		} else {
			params.put("isHudsonConfigured", Boolean.TRUE);
			final List<HudsonStatusPortletResult> results = new ArrayList<HudsonStatusPortletResult>();
			for (HudsonServer server : hudsonServerManager.getServers()) {
				final HudsonStatusPortletResult result = new HudsonStatusPortletResult(server);
				try {
					result.setJobs(hudsonServerAccessor.getJobs(server));
				} catch (HudsonServerAccessorException e) {
					if (e.getCause() instanceof MalformedURLException) {
						result.setError(getErrorText("hudson.error.invalidhudsonurl"));
					} else {
						result.setError(getErrorText("hudson.error.cannotconnecthudson"));
					}
				}
				results.add(result);
			}
			params.put("results", results);
		}
		return params;
	}

}
