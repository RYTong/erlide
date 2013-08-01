package com.rytong.ui.editor;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detector for Number.
 * @author lu.jingbo@rytong.com
 *
 */
public class NumberDetector implements IWordDetector {

	/* (non-Javadoc)
	* Method declared on IWordDetector
 	*/
	public boolean isWordStart(char c) {
		return (c >= '0' && c <= '9');
	}

	/* (non-Javadoc)
	* Method declared on IWordDetector
 	*/
	public boolean isWordPart(char c) {
		return ((c >= '0' && c <= '9') || c == '.');
	}

}
