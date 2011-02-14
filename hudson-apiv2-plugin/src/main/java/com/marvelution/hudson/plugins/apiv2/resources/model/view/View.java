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

package com.marvelution.hudson.plugins.apiv2.resources.model.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Jobs;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "ViewType", namespace = NameSpaceUtils.VIEW_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createViews")
@XmlRootElement(name = "View", namespace = NameSpaceUtils.VIEW_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class View extends Model {

	@XmlElement(name = "name", required = true)
	private String name;
	@XmlElement(name = "description")
	private String description;
	@XmlElement(name = "url")
	private String url;
	@XmlElementRef
	private Jobs jobs;
	
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
	 * Getter for jobs
	 *
	 * @return the jobs
	 */
	public Jobs getJobs() {
		return jobs;
	}
	
	/**
	 * Setter for jobs
	 * 
	 * @param jobs the jobs to set
	 */
	public void setJobs(Jobs jobs) {
		this.jobs = jobs;
	}

}
