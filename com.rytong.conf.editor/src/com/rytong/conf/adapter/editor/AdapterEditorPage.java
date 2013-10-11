package com.rytong.conf.adapter.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;
import org.erlide.utils.Util;
import org.xml.sax.SAXException;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangBinary;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.editor.handler.AdapterHandler;
import com.rytong.conf.editor.handler.ConfErrorHandler;
import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.editor.pages.EwpCollections;
import com.rytong.ui.editor.conf.ConfEditor;
import com.rytong.ui.editor.conf.SourceEditor;

public class AdapterEditorPage {

    private AdapterEditor parent_editor;
    private Composite parent;
    private SourceEditor editor;
    private IDocument document;

    private AdapterEditorTreeComposite left_com;
    private AdapterEditorProcedureComposite right_com;
    private AdapterEditorAdapterComposite adp_com;
    private AdapterHandler confPaser = new AdapterHandler();
    private EwpAdpaterList tmpAdpList ;

    public AdapterEditorPage(AdapterEditor parent_editor, Composite parent){
        this.parent = parent;
        this.parent_editor = parent_editor;
    }

    /** Return the adapter editor composite
     *
     * @return AdapterEditorAdapterComposite
     */
    public AdapterEditorAdapterComposite get_adapter_composite(){
        return adp_com;
    }

    public AdapterEditorProcedureComposite get_right(){
        return right_com;
    }

    public AdapterEditorTreeComposite get_left(){
        return left_com;
    }

    public Composite getParent(){
        return parent;
    }

/*	public void setDirtyFlag(){
        parent_editor.setFlag(true);
    }*/

    /**
     * Creates page 0 of the multi-page editor,
     * which contains a text editor.
     */
    public void initialPage() {

        //Composite composite_left = new Composite(parent, SWT.BORDER);

        //GridLayout layout = new GridLayout();
        parent.setLayout( new FormLayout());

        // overview label sets
        Label label_overview = new Label(parent, 0);
        // @FIXME Set the font style of this label
        label_overview.setText("Overview");

        FormData label_form = new FormData();
        label_form.left = new FormAttachment(0,6);
        label_form.right = new FormAttachment(30);
        label_form.top = new FormAttachment(0,5);
        label_overview.setLayoutData(label_form);

        // initial left composite
        right_com = new AdapterEditorProcedureComposite(this);
        adp_com = new AdapterEditorAdapterComposite(this);
        left_com = new AdapterEditorTreeComposite(this);


        left_com.initial_left_com();
        right_com.initial_right_com();
        adp_com.initial_right_com();

    }

    public void paintPage(SourceEditor maineditor){

        editor = maineditor;
        document = editor.getDocumentProvider()
                .getDocument(editor.getEditorInput());
        String channelxml = getContent();
        ErlLogger.debug("channelxml:"+channelxml);
        doParse(channelxml);
        right_com.setProcedureEditorUnVisiable();


    }

    private final String conFromFile= "path";
    private final String conFromDoc= "text";
    private String getContent(){
        //IPath filePath =  ((FileEditorInput) editor.getEditorInput()).getFile().getLocation();
        //filePathStr = filePath.toString();
        OtpErlangObject result=null;
        String text = document.get();

        if (text == "" || text ==null){
             ErlLogger.debug("document get null!");
                IPath filePath =  ((FileEditorInput) editor.getEditorInput()).getFile().getLocation();
                String filePathStr = filePath.toString();
                result = getIdeBackend(conFromFile, filePathStr);
        } else {
             ErlLogger.debug("document text not null!");
                result = getIdeBackend(conFromDoc, text);
        }

        return Util.stringValue(result);

    }

