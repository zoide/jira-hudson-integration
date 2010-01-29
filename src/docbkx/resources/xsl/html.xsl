<?xml version="1.0" encoding="utf-8"?>
<!--
 ~ Licensed to Marvelution under one or more contributor license 
 ~ agreements.	See the NOTICE file distributed with this work 
 ~ for additional information regarding copyright ownership.
 ~ Marvelution licenses this file to you under the Apache License,
 ~ Version 2.0 (the "License"); you may not use this file except
 ~ in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~	http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing,
 ~ software distributed under the License is distributed on an
 ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ~ KIND, either express or implied. See the License for the
 ~ specific language governing permissions and limitations
 ~ under the License.
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:fo="http://www.w3.org/1999/XSL/Format"
				version="1.0">

	<xsl:import href="urn:docbkx:stylesheet"/>
	<xsl:param name="html.stylesheet">html.css</xsl:param>
	<xsl:param name="use.extensions">1</xsl:param>
	<xsl:param name="tablecolumns.extension">0</xsl:param>
	<xsl:param name="callout.extensions">1</xsl:param>
	<xsl:param name="graphicsize.extension">0</xsl:param>
	<xsl:param name="generate.toc">
		book toc,figure,table,example
	</xsl:param>
	<xsl:param name="toc.section.depth">3</xsl:param>
	<xsl:param name="chapter.autolabel">1</xsl:param>
	<xsl:param name="section.autolabel" select="1"/>
	<xsl:param name="section.label.includes.component.label" select="1"/>
	<xsl:param name="callout.graphics">0</xsl:param>
	<xsl:param name="callout.defaultcolumn">90</xsl:param>
	<xsl:param name="admon.graphics">0</xsl:param>
	<xsl:param name="formal.title.placement">
		figure after
		example before
		equation before
		table before
		procedure before
	</xsl:param>
	<xsl:template match="author" mode="titlepage.mode">
		<xsl:if test="name(preceding-sibling::*[1]) = 'author'">
			<xsl:text>, </xsl:text>
		</xsl:if>
		<span class="{name(.)}">
			<xsl:call-template name="person.name"/>
			<xsl:apply-templates mode="titlepage.mode" select="./contrib"/>
			<xsl:apply-templates mode="titlepage.mode" select="./affiliation"/>
		</span>
	</xsl:template>
	<xsl:template match="authorgroup" mode="titlepage.mode">
		<div class="{name(.)}">
			<h2>Authors</h2>
			<p/>
			<xsl:apply-templates mode="titlepage.mode"/>
		</div>
	</xsl:template>

</xsl:stylesheet>
