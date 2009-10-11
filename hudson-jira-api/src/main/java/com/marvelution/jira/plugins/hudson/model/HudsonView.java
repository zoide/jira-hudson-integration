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

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Model class for Hudson Views
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("view")
public class HudsonView implements Comparable<HudsonView> {

	private String name;

	private String description;

	@XStreamAlias("jobs")
	private JobsList jobs;

	/**
	 * Default argument less constructor for XStream
	 */
	public HudsonView() {
	}

	/**
	 * Constructor
	 * 
	 * @param name the view name
	 * @param description the view description
	 */
	public HudsonView(String name, String description) {
		setName(name);
		setDescription(description);
	}

	/**
	 * Get the View name
	 * 
	 * @return the View name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the View name
	 * 
	 * @param name the View name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the View description
	 * 
	 * @return the View description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the View description
	 * 
	 * @param description the View description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the Jobs related to the View
	 * 
	 * @return {@link List} of {@link Job} related to the View
	 */
	public List<Job> getJobs() {
		if (jobs == null) {
			jobs = new JobsList();
		}
		return jobs.getJobs();
	}

	/**
	 * Set the Jobs related to the View
	 * 
	 * @param jobs {@link List} of {@link Job} related to the View
	 */
	public void setJobs(List<Job> jobs) {
		this.jobs = new JobsList();
		this.jobs.getJobs().addAll(jobs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HudsonView) {
			return getName().equals(((HudsonView) obj).getName());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(HudsonView other) {
		return getName().compareTo(other.getName());
	}

}
