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
import hudson.model.Run;

import javax.ws.rs.Path;

import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.BuildResource;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchBuildException;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchJobException;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;

/**
 * The {@link BuildResource} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@Scope(ScopeType.SINGLETON)
@Parent(BaseRestResource.class)
@Path("builds")
public class BuildResourceRestImpl extends BaseRestResource implements BuildResource {

	private final Logger log = Logger.getLogger(BuildResourceRestImpl.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getBuild(String jobName, Integer buildNumber) {
		return DozerUtils.getMapper().map(getHudsonBuild(jobName, buildNumber), Build.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Builds getBuilds(String jobName, Integer offset, Integer count) {
		Builds builds = new Builds();
		hudson.model.Job<?, ? extends AbstractBuild<?, ?>> job = getHudsonJob(jobName);
		if (offset > -1 && count > 0) {
			log.fine("Mapping " + count + " builds from " + offset + " of job " + job.getFullName());
			AbstractBuild<?, ?> build = job.getBuildByNumber(offset);
			if (build != null) {
				do {
					builds.add(DozerUtils.getMapper().map(build, Build.class));
					build = build.getNextBuild();
				} while (build != null && builds.size() < count);
			} else {
				throw new NoSuchBuildException(jobName, offset);
			}
		} else {
			log.fine("Mapping all builds of job " + job.getFullName());
			for (AbstractBuild<?, ?> build : job.getBuilds()) {
				builds.add(DozerUtils.getMapper().map(build, Build.class));
			}
		}
		return builds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getFirstBuild(String jobName) {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		if (job.getFirstBuild() != null) {
			log.fine("Mapping the first build of job " + job.getFullName());
			return DozerUtils.getMapper().map(job.getFirstBuild(), Build.class);
		}
		throw new NoSuchBuildException(jobName, "First");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getLastBuild(String jobName) {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		if (job.getLastBuild() != null) {
			log.fine("Mapping the last build of job " + job.getFullName());
			return DozerUtils.getMapper().map(job.getLastBuild(), Build.class);
		}
		throw new NoSuchBuildException(jobName, "Last");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getLastSuccessfulBuild(String jobName) {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		if (job.getLastSuccessfulBuild() != null) {
			log.fine("Mapping the last successful build of job " + job.getFullName());
			return DozerUtils.getMapper().map(job.getLastSuccessfulBuild(), Build.class);
		}
		throw new NoSuchBuildException(jobName, "Last Successful");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getLastCompletedBuild(String jobName) {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		if (job.getLastCompletedBuild() != null) {
			log.fine("Mapping the last completed build of job " + job.getFullName());
			return DozerUtils.getMapper().map(job.getLastCompletedBuild(), Build.class);
		}
		throw new NoSuchBuildException(jobName, "Last Completed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getLastFailedBuild(String jobName) {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		if (job.getLastFailedBuild() != null) {
			log.fine("Mapping the last failed build of job " + job.getFullName());
			return DozerUtils.getMapper().map(job.getLastFailedBuild(), Build.class);
		}
		throw new NoSuchBuildException(jobName, "Last Failed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getLastUnstableBuild(String jobName) {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		if (job.getLastUnstableBuild() != null) {
			log.fine("Mapping the last unstable build of job " + job.getFullName());
			return DozerUtils.getMapper().map(job.getLastUnstableBuild(), Build.class);
		}
		throw new NoSuchBuildException(jobName, "Last Ustable");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getLastStableBuild(String jobName) {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		if (job.getLastStableBuild() != null) {
			log.fine("Mapping the last stable build of job " + job.getFullName());
			return DozerUtils.getMapper().map(job.getLastStableBuild(), Build.class);
		}
		throw new NoSuchBuildException(jobName, "Last Stable");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Builds getBuilds(String jobName, Long from, Long to) throws NoSuchJobException {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		Builds builds = new Builds();
		for (Run<?, ?> run : job.getBuilds()) {
			if (run.getTimeInMillis() >= from && run.getTimeInMillis() <= to) {
				builds.add(DozerUtils.getMapper().map(run, Build.class));
			}
		}
		return builds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Builds getBuilds(String jobName, Long from) throws NoSuchJobException {
		hudson.model.Job<?, ?> job = getHudsonJob(jobName);
		Builds builds = new Builds();
		for (Run<?, ?> run : job.getBuilds()) {
			if (run.getTimeInMillis() >= from) {
				builds.add(DozerUtils.getMapper().map(run, Build.class));
			}
		}
		return builds;
	}

}
