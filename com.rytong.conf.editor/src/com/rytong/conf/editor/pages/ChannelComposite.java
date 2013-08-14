package com.rytong.conf.editor.pages;

import java.util.HashMap;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.editors.text.TextEditor;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;
import org.erlide.utils.Util;

import com.ericsson.otp.erlang.OtpErlangBinary;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;

public class ChannelComposite {

	private Composite composite_channel=null;
	private CollectionsPage parent;

	private Label chaIdLabel;
	private Text chaIdText;

	private Label chaAppLabel;
	private Text chaAppText;

	private Label chaNameLabel;
	private Text chaNameText;

	private Label chaEntryLabel;
	private Text chaEntryText;

	private Label chaViewsLabel;
	private Text chaViewsText;

	private Label chaStateLabel;
	private Text chaStateText;

	String cId = "id";
	String cApp = "app";
	String cName = "name";
	String cEntry= "entry";
	String cViews= "views";
	String cprops= "props";
	String cState = "state";




	public ChannelComposite ChannelComposite() {
		return this;
	}

	public ChannelComposite getPage() {
		return this;
	}


	public Composite getComposite(){
		return composite_channel;
	}

	public void initialChannelsComposite(CollectionsPage parent){
		this.parent = parent;
		//right composite
		if (composite_channel != null ) {
			composite_channel.dispose();
		}
		composite_channel = new Composite(parent.pagecomposite, SWT.BORDER);
		FormData rightcomsite_form = new FormData();
		rightcomsite_form.left = new FormAttachment(2,1);
		rightcomsite_form.right = new FormAttachment(50);
		rightcomsite_form.top = new FormAttachment(5);
		rightcomsite_form.bottom = new FormAttachment(100);
		composite_channel.setLayoutData(rightcomsite_form);

		GridLayout layout_right = new GridLayout();
		layout_right.numColumns = 6;
		layout_right.verticalSpacing=10;
		composite_channel.setLayout(layout_right);

		GridData label_gd = new GridData(GridData.BEGINNING);
		label_gd.horizontalSpan = 10;

		Label label_all = new Label(composite_channel, 0);
		label_all.setText("Channel Detail Editor");
		label_all.setLayoutData(label_gd);


		chaIdLabel = new Label(composite_channel, SWT.NONE);
		chaIdLabel.setText("Id:");
		chaIdText = new Text(composite_channel, SWT.BORDER);
		chaIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		chaAppLabel = new Label(composite_channel, SWT.NONE);
		chaAppLabel.setText("App:");
		chaAppText = new Text(composite_channel, SWT.BORDER);
		chaAppText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		chaNameLabel = new Label(composite_channel, SWT.NONE);
		chaNameLabel.setText("Name:");
		chaNameText = new Text(composite_channel, SWT.BORDER);
		chaNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		chaEntryLabel = new Label(composite_channel, SWT.NONE);
		chaEntryLabel.setText("Url:");
		chaEntryText = new Text(composite_channel, SWT.BORDER);
		chaEntryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		chaViewsLabel = new Label(composite_channel, SWT.NONE);
		chaViewsLabel.setText("User Id:");
		chaViewsText = new Text(composite_channel, SWT.BORDER);
		chaViewsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));


		chaStateLabel = new Label(composite_channel, SWT.NONE);
		chaStateLabel.setText("State:");
		chaStateText = new Text(composite_channel, SWT.BORDER);
		chaStateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));
		//chaStateText.addModifyListener(listener);
		composite_channel.setVisible(false);

	}

	private HashMap<String, ModifyListener> listenerMap = new HashMap<String, ModifyListener>();
	public void addListener(final String Key, final Text text, final EwpChannels cha){
		ModifyListener listener=null;
		text.addModifyListener(listener= new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				Text redefineText = (Text) e.getSource();
				cha.set_value(Key, redefineText.getText());

				OtpErlangObject re = parent.getIdeBackend(parent.confCon, cha.type, cha.cha_id, Key, redefineText.getText());
				String reStr = Util.stringValue(re);
				//ErlLogger.debug("Ll:"+document.getLength());
				try {
					parent.document.replace(0, parent.document.getLength(), reStr);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			};
		});
		if (listenerMap.get(Key)!=null)
			listenerMap.remove(Key);
		else {
			listenerMap.put(Key, listener);
		};
	}

	public void removeListener(String Key, final Text text){
		if (listenerMap.get(Key)!=null)
		{
			ModifyListener listener = listenerMap.get(Key);
			text.removeModifyListener(listener);
			listenerMap.remove(Key);
		}
	}


	public void setText(EwpChannels cha){
		removeListener(cId, chaIdText);
		removeListener(cApp, chaAppText);
		removeListener(cName, chaNameText);
		removeListener(cEntry, chaEntryText);
		removeListener(cViews, chaViewsText);
		//removeListener(cType, chaTypeText);
		removeListener(cState, chaStateText);

		chaIdText.setText(cha.cha_id);
		chaAppText.setText(cha.cha_app);
		chaNameText.setText(cha.cha_name);
		chaEntryText.setText(cha.cha_entry);
		chaViewsText.setText(cha.cha_views);

		chaStateText.setText(cha.cha_state);

		addListener(cId, chaIdText, cha);
		addListener(cApp, chaAppText, cha);
		addListener(cName, chaNameText, cha);
		addListener(cEntry, chaEntryText, cha);
		addListener(cViews, chaViewsText, cha);
		addListener(cState, chaStateText, cha);

		composite_channel.setVisible(true);
	}


	public void setTextEmpty(String str){
		removeListener(cId, chaIdText);
		removeListener(cApp, chaAppText);
		removeListener(cName, chaNameText);
		removeListener(cEntry, chaEntryText);
		removeListener(cViews, chaViewsText);
		//removeListener(cType, chaTypeText);
		removeListener(cState, chaStateText);

		chaIdText.setText(str);
		chaAppText.setText(str);
		chaNameText.setText(str);
		chaEntryText.setText(str);
		chaViewsText.setText(str);
		chaStateText.setText(str);
		composite_channel.setVisible(false);
	}





}
