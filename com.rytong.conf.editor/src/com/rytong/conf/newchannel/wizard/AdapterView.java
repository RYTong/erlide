package com.rytong.conf.newchannel.wizard;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

public class AdapterView {
	public String viewName = "";
	public String tranCode = "";
	public String adapter = "";
	public String procedure = "";
	public String callBack = "";

	//public HashMap<TableItem, ViewParams> params;
	public ArrayList<AdapterParams> paramsList;

	public AdapterView(){
		//params = new HashMap<TableItem, ViewParams>();
		paramsList = new ArrayList<AdapterParams>();
	}

	public AdapterView AdapterView(){
		return this;
	}


}
