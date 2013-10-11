package com.rytong.conf.newchannel.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;

public class WizarParams implements Cloneable{

	public Boolean sourceFlag = false;
	public Boolean csFlag = false;
	public Boolean offFileFlag = false;
	public String plateForm = "";
	public String resoulction = "";

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
}
