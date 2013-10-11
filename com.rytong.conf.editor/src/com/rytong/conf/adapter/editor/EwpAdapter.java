package com.rytong.conf.adapter.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.util.ConfParams;

public class EwpAdapter {


    private String name;
    private String host;
    private String protocol;
    private String port;
    private String return_type;
    private HashMap<String, EwpProcedure> childrenMap;

    private String oldName;

    private boolean status = true;
    private String errMsg = "";


    private static String sname = "name";
    private static String shost = "host";
    private static String sprotocol = "protocol";
    private static String sport = "port";
    private static String sreturn_type = "return_type";

    public EwpAdapter(){
        name = "";
        host = "";
        protocol = "http";
        port = "";
        return_type = "xml";
        childrenMap = new HashMap<String, EwpProcedure>();
        oldName = "";

    }

    public boolean has_children(){
        if (childrenMap.size() >0){
            return true;
        } else
            return false;
    }

    public void setValue(String id, String value){
        if(id == sname){
            this.name = value;
            this.oldName = value;
        }else if(id == sreturn_type)
            return_type = value;
        else if(id == shost)
            host = value;
        else if(id == sprotocol)
            protocol = value;
        else if(id == sport)
            port = value;
        else{
            ErlLogger.debug("error params!");
        }
    }

    public boolean checkNeededValue(){
        if(name.replace(" ", "").isEmpty()||host.replace(" ", "").isEmpty())
            return false;
        else
            return true;
    }

    public void setStatus(){
        status = true;
    }

    public void setErrStatus(){
        status = false;
    }

    public boolean getStatus(){
        return status;
    }

    public void setErrMsg(String msg){
        errMsg = msg;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHost(String host){
        this.host = host;
    }

    public void setProtocol(String protocol){
        this.protocol = protocol;
    }

    public void setPort(String port){
        this.port = port;
    }

    public void setReturnType(String return_type){
        this.return_type = return_type;
    }

    public void setChildren(EwpProcedure tmpProcedure){
        String adapterName = tmpProcedure.getAdapter();
        if (adapterName.equalsIgnoreCase(name)){
            childrenMap.put(tmpProcedure.getId(), tmpProcedure);
        }
    }

    public void removeChildren(String id){
        childrenMap.remove(id);
    }

    public void refreshChildren(EwpProcedure tmpProcedure){
        childrenMap.remove(tmpProcedure.getId());
        childrenMap.put(tmpProcedure.getId(), tmpProcedure);
    }

    public void setOldName(String oldName){
        this.oldName = oldName;
    }

    public String getName(){
        return name;
    }

    public String getHost(){
        return host;
    }

    public String getProtocol(){
        return protocol;
    }

    public String getPort(){
        return port;
    }

    public String getReturnType(){
        return return_type;
    }

    public EwpProcedure getChildren(String Id){
        return childrenMap.get(Id);
    }

    public Object[] getChildrenArray (){
        return childrenMap.values().toArray();
    }

    public List<EwpProcedure> getChildrenList (){
        ArrayList<EwpProcedure> list = new ArrayList<EwpProcedure>();
        for(EwpProcedure obj: childrenMap.values()){
            list.add(obj);
        }
        return list;
    }

    public OtpErlangTuple formAdapter(){
        //Name, Host, Port, Protocol, ReturnType
        OtpErlangObject[] request = new OtpErlangObject[5];
        request[0] = new OtpErlangList(name);
        request[1] = new OtpErlangList(host);
        request[2] = new OtpErlangList(port);
        request[3] = new OtpErlangAtom(protocol);
        request[4] = new OtpErlangAtom(return_type);
        return new OtpErlangTuple(request);
    }

    public OtpErlangTuple editAdapterName(){
        return editAdapterTuple(sname, name);
    }

    public OtpErlangTuple editAdapterHost(){
        return editAdapterTuple(shost, host);
    }

    public OtpErlangTuple editAdapterPort(){
        return editAdapterTupleIntValue(sport, port);
    }

    public OtpErlangTuple editAdapterProtocol(){
        return editAdapterTupleAtomValue(sprotocol, protocol);
    }

    public OtpErlangTuple editAdapterReturnType(){
        return editAdapterTupleAtomValue(sreturn_type, return_type);
    }

    private OtpErlangTuple editAdapterTuple(String Key, String Value){
        // {OldName, Name, RVal}
        //Id, App, Name, Entry, Views, Props, State
        OtpErlangObject[] request = new OtpErlangObject[3];
        request[0] = new OtpErlangList(oldName);
        request[1] = new OtpErlangAtom(Key);
        request[2] = new OtpErlangList(Value);
        return new OtpErlangTuple(request);
    }

    private OtpErlangTuple editAdapterTupleAtomValue(String Key, String Value){
        // {OldName, Name, RVal}
        //Id, App, Name, Entry, Views, Props, State
        OtpErlangObject[] request = new OtpErlangObject[3];
        request[0] = new OtpErlangList(oldName);
        request[1] = new OtpErlangAtom(Key);
        request[2] = new OtpErlangAtom(Value);
        return new OtpErlangTuple(request);
    }
    private OtpErlangTuple editAdapterTupleIntValue(String Key, String Value){
        // {OldName, Name, RVal}
        //Id, App, Name, Entry, Views, Props, State
        OtpErlangObject[] request = new OtpErlangObject[3];
        request[0] = new OtpErlangList(oldName);
        request[1] = new OtpErlangAtom(Key);
        request[2] = new OtpErlangInt(Integer.valueOf(Value));
        return new OtpErlangTuple(request);
    }

}
