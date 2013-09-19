package com.rytong.conf.editor.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.newchannel.wizard.AdapterView;
import com.rytong.conf.newchannel.wizard.WizarParams;

public class EwpChannels implements Cloneable{
	public String cha_id="";
	public String cha_app="";
	public String cha_name="";
	public String cha_entry="";
	public String cha_views="";
	//public String cha_props;
	public String cha_state="";
	public String type="channels";
	public String csFlag = "";
	public WizarParams add_view = new WizarParams();


	public static String CHANNEL_ADAPTER = "channel_adapter";
	public static String NEW_CALLBACK = "new_callback";
	public static String CHANNEL_CALLBACK = "channel_callback";

	public HashMap<String, String> cha_props = new HashMap<String, String>();

	public EwpChannels EwpChannels(){
		ErlLogger.debug("EwpChannels new!");
		return this;
	}

	public EwpChannels clone(){
		EwpChannels cha = null;
		try {
			cha =(EwpChannels) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cha;
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


	public static int get_entry_index(String flag){
		if (flag .equalsIgnoreCase(EwpChannels.CHANNEL_ADAPTER))
			return 0;
		else if(flag.equalsIgnoreCase(EwpChannels.NEW_CALLBACK))
			return 1;
		else
			return 2;
	}

	private static String[] methodStr = new String[] {"method", "post"};
	private static String[] encryptStr = new String[] {"encrypt_flag", "1"};
	public void initial_props(){
		cha_props.put(methodStr[0], methodStr[1]);
		cha_props.put(encryptStr[0], encryptStr[1]);
	}

	public boolean check_props(String Key){
		if (Key.equalsIgnoreCase(methodStr[0])||Key.equalsIgnoreCase(encryptStr[0]))
			return true;
		else
			return false;
	}

	public OtpErlangList get_views_list(){
		HashMap<TableItem, AdapterView> viewMap = add_view.viewMap;
		ArrayList<OtpErlangObject> list = new ArrayList<OtpErlangObject>();
		Map<TableItem, AdapterView> map = viewMap;
		Iterator<Entry<TableItem, AdapterView>> chaiter = map.entrySet().iterator();

		while (chaiter.hasNext()) {
			Map.Entry<TableItem, AdapterView> entry = chaiter.next();
			//String key = (String) entry.getKey();
			AdapterView views =  entry.getValue();
			//ErlLogger.debug("cha key:"+key);
			OtpErlangTuple tmpRe = formParamsStr(views.tranCode, views.viewName);
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

	public OtpErlangList get_props_tuple(){
		ArrayList<OtpErlangObject> list = new ArrayList<OtpErlangObject>();
		Map<String, String> map = cha_props;
		Iterator<Entry<String, String>> chaiter = map.entrySet().iterator();

		while (chaiter.hasNext()) {
			Map.Entry entry = (Map.Entry) chaiter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			//ErlLogger.debug("cha key:"+key);
			OtpErlangTuple tmpRe = formParams(key, value);
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


	public OtpErlangTuple formParams(String key, String value){
		OtpErlangObject[] request = new OtpErlangObject[2];
		// we assume that all the key should be atom.
		request[0]=new OtpErlangAtom(key);
		request[1]=new OtpErlangList(value);
		return new OtpErlangTuple(request);
	}

	public OtpErlangTuple formParamsStr(String key, String value){
		OtpErlangObject[] request = new OtpErlangObject[2];
		// we assume that all the key should be atom.
		request[0]=new OtpErlangList(key);
		request[1]=new OtpErlangList(value);
		return new OtpErlangTuple(request);
	}

	public boolean checkValue(){
		if (cha_id.replace(" ", "").isEmpty()||
				cha_app.replace(" ", "").isEmpty() ||
				cha_name.replace(" ", "").isEmpty())
			return false;
		else
			return true;
	}

}
