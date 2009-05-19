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
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.marvelution.jira.plugins.hudson.ApiVersion;
import com.marvelution.jira.plugins.hudson.model.Jobs;
import com.marvelution.jira.plugins.hudson.xstream.XStreamMarshaller;

import hudson.Plugin;
import hudson.model.Hudson;
import hudson.model.JobPropertyDescriptor;
import hudson.plugins.jiraapi.api.JiraApi;
import hudson.plugins.jiraapi.index.IssueIndexer;
import hudson.tasks.BuildStep;
import hudson.triggers.Trigger;

/**
 * Main {@link Plugin} implementation for the Jira Project Key plugin
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class PluginImpl extends Plugin {

	private static final Logger LOGGER = Logger.getLogger(PluginImpl.class.getName());

	private static final String DEFAULT_CACHE_FILENAME = "jira-issue-index.xml";

	private static final long DEFAULT_THREAD_DELAY = 3600000L;

	private static final long START_THREAD_DELAY = 10000L;

	private final transient JiraApi jiraApi = new JiraApi();

	private transient IssueIndexer indexer;

	private String indexFilename;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws Exception {
		load();
		indexer.validateIssueIndex();
		Trigger.timer.schedule(new IssueIndexerThread(), START_THREAD_DELAY, DEFAULT_THREAD_DELAY);
		// Adding the JiraIssueIndexerRecorder here is needed so that the Hudson Web UI can check/uncheck the
		// configuration of the recorder in the Job Configuration page. Using the @Extension annotation disables this
		// functionality
		BuildStep.PUBLISHERS.add(JiraIssueIndexerRecorderDescriptor.DESCRIPTOR);
		JobPropertyDescriptor.all().add(JiraProjectKeyJobProperty.DESCRIPTOR);
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
		writeXmlToResponse(request, response, XStreamMarshaller.marshal(ApiVersion.getVersion()));
	}

	/**
	 * Handler for getJobs requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetJobs(final StaplerRequest request, final StaplerResponse response) throws IOException,
			ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		final Jobs jobs = jiraApi.getAllJiraProjects();
		writeXmlToResponse(request, response, XStreamMarshaller.marshal(jobs));
	}

	/**
	 * Handler for getBuilds requests
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @throws IOException in case of IO Exceptions
	 * @throws ServletException in case of Servlet Exceptions
	 */
	public void doGetBuilds(final StaplerRequest request, final StaplerResponse response) throws IOException,
			ServletException {
		Hudson.getInstance().checkPermission(Hudson.READ);
		String xml = "";
		if (StringUtils.isNotEmpty(request.getParameter("projectKey"))
			&& StringUtils.isNotEmpty(request.getParameter("projectVersion"))) {
			xml = XStreamMarshaller.marshal(jiraApi.getBuildsByJiraVersion(request.getParameter("projectKey"), Long
					.valueOf(request.getParameter("startDate")), Long.valueOf(request.getParameter("releaseDate"))));
		} else if (StringUtils.isNotEmpty(request.getParameter("projectKey"))) {
			xml = XStreamMarshaller.marshal(jiraApi.getBuildsByJiraProject(request.getParameter("projectKey")));
		} else if (StringUtils.isNotEmpty(request.getParameter("issueKeys"))) {
			final String[] issueKeys = request.getParameter("issueKeys").split(",");
			xml = XStreamMarshaller.marshal(jiraApi.getBuildsByJiraIssueKeys(issueKeys));
		} else {
			throw new ServletException("Invalid request");
		}
		writeXmlToResponse(request, response, xml);
	}

	/**
	 * Write the response XML to {@link StaplerResponse}
	 * 
	 * @param request the {@link StaplerRequest}
	 * @param response the {@link StaplerResponse}
	 * @param xml the XML {@link String} to write
	 * @throws ServletException in case of failures when writing the XML to the {@link StaplerResponse}
	 */
	private void writeXmlToResponse(final StaplerRequest request, final StaplerResponse response, final String xml)
			throws ServletException {
		response.setHeader("Content-type", "application/xml");
		Writer writer = null;
		try {
			writer = response.getCompressedWriter(request);
			writer.write(xml);
			writer.flush();
		} catch (IOException e) {
			throw new ServletException("Failed to write XML to response. Reason: " + e.getMessage(), e);
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
