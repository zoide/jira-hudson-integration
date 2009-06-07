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

package com.marvelution.jira.plugins.hudson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.BuildsList;
import com.marvelution.jira.plugins.hudson.model.HudsonServerAware;
import com.marvelution.jira.plugins.hudson.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.JobsList;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.xstream.XStreamMarshaller;
import com.marvelution.jira.plugins.hudson.xstream.XStreamMarshallerException;

/**
 * Implementation of the {@link HudsonServerAccessor} interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerAccessorImpl implements HudsonServerAccessor {

	private static final int TIMEOUT_MS = 30000;

	private static final int MAX_TOTAL_CONNECTIONS = 40;

	private static final int MAX_HOST_CONNECTIONS = 4;

	private HudsonServerManager serverManager;

	private final HttpClient httpClient;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	public DefaultHudsonServerAccessorImpl(HudsonServerManager serverManager) {
		this.serverManager = serverManager;
		final HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
		connectionManagerParams.setConnectionTimeout(TIMEOUT_MS);
		connectionManagerParams.setDefaultMaxConnectionsPerHost(MAX_HOST_CONNECTIONS);
		connectionManagerParams.setMaxTotalConnections(MAX_TOTAL_CONNECTIONS);
		final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.setParams(connectionManagerParams);
		httpClient = new HttpClient(connectionManager);
	}

	/**
	 * {@inheritDoc}
	 */
	public ApiImplementation getApiImplementation(HudsonServer hudsonServer)
					throws HudsonServerAccessorException {
		final String response = getHudsonServerActionResponse(hudsonServer, GET_API_VERSION_ACTION, null);
		try {
			final com.marvelution.jira.plugins.hudson.model.ApiImplementation version =
				XStreamMarshaller.unmarshal(response,
					com.marvelution.jira.plugins.hudson.model.ApiImplementation.class);
			return version;
		} catch (XStreamMarshallerException e) {
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Version object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Job> getProjectsList(HudsonServer hudsonServer) throws HudsonServerAccessorException {
		final String response = getHudsonServerActionResponse(hudsonServer, LIST_ALL_PROJECTS_ACTION, null);
		try {
			final JobsList jobs = XStreamMarshaller.unmarshal(response, JobsList.class);
			return jobs.getJobs();
		} catch (XStreamMarshallerException e) {
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Job> getProjects(HudsonServer hudsonServer) throws HudsonServerAccessorException {
		final String response = getHudsonServerActionResponse(hudsonServer, GET_ALL_PROJECTS_ACTION, null);
		try {
			final JobsList jobs = XStreamMarshaller.unmarshal(response, JobsList.class);
			return jobs.getJobs();
		} catch (XStreamMarshallerException e) {
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Job getProject(Project project) throws HudsonServerAccessorException {
		return getProject(serverManager.getServerByJiraProject(project), project);
	}

	/**
	 * {@inheritDoc}
	 */
	public Job getProject(HudsonServer hudsonServer, Project project) throws HudsonServerAccessorException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("projectKey", project.getKey());
		final String response = getHudsonServerActionResponse(hudsonServer, GET_PROJECT_ACTION, params);
		try {
			return XStreamMarshaller.unmarshal(response, Job.class);
		} catch (XStreamMarshallerException e) {
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(Project project) throws HudsonServerAccessorException {
		return getBuilds(serverManager.getServerByJiraProject(project), project);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Project project)
			throws HudsonServerAccessorException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("projectKey", project.getKey());
		final String response = getHudsonServerActionResponse(hudsonServer, GET_PROJECT_BUILDS_ACTION, params);
		try {
			final BuildsList builds = XStreamMarshaller.unmarshal(response, BuildsList.class);
			associateHudsonServer(builds.getBuilds(), hudsonServer);
			return builds.getBuilds();
		} catch (XStreamMarshallerException e) {
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(Version version) throws HudsonServerAccessorException {
		return getBuilds(serverManager.getServerByJiraProject(version.getProjectObject()), version);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Version version)
			throws HudsonServerAccessorException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("projectKey", version.getProjectObject().getKey());
		params.put("versionKey", version.getName());
		if (version.isReleased()) {
			params.put("releaseDate", String.valueOf(version.getReleaseDate().getTime()));
		}
		long startDate = 0L;
		final List<?> versions = (List<?>) version.getProjectObject().getVersions();
		if (versions.size() > 1) {
			for (int i = (int) (version.getSequence().longValue() - 1); i >= 0; i--) {
				final Version prevVersion = (Version) versions.get(i);
				if (prevVersion.getSequence() < version.getSequence() && prevVersion.isReleased()) {
					startDate = prevVersion.getReleaseDate().getTime();
				}
			}
			params.put("startDate", String.valueOf(startDate));
		}
		final String response = getHudsonServerActionResponse(hudsonServer, GET_VERSION_BUILDS_ACTION, params);
		try {
			final BuildsList builds = XStreamMarshaller.unmarshal(response, BuildsList.class);
			associateHudsonServer(builds.getBuilds(), hudsonServer);
			return builds.getBuilds();
		} catch (XStreamMarshallerException e) {
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(List<String> issueKeys) throws HudsonServerAccessorException {
		final List<Build> builds = new ArrayList<Build>();
		for (HudsonServer server : serverManager.getServers()) {
			builds.addAll(getBuilds(server, issueKeys));
		}
		return builds;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, List<String> issueKeys)
			throws HudsonServerAccessorException {
		final Map<String, String> params = new HashMap<String, String>();
		String issueKeysString = "";
		for (String issueKey : issueKeys) {
			issueKeysString += issueKey + ",";
		}
		params.put("issueKeys", issueKeysString);
		final String response = getHudsonServerActionResponse(hudsonServer, GET_ISSUE_BUILDS_ACTION, params);
		try {
			final BuildsList builds = XStreamMarshaller.unmarshal(response, BuildsList.class);
			associateHudsonServer(builds.getBuilds(), hudsonServer);
			return builds.getBuilds();
		} catch (XStreamMarshallerException e) {
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Gets the Remote API response from the {@link HudsonServer}
	 * 
	 * @param hudsonServer the {@link HudsonServer} to execute the action on
	 * @param action the action url to execute
	 * @param params extra parameters to add to the action
	 * @return something
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson Server
	 */
	private String getHudsonServerActionResponse(HudsonServer hudsonServer, String action, Map<String, String> params)
			throws HudsonServerAccessorException {
		String response = "";
		URL actionUrl;
		PostMethod actionMethod = null;
		try {
			actionUrl = constructHudsonActionURL(hudsonServer, action);
			actionMethod = new PostMethod(actionUrl.toString());
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					actionMethod.addParameter(entry.getKey(), entry.getValue());
				}
			}
		} catch (MalformedURLException e) {
			throw new HudsonServerAccessorException("Failed to construct the Hudson Server Action URL", e);
		}
		if (hudsonServer.isSecuredHudsonServer()) {
			try {
				String loginAction = constructHudsonActionURL(hudsonServer, "/j_security_check").toString();
				while (true) {
					final PostMethod loginMethod = new PostMethod(loginAction);
					loginMethod.addParameter("j_username", hudsonServer.getUsername());
					loginMethod.addParameter("j_password", hudsonServer.getPassword());
					loginMethod.addParameter("action", "login");
					httpClient.executeMethod(loginMethod);
					if (loginMethod.getStatusCode() / 100 != 3) {
						loginMethod.releaseConnection();
						break;
					}
					loginAction = loginMethod.getResponseHeader("Location").getValue();
				}
				httpClient.executeMethod(actionMethod);
				if (actionMethod.getStatusCode() == HttpStatus.SC_OK) {
					response = actionMethod.getResponseBodyAsString();
				}
			} catch (MalformedURLException e) {
				throw new HudsonServerAccessorException("Invalid Hduson action URL specified", e);
			} catch (HttpException e) {
				throw new HudsonServerAccessorException("Failed to connect to the Hudson Server", e);
			} catch (IOException e) {
				throw new HudsonServerAccessorException("Failed to connect to the Hudson Server", e);
			} finally {
				if (actionMethod != null) {
					actionMethod.releaseConnection();
				}
				GetMethod logoutMethod = null;
				try {
					final String logoutAction = constructHudsonActionURL(hudsonServer, "/logout").toString();
					logoutMethod = new GetMethod(logoutAction);
					httpClient.executeMethod(logoutMethod);
				} catch (MalformedURLException e) {
					/* INGORE */
				} catch (HttpException e) {
					/* INGORE */
				} catch (IOException e) {
					/* INGORE */
				} finally {
					if (logoutMethod != null) {
						logoutMethod.releaseConnection();
					}
				}
			}
		} else {
			try {
				httpClient.executeMethod(actionMethod);
				if (actionMethod.getStatusCode() == HttpStatus.SC_OK) {
					response = actionMethod.getResponseBodyAsString();
				}
			} catch (HttpException e) {
				throw new HudsonServerAccessorException("Failed to connect to the Hudson Server", e);
			} catch (IOException e) {
				throw new HudsonServerAccessorException("Failed to connect to the Hudson Server", e);
			} finally {
				if (actionMethod != null) {
					actionMethod.releaseConnection();
				}
			}
		}
		return response;
	}

	/**
	 * Constructs the HudsonServer action {@link URL}
	 * 
	 * @param hudsonServer the {@link HudsonServer}
	 * @param action the action
	 * @return the action {@link URL}
	 * @throws MalformedURLException is case the URL is malformed
	 */
	private URL constructHudsonActionURL(HudsonServer hudsonServer, String action) throws MalformedURLException {
		return new URL(hudsonServer.getHost() + action);
	}

	/**
	 * Associate a {@link HudsonServer} with the given {@link List} of {@link HudsonServerAware} objects
	 * 
	 * @param objects the {@link List} of {@link HudsonServerAware} objects
	 * @param server the {@link HudsonServer}
	 */
	private void associateHudsonServer(List<?> objects, HudsonServer server) {
		for (Object object : objects) {
			if (object instanceof HudsonServerAware) {
				((HudsonServerAware) object).setHudsonServerId(server.getServerId());
			}
		}
	}

}
