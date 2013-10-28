package com.rytong.conf.editor.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
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
import org.eclipse.swt.layout.FormLayout;
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
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.rytong.conf.editor.ChannelConfPlugin;
import com.rytong.conf.newchannel.wizard.NewChaWizard;
import com.rytong.conf.newcollection.wizard.NewCollWizard;

public class ChannelTable {

    private Composite pagecomposite;
    private Composite cha_table;
    private CollectionsPage parent;

    private Button newbutton;
    private Button editbutton;
    private Button removebutton;

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

        cha_table.setLayout(new FormLayout());

        Label label_all = new Label(cha_table, 0);
        label_all.setText("Channel Table");

        FormData labelcomsite_form = new FormData();
        labelcomsite_form.left = new FormAttachment(0, 5);
        labelcomsite_form.right = new FormAttachment(100, -5);
        labelcomsite_form.top = new FormAttachment(0, 3);
        //labelcomsite_form.bottom = new FormAttachment(100, -2);
        label_all.setLayoutData(labelcomsite_form);

        table = new Table(cha_table, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        FormData tablecomsite_form = new FormData();
        tablecomsite_form.left = new FormAttachment(0, 5);
        tablecomsite_form.right = new FormAttachment(100, -106);
        tablecomsite_form.top = new FormAttachment(0, 23);
        tablecomsite_form.bottom = new FormAttachment(100, -5);
        table.setLayoutData(tablecomsite_form);

        Transfer[] Types = new Transfer[]{TextTransfer.getInstance()};
        int operations = DND.DROP_MOVE|DND.DROP_COPY|DND.DROP_LINK;

        final DragSource source = new DragSource(table, operations);
        source.setTransfer(Types);
        setDragListener(source);


        TableColumn collId = new TableColumn(table, SWT.NONE);
        collId.setWidth(200);
        collId.setText("Channel Id");
        TableColumn collName = new TableColumn(table, SWT.NONE);
        collName.setWidth(150);
        collName.setText("Channel Name");

        newbutton = new Button(cha_table, SWT.LEFT);
        newbutton.setText(" New...");
        newbutton.setLayoutData(setButtonLayout(0));

        editbutton = new Button(cha_table, SWT.LEFT);
        editbutton.setText(" Edit ");
        editbutton.setLayoutData(setButtonLayout(1));

        removebutton = new Button(cha_table, SWT.LEFT);
        removebutton.setText(" Remove");
        removebutton.setLayoutData(setButtonLayout(2));

        setButtonListener();
        //collStateText.addModifyListener(listener);
        //cha_table.setVisible(false);
        setTableListener(table);
    }

    public FormData setButtonLayout(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(100, -103);
        tmp_form.right = new FormAttachment(100, -3);
        tmp_form.top = new FormAttachment(0, 23+i*30);
        return tmp_form;
    }

    public void refreshTable(){
        editbutton.setEnabled(false);
        removebutton.setEnabled(false);
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
        table.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent event) {
                if (event.getSource() != null ){
                    setCollTableDeselect();

                    TableItem[] eventItem = table.getSelection();
                    if (eventItem.length == 1){
                        selectionPage(eventItem[0].getText());
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(true);
                    } else if (eventItem.length > 1){
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(false);
                    } else {
                        table.deselectAll();
                        removebutton.setEnabled(false);
                        editbutton.setEnabled(false);
                        parent.setVisiable();
                    }
                }else {
                    ErlLogger.debug("select2 null :!");
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                table.deselectAll();
                parent.setVisiable();
            }
        });
/*        table.addMouseListener(new MouseAdapter(){
            public void mouseDown(MouseEvent event) {
                if (event.getSource() != null ){
                    setCollTableDeselect();

                    TableItem[] eventItem = table.getSelection();
                    if (eventItem.length == 1){
                        selectionPage(eventItem[0].getText());
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(true);
                    } else if (eventItem.length > 1){
                        removebutton.setEnabled(true);
                        editbutton.setEnabled(false);
                    } else {
                        table.deselectAll();
                        removebutton.setEnabled(false);
                        editbutton.setEnabled(false);
                        parent.setVisiable();
                    }
                }else {
                    ErlLogger.debug("select2 null :!");
                }

            }

        });
    */
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

    public void setButtonListener(){

        newbutton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                table.deselectAll();
                ErlLogger.debug("button listener:"+newbutton.getText());
                NewChaWizard wmain= new NewChaWizard(null, null, null);
                Set<String> tmpset = tableMapStore.keySet();
                ErlLogger.debug("tmpset size"+tmpset.size());
                wmain.initial(parent, tableMapStore, null);
                //parent.refreshTreePage();

            }
        });

        editbutton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                TableItem[] tmpItem = table.getSelection();

                URL url = ChannelConfPlugin.getDefault().getBundle()
                        .getEntry("templates/channel_oldCallback_cs_template.cs");
                //url.
                try {
                    File tempDir = new File(FileLocator.toFileURL(url).getFile());

                    ErlLogger.debug("test url:"+url);
                    ErlLogger.debug("test url:"+tempDir.exists());



                    String content = null;
                    InputStreamReader read = new InputStreamReader(new FileInputStream(tempDir),"utf-8");
                    BufferedReader reader = new BufferedReader(read);

                    String tempString = null;
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        // 显示行号
                        content=content+tempString;
                    }

                    ErlLogger.debug("File content:"+content);

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                ErlLogger.debug("button listener:"+editbutton.getText());
                if(tmpItem.length==1){
                    ErlLogger.debug("selection:"+tmpItem[0].getText(0));
                    NewChaWizard wmain= new NewChaWizard(null, null, null);
                    Set<String> tmpset = tableMapStore.keySet();
                    ErlLogger.debug("tmpset size"+tmpset.size());
                    wmain.initial(parent, tableMapStore, tmpItem[0].getText(0));
                }
                //parent.refreshTreePage();

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
                    parent.erlBackend_removeCha(new OtpErlangList(result));
                    // refresh the tree composite and cha table composite
                    for(int i =0; i< len; i++){
                        tmpKey = tmpList.get(i);
                        parent.ChaMap.remove(tmpKey);
                        parent.refreshTreeItemPage(tmpKey);
                    }
                    parent.setVisiable();
                    parent.cha_table.refreshTable();
                }
            }
    });
}


}