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

package com.marvelution.jira.plugins.hudson.services.servers;

import org.apache.commons.lang.StringUtils;

/**
 * Hudson Server utility class
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
public class HudsonServerUtils {

	/**
	 * Get a Whitelist url for the given {@link HudsonServer} host url
	 * 
	 * @param server the {@link HudsonServer}
	 * @return the whitelist url
	 */
	public static String getHostWhitelistUrl(HudsonServer server) {
		return getWhitelistUrl(server.getHost());
	}

	/**
	 * Get a Whitelist url for the given {@link HudsonServer} public host url
	 * 
	 * @param server the {@link HudsonServer}
	 * @return the whitelist url
	 */
	public static String getPublicHostWhitelistUrl(HudsonServer server) {
		return getWhitelistUrl(server.getPublicHost());
	}

	/**
	 * Get a Whitelist url for the given url
	 * 
	 * @param url the url to get a whitelist url for
	 * @return the whitelist url
	 */
	public static String getWhitelistUrl(String url) {
		if (url.endsWith("/")) {
			return url + "*";
		} else {
			return url + "/*";
		}
	}

	/**
	 * Check method to see if a {@link HudsonServer} is secured or anonymous
	 * 
	 * @param server the {@link HudsonServer} to check
	 * @return <code>true</code> if username and password are set (secured), <code>false</code> (anonymous) otherwise
	 */
	public static boolean isServerSecured(HudsonServer server) {
		return StringUtils.isNotBlank(server.getUsername()) && StringUtils.isNotBlank(server.getPassword());
	}

}
