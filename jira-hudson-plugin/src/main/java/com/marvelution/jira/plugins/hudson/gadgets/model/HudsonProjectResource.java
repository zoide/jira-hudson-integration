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

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Hudson Project Resource
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlRootElement
public class HudsonProjectResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SIMPLE_STYLE;

	@XmlElement
	private String name;

	@XmlElement
	private String url;

	@XmlElement
	private String description;

	@XmlElement
	private Collection<HudsonBuildResource> builds;

	/**
	 * Default Constructor
	 */
	public HudsonProjectResource() {
	}

	/**
	 * Constructor
	 * 
	 * @param name the project name
	 * @param url the project url
	 * @param descrption the project description
	 */
	public HudsonProjectResource(String name, String url, String descrption) {
		this.name = name;
		this.url = url;
		this.description = descrption;
		builds = new ArrayList<HudsonBuildResource>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the builds
	 */
	public Collection<HudsonBuildResource> getBuilds() {
		return builds;
	}

	/**
	 * Add a {@link HudsonBuildResource} to the project
	 * 
	 * @param build the {@link HudsonBuildResource}
	 */
	public void addBuild(HudsonBuildResource build) {
		builds.add(build);
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
		return ToStringBuilder.reflectionToString(this, HudsonProjectResource.TO_STRING_STYLE);
	}

}
