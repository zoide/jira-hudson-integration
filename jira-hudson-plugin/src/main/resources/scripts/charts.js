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

/**
 * Display a chart in a div element
 * 
 * @param gadget the Gadget where the chart will be displayed in
 * @param baseUrl the baseUrl of the Jira instance
 * @param server the Hudson server where the data came from
 * @param charts the JSON chart objects
 */
AJS.gadget.hudson.charts.displayChartImage = function (gadget, baseUrl, server, charts) {
	var chartsDiv = AJS.$("<div/>").addClass("view-container");
	var tabsContainer = AJS.$("<div/>").attr({id: "tabs"});
	var chartsContainer = AJS.$("<div/>").attr({id: "charts"});
	var showInitially = true;
	AJS.$.each(charts, function(index, chart) {
		AJS.gadget.hudson.charts.createChartTab(gadget, index, tabsContainer, chartsContainer, baseUrl, chart, showInitially);
		showInitially = false;
	});
	tabsContainer.appendTo(chartsDiv);
	chartsContainer.appendTo(chartsDiv);
	gadget.getView().append(chartsDiv);
	gadget.getView().append(AJS.gadget.hudson.common.generateGadgetFooter(gadget, server));
}

AJS.gadget.hudson.charts.createChartTab = function (gadget, index, tabsContainer, chartsContainer, baseUrl, chart, showInitially) {
	var tab = AJS.$("<span/>").attr({
		id: "tab_" + index
	}).addClass("tab").text(chart.project.name);
	if (showInitially) {
		tab.addClass("selected");
	}
	tab.click(function() {
		AJS.$("#tabs .tab").each(function(index, item) {
			AJS.$("#" + item.id).removeClass("selected");
		});
		AJS.$("#charts .chart").each(function(index, item) {
			AJS.$("#" + item.id).css("display", "none");
		});
		AJS.$("#tab_" + index).addClass("selected");
		AJS.$("#chart_" + index).css("display", "block");
	});
	tabsContainer.append(tab);
	var chartDiv = AJS.$("<div/>").attr({
		id: "chart_" + index
	}).addClass("chart");
	if (chart.generated) {
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
	} else {
		chartDiv.append(
			AJS.gadget.hudson.common.generateErrorMessageBox([gadget.getMsg("hudson.gadget.error.chart.not.generated")])
		);
	}
	if (showInitially) {
		chartDiv.css("display", "block");
	} else {
		chartDiv.css("display", "none");
	}
	chartsContainer.append(chartDiv);
}
