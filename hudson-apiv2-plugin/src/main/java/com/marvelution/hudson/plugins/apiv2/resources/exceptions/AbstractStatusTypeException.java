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

package com.marvelution.hudson.plugins.apiv2.resources.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

import com.marvelution.hudson.plugins.apiv2.resources.exceptions.mappers.StatusTypeExceptionMapper;

/**
 * Abstract {@link StatusType} {@link RuntimeException}.
 * This case needs to be subclassed in order for the exception to be handled by the {@link StatusTypeExceptionMapper}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public abstract class AbstractStatusTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Get the {@link Family} type of the exception
	 * 
	 * @return the {@link Family}
	 */
	public abstract Family getFamily();

	/**
	 * Get the {@link Status} type of the exception
	 * 
	 * @return the {@link Status} type
	 */
	public abstract Status getStatus();

	/**
	 * Get the Reason Phrase of the exception
	 * 
	 * @return the Reason Phrase
	 * @see AbstractStatusTypeException#getMessage()
	 */
	public String getReasonPhrase() {
		return getMessage();
	}

}
