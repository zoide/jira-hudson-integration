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

AJS.$.namespace("AJS.gadget.hudson.charts");

AJS.gadget.hudson.charts.displayChartImage = function (gadget, baseUrl, server, project, chart) {
	var chartDiv = AJS.$("<div/>").addClass("chart-img");
	var chartAttrs = {
		src: baseUrl + "/charts?filename=" + chart.location,
		border: 0,
		width: chart.width,
		height: chart.height
	};
	if (chart.imageMap && chart.imageMapName) {
		gadget.getView().append(chart.imageMap);
		chartAttrs.usemap = "#" + chart.imageMapName;
	}
	chartDiv.append(AJS.$("<img/>").attr(chartAttrs));
	gadget.getView().append(chartDiv);
	gadget.getView().append(AJS.gadget.hudson.common.generateGadgetFooter(gadget, server));
}
