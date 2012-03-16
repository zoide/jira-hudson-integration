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

package com.marvelution.jira.plugins.hudson.streams;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.StringUtils;

import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.streams.api.StreamsEntry;
import com.atlassian.streams.api.StreamsEntry.Html;
import com.atlassian.streams.api.StreamsEntry.Renderer;
import com.atlassian.streams.api.common.Option;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.ActivityType;

/**
 * Helper factory object to get {@link StreamsEntry} {@link Renderer} implementations for different activities
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @Since 4.2.0
 */
public class HudsonStreamsEntryRenderer implements StreamsEntry.Renderer {

	private final I18nResolver i18nResolver;
	private final Activity activity;

	/**
	 * Constructor
	 *
	 * @param i18nResolver the {@link I18nResolver} implementation
	 * @param Activity the {@link Activity}
	 */
	public HudsonStreamsEntryRenderer(I18nResolver i18nResolver, Activity activity) {
		this.i18nResolver = checkNotNull(i18nResolver, "i18nResolver");
		this.activity = checkNotNull(activity, "activity");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Html renderTitleAsHtml(StreamsEntry entry) {
		String userHtml = getUserHtmlPart(entry);
		String titleHtml = getTitleHtmlPart(entry);
		return new Html(i18nResolver.getText(getI18nKeyForActivity(activity.getType()), userHtml, titleHtml));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Option<Html> renderSummaryAsHtml(StreamsEntry entry) {
		return Option.none();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Option<Html> renderContentAsHtml(StreamsEntry entry) {
		return Option.none();
	}

	/**
	 * Get the I18N key for the given {@link ActivityType}
	 * 
	 * @param activity the {@link ActivityType} to get he I18N key for
	 * @return the I18N key
	 */
	private String getI18nKeyForActivity(ActivityType activity) {
		return String.format("streams.hudson.activity.title.%1$s", activity.name().toLowerCase().replace('_', '.'));
	}

	/**
	 * Getter for the Html Title part.
	 * 
	 * @param entry the {@link StreamsEntry}
	 * @return the Title html part
	 */
	private String getTitleHtmlPart(StreamsEntry entry) {
		if (activity.getUri() != null) {
			return "<a href='" + activity.getUri() + "'>" + activity.getTitle() + "</a>";
		}
		return activity.getTitle();
	}

	/**
	 * Getter for the Html User part. This Html contains either a link to the users page on the Hudson server or just
	 * the username if the user page is not available
	 * 
	 * @param entry the {@link StreamsEntry}
	 * @return the User html part
	 */
	private String getUserHtmlPart(StreamsEntry entry) {
		if (activity.getUser() != null && StringUtils.isNotBlank(activity.getUser().getUrl())) {
			return "<a href='" + activity.getUser().getUrl() + "'>" + activity.getUser().getUsername() + "</a>";
		}
		return activity.getUser().getUsername();
	}

}
