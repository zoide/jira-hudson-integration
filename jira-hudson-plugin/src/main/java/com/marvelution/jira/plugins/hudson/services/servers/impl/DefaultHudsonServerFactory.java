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

package com.marvelution.jira.plugins.hudson.services.servers.impl;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerFactory;

/**
 * Default {@link HudsonServerFactory} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerFactory implements HudsonServerFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer create() {
		return new DefaultHudsonServer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer create(String name, String url) {
		return new DefaultHudsonServer(name, url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer create(String name, String url, String username, String password) {
		return new DefaultHudsonServer(name, url, username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer create(String name, Host host) {
		return new DefaultHudsonServer(name, host.getHost(), host.getUsername(), host.getPassword());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer create(HudsonServer server) {
		return new DefaultHudsonServer(server);
	}

}
