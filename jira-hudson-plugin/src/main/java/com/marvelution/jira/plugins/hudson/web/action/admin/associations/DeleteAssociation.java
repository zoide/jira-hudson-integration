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

package com.marvelution.jira.plugins.hudson.web.action.admin.associations;

import com.atlassian.jira.project.Project;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.admin.AbstractHudsonProjectAdminWebActionSupport;

/**
 * Delete {@link HudsonServer} WebAction implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DeleteAssociation extends AbstractHudsonProjectAdminWebActionSupport {

	private static final long serialVersionUID = 1L;

	private final HudsonAssociationManager associationManager;

	private int associationId;
	private HudsonAssociation association;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 */
	protected DeleteAssociation(HudsonServerManager serverManager, HudsonAssociationManager associationManager) {
		super(serverManager);
		this.associationManager = associationManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		association = associationManager.getAssociation(getAssociationId());
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		associationManager.removeAssociation(getAssociationId());
		return getRedirect(ADMINISTER_ASSOCIATIONS);
	}

	/**
	 * Getter for associationId
	 * 
	 * @return the associationId
	 */
	public int getAssociationId() {
		return associationId;
	}

	/**
	 * Setter for association Id
	 * 
	 * @param associationId the association Id to set
	 */
	public void setAssociationId(int associationId) {
		this.associationId = associationId;
	}

	/**
	 * Getter for association
	 * 
	 * @return the association
	 */
	public HudsonAssociation getAssociation() {
		return association;
	}

	/**
	 * Getter for the Job Name
	 * 
	 * @return the Job Name
	 */
	public String getJobName() {
		return getAssociation().getJobName();
	}

	/**
	 * Getter for the {@link Project}
	 * 
	 * @return the {@link Project}
	 */
	public Project getAssociationProject() {
		return getProjectManager().getProjectObj(getAssociation().getProjectId());
	}

	/**
	 * Getter for the {@link HudsonServer}
	 * 
	 * @return the {@link HudsonServer}
	 */
	public HudsonServer getServer() {
		return getAssociation().getServer();
	}

}
