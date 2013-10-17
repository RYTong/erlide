package com.rytong.ui.editor.conf;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * @author lu.jingbo
 *
 */
public class SourceDocumentProvider extends FileDocumentProvider {
	private ConfEditor ce;

	public SourceDocumentProvider() {
		this(null);
	}
	
	public SourceDocumentProvider(ConfEditor ce) {
		super();
		this.ce = ce;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.StorageDocumentProvider#createDocument(java.lang.Object)
	 */
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		document.addDocumentListener(new IDocumentListener() {
			
			@Override
			public void documentChanged(DocumentEvent event) {
				ce.markDirty();
			}
			
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
		});
		return document;
	}

}
