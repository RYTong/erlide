package com.rytong.conf.newchannel.wizard;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.util.ChannelConfFileUtil;
import com.rytong.conf.util.FormatErlangSource;

public class ChannelAdapterTemplate {

    private static String SRC_PATH="/src/channels/";
    private static String TEMP_PATH = "/public/cs/channels/";

    private String projectPath="";
    private NewChaWizard wizard;
    private FormatErlangSource srcBuilder;
    private ChannelConfFileUtil fileBuilder;

    private static String CHANNEL_TEMPLATE_DESTINATION =  "/public/www/resource_dev/";
    private static String[] COMMON_CHANNEL_DIR = new String[]{"images", "css", "lua", "xhtml","channels"};
    private static String[] CHANNEL_TEMPLATE_DIR_LIST = new String[]{"xhtml", "css", "lua", "images", "json"};

    private static String[] COMMON_ROOT_DIRS = new String[] {"wp", "iphone", "android", "common"};
    private static String[] COMMON_BASE_DIRS = new String[] {"default"};
    private static String[] COMMON_CHANNELS = new String[] {"channels"};

    private static String COMMON_BASE_DIR = "default";
    private static String DRFAULT_PLATFORM = "common";
    private static String DRFAULT_BASE = "channels";
    private static String TEMPLATE_EXTENSION_JSON = "json";
    private static String TEMPLATE_EXTENSION_XHTML= "xhtml";


    public ChannelAdapterTemplate(NewChaWizard wizard) {
        // TODO Auto-generated constructor stub
        this.wizard = wizard;
        TextEditor tmpEditor = wizard.getTextEditor();
        IProject tmpProject = ((FileEditorInput) tmpEditor.getEditorInput()).getFile().getProject();
        projectPath = tmpProject.getLocation().toString();
        srcBuilder = new FormatErlangSource();
        fileBuilder = new ChannelConfFileUtil();
    }

    public ChannelAdapterTemplate ChannelAdapterTemplate(){
        return this;
    }


    public String getCsTemFile(String chaId, String name, String tranCode, String tail){
        if (chaId != null){
            if (!name.equalsIgnoreCase("") && name != null){
                return projectPath.concat(TEMP_PATH).concat(chaId).concat("/").concat(name).concat(".cs");
            } else
                return projectPath.concat(TEMP_PATH).concat(chaId).concat("/")
                        .concat(chaId).concat("_").concat(tranCode).concat(tail).concat(".cs");
        }
        return projectPath.concat(TEMP_PATH).concat(chaId).concat("temp").concat(tail).concat(".cs");
    }


    public String getTemFile(String path, String chaId, String name, String tranCode, String tail){
        if (chaId != null){
            if (!name.equalsIgnoreCase("") && name != null){
                return path.concat(name).concat(tail);
            } else
                return path.concat(chaId).concat("_").concat(tranCode).concat(tail);
        }
        return path.concat("temp").concat(tail);
    }

    private String formatPath(String path, String tranCode, String extension) {
        return path.concat("/").concat(extension).concat("/").concat(tranCode).concat(".").concat(extension);
    }

    private String getOffRootPath() {
        return projectPath.concat(CHANNEL_TEMPLATE_DESTINATION);
    }

    private File initialOffRootPath(){
        String rootPath = getOffRootPath();
        File offPath = new File(rootPath);
        if (!offPath.exists())
            offPath.mkdirs();
        return offPath;
    }

