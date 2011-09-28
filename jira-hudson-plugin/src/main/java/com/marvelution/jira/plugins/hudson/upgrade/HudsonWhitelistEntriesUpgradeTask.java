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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.atlassian.jira.bc.whitelist.WhitelistManager;
import com.atlassian.sal.api.message.Message;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;


/**
 * {@link PluginUpgradeTask} to add all the Configured Hudson/Jenkins Server URLs to the whitelist
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 *@since 4.2.0
 */
public class HudsonWhitelistEntriesUpgradeTask implements PluginUpgradeTask {

	private final Logger logger = Logger.getLogger(HudsonWhitelistEntriesUpgradeTask.class);

	private final WhitelistManager whitelistManager;

	private final HudsonServerManager serverManager;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param whitelistManager the {@link WhitelistManager} implementation
	 */
	public HudsonWhitelistEntriesUpgradeTask(HudsonServerManager serverManager, WhitelistManager whitelistManager) {
		this.serverManager = serverManager;
		this.whitelistManager = whitelistManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Message> doUpgrade() throws Exception {
		if (serverManager.hasServers()) {
			logger.debug("We have Servers, start the upgrade task");
			List<String> newRules = new ArrayList<String>(whitelistManager.getRules());
			for (HudsonServer server : serverManager.getServers()) {
				if (!newRules.contains(server.getHostWhitelistUrl())) {
					newRules.add(server.getHostWhitelistUrl());
				}
				if (!newRules.contains(server.getPublicHostWhitelistUrl())) {
					newRules.add(server.getPublicHostWhitelistUrl());
				}
			}
			logger.debug("Updating the Whitelist rules");
			whitelistManager.updateRules(newRules, whitelistManager.isDisabled());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBuildNumber() {
		return 5;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getShortDescription() {
		return "Upgrade task to add all teh server urls to the whitelist";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPluginKey() {
		return JiraPluginUtils.getPluginKey();
	}

}
