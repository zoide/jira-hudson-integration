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

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.marvelution.hudson.plugins.apiv2.APIv2Plugin;
import com.marvelution.hudson.plugins.apiv2.cache.activity.ActivityCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.BuildActivityCache;
import com.marvelution.hudson.plugins.apiv2.cache.activity.JobActivityCache;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.listeners.ItemListener;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
@Extension
public class JobActivityCacheItemListener extends ItemListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDeleted(Item item) {
		Collection<ActivityCache> toBeRemoved = Lists.newArrayList();
		for (ActivityCache cache : APIv2Plugin.getActivitiesCache().getSortedActivities()) {
			if (item.getFullName().equals(cache.getJob()) || item.getFullName().equals(cache.getParent())) {
				toBeRemoved.add(cache);
			}
		}
		APIv2Plugin.getActivitiesCache().removeAll(toBeRemoved);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRenamed(Item item, String oldName, String newName) {
		String newFullName, oldFullName;
		if (item.getParent().getFullName().length() == 0) {
			newFullName = newName;
			oldFullName = oldName;
		} else {
			newFullName = item.getParent().getFullName() + "/" + newName;
			oldFullName = item.getParent().getFullName() + "/" + oldName;
		}
		Collection<ActivityCache> toBeRemoved = Lists.newArrayList();
		Collection<ActivityCache> toBeAdded = Lists.newArrayList();
		for (ActivityCache cache : APIv2Plugin.getActivitiesCache().getSortedActivities()) {
			ActivityCache newCache = null;
			if (oldFullName.equals(cache.getJob())) {
				newCache = getRenamedJobActivityCache(cache, newFullName);
			} else if (oldFullName.equals(cache.getParent())) {
				newCache =
					getRenamedJobActivityCache(cache, StringUtils.replace(cache.getJob(), oldFullName, newFullName));
				newCache.setParent(newFullName);
			}
			if (newCache != null && !cache.equals(newCache)) {
				toBeRemoved.add(cache);
				toBeAdded.add(newCache);
			}
		}
		APIv2Plugin.getActivitiesCache().removeAll(toBeRemoved);
		APIv2Plugin.getActivitiesCache().addAll(toBeAdded);
	}

	/**
	 * Get a {@link ActivityCache} for the renamed Job
	 * 
	 * @param cache the Old {@link ActivityCache}
	 * @param newJobName the new Job name
	 * @return the new {@link ActivityCache}
	 */
	private ActivityCache getRenamedJobActivityCache(ActivityCache cache, String newJobName) {
		ActivityCache newCache;
		if (cache instanceof BuildActivityCache) {
			newCache = new BuildActivityCache(cache.getTimestamp(), newJobName, ((BuildActivityCache) cache).getBuild());
		} else {
			newCache = new JobActivityCache(cache.getTimestamp(), newJobName);
		}
		newCache.setCulprit(cache.getCulprit());
		newCache.setParent(cache.getParent());
		return newCache;
	}

}
