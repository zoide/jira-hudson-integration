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

import java.util.Collection;

import org.dozer.ConfigurableCustomConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;
import org.dozer.MappingException;
import org.dozer.util.ReflectionUtils;

import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class CollectionToListableModelDozerConverter implements ConfigurableCustomConverter, MapperAware {

	private Mapper mapper;
	private String parameter;

	/**
	 * Constructor
	 */
	public CollectionToListableModelDozerConverter() {
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
	    if (ListableModel.class.equals(sourceClass) || ListableModel.class.isAssignableFrom(sourceClass)) {
	    	return convertToCollection((ListableModel<?>) sourceFieldValue,
	    			(Collection<?>) existingDestinationFieldValue);
	    } else if (ListableModel.class.equals(destinationClass) ||
	    		ListableModel.class.isAssignableFrom(destinationClass)) {
    		try {
				existingDestinationFieldValue = destinationClass.newInstance();
			} catch (Exception e) {
				throw new MappingException("Failed to create a new " + destinationClass.getName(), e);
			}
	    	return convertToListableModel((Collection<?>) sourceFieldValue,
	    			(ListableModel<? extends Model>) existingDestinationFieldValue);
	    } else {
	    	throw new MappingException("Both source and destination classes are not of type ListableModel");
	    }
	}

	@SuppressWarnings("unchecked")
	private <MODEL extends Model> ListableModel<MODEL> convertToListableModel(Collection<?> source,
			ListableModel<MODEL> destination) {
		if (source != null) {
			Class<MODEL> target = (Class<MODEL>) ReflectionUtils.determineGenericsType(destination.getClass()
					.getGenericSuperclass());
			for (Object item : source) {
				destination.add(target.cast(mapper.map(item, target)));
			}
		}
		return destination;
	}

	private Collection<?> convertToCollection(ListableModel<? extends Model> source, Collection<?> destination) {
		// TODO Code me
		return destination;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Getter for the Converter parameter
	 * 
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

}
