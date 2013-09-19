package com.rytong.ui.editor.lua;

//import net.sf.lunareclipse.core.LunarEclipse;
//import net.sf.lunareclipse.editor.LuaEditor;
//import net.sf.lunareclipse.editor.preferences.PreferenceConstants;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.MismatchedNotSetException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MismatchedTreeNodeException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class LuaAnalyser extends LuaParser {

	private IResource fResource;

	private LuaEditor fEditor;

//	private boolean fError = false;

	public LuaAnalyser(TokenStream input) {
		super(input);
	}

	public void reportError(RecognitionException e) {
//		fError = true;
		if (errorRecovery) {
			// System.err.print("[SPURIOUS] ");
			return;
		}
		errorRecovery = true;
//		System.out.println("Resource: " + fResource);
//		System.out.println("Marker on: " + LunarEclipse.getDefault().getPreferenceStore().getBoolean(
//				PreferenceConstants.MARKERS_PREF));
//		if (fResource != null && LunarEclipse.getDefault().getPreferenceStore().getBoolean(
//						PreferenceConstants.MARKERS_PREF)) {
		
			updateErrorInfo(this.getTokenNames(), e);
//		}
	}

	private void updateErrorInfo(String[] tokenNames, RecognitionException e) {
		IMarker m;
		String info = "unknown error";
		String tokenName = null;
		
		if (e instanceof MismatchedTokenException) {
			MismatchedTokenException mte = (MismatchedTokenException) e;
			if (mte.expecting == Token.EOF) {
				tokenName = "EOF";
			} else {
				tokenName = tokenNames[mte.expecting];
			}
			info = "found '" + e.token.getText() + "' expecting " + tokenName;
		} else if (e instanceof MismatchedTreeNodeException) {
			MismatchedTreeNodeException mtne = (MismatchedTreeNodeException) e;
			if (mtne.expecting == Token.EOF) {
				tokenName = "EOF";
			} else {
				tokenName = tokenNames[mtne.expecting];
			}
			info = "mismatched tree node " + mtne.foundNode
					+ "; expecting type " + tokenName;
		} else if (e instanceof NoViableAltException) {
			info = "unexpected '" + e.token.getText() + "'";
		} else if (e instanceof EarlyExitException) {
			info = "unexpected '" + e.token.getText() + "'";
		} else if (e instanceof MismatchedSetException) {
			MismatchedSetException mse = (MismatchedSetException) e;
			info = "found '" + e.token.getText() + "' expecting "
					+ mse.expecting;
		} else if (e instanceof MismatchedNotSetException) {
			MismatchedNotSetException mse = (MismatchedNotSetException) e;
			info = "found '" + e.token.getText() + "' expecting "
					+ mse.expecting;
		} else if (e instanceof FailedPredicateException) {
			FailedPredicateException fpe = (FailedPredicateException) e;
			info = "rule " + fpe.ruleName + " failed predicate: {"
					+ fpe.predicateText + "}?";
		}
		try {
			m = fResource.createMarker(IMarker.PROBLEM);
			m.setAttribute(IMarker.LINE_NUMBER, e.line);
			m.setAttribute(IMarker.MESSAGE, info);
			m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			if (e.token.getText() != null) {
				m.setAttribute(IMarker.CHAR_START, ((CommonToken) e.token)
						.getStartIndex());
				m.setAttribute(IMarker.CHAR_END, ((CommonToken) e.token)
						.getStopIndex() + 1);
			}
			// System.err.println("Error in line " + e.line + ":" +
			// (e.charPositionInLine + 1) + ":"
			// + ((CommonToken) e.token).getStartIndex() + "-" + (((CommonToken)
			// e.token).getStopIndex() + 1) + " " + info);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void setResource(IResource resource) {
		fResource = resource;
	}

	public void setEditor(LuaEditor editor) {
		fEditor = editor;
		fResource = (IResource) fEditor.getEditorInput().getAdapter(
				IResource.class);
		try {
			fResource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
