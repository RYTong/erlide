package com.rytong.ui.formatter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.rytong.ui.editor.conf.ConfEditor;

/**
 * @author lu.jingbo@rytong.com
 * 
 */
public class FormatUtil {

	public static IFormatter getFormatter(ExecutionEvent event) {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		IEditorInput input = HandlerUtil.getActiveEditorInput(event);
		if (editor instanceof ConfEditor && ((ConfEditor) editor).getActivePage()==0) {
			IDocument document = ((ConfEditor) editor).getSourcePage()
					.getDocumentProvider().getDocument(input);
			return new ConfFormatter(document);
		}
		return null;
	}

}
