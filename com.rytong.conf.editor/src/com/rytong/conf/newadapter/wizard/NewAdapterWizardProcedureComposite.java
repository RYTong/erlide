package com.rytong.conf.newadapter.wizard;

import java.util.ArrayList;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.adapter.editor.AdapterEditorProcedureParamsDialog;
import com.rytong.conf.adapter.editor.EwpAdapter;
import com.rytong.conf.adapter.editor.EwpAdpaterList;
import com.rytong.conf.adapter.editor.EwpProcedure;
import com.rytong.conf.util.ConfParams;

public class NewAdapterWizardProcedureComposite {

    private NewAdapterWizardComonPage parent;
    private Group g_group;

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
    private boolean addFlagGlobal = true;
    protected EwpAdpaterList tmpEwpAdpList;
    private String error_msg=null;

    /**
     * 添加procedure想到内的，procedure编辑页面
     * @return
     */
    public NewAdapterWizardProcedureComposite NewAdapterWizardProcedureComposite(){
        return this;
    }

    /**
     * 初始化赋值，添加procedure edit 容器
     * @param parent
     * @param composite
     * @return
     */
    public EwpProcedure initial_composite(NewAdapterWizardComonPage parent, Composite composite){
        this.parent = parent;
        tmpEwpAdpList = parent.getAdpList();
        nowAdp = new EwpProcedure();
        g_group = new Group(composite, SWT.BORDER);
        g_group.setText("Procedure Detail:");
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(100,-5);
        comsite_form.top = new FormAttachment(0,38);
        comsite_form.bottom = new FormAttachment(100, -10);
        g_group.setLayoutData(comsite_form);
        g_group.setLayout(new FormLayout());
        initial_ui_element(g_group);
        setTextListener();
        setParamsTableListener();
        return nowAdp;
    }

    /**
     * 初始化容器，在容器内添加procedure相关元素
     */
    private void initial_ui_element(Composite composite){
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
        initial_combo_adapter(combo_adapter);

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
    }

    /**
     * 为procedure的adapter combo进行处事化操作
     * @param tmpCom
     */
    private void initial_combo_adapter(Combo tmpCom){
        Object[] tmpObj = tmpEwpAdpList.getAdapterArray();
        ArrayList<String> tmpArray = new ArrayList<String>();
        for (int i=0; i< tmpObj.length; i++){
            tmpArray.add(((EwpAdapter) tmpObj[i]).getName());
        }
        tmpCom.setItems((String[]) tmpArray.toArray(new String[tmpArray.size()]));
    }

    public void setCompositeVisiable(){
        g_group.setVisible(true);
    }
    public void setCompositeUnVisiable(){
        g_group.setVisible(false);
    }

