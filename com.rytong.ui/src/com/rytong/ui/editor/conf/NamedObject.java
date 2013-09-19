package com.rytong.ui.editor.conf;


public	class NamedObject {
	private String name;
	protected SimpleModel model;
	
	public NamedObject(String name) {
		this.name = name;
	}
	public void setModel(SimpleModel model) {
		this.model = model;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return getName();
	}
	public void setName(String name) {
		this.name = name;
		model.fireModelChanged(new Object [] {this}, IModelListener.CHANGED, ""); //$NON-NLS-1$
	}
}