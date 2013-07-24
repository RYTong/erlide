package com.rytong.ui.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.rytong.ui.RytongUIConstants;


/**
 * The activator class controls the plug-in life cycle
 */
public class RytongUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.rytong.ui"; //$NON-NLS-1$

    /**
     * The shared instance.
     */
   private static volatile RytongUIPlugin plugin;
   
   /**
    * Resource bundle.
    */
   private ResourceBundle resourceBundle;
	
	/**
	 * The constructor
	 */
	public RytongUIPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle
                    .getBundle("com.rytong.ui.RytongUIResources");
        } catch (final MissingResourceException x) {
            x.printStackTrace();
            resourceBundle = null;
        }
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

    @Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		 final URL baseURL = getBundle().getEntry("/");
        createImageDescriptor(RytongUIConstants.IMG_NEW_PROJECT_WIZARD, baseURL);
	}

	/**
     * Returns the shared instance.
     * 
     * @return The plugin
     */
    public static RytongUIPlugin getDefault() {
        if (plugin == null) {
            plugin = new RytongUIPlugin();
        }
        return plugin;
    }
	

    /**
     * Creates an image and places it in the image registry.
     * 
     * @param id
     *            The image id
     * @param baseURL
     *            The descriptor url
     */
    protected void createImageDescriptor(final String id, final URL baseURL) {
        URL url = null;
        try {
            url = new URL(baseURL, RytongUIConstants.ICON_PATH + id);
        } catch (final MalformedURLException e) {
            // ignore exception
        }

        getImageRegistry().put(id, ImageDescriptor.createFromURL(url));
    }
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public ImageDescriptor getImageDescriptor(String id) {
		//return imageDescriptorFromPlugin(PLUGIN_ID, path);
		final ImageDescriptor returnImageDescriptor = getImageRegistry()
		        .getDescriptor(id);
		return returnImageDescriptor;
	}
	
    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not
     * found.
     * 
     * @param key
     *            The resource
     * @return The identified string
     */
    public static String getResourceString(final String key) {
        final ResourceBundle bundle = RytongUIPlugin.getDefault()
                .getResourceBundle();
        try {

            final String returnString = bundle != null ? bundle.getString(key)
                    : key;
            return returnString;
        } catch (final MissingResourceException e) {
            return key;
        }
    }
    
    /**
     * Returns the plugin's resource bundle,
     * 
     * @return The requested bundle
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
    
    public static String[] getAppDirs() {
    	return new String[]{
    			RytongUIConstants.APP_BIN_DIR,
            RytongUIConstants.APP_CONFIG_DIR,
            RytongUIConstants.APP_DRIVERS_DIR,
            RytongUIConstants.APP_EBIN_DIR,
    			RytongUIConstants.APP_CERT_DIR, 
    			RytongUIConstants.APP_CS_DIR, 
    			RytongUIConstants.APP_INCLUDE_DIR,
    			RytongUIConstants.APP_LOG_DIR,
    			RytongUIConstants.APP_PUSHKEY_DIR,
    			RytongUIConstants.APP_SRC_DIR,
    			RytongUIConstants.APP_WWW_DIR};
    }
}
