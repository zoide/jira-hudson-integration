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

package com.marvelution.jira.plugins.hudson.services.associations;

import com.atlassian.jira.project.Project;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * The Hudson Assication Interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonAssociation {

	/**
	 * Getter for the Association Id
	 * 
	 * @return the Association Id
	 */
	int getAssociationId();

	/**
	 * Setter for the Association Id
	 * 
	 * @param assicationId the Association Id
	 */
	void setAssociationId(int associationId);

	/**
	 * Getter for the {@link HudsonServer} Id
	 * 
	 * @return the {@link HudsonServer} Id
	 */
	int getServerId();

	/**
	 * Setter for the {@link HudsonServer} Id
	 * 
	 * @param serverId the {@link HudsonServer} Id
	 */
	void setServerId(int serverId);

	/**
	 * Getter for the JIRA {@link Project} Id
	 * 
	 * @return the JIRA {@link Project} Id
	 */
	long getProjectId();

	/**
	 * Setter for the JIRA {@link Project} Id
	 * 
	 * @param projectId the JIRA {@link Project} Id
	 */
	void setProjectId(long projectId);

	/**
	 * Getter for the Hudson Job name
	 * 
	 * @return the Hudson Job name
	 */
	String getJobName();

	/**
	 * Setter for the Hudson Job name
	 * 
	 * @param jobName the Hudson Job name
	 */
	void setJobName(String jobName);

}
