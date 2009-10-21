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

package com.marvelution.jira.plugins.hudson.api;

import java.io.IOException;

import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;

/**
 * Helper class to get the Api Version
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class JiraApi {

	/**
	 * Get the {@link ApiImplementation} of the API
	 * 
	 * @deprecated Use the {@link ApiImplementation} constructor to get the {@link ApiImplementation}
	 * @return the {@link ApiImplementation}
	 */
	@Deprecated
	public static ApiImplementation getApiImplementation() {
		try {
			return ApiImplementation.getApiImplementation();
		} catch (IOException e) {
			return null;
		}
	}

}
