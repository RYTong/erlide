package com.rytong.conf.adapter.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.erlide.jinterface.ErlLogger;

import com.rytong.ui.editor.conf.SourceEditor;

public class AdapterEditor extends MultiPageEditorPart implements IResourceChangeListener {

	private SourceEditor editor = new SourceEditor();

	private int pageindex;
	private AdapterEditorPage adapterPage;

	public AdapterEditor(){
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}


	@Override
	protected void createPages() {
		// TODO Auto-generated method stub
		createPage0();
		createPage1();
	}


	void createPage0(){
		//AdapterEditorPage
		Composite composite = new Composite(getContainer(), SWT.FULL_SELECTION);
		adapterPage = new AdapterEditorPage(this, composite);
		adapterPage.initialPage();


		int index = addPage(composite);
		ErlLogger.debug("index:"+index);
		setPageText(index, "OverView");
	}
	/**
	 * Creates page 1 of the multi-page editor,
	 * which show the resource text
	 */
	void createPage1() {
		try {

			int index = addPage(editor, getEditorInput());
			ErlLogger.debug("index:"+index);
			//ErlLogger.debug("getEditorInput:"+getEditorInput());

			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
					getSite().getShell(),
					"Error creating nested text editor",
					null,
					e.getStatus());
		}
	}

	/**
	 * Calculates the contents of page 2 when the M is activated.
	 */
	public void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		ErlLogger.debug("page change index:"+newPageIndex);
		if (newPageIndex == 0) {
			pageindex=0;
			ErlLogger.debug("page change 0!");
			adapterPage.paintPage(editor);

		}else if (newPageIndex == 1) {
			pageindex=1;
			ErlLogger.debug("page change 1!");
		}

	}


	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

/*	private boolean ui_dirty = false;
	public boolean isDirty() {
		// use nestedEditors to avoid SWT requests; see bug 12996
		boolean flag = super.isDirty();
		if (ui_dirty)
			return ui_dirty;
		else
			return flag;
	}

	public void setFlag(boolean flag){
		ui_dirty = flag;

	}
	*/


	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		ErlLogger.debug("do save"+pageindex);
		getEditor(1).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		ErlLogger.debug("do save as");
		IEditorPart editor = getEditor(pageindex);
		editor.doSaveAs();
		setPageText(pageindex, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		ErlLogger.debug("resourceChanged input");
		final IEditorInput input=getEditorInput();
		ErlLogger.debug("resource input:"+(input instanceof IFileEditorInput));



		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}
			});
		}
	}
}
