package com.rytong.conf.util;

public class ConfParams {

	private String key;
	private String value;

	public ConfParams(){
		key = "";
		value = "";
	}

	public ConfParams(String key, String value){
		this.key = key;
		this.value = value;
	}

	public void setKey(String key){
		this.key = key;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getKey(){
		return key;
	}

	public String getValue(){
		return value;
	}

}
