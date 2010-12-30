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

package com.marvelution.hudson.plugins.apiv2.resources.utils;

/**
 * Utility class that holds all the Namespaces used by this plugin
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public final class NameSpaceUtils {

	/**
	 * The base namespace
	 */
	public static final String BASE_NAMESPAVE = "http://hudson.marvelution.com";

	/**
	 * The API version 2 namespace
	 */
	public static final String APIV2_NAMESPACE = BASE_NAMESPAVE + "/apiv2";

	/**
	 * The Job namespace
	 */
	public static final String JOB_NAMESPACE = APIV2_NAMESPACE + "/job";

	/**
	 * The Build namespace
	 */
	public static final String BUILD_NAMESPACE = APIV2_NAMESPACE + "/build";

	/**
	 * The Build Trigger namespace
	 */
	public static final String BUILD_TRIGGER_NAMESPACE = BUILD_NAMESPACE + "/trigger";

	/**
	 * The View namespace
	 */
	public static final String VIEW_NAMESPACE = APIV2_NAMESPACE + "/view";

}
