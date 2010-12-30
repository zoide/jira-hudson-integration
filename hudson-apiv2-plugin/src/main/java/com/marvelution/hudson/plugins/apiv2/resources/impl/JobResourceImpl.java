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

import javax.ws.rs.Path;

import com.marvelution.hudson.plugins.apiv2.resources.BaseRestResource;
import com.marvelution.hudson.plugins.apiv2.resources.JobResource;
import com.marvelution.hudson.plugins.apiv2.resources.model.Job;

/**
 * The {@link JobResource} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@Path("/job")
public class JobResourceImpl extends BaseRestResource implements JobResource {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Job getJob(String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobs() {
		// TODO Auto-generated method stub
		return null;
	}

}
