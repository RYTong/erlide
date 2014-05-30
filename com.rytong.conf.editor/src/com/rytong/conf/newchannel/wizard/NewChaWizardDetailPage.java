package com.rytong.conf.newchannel.wizard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.editor.pages.EwpCollections;

public class NewChaWizardDetailPage extends WizardPage {
    private static String PAGE_NAME = "Create a Channel";
    private static String PAGE_DESC = "创建一个新的Channel.";

    private NewChaWizard wizard;
    private EwpChannels cha;
    private HashMap<String, EwpChannels>  keyMap;
    private String selectId=null;
    private static String msg=null;

    private Label idLabel;
    private Label appLabel;
    private Label nameLabel;
    private Label entryLabel;
    private Label stateLabel;

    private Text chaIdText;
    private Text chaAppText;
    private Text chaNameText;
    private Combo chaEntryCom;
    private Button chaStateBut;

    private Table table ;
    private Button addBut ;
    private Button editBut ;
    private Button removeBut ;

    private Button testBut;

    private Composite parent;

    private static String dialogKeyStr;
    private static String dialogValueStr;

    private static TableItem[] selectItem;
    private IProject erlProject;


    protected NewChaWizardDetailPage(NewChaWizard wizard) {
        super(PAGE_NAME);
        setTitle(PAGE_NAME);
        setDescription(PAGE_DESC);
        this.wizard = wizard;
        this.cha = wizard.cha;
        this.keyMap = wizard.keyMap;
        this.selectId = wizard.selectId;
        TextEditor tmpEditor = wizard.getTextEditor();
        erlProject = ((FileEditorInput) tmpEditor.getEditorInput()).getFile().getProject();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createControl(Composite parent) {
        // TODO Auto-generated method stub
        this.parent = parent;
        //Color  test = new Color(parent.getDisplay(), 0,0,0);
        //parent.setBackground(test);

        Composite parentcomposite = new Composite(parent, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        parentcomposite.setLayout(formLayout);
        Group composite = new Group(parentcomposite, SWT.BORDER);
        composite.setText("Channel Detail:");
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(100,-5);
        comsite_form.top = new FormAttachment(0,3);
        comsite_form.bottom = new FormAttachment(0, 165);
        composite.setLayoutData(comsite_form);

        composite.setLayout(new FormLayout());

        Listener listener = setTextListener();
        idLabel = new Label(composite, SWT.NONE);
        idLabel.setText("Id*:");
        idLabel.setLayoutData(setLabelLayout(0));
        chaIdText = new Text(composite, SWT.BORDER);
        chaIdText.setLayoutData(setButtonLayout(0));

        appLabel = new Label(composite, SWT.NONE);
        appLabel.setText("App*:");
        appLabel.setLayoutData(setLabelLayout(1));
        chaAppText = new Text(composite, SWT.BORDER);
        chaAppText.setLayoutData(setButtonLayout(1));


        nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText("Name*:");
        nameLabel.setLayoutData(setLabelLayout(2));
        chaNameText = new Text(composite, SWT.BORDER);
        chaNameText.setLayoutData(setButtonLayout(2));

        entryLabel  = new Label(composite, SWT.NONE);
        entryLabel.setText("Entry:");
        entryLabel.setLayoutData(setLabelLayout(3));
        chaEntryCom = new Combo(composite, SWT.BORDER|SWT.READ_ONLY);
        chaEntryCom.setItems(new String[]{"适配","新回调","旧回调"});
        chaEntryCom.select(0);
        chaEntryCom.setLayoutData(setButtonLayout(3));
        setComboListener(chaEntryCom);

        stateLabel = new Label(composite, SWT.NONE);
        stateLabel.setText("State:");
        stateLabel.setLayoutData(setLabelLayout(4));
        chaStateBut= new Button(composite, SWT.BORDER|SWT.CHECK);
        chaStateBut.setSelection(true);
        chaStateBut.setText("开启");
        chaStateBut.setLayoutData(setButtonLayout(4));

        chaIdText.addListener(SWT.Modify, listener);
        chaAppText.addListener(SWT.Modify, listener);
        chaAppText.setText(erlProject.getName());
        chaNameText.addListener(SWT.Modify, listener);
        setCheckButtonListener(chaStateBut);



        //ErlLogger.debug("shell:"+parent.getShell().getLocation());
        //Point point = parent.getShell().getLocation();
        //parent.getShell().setSize(500, 600);
        //parent.getShell().setLocation(point.x, point.y);
        setControl(parentcomposite);
        if (selectId !=null){
            initial_text();
            draw_props(parentcomposite);
            setPageComplete(true);
        } else{
            initial_default();
            draw_props(parentcomposite);
            setPageComplete(false);
        }

    }

    public void draw_props(Composite composite){

        Group props = new Group(composite, SWT.BORDER);
        FormData group_form = new FormData();
        group_form.left = new FormAttachment(0,5);
        group_form.right = new FormAttachment(100,-5);
        group_form.top = new FormAttachment(0,170);
        group_form.bottom = new FormAttachment(100,-10);
        props.setLayoutData(group_form);
        props.setText("可选参数:");
        props.setLayout(new FormLayout());


        table = new Table(props, SWT.BORDER|SWT.MULTI);
        table.setHeaderVisible(true);
        //table.setLinesVisible(true);


        TableColumn keyColumn = new TableColumn(table, SWT.BORDER);
        keyColumn.setText("Key");
        keyColumn.setWidth(200);
        TableColumn valueColumn = new TableColumn(table, SWT.BORDER);
        valueColumn.setText("Value");
        valueColumn.setWidth(200);

        FormData table_form = new FormData();
        table_form.left = new FormAttachment(0,5);
        table_form.right = new FormAttachment(100,-135);
        table_form.top = new FormAttachment(0,2);
        table_form.bottom = new FormAttachment(100,-5);
        table.setLayoutData(table_form);
        setTableListener();
        initial_tableItem();

        addBut = new Button(props, SWT.BORDER);
        addBut.setText("Add ...");
        FormData add_form = new FormData();
        add_form.left = new FormAttachment(100,-130);
        add_form.right = new FormAttachment(100,-5);
        add_form.top = new FormAttachment(0,3);
        addBut.setLayoutData(add_form);


        editBut = new Button(props, SWT.BORDER);
        editBut.setText("Edit");
        FormData edit_form = new FormData();
        edit_form.left = new FormAttachment(100,-130);
        edit_form.right = new FormAttachment(100,-5);
        edit_form.top = new FormAttachment(0,30);
        editBut.setLayoutData(edit_form);
        editBut.setEnabled(false);

        removeBut = new Button(props, SWT.BORDER);
        removeBut.setText("Remove");
        FormData rem_form = new FormData();
        rem_form.left = new FormAttachment(100,-130);
        rem_form.right = new FormAttachment(100,-5);
        rem_form.top = new FormAttachment(0,57);
        removeBut.setLayoutData(rem_form);
        removeBut.setEnabled(false);

        /**
         *  use for test
        testBut = new Button(props, SWT.BORDER);
        testBut.setText("tet");
        FormData testButF = new FormData();
        testButF.left = new FormAttachment(100,-130);
        testButF.right = new FormAttachment(100,-5);
        testButF.top = new FormAttachment(0,84);
        testBut.setLayoutData(testButF);
        */

        setButtonListener();

    }

    private void initial_tableItem(){
        //HashMap<String EwpChannels> tmpMap = parent;
        Map<String, String> map = cha.cha_props;
        Iterator chaiter = map.entrySet().iterator();

        while (chaiter.hasNext()) {
            Map.Entry entry = (Map.Entry) chaiter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            //ErlLogger.debug("cha key:"+key);
            new TableItem(table, SWT.NONE).setText(new String[] {key, value});
        }
    }

    private void initial_text(){
        ErlLogger.debug("initial text:"+cha.cha_id);
        chaIdText.setText(cha.cha_id);
        chaAppText.setText(cha.cha_app);
        chaNameText.setText(cha.cha_name);
        chaEntryCom.select(EwpChannels.get_entry_index(cha.cha_entry));
        if (get_state_type(cha.cha_state)){
            chaStateBut.setSelection(true);
            chaStateBut.setText("开启");
        } else {
            chaStateBut.setSelection(false);
            chaStateBut.setText("未开启");
        }
    }

    public void initial_default(){
        cha.initial_props();
        cha.cha_entry = EwpChannels.CHANNEL_ADAPTER;
        cha.cha_state = "1";
    }


    private boolean get_state_type(String state){
        if (state.equalsIgnoreCase("1"))
            return true;
        else
            return false;
    }

    // ---------- set listener ----------

    private FormData setLabelLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(0, 100);
        comsite_form.top = new FormAttachment(0,5+i*29);
        return comsite_form;
    }

    private FormData setButtonLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,107);
        comsite_form.right = new FormAttachment(100, -10);
        comsite_form.top = new FormAttachment(0,5+i*28);
        return comsite_form;
    }

    //@FIXME add params verify
    private Listener setTextListener(){
        Listener listener = new Listener(){

            public void handleEvent(Event event) {
                // TODO Auto-generated method stub
                if(event.widget ==null || !(event.widget instanceof Text))
                    return;

                String string = ((Text) event.widget).getText();
                String tmpstr = string.replace(" ", "");
                if(event.widget==chaIdText){
                    //ErlLogger.debug("id");
                    msg = null;
                    if (tmpstr.equalsIgnoreCase(selectId)){
                        cha.cha_id=string;
                    } else if (keyMap.containsKey(tmpstr)) {
                        msg = "Input ID already exists.";
                    } else {
                        cha.cha_id=string;
                    }

                }else if(event.widget==chaAppText){
                    //ErlLogger.debug("app");
                    cha.cha_app=string;
                }else if(event.widget==chaNameText){
                    //ErlLogger.debug("name");
                    cha.cha_name=string;
                }
                setErrorNotice(msg);
            }
        };
        return listener;
    }

    private void setErrorNotice(String msg){
        setErrorMessage(msg);
        if (msg != null){
            setPageComplete(false);
        } else if(cha.checkValue())
            setPageComplete(true);
        else
            setPageComplete(false);
    }


    private void setCheckButtonListener(final Button button){
        button.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                if (button.getSelection()){
                    button.setText("开启");
                    cha.cha_state="1";
                } else {
                    button.setText("未开启");
                    cha.cha_state="0";
                }
            }
        });
    }


    private void setComboListener(final Combo combo){
        combo.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("selection :"+chaEntryCom.getSelectionIndex());
                int index = chaEntryCom.getSelectionIndex();
                String oldEntry = cha.cha_entry;
                if (index == 0)
                    cha.cha_entry = EwpChannels.CHANNEL_ADAPTER;
                else if(index == 1)
                    cha.cha_entry = EwpChannels.NEW_CALLBACK;
                else
                    cha.cha_entry = EwpChannels.CHANNEL_CALLBACK;
                refreshViewPage(oldEntry, cha.cha_entry, index);
            }
        });
    }

    private void refreshViewPage(String oldEntry, String entry, int flag){

        if (!entry.equalsIgnoreCase(oldEntry)){
            NewChaWizardViewPage tmpPage = wizard.getViewPage();
            tmpPage.set_visiable(flag);
        }

    }

    private void setTableListener(){
        table.addMouseListener(new MouseAdapter(){
            public void mouseDown(MouseEvent event) {
                ErlLogger.debug("table event.");
                if (event.getSource() != null){
                    selectItem = table.getSelection();
                    ErlLogger.debug("table event:"+selectItem.length);
                    //ErlLogger.debug("table event:"+selectItem[0].getText());

                    if (selectItem.length == 1){
                        if (cha.check_props(selectItem[0].getText(0))){
                            editBut.setEnabled(true);
                            removeBut.setEnabled(false);
                        }else{
                            editBut.setEnabled(true);
                            removeBut.setEnabled(true);
                        }
                    } else if (selectItem.length > 1){
                        if (selectItem.length==2&&
                                cha.check_props(selectItem[0].getText(0))&&
                                cha.check_props(selectItem[1].getText(0))){
                            editBut.setEnabled(false);
                            removeBut.setEnabled(false);
                        }
                        else{
                            editBut.setEnabled(false);
                            removeBut.setEnabled(true);
                        }
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

    private void setButtonListener(){
        addBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("add button press !");
                AddDiaolog newDialog = new AddDiaolog(parent.getShell(), addBut);
                newDialog.open();
                ErlLogger.debug("dialog result :"+newDialog.getReturnCode());
                if (newDialog.getReturnCode()==Window.OK){
                    TableItem tmpItem = new TableItem(table, SWT.BORDER);
                    dialogValueStr = dialogValueStr.replace(" ", "");
                    tmpItem.setText(new String[]{dialogKeyStr, dialogValueStr});
                    cha.cha_props.put(dialogKeyStr, dialogValueStr);
                }

                //test.open();
            }
        });

        editBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("edit button press !");
                AddDiaolog newDialog = new AddDiaolog(parent.getShell(), editBut);
                newDialog.open();
                ErlLogger.debug("dialog result :"+newDialog.getReturnCode());
                if (newDialog.getReturnCode()==Window.OK){
                    //table.get
                    dialogValueStr = dialogValueStr.replace(" ", "");
                    selectItem[0].setText(new String[] {dialogKeyStr, dialogValueStr});
                    cha.cha_props.remove(selectItem[0].getText(0));
                    cha.cha_props.put(dialogKeyStr, dialogValueStr);
                }

            }
        });

        removeBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("remove button press !");
                // @FIXME check_props is false,the item should be removed.
                //cha.check_props(selectItem[0].getText(0))
                cha.cha_props.remove(selectItem[0].getText(0));
                int[] indeces= table.getSelectionIndices();
                table.remove(indeces);
                //table
            }
        });

