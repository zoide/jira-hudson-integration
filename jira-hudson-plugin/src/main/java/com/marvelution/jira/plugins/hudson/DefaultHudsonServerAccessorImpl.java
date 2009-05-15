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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import com.marvelution.jira.plugins.hudson.model.Builds;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.Jobs;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.xstream.XStreamMarshaller;

/**
 * Implementation of the {@link HudsonServerAccessor} interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerAccessorImpl implements HudsonServerAccessor {

	private static final int TIMEOUT_MS = 30000;

	private static final int MAX_TOTAL_CONNECTIONS = 40;

	private static final int MAX_HOST_CONNECTIONS = 4;

	private final HttpClient httpClient;

	/**
	 * Constructor
	 */
	public DefaultHudsonServerAccessorImpl() {
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
	public List<Job> getJobs(HudsonServer hudsonServer) throws HudsonServerAccessorException {
		final String response = getHudsonServerActionResponse(hudsonServer, GET_JOBS_ACTION, null);
		final Jobs jobs = XStreamMarshaller.unmarshal(response, Jobs.class);
		return jobs.getJobs();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Project project)
			throws HudsonServerAccessorException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("projectKey", project.getKey());
		final String response = getHudsonServerActionResponse(hudsonServer, GET_BUILDS_ACTION, params);
		final Builds builds = XStreamMarshaller.unmarshal(response, Builds.class);
		return builds.getBuilds();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Version version)
			throws HudsonServerAccessorException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("projectVersion", version.getName());
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
		final String response = getHudsonServerActionResponse(hudsonServer, GET_BUILDS_ACTION, params);
		final Builds builds = XStreamMarshaller.unmarshal(response, Builds.class);
		return builds.getBuilds();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Collection<String> issueKeys)
			throws HudsonServerAccessorException {
		final Map<String, String> params = new HashMap<String, String>();
		String issueKeysString = "";
		for (String issueKey : issueKeys) {
			issueKeysString += issueKey + ",";
		}
		params.put("issueKeys", issueKeysString);
		final String response = getHudsonServerActionResponse(hudsonServer, GET_BUILDS_ACTION, params);
		final Builds builds = XStreamMarshaller.unmarshal(response, Builds.class);
		return builds.getBuilds();
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

}
