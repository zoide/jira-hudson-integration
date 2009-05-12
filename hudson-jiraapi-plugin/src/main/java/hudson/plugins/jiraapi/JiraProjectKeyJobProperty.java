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

import hudson.model.AbstractProject;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import com.marvelution.jira.plugins.hudson.utils.JiraKeyUtils;

/**
 * {@link JobProperty} to implement a property for a Jira Project Key
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@ExportedBean
public class JiraProjectKeyJobProperty extends JobProperty<AbstractProject<?, ?>> {

	public static final JiraProjectKeyJobPropertyDescriptor DESCRIPTOR = new JiraProjectKeyJobPropertyDescriptor();

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
	public void setKey(String key) {
		if (JiraKeyUtils.isValidProjectKey(key)) {
			this.key = key;
		} else {
			throw new IllegalArgumentException(key + " is not a valid JIRA Project Key");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public JobPropertyDescriptor getDescriptor() {
		return DESCRIPTOR;
	}
	
	/**
	 * Gets the JIRA Property display name
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return DESCRIPTOR.getDisplayName();
	}

}
