package com.rytong.ui.editor.lua.model;

public class Symbol {
	protected String name = new String();
	private Section sec;
	private Block parent;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Section getSection() {
		return sec;
	}

	public void setSection(Section sec) {
		this.sec = sec;
	}
	
	public String toString(){
		return name;
	}

	public Block getParent() {
		return parent;
	}


	public void setParent(Block parent) {
		this.parent = parent;
	}
}
