package com.rytong.conf.editor.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;
import com.rytong.conf.newcollection.wizard.NewCollWizard;

public class CollectionTable {

	private Composite coll_table;
	private CollectionsPage parent;

	private Table table=null;
	private HashMap<String, TableItem> tableMap=null;
	private HashMap<String, Object> tableMapStore = null;

	public CollectionTable CollectionTable(){
		ErlLogger.debug("CollectionTable initial!");
		return this;
	}

	public CollectionTable getPage(){
		return this;
	}


	public void initialCollectionsComposite(CollectionsPage parent){
		this.parent=parent;
		//right composite
		if (coll_table != null ) {
			coll_table.dispose();
		}

		// set the layout of collection table composite in main composite
		coll_table = new Composite(parent.pagecomposite, SWT.BORDER);
		FormData rightcomsite_form = new FormData();
		rightcomsite_form.left = new FormAttachment(50,5);
		rightcomsite_form.right = new FormAttachment(99);
		rightcomsite_form.top = new FormAttachment(5);
		rightcomsite_form.bottom = new FormAttachment(50, -2);
		coll_table.setLayoutData(rightcomsite_form);

		// set the layout of collection table composite
		GridLayout layout_right = new GridLayout();
		layout_right.numColumns = 10;
		layout_right.verticalSpacing=10;
		coll_table.setLayout(layout_right);

		GridData label_gd = new GridData(GridData.BEGINNING);
		label_gd.horizontalSpan = 10;

		Label label_all = new Label(coll_table, 0);
		label_all.setText("Collection Table");
		label_all.setLayoutData(label_gd);

		table = new Table(coll_table, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,8,9));

		Transfer[] Types = new Transfer[]{TextTransfer.getInstance()};
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		final DragSource source = new DragSource(table, operations);
		source.setTransfer(Types);
		setDragListener(source);

		TableColumn collId = new TableColumn(table, SWT.NONE);
		collId.setWidth(200);
		collId.setText("Collection Id");
		TableColumn collName = new TableColumn(table, SWT.NONE);
		collName.setWidth(100);
		collName.setText("Collection Name");


		Button newbutton = new Button(coll_table, SWT.NONE);
		newbutton.setText(" New...  ");
		newbutton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false,2,1));


		Button removebutton = new Button(coll_table, SWT.NONE);
		removebutton.setText("Remove");
		removebutton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false,2,1));
		// add button listener
		setButtonListener(newbutton, removebutton);

		//collStateText.addModifyListener(listener);
		//coll_table.setVisible(false);
		setTableListener(table);

	}

	public void refreshTable(){
		table.removeAll();
		tableMap = new HashMap<String, TableItem>();
		tableMapStore = new HashMap<String, Object>();
		Map<String, Object> map = parent.CollMap;
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object obj = entry.getValue();
			//ErlLogger.debug("key:"+key);
			EwpCollections collObj = (EwpCollections) obj;
			//ErlLogger.debug("type:"+collObj.coll_type.equalsIgnoreCase("1")+" !");
			if (collObj.coll_type.equalsIgnoreCase("1")){
				// create a new tree item
				TableItem tableId = newTableItem();
				setTableText(tableId, collObj.coll_id, collObj.coll_name);
				tableMap.put(collObj.coll_id, tableId);
				// store the object into hashmap
				tableMapStore.put(collObj.coll_id, collObj);

			} else {
				TableItem tableId = newTableItem();
				setTableText(tableId, collObj.coll_id, collObj.coll_name);
				tableMap.put(collObj.coll_id, tableId);
				// store the object into hashmap
				tableMapStore.put(collObj.coll_id, collObj);
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
					setChaTableDeselect();
					Table eventTab = (Table) event.getSource();
					int Len = eventTab.getItemCount();
					TableItem[] eventItem = eventTab.getSelection();

					ErlLogger.debug("len:"+eventItem.length+"  Y:"+event.y+"  H:"+Len*17);
					if (eventItem.length==1 && (event.y < Len*17) ){
						selectionPage(eventItem[0].getText());
					} else {
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


	public void setChaTableDeselect(){
		parent.cha_table.setTableDeselect();
	}

	public void setTableDeselect(){
		table.deselectAll();
	}

	public void selectionPage(String key){
			Object selectedObj = tableMapStore.get(key);
			EwpCollections coll = (EwpCollections) selectedObj;
			ErlLogger.debug("select chaObj:"+coll.coll_id);

			parent.setUnVisiable();
			parent.chaPage.setTextEmpty("");
			parent.collPage.setText(coll);
	}

	public void setButtonListener(final Button nbutton, final Button rbutton){
		nbutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				table.deselectAll();
				ErlLogger.debug("button listener:"+nbutton.getText());
				NewCollWizard wmain= new NewCollWizard(null, null);
				Set<String> tmpset = tableMapStore.keySet();
				ErlLogger.debug("tmpset size"+tmpset.size());
				wmain.initial(parent, tmpset);
				parent.CollMap.put(parent.newWizardColl.coll_id, parent.newWizardColl);
				parent.coll_table.refreshTable();
				parent.setVisiable();
				parent.refreshTreePage();

			}
		});

		rbutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				ErlLogger.debug("button listener:"+items.length);
				if(items.length==1){
					table.deselectAll();
					String tmpKey = items[0].getText();
					ErlLogger.debug("button listener h:"+tmpKey);

					parent.erlBackend_removeColl(tmpKey);
					parent.CollMap.remove(tmpKey);
					// refresh the tree composite and coll table composite
					parent.coll_table.refreshTable();
					parent.setVisiable();
					parent.refreshTreePage();
				}
			}
		});
	}

}
