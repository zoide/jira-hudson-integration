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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * HealthReport XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "HealthReportType", namespace = NameSpaceUtils.JOB_NAMESPACE)
@XmlRootElement(name = "HealthReport", namespace = NameSpaceUtils.JOB_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class HealthReport extends Model implements Comparable<HealthReport> {

	public static final HealthReport NO_RECENT_BUILD = new HealthReport("No recent build failures",
		HealthReportIcon.HEALTH_OVER_80, 100);

	@XmlElement(name = "description")
	private String description;
	@XmlElement(name = "icon", required = true)
	private HealthReportIcon icon;
	@XmlElement(name = "score", required = true)
	private int score;

	/**
	 * Default Constructor
	 */
	public HealthReport() {
	}

	/**
	 * Constructor
	 * 
	 * @param description the description the health report
	 * @param icon the icon name
	 * @param score the score of the health report
	 */
	public HealthReport(String description, HealthReportIcon icon, int score) {
		this.description = description;
		this.icon = icon;
		this.score = score;
	}

	/**
	 * Getter for description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for icon
	 *
	 * @return the icon
	 */
	public HealthReportIcon getIcon() {
		return icon;
	}

	/**
	 * Setter for icon
	 * 
	 * @param icon the icon to set
	 */
	public void setIcon(HealthReportIcon icon) {
		this.icon = icon;
	}

	/**
	 * Getter for score
	 *
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Setter for score
	 * 
	 * @param score the score to set
	 */
	public void setScore(int score) {
		if (score < 0 || score > 100) {
			throw new IllegalArgumentException(
				"Invalid score value given. Value may only be 0 or larger up to 100 or smaller");
		}
		this.score = score;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(HealthReport o) {
		if (getScore() == o.getScore()) {
			return 0;
		} else if (getScore() < o.getScore()) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * Static method implementation to get the smallest of the two {@link HealthReport} objects given
	 * 
	 * @param a {@link HealthReport} a
	 * @param b {@link HealthReport} b
	 * @return the {@link HealthReport} with the smallest score value
	 */
	public static HealthReport min(HealthReport a, HealthReport b) {
		if (a == null && b == null) {
			return null;
		} else if (a == null) {
			return b;
		} else if (b == null) {
			return a;
		} else if (a.compareTo(b) <= 0) {
			return a;
		} else {
			return b;
		}
	}

	/**
	 * Static method implementation to get the largest of the two {@link HealthReport} objects given
	 * 
	 * @param a {@link HealthReport} a
	 * @param b {@link HealthReport} b
	 * @return the {@link HealthReport} with the largest score value
	 */
	public static HealthReport max(HealthReport a, HealthReport b) {
		if (a == null && b == null) {
			return null;
		} else if (a == null) {
			return b;
		} else if (b == null) {
			return a;
		} else if (a.compareTo(b) >= 0) {
			return a;
		} else {
			return b;
		}
	}

}
