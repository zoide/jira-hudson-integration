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

package com.marvelution.hudson.plugins.apiv2.listeners;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.marvelution.hudson.plugins.apiv2.APIv2Plugin;
import com.marvelution.hudson.plugins.apiv2.cache.activity.ActivityCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.JobActivityCache;
import com.marvelution.hudson.plugins.apiv2.resources.model.User;

import hudson.Extension;
import hudson.XmlFile;
import hudson.model.Job;
import hudson.model.Saveable;
import hudson.model.listeners.SaveableListener;

/**
 * {@link SaveableListener} that will add any save actions on an {@link Job} to the {@link ActivityCache}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
@Extension
public class JobActivityCacheSaveableListener extends SaveableListener {

	private static final Logger LOGGER = Logger.getLogger(JobActivityCacheSaveableListener.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onChange(Saveable o, XmlFile file) {
		if (o instanceof Job) {
			Job<?, ?> item = (Job<?, ?>) o;
			LOGGER.log(Level.FINE, "Adding save action on " + item.getFullName() + " to the Activity Cache");
			JobActivityCache activity =
				new JobActivityCache(Calendar.getInstance().getTimeInMillis(), item.getFullName());
			if (hudson.model.User.current() != null) {
				activity.setCulprit(hudson.model.User.current().getId());
			} else {
				activity.setCulprit(User.ANONYMOUS.getUserId());
			}
			if (item.getParent().getFullName().length() > 0) {
				activity.setParent(item.getParent().getFullName());
			}
			APIv2Plugin.getActivitiesCache().add(activity);
		}
	}

}
