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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.marvelution.hudson.plugins.apiv2.client.ClientException;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Jobs;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchAssociationException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchServerException;
import com.marvelution.jira.plugins.hudson.rest.model.Server;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Helper REST Resource for HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("server")
@Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED } )
@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
@Singleton
public class HudsonServerRestResource {

	private final HudsonServerManager serverManager;
	private final HudsonAssociationManager associationManager;
	private final HudsonClientFactory clientFactory;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	public HudsonServerRestResource(HudsonServerManager serverManager, HudsonAssociationManager associationManager,
					HudsonClientFactory clientFactory) {
		this.serverManager = serverManager;
		this.associationManager = associationManager;
		this.clientFactory = clientFactory;
	}

	/**
	 * Helper method to get all the Jobs configured on a {@link HudsonServer} with the given Id
	 * 
	 * @param serverId the {@link HudsonServer} Id
	 * @return the {@link Jobs}
	 * @throws ClientException in case of {@link HudsonClient} communication issues
	 */
	@GET
	@Path("{serverId}/listJobs")
	public Jobs listAllJobs(@PathParam("serverId") Integer serverId) throws ClientException {
		if (serverManager.hasServer(serverId)) {
			final HudsonClient client = clientFactory.create(serverManager.getServer(serverId));
			return client.findAll(JobQuery.createForJobList(true, false));
		} else {
			throw new NoSuchServerException(serverId);
		}
	}

	/**
	 * Helper method to get the {@link Job} data of a Hudson Job via its {@link HudsonAssociation} Id
	 * 
	 * @param associationId the {@link HudsonAssociation} Id to get the Job Data for
	 * @param includeAllBuilds flag to include all builds in the job response
	 * @return the {@link Job}
	 * @throws ClientException in case of {@link HudsonClient} communication issues
	 */
	@GET
	@Path("job/{associationId}")
	public Job getJobData(@PathParam("associationId") Integer associationId,
					@QueryParam("includeAllBuilds") @DefaultValue("false") boolean includeAllBuilds)
					throws ClientException {
		if (associationManager.hasAssociation(associationId)) {
			final HudsonAssociation association = associationManager.getAssociation(associationId);
			final HudsonClient client = clientFactory.create(serverManager.getServer(association.getServerId()));
			return client.find(JobQuery.createForJobByName(association.getJobName(), includeAllBuilds));
		} else {
			throw new NoSuchAssociationException(associationId);
		}
	}

	/**
	 * Getter for the Server details via its Id.
	 * 
	 * @param serverId the Server Id
	 * @return the {@link Server} details
	 */
	@GET
	@Path("{serverId}")
	public Server getServer(@PathParam("serverId") Integer serverId) {
		if (serverManager.hasServer(serverId)) {
			return new Server(serverManager.getServer(serverId));
		} else {
			throw new NoSuchServerException(serverId);
		}
	}

	/**
	 * Getter for the Server details via an {@link HudsonAssociation} Id.
	 * 
	 * @param associationId the {@link HudsonAssociation} Id
	 * @return the {@link Server} details
	 */
	@GET
	public Server getServerByAssociation(@QueryParam("associationId") Integer associationId) {
		if (associationManager.hasAssociation(associationId)) {
			return getServer(associationManager.getAssociation(associationId).getServerId());
		} else {
			throw new NoSuchAssociationException(associationId);
		}
	}

}
