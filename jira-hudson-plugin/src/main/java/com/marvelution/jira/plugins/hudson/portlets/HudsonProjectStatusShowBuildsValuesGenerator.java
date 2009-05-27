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

package com.marvelution.jira.plugins.hudson.portlets;

import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;

import com.atlassian.configurable.ValuesGenerator;
import com.atlassian.jira.web.bean.I18nBean;

/**
 * {@link ValuesGenerator} implementation to generate the {@link Map} of possible values for the
 * {@link HudsonProjectStatusPortlet} initial build show property
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonProjectStatusShowBuildsValuesGenerator implements ValuesGenerator {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Map getValues(Map params) {
		final Map values = new ListOrderedMap();
		values.put("all", getText("hudson.portlet.configuration.show.builds.all"));
		values.put("last_build", getText("hudson.portlet.configuration.show.builds.only.last"));
		values.put("last_successful_build", getText("hudson.portlet.configuration.show.builds.only.successful"));
		values.put("last_unstable_build", getText("hudson.portlet.configuration.show.builds.only.unstable"));
		values.put("last_failed_build", getText("hudson.portlet.configuration.show.builds.only.failed"));
		values.put("none", getText("hudson.portlet.configuration.show.builds.none"));
		return values;
	}

	/**
	 * Get internationalisation text
	 * 
	 * @param key the key of the text to get
	 * @return the text
	 */
	String getText(String key) {
		return new I18nBean().getText(key);
	}

}
