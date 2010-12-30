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

package com.marvelution.hudson.plugins.apiv2.resources.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchBuildException;

/**
 * {@link ExceptionMapper} to map a {@link NoSuchBuildException} to a {@link Response}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@Provider
public class NoSuchBuildExceptionMapper implements ExceptionMapper<NoSuchBuildException> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response toResponse(final NoSuchBuildException exception) {
		return Response.status(new StatusType() {
			
			@Override
			public int getStatusCode() {
				return Status.NOT_FOUND.getStatusCode();
			}
			
			@Override
			public String getReasonPhrase() {
				return exception.getMessage();
			}
			
			@Override
			public Family getFamily() {
				return Family.CLIENT_ERROR;
			}
		}).build();
	}

}
