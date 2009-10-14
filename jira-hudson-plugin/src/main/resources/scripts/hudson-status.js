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

/**
 * Generate the Status Overview
 * 
 * @param gadget the Gadget to generate the status overview for
 * @param view the view on the Hudson server, may be null
 * @param server he server the builds are configured on
 * @param builds the builds on the server
 * @return the content of the gadget
 */
function generateStatusOverview(gadget, view, server, builds) {
	var content = AJS.$("<table/>").addClass("so");
	AJS.$.each(builds, function (i, build) {
		content.append(AJS.$("<tr/>").append(AJS.$("<td/>").append(generateBuildOverview(gadget, server, build))));
	});
	content.append(generateHudsonGadgetFooter(gadget, server));
	return content;
}

/**
 * Generate a Build Overview div
 * 
 * @param gadget the Gadget the div will be added to
 * @param server the server that executed the build
 * @param build the build data
 * @return the build overview div
 */
function generateBuildOverview(gadget, server, build) {
	var div = AJS.$("<div/>").addClass(build.result);
	
	return div;
}
