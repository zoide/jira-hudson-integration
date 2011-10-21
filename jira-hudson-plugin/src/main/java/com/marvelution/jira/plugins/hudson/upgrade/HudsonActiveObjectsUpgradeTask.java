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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.portal.PortletConfigurationStore;
import com.atlassian.jira.util.Consumer;
import com.atlassian.jira.util.collect.EnclosedIterable;
import com.atlassian.sal.api.message.Message;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.configuration.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonActiveObjectsUpgradeTask implements PluginUpgradeTask {

	/**
	 * Old plugin server PropertySet keys
	 */
	private static final String SERVER_DEFAULT_ID_KEY = "hudson.servers.defaultId";
	private static final String SERVER_KEY_PREFIX = "hudson.server.";
	private static final String SERVER_ID_KEY_SUFFIX = ".id";
	private static final String SERVER_NAME_KEY_SUFFIX = ".name";
	private static final String SERVER_DESCRIPTION_KEY_SUFFIX = ".description";
	private static final String SERVER_HOST_KEY_SUFFIX = ".host";
	private static final String SERVER_PUBLIC_HOST_KEY_SUFFIX = ".publicHost";
	private static final String SERVER_USERNAME_KEY_SUFFIX = ".username";
	private static final String SERVER_PASSWORD_KEY_SUFFIX = ".password";

	/**
	 * Old plugin association PropertySet keys
	 */
	private static final String ASSOCIATION_KEY_PREFIX = "hudson.association.";
	private static final String ASSOCIATION_ID_KEY_SUFFIX = ".id";
	private static final String ASSOCIATION_SID_KEY_SUFFIX = ".sid";
	private static final String ASSOCIATION_PID_KEY_SUFFIX = ".pid";
	private static final String ASSOCIATION_JOB_KEY_SUFFIX = ".job";

	private final Logger logger = Logger.getLogger(HudsonActiveObjectsUpgradeTask.class);
	private final HudsonServerManager serverManager;
	private final HudsonAssociationManager associationManager;
	private final PortletConfigurationStore portletConfigurationStore;

	private static final long PROPERTY_ID = 2L;
	private PropertySet propertySet;
	private Map<Integer, Integer> migratedServerIdMapping = Maps.newHashMap();
	private Map<Integer, Integer> migratedAssociationIdMapping = Maps.newHashMap();

	/**
	 * Constructor
	 *
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param configurationManager the {@link HudsonConfigurationManager} implementation
	 */
	public HudsonActiveObjectsUpgradeTask(HudsonServerManager serverManager,
					HudsonAssociationManager associationManager) {
		this.serverManager = Preconditions.checkNotNull(serverManager, "serverManager");
		this.associationManager = Preconditions.checkNotNull(associationManager, "associationManager");
		this.portletConfigurationStore = getPortletConfigurationStore();
		this.propertySet = loadPropertySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBuildNumber() {
		return 6;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getShortDescription() {
		return "Upgrade the PropertySet storage to the ActiveObjects database";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPluginKey() {
		return JiraPluginUtils.getPluginKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Message> doUpgrade() throws Exception {
		final int defaultServerId = propertySet.getInt(SERVER_DEFAULT_ID_KEY);
		final Collection<String> serverKeys = propertySet.getKeys(SERVER_KEY_PREFIX);
		logger.info("Upgrading all preconfigured Hudson Servers to the new active-objects databse");
		for (String key : serverKeys) {
			if (key.endsWith(SERVER_ID_KEY_SUFFIX)) {
				int serverId = propertySet.getInt(key);
				HudsonServer server = migrateServer(serverId, defaultServerId == serverId);
				logger.info("Upgraded Hudson Server '" + server.getName() + "' from old ID " + serverId + " new ID "
					+ server.getID());
				migratedServerIdMapping.put(serverId, server.getID());
			}
		}
		if (propertySet.exists(SERVER_DEFAULT_ID_KEY)) {
			logger.info("Removing server PropertySet key: " + SERVER_DEFAULT_ID_KEY);
			propertySet.remove(SERVER_DEFAULT_ID_KEY);
		}
		for (String key : serverKeys) {
			if (propertySet.exists(key)) {
				logger.info("Removing server PropertySet key: " + key);
				propertySet.remove(key);
			}
		}
		logger.info("Upgrading all preconfigured Hudson Associations to the new active-objects databse");
		final Collection<String> associationKeys = propertySet.getKeys(ASSOCIATION_KEY_PREFIX);
		for (String key : associationKeys) {
			if (key.endsWith(ASSOCIATION_ID_KEY_SUFFIX)) {
				int associationId = propertySet.getInt(key);
				HudsonAssociation association = migrateAssociation(associationId);
				logger.info("Upgraded Hudson Association '" + association.getJobName() + " on "
					+ association.getServer().getName() + "' from old ID " + associationId + " new ID "
					+ association.getID());
				migratedAssociationIdMapping.put(associationId, association.getID());
			}
		}
		for (String key : associationKeys) {
			if (propertySet.exists(key)) {
				logger.info("Removing association PropertySet key: " + key);
				propertySet.remove(key);
			}
		}
		logger.info("Upgrading association UserPrefs of all configured gadgets");
		final EnclosedIterable<PortletConfiguration> iterable =
			portletConfigurationStore.getAllPortletConfigurations();
		iterable.foreach(new Consumer<PortletConfiguration>() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void consume(PortletConfiguration configuration) {
				if (StringUtils.isNotBlank(configuration.getKey())
					&& configuration.getKey().startsWith(JiraPluginUtils.getPluginKey())) {
					logger.info("Checking Gadget configuration with key " + configuration.getKey() + " and id "
						+ configuration.getId());
					if (configuration.getUserPrefs() != null
						&& configuration.getUserPrefs().containsKey("assocation")) {
						logger.debug("Gadget has the 'association' field that requires an update");
						try {
							int oldAssociationId = Integer.parseInt(configuration.getUserPrefs().get("association"));
							if (migratedAssociationIdMapping.containsKey(oldAssociationId)) {
								Map<String, String> userPrefs = Maps.newHashMap(configuration.getUserPrefs());
								userPrefs.put("association",
									String.valueOf(migratedAssociationIdMapping.get(oldAssociationId)));
								logger.info("Updated association Id " + oldAssociationId + " to the new value "
									+ migratedAssociationIdMapping.get(oldAssociationId));
								portletConfigurationStore.updateUserPrefs(configuration.getId(), userPrefs);
							} else {
								logger.warn("Found unknown assocaition ID. The User will have to check it manually.");
							}
						} catch(NumberFormatException e) {
							logger.error("Failed to update the Assocation ID. The User will have to do it manually.",
								e);
						}
					}
				}
			}

		});
		return null;
	}

	/**
	 * Internal method to load the {@link PropertySet}
	 */
	protected PropertySet loadPropertySet() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("delegator.name", "default");
	    arguments.put("entityName", "HudsonServerProperties");
	    arguments.put("entityId", PROPERTY_ID);
	    return PropertySetManager.getInstance("ofbiz", arguments);
	}

	/**
	 * Get the {@link PortletConfigurationStore}
	 * 
	 * @return the {@link PortletConfigurationStore}
	 */
	protected PortletConfigurationStore getPortletConfigurationStore() {
		return ComponentManager.getComponentInstanceOfType(PortletConfigurationStore.class);
	}

	/**
	 * Internal method to migrate a {@link HudsonServer} from the {@link PropertySet} to the ActiveObjects database
	 * 
	 * @param serverId the id of the server to migrate
	 * @param isDefault flag if the server is default
	 * @return the new {@link HudsonServer}
	 */
	private HudsonServer migrateServer(int serverId, boolean isDefault) {
		String name = propertySet.getString(getServerNameKey(serverId));
		String description = propertySet.getString(getServerDescriptionKey(serverId));
		String host = propertySet.getString(getServerHostKey(serverId));
		String publicHost = propertySet.getString(getServerPublicHostKey(serverId));
		String username = null, password = null;
		if (propertySet.exists(getServerUsernameKey(serverId))) {
			username = propertySet.getString(getServerUsernameKey(serverId));
			password = propertySet.getString(getServerPasswordKey(serverId));
		}
		return serverManager.addServer(name, description, host, publicHost, username, password, true, isDefault);
	}

	/**
	 * Internal method to migrate a {@link HudsonAssociation} from the {@link PropertySet} to the ActiveObjects database
	 * 
	 * @param associationId the Id of the {@link HudsonAssociation} to migrate
	 * @return the migrated {@link HudsonAssociation}
	 */
	private HudsonAssociation migrateAssociation(int associationId) {
		long projectId = propertySet.getLong(getAssociationProjectIdKey(associationId));
		int oldServerId= propertySet.getInt(getAssociationServerIdKey(associationId));
		String jobname = propertySet.getString(getAssociationJobNameKey(associationId));
		int newServerId = migratedServerIdMapping.get(oldServerId);
		return associationManager.addAssociation(newServerId, projectId, jobname);
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Name field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	private String getServerNameKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_NAME_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Description field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	private String getServerDescriptionKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_DESCRIPTION_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Host field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	private String getServerHostKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_HOST_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Public Host field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	private String getServerPublicHostKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_PUBLIC_HOST_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Username field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	private String getServerUsernameKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_USERNAME_KEY_SUFFIX;
	}

	/**
	 * Get the {@link PropertySet} key used for the HudsonServer Password field
	 * 
	 * @param serverId the server id
	 * @return the {@link PropertySet} key
	 */
	private String getServerPasswordKey(int serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_PASSWORD_KEY_SUFFIX;
	}

	/**
	 * Static method to get the property key for the Association Server Id field
	 * 
	 * @param associationId the Association Id to get the field for
	 * @return the Property key
	 */
	private String getAssociationServerIdKey(int associationId) {
		return ASSOCIATION_KEY_PREFIX + associationId + ASSOCIATION_SID_KEY_SUFFIX;
	}

	/**
	 * Static method to get the property key for the Association Project Id field
	 * 
	 * @param associationId the Association Id to get the field for
	 * @return the Property key
	 */
	private String getAssociationProjectIdKey(int associationId) {
		return ASSOCIATION_KEY_PREFIX + associationId + ASSOCIATION_PID_KEY_SUFFIX;
	}

	/**
	 * Static method to get the property key for the Job Name field
	 * 
	 * @param associationId the Association Id to get the field for
	 * @return the Property key
	 */
	private String getAssociationJobNameKey(int associationId) {
		return ASSOCIATION_KEY_PREFIX + associationId + ASSOCIATION_JOB_KEY_SUFFIX;
	}

}
