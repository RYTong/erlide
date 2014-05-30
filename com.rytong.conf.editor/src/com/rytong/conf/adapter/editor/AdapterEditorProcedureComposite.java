package com.rytong.conf.adapter.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.erlide.backend.BackendCore;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.editor.pages.EwpCollections;
import com.rytong.conf.util.ConfParams;

public class AdapterEditorProcedureComposite {

    private Composite parentComposite;
    private AdapterEditorPage parent;
    private Composite composite;
    private Button addBut;
    private Button editBut;
    private Button removeBut;
    private Button remAllBut;

    private Label proc_id;
    private Label proc_adapter;
    private Label proc_return;
    private Label proc_path;
    private Label proc_log;
    private Label proc_code;
    private Label proc_sample;
    private Label proc_useSample;

    private Text text_id;
    private Text text_path;
    private Text text_sample;

    private Combo combo_adapter;
    private Combo text_return;
    private Combo combo_log;
    private Combo combo_code;
    private Combo combo_usesample;

    private Table paramsTab;
    private EwpProcedure nowAdp;
    private boolean leftTableSelect;

    private boolean addFlagGlobal = true;

    public AdapterEditorProcedureComposite(AdapterEditorPage parent){
        this.parentComposite = parent.getParent();
        this.parent = parent;
    }

    public void setProcedureEditorVisiable(){
        composite.setVisible(true);
    }

    public void setProcedureEditorUnVisiable(){
        composite.setVisible(false);
    }

    public void setLeftTableSelected(){
        ErlLogger.debug("leftTableSelect-----!");
        leftTableSelect = true;
    }

    public void setLeftTableDeSelected(){
        ErlLogger.debug("leftTableDeSelect-----!");
        leftTableSelect = false;
    }

    public void initial_right_com(){
        composite = new Composite(parentComposite, SWT.BORDER);
        FormData rightcomsite_form = new FormData();
        rightcomsite_form.left = new FormAttachment(41,3);
        rightcomsite_form.right = new FormAttachment(100 -3);
        rightcomsite_form.top = new FormAttachment(0,23);
        rightcomsite_form.bottom = new FormAttachment(100);
        composite.setLayoutData(rightcomsite_form);
        composite.setLayout(new FormLayout());


        proc_id = new Label(composite, SWT.NONE);
        proc_id.setText("Id*:");
        proc_id.setLayoutData(setLabelLayout(0));
        text_id = new Text(composite, SWT.BORDER);
        text_id.setLayoutData(setTextLayout(0));

        proc_adapter = new Label(composite, SWT.NONE);
        proc_adapter.setText("Adapter*:");
        proc_adapter.setLayoutData(setLabelLayout(1));
        combo_adapter = new Combo(composite, SWT.BORDER);
        combo_adapter.setLayoutData(setTextLayout(1));


        proc_path = new Label(composite, SWT.NONE);
        proc_path.setText("Path:");
        proc_path.setLayoutData(setLabelLayout(2));
        text_path = new Text(composite, SWT.BORDER);
        text_path.setLayoutData(setTextLayout(2));


        proc_return = new Label(composite, SWT.NONE);
        proc_return.setText("Return Type*:");
        proc_return.setLayoutData(setLabelLayout(3));
        text_return = new Combo(composite, SWT.BORDER);
        text_return.setItems(new String[]{"xml", "json"});
        text_return.select(0);
        text_return.setLayoutData(setTextLayout(3));

        proc_code = new Label(composite, SWT.NONE);
        proc_code.setText("CallBack Code:");
        proc_code.setLayoutData(setLabelLayout(4));
        combo_code = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
        // if gen_code is true, the procedure id shoule be {fun, mod}
        combo_code.setItems(new String[]{"true", "false"});
        combo_code.select(1);
        combo_code.setLayoutData(setTextLayout(4));


        proc_log = new Label(composite, SWT.NONE);
        proc_log.setText("Log:");
        proc_log.setLayoutData(setLabelLayout(5));
        combo_log = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
        combo_log.setItems(new String[]{"true", "false"});
        combo_log.select(1);
        combo_log.setLayoutData(setTextLayout(5));

        proc_useSample = new Label(composite, SWT.NONE);
        proc_useSample.setText("Use Sample:");
        proc_useSample.setLayoutData(setLabelLayout(6));
        combo_usesample = new Combo(composite, SWT.BORDER| SWT.READ_ONLY);
        combo_usesample.setItems(new String[]{"true", "false"});
        combo_usesample.select(1);
        combo_usesample.setLayoutData(setTextLayout(6));

        proc_sample = new Label(composite, SWT.NONE);
        proc_sample.setText("Sample Data:");
        proc_sample.setLayoutData(setLabelLayout(7));
        text_sample = new Text(composite, SWT.BORDER);
        text_sample.setLayoutData(setTextLayout(7));

        paramsTab = new Table(composite, SWT.BORDER | SWT.MULTI);
        paramsTab.setHeaderVisible(true);
        paramsTab.setLinesVisible(true);
        FormData table_form = new FormData();
        table_form.left = new FormAttachment(0,5);
        table_form.right = new FormAttachment(65, -10);
        table_form.top = new FormAttachment(0, 240);
        table_form.bottom = new FormAttachment(100, -10);
        paramsTab.setLayoutData(table_form);

        TableColumn key = new TableColumn(paramsTab, SWT.NONE);
        key.setText("Key");
        key.setWidth(150);

        TableColumn value = new TableColumn(paramsTab, SWT.NONE);
        value.setText("Value");
        value.setWidth(150);

        addBut= new Button(composite, SWT.CENTER);
        addBut.setText("New...");
        addBut.setLayoutData(setParamsLayout(0));

        editBut= new Button(composite, SWT.CENTER);
        editBut.setText("Edit");
        editBut.setLayoutData(setParamsLayout(1));

        removeBut = new Button(composite, SWT.CENTER);
        removeBut.setText("Remove");
        removeBut.setLayoutData(setParamsLayout(2));

        remAllBut= new Button(composite, SWT.CENTER);
        remAllBut.setText("Remove All");
        remAllBut.setLayoutData(setParamsLayout(3));
        setParamsTableListener();
        setTextListener();
    }

