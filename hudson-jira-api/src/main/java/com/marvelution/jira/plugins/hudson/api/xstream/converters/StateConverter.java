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

package com.marvelution.jira.plugins.hudson.api.xstream.converters;

import com.marvelution.jira.plugins.hudson.api.model.State;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterMatcher;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * XStream {@link Converter} for {@link State}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class StateConverter implements Converter, ConverterMatcher {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return State.class.equals(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		final State result = (State) source;
		writer.setValue(result.name());
	}

	/**
	 * {@inheritDoc}
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		if (State.NOT_STARTED.name().equals(reader.getValue())) {
			return State.NOT_STARTED;
		} else if (State.BUILDING.name().equals(reader.getValue())) {
			return State.BUILDING;
		} else {
			return State.COMPLETED;
		}
	}

}
