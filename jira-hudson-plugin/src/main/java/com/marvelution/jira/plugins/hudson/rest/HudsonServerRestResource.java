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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.Jobs;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchServerException;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Hekper REST Resource for HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("server")
@Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED } )
@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
@Singleton
public class HudsonServerRestResource {

	private final HudsonServerManager serverManager;
	private final HudsonClientFactory clientFactory;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	public HudsonServerRestResource(HudsonServerManager serverManager, HudsonClientFactory clientFactory) {
		this.serverManager = serverManager;
		this.clientFactory = clientFactory;
	}

	/**
	 * Helper method to get all the Jobs configured on a {@link HudsonServer} with the given Id
	 * 
	 * @param serverId the {@link HudsonServer} Id
	 * @return the {@link Jobs}
	 */
	@GET
	@Path("{serverId}/listJobs")
	public Jobs listAllJobs(@PathParam("serverId") Integer serverId) {
		if (serverManager.hasServer(serverId)) {
			final HudsonClient client = clientFactory.create(serverManager.getServer(serverId));
			return client.findAll(JobQuery.createForJobList());
		} else {
			throw new NoSuchServerException(serverId);
		}
	}

}
