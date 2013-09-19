package com.rytong.ui.editor.lua;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.editors.text.TextEditor;

import com.rytong.ui.editor.ColorManager;
import com.rytong.ui.editor.lua.model.LuaDocument;

/**
 * Rewrite of LuaEditor without using DLTK API. 
 * @author lu.jingbo@rytong.com
 *
 */
public class LuaEditor extends TextEditor {

	private ColorManager colorManager;
   private LuaScanner scanner;
   private LuaDocument luaDocument;

	public LuaEditor() {
		super();
      colorManager = new LuaColorManager();
		setSourceViewerConfiguration(new LuaSourceViewerConfiguration(this));
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
    
	public LuaScanner getLuaScanner() {
		if (scanner == null)
			scanner = new LuaScanner(colorManager);
		return scanner;
	}

	public void setLuaDocument(LuaDocument ld) {
		this.luaDocument = ld;
	}
    
   public LuaDocument getLuaDocument() {
   	return luaDocument;
    }

}
