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

package com.marvelution.hudson.plugins.apiv2.resources.model.activity;

import javax.xml.bind.annotation.XmlRegistry;

import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity.BuildActivity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity.JobActivity;

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
	 * Create an {@link JobActivity} object
	 * 
	 * @return the {@link JobActivity}  object
	 */
	public static JobActivity createJobActivity() {
		return new JobActivity();
	}

	/**
	 * Create an {@link BuildActivity} object
	 * 
	 * @return the {@link BuildActivity}  object
	 */
	public static BuildActivity createBuildActivity() {
		return new BuildActivity();
	}

	/**
	 * Create an {@link Activities} object
	 * 
	 * @return the {@link Activities}  object
	 */
	public static Activities createActivities() {
		return new Activities();
	}

}
