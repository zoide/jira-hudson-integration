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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.sal.api.message.Message;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import com.marvelution.jira.plugins.hudson.encryption.StringEncrypter;
import com.marvelution.jira.plugins.hudson.services.HudsonPropertyManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerIdGenerator;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;
import com.opensymphony.module.propertyset.PropertySet;


/**
 * {@link PluginUpgradeTask} to upgrade the HudsonServer configuration to the new version
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonServerUpgradeTask implements PluginUpgradeTask {

	static final String CONFIG_NEXT_SERVER_ID = "hudson.config.servers.nextId";
    static final String CONFIG_DEFAULT_SERVER_ID = "hudson.config.servers.defaultId";
    static final String CONFIG_SERVER_KEY_PREFIX = "hudson.config.server.";
    static final String CONFIG_SERVER_ID_KEY_SUFFIX = ".id";
    static final String CONFIG_SERVER_NAME_KEY_SUFFIX = ".name";
    static final String CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX = ".description";
    static final String CONFIG_SERVER_HOST_KEY_SUFFIX = ".host";
    static final String CONFIG_SERVER_USERNAME_KEY_SUFFIX = ".username";
    static final String CONFIG_SERVER_PASSWORD_KEY_SUFFIX = ".password";
    static final String CONFIG_SERVER_PROJECTS_KEY_SUFFIX = ".projects";
    static final String CONFIG_SERVER_CRUMB_KEY_SUFFIX = ".crumb";
    static final String CONFIG_SERVER_CRUMB_FIELD_KEY_SUFFIX = ".crumb.field";

	private static final Logger LOGGER = Logger.getLogger(HudsonServerUpgradeTask.class);
	private static final StringEncrypter ENCRYPTER = new StringEncrypter();

	private final HudsonServerFactory serverFactory;
	private final HudsonServerManager serverManager;
	private final HudsonServerIdGenerator idGenerator;
	private final PropertySet propertySet;

	/**
	 * Constructor
	 * 
	 * @param serverFactory the {@link HudsonServerFactory} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param idGenerator the {@link HudsonServerIdGenerator} implementation
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 */
	public HudsonServerUpgradeTask(HudsonServerFactory serverFactory, HudsonServerManager serverManager,
					HudsonServerIdGenerator idGenerator, HudsonPropertyManager propertyManager) {
		this.serverFactory = serverFactory;
		this.serverManager = serverManager;
		this.idGenerator = idGenerator;
		this.propertySet = propertyManager.getPropertySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Message> doUpgrade() throws Exception {
		final List<Integer> serverIds = new ArrayList<Integer>();
		final Collection<?> keys = propertySet.getKeys(CONFIG_SERVER_KEY_PREFIX);
		for (Object entry : keys) {
			final String key = (String) entry;
			if (key.endsWith(CONFIG_SERVER_ID_KEY_SUFFIX)) {
				serverIds.add(Integer.parseInt(StringUtils.substringBetween(key, CONFIG_SERVER_KEY_PREFIX,
					CONFIG_SERVER_ID_KEY_SUFFIX)));
			}
		}
		Collections.sort(serverIds);
		for (Integer serverId : serverIds) {
			loadOldServer(serverId);
			removeOldServer(serverId);
		}
		idGenerator.setInitialNextId(serverIds.get(serverIds.size() - 1) + 1);
		int defaultServerId = 0;
		if (propertySet.exists(CONFIG_DEFAULT_SERVER_ID)
			&& serverManager.hasServer(new Integer(propertySet.getInt(CONFIG_DEFAULT_SERVER_ID)))) {
			defaultServerId = propertySet.getInt(CONFIG_DEFAULT_SERVER_ID);
		} else if (serverManager.hasServers()) {
			HudsonServer server = (HudsonServer) serverManager.getServers().toArray()[0];
			defaultServerId = server.getServerId();
		}
		LOGGER.info("Default server configured with Id: " + defaultServerId);
		serverManager.setDefaultServer(defaultServerId);
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
		return "Upgrade the Hudson Server structure of plugin version 3.3.0 to version 4.0.0";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPluginKey() {
		return JiraPluginUtils.getPluginKey();
	}

	/**
	 * Internal method to load the Server in the old property store and add it to the new property store
	 * 
	 * @param serverId the Id of the server to load
	 */
	private void loadOldServer(int serverId) {
		final HudsonServer server = serverFactory.create();
        server.setServerId(propertySet.getInt(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_ID_KEY_SUFFIX));
        server.setName(propertySet.getString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_NAME_KEY_SUFFIX));
        server.setDescription(propertySet.getString(CONFIG_SERVER_KEY_PREFIX + serverId
                + CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX));
        server.setHost(propertySet.getString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_HOST_KEY_SUFFIX));
        server.setUsername(propertySet.getString(CONFIG_SERVER_KEY_PREFIX + serverId
                + CONFIG_SERVER_USERNAME_KEY_SUFFIX));
        final String encryptedPassword =
                propertySet.getString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PASSWORD_KEY_SUFFIX);
        server.setPassword(ENCRYPTER.decrypt(encryptedPassword));
        serverManager.addServer(server);
	}

	/**
	 * Internal method to remove an old Hudson Server from the {@link PropertySet}
	 * 
	 * @param serverId the Server Id to remove
	 */
	private void removeOldServer(int serverId) {
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_ID_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_NAME_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_HOST_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_USERNAME_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PASSWORD_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PROJECTS_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_CRUMB_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_CRUMB_FIELD_KEY_SUFFIX);
	}

}
