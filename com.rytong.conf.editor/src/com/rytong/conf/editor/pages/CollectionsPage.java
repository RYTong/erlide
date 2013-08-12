package com.rytong.conf.editor.pages;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.core.model.util.ErlideUtil;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;
import org.erlide.utils.Util;
import org.xml.sax.SAXException;

import com.ericsson.otp.erlang.OtpErlangBinary;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.editor.editors.MultiConfEditor;
import com.rytong.conf.editor.handler.ConfErrorHandler;

public class CollectionsPage {

	public TextEditor editor;
	public String filePathStr="";
	public OtpErlangBinary confCon=null;
	public Composite pagecomposite;

	private IDocument document;
	private IProject fproject;

	private Composite composite_left;


	private IBackend ideBackend = null;

	private CollectionTable coll_table = new CollectionTable();
	private ChannelTable cha_table = new ChannelTable();

	public CollectionComposite collPage = new CollectionComposite();
	public ChannelComposite chaPage = new ChannelComposite();


	private Tree tree=null;
	private TreeItem maintree=null;
	private HashMap<String, TreeItem> treeMap = new HashMap<String, TreeItem>();
	private HashMap<String, Object> treeMapStore = new HashMap<String, Object>();
	private String channelxml;
	private ConfErrorHandler confPaser = new ConfErrorHandler();

	private HashMap<String,Object> CollMap = null;
	private HashMap<String,EwpChannels> ChaMap = null;
	private	final TreeItem[] dragSourceItem = new TreeItem[1];
	private Object selectedObj = null;
	private TreeItem selectedparent = null;


	public CollectionsPage CollectionsPage() {
		ErlLogger.debug("CollectionTable initial!");
		return this;
	}

	public CollectionsPage getPage() {
		return this;
	}

	public void setComposite(Composite composite){
		pagecomposite = composite;
	}

	public void setEditor(TextEditor maineditor){
		editor = maineditor;
		document = editor.getDocumentProvider()
				.getDocument(editor.getEditorInput());
	}

	public void setEditoeInput(IEditorInput input) {
		editor.setInput(input);
	}


	public void setUnVisiable(){
		composite_left.setVisible(false);
	}

	public void setVisiable(){
		collPage.setTextEmpty("");
		chaPage.setTextEmpty("");
		composite_left.setVisible(true);
	}

	public void setSelectParentNull(){
		selectedparent=null;
	}

	public void setTableDeselect(int index){
		if (index ==0 ){
			cha_table.setTableDeSelect();
		} else {
			coll_table.setTableDeSelect();
		}

	}

	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	public void initialPage() {

		composite_left = new Composite(pagecomposite, SWT.BORDER);

		//GridLayout layout = new GridLayout();

		FormLayout layout = new FormLayout();
		pagecomposite.setLayout(layout);

		// overview label sets
		Label label_overview = new Label(pagecomposite, 0);
		label_overview.setText("overview");

		FormData label_form = new FormData();
		label_form.left = new FormAttachment(2,1);
		label_form.right = new FormAttachment(30);
		label_form.top = new FormAttachment(1);
		label_overview.setLayoutData(label_form);

		// initial right composite


		coll_table.initialCollectionsComposite(pagecomposite);
		cha_table.initialCollectionsComposite(pagecomposite);
		collPage.initialCollectionsComposite(pagecomposite);
		chaPage.initialChannelsComposite(pagecomposite);

		initialLeftComposite();
	}


	public void paintPage(){
		channelxml = getContent();
		coll_table.setParent(this);
		cha_table.setParent(this);

		chaPage.setParent(this);
		collPage.setParent(this);

		ErlLogger.debug("Channel xml:"+channelxml);
		//ErlLogger.debug("document:"+document.get());

		refreshTree();
		confPaser.setPage(this);

		doParse(channelxml);
		CollMap = confPaser.getCollectionsMap();
		ChaMap = confPaser.getChannelsMap();
		setTreePage(CollMap, ChaMap);

	}

	public TreeItem setTreeItem(String id, String name){
		TreeItem treeId = new TreeItem(tree, SWT.NONE);
		treeId.setText(new String[]{id, name});
		return treeId;
	}

