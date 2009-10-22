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

package com.marvelution.jira.plugins.hudson.utils;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.web.bean.I18nBean;
import com.marvelution.jira.plugins.hudson.model.triggers.LegacyCodeTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.ProjectTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.RemoteTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.SCMTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.TimeTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.Trigger;
import com.marvelution.jira.plugins.hudson.model.triggers.UserTrigger;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.opensymphony.user.User;

/**
 * Hudson Build Trigger Parser helper class
 * 
 * @author @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class HudsonBuildTriggerParser {

	private final I18nBean i18n;

	private final UserUtil userUtil;

	private HudsonServer server;

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation to get the {@link I18nBean}
	 *            from for the current user
	 * @param userUtil the {@link UserUtil} implementation
	 */
	public HudsonBuildTriggerParser(JiraAuthenticationContext authenticationContext, UserUtil userUtil) {
		i18n = authenticationContext.getI18nBean("com.marvelution.jira.plugins.hudson.hudson-trigger");
		this.userUtil = userUtil;
	}

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation to get the {@link I18nBean}
	 *            from for the current user
	 * @param userUtil the {@link UserUtil} implementation
	 * @param server the {@link HudsonServer} where the triggers originated from
	 */
	public HudsonBuildTriggerParser(JiraAuthenticationContext authenticationContext, UserUtil userUtil,
									HudsonServer server) {
		this(authenticationContext, userUtil);
		this.server = server;
	}

	/**
	 * Sets the {@link HudsonServer} to use during parsing of the triggers
	 * 
	 * @param server the {@link HudsonServer}
	 */
	public void setServer(HudsonServer server) {
		this.server = server;
	}

	/**
	 * Parse a given {@link Trigger} into a human readable message
	 * 
	 * @param <TRIGGER> the {@link Trigger} object type
	 * @param trigger the {@link Trigger} to parse
	 * @return the parsed human readable message
	 */
	public <TRIGGER extends Trigger> String parse(TRIGGER trigger) {
		if (trigger instanceof UserTrigger) {
			if (userUtil.getUser(((UserTrigger) trigger).getUserName()) != null) {
				final User user = userUtil.getUser(((UserTrigger) trigger).getUserName());
				return i18n.getText("hudson.trigger.jira.user", user.getName(), user.getFullName());
			} else {
				return i18n.getText("hudson.trigger.hudson.user", server.getHost(), ((UserTrigger) trigger)
					.getUserName());
			}
		} else if (trigger instanceof ProjectTrigger) {
			return i18n.getText("hudson.trigger.project", server.getHost(), ((ProjectTrigger) trigger)
				.getProjectUrl(), ((ProjectTrigger) trigger).getProjectName(), ((ProjectTrigger) trigger)
				.getBuildNumber());
		} else if (trigger instanceof LegacyCodeTrigger) {
			return i18n.getText("hudson.trigger.legacy");
		} else if (trigger instanceof RemoteTrigger) {
			if (StringUtils.isNotEmpty(((RemoteTrigger) trigger).getHost())
				&& StringUtils.isNotEmpty(((RemoteTrigger) trigger).getNote())) {
				return i18n.getText("hudson.trigger.remote.host.note", ((RemoteTrigger) trigger).getHost(),
					((RemoteTrigger) trigger).getNote());
			} else if (StringUtils.isNotEmpty(((RemoteTrigger) trigger).getHost())) {
				return i18n.getText("hudson.trigger.remote.host", ((RemoteTrigger) trigger).getHost());
			} else {
				return i18n.getText("hudson.trigger.remote.no.information");
			}
		} else if (trigger instanceof SCMTrigger) {
			return i18n.getText("hudson.trigger.scm");
		} else if (trigger instanceof TimeTrigger) {
			return i18n.getText("hudson.trigger.time");
		} else {
			return i18n.getText("hudson.trigger.unknown");
		}
	}

}
