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

package com.marvelution.jira.plugins.hudson.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.marvelution.jira.plugins.hudson.services.HudsonPropertyManager;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;

/**
 * Default {@link HudsonPropertyManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonPropertyManager implements HudsonPropertyManager {

	private static final long PROPERTY_ID = 2L;
	private static DefaultHudsonPropertyManager instance;
	private PropertySet propertySet;

	/**
	 * Private constructor to force the use of the {@link #getInstance()} method
	 */
	private DefaultHudsonPropertyManager() {
		loadPropertySet();
	}

	/**
	 * Getter for the static instance of the {@link HudsonPropertyManager}
	 * 
	 * @return the {@link HudsonPropertyManager} implementation
	 */
	public static HudsonPropertyManager getInstance() {
		if (instance == null) {
			instance = new DefaultHudsonPropertyManager();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropertySet getPropertySet() {
		return propertySet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh() {
		loadPropertySet();
	}

	/**
	 * Internal method to load the {@link PropertySet}
	 */
	private void loadPropertySet() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("delegator.name", "default");
	    arguments.put("entityName", "HudsonServerProperties");
	    arguments.put("entityId", PROPERTY_ID);
	    propertySet = PropertySetManager.getInstance("ofbiz", arguments);
	}

}
