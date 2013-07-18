/*******************************************************************************
 * Copyright (c) 2009, 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package com.rytong.template.editor.lua;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.erlide.jinterface.ErlLogger;

import com.rytong.template.editor.actions.IndentAction;



import com.rytong.template.editor.Activator;
import com.rytong.template.editor.lua.LuaLanguageToolkit;
import com.rytong.template.editor.template.TemplateEditorMessages;



public class LuaEditor extends ScriptEditor {

    public static final String EDITOR_ID = "com.rytong.editors.LuaEditor";

    public static final String EDITOR_CONTEXT = "#EWPLuaContext";

    public static final String INDENT = "com.rytong.template.editor.actions.indent";


    IEditorInput input = null;

    private IndentAction indentAction;

    protected void initializeEditor() {
        super.initializeEditor();
        setEditorContextMenuId(EDITOR_CONTEXT);
    }

    public IPreferenceStore getScriptPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }

    /** Connects partitions used to deal with comments or strings in editor. */
    protected void connectPartitioningToElement(IEditorInput input, IDocument document) {
        ErlLogger.debug("Lua Editor start!");
        if (document instanceof IDocumentExtension3) {
            this.input = input;
            IDocumentExtension3 extension = (IDocumentExtension3) document;
            if (extension.getDocumentPartitioner(ILuaPartitions.LUA_PARTITIONING) == null) {
                LuaTextTools tools = Activator.getDefault().getLuaTextTools();
                tools.setupDocumentPartitioner(document, ILuaPartitions.LUA_PARTITIONING);
            }

        }
    }

    @Override
    public String getEditorId() {
        // TODO Auto-generated method stub
        return EDITOR_ID;
    }

    @Override
    public IDLTKLanguageToolkit getLanguageToolkit() {
        // TODO Auto-generated method stub
        return LuaLanguageToolkit.getDefault();
    }

    @Override
    public ScriptTextTools getTextTools() {
        return Activator.getDefault().getLuaTextTools();
    }


    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        this.input = getEditorInput();
    }


    @Override
    protected void initializeKeyBindingScopes() {
        setKeyBindingScopes(new String[] { "com.rytong.template.editor.templateEditorScope" }); //$NON-NLS-1$
    }

    @Override
    protected void createActions() {
        super.createActions();

        indentAction = new IndentAction(
                                        TemplateEditorMessages.getBundleForConstructedKeys(),
                                        "Indent.", this); //$NON-NLS-1$
        indentAction.setActionDefinitionId(INDENT);
        setAction("Indent", indentAction); //$NON-NLS-1$
        markAsStateDependentAction("Indent", true); //$NON-NLS-1$
        markAsSelectionDependentAction("Indent", true); //$NON-NLS-1$
    }

    protected IDocument getInputDocument()
    {
        IDocument document = getDocumentProvider().getDocument(input);
        return document;
    }

    protected IFile getInputFile(IEditorInput input)
    {
        IFileEditorInput ife = (IFileEditorInput) input;
        IFile file = ife.getFile();
        return file;
    }

}
