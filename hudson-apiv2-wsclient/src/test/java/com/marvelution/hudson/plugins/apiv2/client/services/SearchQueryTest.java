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

package com.marvelution.hudson.plugins.apiv2.client.services;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import com.marvelution.hudson.plugins.apiv2.client.services.SearchQuery.IssueSearchQuery;

/**
 * Testcase for {@link SearchQuery}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
public class SearchQueryTest {

	/**
	 * Test {@link SearchQuery#createForIssueSearch(java.util.Collection)}
	 */
	@Test
	public void testSearchQueryWithoutAJob() {
		IssueSearchQuery query = SearchQuery.createForIssueSearch(Collections.singletonList("MARVJIRAHUDSON-201"));
		assertEquals("/apiv2/search/issues?key[]=MARVJIRAHUDSON-201&", query.getUrl());
	}

	/**
	 * Test {@link SearchQuery#createForIssueSearch(java.util.Collection, String))}
	 */
	@Test
	public void testSearchQueryWithAJob() {
		IssueSearchQuery query =
			SearchQuery.createForIssueSearch(Collections.singletonList("MARVJIRAHUDSON-201"),
				"JIRA Hudson Integration");
		assertEquals("/apiv2/search/issues?key[]=MARVJIRAHUDSON-201&jobname=JIRA%20Hudson%20Integration&",
			query.getUrl());
	}

}
