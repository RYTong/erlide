package com.rytong.conf.adapter.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.util.ConfParams;

public class EwpProcedure {

    private String id;
    private String adapter;
    private String return_type;
    private boolean gen_log;
    private boolean gen_code;
    private boolean use_sample;
    private String sample_data;
    private String path;

    private String oldId;
    private String oldAdapter;

    private static String sid = "id";
    private static String sadapter = "adapter";
    private static String sreturn_type = "return_type";
    private static String sgen_log = "gen_log";
    private static String sgen_code = "gen_code";
    private static String suse_sample = "use_sample_data";
    private static String ssample_data ="data_sample";
    private static String spath = "path";
    private static String sParamters = "parameters";

    private HashMap<String, ConfParams> params ;



    public EwpProcedure(){
        params = new HashMap<String, ConfParams>();
        id = "";
        adapter = "";
        return_type = "xml";
        gen_log = false;
        gen_code = false;
        use_sample = false;
        sample_data = "";
        path = "";
    }

    public void setValue(String id, String value){
        if(id == "id"){
            this.id = value;
            this.oldId = value;
        }else if(id == "adapter"){
            adapter = value;
            oldAdapter = value;
        }else if(id == "return_type")
            return_type = value;
        else if(id == "path")
            path = value;
        else if(id == "gen_log")
            gen_log = check_type(value);
        else if(id == "gen_code")
            gen_code = check_type(value);
        else if(id == "use_sample_data")
            use_sample = check_type(value);
        else if (id == "data_sample")
            sample_data = value;
        else{
            if (id != "parameters" && !value.isEmpty()){
                params.put(id, new ConfParams(id, value));
            }
        }
    }

    private Boolean check_type(String str){
        if (str.equalsIgnoreCase("true"))
            return true;
        else
            return false;
    }

