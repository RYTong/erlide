package com.rytong.ui.editor.lua.model;

import java.util.ArrayList;
import java.util.List;

public class Block extends Symbol{

	List<Symbol> symbolList;
	List<Symbol> variablesList;
	private Block parent;
	
	public Block(){
		super();
		symbolList = new ArrayList<Symbol>();
		variablesList = new ArrayList<Symbol>();
	}
	
	
	public void addSymbol(Symbol symbol){
		symbolList.add(symbol);
	}

	public List<Symbol> getSymbolList() {
		return symbolList;
	}

	public List<Symbol> getCleanedList(){
		List<Symbol> result = new ArrayList<Symbol>();
		for (Symbol symbol : symbolList) {
			if(symbol instanceof Function ||symbol instanceof Variable){
				result.add(symbol);
			}
		}		
		return result;
	}

	public List<Symbol> getFunctionList(){
		List<Symbol> result = new ArrayList<Symbol>();
		for (Symbol symbol : symbolList) {
			if(symbol instanceof Function){
				result.add(symbol);
			}
		}		
		return result;
	}
	
	public Block getParent() {
		return parent;
	}


	public void setParent(Block parent) {
		this.parent = parent;
	}
	
	public void addVariable(Variable v){
		if(!variablesList.contains(v)){
			variablesList.add(v);
		}
	}
	
	public List getVariablesList(){
		return variablesList;
	}
	
}
