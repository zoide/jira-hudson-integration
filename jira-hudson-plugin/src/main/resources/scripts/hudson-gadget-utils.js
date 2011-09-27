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

AJS.$.namespace("AJS.hudson.gadget.utils");

/**
 * Create the Ajax Options for invoking the Hudson Server
 * 
 * @param server the Hudson Server object with host, username and password properties
 * @param apiUrl the API url that needs to be invoked
 * @param successHandler the function executed on a successful invocation
 * @param errorHandler the function executed on a failure invocation
 */
AJS.hudson.gadget.utils.getAjaxOptions = function(server, apiUrl, successHandler, errorHandler) {
	var options = {
		type : "GET",
		dataTpe : "json"
	};
	if (server.secured) {
		// The makeRequest servlet of Atlassian doesn't handle the authenticate correctly for Hudson instances
		// So redirect the call to the Hudson Make Request servlet that does it
		options.url = server.baseUrl + "/plugins/servlet/hudson/makeRequest";
		options.data = {
			url: server.host + apiUrl,
			type: "json"
		};
	} else {
		options.url = server.host + apiUrl;
	}
	if (errorHandler !== undefined) {
		options.error = errorHandler;
	}
	if (successHandler !== undefined) {
		options.success = successHandler;
	}
	return options;
}

/**
 * Parse a given string into a Hudson Server object
 * 
 * @param baseUrl the base URL of JIRA
 * @param serverString the server string to parse
 * @return the Server object
 */
AJS.hudson.gadget.utils.parseServer = function(baseUrl, serverString) {
	var matches = serverString.match(/(http|https):\/\/(.*):(.*)@(.*)/);
	if (matches !== null) {
		return {
			baseUrl: baseUrl,
			host : serverString,
			secured : true
		};
	} else {
		return {
			baseUrl: baseUrl,
			host : serverString,
			secured : false
		};
	}
}
