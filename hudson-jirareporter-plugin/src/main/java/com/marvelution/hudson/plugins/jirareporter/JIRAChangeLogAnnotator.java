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

import hudson.Extension;
import hudson.MarkupText;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void annotate(AbstractBuild<?, ?> build, Entry change, MarkupText text) {
		if (JIRASite.getSite(build) == null) {
			// The project of this build is not configured with the JIRABuildResultReportNotifier so skip
			// changelog annotation
			return;
		}
		// TODO Implement the changelog annotation
	}

}
