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

package com.marvelution.jira.plugins.hudson.web.action.admin;

import javax.servlet.http.HttpServletRequest;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.projectconfig.util.ServletRequestProjectConfigRequestCache;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.ExecutingHttpRequest;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
public class AbstractHudsonProjectAdminWebActionSupport extends AbstractHudsonAdminWebActionSupport {

	private static final long serialVersionUID = 1L;

	private long context = 0L;

	/**
	 * Constructor
	 *
	 * @param serverManager the {@link SonarServerManager} implementation
	 */
	protected AbstractHudsonProjectAdminWebActionSupport(HudsonServerManager serverManager) {
		super(serverManager);
	}

	/**
	 * Internal method to add the context project to the request attributes for the project-config-plugin
	 */
	protected void initRequest() {
		if (context != 0L && getProject() != null) {
			HttpServletRequest request = ExecutingHttpRequest.get();
			request.setAttribute(ServletRequestProjectConfigRequestCache.class.getName() + ":project", getProject());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPermissions() {
		return super.hasPermissions() || (context != 0L
				&& getPermissionManager().hasPermission(Permissions.PROJECT_ADMIN, getProject(), getLoggedInUser()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		initRequest();
		return super.doDefault();
	}

	/**
	 * Getter for context
	 *
	 * @return the context
	 */
	public long getContext() {
		return context;
	}

	/**
	 * Setter for context
	 *
	 * @param context the context to set
	 */
	public void setContext(long context) {
		this.context = context;
	}

	/**
	 * Getter if the context decorator should be used or the global admin one
	 * 
	 * @return <code>true</code> if {@link #context} != 0
	 */
	public boolean useContextDecorator() {
		return context != 0;
	}

	/**
	 * Getter for the context Project
	 * 
	 * @return the context Project
	 */
	public Project getProject() {
		if (context != 0L) {
			return getProjectManager().getProjectObj(context);
		} else {
			return null;
		}
	}

}
