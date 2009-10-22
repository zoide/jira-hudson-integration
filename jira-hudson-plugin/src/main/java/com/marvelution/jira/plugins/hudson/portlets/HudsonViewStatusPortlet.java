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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.configurable.ObjectConfigurationException;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.model.HudsonView;
import com.marvelution.jira.plugins.hudson.model.HudsonViewStatusPortletResult;
import com.marvelution.jira.plugins.hudson.panels.HudsonBuildsTabPanelHelper;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link Portlet} implementation to show the status of projects related to a Hudson view
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonViewStatusPortlet extends AbstractHudsonPorlet {

	/**
	 * Log4j Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(HudsonViewStatusPortlet.class);

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation
	 * @param userUtil the {@link UserUtil} implementation
	 * @param webResourceManager the {@link WebResourceManager} implementation
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param applicationProperties the {@link ApplicationProperties} implementation
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor} implementation
	 * @param hudsonServerManager the {@link HudsonServerManager} implementationr
	 */
	public HudsonViewStatusPortlet(JiraAuthenticationContext authenticationContext, UserUtil userUtil,
									WebResourceManager webResourceManager, PermissionManager permissionManager,
									ApplicationProperties applicationProperties,
									HudsonServerAccessor hudsonServerAccessor,
									HudsonServerManager hudsonServerManager) {
		super(authenticationContext, userUtil, webResourceManager, permissionManager, applicationProperties,
			hudsonServerAccessor, hudsonServerManager);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getVelocityParams(PortletConfiguration portletConfiguration) {
		final Map<String, Object> params = super.getVelocityParams(portletConfiguration);
		params.put("baseResourceUrl", "/download/resources/" + HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN
			+ ":hudson-view-status");
		if (isHudsonConfigured()) {
			final HudsonViewStatusPortletResult result = new HudsonViewStatusPortletResult(null, null);
			try {
				final String[] configuration = portletConfiguration.getProperty("hudsonView").split(";view:");
				final HudsonServer server = hudsonServerManager.getServer(Integer.parseInt(configuration[0]));
				result.setServer(server);
				final HudsonView view = hudsonServerAccessor.getView(server, configuration[1]);
				if (!StringUtils.isEmpty(view.getName())) {
					result.setViewName(view.getName());
					result.setViewDescription(view.getDescription());
					result.setJobs(view.getJobs());
				} else {
					result.setError(getErrorText("hudson.error.portlet.no.view.found"));
				}
			} catch (ObjectConfigurationException e) {
				LOGGER.error("Invalid portlet configuration.", e);
				result.setError(getErrorText("hudson.error.portlet.invalid.configuration"));
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid portlet configuration.", e);
				result.setError(getErrorText("hudson.error.portlet.invalid.configuration"));
			} catch (HudsonServerAccessorException e) {
				LOGGER.error("Failed to connect to the Hudson server.", e);
				result.setError(getErrorText("hudson.error.cannot.connect"));
			} catch (HudsonServerAccessDeniedException e) {
				LOGGER.error("Failed to connect to the Hudson server. Access Denied", e);
				result.setError(getErrorText("hudson.error.access.denied"));
			}
			params.put("result", result);
		}
		return params;
	}

}
