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

package com.marvelution.jira.plugins.hudson.services.impl;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.connectors.Connector;
import com.marvelution.hudson.plugins.apiv2.client.connectors.HttpClient4Connector;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.impl.DefaultHudsonServer;

/**
 * Default {@link HudsonClientFactory}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonClientFactory implements HudsonClientFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonClient create(HudsonServer server) {
		return new HudsonClient(getConnector(server));
	}

	/**
	 * Get a connector for the given {@link HudsonServer}
	 * 
	 * @param server the {@link HudsonServer} to get the {@link Connector} for
	 * @return the {@link HttpClient4Connector}
	 */
	private HttpClient4Connector getConnector(HudsonServer server) {
		Host host = null;
		if (server instanceof DefaultHudsonServer) {
			host = (DefaultHudsonServer) server;
		} else {
			host = new Host(server.getHost(), server.getUsername(), server.getPassword());
		}
		return new HttpClient4Connector(host);
	}

}
