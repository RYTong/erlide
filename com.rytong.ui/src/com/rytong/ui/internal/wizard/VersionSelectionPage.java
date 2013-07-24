package com.rytong.ui.internal.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.rytong.ui.internal.RytongUIMessages;

public class VersionSelectionPage extends WizardPage {
   private AppFieldData fData;

	protected VersionSelectionPage(String pageName, AppFieldData fData) {
		super(pageName);
      this.fData = fData;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
      layout.marginTop=60;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
       
		Label label = new Label(container, SWT.NONE);
		label.setText(RytongUIMessages.VersionSelectionPage_label);
		GridData gd_lbl = new GridData();
		label.setLayoutData(gd_lbl);
        
		final Combo versCtrl = new Combo(container, SWT.READ_ONLY);
      final String[] versions = RytongUIMessages.VersionSelectionPage_versions.split("\\s+");
      fData.ewpVersion = versions[0];
		versCtrl.setItems(versions);
      versCtrl.select(0);
      versCtrl.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent e) {
    			super.widgetSelected(e);
            update_data(versions[versCtrl.getSelectionIndex()]);
    		}
        });
      GridData gd_vers = new GridData();
		versCtrl.setLayoutData(gd_vers);

      setControl(container);
      setPageComplete(validatePage());

	}
    
   public void update_data(String ver) {
		fData.ewpVersion = ver;
      setPageComplete(validatePage());
      
      IWizardPage page = this.getNextPage();
      if (page.getControl() != null)
      	page.dispose();
    }
    
	private boolean validatePage() {
    	return true;
    	//return fData.ewpVersion != null;
	}
}
