package com.rytong.ui.econfeditor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.rytong.ui.internal.RytongUIPlugin;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class ColorManager {

	public static final String STRING = "colorString";
   public static final String BOOLEAN = "colorBoolean";
   public static final String NUMBER = "colorNumber";
   public static final String UNDEFINED = "colorUndefined";
   public static final String COMMENT = "colorComment";
	public static final String DEFAULT = "colorDefault";

	protected Map<String, Color> fColorTable = new HashMap<String, Color>(10);

	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
	public Color getColor(String rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), PreferenceConverter.getColor(RytongUIPlugin.getRytongPreferenceStore(), rgb));
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
