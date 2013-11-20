package com.rytong.conf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.ChannelConfPlugin;
import com.rytong.conf.editor.pages.EwpChannels;

public class ChannelConfFileUtil {
    private String separator = System.getProperty("line.separator");
    private static String SRC_PATH="/src/channels/";
    private static String TEMP_PATH = "/public/cs/channels/";

    private static String channelAdapterTem = "channel_adapter_cs_template.tmp";
    private static String channelAdapterxHtmlTem = "channel_adapter_xHtml_template.tmp";
    private static String channelAdapterErlTem = "channel_adapter_erl_template.tmp";
    private static String newCallbackCsTmp = "channel_newCallback_cs_template.tmp";
    private static String newCallbackErlTmp = "channel_newCallback_erl_template.tmp";
    private static String channelCallbackCsTmp = "channel_callback_cs_template.tmp";

    public ChannelConfFileUtil ChannelConfFileUtil(){

        return this;
    }


    public String getCsTemPath(String projectPath, String chaId){
        if (chaId != null)
            return projectPath.concat(TEMP_PATH).concat(chaId).concat("/");
        return projectPath.concat(TEMP_PATH);
    }

    public String getCsTemFile(String path, String chaId, String name, String tranCode, String tail){
        if (chaId != null){
            if (!name.equalsIgnoreCase("") && name != null){
                return path.concat(name).concat(".cs");
            } else
                return path.concat(chaId).concat("_").concat(tranCode).concat(tail).concat(".cs");
        }
        return path.concat("temp").concat(tail).concat(".cs");
    }

    public String getSrcFile(String projectPath, String chaId){
        String path = getSrcPath(projectPath);
        createPath(new File(path));
        return path.concat(chaId).concat(".erl");
    }

    public String getSrcPath(String projectPath){
        return projectPath.concat(SRC_PATH);
    }

    public void createPath(File path){
        if (!path.exists())
            path.mkdirs();
    }


    public String getPluginTmpPath(String channelType){
        if (channelType.equalsIgnoreCase(EwpChannels.CHANNEL_ADAPTER))
            return channelAdapterTem;
        else if (channelType.equalsIgnoreCase(EwpChannels.NEW_CALLBACK))
            return newCallbackCsTmp;
        else
            return channelCallbackCsTmp;
    }

    public String getXhtmlTmpPath(){
        return channelAdapterxHtmlTem;
    }

    public String getAdapterErlTmpPath(){
        return channelAdapterErlTem;
    }

    public String getNewCallBackErlTmpPath(){
        return newCallbackErlTmp;
    }


    public void doCreateTmp( File tmpFile, String content){
        if (!tmpFile.exists()){
            try {
                FileWriter fw = new FileWriter(tmpFile);
                //ErlLogger.debug("File content:"+content);
                fw.write(content);
                fw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            ErlLogger.debug("The template file "+ tmpFile.getName() +"is exit!");
    }

    /**
     * 获得plugin中模版的位置
     * @param type
     * @return
     */
     public String getTemplateContent(String type, String tmpName){
         ErlLogger.debug("tmpFileName:-----"+tmpName);
         URL url = ChannelConfPlugin.getDefault().getBundle()
                 .getEntry("templates/"+type);
         //ErlLogger.debug("url:-----"+url.toString());
         String content="";
         try {
             File tempDir = new File(FileLocator.toFileURL(url).getFile());
             for (File f : tempDir.listFiles()) {
                 if (f.getName().equalsIgnoreCase(tmpName)){
                     //ErlLogger.debug("f:"+f.getAbsolutePath());
                     content = getContent(f);
                 }
                 //@Fix me
             }
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         return content;
     }

     /**
      * 读取文件内容
      * @param appFile
      * @return
      */
     public String getContent(File appFile){
         try{
             //ErlLogger.debug("App file path:"+appFile);
             if (appFile.isFile()){
                 //String content = "";
                 InputStreamReader read = new InputStreamReader(new FileInputStream(appFile),"utf-8");
                 BufferedReader reader = new BufferedReader(read);
                 StringBuilder sbuilder = new StringBuilder();
                 String tempString = null;
                 // 一次读入一行，直到读入null为文件结束
                 if ((tempString = reader.readLine()) != null){
                     sbuilder.append(tempString);
                     while ((tempString = reader.readLine()) != null) {
                         // 显示行号
                         sbuilder.append(separator).append(tempString);
                     }
                     //ErlLogger.debug("File content:"+content);
                     read.close();
                     return sbuilder.toString();
                 }
             }
             return "";
         }  catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         return "";
     }
}
