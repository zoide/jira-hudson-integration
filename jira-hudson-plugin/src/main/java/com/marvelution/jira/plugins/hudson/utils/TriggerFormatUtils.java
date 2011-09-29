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

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.I18nHelper;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.ProjectTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.RemoteTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.SCMTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.TimeTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.Trigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.UserTrigger;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * Utility class for formatting {@link Trigger} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class TriggerFormatUtils {

	private I18nHelper i18nHelper;
	private String contextPath = "";
	private HudsonServer server;

	/**
	 * Constructor, uses the given {@link JiraAuthenticationContext} to get a {@link I18nHelper} instance
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation
	 * @param contextPath the context path of the JIRA instance
	 * @param server the {@link HudsonServer}
	 */
	public TriggerFormatUtils(JiraAuthenticationContext authenticationContext, String contextPath,
			HudsonServer server) {
		i18nHelper = authenticationContext.getI18nHelper();
		this.contextPath = contextPath;
		this.server = server;
	}

	/**
	 * Method to format a {@link ProjectTrigger}
	 * 
	 * @param trigger the {@link ProjectTrigger} to format
	 * @return the formatted {@link ProjectTrigger}
	 */
	public String format(ProjectTrigger trigger) {
		return i18nHelper.getText("hudson.panel.build.trigger.project", server.getPublicHost(), trigger.getUrl(),
				trigger.getName(), String.valueOf(trigger.getBuildNumber()));
	}

	/**
	 * Method to format a {@link RemoteTrigger}
	 * 
	 * @param trigger the {@link RemoteTrigger} to format
	 * @return the formatted {@link RemoteTrigger}
	 */
	public String format(RemoteTrigger trigger) {
		if (StringUtils.isNotBlank(trigger.getHost()) && StringUtils.isNotBlank(trigger.getNote())) {
			return i18nHelper.getText("hudson.panel.build.trigger.remote.with.host.and.note", trigger.getHost(),
					trigger.getNote());
		} else if (StringUtils.isNotBlank(trigger.getNote())) {
			return i18nHelper.getText("hudson.panel.build.trigger.remote.with.note", trigger.getNote());
		} else {
			return i18nHelper.getText("hudson.panel.build.trigger.remote");
		}
	}

	/**
	 * Method to format a {@link SCMTrigger}
	 * 
	 * @param trigger the {@link SCMTrigger} to format
	 * @return the formatted {@link SCMTrigger}
	 */
	public String format(SCMTrigger trigger) {
		return i18nHelper.getText("hudson.panel.build.trigger.scm");
	}

	/**
	 * Method to format a {@link TimeTrigger}
	 * 
	 * @param trigger the {@link TimeTrigger} to format
	 * @return the formatted {@link TimeTrigger}
	 */
	public String format(TimeTrigger trigger) {
		return i18nHelper.getText("hudson.panel.build.trigger.time");
	}

	/**
	 * Method to format a {@link UserTrigger}
	 * 
	 * @param trigger the {@link UserTrigger} to format
	 * @return the formatted {@link UserTrigger}
	 */
	public String format(UserTrigger trigger) {
		if (UserUtils.userExists(trigger.getUsername())) {
			// We have a JIRA user that triggered the build create link to that users profile page
			final User user = UserUtils.getUser(trigger.getUsername());
			return i18nHelper.getText("hudson.panel.build.trigger.jira.user", contextPath, user.getName(),
				user.getDisplayName());
		}
		// It was a Hudson user that triggered the build
		return i18nHelper.getText("hudson.panel.build.trigger.hudson.user", server.getPublicHost(), trigger.getUsername());
	}

	/**
	 * Method to format a {@link Trigger}
	 * 
	 * @param trigger the {@link Trigger} to format
	 * @return the formatted {@link Trigger}
	 */
	public String format(Trigger trigger) {
		return i18nHelper.getText("hudson.panel.build.trigger.unknown");
	}

}
