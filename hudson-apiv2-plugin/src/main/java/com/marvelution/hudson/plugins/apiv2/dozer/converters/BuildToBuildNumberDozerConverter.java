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

import hudson.model.AbstractBuild;

import org.dozer.DozerConverter;

/**
 * {@link DozerConverter} implementation to convert a {@link Job} into a String containing the job name
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@SuppressWarnings("rawtypes")
public class BuildToBuildNumberDozerConverter extends DozerConverter<AbstractBuild, Integer> {

	/**
	 * Constructor
	 */
	public BuildToBuildNumberDozerConverter() {
		super(AbstractBuild.class, Integer.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer convertTo(AbstractBuild source, Integer destination) {
		if (source == null) {
			return 0;
		} else {
			return source.getNumber();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractBuild convertFrom(Integer source, AbstractBuild destination) {
		throw new UnsupportedOperationException("Cannot convert from a Build Number to a hudson.model.AbstractBuild object");
	}

}
