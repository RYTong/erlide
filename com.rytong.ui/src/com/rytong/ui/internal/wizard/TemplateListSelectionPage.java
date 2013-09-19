package com.rytong.ui.internal.wizard;

import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.rytong.ui.internal.RytongUIMessages;
import com.rytong.ui.internal.parts.FormBrowser;
import com.rytong.ui.internal.templates.AppTemplate;
import com.rytong.ui.internal.templates.AppTemplateList;
import com.rytong.ui.internal.templates.TemplateListContentProvider;
import com.rytong.ui.internal.templates.TemplateListLabelProvider;

public class TemplateListSelectionPage extends WizardPage implements ISelectionChangedListener {
	private Button fUseTemplate;
   private TableViewer wizardSelectionViewer;
   private FormBrowser descriptionBrowser;
   private AppFieldData fData;

	public AppFieldData getData() {
	return fData;
}

	public TemplateListSelectionPage(String pageName, AppFieldData fData) {
		super(pageName);
      this.fData = fData;
      setPageComplete(false);
	}

	public TemplateListSelectionPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
      setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 10;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		fUseTemplate = new Button(container, SWT.CHECK);
		fUseTemplate.setText(RytongUIMessages.TemplateListSelectionPage_label);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		fUseTemplate.setLayoutData(gd);
       
		Label label = new Label(container, SWT.NONE);
		label.setText(RytongUIMessages.TemplateListSelectionPage_templates);
		GridData gd2 = new GridData();
		label.setLayoutData(gd2);

		SashForm sashForm = new SashForm(container, SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		sashForm.setLayoutData(gd);
        
		wizardSelectionViewer = new TableViewer(sashForm, SWT.BORDER);
		wizardSelectionViewer.setContentProvider(new TemplateListContentProvider());
		wizardSelectionViewer.setLabelProvider(new TemplateListLabelProvider());
//		wizardSelectionViewer.setComparator(ListUtil.NAME_COMPARATOR);
      wizardSelectionViewer.setInput(new AppTemplateList(fData.ewpVersion));
      wizardSelectionViewer.addSelectionChangedListener(this);
        
		//create descriptionIn
		descriptionBrowser = new FormBrowser(SWT.BORDER | SWT.V_SCROLL);
		descriptionBrowser.setText("");
		descriptionBrowser.createControl(sashForm);
		Control c = descriptionBrowser.getControl();
		GridData gd3 = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 200;
		c.setLayoutData(gd3);
        
		fUseTemplate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//wizardSelectionViewer.getControl().setEnabled(fUseTemplate.getSelection());
				if (!fUseTemplate.getSelection()) {
            	fUseTemplate.setSelection(true);
					setDescription("");
               String Body = "\"Knock, knock.\"\n\n\"Who's there?\"\n\nvery long pause….\n\n\"Java.\"\n\n:-o";
    				new MessageDialog(getShell(), "Work in progress", null,
    					    Body, MessageDialog.INFORMATION, new String[] {"Later"}, 0).open();
				}
				setDescriptionEnabled(fUseTemplate.getSelection());
				getContainer().updateButtons();
			}
		});
		fUseTemplate.setSelection(true);
      setControl(container);
      setPageComplete(validatePage());
	}
    
	private void setDescriptionEnabled(boolean enabled) {
		Control dcontrol = descriptionBrowser.getControl();
		if (dcontrol != null)
			dcontrol.setEnabled(enabled);
	}

	@Override
	public void dispose() {
		super.dispose();
      setControl(null);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		setErrorMessage(null);
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		AppTemplate currentWizardSelection = null;
		Iterator<?> iter = selection.iterator();
		if (iter.hasNext())
			currentWizardSelection = (AppTemplate) iter.next();
		if (currentWizardSelection == null) {
			setDescriptionText(""); //$NON-NLS-1$
//			setSelectedNode(null);
			return;
		}
		final AppTemplate finalSelection = currentWizardSelection;
//		setSelectedNode(createWizardNode(finalSelection));
		setDescriptionText(finalSelection.getDescription());
      fData.template = finalSelection;
		getContainer().updateButtons();		
     	setPageComplete(validatePage());
	}
    
	public void setDescriptionText(String text) {
		if (text == null)
			text = RytongUIMessages.TemplateListSelectionPage_noDes;
		descriptionBrowser.setText(text);
	}
    
	private boolean validatePage() {
    	return fUseTemplate.getSelection() &&
    				!wizardSelectionViewer.getSelection().isEmpty();
	}

//   private void setSelectedNode(IWizardNode node) {
//      addSelectedNode(node);
//      selectedNode = node;
//      if (isCurrentPage()) {
//			getContainer().updateButtons();
//		}
//    }
    
}
