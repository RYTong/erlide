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

import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;

public class LuaLanguageToolkit extends AbstractLanguageToolkit {

    private static LuaLanguageToolkit toolkit;

    public static IDLTKLanguageToolkit getDefault() {
        if (toolkit == null) {
            toolkit = new LuaLanguageToolkit();
        }
        return toolkit;
    }
    @Override
    public String getLanguageContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLanguageName() {
        // TODO Auto-generated method stub
        return "Lua";
    }

    @Override
    public String getNatureId() {
        // TODO Auto-generated method stub
        return null;
    }

}
