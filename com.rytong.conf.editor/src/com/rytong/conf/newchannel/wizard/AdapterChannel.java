package com.rytong.conf.newchannel.wizard;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.util.ChannelConfUtil;
import com.rytong.conf.util.ChannelConfUtil.AddDiaolog;
import com.rytong.conf.util.ChannelConfUtil.paramsDiaolog;

public class AdapterChannel {
    private AdapterChannel adapterChannel = this;
    private NewChaWizard wizard;
    private NewChaWizardViewPage parent;
    private EwpChannels cha;
    private String selectId;

    private Composite parentcomposite;


    private Group offGroup;
    private Group templateGroup;

    private Label separateLine;
    private Label entryLabel;
    private Label templateFlag;
    private Label csFlag;
    private Label offFlag;


    private Button templateBut;
    private Button csBut;
    private Button offBut;
    //private Combo chaEntryCom;

    private Label imagesLabel;
    private Label cssLabel;
    private Label luaLabel;
    private Label xhtmlLabel;
    private Label channelLabel;

    private Button imagesBut;
    private Button cssBut;
    private Button luaBut;
    private Button xhtmlBut;
    private Button channelBut;

    private Label plateLabel;
    private Label resolutionLabel;

    private Combo plateCom;
    private Combo resolutionCom;
    private static String[] plateforms = {"IPhone", "Android", "WinPhone"};
    private static String[] iPhoneRes = {"320-480", "640-960", "640-1136"};
    private static String[] androidRes = {"320-480", "480-800", "640-960", "320-240", "600-1024", "1280-720", "1280-800"};
    private static String PLAT_IP="iphone";
    private static String PLAT_AD="android";
    private static String PLAT_WP="winphone";

    private static String imagesStr = "images";
    private static String cssStr = "css";
    private static String luaStr = "lua";
    private static String xhtmlStr = "xhtml";
    private static String jsonStr = "json";

    private WizarParams  cha_view;

    private boolean addFlagGlobal = true;
    private int group_status = 0;
    private int mod_status=4;
    private int cs_status=2;
    private int off_status=1;
    private ChannelConfUtil confUtil ;


    public AdapterChannel(NewChaWizard wizard, NewChaWizardViewPage parent){
        this.wizard = wizard;
        this.parent = parent;
        cha = wizard.cha;
        cha_view = cha.add_view;
        this.selectId = wizard.selectId;
        parentcomposite = parent.parentcomposite;
        confUtil = parent.confUtil;
    }

    public void initial_text(){
        //ErlLogger.debug("initial text:"+cha.cha_id);
        Iterator<AdapterView> tmpIt = cha.add_view.storeList.iterator();
        ErlLogger.debug("tmpMap:"+cha.add_view.storeList.size());
        cha.add_view.removeMapAll();

        while(tmpIt.hasNext()){
            AdapterView tmpVIews= tmpIt.next();
            TableItem tmpItem = addAdaptItem(tmpVIews);
            cha_view.addAdapterView(tmpItem, tmpVIews);
            remAllBut.setEnabled(true);
        }
    }

    public  void set_unvisiable(){
        templateGroup.setVisible(false);
    }

    public  void set_visiable(){
        templateGroup.setVisible(true);
    }

    public void initial_composite(){
        adapter_group(parentcomposite);
    }

