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

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity.BuildActivity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity.JobActivity;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Activities XML object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "ActivitiesType", namespace = NameSpaceUtils.ACTIVITY_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createActivities")
@XmlRootElement(name = "Activities", namespace = NameSpaceUtils.ACTIVITY_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Activities extends ListableModel<Activity> {

	@XmlElementRefs({
		@XmlElementRef(name = "JobActivity", type = JobActivity.class),
		@XmlElementRef(name = "BuildActivity", type = BuildActivity.class)
	})
	private Collection<Activity> items;

	/**
	 * Default Constructor
	 */
	public Activities() {
		items = new ArrayList<Activity>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Activity> getItems() {
		return items;
	}

}
