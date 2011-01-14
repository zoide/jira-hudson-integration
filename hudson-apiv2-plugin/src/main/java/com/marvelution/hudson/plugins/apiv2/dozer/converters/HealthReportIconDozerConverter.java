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

package com.marvelution.hudson.plugins.apiv2.dozer.converters;

import org.dozer.DozerConverter;

import com.marvelution.hudson.plugins.apiv2.resources.model.HealthReportIcon;

/**
 * {@link DozerConverter} implementation to convert a Icon URL String (from {@link hudson.model.HealthReport}) to a
 * {@link HealthReportIcon}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HealthReportIconDozerConverter extends DozerConverter<String, HealthReportIcon> {

	/**
	 * Constructor
	 */
	public HealthReportIconDozerConverter() {
		super(String.class, HealthReportIcon.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HealthReportIcon convertTo(String source, HealthReportIcon destination) {
		if (source == null) {
			return HealthReportIcon.HEALTH_UNKNOWN;
		} else if (source.equals(HealthReportIcon.HEALTH_OVER_80.getIconName())) {
			return HealthReportIcon.HEALTH_OVER_80;
		} else if (source.equals(HealthReportIcon.HEALTH_60_TO_79.getIconName())) {
			return HealthReportIcon.HEALTH_60_TO_79;
		} else if (source.equals(HealthReportIcon.HEALTH_40_TO_59.getIconName())) {
			return HealthReportIcon.HEALTH_40_TO_59;
		} else if (source.equals(HealthReportIcon.HEALTH_20_TO_39.getIconName())) {
			return HealthReportIcon.HEALTH_20_TO_39;
		} else if (source.equals(HealthReportIcon.HEALTH_00_TO_19.getIconName())) {
			return HealthReportIcon.HEALTH_00_TO_19;
		}
		return HealthReportIcon.HEALTH_UNKNOWN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convertFrom(HealthReportIcon source, String destination) {
		if (source == null) {
			return "empty.gif";
		} else {
			return source.getIconName();
		}
	}

}
