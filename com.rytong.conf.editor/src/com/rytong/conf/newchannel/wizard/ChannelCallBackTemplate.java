package com.rytong.conf.newchannel.wizard;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.util.ChannelConfFileUtil;
import com.rytong.conf.util.FormatErlangSource;

public class ChannelCallBackTemplate {

    private String projectPath="";
    private NewChaWizard wizard;
    //private FormatErlangSource srcBuilder;
    private ChannelConfFileUtil fileBuilder;
    private static final String NEWCHANNEL_CALLBACK_CHANNEL = "\\$channel";
    private static final String NEWCHANNEL_CALLBACK_TRANCODE = "\\$trancode";
    private static final String NEWCHANNEL_CALLBACK_CS = "\\$cs";
    private static final String ADAPTER_TRANCODE_VAR = "TranCode";
    private static final String D_MARK = "\"";

    private static final String NEWCHANNEL_CALLBACK_ERL_BEFORE_RE = "\n[\r\\s\t]*before_request\\([^\\.]*\\.";
    private static final String NEWCHANNEL_CALLBACK_ERL_AFTER_RE = "\n[\r\\s\t]*after_request\\([^\\.]*\\.";


    public ChannelCallBackTemplate(NewChaWizard wizard) {
        // TODO Auto-generated constructor stub
        this.wizard = wizard;
        TextEditor tmpEditor = wizard.getTextEditor();
        IProject tmpProject = ((FileEditorInput) tmpEditor.getEditorInput()).getFile().getProject();
        projectPath = tmpProject.getLocation().toString();
        //srcBuilder = new FormatErlangSource();
        fileBuilder = new ChannelConfFileUtil();
    }


    /***
     * 创建cs模版
     * @param cha
     */
    public void createNCBCsTemplate(EwpChannels cha){
        WizarParams viewMap = cha.getViewMap();
        if (viewMap.getNCBCsFlag()){
            HashMap<TableItem, OldCallbackParams> newCallBackMap = viewMap.getNCBViewMap();
            createCsTemplate(cha, newCallBackMap);
        }
    }


    public void createOCBCsTemplate(EwpChannels cha){
        WizarParams viewMap = cha.getViewMap();
        if (viewMap.getOCBCsFlag()){
            HashMap<TableItem, OldCallbackParams> oldCallBackMap = viewMap.getOCBViewMap();
            createCsTemplate(cha, oldCallBackMap);
        }
    }

    private void createCsTemplate(EwpChannels cha, HashMap<TableItem, OldCallbackParams> callBackMap){
        String csPathStr = fileBuilder.getCsTemPath(projectPath, cha.cha_id);
        File csPath = new File(csPathStr);
        fileBuilder.createPath(csPath);

        ErlLogger.debug("csPath:"+csPath);
        ErlLogger.debug("cha.cha_entry:"+cha.cha_entry);

        String tmpFileName = fileBuilder.getPluginTmpPath(cha.cha_entry);
        String content = fileBuilder.getTemplateContent(cha.cha_entry, tmpFileName);
        content = content.replaceAll(NEWCHANNEL_CALLBACK_CHANNEL, cha.cha_id);

        Map<TableItem, OldCallbackParams> tmpMap = callBackMap;
        Iterator<Entry<TableItem, OldCallbackParams>> tmpIter = tmpMap.entrySet().iterator();
        while(tmpIter.hasNext()){
            Entry<TableItem, OldCallbackParams> tmpEnt = tmpIter.next();
            OldCallbackParams tmpView = tmpEnt.getValue();
            String tranCode = tmpView.tranCode;
            String csName = tmpView.viewName;
            content = content.replaceAll(NEWCHANNEL_CALLBACK_TRANCODE, tranCode);
            content = content.replaceAll(NEWCHANNEL_CALLBACK_CS, csName);
            File csTmp = new File(fileBuilder.getCsTemFile(csPathStr, cha.cha_id, csName, tranCode, ""));
            ErlLogger.debug("csTmp:"+csTmp);
            fileBuilder.doCreateTmp(csTmp, content);
        }
    }

