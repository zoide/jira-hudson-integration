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

import hudson.model.AbstractBuild;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.marvelution.hudson.plugins.apiv2.APIv2Plugin;
import com.marvelution.hudson.plugins.apiv2.cache.issue.IssueCache;
import com.marvelution.hudson.plugins.apiv2.cache.issue.IssueCachePredicates;
import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.SearchResource;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchJobException;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.hudson.plugins.apiv2.utils.JiraKeyUtils;

/**
 * The {@link SearchResource} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Scope(ScopeType.SINGLETON)
@Parent(BaseRestResource.class)
@Path("search")
public class SearchRestResourceImpl extends BaseRestResource implements SearchResource {

	Logger log = Logger.getLogger(SearchResource.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Builds searchForIssues(String[] keys, String jobName) throws NoSuchJobException {
		Builds builds = new Builds();
		Predicate<IssueCache> predicates = getSearchPredicates(keys, jobName);
		Collection<IssueCache> includes = Collections2.filter(APIv2Plugin.getIssuesCache(), predicates);
		Map<String, Set<Integer>> buildsMap = Maps.newHashMap();
		for (IssueCache cache : includes) {
			if (buildsMap.containsKey(cache.getJob())) {
				buildsMap.get(cache.getJob()).add(cache.getBuild());
			} else {
				buildsMap.put(cache.getJob(), Sets.newHashSet(cache.getBuild()));
			}
		}
		for (Entry<String, Set<Integer>> entry : buildsMap.entrySet()) {
			hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job = getHudsonJob(entry.getKey());
			for (Integer number : entry.getValue()) {
				builds.add(DozerUtils.getMapper().map(job.getBuildByNumber(number), Build.class));
			}
		}
		builds.sortBuilds();
		return builds;
	}

	/**
	 * @param keys
	 * @param jobName
	 * @return
	 */
	private Predicate<IssueCache> getSearchPredicates(String[] keys, String jobName) {
		List<Predicate<IssueCache>> issuePredicates = Lists.newArrayList();
		for (String key : keys) {
			if (JiraKeyUtils.isValidProjectKey(key)) {
				issuePredicates.add(IssueCachePredicates.isRelatedToJIRAProject(key));
			} else if (JiraKeyUtils.isValidIssueKey(key)) {
				issuePredicates.add(IssueCachePredicates.isRelatedToJIRAIssue(key));
			}
		}
		if (StringUtils.isNotBlank(jobName)) {
			return Predicates.and(IssueCachePredicates.isRelatedToHudsonJob(jobName), Predicates.or(issuePredicates));
		} else {
			return Predicates.or(issuePredicates);
		}
	}

}
