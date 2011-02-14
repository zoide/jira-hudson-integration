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

package com.marvelution.hudson.plugins.apiv2.resources.model.job;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Job XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "JobType", namespace = NameSpaceUtils.JOB_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createJob")
@XmlRootElement(name = "Job", namespace = NameSpaceUtils.JOB_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Job extends Model {

	@XmlElement(name = "name", required = true)
	private String name;
	@XmlElement(name = "url", required = true)
	private String url;
	@XmlElement(name = "description")
	private String description;
	@XmlElement(name = "buildable", required = true)
	private boolean buildable;
	@XmlElementRef
	private Builds builds;
	@XmlElement(name = "firstBuildNumber")
	private int firstBuildNumber;
	@XmlElement(name = "lastBuildNumber")
	private int lastBuildNumber;
	@XmlElement(name = "lastSuccessfulBuildNumber")
	private int lastSuccessfulBuildNumber;
	@XmlElement(name = "lastUnstableBuildNumber")
	private int lastUnstableBuildNumber;
	@XmlElement(name = "lastStableBuildNumber")
	private int lastStableBuildNumber;
	@XmlElement(name = "lastFailedBuildNumber")
	private int lastFailedBuildNumber;
	@XmlElement(name = "lastCompletedBuildNumber")
	private int lastCompletedBuildNumber;
	@XmlElement(name = "healthReports")
	private HealthReports healthReports;
	@XmlElement(name = "module")
	@XmlElementWrapper(name = "modules")
	private Jobs modules;

	/**
	 * Default Constructor
	 */
	public Job() {
	}

	/**
	 * Constructor
	 * 
	 * @param jobName the Job Name
	 */
	public Job(String jobName) {
		setName(jobName);
	}

	/**
	 * Getter for the name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for url
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter for url
	 * 
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * Getter for buildable
	 *
	 * @return the buildable
	 */
	public boolean isBuildable() {
		return buildable;
	}

	/**
	 * Setter for buildable
	 * 
	 * @param buildable the buildable to set
	 */
	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}

	/**
	 * Getter for builds
	 *
	 * @return the builds
	 */
	public Builds getBuilds() {
		return builds;
	}

	/**
	 * Setter for builds
	 * 
	 * @param builds the builds to set
	 */
	public void setBuilds(Builds builds) {
		this.builds = builds;
	}

	/**
	 * Getter for the first {@link Build} of the {@link Job}
	 * 
	 * @return the first {@link Build}
	 * @see #getBuild(int)
	 */
	public Build getFirstBuild() {
		return getBuild(firstBuildNumber);
	}

	/**
	 * Getter for firstBuildNumber
	 *
	 * @return the firstBuildNumber
	 */
	public int getFirstBuildNumber() {
		return firstBuildNumber;
	}

	/**
	 * Setter for firstBuildNumber
	 * 
	 * @param firstBuildNumber the firstBuildNumber to set
	 */
	public void setFirstBuildNumber(int firstBuildNumber) {
		this.firstBuildNumber = firstBuildNumber;
	}

	/**
	 * Getter for the last {@link Build} of the {@link Job}
	 * 
	 * @return the last {@link Build}
	 * @see #getBuild(int)
	 */
	public Build getLastBuild() {
		return getBuild(lastBuildNumber);
	}

	/**
	 * Getter for lastBuildNumber
	 *
	 * @return the lastBuildNumber
	 */
	public int getLastBuildNumber() {
		return lastBuildNumber;
	}

	/**
	 * Setter for lastBuildNumber
	 * 
	 * @param lastBuildNumber the lastBuildNumber to set
	 */
	public void setLastBuildNumber(int lastBuildNumber) {
		this.lastBuildNumber = lastBuildNumber;
	}

	/**
	 * Getter for the last successful {@link Build} of the {@link Job}
	 * 
	 * @return the last successful {@link Build}
	 * @see #getBuild(int)
	 */
	public Build getLastSuccessfulBuild() {
		return getBuild(lastSuccessfulBuildNumber);
	}

	/**
	 * Getter for lastSuccessfulBuildNumber
	 *
	 * @return the lastSuccessfulBuildNumber
	 */
	public int getLastSuccessfulBuildNumber() {
		return lastSuccessfulBuildNumber;
	}

	/**
	 * Setter for lastSuccessfulBuildNumber
	 * 
	 * @param lastSuccessfulBuildNumber the lastSuccessfulBuildNumber to set
	 */
	public void setLastSuccessfulBuildNumber(int lastSuccessfulBuildNumber) {
		this.lastSuccessfulBuildNumber = lastSuccessfulBuildNumber;
	}

	/**
	 * Getter for the last unstable {@link Build} of the {@link Job}
	 * 
	 * @return the last unstable {@link Build}
	 * @see #getBuild(int)
	 */
	public Build getLastUnstableBuild() {
		return getBuild(lastUnstableBuildNumber);
	}

	/**
	 * Getter for lastUnstableBuildNumber
	 *
	 * @return the lastUnstableBuildNumber
	 */
	public int getLastUnstableBuildNumber() {
		return lastUnstableBuildNumber;
	}

	/**
	 * Setter for lastUnstableBuildNumber
	 * 
	 * @param lastUnstableBuildNumber the lastUnstableBuildNumber to set
	 */
	public void setLastUnstableBuildNumber(int lastUnstableBuildNumber) {
		this.lastUnstableBuildNumber = lastUnstableBuildNumber;
	}

	/**
	 * Getter for the last stable {@link Build} of the {@link Job}
	 * 
	 * @return the last stable {@link Build}
	 * @see #getBuild(int)
	 */
	public Build getLastStableBuild() {
		return getBuild(lastStableBuildNumber);
	}

	/**
	 * Getter for lastStableBuildNumber
	 *
	 * @return the lastStableBuildNumber
	 */
	public int getLastStableBuildNumber() {
		return lastStableBuildNumber;
	}

	/**
	 * Setter for lastStableBuildNumber
	 * 
	 * @param lastStableBuildNumber the lastStableBuildNumber to set
	 */
	public void setLastStableBuildNumber(int lastStableBuildNumber) {
		this.lastStableBuildNumber = lastStableBuildNumber;
	}

	/**
	 * Getter for the last failed {@link Build} of the {@link Job}
	 * 
	 * @return the last failed {@link Build}
	 * @see #getBuild(int)
	 */
	public Build getLastFailedBuild() {
		return getBuild(lastFailedBuildNumber);
	}

	/**
	 * Getter for lastFailedBuildNumber
	 *
	 * @return the lastFailedBuildNumber
	 */
	public int getLastFailedBuildNumber() {
		return lastFailedBuildNumber;
	}

	/**
	 * Setter for lastFailedBuildNumber
	 * 
	 * @param lastFailedBuildNumber the lastFailedBuildNumber to set
	 */
	public void setLastFailedBuildNumber(int lastFailedBuildNumber) {
		this.lastFailedBuildNumber = lastFailedBuildNumber;
	}

	/**
	 * Getter for the last completed {@link Build} of the {@link Job}
	 * 
	 * @return the last completed {@link Build}
	 * @see #getBuild(int)
	 */
	public Build getLastCompletedBuild() {
		return getBuild(lastCompletedBuildNumber);
	}

	/**
	 * Getter for lastCompletedBuildNumber
	 *
	 * @return the lastCompletedBuildNumber
	 */
	public int getLastCompletedBuildNumber() {
		return lastCompletedBuildNumber;
	}

	/**
	 * Setter for lastCompletedBuildNumber
	 * 
	 * @param lastCompletedBuildNumber the lastCompletedBuildNumber to set
	 */
	public void setLastCompletedBuildNumber(int lastCompletedBuildNumber) {
		this.lastCompletedBuildNumber = lastCompletedBuildNumber;
	}

	/**
	 * Getter for a specific {@link Build} of the {@link Job}
	 * 
	 * @param number the number of the {@link Build} to get
	 * @return the specific {@link Build}, may be <code>null</code>
	 */
	public Build getBuild(int number) {
		if (number < 1) {
			throw new IllegalArgumentException("The build number must be equal or larger then 1 (one)");
		}
		for (Build build : builds) {
			if (build.getBuildNumber() == number) {
				return build;
			}
		}
		return null;
	}
	
	/**
	 * Getter for healthReports
	 *
	 * @return the healthReports
	 */
	public HealthReports getHealthReports() {
		return healthReports;
	}

	/**
	 * Setter for healthReports
	 * 
	 * @param healthReports the healthReports to set
	 */
	public void setHealthReports(HealthReports healthReports) {
		this.healthReports = healthReports;
	}

	/**
	 * Getter for modules
	 *
	 * @return the modules
	 */
	public Jobs getModules() {
		return modules;
	}

	/**
	 * Setter for modules
	 * 
	 * @param modules the modules to set
	 */
	public void setModules(Jobs modules) {
		this.modules = modules;
	}

}
