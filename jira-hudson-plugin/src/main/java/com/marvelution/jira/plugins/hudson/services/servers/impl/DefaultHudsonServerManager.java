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

package com.marvelution.jira.plugins.hudson.services.servers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.marvelution.jira.plugins.hudson.encryption.StringEncrypter;
import com.marvelution.jira.plugins.hudson.services.HudsonPropertyManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerIdGenerator;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.opensymphony.module.propertyset.PropertySet;

/**
 * Default {@link HudsonServerManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerManager implements HudsonServerManager, InitializingBean {

	private static final String SERVER_DEFAULT_ID_KEY = "hudson.servers.defaultId";
	private static final String SERVER_KEY_PREFIX = "hudson.server.";
	private static final String SERVER_ID_KEY_SUFFIX = ".id";
	private static final String SERVER_NAME_KEY_SUFFIX = ".name";
	private static final String SERVER_DESCRIPTION_KEY_SUFFIX = ".description";
	private static final String SERVER_HOST_KEY_SUFFIX = ".host";
	private static final String SERVER_PUBLIC_HOST_KEY_SUFFIX = ".publicHost";
	private static final String SERVER_USERNAME_KEY_SUFFIX = ".username";
	private static final String SERVER_PASSWORD_KEY_SUFFIX = ".password";

	private static final Logger LOGGER = Logger.getLogger(DefaultHudsonServerManager.class);
	private static final StringEncrypter ENCRYPTER = new StringEncrypter();

	private final HudsonPropertyManager propertyManager;
	private final HudsonServerFactory serverFactory;
	private final HudsonServerIdGenerator idGenerator;

	private int defaultServerId = 0;
	private Map<Integer, HudsonServer> servers = new HashMap<Integer, HudsonServer>();
	private Map<String, Integer> nameMapping = new HashMap<String, Integer>();

	/**
	 * Constructor
	 * 
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 * @param serverFactory the {@link HudsonServerFactory} implementation
	 * @param idGenerator the {@link HudsonServerIdGenerator} implementation
	 */
	public DefaultHudsonServerManager(HudsonPropertyManager propertyManager, HudsonServerFactory serverFactory,
			HudsonServerIdGenerator idGenerator) {
		this.propertyManager = propertyManager;
		this.serverFactory = serverFactory;
		this.idGenerator = idGenerator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConfigured() {
		return hasServers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasServers() {
		return !servers.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasServer(int serverId) {
		return servers.containsKey(serverId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasServer(String name) {
		if (nameMapping.containsKey(name)) {
			return hasServer(nameMapping.get(name));
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<HudsonServer> getServers() {
		return Collections.unmodifiableCollection(servers.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer getServer(int serverId) {
		return servers.get(serverId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer getServer(String name) {
		if (nameMapping.containsKey(name)) {
			return getServer(nameMapping.get(name));
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer getDefaultServer() {
		return getServer(defaultServerId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultServer(int serverId) {
		setDefaultServer(servers.get(serverId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultServer(HudsonServer server) {
		if (server != null && !isDefaultServer(server)) {
			defaultServerId = server.getServerId();
			LOGGER.debug("Setting HudsonServer '" + server + "' as DEFAULT");
			propertyManager.getPropertySet().setInt(SERVER_DEFAULT_ID_KEY, defaultServerId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDefaultServer(HudsonServer server) {
		if (server == null) {
			return false;
		}
		return server.getServerId() == defaultServerId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addServer(HudsonServer server) {
		if (server == null) {
			throw new IllegalArgumentException("server argument may NOT be null");
		}
		if (server.getServerId() == 0) {
			server.setServerId(idGenerator.next());
		}
		store(server);
		add(server);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeServer(int serverId) {
		if (!hasServer(serverId)) {
			throw new IllegalArgumentException("No HudsonServer configured with Id: " + serverId);
		}
		removeServer(servers.get(serverId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeServer(HudsonServer server) {
		if (server == null || server.getServerId() == 0) {
			return;
		}
		remove(server.getServerId());
		servers.remove(server.getServerId());
		nameMapping.remove(server.getName());
		if (isDefaultServer(server)) {
			LOGGER.info("Deleted the Default HudsonServer, making sure another will be set as default");
			if (hasServers()) {
				setDefaultServer(servers.keySet().toArray(new Integer[servers.size()])[0]);
			} else {
				defaultServerId = 0;
				propertyManager.getPropertySet().remove(SERVER_DEFAULT_ID_KEY);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<String> keys = propertyManager.getPropertySet().getKeys(SERVER_KEY_PREFIX);
		for (String key : keys) {
			if (key.endsWith(SERVER_ID_KEY_SUFFIX)) {
				load(propertyManager.getPropertySet().getInt(key));
			}
		}
		if (propertyManager.getPropertySet().exists(SERVER_DEFAULT_ID_KEY)) {
			setDefaultServer(propertyManager.getPropertySet().getInt(SERVER_DEFAULT_ID_KEY));
		} else if (hasServers()) {
			setDefaultServer(servers.keySet().toArray(new Integer[servers.size()])[0]);
		}
	}

	/**
	 * Internal method to add a given {@link HudsonServer} to the internal cache and mappings
	 * 
	 * @param server the {@link HudsonServer} to add
	 */
	private void add(HudsonServer server) {
		servers.put(server.getServerId(), server);
		nameMapping.put(server.getName(), server.getServerId());
		LOGGER.info("Added HudsonServer:" + server);
		if (servers.size() == 1) {
			// We just added the first server make sure it is default.
			setDefaultServer(server);
		}
	}

	/**
	 * Internal method to load a {@link HudsonServer} form the store
	 * 
	 * @param serverId the Id of the {@link HudsonServer} to load
	 */
	private void load(int serverId) {
		HudsonServer server = serverFactory.create();
		server.setServerId(propertyManager.getPropertySet().getInt(getServerIdKey(serverId)));
		server.setName(propertyManager.getPropertySet().getString(getServerNameKey(serverId)));
		server.setDescription(propertyManager.getPropertySet().getString(getServerDescriptionKey(serverId)));
		server.setHost(propertyManager.getPropertySet().getString(getServerHostKey(serverId)));
		server.setPublicHost(propertyManager.getPropertySet().getString(getServerPublicHostKey(serverId)));
		if (propertyManager.getPropertySet().exists(getServerUsernameKey(serverId))) {
			server.setUsername(propertyManager.getPropertySet().getString(getServerUsernameKey(serverId)));
			String encryptedPassword = propertyManager.getPropertySet().getString(getServerPasswordKey(serverId));
			server.setPassword(ENCRYPTER.decrypt(encryptedPassword));
		}
		LOGGER.debug("Loaded HudsonServer: " + server);
		add(server);
	}

	/**
	 * Internal method to store a {@link HudsonServer}
	 * 
	 * @param server the {@link HudsonServer} to store
	 */
	private void store(HudsonServer server) {
		int serverId = server.getServerId();
		propertyManager.getPropertySet().setInt(getServerIdKey(serverId), serverId);
		propertyManager.getPropertySet().setString(getServerNameKey(serverId), server.getName());
		propertyManager.getPropertySet().setString(getServerDescriptionKey(serverId), server.getDescription());
		propertyManager.getPropertySet().setString(getServerHostKey(serverId), server.getHost());
		propertyManager.getPropertySet().setString(getServerPublicHostKey(serverId), server.getPublicHost());
		if (server.isSecured()) {
			propertyManager.getPropertySet().setString(getServerUsernameKey(serverId), server.getUsername());
			String encryptedPassword = ENCRYPTER.encrypt(server.getPassword());
			propertyManager.getPropertySet().setString(getServerPasswordKey(serverId), encryptedPassword);
		}
		LOGGER.debug("Stored HudsonServer: " + server);
	}

	/**
	 * Internal method to delete the {@link HudsonServer} form the {@link PropertySet}
	 * 
	 * @param serverId the server Id
	 */
	private void remove(int serverId) {
		propertyManager.getPropertySet().remove(getServerIdKey(serverId));
		propertyManager.getPropertySet().remove(getServerNameKey(serverId));
		propertyManager.getPropertySet().remove(getServerDescriptionKey(serverId));
		propertyManager.getPropertySet().remove(getServerHostKey(serverId));
		propertyManager.getPropertySet().remove(getServerPublicHostKey(serverId));
		if (propertyManager.getPropertySet().exists(getServerUsernameKey(serverId))) {
			propertyManager.getPropertySet().remove(getServerUsernameKey(serverId));
			propertyManager.getPropertySet().remove(getServerPasswordKey(serverId));
		}
		LOGGER.debug("Deleted HudsonServer with Id: " + serverId);
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Id field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	public static String getServerIdKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_ID_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Name field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	public static String getServerNameKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_NAME_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Description field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	public static String getServerDescriptionKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_DESCRIPTION_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Host field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	public static String getServerHostKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_HOST_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Public Host field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	public static String getServerPublicHostKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_PUBLIC_HOST_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Username field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	public static String getServerUsernameKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_USERNAME_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Password field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	public static String getServerPasswordKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_PASSWORD_KEY_SUFFIX;
	}

}
