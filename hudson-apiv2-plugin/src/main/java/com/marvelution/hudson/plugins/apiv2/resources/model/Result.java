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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Result XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "ResultType", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlEnum(String.class)
public enum Result {

	SUCCESS("blue.gif"), FAILURE("red.gif"), UNSTABLE("yellow.gif"), NOT_BUILT("grey.gif"), ABORTED("red.gif");

	private String icon;

	/**
	 * Constructor
	 * 
	 * @param icon icon name
	 */
	private Result(String icon) {
		this.icon = icon;
	}

	/**
	 * Getter for the icon name
	 * 
	 * @return the icon name
	 */
	public String getIcon() {
		return icon;
	}

}
