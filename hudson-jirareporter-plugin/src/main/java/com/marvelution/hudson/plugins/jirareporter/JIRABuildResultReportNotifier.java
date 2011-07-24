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

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.marvelution.hudson.plugins.jirareporter.utils.HudsonPluginUtils;

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
import hudson.util.CopyOnWriteList;

/**
 * A {@link Notifier} to report non successful builds to JIRA
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JIRABuildResultReportNotifier extends Notifier {

	@Extension
	public static final JIRABuildReportNotifierDescriptorImpl DESCRIPTOR = new JIRABuildReportNotifierDescriptorImpl();

	public final String siteName;
	public final String projectKey;

	/**
	 * Constructor
	 * 
	 * @param siteName the JIRA Site name
	 * @param projectKey the JIRA Project Key
	 */
	@DataBoundConstructor
	public JIRABuildResultReportNotifier(String siteName, String projectKey) {
		if (siteName == null) {
			// Defaults to the first one
			JIRASite[] sites = DESCRIPTOR.getSites();
			if (sites.length > 0)
				siteName = sites[0].name;
		}
		this.siteName = siteName;
		this.projectKey = projectKey;
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
	public JIRABuildReportNotifierDescriptorImpl getDescriptor() {
		return DESCRIPTOR;
	}

	public JIRASite getSite() {
		JIRASite[] sites = getDescriptor().getSites();
		if (siteName == null && sites.length > 0) {
			// Return the default one
			return sites[0];
		}
		for (JIRASite site : sites) {
			if (site.name.equals(siteName)) {
				return site;
			}
		}
		return null;
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
	public static class JIRABuildReportNotifierDescriptorImpl extends BuildStepDescriptor<Publisher> {

		private final CopyOnWriteList<JIRASite> sites = new CopyOnWriteList<JIRASite>();

		public static final String PARAMETER_PREFIX = "jbrr.";

		/**
		 * Default Constructor
		 */
		public JIRABuildReportNotifierDescriptorImpl() {
			super(JIRABuildResultReportNotifier.class);
			load();
		}

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

		/**
		 * Add a Site to the Configuration
		 * 
		 * @param site the {@link JIRASite} to add
		 */
		public void setSites(JIRASite site) {
			sites.add(site);
		}

		/**
		 * Get all the configured {@link JIRASite} objects in an Array
		 * 
		 * @return the {@link JIRASite} array
		 */
		public JIRASite[] getSites() {
			return sites.toArray(new JIRASite[0]);
		}

		public String getBaseHelpURL() {
			return "/plugin/" + HudsonPluginUtils.getPluginArifactId() + "/help-";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public JIRABuildResultReportNotifier newInstance(StaplerRequest req, JSONObject formData)
				throws FormException {
			JIRABuildResultReportNotifier notifier = req.bindParameters(JIRABuildResultReportNotifier.class, PARAMETER_PREFIX);
			if (notifier.siteName == null) {
				notifier = null;
			}
			return notifier;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
			sites.replaceBy(req.bindParametersToList(JIRASite.class, PARAMETER_PREFIX));
			save();
			return true;
		}
		
	}

}