    public void adapter_group(Composite parentcomposite){

        templateGroup = new Group(parentcomposite, SWT.BORDER);

        FormData template_form = new FormData();
        template_form.left = new FormAttachment(0);
        template_form.right = new FormAttachment(100,-5);
        template_form.top = new FormAttachment(0,30);
        template_form.bottom = new FormAttachment(100, -3);
        templateGroup.setLayoutData(template_form);
        templateGroup.setLayout(new FormLayout());
        //templateGroup.setText("Template List");


        templateFlag = new Label(templateGroup, SWT.NONE);
        templateFlag.setText("生成辅助代码");
        templateFlag.setLayoutData(setFlagLableForm( 0));
        templateBut = new Button(templateGroup, SWT.BORDER | SWT.CHECK);
        templateBut.setLayoutData(setFlagButForm(0));

        csFlag = new Label(templateGroup, SWT.NONE);
        csFlag.setText("生成CS模板");
        csFlag.setLayoutData(setFlagLableForm(1));
        csBut = new Button(templateGroup, SWT.BORDER | SWT.CHECK);
        csBut.setLayoutData(setFlagButForm(1));


        offFlag = new Label(templateGroup, SWT.NONE);
        offFlag.setText("生成离线资源文件");
        offFlag.setLayoutData(setFlagLableForm(2));
        offBut = new Button(templateGroup, SWT.BORDER | SWT.CHECK);
        offBut.setLayoutData(setFlagButForm(2));
        offline_group(templateGroup);

        templateBut.addListener(SWT.Selection, setCheckButListener());
        csBut.addListener(SWT.Selection, setCheckButListener());
        offBut.addListener(SWT.Selection, setCheckButListener());

        ErlLogger.debug("entry:"+cha.cha_entry);
        //if (cha.cha_entry.equalsIgnoreCase(EwpChannels.CHANNEL_ADAPTER)){
        adapter_table(templateGroup);
        setGroupStatus(false);
        setInitialOffConf();
    }

    public void offline_group(Group templateGroup){
        offGroup = new Group(templateGroup, SWT.BORDER);
        FormData off_form = new FormData();
        off_form.left = new FormAttachment(0,3);
        off_form.right = new FormAttachment(100,-3);
        off_form.top = new FormAttachment(0,28);
        off_form.bottom = new FormAttachment(0, 90);
        offGroup.setLayoutData(off_form);
        offGroup.setLayout(new FormLayout());

        plateLabel = new Label(offGroup, SWT.NONE);
        plateLabel.setText("平台:");
        plateLabel.setLayoutData(setComLableForm(0));
        plateCom = new Combo(offGroup, SWT.BORDER);
        plateCom.setItems(plateforms);
        plateCom.setLayoutData(setComButForm(0));

        resolutionLabel = new Label(offGroup, SWT.NONE);
        resolutionLabel.setText("分辨率:");
        resolutionLabel.setLayoutData(setComLableForm(1));
        resolutionCom = new Combo(offGroup, SWT.BORDER);
        resolutionCom.setItems(iPhoneRes);
        resolutionCom.setLayoutData(setComButForm(1));

        imagesLabel = new Label(offGroup, SWT.NONE);
        imagesLabel.setText("images");
        imagesLabel.setLayoutData(setOffLableForm(0));
        imagesBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
        imagesBut.setLayoutData(setOffButForm(0));

        cssLabel = new Label(offGroup, SWT.NONE);
        cssLabel.setText("css");
        cssLabel.setLayoutData(setOffLableForm(1));
        cssBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
        cssBut.setLayoutData(setOffButForm(1));

        luaLabel = new Label(offGroup, SWT.NONE);
        luaLabel.setText("lua");
        luaLabel.setLayoutData(setOffLableForm(2));
        luaBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
        luaBut.setLayoutData(setOffButForm(2));

        xhtmlLabel = new Label(offGroup, SWT.NONE);
        xhtmlLabel.setText("xhtml");
        xhtmlLabel.setLayoutData(setOffLableForm(3));
        xhtmlBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
        xhtmlBut.setLayoutData(setOffButForm(3));

        channelLabel = new Label(offGroup, SWT.NONE);
        channelLabel.setText("json");
        channelLabel.setLayoutData(setOffLableForm(4));
        channelBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
        channelBut.setLayoutData(setOffButForm(4));

        plateCom.addListener(SWT.Modify , setComboListener());
        resolutionCom.addListener(SWT.Modify, setComboListener());
        imagesBut.addListener(SWT.Selection, setOffListener());
        cssBut.addListener(SWT.Selection, setOffListener());
        luaBut.addListener(SWT.Selection, setOffListener());
        xhtmlBut.addListener(SWT.Selection, setOffListener());
        channelBut.addListener(SWT.Selection, setOffListener());

        imagesBut.setSelection(true);
        cssBut.setSelection(true);
        luaBut.setSelection(true);
        xhtmlBut.setSelection(true);
        channelBut.setSelection(true);

        //offGroup.setEnabled(false);

    }


