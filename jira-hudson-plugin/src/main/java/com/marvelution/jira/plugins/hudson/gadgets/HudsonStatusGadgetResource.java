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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.atlassian.jira.rest.v1.model.errors.ValidationError;
import com.atlassian.jira.rest.v1.util.CacheControl;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonBuildResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonProjectResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonServerResource;
import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.HudsonView;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.triggers.Trigger;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.DateTimeUtils;
import com.marvelution.jira.plugins.hudson.utils.HudsonBuildTriggerParser;

/**
 * Resource for the HudsonStatus Gadget
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("/hudsonStatus")
@AnonymousAllowed
@Produces({ MediaType.APPLICATION_JSON })
public class HudsonStatusGadgetResource extends AbstractGadgetResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;

	private final Logger logger = Logger.getLogger(HudsonStatusGadgetResource.class);

	private HudsonServerManager serverManager;

	private HudsonServerAccessor serverAccessor;

	private DateTimeUtils dateTimeUtils;

	private HudsonBuildTriggerParser buildTriggerParser;

	/**
	 * Constructor
	 * 
	 * @param authenticationContext this {@link JiraAuthenticationContext} implementation
	 * @param userUtil the {@link UserUtil} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param serverAccessor the {@link HudsonServerAccessor} implementation
	 */
	public HudsonStatusGadgetResource(JiraAuthenticationContext authenticationContext, UserUtil userUtil,
										HudsonServerManager serverManager, HudsonServerAccessor serverAccessor) {
		this.serverManager = serverManager;
		this.serverAccessor = serverAccessor;
		this.dateTimeUtils = new DateTimeUtils(authenticationContext);
		this.buildTriggerParser = new HudsonBuildTriggerParser(authenticationContext, userUtil);
	}

	/**
	 * Validate the Gadget Configuration
	 * 
	 * @param serverId the {@link HudsonServer} Id configured for the Gadget
	 * @param view the View name to get the projects for, may be <code>null</code> in which case all the projects will
	 *            be returned
	 * @return the {@link Response}
	 */
	@GET
	@Path("validate")
	public Response validate(@QueryParam("serverId") int serverId, @QueryParam("view") String view) {
		final Collection<ValidationError> errors = new ArrayList<ValidationError>();
		if (serverManager.isHudsonConfigured()) {
			final HudsonServer hudsonServer = serverManager.getServer(serverId);
			if (hudsonServer != null) {
				try {
					if (StringUtils.isNotBlank(view)) {
						final List<HudsonView> views = serverAccessor.getViewsList(hudsonServer);
						if (!views.contains(new HudsonView(view, ""))) {
							errors.add(new ValidationError("view", "hudson.error.invalid.server.view.selected"));
						}
					}
				} catch (HudsonServerAccessorException e) {
					errors.add(new ValidationError("serverId", "hudson.error.cannot.connect"));
				} catch (HudsonServerAccessDeniedException e) {
					errors.add(new ValidationError("serverId", "hudson.error.access.denied"));
				}
			} else {
				errors.add(new ValidationError("serverId", "hudson.error.invalid.server.selected"));
			}
		}
		return createValidationResponse(errors);
	}

	/**
	 * Generate the content variables for the Gadget
	 * 
	 * @param serverId the {@link HudsonServer} Id configured for the Gadget
	 * @param view the View name to get the projects for, may be <code>null</code> in which case all the projects will
	 *            be returned
	 * @return the {@link Response}
	 */
	@GET
	@Path("generate")
	public Response generate(@QueryParam("serverId") int serverId, @QueryParam("view") String view) {
		final Set<String> errors = new HashSet<String>();
		if (serverManager.isHudsonConfigured()) {
			HudsonServer hudsonServer = serverManager.getServer(serverId);
			if (hudsonServer == null) {
				hudsonServer = serverManager.getDefaultServer();
			}
			final HudsonServerResource server = new HudsonServerResource(hudsonServer.getName(), hudsonServer
					.getHost(), hudsonServer.getSmallImageUrl());
			final List<HudsonProjectResource> projects = new ArrayList<HudsonProjectResource>();
			buildTriggerParser.setServer(hudsonServer);
			try {
				List<Job> jobs;
				if (StringUtils.isNotBlank(view)) {
					jobs = serverAccessor.getView(hudsonServer, view).getJobs();
				} else {
					jobs = serverAccessor.getProjects(hudsonServer);
				}
				for (Job job : jobs) {
					final HudsonProjectResource project =
						new HudsonProjectResource(job.getName(), job.getUrl(), job.getDescription());
					final Build build = job.getLastBuild();
					final String trigger =
						(build.getTriggers().isEmpty() ? buildTriggerParser.parse(new Trigger() {
						}) : buildTriggerParser.parse(build.getTriggers().get(0)));
					project.addBuild(new HudsonBuildResource(build.getNumber(), dateTimeUtils
						.getTimeSpanString(build.getDuration()), dateTimeUtils.getPastTimeString(build
						.getTimestamp()), trigger, build.getResult().name().toLowerCase(), build.getResult()
						.getIcon()));
					projects.add(project);
				}
			} catch (HudsonServerAccessorException e) {
				logger.error(e.getMessage(), e);
				errors.add("hudson.error.cannot.connect");
			} catch (HudsonServerAccessDeniedException e) {
				logger.error(e.getMessage(), e);
				errors.add("hudson.error.access.denied");
			}
			return Response.ok(new HudsonStatusResource(server, projects, errors)).cacheControl(
				CacheControl.NO_CACHE).build();
		} else {
			errors.add("hudson.error.not.configured");
		}
		return Response.ok(new HudsonStatusResource(errors)).cacheControl(CacheControl.NO_CACHE).build();
	}

	/**
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlType(namespace = "com.marvelution.jira.plugins.hudson.gadgets.HudsonStatusGadgetResource")
	@XmlRootElement
	public static class HudsonStatusResource {

		@XmlElement
		private HudsonServerResource server;

		@XmlElement
		private Collection<HudsonProjectResource> projects;

		@XmlElement
		private boolean hasProjects;

		@XmlElement
		private boolean hasErrors;

		@XmlElement
		private Collection<String> errors;

		/**
		 * Default constructor
		 */
		public HudsonStatusResource() {
		}

		/**
		 * Constructor
		 * 
		 * @param server the {@link HudsonServerResource} where the builds are running on
		 * @param projects the {@link HudsonProjectResource} the projects on the Hudson Server
		 * @param errors {@link Collection} of i18n error keys, may be <code>null</code>
		 */
		public HudsonStatusResource(HudsonServerResource server, Collection<HudsonProjectResource> projects,
									Collection<String> errors) {
			this.server = server;
			this.projects = projects;
			hasProjects = (this.projects != null ? !this.projects.isEmpty() : false);
			this.errors = errors;
			hasErrors = (this.errors != null ? !this.errors.isEmpty() : false);
		}

		/**
		 * Constructor
		 * 
		 * @param errors {@link Collection} of i18n error keys, may be <code>null</code>
		 */
		public HudsonStatusResource(Collection<String> errors) {
			hasProjects = false;
			this.errors = errors;
			hasErrors = (this.errors != null ? !this.errors.isEmpty() : false);
		}

		/**
		 * @return the server
		 */
		public HudsonServerResource getServer() {
			return server;
		}

		/**
		 * @return the hasBuilds
		 */
		public boolean isHasProjects() {
			return hasProjects;
		}

		/**
		 * @return the builds
		 */
		public Collection<HudsonProjectResource> getProjects() {
			return projects;
		}

		/**
		 * @return the hasErrors
		 */
		public boolean isHasErrors() {
			return hasErrors;
		}

		/**
		 * @return the errors
		 */
		public Collection<String> getErrors() {
			return errors;
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
			return ToStringBuilder.reflectionToString(this, HudsonStatusGadgetResource.TO_STRING_STYLE);
		}

	}

}
