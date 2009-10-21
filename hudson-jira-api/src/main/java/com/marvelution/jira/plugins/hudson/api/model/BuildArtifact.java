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

package com.marvelution.jira.plugins.hudson.api.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Model class for Hudson Build Artifacts
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("artifact")
public class BuildArtifact {

	private String name;

	private String url;

	/**
	 * Default argument less constructor for XStream
	 */
	public BuildArtifact() {
	}

	/**
	 * Constructor
	 * 
	 * @param name the Artifact name
	 * @param url the Artifact url
	 */
	public BuildArtifact(String name, String url) {
		setName(name);
		setUrl(url);
	}

	/**
	 * Get the artifact name
	 * 
	 * @return the artifact name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the artifact name
	 * 
	 * @param name the artifact name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the Artifact url
	 * 
	 * @return the artifact url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the artifact url
	 * 
	 * @param url the artifact url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
