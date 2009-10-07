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

import java.io.IOException;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ServletUtilities;

import com.atlassian.core.util.RandomGenerator;

/**
 * Utility class for Hudson JFreeCharts
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonChart {

	private JFreeChart chart;

	private boolean generated = false;

	private ChartRenderingInfo renderingInfo = null;

	private String location = null;

	private String imageMap;

	private String imageMapName;

	private int width = 0;

	private int height = 0;

	/**
	 * Constructor
	 * 
	 * @param chart the {@link JFreeChart}
	 */
	public HudsonChart(JFreeChart chart) {
		this.chart = chart;
	}

	/**
	 * Get the {@link JFreeChart}
	 * 
	 * @return the {@link JFreeChart}
	 */
	public JFreeChart getChart() {
		return chart;
	}

	/**
	 * Get if the chart has been generated
	 * 
	 * @return <code>true</code> if the chart has been generated, <code>false</code> otherwise
	 */
	public boolean isGenerated() {
		return generated;
	}

	/**
	 * Generate the chart image form the {@link JFreeChart} object
	 * 
	 * @param chartWidth the width in pixels
	 * @param chartHeight the height in pixels
	 * @throws IOException in case the chart cannot be generated
	 */
	public void generate(int chartWidth, int chartHeight) throws IOException {
		if (isGenerated()) {
			return;
		}
		width = chartWidth;
		height = chartHeight;
		renderingInfo = new ChartRenderingInfo();
		location = ServletUtilities.saveChartAsPNG(chart, width, height, renderingInfo, null);
		imageMapName = "chart-" + RandomGenerator.randomString(5);
	    imageMap = ChartUtilities.getImageMap(imageMapName, renderingInfo);
		generated = true;
	}

	/**
	 * Get the chart location
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Get the chart image map
	 * 
	 * @return the image map
	 */
	public String getImageMap() {
		return imageMap;
	}

	/**
	 * Get the chart image map name
	 * 
	 * @return the image map name
	 */
	public String getImageMapName() {
		return imageMapName;
	}

	
	/**
	 * Get the chart image width
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	
	/**
	 * Get the chart image height
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

}
