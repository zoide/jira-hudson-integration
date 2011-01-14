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

package com.marvelution.jira.plugins.hudson.services.associations.impl;

import com.atlassian.jira.project.Project;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;

/**
 * The Default {@link HudsonAssociation} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonAssociation implements HudsonAssociation, Comparable<HudsonAssociation> {

	private int associationId;
	private int serverId;
	private long projectId;
	private String jobName;

	/**
	 * Constructor
	 */
	public DefaultHudsonAssociation() {
		associationId = 0;
		serverId = 0;
		projectId = 0L;
		jobName = "";
	}

	/**
	 * Constructor
	 * 
	 * @param associationId the Association Id
	 * @param serverId the {@link HudsonServer} id
	 * @param projectId the JIRA {@link Project} Id
	 * @param jobName the Hudson Job name
	 */
	public DefaultHudsonAssociation(int associationId, int serverId, long projectId, String jobName) {
		this.associationId = associationId;
		this.serverId = serverId;
		this.projectId = projectId;
		this.jobName = jobName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAssociationId() {
		return associationId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAssociationId(int associationId) {
		this.associationId = associationId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getServerId() {
		return serverId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getProjectId() {
		return projectId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobName() {
		return jobName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(HudsonAssociation other) {
		return getJobName().compareTo(other.getJobName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HudsonAssociation) {
			HudsonAssociation other = (HudsonAssociation) obj;
			return (getAssociationId() == other.getAssociationId() && getServerId() == other.getServerId()
					&& getProjectId() == other.getProjectId() && getJobName().equals(other.getJobName()));
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return (int) (Integer.valueOf(getAssociationId()).hashCode() + Integer.valueOf(getServerId()).hashCode()
				+ Long.valueOf(getProjectId()) + getJobName().hashCode());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[HudsonAssication: " + getAssociationId() + "; sid:" + getServerId() + "; pid:" + getProjectId()
				+ "; job: " + getJobName() + "]";
	}

}
