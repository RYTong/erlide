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

package com.rytong.template.editor.lua;

import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;

import com.rytong.template.editor.Activator;

public class LuaContentAssistPreference extends ContentAssistPreference {
    private static LuaContentAssistPreference sDefault;

    public static ContentAssistPreference getDefault() {
        if (sDefault == null) {
            sDefault = new LuaContentAssistPreference();
        }
        return sDefault;
    }
    @Override
    protected ScriptTextTools getTextTools() {
        return Activator.getDefault().getLuaTextTools();
    }

}
