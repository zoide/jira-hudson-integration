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

package com.marvelution.hudson.plugins.apiv2.dozer.converters;

import java.util.Collection;
import java.util.HashSet;

import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;

import org.dozer.DozerConverter;

import com.marvelution.hudson.plugins.apiv2.utils.JiraKeyUtils;

/**
 * {@link DozerConverter} implementation to convert {@link ChangeLogSet} into a {@link Collection} of JIRA Issue Keys
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@SuppressWarnings("rawtypes")
public class RelatedIssueKeysDozerConverter extends DozerConverter<ChangeLogSet, Collection> {

	/**
	 * Constructor
	 */
	public RelatedIssueKeysDozerConverter() {
		super(ChangeLogSet.class, Collection.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> convertTo(ChangeLogSet source, Collection destination) {
		Collection<String> issueKeys = new HashSet<String>();
		for (Entry entry : (ChangeLogSet<? extends Entry>)source) {
			issueKeys.addAll(JiraKeyUtils.getJiraIssueKeysFromText(entry.getMsg()));
		}
		return issueKeys;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChangeLogSet<? extends Entry> convertFrom(Collection source, ChangeLogSet destination) {
		throw new UnsupportedOperationException("Unable to map from a Collection to a ChangeLogSet");
	}

}
