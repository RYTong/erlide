package com.rytong.conf.newchannel.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

public class WizarParams {

	public String sourceFlag = "";
	public String csFlag = "";
	public String offFileFlag = "";
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

	public void addOldView(String tranCode, String viewName){
		OldCallbackParams tmpOldV = new OldCallbackParams();
		tmpOldV.tranCode = tranCode;
		tmpOldV.viewName = viewName;
		oldList.add(tmpOldV);
	}
}