    private FormData setLabelLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(0, 100);
        comsite_form.top = new FormAttachment(0,5+i*29);
        return comsite_form;
    }

    private FormData setTextLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,107);
        comsite_form.right = new FormAttachment(100, -10);
        comsite_form.top = new FormAttachment(0,5+i*28);
        return comsite_form;
    }


    public FormData setParamsLayout(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(65, 10);
        tmp_form.right = new FormAttachment(100, -10);
        tmp_form.top = new FormAttachment(0, 240+i*30);
        return tmp_form;
    }

    private String nowAdpId ;
    private String nowAdpAdpter ;
    private String nowAdpPath ;
    private String nowAdpSample ;
    private String nowAdpReturnType ;
    private boolean nowAdpLog ;
    private boolean nowAdpCode ;
    private boolean nowAdpUseSample ;

    private boolean setTextFlag= false;

    public void setProcedureValue(EwpProcedure adp){
        setProcedureEditorVisiable();
        setTableDeselect();
        setTextFlag = true;
        nowAdp = adp;
        nowAdpId = adp.getId();
        nowAdpAdpter = adp.getAdapter();
        nowAdpPath = adp.getPath();
        nowAdpSample = adp.getSample();
        nowAdpReturnType = adp.getReturnType();
        nowAdpLog = adp.getLog();
        nowAdpCode = adp.getCode();
        nowAdpUseSample = adp.getUseSample();

        text_id.setText(nowAdpId);
        initial_combo_adapter();
        combo_adapter.setText(nowAdpAdpter);
        text_path.setText(nowAdpPath);
        text_sample.setText(nowAdpSample);
        text_return.setText(nowAdpReturnType);
        combo_log.select(getFlag(nowAdpLog));
        combo_code.select(getFlag(nowAdpCode));
        combo_usesample.select(getFlag(nowAdpUseSample));
        setParamsTable(adp);
        setTextFlag = false;
    }

    private void initial_combo_adapter(){
        Object[] tmpObj = parent.getAdpList().getAdapterArray();  //获取复制对象， 这一步可以忽略
        ArrayList<String> tmpArray = new ArrayList<String>(); // 初始化一个数组
//        String[] test = {"北京", "上海", "骨弓", "待定"}; // 要付的值
//        for (int i=0; i< test.length; i++){            // 赋值
//            tmpArray.add((String) test[i]);
//        }
//
//        combo_adapter.setItems((String [] ) tmpArray.toArray(new String[tmpArray.size()])); // 给combox复制
//        combo_adapter.select(0);  // 默认选中


        for (int i=0; i< tmpObj.length; i++){
            tmpArray.add(((EwpAdapter) tmpObj[i]).getName());
        }
        combo_adapter.setItems((String[]) tmpArray.toArray(new String[tmpArray.size()]));

    }

    public void setParamsTable(EwpProcedure adp){
        paramsTab.removeAll();
        //HashMap<String EwpChannels> tmpMap = parent;
        Map<String, ConfParams> map = adp.getParams();
        Iterator adpIter = map.entrySet().iterator();
        while (adpIter.hasNext()) {
            Map.Entry entry = (Map.Entry) adpIter.next();
            ConfParams obj = (ConfParams) entry.getValue();
            //ErlLogger.debug("cha key:"+key);
            new TableItem(paramsTab, SWT.NONE).setText(new String[] {obj.getKey(), obj.getValue()});
        }
    }

    public int getFlag(boolean flag){
        if(flag)
            return 0;
        else
            return 1;
    }

    public void setTableDeselect(){
        paramsTab.deselectAll();
        removeBut.setEnabled(false);
        editBut.setEnabled(false);
    }

    private void setParamsTableListener(){
        paramsTab.addMouseListener(new MouseAdapter(){
            public void mouseDown(MouseEvent event) {
                if (event.getSource() != null ){
                    TableItem[] eventItem = paramsTab.getSelection();

                    if (eventItem.length == 1){
                        removeBut.setEnabled(true);
                        editBut.setEnabled(true);
                    } else if (eventItem.length > 1){
                        removeBut.setEnabled(true);
                        editBut.setEnabled(false);
                    } else {
                        setTableDeselect();
                    }
                }else {
                    ErlLogger.debug("select null :!");
                }
            }
        });

        addBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("addbut!");
                if (leftTableSelect){
                    ConfParams tmpParam = new ConfParams();
                    AdapterEditorProcedureParamsDialog tmpDialog =
                            new AdapterEditorProcedureParamsDialog(parentComposite.getShell(), tmpParam, addFlagGlobal);
                    tmpDialog.open();
                    if(tmpDialog.getReturnCode() == Window.OK){
                        nowAdp.addParam(tmpParam);
                        new TableItem(paramsTab, SWT.NONE).setText(new String[] {tmpParam.getKey(), tmpParam.getValue()});
                        OtpErlangTuple tmpTup = nowAdp.editParameters();
                        parent.erlBackend_editProcedure(tmpTup);
                    }
                }
            }
        });

        editBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("editBut!");
                if (leftTableSelect ){
                    int[] selection = paramsTab.getSelectionIndices();
                    if (selection.length > 0 ){
                        String key= paramsTab.getItem(selection[0]).getText(0);
                        ConfParams tmpParam = nowAdp.getParam(key);
                        AdapterEditorProcedureParamsDialog tmpDialog =
                                new AdapterEditorProcedureParamsDialog(parentComposite.getShell(), tmpParam, !addFlagGlobal);
                        tmpDialog.open();
                        if(tmpDialog.getReturnCode() == Window.OK){
                            nowAdp.refreshParam(key, tmpParam);
                            paramsTab.remove(selection[0]);
                            new TableItem(paramsTab, selection[0], SWT.NONE).setText(new String[] {tmpParam.getKey(), tmpParam.getValue()});
                            OtpErlangTuple tmpTup = nowAdp.editParameters();
                            parent.erlBackend_editProcedure(tmpTup);
                        }
                    }
                }

            }
        });

        removeBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("removeBut!");
                if (leftTableSelect ){

                    int[] selection = paramsTab.getSelectionIndices();
                    if (selection.length > 0 ){
                        for(int i =0; i < selection.length ; i++){
                            String key= paramsTab.getItem(selection[i]).getText(0);
                            paramsTab.remove(selection[i]);
                            nowAdp.removeParam(key);
                        }
                        OtpErlangTuple tmpTup = nowAdp.editParameters();
                        parent.erlBackend_editProcedure(tmpTup);
                    }
                }
            }
        });

        remAllBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("remAllBut!");
                if (leftTableSelect){
                    paramsTab.removeAll();
                    nowAdp.removeParams();
                    OtpErlangTuple tmpTup = nowAdp.editParameters();
                    parent.erlBackend_editProcedure(tmpTup);
                }
            }
        });
    }

    public void refreshProcedure(String index, EwpProcedure tmpProcedure){
        EwpAdpaterList adpList = parent.getAdpList();
        if (index == null)
            adpList.refreshProcedureOther(tmpProcedure);
        else if (index.equalsIgnoreCase(tmpProcedure.getSid()))
            adpList.refreshProcedureId(tmpProcedure);
        else if(index.equalsIgnoreCase(tmpProcedure.getSadapter()))
            adpList.refreshProcedureAdapter(tmpProcedure);
        parent.refreshTree();
    }



    public void setTextListener(){
        text_id.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tId = text_id.getText();

                if(leftTableSelect && !setTextFlag)
                {
                    String oldId = nowAdp.getId();
                    nowAdp.setId(tId);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterId();
                    ErlLogger.debug("text_id:"+tId);
                    parent.erlBackend_editProcedure(tmpTup);
                    nowAdp.setoldId(oldId);
                    refreshProcedure(nowAdp.getSid(), nowAdp);

                }
            }
        });
        combo_adapter.addModifyListener(new ModifyListener(){
            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tAdapter = combo_adapter.getText();
                if(leftTableSelect && !setTextFlag){
                    String oldAdapter = nowAdp.getAdapter();
                    nowAdp.setAdapter(tAdapter);
                    ErlLogger.debug("text_adapter:"+tAdapter);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterAdapter();
                    parent.erlBackend_editProcedure(tmpTup);
                    nowAdp.setoldAdapter(oldAdapter);
                    refreshProcedure(nowAdp.getSadapter(), nowAdp);
                }
            }

        });
        text_path.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tPath = text_path.getText();
                if(leftTableSelect && !setTextFlag){
                    nowAdp.setPath(tPath);
                    ErlLogger.debug("text_path:"+tPath);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterPath();
                    parent.erlBackend_editProcedure(tmpTup);
                    refreshProcedure(null, nowAdp);
                }
            }

        });

        text_sample.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tSample= text_sample.getText();
                if(leftTableSelect && !setTextFlag){
                    ErlLogger.debug("text_sample:"+tSample);
                    nowAdp.setSample(tSample);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterSample();
                    parent.erlBackend_editProcedure(tmpTup);
                    refreshProcedure(null, nowAdp);
                }
            }

        });

        text_return.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tReturn = text_return.getText();
                if(leftTableSelect && !setTextFlag){
                    nowAdp.setReturnType(tReturn);
                    ErlLogger.debug("text_return:"+tReturn);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterReturnType();
                    parent.erlBackend_editProcedure(tmpTup);
                    refreshProcedure(null, nowAdp);
                }
            }

        });

        combo_log.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                boolean tLog = getReFlag(combo_log.getSelectionIndex());
                if(leftTableSelect && !setTextFlag){
                    nowAdp.setLog(tLog);
                    ErlLogger.debug("combo_log:"+tLog);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterLog();
                    parent.erlBackend_editProcedure(tmpTup);
                    refreshProcedure(null, nowAdp);
                }
            }
        });
        combo_code.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                boolean tCode= getReFlag(combo_code.getSelectionIndex());
                if(leftTableSelect && !setTextFlag){
                    nowAdp.setCode(tCode);
                    ErlLogger.debug("combo_code:"+tCode);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterCode();
                    parent.erlBackend_editProcedure(tmpTup);
                }
            }
        });
        combo_usesample.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                boolean tUseSample = getReFlag(combo_usesample.getSelectionIndex());
                if(leftTableSelect && !setTextFlag){
                    nowAdp.setUseSample(tUseSample);
                    ErlLogger.debug("combo_usesample:"+tUseSample);
                    OtpErlangTuple tmpTup = nowAdp.editAdapterUseSample();
                    parent.erlBackend_editProcedure(tmpTup);
                    refreshProcedure(null, nowAdp);
                }
            }
        });
    }

    public boolean getReFlag(int flag){
        ErlLogger.debug("flag:"+flag);
        if(flag == 0)
            return true;
        else
            return false;
    }
}
