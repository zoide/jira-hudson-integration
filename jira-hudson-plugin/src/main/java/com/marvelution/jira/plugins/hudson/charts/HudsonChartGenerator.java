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

import com.atlassian.jira.charts.jfreechart.ChartGenerator;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * Hudson plugin specific {@link ChartGenerator}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonChartGenerator extends ChartGenerator {

	Color RED_OUTLINE = new Color(204, 0, 0);
	Color RED_PAINT = new Color(255, 204, 204);
	Color GREEN_OUTLINE = new Color(0, 204, 0);
	Color GREEN_PAINT = new Color(204, 255, 204);
	Color YELLOW_OUTLINE = new Color(204, 204, 0);
	Color YELLOW_PAINT = new Color(255, 255, 204);
	Color GRAY_OUTLINE = new Color(204, 204, 204);
	Color GRAY_PAINT = new Color(238, 238, 238);

	/**
	 * Set the data for the Chart
	 * 
	 * @param server the {@link HudsonServer}
	 * @param job the {@link Job}
	 */
	void setData(HudsonServer server, Job job);

	/**
	 * Set the Data for the chart
	 * 
	 * @param server the {@link HudsonServer}
	 * @param builds the {@link Builds} collection
	 */
	void setData(HudsonServer server, Builds builds);

}
