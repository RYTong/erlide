package com.rytong.ui.editor.lua.model;

public class Function extends Block {

	private boolean isLocal;


	private String[] parameterList = {};
	
	public Function(){
		super();
	}

	public Function(String name , boolean isLocal) {
		super();
		this.name = name;
		this.isLocal = isLocal;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(name);
		result.append("(");
		for (int i = 0; i < parameterList.length; i++) {
			result.append(parameterList[i]);
			if(i != parameterList.length-1){
				result.append(",");
			}
		}
		result.append(")");
		return result.toString();
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public String getContentProposal() {
		StringBuffer result = new StringBuffer();
		result.append(name);
		result.append("(");
		for (int i = 0; i < parameterList.length; i++) {
			result.append("${"+ parameterList[i]+ "}");
			if(i != parameterList.length-1){
				result.append(",");
			}
		}
		result.append(")");
		return result.toString();
	}

	public String getContentDescription() {
		StringBuffer result = new StringBuffer();
		result.append(name);
		result.append("(");
		for (int i = 0; i < parameterList.length; i++) {
			result.append(parameterList[i]);
			if(i != parameterList.length-1){
				result.append(",");
			}
		}
		result.append(")");
		return result.toString();
	}


	public void addFunctionParameter(String params) {
		if (params != null && !params.equals("")) {
			parameterList = params.split(",");
		}
	}
}
