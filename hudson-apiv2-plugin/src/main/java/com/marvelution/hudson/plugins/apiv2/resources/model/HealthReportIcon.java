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

package com.marvelution.hudson.plugins.apiv2.resources.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Enumeration for the icons of available to health reports
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "HealthReportIconType", namespace = NameSpaceUtils.JOB_NAMESPACE)
@XmlEnum(String.class)
public enum HealthReportIcon {

	HEALTH_80_PLUS("health-80plus.gif"),
	HEALTH_60_TO_79("health-60to79.gif"),
	HEALTH_40_TO_59("health-40to59.gif"),
	HEALTH_20_TO_39("health-20to39.gif"),
	HEALTH_00_TO_19("health-00to19.gif");

	private String iconName;

	/**
	 * Constructor
	 * 
	 * @param iconName the icon name
	 */
	private HealthReportIcon(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * Getter for the icon name
	 * 
	 * @return the icon name
	 */
	public String getIconName() {
		return iconName;
	}

}
