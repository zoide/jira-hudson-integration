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

package hudson.plugins.jiraapi.index;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.plugins.jiraapi.JiraProjectKeyJobProperty;
import hudson.plugins.jiraapi.index.model.IssueIndex;
import hudson.plugins.jiraapi.index.model.Issue;
import hudson.plugins.jiraapi.index.model.Project;
import hudson.plugins.jiraapi.utils.JiraKeyUtils;
import hudson.plugins.jiraapi.utils.ProjectUtils;
import hudson.scm.ChangeLogSet.Entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;

/**
 * Issue Indexer helper class
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class IssueIndexer {

	private static final Logger LOGGER = Logger.getLogger(IssueIndexer.class.getName());

	private static IssueIndexer instance;

	private static XStream xstream;

	private File indexFile;

	private IssueIndex index;

	/**
	 * Private constructor
	 */
	private IssueIndexer() {
		index = new IssueIndex();
	}

	/**
	 * Get the instance of the {@link IssueIndexer}
	 * 
	 * @return the {@link IssueIndexer} instance
	 */
	public static IssueIndexer getInstance() {
		if (instance == null) {
			instance = new IssueIndexer();
		}
		return instance;
	}

	/**
	 * Sets the index {@link File}
	 * 
	 * @param indexFile the index {@link File}
	 */
	public void setIndexFile(File indexFile) {
		this.indexFile = indexFile;
	}

	/**
	 * Get a view of the Issue Index
	 * 
	 * @param issueKey the issue key to get the index for
	 * @return unmodifiable {@link List} of indexed {@link Issue} objects
	 */
	public Issue getIssueIndex(String issueKey) {
		return index.getIssueIndex(issueKey);
	}

	/**
	 * Load the index
	 * 
	 * @throws IOException in case the cache file cannot be loaded
	 */
	public void load() throws IOException {
		LOGGER.log(Level.FINE, "Loading index from file: " + indexFile.getName());
		synchronized (index) {
			index = (IssueIndex) xstream.fromXML(new FileInputStream(this.indexFile));
		}
	}

	/**
	 * Save the index
	 * 
	 * @throws IOException in case of write errors to the cache file
	 */
	public void save() throws IOException {
		LOGGER.log(Level.FINE, "Saving index to file: " + indexFile.getName());
		synchronized (indexFile) {
			xstream.toXML(index, new FileOutputStream(indexFile));
		}
	}

	/**
	 * Validates the {@link IssueIndex} and removes builds that are no longer available from the index
	 * 
	 * @throws IOException in case the validated index cannot be saved to the file system
	 */
	public void validateIssueIndex() throws IOException {
		LOGGER.log(Level.FINE,
			"Validating the Issue Index to make sure only valid Jobs and Builds are related to Issue Keys");
		if (index == null || index.getIndex() == null || index.getIndex().isEmpty()) {
			return;
		}
		for (final Iterator<Issue> issueIter = index.getIndex().iterator(); issueIter.hasNext();) {
			final Issue issue = issueIter.next();
			for (final Iterator<Project> jobIter = issue.getProjects().iterator(); jobIter.hasNext();) {
				final Project job = jobIter.next();
				final hudson.model.TopLevelItem item = Hudson.getInstance().getItem(job.getName());
				if (item != null && item instanceof AbstractProject<?, ?>) {
					final SortedMap<Integer, ?> builds = ((AbstractProject<?, ?>) item).getBuildsAsMap();
					for (final Iterator<Integer> buildIter = job.getBuildNumbers().iterator(); buildIter.hasNext();) {
						if (!builds.containsKey(buildIter.next())) {
							buildIter.remove();
						}
					}
					if (job.getBuildNumbers().isEmpty()) {
						jobIter.remove();
					}
				} else {
					jobIter.remove();
				}
			}
			if (issue.getProjects().isEmpty()) {
				issueIter.remove();
			}
		}
		synchronized (this) {
			save();
		}
	}

	/**
	 * Index all the Jira issue to build relations
	 * 
	 * @throws IOException in case of write errors to the cache file
	 */
	public void fullIndex() throws IOException {
		final IssueIndex newIndex = new IssueIndex();
		LOGGER.log(Level.FINE, "Starting full scan");
		for (AbstractProject<?, ?> project : ProjectUtils.getAllProjects()) {
			final String jobName = project.getName();
			LOGGER.log(Level.FINE, " - Processing job: " + jobName);
			for (AbstractBuild<?, ?> build : project.getBuilds()) {
				final Integer buildNumber = Integer.valueOf(build.getNumber());
				LOGGER.log(Level.FINE, "    - Processing job build: " + buildNumber);
				final List<String> keys = findBuildRelatedIssues(build);
				for (String key : keys) {
					newIndex.addIssue(key, jobName, buildNumber);
				}
			}
		}
		synchronized (index) {
			this.index.setIndex(newIndex.getIndex());
			save();
		}
	}

	/**
	 * Index the Jira issue to build relations for a given build
	 * 
	 * @param build the build to index
	 * @throws IOException in case of write errors to the cache file
	 */
	public void indexBuild(AbstractBuild<?, ?> build) throws IOException {
		final String jobName = build.getProject().getName();
		final Integer buildNumber = Integer.valueOf(build.getNumber());
		final List<String> keys = findBuildRelatedIssues(build);
		synchronized (index) {
			for (String key : keys) {
				index.addIssue(key, jobName, buildNumber);
			}
			save();
		}
	}

	/**
	 * Find all related Jira Issue keys in the changeset of the given Build
	 * 
	 * @param build the Build to find the related Jira Issue keys in
	 * @return the {@link List} of Jira issue keys
	 */
	private List<String> findBuildRelatedIssues(AbstractBuild<?, ?> build) {
		final List<String> keys = new ArrayList<String>();
		final JiraProjectKeyJobProperty jobProperty =
			ProjectUtils.getJiraProjectKeyPropertyOfProject(build.getProject());
		if (jobProperty != null) {
			final Pattern issueKeyPattern = jobProperty.getIssueKeyPattern();
			for (Entry entry : build.getChangeSet()) {
				if (entry != null && StringUtils.isNotBlank(entry.getMsg())) {
					keys.addAll(JiraKeyUtils.getJiraIssueKeysFromText(entry.getMsg(), issueKeyPattern));
				}
			}
		} else {
			LOGGER.warning("No JiraProjectKeyJobProperty is configured for Job '" + build.getProject().getName()
				+ "'.");
		}
		return keys;
	}

	static {
		xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(IssueIndex.class);
		xstream.processAnnotations(Issue.class);
		xstream.processAnnotations(Project.class);
	}

}
