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

import com.marvelution.jira.plugins.hudson.service.HudsonPropertyManager;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;

import java.util.HashMap;

/**
 * Default implementation of the {@link HudsonPropertyManager} interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class DefaultHudsonPropertyManagerImpl implements HudsonPropertyManager {

	private static final long PROPERTIES_ID = 2;

	private static DefaultHudsonPropertyManagerImpl propertiesManager;

	private PropertySet propertySet;

	/**
	 * Hidden constructor which loads the property set
	 */
	public DefaultHudsonPropertyManagerImpl() {
		loadPropertySet();
	}

	/**
	 * Singleton retriever
	 * 
	 * @return the singleton {@link DefaultHudsonPropertyManagerImpl}
	 */
	public static DefaultHudsonPropertyManagerImpl getInstance() {
		if (propertiesManager == null) {
			propertiesManager = new DefaultHudsonPropertyManagerImpl();
		}
		return propertiesManager;
	}

	/**
	 * Only useful for mocking out the PropertiesManager
	 * 
	 * @param propertiesManager the {@link DefaultHudsonPropertyManagerImpl}
	 */
	public static void setPropertiesManager(DefaultHudsonPropertyManagerImpl propertiesManager) {
		DefaultHudsonPropertyManagerImpl.propertiesManager = propertiesManager;
	}

	/**
	 * {@inheritDoc}
	 */
	public PropertySet getPropertySet() {
		return propertySet;
	}

	/**
	 * Refresh the properties from the database
	 */
	public void refresh() {
		loadPropertySet();
	}

	/**
	 * Locate PropertySet using PropertyStore for this sequenceName/sequenceId mapping.
	 */
	@SuppressWarnings("unchecked")
	protected void loadPropertySet() {
		final HashMap psArgs = new HashMap();
		psArgs.put("delegator.name", "default");
		psArgs.put("entityName", "HudsonServerProperties");
		psArgs.put("entityId", new Long(PROPERTIES_ID));
		propertySet = PropertySetManager.getInstance("ofbiz", psArgs);
	}

}
