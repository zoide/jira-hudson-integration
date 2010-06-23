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

package com.marvelution.jira.plugins.hudson;

import com.marvelution.jira.plugins.hudson.service.AbstractHudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * Hudson Server Configuration access object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerImpl extends AbstractHudsonServer {

	/**
	 * Default constructors
	 */
	public DefaultHudsonServerImpl() {
		super();
	}

	/**
	 * Construct {@link HudsonServer} based on a {@link HudsonServer}
	 * 
	 * @param hudsonServer the {@link HudsonServer} to copy
	 */
	public DefaultHudsonServerImpl(HudsonServer hudsonServer) {
		super(hudsonServer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(HudsonServer otherServer) {
		if (otherServer == null) {
			throw new IllegalArgumentException("otherServer may not be null");
		}
		return getName().compareTo(otherServer.getName());
	}

}
