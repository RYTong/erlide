package com.rytong.ui.editor.conf;
import java.util.ArrayList;

import com.rytong.ui.editor.conf.model.Annotation;
import com.rytong.ui.editor.conf.model.Element;

public class SimpleModel {
	private ArrayList modelListeners;
	private ArrayList objects;
	public SimpleModel() {
		modelListeners = new ArrayList();
		objects = new ArrayList<NamedObject>();
		initialize();
	}
	public void addModelListener(IModelListener listener) {
		if (!modelListeners.contains(listener))
			modelListeners.add(listener);
	}
	public void removeModelListener(IModelListener listener) {
		modelListeners.remove(listener);
	}
	public void fireModelChanged(Object[] objects, String type, String property) {
		for (int i = 0; i < modelListeners.size(); i++) {
			((IModelListener) modelListeners.get(i)).modelChanged(objects,
					type, property);
		}
	}
	public Object[] getContents() {
		return objects.toArray();
	}
	private void initialize() {
		Annotation anot_id = new Annotation();
		anot_id.setName("id");
		anot_id.setValue(Annotation.Type.STRING);
		Element adapter_id = new Element(anot_id, null);
		Annotation anot_host = new Annotation();
		anot_host.setName("host");
		anot_host.setValue(Annotation.Type.STRING);
		Element adapter_host = new Element(anot_host);
		Annotation anot_adapter = new Annotation();
		anot_adapter.setDocstring("配置Adapter内容");
		anot_adapter.setName("Adapter");
		Element adapter = new Element(anot_adapter, new Element[]{adapter_id, adapter_host});
		
		Annotation anot_adapterid = new Annotation();
		anot_adapterid.setName("AdapterID");
		anot_adapterid.setValue(Annotation.Type.STRING);
		Element procedure_adapterid = new Element(anot_adapterid, null);
		Annotation anot_name = new Annotation();
		anot_name.setName("name");
		anot_name.setValue(Annotation.Type.STRING);
		Element procedure_name = new Element(anot_name);
		Annotation anot_procedure = new Annotation();
		anot_procedure.setDocstring("配置Procedure内容");
		anot_procedure.setName("Procedure");
		Element procedure = new Element(anot_procedure, new Element[]{procedure_adapterid, procedure_name});
		
		NamedObject[] objects = {
				new Conf("adapter", adapter, 2, true, "Some text"),
				new Conf("procedure", procedure, 2, true, "Some text")
				};
		add(objects, false);
	}
	public void add(NamedObject[] objs, boolean notify) {
		for (int i = 0; i < objs.length; i++) {
			objects.add(objs[i]);
			objs[i].setModel(this);
		}
		if (notify)
			fireModelChanged(objs, IModelListener.ADDED, "");
	}
	public void remove(NamedObject[] objs, boolean notify) {
		for (int i = 0; i < objs.length; i++) {
			objects.remove(objs[i]);
			objs[i].setModel(null);
		}
		if (notify)
			fireModelChanged(objs, IModelListener.REMOVED, "");
	}
}
