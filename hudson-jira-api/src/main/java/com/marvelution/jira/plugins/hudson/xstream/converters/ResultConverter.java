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

package com.marvelution.jira.plugins.hudson.xstream.converters;

import com.marvelution.jira.plugins.hudson.model.Result;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterMatcher;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * XStream {@link Converter} for {@link Result}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ResultConverter implements Converter, ConverterMatcher {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class clazz) {
		return (Result.class.equals(clazz));
	}

	/**
	 * {@inheritDoc}
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		final Result result = (Result) source;
		writer.setValue(result.name());
	}

	/**
	 * {@inheritDoc}
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		if (Result.SUCCESS.name().equals(reader.getValue())) {
			return Result.SUCCESS;
		} else if (Result.FAILURE.name().equals(reader.getValue())) {
			return Result.FAILURE;
		} else if (Result.UNSTABLE.name().equals(reader.getValue())) {
			return Result.UNSTABLE;
		} else if (Result.ABORTED.name().equals(reader.getValue())) {
			return Result.ABORTED;
		} else {
			return Result.NOT_BUILT;
		}
	}

}
