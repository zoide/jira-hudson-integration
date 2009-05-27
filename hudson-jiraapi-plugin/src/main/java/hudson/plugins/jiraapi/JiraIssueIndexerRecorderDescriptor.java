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

package hudson.plugins.jiraapi;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

/**
 * {@link Descriptor} for {@link JiraIssueIndexerRecorder}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class JiraIssueIndexerRecorderDescriptor extends BuildStepDescriptor<Publisher> {

	public static final BuildStepDescriptor<Publisher> DESCRIPTOR = new JiraIssueIndexerRecorderDescriptor();

	/**
	 * Constructor
	 */
	public JiraIssueIndexerRecorderDescriptor() {
		super(JiraIssueIndexerRecorder.class);
		load();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayName() {
		return Messages.getJiraIssueIndexerDisplayName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHelpFile() {
		return "/plugin/hudson-jiraapi-plugin/help-indexer.html";
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isApplicable(Class<? extends AbstractProject> jobType) {
		return AbstractProject.class.isAssignableFrom(jobType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Publisher newInstance(StaplerRequest req, JSONObject formData)
					throws hudson.model.Descriptor.FormException {
		return req.bindJSON(JiraIssueIndexerRecorder.class, formData);
	}

}
