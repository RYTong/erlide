package com.rytong.conf.adapter.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangTuple;



public class AdapterEditorAdapterComposite {
	private Composite parentComposite;
	private AdapterEditorPage parent;
	private Composite composite;
	private boolean leftTableSelect = false;

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

	public AdapterEditorAdapterComposite(AdapterEditorPage parent){
		this.parentComposite = parent.getParent();
		this.parent = parent;
	}

	public void setAdapterEditorVisiable(){
		composite.setVisible(true);
	}

	public void setAdapterEditorUnVisiable(){
		leftTableSelect = false;
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
		initial_ui_element(composite);
		setTextListener();
	}

	public void initial_ui_element(Composite composite){
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

	private boolean setTextFlag= false;
	public void setAdapterValue(EwpAdapter adp){
		setTextFlag = true;
		setAdapterEditorVisiable();
		nowAdp = adp;
		text_name.setText(adp.getName());
		text_host.setText(adp.getHost());
		text_port.setText(adp.getPort());
		//"http", "rpc", "socket", "extend"
		String protocol = adp.getProtocol();
		if (protocol.equalsIgnoreCase("http"))
			text_protocol.select(0);
		else if(protocol.equalsIgnoreCase("rpc"))
			text_protocol.select(1);
		else if(protocol.equalsIgnoreCase("socket"))
			text_protocol.select(2);
		else if(protocol.equalsIgnoreCase("extend"))
			text_protocol.select(3);
		else
			text_protocol.setText(protocol);
		String returnType = adp.getReturnType();
		if (returnType.equalsIgnoreCase("xml"))
			text_return.select(0);
		else if (returnType.equalsIgnoreCase("json"))
			text_return.select(1);
		else
			text_return.setText(returnType);

		setTextFlag = false;
	}

	public void setTextListener(){
		text_name.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				String tName = text_name.getText();

				if(!setTextFlag){
					String oldName = nowAdp.getName();
					nowAdp.setName(tName);
					OtpErlangTuple tmpTup = nowAdp.editAdapterName();
					ErlLogger.debug("text_id:"+tName);
					parent.erlBackend_editAdapter(tmpTup);
					nowAdp.setOldName(oldName);
				}
			}
		});
		text_host.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				String tHost = text_host.getText();
				if(!setTextFlag){
					nowAdp.setHost(tHost);
					OtpErlangTuple tmpTup = nowAdp.editAdapterHost();
					ErlLogger.debug("tHost:"+tHost);
					parent.erlBackend_editAdapter(tmpTup);
				}
			}
		});
		text_port.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				String tPort = text_port.getText();

				if(!setTextFlag){
					nowAdp.setPort(tPort);
					OtpErlangTuple tmpTup = nowAdp.editAdapterPort();
					ErlLogger.debug("tPort:"+tPort);
					parent.erlBackend_editAdapter(tmpTup);
				}
			}
		});
		text_protocol.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				String tProtocol = text_protocol.getText();

				if(!setTextFlag){
					nowAdp.setProtocol(tProtocol);
					OtpErlangTuple tmpTup = nowAdp.editAdapterProtocol();
					ErlLogger.debug("tProtocol:"+tProtocol);
					parent.erlBackend_editAdapter(tmpTup);
				}
			}
		});
		text_return.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				String tReturn = text_return.getText();

				if(!setTextFlag){
					nowAdp.setReturnType(tReturn);
					OtpErlangTuple tmpTup = nowAdp.editAdapterReturnType();
					ErlLogger.debug("tReturn:"+tReturn);
					parent.erlBackend_editAdapter(tmpTup);
				}
			}
		});
	}

}
