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
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dozer.DozerConverter;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.ProjectTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.RemoteTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.SCMTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.TimeTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.Trigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.UnknownTrigger;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.triggers.UserTrigger;

/**
 * {@link DozerConverter} implementation to convert a {@link List} of {@link Cause} objects to a {@link List} of
 * {@link Trigger} implementations
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class TriggerDozerConverter extends DozerConverter<Cause, Trigger> {

	private static final Logger LOGGER = Logger.getLogger(TriggerDozerConverter.class.getName());

	/**
	 * Constructor
	 */
	public TriggerDozerConverter() {
		super(Cause.class, Trigger.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trigger convertTo(Cause source, Trigger destination) {
		if (source instanceof UpstreamCause) {
			UpstreamCause upstreamCause = (UpstreamCause) source;
			return ProjectTrigger.create(upstreamCause.getUpstreamProject(), upstreamCause.getUpstreamUrl(),
					upstreamCause.getUpstreamBuild(), source.getShortDescription());
		} else if (source instanceof RemoteCause) {
			RemoteCause remoteCause = (RemoteCause) source;
			String host = "";
			try {
				Field hostField = remoteCause.getClass().getDeclaredField("addr");
				hostField.setAccessible(true);
				host = hostField.get(remoteCause).toString();
			} catch (Exception e) {
				LOGGER.log(Level.FINEST, "Failed to get the Remote Host information form the RemoteCause.");
			}
			return RemoteTrigger.create(remoteCause.getShortDescription(), host);
		} else if (source instanceof TimerTriggerCause) {
			return TimeTrigger.create(-1L, source.getShortDescription());
		} else if (source instanceof SCMTriggerCause) {
			return SCMTrigger.create(source.getShortDescription());
		} else {
			// It might be a UserIdCause
			// This is needed as Hudson doesn't have the UserIdCause that Jenkins has
			try {
				Method useridGetter = source.getClass().getMethod("getUserId");
				String userid = (String) useridGetter.invoke(source);
				return UserTrigger.create(userid, source.getShortDescription());
			} catch (Exception e) {
				// It might be a UserCause
				// This is needed as Hudson doesn't have the UserIdCause that Jenkins has, but they both have the
				// getUsername() method
				try {
					Method usernameGetter = source.getClass().getMethod("getUserName");
					String username = (String) usernameGetter.invoke(source);
					return UserTrigger.create(username, source.getShortDescription());
				} catch (Exception ee) {
					// Its not a User(Id)Cause so ignore the exception
				}
			}
		}
		return UnknownTrigger.create(source.getClass().getSimpleName(), source.getShortDescription());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cause convertFrom(Trigger source, Cause destination) {
		throw new UnsupportedOperationException("Unable to map from a Trigger to a Cause");
	}

}
