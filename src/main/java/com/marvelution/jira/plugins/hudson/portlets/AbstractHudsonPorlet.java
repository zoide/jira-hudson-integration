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

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.SortTool;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.portal.PortletImpl;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.web.bean.I18nBean;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.DateTimeUtils;
import com.marvelution.jira.plugins.hudson.utils.JobUtils;

/**
 * Abstract Jira {@link Portlet} implementation for all Hudson related Portlets
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AbstractHudsonPorlet extends PortletImpl {

	protected static final Logger LOGGER = Logger.getLogger(HudsonStatusPortlet.class);

	protected HudsonServerManager hudsonServerManager;
	
	protected HudsonServerAccessor hudsonServerAccessor;
	
	private I18nBean i18nBean = null;
	
	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext}
	 * @param permissionManager the {@link PermissionManager}
	 * @param applicationProperties the {@link ApplicationProperties}
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor}
	 * @param hudsonServerManager the {@link HudsonServer}
	 */
	public AbstractHudsonPorlet(JiraAuthenticationContext authenticationContext, PermissionManager permissionManager,
			ApplicationProperties applicationProperties, HudsonServerAccessor hudsonServerAccessor,
			HudsonServerManager hudsonServerManager) {
		super(authenticationContext, permissionManager, applicationProperties);
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
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getVelocityParams(PortletConfiguration portletConfiguration) {
		final Map<String, Object> params = super.getVelocityParams(portletConfiguration);
		params.put("dateTimeUtils", new DateTimeUtils(authenticationContext));
		params.put("jobUtils", new JobUtils());
		params.put("sorter", new SortTool());
		return params;
	}

}
