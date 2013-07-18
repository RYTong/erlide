/*******************************************************************************
 * Copyright (c) 2009, 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/

package com.rytong.template.editor.template;

import org.eclipse.jface.text.IDocument;

public interface ITemplatePartitions {
    public static final String LUA_PARTITIONING =  TemplateConstants.LUA_PARTITIONING;
    public static final String TEMPLATE_PARTITIONING = "__ewp_template_partitioning";

    public static final String XML_TAG = "__xml_tag";

    public static final String LUA = "__lua";
    public static final String CS = "__cs";
    public static final String TEMPLATE_STRING = "__tepmlate_string";
    public static final String LUA_COMMENT = "__lua_comment"; //$NON-NLS-1$
    public static final String LUA_MULTI_LINE_COMMENT = "__lua_multi_line_comment"; //$NON-NLS-1$
    public static final String LUA_NUMBER = "__lua_number"; //$NON-NLS-1$
    public static final String LUA_STRING = "__lua_string"; //$NON-NLS-1$
    public static final String LUA_SINGLE_QUOTE_STRING = "__lua_single_quote_string"; //$NON-NLS-1$

    public static final String[] TEMPLATE_PARTITION_TYPES = new String[] {
    	IDocument.DEFAULT_CONTENT_TYPE,
    	ITemplatePartitions.LUA,
    	ITemplatePartitions.CS,
    	ITemplatePartitions.TEMPLATE_STRING,
    	ITemplatePartitions.XML_TAG,
    	ITemplatePartitions.LUA_COMMENT,
    	ITemplatePartitions.LUA_STRING,
    	ITemplatePartitions.LUA_SINGLE_QUOTE_STRING,
    	ITemplatePartitions.LUA_MULTI_LINE_COMMENT,
    	ITemplatePartitions.LUA_NUMBER };


}
