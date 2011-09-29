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

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.ClassUtils;
import org.dozer.util.DozerClassLoader;
import org.dozer.util.MappingUtils;

import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;

/**
 * Hudson Specific {@link DozerClassLoader}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonDozerClassLoader implements DozerClassLoader {

	private static final Logger LOGGER = Logger.getLogger(HudsonDozerClassLoader.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> loadClass(String className) {
		LOGGER.log(Level.FINE, "Trying to load Class: " + className);
		Class<?> result = null;
		try {
			result = ClassUtils.getClass(HudsonPluginUtils.getPluginClassloader(), className);
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
		LOGGER.log(Level.FINE, "Trying to load Resource: " + uri);
		return HudsonPluginUtils.getPluginClassloader().getResource(uri);
	}

}