    /**
     * 计算label在容器内的布局
     * @param i
     * @return
     */
    private FormData setLabelLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(0, 100);
        comsite_form.top = new FormAttachment(0,5+i*29);
        return comsite_form;
    }

    /**
     * 计算text在容器内的布局
     * @param i
     * @return
     */
    private FormData setTextLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,107);
        comsite_form.right = new FormAttachment(100, -10);
        comsite_form.top = new FormAttachment(0,5+i*28);
        return comsite_form;
    }

    /**
     * 计算parameters table在容器内的布局
     * @param i
     * @return
     */
    public FormData setParamsLayout(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(65, 10);
        tmp_form.right = new FormAttachment(100, -10);
        tmp_form.top = new FormAttachment(0, 240+i*30);
        return tmp_form;
    }

    /**
     * 为text 添加监听处理，并进行相关复制操作
     */
    public void setTextListener(){
        text_id.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tId = text_id.getText();
                nowAdp.setId(tId);
                check_error();
            }
        });
        combo_adapter.addModifyListener(new ModifyListener(){
            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tAdapter = combo_adapter.getText();
                nowAdp.setAdapter(tAdapter);
                ErlLogger.debug("tAdapter:"+tAdapter);
                check_error();
            }

        });
        text_path.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tPath = text_path.getText();
                nowAdp.setPath(tPath);
                ErlLogger.debug("text_path:"+tPath);
            }

        });

        text_sample.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tSample= text_sample.getText();
                ErlLogger.debug("text_sample:"+tSample);
                nowAdp.setSample(tSample);
            }

        });

        text_return.addModifyListener(new ModifyListener(){

            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tReturn = text_return.getText();
                nowAdp.setReturnType(tReturn);
                ErlLogger.debug("text_return:"+tReturn);
                check_exist();
            }

        });

        combo_log.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                boolean tLog = getReFlag(combo_log.getSelectionIndex());
                nowAdp.setLog(tLog);
                ErlLogger.debug("combo_log:"+tLog);
            }
        });
        combo_code.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                boolean tCode= getReFlag(combo_code.getSelectionIndex());
                nowAdp.setCode(tCode);
                ErlLogger.debug("combo_code:"+tCode);
            }
        });
        combo_usesample.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                boolean tUseSample = getReFlag(combo_usesample.getSelectionIndex());
                nowAdp.setUseSample(tUseSample);
                ErlLogger.debug("combo_usesample:"+tUseSample);
            }
        });
    }

    /**
     * 设置parameter table 为未选中的状态
     */
    public void setTableDeselect(){
        paramsTab.deselectAll();
        removeBut.setEnabled(false);
        editBut.setEnabled(false);
    }

    /**
     * 为parameter table添加监听事件，用来动态改变删除及修改按键的状态
     */
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
                ConfParams tmpParam = new ConfParams();
                AdapterEditorProcedureParamsDialog tmpDialog =
                        new AdapterEditorProcedureParamsDialog(parent.getComposite().getShell(), tmpParam, addFlagGlobal);
                tmpDialog.open();
                if(tmpDialog.getReturnCode() == Window.OK){
                    nowAdp.addParam(tmpParam);
                    new TableItem(paramsTab, SWT.NONE).setText(new String[] {tmpParam.getKey(), tmpParam.getValue()});
                }

            }
        });

        editBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("editBut!");
                int[] selection = paramsTab.getSelectionIndices();
                if (selection.length > 0 ){
                    String key= paramsTab.getItem(selection[0]).getText(0);
                    ConfParams tmpParam = nowAdp.getParam(key);
                    AdapterEditorProcedureParamsDialog tmpDialog =
                            new AdapterEditorProcedureParamsDialog(parent.getComposite().getShell(), tmpParam, !addFlagGlobal);
                    tmpDialog.open();
                    if(tmpDialog.getReturnCode() == Window.OK){
                        nowAdp.refreshParam(key, tmpParam);
                        paramsTab.remove(selection[0]);
                        new TableItem(paramsTab, selection[0], SWT.NONE).setText(new String[] {tmpParam.getKey(), tmpParam.getValue()});
                    }
                }

            }
        });

        removeBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("removeBut!");
                int[] selection = paramsTab.getSelectionIndices();
                if (selection.length > 0 ){
                    for(int i =0; i < selection.length ; i++){
                        String key= paramsTab.getItem(selection[i]).getText(0);
                        paramsTab.remove(selection[i]);
                        nowAdp.removeParam(key);
                    }
                }
            }
        });

        remAllBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("remAllBut!");
                paramsTab.removeAll();
                nowAdp.removeParams();
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

    /**
     * 判断是否在wizard中显示error
     */
    private void check_error(){
        if (tmpEwpAdpList.checkExistedProcedure(text_id.getText(), combo_adapter.getText())){
            error_msg = "该procedure已经存在！";
            setError(error_msg);
            setPageCompleteState(false);
        } else if (nowAdp.checkNeededValue()){
            setError(error_msg=null);
            setPageCompleteState(true);
        }else {
            setError(error_msg=null);
            setPageCompleteState(false);
        }
    }

    private void check_exist(){
        if (nowAdp.checkNeededValue() && error_msg == null){
            setPageCompleteState(true);
        }else {
            setPageCompleteState(false);
        }
    }

    private void setError(String msg){
        parent.setErrorMessage(msg);
    }

    protected void setPageCompleteState(boolean state){
        parent.setPageComplete(state);
    }

}
