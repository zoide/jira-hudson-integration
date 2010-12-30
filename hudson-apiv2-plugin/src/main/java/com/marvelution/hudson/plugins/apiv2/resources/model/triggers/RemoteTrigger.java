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
 * Remote {@link Trigger} implementation
 * {@link Trigger} that indicates that a Remote System triggered the build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "RemoteTriggerType", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlRootElement(name = "RemoteTrigger", namespace = NameSpaceUtils.BUILD_TRIGGER_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoteTrigger extends Trigger {

	@XmlElement(name = "host", required = true)
	private String host;
	@XmlElement(name = "note")
	private String note;
	
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
	 * Getter for note
	 *
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * Setter for note
	 * 
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

}
