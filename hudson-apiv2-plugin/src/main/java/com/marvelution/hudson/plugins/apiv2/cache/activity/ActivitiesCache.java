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
import java.util.Iterator;
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
public class ActivitiesCache implements Iterable<ActivityCache>, Collection<ActivityCache> {

	public static final long MERGE_TIMESPAN = 60 * 1000;

	private List<ActivityCache> activities = Lists.newArrayList();
	@XStreamOmitField
	private ActivityCache previousActivity = null;

	/**
	 * Getter for getActivities()
	 *
	 * @return the getActivities()
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
		return ordering.immutableSortedCopy(getActivities());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(ActivityCache activity) {
		if (previousActivity != null && previousActivity.isTheSame(activity)) {
			// The Activity was on the same object and executed by the same User, check to see if the activity needs
			// to be merged
			if (activity.getTimestamp() - previousActivity.getTimestamp() < MERGE_TIMESPAN) {
				return false;
			}
		}
		previousActivity = activity;
		return getActivities().add(activity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return getActivities().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return getActivities().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object paramObject) {
		return getActivities().contains(paramObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ActivityCache> iterator() {
		return getActivities().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return getActivities().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] paramArrayOfT) {
		return getActivities().toArray(paramArrayOfT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object paramObject) {
		return getActivities().remove(paramObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> paramCollection) {
		return getActivities().containsAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends ActivityCache> paramCollection) {
		return getActivities().addAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> paramCollection) {
		return getActivities().removeAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> paramCollection) {
		return getActivities().retainAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		getActivities().clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object paramObject) {
		return getActivities().equals(paramObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getActivities().hashCode();
	}

}
