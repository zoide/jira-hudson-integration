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


/**
 * Cusomt {@link DozerConverter} to convert a String representing the skipped status to a Boolean
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.0.1
 */
public class StatusToIsSkippedDozerConverter extends DozerConverter<String, Boolean> {

	private static final String SKIPPED_STATUS = "SKIP";

	/**
	 * Constructor
	 */
	public StatusToIsSkippedDozerConverter() {
		super(String.class, Boolean.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean convertTo(String source, Boolean destination) {
		if (source.equalsIgnoreCase(SKIPPED_STATUS)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convertFrom(Boolean source, String destination) {
		if (source) {
			return SKIPPED_STATUS;
		} else {
			return "";
		}
	}

}
