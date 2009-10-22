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

package hudson.plugins.jiraapi.index.model;

import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Model for Issues in the index
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("issue")
public final class Issue {

	private String key;

	@XStreamImplicit(itemFieldName = "project")
	private Set<Project> projects = new HashSet<Project>();

	/**
	 * Constructor
	 * 
	 * @param key the key of the issue
	 */
	public Issue(String key) {
		setKey(key);
	}

	/**
	 * Gets the issue key
	 * 
	 * @return the issue key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the issue key
	 * 
	 * @param key the issue key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the {@link Set} of related {@link Project}
	 * 
	 * @return the {@link Set} of related {@link Project} objects
	 */
	public Set<Project> getProjects() {
		return projects;
	}

	/**
	 * Sets the {@link Set} of related {@link Project}
	 * 
	 * @param projects the {@link Set} of related {@link Project} objects
	 */
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	/**
	 * Merge a given {@link Issue} into this {@link Issue}
	 * 
	 * @param toBeMerged the {@link Issue} to merge
	 */
	public void merge(Issue toBeMerged) {
		if (toBeMerged != null && this.equals(toBeMerged)) {
			for (Project mergeJob : toBeMerged.getProjects()) {
				if (getProjects().contains(mergeJob)) {
					for (Project job : getProjects()) {
						if (job.equals(mergeJob)) {
							job.merge(mergeJob);
						}
					}
				} else {
					getProjects().add(mergeJob);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Issue) {
			final Issue other = (Issue) obj;
			return getKey().equals(other.getKey());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getKey();
	}

}
