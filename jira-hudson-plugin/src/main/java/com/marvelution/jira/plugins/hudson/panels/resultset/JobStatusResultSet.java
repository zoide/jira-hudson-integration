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

package com.marvelution.jira.plugins.hudson.panels.resultset;

import java.io.IOException;

import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.atlassian.jira.util.I18nHelper;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.jira.plugins.hudson.charts.BuildResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.BuildTestResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.HudsonChartGenerator;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * {@link ResultSet} implementation specific for the Job Status view on the panels
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobStatusResultSet extends AbstractResultSet<Job> {

	public static final int CHART_HEIGHT = 300;
	public static final int CHART_WIDTH = 400;

	private final I18nHelper i18nHelper;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param job the {@link Job}
	 */
	public JobStatusResultSet(HudsonServer server, Job job, I18nHelper i18nHelper) {
		super(server, job);
		this.i18nHelper = i18nHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultViews getView() {
		return ResultViews.JOBSTATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasResults() {
		return getResults() != null;
	}

	/**
	 * Getter for the Build Results Ratio Chart
	 * 
	 * @return the Build Results Ratio Chart {@link ChartHelper}
	 * @throws IOException in case of generation issues
	 */
	public ChartHelper getBuildResultsRatioChart() throws IOException {
		final HudsonChartGenerator chartGenerator = new BuildResultsRatioChartGenerator(i18nHelper);
		chartGenerator.setData(getServer(), getResults());
		final ChartHelper chart = chartGenerator.generateChart();
		chart.generate(CHART_WIDTH, CHART_HEIGHT);
		return chart;
	}

	/**
	 * Getter for the Build Test Results Ratio Chart
	 * 
	 * @return the Build Test Results Ratio Chart {@link ChartHelper}
	 * @throws IOException in case of generation issues
	 */
	public ChartHelper getBuildTestResultsRatioChart() throws IOException {
		final HudsonChartGenerator chartGenerator = new BuildTestResultsRatioChartGenerator(i18nHelper);
		chartGenerator.setData(getServer(), getResults());
		final ChartHelper chart = chartGenerator.generateChart();
		chart.generate(CHART_WIDTH, CHART_HEIGHT);
		return chart;
	}

}
