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

import hudson.model.Hudson;
import hudson.model.Project;

import javax.ws.rs.Path;

import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.JobResource;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchJobException;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Jobs;

/**
 * The {@link JobResource} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@Scope(ScopeType.SINGLETON)
@Parent(BaseRestResource.class)
@Path("jobs")
public class JobResourceRestImpl extends BaseRestResource implements JobResource {

	private final Logger log = Logger.getLogger(JobResourceRestImpl.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Job getJob(String name, Boolean includeAllBuilds) {
		hudson.model.Job<?, ?> job = getHudsonJob(name);
		if (job != null) {
			log.fine("Found job with name: " + name);
			return mapJob(job, DozerUtils.FULL_MAP_ID, includeAllBuilds);
		}
		throw new NoSuchJobException(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Job getJobStatus(String name, Boolean includeAllBuilds) throws NoSuchJobException {
		hudson.model.Job<?, ?> job = getHudsonJob(name);
		if (job != null) {
			log.fine("Found job with name: " + name);
			return mapJob(job, DozerUtils.FULL_MAP_ID, includeAllBuilds);
		}
		throw new NoSuchJobException(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Jobs getJobs(Boolean includeAllBuilds) {
		Jobs jobs = new Jobs();
		for (hudson.model.Job<?, ?> item : Hudson.getInstance().getAllItems(hudson.model.Job.class)) {
			log.fine("Found job with name: " + item.getFullName() + " checking permissions");
			if (item.hasPermission(Project.READ)) {
				jobs.add(mapJob(item, DozerUtils.FULL_MAP_ID, includeAllBuilds));
			}
		}
		return jobs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Jobs listJobs(Boolean nameOnly, Boolean includeAllBuilds) {
		String mapperContext = DozerUtils.FULL_MAP_ID;
		if (nameOnly != null && Boolean.TRUE.equals(nameOnly)) {
			mapperContext = DozerUtils.NAMEONLY_MAP_ID;
			includeAllBuilds = Boolean.FALSE;
		}
		Jobs jobs = new Jobs();
		for (hudson.model.Job<?, ?> item : Hudson.getInstance().getAllItems(hudson.model.Job.class)) {
			log.fine("Found job with name: " + item.getFullName() + " checking permissions");
			if (item.hasPermission(Project.READ)) {
				jobs.add(mapJob(item, mapperContext, includeAllBuilds));
			}
		}
		return jobs;
	}

	/**
	 * Internal method to map a given Hudson {@link hudson.model.Job} to a {@link Job}
	 * 
	 * @param item the {@link hudson.model.Job} to map
	 * @param mapperContext the mapper context
	 * @param includeAllBuilds flag to include all the builds or just the first and latest
	 * @return the {@link Job}
	 */
	private Job mapJob(hudson.model.Job<?, ?> item, String mapperContext, Boolean includeAllBuilds) {
		Job job = DozerUtils.getMapper().map(item, Job.class, mapperContext);
		if (includeAllBuilds != null && Boolean.TRUE.equals(includeAllBuilds)) {
			log.fine("Adding all builds to mapped job " + job.getName());
			job.setBuilds(DozerUtils.getMapper().map(item.getBuilds(), Builds.class));
		}
		return job;
	}

}
