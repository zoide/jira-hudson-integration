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

package com.marvelution.jira.plugins.hudson.charts.renderers;

import java.awt.Paint;
import java.util.Map;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;

import com.marvelution.hudson.plugins.apiv2.resources.model.Build;
import com.marvelution.jira.plugins.hudson.charts.HudsonChartGenerator;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * A {@link XYBarRenderer} for rendering the Bars for the builds depending on the build result
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildResultRenderer extends XYBarRenderer implements XYURLGenerator, XYToolTipGenerator {

	private static final long serialVersionUID = 1L;

	private HudsonServer server;
	private Map<Integer, Build> builds;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param builds the {@link Map} of {@link Build} objects
	 */
	public BuildResultRenderer(HudsonServer server, Map<Integer, Build> builds) {
		this.server = server;
		this.builds = builds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Paint getItemPaint(int row, int column) {
		final int number = new Double(getPlot().getDataset().getXValue(0, column)).intValue();
		final Build build = builds.get(number);
		switch (build.getResult()) {
		case SUCCESSFUL:
			return HudsonChartGenerator.GREEN_PAINT;
		case FAILED:
			return HudsonChartGenerator.RED_PAINT;
		case UNSTABLE:
			return HudsonChartGenerator.YELLOW_PAINT;
		case ABORTED:
		case NOTBUILD:
		default:
			return HudsonChartGenerator.GRAY_PAINT;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Paint getItemOutlinePaint(int row, int column) {
		final int number = new Double(getPlot().getDataset().getXValue(0, column)).intValue();
		final Build build = builds.get(number);
		switch (build.getResult()) {
		case SUCCESSFUL:
			return HudsonChartGenerator.GREEN_OUTLINE;
		case FAILED:
			return HudsonChartGenerator.RED_OUTLINE;
		case UNSTABLE:
			return HudsonChartGenerator.YELLOW_OUTLINE;
		case ABORTED:
		case NOTBUILD:
		default:
			return HudsonChartGenerator.GRAY_OUTLINE;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateToolTip(XYDataset dataset, int series, int item) {
		final int number = new Double(dataset.getXValue(0, item)).intValue();
		final Build build = builds.get(number);
		return "Build: " + build.getBuildNumber() + " - " + build.getResult().name();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateURL(XYDataset dataset, int series, int item) {
		final int number = new Double(dataset.getXValue(0, item)).intValue();
		final Build build = builds.get(number);
		return server.getHost() + "/" + build.getUrl();
	}

}
