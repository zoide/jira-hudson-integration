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

import hudson.model.Cause;
import hudson.model.Cause.RemoteCause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserCause;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dozer.DozerConverter;

import com.marvelution.hudson.plugins.apiv2.resources.model.triggers.ProjectTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.triggers.RemoteTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.triggers.SCMTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.triggers.TimeTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.triggers.Trigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.triggers.UserTrigger;

/**
 * {@link DozerConverter} implementation to convert a {@link List} of {@link Cause} objects to a {@link List} of
 * {@link Trigger} implementations
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@SuppressWarnings("rawtypes")
public class TriggerDozerConverter extends DozerConverter<List, Collection> {

	private static final Logger LOGGER = Logger.getLogger(TriggerDozerConverter.class.getName());

	/**
	 * Constructor
	 */
	public TriggerDozerConverter() {
		super(List.class, Collection.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Trigger> convertTo(List source, Collection destination) {
		Collection<Trigger> triggers = new ArrayList<Trigger>();
		for (Cause cause : (List<Cause>) source) {
			if (cause instanceof UserCause) {
				triggers.add(UserTrigger.create(((UserCause) cause).getUserName()));
			} else if (cause instanceof UpstreamCause) {
				UpstreamCause upstreamCause = (UpstreamCause) cause;
				triggers.add(ProjectTrigger.create(upstreamCause.getUpstreamProject(), upstreamCause.getUpstreamUrl(),
						upstreamCause.getUpstreamBuild()));
			} else if (cause instanceof RemoteCause) {
				RemoteCause remoteCause = (RemoteCause) cause;
				String host = "";
				try {
					Field hostField = remoteCause.getClass().getDeclaredField("addr");
					hostField.setAccessible(true);
					host = hostField.get(remoteCause).toString();
				} catch (Exception e) {
					LOGGER.log(Level.FINE, "Failed to get the Remote Host information form the RemoteCause.");
				}
				triggers.add(RemoteTrigger.create(remoteCause.getShortDescription(), host));
			} else if (cause instanceof TimerTriggerCause) {
				triggers.add(new TimeTrigger());
			} else if (cause instanceof SCMTriggerCause) {
				triggers.add(new SCMTrigger());
			}
		}
		return triggers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Cause> convertFrom(Collection source, List destination) {
		throw new UnsupportedOperationException("Unable to map from a Collection to a List<Cause>");
	}

}
