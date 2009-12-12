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

package com.marvelution.jira.plugins.hudson.rest.exceptions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

/**
 * Testcase for {@link NotAuthorizedExceptionMapper}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class NotAuthorizedExceptionMapperTest {

	/**
	 * Test {@link NotAuthorizedExceptionMapper#toResponse(NotAuthorizedException)}
	 */
	@Test
	public void testToResponse() {
		final Response response = new NotAuthorizedExceptionMapper().toResponse(new NotAuthorizedException(
				"Access is denied"));
		assertThat(response.getStatus(), is(Status.FORBIDDEN.getStatusCode()));
		assertThat(response.getEntity(), is(com.atlassian.plugins.rest.common.Status.class));
		final com.atlassian.plugins.rest.common.Status status =
			(com.atlassian.plugins.rest.common.Status) response.getEntity();
		assertThat(status.getCode(), is(Status.FORBIDDEN.getStatusCode()));
		assertThat(status.getMessage(), is("Access is denied"));
	}

}
