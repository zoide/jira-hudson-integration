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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;

/**
 * Base SearchQuery implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class SearchQuery<MODEL extends Model, LISTMODEL extends ListableModel<MODEL>>
		extends AbstractListableQuery<MODEL, LISTMODEL> {

	/**
	 * Constructor
	 * 
	 * @param modelClass the <MODEL> base type
	 * @param listModelClass the <LISTMODEL> Listable type
	 */
	protected SearchQuery(Class<MODEL> modelClass, Class<LISTMODEL> listModelClass) {
		super(modelClass, listModelClass, QueryType.GET);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSpecificUrl() {
		final StringBuilder url = new StringBuilder();
		url.append("search/").append(getSearchMethod()).append("?");
		if (getParameters() != null) {
			for (Entry<String, Object> entry : getParameters().entrySet()) {
				addUrlParameter(url, entry.getKey(), entry.getValue());
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
	 * Internal method to get possible parameters
	 * 
	 * @return a {@link Map} of {@link String}, {@link Object} containing the parameters, may be 
	 * 	       <code>null</code> or <code>empty</code>
	 */
	protected abstract Map<String, Object> getParameters();

	/**
	 * Build {@link SearchQuery} implementation that searches by Issue keys
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 *
	 * @since 4.4.0
	 */
	public static class IssueSearchQuery extends SearchQuery<Build, Builds> {

		private String[] keys;
		private String job;

		/**
		 * Constructor
		 *
		 * @param modelClass
		 * @param listModelClass
		 */
		protected IssueSearchQuery(String[] keys, String job) {
			super(Build.class, Builds.class);
			this.keys = keys;
			this.job = job;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected String getSearchMethod() {
			return "issues";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Map<String, Object> getParameters() {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("key", keys);
			if (StringUtils.isNotBlank(job)) {
				params.put("job", job);
			}
			return params;
		}

	}

	/**
	 * Method to create a {@link IssueSearchQuery} that will search in all the builds
	 * 
	 * @param query the query {@link String} array to search for
	 * @return the {@link IssueSearchQuery}
	 */
	public static IssueSearchQuery createForIssueSearch(String[] query) {
		return createForIssueSearch(query, null);
	}

	/**
	 * Method to create a {@link IssueSearchQuery} that will search in all the builds of a specific Hudson Job given
	 * by name
	 * 
	 * @param query the query {@link String} array to search for
	 * @param jobName the Job name of the specific Hudson job, may be <code>null</code>
	 * @return the {@link IssueSearchQuery}
	 */
	public static IssueSearchQuery createForIssueSearch(String[] query, String jobName) {
		return new IssueSearchQuery(query, jobName);
	}

	/**
	 * Method to create a {@link IssueSearchQuery} that will search in all the builds
	 * 
	 * @param query the query {@link String} {@link Collection} to search for
	 * @return the {@link IssueSearchQuery}
	 */
	public static IssueSearchQuery createForIssueSearch(Collection<String> query) {
		return createForIssueSearch(query, null);
	}

	/**
	 * Method to create a {@link IssueSearchQuery} that will search in all the builds of a specific Hudson Job given
	 * by name
	 * 
	 * @param query the query {@link String} {@link Collection} to search for
	 * @param jobName the Job name of the specific Hudson job, may be <code>null</code>
	 * @return the {@link IssueSearchQuery}
	 */
	public static IssueSearchQuery createForIssueSearch(Collection<String> query, String jobName) {
		return new IssueSearchQuery(query.toArray(new String[query.size()]), jobName);
	}

}
