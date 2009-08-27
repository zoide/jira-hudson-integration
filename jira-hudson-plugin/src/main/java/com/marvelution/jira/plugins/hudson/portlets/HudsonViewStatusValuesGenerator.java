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

package com.marvelution.jira.plugins.hudson.portlets;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.log4j.Logger;

import com.atlassian.configurable.ValuesGenerator;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.web.bean.I18nBean;
import com.marvelution.jira.plugins.hudson.model.HudsonView;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link ValuesGenerator} implementation to generate the {@link Map} of possible values for the
 * {@link HudsonViewStatusPortlet} hudson view property
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonViewStatusValuesGenerator implements ValuesGenerator {

	/**
	 * Log4j Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(HudsonViewStatusValuesGenerator.class);

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Map getValues(Map params) {
		final Map values = new ListOrderedMap();
		for (HudsonServer server : getServerManager().getServers()) {
			try {
				final List<HudsonView> views = getServerAccessor().getViewsList(server);
				for (HudsonView view : views) {
					values.put(server.getServerId() + ";view:" + view.getName(), server.getName() + " - "
						+ view.getName());
				}
			} catch (HudsonServerAccessorException e) {
				LOGGER.error("Failed to get Views from Hudson Server: " + server.getName(), e);
			} catch (HudsonServerAccessDeniedException e) {
				LOGGER.error("Failed to get Views from Hudson Server: " + server.getName(), e);
			}
		}
		return values;
	}

	/**
	 * Get internationalisation text
	 * 
	 * @param key the key of the text to get
	 * @return the text
	 */
	String getText(String key) {
		return new I18nBean().getText(key);
	}

	/**
	 * Get the {@link HudsonServerManager} from the {@link ComponentManager}
	 * 
	 * @return the {@link HudsonServerManager}
	 */
	private HudsonServerManager getServerManager() {
		return (HudsonServerManager) ComponentManager.getComponentInstanceOfType(HudsonServerManager.class);
	}

	/**
	 * Get the {@link HudsonServerAccessor} from the {@link ComponentManager}
	 * 
	 * @return the {@link HudsonServerAccessor}
	 */
	private HudsonServerAccessor getServerAccessor() {
		return (HudsonServerAccessor) ComponentManager.getComponentInstanceOfType(HudsonServerAccessor.class);
	}

}
