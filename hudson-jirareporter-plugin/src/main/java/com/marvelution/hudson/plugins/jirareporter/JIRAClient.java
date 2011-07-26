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

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteIssueType;
import com.atlassian.jira.rpc.soap.client.RemoteNamedObject;
import com.atlassian.jira.rpc.soap.client.RemotePriority;
import com.atlassian.jira.rpc.soap.client.RemoteProject;
import com.marvelution.hudson.plugins.jirareporter.utils.IssueTextUtils;

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
	 * @return the Issue Key of the created issue
	 * @throws RemoteException in case of errors
	 */
	public String createIssue(AbstractBuild<?, ?> build, String projectKey, String issueType, String issuePriority)
			throws RemoteException {
		RemoteIssue newIssue = new RemoteIssue();
		newIssue.setProject(projectKey);
		newIssue.setType(issueType);
		newIssue.setPriority(issuePriority);
		newIssue.setSummary(IssueTextUtils.createIssueSummary(build, site));
		newIssue.setDescription(IssueTextUtils.createIssueDescription(build, site));
		newIssue.setReporter(site.username);
		newIssue.setEnvironment(IssueTextUtils.createIssueEnvironment(build, site));
		RemoteIssue raisedIssue = service.createIssue(token, newIssue);
		return raisedIssue.getKey();
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
		return getIssueAction(issue, site.getCloseAction()) != null;
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
		RemoteNamedObject action = getIssueAction(issue, site.getCloseAction());
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

}
