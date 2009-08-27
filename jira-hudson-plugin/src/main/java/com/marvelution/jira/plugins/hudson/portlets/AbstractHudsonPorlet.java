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

import org.apache.velocity.tools.generic.SortTool;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.portal.PortletImpl;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.web.bean.I18nBean;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.panels.HudsonBuildsTabPanelHelper;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.BuildUtils;
import com.marvelution.jira.plugins.hudson.utils.DateTimeUtils;
import com.marvelution.jira.plugins.hudson.utils.HudsonBuildTriggerParser;
import com.marvelution.jira.plugins.hudson.utils.JobUtils;

/**
 * Abstract Jira {@link Portlet} implementation for all Hudson related Portlets
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AbstractHudsonPorlet extends PortletImpl {

	private final WebResourceManager webResourceManager;

	protected HudsonServerManager hudsonServerManager;
	
	protected HudsonServerAccessor hudsonServerAccessor;

	protected UserUtil userUtil;
	
	private I18nBean i18nBean = null;
	
	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation
	 * @param userUtil the {@link UserUtil} implementation
	 * @param webResourceManager the {@link WebResourceManager} implementation
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param applicationProperties the {@link ApplicationProperties} implementation
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor} implementation
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 */
	public AbstractHudsonPorlet(JiraAuthenticationContext authenticationContext, UserUtil userUtil,
								WebResourceManager webResourceManager, PermissionManager permissionManager,
								ApplicationProperties applicationProperties,
								HudsonServerAccessor hudsonServerAccessor, HudsonServerManager hudsonServerManager) {
		super(authenticationContext, permissionManager, applicationProperties);
		this.webResourceManager = webResourceManager;
		this.userUtil = userUtil;
		this.hudsonServerManager = hudsonServerManager;
		this.hudsonServerAccessor = hudsonServerAccessor;
	}
	
	/**
	 * Get the Internationalisation Bean
	 * 
	 * @return the {@link I18nBean} implementation
	 */
	protected I18nBean getI18nBean() {
		if (i18nBean == null) {
			i18nBean = authenticationContext.getI18nBean("com.marvelution.jira.plugins.hudson.hudson-portlet");
		}
		return i18nBean;
	}

	/**
	 * Gets the text for an internationalisation key
	 * 
	 * @param i18nKey the internationalisation key
	 * @return the text
	 */
	protected String getText(String i18nKey) {
		return getI18nBean().getText(i18nKey);
	}

	/**
	 * Gets the error text for an internationalisation key
	 * 
	 * @param i18nKey the internationalisation key
	 * @return the error text
	 */
	protected String getErrorText(String i18nKey) {
		return authenticationContext.getI18nBean("com.marvelution.jira.plugins.hudson.hudson-error").getText(i18nKey);
	}

	/**
	 * Checks if a Hudson Server is configured
	 * 
	 * @return <code>true</code> if a Hudson server is configured, <code>false</code> otherwise
	 */
	public boolean isHudsonConfigured() {
		return hudsonServerManager.isHudsonConfigured();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getVelocityParams(PortletConfiguration portletConfiguration) {
		webResourceManager.requireResource(HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
		final Map<String, Object> params = getSuperClassVelocityParams(portletConfiguration);
		if (!isHudsonConfigured()) {
			params.put("isHudsonConfigured", Boolean.FALSE);
			params.put("errorMessage", getErrorText("hudson.error.not.configured"));
		} else {
			params.put("isHudsonConfigured", Boolean.TRUE);
			params.put("dateTimeUtils", new DateTimeUtils(authenticationContext));
			params.put("buildUtils", new BuildUtils());
			params.put("jobUtils", new JobUtils());
			params.put("sorter", new SortTool());
			params.put("buildTriggerParser", new HudsonBuildTriggerParser(authenticationContext, userUtil));
		}
		return params;
	}

	/**
	 * Get the velocity parameters from the super class. Done this way so that testing is easier
	 * 
	 * @param portletConfiguration the {@link PortletConfiguration} of this portlet instance
	 * @return the {@link Map} of velocity parameters
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getSuperClassVelocityParams(PortletConfiguration portletConfiguration) {
		return super.getVelocityParams(portletConfiguration);
	}

}
