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

package com.marvelution.jira.plugins.hudson.panels.resultset;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * {@link ResultSet} implementation specific for builds listing (buildsByJob/buildsByIssue) views
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildsResultSet extends AbstractResultSet<Builds> {

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param builds the {@link Builds}
	 */
	public BuildsResultSet(HudsonServer server, Builds builds) {
		super(server, builds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultViews getView() {
		return ResultViews.BUILDS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasResults() {
		return (getResults() != null && !getResults().isEmpty());
	}

}
