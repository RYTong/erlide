package com.rytong.template.editor.lua;

import org.eclipse.dltk.ui.editor.highlighting.SemanticHighlighting;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.texteditor.ITextEditor;


public class LuaTextTools extends ScriptTextTools {

	private final static String[] LEGAL_CONTENT_TYPE = new String[] {
    	ILuaPartitions.LUA_COMMENT,
    	ILuaPartitions.LUA_STRING,
    	ILuaPartitions.LUA_SINGLE_QUOTE_STRING,
    	ILuaPartitions.LUA_MULTI_LINE_COMMENT,
    	ILuaPartitions.LUA_NUMBER
	};


	public LuaTextTools(boolean autoDisposeOnDisplayDispose) {
        super(ILuaPartitions.LUA_PARTITIONING, LEGAL_CONTENT_TYPE, autoDisposeOnDisplayDispose);
	}

	@Override
	public ScriptSourceViewerConfiguration createSourceViewerConfiguraton(
			IPreferenceStore preferenceStore, ITextEditor editor, String partitioning) {
		// TODO Auto-generated method stub
		return new LuaSourceViewerConfiguration(getColorManager(), preferenceStore, editor, partitioning);
	}


	public IPartitionTokenScanner getPartitionScanner() {
		return new LuaPartitionScanner();
	}


//	public SemanticHighlighting[] getSemanticHighlightings() {
//		return new LuaSemanticUpdateWorker().getSemanticHighlightings();
//	}


}

