package com.rytong.ui.editor.conf;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.ui.editor.ColorManager;
import com.rytong.ui.internal.RytongUIPlugin;
import com.rytong.ui.internal.preference.PreferenceConstants;

/**
 * ConfEditor is the TextEditor to edit Erlang conf based conf file for ewp.
 * 
 * @author lu.jingbo@rytong.com
 * 
 */
public class SourceEditor extends TextEditor {

	private ColorManager colorManager;
	private ConfScanner confScanner;

	public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
	public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

	public SourceEditor() {
		this(null);
	}

	public SourceEditor(ConfEditor ce) {
		super();
		setDocumentProvider(new SourceDocumentProvider(ce));
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		colorManager = new ConfColorManager();
		SourceViewerConfiguration cfg = new ConfSourceViewerConfiguration(this);
		setSourceViewerConfiguration(cfg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#
	 * configureSourceViewerDecorationSupport
	 * (org.eclipse.ui.texteditor.SourceViewerDecorationSupport)
	 */
	@Override
	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		char[] matchChars = { '{', '}', '[', ']' }; // which brackets to match
		ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(
				matchChars, IDocumentExtension3.DEFAULT_PARTITIONING);
		support.setCharacterPairMatcher(matcher);
		support.setMatchingCharacterPainterPreferenceKeys(
				EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);

		// Enable bracket highlighting in the preference store
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(EDITOR_MATCHING_BRACKETS, true);
		store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, "128,128,128");
	}

	@Override
	public void doSaveAs() {
		make_errors();
		super.doSaveAs();
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		make_errors();
		super.doSave(progressMonitor);
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();
	}

	public ConfScanner getConfScanner() {
		if (confScanner == null)
			confScanner = new ConfScanner(colorManager);
		return confScanner;
	}

	private void make_errors() {
		IEditorInput editorInput = this.getEditorInput();

		if (!(editorInput instanceof IFileEditorInput)) {
			return;
		}
		String content = getDocumentProvider().getDocument(editorInput).get();
		IFile file = ((IFileEditorInput) editorInput).getFile();
		// String fn = file.getLocation().toFile().getAbsolutePath();

		// start by clearing all the old markers.
		int depth = IResource.DEPTH_INFINITE;
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, depth);
		} catch (CoreException e) {
			ErlLogger.info("failed to delete old markers.");
			e.printStackTrace();
		}
		Object[] rst = consult_conf(content);
		if (rst != null) {
			int lineno = Integer.valueOf(rst[0].toString());
			String errmsg = (String) rst[1];
			IPreferenceStore prefs = RytongUIPlugin.getDefault()
					.getPreferenceStore();
			String severity = prefs.getString(PreferenceConstants.VALIDATION);
			if (PreferenceConstants.SYNTAX_VALIDATION_IGNORE.equals(severity)) {
				ErlLogger
						.debug("Possible syntax errors ignored due to preference settings");
				return;
			}

			int markerSeverity = IMarker.SEVERITY_ERROR;

			if (PreferenceConstants.SYNTAX_VALIDATION_WARNING.equals(severity))
				markerSeverity = IMarker.SEVERITY_WARNING;
			IMarker marker;
			try {
				marker = file.createMarker(IMarker.PROBLEM);

				marker.setAttribute(IMarker.SEVERITY, markerSeverity);
				marker.setAttribute(IMarker.MESSAGE, errmsg);
				marker.setAttribute(IMarker.LINE_NUMBER, lineno);
			} catch (CoreException e) {
				ErlLogger.warn("failed to create marker.");
				e.printStackTrace();
			}
		}
	}

	private Object[] consult_conf(String content) {

		IBackend ideBackend = BackendCore.getBackendManager().getIdeBackend();
		Object[] rst = null;
		if (ideBackend != null) {
			ErlLogger.debug("call erlang backend to parse conf file");
			OtpErlangObject res = null;
			try {
				res = ideBackend.call("rytong_util", "consult", "s", content);
				if (res instanceof OtpErlangTuple) {
					final OtpErlangTuple tuple = (OtpErlangTuple) res;
					ErlLogger.debug("the tuple : " + tuple);
					OtpErlangAtom tag = (OtpErlangAtom) tuple.elementAt(0);

					if (tag.atomValue().equals("error")) {
						OtpErlangTuple errdata = (OtpErlangTuple) tuple
								.elementAt(1);
						int lineno = ((OtpErlangLong) errdata.elementAt(0))
								.intValue();
						String errmsg = ((OtpErlangString) errdata.elementAt(1))
								.stringValue();
						rst = new Object[] { lineno, errmsg };
					}
				}
			} catch (Exception e) {
				ErlLogger.warn("failed to consult conf file.");
				e.printStackTrace();
			}
		}
		return rst;
	}
}
