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

import com.atlassian.jira.rest.v1.util.CacheControl;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.marvelution.jira.plugins.hudson.gadgets.model.OptionResource;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * REST resource to get HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("/hudsonServers")
@AnonymousAllowed
@Produces({ MediaType.APPLICATION_JSON })
public class HudsonServersResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SIMPLE_STYLE;

	private HudsonServerManager serverManager;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	public HudsonServersResource(HudsonServerManager serverManager) {
		this.serverManager = serverManager;
	}

	/**
	 * Generate the list of HudsonServers
	 * 
	 * @return {@link Response} containing all the configured HudsonServers
	 */
	@GET
	public Response generate() {
		final List<OptionResource> options = new ArrayList<OptionResource>();
		for (HudsonServer server : serverManager.getServers()) {
			options.add(new OptionResource(server.getName(), Integer.toString(server.getServerId())));
		}
		return Response.ok(new HudsonServers(options)).cacheControl(CacheControl.NO_CACHE).build();
	}

	/**
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlType(namespace = "com.marvelution.jira.plugins.hudson.gadgets.HudsonServersResource")
	@XmlRootElement
	public static class HudsonServers {
		
		@XmlElement
		private Collection<OptionResource> servers;

		/**
		 * Default Constructor
		 */
		public HudsonServers() {
		}

		/**
		 * Constructor
		 * 
		 * @param servers the {@link Collection} of {@link OptionResource}
		 */
		public HudsonServers(Collection<OptionResource> servers) {
			this.servers = servers;
		}

		/**
		 * Get the {@link Collection} of {@link OptionResource}
		 * 
		 * @return {@link Collection} of {@link OptionResource}
		 */
		public Collection<OptionResource> getServers() {
			return servers;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object object) {
			return EqualsBuilder.reflectionEquals(this, object);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, HudsonServersResource.TO_STRING_STYLE);
		}

	}

}
