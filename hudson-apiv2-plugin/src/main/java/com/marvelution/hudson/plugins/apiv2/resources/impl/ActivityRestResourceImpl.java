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
import hudson.model.Hudson;
import hudson.model.Project;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.marvelution.hudson.plugins.apiv2.APIv2Plugin;
import com.marvelution.hudson.plugins.apiv2.cache.activity.ActivityCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.BuildActivityCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.JobActivityCache;
import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.ActivityResource;
import com.marvelution.hudson.plugins.apiv2.resources.model.User;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activities;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity.BuildActivity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity.JobActivity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.ActivityType;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;

/**
 * The REST implementation of the {@link ActivityResource} interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
@Scope(ScopeType.SINGLETON)
@Parent(BaseRestResource.class)
@Path("activity")
public class ActivityRestResourceImpl extends BaseRestResource implements ActivityResource {

	private final Logger LOGGER = Logger.getLogger(ActivityRestResourceImpl.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Activities getActivities(ActivityType[] types, String[] jobs, String[] userIds, int maxResults) {
		Activities activities = new Activities();
		if (ArrayUtils.isEmpty(types)) {
			types = ActivityType.values();
		}
		for (ActivityCache cache : APIv2Plugin.getActivitiesCache().getSortedActivities()) {
			try {
				hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job = getHudsonJob(cache.getJob());
				if ((ArrayUtils.isEmpty(jobs) || ArrayUtils.contains(jobs, job.getFullName()))
					&& (ArrayUtils.isEmpty(userIds) || ArrayUtils.contains(userIds, cache.getCulprit()))
					&& job.hasPermission(Project.READ)) {
					if (cache instanceof JobActivityCache && ArrayUtils.contains(types, ActivityType.JOB)) {
						activities.add(getActivityFromCache((JobActivityCache) cache, job));
					} else if (cache instanceof BuildActivityCache && ArrayUtils.contains(types, ActivityType.BUILD)) {
						activities.add(getActivityFromCache((BuildActivityCache) cache, job));
					}
					if (activities.size() == maxResults) {
						// We have the maximum number of results wanted
						break;
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING,
					"Failed to add ActivityCache object " + cache.toString() + ". Reason: " + e.getMessage());
			}
		}
		return activities;
	}

	/**
	 * Get a {@link JobActivity} object from the given {@link JobActivityCache} and {@link hudson.model.Job}
	 * 
	 * @param cache the {@link JobActivityCache}
	 * @param job the {@link hudson.model.Job}
	 * @return the {@link JobActivity}
	 */
	public Activity getActivityFromCache(JobActivityCache cache,
					hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job) {
		JobActivity activity = new JobActivity();
		activity.setUri(URI.create(Hudson.getInstance().getRootUrl() + job.getUrl()));
		activity.setTimestamp(cache.getTimestamp());
		activity.setSystem(HudsonPluginUtils.getHudsonSystem());
		activity.setUser(getUser(cache.getCulprit()));
		activity.setJob(DozerUtils.getMapper().map(job, Job.class, DozerUtils.ACTIVITY_MAP_ID));
		return activity;
	}

	/**
	 * Get a {@link BuildActivity} object from the given {@link BuildActivityCache} and {@link hudson.model.Job}
	 * 
	 * @param cache the {@link BuildActivityCache}
	 * @param job the {@link hudson.model.Job}
	 * @return the {@link BuildActivity}
	 */
	public Activity getActivityFromCache(BuildActivityCache cache,
					hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job) {
		AbstractBuild<?, ?> build = getHudsonBuild(job, cache.getBuild());
		BuildActivity activity = new BuildActivity();
		activity.setUri(URI.create(Hudson.getInstance().getRootUrl() + build.getUrl()));
		activity.setSystem(HudsonPluginUtils.getHudsonSystem());
		activity.setBuild(DozerUtils.getMapper().map(build, Build.class));
		activity.setTimestamp(build.getTimeInMillis());
		User user = getUser(cache.getCulprit());
		if (User.UNKNOWN.equals(user)) {
			user = activity.getSystem().getSystemUser();
		}
		activity.setUser(user);
		return activity;
	}

	/**
	 * Get the {@link User} for a given userId or username
	 * 
	 * @param culprit the userId or username
	 * @return the {@link User}, can be {@link User#ANONYMOUS} or {@link User#UNKNOWN} but never <code>null</code>
	 */
	public User getUser(String culprit) {
		if (User.ANONYMOUS.getUserId().equals(culprit)) {
			return User.ANONYMOUS;
		}
		hudson.model.User user = hudson.model.User.get(culprit, false);
		if (user == null) {
			return User.UNKNOWN;
		} else {
			return DozerUtils.getMapper().map(user, User.class);
		}
	}

}