    public boolean checkNeededValue(){
        if(id.replace(" ", "").isEmpty()||
                adapter.replace(" ", "").isEmpty()||
                return_type.replace(" ", "").isEmpty())
            return false;
        else
            return true;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setAdapter(String adapter){
        this.adapter = adapter;
    }

    public void setoldId(String id){
        this.oldId = id;
    }

    public void setoldAdapter(String adapter){
        this.oldAdapter = adapter;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setReturnType(String return_type){
        this.return_type = return_type;
    }

    public void setLog(Boolean flag){
        this.gen_log = flag;
    }

    public void setCode(Boolean Code){
        this.gen_code = Code;
    }

    public void setSample(String sample){
        this.sample_data = sample;
    }

    public void setUseSample(Boolean sampleFlag){
        this.use_sample = sampleFlag;
    }


    public String getId(){
        return id;
    }

    public String getOldId(){
        return oldId;
    }

    public String getAdapter(){
        return adapter;
    }

    public String getOldAdapter(){
        return oldAdapter;
    }

    public String getReturnType(){
        return return_type;
    }

    public Boolean getLog(){
        return gen_log;
    }

    public Boolean getCode(){
        return gen_code;
    }

    public String getSample(){
        return sample_data;
    }

    public String getPath(){
        return path;
    }

    public Boolean getUseSample(){
        return use_sample;
    }

    public HashMap<String, ConfParams> getParams(){
        return params;
    }

    public void removeParam(String key){
        params.remove(key);
    }

    public void removeParams(){
        params.clear();
    }

    public void addParam(ConfParams obj){
        params.put(obj.getKey(), obj);
    }

    public void refreshParam(String key, ConfParams obj){
        params.remove(key);
        params.put(key, obj);
    }

    public ConfParams getParam(String key){
        return params.get(key);
    }

    public String getSid(){
        return sid;
    }

    public String getSadapter(){
        return sadapter;
    }

    public OtpErlangTuple formProcedure(){


        //Id, App, Name, Entry, Views, Props, State
        OtpErlangObject[] request = new OtpErlangObject[9];
        //{Id, Adapter, Path, Return_type, Code, Log, UseSample, SampleData}
        request[0] = new OtpErlangList(id);
        request[1] = new OtpErlangList(adapter);
        request[2] = new OtpErlangAtom(return_type);
        request[3] = new OtpErlangAtom(gen_log);
        request[4] = new OtpErlangAtom(gen_code);
        request[5] = new OtpErlangAtom(use_sample);
        request[6] = new OtpErlangList(sample_data);
        request[7] = new OtpErlangList(path);

        OtpErlangList props = get_props_tuple();
        if (props!=null)
            request[8]=props;
        else
            request[8]=new OtpErlangList();
        return new OtpErlangTuple(request);
    }

    public OtpErlangList get_props_tuple(){
        ArrayList<OtpErlangObject> list = new ArrayList<OtpErlangObject>();
        Map<String, ConfParams> map = params;
        Iterator<Entry<String, ConfParams>> adpiter = map.entrySet().iterator();
        while (adpiter.hasNext()) {
            Map.Entry entry = (Map.Entry) adpiter.next();
            ConfParams value = (ConfParams) entry.getValue();
            //ErlLogger.debug("cha key:"+key);
            OtpErlangTuple tmpRe = formParamsStr(value.getKey(), value.getValue());
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

    public OtpErlangTuple formParamsStr(String key, String value){
        OtpErlangObject[] request = new OtpErlangObject[2];
        // we assume that all the key should be atom.
        request[0]=new OtpErlangList(key);
        request[1]=new OtpErlangList(value);
        return new OtpErlangTuple(request);
    }

    // format edit params
    public OtpErlangTuple editAdapterId(){
        return editAdapterTuple(sid, id);
    }

    public OtpErlangTuple editAdapterAdapter(){
        return editAdapterTuple(sadapter, adapter);
    }
    public OtpErlangTuple editAdapterPath(){
        return editAdapterTuple(spath, path);
    }

    public OtpErlangTuple editAdapterReturnType(){
        return editAdapterTuple(sreturn_type, return_type);
    }
    public OtpErlangTuple editAdapterLog(){
        return editAdapterTuple(sgen_log, gen_log);
    }
    public OtpErlangTuple editAdapterCode(){
        return editAdapterTuple(sgen_code, gen_code);
    }
    public OtpErlangTuple editAdapterUseSample(){
        return editAdapterTuple(suse_sample, use_sample);
    }
    public OtpErlangTuple editAdapterSample(){
        return editAdapterTuple(ssample_data, sample_data);
    }

    public OtpErlangTuple editParameters(){
        //params
        OtpErlangObject[] request = new OtpErlangObject[4];

        request[0] = new OtpErlangList(oldId);
        request[1] = new OtpErlangList(oldAdapter);
        request[2] = new OtpErlangAtom(sParamters);

        OtpErlangList props = get_props_tuple();
        if (props!=null)
            request[3]=props;
        else
            request[3]=new OtpErlangList();

        return new OtpErlangTuple(request);
    }

    private OtpErlangTuple editAdapterTuple(String Key, String Value){

        // {Id, Adapter, RId, RVal}
        //Id, App, Name, Entry, Views, Props, State
        OtpErlangObject[] request = new OtpErlangObject[4];

        request[0] = new OtpErlangList(oldId);
        request[1] = new OtpErlangList(oldAdapter);
        request[2] = new OtpErlangAtom(Key);
        request[3] = new OtpErlangList(Value);

        return new OtpErlangTuple(request);
    }

    private OtpErlangTuple editAdapterTuple(String Key, boolean Value){

        // {Id, Adapter, RId, RVal}
        //Id, App, Name, Entry, Views, Props, State
        OtpErlangObject[] request = new OtpErlangObject[4];

        request[0] = new OtpErlangList(id);
        request[1] = new OtpErlangList(adapter);
        request[2] = new OtpErlangAtom(Key);
        request[3] = new OtpErlangAtom(Value);

        return new OtpErlangTuple(request);
    }


}
