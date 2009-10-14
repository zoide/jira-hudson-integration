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
public class HudsonServerResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;

	@XmlElement
	private String name;

	@XmlElement
	private String url;

	@XmlElement
	private String imageUrl;

	/**
	 * Default Constructor
	 */
	public HudsonServerResource() {
	}

	/**
	 * Constructor
	 * 
	 * @param name the Server name
	 * @param url the Server url
	 * @param imageUrl the Image url of the Server
	 */
	public HudsonServerResource(String name, String url, String imageUrl) {
		this.name = name;
		this.url = url;
		this.imageUrl = imageUrl;
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
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
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
		return ToStringBuilder.reflectionToString(this, HudsonServerResource.TO_STRING_STYLE);
	}

}
