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

package com.marvelution.hudson.plugins.apiv2.client;

import com.marvelution.hudson.plugins.apiv2.client.connectors.ConnectorResponse;

/**
 * Exception throws by the {@link HudsonClient} is case of errors
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ClientException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ConnectorResponse response;

	/**
	 * Constructor
	 */
	public ClientException(ConnectorResponse response) {
		this.response = response;
	}

	/**
	 * Constructor
	 * 
	 * @param message the exception message
	 */
	public ClientException(String message, ConnectorResponse response) {
		super(message);
		this.response = response;
	}

	/**
	 * Constructor
	 * 
	 * @param cause the exception cause
	 */
	public ClientException(Throwable cause, ConnectorResponse response) {
		super(cause);
		this.response = response;
	}

	/**
	 * Constructor
	 * 
	 * @param message the exception message
	 * @param cause the exception cause
	 */
	public ClientException(String message, Throwable cause, ConnectorResponse response) {
		super(message, cause);
		this.response = response;
	}

	/**
	 * Getter for response
	 * 
	 * @return the response
	 */
	public ConnectorResponse getResponse() {
		return response;
	}

}
