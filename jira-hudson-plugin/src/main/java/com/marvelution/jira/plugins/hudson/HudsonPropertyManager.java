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

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;

import java.util.HashMap;

/**
 * The Property Manager for the Hudson Server configuration
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class HudsonPropertyManager {

	private static final long ID = 2;

	private static HudsonPropertyManager propertiesManager;

	private PropertySet propertySet;

	/**
	 * Hidden constructor which loads the property set
	 */
	public HudsonPropertyManager() {
		loadPropertySet();
	}

	/**
	 * Singleton retriever
	 * 
	 * @return the singleton {@link HudsonPropertyManager}
	 */
	public static HudsonPropertyManager getInstance() {
		if (propertiesManager == null) {
			propertiesManager = new HudsonPropertyManager();
		}
		return propertiesManager;
	}

	/**
	 * Only useful for mocking out the PropertiesManager
	 * 
	 * @param propertiesManager the {@link HudsonPropertyManager}
	 */
	public static void setPropertiesManager(HudsonPropertyManager propertiesManager) {
		HudsonPropertyManager.propertiesManager = propertiesManager;
	}

	/**
	 * Get the property set
	 * 
	 * @return the Property Set
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
		psArgs.put("entityName", "BambooServerProperties");
		psArgs.put("entityId", new Long(ID));

		propertySet = PropertySetManager.getInstance("ofbiz", psArgs);
	}
}
