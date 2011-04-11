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

AJS.$.namespace("AJS.hudson.gadget.config");

/**
 * Get the Theme configuration of the Gadget
 * 
 * @param gadget the gadget to get the theme configuration for
 * @return the theme configuration
 */
AJS.hudson.gadget.config.getThemeConfiguration = function(gadgets) {
	if (gadgets.window.getViewportDimensions().width < 500) {
		return "top-label";
	} else {
		return "long-label";
	}
}

/**
 * Get the fields array for the given gadget
 * 
 * @param gadget the gadget to get the fields array for
 * @return the fields array
 */
AJS.hudson.gadget.config.getServerOnlyFields = function(gadget) {
	return [
	    AJS.hudson.gadget.config.serverField(gadget),
	    AJS.hudson.gadget.config.dateFormatField(gadget),
		AJS.hudson.gadget.config.isConfiguredField,
		AJS.hudson.gadget.config.titleRequiredField
	];
}

/**
 * Get the fields array for the given gadget
 * 
 * @param gadget the gadget to get the fields array for
 * @return the fields array
 */
AJS.hudson.gadget.config.getChartFields = function(gadget, args) {
	return [
	    AJS.hudson.gadget.config.associationField(gadget, args.options.associations),
	    AJS.hudson.gadget.config.chartsField(gadget),
		AJS.hudson.gadget.config.isConfiguredField,
		AJS.hudson.gadget.config.titleRequiredField
	];
}

/**
 * The Gadgets' Server field
 * 
 * @param gadget the gadget to get the server field for
 * @return the server field
 */
AJS.hudson.gadget.config.serverField = function(gadget) {
	return {
		id: "hudsonServer",
		userpref: "server",
		label: gadget.getMsg("hudson.gadget.server.label"),
		description: gadget.getMsg("hudson.gadget.server.description"),
		type: "text",
		value: gadget.getPref("server")
	}
}

/**
 * The Gadgets' Date Format field
 * 
 * @param gadget the gadget to get the date format field for
 * @return the date format field
 */
AJS.hudson.gadget.config.dateFormatField = function(gadget) {
	return {
		id: "dateFormat",
		userpref: "dateFormat",
		label: gadget.getMsg("hudson.gadget.date.format.label"),
		description: gadget.getMsg("hudson.gadget.date.format.description"),
		type: "text",
		value: gadget.getPref("dateFormat")
	}
}

/**
 * The Gadgets' Associations field
 * 
 * @param gadget the gadget to get the associations field for
 * @return the associations field
 */
AJS.hudson.gadget.config.associationField = function(gadget, associations) {
	return {
		id: "association",
		userpref: "association",
		label: gadget.getMsg("hudson.gadget.associations.label"),
		description: gadget.getMsg("hudson.gadget.associations.description"),
		type: "select",
		value: gadget.getPref("association"),
		options: associations
	}
}

/**
 * The Gadgets' Charts field
 * 
 * @param gadget the gadget to get the charts field for
 * @return the charts field
 */
AJS.hudson.gadget.config.chartsField = function(gadget) {
	return {
		id: "chart",
		userpref: "chart",
		label: gadget.getMsg("hudson.gadget.charts.label"),
		description: gadget.getMsg("hudson.gadget.charts.description"),
		type: "select",
		value: gadget.getPref("chart"),
		options: [{
			label: gadget.getMsg("hudson.gadget.buildResultsRationChart.title"),
			value: "buildResultsRationChart"
		},{
			label: gadget.getMsg("hudson.gadget.buildTestResultsRationChart.title"),
			value: "buildTestResultsRationChart"
		}]
	}
}

/**
 * The Gadgets' isConfigured field
 */
AJS.hudson.gadget.config.isConfiguredField = {
	userpref: "isConfigured",
	type: "hidden",
	value: "true"
}

/**
 * The Gadgets' isConfigured field
 */
AJS.hudson.gadget.config.titleRequiredField = {
	userpref: "titleRequired",
	type: "hidden",
	value: "true"
}
