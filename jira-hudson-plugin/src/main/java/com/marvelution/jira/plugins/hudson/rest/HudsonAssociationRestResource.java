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

package com.marvelution.jira.plugins.hudson.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.plugins.rest.common.security.AuthenticationContext;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NotAuthorizedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;
import com.sun.jersey.spi.resource.Singleton;

/**
 * REST API for Hudson Server Associations
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("/association")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Singleton
public class HudsonAssociationRestResource {

	private HudsonServerManager serverManager;

	private UserUtil userUtil;

	private ProjectManager projectManager;

	private PermissionManager permissionManager;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param userUtil the {@link UserUtil} implementation
	 * @param projectManager the {@link ProjectManager} implementation
	 * @param permissionManager the {@link PermissionManager} implementation
	 */
	public HudsonAssociationRestResource(HudsonServerManager serverManager, UserUtil userUtil,
										ProjectManager projectManager, PermissionManager permissionManager) {
		this.serverManager = serverManager;
		this.userUtil = userUtil;
		this.projectManager = projectManager;
		this.permissionManager = permissionManager;
	}

	/**
	 * REST Endpoint to add a Sonar Association
	 * 
	 * @param projectKey the project key
	 * @param hudsonServerId the Hudson server id
	 * @param authenticationContext the {@link AuthenticationContext}
	 */
	@POST
	@Path("{projectId}")
	public void associateProject(@PathParam("projectId") String projectKey,
					@FormParam("hudsonServer") int hudsonServerId,
					@Context AuthenticationContext authenticationContext) {
		final Project project = projectManager.getProjectObjByKey(projectKey);
		final User user = userUtil.getUser(authenticationContext.getPrincipal().getName());
		if (project != null && user != null
			&& permissionManager.hasPermission(Permissions.PROJECT_ADMIN, project, user)) {
			final HudsonServer server = serverManager.getServer(hudsonServerId);
			if (server != null) {
				server.addAssociatedProjectKey(projectKey);
				serverManager.put(server);
			}
		} else {
			throw new NotAuthorizedException("Access is denied");
		}
	}

}
