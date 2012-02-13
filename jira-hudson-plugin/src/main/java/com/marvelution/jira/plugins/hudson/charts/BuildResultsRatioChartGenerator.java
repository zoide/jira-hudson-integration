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

package com.marvelution.jira.plugins.hudson.charts;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.atlassian.jira.charts.jfreechart.util.ChartDefaults;
import com.atlassian.jira.charts.jfreechart.util.ChartUtil;
import com.atlassian.jira.util.I18nHelper;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.jira.plugins.hudson.charts.renderers.BuildResultRenderer;
import com.marvelution.jira.plugins.hudson.charts.utils.DurationFormat;

/**
 * {@link HudsonChartGenerator} implementation for generating the build results ratio chart
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildResultsRatioChartGenerator extends AbstractHudsonChartGenerator {

	/**
	 * Constructor
	 */
	public BuildResultsRatioChartGenerator() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param i18nHelper the {@link I18nHelper} implementation
	 */
	public BuildResultsRatioChartGenerator(I18nHelper i18nHelper) {
		super(i18nHelper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartHelper generateChart() {
		final Map<Integer, Build> buildMap = new HashMap<Integer, Build>();
		final CategoryTableXYDataset dataSet = new CategoryTableXYDataset();
		for (Build build : builds) {
			buildMap.put(Integer.valueOf(build.getBuildNumber()), build);
			dataSet.add(Double.valueOf(build.getBuildNumber()),
					Double.valueOf(build.getDuration()), getI18n().getText("hudson.charts.duration"));
		}
		final JFreeChart chart = ChartFactory.createXYBarChart("", "", false,
				getI18n().getText("hudson.charts.duration"), dataSet, PlotOrientation.VERTICAL, false, false, false);
		chart.setBackgroundPaint(Color.WHITE);
		chart.setBorderVisible(false);
		final BuildResultRenderer renderer = new BuildResultRenderer(server, buildMap);
		renderer.setBaseItemLabelFont(ChartDefaults.defaultFont);
		renderer.setBaseItemLabelsVisible(false);
		renderer.setMargin(0.0D);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
		renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		renderer.setBaseToolTipGenerator(renderer);
		renderer.setURLGenerator(renderer);
		final XYPlot xyPlot = chart.getXYPlot();
		xyPlot.setAxisOffset(new RectangleInsets(1.0D, 1.0D, 1.0D, 1.0D));
		xyPlot.setRenderer(renderer);
		final NumberAxis domainAxis = new NumberAxis();
		domainAxis.setLowerBound(Collections.min(buildMap.keySet()));
		domainAxis.setUpperBound(Collections.max(buildMap.keySet()));
		final TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
		domainAxis.setStandardTickUnits(ticks);
		xyPlot.setDomainAxis(domainAxis);
		final DateAxis rangeAxis = new DateAxis();
		final DurationFormat durationFormat = new DurationFormat();
		rangeAxis.setDateFormatOverride(durationFormat);
		rangeAxis.setLabel(getI18n().getText("hudson.charts.duration"));
		xyPlot.setRangeAxis(rangeAxis);
		ChartUtil.setupPlot(xyPlot);
		return new ChartHelper(chart);
	}

}
