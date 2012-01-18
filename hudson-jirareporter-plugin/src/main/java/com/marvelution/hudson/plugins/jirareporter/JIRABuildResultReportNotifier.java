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
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.xml.rpc.ServiceException;

import net.sf.json.JSONObject;

import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteIssueType;
import com.atlassian.jira.rpc.soap.client.RemoteProject;
import com.marvelution.hudson.plugins.jirareporter.utils.HudsonPluginUtils;

import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Run;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

/**
 * A {@link Notifier} to report non successful builds to JIRA
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JIRABuildResultReportNotifier extends Notifier {

	private static final Logger LOGGER = Logger.getLogger(JIRABuildResultReportNotifier.class.getName());

	@Extension
	public static final JIRABuildReportNotifierDescriptorImpl DESCRIPTOR = new JIRABuildReportNotifierDescriptorImpl();

	public final String siteName;
	public final String projectKey;
	public final boolean autoClose;
	public final String issueType;
	public final String issuePriority;
	public final boolean assignToBuildBreaker;

	/**
	 * Constructor
	 * 
	 * @param siteName the JIRA Site name
	 * @param projectKey the JIRA Project Key
	 * @param autoClose flag to auto-close previously raised issues
	 * @param issueType the type of issue to raise
	 * @param issuePriority the priority of the raised issue
	 * @param assignToBuildBreaker flag to assign the new issue to the build breaker
	 */
	@DataBoundConstructor
	public JIRABuildResultReportNotifier(String siteName, String projectKey, boolean autoClose, String issueType,
			String issuePriority, boolean assignToBuildBreaker) {
		if (siteName == null) {
			// Defaults to the first one
			JIRASite[] sites = DESCRIPTOR.getSites();
			if (sites.length > 0)
				siteName = sites[0].name;
		}
		this.siteName = siteName;
		this.projectKey = projectKey;
		this.autoClose = autoClose;
		this.issueType = issueType;
		this.issuePriority = issuePriority;
		this.assignToBuildBreaker = assignToBuildBreaker;
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

	/**
	 * Getter for the {@link JIRASite} object using the configured {@link #siteName}
	 * 
	 * @return the {@link JIRASite}, may be <code>null</code>
	 */
	public JIRASite getSite() {
		JIRASite[] sites = getDescriptor().getSites();
		if (siteName == null && sites.length > 0) {
			// Return the default JIRASite
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
		JIRAClient client = null;
		// Get the previous JIRA Build Action, if any.
		Run<?, ?> prevBuild = build.getPreviousBuild();
		JIRABuildResultReportAction buildAction = null;
		if (prevBuild != null) {
			buildAction = prevBuild.getAction(JIRABuildResultReportAction.class);
		}
		try {
			client = getSite().createClient();
			if (build.getResult().isWorseThan(Result.SUCCESS)) {
				String issueKey = null;
				if (buildAction != null && StringUtils.isNotBlank(buildAction.raisedIssueKey)
						&& !buildAction.resolved) {
					// Oke the previous build also failed and has an Issue linked to it.
					// So relink that issue also to this build
					issueKey = client.updateIssue(build, buildAction.raisedIssueKey);
				} else {
					issueKey = client.createIssue(build, projectKey, issueType, issuePriority, assignToBuildBreaker);
				}
				build.addAction(new JIRABuildResultReportAction(build, issueKey, false));
				listener.getLogger().println("JBRR: Raised build failure issue: " + issueKey);
			} else if (autoClose && build.getResult().isBetterOrEqualTo(Result.SUCCESS)) {
				// Auto close the previously raised issues, if any
				if (buildAction != null) {
					RemoteIssue issue = client.getIssue(buildAction);
					if (issue == null || issue.getKey() == null) {
						listener.getLogger().println("WARN: Failed to automatically close issue: "
							+ buildAction.raisedIssueKey + " unable to locate the issue in JIRA site "
							+ getSite().name);
					} else if (client.canCloseIssue(issue)) {
						if (client.closeIssue(issue, build)) {
							build.addAction(new JIRABuildResultReportAction(build, issue.getKey(), true));
							listener.getLogger().println("INFO: Closed issue " + issue.getKey() + " using action: "
								+ getSite().getCloseActionName());
						} else {
							listener.getLogger().println("WARN: Failed to automatically close issue: "
								+ issue.getKey());
						}
					}
				}
			}
		} catch (AxisFault e) {
			listener.error("JBRR: " + e.getFaultString());
		} catch (ServiceException e) {
			listener.error("JBRR: " + e.getMessage());
		} catch (MalformedURLException e) {
			listener.error("JBRR: Invalid JIRA URL configured");
		} finally {
			if (client != null) {
				client.logout();
			}
		}
		return true;
	}

	/**
	 * The {@link BuildStepDescriptor} implementation for the {@link JIRABuildResultReportNotifier}
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static class JIRABuildReportNotifierDescriptorImpl extends BuildStepDescriptor<Publisher> {

		public static final String PARAMETER_PREFIX = "jbrr.";

		private final CopyOnWriteList<JIRASite> sites = new CopyOnWriteList<JIRASite>();

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

		/**
		 * Getter for the base of all the help urls
		 * 
		 * @return the base help url
		 */
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

		/**
		 * Check method to validate the URL given
		 * 
		 * @param value the URL to check
		 * @return the {@link FormValidation} results
		 * @throws IOException in case of IO exceptions
		 * @throws ServletException
		 */
		public FormValidation doUrlCheck(@QueryParameter final String value) throws IOException, ServletException {
			if (!Hudson.getInstance().hasPermission(Hudson.ADMINISTER)) {
				return FormValidation.ok();
			}
			return new FormValidation.URLCheck() {

				@Override
				protected FormValidation check() throws IOException, ServletException {
					String url = Util.fixEmpty(value);
					if (url == null) {
						return FormValidation.error("The URL parameter is required");
					} else if (!url.endsWith("/")) {
						url = url + "/";
					}
					try {
						if (!findText(open(new URL(url)), "Atlassian JIRA")) {
							return FormValidation.error("This URL does not point to an Atlassian JIRA instance");
						}
						URL soapUrl = new URL(new URL(url), JIRASite.SERVICE_ENDPOINT_WSDL);
						if (!findText(open(soapUrl), "wsdl:definitions")) {
							return FormValidation.error("Unable to access the JIRA SOAP service. Is it enabled?");
						}
						return FormValidation.ok();
					} catch (IOException e) {
						return handleIOException(url, e);
					}
				}
			}.check();
		}

		/**
		 * Check method to validate the authentication details
		 * 
		 * @param request the {@link StaplerRequest}
		 * @return the {@link FormValidation} result
		 * @throws IOException in case of IO exceptions
		 */
		public FormValidation doLoginCheck(StaplerRequest request) throws IOException {
			String url = Util.fixEmpty(request.getParameter("url"));
			if (url == null) {
				return FormValidation.ok();
			}
			JIRASite site = new JIRASite("Login Check", new URL(url), request.getParameter("user"),
				request.getParameter("pass"), false, null, false);
			try {
				site.createClient();
				return FormValidation.ok();
			} catch (RemoteAuthenticationException e) {
				return FormValidation.error(e.getMessage());
			} catch (AxisFault e) {
				return FormValidation.error(e.getFaultString());
			} catch (ServiceException e) {
				return FormValidation.error(e.getMessage());
			}
		}

		/**
		 * Fill method for the SiteName field select box
		 * 
		 * @return the {@link ListBoxModel} with all possible {@link JIRASite} names
		 */
		public ListBoxModel doFillSiteNameItems() {
			ListBoxModel model = new ListBoxModel();
			for (JIRASite site : JIRABuildResultReportNotifier.DESCRIPTOR.getSites()) {
				model.add(site.name);
			}
			return model;
		}

		/**
		 * Fill method for the Issue Priorities select box
		 * 
		 * @param siteName the {@link JIRASite} name to get the priorities from
		 * @return the possible priorities
		 * @throws FormException in case of errors
		 */
		public ListBoxModel doFillIssuePriorityItems(@QueryParameter(PARAMETER_PREFIX + "siteName") String siteName)
					throws FormException {
			ListBoxModel model = new ListBoxModel();
			if (StringUtils.isBlank(siteName)) {
				return model;
			}
			JIRAClient client = null;
			try {
				client = JIRASite.getSite(siteName).createClient();
				for (Map.Entry<String, String> priority : client.getPriorities().entrySet()) {
					model.add(priority.getValue(), priority.getKey());
				}
			} catch (RemoteException e) {
				LOGGER.log(Level.SEVERE, "Failed to get the JIRA Projects", e);
				throw new FormException("Failed to get JIRA Project Keys", e, PARAMETER_PREFIX + "projectKey");
			} catch (MalformedURLException e) {
				throw new FormException("Invalid JIRA URL provided", e, PARAMETER_PREFIX + "projectKey");
			} catch (ServiceException e) {
				LOGGER.log(Level.SEVERE, "Failed to get the JIRA Projects", e);
				throw new FormException("Failed to get JIRA Project Keys", e, PARAMETER_PREFIX + "projectKey");
			} finally {
				if (client != null) {
					client.logout();
				}
			}
			return model;
		}

		/**
		 * Fill method for the ProjectKey field list box
		 * 
		 * @param siteName the name of the selected {@link JIRASite}
		 * @return the possible values for the list box in a {@link ListBoxModel}
		 * @throws FormException in case of communication issues with JIRA
		 */
		public ListBoxModel doFillProjectKeyItems(@QueryParameter(PARAMETER_PREFIX + "siteName") String siteName)
					throws FormException {
			ListBoxModel model = new ListBoxModel();
			if (StringUtils.isBlank(siteName)) {
				return model;
			}
			JIRAClient client = null;
			try {
				client = JIRASite.getSite(siteName).createClient();
				for (RemoteProject project : client.getProjects()) {
					model.add(project.getName(), project.getKey());
				}
			} catch (RemoteException e) {
				LOGGER.log(Level.SEVERE, "Failed to get the JIRA Projects", e);
				throw new FormException("Failed to get JIRA Project Keys", e, PARAMETER_PREFIX + "projectKey");
			} catch (MalformedURLException e) {
				throw new FormException("Invalid JIRA URL provided", e, PARAMETER_PREFIX + "projectKey");
			} catch (ServiceException e) {
				LOGGER.log(Level.SEVERE, "Failed to get the JIRA Projects", e);
				throw new FormException("Failed to get JIRA Project Keys", e, PARAMETER_PREFIX + "projectKey");
			} finally {
				if (client != null) {
					client.logout();
				}
			}
			return model;
		}

		/**
		 * Fill method for the IssueType field list box
		 * 
		 * @param siteName the name of the selected {@link JIRASite}
		 * @param projectKey the key of the JIRA Project
		 * @return the possible values for the list box in a {@link ListBoxModel}
		 * @throws FormException in case of communication issues with JIRA
		 */
		public ListBoxModel doFillIssueTypeItems(@QueryParameter(PARAMETER_PREFIX + "siteName") String siteName,
						@QueryParameter(PARAMETER_PREFIX + "projectKey") String projectKey) throws FormException {
			ListBoxModel model = new ListBoxModel();
			if (StringUtils.isBlank(siteName) || StringUtils.isBlank(projectKey)) {
				return model;
			}
			JIRAClient client = null;
			try {
				client = JIRASite.getSite(siteName).createClient();
				for (RemoteIssueType issueType : client.getIssueTypesForProject(projectKey)) {
					model.add(issueType.getName(), issueType.getId());
				}
			} catch (RemoteException e) {
				LOGGER.log(Level.SEVERE, "Failed to get the JIRA Projects", e);
				throw new FormException("Failed to get JIRA Project IssueTypes", e, PARAMETER_PREFIX + "projectKey");
			} catch (MalformedURLException e) {
				throw new FormException("Invalid JIRA URL provided", e, PARAMETER_PREFIX + "projectKey");
			} catch (ServiceException e) {
				LOGGER.log(Level.SEVERE, "Failed to get the JIRA Projects", e);
				throw new FormException("Failed to get JIRA Project IssueTypes", e, PARAMETER_PREFIX + "projectKey");
			} finally {
				if (client != null) {
					client.logout();
				}
			}
			return model;
		}
		
	}

}
