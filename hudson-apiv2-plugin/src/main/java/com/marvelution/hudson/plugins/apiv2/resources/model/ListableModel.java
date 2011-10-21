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

import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Base Model class for all Listable/Collection XML objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @param <MODEL> the {@link Model} type that can be listed in a {@link Collection}
 */
@XmlType(name = "ListableModelType", namespace = NameSpaceUtils.APIV2_NAMESPACE)
@XmlRootElement(name = "ListableModel", namespace = NameSpaceUtils.APIV2_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ListableModel<MODEL extends Model> extends Model implements Iterable<MODEL>, Collection<MODEL> {

	/**
	 * Getter for the items {@link Collection}
	 * 
	 * @return the {@link Collection} of <MODEL> objects
	 */
	public abstract Collection<MODEL> getItems();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return getItems().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return getItems().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(MODEL object) {
		return getItems().add(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends MODEL> collection) {
		if (collection != null) {
			return getItems().addAll(collection);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object object) {
		return getItems().remove(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> collection) {
		if (collection != null) {
			return getItems().removeAll(collection);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> collection) {
		if (collection != null) {
			return getItems().retainAll(collection);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object object) {
		return getItems().contains(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> collection) {
		if (collection != null) {
			return getItems().containsAll(collection);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		getItems().clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return getItems().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] arrrayOfType) {
		return getItems().toArray(arrrayOfType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<MODEL> iterator() {
		return getItems().iterator();
	}

}
