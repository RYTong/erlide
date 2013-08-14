package com.rytong.conf.editor.pages;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;

public class EwpCollections {
	public String coll_id="";
	public String coll_app="";
	public String coll_name="";
	public String coll_url="";
	public String coll_userid="";
	public String coll_type="";
	public String coll_state="";


	public String type="collections";

	public EwpCollectionItems itemObj;
	public ArrayList<EwpCollectionItems> itemList=new ArrayList();
	public HashMap<String, EwpCollectionItems> itemsMap=new HashMap<String, EwpCollectionItems>();

	public EwpCollections EwpCollections(){
		ErlLogger.debug("EwpCollections new!");
		return this;
	}

	public EwpCollectionItems initialItem(){
		itemObj= new EwpCollectionItems();
		itemObj = itemObj.getItemObj();
		return itemObj;
	}

	public boolean checkValue(){
		if ((coll_id.isEmpty()||coll_id.replace(" ", "").isEmpty())||
				(coll_app.isEmpty()||coll_app.replace(" ", "").isEmpty())||
				(coll_name.isEmpty()||coll_name.replace(" ", "").isEmpty())||
				(coll_type.isEmpty()||coll_type.replace(" ", "").isEmpty())||
				(coll_state.isEmpty()||coll_state.replace(" ", "").isEmpty())
				)
			return false;
		else
			return true;
	}


	public void addItem(EwpCollectionItems itemObj){
		if (itemObj.item_id !=null)
			itemList.add(itemObj);
	}


	public void addItem(String id, EwpCollectionItems obj){
		itemsMap.put(id, obj);
	}

	public void set_value(String id, String value){
		if (id=="id"){
			coll_id=value;
		} else if(id=="app"){
			coll_app=value;
		} else if(id=="name"){
			coll_name=value;
		} else if(id=="url"){
			coll_url=value;
		} else if(id=="user_id"){
			coll_userid=value;
		} else if(id=="type"){
			coll_type=value;
		} else if(id=="state"){
			coll_state=value;
		}else {
			ErlLogger.debug("ewp collection :"+id);
		}
	}

	public String get_value(String id){
		if (id=="id"){
			return coll_id;
		} else if(id=="app"){
			return coll_app;
		} else if(id=="name"){
			return coll_name;
		} else if(id=="url"){
			return coll_url;
		} else if(id=="user_id"){
			return coll_userid;
		} else if(id=="type"){
			return coll_type;
		} else if(id=="state"){
			return coll_state;
		}else {
			ErlLogger.debug("ewp collection :"+id);
			return "";
		}
	}

	public void set_id(String id){
		coll_id=id;
	}

	public void set_app(String app){
		coll_app=app;
	}

	public void set_name(String name){
		coll_name=name;
	}

	public void set_url(String url){
		coll_url=url;
	}

	public void set_views(String views){
		coll_userid=views;
	}

	public void set_type(String type){
		coll_type=type;
	}

	public void set_state(String state){
		coll_state=state;
	}


	//-----------------@FIXME

	public String[] must_input={"id", "app", "name", "type", "state"};
	public String[] text={"id", "app", "name", "url", "user_id", "type", "state"};
	private Table table;
	HashMap<String, Integer> tableMap;


	public HashMap<String, Boolean> initial_label_flag(){
		HashMap<String, Boolean> labelMap = new HashMap<String, Boolean>();
		for (int i=0; i<text.length;i++){
			for(int f=0;f<must_input.length;f++){
				if(text[i]==must_input[f])
					labelMap.put(text[i], true);
			}
		}
		return labelMap;
	}


	public HashMap<String, String> storeMap=null;
	public void initial_store(){
		storeMap=new HashMap<String, String>();
	}

	public void store_value(String id, String value){
		if (storeMap.get(id)!=null){
			storeMap.put(id, value);
		}else {
			ErlLogger.debug("ewp collection :"+id);
		}
	}

	public void set_Wizard_Table(Table table,  HashMap<String, Integer> tableMap){
		this.table=table;
		this.tableMap=tableMap;
	}

	public OtpErlangList get_items_tuple(){

		if (table==null){
			ErlLogger.debug("table: null");
			return null;
		}
		else {
			ArrayList<OtpErlangObject> list = new ArrayList<OtpErlangObject>();
			TableItem[] tmp = table.getItems();
			//ErlLogger.debug("collection tmp.length:"+tmp.length);
			for (int i=0;i<tmp.length;i++){
				//ErlLogger.debug("collection tmp.length:"+tmp[i].getText());
				OtpErlangTuple tmpRe = formParams(tmp[i], i+1);
				if (tmpRe!=null)
					list.add(tmpRe);
			}
			if (list.size()!=0){
				OtpErlangObject[] result = new OtpErlangObject[list.size()];
				for(int i=0; i<list.size();i++)
					result[i]=list.get(i);
				return new OtpErlangList(result);
			}
			else
				return null;
		}
	}

	public OtpErlangTuple formParams(TableItem item, int order){
		if (item != null){
			EwpCollectionItems tmpItem= initialItem();
			String id = item.getText();
			Integer Type = tableMap.get(id);
			tmpItem.item_id=id;
			tmpItem.item_type=String.valueOf(Type);
			tmpItem.menu_order=String.valueOf(order);
			addItem(tmpItem);
			OtpErlangObject[] request = new OtpErlangObject[3];
			request[0]=new OtpErlangList(id);
			request[1]=new OtpErlangInt(Type);
			request[2]=new OtpErlangInt(order);
			return new OtpErlangTuple(request);
		}
		else
			return null;
	}


}
