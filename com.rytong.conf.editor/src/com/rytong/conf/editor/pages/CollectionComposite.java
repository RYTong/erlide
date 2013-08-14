package com.rytong.conf.editor.pages;

import java.util.HashMap;
import org.eclipse.jface.text.BadLocationException;
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
import org.erlide.utils.Util;

import com.ericsson.otp.erlang.OtpErlangObject;

public class CollectionComposite {
	private Composite composite_coll=null;
	private CollectionsPage parent;

	String cId = "id";
	String cApp = "app";
	String cName = "name";
	String cUrl = "url";
	String cUid = "user_id";
	String cType = "type";
	String cState = "state";


	private Label collIdLabel;
	private Text collIdText;

	private Label collAppLabel;
	private Text collAppText;

	private Label collNameLabel;
	private Text collNameText;

	private Label collUrlLabel;
	private Text collUrlText;

	private Label collUidLabel;
	private Text collUidText;

	private Label collTypeLabel;
	private Text collTypeText;

	private Label collStateLabel;
	private Text collStateText;



	public CollectionComposite CollectionComposite() {
		return this;
	}

	public CollectionComposite getPage() {
		return this;
	}

	public Composite getComposite(){
		return composite_coll;
	}



	public void initialCollectionsComposite(CollectionsPage parent){
		this.parent = parent;
		//right composite
		if (composite_coll != null ) {
			composite_coll.dispose();
		}
		composite_coll = new Composite(parent.pagecomposite, SWT.BORDER);
		FormData rightcomsite_form = new FormData();
		rightcomsite_form.left = new FormAttachment(2,1);
		rightcomsite_form.right = new FormAttachment(50);
		rightcomsite_form.top = new FormAttachment(5);
		rightcomsite_form.bottom = new FormAttachment(100);
		composite_coll.setLayoutData(rightcomsite_form);

		GridLayout layout_right = new GridLayout();
		layout_right.numColumns = 6;
		layout_right.verticalSpacing=10;
		composite_coll.setLayout(layout_right);

		GridData label_gd = new GridData(GridData.BEGINNING);
		label_gd.horizontalSpan = 10;

		Label label_all = new Label(composite_coll, 0);
		label_all.setText("Collection Detail Editor");
		label_all.setLayoutData(label_gd);

		collIdLabel = new Label(composite_coll, SWT.NONE);
		collIdLabel.setText("Id:");
		collIdText = new Text(composite_coll, SWT.BORDER);
		collIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		collAppLabel = new Label(composite_coll, SWT.NONE);
		collAppLabel.setText("App:");
		collAppText = new Text(composite_coll, SWT.BORDER);
		collAppText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		collNameLabel = new Label(composite_coll, SWT.NONE);
		collNameLabel.setText("Name:");
		collNameText = new Text(composite_coll, SWT.BORDER);
		collNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		collUrlLabel = new Label(composite_coll, SWT.NONE);
		collUrlLabel.setText("Url:");
		collUrlText = new Text(composite_coll, SWT.BORDER);
		collUrlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		collUidLabel = new Label(composite_coll, SWT.NONE);
		collUidLabel.setText("User Id:");
		collUidText = new Text(composite_coll, SWT.BORDER);
		collUidText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		collTypeLabel = new Label(composite_coll, SWT.NONE);
		collTypeLabel.setText("Type:");
		collTypeText = new Text(composite_coll, SWT.BORDER);
		collTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));

		collStateLabel = new Label(composite_coll, SWT.NONE);
		collStateLabel.setText("State:");
		collStateText = new Text(composite_coll, SWT.BORDER);
		collStateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5,1 ));
		//collStateText.addModifyListener(listener);
		composite_coll.setVisible(false);

	}


	private HashMap<String, ModifyListener> listenerMap = new HashMap<String, ModifyListener>();
	public void addListener(final String Key, final Text text, final EwpCollections coll){
		ModifyListener listener=null;
		text.addModifyListener(listener= new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				Text redefineText = (Text) e.getSource();
				coll.set_value(Key, redefineText.getText());

				OtpErlangObject re = parent.getIdeBackend(parent.confCon, coll.type, coll.coll_id, Key, redefineText.getText());
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

	public void setText(EwpCollections coll){

		removeListener(cId, collIdText);
		removeListener(cApp, collAppText);
		removeListener(cName, collNameText);
		removeListener(cUrl, collUrlText);
		removeListener(cUid, collUidText);
		removeListener(cType, collTypeText);
		removeListener(cState, collStateText);

		collIdText.setText(coll.coll_id);
		collAppText.setText(coll.coll_app);
		collNameText.setText(coll.coll_name);
		collUrlText.setText(coll.coll_url);
		collUidText.setText(coll.coll_userid);
		collTypeText.setText(coll.coll_type);
		collStateText.setText(coll.coll_state);
		//addStateListener(collTypeText,listener,coll);
		addListener(cId, collIdText, coll);
		addListener(cApp, collAppText, coll);
		addListener(cName, collNameText, coll);
		addListener(cUrl, collUrlText, coll);
		addListener(cUid, collUidText, coll);
		addListener(cType, collTypeText, coll);
		addListener(cState, collStateText, coll);

		composite_coll.setVisible(true);
	}

	public void setTextEmpty(String str){
		removeListener(cId, collIdText);
		removeListener(cApp, collAppText);
		removeListener(cName, collNameText);
		removeListener(cUrl, collUrlText);
		removeListener(cUid, collUidText);
		removeListener(cType, collTypeText);
		removeListener(cState, collStateText);

		collIdText.setText(str);
		collAppText.setText(str);
		collNameText.setText(str);
		collUrlText.setText(str);
		collUidText.setText(str);
		collTypeText.setText(str);
		collStateText.setText(str);
		composite_coll.setVisible(false);
	}

}
