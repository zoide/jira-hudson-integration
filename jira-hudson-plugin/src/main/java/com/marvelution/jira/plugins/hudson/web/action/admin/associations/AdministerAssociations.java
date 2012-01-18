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

import java.util.Collection;

import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.admin.AbstractHudsonProjectAdminWebActionSupport;

/**
 * {@link AbstractHudsonAdminWebActionSupport} implementation to administer Hudson server
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AdministerAssociations extends AbstractHudsonProjectAdminWebActionSupport {

	private static final long serialVersionUID = 1L;

	private final HudsonAssociationManager associationManager;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 */
	protected AdministerAssociations(HudsonServerManager serverManager, HudsonAssociationManager associationManager) {
		super(serverManager);
		this.associationManager = associationManager;
	}

	/**
	 * Getter for all the {@link HudsonAssociation}s
	 * 
	 * @return {@link Collection} with all the configured {@link HudsonAssociation} objects
	 */
	public Collection<HudsonAssociation> getAssociations() {
		if (getContext() != 0L && getProject() != null) {
			return associationManager.getAssociations(getProject());
		} else {
			return associationManager.getAssociations();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		initRequest();
		return SUCCESS;
	}

}
