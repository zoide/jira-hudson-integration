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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * SCM {@link Trigger} implementation
 * {@link Trigger} that indicates that s SCM change triggered the build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "SCMTriggerType", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createSCMTrigger")
@XmlRootElement(name = "SCMTrigger", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
// TODO Add ChangeLog to trigger
public class SCMTrigger extends Trigger {

	/**
	 * Static helper method to create a {@link SCMTrigger}
	 * 
	 * @param shortDescription the short description of the Trigger
	 * @return the {@link SCMTrigger}
	 */
	public static SCMTrigger create(String shortDescription) {
		SCMTrigger trigger = new SCMTrigger();
		trigger.setShortDescription(shortDescription);
		return trigger;
	}

}
