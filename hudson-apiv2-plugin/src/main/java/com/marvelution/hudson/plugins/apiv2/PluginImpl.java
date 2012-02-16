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

package com.marvelution.hudson.plugins.apiv2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;

import com.google.common.collect.Lists;
import com.marvelution.hudson.plugins.apiv2.cache.activity.ActivitiesCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.BuildActivityCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.JobActivityCache;
import com.marvelution.hudson.plugins.apiv2.servlet.filter.HudsonAPIV2ServletFilter;
import com.thoughtworks.xstream.XStream;

import hudson.Plugin;
import hudson.util.PluginServletFilter;

/**
 * Main Plugin implementation of the Hudson REST plugin.
 * This class is responsible for the registration of the
 * <a href="http://incubator.apache.org/wink/">Wink Application</a> implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @plugin
 */
public class PluginImpl extends Plugin {

	private static final Logger LOGGER = Logger.getLogger(PluginImpl.class.getName());
	private static final String APIV2_DIRECTORY_NAME = "APIv2";
	private static final String ACTIVITIES_CHACHE_FILE = "activities-cache.xml";
	private static final XStream XSTREAM = new XStream();

	private static PluginImpl plugin;

	private List<Filter> filters = Lists.newArrayList();
	private ActivitiesCache activitiesCache;

	/**
	 * {@inheritDoc}
	 */
	public void start() throws Exception {
		plugin = this;
		LOGGER.log(Level.FINE, "Adding the APIv2 Filters");
		filters.add(new HudsonAPIV2ServletFilter());
		for (Filter filter : filters) {
			PluginServletFilter.addFilter(filter);
		}
		LOGGER.log(Level.FINE, "Loading the Activity Cache");
		File activityCacheFile = getFile(ACTIVITIES_CHACHE_FILE);
		if (activityCacheFile.exists()) {
			activitiesCache = (ActivitiesCache) XSTREAM.fromXML(new FileInputStream(activityCacheFile));
		} else {
			activitiesCache = new ActivitiesCache();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop() throws Exception {
		LOGGER.log(Level.FINE, "Removing the APIv2 Filters");
		for (Filter filter : filters) {
			PluginServletFilter.removeFilter(filter);
		}
		filters.clear();
		LOGGER.log(Level.FINE, "Storing the Activity Cache");
		XSTREAM.toXML(activitiesCache, new FileOutputStream(getFile(ACTIVITIES_CHACHE_FILE)));
		plugin = null;
	}

	/**
	 * Getter for the {@link ActivitiesCache}
	 * 
	 * @return the {@link ActivitiesCache}
	 */
	public static ActivitiesCache getActivitiesCache() {
		return plugin.activitiesCache;
	}

	/**
	 * Getter for a {@link File} by name
	 * 
	 * @param filename the file name to get
	 * @return the {@link File}
	 */
	public File getFile(String filename) {
		File dir = new File(getConfigXml().getFile().getParent(), APIV2_DIRECTORY_NAME);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return new File(dir, filename);
	}

	/**
	 * Initialize the {@link XStream} object
	 */
	static {
		XSTREAM.autodetectAnnotations(true);
		XSTREAM.processAnnotations(ActivitiesCache.class);
		XSTREAM.processAnnotations(JobActivityCache.class);
		XSTREAM.processAnnotations(BuildActivityCache.class);
	}

}
