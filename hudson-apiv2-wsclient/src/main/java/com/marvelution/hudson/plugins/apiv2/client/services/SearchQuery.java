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

package com.marvelution.hudson.plugins.apiv2.client.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Jobs;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class SearchQuery<MODEL extends Model, LISTMODEL extends ListableModel<MODEL>>
		extends AbstractListableQuery<MODEL, LISTMODEL> {

	protected final String[] query;

	/**
	 * Constructor
	 * 
	 * @param modelClass the <MDL> base type
	 * @param listModelClass the <MDL> Listable type
	 */
	protected SearchQuery(Class<MODEL> modelClass, Class<LISTMODEL> listModelClass, String[] query) {
		super(modelClass, listModelClass);
		this.query = query;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSpecificUrl() {
		final StringBuilder url = new StringBuilder();
		url.append("search/").append(getSearchMethod());
		url.append("?query=").append(urlEncode(StringUtils.join(query, " ")));
		if (getExtraParameters() != null) {
			for (Entry<String, String> entry : getExtraParameters().entrySet()) {
				url.append("&").append(entry.getKey()).append("=").append(urlEncode(entry.getValue()));
			}
		}
		return url.toString();
	}

	/**
	 * Internal method to get the search method used
	 * 
	 * @return the search method name
	 */
	protected abstract String getSearchMethod();

	/**
	 * Internal method to get possible extra parameters
	 * 
	 * @return a {@link Map} of {@link String}, {@link String} containing the extra parameters, may be 
	 * 	       <code>null</code> or <code>empty</code>
	 */
	protected abstract Map<String, String> getExtraParameters();

	/**
	 * Job {@link SearchQuery} implementaiton
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static class JobSearchQuery extends SearchQuery<Job, Jobs> {

		protected boolean nameOnly = false;

		/**
		 * Constructor
		 */
		protected JobSearchQuery(String[] query) {
			super(Job.class, Jobs.class, query);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected String getSearchMethod() {
			return "jobs";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Map<String, String> getExtraParameters() {
			return Collections.singletonMap("nameOnly", String.valueOf(nameOnly));
		}

	}

	/**
	 * Build {@link SearchQuery} implementaiton
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static class BuildSearchQuery extends SearchQuery<Build, Builds> {

		protected String jobName;

		/**
		 * Constructor
		 */
		protected BuildSearchQuery(String[] query) {
			super(Build.class, Builds.class, query);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected String getSearchMethod() {
			return "builds";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Map<String, String> getExtraParameters() {
			return Collections.singletonMap("jobname", jobName);
		}

	}

	/**
	 * Method to create a {@link JobSearchQuery} that will search in both the name and the description
	 * 
	 * @param query the query {@link String} array to search for
	 * @return the {@link JobSearchQuery}
	 * @see #createForJobSearch(String[], boolean)
	 */
	public static JobSearchQuery createForJobSearch(String[] query) {
		return createForJobSearch(query, false);
	}

	/**
	 * Method to create a {@link JobSearchQuery} that will search only in the name
	 * 
	 * @param query the query {@link String} array to search for
	 * @return the {@link JobSearchQuery}
	 * @see #createForJobSearch(String[], boolean)
	 */
	public static JobSearchQuery createForJobSearchNameOnly(String[] query) {
		return createForJobSearch(query, true);
	}

	/**
	 * Method to create a {@link JobSearchQuery} that will search in the name and description based on the
	 * {@link Boolean} flag given
	 * 
	 * @param query the query {@link String} array to search for
	 * @param nameOnly the {@link Boolean} flag to search in; <code>true</code> for name only search,
	 *        <code>false</code> for name and description search
	 * @return the {@link JobSearchQuery}
	 */
	public static JobSearchQuery createForJobSearch(String[] query, boolean nameOnly) {
		JobSearchQuery searchQuery = new JobSearchQuery(query);
		searchQuery.nameOnly = nameOnly;
		return searchQuery;
	}

	/**
	 * Method to create a {@link BuildSearchQuery} that will search in all the builds of all the Hudson jobs
	 * 
	 * @param query the query {@link String} array to search for
	 * @return the {@link BuildSearchQuery}
	 * @see #createForBuildSearch(String[], String)
	 */
	public static BuildSearchQuery createForBuildSearch(String[] query) {
		return createForBuildSearch(query, "");
	}

	/**
	 * Method to create a {@link BuildSearchQuery} that will search in all the builds of all the Hudson jobs
	 * 
	 * @param query the query {@link String} {@link Collection} to search for
	 * @return the {@link BuildSearchQuery}
	 * @see #createForBuildSearch(String[], String)
	 */
	public static BuildSearchQuery createForBuildSearch(Collection<String> query) {
		return createForBuildSearch(query, "");
	}

	/**
	 * Method to create a {@link BuildSearchQuery} that will search in all the builds of a specific Hudson Job given
	 * by name
	 * 
	 * @param query the query {@link String} array to search for
	 * @param jobName the Job name of the specific Hudson job
	 * @return the {@link BuildSearchQuery}
	 */
	public static BuildSearchQuery createForBuildSearch(String[] query, String jobName) {
		BuildSearchQuery searchQuery = new BuildSearchQuery(query);
		searchQuery.jobName = jobName;
		return searchQuery;
	}

	/**
	 * Method to create a {@link BuildSearchQuery} that will search in all the builds of a specific Hudson Job given
	 * by name
	 * 
	 * @param query the query {@link String} {@link Collection} to search for
	 * @param jobName the Job name of the specific Hudson job
	 * @return the {@link BuildSearchQuery}
	 */
	public static BuildSearchQuery createForBuildSearch(Collection<String> query, String jobName) {
		BuildSearchQuery searchQuery = new BuildSearchQuery(query.toArray(new String[query.size()]));
		searchQuery.jobName = jobName;
		return searchQuery;
	}

}
