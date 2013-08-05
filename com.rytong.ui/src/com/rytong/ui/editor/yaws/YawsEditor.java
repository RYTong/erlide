package com.rytong.ui.editor.yaws;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.editors.text.TextEditor;

import com.rytong.ui.editor.ColorManager;

/**
 * YawsEditor is the TextEditor to edit Yaws conf file.
 * 
 * @author lu.jingbo@rytong.com
 * 
 */
public class YawsEditor extends TextEditor {

	private ColorManager colorManager;
	private YawsScanner yawsScanner;

	public YawsEditor() {
		super();
		// setDocumentProvider(provider);
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		colorManager = new YawsColorManager();
		SourceViewerConfiguration cfg = new YawsSourceViewerConfiguration(this);
		setSourceViewerConfiguration(cfg);
	}

	@Override
	public void doSaveAs() {
		super.doSaveAs();
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	public YawsScanner getConfScanner() {
		if (yawsScanner == null)
			yawsScanner = new YawsScanner(colorManager);
		return yawsScanner;
	}


}
