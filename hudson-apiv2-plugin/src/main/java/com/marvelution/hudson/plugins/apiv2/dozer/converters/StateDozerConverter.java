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

import com.marvelution.hudson.plugins.apiv2.resources.model.build.State;

/**
 * {@link DozerConverter} to convert a {@link hudson.model.Run.State} into a {@link State} and back
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@SuppressWarnings("rawtypes")
public class StateDozerConverter extends DozerConverter<hudson.model.AbstractBuild, State> {

	/**
	 * Constructor
	 */
	public StateDozerConverter() {
		super(hudson.model.AbstractBuild.class, State.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State convertTo(hudson.model.AbstractBuild source, State destination) {
		if (source.isBuilding()) {
			return State.BUILDING;
		} else if (source.hasntStartedYet()) {
			return State.NOT_STARTED;
		} else if (source.isLogUpdated()) {
			return State.COMPLETED;
		} else {
			return State.UNKNOWN;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public hudson.model.AbstractBuild<?, ?> convertFrom(State source, hudson.model.AbstractBuild destination) {
		throw new UnsupportedOperationException("Canot convert a State inot a hudson.model.AbstractBuild object");
	}

}
