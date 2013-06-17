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

package com.rytong.template.editor.template;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptPresentationReconciler;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.texteditor.ITextEditor;

import com.rytong.template.editor.cs.CSCodeScanner;
import com.rytong.template.editor.xml.XMLTagScanner;
import com.rytong.template.editor.lua.LuaCodeScanner;
import com.rytong.template.editor.lua.LuaContentAssistPreference;

public class TemplateSourceViewerConfiguration extends
        ScriptSourceViewerConfiguration {

    private AbstractScriptScanner fLuaCodeScanner;
    private AbstractScriptScanner fCSCodeScanner;
	private AbstractScriptScanner fXMLTagScanner;
	private AbstractScriptScanner fTemplateStringScanner;
//	private XMLScanner fXMLScanner;
//	private ColorManager colorManager;
//    private AbstractScriptScanner fCommentScanner;
//    private AbstractScriptScanner fMultilineCommentScanner;
//    private AbstractScriptScanner fNumberScanner;
    
    public TemplateSourceViewerConfiguration(IColorManager colorManager, IPreferenceStore preferenceStore, ITextEditor editor, String partitioning) {
        super(colorManager, preferenceStore, editor, partitioning);
    }
    
    @Override
    protected ContentAssistPreference getContentAssistPreference() {
        return LuaContentAssistPreference.getDefault();
    }
    
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new ScriptPresentationReconciler();
        reconciler.setDocumentPartitioning(this.getConfiguredDocumentPartitioning(sourceViewer));

        DefaultDamagerRepairer dr;
        
        dr = new DefaultDamagerRepairer(this.fCSCodeScanner);
        reconciler.setDamager(dr, ITemplatePartitions.CS);
        reconciler.setRepairer(dr, ITemplatePartitions.CS);
        
        dr = new DefaultDamagerRepairer(this.fTemplateStringScanner);
        reconciler.setDamager(dr, ITemplatePartitions.TEMPLATE_STRING);
        reconciler.setRepairer(dr, ITemplatePartitions.TEMPLATE_STRING);
        
        
        dr = new DefaultDamagerRepairer(this.fLuaCodeScanner);
        reconciler.setDamager(dr, ITemplatePartitions.LUA);
        reconciler.setRepairer(dr, ITemplatePartitions.LUA);
        

        dr = new DefaultDamagerRepairer(this.fXMLTagScanner);
        reconciler.setDamager(dr, ITemplatePartitions.XML_TAG);
        reconciler.setRepairer(dr, ITemplatePartitions.XML_TAG);
        

//        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
//        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);


        return reconciler;
    }

    /**
     * This method is called from base class.
     */
    protected void initializeScanners() {
        // This is lua code scanner
        this.fLuaCodeScanner = new LuaCodeScanner(this.getColorManager(), this.fPreferenceStore);
        this.fCSCodeScanner = new CSCodeScanner(this.getColorManager(), this.fPreferenceStore);

        // This is xml scanners for partitions.
        this.fXMLTagScanner = new XMLTagScanner(this.getColorManager(), this.fPreferenceStore);
        this.fTemplateStringScanner = new TemplateStringScanner(this.getColorManager(), this.fPreferenceStore);

    }

    public void handlePropertyChangeEvent(PropertyChangeEvent event) {
        if (this.fLuaCodeScanner.affectsBehavior(event)) {
            this.fLuaCodeScanner.adaptToPreferenceChange(event);
        }

        if (this.fCSCodeScanner.affectsBehavior(event)) {
            this.fCSCodeScanner.adaptToPreferenceChange(event);
        }
        if (this.fXMLTagScanner.affectsBehavior(event)) {
            this.fXMLTagScanner.adaptToPreferenceChange(event);
        }
        if (this.fTemplateStringScanner.affectsBehavior(event)) {
            this.fTemplateStringScanner.adaptToPreferenceChange(event);
        }
    }

    public boolean affectsTextPresentation(PropertyChangeEvent event) {
        return this.fLuaCodeScanner.affectsBehavior(event)||this.fCSCodeScanner.affectsBehavior(event) 
        		|| this.fXMLTagScanner.affectsBehavior(event) || this.fTemplateStringScanner.affectsBehavior(event);
    }

    

    /**
     * Lua specific one line comment
     * 
     * @see ScriptSourceViewerConfiguration#getCommentPrefix()
     */
    @Override
    protected String getCommentPrefix() {
        return TemplateConstants.COMMENT_STRING;
    }

    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer) {
        return ITemplatePartitions.TEMPLATE_PARTITION_TYPES;
    }

}
