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

package com.marvelution.jira.plugins.hudson.chart;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.CategoryTableXYDataset;

import com.marvelution.jira.plugins.hudson.chart.utils.DurationFormat;
import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * {@link HudsonChart} utility class
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ChartUtils {

	public static final int PORTLET_CHART_HEIGHT = 300;

	public static final int PORTLET_CHART_WIDTH = 450;

	public static final Color RED_OUTLINE = new Color(204, 0, 0);

	public static final Color RED_PAINT = new Color(255, 204, 204);

	public static final Color GREEN_OUTLINE = new Color(0, 204, 0);

	public static final Color GREEN_PAINT = new Color(204, 255, 204);

	public static final Color YELLOW_OUTLINE = new Color(204, 204, 0);

	public static final Color YELLOW_PAINT = new Color(255, 255, 204);

	/**
	 * Helper method to generate the Success Failed builds ratio chart of a Hudson project
	 * 
	 * @param server the {@link HudsonServer} the data came from
	 * @param project the Hudson Project ({@link Job}) to generate the chart for
	 * @return {@link HudsonChart}
	 */
	public static HudsonChart generateSuccessFailedRatioChart(HudsonServer server, Job project) {
		final Map<Integer, Build> builds = new HashMap<Integer, Build>();
		for (Build build : project.getBuilds()) {
			builds.put(build.getNumber(), build);
		}
		return generateSuccessFailedRatioChart(server, builds);
	}

	/**
	 * Helper method to generate the Success Failed builds ratio chart of a List of Hudson Builds
	 * 
	 * @param server the {@link HudsonServer} the data came from
	 * @param builds the Hudson Builds to generate the chart for
	 * @return {@link HudsonChart}
	 */
	public static HudsonChart generateSuccessFailedRatioChart(HudsonServer server, Map<Integer, Build> builds) {
		final CategoryTableXYDataset dataSet = new CategoryTableXYDataset();
		for (Entry<Integer, Build> entry : builds.entrySet()) {
			dataSet.add(new Integer(entry.getValue().getNumber()).doubleValue(), new Long(entry.getValue()
				.getDuration()).doubleValue(), "Duration");
		}
		final JFreeChart chart = ChartFactory.createXYBarChart("", "", false, "Duration", dataSet,
			PlotOrientation.VERTICAL, false, false, false);
		chart.setBackgroundPaint(Color.WHITE);
		final SuccessFailureRenderer renderer = new SuccessFailureRenderer(server, builds);
		renderer.setToolTipGenerator(renderer);
		renderer.setURLGenerator(renderer);
		final XYPlot xyPlot = chart.getXYPlot();
		xyPlot.setRenderer(renderer);
		final NumberAxis domainAxis = new NumberAxis(); 
		domainAxis.setLowerBound(Collections.min(builds.keySet()));
		domainAxis.setUpperBound(Collections.max(builds.keySet()));
		final TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
	    domainAxis.setStandardTickUnits(ticks);
	    xyPlot.setDomainAxis(domainAxis);
		final DateAxis rangeAxis = new DateAxis();
		final DurationFormat durationFormat = new DurationFormat();
		rangeAxis.setDateFormatOverride(durationFormat);
		rangeAxis.setLabel("Duration");
		xyPlot.setRangeAxis(rangeAxis);
		return new HudsonChart(chart);
	}

}
