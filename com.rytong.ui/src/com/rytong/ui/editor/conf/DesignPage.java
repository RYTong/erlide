package com.rytong.ui.editor.conf;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.forms.*;
import org.eclipse.ui.forms.editor.*;
import org.eclipse.ui.forms.widgets.*;


/**
 * @author lu.jingbo@rytong.com
 *
 */
public class DesignPage extends FormPage {
	private ScrolledPropertiesBlock block;
    
	public DesignPage(FormEditor editor) {
		super(editor, "fourth", Messages.getString("MasterDetailsPage.label")); //$NON-NLS-1$ //$NON-NLS-2$
		block = new ScrolledPropertiesBlock(this);
	}
    
	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		//FormToolkit toolkit = managedForm.getToolkit();
		form.setText(Messages.getString("MasterDetailsPage.title")); //$NON-NLS-1$
		//form.setBackgroundImage(FormArticlePlugin.getDefault().getImage( FormArticlePlugin.IMG_FORM_BG));
		block.createContent(managedForm);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#isActive()
	 */
	@Override
	public boolean isActive() {
		return super.isActive();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		super.doSave(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		super.doSaveAs();
	}

}