    /***
     * 生成离线资源文件
     * /public/www/resource_dev/["wp", "iphone", "android", "common"
     * @param cha
     */
    public void createOffTemplate(EwpChannels cha){
        WizarParams viewMap = cha.getViewMap();

        if (cha.cha_entry.equalsIgnoreCase(EwpChannels.CHANNEL_ADAPTER)&&viewMap.getOffFlag()){
            HashMap<TableItem, AdapterView> adapterMap = viewMap.getAdapterViewMap();

            File rootPath = initialOffRootPath();
            String channelPath = initial_dir(rootPath, cha);

            ErlLogger.debug("channelPath:"+channelPath);
            String xHtmlTmpFileName = fileBuilder.getXhtmlTmpPath();
            String xHtmlTmpcontent = fileBuilder.getTemplateContent(cha.cha_entry, xHtmlTmpFileName);

            String csTmpFileName = fileBuilder.getPluginTmpPath(cha.cha_entry);
            String csTmpContent = fileBuilder.getTemplateContent(cha.cha_entry, csTmpFileName);

            csTmpContent = csTmpContent.replaceAll("\\$channel", cha.cha_id);

            Map<TableItem, AdapterView> tmpMap = adapterMap;
            Iterator<Entry<TableItem, AdapterView>> tmpIter = tmpMap.entrySet().iterator();
            while(tmpIter.hasNext()){
                Entry<TableItem, AdapterView> tmpEnt = tmpIter.next();
                AdapterView tmpView = tmpEnt.getValue();
                String tranCode = tmpView.tranCode;
                File tmpJsonFile = new File(formatPath(channelPath, tranCode, TEMPLATE_EXTENSION_JSON));
                File tmXhtmlFile = new File(formatPath(channelPath, tranCode, TEMPLATE_EXTENSION_XHTML));
                ErlLogger.debug("tmpJsonFile:"+tmpJsonFile);
                ErlLogger.debug("tmXhtmlFile:"+tmXhtmlFile);

                createJsonTmp(tmpJsonFile, csTmpContent, tranCode);
                createXhtmlFile(tmXhtmlFile, xHtmlTmpcontent);
            }
        }
    }

    private void createJsonTmp(File jsonFile, String content, String tranCode){
        content = content.replaceAll("\\$trancode", tranCode);
        fileBuilder.doCreateTmp(jsonFile, content);
    }

    private void createXhtmlFile(File xhtmlFile, String content){
        fileBuilder.doCreateTmp(xhtmlFile, content);
    }

    private String initial_dir(File rootPath, EwpChannels cha){
        String platform = cha.getViewMap().getPlatform();
        String res = cha.getViewMap().getResolution();
        String chaId = cha.cha_id;

        initialRootDir(rootPath, chaId);
        ErlLogger.debug("p is empty:"+platform.replace(" ", "").isEmpty()+"|"+platform);
        if (platform.replace(" ", "").isEmpty()){
            platform = DRFAULT_PLATFORM;
            res="";
        }
        String rootPathStr = rootPath.getPath();
        ErlLogger.debug("rootPathStr:"+rootPathStr);
        String destPathStr = rootPathStr.concat("/").concat(platform);
            File platformDir = new File(destPathStr);
        if (!platformDir.exists()){
            platformDir.mkdirs();
            initialBaseDirs(platformDir.getName(), chaId);
        }
        String channelIdPath="";
        if (platform.equalsIgnoreCase(DRFAULT_PLATFORM)){
            channelIdPath = destPathStr.concat("/").concat(DRFAULT_BASE).concat("/").concat(cha.cha_id);
            ErlLogger.debug("channelIdPath 1:"+channelIdPath);
            initialChannelTmpDirs(channelIdPath);
        } else if ((!platform.equalsIgnoreCase(DRFAULT_PLATFORM)) && res.replace(" ", "").isEmpty()){
            channelIdPath = destPathStr.concat("/").concat(COMMON_BASE_DIR).concat("/").concat(DRFAULT_BASE).concat("/").concat(cha.cha_id);
            ErlLogger.debug("channelIdPath 2:"+channelIdPath);
            initialChannelTmpDirs(channelIdPath);
        } else {
            initialChannelsDirs(destPathStr.concat("/").concat(res).concat("/"), cha.cha_id);
            channelIdPath = destPathStr.concat("/").concat(res).concat("/").concat(DRFAULT_BASE).concat("/").concat(cha.cha_id);
            ErlLogger.debug("channelIdPath 3:"+channelIdPath);
            initialChannelTmpDirs(channelIdPath);
        }

        return channelIdPath;
    }

