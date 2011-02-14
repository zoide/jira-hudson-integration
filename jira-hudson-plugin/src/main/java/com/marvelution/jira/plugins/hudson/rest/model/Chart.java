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

package com.marvelution.jira.plugins.hudson.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.atlassian.jira.charts.jfreechart.ChartHelper;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlType(name = "ChartType", namespace = "http://jira.marvelution.com/hudson")
@XmlRootElement(name = "Chart", namespace = "http://jira.marvelution.com/hudson")
@XmlAccessorType(XmlAccessType.FIELD)
public class Chart {

	@XmlElement
	private String location;
	@XmlElement
	private String imageMapName;
	@XmlElement
	private String imageMap;
	@XmlElement
	private int width;
	@XmlElement
	private int height;

	/**
	 * Constructor
	 * 
	 * @param location the file location of the chart
	 * @param imageMapName the image map name
	 * @param imageMap the image map
	 * @param width the width in pixels
	 * @param height the height in pixels
	 */
	public Chart(String location, String imageMapName, String imageMap, int width, int height) {
		this.location = location;
		this.imageMapName = imageMapName;
		this.imageMap = imageMap;
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor
	 * 
	 * @param chartHelper the {@link ChartHelper} object to get all the data from
	 */
	public Chart(ChartHelper chartHelper) {
		this(chartHelper.getLocation(), chartHelper.getImageMapName(), chartHelper.getImageMap(),
				Double.valueOf(chartHelper.getRenderingInfo().getChartArea().getWidth()).intValue(),
				Double.valueOf(chartHelper.getRenderingInfo().getChartArea().getHeight()).intValue());
	}

	/**
	 * Getter for location
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Setter for location
	 * 
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Getter for imageMapName
	 * 
	 * @return the imageMapName
	 */
	public String getImageMapName() {
		return imageMapName;
	}

	/**
	 * Setter for imageMapName
	 * 
	 * @param imageMapName the imageMapName to set
	 */
	public void setImageMapName(String imageMapName) {
		this.imageMapName = imageMapName;
	}

	/**
	 * Getter for imageMap
	 * 
	 * @return the imageMap
	 */
	public String getImageMap() {
		return imageMap;
	}

	/**
	 * Setter for imageMap
	 * 
	 * @param imageMap the imageMap to set
	 */
	public void setImageMap(String imageMap) {
		this.imageMap = imageMap;
	}

	/**
	 * Getter for width
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Setter for width
	 * 
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Getter for height
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Setter for height
	 * 
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}
