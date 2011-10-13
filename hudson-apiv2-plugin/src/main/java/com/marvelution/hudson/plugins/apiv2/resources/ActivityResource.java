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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activities;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.ActivityType;

/**
 * The Activity Resource interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
public interface ActivityResource {

	/**
	 * Resource service to get activities of the given types
	 * 
	 * @param types the {@link ActivityType} types requested, default to all {@link ActivityType} types
	 * @param jobs array of job names to filter by, if an entry starts with a ! then it is a not filter
	 * @param userId array of user Ids to filter by, if an entry starts with a ! then it is a not filter
	 * @param maxResults the maximum number of activities to return, defaults to 10
	 * @return the {@link Activities} collection
	 */
	@GET
	Activities getActivities(@QueryParam("type[]") ActivityType[] types, @QueryParam("job[]") String[] jobs,
		@QueryParam("user[]") String[] userIds, @DefaultValue("10") @QueryParam("max") int maxResults);

}
