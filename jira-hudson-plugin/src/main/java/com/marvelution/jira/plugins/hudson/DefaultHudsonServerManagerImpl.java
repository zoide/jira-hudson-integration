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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.marvelution.jira.plugins.hudson.encryption.StringEncrypter;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerFactory;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.service.HudsonPropertyManager;
import com.opensymphony.module.propertyset.PropertySet;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Default {@link HudsonServerManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerManagerImpl implements HudsonServerManager {

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

	static final String CONFIG_PROJECT_KEY_SEPARATORS = " ,;:";

	private static final Logger LOGGER = Logger.getLogger(DefaultHudsonServerManagerImpl.class);

	private static final StringEncrypter ENCRYPTER = new StringEncrypter();

	private final PropertySet propertySet;

	private final HudsonServerFactory serverFactory;

	private final ProjectManager projectManager;

	private final Map<Integer, HudsonServer> servers = new HashMap<Integer, HudsonServer>();

	private final Map<String, Integer> nameMapping = new HashMap<String, Integer>();

	private final Map<String, Integer> projectMapping = new HashMap<String, Integer>();

	private int defaultServerId;

	/**
	 * Constructor
	 * 
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 * @param serverFactory the {@link HudsonServerFactory} implementation
	 * @param projectManager the {@link ProjectManager} implementation
	 */
	public DefaultHudsonServerManagerImpl(HudsonPropertyManager propertyManager,
											HudsonServerFactory serverFactory, ProjectManager projectManager) {
		propertySet = propertyManager.getPropertySet();
		this.serverFactory = serverFactory;
		this.projectManager = projectManager;
		final Collection<Integer> serverIds = new HashSet<Integer>();
		final Collection<?> keys = propertySet.getKeys(CONFIG_SERVER_KEY_PREFIX);
		for (Object entry : keys) {
			final String key = (String) entry;
			if (key.endsWith(CONFIG_SERVER_ID_KEY_SUFFIX)) {
				serverIds.add(Integer.parseInt(StringUtils.substringBetween(key, CONFIG_SERVER_KEY_PREFIX,
					CONFIG_SERVER_ID_KEY_SUFFIX)));
			}
		}
		for (Integer serverId : serverIds) {
			load(serverId);
		}
		if (propertySet.exists(CONFIG_DEFAULT_SERVER_ID)
			&& servers.containsKey(new Integer(propertySet.getInt(CONFIG_DEFAULT_SERVER_ID)))) {
			defaultServerId = propertySet.getInt(CONFIG_DEFAULT_SERVER_ID);
			LOGGER.debug("Default server configure with Id: " + defaultServerId);
		} else if (!servers.isEmpty()) {
			LOGGER.debug("No default server configure making the first one in the list default");
			setDefaultServer((HudsonServer) servers.values().toArray()[0]);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isHudsonConfigured() {
		return defaultServerId > 0 && servers.containsKey(defaultServerId);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasServers() {
		return !servers.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasServer(String serverName) {
		return (!StringUtils.isEmpty(serverName) && nameMapping.containsKey(serverName) && servers
			.containsKey(nameMapping.get(serverName)));
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<HudsonServer> getServers() {
		return Collections.unmodifiableCollection(servers.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public HudsonServer getServer(int serverId) {
		if (serverId <= 0) {
			return null;
		}
		return servers.get(new Integer(serverId));
	}

	/**
	 * {@inheritDoc}
	 */
	public HudsonServer getServer(String serverName) {
		if (StringUtils.isEmpty(serverName) || !nameMapping.containsKey(serverName)) {
			return null;
		}
		return getServer(nameMapping.get(serverName));
	}

	/**
	 * {@inheritDoc}
	 */
	public HudsonServer getServerByJiraProject(Project project) {
		if (project != null && projectMapping.containsKey(project.getKey())) {
			return getServer(projectMapping.get(project.getKey()));
		}
		return getDefaultServer();
	}

	/**
	 * {@inheritDoc}
	 */
	public HudsonServer getDefaultServer() {
		if (defaultServerId == 0) {
			return null;
		}
		return getServer(defaultServerId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultServer(HudsonServer hudsonServer) {
		if (hudsonServer == null) {
			throw new IllegalArgumentException("hudsonServer may not be null");
		}
		if (!isDefaultServer(hudsonServer)) {
			LOGGER.debug("Setting hudson server [" + hudsonServer.getName() + "] as DEFAULT");
			defaultServerId = hudsonServer.getServerId();
			propertySet.setInt(CONFIG_DEFAULT_SERVER_ID, defaultServerId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDefaultServer(HudsonServer hudsonServer) {
		return hudsonServer.getServerId() == defaultServerId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void put(HudsonServer hudsonServer) {
		add(hudsonServer);
		store(hudsonServer);
	}

	/**
	 * Add a {@link HudsonServer} to the list of configured servers
	 * 
	 * @param hudsonServer the {@link HudsonServer}
	 */
	private void add(HudsonServer hudsonServer) {
		if (hudsonServer == null) {
			throw new IllegalArgumentException("hudsonServer may not be null");
		}
		if (hudsonServer.getServerId() == 0) {
			hudsonServer.setServerId(generateServerId());
		}
		for (final Iterator<String> iter = hudsonServer.getAssociatedProjectKeys().iterator(); iter.hasNext();) {
			final String projectKey = iter.next();
			final Project project = projectManager.getProjectObjByKey(projectKey);
			if (project != null) {
				final HudsonServer conflictingServer = getServerByJiraProject(project);
				if (conflictingServer != null && conflictingServer.getServerId() != hudsonServer.getServerId()) {
					LOGGER.warn("Found conflicting Hudson Server for project " + project.getName() + " ["
						+ project.getKey() + "]. Removing project association from the conflicting Hudson Server.");
					conflictingServer.removeAssociatedProjectKey(projectKey);
					store(conflictingServer);
					projectMapping.remove(projectKey);
				}
				LOGGER.debug("Mapped project " + project.getName() + " [" + projectKey + "] to Hudson Server: "
					+ hudsonServer.getName());
				projectMapping.put(projectKey, new Integer(hudsonServer.getServerId()));
			} else {
				LOGGER.warn("Project key " + projectKey
					+ " is not valid. Removing it from the project association list.");
				iter.remove();
			}
		}
		servers.put(new Integer(hudsonServer.getServerId()), hudsonServer);
		nameMapping.put(hudsonServer.getName(), hudsonServer.getServerId());
		if (defaultServerId == 0) {
			setDefaultServer(hudsonServer);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(int serverId) {
		if (serverId <= 0) {
			throw new IllegalArgumentException("serverId may not be less then or equal to 0 (zero)");
		}
		if (!servers.containsKey(new Integer(serverId))) {
			throw new IllegalStateException("No Hudson Server configured with Id: " + serverId);
		}
		final HudsonServer server = getServer(serverId);
		nameMapping.remove(server.getName());
		servers.remove(new Integer(serverId));
		remove(server);
		if (isDefaultServer(server)) {
			if (servers.isEmpty()) {
				defaultServerId = 0;
				propertySet.remove(CONFIG_DEFAULT_SERVER_ID);
			} else {
				setDefaultServer((HudsonServer) servers.values().toArray()[0]);
			}
		}
	}

	/**
	 * Load a {@link HudsonServer} from the datastore
	 * 
	 * @param serverId the serverId to load
	 */
	@SuppressWarnings("unchecked")
	private void load(int serverId) {
		final HudsonServer server = serverFactory.createHudsonServer();
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
		final String projects =
			propertySet.getString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PROJECTS_KEY_SUFFIX);
		server.setAssociatedProjectKeys(Arrays.asList(projects.split(CONFIG_PROJECT_KEY_SEPARATORS)));
		add(server);
	}

	/**
	 * Store a {@link HudsonServer} in the datastore
	 * 
	 * @param hudsonServer the {@link HudsonServer} to store
	 */
	private void store(HudsonServer hudsonServer) {
		final int serverId = hudsonServer.getServerId();
		propertySet.setInt(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_ID_KEY_SUFFIX, hudsonServer
			.getServerId());
		propertySet.setString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_NAME_KEY_SUFFIX, hudsonServer
			.getName());
		propertySet.setString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX,
			hudsonServer.getDescription());
		propertySet.setString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_HOST_KEY_SUFFIX, hudsonServer
			.getHost());
		propertySet.setString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_USERNAME_KEY_SUFFIX, hudsonServer
			.getUsername());
		final String encryptedPassword = ENCRYPTER.encrypt(hudsonServer.getPassword());
		propertySet.setString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PASSWORD_KEY_SUFFIX,
			encryptedPassword);
		final String projects = StringUtils.join(hudsonServer.getAssociatedProjectKeys(), ',');
		propertySet.setString(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PROJECTS_KEY_SUFFIX, projects);
	}

	/**
	 * Remove a {@link HudsonServer} from the datastore
	 * 
	 * @param hudsonServer the {@link HudsonServer} to remove
	 */
	private void remove(HudsonServer hudsonServer) {
		final int serverId = hudsonServer.getServerId();
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_ID_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_NAME_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_HOST_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_USERNAME_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PASSWORD_KEY_SUFFIX);
		propertySet.remove(CONFIG_SERVER_KEY_PREFIX + serverId + CONFIG_SERVER_PROJECTS_KEY_SUFFIX);
	}

	/**
	 * Generate the next {@link HudsonServer} Id
	 * 
	 * @return the Id for the next {@link HudsonServer}
	 */
	private int generateServerId() {
		int nextId = 1;
		if (propertySet.exists(CONFIG_NEXT_SERVER_ID)) {
			nextId = propertySet.getInt(CONFIG_NEXT_SERVER_ID);
		}
		propertySet.setInt(CONFIG_NEXT_SERVER_ID, nextId + 1);
		return nextId;
	}

}
