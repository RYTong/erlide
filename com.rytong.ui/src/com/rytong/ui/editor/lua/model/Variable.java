package com.rytong.ui.editor.lua.model;

public class Variable extends Symbol{

	private boolean isLocal;
	
	public Variable(String name, boolean isLocal){
		super();
		this.name = name;
		this.isLocal = isLocal;
	}
	
	public String toString(){
		return name;
	}


	public boolean isLocal() {
		return isLocal;
	}


	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	
}
