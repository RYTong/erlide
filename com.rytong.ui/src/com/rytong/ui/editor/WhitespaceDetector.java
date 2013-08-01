package com.rytong.ui.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class WhitespaceDetector implements IWhitespaceDetector {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
	 */
	@Override
	public boolean isWhitespace(char c) {
		return Character.isWhitespace(c);
	}

}
