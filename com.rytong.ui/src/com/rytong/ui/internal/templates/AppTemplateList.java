package com.rytong.ui.internal.templates;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;

import com.rytong.ui.RytongUIConstants;
import com.rytong.ui.internal.RytongUIPlugin;

public class AppTemplateList {
	private List<AppTemplate> templates = new ArrayList<AppTemplate>();

	public AppTemplateList(String ewpVersion) {
		initializeTemplateList(ewpVersion);
	}

	private void initializeTemplateList(String ewpVersion) {
		// System.out.println("the ewpVersion is: " + ewpVersion);
		File tempDir = null;
		try {
			URL url = RytongUIPlugin.getDefault().getBundle()
					.getEntry("templates/" + ewpVersion);
			tempDir = new File(FileLocator.toFileURL(url).getFile());
		} catch (Exception e1) {
			//e1.printStackTrace();
         System.out.println("Cannot find template dir for version: "+ewpVersion);
			return;
		}
		for (File f : tempDir.listFiles()) {
			if (f.isDirectory()) {
				String abname = f.getAbsolutePath();
				Properties prop = new Properties();
				try {
					prop.load(new FileInputStream(abname + "/"
							+ RytongUIConstants.TEMPLATE_PROPFILE));
					this.templates
							.add(new AppTemplate(prop.getProperty("name"),
														prop.getProperty("label"),
														prop.getProperty("desc"),
														abname));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Object[] getAppTemplateList() {
		return templates.toArray();
	}

}
