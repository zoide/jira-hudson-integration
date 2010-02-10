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
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.marvelution.jira.plugins.hudson.service.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.service.HudsonPropertyManager;
import com.opensymphony.module.propertyset.PropertySet;

/**
 * Default {@link HudsonConfigurationManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 * 
 * @see 3.3.0
 */
public class DefaultHudsonConfigurationManagerImpl implements HudsonConfigurationManager {

	private static final String CONFIG_SETTING_PREFIX = "hudson.config.settings.";

	private final PropertySet propertySet;

	private final Properties properties = new Properties();

	/**
	 * Constructor
	 * 
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 */
	public DefaultHudsonConfigurationManagerImpl(HudsonPropertyManager propertyManager) {
		propertySet = propertyManager.getPropertySet();
		load();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBooleanProperty(String key) {
		return Boolean.parseBoolean(getProperty(key));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty(String key) {
		return properties.getProperty(key, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProperty(String key, Object value) {
		properties.setProperty(key, String.valueOf(value));
		store(key, String.valueOf(value));
	}

	/**
	 * Load all the properties from the {@link PropertySet}
	 */
	private void load() {
		final Collection<?> keys = propertySet.getKeys(CONFIG_SETTING_PREFIX);
		for (Object element : keys) {
			if (element instanceof String) {
				final String key = (String) element;
				properties.setProperty(StringUtils.substringAfter(key, CONFIG_SETTING_PREFIX), propertySet
					.getString(key));
			}
		}
	}

	/**
	 * Store the property value
	 * 
	 * @param key the property key
	 * @param value the property value
	 */
	private void store(String key, String value) {
		propertySet.setString(CONFIG_SETTING_PREFIX + "." + key, value);
	}

}