    /***
     * 创建erlang source 模板
     * @param cha
     */
    //-define(CALLBACK_LIST, ['before_request', 'after_request']).
    public void createSrcTemplate(EwpChannels cha){
        WizarParams viewMap = cha.getViewMap();
        if (viewMap.getNCBSrcFlag()){
            HashMap<TableItem, OldCallbackParams> newCallBackMap = viewMap.getNCBViewMap();

            String srcPathStr = fileBuilder.getSrcFile(projectPath, cha.cha_id);
            ErlLogger.debug("csPath:"+srcPathStr);
            ErlLogger.debug("cha.cha_entry:"+cha.cha_entry);

            String tmpFileName = fileBuilder.getNewCallBackErlTmpPath();
            String content = fileBuilder.getTemplateContent(cha.cha_entry, tmpFileName);
            content = content.replaceAll(NEWCHANNEL_CALLBACK_CHANNEL, cha.cha_id);

            Pattern beforePattern = Pattern.compile(NEWCHANNEL_CALLBACK_ERL_BEFORE_RE);
            Matcher beforeMatcher = beforePattern.matcher(content);
            String beforeFunTemp = "";
            if (beforeMatcher.find())
                beforeFunTemp = beforeMatcher.group(0);

            ErlLogger.debug("beforeFunTemp find result:"+beforeFunTemp);
            //ErlLogger.debug("content:"+content);

            Pattern afterPattern = Pattern.compile(NEWCHANNEL_CALLBACK_ERL_AFTER_RE);
            Matcher afterMatcher = afterPattern.matcher(content);
            String afterFunTemp = "";
            if (afterMatcher.find())
                afterFunTemp = afterMatcher.group(0);
            ErlLogger.debug("afterFunTemp find result:"+afterFunTemp);

            String reBeforeFun = "";
            String reAfterFun = "";

            Map<TableItem, OldCallbackParams> tmpMap = newCallBackMap;
            Iterator<Entry<TableItem, OldCallbackParams>> tmpIter = tmpMap.entrySet().iterator();
            while(tmpIter.hasNext()){
                String tmpBeforeFun = beforeFunTemp;
                String tmpAfterFun = afterFunTemp;
                Entry<TableItem, OldCallbackParams> tmpEnt = tmpIter.next();
                OldCallbackParams tmpView = tmpEnt.getValue();
                String tranCode = D_MARK.concat(tmpView.tranCode).concat(D_MARK);

                tmpBeforeFun = tmpBeforeFun.replaceAll(NEWCHANNEL_CALLBACK_TRANCODE, tranCode);
                tmpBeforeFun = tmpBeforeFun.replaceAll("\\.", "\\;");
                reBeforeFun = reBeforeFun.concat(tmpBeforeFun);
                tmpAfterFun = tmpAfterFun.replaceAll(NEWCHANNEL_CALLBACK_TRANCODE, tranCode);
                tmpAfterFun = tmpAfterFun.replaceAll("\\.", "\\;");
                reAfterFun = reAfterFun.concat(tmpAfterFun);
            }
            reBeforeFun = reBeforeFun.concat(beforeFunTemp.replaceAll(NEWCHANNEL_CALLBACK_TRANCODE, ADAPTER_TRANCODE_VAR));
            reAfterFun = reAfterFun.concat(afterFunTemp.replaceAll(NEWCHANNEL_CALLBACK_TRANCODE, ADAPTER_TRANCODE_VAR));

            content = beforeMatcher.replaceFirst(reBeforeFun);
            afterMatcher = afterPattern.matcher(content);
            content = afterMatcher.replaceFirst(reAfterFun);

            File erlTemp = new File(srcPathStr);
            ErlLogger.debug("erlTemp:"+erlTemp);
            fileBuilder.doCreateTmp(erlTemp, content);
        }
    }

}

