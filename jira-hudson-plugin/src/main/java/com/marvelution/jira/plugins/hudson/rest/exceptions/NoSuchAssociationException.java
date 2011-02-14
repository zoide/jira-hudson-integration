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

/**
 * Exception thrown if an invalid Association id is given
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class NoSuchAssociationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int associationId;

	/**
	 * Constructor
	 * 
	 * @param associationId the Association Id
	 */
	public NoSuchAssociationException(int associationId) {
		this.associationId = associationId;
	}

	/**
	 * Getter for the Association Id
	 * 
	 * @return the Association Id
	 */
	public int getAssociationId() {
		return associationId;
	}

}
