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

import static com.atlassian.core.util.DateUtils.*;
import static org.apache.commons.lang.time.DateFormatUtils.*;

import java.util.Calendar;
import java.util.Date;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.I18nHelper;

/**
 * Date Formatting Utility class
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DateFormatUtils {

	private I18nHelper i18nHelper;
	private boolean useTimePastSinceCurrectTime = false;

	/**
	 * Default Constructor, will use the {@link ComponentManager} to get the {@link I18nHelper} class
	 * And will disable the 'Time Past Current Time' string formatting feature.
	 */
	public DateFormatUtils() {
		i18nHelper = ComponentManager.getInstance().getJiraAuthenticationContext().getI18nHelper();
		useTimePastSinceCurrectTime = false;
	}

	/**
	 * Constructor, will use the {@link JiraAuthenticationContext} to get the {@link I18nHelper} class
	 * And will disable the 'Time Past Current Time' string formatting feature.
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} to use to get the {@link I18nHelper} class
	 */
	public DateFormatUtils(JiraAuthenticationContext authenticationContext) {
		i18nHelper = authenticationContext.getI18nHelper();
		useTimePastSinceCurrectTime = false;
	}

	/**
	 * Constructor, will use the {@link JiraAuthenticationContext} to get the {@link I18nHelper} class
	 * And will enable or disable the 'Time Past Current Time' string formatting feature pending the value of the
	 * boolean argument.
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} used to get the {@link I18nHelper} class
	 * @param useTimePastSinceCurrectTime the 'Time Past Current Time' string formatting feature setting
	 */
	public DateFormatUtils(JiraAuthenticationContext authenticationContext, boolean useTimePastSinceCurrectTime) {
		this(authenticationContext);
		this.useTimePastSinceCurrectTime = useTimePastSinceCurrectTime;
	}

	/**
	 * Format the given time-stamp to a human-readable time string. If the 'Time Past Since Current Time' feature is
	 * not used, then the ISO Datetime format is used to format the given timestamp
	 * 
	 * @param timestamp the {@link Calendar} to format
	 * @return the formatted time-stamp
	 */
	public String format(Calendar timestamp) {
		return format(timestamp.getTimeInMillis());
	}

	/**
	 * Format the given time-stamp to a human-readable time string. If the 'Time Past Since Current Time' feature is
	 * not used, then the ISO Datetime format is used to format the given timestamp
	 * 
	 * @param timestamp the {@link Date} to format
	 * @return the formatted time-stamp
	 */
	public String format(Date timestamp) {
		return format(timestamp.getTime());
	}

	/**
	 * Format the given time-stamp to a human-readable time string. If the 'Time Past Since Current Time' feature is
	 * not used, then the ISO Datetime format is used to format the given timestamp
	 * 
	 * @param timestamp the time-stamp to format
	 * @return the formatted time-stamp
	 */
	public String format(long timestamp) {
		if (useTimePastSinceCurrectTime) {
			return getPastTimeString(timestamp);
		} else {
			final Date today = Calendar.getInstance(i18nHelper.getLocale()).getTime();
			if (ISO_DATE_FORMAT.format(today).hashCode() == ISO_DATE_FORMAT.format(timestamp).hashCode()) {
				return ISO_TIME_NO_T_FORMAT.format(timestamp);
			} else {
				return ISO_DATE_FORMAT.format(timestamp);
			}
		}
	}

	/**
	 * Format a given timestamp to a {@link String}
	 * 
	 * @param duration the timestamp in milliseconds
	 * @return the formatted time {@link String}
	 */
	public String getTimeSpanString(final long duration) {
		final long years = duration / YEAR_MILLIS;
		long remDuration = duration % YEAR_MILLIS;
		final long months = remDuration / MONTH_MILLIS;
		remDuration %= MONTH_MILLIS;
		final long days = remDuration / DAY_MILLIS;
		remDuration %= DAY_MILLIS;
		final long hours = remDuration / HOUR_MILLIS;
		remDuration %= HOUR_MILLIS;
		final long minutes = remDuration / MINUTE_MILLIS;
		remDuration %= MINUTE_MILLIS;
		final long seconds = remDuration / SECOND_MILLIS;
		remDuration %= SECOND_MILLIS;
		final long millisecs = remDuration;
		if (years > 0L) {
			return makeTimeSpanString(years, i18nHelper.getText("hudson.time.year", Long.valueOf(years)), months,
					i18nHelper.getText("hudson.time.month", Long.valueOf(months)));
		} else if (months > 0L && days == 1L) {
			return makeTimeSpanString(months, i18nHelper.getText("hudson.time.month", Long.valueOf(months)),
					days, i18nHelper.getText("hudson.time.day", Long.valueOf(days)));
		} else if (months > 0L) {
			return makeTimeSpanString(months, i18nHelper.getText("hudson.time.month", Long.valueOf(months)),
					days, i18nHelper.getText("hudson.time.days", Long.valueOf(days)));
		} else if (days == 1L) {
			return makeTimeSpanString(days, i18nHelper.getText("hudson.time.day", Long.valueOf(days)), hours,
					i18nHelper.getText("hudson.time.hour", Long.valueOf(hours)));
		} else if (days > 0L) {
			return makeTimeSpanString(days, i18nHelper.getText("hudson.time.days", Long.valueOf(days)), hours,
					i18nHelper.getText("hudson.time.hour", Long.valueOf(hours)));
		} else if (hours > 0L) {
			return makeTimeSpanString(hours, i18nHelper.getText("hudson.time.hour", Long.valueOf(hours)), minutes,
					i18nHelper.getText("hudson.time.minute", Long.valueOf(minutes)));
		} else if (minutes > 0L) {
			return makeTimeSpanString(minutes, i18nHelper.getText("hudson.time.minute", Long.valueOf(minutes)), seconds,
					i18nHelper.getText("hudson.time.second", Long.valueOf(seconds)));
		} else if (seconds >= 10L) {
			return i18nHelper.getText("hudson.time.second", Long.valueOf(seconds));
		} else if (seconds >= 1L) {
			return i18nHelper.getText( "hudson.time.second",
					Float.valueOf((float) seconds + (float) (millisecs / 100L) / 10.0F));
		} else if (millisecs >= 100L) {
			return i18nHelper.getText("hudson.time.second",
					Float.valueOf((float) (millisecs / 10L) / 100.0F));
		}
		return i18nHelper.getText("hudson.time.millisecond", Long.valueOf(millisecs));
	}

	/**
	 * Format a given timestamp to a {@link String}
	 * 
	 * @param duration the timestamp in milliseconds
	 * @return the formatted time {@link String}
	 */
	public String getPastTimeString(long duration) {
		return i18nHelper.getText("hudson.past.time", getTimeSpanString(getTimePastSince(duration)));
	}

	/**
	 * Private final formatter called before returning the formatted
	 * {@link String}
	 * 
	 * @param bigUnit the big time unit
	 * @param bigLabel the big time unit label
	 * @param smallUnit the small time unit
	 * @param smallLabel the small time unit label
	 * @return the formatted {@link String}
	 */
	private String makeTimeSpanString(long bigUnit, String bigLabel,
			long smallUnit, String smallLabel) {
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
		return Calendar.getInstance(i18nHelper.getLocale()).getTimeInMillis() - time;
	}

}
