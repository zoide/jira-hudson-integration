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

package com.marvelution.jira.plugins.hudson.gadgets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.rest.v1.model.errors.ErrorCollection;
import com.atlassian.jira.rest.v1.model.errors.ValidationError;
import com.marvelution.jira.plugins.hudson.api.model.JobsList;
import com.marvelution.jira.plugins.hudson.api.xstream.XStreamMarshaller;
import com.marvelution.jira.plugins.hudson.chart.ChartUtils;
import com.marvelution.jira.plugins.hudson.gadgets.HudsonChartsGadgetResource.HudsonChartsResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonChartResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonProjectResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonServerResource;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Testcase for {@link HudsonChartsGadgetResource}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonChartsGadgetResourceTest {

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private HudsonServerAccessor serverAccessor;

	@Mock
	private HudsonServer server;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private Project project;

	private HudsonChartsGadgetResource gadgetResource;

	/**
	 * Setup test variables
	 * 
	 * @throws Exception in case of exceptions
	 */
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		gadgetResource = new HudsonChartsGadgetResource(serverManager, serverAccessor, projectManager);
		when(serverManager.getServerByJiraProject(project)).thenReturn(server);
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#validate(String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidate() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(projectManager.getProjectObj(1L)).thenReturn(project);
		final Response response = gadgetResource.validate("project-1");
		assertThat(response.getEntity(), nullValue());
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObj(1L);
		
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#validate(String)} Hudson not configured
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateHudsonNotConfigured() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(false);
		final Response response = gadgetResource.validate("project-1");
		assertThat(response.getEntity(), is(ErrorCollection.class));
		final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
		assertThat(errorCollection.getErrorMessages().isEmpty(), is(true));
		final List<ValidationError> errors = (List<ValidationError>) errorCollection.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.get(0).getField(), is("projectId"));
		assertThat(errors.get(0).getError(), is("hudson.error.not.configured"));
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#validate(String)} invalid project
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateInvalidProject() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(projectManager.getProjectObj(1L)).thenReturn(null);
		final Response response = gadgetResource.validate("project-1");
		assertThat(response.getEntity(), is(ErrorCollection.class));
		final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
		assertThat(errorCollection.getErrorMessages().isEmpty(), is(true));
		final List<ValidationError> errors = (List<ValidationError>) errorCollection.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.get(0).getField(), is("projectId"));
		assertThat(errors.get(0).getError(), is("hudson.error.invalid.project.selected"));
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObj(1L);
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#generateBuildTrend(String)}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testGenerateBuildTrend() throws Exception {
		when(server.getName()).thenReturn("Hudson CI");
		when(server.getHost()).thenReturn("http://localhost:8080");
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(projectManager.getProjectObj(1L)).thenReturn(project);
		when(serverManager.getServerByJiraProject(project)).thenReturn(server);
		when(serverAccessor.getProject(server, project)).thenReturn(
			XStreamMarshaller.unmarshal(getXmlFromClasspath("jobs.xml"), JobsList.class).getJobs());
		final Response response = gadgetResource.generateBuildTrend("1");
		assertThat(response.getEntity(), is(HudsonChartsResource.class));
		final HudsonChartsResource resource = (HudsonChartsResource) response.getEntity();
		assertThat(resource.isHasErrors(), is(false));
		final HudsonServerResource serverResource = resource.getServer();
		assertThat(serverResource.getName(), is("Hudson CI"));
		assertThat(serverResource.getUrl(), is("http://localhost:8080"));
		assertThat(serverResource.getImageUrl(), is(""));
		final HudsonChartResource chartResource = ((List<HudsonChartResource>) resource.getCharts()).get(0);
		assertThat(chartResource.isGenerated(), is(true));
		assertThat(chartResource.getLocation().startsWith("jfreechart-onetime-"), is(true));
		assertThat(chartResource.getLocation().endsWith(".png"), is(true));
		assertThat(chartResource.getImageMapName().startsWith("chart-"), is(true));
		assertThat(chartResource.getImageMap().isEmpty(), is(false));
		assertThat(chartResource.getWidth(), is(ChartUtils.PORTLET_CHART_WIDTH));
		assertThat(chartResource.getHeight(), is(ChartUtils.PORTLET_CHART_HEIGHT));
		final HudsonProjectResource projectResource = chartResource.getProject();
		assertThat(projectResource.getName(), is("Marvelution"));
		assertThat(projectResource.getUrl(), is("job/Marvelution/"));
		assertThat(projectResource.getDescription(), is("Marvelution organization Project Object Model"));
		verify(serverAccessor, VerificationModeFactory.times(1)).getProject(server, project);
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObj(1L);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#generateBuildTrend(String)} with Hudson not configured
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testGenerateBuildTrendHudsonNotConfigured() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(false);
		final Response response = gadgetResource.generateBuildTrend("project-1");
		assertThat(response.getEntity(), is(HudsonChartsResource.class));
		final HudsonChartsResource resource = (HudsonChartsResource) response.getEntity();
		assertThat(resource.isHasErrors(), is(true));
		assertThat(resource.getErrors().isEmpty(), is(false));
		assertThat(resource.getErrors().size(), is(1));
		assertThat(resource.getErrors().contains("hudson.error.not.configured"), is(true));
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#generateBuildTrend(String)} with an invalid project
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testGenerateBuildTrendInvalidProject() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(projectManager.getProjectObj(1L)).thenReturn(null);
		final Response response = gadgetResource.generateBuildTrend("project-1");
		assertThat(response.getEntity(), is(HudsonChartsResource.class));
		final HudsonChartsResource resource = (HudsonChartsResource) response.getEntity();
		assertThat(resource.isHasErrors(), is(true));
		assertThat(resource.getErrors().isEmpty(), is(false));
		assertThat(resource.getErrors().size(), is(1));
		assertThat(resource.getErrors().contains("hudson.error.no.valid.project"), is(true));
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObj(1L);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#generateBuildTrend(String)} with the project not on Hudson
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testGenerateBuildTrendProjectNotOnHudson() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(projectManager.getProjectObj(1L)).thenReturn(project);
		when(serverManager.getServerByJiraProject(project)).thenReturn(server);
		when(serverAccessor.getProject(server, project)).thenReturn(null);
		final Response response = gadgetResource.generateBuildTrend("1");
		assertThat(response.getEntity(), is(HudsonChartsResource.class));
		final HudsonChartsResource resource = (HudsonChartsResource) response.getEntity();
		assertThat(resource.isHasErrors(), is(true));
		assertThat(resource.getErrors().isEmpty(), is(false));
		assertThat(resource.getErrors().size(), is(1));
		assertThat(resource.getErrors().contains("hudson.error.project.not.on.hudson"), is(true));
		verify(serverAccessor, VerificationModeFactory.times(1)).getProject(server, project);
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObj(1L);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#generateBuildTrend(String)} with throwing a
	 * {@link HudsonServerAccessorException}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testGenerateBuildTrendServerAccessorException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(projectManager.getProjectObj(1L)).thenReturn(project);
		when(serverManager.getServerByJiraProject(project)).thenReturn(server);
		when(serverAccessor.getProject(server, project)).thenThrow(new HudsonServerAccessorException("Failure"));
		final Response response = gadgetResource.generateBuildTrend("1");
		assertThat(response.getEntity(), is(HudsonChartsResource.class));
		final HudsonChartsResource resource = (HudsonChartsResource) response.getEntity();
		assertThat(resource.isHasErrors(), is(true));
		assertThat(resource.getErrors().isEmpty(), is(false));
		assertThat(resource.getErrors().size(), is(1));
		assertThat(resource.getErrors().contains("hudson.error.cannot.connect"), is(true));
		verify(serverAccessor, VerificationModeFactory.times(1)).getProject(server, project);
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObj(1L);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonChartsGadgetResource#generateBuildTrend(String)} with throwing a
	 * {@link HudsonServerAccessDeniedException}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testGenerateBuildTrendServerAccessDeniedException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(projectManager.getProjectObj(1L)).thenReturn(project);
		when(serverManager.getServerByJiraProject(project)).thenReturn(server);
		when(serverAccessor.getProject(server, project)).thenThrow(new HudsonServerAccessDeniedException("Failure"));
		final Response response = gadgetResource.generateBuildTrend("1");
		assertThat(response.getEntity(), is(HudsonChartsResource.class));
		final HudsonChartsResource resource = (HudsonChartsResource) response.getEntity();
		assertThat(resource.isHasErrors(), is(true));
		assertThat(resource.getErrors().isEmpty(), is(false));
		assertThat(resource.getErrors().size(), is(1));
		assertThat(resource.getErrors().contains("hudson.error.access.denied"), is(true));
		verify(serverAccessor, VerificationModeFactory.times(1)).getProject(server, project);
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObj(1L);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Load the XML file from the class-path
	 * 
	 * @param filename the filename to load
	 * @return the content of the file
	 * @throws IOException in case of load exceptions
	 */
	protected String getXmlFromClasspath(String filename) throws IOException {
		final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		return com.atlassian.jira.util.IOUtil.toString(inputStream);
	}

}