//        testBut.addSelectionListener(new SelectionAdapter(){
//            public void widgetSelected(SelectionEvent e) {
//                ErlLogger.debug("test button press !");
//                // @FIXME check_props is false,the item should be removed.
//                //cha.check_props(selectItem[0].getText(0))
//                dotest();
//                //table
//            }
//        });
    }


    private void dotest(){
        ErlLogger.debug("project name:".concat(erlProject.getName()));
    }

    /*
      Rectangle screenSize = Display.getDefault().getClientArea();

      dialog.setSize(450,200);
      dialog.setLocation((screenSize.width - dialog.getBounds().width) / 2,(
              screenSize.height - dialog.getBounds().height) / 2);*/


    class AddDiaolog extends Dialog {
        private String dialogTitle="";
        private String dialogText="";
        private Button okBut;

        private Label keyLabel;
        private Label valueLabel;
        private Text dialogTexKey;
        private Text dialogTexValue;

        private Button flagBut;


        public AddDiaolog(Shell parent, Button but)
        {
            super(parent);
            dialogKeyStr = "";
            dialogValueStr = "";
            flagBut = but;
            if (but.equals(addBut)){
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
            keyLabel.setText("Key:");
            keyLabel.setLayoutData(setLabelLayout(1));
            dialogTexKey = new Text(composite, SWT.BORDER);
            dialogTexKey.setLayoutData(setTextLayout(1));

            valueLabel = new Label(composite, SWT.NONE);
            valueLabel.setText("Value:");
            valueLabel.setLayoutData(setLabelLayout(2));
            dialogTexValue = new Text(composite, SWT.BORDER);
            dialogTexValue.setLayoutData(setTextLayout(2));
            if (flagBut.equals(editBut)){

                dialogKeyStr = selectItem[0].getText(0);
                dialogValueStr = selectItem[0].getText(1);
                dialogTexKey.setText(dialogKeyStr);
                dialogTexValue.setText(dialogValueStr);
            }

            Label notic = new Label(composite, SWT.WRAP);
            notic.setText("注意：本处输入需要输入直接显示内容，例如需要String则输入\"String\"，需要Atom或者其他则直接输入。");
            FormData notic_form = new FormData();
            notic_form.left = new FormAttachment(0,5);
            notic_form.right = new FormAttachment(100, -5);
            notic_form.top = new FormAttachment(0,5+3*29);
            notic.setLayoutData(notic_form);


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
            if (flagBut.equals(addBut)){
                okBut.setEnabled(false);
            }
            return new Point(400,240);
        }

        private void setTextListener(){
            dialogTexKey.addModifyListener(new ModifyListener(){
                public void modifyText(ModifyEvent e) {
                    // TODO Auto-generated method stub
                    dialogKeyStr = dialogTexKey.getText();
                    //ErlLogger.debug("dialogKeyStr:"+dialogKeyStr);
                    setOkButton();

                }
            });
            dialogTexValue.addModifyListener(new ModifyListener(){
                public void modifyText(ModifyEvent e) {
                    // TODO Auto-generated method stub
                    dialogValueStr = dialogTexValue.getText();
                    //@FIXME check the type of input params
                    //ErlLogger.debug("dialogKeyStr index:"+dialogValueStr.indexOf("\""));
                    ErlLogger.debug("dialogKeyStr:"+dialogKeyStr);
                    setOkButton();
                }
            });
        }

        private void setOkButton(){

            if (dialogKeyStr.replace(" ", "").isEmpty()	|| dialogValueStr.replace(" ", "").isEmpty())
                okBut.setEnabled(false);
            else
                okBut.setEnabled(true);
        }

    }



}
