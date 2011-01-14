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

package com.marvelution.hudson.plugins.apiv2.wink;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wink.common.WinkApplication;
import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.internal.registry.metadata.ProviderMetadataCollector;
import org.apache.wink.common.internal.registry.metadata.ResourceMetadataCollector;
import org.apache.wink.common.internal.utils.FileLoader;

import com.marvelution.hudson.plugins.apiv2.resources.impl.BaseRestResource;
import com.marvelution.hudson.plugins.apiv2.servlet.filter.HudsonAPIV2ServletFilter;

/**
 * {@link WinkApplication} implementation specific for Hudson
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 */
public class HudsonWinkApplication extends WinkApplication {

	public static final String[] DEFAULT_APPLICATION_RESOURCE_PACKAGES = {
		"com.marvelution.hudson.plugins.apiv2.resources",
	};

	private static final Logger logger = Logger.getLogger(HudsonAPIV2ServletFilter.class.getName());

	private Set<Class<?>> jaxRSClasses = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Class<?>> getClasses() {
		if (jaxRSClasses == null) {
			jaxRSClasses = new HashSet<Class<?>>();
			final Set<Class<?>> classes = new HashSet<Class<?>>();
			for (String resourcePackageName : DEFAULT_APPLICATION_RESOURCE_PACKAGES) {
				classes.addAll(getClassesFromPackage(resourcePackageName));
			}
			processClasses(classes);
			logger.info("Loaded all REST Resource/Provider classes");
		}
		return jaxRSClasses;
	}

	/**
	 * Get all the classes that are located in a source package.
	 * All sub packages are also processed.
	 * 
	 * @param resourcePackageName the source package to get all the classes for
	 * @return {@link Set} of {@link Class} objects that are located in the source package or one of its sub packages
	 */
	private Set<Class<?>> getClassesFromPackage(String resourcePackageName) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			final URL resourcePackage = FileLoader.loadFile(resourcePackageName.replace(".", "/"));
			for (String filename : new File(resourcePackage.toURI()).list()) {
				if (filename.endsWith(".class")) {
					final String className = filename.replace(".class", "");
					try {
						classes.add(Class.forName(resourcePackageName + "." + className));
					} catch (ClassNotFoundException e) {
						logger.log(Level.SEVERE, "Failed to load REST Resources in package: " + resourcePackageName
							+ "." + className, e);
					}
				} else {
					classes.addAll(getClassesFromPackage(resourcePackageName + "." + filename));
				}
			}
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Failed to load REST Resources in package: " + resourcePackageName, e);
		} catch (URISyntaxException e) {
			logger.log(Level.SEVERE, "Failed to load REST Resources in package: " + resourcePackageName, e);
		}
		return classes;
	}

	/**
	 * Process the given {@link Set} of {@link Class} objects and add them to the JAX-RS Class List if they are valid
	 * JAX-RS Classes
	 * 
	 * @param classes {@link Set} of {@link Class} objects to process
	 */
	private void processClasses(Set<Class<?>> classes) {
		for (Class<?> cls : classes) {
			if (ProviderMetadataCollector.isProvider(cls)) {
				logger.log(Level.FINE, "Loaded REST Provider class: " + cls.getName());
				jaxRSClasses.add(cls);
			} else if (ResourceMetadataCollector.isResource(cls)) {
				final Parent parent = (Parent) cls.getAnnotation(Parent.class);
				if ((parent != null && BaseRestResource.class.equals(parent.value()))) {
					logger.log(Level.FINE, "Loaded REST Resource class: " + cls.getName());
					jaxRSClasses.add(cls);
				} else if (BaseRestResource.class.equals(cls)) {
					logger.log(Level.FINE, "Loaded Base REST Resource class: " + cls.getName());
					jaxRSClasses.add(cls);
				} else {
					logger.log(Level.FINE, "Class [" + cls.getName() + "] is not a valid REST Resource, "
						+ "the @Parent(RestBaseResource.class) annotation is missing");
				}
			} else {
				logger.log(Level.FINE, "Skipping class [" + cls.getName() + "]; Its not a REST Resource or Provider");
			}
		}
	}

}
