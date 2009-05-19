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

import java.io.IOException;
import java.io.PrintStream;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.plugins.jiraapi.index.IssueIndexer;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

/**
 * Jira Issue Indexer {@link Publisher}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
//@Extension
public class JiraIssueIndexerRecorder extends Recorder {

	private final transient IssueIndexer indexer;

	/**
	 * Constructor
	 */
	@DataBoundConstructor
	public JiraIssueIndexerRecorder() {
		indexer = IssueIndexer.getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
					throws InterruptedException, IOException {
		final PrintStream logger = listener.getLogger();
		logger.println("Started indexing related Jira issue keys");
		try {
			indexer.indexBuild(build);
			logger.println("Successfully indexed related Jira issue keys");
		} catch (IOException e) {
			logger.println("Failed to index Jira issues related to this build");
		}
		logger.println("Finished indexing related Jira issue keys");
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public BuildStepDescriptor<Publisher> getDescriptor() {
		return JiraIssueIndexerRecorderDescriptor.DESCRIPTOR;
	}

}
