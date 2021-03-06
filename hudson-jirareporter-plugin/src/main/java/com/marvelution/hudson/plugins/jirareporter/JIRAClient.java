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

import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.Cause.UserCause;
import hudson.scm.ChangeLogSet;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteComment;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteIssueType;
import com.atlassian.jira.rpc.soap.client.RemoteNamedObject;
import com.atlassian.jira.rpc.soap.client.RemotePriority;
import com.atlassian.jira.rpc.soap.client.RemoteProject;
import com.atlassian.jira.rpc.soap.client.RemoteUser;
import com.marvelution.hudson.plugins.jirareporter.utils.IssueTextUtils;
import com.marvelution.hudson.plugins.jirareporter.utils.IssueTextUtils.Type;

/**
 * A SOAP Client for a {@link JIRASite}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JIRAClient {

	private static final Logger LOGGER = Logger.getLogger(JIRAClient.class.getName());

	private final JiraSoapService service;
	private final String token;
	private final JIRASite site;

	/**
	 * Private Constructor
	 * 
	 * @param service the {@link JiraSoapService} implementation
	 * @param token the security token
	 * @param site the {@link JIRASite} configuration
	 */
	JIRAClient(JiraSoapService service, String token, JIRASite site) {
		this.service = service;
		this.token = token;
		this.site = site;
	}

	/**
	 * Logout from the current session
	 */
	public void logout() {
		try {
			service.logout(token);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Failed to logout from JIRA: " + e.getMessage());
		}
	}

	/**
	 * Get all the projects on the {@link JIRASite}
	 * 
	 * @return {@link RemoteProject} array
	 * @throws RemoteException in case of errors
	 */
	public RemoteProject[] getProjects() throws RemoteException {
		return service.getProjectsNoSchemes(token);
	}

	/**
	 * Raise an Issue on the {@link JIRASite} for the given failed {@link AbstractBuild}
	 * 
	 * @param build the {@link AbstractBuild} that failed
	 * @param projectKey the key of the project to raise the issue in
	 * @param issueType the type of the issue to raise
	 * @param issuePriority the issue priority
	 * @param assignToBuildBreaker flag to assign the issue to the build breaker
	 * @return the Issue Key of the created issue
	 * @throws RemoteException in case of errors
	 */
	public String createIssue(AbstractBuild<?, ?> build, String projectKey, String issueType, String issuePriority,
					boolean assignToBuildBreaker) throws RemoteException {
		RemoteIssue newIssue = new RemoteIssue();
		newIssue.setProject(projectKey);
		newIssue.setType(issueType);
		newIssue.setPriority(issuePriority);
		newIssue.setSummary(IssueTextUtils.createFieldText(Type.SUMMARY, build, site));
		newIssue.setDescription(IssueTextUtils.createFieldText(Type.DESCRIPTION, build, site));
		newIssue.setEnvironment(IssueTextUtils.createFieldText(Type.ENVIRONMENT, build, site));
		newIssue.setReporter(site.username);
		newIssue.setAssignee(getFirstBuildBreakerFromBuild(build));
		RemoteIssue raisedIssue = service.createIssue(token, newIssue);
		return raisedIssue.getKey();
	}

	/**
	 * Update an Issue by adding a Comment to it
	 * 
	 * @param build the {@link AbstractBuild} with build data
	 * @param issueKey the key of the issue to update
	 * @return the updated issue key
	 * @throws RemoteException in case of errors
	 */
	public String updateIssue(AbstractBuild<?, ?> build, String issueKey) throws RemoteException {
		RemoteComment comment = new RemoteComment();
		comment.setAuthor(site.username);
		comment.setBody(IssueTextUtils.createFieldText(Type.DESCRIPTION, build, site));
		service.addComment(token, issueKey, comment);
		return issueKey;
	}

	/**
	 * Get {@link RemoteIssue} objects via the JQL Search
	 * 
	 * @param jqlQuery the JQL Query to execute
	 * @param maxResutls the maximum number of results
	 * @return the {@link RemoteIssue} array
	 * @throws RemoteException in case of errors
	 */
	public RemoteIssue[] getIssuesFromJqlSearch(String jqlQuery, int maxResutls) throws RemoteException {
		return service.getIssuesFromJqlSearch(token, jqlQuery, maxResutls);
	}

	/**
	 * Get the {@link RemoteIssue} object for the given {@link JIRABuildResultReportAction}
	 * 
	 * @param action the {@link JIRABuildResultReportAction} action to get the Issue for
	 * @return the {@link RemoteIssue}
	 * @throws RemoteException in case of errors
	 */
	public RemoteIssue getIssue(JIRABuildResultReportAction action) throws RemoteException {
		return getIssue(action.raisedIssueKey);
	}

	/**
	 * Get the {@link RemoteIssue} object for the given Issue Key
	 * 
	 * @param issueKey the Issue Key to get
	 * @return the {@link RemoteIssue}
	 * @throws RemoteException in case of errors
	 */
	public RemoteIssue getIssue(String issueKey) throws RemoteException {
		return service.getIssue(token, issueKey);
	}

	/**
	 * Check if the {@link JIRASite#closeAction} is available for the given {@link RemoteIssue}
	 * 
	 * @param issue the {@link RemoteIssue} to check
	 * @return <code>true</code> is the {@link JIRASite#closeAction} is available, <code>false</code> otherwise
	 * @throws RemoteException in case of errors
	 */
	public boolean canCloseIssue(RemoteIssue issue) throws RemoteException {
		return getIssueAction(issue, site.getCloseActionName()) != null;
	}

	/**
	 * Getter for a specific Issue action
	 * 
	 * @param issue the {@link RemoteIssue} to get the action for
	 * @param actionName the action name to get
	 * @return the {@link RemoteNamedObject} with the action name and Id, may be <code>null</code> is the
	 * 			action is not available for the given issue
	 * @throws RemoteException in case of errors
	 */
	public RemoteNamedObject getIssueAction(RemoteIssue issue, String actionName) throws RemoteException {
		for (RemoteNamedObject action : service.getAvailableActions(token, issue.getKey())) {
			if (actionName.equals(action.getName())) {
				return action;
			}
		}
		return null;
	}

	/**
	 * Trigger the {@link JIRASite#closeAction} on the given {@link RemoteIssue}
	 * 
	 * @param issue the {@link RemoteIssue} to trigger the action on
	 * @param build the {@link AbstractBuild} that resulted in this trigger
	 * @return <code>true</code> is successful, <code>false</code> otherwise
	 * @throws RemoteException in case of errors
	 */
	public boolean closeIssue(RemoteIssue issue, AbstractBuild<?, ?> build) throws RemoteException {
		RemoteNamedObject action = getIssueAction(issue, site.getCloseActionName());
		if (action != null) {
			// TODO Get the Action fields?
			service.progressWorkflowAction(token, issue.getKey(), action.getId(), null);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the available project Issue Types
	 * 
	 * @param projectKey the key of the JIRA project
	 * @return array of {@link RemoteIssueType} objects
	 * @throws RemoteException in case of errors
	 */
	public RemoteIssueType[] getIssueTypesForProject(String projectKey) throws RemoteException {
		RemoteProject project = service.getProjectByKey(token, projectKey);
		return service.getIssueTypesForProject(token, project.getId());
	}

	/**
	 * Get all the Priorities on the JIRA Site
	 * 
	 * @return {@link Map} of Priorities (type id as key and name as value)
	 * @throws RemoteException in case of errors
	 */
	public Map<String, String> getPriorities() throws RemoteException {
		if (site.priorities == null) {
			Map<String, String> prios = new HashMap<String, String>();
			for (RemotePriority priority : service.getPriorities(token)) {
				prios.put(priority.getId(), priority.getName());
			}
			site.priorities = Collections.unmodifiableMap(prios);
		}
		return site.priorities;
	}

	/**
	 * Get the {@link RemoteUser} for the username given
	 * 
	 * @param name the name of the user to get
	 * @return the {@link RemoteUser}
	 * @throws RemoteException in case of errors
	 */
	public RemoteUser getUser(String name) throws RemoteException {
		LOGGER.log(Level.FINE, "Checking if user " + name + " exists on JIRA Site " + site.url);
		return service.getUser(token, name);
	}

	/**
	 * Helper method to get the first Build Breaker that also has a valid account on the JIRA site
	 * 
	 * @param build the Build to get the breaker from
	 * @return the first valid account name of the build breaker, may be <code>null</code>
	 */
	private String getFirstBuildBreakerFromBuild(AbstractBuild<?, ?> build) {
		try {
			for (Cause cause : build.getCauses()) {
				if (cause instanceof UserCause) {
					UserCause userCause = (UserCause) cause;
					RemoteUser user = getUser(userCause.getUserName());
					if (user != null) {
						return user.getName();
					}
				}
			}
			// Still here? Then loop through the changelog
			for (ChangeLogSet.Entry entry : build.getChangeSet()) {
				RemoteUser user = getUser(entry.getAuthor().getId());
				if (user != null) {
					return user.getName();
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to get a valid first build breaker user: " + e.getMessage(), e);
		}
		return null;
	}

}
