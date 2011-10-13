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

import hudson.model.AbstractBuild;

import javax.ws.rs.Path;

import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.ChangeLogResource;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchBuildException;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.NoSuchJobException;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog;

/**
 * REST implementation for the {@link ChangeLogResource}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Scope(ScopeType.SINGLETON)
@Parent(BaseRestResource.class)
@Path("changelog")
public class ChangeLogResourceRestImpl extends BaseRestResource implements ChangeLogResource {

	private final Logger log = Logger.getLogger(ChangeLogResourceRestImpl.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChangeLog getChangeLog(String jobname, Integer buildNumber) throws NoSuchJobException, NoSuchBuildException {
		final AbstractBuild<?, ?> build = getHudsonBuild(jobname, buildNumber);
		log.fine("Getting changelog of build: " + build.getNumber() + " of job " + build.getParent().getFullName());
		return DozerUtils.getMapper().map(build.getChangeSet(), ChangeLog.class);
	}

}
