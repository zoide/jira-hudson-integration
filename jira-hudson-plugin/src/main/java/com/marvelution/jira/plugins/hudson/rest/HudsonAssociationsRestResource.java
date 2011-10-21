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

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.marvelution.jira.plugins.hudson.rest.model.Associations;
import com.marvelution.jira.plugins.hudson.rest.model.Option;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;


/**
 * REST Resource to get all Association information
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("/associations")
@AnonymousAllowed
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class HudsonAssociationsRestResource {

	private HudsonAssociationManager associationManager;

	/**
	 * Default Constructor
	 * 
	 * @param associationManager the {@link HudsonAssociationManager}
	 */
	public HudsonAssociationsRestResource(HudsonAssociationManager associationManager) {
		this.associationManager = associationManager;
	}

	/**
	 * Getter to get all the configured associations.
	 * 
	 * @return an {@link Associations} collection with all the configured associations
	 */
	@GET
	public Response getAssociations() {
		final Collection<Option> options = new ArrayList<Option>();
		for (HudsonAssociation association : associationManager.getAssociations()) {
			final HudsonServer server = association.getServer();
			final String label = association.getJobName() + " (" + server.getName() + ")";
			options.add(new Option(label, String.valueOf(association.getID())));
		}
		return Response.ok(new Associations(options)).build();
	}

}
