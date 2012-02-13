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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import net.java.ao.Query;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.project.Project;
import com.google.common.collect.Lists;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;

/**
 * The Default {@link HudsonAssociationManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonAssociationManagerService implements HudsonAssociationManager {

	private final ActiveObjects objects;
	private final HudsonServerManager serverManager;

	/**
	 * Constructor
	 *
	 * @param objects the {@link ActiveObjects} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	public HudsonAssociationManagerService(ActiveObjects objects, HudsonServerManager serverManager) {
		this.objects = checkNotNull(objects, "activeObjects");
		this.serverManager = checkNotNull(serverManager, "serverManager");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAssociation(int associationId) {
		return getAssociation(associationId) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAssocations() {
		return objects.count(HudsonAssociation.class) > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAssociations(Project project) {
		return getAssociations(project).size() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation getAssociation(int associationId) {
		return objects.get(HudsonAssociation.class, associationId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<HudsonAssociation> getAssociations() {
		return Lists.newArrayList(objects.find(HudsonAssociation.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<HudsonAssociation> getAssociations(Project project) {
		return Lists.newArrayList(objects.find(HudsonAssociation.class,
			Query.select().where("PROJECT_ID = ?", project.getId())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation addAssociation(HudsonServer server, long projectId, String jobname) {
		checkNotNull(server, "server");
		checkNotNull(projectId, "projectId");
		checkNotNull(jobname, "jobname");
		HudsonAssociation association = objects.create(HudsonAssociation.class);
		association.setServer(server);
		association.setProjectId(projectId);
		association.setJobName(jobname);
		association.save();
		return association;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation addAssociation(int serverId, long projectId, String jobname) {
		return addAssociation(serverManager.getServer(serverId), projectId, jobname);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation addAssociation(HudsonAssociation association) {
		return addAssociation(association.getServer(), association.getProjectId(), association.getJobName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation updateAssociation(int associationId, HudsonServer server, long projectId, String jobname) {
		HudsonAssociation association = getAssociation(associationId);
		checkNotNull(association, "association");
		checkNotNull(server, "server");
		checkNotNull(projectId, "projectId");
		checkNotNull(jobname, "jobname");
		association.setServer(server);
		association.setProjectId(projectId);
		association.setJobName(jobname);
		association.save();
		return association;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation updateAssociation(int associationId, int serverId, long projectId, String jobname) {
		return updateAssociation(associationId, serverManager.getServer(serverId), projectId, jobname);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation updateAssociation(HudsonAssociation association) {
		checkNotNull(association, "association");
		association.save();
		return association;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAssociation(int associationId) {
		HudsonAssociation association = getAssociation(associationId);
		checkNotNull(association, "No HudsonAssociation configured with Id: " + associationId);
		removeAssociation(association);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAssociation(HudsonAssociation association) {
		checkNotNull(association, "association");
		objects.delete(association);
	}

}
