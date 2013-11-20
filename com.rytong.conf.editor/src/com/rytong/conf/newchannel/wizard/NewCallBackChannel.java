package com.rytong.conf.newchannel.wizard;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.util.ChannelConfFileUtil;
import com.rytong.conf.util.ChannelConfUtil;
import com.rytong.conf.util.ChannelConfUtil.viewDiaolog;

public class NewCallBackChannel {
    private NewChaWizard wizard;
    private NewChaWizardViewPage parent;
    private ChannelConfUtil confUtil;
    private ChannelCallBackTemplate templateBuilder;

    private EwpChannels cha;
    private String selectId;

    private Label srcLebal;
    private Button srcBut;
    private Label csLabel;
    private Button csBut;

    protected Composite composite;
    private Table table;
    private Button addBut;
    private Button editBut;
    private Button removeBut;
    private Button remAllBut;

    private boolean addFlagGlobal = true;

    public NewCallBackChannel(NewChaWizard wizard, NewChaWizardViewPage parent){
        this.wizard = wizard;
        this.parent = parent;
        cha = wizard.cha;
        this.selectId = wizard.selectId;
        confUtil = parent.confUtil;
        templateBuilder = new ChannelCallBackTemplate(wizard);
    }

    public Composite initial_composite(){

        composite = new Group(parent.parentcomposite, SWT.BORDER);

        FormData template_form = new FormData();
        template_form.left = new FormAttachment(0);
        template_form.right = new FormAttachment(100,-5);
        template_form.top = new FormAttachment(0,30);
        template_form.bottom = new FormAttachment(100, -3);
        composite.setLayoutData(template_form);
        composite.setLayout(new FormLayout());
        //templateGroup.setText("Template List");

        srcLebal = new Label(composite, SWT.NONE);
        srcLebal.setText("生成辅助代码");
        srcLebal.setLayoutData(setLabelForm( 0));
        srcBut = new Button(composite, SWT.BORDER | SWT.CHECK);
        srcBut.setLayoutData(setButForm(0));
        srcBut.addListener(SWT.Selection, setCheckBoxListener());

        csLabel = new Label(composite, SWT.NONE);
        csLabel.setText("生成CS模板");
        csLabel.setLayoutData(setLabelForm(1));
        csBut = new Button(composite, SWT.BORDER | SWT.CHECK);
        csBut.setLayoutData(setButForm(1));
        csBut.addListener(SWT.Selection, setCheckBoxListener());

        draw_table(composite);

        initial_element();

        ErlLogger.debug("entry:"+cha.cha_entry);

        return composite;
    }


    public void draw_table(Composite composite){
        table = new Table(composite, SWT.BORDER | SWT.MULTI);
        TableColumn tranColumn = new TableColumn(table, SWT.NONE);
        tranColumn.setText("tranCode");
        tranColumn.setWidth(200);
        TableColumn viewColumn = new TableColumn(table, SWT.NONE);
        viewColumn.setText("viewName");
        viewColumn.setWidth(200);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        FormData table_form = new FormData();
        table_form.top = new FormAttachment(0, 40);
        table_form.left = new FormAttachment(0, 20);
        table_form.right = new FormAttachment(100, -170);
        table_form.bottom = new FormAttachment(100, -20);
        table.setLayoutData(table_form);

        addBut = new Button(composite, SWT.BORDER | SWT.CENTER);
        addBut.setText("Add...");
        addBut.setLayoutData(setButtonLayout(0));

        editBut = new Button(composite, SWT.BORDER | SWT.CENTER);
        editBut.setText("Edit");
        editBut.setLayoutData(setButtonLayout(1));

        removeBut = new Button(composite, SWT.BORDER | SWT.CENTER);
        removeBut.setText("Remove");
        removeBut.setLayoutData(setButtonLayout(2));

        remAllBut = new Button(composite, SWT.BORDER | SWT.CENTER);
        remAllBut.setText("Remove All");
        remAllBut.setLayoutData(setButtonLayout(3));

        setTableListener();
        setButtonListener();
    }

    public void initial_element(){
        table.setEnabled(false);
            addBut.setEnabled(false);
            editBut.setEnabled(false);
            removeBut.setEnabled(false);
            remAllBut.setEnabled(false);

    }

    public void initial_text(){
        editBut.setEnabled(false);
        removeBut.setEnabled(false);
        table.setEnabled(true);
        HashMap<TableItem, OldCallbackParams> tmpNewViewMap = cha.add_view.newViewMap;
        Map<TableItem, OldCallbackParams> map = tmpNewViewMap;
        Iterator<Entry<TableItem, OldCallbackParams>> chaiter = map.entrySet().iterator();
        while(chaiter.hasNext()){
            Entry<TableItem, OldCallbackParams> tmpIter = chaiter.next();
            OldCallbackParams viewParam = tmpIter.getValue();
            addParamsItem(table, viewParam);
        }
        if (table.getItemCount() == 0)
            remAllBut.setEnabled(false);

    }

    public TableItem addParamsItem(Table table, OldCallbackParams tmpParams){
        TableItem tmpItem = new TableItem(table, SWT.NONE);
        tmpItem.setText(new String[] {tmpParams.tranCode, tmpParams.viewName});
        return tmpItem;
    }

    public Listener setCheckButListener(){
        Listener listener = new Listener(){
            @Override
            public void handleEvent(Event event) {
                // TODO Auto-generated method stub
                Button tmpBut = (Button) event.widget;

                if (tmpBut == csBut) {
                    cha.add_view.setCsFlag(ChannelConfUtil.getFlag(tmpBut.getSelection()));
                } else {
                    ErlLogger.debug("else button!");
                }
            }

        };
        return listener;
    }

