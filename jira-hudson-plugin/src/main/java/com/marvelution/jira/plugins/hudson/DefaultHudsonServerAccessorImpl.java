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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.BuildsList;
import com.marvelution.jira.plugins.hudson.api.model.HudsonServerAware;
import com.marvelution.jira.plugins.hudson.api.model.HudsonView;
import com.marvelution.jira.plugins.hudson.api.model.HudsonViewsList;
import com.marvelution.jira.plugins.hudson.api.model.Job;
import com.marvelution.jira.plugins.hudson.api.model.JobsList;
import com.marvelution.jira.plugins.hudson.api.xstream.XStreamMarshaller;
import com.marvelution.jira.plugins.hudson.api.xstream.XStreamMarshallerException;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Implementation of the {@link HudsonServerAccessor} interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerAccessorImpl implements HudsonServerAccessor {

	private static final Logger LOGGER = Logger.getLogger(DefaultHudsonServerAccessorImpl.class);

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
	public void getCrumb(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		try {
			final GetMethod method =
				createGetMethod(hudsonServer, constructHudsonActionURL(hudsonServer, GET_CRUMB_ACTION).toString());
			getHudsonServerActionResponse(hudsonServer, method);
			if (method.getResponseHeader("Crumb") != null && method.getResponseHeader("Crumb-Field") != null) {
				hudsonServer.setCrumb(method.getResponseHeader("Crumb").getValue());
				hudsonServer.setCrumbField(method.getResponseHeader("Crumb-Field").getValue());
			}
		} catch (MalformedURLException e) {
			LOGGER.error("Failed to get Crumb information form Hudson Server: " + hudsonServer.getName(), e);
			throw new HudsonServerAccessorException("Failed to get Crumb information form Hudson Server: "
				+ hudsonServer.getName(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ApiImplementation getApiImplementation(HudsonServer hudsonServer)
					throws HudsonServerAccessorException, HudsonServerAccessDeniedException {
		final String response = getHudsonServerActionResponse(hudsonServer, GET_API_VERSION_ACTION, null);
		try {
			final ApiImplementation api = XStreamMarshaller.unmarshal(response, ApiImplementation.class);
			return api;
		} catch (XStreamMarshallerException e) {
			LOGGER.error("Failed to unmarshal the Hudson server response to a API Implementation object. Reason: "
				+ e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a API Implementation object. Reason: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Job> getProjectsList(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		final String response = getHudsonServerActionResponse(hudsonServer, LIST_ALL_PROJECTS_ACTION, null);
		try {
			final JobsList jobs = XStreamMarshaller.unmarshal(response, JobsList.class);
			return jobs.getJobs();
		} catch (XStreamMarshallerException e) {
			LOGGER.error(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: " + e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Job> getProjects(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		final String response = getHudsonServerActionResponse(hudsonServer, GET_ALL_PROJECTS_ACTION, null);
		try {
			final JobsList jobs = XStreamMarshaller.unmarshal(response, JobsList.class);
			return jobs.getJobs();
		} catch (XStreamMarshallerException e) {
			LOGGER.error(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: " + e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Job getProject(Project project) throws HudsonServerAccessorException, HudsonServerAccessDeniedException {
		return getProject(serverManager.getServerByJiraProject(project), project);
	}

	/**
	 * {@inheritDoc}
	 */
	public Job getProject(HudsonServer hudsonServer, Project project) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("projectKey", project.getKey());
		final String response = getHudsonServerActionResponse(hudsonServer, GET_PROJECT_ACTION, params);
		try {
			return XStreamMarshaller.unmarshal(response, Job.class);
		} catch (XStreamMarshallerException e) {
			LOGGER.error("Failed to unmarshal the Hudson server response to a Job object. Reason: " + e.getMessage(),
				e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Job object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(Project project) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		return getBuilds(serverManager.getServerByJiraProject(project), project);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Project project) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("projectKey", project.getKey());
		final String response = getHudsonServerActionResponse(hudsonServer, GET_PROJECT_BUILDS_ACTION, params);
		try {
			final BuildsList builds = XStreamMarshaller.unmarshal(response, BuildsList.class);
			associateHudsonServer(builds.getBuilds(), hudsonServer);
			return builds.getBuilds();
		} catch (XStreamMarshallerException e) {
			LOGGER.error("Failed to unmarshal the Hudson server response to a Builds object. Reason: "
				+ e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(Version version) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		return getBuilds(serverManager.getServerByJiraProject(version.getProjectObject()), version);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Version version) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
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
			LOGGER.error("Failed to unmarshal the Hudson server response to a Builds object. Reason: "
				+ e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(Collection<String> issueKeys) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		final List<Build> builds = new ArrayList<Build>();
		for (HudsonServer server : serverManager.getServers()) {
			builds.addAll(getBuilds(server, issueKeys));
		}
		return builds;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Build> getBuilds(HudsonServer hudsonServer, Collection<String> issueKeys)
					throws HudsonServerAccessorException, HudsonServerAccessDeniedException {
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
			LOGGER.error("Failed to unmarshal the Hudson server response to a Builds object. Reason: "
				+ e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<HudsonView> getViewsList(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		final String response = getHudsonServerActionResponse(hudsonServer, LIST_ALL_VIEWS_ACTION, null);
		try {
			final HudsonViewsList viewsList = XStreamMarshaller.unmarshal(response, HudsonViewsList.class);
			return viewsList.getViews();
		} catch (XStreamMarshallerException e) {
			LOGGER.error("Failed to unmarshal the Hudson server response to a Views object. Reason: "
				+ e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Views object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public HudsonView getView(HudsonServer hudsonServer, String name) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("viewName", name);
		final String response = getHudsonServerActionResponse(hudsonServer, GET_VIEW_ACTION, params);
		try {
			return XStreamMarshaller.unmarshal(response, HudsonView.class);
		} catch (XStreamMarshallerException e) {
			LOGGER.error("Failed to unmarshal the Hudson server response to a Views object. Reason: "
				+ e.getMessage(), e);
			throw new HudsonServerAccessorException(
				"Failed to unmarshal the Hudson server response to a Views object. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Gets the Remote API response from the {@link HudsonServer}
	 * 
	 * @param hudsonServer the {@link HudsonServer} to execute the action on
	 * @param action the action url to execute
	 * @param params extra parameters to add to the action
	 * @return the action response from the {@link HudsonServer}
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson Server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access
	 */
	public String getHudsonServerActionResponse(HudsonServer hudsonServer, String action, Map<String, String> params)
					throws HudsonServerAccessorException, HudsonServerAccessDeniedException {
		if (hudsonServer == null) {
			throw new HudsonServerAccessorException("hudsonServer may not be null");
		}
		try {
			final URL actionUrl = constructHudsonActionURL(hudsonServer, action);
			final PostMethod actionMethod = createPostMethod(hudsonServer, actionUrl.toString());
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					actionMethod.addParameter(entry.getKey(), entry.getValue());
				}
			}
			return getHudsonServerActionResponse(hudsonServer, actionMethod);
		} catch (MalformedURLException e) {
			LOGGER.error("Failed to construct the Hudson Server Action URL", e);
			throw new HudsonServerAccessorException("Failed to construct the Hudson Server Action URL", e);
		}
	}

	/**
	 * Gets the Remote API response from the {@link HudsonServer}
	 * 
	 * @param hudsonServer the {@link HudsonServer} to execute the action on
	 * @param actionMethod the {@link HttpMethod} to execute
	 * @return the action response from the {@link HudsonServer}
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson Server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access
	 */
	public String getHudsonServerActionResponse(HudsonServer hudsonServer, HttpMethod actionMethod)
					throws HudsonServerAccessorException, HudsonServerAccessDeniedException {
		if (hudsonServer == null) {
			throw new HudsonServerAccessorException("hudsonServer may not be null");
		}
		String response = "";
		if (hudsonServer.isSecuredHudsonServer()) {
			try {
				String loginAction = constructHudsonActionURL(hudsonServer, "/j_acegi_security_check").toString();
				while (true) {
					final PostMethod loginMethod = createPostMethod(hudsonServer, loginAction);
					loginMethod.addParameter("j_username", hudsonServer.getUsername());
					loginMethod.addParameter("j_password", hudsonServer.getPassword());
					loginMethod.addParameter("action", "login");
					getHttpClient().executeMethod(loginMethod);
					if (loginMethod.getStatusCode() / 100 != 3) {
						loginMethod.releaseConnection();
						break;
					}
					loginAction = loginMethod.getResponseHeader("Location").getValue();
				}
				getHttpClient().executeMethod(actionMethod);
				if (actionMethod.getStatusCode() == HttpStatus.SC_OK) {
					response = getResponseBodyAsString(actionMethod);
				} else {
					handleNotOKResponse(actionMethod);
				}
			} catch (MalformedURLException e) {
				LOGGER.error("Invalid Hduson action URL specified", e);
				throw new HudsonServerAccessorException("Invalid Hduson action URL specified", e);
			} catch (IOException e) {
				LOGGER.error("Failed to connect to the Hudson Server", e);
				throw new HudsonServerAccessorException("Failed to connect to the Hudson Server", e);
			} finally {
				if (actionMethod != null) {
					actionMethod.releaseConnection();
				}
				GetMethod logoutMethod = null;
				try {
					final String logoutAction = constructHudsonActionURL(hudsonServer, "/logout").toString();
					logoutMethod = createGetMethod(hudsonServer, logoutAction);
					getHttpClient().executeMethod(logoutMethod);
				} catch (Exception e) {
					/* INGORE */
				} finally {
					if (logoutMethod != null) {
						logoutMethod.releaseConnection();
					}
				}
			}
		} else {
			try {
				getHttpClient().executeMethod(actionMethod);
				if (actionMethod.getStatusCode() == HttpStatus.SC_OK) {
					response = getResponseBodyAsString(actionMethod);
				} else {
					handleNotOKResponse(actionMethod);
				}
			} catch (IOException e) {
				LOGGER.error("Failed to connect to the Hudson Server", e);
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
	 * Get the {@link HttpClient}
	 * 
	 * @return the {@link HttpClient}
	 */
	protected HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * Create {@link PostMethod}
	 *
	 * @param server the {@link HudsonServer}
	 * @param url the URL of the {@link PostMethod}
	 * @return the {@link PostMethod}
	 */
	protected PostMethod createPostMethod(HudsonServer server, String url) {
		final PostMethod method = new PostMethod(url);
		addDefaultHeaders(server, method);
		return method;
	}

	/**
	 * Create {@link GetMethod}
	 *
	 * @param server the {@link HudsonServer}
	 * @param url the URL of the {@link GetMethod}
	 * @return the {@link GetMethod}
	 */
	protected GetMethod createGetMethod(HudsonServer server, String url) {
		final GetMethod method = new GetMethod(url);
		addDefaultHeaders(server, method);
		return method;
	}

	/**
	 * Add the default headers of the {@link HttpMethod}
	 * 
	 * @param server the {@link HudsonServer} containing the Crumb implementation configuration details
	 * @param method the {@link HttpMethod} to add the Crumb parameter to
	 */
	protected void addDefaultHeaders(HudsonServer server, HttpMethod method) {
		method.addRequestHeader(new Header("User-Agent", "Jira Hudson Integration Client/1.0"));
		if (StringUtils.isNotEmpty(server.getCrumb()) && StringUtils.isNotEmpty(server.getCrumbField())) {
			method.addRequestHeader(new Header(server.getCrumbField(), server.getCrumb()));
		}
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

	/**
	 * Get the {@link HttpMethod} response body as a {@link String} Fix for issue: MARVJIRAHUDSON-16
	 * 
	 * @param method the {@link HttpMethod} to get the response body from
	 * @return the response body as a String
	 * @throws IOException in case of exceptions
	 */
	private String getResponseBodyAsString(HttpMethod method) throws IOException {
		final InputStream inputStream = method.getResponseBodyAsStream();
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
		} finally {
			inputStream.close();
		}
		return stringBuilder.toString();
	}

	/**
	 * Handle responses that don't return a 200 status code
	 * 
	 * @param method the {@link HttpMethod} to handle the response for
	 * @throws HudsonServerAccessDeniedException in case of unrecoverable errors
	 * @throws IOException in case the response body cannot be read
	 */
	private void handleNotOKResponse(HttpMethod method) throws HudsonServerAccessDeniedException, IOException {
		switch (method.getStatusCode()) {
			case HttpStatus.SC_FORBIDDEN:
				throw new HudsonServerAccessDeniedException("Hudson denied access to the plugin API.");
			default:
				break;
		}
	}

}
