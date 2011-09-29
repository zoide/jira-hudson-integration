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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * Build Trigger interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public abstract class Trigger extends Model {

	@XmlElement(name = "shortDescription")
	private String shortDescription;

	/**
	 * Getter for shortDescription
	 * 
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Setter for shortDescription
	 * 
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Getter for the type
	 * 
	 * @return the type of {@link Trigger}
	 */
	@XmlAttribute(name = "type", required = true)
	public final String getType() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Get a {@link String} representation of the {@link Trigger}
	 * 
	 * @return the {@link String} representation
	 */
	@Override
	public String toString() {
		return this.getClass().getName();
	}

}
