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

package com.marvelution.jira.plugins.hudson.charts;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.util.I18nHelper;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class AbstractHudsonChartGenerator implements HudsonChartGenerator {

	private I18nHelper i18nHelper;
	protected final HudsonServer server;
	protected final Builds builds;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer} the data came from
	 * @param builds the {@link Builds} collection
	 * @param i18nHelper the {@link I18nHelper} class to use
	 */
	protected AbstractHudsonChartGenerator(HudsonServer server, Builds builds, I18nHelper i18nHelper) {
		this.server = server;
		this.builds = builds;
		this.i18nHelper = i18nHelper;
	}

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer} the data came from
	 * @param builds the {@link Builds} collection
	 */
	protected AbstractHudsonChartGenerator(HudsonServer server, Builds builds) {
		this.server = server;
		this.builds = builds;
		this.i18nHelper = null;
	}

	/**
	 * Getter for the {@link I18nHelper}
	 * 
	 * @return the {@link I18nHelper}
	 */
	protected final I18nHelper getI18n() {
		if (i18nHelper == null) {
			i18nHelper = ComponentManager.getInstance().getJiraAuthenticationContext().getI18nHelper();
		}
		return i18nHelper;
	}

}
