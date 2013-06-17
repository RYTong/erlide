package com.rytong.template.editor.template;

import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class TemplateEditorMessages extends NLS {
	 private static final String BUNDLE_FOR_CONSTRUCTED_KEYS = "com.rytong.template.editor.template.EditorMessages";//$NON-NLS-1$

	    private static final ResourceBundle fgBundleForConstructedKeys = ResourceBundle
	            .getBundle(BUNDLE_FOR_CONSTRUCTED_KEYS);

	    public static final String IndentAction_error_message = "indent action";
	    
	    /**
	     * Returns the message bundle which contains constructed keys.
	     * 
	     * @since 3.1
	     * @return the message bundle
	     */
	    public static ResourceBundle getBundleForConstructedKeys() {
	        return fgBundleForConstructedKeys;
	    }

	    private static final String BUNDLE_NAME = TemplateEditorMessages.class.getName();

	    private TemplateEditorMessages() {
	        // Do not instantiate
	    }

	    static {
	        NLS.initializeMessages(BUNDLE_NAME, TemplateEditorMessages.class);
	    }

}
