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

package com.marvelution.jira.plugins.hudson.model.triggers;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * User {@link Trigger}. {@link Trigger} that indicates that a User started a build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("userTrigger")
public class UserTrigger implements Trigger {

	private String userName;

	/**
	 * Default no-argument constructor for XStream
	 */
	public UserTrigger() {
	}

	/**
	 * Constructor
	 * 
	 * @param userName the username that triggered the build
	 */
	public UserTrigger(String userName) {
		setUserName(userName);
	}

	/**
	 * Gets the username that triggered the build
	 * 
	 * @return the username
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the username that triggered the build
	 * 
	 * @param userName the username
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
