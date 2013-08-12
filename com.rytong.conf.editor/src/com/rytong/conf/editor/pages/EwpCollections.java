package com.rytong.conf.editor.pages;

import java.util.ArrayList;
import java.util.HashMap;

import org.erlide.jinterface.ErlLogger;

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




}
