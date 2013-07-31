package com.rytong.ui.econfeditor;

import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * EconfTextEditor is the TextEditor instance used by the plugin.
 * 
 * @author lu.jingbo@rytong.com
 *
 */
public class EconfEditor extends TextEditor {

	private ColorManager colorManager;
   private EconfScanner econfScanner;

	public EconfEditor() {
		super();
      //setDocumentProvider(provider);
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
      colorManager = new ColorManager();
      SourceViewerConfiguration cfg = new EconfSourceViewerConfiguration(this);
      setSourceViewerConfiguration(cfg);
	}

	@Override
	public void dispose() {
      colorManager.dispose();
		super.dispose();
	}

	public EconfScanner getEconfScanner() {
    	if (econfScanner == null) 
    		econfScanner = new EconfScanner(colorManager);
      return econfScanner;
	}
}
