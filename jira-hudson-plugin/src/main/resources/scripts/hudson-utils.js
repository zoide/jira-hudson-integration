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