	public TreeItem newTreeItem(){
		TreeItem treeId = new TreeItem(tree, SWT.NONE);
		return treeId;
	}
	public void setTreeText(TreeItem treeId, String id, String name){
		treeId.setText(new String[]{id, name});
	}

	public TreeItem setTreeChild(TreeItem ftree, String index, String id, String name){
		TreeItem child = null ;
		if (index == null || index == "") {
			child = new TreeItem(ftree, SWT.NONE);
		}
		else {
			//ErlLogger.debug("tree index is -------:"+Integer.parseInt(index));
			child = new TreeItem(ftree, SWT.NONE, Integer.parseInt(index)-1);
		}
		child.setText(new String[]{id, name});
		return child;
	}

	public TreeItem setTreeChild(TreeItem ftree, String id){
		TreeItem child = new TreeItem(ftree, SWT.NONE);
		child.setText(id);
		return child;
	}


	// paint the channel tree
	public void setTreePage(HashMap<String,Object> collMap, HashMap<String, EwpChannels> chaMap){

		treeMap = new HashMap<String, TreeItem>();
		treeMapStore = new HashMap<String, Object>();
		Map<String, Object> map = collMap;
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object obj = entry.getValue();
			ErlLogger.debug("key:"+key);
			EwpCollections collObj = (EwpCollections) obj;
			ErlLogger.debug("type:"+collObj.coll_type.equalsIgnoreCase("1")+" !");
			if (collObj.coll_type.equalsIgnoreCase("1")){
				// create a new tree item
				TreeItem treeId = newTreeItem();
				setTreeText(treeId, collObj.coll_id, collObj.coll_name);
				treeMap.put(collObj.coll_id, treeId);
				// store the object into hashmap
				treeMapStore.put(collObj.coll_id, collObj);
				setTreeChildPage(collObj, treeId);
			}
		}
		//ErlLogger.debug("------------------tree:zero over !");

		iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object obj = entry.getValue();
			//ErlLogger.debug("key:"+key);
			EwpCollections collObj = (EwpCollections) obj;
			//ErlLogger.debug("type:"+collObj.coll_type.equalsIgnoreCase("1")+" !");
			TreeItem collChiTree = treeMap.get(collObj.coll_id);
			if (collObj.coll_type.equalsIgnoreCase("0") && collChiTree!=null){
				// create a new tree item
				//ErlLogger.debug("tree:"+collObj.coll_id+"over !");
				setTreeChildPage(collObj, collChiTree);
			} else if(collObj.coll_type.equalsIgnoreCase("0")){
				//ErlLogger.debug("tree: is empty ");
				collChiTree = newTreeItem();
				setTreeText(collChiTree, collObj.coll_id, collObj.coll_name);
				treeMap.put(collObj.coll_id, collChiTree);
				// store the object into hashmap
				treeMapStore.put(collObj.coll_id, collObj);
				setTreeChildPage(collObj, collChiTree);
			}
		}

