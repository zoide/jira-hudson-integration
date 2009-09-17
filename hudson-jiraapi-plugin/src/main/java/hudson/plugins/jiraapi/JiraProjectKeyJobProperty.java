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

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.plugins.jiraapi.utils.JiraKeyUtils;

import net.sf.json.JSONObject;

import org.codehaus.plexus.util.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * {@link JobProperty} to implement a property for a Jira Project Key
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@ExportedBean
public class JiraProjectKeyJobProperty extends JobProperty<AbstractProject<?, ?>> implements
		Comparable<JiraProjectKeyJobProperty> {

	private String key = "";

	/**
	 * Constructor
	 * 
	 * @param key the JIRA Key
	 */
	@DataBoundConstructor
	public JiraProjectKeyJobProperty(String key) {
		setKey(key);
	}

	/**
	 * Gets the JIRA Key
	 * 
	 * @return the JIRA Key
	 */
	@Exported(name = "jira-key")
	public String getKey() {
		return key;
	}
	
	/**
	 * Sets the JIRA Project Key
	 * 
	 * @param key the JIRA Project Key
	 */
	@SuppressWarnings("unchecked")
	public void setKey(String key) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		if (JiraKeyUtils.isValidProjectKey(key, getProjectKeyPattern())) {
			this.key = key;
		} else {
			throw new IllegalArgumentException(key + " is not a valid JIRA Project Key ("
				+ getProjectKeyPattern().pattern() + ")");
		}
		final List<AbstractProject> projects = Hudson.getInstance().getAllItems(AbstractProject.class);
		for (AbstractProject<?, ?> project : projects) {
			if (project.getProperty(JiraProjectKeyJobProperty.class) != null
				&& project.getProperty(JiraProjectKeyJobProperty.class).equals(this)) {
				throw new IllegalArgumentException("No key duplicates allowed. Key " + key
					+ " is already used by project: " + project.getName());
			}
		}
	}

	/**
	 * Get the Global Jira project key {@link Pattern}
	 * 
	 * @return the Global Jira project key {@link Pattern}
	 */
	public Pattern getProjectKeyPattern() {
		return ((JiraProjectKeyJobPropertyDescriptor) getDescriptor()).getProjectKeyPattern();
	}

	/**
	 * Get the Global Jira issue key {@link Pattern}
	 * 
	 * @return the Global Jira issue key {@link Pattern}
	 */
	public Pattern getIssueKeyPattern() {
		return ((JiraProjectKeyJobPropertyDescriptor) getDescriptor()).getIssueKeyPattern();
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(JiraProjectKeyJobProperty other) {
		return getKey().compareTo(other.getKey());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JiraProjectKeyJobProperty) {
			return getKey().equals(((JiraProjectKeyJobProperty) obj).getKey());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getKey();
	}

	/**
	 * {@link JobPropertyDescriptor} for {@link JiraProjectKeyJobProperty}
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@Extension
	public static final class JiraProjectKeyJobPropertyDescriptor extends JobPropertyDescriptor {

		private Pattern projectKeyPattern = JiraKeyUtils.DEFAULT_JIRA_PROJECT_KEY_PATTERN;

		private Pattern issueKeyPattern = JiraKeyUtils.DEFAULT_JIRA_ISSUE_KEY_PATTERN;

		/**
		 * Constructor
		 */
		public JiraProjectKeyJobPropertyDescriptor() {
			super(JiraProjectKeyJobProperty.class);
			load();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getDisplayName() {
			return Messages.getJiraKeyPropertyDisplayName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
			try {
				if (StringUtils.isNotEmpty(json.getString("projectKeyPattern"))) {
					projectKeyPattern = Pattern.compile(json.getString("projectKeyPattern"));
				}
			} catch (PatternSyntaxException e) {
				throw new FormException(e, "Invalid Jira Project key pattern");
			}
			try {
				if (StringUtils.isNotEmpty(json.getString("issueKeyPattern"))) {
					issueKeyPattern = Pattern.compile(json.getString("issueKeyPattern"));
				}
			} catch (PatternSyntaxException e) {
				throw new FormException(e, "Invalid Jira Issue key pattern");
			}
			save();
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean isApplicable(Class<? extends Job> jobType) {
			return AbstractProject.class.isAssignableFrom(jobType);
		}

		/**
		 * Get the configured Jira project key pattern
		 * 
		 * @return the Jira project key pattern
		 */
		public Pattern getProjectKeyPattern() {
			return projectKeyPattern;
		}

		/**
		 * Get the configured Jira issue key pattern
		 * 
		 * @return the Jira issue key pattern
		 */
		public Pattern getIssueKeyPattern() {
			return issueKeyPattern;
		}

	}

}