    // parse xml
    private void doParse(String xml) {
        try {
            //ErlLogger.debug("input:"+xml);
            String newxml = xml.replace("&", "&amp;");
            tmpAdpList = new EwpAdpaterList();
            confPaser.setResultObj(tmpAdpList);
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new ByteArrayInputStream(newxml.getBytes()), confPaser);
            //ErlLogger.debug("adpl:"+adpL.getProcedureList().size());
            left_com.getViewer().setInput(tmpAdpList);

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void refreshTree(){
        left_com.getViewer().setInput(tmpAdpList);
    }

    public void setDocument(OtpErlangObject result){
        try {
            OtpErlangList resultList = (OtpErlangList) result;
            OtpErlangObject resultConf = (OtpErlangObject)resultList.elementAt(0);
            confCon = (OtpErlangBinary)resultList.elementAt(1);

            String resultStr = Util.stringValue(resultConf);
            //ErlLogger.debug("resultStr:"+resultStr);
            document.replace(0, document.getLength(), resultStr);

        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void erlBackend_editAdapter(OtpErlangTuple params){
        OtpErlangObject res = null;

        if(ideBackend == null) {
            ideBackend = BackendCore.getBackendManager().getIdeBackend();
        };
        ErlLogger.debug("call ewp backend to add a new procedure parameters.");
        try {
            res = ideBackend.call(15000, "ewp_adapter_conf", "edit_adapter", "1b1s", confCon, params);
        } catch (RpcException e) {
            e.printStackTrace();
        }
        //ErlLogger.debug("the rpc call result : " + result);
        setDocument(res);
    }

    public void erlBackend_editProcedure(OtpErlangTuple params){
        OtpErlangObject res = null;

        if(ideBackend == null) {
            ideBackend = BackendCore.getBackendManager().getIdeBackend();
        };
        ErlLogger.debug("call ewp backend to add a new procedure parameters.");
        try {
            res = ideBackend.call(15000, "ewp_adapter_conf", "edit_procedure", "1b1s", confCon, params);
        } catch (RpcException e) {
            e.printStackTrace();
        }
        //ErlLogger.debug("the rpc call result : " + result);
        setDocument(res);
    }

    public void erlBackend_addAdapter(EwpAdapter adp){
        OtpErlangTuple params = adp.formAdapter();
        OtpErlangObject res = null;

        if(ideBackend == null) {
            ideBackend = BackendCore.getBackendManager().getIdeBackend();
        };
        ErlLogger.debug("call ewp backend to add a new adapter.");
        try {
            res = ideBackend.call(15000, "ewp_adapter_conf", "add_adapter", "1b1s", confCon, params);
        } catch (RpcException e) {
            e.printStackTrace();
        }
        //ErlLogger.debug("the rpc call result : " + result);
        setDocument(res);
    }

    public void erlBackend_addProcedure(EwpProcedure tmppro){
        OtpErlangTuple params = tmppro.formProcedure();
        OtpErlangObject res = null;

        if(ideBackend == null) {
            ideBackend = BackendCore.getBackendManager().getIdeBackend();
        };
        ErlLogger.debug("call ewp backend to add a new procedure.");
        try {
            res = ideBackend.call(15000, "ewp_adapter_conf", "add_procedure", "1b1s", confCon, params);
        } catch (RpcException e) {
            e.printStackTrace();
        }
        //ErlLogger.debug("the rpc call result : " + result);
        setDocument(res);
    }

    // call erlang node
    protected IBackend ideBackend = null;
    public OtpErlangBinary confCon=null;
    public OtpErlangObject getIdeBackend(String flag, String params){
        OtpErlangObject res = null;
        OtpErlangObject result=null;
        if(ideBackend == null) {
            ideBackend = BackendCore.getBackendManager().getIdeBackend();
        };

        try {
            res = ideBackend.call(15000, "ewp_conf_parse", "parse_channel_config", "ss", flag, params);
        } catch (RpcException e) {
            e.printStackTrace();
        };
        OtpErlangList resList = (OtpErlangList) res;
        result = (OtpErlangObject)resList.elementAt(0);
        confCon= (OtpErlangBinary)resList.elementAt(1);
        //ErlLogger.debug("the rpc call result : " + result);
        return result;
    }

    public EwpAdpaterList getAdpList(){
        return tmpAdpList;
    }


}
