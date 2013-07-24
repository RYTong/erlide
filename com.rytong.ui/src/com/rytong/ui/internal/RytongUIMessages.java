package com.rytong.ui.internal;

import org.eclipse.osgi.util.NLS;

public class RytongUIMessages extends NLS {
    
	private static final String BUNDLE_NAME = "com.rytong.ui.internal.rytresources";//$NON-NLS-1$
    
    
	public static String NewAppProjectWizard_title;
    
	public static String NewAppProjectWizard_desc;
    
	
	public static String VersionSelectionPage_title;
    
	public static String VersionSelectionPage_desc;
    
	public static String VersionSelectionPage_label;
    
	public static String VersionSelectionPage_versions;

    
	public static String TemplateListSelectionPage_title;
    
	public static String TemplateListSelectionPage_desc;
    
	public static String TemplateListSelectionPage_label;
    
	public static String TemplateListSelectionPage_templates;
    
   public static String TemplateListSelectionPage_noDes;
    
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, RytongUIMessages.class);
	}
}
