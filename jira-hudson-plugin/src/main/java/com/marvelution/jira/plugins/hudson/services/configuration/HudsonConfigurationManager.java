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

import java.util.Collection;

/**
 * Configuration Manager interface for the Hudson plugin
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonConfigurationManager {

	/**
	 * Getter for the 'Hide Unassociated Hudson Tabs' property
	 * 
	 * @return the value for the property
	 */
	Boolean isHideUnassociatedHudsonTabs();

	/**
	 * Setter for the 'Hide Unassociated Hudson Tabs' property
	 * 
	 * @param hideUnassociatedHudsonTabs the property value
	 */
	void setHideUnassociatedHudsonTabs(Boolean hideUnassociatedHudsonTabs);

	/**
	 * Getter for the 'Filter Hudson Builds' property
	 * 
	 * @return the value for the property
	 */
	Boolean isFilterHudsonBuilds();

	/**
	 * Setter for the 'Filter Hudson Builds' property
	 * 
	 * @param filterHudsonBuilds the property value
	 */
	void setFilterHudsonBuilds(Boolean filterHudsonBuilds);

	/**
	 * Getter for the 'Time Past Date Strings' property
	 * 
	 * @return the value of the property
	 */
	Boolean isTimePastDateStrings();

	/**
	 * Setter for the 'Time Past Date Strings' property
	 * 
	 * @param timePastDateStrings the property value
	 */
	void setTimePastDateStrings(Boolean timePastDateStrings);

	/**
	 * Getter for the 'Show If User Member Of Usergroup' property
	 * 
	 * @return the value for the property
	 */
	Collection<String> getShowIfUserMemberOfUsergroup();

	/**
	 * Setter for the 'Show If User Member Of Usergroup' property
	 * 
	 * @param showIfUserMemberOfUsergroup the property value
	 */
	void setShowIfUserMemberOfUsergroup(Collection<String> showIfUserMemberOfUsergroup);

	/**
	 * Getter for the 'Show If User Member Of Project Role' property
	 * 
	 * @return the value for the property
	 */
	Collection<String> getShowIfUserMemberOfProjectRole();

	/**
	 * Setter for the 'Show If User Member Of Project Role' property
	 * 
	 * @param showIfUserMemberOfProjectRole the property value
	 */
	void setShowIfUserMemberOfProjectRole(Collection<String> showIfUserMemberOfProjectRole);

	/**
	 * Getter for the 'Show If Issue Of IssueType' property
	 * 
	 * @return the value for the property
	 */
	Collection<String> getShowIfIssueOfIssueType();

	/**
	 * Setter for the 'Show If Issue Of IssueType' property
	 * 
	 * @param showIfIssueOfIssueType the property value
	 */
	void setShowIfIssueOfIssueType(Collection<String> showIfIssueOfIssueType);

}
