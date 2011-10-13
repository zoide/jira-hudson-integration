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

package com.marvelution.jira.plugins.hudson.streams;

import static com.atlassian.streams.spi.StandardStreamsFilterOption.ACTIVITY_KEY;
import static com.atlassian.streams.spi.StandardStreamsFilterOption.USER;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.marvelution.jira.plugins.hudson.streams.HudsonStreamsFilterOptionProvider.HUDSON_ASSOCIATION_KEY;
import static com.marvelution.jira.plugins.hudson.streams.HudsonStreamsFilterOptionProvider.HUDSON_ACTIVITY_OBJECT_TYPE;
import static com.marvelution.jira.plugins.hudson.streams.HudsonStreamsFilterOptionProvider.HUDSON_BASE_ACTIVITY_OBJECT_TYPE;
import static com.marvelution.jira.plugins.hudson.streams.HudsonStreamsFilterOptionProvider.HUDSON_JOB_KEY;
import static com.marvelution.jira.plugins.hudson.streams.HudsonStreamsFilterOptionProvider.HUDSON_SERVER_KEY;

import java.net.URI;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.atlassian.plugin.webresource.UrlMode;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.streams.api.ActivityRequest;
import com.atlassian.streams.api.ActivityVerb;
import com.atlassian.streams.api.StreamsEntry;
import com.atlassian.streams.api.StreamsException;
import com.atlassian.streams.api.StreamsFeed;
import com.atlassian.streams.api.UserProfile;
import com.atlassian.streams.api.common.ImmutableNonEmptyList;
import com.atlassian.streams.api.common.Option;
import com.atlassian.streams.spi.Filters;
import com.atlassian.streams.spi.StreamsActivityProvider;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.marvelution.hudson.plugins.apiv2.client.ClientException;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.ActivityQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.User;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activities;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.ActivityType;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;

