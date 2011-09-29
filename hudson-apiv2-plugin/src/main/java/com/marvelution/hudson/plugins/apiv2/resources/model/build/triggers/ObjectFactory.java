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

import javax.xml.bind.annotation.XmlRegistry;

/**
 * ObjectFactory to create Xml objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Default Constructor
	 */
	public ObjectFactory() {
	}

	/**
	 * Create a {@link ProjectTrigger}
	 * 
	 * @return the {@link ProjectTrigger}
	 */
	public static ProjectTrigger createProjectTrigger() {
		return new ProjectTrigger();
	}

	/**
	 * Create a {@link RemoteTrigger}
	 * 
	 * @return the {@link RemoteTrigger}
	 */
	public static RemoteTrigger createRemoteTrigger() {
		return new RemoteTrigger();
	}

	/**
	 * Create a {@link SCMTrigger}
	 * 
	 * @return the {@link SCMTrigger}
	 */
	public static SCMTrigger createSCMTrigger() {
		return new SCMTrigger();
	}

	/**
	 * Create a {@link TimeTrigger}
	 * 
	 * @return the {@link TimeTrigger}
	 */
	public static TimeTrigger createTimeTrigger() {
		return new TimeTrigger();
	}

	/**
	 * Create a {@link UnknownTrigger}
	 * 
	 * @return the {@link UnknownTrigger}
	 */
	public static UnknownTrigger createUnknownTrigger() {
		return new UnknownTrigger();
	}

	/**
	 * Create a {@link UserTrigger}
	 * 
	 * @return the {@link UserTrigger}
	 */
	public static UserTrigger createUserTrigger() {
		return new UserTrigger();
	}

}
