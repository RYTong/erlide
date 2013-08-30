package com.rytong.conf.editor.handler;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.TreeItem;
import org.erlide.jinterface.ErlLogger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.editor.pages.EwpCollectionItems;
import com.rytong.conf.editor.pages.EwpCollections;
import com.rytong.conf.newchannel.wizard.AdapterView;
import com.rytong.conf.newchannel.wizard.WizarParams;

public class ConfErrorHandler extends DefaultHandler  {


	public ConfErrorHandler(){
		ErlLogger.debug("conf default error handler!");
	}

	private EwpCollections collObj;
	private EwpCollectionItems itemsObj;
	private EwpChannels chaObj;
	private WizarParams viewsObj;
	private Object nowObj;
	private static int flag=0;

	//存放所有的节点（这里的节点等于原来的节点+编号）以及它所对应的值
	private HashMap<String,Object> collMap = new HashMap<String,Object>();
	private HashMap<String,EwpChannels> channelMap = new HashMap<String,EwpChannels>();
	//目前的节点
	private String currentElement = null;
	private Object beforeObj = null;
	//目前节点所对应的值
	private String currentValue = null;

	public void setResultMap(HashMap<String, Object> collMap, HashMap<String, EwpChannels> channelMap){
		this.collMap = collMap;
		this.channelMap = channelMap;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		//get the value of current element
		currentValue =currentValue + new String(ch, start, length);
	}

	public void startElement(String uri, String localName, String eName,
			Attributes attr) throws SAXException {
		//ErlLogger.debug("start element :"+eName);
		currentValue="";
		if(eName.equalsIgnoreCase("root")){
			flag=0;
			//currentElement= "";
		} else if(eName.equalsIgnoreCase("collections")){
			//ErlLogger.debug("collections:");
			collObj=new EwpCollections();
			nowObj = collObj;
			currentElement= eName;
			//newCollections();
		} else if(eName.equalsIgnoreCase("channels")){
			//ErlLogger.debug("channel:");
			chaObj=new EwpChannels();
			nowObj=chaObj;
		} else if(eName.equalsIgnoreCase("views")&&nowObj.equals(chaObj)){
			//ErlLogger.debug("channel view:"+currentElement);
			beforeObj  = nowObj;
			viewsObj = chaObj.add_view;
			nowObj=chaObj.add_view;
			currentElement= eName;
		}else if(eName.equalsIgnoreCase("items")&&nowObj.equals(collObj)){
			//ErlLogger.debug("coll item:"+collObj.coll_id);
			beforeObj= nowObj;
			itemsObj= collObj.initialItem();
			nowObj=itemsObj;
			currentElement= eName;
		} else{
			//ErlLogger.debug("else:"+currentElement);
			//ErlLogger.debug("else to:"+eName);
			currentElement= eName;
		}

	}

	public void endElement(String uri, String localName, String eName) throws SAXException {
		//ErlLogger.debug("end element:"+eName);
		if (currentValue!=null){
			//ErlLogger.debug("currentValue:"+currentValue);
			if (nowObj.equals(collObj)){
				//ErlLogger.debug("item object!");
				collObj.set_value(currentElement, currentValue);
			} else if(nowObj.equals(chaObj)){
				//ErlLogger.debug("channel object!");
				chaObj.set_value(currentElement, currentValue);
			} else if(nowObj.equals(itemsObj)){
				//ErlLogger.debug("item object!");
				itemsObj.set_value(currentElement, currentValue);
			}else if(nowObj.equals(viewsObj)){
				//ErlLogger.debug("item object!");
				if	(!eName.equals("views")){
					//ErlLogger.debug("views equal:"+currentElement+"}:"+currentValue);
					viewsObj.addView(currentElement, currentValue);
				}
			}
		};

		if(eName.equalsIgnoreCase("collections")){
			//ErlLogger.debug("hashmap currentValue:"+collObj.coll_id);
			collMap.put(collObj.coll_id,  collObj);
		}else if(eName.equalsIgnoreCase("channels")){
			channelMap.put(chaObj.cha_id,  chaObj);
		}else if(eName.equalsIgnoreCase("items")){
			collObj.addItem(itemsObj);
			nowObj = beforeObj;
		}else if(eName.equalsIgnoreCase("views")){
			nowObj = beforeObj;
		}
	}

}
