package com.rytong.conf.util;

import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.editor.pages.EwpCollectionItems;
import com.rytong.conf.editor.pages.EwpCollections;
import com.rytong.conf.editor.pages.SelectedItem;

public class ChannelConfUtil {

	public ChannelConfUtil ChannelConfUtil(){
		return this;
	}


	// format the parmas for the synchronization of conf file
	public OtpErlangTuple formParams(String oldParent, String nowParent, String id, String type, String index){

		OtpErlangObject[] request = new OtpErlangObject[5];
		request[0]=new OtpErlangList(oldParent);
		request[1]=new OtpErlangList(nowParent);
		request[2]=new OtpErlangList(id);
		request[3]=new OtpErlangList(type);

		request[4]=new OtpErlangList(index);
		return new OtpErlangTuple(request);
	}


	public String checkObjectType(Object obj) {
		if (obj instanceof EwpCollections){
			ErlLogger.debug("-------------------EwpCollections type:"+(obj instanceof EwpCollections));
			return "0";
		} else if(obj instanceof EwpChannels){
			ErlLogger.debug("-------------------EwpChannels type:"+(obj instanceof EwpChannels));
			return "1";
		} else {
			ErlLogger.debug("-------------------EwpCollectionItems type:"+(obj instanceof EwpCollectionItems));
			ErlLogger.debug("null type:"+(obj == null));
			return "3";
		}
	}

	public String[] returnText(SelectedItem selectedObj) {
		if (selectedObj.type=="0"){
			EwpCollections coll = (EwpCollections) selectedObj.obj;
			return new String [] {coll.coll_id, coll.coll_name};
		} else if(selectedObj.type=="1"){
			EwpChannels chaObj = (EwpChannels) selectedObj.obj;
			return new String []{chaObj.cha_id, chaObj.cha_name};
		} else {
			return new String []{"",""};
		}
	}

	public OtpErlangTuple formAddCollParams(String selectId, EwpCollections collobj){
		OtpErlangObject[] request = new OtpErlangObject[9];
		request[0] = new OtpErlangList(selectId);
		request[1]=new OtpErlangList(collobj.coll_id);
		request[2]=new OtpErlangList(collobj.coll_app);
		request[3]=new OtpErlangList(collobj.coll_name);
		request[4]=new OtpErlangList(collobj.coll_url);

		request[5]=new OtpErlangList(collobj.coll_userid);
		request[6]=new OtpErlangList(collobj.coll_type);
		request[7]=new OtpErlangList(collobj.coll_state);
		OtpErlangList items = collobj.get_items_tuple();
		if (items!=null)
			request[8]=items;
		else
			request[8]=new OtpErlangList();
		return new OtpErlangTuple(request);
	}

	public OtpErlangTuple formAddChaParams(String selectId, EwpChannels cha){

		//Id, App, Name, Entry, Views, Props, State
		OtpErlangObject[] request = new OtpErlangObject[8];
		request[0] = new OtpErlangList(selectId);
		request[1]=new OtpErlangList(cha.cha_id);
		request[2]=new OtpErlangList(cha.cha_app);
		request[3]=new OtpErlangList(cha.cha_name);
		request[4]=new OtpErlangAtom(cha.cha_entry);
		OtpErlangList Views = cha.get_views_list();
		if (Views!=null)
			request[5]=Views;
		else
			request[5]=new OtpErlangList();
		request[6]=new OtpErlangInt(Integer.valueOf(cha.cha_state));

		OtpErlangList props = cha.get_props_tuple();
		if (props!=null)
			request[7]=props;
		else
			request[7]=new OtpErlangList();
		return new OtpErlangTuple(request);
	}

}
