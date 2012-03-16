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

package com.marvelution.jira.plugins.hudson.utils;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;

/**
 * User Utility class
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class UserUtils {

	private static final Logger LOGGER = Logger.getLogger(UserUtils.class);

	/**
	 * Helper method to check if the given {@link User} is a member of at least one of the groups in the
	 * {@link Collection}
	 * 
	 * @param user the {@link User} to check
	 * @param groupNames the {@link Collection} of groupnames to check against 
	 * @return <code>true</code> if the {@link User} is a member of at least one group, <code>false</code> otherwise
	 */
	public static boolean isUserMemberOfAtleastOneGroup(final User user, Collection<String> groupNames) {
		return isUserMemberOfAtleastOneGroup(user, groupNames, ComponentManager.getInstance().getCrowdService());
	}

	/**
	 * Helper method to check if the given {@link User} is a member of at least one of the groups in the
	 * {@link Collection}
	 * 
	 * @param user the {@link User} to check
	 * @param groupNames the {@link Collection} of groupnames to check against
	 * @param crowdService the {@link CrowdService} implementation 
	 * @return <code>true</code> if the {@link User} is a member of at least one group, <code>false</code> otherwise
	 */
	public static boolean isUserMemberOfAtleastOneGroup(final User user, Collection<String> groupNames,
			final CrowdService crowdService) {
		if (user == null) {
			throw new IllegalArgumentException("Invalid User variable");
		}
		if (groupNames == null || groupNames.isEmpty()) {
			return false;
		}
		for (String groupName : groupNames) {
			if (crowdService.isUserMemberOfGroup(user.getName(), groupName)) {
				return true;
			}
		}
		// Still here? Then we where not a member of any of the groups in the collection
		return false;
	}

	/**
	 * Helper method to check if the given {@link User} is a member of at least one of the {@link ProjectRole}s in the
	 * {@link Collection}
	 * 
	 * @param user the {@link User} to check
	 * @param project the {@link Project} to check against
	 * @param roleIds the {@link Collection} of {@link ProjectRole} ids to check against 
	 * @return <code>true</code> if the {@link User} is a member of at least one {@link ProjectRole},
	 *         <code>false</code> otherwise
	 */
	public static boolean isUserMemberOfAtleastOneProjectRole(final User user, final Project project,
			Collection<String> roleIds) {
		return isUserMemberOfAtleastOneProjectRole(user, project, roleIds,
				ComponentManager.getComponent(ProjectRoleManager.class));
	}

	/**
	 * Helper method to check if the given {@link User} is a member of at least one of the {@link ProjectRole}s in the
	 * {@link Collection}
	 * 
	 * @param user the {@link User} to check
	 * @param project the {@link Project} to check against
	 * @param roleIds the {@link Collection} of {@link ProjectRole} ids to check against 
	 * @return <code>true</code> if the {@link User} is a member of at least one {@link ProjectRole},
	 *         <code>false</code> otherwise
	 */
	public static boolean isUserMemberOfAtleastOneProjectRole(final User user, final Project project, Collection<String> roleIds,
			ProjectRoleManager roleManager) {
		for (String idString : roleIds) {
			try {
				Long roleId = Long.parseLong(idString);
				ProjectRole role = roleManager.getProjectRole(roleId);
				if (roleManager.isUserInProjectRole(user, role, project)) {
					return true;
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid Project Role ID configured. Skipping ID: " + idString);
			}
		}
		return false;
	}

	/**
	 * Check if a {@link User} exists by its name
	 * 
	 * @param username the user name
	 * @return <code>true</code> if it exists, <code>false</code> otherwise
	 */
	public static boolean userExists(String username) {
		return userExists(username, ComponentManager.getInstance().getCrowdService());
	}

	/**
	 * Check if a {@link User} exists by its name using the specified {@link CrowdService}
	 * 
	 * @param username the user name
	 * @param crowdService the {@link CrowdService} implementation
	 * @return <code>true</code> if it exists, <code>false</code> otherwise
	 */
	public static boolean userExists(String username, CrowdService crowdService) {
		return StringUtils.isNotBlank(username) && getUser(username, crowdService) != null;
	}

	/**
	 * Get a {@link User} exists by its name
	 * 
	 * @param username the user name
	 * @return the {@link User}
	 */
	public static User getUser(String username) {
		return getUser(username, ComponentManager.getInstance().getCrowdService());
	}

	/**
	 * Get a {@link User} exists by its name using the specified {@link CrowdService}
	 * 
	 * @param username the user name
	 * @param crowdService the {@link CrowdService} implementation
	 * @return the {@link User}, <code>null</code> in case the given username is blank
	 */
	public static User getUser(String username, CrowdService crowdService) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		return crowdService.getUser(username);
	}

}
