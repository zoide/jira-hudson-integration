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
};

/**
 * Get the fields array for the given gadget
 * 
 * @param gadget the gadget to get the fields array for
 * @return the fields array
 */
AJS.hudson.gadget.config.getFields = function(gadget) {
	return [
		AJS.hudson.gadget.config.isConfiguredField,
		AJS.hudson.gadget.config.titleRequiredField
	];
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
