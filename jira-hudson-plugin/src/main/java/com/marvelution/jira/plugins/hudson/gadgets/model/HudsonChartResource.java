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

import com.marvelution.jira.plugins.hudson.chart.HudsonChart;

/**
 * Chart Resource
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlRootElement
public class HudsonChartResource {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SIMPLE_STYLE;

	@XmlElement
	private boolean generated;

	@XmlElement
	private String location;

	@XmlElement
	private String imageMap;

	@XmlElement
	private String imageMapName;

	@XmlElement
	private int width;

	@XmlElement
	private int height;

	/**
	 * Default Constructor
	 */
	public HudsonChartResource() {
	}

	/**
	 * Constructor
	 * 
	 * @param chart the {@link HudsonChart}
	 */
	public HudsonChartResource(HudsonChart chart) {
		this(chart.isGenerated(), chart.getLocation(), chart.getImageMap(), chart.getImageMapName(), chart
			.getWidth(), chart.getHeight());
	}

	/**
	 * Constructor
	 * 
	 * @param generated flag if the {@link HudsonChart} is generated
	 * @param location the location of the Chart
	 * @param imageMap the image map of the chart
	 * @param imageMapName the image map name
	 * @param width the width of the chart
	 * @param height the height of the chart
	 */
	public HudsonChartResource(boolean generated, String location, String imageMap, String imageMapName,
								int width, int height) {
		this.generated = generated;
		this.location = location;
		this.imageMap = imageMap;
		this.imageMapName = imageMapName;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the generated
	 */
	public boolean isGenerated() {
		return generated;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return the imageMap
	 */
	public String getImageMap() {
		return imageMap;
	}

	/**
	 * @return the imageMapName
	 */
	public String getImageMapName() {
		return imageMapName;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
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
		return ToStringBuilder.reflectionToString(this, HudsonChartResource.TO_STRING_STYLE);
	}

}
