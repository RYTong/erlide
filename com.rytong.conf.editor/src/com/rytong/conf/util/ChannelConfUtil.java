package com.rytong.conf.util;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.editor.pages.EwpCollectionItems;
import com.rytong.conf.editor.pages.EwpCollections;
import com.rytong.conf.editor.pages.SelectedItem;
import com.rytong.conf.newchannel.wizard.AdapterChannel;
import com.rytong.conf.newchannel.wizard.AdapterParams;
import com.rytong.conf.newchannel.wizard.AdapterView;
import com.rytong.conf.newchannel.wizard.OldCallbackParams;


public class ChannelConfUtil {

    public ChannelConfUtil ChannelConfUtil(){
        return this;
    }

    public static Boolean getFlag(Boolean flag){
        if (flag == true)
            return true;
        else
            return false;
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


    public String checkObjectType(Object obj) {
        if (obj instanceof EwpCollections){
            ErlLogger.debug("-------------------EwpCollections type:"+(obj instanceof EwpCollections));
            return "0";
        } else if(obj instanceof EwpChannels){
            ErlLogger.debug("-------------------EwpChannels type:"+(obj instanceof EwpChannels));
            return "1";
        } else {
            ErlLogger.debug("-------------------EwpCollectionItems type:"+(obj instanceof EwpCollectionItems));
            ErlLogger.debug("null type:"+(obj == null));
            return "3";
        }
    }

    public String[] returnText(SelectedItem selectedObj) {
        if (selectedObj.type=="0"){
            EwpCollections coll = (EwpCollections) selectedObj.obj;
            return new String [] {coll.coll_id, coll.coll_name};
        } else if(selectedObj.type=="1"){
            EwpChannels chaObj = (EwpChannels) selectedObj.obj;
            return new String []{chaObj.cha_id, chaObj.cha_name};
        } else {
            return new String []{"",""};
        }
    }

    public OtpErlangTuple formAddCollParams(String selectId, EwpCollections collobj){
        OtpErlangObject[] request = new OtpErlangObject[9];
        request[0] = new OtpErlangList(selectId);
        request[1]=new OtpErlangList(collobj.coll_id);
        request[2]=new OtpErlangList(collobj.coll_app);
        request[3]=new OtpErlangList(collobj.coll_name);
        request[4]=new OtpErlangList(collobj.coll_url);

        request[5]=new OtpErlangList(collobj.coll_userid);
        request[6]=new OtpErlangList(collobj.coll_type);
        request[7]=new OtpErlangList(collobj.coll_state);
        OtpErlangList items = collobj.get_items_tuple();
        if (items!=null)
            request[8]=items;
        else
            request[8]=new OtpErlangList();
        return new OtpErlangTuple(request);
    }

    public OtpErlangTuple formAddChaParams(String selectId, EwpChannels cha){

        //Id, App, Name, Entry, Views, Props, State
        OtpErlangObject[] request = new OtpErlangObject[8];
        request[0] = new OtpErlangList(selectId);
        request[1]=new OtpErlangList(cha.cha_id);
        request[2]=new OtpErlangList(cha.cha_app);
        request[3]=new OtpErlangList(cha.cha_name);
        request[4]=new OtpErlangAtom(cha.cha_entry);
        OtpErlangList Views = cha.get_views_list();
        if (Views!=null)
            request[5]=Views;
        else
            request[5]=new OtpErlangList();
        request[6]=new OtpErlangInt(Integer.valueOf(cha.cha_state));

        OtpErlangList props = cha.get_props_tuple();
        if (props!=null)
            request[7]=props;
        else
            request[7]=new OtpErlangList();
        return new OtpErlangTuple(request);
    }


    public paramsDiaolog newParamsDiaolog(Shell parent, Boolean addFlag, AdapterParams params){
        return new paramsDiaolog( parent, addFlag, params);
    }

    public AddDiaolog newAddDiaolog(Shell parent, Boolean addFlag, AdapterChannel adapter, AdapterView view){
        return new AddDiaolog(parent, addFlag, adapter, view);
    }

    public viewDiaolog newViewDiaolog(Shell parent, Boolean addFlag, OldCallbackParams oldParams, String chaId){
        return new viewDiaolog(parent, addFlag, oldParams, chaId);
    }

    // -------------------------------------------------
    // add params dialog

    public class paramsDiaolog extends Dialog {
        private String dialogTitle="";
        private String dialogText="";
        private Button okBut;

        private Label dkeyLabel;
        private Label drkeyLabel;
        private Label dvalueLabel;

        private Text dkeyText;
        private Text drkeyText;
        private Combo dfromCom;

        private boolean addFlag;
        private AdapterParams params;

        public paramsDiaolog(Shell parent, Boolean addFlag, AdapterParams params){
            super(parent);
            this.addFlag = addFlag;
            this.params = params;
            if (addFlag){
                dialogTitle="Add Props...";
                dialogText = " Add a new View params.";
            } else{
                dialogTitle="Edit Props...";
                dialogText = " Edit a new View params.";
            }
        }


        //@FIXME add mouse right down listener
        protected Control createDialogArea(Composite parent){
            ErlLogger.debug("createDialogArea ok!");
            super.createDialogArea(parent);

            Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(new FormLayout());
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            Label msg = new Label(composite, SWT.NONE);
            msg.setText(dialogText);
            FormData msg_form = new FormData();
            msg_form.left = new FormAttachment(0,5);
            msg_form.right = new FormAttachment(100);
            msg_form.top = new FormAttachment(0);
            msg.setLayoutData(msg_form);

            dkeyLabel = new Label(composite, SWT.NULL);
            dkeyLabel.setText("取值参数名:");
            dkeyLabel.setLayoutData(setLabelLayout(0));
            dkeyText = new Text(composite, SWT.BORDER);
            dkeyText.setLayoutData(setTextLayout(0));

            drkeyLabel = new Label(composite, SWT.NONE);
            drkeyLabel.setText("请求参数名:");
            drkeyLabel.setLayoutData(setLabelLayout(1));
            drkeyText = new Text(composite, SWT.BORDER);
            drkeyText.setLayoutData(setTextLayout(1));

            dvalueLabel = new Label(composite, SWT.NONE);
            dvalueLabel.setText("取值方式:");
            dvalueLabel.setLayoutData(setLabelLayout(2));
            dfromCom = new Combo(composite, SWT.BORDER);
            dfromCom.setLayoutData(setTextLayout(2));
            dfromCom.setItems(AdapterParams.GETTER_TYPE);


            if (!addFlag){
                dkeyText.setText(params.getKey);
                drkeyText.setText(params.requestKey);
                dfromCom.setText(params.GetFrom);
            }

            dkeyText.addListener(SWT.Modify, setTextListener());
            drkeyText.addListener(SWT.Modify, setTextListener());
            setFromListener();
            return composite;
        }

        private FormData setLabelLayout(int i){
            FormData comsite_form = new FormData();
            comsite_form.left = new FormAttachment(0,5);
            comsite_form.right = new FormAttachment(0, 100);
            comsite_form.top = new FormAttachment(0,25+i*27);
            return comsite_form;
        }

        private FormData setTextLayout(int i){
            FormData comsite_form = new FormData();
            comsite_form.left = new FormAttachment(0,107);
            comsite_form.right = new FormAttachment(100, -10);
            comsite_form.top = new FormAttachment(0,25+i*27);
            return comsite_form;
        }

        protected void configureShell(Shell newShell){
            ErlLogger.debug("configureShell ok!");
            super.configureShell(newShell);
            newShell.setText(dialogTitle);
        }

        protected Point getInitialSize() {
            ErlLogger.debug("getInitialSize ok!");
            okBut = getButton(IDialogConstants.OK_ID);
            if (addFlag){
                okBut.setEnabled(false);
            }
            return new Point(400,210);
        }

        private Listener setTextListener(){
            Listener listener = new Listener(){
                @Override
                public void handleEvent(Event event) {
                    // TODO Auto-generated method stub
                    Text tmpText = (Text) event.widget;
                    ErlLogger.debug("text listener!");
                    if (tmpText == dkeyText) {
                        params.getKey = tmpText.getText();
                        setOkButton();
                    } else if (tmpText == drkeyText) {
                        params.requestKey = tmpText.getText();
                        setOkButton();
                    } else {
                        ErlLogger.debug("unkonw button!");
                    }
                }
            };
            return listener;
        }

        private void setFromListener(){
            dfromCom.addModifyListener(new ModifyListener(){
                @Override
                public void modifyText(ModifyEvent e) {
                    // TODO Auto-generated method stub
                    ErlLogger.debug("from com!");
                    params.GetFrom = dfromCom.getText();
                    setOkButton();
                }
            });
        }

        private void setOkButton(){
            if (params.getKey.replace(" ", "").isEmpty()||
                    params.requestKey.replace(" ", "").isEmpty()||
                    params.GetFrom.replace(" ", "").isEmpty())
                okBut.setEnabled(false);
            else
                okBut.setEnabled(true);
        }

    }


    // add params dialog
    public class AddDiaolog extends Dialog {
        private String dialogTitle="";
        private String dialogText="";
        private Button okBut;

        private Label dTranCodeLabel;
        private Label dAdapterLabel;
        private Label dProcedureLabel;
        private Label dViewLabel;

        private Text dTranCodeText;
        private Text dAdapterText;
        private Text dProcedureText;
        private Text dViewText;

        private boolean addFlag;
        private Button daddBut;
        private Button deditBut;
        private Button dremoveBut;
        private Button dremAllBut;

        private Table dparamsTable;

        private AdapterView view;
        private AdapterChannel adapter;
        private EwpChannels cha ;


        public AddDiaolog(Shell parent, Boolean addFlag, AdapterChannel adapter, AdapterView view){
            super(parent);
            this.addFlag = addFlag;
            this.view = view;
            this.adapter = adapter;
            cha = adapter.getNowChannel();
            if (addFlag){
                dialogTitle="Add Props...";
                dialogText = " Add a new channel View params.";
            } else{
                dialogTitle="Edit Props...";
                dialogText = " Edit a new channel View params.";
            }
        }


        //@FIXME add mouse right down listener
        protected Control createDialogArea(Composite parent){
            ErlLogger.debug("createDialogArea ok!");
            super.createDialogArea(parent);

            Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(new FormLayout());
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            Label msg = new Label(composite, SWT.NONE);
            msg.setText(dialogText);
            msg.setLayoutData(setLabelLayout(0));

            dTranCodeLabel = new Label(composite, SWT.NULL);
            dTranCodeLabel.setText("tranCode:");
            dTranCodeLabel.setLayoutData(setLabelLayout(1));
            dTranCodeText = new Text(composite, SWT.BORDER);
            dTranCodeText.setLayoutData(setTextLayout(1));

            dAdapterLabel = new Label(composite, SWT.NONE);
            dAdapterLabel.setText("adapter:");
            dAdapterLabel.setLayoutData(setLabelLayout(2));
            dAdapterText = new Text(composite, SWT.BORDER);
            dAdapterText.setLayoutData(setTextLayout(2));

            dProcedureLabel = new Label(composite, SWT.NONE);
            dProcedureLabel.setText("Procedure:");
            dProcedureLabel.setLayoutData(setLabelLayout(3));
            dProcedureText = new Text(composite, SWT.BORDER);
            dProcedureText.setLayoutData(setTextLayout(3));

            dViewLabel = new Label(composite, SWT.NONE);
            dViewLabel.setText("ViewName:");
            dViewLabel.setLayoutData(setLabelLayout(4));
            dViewText = new Text(composite, SWT.BORDER);
            dViewText.setLayoutData(setTextLayout(4));

            if (!addFlag){
                dTranCodeText.setText("");
                dAdapterText.setText("");
                dProcedureText.setText("");
                dViewText.setText("");
            }


            dparamsTable = new Table(composite, SWT.BORDER);
            FormData params_form = new FormData();
            params_form.left = new FormAttachment(0,5);
            params_form.right = new FormAttachment(0,305);
            params_form.top = new FormAttachment(0,5+5*29);
            params_form.bottom = new FormAttachment(0, 300);
            dparamsTable.setLayoutData(params_form);

            TableColumn getKeyColumn =	new TableColumn(dparamsTable, SWT.NONE);
            getKeyColumn.setText("GetKey");
            getKeyColumn.setWidth(100);
            TableColumn requestKeyColumn = new TableColumn(dparamsTable, SWT.NONE);
            requestKeyColumn.setText("RequestKey");
            requestKeyColumn.setWidth(100);
            TableColumn getTypeColumn = new TableColumn(dparamsTable, SWT.NONE);
            getTypeColumn.setText("GetType");
            getTypeColumn.setWidth(100);

            dparamsTable.setLinesVisible(true);
            dparamsTable.setHeaderVisible(true);


            daddBut = new Button(composite, SWT.NONE | SWT.CENTER);
            daddBut.setText("Add...");
            daddBut.setLayoutData(setdButtonLayout(0));

            deditBut = new Button(composite, SWT.NONE | SWT.CENTER);
            deditBut.setText("Edit");
            deditBut.setLayoutData(setdButtonLayout(1));
            deditBut.setEnabled(false);

            dremoveBut = new Button(composite, SWT.NONE | SWT.CENTER);
            dremoveBut.setText("Remove");
            dremoveBut.setLayoutData(setdButtonLayout(2));
            dremoveBut.setEnabled(false);

            dremAllBut = new Button(composite, SWT.NONE | SWT.CENTER);
            dremAllBut.setText("Remove All");
            dremAllBut.setLayoutData(setdButtonLayout(3));
            dremAllBut.setEnabled(false);
            //add button listener
            setButtonListener();

            dTranCodeText.addListener(SWT.Modify, setTextListener());
            dAdapterText.addListener(SWT.Modify, setTextListener());
            dProcedureText.addListener(SWT.Modify, setTextListener());
            dViewText.addListener(SWT.Modify, setTextListener());
            return composite;
        }

        private FormData setLabelLayout(int i){
            FormData comsite_form = new FormData();
            comsite_form.left = new FormAttachment(0,5);
            comsite_form.right = new FormAttachment(0, 100);
            comsite_form.top = new FormAttachment(0,5+i*27);
            return comsite_form;
        }

        private FormData setTextLayout(int i){
            FormData comsite_form = new FormData();
            comsite_form.left = new FormAttachment(0,107);
            comsite_form.right = new FormAttachment(100, -10);
            comsite_form.top = new FormAttachment(0,5+i*27);
            return comsite_form;
        }

        private FormData setdButtonLayout(int i){
            FormData comsite_form = new FormData();
            comsite_form.left = new FormAttachment(0, 308);
            comsite_form.right = new FormAttachment(100, -5);
            comsite_form.top = new FormAttachment(0,8+5*29+i*28);
            return comsite_form;
        }


        protected void configureShell(Shell newShell){
            ErlLogger.debug("configureShell ok!");
            super.configureShell(newShell);
            newShell.setText(dialogTitle);
        }

        protected Point getInitialSize() {
            ErlLogger.debug("getInitialSize ok!");
            okBut = getButton(IDialogConstants.OK_ID);
            if (addFlag){
                okBut.setEnabled(false);
            }
            return new Point(450,410);
        }


        private Listener setTextListener(){
            Listener listener = new Listener(){
                @Override
                public void handleEvent(Event event) {
                    // TODO Auto-generated method stub
                    Text tmpText = (Text) event.widget;
                    //ErlLogger.debug("text listener!");
                    if (tmpText == dTranCodeText) {
                        view.tranCode = tmpText.getText();
                        dViewText.setText(cha.cha_id+"_"+view.tranCode);
                        setOkButton();
                    } else if (tmpText == dAdapterText) {
                        view.adapter = tmpText.getText();
                        setOkButton();
                    } else if (tmpText == dProcedureText) {
                        view.procedure = tmpText.getText();
                        setOkButton();
                    } else if (tmpText == dViewText) {
                        ErlLogger.debug("name listener!");
                        view.viewName = tmpText.getText();
                        setOkButton();
                    } else {
                        ErlLogger.debug("unkonw button!");
                    }
                }
            };
            return listener;
        }

        private void setButtonListener(){
            daddBut.addSelectionListener(new SelectionAdapter(){
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    ErlLogger.debug("add button!");
                    AdapterParams tmpParam = new AdapterParams();
                    paramsDiaolog addLog = new paramsDiaolog(adapter.getShell(), adapter.getGlobalAddFlag(), tmpParam);
                    addLog.open();
                    if (addLog.getReturnCode() == Window.OK) {
                        //@FIXME error table
                        adapter.addParamsItem(dparamsTable, tmpParam);
                        view.paramsList.add(tmpParam);
                    }
                }
            });

            deditBut.addSelectionListener(new SelectionAdapter(){
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    ErlLogger.debug("editBut button!");
                }
            });

            dremoveBut.addSelectionListener(new SelectionAdapter(){
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    ErlLogger.debug("remove button!");
                }
            });

