package com.rytong.template.editor.actions;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRewriteTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.core.ErlangCore;
import org.erlide.core.ErlangStatus;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;
import org.erlide.utils.Util;


import com.ericsson.otp.erlang.OtpErlangObject;
import com.rytong.template.editor.template.TemplateEditor;
import com.rytong.template.editor.template.TemplateEditorMessages;

public class IndentAction extends TextEditorAction {

	public IndentAction(ResourceBundle bundle, String prefix,
			ITextEditor editor) {
		super(bundle, prefix, editor);
		// TODO Auto-generated constructor stub
	}

    protected ISelection getSelection() {
        return getTextEditor().getSelectionProvider().getSelection();
    }

    /**
     * Extend the selection that the action will work on. Default
     * implementation, extend to whole lines. Might be overridden.
     *
     * @param document
     *            text {@link IDocument}
     * @param selection
     *            original selection
     * @return new {@link ITextSelection} extended to the whole lines
     *         intersected by selection
     */
    public static ITextSelection extendSelectionToWholeLines(
            final IDocument document, final ITextSelection selection) {
        final int startLine = selection.getStartLine();
        final int endLine = selection.getEndLine();
        int startLineOffset;
        try {
            startLineOffset = document.getLineOffset(startLine);
            final int endTextOffset = document.getLineOffset(endLine)
                    + document.getLineLength(endLine);
            return new TextSelection(document, startLineOffset, endTextOffset
                    - startLineOffset);
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
        return selection;
    }


    /**
     * Selects the given range on the editor.
     *
     * @param newOffset
     *            the selection offset
     * @param newLength
     *            the selection range
     */
    protected void selectAndReveal(final int newOffset, final int newLength) {
        final ITextEditor editor = getTextEditor();
        if (editor instanceof TemplateEditor) {
            final TemplateEditor erlEditor = (TemplateEditor) editor;
            erlEditor.selectAndReveal(newOffset, newLength);
        } else {
            // this is too intrusive, but will never get called anyway
            getTextEditor().selectAndReveal(newOffset, newLength);
        }

    }

    protected OtpErlangObject indentLines(final int offset, final int length,
    		final String text) throws RpcException {
    	IBackend ideBackend = BackendCore.getBackendManager().getIdeBackend();
    	if(ideBackend != null) {
    		ErlLogger.debug("call ewp backend to indent the cs file");
    		OtpErlangObject res = ideBackend.call(15000, "ewp_tmpl_indent", "indent_lines", "iis", offset, length, text);
    		ErlLogger.debug("the rpc call result : " + res);
    		return res;

    	}
    	return null;

    }

    @Override
    public void run() {
        super.run();
        final ISelection sel = getSelection();
        if (sel == null || sel.isEmpty() || !(sel instanceof ITextSelection)) {
            return;
        }
        if (!validateEditorInputState()) {
            return;
        }
        final ITextEditor textEditor = getTextEditor();
        final IDocument document = textEditor.getDocumentProvider()
                .getDocument(textEditor.getEditorInput());
        final ITextSelection selection = extendSelectionToWholeLines(document,
                (ITextSelection) sel);
//        final ITextSelection getSelection = getTextSelection(document,
//                selection);

        final int startLine = selection.getStartLine();
        final int endLine = selection.getEndLine();
        final int nLines = endLine - startLine + 1;


        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String text;
                OtpErlangObject r1 = null;
                try {
                    text = document.get(selection.getOffset(), selection.getLength());
                    ErlLogger.debug("the selected text : "+ text);
                    // call erlang, with selection within text
                    r1 = indentLines(0, 0, text);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                final String newText = Util.stringValue(r1);
                if (newText == null) {
                    final Status status = new Status(IStatus.ERROR,
                            ErlangCore.PLUGIN_ID,
                            ErlangStatus.INTERNAL_ERROR.getValue(), "indent returned "
                                    + r1 + " instead of a string", null);
                    ErlLogger.error("INTERNAL ERROR: indent returned " + r1
                            + " instead of a string");

                    ErrorDialog.openError(textEditor.getSite().getShell(),
                    		TemplateEditorMessages.IndentAction_error_message,
                            String.valueOf(r1), status);
                    return;
                }
                final IRewriteTarget target = (IRewriteTarget) textEditor
                        .getAdapter(IRewriteTarget.class);
                if (target != null) {
                    target.beginCompoundChange();
                    if (nLines > 1) {
                        target.setRedraw(false);
                    }
                }
                try {
                    // ErlLogger.debug("'"+newText+"'");
                    if (!document.get(selection.getOffset(),
                            selection.getLength()).equals(newText)) {
                        document.replace(selection.getOffset(),
                                selection.getLength(), newText);
                    }
                    selectAndReveal(selection.getOffset(), newText.length());
                } catch (final BadLocationException e) {
                    ErlLogger.warn(e);
                }
                if (target != null) {
                    target.endCompoundChange();
                    if (nLines > 1) {
                        target.setRedraw(true);
                    }
                }
            }
        };
        if (nLines > 50) {
            final Display display = textEditor.getEditorSite()
                    .getWorkbenchWindow().getShell().getDisplay();
            BusyIndicator.showWhile(display, runnable);
        } else {
            runnable.run();
        }
    }

}
