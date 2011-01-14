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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Jobs XML object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "JobsType", namespace = NameSpaceUtils.JOB_NAMESPACE)
@XmlRootElement(name = "Jobs", namespace = NameSpaceUtils.JOB_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Jobs extends ListableModel<Job> {

	@XmlElement(name = "job", required = true)
	private Collection<Job> items;

	/**
	 * Default Constructor
	 */
	public Jobs() {
	}

	/**
	 * Getter for jobs
	 * 
	 * @return the jobs
	 */
	public Collection<Job> getJobs() {
		if (items == null) {
			items = new ArrayList<Job>();
		}
		return items;
	}

	/**
	 * Add a single {@link Job} to the collection
	 * 
	 * @param job the {@link Job} to add
	 */
	public void add(Job job) {
		getJobs().add(job);
	}

	/**
	 * Add a {@link Collection} of {@link Job} objects to the {@link Collection}
	 * 
	 * @param jobs the {@link Collection} of {@link Job} objects to add
	 */
	public void addAll(Collection<Job> jobs) {
		getJobs().addAll(jobs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Job> iterator() {
		return getJobs().iterator();
	}

}
