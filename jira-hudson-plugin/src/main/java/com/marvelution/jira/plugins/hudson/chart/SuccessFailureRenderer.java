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

import java.awt.Paint;
import java.util.Map;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;

import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.Result;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * {@link XYBarRenderer} for the {@link HudsonChart}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class SuccessFailureRenderer extends XYBarRenderer implements XYToolTipGenerator, XYURLGenerator {

	private static final long serialVersionUID = 1L;

	private HudsonServer server;

	private Map<Integer, Build> buildsMap;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer} the data came from
	 * @param buildsMap {@link Map} containing all the builds shown in the chart
	 */
	public SuccessFailureRenderer(HudsonServer server, Map<Integer, Build> buildsMap) {
		this.server = server;
		this.buildsMap = buildsMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Paint getItemPaint(int row, int column) {
		final Integer columnKey = Integer.valueOf(Double.valueOf(getPlot().getDataset().getXValue(0, column)).intValue());
		final Build build = buildsMap.get(columnKey);
		if (Result.SUCCESS.equals(build.getResult())) {
			return ChartUtils.GREEN_OUTLINE;
		} else if (Result.UNSTABLE.equals(build.getResult())) {
			return ChartUtils.YELLOW_OUTLINE;
		}
		return ChartUtils.RED_OUTLINE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Paint getItemOutlinePaint(int row, int column) {
		final Integer columnKey = Integer.valueOf(Double.valueOf(getPlot().getDataset().getXValue(0, column)).intValue());
		final Build build = buildsMap.get(columnKey);
		if (Result.SUCCESS.equals(build.getResult())) {
			return ChartUtils.GREEN_OUTLINE;
		} else if (Result.UNSTABLE.equals(build.getResult())) {
			return ChartUtils.YELLOW_OUTLINE;
		}
		return ChartUtils.RED_OUTLINE;
	}

	/**
	 * {@inheritDoc}
	 */
	public String generateToolTip(XYDataset dataset, int row, int column) {
		final Integer buildNumber = Integer.valueOf(Double.valueOf(dataset.getXValue(0, column)).intValue());
	    final Build build = buildsMap.get(buildNumber);
		return "Build: " + build.getNumber() + " - " + build.getResult().name();
	}

	/**
	 * {@inheritDoc}
	 */
	public String generateURL(XYDataset dataset, int row, int column) {
		final Integer buildNumber = Integer.valueOf(Double.valueOf(dataset.getXValue(0, column)).intValue());
	    final Build build = buildsMap.get(buildNumber);
		return server.getHost() + "/" + build.getUrl();
	}

}
