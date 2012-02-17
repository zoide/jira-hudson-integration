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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.marvelution.hudson.plugins.apiv2.APIv2Plugin;
import com.marvelution.hudson.plugins.apiv2.cache.activity.ActivityCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.BuildActivityCache;
import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.Trigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.UserTrigger;
import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

/**
 * {@link RunListener} that will add the {@link Run} execution to the {@link ActivityCache}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
@SuppressWarnings("rawtypes")
@Extension
public class BuildActivityCacheRunListener extends RunListener<Run> {

	private static final Logger LOGGER = Logger.getLogger(BuildActivityCacheRunListener.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCompleted(Run r, TaskListener listener) {
		LOGGER.log(Level.FINE, "Adding build action on " + r.getParent().getFullName() + " to the Activity Cache");
		APIv2Plugin.getActivitiesCache().add(getBuildActivityCacheFromRun(r));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDeleted(Run r) {
		LOGGER.log(Level.FINE, "Removing build action on " + r.getParent().getFullName() + " from the Activity Cache");
		APIv2Plugin.getActivitiesCache().remove(getBuildActivityCacheFromRun(r));
	}

	/**
	 * Get a {@link BuildActivityCache} from the given {@link Run}
	 * 
	 * @param r the {@link Run} to get the {@link BuildActivityCache} from
	 * @return the {@link BuildActivityCache}
	 */
	private BuildActivityCache getBuildActivityCacheFromRun(Run r) {
		BuildActivityCache activity =
			new BuildActivityCache(r.getTimeInMillis(), r.getParent().getFullName(), r.getNumber());
		if (!(r.getParent().getParent() instanceof Hudson)) {
			activity.setParent(r.getParent().getParent().getFullName());
		}
		String culprit = HudsonPluginUtils.getHudsonSystem().getSystemUser().getUserId();
		if (r.getCauses() != null) {
			for (Object cause : r.getCauses()) {
				Trigger trigger = DozerUtils.getMapper().map(cause, Trigger.class);
				if (trigger != null) {
					if (trigger instanceof UserTrigger) {
						culprit = ((UserTrigger) trigger).getUsername();
						break;
					}
				}
			}
		}
		activity.setCulprit(culprit);
		return activity;
	}

}
