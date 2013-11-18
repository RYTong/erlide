package com.rytong.conf.newchannel.wizard;

public class AdapterParams {
    public static final String[] GETTER_TYPE = {"param", "arg", "session"};
    public String getKey = "";
    public String requestKey = "";
    // default is arg,param, session
    public String GetFrom = "";


    public AdapterParams AdapterParams(){
        return this;
    }
}
