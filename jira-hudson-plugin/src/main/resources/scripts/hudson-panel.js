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

AJS.$.namespace("AJS.hudson.panel");

/**
 * Base object containing all the options for the panel
 */
AJS.hudson.panel.config = {};

/**
 * Method to initialize the Hudson panel
 * 
 * @param config the configuration to use when initialize the panel
 */
AJS.hudson.panel.initializePanel = function(config) {
	AJS.hudson.panel.config = config;
	var viewsWidth = 0;
	AJS.$("#views .tab").each(function(index, item) {
		var tab = AJS.$("#" + item.id);
		var tabData = tab.attr('id').split('_');
		viewsWidth += tab.width() + 20;
		tab.click(function(event) {
			AJS.hudson.panel.toggleView(tabData[1]);
			event.preventDefault();
		});
		if (AJS.hudson.panel.config.view == "" || AJS.hudson.panel.config.view == tabData[1]) {
			tab.addClass("selected");
			AJS.hudson.panel.config.view = tabData[1];
		}
	});
	var associationsWidth = 0;
	AJS.$("#associations .tab").each(function(index, item) {
		var tab = AJS.$("#" + item.id);
		var tabData = tab.attr('id').split('_');
		associationsWidth += tab.width() + 20;
		tab.click(function(event) {
			AJS.hudson.panel.toggleAssociation(tabData[1]);
			event.preventDefault();
		});
		if (AJS.hudson.panel.config.associationId == 0 || AJS.hudson.panel.config.associationId == tabData[1]) {
			tab.addClass("selected");
			AJS.hudson.panel.config.associationId = tabData[1];
		}
	});
	var associationsWrapper = AJS.$("#sliderWrapper");
	var wrapperWidth = AJS.$("#hudsonPanelContent").width() - viewsWidth - 50;
	associationsWrapper.width(wrapperWidth);
	if (associationsWidth > wrapperWidth) {
		var sliderLeft = AJS.$("#sliderLeft").css("display", "block");
		var sliderRight = AJS.$("#sliderRight").css("display", "block");
		associationsWrapper.parent().width(wrapperWidth);
		associationsWrapper.width(wrapperWidth - (associationsWrapper.height() * 2) - 10);
		associationsWrapper.css("margin-left", "14px");
		associationsWrapper.css("margin-right", "14px");
		sliderLeft.hover(function() {
			sliderLeft.css({"background-color": "#FFF"});
			AJS.$("#associations li").animate({left: "0px"}, 4500);
		}, function() {
			sliderLeft.css({"background-color": "#CCC"});
			AJS.$("#associations li").stop();
		});
		sliderLeft.click(function() {
			AJS.$("#associations li").stop();
			AJS.$("#associations li").animate({left: "0px"}, 200);
		});
		sliderRight.css("margin-top", "-" + sliderRight.height() + "px");
		var leftPosition = "-" + (associationsWidth - (wrapperWidth / 2)) + "px";
		sliderRight.hover(function() {
			sliderRight.css({"background-color": "#FFF"});
			AJS.$("#associations li").animate({left: leftPosition}, 4500);
		}, function() {
			sliderRight.css({"background-color": "#CCC"});
			AJS.$("#associations li").stop();
		});
		sliderRight.click(function() {
			AJS.$("#associations li").stop();
			AJS.$("#associations li").animate({left: leftPosition}, 200);
		});
	}
	AJS.hudson.panel.updatePanelContent();
}

/**
 * Method to toggle the view showing in the hudsonPanelContentHtml
 * 
 * @param view the view to show
 */
AJS.hudson.panel.toggleView = function(view) {
	AJS.hudson.panel.config.view = view;
	AJS.$("#views .tab").each(function(index, item) {
		AJS.$("#" + item.id).removeClass("selected");
	});
	AJS.$("#view_" + view).addClass("selected");
	AJS.hudson.panel.updatePanelContent();
}

/**
 * Method to toggle the association showing in the hudsonPanelContentHtml
 * 
 * @param associationId the association id to show
 */
AJS.hudson.panel.toggleAssociation = function(associationId) {
	AJS.hudson.panel.config.associationId = associationId;
	AJS.$("#associations .tab").each(function(index, item) {
		AJS.$("#" + item.id).removeClass("selected");
	});
	AJS.$("#association_" + associationId).addClass("selected");
	AJS.hudson.panel.updatePanelContent();
}

/**
 * Method to update the hudsonPanelContentHtml div with a request from the Hudson server
 */
AJS.hudson.panel.updatePanelContent = function() {
	AJS.$.ajax({
	    type : "GET",
	    dataType: "html",
	    data: ({
	    	view: AJS.hudson.panel.config.view,
	    	associationId: AJS.hudson.panel.config.associationId,
	    	module: AJS.hudson.panel.config.module,
	    	objectId: AJS.hudson.panel.config.objectId
	    }),
	    url : AJS.hudson.panel.config.contextPath + "/secure/ViewHudsonPanelContent.jspa",
	    success : function(html) {
	        AJS.$('#hudsonPanelContentHtml').html(html);
	    }
	});
}

/**
 * Method to register a collapsable list
 */
AJS.hudson.panel.registerList = function(listName, listType) {
	AJS.$("." + listName + " .label").click(function() {
		AJS.hudson.panel.toggleList(listName, listType);
	});
}

/**
 * Method to toggle the visibility of a list
 */
AJS.hudson.panel.toggleList = function(listName, listType) {
	AJS.$("." + listName + " li").each(function(index, item) {
		var liItem = AJS.$(item);
		if (AJS.$(this).attr("class") != "label") {
			if (liItem.css("display") == "none") {
				liItem.css({display: listType});
			} else {
				liItem.css({display: "none"});
			}
		}
	});
}
