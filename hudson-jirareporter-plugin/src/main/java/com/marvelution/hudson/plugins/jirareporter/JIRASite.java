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

import java.net.MalformedURLException;
import java.net.URL;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * JIRA Site object used by the Notifier
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JIRASite {

	public final String name;
	public final URL url;
	public final String username;
	public final String password;
	public final boolean supportsWikiStyle;

	/**
	 * Constructor
	 * 
	 * @param name the JIRA Site name
	 * @param url the JIRA Site {@link URL}
	 * @param username the username to use for Authentication with the JIRA Site
	 * @param password the password for the given username
	 * @param supportsWikiStyle flag is Wiki style texts are supported
	 */
	@DataBoundConstructor
	public JIRASite(String name, URL url, String username, String password, boolean supportsWikiStyle) {
		this.name = name;
		if (!url.toExternalForm().endsWith("/"))
		try {
			url = new URL(url.toExternalForm() + "/");
		} catch (MalformedURLException e) {
			// Ignore this, cannot happen any way
		}
		this.url = url;
		this.username = username;
		this.password = password;
		this.supportsWikiStyle = supportsWikiStyle;
	}
	
}
