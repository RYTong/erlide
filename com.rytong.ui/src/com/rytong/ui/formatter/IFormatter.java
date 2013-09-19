package com.rytong.ui.formatter;

import org.eclipse.jface.text.IDocument;


/**
 * @author lu.jingbo@rytong.com
 *
 */
public interface IFormatter {

	public void format(String source, int offset, int length);
    
    public IDocument getDocument();
    
}
