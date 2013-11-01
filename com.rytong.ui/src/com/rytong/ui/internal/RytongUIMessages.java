package com.rytong.ui.internal;

import org.eclipse.osgi.util.NLS;

public class RytongUIMessages extends NLS {
    
	private static final String BUNDLE_NAME = "com.rytong.ui.internal.rytresources";//$NON-NLS-1$
    	
	public static String NewAppProjectWizard_title;

	public static String NewAppProjectWizard_desc;

	public static String VersionSelectionPage_title;

	public static String VersionSelectionPage_desc;

	public static String VersionSelectionPage_label;

	public static String VersionSelectionPage_plabel;

	public static String VersionSelectionPage_versions;

	public static String TemplateListSelectionPage_title;

	public static String TemplateListSelectionPage_desc;

	public static String TemplateListSelectionPage_label;

	public static String TemplateListSelectionPage_templates;

	public static String TemplateListSelectionPage_noDes;

	public static String NewAdapterConfWizard_title;

	public static String CreateAdapterConfPage_title;

	public static String CreateAdapterConfPage_desc;

	public static String CreateAdapterConfPage_name_label;

	public static String CreateAdapterConfPage_protocol_label;

	public static String CreateAdapterConfPage_protocol_names;

	public static String PreferencePage_Name;

	public static String PreferencePage_Change;


	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, RytongUIMessages.class);
	}
}
