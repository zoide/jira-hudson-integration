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

package com.marvelution.hudson.plugins.apiv2.resources.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;


/**
 * User {@link Model} object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlType(name = "UserType", namespace = NameSpaceUtils.APIV2_NAMESPACE)
@XmlRootElement(name = "User", namespace = NameSpaceUtils.APIV2_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class User extends Model implements Comparable<User> {

	@XmlElement(name = "userId", required = true)
	private String userId;
	@XmlElement(name = "userName", required = true)
	private String username;
	@XmlElement(name = "userDesc", required = false)
	private String description;

	/**
	 * Default Constructor
	 */
	public User() {}

	/**
	 * Constructor
	 * 
	 * @param userId the userId
	 * @param username the User name
	 * @param description a description
	 */
	public User(String userId, String username, String description) {
		this.userId = userId;
		this.username = username;
		this.description = description;
	}

	/**
	 * Getter for userId
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter for userId
	 * 
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * Getter for description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof User) {
			User other = (User) obj;
			return other.getUserId().equals(getUserId()) && other.getUsername().equals(getUsername());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getUserId().hashCode() + getUsername().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(User other) {
		return getUsername().compareTo(other.getUsername());
	}

}
