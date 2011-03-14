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
		dataTpe : "json",
		url: server.host + apiUrl
	};
	if (errorHandler !== undefined) {
		options.error = errorHandler;
	}
	if (successHandler !== undefined) {
		options.success = successHandler;
	}
	if (server.secured && server.username !== undefined && server.password !== undefined) {
		options.username = server.username;
		options.password = server.password;
	}
	return options;
}

/**
 * Parse a given string into a Hudson Server object
 * 
 * @param serverString the server string to parse
 * @return the Server object
 */
AJS.hudson.gadget.utils.parseServer = function(serverString) {
	var matches = serverString.match(/(http|https):\/\/(.*):(.*)@(.*)/);
	if (matches !== null) {
		return {
			username : matches[2],
			password : matches[3],
			host : matches[1] + "://" + matches[4],
			secured : true
		};
	} else {
		return {
			host : serverString,
			secured : false
		};
	}
}
