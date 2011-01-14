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

package com.marvelution.hudson.plugins.apiv2.resources.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Views XML object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "ViewsType", namespace = NameSpaceUtils.VIEW_NAMESPACE)
@XmlRootElement(name = "Views", namespace = NameSpaceUtils.VIEW_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Views extends ListableModel<View> {

	@XmlElement(name = "view", required = true)
	private Collection<View> items;

	/**
	 * Default Constructor
	 */
	public Views() {
	}

	/**
	 * Getter for views
	 * 
	 * @return the views
	 */
	public Collection<View> getViews() {
		if (items == null) {
			items = new ArrayList<View>();
		}
		return items;
	}

	/**
	 * Add a single {@link View} to the collection
	 * 
	 * @param view the {@link View} to add
	 */
	public void add(View view) {
		getViews().add(view);
	}

	/**
	 * Add a {@link Collection} of {@link View} objects to the {@link Collection}
	 * 
	 * @param views the {@link Collection} of {@link View} objects to add
	 */
	public void addAll(Collection<View> views) {
		getViews().addAll(views);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<View> iterator() {
		return getViews().iterator();
	}

}
