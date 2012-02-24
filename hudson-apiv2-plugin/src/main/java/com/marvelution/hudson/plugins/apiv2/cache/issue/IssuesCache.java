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

package com.marvelution.hudson.plugins.apiv2.cache.issue;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.list.SynchronizedList;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
public class IssuesCache implements Iterable<IssueCache>, Collection<IssueCache> {

	private List<IssueCache> issues = Lists.newArrayList();

	/**
	 * Getter for issues
	 *
	 * @return the issues
	 */
	@SuppressWarnings("unchecked")
	private List<IssueCache> getIssues() {
		return SynchronizedList.decorate(issues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return getIssues().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return getIssues().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object paramObject) {
		return getIssues().contains(paramObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return getIssues().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] paramArrayOfT) {
		return getIssues().toArray(paramArrayOfT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(IssueCache paramE) {
		return getIssues().add(paramE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object paramObject) {
		return getIssues().remove(paramObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> paramCollection) {
		return getIssues().containsAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends IssueCache> paramCollection) {
		return getIssues().addAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> paramCollection) {
		return getIssues().removeAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> paramCollection) {
		return getIssues().retainAll(paramCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		getIssues().clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IssueCache> iterator() {
		return getIssues().iterator();
	}

}
