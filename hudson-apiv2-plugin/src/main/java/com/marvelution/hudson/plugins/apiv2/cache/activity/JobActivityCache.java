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

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The Job Activity Cache object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
public class JobActivityCache implements ActivityCache {

	private final long timestamp;
	private final String job;
	private String culprit;
	private String parent;

	/**
	 * Constructor
	 *
	 * @param timestamp
	 * @param job
	 */
	public JobActivityCache(long timestamp, String job) {
		this.timestamp = timestamp;
		this.job = job;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJob() {
		return job;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCulprit() {
		return culprit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCulprit(String culprit) {
		this.culprit = culprit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTheSame(ActivityCache activity) {
		if (activity != null && activity instanceof JobActivityCache) {
			return (getJob().equals(activity.getJob())
				&& (getParent() == null || (getParent().equals(((JobActivityCache) activity).getParent())))
				&& (getCulprit() == null || (getCulprit().equals(activity.getCulprit()))));
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof ActivityCache) {
			return isTheSame((ActivityCache) object) && getTimestamp() == ((ActivityCache) object).getTimestamp();
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": at " + getTimestamp() + " by " + getCulprit() + " on " + getJob();
	}

}
