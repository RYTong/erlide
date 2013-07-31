package com.rytong.ui.internal.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import com.rytong.ui.econfeditor.ColorManager;
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
		PreferenceConverter.setDefault(store, ColorManager.STRING, new RGB(0, 128, 0));
		PreferenceConverter.setDefault(store, ColorManager.BOOLEAN, new RGB(0, 0, 128));
		PreferenceConverter.setDefault(store, ColorManager.NUMBER, new RGB(128, 0, 128));
		PreferenceConverter.setDefault(store, ColorManager.UNDEFINED, new RGB(128, 0, 0));
		PreferenceConverter.setDefault(store, ColorManager.COMMENT, new RGB(128, 128, 128));
		PreferenceConverter.setDefault(store, ColorManager.DEFAULT, new RGB(64, 64, 64));
	}

}
