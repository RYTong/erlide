package com.rytong.ui.editor.conf;


public interface IModelListener {
	String ADDED="__added"; //$NON-NLS-1$
	String REMOVED="__removed"; //$NON-NLS-1$
	String CHANGED = "__changed"; //$NON-NLS-1$
	void modelChanged(Object[] objects, String type, String property);
}