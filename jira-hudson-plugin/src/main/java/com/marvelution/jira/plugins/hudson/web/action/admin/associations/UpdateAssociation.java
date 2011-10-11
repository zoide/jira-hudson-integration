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

import java.util.ArrayList;
import java.util.Collection;

import com.marvelution.hudson.plugins.apiv2.client.ClientException;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationFactory;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.admin.KeyValuePair;
import com.marvelution.jira.plugins.hudson.web.action.admin.ModifyActionType;

/**
 * Update {@link HudsonServer} Web Action implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@SuppressWarnings("unchecked")
public class UpdateAssociation extends AbstractModifyAssociation {

	private static final long serialVersionUID = 1L;

	private final HudsonClientFactory clientFactory;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationFactory the {@link HudsonAssociationFactory} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	protected UpdateAssociation(HudsonServerManager serverManager, HudsonAssociationFactory associationFactory,
			HudsonAssociationManager associationManager, HudsonClientFactory clientFactory) {
		super(serverManager, associationFactory, associationManager);
		this.clientFactory = clientFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		if (!associationManager.hasAssociation(getAssociationId())) {
			return getRedirect(ADMINISTER_ASSOCIATIONS);
		}
		association = associationFactory.create(associationManager.getAssociation(getAssociationId()));
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doValidation() {
		super.doValidation();
		// TODO Validate duplicate
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModifyActionType getActionType() {
		return ModifyActionType.UPDATE_ASSOCIATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<KeyValuePair> getExtraHiddenInput() {
		return new ArrayList<KeyValuePair>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getJobOptions() {
		HudsonClient client = clientFactory.create(serverManager.getServer(getHudsonId()));
		Collection<String> options = new ArrayList<String>();
		try {
			for (Job job : client.findAll(JobQuery.createForJobList(true, false))) {
				options.add(job.getName());
			}
		} catch (ClientException e) {
			// TODO Improve on this
			options.add(e.getMessage());
		}
		return options;
	}

}
