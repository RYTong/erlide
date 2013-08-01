package com.rytong.ui.editor;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class AtomDetector implements IWordDetector {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

}
