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

package com.marvelution.jira.plugins.hudson.gadgets;

import java.util.Collection;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.atlassian.jira.rest.v1.model.errors.ErrorCollection;
import com.atlassian.jira.rest.v1.model.errors.ValidationError;
import com.atlassian.jira.rest.v1.util.CacheControl;

/**
 * Abstract Gadget Resource implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class AbstractGadgetResource {

	/**
	 * Filter prefix value
	 */
	public static final String FILTER_PREFIX = "filter-";

	/**
	 * Project prefix value
	 */
	public static final String PROJECT_PREFIX = "project-";

	/**
	 * Create an Error Response
	 * 
	 * @param errors the collection of Errors to send in the {@link Response}
	 * @return the {@link Response}
	 */
	protected Response createErrorResponse(Collection<ValidationError> errors) {
		return Response.status(Status.BAD_REQUEST).entity(ErrorCollection.Builder.newBuilder(errors).build())
			.cacheControl(CacheControl.NO_CACHE).build();
	}

	/**
	 * Create Validation Response
	 * 
	 * @param errors the {@link Collection} of Errors, may be <code>null</code>
	 * @return the {@link Response}
	 */
	protected Response createValidationResponse(Collection<ValidationError> errors) {
		if (errors == null || errors.isEmpty()) {
			return Response.ok().cacheControl(CacheControl.NO_CACHE).build();
		}
		return createErrorResponse(errors);
	}

	/**
	 * Strip the filter prefix from the filterId to get the actual Id value as {@link Long}
	 * 
	 * @param filterId the Filter Id to strip the prefix from
	 * @param prefix the prefix to be stripped
	 * @return the {@link Long} value of the filterId after the prefix is stripped of
	 */
	protected Long stripFilterPrefix(String filterId, String prefix) {
		if (filterId.startsWith(prefix)) {
			final String numPart = filterId.substring(prefix.length());
			return Long.valueOf(numPart);
		}
		return Long.valueOf(filterId);
	}

}
