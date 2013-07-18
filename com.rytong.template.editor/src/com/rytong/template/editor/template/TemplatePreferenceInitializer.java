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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.editor.highlighting.SemanticHighlightingUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.EditorsUI;

import com.rytong.template.editor.lua.LuaColorConstants;
import com.rytong.template.editor.Activator;


public class TemplatePreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        DLTKUIPlugin.getDefault().getPreferenceStore().setDefault(PreferenceConstants.EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE, true);

        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

        EditorsUI.useAnnotationsPreferencePage(store);
        EditorsUI.useQuickDiffPreferencePage(store);

        // Initialize DLTK default values
        PreferenceConstants.initializeDefaultValues(store);

        // Initialize Lua code color and style constants
        PreferenceConverter.setDefault(store, ITemplateColorConstants.LUA_SINGLE_LINE_COMMENT, new RGB(63, 127, 95));
        PreferenceConverter.setDefault(store, ITemplateColorConstants.LUA_MULTI_LINE_COMMENT, new RGB(63, 95, 191));
        PreferenceConverter.setDefault(store, ITemplateColorConstants.LUA_KEYWORD, new RGB(195, 143, 71));
        PreferenceConverter.setDefault(store, ITemplateColorConstants.LUA_NUMBER, new RGB(185, 20, 20));
        PreferenceConverter.setDefault(store, ITemplateColorConstants.LUA_STRING, new RGB(197,83,123));//197,83,123  42, 0, 255
		PreferenceConverter.setDefault(store, LuaColorConstants.COMMENT_TASK_TAGS, new RGB(127, 159, 191));

		// Semantic highlighting preferences initialization
		SemanticHighlightingUtils.initializeDefaultValues(store, Activator.getDefault().getTextTools().getSemanticHighlightings());

		PreferenceConverter.setDefault(store, LuaColorConstants.LUA_LOCAL_VARIABLE, new RGB(103, 103, 103));
		PreferenceConverter.setDefault(store, LuaColorConstants.LUA_GLOBAL_VARIABLE, new RGB(0, 0, 0));
		store.setDefault(LuaColorConstants.LUA_GLOBAL_VARIABLE + PreferenceConstants.EDITOR_BOLD_SUFFIX, true);

		PreferenceConverter.setDefault(store, LuaColorConstants.LUA_DOC, new RGB(63, 95, 191));
		PreferenceConverter.setDefault(store, LuaColorConstants.LUA_DOC_TAGS, new RGB(127, 159, 191));
		store.setDefault(LuaColorConstants.LUA_DOC_TAGS + PreferenceConstants.EDITOR_BOLD_SUFFIX, true);

        // CS color
        PreferenceConverter.setDefault(store, ITemplateColorConstants.CS_KEYWORD, new RGB(127, 0, 85));
        PreferenceConverter.setDefault(store, ITemplateColorConstants.CS_TAG, new RGB(0, 128, 0));
        PreferenceConverter.setDefault(store, ITemplateColorConstants.CS_STRING, new RGB(42, 0, 255));

        // XML color
        PreferenceConverter.setDefault(store, ITemplateColorConstants.XML_TAG, new RGB(0, 0, 128));
        PreferenceConverter.setDefault(store, ITemplateColorConstants.XML_STRING, new RGB(42, 0, 255));

        store.setDefault(ITemplateColorConstants.LUA_SINGLE_LINE_COMMENT + PreferenceConstants.EDITOR_BOLD_SUFFIX, false);
        store.setDefault(ITemplateColorConstants.LUA_SINGLE_LINE_COMMENT + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);

        store.setDefault(ITemplateColorConstants.LUA_MULTI_LINE_COMMENT + PreferenceConstants.EDITOR_BOLD_SUFFIX, false);
        store.setDefault(ITemplateColorConstants.LUA_MULTI_LINE_COMMENT + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);

        store.setDefault(ITemplateColorConstants.LUA_KEYWORD + PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
        store.setDefault(ITemplateColorConstants.LUA_KEYWORD + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);
        store.setDefault(PreferenceConstants.EDITOR_FOLDING_ENABLED, true);


        // Enable code folding
        store.setDefault(PreferenceConstants.EDITOR_COMMENTS_FOLDING_ENABLED, true);
        store.setDefault(PreferenceConstants.EDITOR_DOCS_FOLDING_ENABLED, true);

        // Enable auto close
        store.setDefault(PreferenceConstants.EDITOR_CLOSE_BRACES, true);
        store.setDefault(PreferenceConstants.EDITOR_CLOSE_BRACKETS, true);
        store.setDefault(PreferenceConstants.EDITOR_CLOSE_STRINGS, true);

        // Set Tab
        store.setDefault(CodeFormatterConstants.FORMATTER_TAB_CHAR,
                CodeFormatterConstants.TAB);
        store.setDefault(CodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
        store.setDefault(CodeFormatterConstants.FORMATTER_INDENTATION_SIZE, "4");

    }


}
