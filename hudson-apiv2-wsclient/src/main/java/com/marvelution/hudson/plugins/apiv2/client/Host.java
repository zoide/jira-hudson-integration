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


/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class Host {

	private String host;
	private String username;
	private String password;

	/**
	 * Default constructor for unsecured instances
	 * 
	 * @param host the host url
	 */
	public Host(String host) {
		super();
		this.host = host;
	}

	/**
	 * Constructor for secured instances
	 * 
	 * @param host the host url
	 * @param username the authentication username
	 * @param password teh authentication password
	 */
	public Host(String host, String username, String password) {
		super();
		this.host = host;
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

}
