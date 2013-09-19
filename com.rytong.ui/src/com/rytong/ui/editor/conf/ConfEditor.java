package com.rytong.ui.editor.conf;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.rytong.ui.internal.RytongUIPlugin;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class ConfEditor extends FormEditor {

	/**
	 * The text editor used in source page.
	 */
   private TextEditor editor;
    
    /**
     * The index of the page containing the source editor
     */
   private int editorIndex = 0;
   
   private boolean isDirty = false;


	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
	}

	@Override
	protected FormToolkit createToolkit(Display display) {
		// Create a toolkit that shares colors between editors.
		return new FormToolkit(RytongUIPlugin.getDefault().getFormColors(
				display));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		createSourcePage();
      try {
		addPage((new DesignPage(this)));
	} catch (PartInitException e) {
		e.printStackTrace();
	}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
    	editor.doSave(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
    
    
    /* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#isDirty()
	 */
	@Override
	public boolean isDirty() {
    	return isDirty;
	}
    
    public void markDirty() {
    	if (!isDirty) {
        	isDirty = true;
        	firePropertyChange(PROP_DIRTY);
    	}
    }

	/**
     * get source text editor
     */
    public TextEditor getSourcePage() {
    	return this.editor;
    }

	private void createSourcePage() {
        try {
            editor = new SourceEditor();
            editorIndex = addPage(editor, getEditorInput());
            setPageText(editorIndex, "Source");
        } catch (PartInitException e) {
            ErrorDialog
                    .openError(
                            getSite().getShell(),
                            "ErrorCreatingNestedEditor", null, e.getStatus()); //$NON-NLS-1$
	    }		
	}

}
