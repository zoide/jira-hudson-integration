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

package com.marvelution.hudson.plugins.jirareporter;

import com.marvelution.hudson.plugins.jirareporter.utils.HudsonPluginUtils;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JIRABuildResultReportAction implements Action {

	public final AbstractBuild<?, ?> owner;
	public final String raisedIssueKey;

	/**
	 * Constructor
	 * 
	 * @param owner the {@link AbstractBuild} that this {@link Action} is owner by
	 * @param raisedIssueKey the raised JIRA Issue Key
	 */
	public JIRABuildResultReportAction(AbstractBuild<?, ?> owner, String raisedIssueKey) {
		this.owner = owner;
		this.raisedIssueKey = raisedIssueKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIconFileName() {
		return "/plugin/" + HudsonPluginUtils.getPluginArifactId() + "/jira.png";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayName() {
		return "JIRA Build Result Report";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrlName() {
		return "jbrr";
	}

	/**
	 * Get the {@link JIRABuildResultReportNotifier} configuration of the Project
	 * 
	 * @return the {@link JIRABuildResultReportNotifier}
	 */
	public JIRABuildResultReportNotifier getNotifier() {
		DescribableList<Publisher,Descriptor<Publisher>> publishers = owner.getProject().getPublishersList();
		return publishers.get(JIRABuildResultReportNotifier.class);
	}

}
