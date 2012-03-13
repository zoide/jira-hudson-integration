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
import com.marvelution.hudson.plugins.apiv2.cache.issue.IssueKey;
import com.marvelution.hudson.plugins.apiv2.cache.issue.IssuesCache;
import com.marvelution.hudson.plugins.apiv2.utils.JiraKeyUtils;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;

/**
 * {@link RunListener} implementation to update the {@link IssuesCache}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
@SuppressWarnings("rawtypes")
@Extension
public class JiraIssueKeyCacheRunListener extends RunListener<Run> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCompleted(Run r, TaskListener listener) {
		Collection<IssueCache> toBeAdded = Lists.newArrayList();
		if (r instanceof AbstractBuild) {
			// We can only handle AbstractBuild implementations since we need the change log
			AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) r;
			for (Entry entry : (ChangeLogSet<? extends Entry>) build.getChangeSet()) {
				for (String key : JiraKeyUtils.getJiraIssueKeysFromText(entry.getMsg(),
						APIv2Plugin.getIssuesCache().getIssueKeyPattern())) {
					try {
						toBeAdded.add(new IssueCache(IssueKey.getIssueKey(key), r.getParent().getFullName(), r
							.getNumber()));
					} catch (Exception e) {
						// Ignore this
					}
				}
			}
		}
		APIv2Plugin.getIssuesCache().addAll(toBeAdded);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDeleted(Run r) {
		Collection<IssueCache> toBeRemoved = Lists.newArrayList();
		for (IssueCache cache : APIv2Plugin.getIssuesCache()) {
			if (cache.getJob().equals(r.getParent().getFullName()) && cache.getBuild() == r.getNumber()) {
				toBeRemoved.add(cache);
			}
		}
		APIv2Plugin.getIssuesCache().removeAll(toBeRemoved);
	}

}
