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

package hudson.plugins.jiraapi.index.model;

import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Model class for the Issue Index
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("issue-index")
public class IssueIndex {

	@XStreamImplicit(itemFieldName = "issue")
	private Set<Issue> index = new HashSet<Issue>();

	/**
	 * Gets the Index
	 * 
	 * @return {@link Set} of {@link Issue} objects
	 */
	public Set<Issue> getIndex() {
		return index;
	}

	/**
	 * Sets the Index
	 * 
	 * @param index {@link Set} of {@link Issue} objects
	 */
	public void setIndex(Set<Issue> index) {
		this.index = index;
	}

	/**
	 * Get the index of a specific issue
	 * 
	 * @param key the key of the Issue to get the index for
	 * @return the {@link Issue} object containing the indexed builds
	 */
	public Issue getIssueIndex(String key) {
		final Issue temp = new Issue(key);
		for (Issue issue : index) {
			if (issue.equals(temp)) {
				return issue;
			}
		}
		return null;
	}

	/**
	 * Add an {@link Issue} to the index
	 * 
	 * @param issue the {@link Issue} to add
	 */
	public void addIssue(Issue issue) {
		if (getIndex().contains(issue)) {
			final Issue inIndex = getIssueIndex(issue.getKey());
			inIndex.merge(issue);
		} else {
			getIndex().add(issue);			
		}
	}

	/**
	 * Ad an {@link Issue} to the index
	 * 
	 * @param key the Issue key
	 * @param projects the {@link Set} of {@link Project} objects
	 */
	public void addIssue(String key, Set<Project> projects) {
		final Issue issue = new Issue(key);
		issue.setProjects(projects);
		addIssue(issue);
	}

	/**
	 * Add an {@link Issue} to the index
	 * 
	 * @param key the Issue key
	 * @param jobName the Job name
	 * @param buildNumbers the {@link Set} of {@link Integer} build numbers
	 */
	public void addIssue(String key, String jobName, Set<Integer> buildNumbers) {
		addIssue(key, jobName, null, buildNumbers);
	}

	/**
	 * Add an {@link Issue} to the index
	 * 
	 * @param key the Issue key
	 * @param jobName the Job name
	 * @param parentName the Job parent name
	 * @param buildNumbers the {@link Set} of {@link Integer} build numbers
	 */
	public void addIssue(String key, String jobName, String parentName, Set<Integer> buildNumbers) {
		final Project project = new Project(jobName, parentName);
		project.setBuildNumbers(buildNumbers);
		final Issue issue = new Issue(key);
		issue.getProjects().add(project);
		addIssue(issue);
	}

	/**
	 * Add an {@link Issue} to the index
	 * 
	 * @param key the Issue key
	 * @param jobName the Job name
	 * @param buildNumber the {@link Integer} build number
	 */
	public void addIssue(String key, String jobName, Integer buildNumber) {
		addIssue(key, jobName, null, buildNumber);
	}

	/**
	 * Add an {@link Issue} to the index
	 * 
	 * @param key the Issue key
	 * @param jobName the Job name
	 * @param parentName the Job parent name
	 * @param buildNumber the {@link Integer} build number
	 */
	public void addIssue(String key, String jobName, String parentName, Integer buildNumber) {
		final Project project = new Project(jobName, parentName);
		final Set<Integer> buildNumbers = new HashSet<Integer>();
		buildNumbers.add(buildNumber);
		project.setBuildNumbers(buildNumbers);
		final Issue issue = new Issue(key);
		issue.getProjects().add(project);
		addIssue(issue);
	}

}
