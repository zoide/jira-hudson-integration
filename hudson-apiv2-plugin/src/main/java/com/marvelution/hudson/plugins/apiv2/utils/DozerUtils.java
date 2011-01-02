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

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wink.common.internal.utils.FileLoader;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * Utility class for the <a href="http://dozer.sourceforge.net">Dozer</a> {@link Mapper} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class DozerUtils {

	private static final Logger LOGGER = Logger.getLogger(DozerUtils.class.getName());
	private static DozerBeanMapper mapper = null;
	private static List<String> mappingFiles = null;

	/**
	 * Get the {@link Mapper} implementation
	 * 
	 * @return the {@link Mapper}
	 */
	public static Mapper getMapper() {
		if (mapper == null) {
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
			try {
				final URL resourcePackage = FileLoader.loadFile("META-INF/dozer");
				for (String filename : new File(resourcePackage.toURI()).list()) {
					if (filename.endsWith(".xml")) {
						mappingFiles.add(filename);
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Failed to load Dozer Mapper configuration", e);
			}
		}
		return mappingFiles;
	}

}
