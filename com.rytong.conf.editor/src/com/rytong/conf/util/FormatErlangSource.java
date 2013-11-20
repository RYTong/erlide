package com.rytong.conf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.ChannelConfPlugin;
import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.newchannel.wizard.AdapterParams;
import com.rytong.conf.newchannel.wizard.AdapterView;
import com.rytong.conf.newchannel.wizard.WizarParams;

public class FormatErlangSource {
    private static String ADAPTER_FUNCTION = "channel_adapter_erl_function.tmp";
    private static final String ADAPTER_VARIABLE = "    $var = $getter(\"$key\", []),";
    private static final String REPLACE_VAR = "\\$var";
    private static final String REPLACE_GETTER = "\\$getter";
    private static final String REPLACE_KEY = "\\$key";
    private static final String REPLACE_GETTER_SYMBOL = "?";
    private static final String ADAPTER_TRANCODE = "tranCode";
    private static final String ADAPTER_TRANCODE_VAR = "TranCode";

    private static final String ADAPTER_REQUEST_PARAMS = "{'$key', $value}";
    private static final String ADAPTER_REQUEST_PARAMS_FORMAT =
            "                                      {'$key', $value}";
    private static final String ADAPTER_REQUEST_PARAMS_KEY = "\\$key";
    private static final String ADAPTER_REQUEST_PARAMS_VAL = "\\$value";
    private static final String ADAPTER_REQUEST_PARAMS_SYMBOL = ",";

    private static final String RE_ADAPTER_ERL_TRANCODE = "\\$trancode";
    private static final String RE_ADAPTER_ERL_MODULE = "\\$module";
    private static final String RE_ADAPTER_ERL_DATE = "\\$date";
    private static final String RE_ADAPTER_ERL_FUN = "\\$function";
    private static final String RE_ADAPTER_ERL_PARAMS = "\\$params";
    private static final String RE_ADAPTER_ERL_ADAPTER = "\\$adapter";
    private static final String RE_ADAPTER_ERL_PROCEDURE = "\\$procedure";
    private static final String RE_ADAPTER_ERL_KEYLIST = "\\$keylist";
    private static final String RE_ADAPTER_ERL_KEYLIST_LBRACKET = "[";
    private static final String RE_ADAPTER_ERL_KEYLIST_RBRACKET = "]";

    private String separator = System.getProperty("line.separator");
    private ChannelConfFileUtil fileBuilder;

    public String getDateStr(){
        DateFormat dateForm = new SimpleDateFormat( "yyyy-MM-dd");
        return dateForm.format(new Date());
    }

    public FormatErlangSource(){
        //TODO some thing
        fileBuilder = new ChannelConfFileUtil();
    }


    public String formatAdaoterErlSource(EwpChannels cha, String tmpFileName){

        ErlLogger.debug("separator:"+System.getProperty("line.separator"));
        String content = fileBuilder.getTemplateContent(cha.cha_entry, tmpFileName);

        content = content.replaceAll(RE_ADAPTER_ERL_MODULE, cha.cha_id);
        content = content.replaceAll(RE_ADAPTER_ERL_DATE, getDateStr());
        //ErlLogger.debug("date:"+getDateStr());

        //ErlLogger.debug("content:"+content);
        String funContent = formatAdapterFunction(cha, content);
        //ErlLogger.debug("funContent:"+funContent);
        return content.replaceAll(RE_ADAPTER_ERL_FUN, funContent);
    }

