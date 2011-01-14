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

import com.marvelution.hudson.plugins.apiv2.resources.model.Result;

/**
 * {@link DozerConverter} to convert a {@link hudson.model.Result} into a {@link Result} and back
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class ResultDozerConverter extends DozerConverter<hudson.model.Result, Result> {

	/**
	 * Constructor
	 */
	public ResultDozerConverter() {
		super(hudson.model.Result.class, Result.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Result convertTo(hudson.model.Result source, Result destination) {
		if (source == null) {
			return Result.NOT_BUILT;
		} else if (source.equals(hudson.model.Result.SUCCESS)) {
			return Result.SUCCESS;
		} else if (source.equals(hudson.model.Result.FAILURE)) {
			return Result.FAILURE;
		} else if (source.equals(hudson.model.Result.UNSTABLE)) {
			return Result.UNSTABLE;
		} else if (source.equals(hudson.model.Result.ABORTED)) {
			return Result.ABORTED;
		}
		return Result.NOT_BUILT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public hudson.model.Result convertFrom(Result source, hudson.model.Result destination) {
		if (source == null) {
			return hudson.model.Result.NOT_BUILT;
		} else if (source.equals(Result.SUCCESS)) {
			return hudson.model.Result.SUCCESS;
		} else if (source.equals(Result.FAILURE)) {
			return hudson.model.Result.FAILURE;
		} else if (source.equals(Result.UNSTABLE)) {
			return hudson.model.Result.UNSTABLE;
		} else if (source.equals(Result.ABORTED)) {
			return hudson.model.Result.ABORTED;
		}
		return hudson.model.Result.NOT_BUILT;
	}

}
