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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.wink.common.annotations.Parent;

import com.marvelution.hudson.plugins.apiv2.resources.BuildResource;
import com.marvelution.hudson.plugins.apiv2.resources.model.Build;

/**
 * The {@link BuildResource} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@Parent(BaseRestResource.class)
@Path("/build")
public class BuildResourceRestImpl extends BaseRestResource implements BuildResource {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}")
	public Build getBuild(@PathParam("jobName") String jobName, @QueryParam("buildNumber") Integer buildNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/all")
	public List<Build> getBuilds(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/firstbuild")
	public Build getFirstBuild(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/lastbuild")
	public Build getLastBuild(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/lastSuccessfulBuild")
	public Build getLastSuccessfulBuild(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/lastCompletedBuild")
	public Build getLastCompletedBuild(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/lastFailedBuild")
	public Build getLastFailedBuild(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/lastUnstableBuild")
	public Build getLastUnstableBuild(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("{jobName}/lastStableBuild")
	public Build getLastStableBuild(@PathParam("jobName") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

}
