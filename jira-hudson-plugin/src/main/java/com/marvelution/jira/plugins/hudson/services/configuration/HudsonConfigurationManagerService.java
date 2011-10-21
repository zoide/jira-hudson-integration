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

package com.marvelution.jira.plugins.hudson.services.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;

/**
 * Default {@link HudsonConfigurationManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonConfigurationManagerService implements HudsonConfigurationManager {

	private static final String ARRAY_SEPARATOR = ";;";
	private static final String CONFIG_SETTING_PREFIX = "hudson.configuration.";
	private static final String HIDE_UNASSOCIATED_HUDSON_TABS = CONFIG_SETTING_PREFIX
		+ "hide.unassociated.hudson.tabs";
	private static final String FILTER_HUDSON_BUILDS = CONFIG_SETTING_PREFIX + "filter.hudson.builds";
	private static final String TIME_PAST_DATE_STRINGS = CONFIG_SETTING_PREFIX + "time.past.date.strings";
	private static final String SHOW_IF_USER_MEMEBER_OF_USERGROUP = CONFIG_SETTING_PREFIX 
		+ "show.if.user.member.of.usergroup";
	private static final String SHOW_IF_USER_MEMEBER_OF_PROJECTROLE = CONFIG_SETTING_PREFIX 
		+ "show.if.user.member.of.projectrole";
	private static final String SHOW_IF_ISSUE_OF_ISSUETYPE = CONFIG_SETTING_PREFIX + "show.if.issue.of.issuetype";

	private static final long PROPERTY_ID = 2L;
	private PropertySet propertySet;

	private Boolean hideUnassociatedHudsonTabs = null;
	private Boolean filterHudsonBuilds = null;
	private Boolean timePastDateStrings = null;
	private Collection<String> showIfUserMemberOfUsergroup = null;
	private Collection<String> showIfUserMemberOfProjectRole = null;
	private Collection<String> showIfIssueOfIssueType = null;

	/**
	 * Constructor
	 */
	public HudsonConfigurationManagerService() {
		loadPropertySet();
		Preconditions.checkNotNull(propertySet, "propertySet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isHideUnassociatedHudsonTabs() {
		if (hideUnassociatedHudsonTabs == null) {
			hideUnassociatedHudsonTabs = propertySet.getBoolean(HIDE_UNASSOCIATED_HUDSON_TABS);
		}
		return hideUnassociatedHudsonTabs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHideUnassociatedHudsonTabs(Boolean hideUnassociatedHudsonTabs) {
		this.hideUnassociatedHudsonTabs = hideUnassociatedHudsonTabs;
		propertySet.setBoolean(HIDE_UNASSOCIATED_HUDSON_TABS, hideUnassociatedHudsonTabs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isFilterHudsonBuilds() {
		if (filterHudsonBuilds == null) {
			filterHudsonBuilds = propertySet.getBoolean(FILTER_HUDSON_BUILDS);
		}
		return filterHudsonBuilds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFilterHudsonBuilds(Boolean filterHudsonBuilds) {
		this.filterHudsonBuilds = filterHudsonBuilds;
		propertySet.setBoolean(FILTER_HUDSON_BUILDS, filterHudsonBuilds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isTimePastDateStrings() {
		if (timePastDateStrings == null) {
			timePastDateStrings = propertySet.getBoolean(TIME_PAST_DATE_STRINGS);
		}
		return timePastDateStrings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTimePastDateStrings(Boolean timePastDateStrings) {
		this.timePastDateStrings = timePastDateStrings;
		propertySet.setBoolean(TIME_PAST_DATE_STRINGS, timePastDateStrings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getShowIfUserMemberOfUsergroup() {
		if (showIfUserMemberOfUsergroup == null) {
			showIfUserMemberOfUsergroup = getTextAsCollection(propertySet.getText(SHOW_IF_USER_MEMEBER_OF_USERGROUP));
		}
		return showIfUserMemberOfUsergroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowIfUserMemberOfUsergroup(Collection<String> showIfUserMemberOfUsergroup) {
		this.showIfUserMemberOfUsergroup = showIfUserMemberOfUsergroup;
		propertySet.setText(SHOW_IF_USER_MEMEBER_OF_USERGROUP, getCollectionAsText(showIfUserMemberOfUsergroup));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getShowIfUserMemberOfProjectRole() {
		if (showIfUserMemberOfProjectRole == null) {
			showIfUserMemberOfProjectRole = getTextAsCollection(propertySet.getText(
					SHOW_IF_USER_MEMEBER_OF_PROJECTROLE));
		}
		return showIfUserMemberOfProjectRole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowIfUserMemberOfProjectRole(Collection<String> showIfUserMemberOfProjectRole) {
		this.showIfUserMemberOfProjectRole = showIfUserMemberOfProjectRole;
		propertySet.setText(SHOW_IF_USER_MEMEBER_OF_PROJECTROLE, getCollectionAsText(showIfUserMemberOfProjectRole));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getShowIfIssueOfIssueType() {
		if (showIfIssueOfIssueType == null) {
			showIfIssueOfIssueType = getTextAsCollection(propertySet.getText(SHOW_IF_ISSUE_OF_ISSUETYPE));
		}
		return showIfIssueOfIssueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowIfIssueOfIssueType(Collection<String> showIfIssueOfIssueType) {
		this.showIfIssueOfIssueType = showIfIssueOfIssueType;
		propertySet.setText(SHOW_IF_ISSUE_OF_ISSUETYPE, getCollectionAsText(showIfIssueOfIssueType));
	}

	/**
	 * Convert a {@link String} {@link Collection} into a single {@link String}
	 * 
	 * @param collection the {@link Collection} to convert
	 * @return the resulting {@link String}
	 */
	private String getCollectionAsText(Collection<String> collection) {
		return StringUtils.join(collection, ARRAY_SEPARATOR);
	}

	/**
	 * Convert a {@link String} into a {@link String} {@link Collection}
	 * 
	 * @param text the {@link String} to convert
	 * @return the resulting {@link Collection}
	 */
	private Collection<String> getTextAsCollection(String text) {
		if (StringUtils.isBlank(text)) {
			return new ArrayList<String>();
		}
		return Arrays.asList(StringUtils.split(text, ARRAY_SEPARATOR));
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