    private Table adapter_table;
    private Table params_table;
    private Button addBut;
    private Button editBut;
    private Button removeBut;
    private Button remAllBut;
    private Group treeGroup;
    public void adapter_table(Composite parent){

        treeGroup = new Group(parent, SWT.BORDER);
        //treeGroup.setText("Views");
        treeGroup.setLayout(new FormLayout());
        FormData tGroup_form = new FormData();
        tGroup_form.left = new FormAttachment(0,5);
        tGroup_form.right = new FormAttachment(100,-5);
        tGroup_form.top = new FormAttachment(0,93);
        tGroup_form.bottom = new FormAttachment(100);
        treeGroup.setLayoutData(tGroup_form);


        adapter_table = new Table(treeGroup, SWT.BORDER| SWT.MULTI);
        TableColumn tranCodeColumn =	new TableColumn(adapter_table, SWT.NONE);
        tranCodeColumn.setText("TranCode");
        tranCodeColumn.setWidth(100);
        TableColumn viewNameColumn = new TableColumn(adapter_table, SWT.NONE);
        viewNameColumn.setText("ViewName");
        viewNameColumn.setWidth(100);
        TableColumn adapterColumn = new TableColumn(adapter_table, SWT.NONE);
        adapterColumn.setText("Adapter");
        adapterColumn.setWidth(100);
        TableColumn procedureColumn = new TableColumn(adapter_table, SWT.NONE);
        procedureColumn.setText("Procedure");
        procedureColumn.setWidth(100);

        adapter_table.setLinesVisible(true);
        adapter_table.setHeaderVisible(true);


        FormData adapte_form = new FormData();
        adapte_form.left = new FormAttachment(0,5);
        adapte_form.right = new FormAttachment(0,405);
        adapte_form.top = new FormAttachment(0,5);
        adapte_form.bottom = new FormAttachment(100, -5);
        adapter_table.setLayoutData(adapte_form);

        addBut = new Button(treeGroup,  SWT.CENTER);
        addBut.setText("Add...");
        addBut.setLayoutData(setButtonLayout(0));

        editBut = new Button(treeGroup, SWT.CENTER);
        editBut.setText("Edit");
        editBut.setLayoutData(setButtonLayout(1));
        editBut.setEnabled(false);

        removeBut = new Button(treeGroup, SWT.CENTER);
        removeBut.setText("Remove");
        removeBut.setLayoutData(setButtonLayout(2));
        removeBut.setEnabled(false);

        remAllBut = new Button(treeGroup, SWT.CENTER);
        remAllBut.setText("Remove All");
        remAllBut.setLayoutData(setButtonLayout(3));
        remAllBut.setEnabled(false);
        // add button listener
        setParamsListener();

        // create a parmas table
        params_table = new Table(treeGroup, SWT.BORDER | SWT.MULTI);
        FormData params_form = new FormData();
        params_form.left = new FormAttachment(0,510);
        params_form.right = new FormAttachment(100,-5);
        params_form.top = new FormAttachment(0,5);
        params_form.bottom = new FormAttachment(100, -5);
        params_table.setLayoutData(params_form);

        TableColumn getKeyColumn =	new TableColumn(params_table, SWT.NONE);
        getKeyColumn.setText("GetKey");
        getKeyColumn.setWidth(100);
        TableColumn requestKeyColumn = new TableColumn(params_table, SWT.NONE);
        requestKeyColumn.setText("RequestKey");
        requestKeyColumn.setWidth(100);
        TableColumn getTypeColumn = new TableColumn(params_table, SWT.NONE);
        getTypeColumn.setText("GetType");
        getTypeColumn.setWidth(100);


        params_table.setLinesVisible(true);
        params_table.setHeaderVisible(true);

        setTableListener();
        //ErlLogger.debug("main:"+
        //treeGroup.isFocusControl()+" el:"+adapter_table.isFocusControl() +"  "+params_table.isFocusControl());

    }

