package com.rytong.ui.editor.conf;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public abstract class OpenFormEditorAction extends AbstractHandler {

	protected Object openEditor(String inputName, String editorId, IWorkbenchWindow window) {
		return openEditor(new FormEditorInput(inputName), editorId, window);
	}
	
	protected Object openEditor(IEditorInput input, String editorId, IWorkbenchWindow window) {
		IWorkbenchPage page = window.getActivePage();
		try {
			page.openEditor(input, editorId);
		} catch (PartInitException e) {
			System.out.println(e);
		}
		return null;
	}
}
