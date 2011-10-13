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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wink.common.internal.utils.FileLoader;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.util.DozerConstants;

import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;

/**
 * Utility class for the <a href="http://dozer.sourceforge.net">Dozer</a> {@link Mapper} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class DozerUtils {

	private static final String DOZER_CONFIG_LOCATION = "META-INF/dozer";
	private static final Logger LOGGER = Logger.getLogger(DozerUtils.class.getName());

	private static DozerBeanMapper mapper = null;
	private static List<String> mappingFiles = null;

	public static final String FULL_MAP_ID = "full";
	public static final String NAMEONLY_MAP_ID = "nameOnly";
	public static final String ACTIVITY_MAP_ID = "activity";

	/**
	 * Get the {@link Mapper} implementation
	 * 
	 * @return the {@link Mapper}
	 */
	public static Mapper getMapper() {
		if (mapper == null) {
			System.setProperty(DozerConstants.CONFIG_FILE_SYS_PROP, DOZER_CONFIG_LOCATION + "/configuration.properties");
			mapper = new DozerBeanMapper();
			mapper.setMappingFiles(getMappingFiles());
		}
		return mapper;
	}

	/**
	 * Get all the custom Dozer mapping files
	 * 
	 * @return {@link List} of all the mapping files
	 */
	public static List<String> getMappingFiles() {
		if (mappingFiles == null) {
			// First load, add all the plugin default mappings
			mappingFiles = new ArrayList<String>();
			try {
				final URL resourcePackage = FileLoader.loadFile(DOZER_CONFIG_LOCATION);
				for (String filename : new File(resourcePackage.toURI()).list()) {
					if (!filename.startsWith("optional") && filename.endsWith(".xml")) {
						LOGGER.log(Level.FINE, "Loaded Dozer Mapping file: " + DOZER_CONFIG_LOCATION + "/" + filename);
						mappingFiles.add(DOZER_CONFIG_LOCATION + "/" + filename);
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Failed to load Dozer Mapper configuration", e);
			}
		}
		if (HudsonPluginUtils.hasTestNGPlugin()) {
			// TestNG plugin is used, add the TestNG Dozer mapping
			mappingFiles.add(DOZER_CONFIG_LOCATION + "/optional-testng-dozer-mapping.xml");
		}
		// TODO Support user specific mapping files
		return mappingFiles;
	}

}
