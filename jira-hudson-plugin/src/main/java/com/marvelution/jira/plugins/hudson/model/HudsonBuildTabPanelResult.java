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

package com.marvelution.jira.plugins.hudson.model;

import java.util.List;

import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * {@link HudsonServerResult} implementation for Build Tab Panels
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildTabPanelResult extends HudsonServerResult<List<Build>> {

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer} the result originated from
	 */
	public HudsonBuildTabPanelResult(HudsonServer server) {
		super(server);
	}

	/**
	 * Wrapper for {@link #getResults()}
	 * 
	 * @return the {@link List} of {@link Build} objects
	 */
	public List<Build> getBuilds() {
		return getResults();
	}

	/**
	 * Wrapper for {@link #setResults(List)}
	 * 
	 * @param builds the {@link List} of {@link Build} objects
	 */
	public void setBuilds(List<Build> builds) {
		setResults(builds);
	}

	/**
	 * Check if the results contain any builds
	 * 
	 * @return <code>true</code> if {@link #getResults()} != <code>null</code> and {@link #getResults()} !=
	 *         <code>empty</code>
	 */
	public boolean hasBuilds() {
		return getBuilds() != null && !getBuilds().isEmpty();
	}

	/**
	 * Gets the {@link HudsonServer} Image URL
	 * 
	 * @return the Image URL
	 */
	@Override
	public String getServerImageUrl() {
		return getServerLargeImageUrl();
	}

}
