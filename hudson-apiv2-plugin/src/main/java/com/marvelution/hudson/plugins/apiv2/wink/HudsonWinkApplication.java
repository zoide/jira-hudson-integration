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
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wink.common.WinkApplication;
import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.internal.registry.metadata.ProviderMetadataCollector;
import org.apache.wink.common.internal.registry.metadata.ResourceMetadataCollector;

import com.marvelution.hudson.plugins.apiv2.resources.impl.BaseRestResource;
import com.marvelution.hudson.plugins.apiv2.servlet.filter.HudsonAPIV2ServletFilter;
import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;

/**
 * {@link WinkApplication} implementation specific for Hudson
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark rekveld</a>
 */
public class HudsonWinkApplication extends WinkApplication {

	public static final String[] DEFAULT_APPLICATION_RESOURCE_PACKAGES = {
		"com.marvelution.hudson.plugins.apiv2.resources",
	};

	private static final Logger LOGGER = Logger.getLogger(HudsonAPIV2ServletFilter.class.getName());

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
				try {
					Enumeration<URL> resources = HudsonPluginUtils.getPluginClassloader().getResources(resourcePackageName.replace(".", "/"));
					while (resources.hasMoreElements()) {
						URL resource = resources.nextElement();
						if (resource.getProtocol().equalsIgnoreCase("jar")) {
							// Load resources from the JAR file
							JarURLConnection connection = (JarURLConnection) resource.openConnection();
							classes.addAll(getClassesFromJarFile(connection.getJarFile(), resourcePackageName));
						} else if (resource.getProtocol().equalsIgnoreCase("file")) {
							// Load resources form File system
							classes.addAll(getClassesFromPackage(new File(resource.getFile()), resourcePackageName));
						} else {
							LOGGER.info("Skipping resource [" + resource.toString() + "]; Unsupport resource protocol");
						}
					}
				} catch (IOException e) {
				}
			}
			processClasses(classes);
			LOGGER.info("Loaded all REST Resource/Provider classes");
		}
		return jaxRSClasses;
	}

	/**
	 * Get all the classes that are located in a source package.
	 * All sub packages are also processed.
	 * 
	 * @param baseResource the {@link File} base resource to load from
	 * @param resourcePackageName the source package to get all the classes for
	 * @return {@link Set} of {@link Class} objects that are located in the source package or one of its sub packages
	 */
	private Set<Class<?>> getClassesFromPackage(File baseResource, String resourcePackageName) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for (File file : baseResource.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".class")) {
				final String className = file.getName().substring(0, file.getName().lastIndexOf('.'));
				try {
					classes.add(Class.forName(resourcePackageName + "." + className));
					LOGGER.log(Level.FINE, "Loaded Resource: " + resourcePackageName + "." + className);
				} catch (ClassNotFoundException e) {
					LOGGER.log(Level.SEVERE, "Failed to load REST Resources: " + resourcePackageName
						+ "." + className, e);
				}
			} else {
				classes.addAll(getClassesFromPackage(file, resourcePackageName + "." + file.getName()));
			}
		}
		return classes;
	}

	/**
	 * Get all the classes that are in a {@link JarFile} and that are in the given package
	 * 
	 * @param jarFile the {@link JarFile} to filter
	 * @param resourcePackageName the package to filter with
	 * @return {@link Set} of {@link Class} objects
	 * 
	 * @since 4.1.0
	 */
	private Set<Class<?>> getClassesFromJarFile(JarFile jarFile, String resourcePackageName) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
				String className = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf('.'));
				className = className.replaceAll("/", ".");
				if (className.startsWith(resourcePackageName)) {
					try {
						classes.add(Class.forName(className));
						LOGGER.log(Level.FINE, "Loaded Resource: " + className);
					} catch (ClassNotFoundException e) {
						LOGGER.log(Level.SEVERE, "Failed to load REST Resources: " + className, e);
					}
				}
			}
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
				LOGGER.log(Level.FINE, "Loaded REST Provider class: " + cls.getName());
				jaxRSClasses.add(cls);
			} else if (ResourceMetadataCollector.isResource(cls)) {
				final Parent parent = (Parent) cls.getAnnotation(Parent.class);
				if ((parent != null && BaseRestResource.class.equals(parent.value()))) {
					LOGGER.log(Level.FINE, "Loaded REST Resource class: " + cls.getName());
					jaxRSClasses.add(cls);
				} else if (BaseRestResource.class.equals(cls)) {
					LOGGER.log(Level.FINE, "Loaded Base REST Resource class: " + cls.getName());
					jaxRSClasses.add(cls);
				} else {
					LOGGER.log(Level.FINE, "Class [" + cls.getName() + "] is not a valid REST Resource, "
						+ "the @Parent(RestBaseResource.class) annotation is missing");
				}
			} else {
				LOGGER.log(Level.FINE, "Skipping class [" + cls.getName() + "]; Its not a REST Resource or Provider");
			}
		}
	}

}
