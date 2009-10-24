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

package com.marvelution.jira.plugins.hudson.gadgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.rest.v1.model.errors.ValidationError;
import com.atlassian.jira.rest.v1.util.CacheControl;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.marvelution.jira.plugins.hudson.api.model.Job;
import com.marvelution.jira.plugins.hudson.chart.ChartUtils;
import com.marvelution.jira.plugins.hudson.chart.HudsonChart;
import com.marvelution.jira.plugins.hudson.gadgets.model.AbstractResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonChartResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonProjectResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonServerResource;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Gadget Resource for Hudson Charts
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("/hudsonCharts")
@AnonymousAllowed
@Produces({ MediaType.APPLICATION_JSON })
public class HudsonChartsGadgetResource extends AbstractGadgetResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SIMPLE_STYLE;

	private final Logger logger = Logger.getLogger(HudsonChartsGadgetResource.class);

	private HudsonServerManager serverManager;

	private HudsonServerAccessor serverAccessor;

	private ProjectManager projectManager;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param serverAccessor the {@link HudsonServerAccessor} implementation
	 * @param projectManager the {@link ProjectManager} implementation
	 */
	public HudsonChartsGadgetResource(HudsonServerManager serverManager, HudsonServerAccessor serverAccessor,
										ProjectManager projectManager) {
		this.serverManager = serverManager;
		this.serverAccessor = serverAccessor;
		this.projectManager = projectManager;
	}

	/**
	 * Validate the Chart configuration
	 * 
	 * @param projectId the Project Id to generate the requested chart
	 * @return the validated {@link Response} that may contain Errors
	 */
	@GET
	@Path("validate")
	public Response validate(@QueryParam("projectId") String projectId) {
		final Collection<ValidationError> errors = new ArrayList<ValidationError>();
		if (serverManager.isHudsonConfigured()) {
			final Project project = projectManager.getProjectObj(stripFilterPrefix(projectId, PROJECT_PREFIX));
			if (project == null) {
				errors.add(new ValidationError("projectId", "hudson.error.invalid.project.selected"));
			}
		} else {
			errors.add(new ValidationError("projectId", "hudson.error.not.configured"));
		}
		return createValidationResponse(errors);
	}

	/**
	 * Generate the Build Trend chart of a Project
	 * 
	 * @param projectId the Project Id to generate the build trend chart for
	 * @return the {@link Response} containing all JSON object with the Chart data
	 */
	@GET
	@Path("generateBuildTrend")
	public Response generateBuildTrend(@QueryParam("projectId") String projectId) {
		final Set<String> errors = new HashSet<String>();
		if (serverManager.isHudsonConfigured()) {
			final Project project = projectManager.getProjectObj(stripFilterPrefix(projectId, PROJECT_PREFIX));
			if (project != null) {
				final HudsonServer server = serverManager.getServerByJiraProject(project);
				final HudsonServerResource serverResource =
					new HudsonServerResource(server.getName(), server.getHost(), "");
				try {
					final Job job = serverAccessor.getProject(server, project);
					if (job != null) {
						final HudsonProjectResource projectResource =
							new HudsonProjectResource(job.getName(), job.getUrl(), job.getDescription());
						final HudsonChart chart = ChartUtils.generateSuccessFailedRatioChart(server, job);
						chart.generate(ChartUtils.PORTLET_CHART_WIDTH, ChartUtils.PORTLET_CHART_HEIGHT);
						final HudsonChartResource chartResource = new HudsonChartResource(chart);
						return Response.ok(new HudsonChartsResource(serverResource, projectResource, chartResource))
							.cacheControl(CacheControl.NO_CACHE).build();
					} else {
						errors.add("hudson.error.project.not.on.hudson");
					}
				} catch (HudsonServerAccessorException e) {
					logger.error(e.getMessage(), e);
					errors.add("hudson.error.cannot.connect");
				} catch (HudsonServerAccessDeniedException e) {
					logger.error(e.getMessage(), e);
					errors.add("hudson.error.access.denied");
				} catch (IOException e) {
					logger.error("Failed to the Hudson Project build trend Chart", e);
					errors.add("hudson.error.chart.generation.failed");
				}
			} else {
				errors.add("hudson.error.no.valid.project");
			}
		} else {
			errors.add("hudson.error.not.configured");
		}
		return Response.ok(new HudsonChartsResource(errors)).cacheControl(CacheControl.NO_CACHE)
			.build();
	}

	/**
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlRootElement
	public static class HudsonChartsResource extends AbstractResource {

		@XmlElement
		private HudsonServerResource server;

		@XmlElement
		private HudsonProjectResource project;

		@XmlElement
		private HudsonChartResource chart;

		/**
		 * Default Constructor
		 */
		public HudsonChartsResource() {
		}

		/**
		 * Constructor
		 * 
		 * @param server the {@link HudsonServerResource}
		 * @param project the {@link HudsonProjectResource}
		 * @param chart the {@link HudsonChartResource}
		 */
		public HudsonChartsResource(HudsonServerResource server, HudsonProjectResource project,
									HudsonChartResource chart) {
			this.server = server;
			this.chart = chart;
			this.project = project;
		}

		/**
		 * Constructor
		 * 
		 * @param errors the {@link Collection} of Error i18n keys
		 */
		public HudsonChartsResource(Collection<String> errors) {
			super(errors);
		}

		/**
		 * @return the server
		 */
		public HudsonServerResource getServer() {
			return server;
		}

		/**
		 * @return the project
		 */
		public HudsonProjectResource getProject() {
			return project;
		}

		/**
		 * @return the chart
		 */
		public HudsonChartResource getChart() {
			return chart;
		}

		/**
		 * {@inheritDoc}
		 */
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean equals(Object object) {
			return EqualsBuilder.reflectionEquals(this, object);
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			return ToStringBuilder.reflectionToString(this, HudsonChartsGadgetResource.TO_STRING_STYLE);
		}

	}

}