    private void initialRootDir(File rootPath, String chaId){
        String fileList = rootPath.getParentFile().toString();
        ErlLogger.debug("p path:"+rootPath.getParent());
        String rootPathStr = rootPath.getPath();
        for (String s: COMMON_ROOT_DIRS){
            String tmpFName = rootPathStr+"/"+s;
            File tmpF = new File(tmpFName);
            ErlLogger.debug("st:"+tmpF.exists()+"|"+tmpFName);
            if (!tmpF.exists())
                tmpF.mkdirs();
            if (!tmpF.getName().equalsIgnoreCase(DRFAULT_PLATFORM))
                initialBaseDirs(tmpFName, chaId);
            else
                initialChannelsDirs(tmpFName, chaId);
        }
    }
    private void initialBaseDirs(String dirName, String chaId){
        for (String cbd:COMMON_BASE_DIRS){
            String defaultFStr = dirName.concat("/").concat(cbd);
            File defaultF = new File (defaultFStr);
            if (!defaultF.exists()){
                defaultF.mkdirs();
            }
            initialChannelsDirs(defaultFStr, chaId);
        }
    }

    private void initialChannelsDirs(String baseDirStr, String chaId){
        for (String cbc: COMMON_CHANNEL_DIR){
            File defaultC = new File (baseDirStr.concat("/").concat(cbc));
            if (!defaultC.exists()){
                defaultC.mkdirs();
            }
            if (cbc.equalsIgnoreCase(DRFAULT_BASE)){
                String channelIdPath = baseDirStr.concat("/").concat(cbc).concat("/").concat(chaId);
                File channelIdFile = new File(channelIdPath);
                if (!channelIdFile.exists()){
                    channelIdFile.mkdirs();
                }
            }
        }
    }

    private void initialChannelTmpDirs(String channelDir){
        for(String ctdl:CHANNEL_TEMPLATE_DIR_LIST){
            File defaultC = new File (channelDir.concat("/").concat(ctdl));
            if (!defaultC.exists()){
                defaultC.mkdirs();
            }
        }
    }

    /***
     * 创建erlang 文件
     * @param cha
     */
    public void createAdpErlTemplate(EwpChannels cha){
        WizarParams viewMap = cha.getViewMap();
        if (cha.cha_entry.equalsIgnoreCase(EwpChannels.CHANNEL_ADAPTER)&&viewMap.getSrcFlag()){
            HashMap<TableItem, AdapterView> adapterMap = viewMap.getAdapterViewMap();

            String srcPath = fileBuilder.getSrcFile(projectPath,cha.cha_id);
            File srcFile = new File(srcPath);
            ErlLogger.debug("srcPath:".concat(srcPath));

            String tmpFileName = fileBuilder.getAdapterErlTmpPath();
            String content = srcBuilder.formatAdaoterErlSource(cha, tmpFileName);
            //ErlLogger.debug("content:"+content);
            fileBuilder.doCreateTmp(srcFile, content);
        }
    }

    /***
     * 创建cs模版
     * @param cha
     */
    public void createCsTemplate(EwpChannels cha){
        WizarParams viewMap = cha.getViewMap();
        if (viewMap.getCsFlag()){
            HashMap<TableItem, AdapterView> adapterMap = viewMap.getAdapterViewMap();

            String csPathStr = fileBuilder.getCsTemPath(projectPath, cha.cha_id);
            File csPath = new File(csPathStr);
            fileBuilder.createPath(csPath);

            ErlLogger.debug("csPath:"+csPath);

            String tmpFileName = fileBuilder.getPluginTmpPath(cha.cha_entry);
            String content = fileBuilder.getTemplateContent(cha.cha_entry, tmpFileName);

            content = content.replaceAll("\\$channel", cha.cha_id);

            Map<TableItem, AdapterView> tmpMap = adapterMap;
            Iterator<Entry<TableItem, AdapterView>> tmpIter = tmpMap.entrySet().iterator();
            while(tmpIter.hasNext()){
                Entry<TableItem, AdapterView> tmpEnt = tmpIter.next();
                TableItem tmpItem = tmpEnt.getKey();
                AdapterView tmpView = tmpEnt.getValue();
                String tranCode = tmpView.tranCode;
                String csName = tmpView.viewName;
                content = content.replaceAll("\\$trancode", tranCode);
                File csTmp = new File(fileBuilder.getCsTemFile(csPathStr, cha.cha_id, csName, tranCode, ""));
                ErlLogger.debug("csTmp:"+csTmp);
                fileBuilder.doCreateTmp(csTmp, content);
            }
        }
    }
}
