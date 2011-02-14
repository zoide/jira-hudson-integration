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
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.wink.common.annotations.Parent;

import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.SearchResource;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Jobs;

/**
 * The {@link SearchResource} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Parent(BaseRestResource.class)
@Path("search")
public class SearchRestResourceImpl extends BaseRestResource implements SearchResource {

	Logger log = Logger.getLogger(SearchResource.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Jobs searchForJobs(String query, boolean nameOnly) {
		Jobs jobs = new Jobs();
		for (hudson.model.Job<?, ?> item : Hudson.getInstance().getAllItems(hudson.model.Job.class)) {
			log.info("Search in Job " + item.getDisplayName());
			if (stringMatchesQuery(query, item.getDisplayName()) || (!nameOnly && stringMatchesQuery(query,
					item.getDescription()))) {
				log.info("ADDING JOB " + item.getFullName());
				jobs.add(DozerUtils.getMapper().map(item, Job.class, "full"));
			}
		}
		return jobs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Builds searchForBuilds(String query, String jobName) {
		Builds builds = new Builds();
		if (StringUtils.isNotBlank(jobName)) {
			return searchForBuilds(getHudsonJob(jobName), query);
		} else {
			for (hudson.model.Job<?, ?> item : Hudson.getInstance().getAllItems(hudson.model.Job.class)) {
				builds.addAll(searchForBuilds(item, query).getItems());
			}
		}
		return builds;
	}

	/**
	 * Search for matching Builds in a Hudson Job that match the given query
	 * 
	 * @param job the Hudson {@link hudson.model.Job} to search through its builds
	 * @param query the query string to search for within the change log of a build
	 * @return the {@link Builds} collection of {@link Build} objects that match the query
	 */
	private Builds searchForBuilds(hudson.model.Job<?, ?> job, String query) {
		Builds builds = new Builds();
		for (Object object : job.getBuilds()) {
			if (object instanceof AbstractBuild<?, ?>) {
				AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) object;
				if (searchThroughChangeLog(build.getChangeSet(), query)) {
					builds.add(DozerUtils.getMapper().map(build, Build.class));
				}
			}
		}
		return builds;
	}

	/**
	 * Search through to the {@link ChangeLogSet} for query string
	 * 
	 * @param changeSet the {@link ChangeLogSet} to search through
	 * @param query the query string to search for in the {@link ChangeLogSet}
	 * @return <code>true</code> if the query string is found in any of the entries in {@link ChangeLogSet} contains
	 *         the query string
	 */
	private boolean searchThroughChangeLog(ChangeLogSet<? extends Entry> changeSet, String query) {
		for (Entry entry : changeSet) {
			if (stringMatchesQuery(query, entry.getMsg())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Internal method to check if the given {@link String} matches the query items
	 * @param query the query {@link String} [] to match
	 * @param searchString the {@link String} to match against
	 * @return <code>true</code> if a query {@link String} is found in the search {@link String}
	 */
	private boolean stringMatchesQuery(String query, String searchString) {
		for (String item : StringUtils.split(query, " ")) {
			log.info("Search for " + item + " in " + searchString);
			if (StringUtils.containsIgnoreCase(searchString, item)) {
				log.info("FOUND IT");
				return true;
			}
		}
		log.info("DIDN'T FOUND IT");
		return false;
	}

}
