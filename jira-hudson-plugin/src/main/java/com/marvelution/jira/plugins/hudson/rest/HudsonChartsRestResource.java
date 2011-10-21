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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.atlassian.sal.api.message.I18nResolver;
import com.marvelution.hudson.plugins.apiv2.client.ClientException;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.jira.plugins.hudson.charts.BuildResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.BuildTestResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.HudsonChartGenerator;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchAssociationException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchChartException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchJobException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchServerException;
import com.marvelution.jira.plugins.hudson.rest.model.Chart;
import com.marvelution.jira.plugins.hudson.rest.model.Charts;
import com.marvelution.jira.plugins.hudson.rest.model.Option;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonClientFactory;
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

	private final I18nResolver i18nResolver;
	private final HudsonServerManager serverManager;
	private final HudsonAssociationManager associationManager;
	private final HudsonClientFactory clientFactory;

	private final Map<String, Class<? extends HudsonChartGenerator>> charts =
		new HashMap<String, Class<? extends HudsonChartGenerator>>();

	/**
	 * Constructor
	 * 
	 * @param i18nResolver the {@link I18nResolver} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	public HudsonChartsRestResource(I18nResolver i18nResolver, HudsonServerManager serverManager,
			HudsonAssociationManager associationManager, HudsonClientFactory clientFactory) {
		this.i18nResolver = i18nResolver;
		this.serverManager = serverManager;
		this.associationManager = associationManager;
		this.clientFactory = clientFactory;
		// TODO Dynamically lookup all the HudsonChartGenerator implementations
		charts.put("BuildResultsRatioChartGenerator", BuildResultsRatioChartGenerator.class);
		charts.put("BuildTestResultsRatioChartGenerator", BuildTestResultsRatioChartGenerator.class);
	}

	/**
	 * Endpoint method to generate a given chart for an Association specified
	 * 
	 * @param type the type of Chart to generate (This is used to select the {@link HudsonChartGenerator} implementation
	 * @param associationId the Id of the Hudson Association configured using the <a href="http://docs.marvelution.com/display/MARVJIRAHUDSON/Manage%20Hudson%20Associations">Manage Hudson Associations</a> feature
	 * @return the {@link Chart} object with all the information related to the chart
	 * @throws IOException in case of errors
	 * @throws ClientException in case of {@link HudsonClient} communication issues
	 */
	@GET
	@Path("generate/{type}/{associationId}")
	public Chart getChart(@PathParam("type") String type, @PathParam("associationId") int associationId)
			throws IOException, ClientException {
		if (associationManager.hasAssociation(associationId)) {
			final HudsonAssociation association = associationManager.getAssociation(associationId);
			return getChart(type, association.getServer().getID(), association.getJobName());
		} else {
			throw new NoSuchAssociationException(associationId);
		}
	}

	/**
	 * Endpoint method to generate a given chart for a server and jobname specified
	 * 
	 * @param type the type of Chart to generate (This is used to select the {@link HudsonChartGenerator} implementation
	 * @param serverId the Id of the Hudson Server that was configured using the <a href="http://docs.marvelution.com/display/MARVJIRAHUDSON/Manage%20Hudson%20Servers">Manage Hudson Servers</a> feature
	 * @param jobname the job name of the job to get the data for
	 * @return the {@link Chart} object with all the information related to the chart
	 * @throws IOException in case of errors
	 * @throws ClientException in case of {@link HudsonClient} communication issues
	 */
	@GET
	@Path("generate/{type}")
	public Chart getChart(@PathParam("type") String type, @QueryParam("serverId") int serverId,
			@QueryParam("jobName") String jobname) throws IOException, ClientException {
		if (serverManager.hasServer(serverId)) {
			final HudsonServer server = serverManager.getServer(serverId);
			final HudsonClient client = clientFactory.create(server);
			final Job job = client.find(JobQuery.createForJobByName(jobname, true));
			if (job != null && StringUtils.isNotBlank(job.getName())) {
				ChartHelper chartHelper = null;
				if (charts.containsKey(type)) {
					try {
						HudsonChartGenerator chartGenerator = charts.get(type).newInstance();
						chartGenerator.setData(server, job);
						chartHelper = chartGenerator.generateChart();
					} catch (InstantiationException e) {
						throw new RuntimeException("Failed to instantiate the Chart Generator: " + type, e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("Illegal access attempt", e);
					}
				} else {
					throw new NoSuchChartException(type);
				}
				chartHelper.generate(CHART_WIDTH, CHART_HEIGHT);
				return new Chart(chartHelper);
			} else {
				throw new NoSuchJobException(jobname);
			}
		} else {
			throw new NoSuchServerException(serverId);
		}
	}

	/**
	 * Getter for all the supported Charts that this Rest Endpoint can generate
	 * 
	 * @return {@link Charts} collection with all charts supported
	 */
	@GET
	public Response getCharts() {
		final Collection<Option> options = new ArrayList<Option>();
		for (Map.Entry<String, Class<? extends HudsonChartGenerator>> entry : charts.entrySet()) {
			final String label = i18nResolver.getText("hudson.charts." + entry.getKey() + ".title");
			options.add(new Option(label, entry.getKey()));
		}
		return Response.ok(new Charts(options)).build();
	}

}
