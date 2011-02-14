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

package com.marvelution.hudson.plugins.apiv2.client.services;

import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * Abstract {@link ListableQuery} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @param <MODEL> the {@link Model} type
 * @param <LISTMODEL> the {@link Model} type
 */
public abstract class AbstractListableQuery<MODEL extends Model, LISTMODEL extends ListableModel<MODEL>>
		extends AbstractQuery<MODEL> implements ListableQuery<MODEL, LISTMODEL>, Query<MODEL> {

	private Class<LISTMODEL> listModelClass;

	/**
	 * Protected constructor so this class must be subclassed
	 *  
	 * @param modelClass the {@link Class}
	 */
	protected AbstractListableQuery(Class<MODEL> modelClass, Class<LISTMODEL> listModelClass) {
		super(modelClass);
		this.listModelClass = listModelClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<LISTMODEL> getListableModelClass() {
		return listModelClass;
	}

}
