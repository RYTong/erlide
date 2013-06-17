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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;
import org.erlide.utils.SystemUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.rytong.template.editor.actions.IndentAction;


import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.template.editor.Activator;
import com.rytong.template.editor.lua.LuaLanguageToolkit;
import com.rytong.template.editor.markers.TemplateErrorHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TemplateEditor extends ScriptEditor {

    public static final String EDITOR_ID = "com.rytong.editors.TemplateEditor";

    public static final String EDITOR_CONTEXT = "#EWPTemplateEditorContext";

    public static final String INDENT = "com.rytong.template.editor.actions.indent";

    SAXParserFactory fParserFactory = null;

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
        if (document instanceof IDocumentExtension3) {
            this.input = input;
            IDocumentExtension3 extension = (IDocumentExtension3) document;
            if (extension.getDocumentPartitioner(ITemplatePartitions.TEMPLATE_PARTITIONING) == null) {
                TemplateTextTools tools = Activator.getDefault().getTextTools();
                tools.setupDocumentPartitioner(document, ITemplatePartitions.TEMPLATE_PARTITIONING);
            }
            validateAndMark();
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
        return Activator.getDefault().getTextTools();
    }


    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        this.input = getEditorInput();
        validateAndMark();
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


    protected void validateAndMark() {
        try
            {
                IFile file = getInputFile(input);
                ErlLogger.debug("the file name : " +file.getName());
                String content = getInputDocument().get();
                TemplateErrorHandler reporter = new TemplateErrorHandler(file);
                reporter.removeExistingMarkers();


                try {
                    IBackend ewpBackend = BackendCore.getBackendManager().getEWPBackend();
                    //ErlLogger.debug("is ewpBackend:"+ewpBackend);
                    if(ewpBackend != null) {
                        ErlLogger.debug("call ewp backend to parse the cs file");
                        OtpErlangObject res = ewpBackend.call("tmpl", "validate_for_ide", "s", content);
                        //ErlLogger.debug("the rpc call result : " + res);
                        if(res instanceof OtpErlangTuple) {
                            final OtpErlangTuple tuple = (OtpErlangTuple) res;
                            ErlLogger.debug("the tuple : " + tuple);
                            OtpErlangList list = (OtpErlangList) tuple.elementAt(1);
                            for(final OtpErlangObject o : list) {
                                final OtpErlangTuple error = (OtpErlangTuple) o;
                                OtpErlangLong l = (OtpErlangLong) error.elementAt(0);
                                OtpErlangString s = (OtpErlangString) error.elementAt(1);
                                //OtpErlangAtom a = (OtpErlangAtom) tuple.elementAt(0);
                                reporter.addCSError(l.intValue(), s.stringValue());
                            }

                        }
                    }
                    SAXParser parser = getParser();
                    XMLReader reader = parser.getXMLReader();

                    reader.setFeature("http://xml.org/sax/features/validation", false);
                    reader.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
                    reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                    reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
                    reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                    reader.setFeature("http://xml.org/sax/features/use-entity-resolver2", false);
                    reader.setFeature("http://xml.org/sax/features/resolve-dtd-uris", false);


                    /*
                    try
                    {
                    String strLang = XMLConstants.W3C_XML_SCHEMA_NS_URI;
                    SchemaFactory factory = SchemaFactory.newInstance(strLang);

                    InputStream fc = file.getContents();
                    StreamSource fs = new StreamSource(fc);
                    Schema ss = factory.newSchema();
                    Validator validator = ss.newValidator();
                    validator.validate(fs);
                    System.out.println("Result:Valid!");
                    }catch (Exception e){
                    	e.printStackTrace();
                    	System.out.println("Result : Invalid!");
                    }
                    */
        			ErlLogger.debug("is valide:"+parser.isValidating());
        			ErlLogger.debug("is namespace:"+parser.isNamespaceAware());
                    InputStream fc = file.getContents();

            		int i = -1;
            		//org.apache.commons.io.output.ByteArrayOutputStream
            		ByteArrayOutputStream is2str = new ByteArrayOutputStream();
            		while ((i = fc.read()) != -1) {
            			is2str.write(i);
            		}
            		String filestr = is2str.toString();

            		Pattern pattern = Pattern.compile("[\n\t\r\\s]*<[\n\r\t\\s]*\\?[\n\r\\s\t]*xml");
            		Matcher matcher = pattern.matcher(filestr);
            		ErlLogger.debug("find result:"+matcher.find(0));

            		if (matcher.find(0) == true){
            			parser.parse(file.getContents(), reporter);
            		} else {
            			StringBufferInputStream s1 = new StringBufferInputStream("<ewp_tmpl_root>");
            			StringBufferInputStream s2 = new StringBufferInputStream("</ewp_tmpl_root>");
            			SequenceInputStream s = new SequenceInputStream(s1, file.getContents());
            			parser.parse(new SequenceInputStream(s, s2), reporter);
            		}

                }
                catch (SAXParseException se) {
                }catch (RpcException e) {
                    //ErlLogger.debug("error happened when do rpc call");
                    e.printStackTrace();
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        catch (Exception e)
            {
                //e.printStackTrace();
            }
    }

    private SAXParser getParser() throws ParserConfigurationException,
                                         SAXException {
        if (fParserFactory == null) {
            fParserFactory = SAXParserFactory.newInstance();
            fParserFactory.setValidating(false);
        }
        return fParserFactory.newSAXParser();
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
