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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Model class for HeathReports
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("healthReport")
public class HealthReport implements Comparable<HealthReport> {

	public static final HealthReport NO_RECENT_BUILDS =
		new HealthReport("No recent build failures", "health-80plus.gif", 100);

	private String description;

	private String icon;

	private int score;

	/**
	 * Constructor
	 * 
	 * @param description the description
	 * @param icon the icon name
	 * @param score the heath score
	 */
	public HealthReport(String description, String icon, int score) {
		setDescription(description);
		setIcon(icon);
		setScore(score);
	}

	/**
	 * Gets the description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 * 
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the heath icon
	 * 
	 * @return the icon name
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the heath icon
	 * 
	 * @param icon the icon name
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the heath score
	 * 
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the heath score
	 * 
	 * @param score the score 0 - 100
	 */
	public void setScore(int score) {
		if (score < 0 || score > 100) {
			throw new IllegalArgumentException("score must be between 0 and 100 percent");
		}
		this.score = score;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(HealthReport other) {
		return new Integer(getScore()).compareTo(new Integer(other.getScore()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof HealthReport) {
			final HealthReport other = (HealthReport) obj;
			return getDescription().equals(other.getDescription())
				&& Integer.valueOf(getScore()).equals(Integer.valueOf(other.getScore()))
				&& getIcon().equals(other.getIcon());
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Integer.valueOf(getScore()).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getDescription();
	}

}
