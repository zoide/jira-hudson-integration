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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchViewException;
import com.marvelution.hudson.plugins.apiv2.resources.model.View;

/**
 * View REST Resource Endpoint Interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public interface ViewResource {

	/**
	 * Get the {@link View} identified by the given name
	 * 
	 * @param name the {@link View} name to get
	 * @return the {@link View}
	 * @throws NoSuchViewException in case the {@link View} with the given name doesn't exist
	 */
	@GET
	View getView(@QueryParam("name") String name) throws NoSuchViewException;

	/**
	 * Get all the {@link View} objects on the Hudson server
	 * 
	 * @return {@link List} of all the {@link View} objects
	 */
	@GET
	@Path("/all")
	List<View> getViews();

}
