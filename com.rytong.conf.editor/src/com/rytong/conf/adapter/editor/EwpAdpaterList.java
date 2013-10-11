package com.rytong.conf.adapter.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.erlide.jinterface.ErlLogger;

public class EwpAdpaterList {
    private ArrayList<EwpProcedure> procedureList;
    private ArrayList<String> procedureIndexList;
    //private ArrayList<EwpAdapter> adapterList;
    private HashMap<String, EwpAdapter> adapaterMap;


    public EwpAdpaterList (){
        procedureList = new ArrayList<EwpProcedure>();
        //adapterList = new ArrayList<EwpAdapter>();
        adapaterMap = new HashMap<String, EwpAdapter>();
        procedureIndexList = new ArrayList<String>();
    }

    public void addProcedureList(EwpProcedure tmpProcedure){
        procedureList.add( tmpProcedure);
        procedureIndexList.add(createIndex(tmpProcedure.getId(), tmpProcedure.getAdapter()));
        EwpAdapter tmpAdapter = adapaterMap.get(tmpProcedure.getAdapter());
        if (tmpAdapter != null){
            ErlLogger.debug("father adapter:"+tmpAdapter.getName());
            tmpAdapter.setChildren(tmpProcedure);
        }
    }

    public void refreshProcedureId(EwpProcedure tmpProcedure){
        String oldId = tmpProcedure.getOldId();
        String newId = tmpProcedure.getId();
        String adapterName = tmpProcedure.getAdapter();
        Iterator<EwpProcedure> tmp = procedureList.iterator();
        while(tmp.hasNext()){
            EwpProcedure tmpPro = tmp.next();
            String tmpId = tmpPro.getId();
            String tmpAdapterName = tmpPro.getAdapter();
            if(tmpId.equalsIgnoreCase(oldId) && tmpAdapterName.equalsIgnoreCase(adapterName)){
                procedureList.remove(tmpPro);
                procedureList.add(tmpProcedure);
                procedureIndexList.remove(createIndex(oldId, adapterName));
                procedureIndexList.add(createIndex(newId, adapterName));
                break;
            }
        }
        EwpAdapter adpter = adapaterMap.get(adapterName);
        if (adpter != null){
            ErlLogger.debug("father adapter:"+adpter.getName());
            adpter.removeChildren(adapterName);
            adpter.setChildren(tmpProcedure);
        }
    }

    public void refreshProcedureAdapter(EwpProcedure tmpProcedure){
        String id = tmpProcedure.getId();
        String oldAdapterName = tmpProcedure.getOldAdapter();
        String newAdapterName = tmpProcedure.getAdapter();
        Iterator<EwpProcedure> tmp = procedureList.iterator();
        while(tmp.hasNext()){
            EwpProcedure tmpPro = tmp.next();
            String tmpId = tmpPro.getId();
            String tmpAdapterName = tmpPro.getAdapter();
            if(tmpId.equalsIgnoreCase(id) && tmpAdapterName.equalsIgnoreCase(oldAdapterName)){
                ErlLogger.debug("refresh procedure!");
                procedureList.remove(tmpPro);
                procedureList.add(tmpProcedure);
                procedureIndexList.remove(createIndex(tmpId, tmpAdapterName));
                procedureIndexList.add(createIndex(id, newAdapterName));
                break;
            }
        }
        EwpAdapter oldAdapter = adapaterMap.get(oldAdapterName);
        EwpAdapter newAdapter = adapaterMap.get(newAdapterName);
        if (oldAdapter != null){
            ErlLogger.debug("old father adapter:"+oldAdapter.getName());
            oldAdapter.removeChildren(id);
        }
        if (newAdapter != null){
            ErlLogger.debug("father adapter:"+newAdapter.getName());
            newAdapter.setChildren(tmpProcedure);
        }
    }

    public void refreshProcedureOther(EwpProcedure tmpProcedure){
        String id = tmpProcedure.getId();
        String adapterName = tmpProcedure.getAdapter();
        Iterator<EwpProcedure> tmp = procedureList.iterator();
        while(tmp.hasNext()){
            EwpProcedure tmpPro = tmp.next();
            String tmpId = tmpPro.getId();
            String tmpAdapterName = tmpPro.getAdapter();
            if(tmpId.equalsIgnoreCase(id) && tmpAdapterName.equalsIgnoreCase(adapterName)){
                procedureList.remove(tmpPro);
                procedureList.add(tmpProcedure);
                break;
            }
        }
        EwpAdapter adpter = adapaterMap.get(adapterName);
        if (adpter != null){
            ErlLogger.debug("father adapter:"+adpter.getName());
            adpter.refreshChildren(tmpProcedure);
        }
    }


    public List<EwpProcedure> getProcedureList(EwpAdapter tmpAdapter){
        return tmpAdapter.getChildrenList();
    }

    public Object[] getProcedureArray(EwpAdapter tmpAdapter){
        return tmpAdapter.getChildrenArray();
    }

    public void addAdapterList(EwpAdapter tmpAdapter){
        ErlLogger.debug("tmpAdapter name :"+tmpAdapter.getName());
        EwpAdapter tmpAdp = adapaterMap.get(tmpAdapter.getName());
        if (tmpAdp == null){
            Iterator<EwpProcedure> tmp = procedureList.iterator();
            while(tmp.hasNext()){
                tmpAdapter.setChildren(tmp.next());
            }
            adapaterMap.put(tmpAdapter.getName(), tmpAdapter);
        } else {
            tmpAdp.setErrStatus();
            tmpAdp.setErrMsg("");
        }
    }

    public List<EwpAdapter> getAdapterList(){
        ArrayList<EwpAdapter> list = new ArrayList<EwpAdapter>();
        for(EwpAdapter obj: adapaterMap.values()){
            list.add(obj);
        }
        return list;
    }

    public Object[] getAdapterArray(){
        return adapaterMap.values().toArray();
    }


    public boolean checkExistedAdapter(String adpName){
        EwpAdapter tmpAdapter = adapaterMap.get(adpName);
        if (tmpAdapter != null){
            return true;
        } else
            return false;
    }

    public boolean checkExistedProcedure(String proId, String adapterName){
        if (procedureIndexList.contains(createIndex(proId, adapterName)))
            return true;
        else
            return false;
    }

    private String createIndex(String proId, String adapterName){
        return proId+"|"+ adapterName;
    }

}
