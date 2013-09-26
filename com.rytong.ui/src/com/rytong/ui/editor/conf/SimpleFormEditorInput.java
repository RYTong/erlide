package com.rytong.ui.editor.conf;

public class SimpleFormEditorInput extends FormEditorInput {
	private SimpleModel model;
	
	public SimpleFormEditorInput(String name) {
		super(name);
		model = new SimpleModel();
	}
	
	public SimpleModel getModel() {
		return model;
	}
}
