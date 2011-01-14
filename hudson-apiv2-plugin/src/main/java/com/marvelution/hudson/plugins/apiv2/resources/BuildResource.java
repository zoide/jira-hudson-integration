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

package com.marvelution.hudson.plugins.apiv2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchBuildException;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchJobException;
import com.marvelution.hudson.plugins.apiv2.resources.model.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.Builds;

/**
 * Build Resource Endpoint interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
// TODO add method to get builds by issue key
public interface BuildResource {

	/**
	 * Get a single {@link Build} for a Jobname identified by the build number and job name combination
	 * 
	 * @param jobName the Job name to get the build for
	 * @param buildNumber the build number of the build to get
	 * @return the {@link Build}, may not be <code>null</code>
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the build identified by the buildNumber doesn't exist
	 */
	@GET
	@Path("{jobName}")
	Build getBuild(@PathParam("jobName") String jobName, @QueryParam("buildNumber") Integer buildNumber) throws
		NoSuchJobException, NoSuchBuildException;

	/**
	 * Get all the builds for a specific Job
	 * 
	 * @param jobName the Job name to get all the builds for
	 * @return the {@link Builds} collection
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 */
	@GET
	@Path("{jobName}/all")
	Builds getBuilds(@PathParam("jobName") String jobName) throws NoSuchJobException;

	/**
	 * Get the first {@link Build} of a Job
	 * 
	 * @param jobName the Job name to get the {@link Build} for
	 * @return the first {@link Build} of the Job
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the Job doesn't have a first build doesn't exist
	 */
	@GET
	@Path("{jobName}/first")
	Build getFirstBuild(@PathParam("jobName") String jobName) throws NoSuchJobException, NoSuchBuildException;

	/**
	 * Get the last {@link Build} of a Job
	 * 
	 * @param jobName the Job name to get the {@link Build} for
	 * @return the last {@link Build} of the Job
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the Job doesn't have a last  build doesn't exist
	 */
	@GET
	@Path("{jobName}/last")
	Build getLastBuild(@PathParam("jobName") String jobName) throws NoSuchJobException, NoSuchBuildException;

	/**
	 * Get the last successful {@link Build} of a Job
	 * 
	 * @param jobName the Job name to get the {@link Build} for
	 * @return the last successful {@link Build} of the Job
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the Job doesn't have a last successful build doesn't exist
	 */
	@GET
	@Path("{jobName}/lastSuccessful")
	Build getLastSuccessfulBuild(@PathParam("jobName") String jobName) throws NoSuchJobException, NoSuchBuildException;

	/**
	 * Get the last completed {@link Build} of a Job
	 * 
	 * @param jobName the Job name to get the {@link Build} for
	 * @return the last completed {@link Build} of the Job
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the Job doesn't have a last completed build doesn't exist
	 */
	@GET
	@Path("{jobName}/lastCompleted")
	Build getLastCompletedBuild(@PathParam("jobName") String jobName) throws NoSuchJobException, NoSuchBuildException;

	/**
	 * Get the last failed {@link Build} of a Job
	 * 
	 * @param jobName the Job name to get the {@link Build} for
	 * @return the last failed {@link Build} of the Job
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the Job doesn't have a last failed build doesn't exist
	 */
	@GET
	@Path("{jobName}/lastFailed")
	Build getLastFailedBuild(@PathParam("jobName") String jobName) throws NoSuchJobException, NoSuchBuildException;

	/**
	 * Get the last unstable {@link Build} of a Job
	 * 
	 * @param jobName the Job name to get the {@link Build} for
	 * @return the last unstable {@link Build} of the Job
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the Job doesn't have a last unstable build doesn't exist
	 */
	@GET
	@Path("{jobName}/lastUnstable")
	Build getLastUnstableBuild(@PathParam("jobName") String jobName) throws NoSuchJobException, NoSuchBuildException;

	/**
	 * Get the last stable {@link Build} of a Job
	 * 
	 * @param jobName the Job name to get the {@link Build} for
	 * @return the last stable {@link Build} of the Job
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 * @throws NoSuchBuildException in case the Job doesn't have a last stable build doesn't exist
	 */
	@GET
	@Path("{jobName}/lastStable")
	Build getLastStableBuild(@PathParam("jobName") String jobName) throws NoSuchJobException, NoSuchBuildException;

	/**
	 * Get all the builds from a specific Job that where executed between the given from and to times
	 * 
	 * @param jobName the Job name to get the build for
	 * @param from the time (in milliseconds) that the builds need to be started after
	 * @param to the time (in milliseconds) that the builds need to be executed before
	 * @return the {@link Builds} collection
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 */
	@GET
	@Path("{jobName}/between")
	Builds getBuilds(@PathParam("jobName") String jobName, @QueryParam("from") Long from, @QueryParam("to") Long to)
		throws NoSuchJobException;

	/**
	 * Get all the builds from a specific Job that where executed after the given from time
	 * 
	 * @param jobName the Job name to get the build for
	 * @param from the time (in milliseconds) that the builds need to be started after
	 * @return the {@link Builds} collection
	 * @throws NoSuchJobException in case the job identified by the jobName doesn't exist
	 */
	@GET
	@Path("{jobName}/after")
	Builds getBuilds(@PathParam("jobName") String jobName, @QueryParam("from") Long from) throws NoSuchJobException;

}
