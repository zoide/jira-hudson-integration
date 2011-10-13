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

package com.marvelution.hudson.plugins.apiv2.resources.impl;

import java.util.logging.Logger;

import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.model.Project;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.marvelution.hudson.plugins.apiv2.resources.exceptions.ForbiddenException;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchBuildException;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchJobException;

/**
 * Parent (Base) REST resource for all REST implementations for Hudson
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 */
@Path(BaseRestResource.BASE_REST_URI)
@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
public class BaseRestResource {

	private final Logger log = Logger.getLogger(BaseRestResource.class.getName());

	/**
	 * The base url for all the rest endpoints
	 */
	public static final String BASE_REST_URI = "apiv2";

	/**
	 * Internal method to get the {@link hudson.model.Job} for the given name
	 * 
	 * @param jobName the name of the {@link hudson.model.Job} to get
	 * @return the {@link hudson.model.Job}
	 * @throws NoSuchJobException in case there is no {@link hudson.model.Job} configured with the given name
	 */
	@SuppressWarnings("unchecked")
	protected hudson.model.Job<?, ? extends AbstractBuild<?, ?>> getHudsonJob(String jobName)
				throws NoSuchJobException {
		if (jobName == null || StringUtils.isBlank(jobName)) {
			throw new IllegalArgumentException("Job Name may not me null or empty");
		}
		hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job = Hudson.getInstance().getItemByFullName(jobName,
			hudson.model.Job.class);
		if (job != null) {
			log.fine("Found the job " + job.getFullName() + ", checking permissions");
			if (job.hasPermission(Project.READ)) {
				return job;
			} else {
				throw new ForbiddenException();
			}
		} else {
			// We failed to get the Job, but it could be a module.
			// Loop through all the Jobs to locate it.
			log.fine("Could not find the Job using method getItemByFullName, going to loop through all the Job Items");
			for (hudson.model.Job<?, ? extends AbstractBuild<?, ?>> item : Hudson.getInstance().getAllItems(
					hudson.model.Job.class)) {
				// Only search in the Jobs the user can view
				if (jobName.equals(item.getName()) && item.hasPermission(Project.READ)) {
					return item;
				}
			}
		}
		throw new NoSuchJobException(jobName);
	}

	/**
	 * Internal method to get the {@link AbstractBuild} by the build number and for the {@link hudson.model.Job} given
	 * name
	 * 
	 * @param job the {@link hudson.model.Job} to get the build from
	 * @param number the number of the build to get
	 * @return the {@link AbstractBuild}
	 * @throws NoSuchBuildException in case there is no {@link AbstractBuild} with the given number within the given
	 *         {@link hudson.model.Job}
	 */
	protected AbstractBuild<?, ?> getHudsonBuild(hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job, int number) {
		final AbstractBuild<?, ?> build = job.getBuildByNumber(number);
		if (build != null) {
			log.fine("Found build " + number + " for job " + job.getFullName());
			return build;
		}
		throw new NoSuchBuildException(job.getFullName(), number);
	}

	/**
	 * Internal method to get the {@link AbstractBuild} by the build number and for the Job name given
	 * 
	 * @param job the Job name to get the build from
	 * @param number the number of the build to get
	 * @return the {@link AbstractBuild}
	 * @throws NoSuchBuildException in case there is no {@link AbstractBuild} with the given number within the given
	 *         {@link hudson.model.Job}
	 */
	protected AbstractBuild<?, ?> getHudsonBuild(String job, int number) {
		return getHudsonBuild(getHudsonJob(job), number);
	}

}
