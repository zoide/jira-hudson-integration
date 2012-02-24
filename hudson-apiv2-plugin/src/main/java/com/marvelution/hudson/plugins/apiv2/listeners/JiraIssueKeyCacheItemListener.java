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

import com.google.common.collect.Lists;
import com.marvelution.hudson.plugins.apiv2.APIv2Plugin;
import com.marvelution.hudson.plugins.apiv2.cache.issue.IssueCache;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.listeners.ItemListener;

/**
 * {@link ItemListener} implementation to keep the IssuesCache up to date on item rename and delete actions
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
@Extension
public class JiraIssueKeyCacheItemListener extends ItemListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDeleted(Item item) {
		Collection<IssueCache> toBeRemoved = Lists.newArrayList();
		for (IssueCache cache : APIv2Plugin.getIssuesCache()) {
			if (cache.getJob().equals(item.getFullName())) {
				toBeRemoved.add(cache);
			}
		}
		APIv2Plugin.getIssuesCache().removeAll(toBeRemoved);
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
		Collection<IssueCache> toBeRemoved = Lists.newArrayList();
		Collection<IssueCache> toBeAdded = Lists.newArrayList();
		for (IssueCache cache : APIv2Plugin.getIssuesCache()) {
			if (oldFullName.equals(cache.getJob())) {
				toBeAdded.add(new IssueCache(cache.getIssueKey(), newFullName, cache.getBuild()));
				toBeRemoved.add(cache);
			}
		}
		APIv2Plugin.getIssuesCache().removeAll(toBeRemoved);
		APIv2Plugin.getIssuesCache().addAll(toBeAdded);
	}

}
