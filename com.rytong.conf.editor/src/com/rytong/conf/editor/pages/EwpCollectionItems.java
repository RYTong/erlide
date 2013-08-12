package com.rytong.conf.editor.pages;

import org.erlide.jinterface.ErlLogger;

public class EwpCollectionItems {
	public String item_id;
	public String item_type;
	public String menu_order;
	private String type="item";

	public EwpCollectionItems(){
		ErlLogger.debug("EwpCollectionItems construction!");
	}

	public EwpCollectionItems getItemObj(){
		ErlLogger.debug("EwpCollectionItems new!");
		return this;
	}

	public void set_value(String id, String value){
		if(id=="item_id"){
			item_id=value;
		} else if(id=="item_type"){
			item_type=value;
		} else if(id=="menu_order"){
			menu_order=value;
		} else {
			ErlLogger.debug("ewp collection :"+id);
		}
	}

	public void set_item_id(String id){
		item_id=id;
	}

	public void set_type(String type){
		item_type=type;
	}

	public void set_order(String order){
		menu_order=order;
	}

}
