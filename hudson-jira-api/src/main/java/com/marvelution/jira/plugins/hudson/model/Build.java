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

import com.marvelution.jira.plugins.hudson.xstream.converters.ResultConverter;
import com.marvelution.jira.plugins.hudson.xstream.converters.StateConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * Model class for Hudson Builds
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("build")
public class Build {

	private int buildNumber;

	private String url;
	
	private long duration = 0L;
	
	private long timestamp = 0L;

	@XStreamConverter(ResultConverter.class)
	@XStreamAlias("result")
	private Result result;

	@XStreamConverter(StateConverter.class)
	@XStreamAlias("state")
	private State state;

	/**
	 * Constructor
	 * 
	 * @param buidNumber the build number
	 * @param url the base url of the build
	 */
	public Build(int buidNumber, String url) {
		setBuildNumber(buildNumber);
		setUrl(url);
	}

	/**
	 * Gets the build number of this build
	 * 
	 * @return the build number
	 */
	public int getBuildNumber() {
		return buildNumber;
	}

	/**
	 * Sets the build number
	 * 
	 * @param buildNumber the build number
	 */
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	/**
	 * Gets the base url for this build
	 * 
	 * @return the base url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the base url for this build
	 * 
	 * @param url the base url
	 */
	public void setUrl(String url) {
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		if (!url.startsWith("http") && !url.startsWith("/")) {
			url = "/" + url;
		}
		this.url = url;
	}

	/**
	 * Gets the duration of the Build
	 * 
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of the Build
	 * 
	 * @param duration the duration of the build
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * Gets the timestamp when the Build executed
	 * 
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp when the Build was executed
	 * 
	 * @param timestamp the timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the {@link Result}
	 * 
	 * @return the {@link Result}
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * Sets the {@link Result} for this build
	 * 
	 * @param result the {@link Result}
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Gets the Build {@link State}
	 * 
	 * @return the Build {@link State}
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the Build {@link State}
	 * 
	 * @param state the Build {@link State}
	 */
	public void setState(State state) {
		this.state = state;
	}

}
