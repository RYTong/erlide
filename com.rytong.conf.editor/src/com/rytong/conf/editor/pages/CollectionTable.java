package com.rytong.conf.editor.pages;

import java.util.ArrayList;
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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.rytong.conf.newcollection.wizard.NewCollWizard;

public class CollectionTable {

    private Composite coll_table;
    private CollectionsPage parent;

    private Button newbutton;
    private Button editbutton;
    private Button removebutton;

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
        rightcomsite_form.top = new FormAttachment(0,20);
        rightcomsite_form.bottom = new FormAttachment(50, -2);
        coll_table.setLayoutData(rightcomsite_form);

        // set the layout of collection table composite


        coll_table.setLayout(new FormLayout());

        Label label_all = new Label(coll_table, 0);
        label_all.setText("Collection Table");

        FormData labelcomsite_form = new FormData();
        labelcomsite_form.left = new FormAttachment(0, 5);
        labelcomsite_form.right = new FormAttachment(100, -5);
        labelcomsite_form.top = new FormAttachment(0, 3);
        //labelcomsite_form.bottom = new FormAttachment(100, -2);
        label_all.setLayoutData(labelcomsite_form);


        table = new Table(coll_table, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        FormData tablecomsite_form = new FormData();
        tablecomsite_form.left = new FormAttachment(0, 5);
        tablecomsite_form.right = new FormAttachment(100, -106);
        tablecomsite_form.top = new FormAttachment(0, 23);
        tablecomsite_form.bottom = new FormAttachment(100, -5);
        table.setLayoutData(tablecomsite_form);


        Transfer[] Types = new Transfer[]{TextTransfer.getInstance()};
        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
        final DragSource source = new DragSource(table, operations);
        source.setTransfer(Types);
        setDragListener(source);

        TableColumn collId = new TableColumn(table, SWT.NONE);
        collId.setWidth(200);
        collId.setText("Collection Id");
        TableColumn collName = new TableColumn(table, SWT.NONE);
        collName.setWidth(150);
        collName.setText("Collection Name");


        newbutton = new Button(coll_table, SWT.LEFT);
        newbutton.setText(" New...");
        newbutton.setLayoutData(setButtonLayout(0));

        editbutton = new Button(coll_table, SWT.LEFT);
        editbutton.setText(" Edit");
        editbutton.setLayoutData(setButtonLayout(1));


        removebutton = new Button(coll_table, SWT.LEFT);
        removebutton.setText(" Remove");
        removebutton.setLayoutData(setButtonLayout(2));
        // add button listener
        setButtonListener();

        //collStateText.addModifyListener(listener);
        //coll_table.setVisible(false);
        setTableListener(table);

    }


    public void refreshTable(){
        editbutton.setEnabled(false);
        removebutton.setEnabled(false);
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


    public FormData setButtonLayout(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(100, -103);
        tmp_form.right = new FormAttachment(100, -3);
        tmp_form.top = new FormAttachment(0, 23+i*30);
        return tmp_form;
    }

    // add table listener

    public void setTableListener(final Table table) {
        table.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent event) {
                if (event.getSource() != null ){
                    setChaTableDeselect();
                    TableItem[] eventItem = table.getSelection();

                    if (eventItem.length == 1){
                        selectionPage(eventItem[0].getText());
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(true);
                    } else if (eventItem.length > 1){
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(false);
                    } else {
                        setTableDeselect();
                        parent.setVisiable();
                    }
                }else {
                    ErlLogger.debug("select2 null :!");
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                setTableDeselect();
                parent.setVisiable();
            }
        });

/*		table.addMouseListener(new MouseAdapter(){
            public void mouseDown(MouseEvent event) {
                if (event.getSource() != null ){
                    setChaTableDeselect();
                    TableItem[] eventItem = table.getSelection();

                    if (eventItem.length == 1){
                        selectionPage(eventItem[0].getText());
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(true);
                    } else if (eventItem.length > 1){
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(false);
                    } else {
                        setTableDeselect();
                        parent.setVisiable();
                    }
                }else {
                    ErlLogger.debug("select2 null :!");
                }
            }
        });*/
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
        removebutton.setEnabled(false);
        editbutton.setEnabled(false);
    }

    public void selectionPage(String key){
            Object selectedObj = tableMapStore.get(key);
            EwpCollections coll = (EwpCollections) selectedObj;
            ErlLogger.debug("select chaObj:"+coll.coll_id);

            parent.setUnVisiable();
            parent.chaPage.setTextEmpty("");
            parent.collPage.setText(coll);
    }

    public void setButtonListener(){
        newbutton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                setTableDeselect();
                parent.setVisiable();
                ErlLogger.debug("button listener:"+newbutton.getText());
                NewCollWizard wmain= new NewCollWizard(null, null, null);
                //Set<String> tmpset = tableMapStore.keySet();
                //ErlLogger.debug("tmpset size"+tmpset.size());
                wmain.initial(parent, tableMapStore, null);

            }
        });

        editbutton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                TableItem[] tmpItem = table.getSelection();
                if(tmpItem.length==1){
                    ErlLogger.debug("button listener:"+editbutton.getText());
                    NewCollWizard wmain= new NewCollWizard(null, null, null);
                    //Set<String> tmpset = tableMapStore.keySet();
                    //ErlLogger.debug("tmpset size"+tmpset.size());
                    wmain.initial(parent, tableMapStore, tmpItem[0].getText(0));
                }
            }
        });

        removebutton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                ErlLogger.debug("button listener:"+items.length);
                int len = items.length;
                if(len > 0){

                    table.deselectAll();
                    String tmpKey;

                    ArrayList<String> tmpList = new ArrayList<String>();
                    OtpErlangObject[] result = new OtpErlangObject[len];

                    for(int i =0; i< len; i++){
                        tmpKey = items[i].getText();
                        tmpList.add(tmpKey);
                        result[i]=new OtpErlangList(items[i].getText());
                    }
                    parent.erlBackend_removeColl(new OtpErlangList(result));
                    // refresh the tree composite and cha table composite
                    for(int i =0; i< len; i++){
                        tmpKey = tmpList.get(i);
                        parent.CollMap.remove(tmpKey);
                        // @FIXME
                        //parent.refreshTreeItemPage(tmpKey);
                    }
                    parent.setVisiable();

                    // refresh the tree composite and coll table composite
                    parent.coll_table.refreshTable();
                    parent.refreshTreePage();
                }
            }
        });
    }

}
