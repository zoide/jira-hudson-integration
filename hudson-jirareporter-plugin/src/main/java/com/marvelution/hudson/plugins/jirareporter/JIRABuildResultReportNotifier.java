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

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

/**
 * A {@link Notifier} to report non successful builds to JIRA
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@SuppressWarnings("unchecked")
public class JIRABuildResultReportNotifier extends Notifier {

	public final String serverAddress;
	public final String projectKey;
	public final String username;
	public final String password;

	@DataBoundConstructor
	public JIRABuildResultReportNotifier(String serverAddress, String projectKey, String username, String password) {
		this.serverAddress = serverAddress;
		this.projectKey = projectKey;
		this.username = username;
		this.password = password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
			throws InterruptedException, IOException {
		if (build.getResult().isWorseThan(Result.SUCCESS)) {
			// TODO Report the Not Successful build to JIRA
			// TODO Replace TEST-1 with the actual Issue Key
			build.addAction(new JIRABuildResultReportAction(build, "TEST-1"));
		}
		return true;
	}

	/**
	 * The {@link BuildStepDescriptor} implementation for the {@link JIRABuildResultReportNotifier}
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@Extension
	public static class JIRABuildReportNotifierDescriptorImpl extends BuildStepDescriptor<Publisher> {

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getDisplayName() {
			return "JIRA Build Result Reporter";
		}
		
	}

}
