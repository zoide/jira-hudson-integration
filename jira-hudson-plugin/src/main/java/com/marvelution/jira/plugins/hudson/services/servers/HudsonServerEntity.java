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

package com.marvelution.jira.plugins.hudson.services.servers;

import com.atlassian.gzipfilter.org.apache.commons.lang.StringUtils;
import com.marvelution.security.crypto.SimpleStringEncryptor;
import com.marvelution.security.crypto.StringEncryptor;

/**
 * Entity implementation for the {@link HudsonServer} entity
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 1.0.0
 */
public class HudsonServerEntity {

	private static final StringEncryptor ENCRYPTOR = new SimpleStringEncryptor("peRaxUkuStUwefRApeY6drADahucUM");

	private HudsonServer server;

	/**
	 * Constructor
	 *
	 * @param server the {@link HudsonServer} entity
	 */
	public HudsonServerEntity(HudsonServer server) {
		this.server = server;
	}

	/**
	 * Decrypt the password from the database
	 * 
	 * @return the decrypted password
	 * @see com.marvelution.jira.plugins.hudson.services.servers.HudsonServer#getPassword()
	 */
	public String getPassword() {
		return ENCRYPTOR.decrypt(server.getPassword());
	}

	/**
	 * Encrypt the password before storing it in the database
	 * 
	 * @param password the password to encrypt
	 * @see com.marvelution.jira.plugins.hudson.services.servers.HudsonServer#setPassword(java.lang.String)
	 */
	public void setPassword(String password) {
		server.setPassword(ENCRYPTOR.encrypt(password));
	}

	/**
	 * Getter for the Server Public Host URL.
	 * If the Public Host URL is not set then the Host URL is returned.
	 * 
	 * @return the Public Host URL
	 * @see com.marvelution.jira.plugins.hudson.services.servers.HudsonServer#getPublicHost()
	 */
	public String getPublicHost() {
		if (StringUtils.isNotBlank(server.getPublicHost())) {
			return server.getPublicHost();
		} else {
			return server.getHost();
		}
	}

}
