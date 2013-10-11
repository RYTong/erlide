package com.rytong.conf.newadapter.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.adapter.editor.EwpAdapter;
import com.rytong.conf.adapter.editor.EwpAdpaterList;

public class NewAdapterWizardAdapterComposite {

    private NewAdapterWizardComonPage parent;
    private Group g_group;
    private static String[] adapter_protocol = new String[]{"http", "rpc", "socket", "extend"};
    private static String[] adapter_returnType = new String[]{"xml", "json"};

    private Label adp_name;
    private Label adp_host;
    private Label adp_port;
    private Label adp_protocol;
    private Label adp_returnType;


    private Text text_name;
    private Text text_host;
    private Text text_port;
    private Combo text_protocol;
    private Combo text_return;

    private EwpAdapter nowAdp;
    private String error_msg=null;
    protected EwpAdpaterList tmpEwpAdpList;

    public NewAdapterWizardAdapterComposite NewAdapterWizardAdapterComposite() {
        // TODO Auto-generated constructor stub
        return this;
    }


    public EwpAdapter initial_composite(NewAdapterWizardComonPage parent, Composite composite){
        this.parent = parent;
        nowAdp = new EwpAdapter();
        tmpEwpAdpList = parent.getAdpList();
        g_group = new Group(composite, SWT.BORDER);
        g_group.setText("Adapter Detail:");
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(100,-5);
        comsite_form.top = new FormAttachment(0,38);
        comsite_form.bottom = new FormAttachment(100, -10);
        g_group.setLayoutData(comsite_form);
        g_group.setLayout(new FormLayout());
        initial_ui_element(g_group);
        setNewTextListener();
        return nowAdp;
    }

    public void setCompositeVisiable(){
        g_group.setVisible(true);
    }
    public void setCompositeUnVisiable(){
        g_group.setVisible(false);
    }


    private void initial_ui_element(Composite composite){
        adp_name = new Label(composite, SWT.NONE);
        adp_name.setText("Name*:");
        adp_name.setLayoutData(setLabelLayout(0));

        text_name = new Text(composite, SWT.BORDER);
        text_name.setLayoutData(setTextLayout(0));

        adp_host = new Label(composite, SWT.NONE);
        adp_host.setText("Host*:");
        adp_host.setLayoutData(setLabelLayout(1));
        text_host = new Text(composite, SWT.BORDER);
        text_host.setLayoutData(setTextLayout(1));

        adp_port = new Label(composite, SWT.NONE);
        adp_port.setText("Port:");
        adp_port.setLayoutData(setLabelLayout(2));
        text_port = new Text(composite, SWT.BORDER);
        text_port.setLayoutData(setTextLayout(2));

        adp_protocol = new Label(composite, SWT.NONE);
        adp_protocol.setText("Protocol*:");
        adp_protocol.setLayoutData(setLabelLayout(3));

        text_protocol = new Combo(composite, SWT.BORDER);
        text_protocol.setItems(adapter_protocol);
        text_protocol.select(0);
        text_protocol.setLayoutData(setTextLayout(3));

        adp_returnType = new Label(composite, SWT.NONE);
        adp_returnType.setText("ReturnType*:");
        adp_returnType.setLayoutData(setLabelLayout(4));

        text_return = new Combo(composite, SWT.BORDER);
        text_return.setItems(adapter_returnType);
        text_return.select(0);
        text_return.setLayoutData(setTextLayout(4));
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

    public void setNewTextListener(){
        text_name.addModifyListener(new ModifyListener(){
            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tName = text_name.getText();
                //ErlLogger.debug("text_id:"+tName);
                nowAdp.setName(tName);
                check_state();
            }
        });
        text_host.addModifyListener(new ModifyListener(){
            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tHost = text_host.getText();
                nowAdp.setHost(tHost);
                check_state();
            }
        });
        text_port.addModifyListener(new ModifyListener(){
            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tPort = text_port.getText();
                nowAdp.setPort(tPort);
                //ErlLogger.debug("tPort:"+tPort);
            }
        });
        text_protocol.addModifyListener(new ModifyListener(){
            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tProtocol = text_protocol.getText();
                nowAdp.setProtocol(tProtocol);
                //ErlLogger.debug("tProtocol:"+tProtocol);
            }
        });
        text_return.addModifyListener(new ModifyListener(){
            @Override
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                String tReturn = text_return.getText();
                nowAdp.setReturnType(tReturn);
                //ErlLogger.debug("tReturn:"+tReturn);
            }
        });
    }


    private void check_state(){
        if (tmpEwpAdpList.checkExistedAdapter(text_name.getText())){
            error_msg = "该Adapter已经存在！";
            setError(error_msg);
            setPageCompleteState(false);
        } else if (nowAdp.checkNeededValue()){
            setError(null);
            setPageCompleteState(true);
        }else {
            setError(null);
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
