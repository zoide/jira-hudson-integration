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

package com.marvelution.jira.plugins.hudson.web.action;

import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerFactory;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link AbstractEditHudsonServer} implementation for adding HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AddHudsonServer extends AbstractEditHudsonServer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 * @param hudsonServerFactory the {@link HudsonServerFactory} implementation
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor} implementation
	 */
	public AddHudsonServer(HudsonServerManager hudsonServerManager, HudsonServerFactory hudsonServerFactory,
					HudsonServerAccessor hudsonServerAccessor) {
		super(hudsonServerManager, hudsonServerFactory, hudsonServerAccessor);
	}

	/**
	 * {@inheritDoc}
	 */
	public void doValidation() {
		super.doValidation();
		if (getHudsonServerManager().hasServer(getName())) {
			addError("name", getText("hudson.config.serverName.duplicate"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String doExecute() throws Exception {
		if (hasAnyErrors()) {
			return "input";
		}
		getHudsonServerManager().put(hudsonServer);
		return getRedirect("ViewHudsonServers.jspa");
	}

}
