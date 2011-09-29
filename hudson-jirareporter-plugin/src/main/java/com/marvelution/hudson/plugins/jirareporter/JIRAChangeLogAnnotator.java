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

package com.marvelution.hudson.plugins.jirareporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.rpc.soap.client.RemoteIssue;

import hudson.Extension;
import hudson.MarkupText;
import hudson.MarkupText.SubText;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogAnnotator;
import hudson.scm.ChangeLogSet.Entry;

/**
 * {@link ChangeLogAnnotator} implementation specific for JIRA issues
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Extension
public class JIRAChangeLogAnnotator extends ChangeLogAnnotator {

	private static final Logger LOGGER = Logger.getLogger(JIRAChangeLogAnnotator.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void annotate(AbstractBuild<?, ?> build, Entry change, MarkupText text) {
		if (!JIRASite.hasSite(build)) {
			// The project of this build is not configured with the JIRABuildResultReportNotifier so skip
			// changelog annotation
			return;
		}
		final Map<String, RemoteIssue> knownIssues = new HashMap<String, RemoteIssue>();
		final JIRASite site = JIRASite.getSite(build);
		if (site.checkIssueExistence) {
			// Checking for existing Issue Keys, loop through the message and get all the existing issues with one call
			JIRAClient client = null;
			try {
				client = site.createClient();
				List<String> keys = new ArrayList<String>();
				Matcher matcher = site.getIssuePattern().matcher(text.getText());
				while (matcher.find()) {
					keys.add(matcher.group(0));
				}
				if (!keys.isEmpty()) {
					String jqlQuery = "issuekey in (" + StringUtils.join(keys, ",") + ")";
					LOGGER.log(Level.FINER, "Searching with JQL: " + jqlQuery);
					for (RemoteIssue issue : client.getIssuesFromJqlSearch(jqlQuery, keys.size())) {
						LOGGER.log(Level.FINEST, "Found: " + issue.getKey());
						knownIssues.put(issue.getKey().toUpperCase(), issue);
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.FINER, "Unable to check the existence of one or more issues");
			} finally {
				if (client != null) {
					client.logout();
				}
			}
		}
		for (SubText token : text.findTokens(site.getIssuePattern())) {
			if (site.checkIssueExistence) {
				// Check is the issue exists before annotating it
				// Checked issue keys are stored in uppercase
				final String issueKey = token.group(0).toUpperCase();
				if (knownIssues.containsKey(issueKey)) {
					RemoteIssue issue = knownIssues.get(issueKey);
					token.surroundWith(String.format("<a href='%sbrowse/%s' tooltip='%s'>", site.url,
						issue.getKey(), Util.escape(issue.getSummary())), "</a>");
				} else {
					LOGGER.log(Level.FINER, "Key " + token.group(0) + " was not found, skipping it");
				}
			} else {
				// Issue existence check disabled, just assume its there
				token.surroundWith(String.format("<a href='%sbrowse/%s'>", site.url, token.group(0)), "</a>");
			}
		}
	}

}