    public Listener setCheckButListener(){
        Listener listener = new Listener(){
            @Override
            public void handleEvent(Event event) {
                // TODO Auto-generated method stub
                Button tmpBut = (Button) event.widget;

                if (tmpBut == templateBut) {
                    ErlLogger.debug("templateBut button!");
                    boolean newFlag = ChannelConfUtil.getFlag(tmpBut.getSelection());
                    cha_view.setSrcFlag(newFlag);
                    setGroupSt(mod_status, newFlag);
                    //erlTest();
                } else if (tmpBut == csBut) {
                    ErlLogger.debug("csbut button!");
                    boolean tmpFlag = ChannelConfUtil.getFlag(tmpBut.getSelection());
                    cha_view.setCsFlag(tmpFlag);
                    setGroupSt(cs_status, tmpFlag);
                    //tmpTest();
                    //ErlLogger.debug("root:"+root.getFullPath().toString());
                } else if (tmpBut == offBut ){
                    ErlLogger.debug("offbut button!");
                    Boolean tmpFlag = tmpBut.getSelection();
                    cha_view.setOffFlag(ChannelConfUtil.getFlag(tmpFlag));
                    setGroupSt(off_status, tmpFlag);
                    //offTest();
                } else {
                    ErlLogger.debug("else button!");
                }
            }

        };
        return listener;
    }

    /**
     * channel type为adapter时,选择生成辅助代码或者模版，容器内容会有变化
     * @param st
     * @param flag
     */
    private void setGroupSt(int st, Boolean flag){
        if (flag == true){
            ErlLogger.debug("+group_status:"+group_status+"|"+st);
            if (st == off_status){
                imagesBut.setEnabled(true);
                cssBut.setEnabled(true);
                luaBut.setEnabled(true);
                xhtmlBut.setEnabled(true);
                channelBut.setEnabled(true);
                plateCom.setEnabled(true);
                resolutionCom.setEnabled(true);
            }
            if (group_status > 3);
            else if (st == mod_status) {
                treeGroup.setEnabled(true);
                adapter_table.setEnabled(true);
                params_table.setEnabled(true);
            } else if (st == cs_status){
                treeGroup.setEnabled(true);
                adapter_table.setEnabled(true);
                params_table.setEnabled(false);
            } else {
                treeGroup.setEnabled(true);
                adapter_table.setEnabled(true);
                params_table.setEnabled(false);
            }
            addStatus(st);
        } else {
            reduceStatus(st);
            ErlLogger.debug("-group_status:"+group_status+"|"+st);
            if (st == off_status){
                imagesBut.setEnabled(false);
                cssBut.setEnabled(false);
                luaBut.setEnabled(false);
                xhtmlBut.setEnabled(false);
                channelBut.setEnabled(false);
                plateCom.setEnabled(false);
                resolutionCom.setEnabled(false);
            }

            if (group_status >3);
            else if (group_status >0){
                params_table.setEnabled(false);
            } else {
                treeGroup.setEnabled(false);
                adapter_table.setEnabled(false);
                params_table.setEnabled(false);
            }
        }
    }

    private void addStatus(int st){
        group_status=group_status+st;
    }

    private void reduceStatus(int st){
        group_status=group_status-st;
    }

    public Listener setComboListener(){
        Listener listener = new Listener(){

            @Override
            public void handleEvent(Event event) {
                // TODO Auto-generated method stub
                Combo tmp = (Combo) event.widget;
                if (tmp == plateCom) {
                    cha_view.setPlatform(convertPlatForm(tmp.getText()));
                    ErlLogger.debug("p text:"+tmp.getText());
                    //androidRes
                } else if (tmp == resolutionCom) {
                    cha_view.setResolution(tmp.getText());
                    ErlLogger.debug("r text:"+tmp.getText());
                } else {
                    ErlLogger.debug("unkown combox!");
                }
            }
        };
        return listener;
    }

