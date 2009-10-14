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

package com.marvelution.jira.plugins.hudson.gadgets.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * REST Resource for a {@link HudsonServer}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlRootElement
public class HudsonBuildResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;

	@XmlElement
	private int number;

	@XmlElement
	private String duration;

	@XmlElement
	private String timespan;

	@XmlElement
	private String trigger;

	@XmlElement
	private String result;

	@XmlElement
	private String project;

	@XmlElement
	private String projectUrl;

	/**
	 * Default Constructor
	 */
	public HudsonBuildResource() {
	}

	/**
	 * Constructor
	 * 
	 * @param number the build number
	 * @param duration the formatted duration string
	 * @param timespan the formatted timespan string
	 * @param trigger the formatted trigger
	 * @param result the build result
	 * @param project the project name
	 * @param projectUrl the project url
	 */
	public HudsonBuildResource(int number, String duration, String timespan, String trigger, String result,
								String project, String projectUrl) {
		this.number = number;
		this.duration = duration;
		this.timespan = timespan;
		this.trigger = trigger;
		this.result = result;
		this.project = project;
		this.projectUrl = projectUrl;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @return the timespan
	 */
	public String getTimespan() {
		return timespan;
	}

	/**
	 * @return the trigger
	 */
	public String getTrigger() {
		return trigger;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @return the projectUrl
	 */
	public String getProjectUrl() {
		return projectUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(this, object);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this, HudsonBuildResource.TO_STRING_STYLE);
	}

}
