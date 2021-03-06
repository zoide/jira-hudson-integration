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

package com.marvelution.hudson.plugins.apiv2.resources.impl;

import java.util.logging.Logger;

import hudson.model.Hudson;

import javax.ws.rs.Path;

import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.ViewResource;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.ForbiddenException;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchViewException;
import com.marvelution.hudson.plugins.apiv2.resources.model.view.View;
import com.marvelution.hudson.plugins.apiv2.resources.model.view.Views;

/**
 * The {@link View} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@Scope(ScopeType.SINGLETON)
@Parent(BaseRestResource.class)
@Path("views")
public class ViewResourceRestImpl extends BaseRestResource implements ViewResource {

	private final Logger log = Logger.getLogger(ViewResourceRestImpl.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(String name) {
		hudson.model.View view = Hudson.getInstance().getView(name);
		if (view != null) {
			log.fine("Found view by name '" + name + "'. checking permissions");
			if (view.hasPermission(Hudson.READ)) {
				return DozerUtils.getMapper().map(view, View.class, DozerUtils.FULL_MAP_ID);
			}  else {
				throw new ForbiddenException();
			}
		}
		throw new NoSuchViewException(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Views getViews() {
		Views views = new Views();
		for (hudson.model.View view : Hudson.getInstance().getViews()) {
			log.fine("Found view '" + view.getViewName() + "'. checking permissions");
			if (view.hasPermission(Hudson.READ)) {
				views.add(DozerUtils.getMapper().map(view, View.class, DozerUtils.NAMEONLY_MAP_ID));
			}
		}
		return views;
	}

}