/*		Map<String, EwpChannels> mapcha= chaMap;
		Iterator chaiter = mapcha.entrySet().iterator();

		while (chaiter.hasNext()) {
			Map.Entry entry = (Map.Entry) chaiter.next();
			Object key = entry.getKey();
			Object obj = entry.getValue();
			ErlLogger.debug("cha key:"+key);
			EwpChannels chaObj = (EwpChannels) obj;
			TreeItem chaChiTree = treeMap.get(chaObj.cha_id);
			if (chaChiTree!=null){
				// do not create a new tree item
				ErlLogger.debug("tree:"+chaObj.cha_id+" existed!");
			} else {
				TreeItem treeId = newTreeItem();
				setTreeText(treeId, chaObj.cha_id, chaObj.cha_name);
				treeMap.put(chaObj.cha_id, treeId);
				// store the object into hashmap
				treeMapStore.put(chaObj.cha_id, chaObj);
			}
		}*/

		coll_table.setTable(collMap);
		cha_table.setTable(chaMap);
	}

	public void setTreeChildPage(EwpCollections collObj, TreeItem treeId){
		ArrayList<EwpCollectionItems> items = collObj.itemList;
		//ErlLogger.debug("items length:"+items.size());
		Iterator it1 = items.iterator();
		//ErlLogger.debug("array items :"+items.size());
		while(it1.hasNext()){
			Object obj2 = it1.next();
			EwpCollectionItems item = (EwpCollectionItems) obj2;
			//ErlLogger.debug("array item :"+item.item_id+"!"+item.item_type);
			if (item.item_type.equalsIgnoreCase("0")){
				EwpCollections collitem = (EwpCollections) CollMap.get(item.item_id);
				if(collitem!=null){
					TreeItem treeChi= setTreeChild(treeId, item.menu_order, collitem.coll_id, collitem.coll_name);
					treeId.setExpanded(true);
					treeMap.put(collitem.coll_id, treeChi);
					// store the object into hashmap
					treeMapStore.put(collitem.coll_id, collitem);
				} else {
					TreeItem treeChi= setTreeChild(treeId, item.item_id);
					//
					//treeChi.setBackground(color);
					treeMap.put(item.item_id, treeChi);
					// store the object into hashmap
					EwpCollections newColl = new EwpCollections();
					newColl.set_id(item.item_id);
					treeMapStore.put(item.item_id, newColl);

				}
			} else if(item.item_type.equalsIgnoreCase("1")){
				EwpChannels chaitem = ChaMap.get(item.item_id);
				if(chaitem!=null){
					TreeItem treeChi= setTreeChild(treeId, item.menu_order,chaitem.cha_id, chaitem.cha_name);
					treeId.setExpanded(true);
					treeMap.put(chaitem.cha_id, treeChi);
					// store the object into hashmap
					treeMapStore.put(chaitem.cha_id, chaitem);
				} else {
					TreeItem treeChi= setTreeChild(treeId, item.item_id);
					//
					//treeChi.setBackground(color);
					treeMap.put(item.item_id, treeChi);
					// store the object into hashmap
					EwpChannels newCha = new EwpChannels();
					newCha.set_id(item.item_id);
					treeMapStore.put(item.item_id, newCha);

				}
			}
		}
	}

	public void initialLeftComposite(){
		// left composite

		GridLayout layout_left = new GridLayout();
		layout_left.numColumns = 10;
		composite_left.setLayout(layout_left);

		FormData leftcomsite_form = new FormData();
		leftcomsite_form.left = new FormAttachment(2,1);
		leftcomsite_form.right = new FormAttachment(50);
		leftcomsite_form.top = new FormAttachment(5);
		leftcomsite_form.bottom = new FormAttachment(100);
		composite_left.setLayoutData(leftcomsite_form);


		GridData label_gd = new GridData(GridData.BEGINNING);
		label_gd.horizontalSpan = 10;

		Label label_all = new Label(composite_left, 0);
		label_all.setText("Collection Lists");
		label_all.setLayoutData(label_gd);

		// layout data for tree

		tree = new Tree(composite_left, SWT.BORDER);

		Transfer[] Types = new Transfer[]{TextTransfer.getInstance()};
		int operations = DND.DROP_MOVE|DND.DROP_COPY|DND.DROP_LINK;

		final DragSource source = new DragSource(tree, operations);
		source.setTransfer(Types);
		setDragListener(source);

		final DropTarget target = new DropTarget(tree, operations);
		target.setTransfer(Types);
		setDropListener(target);


		setTreeListener(tree);

		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);


		TreeColumn idColumn = new TreeColumn(tree, SWT.NONE);
		idColumn.setWidth(200);
		idColumn.setText("ChannelId");

		TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
		nameColumn.setWidth(80);
		nameColumn.setText("Name");
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,8,9));

		Button addbutton = new Button(composite_left, SWT.NONE);
		addbutton.setText(" Add...  ");
		addbutton.setSize(120, 30);

		addbutton.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false,2,1));


		Button removebutton = new Button(composite_left, SWT.NONE);
		removebutton.setText("Remove");
		removebutton.setSize(120, 30);
		removebutton.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false,2,1));
	}


	public void refreshTree(){
		tree.removeAll();
	}

	public Tree get_tree(){
		return tree;
	}


	private final String conFromFile= "path";
	private final String conFromDoc= "text";

	private String getContent(){
		//IPath filePath =  ((FileEditorInput) editor.getEditorInput()).getFile().getLocation();
		//filePathStr = filePath.toString();
		OtpErlangObject result=null;
		String text = document.get();

		if (text == "" || text ==null){
			 ErlLogger.debug("document get null!");
				IPath filePath =  ((FileEditorInput) editor.getEditorInput()).getFile().getLocation();
				filePathStr = filePath.toString();
				result = getIdeBackend(conFromFile, filePathStr);
		} else {
			 ErlLogger.debug("document text not null!");
				result = getIdeBackend(conFromDoc, text);
		}

		return Util.stringValue(result);

	}

	private void doParse(String xml) {
		try {
			//ErlLogger.debug("input:"+xml);
			String newxml = xml.replace("&", "&amp;");
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new ByteArrayInputStream(newxml.getBytes()), confPaser);


			/*			DocumentBuilder builder = null;
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document xmlDoc = builder.parse(new ByteArrayInputStream(newxml.getBytes()));
			Element rootElement = xmlDoc.getDocumentElement();
			NodeList te = xmlDoc.getChildNodes();
			ErlLogger.debug("rootElement:"+rootElement.getTagName());*/

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void setDragListener(DragSource source){
		source.addDragListener(new DragSourceListener(){

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub

				TreeItem[] selection = tree.getSelection();
				//ErlLogger.debug("dragStart------:"+selection.length+"----:"+selection[0].getItemCount());
				if (selection.length > 0 && selection[0].getItemCount() == 0){
					event.doit = true;
					dragSourceItem[0] = selection[0];
				} else {
					event.doit = false;
				}
			};

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				//ErlLogger.debug("dragSetData------");
				event.data = dragSourceItem[0].getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				ErlLogger.debug("dragFinished--:"+event.detail+
						" |dropmove:"+DND.DROP_MOVE+
						" |dropmove:"+DND.DROP_COPY);
				if (event.detail == DND.DROP_MOVE)
					dragSourceItem[0].dispose();
				dragSourceItem[0] = null;
			}
		});
	}


	public void setDropListener(DropTarget target){
		target.addDropListener(new DropTargetAdapter(){

			public void dragOver(DropTargetEvent event){
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;

				if (event.item != null){
					TreeItem item = (TreeItem) event.item;
					//Point pt =
					composite_left.getDisplay();
					Display dis = composite_left.getDisplay();
					Point pt = dis.map(null, tree, event.x, event.y);
					//ErlLogger.debug("e x:"+event.x);
					//ErlLogger.debug("e y:"+event.y);
					//ErlLogger.debug("pt x:"+pt.x);
					//ErlLogger.debug("pt y:"+pt.y);

					Rectangle bounds = item.getBounds();
					//ErlLogger.debug("eb x:"+bounds.x);
					//ErlLogger.debug("eb y:"+bounds.y);
					//ErlLogger.debug("eb h:"+bounds.height);
					if (pt.y < bounds.y + bounds.height/3){
						//ErlLogger.debug("before------");
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y > bounds.y + 2*bounds.height/3){
						//ErlLogger.debug("after------");
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					} else {
						//ErlLogger.debug("select------");
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				}
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null){
					event.detail = DND.DROP_NONE;
					return;
				}
				int selectflag = checkObjectType(selectedObj) ;
				String[] text = returnText(selectflag);
				int removeflag = checkObjectType(treeMapStore.get(text[0]));

				ErlLogger.debug("drap :"+text[0]);
				if(event.item == null) {
					ErlLogger.debug("event null:");

					if (selectedparent!=null) {
						ErlLogger.debug("old parent text:"+selectedparent.getText());
						editIndex(selectedparent, null, text[0], "");
					} else {
						ErlLogger.debug("null parent!");
					}
					if (removeflag==0){
						TreeItem newItem = new TreeItem(tree, SWT.NONE);
						newItem.setText(text);
					}
				} else {
					TreeItem item = (TreeItem) event.item;

					ErlLogger.debug("event item:"+item.getText());
					TreeItem parent = item.getParentItem();
					Rectangle bounds = item.getBounds();
					Display display = composite_left.getDisplay();
					Point pt = display.map(null, tree, event.x, event.y);
					int event_flag = checkObjectType(treeMapStore.get(item.getText()));
					if (parent !=null) {
						ErlLogger.debug("parent item:"+parent.getText());
						TreeItem[] items = parent.getItems();
						int index = 0;
						ErlLogger.debug("pnn items length:"+items.length);
						for(int i = 0; i < items.length; i++){
							if (items[i] == item){
								index = i;
								break;
							}
						}
						ErlLogger.debug("index:"+index+" |flag:"+event_flag);
						if (pt.y < bounds.y + bounds.height / 3) {
							ErlLogger.debug("pelse 1:");
							ErlLogger.debug("parents is not null event text:"+item.getText());
							editIndex(selectedparent, parent, text[0], String.valueOf(index));
							TreeItem newItem = new TreeItem(parent, SWT.NONE, index);
							newItem.setText(text);
						} else if (pt.y > bounds.y + 2*bounds.height/3) {
							ErlLogger.debug("pelse 2:");
							ErlLogger.debug("parents is not null new parent text:"+item.getText());
							editIndex(selectedparent, parent, text[0], String.valueOf(index+1));
							TreeItem newItem = new TreeItem(parent, SWT.NONE, index+1);
							newItem.setText(text);
						} else if(text[0]==item.getText() && event_flag==selectflag){
							TreeItem newItem = new TreeItem(parent, SWT.NONE, index);
							newItem.setText(text);
						} else if(event_flag==0) {
							ErlLogger.debug("pelse 3:");
							ErlLogger.debug("parents is not null new parent text:"+item.getText());
							editIndex(selectedparent, item, text[0], "");
							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);
						}else {
							ErlLogger.debug("pelse 4:");
							if (removeflag==0){
								TreeItem newItem = new TreeItem(tree, SWT.NONE);
								newItem.setText(text);
							}
						}
					} else {
						TreeItem[] items = tree.getItems();

						int index = 0;
						ErlLogger.debug("parent is null,else items length:"+items.length);
						ErlLogger.debug("item length:"+item.getItems().length);
						for (int i =0; i < items.length; i++){
							if (items[i] == item){
								index = i;
								break;
							}
						}
						ErlLogger.debug("else index:"+index);
						if (pt.y < bounds.y + bounds.height / 3) {
							ErlLogger.debug("else 1:");

							ErlLogger.debug("event not null:");
							if (selectedparent!=null) {
								ErlLogger.debug("old parent text:"+selectedparent.getText());
								editIndex(selectedparent, null, text[0], "");
							} else {
								ErlLogger.debug("null parent!");
							}
							if (removeflag==0){
								TreeItem newItem = new TreeItem(tree, SWT.NONE, index);
								newItem.setText(text);
							}
						} else if (pt.y > bounds.y + 2*bounds.height/3) {
							ErlLogger.debug("else 2:");

							ErlLogger.debug("event not null:");
							if (selectedparent!=null) {
								ErlLogger.debug("old parent text:"+selectedparent.getText());
								editIndex(selectedparent, null, text[0], "");
							} else {
								ErlLogger.debug("null parent!");
							}
							if (removeflag==0){
								TreeItem newItem = new TreeItem(tree, SWT.NONE, index+1);
								newItem.setText(text);
							}
						} else if(event_flag==0) {
							ErlLogger.debug("else 3:");
							ErlLogger.debug("new parent text:"+item.getText());
							editIndex(null, item, text[0], "");
							//OtpErlangTuple reParams = formParams(selectedparent.getText(), "", text[0], "", "");

							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);

						} else {
							// rollback
							TreeItem newItem = new TreeItem(tree, SWT.NONE);
							newItem.setText(text);
						}
						ErlLogger.debug("drop over!");
					}
				}
			}
		});
	}


	public void setTreeListener(final Tree tree) {

		tree.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TreeItem selectitem = (TreeItem)e.item;
				//ErlLogger.debug("ppppppp:"+selectitem.getParentItem());
				if(selectitem == null)
					ErlLogger.debug("selectitem null");
				else
				{
					ErlLogger.debug("select text:"+selectitem.getText());
					selectedparent = selectitem.getParentItem();
					if (selectedparent!=null)
					{
						ErlLogger.debug("select parent text:"+selectedparent.getText());
					}
					else
						ErlLogger.debug("select parent is null!");
					selectedObj = treeMapStore.get(selectitem.getText());
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				TreeItem selectitem = (TreeItem)e.item;
				boolean expandflag = selectitem.getExpanded();
				selectitem.setExpanded(!expandflag);

			}
		});

		/*		tree.addMouseListener(new MouseAdapter(){
			public void mouseDoubleClick(MouseEvent e) {
				boolean expandflag = selectitem.getExpanded();
				selectitem.setExpanded(!expandflag);

			}

		});*/
	}


	public void selectionPage(String key){
		selectedObj = treeMapStore.get(key);
		int flag = checkObjectType(selectedObj) ;
		if (flag == 0){
			EwpCollections coll = (EwpCollections) selectedObj;
			chaPage.setTextEmpty("");
			collPage.setText(coll);
		} else if(flag == 1){
			EwpChannels chaObj = (EwpChannels) selectedObj;
			collPage.setTextEmpty("");
			chaPage.setText(chaObj);
		} else{
			collPage.setTextEmpty("");
			chaPage.setTextEmpty("");
		}
	}

	public String[] returnText(int flag) {
		if (flag==0){
			EwpCollections coll = (EwpCollections) selectedObj;
			return new String [] {coll.coll_id, coll.coll_name};
		} else if(flag==1){
			EwpChannels chaObj = (EwpChannels) selectedObj;
			return new String []{chaObj.cha_id, chaObj.cha_name};
		} else {
			return new String []{"",""};
		}
	}

	public int checkObjectType(Object obj) {
		if (obj instanceof EwpCollections){
			ErlLogger.debug("-------------------EwpCollections type:"+(obj instanceof EwpCollections));
			return 0;
		} else if(obj instanceof EwpChannels){
			ErlLogger.debug("-------------------EwpChannels type:"+(obj instanceof EwpChannels));
			return 1;
		} else {
			ErlLogger.debug("-------------------EwpCollectionItems type:"+(obj instanceof EwpCollectionItems));
			ErlLogger.debug("null type:"+(obj == null));
			return 3;
		}
	}



	public void editIndex(TreeItem oldparent, TreeItem newparent, String text, String index) {
		try {
			String oldParText = "";
			String newParText = "";
			if (oldparent != null) {
				oldParText=oldparent.getText();
			}
			if (newparent!=null){
				newParText = newparent.getText();
			}
			if (index == null)
				index="";
			int type = checkObjectType(selectedObj);
			OtpErlangTuple reParams = formParams(oldParText, newParText, text, Integer.toString(type), index);
			OtpErlangObject result = getIdeBackend(confCon, reParams);
			String reStr = Util.stringValue(result);
			document.replace(0, document.getLength(), reStr);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	// format the parmas for the synchronization of conf file
	public OtpErlangTuple formParams(String oldParent, String nowParent, String id, String type, String index){

		OtpErlangObject[] request = new OtpErlangObject[5];
		request[0]=new OtpErlangList(oldParent);
		request[1]=new OtpErlangList(nowParent);
		request[2]=new OtpErlangList(id);
		request[3]=new OtpErlangList(type);

		request[4]=new OtpErlangList(index);
		return new OtpErlangTuple(request);
	}


	public OtpErlangObject getIdeBackend(OtpErlangBinary Key, String flag, String chaId, String id, String value){

		OtpErlangObject res = null;
		OtpErlangObject result=null;

		if(ideBackend == null) {
			ideBackend = BackendCore.getBackendManager().getIdeBackend();
		};

		ErlLogger.debug("call ewp backend to edit the conf file");
		try {
			res = ideBackend.call(15000, "ewp_conf_parse", "edit_conf", "bssss", Key, flag, chaId, id, value);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OtpErlangList resList = (OtpErlangList) res;
		result = (OtpErlangObject)resList.elementAt(0);
		confCon= (OtpErlangBinary)resList.elementAt(1);
		//ErlLogger.debug("the rpc call result : " + result);

		return result;
	}


	public OtpErlangObject getIdeBackend(OtpErlangBinary Key, OtpErlangTuple params){

		OtpErlangObject res = null;
		OtpErlangObject result=null;

		if(ideBackend == null) {
			ideBackend = BackendCore.getBackendManager().getIdeBackend();
		};

		ErlLogger.debug("call ewp backend to edit the conf file");
		try {
			res = ideBackend.call(15000, "ewp_conf_parse", "change_index", "1b1s", Key, params);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OtpErlangList resList = (OtpErlangList) res;
		result = (OtpErlangObject)resList.elementAt(0);
		confCon= (OtpErlangBinary)resList.elementAt(1);
		//ErlLogger.debug("the rpc call result : " + result);

		return result;
	}


	public OtpErlangObject getIdeBackend(String flag, String params){
		OtpErlangObject res = null;
		OtpErlangObject result=null;
		if(ideBackend == null) {
			ideBackend = BackendCore.getBackendManager().getIdeBackend();
		};

		try {
			res = ideBackend.call(15000, "ewp_conf_parse", "parse_channel_config", "ss", flag, params);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		OtpErlangList resList = (OtpErlangList) res;
		result = (OtpErlangObject)resList.elementAt(0);
		confCon= (OtpErlangBinary)resList.elementAt(1);
		//ErlLogger.debug("the rpc call result : " + result);
		return result;
	}

	/*

	public void initialLeftComposite1(){
		// left composite

		FormLayout layout = new FormLayout();
		composite_left.setLayout(layout);


		FormData leftcomsite_form = new FormData();
		leftcomsite_form.left = new FormAttachment(2,1);
		leftcomsite_form.right = new FormAttachment(50);
		leftcomsite_form.top = new FormAttachment(5);
		leftcomsite_form.bottom = new FormAttachment(100);
		composite_left.setLayoutData(leftcomsite_form);


		GridData label_gd = new GridData(GridData.BEGINNING);
		label_gd.horizontalSpan = 6;



		Label label_all = new Label(composite_left, 0);
		label_all.setText("Collection Lists");
		label_all.setLayoutData(label_gd);

		FormData label_form = new FormData();
		label_form.left = new FormAttachment(2,1);
		label_form.right = new FormAttachment(30);
		label_form.top = new FormAttachment(1);
		label_all.setLayoutData(label_form);

		// layout data for tree

		tree = new Tree(composite_left, SWT.BORDER);
		setTreeListener(tree);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeColumn idColumn = new TreeColumn(tree, SWT.NONE);
		idColumn.setWidth(200);
		idColumn.setText("ChannelId");

		TreeColumn nameColumn = new TreeColumn(tree, SWT.NONE);
		nameColumn.setWidth(80);
		nameColumn.setText("Name");

		FormData tree_form = new FormData();
		tree_form.left = new FormAttachment(2,1);
		tree_form.right = new FormAttachment(75);
		tree_form.top = new FormAttachment(5);
		tree_form.bottom = new FormAttachment(95);
		tree.setLayoutData(tree_form);


		Button addbutton = new Button(composite_left, SWT.NONE);
		addbutton.setText("Add...");

		FormData addb_form = new FormData();
		addb_form.left = new FormAttachment(75,5);
		addb_form.right = new FormAttachment(99);
		addb_form.top = new FormAttachment(5);
		//tree_form.bottom = new FormAttachment(1);
		addbutton.setLayoutData(addb_form);



		Button removebutton = new Button(composite_left, SWT.NONE);
		removebutton.setText("Remove");

		FormData removeb_form = new FormData();
		removeb_form.left = new FormAttachment(75,5);
		removeb_form.right = new FormAttachment(99);
		removeb_form.top = new FormAttachment(10);
		//tree_form.bottom = new FormAttachment(1);
		removebutton.setLayoutData(removeb_form);
	}
	 */

	public void test(){
		try{

			IPath filePath =  ((FileEditorInput) editor.getEditorInput()).getFile().getLocation();
			File appFile = filePath.toFile();
			ErlLogger.debug("App file path:"+appFile);
			String content = null;
			InputStreamReader read = new InputStreamReader(new FileInputStream(appFile),"utf-8");
			BufferedReader reader = new BufferedReader(read);

			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				content=content+tempString;
			}

			ErlLogger.debug("File content:"+content);
			read.close();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
