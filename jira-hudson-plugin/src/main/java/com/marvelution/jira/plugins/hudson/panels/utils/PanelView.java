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

package com.marvelution.jira.plugins.hudson.panels.utils;

/**
 * Panel View enum for the different views that are supported
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public enum PanelView {

	JOB_STATUS("jobStatus"), BUILDS_BY_JOB("buildsByJob"), BUILDS_BY_ISSUE("buildsByIssue");

	private String view;

	/**
	 * Constructor
	 * 
	 * @param view the view name
	 */
	private PanelView(String view) {
		this.view = view;
	}

	/**
	 * Getter for the view
	 * 
	 * @return the view
	 */
	public String getViewName() {
		return view;
	}

	/**
	 * Get the {@link PanelView} for the given view value
	 * 
	 * @param value the view value
	 * @return the {@link PanelView} objects where the given value is equal to the name or the the view or
	 *         {@link #BUILDS_BY_JOB} if the value given is not a valid {@link PanelView}
	 */
	public static PanelView getPanelView(String value) {
		if (JOB_STATUS.getViewName().equals(value) || JOB_STATUS.name().equalsIgnoreCase(value)) {
			return JOB_STATUS;
		} else if (BUILDS_BY_JOB.getViewName().equals(value) || BUILDS_BY_JOB.name().equalsIgnoreCase(value)) {
			return BUILDS_BY_JOB;
		} else if (BUILDS_BY_ISSUE.getViewName().equals(value) || BUILDS_BY_ISSUE.name().equalsIgnoreCase(value)) {
			return BUILDS_BY_ISSUE;
		} else {
			return BUILDS_BY_JOB;
		}
	}

}
