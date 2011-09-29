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

package com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Project {@link Trigger} implementation
 * {@link Trigger} that indicates that another project triggered the build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "ProjectTriggerType", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createProjectTrigger")
@XmlRootElement(name = "ProjectTrigger", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectTrigger extends Trigger {

	@XmlElement(name = "name", required = true)
	private String name;
	@XmlElement(name = "url", required = true)
	private String url;
	@XmlElement(name = "buildNumber", required = true)
	private int buildNumber;
	
	/**
	 * Getter for name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for name
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
	 * Getter for buildNumber
	 *
	 * @return the buildNumber
	 */
	public int getBuildNumber() {
		return buildNumber;
	}
	
	/**
	 * Setter for buildNumber
	 * 
	 * @param buildNumber the buildNumber to set
	 */
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	/**
	 * Static helper method to create a {@link ProjectTrigger}
	 * 
	 * @param name the project name of the triggering project
	 * @param url the project url of the triggering project
	 * @param buildNumber the build number of the triggering project
	 * @return the {@link ProjectTrigger}
	 */
	public static ProjectTrigger create(String name, String url, int buildNumber) {
		return create(name, url, buildNumber, "");
	}

	/**
	 * Static helper method to create a {@link ProjectTrigger}
	 * 
	 * @param name the project name of the triggering project
	 * @param url the project url of the triggering project
	 * @param buildNumber the build number of the triggering project
	 * @param shortDescription the short description of the trigger
	 * @return the {@link ProjectTrigger}
	 */
	public static ProjectTrigger create(String name, String url, int buildNumber, String shortDescription) {
		ProjectTrigger trigger = new ProjectTrigger();
		trigger.setName(name);
		trigger.setUrl(url);
		trigger.setBuildNumber(buildNumber);
		trigger.setShortDescription(shortDescription);
		return trigger;
	}

}
