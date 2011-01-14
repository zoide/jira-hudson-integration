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

package com.marvelution.hudson.plugins.apiv2.resources.model.triggers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Time {@link Trigger} implementation
 * {@link Trigger} that indicates  that a specific Time triggered a build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "TimeTriggerType", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlRootElement(name = "TimeTrigger", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeTrigger extends Trigger {

	@XmlElement(name = "time")
	private long timestamp;

	/**
	 * Getter for the timestamp
	 * 
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter for the timestamp
	 * 
	 * @param timestamp the timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Static helper method to create a {@link TimeTrigger} for the given timestamp
	 * 
	 * @param timestamp the timestamp of the {@link TimeTrigger}
	 * @return the {@link TimeTrigger}
	 */
	public static TimeTrigger create(long timestamp) {
		TimeTrigger trigger = new TimeTrigger();
		trigger.setTimestamp(timestamp);
		return trigger;
	}

}
