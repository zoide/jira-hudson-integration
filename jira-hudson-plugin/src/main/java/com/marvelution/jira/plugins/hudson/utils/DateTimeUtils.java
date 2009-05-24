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

import java.util.Calendar;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.I18nBean;

/**
 * Helper class for formatting Dates and Times
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DateTimeUtils {

	public static final long SECOND = 1000L;

	public static final long MINUTE = 60 * SECOND;

	public static final long HOUR = 60 * MINUTE;

	public static final long DAY = 24 * HOUR;

	public static final long MONTH = 30 * DAY;

	public static final long YEAR = 365 * DAY;

	private I18nBean i18nBean;

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} used to get the {@link I18nBean} for the
	 *            current user
	 */
	public DateTimeUtils(JiraAuthenticationContext authenticationContext) {
		i18nBean = authenticationContext.getI18nBean("com.marvelution.jira.plugins.hudson.hudson-datetime");
	}

	/**
	 * Format a given timestamp to a {@link String}
	 * 
	 * @param duration the timestamp in milliseconds
	 * @return the formatted time {@link String}
	 */
	public String getTimeSpanString(final long duration) {
		final long years = duration / YEAR;
		long remDuration = duration % YEAR;
		final long months = remDuration / MONTH;
		remDuration %= MONTH;
		final long days = remDuration / DAY;
		remDuration %= DAY;
		final long hours = remDuration / HOUR;
		remDuration %= HOUR;
		final long minutes = remDuration / MINUTE;
		remDuration %= MINUTE;
		final long seconds = remDuration / SECOND;
		remDuration %= SECOND;
		final long millisecs = remDuration;

		if (years > 0L) {
			return makeTimeSpanString(years, i18nBean.getText("hudson.time.year", Long.valueOf(years)), months,
				i18nBean.getText("hudson.time.month", Long.valueOf(months)));
		} else if (months > 0L && days == 1L) {
			return makeTimeSpanString(months, i18nBean.getText("hudson.time.month", Long.valueOf(months)), days,
				i18nBean.getText("hudson.time.day", Long.valueOf(days)));
		} else if (months > 0L) {
			return makeTimeSpanString(months, i18nBean.getText("hudson.time.month", Long.valueOf(months)), days,
				i18nBean.getText("hudson.time.days", Long.valueOf(days)));
		} else if (days == 1L) {
			return makeTimeSpanString(days, i18nBean.getText("hudson.time.day", Long.valueOf(days)), hours, i18nBean
				.getText("hudson.time.hour", Long.valueOf(hours)));
		} else if (days > 0L) {
			return makeTimeSpanString(days, i18nBean.getText("hudson.time.days", Long.valueOf(days)), hours, i18nBean
				.getText("hudson.time.hour", Long.valueOf(hours)));
		} else if (hours > 0L) {
			return makeTimeSpanString(hours, i18nBean.getText("hudson.time.hour", Long.valueOf(hours)), minutes,
				i18nBean.getText("hudson.time.minute", Long.valueOf(minutes)));
		} else if (minutes > 0L) {
			return makeTimeSpanString(minutes, i18nBean.getText("hudson.time.minute", Long.valueOf(minutes)),
				seconds, i18nBean.getText("hudson.time.second", Long.valueOf(seconds)));
		} else if (seconds >= 10L) {
			return i18nBean.getText("hudson.time.second", Long.valueOf(seconds));
		} else if (seconds >= 1L) {
			return i18nBean.getText("hudson.time.second", Float.valueOf((float) seconds + (float) (millisecs / 100L)
				/ 10.0F));
		} else if (millisecs >= 100L) {
			return i18nBean.getText("hudson.time.second", Float.valueOf((float) (millisecs / 10L) / 100.0F));
		}
		return i18nBean.getText("hudson.time.millisecond", Long.valueOf(millisecs));
	}

	/**
	 * Format a given timestamp to a {@link String}
	 * 
	 * @param duration the timestamp in milliseconds
	 * @return the formatted time {@link String}
	 */
	public String getPastTimeString(long duration) {
		return i18nBean.getText("hudson.past.time", getTimeSpanString(duration));
	}

	/**
	 * private final formatter called before returning the formatted {@link String}
	 * 
	 * @param bigUnit the big time unit
	 * @param bigLabel the big time unit label
	 * @param smallUnit the small time unit
	 * @param smallLabel the small time unit label
	 * @return the formatted {@link String}
	 */
	private String makeTimeSpanString(long bigUnit, String bigLabel, long smallUnit, String smallLabel) {
		String text = bigLabel;
		if (bigUnit < 10L) {
			text = text + ' ' + smallLabel;
		}
		return text;
	}

	/**
	 * Gets the time past since the time
	 * 
	 * @param time the time in milliseconds given
	 * @return the time in milliseconds that past
	 */
	public long getTimePastSince(long time) {
		return Calendar.getInstance(i18nBean.getLocale()).getTimeInMillis() - time;
	}

}