/**
 * Hudson specific implementation of the {@link StreamsActivityProvider}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
public class HudsonStreamsActivityProvider implements StreamsActivityProvider {

	private final Logger logger = Logger.getLogger(HudsonStreamsActivityProvider.class);
	private final I18nResolver i18nResolver;
	private final HudsonAssociationManager associationManager;
	private final HudsonServerManager serverManager;
	private final HudsonClientFactory clientFactory;
	private final WebResourceManager webResourceManager;

	/**
	 * Constructor
	 *
	 * @param i18nResolver the {@link I18nResolver} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 * @param webResourceManager the {@link WebResourceManager} implementation
	 */
	public HudsonStreamsActivityProvider(I18nResolver i18nResolver, HudsonAssociationManager associationManager,
					HudsonServerManager serverManager, HudsonClientFactory clientFactory,
					WebResourceManager webResourceManager) {
			this.i18nResolver = checkNotNull(i18nResolver, "i18nResolver");
			this.associationManager = checkNotNull(associationManager, "associationManager");
			this.serverManager = checkNotNull(serverManager, "serverManager");
			this.clientFactory = checkNotNull(clientFactory, "clientFactory");
			this.webResourceManager = checkNotNull(webResourceManager, "webResourceManager");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StreamsFeed getActivityFeed(ActivityRequest activityRequest) throws StreamsException {
		Set<ActivityType> activityTypes = getActivityFilters(activityRequest);
		Set<Integer> serverIds;
		Set<String> jobNames;
		Activities activities = new Activities();
		if (!activityTypes.contains(ActivityType.JOB)
				&& activityRequest.getProviderFilters().containsKey(HUDSON_ASSOCIATION_KEY)) {
			logger.debug("Processing Association filters to get all the ServerIds and Job Name to get the action for");
			Set<Integer> associationIds = getAssociationFilters(activityRequest);
			serverIds = Sets.newHashSet();
			jobNames = Sets.newHashSet();
			// Populate the serverIds and JobNames sets from the associations
			for (Integer associationId : associationIds) {
				HudsonAssociation association = associationManager.getAssociation(associationId);
				serverIds.add(association.getServerId());
				jobNames.add(association.getJobName());
			}
		} else {
			// Populate the serverIds and JobNames sets from the ActivityRequest
			logger.debug("Processing the Server and Jobs filters from the ActivityReqeust");
			serverIds = getServerFilters(activityRequest);
			jobNames = getJobnameFilters(activityRequest);
		}
		ActivityQuery query = ActivityQuery.createForActivities(activityTypes).setJobs(jobNames)
			.setUserIds(getUsernameFilters(activityRequest)).setMaxResults(activityRequest.getMaxResults());
		logger.debug("Created activity query: " + query.getUrl());
		for (Integer serverId : serverIds) {
			HudsonServer server = serverManager.getServer(serverId);
			HudsonClient client = clientFactory.create(server);
			try {
				logger.debug("Connecting to server " + server.getHost()
					+ " to get all the activities matching the query");
				activities.addAll(client.findAll(query));
			} catch (ClientException e) {
				logger.error("Failed to execute query: " +server.getHost() + query.getUrl());
				throw new StreamsException("Failed to get activities from server " + server.getName(), e);
			}
		}
		return new StreamsFeed(i18nResolver.getText("streams.hudson.feed.title"), transformActivities(activities),
			Option.<String>none());
	}

	/**
	 * Transform the given {@link Activities} to {@link StreamsEntry} objects
	 * 
	 * @param activities the {@link Activities} to transform
	 * @return the {@link Iterable} with {@link StreamsEntry} objects
	 */
	private Iterable<StreamsEntry> transformActivities(Activities activities) {
		Iterable<StreamsEntry> entries = Iterables.transform(activities, new Function<Activity, StreamsEntry>() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public StreamsEntry apply(Activity from) {
				return toStreamsEntry(from);
			}

		});
       return entries;
	}

	/**
	 * Transform a given {@link Activity} to a {@link StreamsEntry}
	 * 
	 * @param from the {@link Activity} to transform
	 * @return the {@link StreamsEntry}
	 */
	private StreamsEntry toStreamsEntry(final Activity from) {
		StreamsEntry.ActivityObject activityObject = new StreamsEntry.ActivityObject(StreamsEntry.ActivityObject
			.params().id("").alternateLinkUri(from.getUri()).activityObjectType(HUDSON_ACTIVITY_OBJECT_TYPE));
		ActivityVerb verb = HudsonStreamsFilterOptionProvider.getActivityVerbForActivity(from.getType());
		User user = from.getUser();
		UserProfile.Builder builder = new UserProfile.Builder(user.getUserId()).fullName(user.getUsername())
			.email(Option.option(user.getEmail()))
			.profilePictureUri(Option.option(
				URI.create(webResourceManager.getStaticPluginResource(JiraPluginUtils.getPluginKey()
				+ ":hudson-stream-resources", "useravatar.gif", UrlMode.ABSOLUTE))));
		if (StringUtils.isNotBlank(user.getUrl())) {
			logger.debug(user.getUrl() + ": " + URI.create(user.getUrl()).isAbsolute());
			builder.profilePageUri(Option.option(URI.create(user.getUrl())));
		}
		final StreamsEntry.Renderer renderer = new HudsonStreamsEntryRenderer(i18nResolver, from);;
		return new StreamsEntry(StreamsEntry.params()
			.id(from.getUri())
			.postedDate(new DateTime(from.getTimestamp()))
			.authors(ImmutableNonEmptyList.of(builder.build()))
			.addActivityObject(activityObject).verb(verb)
			.addLink(URI.create(webResourceManager.getStaticPluginResource(JiraPluginUtils.getPluginKey()
				+ ":hudson-stream-resources", "puzzle-piece.gif", UrlMode.ABSOLUTE)),
				StreamsActivityProvider.ICON_LINK_REL)
			.alternateLinkUri(from.getUri())
			.renderer(renderer)
			.applicationType(from.getSystem().getHumanName()), i18nResolver);
	}

	/**
	 * Get all the active Association Ids from the {@link ActivityRequest}
	 * 
	 * @param activityRequest the {@link ActivityRequest} to get all the association Ids from
	 * @return the {@link Set} of association ids
	 */
	private Set<Integer> getAssociationFilters(ActivityRequest activityRequest) {
		Set<Integer> ids = Sets.newHashSet();
		for (HudsonAssociation association : associationManager.getAssociations()) {
			ids.add(association.getAssociationId());
		}
		return getFiltersFromActivityRequest(activityRequest, HUDSON_ASSOCIATION_KEY, ids, toInteger);
	}

	/**
	 * Get all the active Server Ids from the {@link ActivityRequest}
	 * 
	 * @param activityRequest the {@link ActivityRequest} to get all the server Ids from
	 * @return the {@link Set} of server ids
	 */
	private Set<Integer> getServerFilters(ActivityRequest activityRequest) {
		Set<Integer> ids = Sets.newHashSet();
		for (HudsonServer server : serverManager.getServers()) {
			ids.add(server.getServerId());
		}
		return getFiltersFromActivityRequest(activityRequest, HUDSON_SERVER_KEY, ids, toInteger);
	}

	/**
	 * Get all the active Job Names from the {@link ActivityRequest}
	 * 
	 * @param activityRequest the {@link ActivityRequest} to get all the Job Names from
	 * @return the {@link Set} of Job Names
	 */
	private Set<String> getUsernameFilters(ActivityRequest activityRequest) {
		Set<String> usernames = Sets.newHashSet(Filters.getIsValues(activityRequest.getProviderFilters().get(
			USER.getKey())));
		Set<String> notValues = Filters.getNotValues(activityRequest.getProviderFilters().get(USER.getKey()));
		for (String value : notValues) {
			usernames.add("!" + value);
		}
		return usernames;
	}

	/**
	 * Get all the active Job Names from the {@link ActivityRequest}
	 * 
	 * @param activityRequest the {@link ActivityRequest} to get all the Job Names from
	 * @return the {@link Set} of Job Names
	 */
	private Set<String> getJobnameFilters(ActivityRequest activityRequest) {
		Set<String> jobs = Sets.newHashSet(Filters.getIsValues(activityRequest.getProviderFilters().get(
			HUDSON_JOB_KEY)));
		Set<String> notValues = Filters.getNotValues(activityRequest.getProviderFilters().get(HUDSON_JOB_KEY));
		for (String value : notValues) {
			jobs.add("!" + value);
		}
		return jobs;
	}

	/**
	 * Get all the active {@link HudsonActivity} objects from the {@link ActivityRequest}
	 * 
	 * @param activityRequest the {@link ActivityRequest} to get the options from
	 * @return the {@link Set} of {@link HudsonActivity} objects
	 */
	private Set<ActivityType> getActivityFilters(ActivityRequest activityRequest) {
		return getFiltersFromActivityRequest(activityRequest, ACTIVITY_KEY,
			Sets.newHashSet(ActivityType.values()), toHudsonActivity);
	}

	/**
	 * Get the Provider Filter values out of the {@link ActivityRequest}
	 * 
	 * @param activityRequest the {@link ActivityRequest}
	 * @param key the key of the Provider Filter
	 * @param allItems the default all items for the provider
	 * @param transformer the {@link Function} to use to transform the {@link String} value to the needed value
	 * @return {@link Set} of transformed values
	 */
	private <T> Set<T> getFiltersFromActivityRequest(ActivityRequest activityRequest, String key, Set<T> allItems,
			Function<String, T> transformer) {
		Set<String> isStringValues = Filters.getIsValues(activityRequest.getProviderFilters().get(key));
		Set<String> notStringValues = Filters.getNotValues(activityRequest.getProviderFilters().get(key));
		Set<T> isValues;
		if (isStringValues.isEmpty()) {
			isValues = ImmutableSet.copyOf(allItems);
		} else {
			isValues = ImmutableSet.copyOf(Iterables.transform(isStringValues, transformer));
		}
		Set<T> notValues = ImmutableSet.copyOf(Iterables.transform(notStringValues, transformer));
		return Sets.difference(isValues, notValues);
	}

	/**
	 * Converts an activity option key (in the form of a String) to an {@link ActivityType}.
	 */
	private static final Function<String, ActivityType> toHudsonActivity = new Function<String, ActivityType>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ActivityType apply(String option) {
			String hudsonActivity = option.substring((HUDSON_BASE_ACTIVITY_OBJECT_TYPE + ":").length());
			return ActivityType.valueOf(hudsonActivity.replace(" ", "_").toUpperCase());
		}

	};

	/**
	 * Converts an activity option key (in the form of a String) to an {@link Integer}.
	 */
	private static final Function<String, Integer> toInteger = new Function<String, Integer>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Integer apply(String option) {
			return Integer.parseInt(option);
		}

	};

}
