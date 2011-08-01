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

import org.apache.commons.lang.StringUtils;

/**
 * Host configuration class.
 * This class contains all the required configuration items to allow a {@link Connector} to connect to the {@link Host}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class Host {

	private String host = "";
	private String username = "";
	private String password = "";

	/**
	 * Default constructor for unsecured instances
	 * 
	 * @param host the host url
	 */
	public Host(String host) {
		this.host = host;
	}

	/**
	 * Constructor for secured instances
	 * 
	 * @param host the host url
	 * @param username the authentication username
	 * @param password the authentication password
	 */
	public Host(String host, String username, String password) {
		this(host);
		this.username = username;
		this.password = password;
	}

	/**
	 * Getter for host
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Setter for host
	 * 
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Getter for username
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter for username
	 * 
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter for password
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for password
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Check if this {@link Host} is a securd host.
	 * Only <code>true</code> if {@link #username} and {@link #password} are both not Blank
	 * 
	 * @return <code>true</code> if secured, <code>false</code> otherwise
	 */
	public boolean isSecured() {
		return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Host) {
			Host otherHost = (Host) obj;
			return (getHost().equals(otherHost.getHost()) && getUsername().equals(otherHost.getUsername())
				&& getPassword().equals(otherHost.getPassword()));
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getHost().hashCode() + getUsername().hashCode() + getPassword().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String string = "[Host: url: " + getHost();
		if (!"".equals(getUsername())) { 
			string += "; username:" + getUsername() + "; password: ******";
		}
		string += "]";
		return string;
	}

}