    private String convertPlatForm(String plat){
        String tmpPlat = plat.toLowerCase();
        if(tmpPlat.equalsIgnoreCase(PLAT_AD)){
            if (resolutionCom.getText().equalsIgnoreCase(""))
                resolutionCom.setItems(androidRes);
            return PLAT_AD;
        } else if (tmpPlat.equalsIgnoreCase(PLAT_IP)){
            if (resolutionCom.getText().equalsIgnoreCase(""))
                resolutionCom.setItems(iPhoneRes);
            return PLAT_IP;
        }else if (tmpPlat.equalsIgnoreCase(PLAT_WP)){
            return PLAT_WP;
        } else
            return plat;
    }


    public Listener setOffListener(){
        Listener listener = new Listener(){
            @Override
            public void handleEvent(Event event) {
                // TODO Auto-generated method stub
                Button tmpBut = (Button) event.widget;
                ErlLogger.debug("off But:"+cha_view.offDir.size());
                if (tmpBut == imagesBut) {
                    if (tmpBut.getSelection())
                        cha_view.offDir.add(imagesStr);
                    else
                        cha_view.offDir.remove(imagesStr);
                } else if(tmpBut == cssBut ){
                    if (tmpBut.getSelection())
                        cha_view.offDir.add(cssStr);
                    else
                        cha_view.offDir.remove(cssStr);
                } else if (tmpBut == luaBut){
                    if (tmpBut.getSelection())
                        cha_view.offDir.add(luaStr);
                    else
                        cha_view.offDir.remove(luaStr);
                } else if(tmpBut == xhtmlBut) {
                    if (tmpBut.getSelection())
                        cha_view.offDir.add(xhtmlStr);
                    else
                        cha_view.offDir.remove(xhtmlStr);
                } else if (tmpBut == channelBut){
                    if (tmpBut.getSelection())
                        cha_view.offDir.add(jsonStr);
                    else
                        cha_view.offDir.remove(jsonStr);
                } else {
                    ErlLogger.debug("unkown off button");
                }
            }
        };
        return listener;
    }

