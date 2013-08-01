package com.rytong.ui.internal.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import com.rytong.ui.editor.conf.ConfColorManager;
import com.rytong.ui.editor.lua.LuaColorManager;
import com.rytong.ui.internal.RytongUIPlugin;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
    
		IPreferenceStore store = RytongUIPlugin.getRytongPreferenceStore();
        
		PreferenceConverter.setDefault(store, ConfColorManager.STRING, new RGB(0, 128, 0));
		PreferenceConverter.setDefault(store, ConfColorManager.BOOLEAN, new RGB(0, 0, 128));
		PreferenceConverter.setDefault(store, ConfColorManager.NUMBER, new RGB(128, 0, 128));
		PreferenceConverter.setDefault(store, ConfColorManager.UNDEFINED, new RGB(128, 0, 0));
		PreferenceConverter.setDefault(store, ConfColorManager.COMMENT, new RGB(128, 128, 128));
		PreferenceConverter.setDefault(store, ConfColorManager.DEFAULT, new RGB(64, 64, 64));
        
		PreferenceConverter.setDefault(store, LuaColorManager.KEYWORD, new RGB(128, 128, 0));
		PreferenceConverter.setDefault(store, LuaColorManager.STRING, new RGB(0, 128, 0));
		PreferenceConverter.setDefault(store, LuaColorManager.NUMBER, new RGB(128, 0, 128));
		PreferenceConverter.setDefault(store, LuaColorManager.COMMENT, new RGB(128, 128, 128));
		PreferenceConverter.setDefault(store, LuaColorManager.DEFAULT, new RGB(64, 64, 64));
        
      store.setDefault(PreferenceConstants.VALIDATION, PreferenceConstants.SYNTAX_VALIDATION_ERROR);
	}

}
