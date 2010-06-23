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

package com.marvelution.jira.plugins.hudson.chart.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Duration {@link DateFormat} for the {@link HudsonChart}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DurationFormat extends DateFormat {

	private static final long serialVersionUID = 1L;

	private static final long MILLISECONDS_IN_ONE_MINUTE = 60000L;

	private static final long MILLISECONDS_IN_ONE_HOUR = 3600000L;

	private static final long MILLISECONDS_IN_ONE_DAY = 24L * MILLISECONDS_IN_ONE_HOUR;

	private static NumberFormat defaultDayFormat = new DecimalFormat("#d ");

	private static NumberFormat defaultHourFormat = new DecimalFormat("#h ");

	private static NumberFormat defaultMinuteFormat = new DecimalFormat("#m ");

	private static NumberFormat defaultSecondFormat = new DecimalFormat("#.#s");

	private NumberFormat dayFormat;

	private NumberFormat hourFormat;

	private NumberFormat minuteFormat;

	private NumberFormat secondFormat;

	private boolean showZeroDay;

	private boolean showZeroHour;

	private boolean showZeroMinute;

	private boolean showZeroSecond;

	private boolean timeInSeconds;

	/**
	 * Default constructor
	 */
	public DurationFormat() {
		this(defaultDayFormat, defaultHourFormat, defaultMinuteFormat, defaultSecondFormat);
	}

	/**
	 * Constructor for DuratonFormat times in seconds
	 * 
	 * @param timeInSeconds flag for time in seconds
	 */
	public DurationFormat(boolean timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	/**
	 * Constructor to set other NumberFormats
	 * 
	 * @param dayFormat the day {@link NumberFormat}
	 * @param hourFormat the hour {@link NumberFormat}
	 * @param minuteFormat the minute {@link NumberFormat}
	 * @param secondFormat the second {@link NumberFormat}
	 */
	public DurationFormat(NumberFormat dayFormat, NumberFormat hourFormat, NumberFormat minuteFormat,
							NumberFormat secondFormat) {
		setShowZeroDay(false);
		setShowZeroHour(false);
		setShowZeroMinute(false);
		setShowZeroSecond(false);
		timeInSeconds = false;
		setDayFormat(dayFormat);
		setHourFormat(hourFormat);
		setMinuteFormat(minuteFormat);
		setSecondFormat(secondFormat);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
		long time = date.getTime();
		if (this.timeInSeconds) {
			time *= 1000L;
		}
		long elapsed = time;
		final long days = elapsed / MILLISECONDS_IN_ONE_DAY;
		elapsed -= days * MILLISECONDS_IN_ONE_DAY;
		final long hours = elapsed / MILLISECONDS_IN_ONE_HOUR;
		elapsed -= hours * MILLISECONDS_IN_ONE_HOUR;
		final long minutes = elapsed / MILLISECONDS_IN_ONE_MINUTE;
		elapsed -= minutes * MILLISECONDS_IN_ONE_MINUTE;
		final double seconds = elapsed / 1000.0D;
		if ((days != 0L) || (this.showZeroDay)) {
			stringBuffer.append(this.dayFormat.format(days));
		}
		if ((hours != 0L) || (this.showZeroHour)) {
			stringBuffer.append(this.hourFormat.format(hours));
		}
		if ((minutes != 0L) || (this.showZeroMinute)) {
			stringBuffer.append(this.minuteFormat.format(minutes));
		}
		if ((seconds != 0.0D) || (this.showZeroSecond)) {
			stringBuffer.append(this.secondFormat.format(seconds));
		}
		return stringBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date parse(String string, ParsePosition parsePosition) {
		return null;
	}

	/**
	 * Set the Day {@link NumberFormat}
	 * 
	 * @param dayFormat the Day {@link NumberFormat}
	 */
	public void setDayFormat(NumberFormat dayFormat) {
		this.dayFormat = dayFormat;
	}

	/**
	 * Set the Hour {@link NumberFormat}
	 * 
	 * @param hourFormat the Hour {@link NumberFormat}
	 */
	public void setHourFormat(NumberFormat hourFormat) {
		this.hourFormat = hourFormat;
	}

	/**
	 * Set the Minute {@link NumberFormat}
	 * 
	 * @param minuteFormat the Minute {@link NumberFormat}
	 */
	public void setMinuteFormat(NumberFormat minuteFormat) {
		this.minuteFormat = minuteFormat;
	}

	/**
	 * Set the Second {@link NumberFormat}
	 * 
	 * @param secondFormat the Second {@link NumberFormat}
	 */
	public void setSecondFormat(NumberFormat secondFormat) {
		this.secondFormat = secondFormat;
	}

	/**
	 * @param showZeroDay to show zero days
	 */
	public void setShowZeroDay(boolean showZeroDay) {
		this.showZeroDay = showZeroDay;
	}

	/**
	 * @param showZeroHour to show zero hours
	 */
	public void setShowZeroHour(boolean showZeroHour) {
		this.showZeroHour = showZeroHour;
	}

	/**
	 * @param showZeroMinute to show zero minutes
	 */
	public void setShowZeroMinute(boolean showZeroMinute) {
		this.showZeroMinute = showZeroMinute;
	}

	/**
	 * @param showZeroSecond to show zero seconds
	 */
	public void setShowZeroSecond(boolean showZeroSecond) {
		this.showZeroSecond = showZeroSecond;
	}

}
