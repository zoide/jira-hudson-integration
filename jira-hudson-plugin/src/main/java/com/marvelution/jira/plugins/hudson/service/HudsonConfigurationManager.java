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

package com.marvelution.jira.plugins.hudson.service;

/**
 * Hudson Configuration Manager interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 * 
 * @see 3.3.0
 */
public interface HudsonConfigurationManager {

	String HIDE_UNASSOCIATED_HUDSON_TAB = "hide.unassociated.hudson.tab";

	/**
	 * Get the {@link String} value of the property key given
	 * 
	 * @param key the property key to get the value for
	 * @return the property value
	 */
	String getProperty(String key);

	/**
	 * Set the Property value for the property with the given key
	 * {@link String#valueOf(Object)} is used to get the {@link String} value of the property value given
	 * 
	 * @param key the property key
	 * @param value the property value
	 */
	void setProperty(String key, Object value);

	/**
	 * Get the {@link Boolean} value of the property key given
	 * 
	 * @param key the property key to get the value for
	 * @return the property value
	 */
	boolean getBooleanProperty(String key);

}
