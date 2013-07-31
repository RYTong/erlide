package com.rytong.ui.econfeditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * EconfSourceViewerConfiguration manages the configuration of the editor.
 * 
 * @author lu.jingbo@rytong.com
 *
 */
public class EconfSourceViewerConfiguration extends SourceViewerConfiguration {

	private EconfEditor editor;

	public EconfSourceViewerConfiguration(EconfEditor editor) {
		super();
      this.editor = editor;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
      PresentationReconciler reconciler = new PresentationReconciler();
      addDamagerRepairer(reconciler, editor.getEconfScanner(), IDocument.DEFAULT_CONTENT_TYPE);
      return reconciler;
	}

	private void addDamagerRepairer(PresentationReconciler reconciler,
			RuleBasedScanner scanner, String contentType) {
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);		
      reconciler.setDamager(dr, contentType);
      reconciler.setRepairer(dr, contentType);
	}

}
