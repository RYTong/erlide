package com.rytong.conf.editor.pages;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

public class EwpChannels {
	public String cha_id="";
	public String cha_app="";
	public String cha_name="";
	public String cha_entry="";
	public String cha_views="";
	//public String cha_props;
	public String cha_state="";
	public String type="channels";

	private HashMap<String, String> cha_props = new HashMap<String, String>();


	public EwpChannels EwpChannels(){
		ErlLogger.debug("EwpChannels new!");
		return this;
	}

	public void set_value(String id, String value){
		if (id=="id"){
			cha_id=value;
		} else if(id=="app"){
			cha_app=value;
		} else if(id=="name"){
			cha_name=value;
		} else if(id=="entry"){
			cha_entry=value;
		} else if(id=="views"){
			cha_views=value;
		} else if(id=="state"){
			cha_state=value;
		}else {
			cha_props.put(id, value);
			ErlLogger.debug("ewp channel :"+id+"  value:"+value);
		}
	}


	public void set_id(String id){
		cha_id=id;
	}

	public void set_app(String app){
		cha_app=app;
	}

	public void set_name(String name){
		cha_name=name;
	}

	public void set_entry(String entry){
		cha_entry=entry;
	}

	public void set_views(String views){
		cha_views=views;
	}



	public void set_state(String state){
		cha_state=state;
	}



}
