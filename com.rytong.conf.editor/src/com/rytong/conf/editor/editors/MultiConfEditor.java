package com.rytong.conf.editor.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.erlide.jinterface.ErlLogger;
import com.rytong.conf.editor.pages.CollectionsPage;
import com.rytong.ui.editor.conf.SourceEditor;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiConfEditor extends MultiPageEditorPart implements IResourceChangeListener{

    /** The text editor used in page 0. */
    private SourceEditor editor = new SourceEditor();

    /** The editor show a tree.  */
    private CollectionsPage collectionPage = new CollectionsPage();

    private int pageindex;

    /**
     * Creates a multi-page editor example.
     */
    public MultiConfEditor() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    /**
     * Creates page 2 of the multi-page editor,
     * which contains a tree .
     */

    private void createPage0() {
        // TODO Auto-generated method stub
        Composite composite = new Composite(getContainer(), SWT.FULL_SELECTION);
        collectionPage.setComposite(composite);
        collectionPage.initialPage();


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
            //editor.
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
     * Creates the pages of the multi-page editor.
     */
    protected void createPages() {

        createPage0();
        createPage1();
    }

    /**
     * The <code>MultiPageEditorPart</code> implementation of this
     * <code>IWorkbenchPart</code> method disposes all nested editors.
     * Subclasses may extend.
     */
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }
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
     * Method declared on IEditorPart
     */
    public void gotoMarker(IMarker marker) {
        setActivePage(0);
        IDE.gotoMarker(getEditor(0), marker);
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
    /* (non-Javadoc)
     * Method declared on IEditorPart.
     */
    public boolean isSaveAsAllowed() {
        return true;
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
            collectionPage.setEditor(editor);

            collectionPage.paintPage();
        }else if (newPageIndex == 1) {
            pageindex=1;
            ErlLogger.debug("page change 1!");
        }

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

    public boolean testFile(){
        return isDirty();
    }



}
