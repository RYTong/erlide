package com.rytong.conf.adapter.editor;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class AdapterLabelProvider extends LabelProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		if (element instanceof EwpProcedure){
			EwpProcedure tmp = (EwpProcedure) element;
			switch (columnIndex){
			case 0:
				return null;
			case 1:
				return null;
			case 2:
				return null;
			}
		} else if(element instanceof EwpAdapter){
			EwpAdapter tmp = (EwpAdapter) element;
			switch (columnIndex){
			case 0:
				return null;
			case 1:
				return null;
			case 2:
				return null;
			}
		}
		return null;//new Image();
	}

	private static String ADAPTER = "Adapter";
	private static String PROCEDURE =  "Procedure";


	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		if (element instanceof EwpProcedure){
			EwpProcedure tmp = (EwpProcedure) element;
			switch (columnIndex){
			case 3:
				return "true";
			case 0:
				return PROCEDURE;
			case 1:
				return tmp.getId();
			}
			return tmp.getId();
		} else if(element instanceof EwpAdapter){
			EwpAdapter tmp = (EwpAdapter) element;
			switch (columnIndex){
			case 3:
				return "true";
			case 0:
				return ADAPTER;
			case 1:
				return tmp.getName();
			}
			return tmp.getName();
		}else {
			return null;
		}
	}

	public String getText(Object element){
		if (element instanceof EwpProcedure){
			EwpProcedure tmp = (EwpProcedure) element;
			return tmp.getId();
		} else if(element instanceof EwpAdapter){
			EwpAdapter tmp = (EwpAdapter) element;
			return tmp.getName();
		}else{
			return (String) element;
		}
	}

}
