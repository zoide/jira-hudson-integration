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

package com.marvelution.jira.plugins.hudson.charts.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Custom {@link DateFormat} implementation for build duration
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DurationFormat extends DateFormat {

	private static final long serialVersionUID = 1L;

	public static final long MILLISECONDS_IN_ONE_MINUTE = 60000L;
	public static final long MILLISECONDS_IN_ONE_HOUR = 60L * MILLISECONDS_IN_ONE_MINUTE;
	public static final long MILLISECONDS_IN_ONE_DAY = 24L * MILLISECONDS_IN_ONE_HOUR;
	public static final NumberFormat DAY_FORMAT = new DecimalFormat("#d ");
	public static final NumberFormat HOUR_FORMAT = new DecimalFormat("#h ");
	public static final NumberFormat MINUTE_FORMAT = new DecimalFormat("#m ");
	public static final NumberFormat SECOND_FORMAT = new DecimalFormat("#.#s");

	private NumberFormat dayFormat;
	private NumberFormat hourFormat;
	private NumberFormat minuteFormat;
	private NumberFormat secondFormat;
	private boolean timeInSeconds = false;
	private boolean showZeroDay = false;
	private boolean showZeroHour = false;
	private boolean showZeroMinute = false;
	private boolean showZeroSecond = false;

	/**
	 * Default constructor
	 */
	public DurationFormat() {
		this(DAY_FORMAT, HOUR_FORMAT, MINUTE_FORMAT, SECOND_FORMAT);
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
	 * Constructor
	 * 
	 * @param dayFormat the {@link NumberFormat} to use for the days
	 * @param hourFormat the {@link NumberFormat} to use for the hours
	 * @param minuteFormat the {@link NumberFormat} to use for the minutes
	 * @param secondFormat the {@link NumberFormat} to use for the seconds
	 */
	public DurationFormat(NumberFormat dayFormat, NumberFormat hourFormat, NumberFormat minuteFormat,
			NumberFormat secondFormat) {
		this.dayFormat = dayFormat;
		this.hourFormat = hourFormat;
		this.minuteFormat = minuteFormat;
		this.secondFormat = secondFormat;
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
	public Date parse(String date, ParsePosition parsePosition) {
		return null;
	}

	/**
	 * Getter for dayFormat
	 * 
	 * @return the dayFormat
	 */
	public NumberFormat getDayFormat() {
		return dayFormat;
	}

	/**
	 * Setter for dayFormat
	 * 
	 * @param dayFormat the dayFormat to set
	 */
	public void setDayFormat(NumberFormat dayFormat) {
		this.dayFormat = dayFormat;
	}

	/**
	 * Getter for hourFormat
	 * 
	 * @return the hourFormat
	 */
	public NumberFormat getHourFormat() {
		return hourFormat;
	}

	/**
	 * Setter for hourFormat
	 * 
	 * @param hourFormat the hourFormat to set
	 */
	public void setHourFormat(NumberFormat hourFormat) {
		this.hourFormat = hourFormat;
	}

	/**
	 * Getter for minuteFormat
	 * 
	 * @return the minuteFormat
	 */
	public NumberFormat getMinuteFormat() {
		return minuteFormat;
	}

	/**
	 * Setter for minuteFormat
	 * 
	 * @param minuteFormat the minuteFormat to set
	 */
	public void setMinuteFormat(NumberFormat minuteFormat) {
		this.minuteFormat = minuteFormat;
	}

	/**
	 * Getter for secondFormat
	 * 
	 * @return the secondFormat
	 */
	public NumberFormat getSecondFormat() {
		return secondFormat;
	}

	/**
	 * Setter for secondFormat
	 * 
	 * @param secondFormat the secondFormat to set
	 */
	public void setSecondFormat(NumberFormat secondFormat) {
		this.secondFormat = secondFormat;
	}

	/**
	 * Getter for timeInSeconds
	 * 
	 * @return the timeInSeconds
	 */
	public boolean isTimeInSeconds() {
		return timeInSeconds;
	}

	/**
	 * Setter for timeInSeconds
	 * 
	 * @param timeInSeconds the timeInSeconds to set
	 */
	public void setTimeInSeconds(boolean timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	/**
	 * Getter for showZeroDay
	 * 
	 * @return the showZeroDay
	 */
	public boolean isShowZeroDay() {
		return showZeroDay;
	}

	/**
	 * Setter for showZeroDay
	 * 
	 * @param showZeroDay the showZeroDay to set
	 */
	public void setShowZeroDay(boolean showZeroDay) {
		this.showZeroDay = showZeroDay;
	}

	/**
	 * Getter for showZeroHour
	 * 
	 * @return the showZeroHour
	 */
	public boolean isShowZeroHour() {
		return showZeroHour;
	}

	/**
	 * Setter for showZeroHour
	 * 
	 * @param showZeroHour the showZeroHour to set
	 */
	public void setShowZeroHour(boolean showZeroHour) {
		this.showZeroHour = showZeroHour;
	}

	/**
	 * Getter for showZeroMinute
	 * 
	 * @return the showZeroMinute
	 */
	public boolean isShowZeroMinute() {
		return showZeroMinute;
	}

	/**
	 * Setter for showZeroMinute
	 * 
	 * @param showZeroMinute the showZeroMinute to set
	 */
	public void setShowZeroMinute(boolean showZeroMinute) {
		this.showZeroMinute = showZeroMinute;
	}

	/**
	 * Getter for showZeroSecond
	 * 
	 * @return the showZeroSecond
	 */
	public boolean isShowZeroSecond() {
		return showZeroSecond;
	}

	/**
	 * Setter for showZeroSecond
	 * 
	 * @param showZeroSecond the showZeroSecond to set
	 */
	public void setShowZeroSecond(boolean showZeroSecond) {
		this.showZeroSecond = showZeroSecond;
	}

}
