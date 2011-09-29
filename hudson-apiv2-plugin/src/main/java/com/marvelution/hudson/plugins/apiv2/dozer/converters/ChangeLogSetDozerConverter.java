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

import hudson.scm.ChangeLogSet;
import hudson.scm.EditType;

import org.dozer.DozerConverter;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog.AffectedFile;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog.ChangeType;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog.Entry;

/**
 * Custom {@link DozerConverter} to convert a {@link ChangeLogSet} to a {@link ChangeLog}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@SuppressWarnings("rawtypes")
public class ChangeLogSetDozerConverter extends DozerConverter<ChangeLogSet, ChangeLog> {

	/**
	 * Constructor
	 */
	public ChangeLogSetDozerConverter() {
		super(ChangeLogSet.class, ChangeLog.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ChangeLog convertTo(ChangeLogSet source, ChangeLog destination) {
		ChangeLog changeLog = new ChangeLog();
		for (hudson.scm.ChangeLogSet.Entry entry : (ChangeLogSet<? extends hudson.scm.ChangeLogSet.Entry>) source) {
			final Entry newEntry = new Entry(entry.getAuthor().getFullName(), entry.getMsg());
			try {
				// Lets try to convert the affected files
				for (hudson.scm.ChangeLogSet.AffectedFile file : entry.getAffectedFiles()) {
					newEntry.getAffectedFiles().add(new AffectedFile(file.getPath(),
							convertChangeType(file.getEditType())));
				}
			} catch (Exception e) {
				// Oke this ChangeLogSet entry doesn't implement the affected files method. Default to the affected
				// paths implementation
				for (String path : entry.getAffectedPaths()) {
					newEntry.getAffectedFiles().add(new AffectedFile(path, ChangeType.UNKNOWN));
				}
			}
			changeLog.add(newEntry);
		}
		return changeLog;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChangeLogSet convertFrom(ChangeLog source, ChangeLogSet destination) {
		throw new UnsupportedOperationException("Unable to map from a ChangeLog into a ChangeLogSet");
	}

	/**
	 * Internal method to convert a {@link EditType} to a {@link ChangeType}
	 * 
	 * @param type the {@link EditType} to convert
	 * @return the {@link ChangeType}
	 */
	private ChangeType convertChangeType(EditType type) {
		if (EditType.ADD.equals(type)) {
			return ChangeType.ADD;
		} else if (EditType.EDIT.equals(type)) {
			return ChangeType.EDIT;
		} else if (EditType.DELETE.equals(type)) {
			return ChangeType.DELETE;
		} else {
			return ChangeType.UNKNOWN;
		}
	}

}
