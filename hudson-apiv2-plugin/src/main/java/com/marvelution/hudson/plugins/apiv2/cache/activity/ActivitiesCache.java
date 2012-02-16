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

package com.marvelution.hudson.plugins.apiv2.cache.activity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.list.SynchronizedList;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
public class ActivitiesCache {

	public static final long MERGE_TIMESPAN = 60 * 1000;

	private List<ActivityCache> activities = Lists.newArrayList();
	@XStreamOmitField
	private ActivityCache previousActivity = null;

	/**
	 * Getter for activities
	 *
	 * @return the activities
	 */
	@SuppressWarnings("unchecked")
	private List<ActivityCache> getActivities() {
		return SynchronizedList.decorate(activities);
	}

	/**
	 * Getter for the Sorted {@link List} of {@link ActivitiesCache}
	 * 
	 * @return the {@link List} of sorted {@link ActivityCache} objects
	 */
	public List<ActivityCache> getSortedActivities() {
		Ordering<ActivityCache> ordering =
			Ordering.natural().reverse().onResultOf(new Function<ActivityCache, Date>() {
				@Override
				public Date apply(ActivityCache from) {
					return new Date(from.getTimestamp());
				}
			});
		return ordering.immutableSortedCopy(activities);
	}

	/**
	 * Add an {@link ActivityCache} to the cache
	 * 
	 * @param activity the {@link ActivityCache} to add
	 */
	public void add(ActivityCache activity) {
		if (previousActivity != null && previousActivity.isTheSame(activity)) {
			// The Activity was on the same object and executed by the same User, check to see if the activity needs
			// to be merged
			if (activity.getTimestamp() - previousActivity.getTimestamp() < MERGE_TIMESPAN) {
				return;
			}
		}
		previousActivity = activity;
		getActivities().add(activity);
	}

	/**
	 * Add all {@link ActivityCache}s to the cache
	 * 
	 * @param activities the {@link ActivityCache}s to add
	 */
	public void addAll(Collection<ActivityCache> activities) {
		getActivities().addAll(activities);
	}

	/**
	 * Remove an {@link ActivityCache} from the cache
	 * 
	 * @param activity the {@link ActivityCache} to remove
	 */
	public void remove(ActivityCache activity) {
		getActivities().remove(activity);
	}

	/**
	 * Remove all {@link ActivityCache}s from the cache
	 * 
	 * @param activity the {@link ActivityCache}s to remove
	 */
	public void removeAll(Collection<ActivityCache> activities) {
		getActivities().removeAll(activities);
	}

}
