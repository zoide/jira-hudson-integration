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

package com.marvelution.jira.plugins.hudson;

import com.marvelution.jira.plugins.hudson.encryption.StringEncrypter;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.opensymphony.module.propertyset.PropertySet;
import org.apache.commons.lang.StringUtils;

/**
 * Hudson Server Configuration access object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonServerImpl implements HudsonServer {

	public static final String SERVER_NAME_KEY = "hudson.config.serverName";

	public static final String HOST_KEY = "hudson.config.host";

	public static final String USERNAME_KEY = "hudson.config.username";

	public static final String PASSWORD_KEY = "hudson.config.password";

	private PropertySet propertySet;
	
	private StringEncrypter encrypter;

	/**
	 * Constructor
	 * 
	 * @param propertyManager the {@link HudsonPropertyManager}
	 */
	public HudsonServerImpl(HudsonPropertyManager propertyManager) {
		this.propertySet = propertyManager.getPropertySet();
		encrypter = new StringEncrypter();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getServername() {
		return propertySet.getString(SERVER_NAME_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getHost() {
		return propertySet.getString(HOST_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getImageUrl() {
		return getHost() + HUDSON_IMAGE_LOCATION;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUsername() {
		return propertySet.getString(USERNAME_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPassword() {
		return encrypter.decrypt(propertySet.getString(PASSWORD_KEY));
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServername(String servername) {
		propertySet.setString(SERVER_NAME_KEY, servername);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHost(String host) {
		if (host.endsWith("/")) {
			host = host.substring(0, host.length() - 1);
		}
		propertySet.setString(HOST_KEY, host);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setUsername(String username) {
		propertySet.setString(USERNAME_KEY, username);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPassword(String password) {
		propertySet.setString(PASSWORD_KEY, encrypter.encrypt(password));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isHudsonConfigured() {
		return StringUtils.isNotBlank(getServername());
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSecuredHudsonServer() {
		return (StringUtils.isNotEmpty(getUsername()) && StringUtils.isNotEmpty(getPassword()));
	}

}
