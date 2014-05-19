package com.rytong.template.debugtool;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.rytong.template.debugtool.actions.SyncPageAction;
import com.rytong.template.debugtool.actions.SyncServerAction;
import com.rytong.template.debugtool.util.SyncSocket;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "com.rytong.template.debugtool"; //$NON-NLS-1$

    protected SyncSocket server ;

    // The shared instance
    private static Activator plugin;

    private SyncPageAction tmp_page_act = null;
    private SyncServerAction tmp_server_act = null;

    /**
     * The constructor
     */
    public Activator() {
        server = new SyncSocket().getSyncSocket();
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

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }


    public SyncSocket getSyncServer(){
        return server;
    }

    public void setSyncServerAct(SyncServerAction tmp_act){
        tmp_server_act = tmp_act;

    }

    public void setSyncPageAct(SyncPageAction tmp_act){
        tmp_page_act = tmp_act;
    }

    public SyncServerAction getSyncServerAct(){
        return tmp_server_act;
    }

    public SyncPageAction getSyncPageAct(){
        return tmp_page_act;
    }
}
