package com.rytong.conf.newchannel.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;

public class WizarParams implements Cloneable{

    private Boolean sourceFlag = false;
    private Boolean csFlag = false;
    private Boolean offFileFlag = false;
    private String platForm = "";
    private String resolution = "";

    public List<String> offDir;
    public HashMap<TableItem, AdapterView> viewMap;
    public ArrayList<AdapterView> storeList;
    public ArrayList<OldCallbackParams> oldList;

    public WizarParams(){
        offDir = new ArrayList<String>();
        viewMap = new HashMap<TableItem, AdapterView>();
        storeList = new ArrayList<AdapterView>();
        oldList = new ArrayList<OldCallbackParams>();
    }

    public WizarParams clone(){
        WizarParams tmp = null;
        try {
            tmp =(WizarParams) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tmp;
    }


    public WizarParams WizarParams() {
        return this;
    }

    public void setSrcFlag(boolean flag){
        sourceFlag=flag;
    }

    public void setCsFlag(boolean flag){
        csFlag=flag;
    }

    public void setOffFlag(boolean flag){
        offFileFlag=flag;
    }

    public void setPlatform(String plat){
        platForm = plat;
    }

    public void setResolution(String res){
        resolution = res;
    }

    public boolean getSrcFlag(){
        return sourceFlag;
    }

    public boolean getCsFlag(){
        return csFlag;
    }

    public boolean getOffFlag(){
        return offFileFlag;
    }

    public String getPlatform(){
        return platForm;
    }

    public String getResolution(){
        return resolution;
    }

    public String[] getTrancodes(){
        Map<TableItem, AdapterView> tmpMap = viewMap;
        Iterator<Entry<TableItem, AdapterView>> tmpIter = tmpMap.entrySet().iterator();
        while(tmpIter.hasNext()){
            Entry<TableItem, AdapterView> tmpEnt = tmpIter.next();
            TableItem tmpItem = tmpEnt.getKey();
            //tmpItem
        }
        return new String[]{""};
    }

    public void removeMapAll(){
        viewMap = new HashMap<TableItem, AdapterView>();
    }

    public void addView(String id, String value){
        AdapterView tmpView = new AdapterView();
        tmpView.tranCode = id;
        tmpView.viewName = value;
        storeList.add(tmpView);
    }


    public ArrayList<OldCallbackParams> initialOldList(){
        ErlLogger.debug("list st:"+(oldList != null)+"| "+ (oldList.size() > 0));
        if ((oldList != null) && (oldList.size() > 0))
            return oldList;
        else {
            oldList = new ArrayList<OldCallbackParams>();
            return oldList;
        }
    }

    public OldCallbackParams getOldView(int index){
        return oldList.get(index);
    }

    public void addOldView(OldCallbackParams tmpObj){
        oldList.add(tmpObj);
    }
    public void addOldView(String tranCode, String viewName){
        OldCallbackParams tmpOldV = new OldCallbackParams();
        tmpOldV.tranCode = tranCode;
        tmpOldV.viewName = viewName;
        oldList.add(tmpOldV);
    }

    public void refreshOldView(int index, OldCallbackParams tmpObj){
        oldList.remove(index);
        //oldList.add(tmpObj);
        oldList.add(index, tmpObj);
    }
    public void refreshOldView(int index, String tranCode, String viewName){
        oldList.remove(index);
        OldCallbackParams tmpOldV = new OldCallbackParams();
        tmpOldV.tranCode = tranCode;
        tmpOldV.viewName = viewName;
        oldList.add(tmpOldV);
    }

    public void removeOldView(int index){
        oldList.remove(index);
    }

    public void clearOldView(){
        oldList = new ArrayList<OldCallbackParams>();
    }

    public HashMap<TableItem, AdapterView> getAdapterViewMap(){
        return viewMap;
    }
}
