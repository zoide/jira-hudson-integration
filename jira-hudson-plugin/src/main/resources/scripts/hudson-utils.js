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

AJS.$.namespace("AJS.hudson.utils");

/**
 * Method to create a div element
 * If the id argument is given then that id is set as the div id
 * 
 * @param id the optional id for the div
 * @returns the created div
 */
AJS.hudson.utils.createDiv = function(id) {
	var div =  AJS.$('<div/>');
	if (id !== undefined && id != "") {
		div.attr('id', id);
	}
	return div;
}

/**
 * Method to create an A element
 * If the text argument is given then that text is used, otherwise the link will also be used for the text
 * 
 * @param link the url link
 * @param text the optional text to show
 * @return the A element
 */
AJS.hudson.utils.createLink = function(link, text) {
	var element = AJS.$('<a/>').attr('href', link);
	if (text !== undefined && text != "") {
		element.text(text);
	} else {
		element.text(link);
	}
	return element;
}

/**
 * Method to create a details table element
 * 
 * @param details the details object
 * @return the details table
 */
AJS.hudson.utils.createDetailsTable = function(details) {
	var table = AJS.$('<table/>');
	for (var index in details) {
		var detail = details[index];
		var row = AJS.$('<tr/>');
		var label = AJS.$('<th/>');
		label.text(detail.label);
		row.append(label);
		var value = AJS.$('<td/>');
		value.html(detail.value);
		row.append(value);
		table.append(row);
	}
	return table;
}

/**
 * Method to create a chart div
 * 
 * @param gadget the gadget to create a chart for
 * @param chart the chart data
 * @return the chart div
 */
AJS.hudson.utils.createChart = function(gadget, chart) {
	var chartDiv = AJS.hudson.utils.createDiv();
	chartDiv.addClass('chartImage');
	chartDiv.html(chart.imageMap);
	var chartImg = AJS.$("<img/>");
	chartImg.attr({
		src: gadget.getBaseUrl() + "/charts?filename=" + chart.location,
		usemap: "#" + chart.imageMapName,
		height: chart.height,
		width: chart.width
	});
	chartDiv.append(chartImg);
	return chartDiv;
}

/**
 * Method to create a View of the build data given
 * 
 * @param gadget the gadget to create the view for
 * @param server the Hudson server the data came from
 * @param job the Job on Hudson that was build
 * @param build the build to create the view for
 * @return the details view
 */
AJS.hudson.utils.createBuildDetailsView = function(gadget, server, job, build) {
	var jobStatus = AJS.$('<ol/>').attr('id', 'jobStatus');
	jobStatus.append('<li class="clear">&nbsp;</li>');
	var trLi = AJS.$('<li/>').addClass('testResults');
	var trUl = AJS.$('<ul/>');
	trUl.append(AJS.$('<li/>').addClass('header').text(gadget.getMsg('hudson.gadget.build.test.results')));
	if (build.testResult !== undefined && build.testResult.failed > 0) {
		AJS.$.each(build.testResult.failedTests, function(index, test) {
			var listItem = AJS.$('<li/>');
			listItem.text(test.className + "." + test.testName);
			if (index == build.testResult.failedTests.length - 1) {
				listItem.addClass('last');
			}
			trUl.append(listItem);
		});
	} else {
		trUl.append(AJS.$('<li/>').addClass('last').text(gadget.getMsg('hudson.gadget.build.no.test.results')));
	}
	trLi.append(trUl);
	jobStatus.append(trLi);
	jobStatus.append('<li class="clear">&nbsp;</li>');
	var baLi = AJS.$('<li/>').addClass('buildArtifacts');
	var baUl = AJS.$('<ul/>');
	baUl.append(AJS.$('<li/>').addClass('header').text(gadget.getMsg('hudson.gadget.build.artifacts')));
	if (build.artifacts !== undefined && build.artifacts.length > 0) {
		AJS.$.each(build.artifacts, function(index, artifact) {
			var listItem = AJS.$('<li/>');
			var href = server.host + "/" + job.url + "/lastBuild/" + artifact.url;
			listItem.append(AJS.hudson.utils.createLink(href, artifact.name));
			if (index == build.artifacts.length - 1) {
				listItem.addClass('last');
			}
			baUl.append(listItem);
		});
	} else {
		baUl.append(AJS.$('<li/>').addClass('last').text(gadget.getMsg('hudson.gadget.build.no.artifacts')));
	}
	baLi.append(baUl);
	jobStatus.append(baLi);
	jobStatus.append('<li class="clear">&nbsp;</li>');
	return jobStatus
}

/**
 * Method to create a Tab view
 * 
 * @param gadget the gadget to create the tab view for
 * @param tabs the array of tab object (tab.name/tab.content)
 * @return the tabs view div
 */
AJS.hudson.utils.createTabView = function(gadget, tabs, selectedTab) {
	var tabsDiv = AJS.hudson.utils.createDiv();
	tabsDiv.addClass('tabbedDetails');
	var tabsList = AJS.$('<ul/>');
	tabsList.attr('id', 'tabs');
	var tabsContent = AJS.hudson.utils.createDiv();
	tabsContent.attr('id', 'tabsContent');
	for (var index in tabs) {
		var tab = AJS.$('<li/>');
		tab.addClass('tab');
		tab.click(function(event) {
			var tabId = AJS.$(event.target).attr('id').split('_')[0];
			AJS.hudson.utils.toggleTabView(gadget, tabId);
			event.preventDefault();
		});
		var tabContent = AJS.hudson.utils.createDiv(tabs[index].id + "_content");
		tabContent.addClass("content");
		if (selectedTab == "" || tabs[index].id == selectedTab) {
			selectedTab = tabs[index].id;
			tab.addClass('selected');
			tabContent.css({display: "block"});
		} else {
			tabContent.css({display: "none"});
		}
		tab.attr('id', tabs[index].id + "_tab");
		tab.text(tabs[index].name);
		tabsList.append(tab);
		tabContent.append(tabs[index].content);
		tabsContent.append(tabContent);
	}
	tabsDiv.append(tabsList);
	tabsDiv.append(tabsContent);
	return tabsDiv;
}

/**
 * Method to toggle the selected tab within a tabbed view
 * 
 * @param gadget the gadget holding the tabs
 * @param tabId the tab id to toggle
 */
AJS.hudson.utils.toggleTabView = function(gadget, tabId) {
	AJS.$("#tabs .tab").each(function(index, item) {
		AJS.$("#" + item.id).removeClass("selected");
	});
	AJS.$("#" + tabId + "_tab").addClass("selected");
	AJS.$("#tabsContent .content").each(function(index, item) {
		AJS.$("#" + item.id).css({display: 'none'});
	});
	AJS.$("#" + tabId + "_content").css({display: 'block'});
	gadget.resize();
}
