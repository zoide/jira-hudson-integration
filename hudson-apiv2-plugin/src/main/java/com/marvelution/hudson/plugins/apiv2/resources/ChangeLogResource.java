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
import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog;

/**
 * {@link ChangeLog} Resource Endpoint interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface ChangeLogResource {

	/**
	 * Get the {@link ChangeLog} of a specific job and build identified by the job name and build number
	 * 
	 * @param jobname the job name to get the builds from
	 * @param buildNumber the build number to get the {@link ChangeLog} for
	 * @return the {@link ChangeLog} for the specific build
	 * @throws NoSuchJobException in case the provided job name value doesn't exist
	 * @throws NoSuchBuildException in case the job doesn't have the specific build
	 */
	@GET
	@Path("{jobname}")
	ChangeLog getChangeLog(@PathParam("jobname") String jobname, @QueryParam("buildNumber") Integer buildNumber)
		throws NoSuchJobException, NoSuchBuildException;

}
