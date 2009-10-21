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
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.atlassian.jira.rest.v1.util.CacheControl;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.marvelution.jira.plugins.hudson.api.model.HudsonView;
import com.marvelution.jira.plugins.hudson.gadgets.model.GroupResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.OptionResource;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * REST resource to get HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("/hudsonViews")
@AnonymousAllowed
@Produces({ MediaType.APPLICATION_JSON })
public class HudsonViewsResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SIMPLE_STYLE;

	private final Logger logger = Logger.getLogger(HudsonViewsResource.class);

	private HudsonServerManager serverManager;

	private HudsonServerAccessor serverAccessor;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param serverAccessor the {@link HudsonServerAccessor} implementation
	 */
	public HudsonViewsResource(HudsonServerManager serverManager, HudsonServerAccessor serverAccessor) {
		this.serverManager = serverManager;
		this.serverAccessor = serverAccessor;
	}

	/**
	 * Generate the list of HudsonServers
	 * 
	 * @return {@link Response} containing all the configured HudsonServers
	 */
	@GET
	public Response generate() {
		final List<GroupResource> groups = new ArrayList<GroupResource>();
		for (HudsonServer server : serverManager.getServers()) {
			try {
				final List<OptionResource> options = new ArrayList<OptionResource>();
				for (HudsonView view : serverAccessor.getViewsList(server)) {
					options.add(new OptionResource(view.getName(), Integer.toString(server.getServerId())));
				}
				groups.add(new GroupResource(server.getName(), options));
			} catch (HudsonServerAccessorException e) {
				logger.error("Failed to connect to Hudson server " + server.getName(), e);
			} catch (HudsonServerAccessDeniedException e) {
				logger.error("Hudson server " + server.getName() + " denied access to the API", e);
			}
		}
		return Response.ok(new HudsonViews(groups)).cacheControl(CacheControl.NO_CACHE).build();
	}

	/**
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlType(namespace = "om.marvelution.jira.plugins.hudson.gadgets.HudsonViewsResource")
	@XmlRootElement
	public static class HudsonViews {
		
		@XmlElement
		private Collection<GroupResource> views;

		/**
		 * Default Constructor
		 */
		public HudsonViews() {
		}

		/**
		 * Constructor
		 * 
		 * @param views the {@link Collection} of {@link GroupResource}
		 */
		public HudsonViews(Collection<GroupResource> views) {
			this.views = views;
		}

		/**
		 * Get the {@link Collection} of {@link GroupResource}
		 * 
		 * @return {@link Collection} of {@link GroupResource}
		 */
		public Collection<GroupResource> getViews() {
			return views;
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
			return ToStringBuilder.reflectionToString(this, HudsonViewsResource.TO_STRING_STYLE);
		}

	}

}