            dremAllBut.addSelectionListener(new SelectionAdapter(){
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    ErlLogger.debug("remove all button!");
                }
            });
        }

        private void setOkButton(){

            if (view.tranCode.replace(" ", "").isEmpty()||
                    view.viewName.replace(" ", "").isEmpty())
                okBut.setEnabled(false);
            else
                okBut.setEnabled(true);
        }
    }

    /***
     * viewDiaolog is a dialog used for new Callback and old callback
     * @author jcrom
     *
     */
    public class viewDiaolog extends Dialog {
        private String dialogTitle="";
        private String dialogText="";
        private Button okBut;

        private Label keyLabel;
        private Label valueLabel;
        private Text dialogTexKey;
        private Text dialogTexValue;

        private boolean addFlag;
        private OldCallbackParams oldParams;
        private String chaId;


        public viewDiaolog(Shell parent, Boolean addFlag, OldCallbackParams oldParams, String chaId)
        {
            super(parent);
            this.addFlag = addFlag;
            this.oldParams = oldParams;
            this.chaId = chaId;
            if (addFlag){
                dialogTitle="Add Props...";
                dialogText = " Add a new channel params.";
            } else{
                dialogTitle="Edit Props...";
                dialogText = " Edit a new channel params.";
            }
        }

        //@FIXME add mouse right down listener
        protected Control createDialogArea(Composite parent){
            ErlLogger.debug("createDialogArea ok!");
            super.createDialogArea(parent);


            Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(new FormLayout());
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            Label msg = new Label(composite, SWT.NONE);
            msg.setText(dialogText);
            msg.setLayoutData(setLabelLayout(0));

            keyLabel = new Label(composite, SWT.NULL);
            keyLabel.setText("TranCode:");
            keyLabel.setLayoutData(setLabelLayout(1));
            dialogTexKey = new Text(composite, SWT.BORDER);
            dialogTexKey.setLayoutData(setTextLayout(1));

            valueLabel = new Label(composite, SWT.NONE);
            valueLabel.setText("ViewName:");
            valueLabel.setLayoutData(setLabelLayout(2));
            dialogTexValue = new Text(composite, SWT.BORDER);
            dialogTexValue.setLayoutData(setTextLayout(2));
            if (!addFlag){
                dialogTexKey.setText(oldParams.tranCode);
                dialogTexValue.setText(oldParams.viewName);
            }

            setTextListener();
            return composite;
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


        /*			newShell.setSize(450,200);
            Rectangle screenSize = Display.getDefault().getClientArea();
            newShell.setLocation((screenSize.width - newShell.getBounds().width) / 2,(
                    screenSize.height - newShell.getBounds().height) / 2); */

        protected void configureShell(Shell newShell){
            ErlLogger.debug("configureShell ok!");
            super.configureShell(newShell);
            newShell.setText(dialogTitle);
        }

        protected Point getInitialSize() {
            ErlLogger.debug("getInitialSize ok!");
            okBut = getButton(IDialogConstants.OK_ID);
            if (addFlag){
                okBut.setEnabled(false);
            }
            return new Point(400,210);
        }

        private void setTextListener(){
            dialogTexKey.addModifyListener(new ModifyListener(){
                public void modifyText(ModifyEvent e) {
                    // TODO Auto-generated method stub
                    oldParams.tranCode = dialogTexKey.getText();
                    oldParams.viewName = formatViewName(oldParams.tranCode);
                    dialogTexValue.setText(oldParams.viewName);
                    //ErlLogger.debug("dialogKeyStr:"+dialogKeyStr);
                    setOkButton();

                }
            });
            dialogTexValue.addModifyListener(new ModifyListener(){
                public void modifyText(ModifyEvent e) {
                    // TODO Auto-generated method stub
                    oldParams.viewName = dialogTexValue.getText();
                    //@FIXME check the type of input params
                    //ErlLogger.debug("dialogKeyStr index:"+dialogValueStr.indexOf("\""));
                    ErlLogger.debug("dialogKeyStr:"+oldParams.viewName);
                    setOkButton();
                }
            });
        }

        private String formatViewName(String tranCode){
            return chaId.concat("_").concat(tranCode);
        }

        private void setOkButton(){

            if (oldParams.tranCode.replace(" ", "").isEmpty() || oldParams.viewName.replace(" ", "").isEmpty())
                okBut.setEnabled(false);
            else
                okBut.setEnabled(true);
        }

    }
}
