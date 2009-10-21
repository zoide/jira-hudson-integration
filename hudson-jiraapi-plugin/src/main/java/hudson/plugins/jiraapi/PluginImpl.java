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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.api.model.JobsList;
import com.marvelution.jira.plugins.hudson.api.xstream.XStreamMarshaller;
import com.marvelution.jira.plugins.hudson.api.xstream.XStreamMarshallerException;

import hudson.Plugin;
import hudson.model.Hudson;
import hudson.plugins.jiraapi.api.ApiImpl;
import hudson.plugins.jiraapi.index.IssueIndexer;
import hudson.security.csrf.CrumbIssuer;

/**
 * Main {@link Plugin} implementation for the Jira Project Key plugin
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @plugin
 */
public class PluginImpl extends Plugin {

	private static final Logger LOGGER = Logger.getLogger(PluginImpl.class.getName());

	private static final String DEFAULT_CACHE_FILENAME = "jira-issue-index.xml";

	private final transient ApiImpl apiImpl = new ApiImpl();

	private transient IssueIndexer indexer;

	private String indexFilename;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws Exception {
		load();
		indexer.validateIssueIndex();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void load() throws IOException {
		super.load();
		indexer = IssueIndexer.getInstance();
		indexer.setIndexFile(getIndexFile());
		if (getIndexFile().exists()) {
			indexer.load();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() throws Exception {
		indexer.save();
		save();
		super.stop();
	}


	/**
	 * Handler for getCrumb requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetCrumb(final StaplerRequest request, final StaplerResponse response) throws IOException,
					ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		final String userAgent = "Jira Hudson Integration Client/1.0";
		if (userAgent.equals(request.getHeader("User-Agent")) && Hudson.getInstance().isUseCrumbs()) {
			final CrumbIssuer issuer = Hudson.getInstance().getCrumbIssuer();
			response.setHeader("Crumb-Field", issuer.getCrumbRequestField());
			response.setHeader("Crumb", issuer.getCrumb(request));
		}
	}

	/**
	 * Handler for getApiVersion requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetApiVersion(final StaplerRequest request, final StaplerResponse response) throws IOException,
			ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		try {
			writeToResponse(request, response, "application/xml", XStreamMarshaller.marshal(ApiImplementation
				.getApiImplementation()));
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for listAllProjects requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doListAllProjects(final StaplerRequest request, final StaplerResponse response) throws IOException,
			ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting list of all projects");
		final JobsList jobs = apiImpl.listAllProjects();
		try {
			writeToResponse(request, response, "application/xml", XStreamMarshaller.marshal(jobs));
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for getAllProjects requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetAllProjects(final StaplerRequest request, final StaplerResponse response) throws IOException,
			ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting all builds of all projects");
		final JobsList jobs = apiImpl.getAllProjects();
		try {
			writeToResponse(request, response, "application/xml", XStreamMarshaller.marshal(jobs));
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for getProject requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @param projectKey the Jira project key of the project to get
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetProject(final StaplerRequest request, final StaplerResponse response,
					@QueryParameter(value = "projectKey", required = true) String projectKey) throws IOException,
					ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting project: " + projectKey);
		try {
			final String xml = XStreamMarshaller.marshal(apiImpl.getProjectByJiraKey(projectKey));
			writeToResponse(request, response, "application/xml", xml);
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for getProjectBuilds requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @param projectKey the Jira project key to get the builds for
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetProjectBuilds(final StaplerRequest request, final StaplerResponse response,
					@QueryParameter(value = "projectKey", required = true) String projectKey) throws IOException,
					ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting all builds related to project: " + projectKey);
		try {
			final String xml = XStreamMarshaller.marshal(apiImpl.getBuildsByJiraProject(projectKey));
			writeToResponse(request, response, "application/xml", xml);
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for getVersionBuilds requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @param projectKey the Jira project key to get the builds for
	 * @param versionKey the Jira Version Id to get the builds for
	 * @param startDate the start date of the version
	 * @param releaseDate the release date of the version
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetVersionBuilds(final StaplerRequest request, final StaplerResponse response,
					@QueryParameter(value = "projectKey", required = true) String projectKey,
					@QueryParameter(value = "versionKey", required = true) String versionKey,
					@QueryParameter("startDate") long startDate,
					@QueryParameter("releaseDate") long releaseDate) throws IOException, ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting all builds related to project [" + projectKey + "] version: " + versionKey);
		try {
			final String xml =
				XStreamMarshaller.marshal(apiImpl.getBuildsByJiraVersion(projectKey, startDate, releaseDate));
			writeToResponse(request, response, "application/xml", xml);
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for getIssueBuilds requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @param issueKeys array of Jira issue keys
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetIssueBuilds(final StaplerRequest request, final StaplerResponse response,
					@QueryParameter(value = "issueKeys", required = true) String[] issueKeys) throws IOException,
					ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting all builds related to issue keys: " + Arrays.asList(issueKeys));
		try {
			final String xml = XStreamMarshaller.marshal(apiImpl.getBuildsByJiraIssueKeys(issueKeys));
			writeToResponse(request, response, "application/xml", xml);
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for List All Views request
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doListAllViews(final StaplerRequest request, final StaplerResponse response) throws IOException,
					ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting all Hudson Views.");
		try {
			final String xml = XStreamMarshaller.marshal(apiImpl.getAllViews());
			writeToResponse(request, response, "application/xml", xml);
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Handler for Get View request
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @param viewName the View name to get
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetView(final StaplerRequest request, final StaplerResponse response,
					@QueryParameter(value = "viewName", required = true) String viewName) throws IOException,
					ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		LOGGER.log(Level.FINE, "Getting Hudson View " + viewName);
		try {
			final String xml = XStreamMarshaller.marshal(apiImpl.getView(viewName));
			writeToResponse(request, response, "application/xml", xml);
		} catch (XStreamMarshallerException e) {
			LOGGER.log(Level.SEVERE, "Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to marshal response object to XML. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Write content to {@link StaplerResponse}
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @param contentType the Content-Type header value
	 * @param content the Content {@link String} to write
	 * @throws ServletException in case of failures when writing the XML to the {@link StaplerResponse}
	 */
	private void writeToResponse(final StaplerRequest request, final StaplerResponse response,
					final String contentType, final String content)
			throws ServletException {
		response.setHeader("Content-type", contentType);
		Writer writer = null;
		try {
			writer = response.getCompressedWriter(request);
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to write to response. Reason: " + e.getMessage(), e);
			throw new ServletException("Failed to write to response. Reason: " + e.getMessage(), e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				/* IGNORE */
			}
		}
	}

	/**
	 * Get the index filename
	 * 
	 * @return the index filename
	 */
	public String getIndexFilename() {
		if (indexFilename == null) {
			indexFilename = DEFAULT_CACHE_FILENAME;
		}
		return indexFilename;
	}

	/**
	 * Sets the index filename
	 * 
	 * @param indexFilename the index filename
	 */
	public void setIndexFilename(String indexFilename) {
		this.indexFilename = indexFilename;
	}

	/**
	 * Gets the index {@link File}
	 * 
	 * @return the index {@link File}
	 */
	public File getIndexFile() {
		return new File(getConfigXml().getFile().getParent(), getIndexFilename());
	}

}
