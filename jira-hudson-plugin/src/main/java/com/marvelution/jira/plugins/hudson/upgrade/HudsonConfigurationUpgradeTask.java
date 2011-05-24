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

package com.marvelution.jira.plugins.hudson.upgrade;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.atlassian.sal.api.message.Message;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import com.marvelution.jira.plugins.hudson.services.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.services.HudsonPropertyManager;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;
import com.opensymphony.module.propertyset.PropertySet;

/**
 * {@link PluginUpgradeTask} to upgrade the plugin configuration to the new format.
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonConfigurationUpgradeTask implements PluginUpgradeTask {

	private static final Logger LOG = Logger.getLogger(HudsonConfigurationUpgradeTask.class);

	private static final String OLD_HIDE_UNASSOCIATED_KEY = "hudson.config.settings.hide.unassociated.hudson.tab";

	private final HudsonConfigurationManager configurationManager;
	private final HudsonPropertyManager propertyManager;

	/**
	 * Constructor
	 * 
	 * @param configurationManager the {@link HudsonConfigurationManager} implementation
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 */
	public HudsonConfigurationUpgradeTask(HudsonConfigurationManager configurationManager,
					HudsonPropertyManager propertyManager) {
		this.configurationManager = configurationManager;
		this.propertyManager = propertyManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Message> doUpgrade() throws Exception {
		PropertySet propertySet = propertyManager.getPropertySet();
		if (propertySet.exists(OLD_HIDE_UNASSOCIATED_KEY)) {
			LOG.info("Copying the 'Hide Unassiciated Hudson Tabs' configuration key from the previous plugin version");
			configurationManager.setHideUnassociatedHudsonTabs(propertySet.getBoolean(OLD_HIDE_UNASSOCIATED_KEY));
			LOG.info("Removing the old key");
			propertySet.remove(OLD_HIDE_UNASSOCIATED_KEY);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBuildNumber() {
		return 4;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getShortDescription() {
		return "Upgrade the Hudson Configuration structure of plugin version 3.3.0 to version 4.0.0";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPluginKey() {
		return JiraPluginUtils.getPluginKey();
	}

}
