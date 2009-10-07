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

package com.marvelution.jira.plugins.hudson.model;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Model for exposing the version of the API through the API
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("jiraApi")
public final class ApiImplementation {

	@XStreamOmitField
	private static ApiImplementation api;

	private String version;

	@XStreamOmitField
	private Set<String> compatibleVersions;

	/**
	 * Private constructor
	 * 
	 * @param version the Api version
	 * @param compatibleVersions the compatible versions
	 */
	private ApiImplementation(String version, Set<String> compatibleVersions) {
		this.version = version;
		this.compatibleVersions = compatibleVersions;
	}

	/**
	 * Static method to get the {@link ApiImplementation}
	 * 
	 * @return the {@link ApiImplementation} instance
	 * @throws IOException in case the properties cannot be loaded to initiate the {@link ApiImplementation}
	 */
	public static ApiImplementation getApiImplementation() throws IOException {
		if (api == null) {
			final Properties props = new Properties();
			props.load(ApiImplementation.class.getResourceAsStream("ApiImplementation.properties"));
			final String currentVersion = props.getProperty("current.version");
			final Set<String> versions = new HashSet<String>();
			for (String version : props.getProperty(currentVersion, currentVersion).split(" ,;:")) {
				versions.add(version);
			}
			api = new ApiImplementation(currentVersion, Collections.unmodifiableSet(versions));
		}
		return api;
	}

	/**
	 * Gets the version
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the Major version of the API
	 * 
	 * @return the Major version
	 */
	public String getMajorVersion() {
		return getVersion().split("\\.")[0];
	}

	/**
	 * Gets the compatible versions
	 * 
	 * @return {@link Set} of compatible versions
	 */
	public Set<String> getCompatibleVersions() {
		return compatibleVersions;
	}

	/**
	 * Check if the given {@link ApiImplementation} is compatible with this one
	 * 
	 * @param other the other {@link ApiImplementation} to check with
	 * @return <code>true</code> if compatible, <code>false</code> otherwise
	 */
	public boolean isCompatibleWith(ApiImplementation other) {
		return (getMajorVersion().equals(other.getMajorVersion()) || getVersion().equals(other.getVersion())
						|| getCompatibleVersions().contains(other.getVersion()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other instanceof ApiImplementation) {
			return getVersion().equals(((ApiImplementation) other).getVersion());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return version.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return version;
	}

}
