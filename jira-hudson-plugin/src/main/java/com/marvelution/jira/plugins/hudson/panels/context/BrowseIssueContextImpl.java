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

package com.marvelution.jira.plugins.hudson.panels.context;

import java.util.Map;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.query.Query;
import com.opensymphony.user.User;

/**
 * {@link BrowseIssueContext} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BrowseIssueContextImpl implements BrowseIssueContext {

	private final User user;
	private final Issue issue;

	/**
	 * Constructor
	 * 
	 * @param issue the {@link Issue} of the context
	 * @param user the {@link User} of the context
	 */
	public BrowseIssueContextImpl(Issue issue, User user) {
		this.issue = issue;
		this.user = user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject() {
		return issue.getProjectObject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUser() {
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query createQuery() {
		throw new UnsupportedOperationException("Query is not supported by teh BrowseIssueContext");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getQueryString() {
		throw new UnsupportedOperationException("Query is not supported by teh BrowseIssueContext");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> createParameterMap() {
		throw new UnsupportedOperationException("Query is not supported by teh BrowseIssueContext");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContextKey() {
		throw new UnsupportedOperationException("Query is not supported by teh BrowseIssueContext");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Issue getIssue() {
		return issue;
	}

}
