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

    private Boolean ad_srcFlag = false;
    private Boolean ad_csFlag = false;
    private Boolean ad_offFlag = false;

    private Boolean nc_srcFlag = false;
    private Boolean nc_csFlag = false;

    private Boolean oc_srcFlag = false;
    private Boolean oc_csFlag = false;

    private String platForm = "";
    private String resolution = "";

    public List<String> offDir;
    private HashMap<TableItem, AdapterView> viewMap;
    public ArrayList<AdapterView> storeList;
    public HashMap<TableItem, OldCallbackParams> oldViewMap;
    public HashMap<TableItem, OldCallbackParams> newViewMap;

    public WizarParams(){
        offDir = new ArrayList<String>();
        viewMap = new HashMap<TableItem, AdapterView>();
        storeList = new ArrayList<AdapterView>();
        oldViewMap = new HashMap<TableItem, OldCallbackParams>();
        newViewMap = new HashMap<TableItem, OldCallbackParams>();
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
        ad_srcFlag=flag;
    }

    public void setCsFlag(boolean flag){
        ad_csFlag=flag;
    }

    public void setOffFlag(boolean flag){
        ad_offFlag=flag;
    }

    public void setPlatform(String plat){
        platForm = plat;
    }

    public void setResolution(String res){
        resolution = res;
    }

    public void setNCBSrcFlag(boolean flag){
        nc_srcFlag = flag;
    }

    public void setNCBCsFlag(boolean flag){
        nc_csFlag = flag;
    }

    public void setOCBSrcFlag(boolean flag){
        oc_srcFlag = flag;
    }

    public void setOCBCsFlag(boolean flag){
        oc_srcFlag = flag;
    }

    public boolean getSrcFlag(){
        return ad_srcFlag;
    }

    public boolean getCsFlag(){
        return ad_csFlag;
    }

    public boolean getOffFlag(){
        return ad_offFlag;
    }

    public String getPlatform(){
        return platForm;
    }

    public String getResolution(){
        return resolution;
    }

    public boolean getNCBSrcFlag(){
        return nc_srcFlag;
    }

    public boolean getNCBCsFlag(){
        return nc_csFlag;
    }

    public boolean getOCBSrcFlag(){
        return oc_srcFlag;
    }

    public boolean getOCBCsFlag(){
        return oc_srcFlag;
    }

    public HashMap<TableItem, OldCallbackParams> getNCBViewMap(){
        return newViewMap;
    }

    public HashMap<TableItem, OldCallbackParams> getOCBViewMap(){
        return oldViewMap;
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

    /***
     * used for new call back
     * @return
     */
    public HashMap<TableItem, OldCallbackParams> initialNewList(){
        ErlLogger.debug("list st:"+(newViewMap != null)+"| "+ (newViewMap.size() > 0));
        if ((newViewMap != null) && (newViewMap.size() > 0))
            return newViewMap;
        else {
            newViewMap = new HashMap<TableItem, OldCallbackParams>();
            return newViewMap;
        }
    }


    public OldCallbackParams getNewView(TableItem tmpItem){
        return newViewMap.get(tmpItem);
    }

    public void addNewView(TableItem tmpItem, OldCallbackParams tmpObj){
        newViewMap.put(tmpItem, tmpObj);
    }
    public void addNewView(TableItem tmpItem, String tranCode, String viewName){
        OldCallbackParams tmpOldV = new OldCallbackParams();
        tmpOldV.tranCode = tranCode;
        tmpOldV.viewName = viewName;
        newViewMap.put(tmpItem, tmpOldV);
    }

    public void refreshNewView(TableItem tmpItem, OldCallbackParams tmpObj){
        newViewMap.remove(tmpItem);
        //oldList.add(tmpObj);
        newViewMap.put(tmpItem, tmpObj);
    }
    public void refreshNewView(TableItem tmpItem, String tranCode, String viewName){
        newViewMap.remove(tmpItem);
        OldCallbackParams tmpOldV = new OldCallbackParams();
        tmpOldV.tranCode = tranCode;
        tmpOldV.viewName = viewName;
        newViewMap.put(tmpItem, tmpOldV);
    }

    public void removeNewView(TableItem tmpItem){
        newViewMap.remove(tmpItem);
    }

    public void clearNewView(){
        newViewMap = new HashMap<TableItem, OldCallbackParams>();
    }

    /***
     * used for old call back
     * @return
     */
    public HashMap<TableItem, OldCallbackParams> initialOldList(){
        ErlLogger.debug("list st:"+(oldViewMap != null)+"| "+ (oldViewMap.size() > 0));
        if ((oldViewMap != null) && (oldViewMap.size() > 0))
            return oldViewMap;
        else {
            oldViewMap = new HashMap<TableItem, OldCallbackParams>();
            return oldViewMap;
        }
    }

    public OldCallbackParams getOldView(TableItem tmpItem){
        return oldViewMap.get(tmpItem);
    }

    public void addOldView(TableItem tmpItem, OldCallbackParams tmpObj){
        oldViewMap.put(tmpItem, tmpObj);
    }
    public void addOldView(TableItem tmpItem, String tranCode, String viewName){
        OldCallbackParams tmpOldV = new OldCallbackParams();
        tmpOldV.tranCode = tranCode;
        tmpOldV.viewName = viewName;
        oldViewMap.put(tmpItem, tmpOldV);
    }

    public void refreshOldView(TableItem tmpItem, OldCallbackParams tmpObj){
        oldViewMap.remove(tmpItem);
        //oldList.add(tmpObj);
        oldViewMap.put(tmpItem, tmpObj);
    }
    public void refreshOldView(TableItem tmpItem, String tranCode, String viewName){
        oldViewMap.remove(tmpItem);
        OldCallbackParams tmpOldV = new OldCallbackParams();
        tmpOldV.tranCode = tranCode;
        tmpOldV.viewName = viewName;
        oldViewMap.put(tmpItem, tmpOldV);
    }

    public void removeOldView(TableItem tmpItem){
        oldViewMap.remove(tmpItem);
    }

    public void clearOldView(){
        oldViewMap = new HashMap<TableItem, OldCallbackParams>();
    }

    public HashMap<TableItem, AdapterView> getAdapterViewMap(){
        return viewMap;
    }

    public void addAdapterView(TableItem tmpItem, AdapterView tmpView){
        viewMap.put(tmpItem, tmpView);
    }

    public void removeAdapterView(TableItem tmpItem){
        viewMap.remove(tmpItem);
    }

    public AdapterView getAdapterView(TableItem tmpItem){
        return viewMap.get(tmpItem);
    }
}
