package com.rytong.conf.editor.handler;

import java.util.HashMap;

import org.erlide.jinterface.ErlLogger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rytong.conf.adapter.editor.EwpAdapter;
import com.rytong.conf.adapter.editor.EwpProcedure;
import com.rytong.conf.adapter.editor.EwpAdpaterList;
import com.rytong.conf.editor.pages.EwpChannels;

public class AdapterHandler  extends DefaultHandler  {


    private Object tmpObj;
    public AdapterHandler(){
        ErlLogger.debug("conf default error handler!");
        tmpObj = new Object();
    }

    private Object nowObj;
    private static int flag=0;

    //存放所有的节点（这里的节点等于原来的节点+编号）以及它所对应的值

    //目前的节点
    private String currentElement = null;
    private Object beforeObj = null;
    //目前节点所对应的值
    private String currentValue = null;
    private EwpAdpaterList adpL;

    public void setResultObj(EwpAdpaterList adpL){
        this.adpL = adpL;
    }

    private EwpProcedure pro;
    private EwpAdapter adp;

    public void characters(char[] ch, int start, int length) throws SAXException {
        //get the value of current element
        currentValue =currentValue + new String(ch, start, length);
    }

    public void startElement(String uri, String localName, String eName,
            Attributes attr) throws SAXException {
        ErlLogger.debug("start element :"+eName);
        currentValue="";
        if(eName.equalsIgnoreCase("root")){
            flag=0;
            nowObj = tmpObj;
            //currentElement= "";
        } else if(eName.equalsIgnoreCase("adapter") && nowObj.equals(tmpObj)){
            ErlLogger.debug("adapter:");
            adp=new EwpAdapter();
            nowObj = adp;
            currentElement= eName;
            //newCollections();
        }else if(eName.equalsIgnoreCase("procedure")){
            ErlLogger.debug("procedure:");
            pro=new EwpProcedure();
            nowObj = pro;
            currentElement= eName;
            //newCollections();
        }else{
            //ErlLogger.debug("else:"+currentElement);
            ErlLogger.debug("else to:"+eName);
            currentElement= eName;
        }
    }

    public void endElement(String uri, String localName, String eName) throws SAXException {
        ErlLogger.debug("end element:"+eName);
        if (currentValue!=null){
            //ErlLogger.debug("currentValue:"+currentValue);
            if (nowObj.equals(pro)){
                //ErlLogger.debug("item object!");
                pro.setValue(currentElement, currentValue);
            } else if(nowObj.equals(adp)){
                adp.setValue(currentElement, currentValue);
            }
        };
        ErlLogger.debug(nowObj.equals(pro)+"|"+nowObj.equals(adp));
        if(eName.equalsIgnoreCase("procedure") && nowObj.equals(pro)){
            adpL.addProcedureList(pro);
            nowObj = tmpObj;
        } else if(eName.equalsIgnoreCase("adapter") && nowObj.equals(adp)){
            adpL.addAdapterList(adp);
            nowObj = tmpObj;
        }
    }

}
