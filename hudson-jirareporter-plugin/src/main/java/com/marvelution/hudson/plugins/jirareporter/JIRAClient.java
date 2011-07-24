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

package com.marvelution.hudson.plugins.jirareporter;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;

/**
 * A SOAP Client for a {@link JIRASite}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JIRAClient {

	private final JiraSoapService service;
	private final String token;
	private final JIRASite site;

	/**
	 * Private Constructor
	 * 
	 * @param service the {@link JiraSoapService} implementation
	 * @param token the security token
	 * @param site the {@link JIRASite} configuration
	 */
	JIRAClient(JiraSoapService service, String token, JIRASite site) {
		this.service = service;
		this.token = token;
		this.site = site;
	}

}
