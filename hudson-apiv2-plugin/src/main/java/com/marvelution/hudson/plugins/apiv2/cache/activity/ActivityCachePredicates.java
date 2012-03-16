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

package com.marvelution.hudson.plugins.apiv2.cache.activity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.ActivityType;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.5.0
 */
public class ActivityCachePredicates {

	public static Predicate<ActivityCache> relatesToJobs(final String[] jobnames) {
		List<Predicate<ActivityCache>> predicates = Lists.newArrayList();
		for (final String jobname : jobnames) {
			if (StringUtils.isNotBlank(jobname)) {
				predicates.add(relatesToJob(jobname));
			}
		}
		return Predicates.or(predicates);
	}

	public static Predicate<ActivityCache> relatesToJob(final String jobname) {
		Preconditions.checkNotNull(jobname, "jobname is null");
		return new Predicate<ActivityCache>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean apply(ActivityCache input) {
				return input != null && input.getJob().equals(jobname);
			}
		};
	}

	public static Predicate<ActivityCache> relatesToUsers(final String[] usernames) {
		List<Predicate<ActivityCache>> predicates = Lists.newArrayList();
		for (final String username : usernames) {
			if (StringUtils.isNotBlank(username)) {
				predicates.add(relatesToUser(username));
			}
		}
		return Predicates.or(predicates);
	}

	public static Predicate<ActivityCache> relatesToUser(final String username) {
		Preconditions.checkNotNull(username, "username is null");
		return new Predicate<ActivityCache>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean apply(ActivityCache input) {
				return input != null && input.getCulprit().equals(username);
			}
		};
	}

	public static Predicate<ActivityCache> isActivity(final ActivityType[] types) {
		List<Predicate<ActivityCache>> predicates = Lists.newArrayList();
		for (final ActivityType type : types) {
			if (type != null) {
				predicates.add(isActivity(type));
			}
		}
		return Predicates.or(predicates);
	}

	public static Predicate<ActivityCache> isActivity(final ActivityType type) {
		Preconditions.checkNotNull(type, "type is null");
		if (ActivityType.BUILD.equals(type)) {
			return isBuildActivity();
		} else if (ActivityType.JOB.equals(type)) {
			return isJobActivity();
		} else {
			throw new IllegalArgumentException("ActivityType " + type + " is not supported");
		}
	}

	public static Predicate<ActivityCache> isBuildActivity() {
		return new Predicate<ActivityCache>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean apply(ActivityCache input) {
				return input instanceof BuildActivityCache;
			}
		};
	}

	public static Predicate<ActivityCache> isJobActivity() {
		return new Predicate<ActivityCache>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean apply(ActivityCache input) {
				return input instanceof JobActivityCache && !(input instanceof BuildActivityCache);
			}
		};
	}

}
