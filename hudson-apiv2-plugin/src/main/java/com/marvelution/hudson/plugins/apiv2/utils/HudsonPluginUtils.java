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

package com.marvelution.hudson.plugins.apiv2.utils;

import hudson.PluginWrapper;
import hudson.model.Hudson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.marvelution.hudson.plugins.apiv2.PluginImpl;

/**
 * Helper class to get Plugin properties
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class HudsonPluginUtils {

	private static final HudsonPluginUtils INSTANCE = new HudsonPluginUtils();

	private final Properties PROPERTIES = new Properties();

	private final PluginWrapper PLUGIN_WRAPPER;

	/**
	 * Private constructor
	 */
	private HudsonPluginUtils() {
		final InputStream input = getClass().getResourceAsStream("HudsonPluginUtils.properties");
		try {
			PROPERTIES.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Hudson Plugin properties", e);
		}
		PLUGIN_WRAPPER = Hudson.getInstance().getPluginManager().getPlugin(PluginImpl.class);
	}

	/**
	 * Getter for the plugin groupId property
	 * 
	 * @return the plugin groupId property value
	 */
	public static String getPluginGroupId() {
		return INSTANCE.getProperty("plugin.groupId");
	}

	/**
	 * Getter for the plugin arifactId property
	 * 
	 * @return the plugin arifactId property value
	 */
	public static String getPluginArifactId() {
		return INSTANCE.getProperty("plugin.artifactId");
	}

	/**
	 * Getter for the plugin version property
	 * 
	 * @return the plugin version property value
	 */
	public static String getPluginVersion() {
		return INSTANCE.getProperty("plugin.version");
	}

	/**
	 * Get if the TestNG plugin is installed on this Hudson/Jenkins platform
	 * 
	 * @return <code>true</code> if installed, <code>false</code> otherwise
	 * 
	 * @since 4.0.2
	 */
	public static boolean hasTestNGPlugin() {
		return Hudson.getInstance().getPluginManager().getPlugin("testng-plugin") != null;
	}

	/**
	 * Get the Plugin {@link ClassLoader}
	 * 
	 * @return the Plugin {@link ClassLoader}
	 * @since 4.1.0
	 */
	public static ClassLoader getPluginClassloader() {
		return INSTANCE.PLUGIN_WRAPPER.classLoader;
	}

	/**
	 * Getter for the {@link PluginWrapper}
	 * 
	 * @return the {@link PluginWrapper}
	 * @since 4.1.0
	 */
	public static PluginWrapper getPluginWrapper() {
		return INSTANCE.PLUGIN_WRAPPER;
	}

	/**
	 * Getter for a property
	 * 
	 * @param key the property key
	 * @return the property value
	 */
	private String getProperty(String key) {
		return getProperty(key, "");
	}

	/**
	 * Getter for a property
	 * 
	 * @param key the property key
	 * @param defaultValue the default value, use if the property is not set
	 * @return the property value, or the given default value if the property is not set
	 */
	private String getProperty(String key, String defaultValue) {
		return PROPERTIES.getProperty(key, defaultValue);
	}

}
