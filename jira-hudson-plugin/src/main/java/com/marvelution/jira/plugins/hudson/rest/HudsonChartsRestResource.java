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

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.jira.plugins.hudson.charts.BuildResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.BuildTestResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchAssociationException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchJobException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchServerException;
import com.marvelution.jira.plugins.hudson.rest.model.Chart;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.sun.jersey.spi.resource.Singleton;

/**
 * REST Endpoint for Hudson Charts
 * 
 * Charts are available at: <code>[BASE_URL]/charts?filename=[CHART_LOCATION]</code>
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("charts")
@Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED } )
@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
@Singleton
public class HudsonChartsRestResource {

	public static final int CHART_WIDTH = 350;
	public static final int CHART_HEIGHT = 250;

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
	public HudsonChartsRestResource(HudsonServerManager serverManager, HudsonAssociationManager associationManager,
			HudsonClientFactory clientFactory) {
		this.serverManager = serverManager;
		this.associationManager = associationManager;
		this.clientFactory = clientFactory;
	}

	@GET
	@Path("buildResultsRationChart/{associationId}")
	public Chart getBuildResultsRatioChart(@PathParam("associationId") int associationId) throws IOException {
		if (associationManager.hasAssociation(associationId)) {
			final HudsonAssociation association= associationManager.getAssociation(associationId);
			return getBuildResultsRatioChart(association.getServerId(), association.getJobName());
		} else {
			throw new NoSuchAssociationException(associationId);
		}
	}

	@GET
	@Path("buildResultsRationChart")
	public Chart getBuildResultsRatioChart(@QueryParam("serverId") int serverId, @QueryParam("jobName") String jobname) throws IOException {
		if (serverManager.hasServer(serverId)) {
			final HudsonServer server = serverManager.getServer(serverId);
			final HudsonClient client = clientFactory.create(server);
			final Job job = client.find(JobQuery.createForJobByName(jobname));
			if (job != null && StringUtils.isNotBlank(job.getName())) {
				final ChartHelper chartHelper = new BuildResultsRatioChartGenerator(server, job.getBuilds()).generateChart();
				chartHelper.generate(CHART_WIDTH, CHART_HEIGHT);
				return new Chart(chartHelper);
			} else {
				throw new NoSuchJobException(jobname);
			}
		} else {
			throw new NoSuchServerException(serverId);
		}
	}

	@GET
	@Path("buildTestResultsRationChart/{associationId}")
	public Chart getBuildTestResultsRatioChart(@PathParam("associationId") int associationId) throws IOException {
		if (associationManager.hasAssociation(associationId)) {
			final HudsonAssociation association= associationManager.getAssociation(associationId);
			return getBuildTestResultsRatioChart(association.getServerId(), association.getJobName());
		} else {
			throw new NoSuchAssociationException(associationId);
		}
	}

	@GET
	@Path("buildTestResultsRationChart")
	public Chart getBuildTestResultsRatioChart(@QueryParam("serverId") int serverId, @QueryParam("jobName") String jobname) throws IOException {
		if (serverManager.hasServer(serverId)) {
			final HudsonServer server = serverManager.getServer(serverId);
			final HudsonClient client = clientFactory.create(server);
			final Job job = client.find(JobQuery.createForJobByName(jobname));
			if (job != null && StringUtils.isNotBlank(job.getName())) {
				final ChartHelper chartHelper = new BuildTestResultsRatioChartGenerator(server, job.getBuilds()).generateChart();
				chartHelper.generate(CHART_WIDTH, CHART_HEIGHT);
				return new Chart(chartHelper);
			} else {
				throw new NoSuchJobException(jobname);
			}
		} else {
			throw new NoSuchServerException(serverId);
		}
	}

}