    public String formatAdapterFunction(EwpChannels cha, String content){
        WizarParams viewMap = cha.getViewMap();
        HashMap<TableItem, AdapterView> adapterMap = viewMap.getAdapterViewMap();
        Map<TableItem, AdapterView> tmpMap = adapterMap;
        Iterator<Entry<TableItem, AdapterView>> tmpIter = tmpMap.entrySet().iterator();
        String funContent = getFunction();
        String resultFunContent = "";
        String tmpContent = "";
        //ErlLogger.debug("funContent:"+funContent);

        while(tmpIter.hasNext()){
            tmpContent = funContent;
            Entry<TableItem, AdapterView> tmpEnt = tmpIter.next();
            //TableItem tmpItem = tmpEnt.getKey();
            AdapterView tmpView = tmpEnt.getValue();

            String initialKeyList = ADAPTER_REQUEST_PARAMS.replaceAll(ADAPTER_REQUEST_PARAMS_KEY, ADAPTER_TRANCODE);
            initialKeyList = initialKeyList.replaceAll(ADAPTER_REQUEST_PARAMS_VAL, ADAPTER_TRANCODE_VAR);
            String[] params = formatAdapterParamsList(tmpView, initialKeyList);

            tmpContent = tmpContent.replaceAll(RE_ADAPTER_ERL_PARAMS, params[0]);
            tmpContent = tmpContent.replaceAll(RE_ADAPTER_ERL_TRANCODE, tmpView.tranCode);
            tmpContent = tmpContent.replaceAll(RE_ADAPTER_ERL_ADAPTER, tmpView.adapter);
            tmpContent = tmpContent.replaceAll(RE_ADAPTER_ERL_PROCEDURE, tmpView.procedure);
            tmpContent = tmpContent.replaceAll(RE_ADAPTER_ERL_KEYLIST, params[1]);

            resultFunContent = resultFunContent.concat(tmpContent).concat(separator).concat(separator);
        }



        //        File csTmp = new File(getCsTemFile(csPathStr, cha.cha_id, csName, tranCode, ""));
        //        ErlLogger.debug("csTmp:"+csTmp);
        //        doCreateTmp(csTmp, content);
        return resultFunContent;
    }

    public String[] formatAdapterParamsList(AdapterView view, String keyList){

        String paramsContent = "";
        String tmpPContent = "";
        String tmpKContent = "";
        Iterator<AdapterParams> viewIter = view.paramsList.iterator();
        int i=1;
        while(viewIter.hasNext()){
            tmpPContent = ADAPTER_VARIABLE;
            AdapterParams tmpParams = viewIter.next();
            String key = tmpParams.getKey;
            String rKey = tmpParams.requestKey;
            String getType = tmpParams.GetFrom;
            String varName = formatVarName(key, i);
            tmpPContent = tmpPContent.replaceAll(REPLACE_VAR, varName);
            tmpPContent = tmpPContent.replaceAll(REPLACE_KEY, key);
            tmpPContent = tmpPContent.replaceAll(REPLACE_GETTER, formatGetterType(getType));
            paramsContent = paramsContent.concat(tmpPContent).concat(separator);

            tmpKContent = ADAPTER_REQUEST_PARAMS_FORMAT.replaceAll(ADAPTER_REQUEST_PARAMS_KEY, rKey);
            tmpKContent = tmpKContent.replaceAll(ADAPTER_REQUEST_PARAMS_VAL, varName);
            keyList = keyList.concat(ADAPTER_REQUEST_PARAMS_SYMBOL).concat(separator).concat(tmpKContent);
            i++;
            //ADAPTER_VARIABLE
        }
        return new String[] {paramsContent, keyList};
    }

    private String formatVarName(String key, int index){
        return firstLetterToUpper(key).concat(Integer.toString(index));
    }


    public static String firstLetterToUpper(String str){
        char[] array = str.toCharArray();
        array[0] -= 32;
        return String.valueOf(array);
    }


    private String formatGetterType(String getType){
        String result= getType;
        String[] getter_type = AdapterParams.GETTER_TYPE;
        for (int i=0; i<getter_type.length; i++){
            if (getter_type[i].equalsIgnoreCase(getType)){
                result = REPLACE_GETTER_SYMBOL.concat(getType);
                return result;
            }
        }
        return result;
    }

    private String getFunction(){
        return fileBuilder.getTemplateContent(EwpChannels.CHANNEL_ADAPTER, ADAPTER_FUNCTION);
    }




}
