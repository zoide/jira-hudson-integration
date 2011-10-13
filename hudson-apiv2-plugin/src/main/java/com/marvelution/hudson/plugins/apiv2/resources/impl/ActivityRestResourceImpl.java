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
import hudson.model.Cause.UserCause;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.ActivityResource;
import com.marvelution.hudson.plugins.apiv2.resources.model.HudsonSystem;
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

	private final Logger log = Logger.getLogger(ActivityRestResourceImpl.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Activities getActivities(ActivityType[] types, String[] jobs, String[] userIds, int maxResults) {
		Activities activities = new Activities();
		if (types == null || types.length == 0) {
			types = ActivityType.values();
		}
		for (hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job : getFilteredHudsonJobs(jobs)) {
			for (ActivityType type : types) {
				switch (type) {
					case JOB:
						activities.add(getNewJobActivity(job));
						break;
					case BUILD:
						activities.addAll(getBuildActivities(job));
						break;
				}
			}
		}
		Collections.sort((List<Activity>) activities.getItems());
		Collections.reverse((List<Activity>) activities.getItems());
		if (activities.size() <= maxResults) {
			return activities;
		} else {
			Activities subset = new Activities();
			subset.addAll(((List<Activity>) activities.getItems()).subList(0, maxResults));
			return subset;
		}
	}

	/**
	 * Get all the Hudson jobs that match the filter
	 * 
	 * @param jobs the array of job name filters, if <code>null</code> or <code>empty</code> then all the jobs the
	 * 			user has read access to are returned. Otherwise the job name must be in the array and the user must
	 * 			have read access to the project. If a job name in the filter has a ! in front of it that is is a not
	 * 			include statement.
	 * @return the {@link List} of Hudson Jobs
	 */
	private List<hudson.model.Job<?, ? extends AbstractBuild<?, ?>>> getFilteredHudsonJobs(String[] jobs) {
		List<hudson.model.Job<?, ? extends AbstractBuild<?, ?>>> hudsonJobs =
			new ArrayList<hudson.model.Job<?,? extends AbstractBuild<?, ?>>>();
		if (jobs == null || jobs.length == 0) {
			log.fine("No job filter. return all the Hudson Jobs that we have read permissions on");
			for (hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job : Hudson.getInstance().getAllItems(
					hudson.model.Job.class)) {
				if (job.hasPermission(Project.READ)) {
					hudsonJobs.add(job);
				}
			}
		} else {
			log.fine("Using job filter '" + StringUtils.join(jobs, ", ")
				+ "' to get job that match and where we have read permissions on");
			for (hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job : Hudson.getInstance().getAllItems(
					hudson.model.Job.class)) {
				if (ArrayUtils.contains(jobs, "!" + job.getFullName())) {
					// Exclude this job as it is a not include filter
					break;
				} else if (ArrayUtils.contains(jobs, job.getFullName()) && job.hasPermission(Project.READ)) {
					hudsonJobs.add(job);
				}
			}
		}
		return hudsonJobs;
	}

	/**
	 * Get an {@link Activity} for the Job creation activity
	 * 
	 * @param job the {@link hudson.model.Job} to get the {@link Activity} from
	 * @return the {@link Activity}
	 */
	private Activity getNewJobActivity(hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job) {
		JobActivity activity = new JobActivity();
		activity.setUri(URI.create(Hudson.getInstance().getRootUrl() + job.getUrl()));
		activity.setTimestamp(job.getConfigFile().getFile().lastModified());
		activity.setSystem(HudsonPluginUtils.getHudsonSystem());
		// TODO Get the user that modified/created the Job
		activity.setUser(User.UNKNOWN);
		activity.setJob(DozerUtils.getMapper().map(job, Job.class, DozerUtils.ACTIVITY_MAP_ID));
		return activity;
	}

	/**
	 * Get the {@link Activities} for the Job
	 * 
	 * @param job the {@link hudson.model.Job} to get the build {@link Activities} from
	 * @return the {@link Activities}
	 */
	private Activities getBuildActivities(hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job) {
		HudsonSystem system = HudsonPluginUtils.getHudsonSystem();
		Activities activities = new Activities();
		for (AbstractBuild<?, ?> build : job.getBuilds()) {
			BuildActivity activity = new BuildActivity();
			activity.setUri(URI.create(Hudson.getInstance().getRootUrl() + build.getUrl()));
			activity.setSystem(system);
			activity.setBuild(DozerUtils.getMapper().map(build, Build.class));
			activity.setTimestamp(build.getTimeInMillis());
			UserCause cause = build.getCause(UserCause.class);
			if (cause != null) {
				hudson.model.User user = Hudson.getInstance().getUser(cause.getUserName());
				if (user == null || User.ANONYMOUS.equals(user.getId())) {
					activity.setUser(User.ANONYMOUS);
				} else {
					activity.setUser(DozerUtils.getMapper().map(user, User.class));
				}
			} else {
				activity.setUser(system.getSystemUser());
			}
			activities.add(activity);
		}
		return activities;
	}

}