    private int focusFlag;
    private void setParamsListener(){
        addBut.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                ErlLogger.debug("add button!");
                if (focusFlag == 1){
                    AdapterView tmpView = new AdapterView();
                    AddDiaolog addLog = confUtil.newAddDiaolog(parent.getShell(), addFlagGlobal, adapterChannel, tmpView);
                    addLog.open();
                    if (addLog.getReturnCode() == Window.OK) {
                        TableItem tmpItem = addAdaptItem(tmpView);
                        adapter_table.setSelection(tmpItem);
                        cha_view.addAdapterView(tmpItem, tmpView);
                        remAllBut.setEnabled(true);
                        refresh_parase_table(tmpView);
                        TableItem[] selItem = adapter_table.getSelection();
                        if (selItem!=null){
                            adapt_item = selItem;
                        } else {
                            adapt_item[0] = tmpItem;
                        }
                    }

                } else {
                    AdapterView tmpViews = cha_view.getAdapterView(adapt_item[0]);
                    AdapterParams tmpParam = new AdapterParams();

                    paramsDiaolog addLog = confUtil.newParamsDiaolog(parent.getShell(), addFlagGlobal, tmpParam);
                    addLog.open();
                    if (addLog.getReturnCode() == Window.OK) {
                        addParamsItem(params_table, tmpParam);
                        tmpViews.paramsList.add(tmpParam);
                    }
                }

            }
        });

        editBut.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                ErlLogger.debug("editBut button!");
                if (focusFlag == 1){

                    AdapterView tmpView = cha_view.getAdapterView(adapt_item[0]);
                    AddDiaolog addLog = confUtil.newAddDiaolog(parent.getShell(), !addFlagGlobal, adapterChannel, tmpView);
                    addLog.open();
                    if (addLog.getReturnCode() == Window.OK) {
                        cha_view.removeAdapterView(adapt_item[0]);
                        cha_view.addAdapterView(updateAdaptItem(adapt_item[0], tmpView), tmpView);
                    }
                }
            }
        });

        removeBut.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                ErlLogger.debug("remove button!");
                if (focusFlag == 1){
                    int[] indeces= adapter_table.getSelectionIndices();
                    adapter_table.remove(indeces);
                    cha_view.removeAdapterView(adapt_item[0]);
                }
            }
        });

        remAllBut.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                ErlLogger.debug("remove all button!");
                if (focusFlag == 1){
                    cha_view.removeMapAll();
                    adapter_table.removeAll();
                }
                editBut.setEnabled(false);
                removeBut.setEnabled(false);
                remAllBut.setEnabled(false);
            }
        });
    }

    // add listener for adapter table
    public TableItem addAdaptItem(AdapterView tmpView){
        TableItem tmpItem = new TableItem(adapter_table, SWT.NONE);
        tmpItem.setText(new String[] {tmpView.tranCode, tmpView.viewName, tmpView.adapter, tmpView.procedure});
        return tmpItem;
    }

    public TableItem addParamsItem(Table table, AdapterParams tmpParams){
        TableItem tmpItem = new TableItem(table, SWT.NONE);
        tmpItem.setText(new String[] {tmpParams.getKey, tmpParams.requestKey, tmpParams.GetFrom});
        return tmpItem;
    }

    public TableItem updateAdaptItem(TableItem tmpItem , AdapterView tmpView){
        tmpItem.setText(new String[] {tmpView.tranCode, tmpView.viewName, tmpView.adapter, tmpView.procedure});
        return tmpItem;
    }


    public void refresh_parase_table( AdapterView tmpViews){
        if (tmpViews != null){
            ArrayList<AdapterParams> tmpList = tmpViews.paramsList;
            params_table.removeAll();
            if (tmpList.size() != 0){
                Iterator<AdapterParams> tmpIt = tmpList.iterator();
                while(tmpIt.hasNext()){
                    AdapterParams tmpParams = tmpIt.next();
                    addParamsItem(params_table, tmpParams);
                }
            }
        } else {
            params_table.removeAll();
        }

    }

    //set table listenr
    private static TableItem[] adapt_item;
    private static TableItem[] param_item;

    private void setTableListener(){

        adapter_table.addFocusListener(new FocusAdapter(){

            @Override
            public void focusGained(FocusEvent e) {
                // TODO Auto-generated method stub
                ErlLogger.debug("main:"+" el:"+adapter_table.isFocusControl() +"  "+params_table.isFocusControl());
                focusFlag = 1;
                Table tmp= (Table) e.getSource();
                ErlLogger.debug("co:"+tmp.getSelectionCount());
                addBut.setEnabled(true);
                if (tmp.getItemCount() >0 )
                    remAllBut.setEnabled(true);
                else
                    remAllBut.setEnabled(false);
            }
        });
        adapter_table.addMouseListener(new MouseAdapter(){
            public void mouseDown(MouseEvent event) {
                ErlLogger.debug("table event.");
                if (event.getSource() != null){
                    adapt_item = adapter_table.getSelection();
                    ErlLogger.debug("table event:"+adapt_item.length);
                    if (adapt_item.length == 1){
                        editBut.setEnabled(true);
                        removeBut.setEnabled(true);
                        refresh_parase_table(cha_view.getAdapterView(adapt_item[0]));
                    } else if (adapt_item.length > 1){
                        editBut.setEnabled(false);
                        removeBut.setEnabled(true);
                        refresh_parase_table(cha_view.getAdapterView(adapt_item[0]));
                    } else {
                        editBut.setEnabled(false);
                        removeBut.setEnabled(false);
                        refresh_parase_table(null);
                    }


                } else {
                    ErlLogger.debug("table event: null");
                }
            }
        });

        //params_table.addListener(SWT.SELECTED, newListener());

        params_table.addFocusListener(new FocusAdapter(){

            @Override
            public void focusGained(FocusEvent e) {
                // TODO Auto-generated method stub
                ErlLogger.debug(" adl:"+adapter_table.isFocusControl() +"  "+params_table.isFocusControl());
                focusFlag = 0;
                if (adapter_table.getSelection().length >0){
                    Table tmp= (Table) e.getSource();
                    ErlLogger.debug("co:"+tmp.getSelectionCount());
                    addBut.setEnabled(true);
                    if (tmp.getItemCount() >0 )
                        remAllBut.setEnabled(true);
                    else
                        remAllBut.setEnabled(false);
                } else {
                    addBut.setEnabled(false);
                    editBut.setEnabled(false);
                    removeBut.setEnabled(false);
                    remAllBut.setEnabled(false);
                }

            }
        });

        params_table.addMouseListener(new MouseAdapter(){
            public void mouseDown(MouseEvent event) {
                ErlLogger.debug("table event.");
                if (event.getSource() != null){
                    param_item = params_table.getSelection();
                    ErlLogger.debug("table event:"+param_item.length);
                    if (param_item.length == 1){
                        editBut.setEnabled(true);
                        removeBut.setEnabled(true);
                    } else if (param_item.length > 1){
                        editBut.setEnabled(false);
                        removeBut.setEnabled(true);
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



    public FormData setOffLableForm(int i ){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(0,25+i*100);
        tmp_form.right = new FormAttachment(0,100+i*100);
        tmp_form.top = new FormAttachment(0, 35);
        return tmp_form;
    }

    public FormData setOffButForm(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(0,5+i*100);
        tmp_form.right = new FormAttachment(0, 25+i*100);
        tmp_form.top = new FormAttachment(0,35);
        return tmp_form;
    }

    public FormData setComLableForm(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(0,3+i*350);
        tmp_form.right = new FormAttachment(0,50+i*350);
        tmp_form.top = new FormAttachment(0, 5);
        return tmp_form;
    }

    public FormData setComButForm(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(0,53+i*350);
        tmp_form.right = new FormAttachment(0, 300+i*350);
        tmp_form.top = new FormAttachment(0,5);
        return tmp_form;
    }


    public FormData setFlagLableForm(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(0,20+i*120);
        tmp_form.right = new FormAttachment(0,120+i*120);
        tmp_form.top = new FormAttachment(0, 5);
        return tmp_form;
    }

    public FormData setFlagButForm(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(0,5+i*120);
        tmp_form.right = new FormAttachment(0, 20+i*120);
        tmp_form.top = new FormAttachment(0,5);
        return tmp_form;
    }

    private FormData setButtonLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0, 405);
        comsite_form.right = new FormAttachment(0, 510);
        comsite_form.top = new FormAttachment(0,5+i*28);
        return comsite_form;
    }

    private void setGroupStatus(Boolean flag){
        if (flag == true){
            imagesBut.setEnabled(true);
            cssBut.setEnabled(true);
            luaBut.setEnabled(true);
            xhtmlBut.setEnabled(true);
            channelBut.setEnabled(true);
            plateCom.setEnabled(true);
            resolutionCom.setEnabled(true);
            treeGroup.setEnabled(true);
        } else {
            imagesBut.setEnabled(false);
            cssBut.setEnabled(false);
            luaBut.setEnabled(false);
            xhtmlBut.setEnabled(false);
            channelBut.setEnabled(false);
            plateCom.setEnabled(false);
            resolutionCom.setEnabled(false);
            addBut.setEnabled(false);
            editBut.setEnabled(false);
            removeBut.setEnabled(false);
            remAllBut.setEnabled(false);
            treeGroup.setEnabled(false);
        }
    }

    private void setInitialOffConf(){
        cha_view.offDir.add(imagesStr);
        cha_view.offDir.add(cssStr);
        cha_view.offDir.add(luaStr);
        cha_view.offDir.add(xhtmlStr);
        cha_view.offDir.add(jsonStr);
    }


    public Shell getShell(){
        return parent.getShell();
    }

    public EwpChannels getNowChannel(){
        return cha;
    }

    public boolean getGlobalAddFlag(){
        return addFlagGlobal;
    }
}
