package com.rytong.ui.internal.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.rytong.ui.internal.RytongUIMessages;

public class CreateAdapterConfPage extends WizardPage {

	private Text name;
   private Combo protocol;
   private String[] protocols;

	public CreateAdapterConfPage(String pageName) {
		super(pageName);
      do_init();
	}

	protected CreateAdapterConfPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
      do_init();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText(RytongUIMessages.CreateAdapterConfPage_name_label);
		
		name = new Text(container, SWT.SINGLE | SWT.BORDER);
		GridData gd_name = new GridData(GridData.FILL, GridData.CENTER, true, false);
		name.setLayoutData(gd_name);
      name.addModifyListener(new ModifyListener() {
    		public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());    			
    		}
    	});
        
		new Label(container, SWT.NONE).setText(RytongUIMessages.CreateAdapterConfPage_protocol_label);
		
      protocol = new Combo(container, SWT.READ_ONLY);
		GridData gd_protocol = new GridData(GridData.FILL, GridData.CENTER, true, false);
		protocol.setLayoutData(gd_protocol);
      protocol.setItems(protocols);
      protocol.select(0);
        
      setControl(container);
      setPageComplete(validatePage());
	}
    
	private boolean validatePage() {
      return name.getText().matches("^\\p{Alpha}+\\w*$");
	}
    
   public String getName() {
      return name.getText();
    }
    
   public String getProtocol() {
   	return protocols[protocol.getSelectionIndex()]; 
    }
    
   private void do_init() {
      protocols = RytongUIMessages.CreateAdapterConfPage_protocol_names.split("\\s+");
      setPageComplete(false);
    }

}
