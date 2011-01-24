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

package com.marvelution.hudson.plugins.apiv2.dozer.utils;

import hudson.PluginWrapper;
import hudson.model.Hudson;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.ClassUtils;
import org.dozer.util.DozerClassLoader;
import org.dozer.util.MappingUtils;

import com.marvelution.hudson.plugins.apiv2.PluginImpl;

/**
 * Hudson Specific {@link DozerClassLoader}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonDozerClassLoader implements DozerClassLoader {

	private static final Logger LOGGER = Logger.getLogger(HudsonDozerClassLoader.class.getName());

	private PluginWrapper pluginWrapper = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> loadClass(String className) {
		LOGGER.log(Level.FINE, "Trying to load: " + className);
		Class<?> result = null;
		try {
			result = ClassUtils.getClass(getPluginWrapper().classLoader, className);
		} catch (ClassNotFoundException e) {
			MappingUtils.throwMappingException(e);
		}
	    return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL loadResource(String uri) {
		LOGGER.log(Level.FINE, "Trying to load: " + uri);
		return getPluginWrapper().classLoader.getResource(uri);
	}

	/**
	 * Get the {@link PluginWrapper} for the plugin so Dozer can load its classes
	 * 
	 * @return the {@link PluginWrapper} of the hudson-apiv2-plugin
	 */
	public PluginWrapper getPluginWrapper() {
		if (pluginWrapper == null) {
			pluginWrapper = Hudson.getInstance().getPluginManager().getPlugin(PluginImpl.class);
		}
		return pluginWrapper;
	}

}
