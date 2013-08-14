package com.rytong.conf.editor.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.editors.text.TextEditor;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangBinary;

public class ChannelTable {

	private Composite pagecomposite;
	private Composite cha_table;
	private CollectionsPage parent;

	private Table table=null;
	private HashMap<String, TableItem> tableMap=null;
	private HashMap<String, EwpChannels> tableMapStore = null;

	public ChannelTable ChannelTable(){
		ErlLogger.debug("CollectionTable initial!");
		return this;
	}

	public ChannelTable getPage(){
		return this;
	}

	public void initialCollectionsComposite(CollectionsPage parent){
		this.parent = parent;
		//right composite
		if (cha_table != null ) {
			cha_table.dispose();
		}

		// set the layout of table composite in main composite
		cha_table = new Composite(parent.pagecomposite, SWT.BORDER);
		FormData rightcomsite_form = new FormData();
		rightcomsite_form.left = new FormAttachment(50,5);
		rightcomsite_form.right = new FormAttachment(99);
		rightcomsite_form.top = new FormAttachment(50, 2);
		rightcomsite_form.bottom = new FormAttachment(100, -2);
		cha_table.setLayoutData(rightcomsite_form);

		// set the layout of table composite
		GridLayout layout_right = new GridLayout();
		layout_right.numColumns = 10;
		layout_right.verticalSpacing=10;
		cha_table.setLayout(layout_right);

		GridData label_gd = new GridData(GridData.BEGINNING);
		label_gd.horizontalSpan = 10;

		Label label_all = new Label(cha_table, 0);
		label_all.setText("Channel Table");
		label_all.setLayoutData(label_gd);

		table = new Table(cha_table, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,8,9));

		Transfer[] Types = new Transfer[]{TextTransfer.getInstance()};
		int operations = DND.DROP_MOVE|DND.DROP_COPY|DND.DROP_LINK;

		final DragSource source = new DragSource(table, operations);
		source.setTransfer(Types);
		setDragListener(source);


		TableColumn collId = new TableColumn(table, SWT.NONE);
		collId.setWidth(200);
		collId.setText("Channel Id");
		TableColumn collName = new TableColumn(table, SWT.NONE);
		collName.setWidth(100);
		collName.setText("Channel Name");

		Button addbutton = new Button(cha_table, SWT.NONE);
		addbutton.setText(" Add...  ");
		addbutton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false,2,1));


		Button removebutton = new Button(cha_table, SWT.NONE);
		removebutton.setText("Remove");
		removebutton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false,2,1));

		//collStateText.addModifyListener(listener);
		//cha_table.setVisible(false);
		setTableListener(table);

	}

	public void refreshTable(){
		table.removeAll();
		tableMap = new HashMap<String, TableItem>();
		tableMapStore = new HashMap<String, EwpChannels>();
		Map<String, EwpChannels> map = parent.ChaMap;
		Iterator chaiter = map.entrySet().iterator();

		while (chaiter.hasNext()) {
			Map.Entry entry = (Map.Entry) chaiter.next();
			Object key = entry.getKey();
			Object obj = entry.getValue();
			//ErlLogger.debug("cha key:"+key);
			EwpChannels chaObj = (EwpChannels) obj;
			TableItem chaTable = tableMap.get(chaObj.cha_id);
			if (chaTable!=null){
				// do not create a new tree item
				//ErlLogger.debug("tree:"+chaObj.cha_id+" existed!");
			} else {
				TableItem tableId = newTableItem();
				setTableText(tableId, chaObj.cha_id, chaObj.cha_name);
				tableMap.put(chaObj.cha_id, tableId);
				// store the object into hashmap
				tableMapStore.put(chaObj.cha_id, chaObj);
			}
		}


	}

	public TableItem newTableItem(){
		TableItem tableId = new TableItem(table, SWT.NONE);
		return tableId;
	}

	public void setTableText(TableItem tableId, String id, String name){
		tableId.setText(new String[]{id, name});
	}

	public void setTableListener(final Table table) {
		table.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent event) {
				if (event.getSource() != null ){
					setCollTableDeselect();
					Table eventTab = (Table) event.getSource();
					int Len = eventTab.getItemCount();
					TableItem[] eventItem = eventTab.getSelection();
					int Height = 17;
					if (eventItem[0] != null)
					{
						Rectangle bounds = eventItem[0].getBounds();
						Height = bounds.height;
					}

					ErlLogger.debug("selectX:"+event.x+"  Y:"+event.y+"  H:"+Len*Height);
					if (eventItem.length==1 && (event.y < Len*17) ){
						selectionPage(eventItem[0].getText());
					} else
					{
						eventTab.deselectAll();
						parent.setVisiable();
					}
				}else {
					ErlLogger.debug("select2 null :!");
				}
			}
		});

		table.addSelectionListener(new SelectionAdapter(){
			public void widgetDefaultSelected(SelectionEvent event) {
				table.deselectAll();
				parent.setVisiable();
			}
		});
	}

	public void selectionPage(String key){
			EwpChannels chaObj = tableMapStore.get(key);
			ErlLogger.debug("select chaObj:"+chaObj.cha_id);

			parent.setUnVisiable();
			parent.collPage.setTextEmpty("");
			parent.chaPage.setText(chaObj);
	}

	public void setTableDeselect(){
		table.deselectAll();
	}

	public void setCollTableDeselect(){
			parent.coll_table.setTableDeselect();
	}



	private	final TableItem[] dragSourceItem = new TableItem[1];
	public void setDragListener(DragSource source){
		source.addDragListener(new DragSourceListener(){

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub

				TableItem[] selection = table.getSelection();
				ErlLogger.debug("dragStart------:"+selection.length+"--text:"+selection[0].getText());

				if (selection.length > 0){
					parent.setVisiable();
					parent.selectedObj=new SelectedItem(selection[0], tableMapStore.get(selection[0].getText()));
					event.doit = true;
					dragSourceItem[0] = selection[0];
				} else {
					event.doit = false;
				}
			};

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				ErlLogger.debug("dragSetData------");
				event.data = dragSourceItem[0].getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				ErlLogger.debug("dragFinished--:"+event.detail);
				ErlLogger.debug("dnd:"+DND.DROP_MOVE);
				if (event.detail == DND.DROP_MOVE)
					ErlLogger.debug("dnd:drop move");
				else if(event.detail == DND.DROP_COPY)
					ErlLogger.debug("dnd:drop copy");
			}
		});
	}


}