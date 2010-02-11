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

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.SortTool;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Abstract {@link JiraWebActionSupport} implementation for managing HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class AbstractHudsonWebActionSupport extends JiraWebActionSupport {

	private static final long serialVersionUID = 1L;

	protected static final Logger LOGGER = Logger.getLogger(AbstractHudsonWebActionSupport.class);

	protected final PermissionManager permissionManager;

	protected final HudsonServerManager hudsonServerManager;

	private SortTool sorter;

	/**
	 * Constructor
	 * 
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 */
	public AbstractHudsonWebActionSupport(PermissionManager permissionManager,
											HudsonServerManager hudsonServerManager) {
		this.permissionManager = permissionManager;
		this.hudsonServerManager = hudsonServerManager;
		sorter = new SortTool();
	}

	/**
	 * Check if the users has the required permissions
	 * 
	 * @return <code>true</code> if so, <code>false</code> otherwise
	 */
	public boolean hasPermissions() {
		return permissionManager.hasPermission(Permissions.ADMINISTER, getRemoteUser());
	}

	/**
	 * {@inheritDoc}
	 */
	public String doDefault() throws Exception {
		return ((hasPermissions()) ? INPUT : PERMISSION_VIOLATION_RESULT);
	}

	/**
	 * Gets the {@link HudsonServerManager} implementation
	 * 
	 * @return the {@link HudsonServerManager} implementation
	 */
	public HudsonServerManager getHudsonServerManager() {
		return hudsonServerManager;
	}

	/**
	 * Gets the {@link SortTool} utility class
	 * 
	 * @return the {@link SortTool}
	 */
	public SortTool getSorter() {
		return sorter;
	}

	/**
	 * Get the {@link I18nHelper}
	 * 
	 * @return the {@link I18nHelper}
	 */
	public I18nHelper getI18nHelper() {
		return ComponentManager.getInstance().getJiraAuthenticationContext().getI18nHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(String i18nKey) {
		return getI18nHelper().getText(i18nKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(String i18nKey, String value) {
		return getI18nHelper().getText(i18nKey, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(String i18nKey, Object value) {
		return getI18nHelper().getText(i18nKey, value);
	}

}
