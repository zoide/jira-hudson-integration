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

package com.marvelution.hudson.plugins.apiv2.client.unmarshallers;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * Utility class for getting an {@link Unmarshaller} for a given {@link Model}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public final class Unmarshallers {

	private static Map<Class<? extends Model>, Unmarshaller> unmarshallers =
		new HashMap<Class<? extends Model>, Unmarshaller>();

	/**
	 * Method to get an {@link Unmarshaller} for the given {@link Model} object
	 * 
	 * @param <MODEL> the type of the {@link Model} object
	 * @param model the <MODEL> object to get the {@link Unmarshaller} for
	 * @return the {@link Unmarshaller} for the given {@link Model}
	 * @throws JAXBException in case of exceptions
	 */
	public static <MODEL extends Model> Unmarshaller getUnmarshaller(MODEL model) throws JAXBException {
		return getUnmarshaller(model.getClass());
	}

	/**
	 * Method to get an {@link Unmarshaller} for the given {@link Class}
	 * 
	 * @param model the model {@link Class} to get the {@link Unmarshaller} for
	 * @return the {@link Unmarshaller}
	 * @throws JAXBException in case of exceptions
	 */
	public static Unmarshaller getUnmarshaller(Class<? extends Model> model) throws JAXBException {
		if (!unmarshallers.containsKey(model)) {
			JAXBContext context = JAXBContext.newInstance(model);
			unmarshallers.put(model, context.createUnmarshaller());
		}
		return unmarshallers.get(model);
	}

}
