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
 * Generate the Error messages box for the gadget and errors given
 * 
 * @param gadget the Gadget to generate the error messages box for
 * @param errors the array of i18n error keys
 * @return the error messages div
 */
function generateErrorMessageBox(gadget, errors) {
	var errorDiv = AJS.$("<div/>").addClass("empty-results");
	var errorList = AJS.$("<ul/>").addClass("styleless-list");
	AJS.$.each(errors, function (i, error) {
		errorList.append(AJS.$("<li/>").text(gadget.getMsg(error)));
	});
	errorList.appendTo(errorDiv);
	return errorDiv;
}

/**
 * Generate the Gadget footer
 * 
 * @param gadget the Gadget that will display the footer
 * @param server the Hudson server used in the Gadget
 * @return the footer div
 */
function generateHudsonGadgetFooter(gadget, server) {
	return AJS.$("<div/>").addClass("footer")
		.append(gadget.getMsg("hudson.gadget.common.connected.to"))
		.append(": ")
		.append(AJS.$("<a/>").attr({href : server.url, target : "_parent", title : server.name}).text(server.url));
}
