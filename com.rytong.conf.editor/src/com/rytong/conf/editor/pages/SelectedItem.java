package com.rytong.conf.editor.pages;

import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.util.ChannelConfUtil;

public class SelectedItem {

	public String id="";
	public Object obj="";
	public String type;
	public TreeItem parent=null;


	private ChannelConfUtil util = new ChannelConfUtil();


	public SelectedItem(TreeItem sitem, Object itemObj) {
		// TODO Auto-generated constructor stub
		initial(sitem, itemObj);
	}

	public SelectedItem(TableItem sitem, Object itemObj) {
		// TODO Auto-generated constructor stub
		initialTable(sitem, itemObj);
	}



	public SelectedItem SelectedItem(){
		return this;
	}

	public void initial(TreeItem sitem, Object itemObj){
		id=sitem.getText();
		obj=itemObj;
		type = util.checkObjectType(obj);


		parent = sitem.getParentItem();
		if (parent!=null)
		{
			ErlLogger.debug("select parent text:"+parent.getText());
		}
		else
			ErlLogger.debug("select parent is null!");
	}

	public void initialTable(TableItem sitem, Object itemObj){
		id=sitem.getText();
		obj=itemObj;
		type = util.checkObjectType(obj);
	}





}
