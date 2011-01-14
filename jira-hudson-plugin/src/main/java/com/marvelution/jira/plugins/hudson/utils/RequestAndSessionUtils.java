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

package com.marvelution.jira.plugins.hudson.utils;

import java.util.Map;

import webwork.action.ActionContext;

import com.atlassian.jira.util.ParameterUtils;

/**
 * Helper class for Request and Session variables
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class RequestAndSessionUtils {

	/**
	 * Getter for a request variable by key, first from the request parameters and if not found from the session
	 *  
	 * @param requestKey the request key to get the value for
	 * @param defaultValue the default value in case the request value is not set
	 * @return the value for the request variable, or the default given value
	 */
	public static String retrieveFromRequestOrSession(String requestKey, String defaultValue) {
		String value = retrieveFromRequestOrSession(requestKey);
		return value != null ? value : defaultValue;
	}

	/**
	 * Getter for a request variable value by key
	 * 
	 * @param requestKey the request variable key to get
	 * @return the value of the variable, may be <code>null</code>
	 */
	public static String retrieveFromRequestOrSession(String requestKey) {
		String sessionKey = JiraPluginUtils.getPluginKey() + "." + requestKey;
		String paramFromRequest = ParameterUtils.getStringParam(ActionContext.getParameters(), requestKey);
		storeInSession(sessionKey, paramFromRequest);
		return paramFromRequest;
	}

	/**
	 * Setter for a key value pair in the current {@link ActionContext} Session {@link Map}
	 * 
	 * @param key the session key
	 * @param value the session value
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public static void storeInSession(String key, Object value) {
		Map session = ActionContext.getSession();
		session.put(key, value);
	}

}
