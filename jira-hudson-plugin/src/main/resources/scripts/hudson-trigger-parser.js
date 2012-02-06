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

AJS.$.namespace("AJS.hudson.trigger.parser");

/**
 * Parse the Triggers of the given Job and Build for the given Gadget
 * 
 * @param gadget the Gadget to parse the triggers for
 * @param server the Hudson server the Job came from
 * @param job the Hudson Job data
 * @param build the Hudson Job Build data
 * @return the parsed triggers as a single string
 */
AJS.hudson.trigger.parser.parse = function(gadget, server, job, build) {
	var triggers = new Array();
	for (var index in build.triggers) {
		if (build.triggers[index].type !== undefined) {
			var trigger = "";
			try {
				eval("trigger = AJS.hudson.trigger.parser.parse" + build.triggers[index].type + "(gadget, server, job, build, build.triggers[index]);");
			} catch (err) {
				trigger = gadget.getMsg("hudson.gadget.trigger.unknown");
			}
			if (AJS.$.inArray(trigger, triggers) == -1) {
				triggers.push(trigger);
			}
		} else {
			for (var name in build.triggers[index]) {
				var trigger = "";
				try {
					eval("trigger = AJS.hudson.trigger.parser.parse" + name + "(gadget, server, job, build, build.triggers[index]." + name + ");");
				} catch (err) {
					trigger = gadget.getMsg("hudson.gadget.trigger.unknown");
				}
				if (AJS.$.inArray(trigger, triggers) == -1) {
					triggers.push(trigger);
				}
			}
		}
	}
	if (triggers.length === 0) {
		triggers.push(gadget.getMsg("hudson.gadget.trigger.unknown"));
	}
	return triggers;
}

AJS.hudson.trigger.parser.parseProjectTrigger = function(gadget, server, job, build, trigger) {
	return AJS.format(gadget.getMsg("hudson.gadget.trigger.project"), server.host, trigger.name, trigger.url, trigger.buildNumber);
}

AJS.hudson.trigger.parser.parseRemoteTrigger = function(gadget, server, job, build, trigger) {
	if (trigger.host !== undefined && trigger.host.length > 0 && trigger.note !== undefined && trigger.note.length > 0) {
		return AJS.format(gadget.getMsg("hudson.gadget.trigger.remote.with.host.and.note"), server.host, trigger.host, trigger.note);
	} else if (trigger.note !== undefined && trigger.note.length > 0) {
		return AJS.format(gadget.getMsg("hudson.gadget.trigger.remote.with.note"), server.host, trigger.note);
	} else {
		return gadget.getMsg("hudson.gadget.trigger.remote");
	}
}

AJS.hudson.trigger.parser.parseSCMTrigger = function(gadget, server, job, build, trigger) {
	return gadget.getMsg("hudson.gadget.trigger.scm");
}

AJS.hudson.trigger.parser.parseTimeTrigger = function(gadget, server, job, build, trigger) {
	return gadget.getMsg("hudson.gadget.trigger.time");
}

AJS.hudson.trigger.parser.parseUserTrigger = function(gadget, server, job, build, trigger) {
	return AJS.format(gadget.getMsg("hudson.gadget.trigger.user"), server.host, trigger.username);
}

AJS.hudson.trigger.parser.parseUnknownTrigger = function(gadget, server, job, build, trigger) {
	return gadget.getMsg("hudson.gadget.trigger.unknown");
}
