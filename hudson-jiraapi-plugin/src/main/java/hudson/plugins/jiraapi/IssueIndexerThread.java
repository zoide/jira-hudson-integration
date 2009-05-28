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
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.PeriodicWork;
import hudson.plugins.jiraapi.index.IssueIndexer;

/**
 * {@link PeriodicWork} implementation to update the Jira issue index
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Extension
public class IssueIndexerThread extends PeriodicWork {

	public static final long DEFAULT_THREAD_DELAY = 3600000L;

	public static final long START_THREAD_DELAY = 10000L;

	private static final Logger LOGGER = Logger.getLogger(IssueIndexerThread.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getInitialDelay() {
		return START_THREAD_DELAY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getRecurrencePeriod() {
		return DEFAULT_THREAD_DELAY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRun() {
		LOGGER.log(Level.INFO, "Starting periodical Full Jira issue indexing task");
		final IssueIndexer indexer = IssueIndexer.getInstance();
		try {
			indexer.fullIndex();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to update the issue index. Reason: " + e.getMessage());
		}
		LOGGER.log(Level.INFO, "Finished periodical Full Jira issue indexing task");
	}

}
