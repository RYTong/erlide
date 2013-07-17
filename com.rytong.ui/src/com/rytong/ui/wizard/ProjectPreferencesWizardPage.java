package com.rytong.ui.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ProjectPreferencesWizardPage extends WizardPage {

	public ProjectPreferencesWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	public ProjectPreferencesWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		composite.setLayout(gl);
        
		Label outLabel = new Label(composite, SWT.NONE);
        outLabel.setText("Now Generating App/EWP standard dir...");
        GridData gd_Label = new GridData();
        gd_Label.minimumWidth = 50;
        outLabel.setLayoutData(gd_Label);
        setControl(composite);
	}

}