    private static TableItem[] table_item;

    public void setTableListener(){
        table.addMouseListener(new MouseAdapter(){
             public void mouseDown(MouseEvent event) {
                    ErlLogger.debug("table event.");
                    if (event.getSource() != null){
                        table_item = table.getSelection();
                        if (table_item.length == 1){
                            ErlLogger.debug("table event:"+table_item.length);
                            editBut.setEnabled(true);
                            removeBut.setEnabled(true);
                        } else if (table_item.length > 1){
                            ErlLogger.debug("table event:"+table_item.length);
                            editBut.setEnabled(false);
                            removeBut.setEnabled(true);
                            remAllBut.setEnabled(true);
                        } else {
                            editBut.setEnabled(false);
                            removeBut.setEnabled(false);
                        }
                    } else {
                        ErlLogger.debug("table event: null");
                    }
             }
        });
    }

    public void setButtonListener(){
        addBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("add button!");
                OldCallbackParams tmpOld = new OldCallbackParams();
                viewDiaolog newDialog = confUtil.newViewDiaolog(parent.getShell(), addFlagGlobal, tmpOld, cha.cha_id);
                newDialog.open();
                ErlLogger.debug("dialog result :"+newDialog.getReturnCode());
                if (newDialog.getReturnCode()==Window.OK){
                    TableItem tmpItem = new TableItem(table, SWT.BORDER);
                    tmpOld.viewName = tmpOld.viewName.replace(" ", "");
                    tmpItem.setText(new String[]{tmpOld.tranCode, tmpOld.viewName});
                    cha.add_view.addNewView(tmpItem, tmpOld);
                    ErlLogger.debug("new list:"+cha.add_view.newViewMap.size());
                    remAllBut.setEnabled(true);
                }


            }
        });

        editBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("edit button!");
                OldCallbackParams tmpOld = cha.add_view.getNewView(table_item[0]);
                ErlLogger.debug("edit button:"+tmpOld.tranCode);
                viewDiaolog newDialog =  confUtil.newViewDiaolog(parent.getShell(), !addFlagGlobal, tmpOld, cha.cha_id);
                newDialog.open();
                ErlLogger.debug("dialog result :"+newDialog.getReturnCode());
                if (newDialog.getReturnCode()==Window.OK){

                    tmpOld.viewName = tmpOld.viewName.replace(" ", "");
                    table_item[0].setText(new String[]{tmpOld.tranCode, tmpOld.viewName});
                    cha.add_view.refreshNewView(table_item[0], tmpOld);
                    ErlLogger.debug("new list:"+cha.add_view.newViewMap.size());
                }

            }
        });

        removeBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("remove button:"+table_item[0]);
                if (table.getItemCount() != 0){
                    //table.remove(table_item[0]);
                    table_item[0].dispose();
                    cha.add_view.removeNewView(table_item[0]);
                    remAllBut.setEnabled(false);
                    removeBut.setEnabled(false);
                }
            }
        });

        remAllBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("remove all button!");
                table.removeAll();
                cha.add_view.clearNewView();
                removeBut.setEnabled(false);
                remAllBut.setEnabled(false);
            }
        });
    }

    private boolean srcFlag = false;
    private boolean csFlag = false;

    public Listener setCheckBoxListener(){
        Listener tmpListener = new Listener(){
            @Override
            public void handleEvent(Event event) {
                Button tmpBut = (Button) event.widget;
                if (tmpBut == srcBut){
                    ErlLogger.debug("srcBut bug!");
                    srcFlag = ChannelConfUtil.getFlag(tmpBut.getSelection());
                    cha.getViewMap().setNCBSrcFlag(srcFlag);
                    ErlLogger.debug("srcBut bug:"+srcFlag);
                    setTableSt();
                    //testSrc();
                } else if (tmpBut == csBut) {
                    ErlLogger.debug("csBut bug!");
                    csFlag = ChannelConfUtil.getFlag(tmpBut.getSelection());
                    cha.getViewMap().setNCBCsFlag(csFlag);
                    ErlLogger.debug("csBut bug:"+csFlag);
                    setTableSt();
                    //testCs();
                } else {
                    ErlLogger.debug("else bug!");
                }
            }
        };
        return tmpListener;
    }

    public void setTableSt(){
        if (srcFlag || csFlag ){
            table.setEnabled(true);
            addBut.setEnabled(true);
            if (table.getItemCount()>0)
                remAllBut.setEnabled(true);
        } else {
            table.setEnabled(false);
            addBut.setEnabled(false);
            editBut.setEnabled(false);
            removeBut.setEnabled(false);
            remAllBut.setEnabled(false);
        }
    }

    public void set_visiable(){
        composite.setVisible(true);
    }

    public void set_unvisiable(){
        composite.setVisible(false);
    }


    /**
     * form data
     */
    private FormData setLabelForm(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(0,40+i*120);
        tmp_form.right = new FormAttachment(0, 120+i*120);
        tmp_form.top = new FormAttachment(0, 10);
        return tmp_form;
    }

    private FormData setButForm(int i){
        FormData tmp_form = new FormData();

        tmp_form.left = new FormAttachment(0,20+i*120);
        tmp_form.right = new FormAttachment(0, 40+i*120);
        tmp_form.top = new FormAttachment(0,10);
        return tmp_form;
    }

    private FormData setButtonLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(100, -150);
        comsite_form.right = new FormAttachment(100, -20);
        comsite_form.top = new FormAttachment(0, 45+i*35);
        return comsite_form;
    }

    private void testCs(){
        templateBuilder.createNCBCsTemplate(cha);
    }

    private void testSrc(){
        templateBuilder.createSrcTemplate(cha);
    }
}
