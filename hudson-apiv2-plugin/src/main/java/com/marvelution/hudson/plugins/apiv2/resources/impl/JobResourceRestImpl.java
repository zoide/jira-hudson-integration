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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.wink.common.annotations.Parent;

import com.marvelution.hudson.plugins.apiv2.resources.JobResource;
import com.marvelution.hudson.plugins.apiv2.resources.model.Job;

/**
 * The {@link JobResource} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@Parent(BaseRestResource.class)
@Path("job")
public class JobResourceRestImpl extends BaseRestResource implements JobResource {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	public Job getJob(@QueryParam("name") String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("all")
	public List<Job> getJobs() {
		// TODO Auto-generated method stub
		Logger.getLogger(JobResourceRestImpl.class.getName()).log(Level.SEVERE, "JobResource.getJobs()");
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@GET
	@Path("list")
	public List<Job> listJobs() {
		// TODO Auto-generated method stub
		return null;
	}

}
