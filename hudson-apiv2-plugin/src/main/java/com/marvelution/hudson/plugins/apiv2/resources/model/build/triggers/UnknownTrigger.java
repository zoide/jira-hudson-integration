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
 * Unkown {@link Trigger} implementation
 * {@link Trigger} that indicates an unknown trigger caused the build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "UnknownTriggerType", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createUnknownTrigger")
@XmlRootElement(name = "UnknownTrigger", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class UnknownTrigger extends Trigger {

	@XmlElement(name = "name", required = true)
	private String name;

	/**
	 * Getter for name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Create a {@link UnknownTrigger} object for the class name given
	 * 
	 * @param className
	 * @param shortDescription the short description of the trigger
	 * @return {@link UnknownTrigger}
	 */
	public static UnknownTrigger create(String className, String shortDescription) {
		UnknownTrigger trigger = new UnknownTrigger();
		trigger.setName(className);
		trigger.setShortDescription(shortDescription);
		return trigger;
	}

}
