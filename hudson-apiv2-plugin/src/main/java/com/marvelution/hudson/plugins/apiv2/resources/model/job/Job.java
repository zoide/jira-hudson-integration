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
	@XmlElement(name = "firstBuild")
	private Build firstBuild;
	@XmlElement(name = "lastBuild")
	private Build lastBuild;
	@XmlElement(name = "lastSuccessfulBuild")
	private Build lastSuccessfulBuild;
	@XmlElement(name = "lastUnstableBuild")
	private Build lastUnstableBuild;
	@XmlElement(name = "lastStableBuild")
	private Build lastStableBuild;
	@XmlElement(name = "lastFailedBuild")
	private Build lastFailedBuild;
	@XmlElement(name = "lastCompletedBuild")
	private Build lastCompletedBuild;
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
		if (builds == null) {
			builds = new Builds();
		}
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
	 * Getter for firstBuild
	 *
	 * @return the firstBuild
	 */
	public Build getFirstBuild() {
		return firstBuild;
	}

	/**
	 * Getter for firstBuildNumber
	 *
	 * @return the firstBuildNumber
	 */
	public int getFirstBuildNumber() {
		return firstBuild.getBuildNumber();
	}

	/**
	 * Setter for firstBuild
	 * 
	 * @param firstBuild the firstBuild to set
	 */
	public void setFirstBuild(Build firstBuild) {
		this.firstBuild = firstBuild;
	}

	/**
	 * Getter for lastBuild
	 *
	 * @return the lastBuild
	 */
	public Build getLastBuild() {
		return lastBuild;
	}

	/**
	 * Getter for lastBuildNumber
	 *
	 * @return the lastBuildNumber
	 */
	public int getLastBuildNumber() {
		return lastBuild.getBuildNumber();
	}

	/**
	 * Setter for lastBuild
	 * 
	 * @param lastBuild the lastBuild to set
	 */
	public void setLastBuild(Build lastBuild) {
		this.lastBuild = lastBuild;
	}

	/**
	 * Getter for lastSuccessfulBuild
	 *
	 * @return the lastSuccessfulBuild
	 */
	public Build getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}

	/**
	 * Getter for lastSuccessfulBuildNumber
	 *
	 * @return the lastSuccessfulBuildNumber
	 */
	public int getLastSuccessfulBuildNumber() {
		return lastSuccessfulBuild.getBuildNumber();
	}

	/**
	 * Setter for lastSuccessfulBuild
	 * 
	 * @param lastSuccessfulBuild the lastSuccessfulBuild to set
	 */
	public void setLastSuccessfulBuild(Build lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}

	/**
	 * Getter for lastUnstableBuild
	 *
	 * @return the lastUnstableBuild
	 */
	public Build getLastUnstableBuild() {
		return lastUnstableBuild;
	}

	/**
	 * Getter for lastUnstableBuildNumber
	 *
	 * @return the lastUnstableBuildNumber
	 */
	public int getLastUnstableBuildNumber() {
		return lastUnstableBuild.getBuildNumber();
	}

	/**
	 * Setter for lastUnstableBuild
	 * 
	 * @param lastUnstableBuild the lastUnstableBuild to set
	 */
	public void setLastUnstableBuild(Build lastUnstableBuild) {
		this.lastUnstableBuild = lastUnstableBuild;
	}

	/**
	 * Getter for lastStableBuild
	 *
	 * @return the lastStableBuild
	 */
	public Build getLastStableBuild() {
		return lastStableBuild;
	}

	/**
	 * Getter for lastStableBuildNumber
	 *
	 * @return the lastStableBuildNumber
	 */
	public int getLastStableBuildNumber() {
		return lastStableBuild.getBuildNumber();
	}

	/**
	 * Setter for lastStableBuild
	 * 
	 * @param lastStableBuild the lastStableBuild to set
	 */
	public void setLastStableBuild(Build lastStableBuild) {
		this.lastStableBuild = lastStableBuild;
	}

	/**
	 * Getter for lastFailedBuild
	 *
	 * @return the lastFailedBuild
	 */
	public Build getLastFailedBuild() {
		return lastFailedBuild;
	}

	/**
	 * Getter for lastFailedBuildNumber
	 *
	 * @return the lastFailedBuildNumber
	 */
	public int getLastFailedBuildNumber() {
		return lastFailedBuild.getBuildNumber();
	}

	/**
	 * Setter for lastFailedBuild
	 * 
	 * @param lastFailedBuild the lastFailedBuild to set
	 */
	public void setLastFailedBuild(Build lastFailedBuild) {
		this.lastFailedBuild = lastFailedBuild;
	}

	/**
	 * Getter for lastCompletedBuild
	 *
	 * @return the lastCompletedBuild
	 */
	public Build getLastCompletedBuild() {
		return lastCompletedBuild;
	}

	/**
	 * Getter for lastCompletedBuildNumber
	 *
	 * @return the lastCompletedBuildNumber
	 */
	public int getLastCompletedBuildNumber() {
		return lastCompletedBuild.getBuildNumber();
	}

	/**
	 * Setter for lastCompletedBuild
	 * 
	 * @param lastCompletedBuild the lastCompletedBuild to set
	 */
	public void setLastCompletedBuild(Build lastCompletedBuild) {
		this.lastCompletedBuild = lastCompletedBuild;
	}

	/**
	 * Getter for a specific {@link Build} of the {@link Job}
	 * 
	 * @param number the number of the {@link Build} to get
	 * @return the specific {@link Build}, may be <code>null</code>
	 */
	public Build getBuild(int number) {
		for (Build build : getBuilds()) {
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
		if (modules == null) {
			modules = new Jobs();
		}
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
