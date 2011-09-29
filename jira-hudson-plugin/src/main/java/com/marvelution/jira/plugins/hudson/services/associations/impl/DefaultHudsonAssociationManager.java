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

package com.marvelution.jira.plugins.hudson.services.associations.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.atlassian.jira.project.Project;
import com.marvelution.jira.plugins.hudson.services.HudsonPropertyManager;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationFactory;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationIdGenerator;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;

/**
 * The Default {@link HudsonAssociationManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonAssociationManager implements HudsonAssociationManager, InitializingBean {

	private static final String ASSOCIATION_KEY_PREFIX = "hudson.association.";
	private static final String ASSOCIATION_ID_KEY_SUFFIX = ".id";
	private static final String ASSOCIATION_SID_KEY_SUFFIX = ".sid";
	private static final String ASSOCIATION_PID_KEY_SUFFIX = ".pid";
	private static final String ASSOCIATION_JOB_KEY_SUFFIX = ".job";

	private static final Logger LOGGER = Logger.getLogger(DefaultHudsonAssociationManager.class);

	private final HudsonPropertyManager propertyManager;
	private final HudsonAssociationFactory associationFactory;
	private final HudsonAssociationIdGenerator idGenerator;

	private Map<Integer, HudsonAssociation> associations = new HashMap<Integer, HudsonAssociation>();
	private Map<Long, Set<Integer>> pidMapping = new HashMap<Long, Set<Integer>>();

	/**
	 * Constructor
	 * 
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 * @param associationFactory the {@link HudsonAssociationFactory} implementation
	 * @param idGenerator the {@link HudsonAssociationIdGenerator} implementation
	 */
	public DefaultHudsonAssociationManager(HudsonPropertyManager propertyManager,
			HudsonAssociationFactory associationFactory, HudsonAssociationIdGenerator idGenerator) {
		this.propertyManager = propertyManager;
		this.associationFactory = associationFactory;
		this.idGenerator = idGenerator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAssociation(int associationId) {
		return associations.containsKey(associationId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAssocations() {
		return !associations.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAssociations(Project project) {
		return pidMapping.containsKey(project.getId()) && !pidMapping.get(project.getId()).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonAssociation getAssociation(int associationId) {
		return associations.get(associationId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<HudsonAssociation> getAssociations() {
		return Collections.unmodifiableCollection(associations.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<HudsonAssociation> getAssociations(Project project) {
		Collection<HudsonAssociation> temp = new HashSet<HudsonAssociation>();
		for (Integer associationId : pidMapping.get(project.getId())) {
			temp.add(getAssociation(associationId));
		}
		return Collections.unmodifiableCollection(temp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAssociation(HudsonAssociation association) {
		if (association == null) {
			throw new IllegalArgumentException("association argument may NOT be null");
		} else if (association.getServerId() == 0 || association.getProjectId() == 0L
				|| StringUtils.isBlank(association.getJobName())) {
			throw new IllegalStateException("A Hudson Association requires a Hudson Server Id, JIRA Project Id and "
					+ "a Hudson Job name");
		}
		if (association.getAssociationId() == 0) {
			association.setAssociationId(idGenerator.next());
		}
		store(association);
		add(association);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAssociation(int associationId) {
		if (!hasAssociation(associationId)) {
			throw new IllegalArgumentException("No HudsonAssociation configured with Id: " + associationId);
		}
		removeAssociation(getAssociation(associationId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAssociation(HudsonAssociation association) {
		if (association == null || association.getAssociationId() == 0) {
			return;
		}
		remove(association.getAssociationId());
		associations.remove(association.getAssociationId());
		for (Entry<Long, Set<Integer>> entry : pidMapping.entrySet()) {
			if (entry.getValue().contains(association.getAssociationId())) {
				entry.getValue().remove(association.getAssociationId());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<String> keys = propertyManager.getPropertySet().getKeys(ASSOCIATION_KEY_PREFIX);
		for (String key : keys) {
			if (key.endsWith(ASSOCIATION_ID_KEY_SUFFIX)) {
				load(propertyManager.getPropertySet().getInt(key));
			}
		}
	}

	/**
	 * Internal method to load a {@link HudsonAssociation}
	 * 
	 * @param associationId the Id of teh {@link HudsonAssociation} to laod
	 */
	private void load(int associationId) {
		HudsonAssociation association = associationFactory.create();
		association.setAssociationId(propertyManager.getPropertySet().getInt(getAssociationIdKey(associationId)));
		association.setServerId(propertyManager.getPropertySet().getInt(getAssociationServerIdKey(associationId)));
		association.setProjectId(propertyManager.getPropertySet().getLong(getAssociationProjectIdKey(associationId)));
		association.setJobName(propertyManager.getPropertySet().getString(getAssociationJobNameKey(associationId)));
		LOGGER.debug("Loaded HudsonAssocation: " + association);
		add(association);
	}

	/**
	 * Internal method to add a {@link HudsonAssociation} to the internal cache
	 * 
	 * @param association the {@link HudsonAssociation} to add
	 */
	private void add(HudsonAssociation association) {
		associations.put(association.getAssociationId(), association);
		if (!pidMapping.containsKey(association.getProjectId())) {
			pidMapping.put(association.getProjectId(), new HashSet<Integer>());
		}
		pidMapping.get(association.getProjectId()).add(association.getAssociationId());
		LOGGER.info("Added HudsonAssociation: " + association);
	}

	/**
	 * Internal method to store a {@link HudsonAssociation}
	 * 
	 * @param association the {@link HudsonAssociation} to store
	 */
	private void store(HudsonAssociation association) {
		int associationId = association.getAssociationId();
		propertyManager.getPropertySet().setInt(getAssociationIdKey(associationId), association.getAssociationId());
		propertyManager.getPropertySet().setInt(getAssociationServerIdKey(associationId), association.getServerId());
		propertyManager.getPropertySet().setLong(getAssociationProjectIdKey(associationId),
				association.getProjectId());
		propertyManager.getPropertySet().setString(getAssociationJobNameKey(associationId), association.getJobName());
		LOGGER.debug("Stored HudsonAssociation: " + association);
	}

	/**
	 * Internal method to remove a {@link HudsonAssociation}
	 * 
	 * @param associationId the {@link HudsonAssociation} to remove
	 */
	private void remove(int associationId) {
		propertyManager.getPropertySet().remove(getAssociationIdKey(associationId));
		propertyManager.getPropertySet().remove(getAssociationServerIdKey(associationId));
		propertyManager.getPropertySet().remove(getAssociationProjectIdKey(associationId));
		propertyManager.getPropertySet().remove(getAssociationJobNameKey(associationId));
		LOGGER.debug("Deleted HudsonAssociation with Id: " + associationId);
	}

	/**
	 * Static method to get the property key for the Association Id field
	 * 
	 * @param associationId the Association Id to get the field for
	 * @return the Property key
	 */
	public static String getAssociationIdKey(int associationId) {
		return ASSOCIATION_KEY_PREFIX + associationId + ASSOCIATION_ID_KEY_SUFFIX;
	}

	/**
	 * Static method to get the property key for the Association Server Id field
	 * 
	 * @param associationId the Association Id to get the field for
	 * @return the Property key
	 */
	public static String getAssociationServerIdKey(int associationId) {
		return ASSOCIATION_KEY_PREFIX + associationId + ASSOCIATION_SID_KEY_SUFFIX;
	}

	/**
	 * Static method to get the property key for the Association Project Id field
	 * 
	 * @param associationId the Association Id to get the field for
	 * @return the Property key
	 */
	public static String getAssociationProjectIdKey(int associationId) {
		return ASSOCIATION_KEY_PREFIX + associationId + ASSOCIATION_PID_KEY_SUFFIX;
	}

	/**
	 * Static method to get the property key for the Job Name field
	 * 
	 * @param associationId the Association Id to get the field for
	 * @return the Property key
	 */
	public static String getAssociationJobNameKey(int associationId) {
		return ASSOCIATION_KEY_PREFIX + associationId + ASSOCIATION_JOB_KEY_SUFFIX;
	}

}
