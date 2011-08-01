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

package com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * User {@link Trigger} implementation
 * {@link Trigger} that indicates that a User triggered a build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "UserTriggerType", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createUserTrigger")
@XmlRootElement(name = "UserTrigger", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class UserTrigger extends Trigger {

	@XmlElement(name = "username", required = true)
	private String username;

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
	 * Static helper method to create a {@link UserTrigger}
	 * 
	 * @param username the username of the user to put in the {@link UserTrigger}
	 * @return the {@link UserTrigger}
	 */
	public static UserTrigger create(String username) {
		return create(username, "");
	}

	/**
	 * Static helper method to create a {@link UserTrigger}
	 * 
	 * @param username the username of the user to put in the {@link UserTrigger}
	 * @param shortDescription the short description of the trigger
	 * @return the {@link UserTrigger}
	 */
	public static UserTrigger create(String username, String shortDescription) {
		UserTrigger trigger = new UserTrigger();
		trigger.setUsername(username);
		trigger.setShortDescription(shortDescription);
		return trigger;
	}

}
