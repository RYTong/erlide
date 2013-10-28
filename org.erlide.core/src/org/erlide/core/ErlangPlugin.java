/*******************************************************************************
 * Copyright (c) 2004 Eric Merritt and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eric Merritt
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.erlide.launch.debug.ErlangDebugOptionsManager;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 *
 * @author Eric Merritt [cyberlync at gmail dot com]
 * @author Vlad Dumitrescu [vladdu55 at gmail dot com]
 * @author jakob
 */

public class ErlangPlugin extends Plugin {
    private static ErlangPlugin plugin;
    private ErlangCore core;

    public static String yawsPath;
    public static String ewpPath;

    public static String yawsVer="unknown";
    public static String ewpVer="unknown";

    public ErlangPlugin() {
        super();
        plugin = this;
    }

    public static ErlangPlugin getDefault() {
        if (plugin == null) {
            plugin = new ErlangPlugin();
        }
        return plugin;
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        try {
            ResourcesPlugin.getWorkspace().removeSaveParticipant(
                    getBundle().getSymbolicName());
            if (core != null) {
                core.stop();
            }
        } finally {
            core = null;
            plugin = null;
            // ensure we call super.stop as the last thing
            super.stop(context);
        }
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        // final Bundle b = Platform.getBundle("org.eclipse.equinox.event");
        // if (b != null && b.getState() == Bundle.RESOLVED) {
        // try {
        // b.start();
        // } catch (final BundleException e) {
        // }
        // }

        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IExtensionRegistry extensionRegistry = Platform
                .getExtensionRegistry();
        final String portableString = workspace.getRoot().getLocation()
                .toPortableString();
        final ErlangDebugOptionsManager erlangDebugOptionsManager = ErlangDebugOptionsManager
                .getDefault();

        yawsPath = guess_path("yaws");
        if (yawsPath == null)
            yawsPath = "/usr/local/lib/yaws";
        ewpPath = guess_path("ewp");

        yawsVer = get_yaws_version();
        ewpVer = get_ewp_version();

        core = new ErlangCore(this, workspace, extensionRegistry,
                portableString, erlangDebugOptionsManager);
        core.start();
    }

    public ErlangCore getCore() {
        return core;
    }

    private String guess_path(String Pkg) {
        try {
            String cmd = "whereis "+Pkg;
            String lineStr = exec_cmd(cmd);
            if (lineStr != null){
                for (String path : lineStr.split(" ")) {
                    if (path.endsWith("/lib/"+Pkg))
                        return path;
                }}
            return null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String get_yaws_version() {
        try {
            String lineStr = exec_cmd("yaws --version");
            if (lineStr != null) {
                String v[] = lineStr.split(" ");
                return v[1];
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return "unknown";
    }

    public static String get_ewp_version() {
        if (ewpPath != null) {
            String ver = read_file(new File(ewpPath+File.separator+"RPM"+File.separator+"VERSION"));
            if (ver != null) {
                String pver = read_file(new File(ewpPath+File.separator+"RPM"+File.separator+"PATCHLEVEL"));
                if (pver !=null)
                    return ver+"."+pver;
                return ver;
            }
        }
        return "unknown";
    }

    public static String read_file(File file) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        String temp=null;
        StringBuffer sb=new StringBuffer();
        temp=br.readLine();
        while(temp!=null) {
            sb.append(temp+" ");
            temp=br.readLine();
        }
        br.close();
        return sb.toString().trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @since 1.1
     */
    public static String exec_cmd(String cmd) {
        Runtime run = Runtime.getRuntime();
        String lineStr = null;
        try {
            Process p = run.exec(cmd);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            lineStr = inBr.readLine();
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)
                    return null;
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineStr;
    }

}
