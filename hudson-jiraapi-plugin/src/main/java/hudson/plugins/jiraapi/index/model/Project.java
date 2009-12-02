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
 * Model for Jobs in the index
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("project")
public final class Project {

	private String name;

	private String parentName;

	@XStreamImplicit(itemFieldName = "build-number")
	private Set<Integer> buildNumbers = new HashSet<Integer>();

	/**
	 * Constructor
	 * 
	 * @param name the name of the job
	 */
	public Project(String name) {
		this(name, null);
	}

	/**
	 * Constructor
	 * 
	 * @param name the name of the job
	 * @param parentName the name of the job parent
	 */
	public Project(String name, String parentName) {
		setName(name);
		setParentName(parentName);
	}

	/**
	 * Gets the job name
	 * 
	 * @return the job name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the job name
	 * 
	 * @param name the job name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the job parent name
	 * 
	 * @return the job parent name
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * Sets the job parent name
	 * 
	 * @param parentName the job parent name
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * Gets the {@link Set} of build numbers
	 * 
	 * @return the {@link Set} of build numbers
	 */
	public Set<Integer> getBuildNumbers() {
		if (buildNumbers == null) {
			buildNumbers = new HashSet<Integer>();
		}
		return buildNumbers;
	}

	/**
	 * Sets the build numbers
	 * 
	 * @param buildNumbers {@link Set} of build numbers
	 */
	public void setBuildNumbers(Set<Integer> buildNumbers) {
		this.buildNumbers = buildNumbers;
	}

	/**
	 * Merge a {@link Project} into this Job
	 * 
	 * @param toBeMerged the {@link Project} to be merged
	 */
	public void merge(Project toBeMerged) {
		if (toBeMerged != null && this.equals(toBeMerged)) {
			getBuildNumbers().addAll(toBeMerged.getBuildNumbers());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Project) {
			final Project other = (Project) obj;
			return getName().equals(other.getName());
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

}
