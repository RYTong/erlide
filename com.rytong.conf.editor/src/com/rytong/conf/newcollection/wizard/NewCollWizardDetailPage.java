package com.rytong.conf.newcollection.wizard;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpCollections;

public class NewCollWizardDetailPage extends WizardPage{
	private static String PAGE_NAME="Create a Collection";
	private static String PAGE_DESC="创建一个新的Collection，并输入详细信息.";
	private static String msg=null;

	protected EwpCollections collObj = null;

	private Label idLabel;
	private Label appLabel;
	private Label nameLabel;
	private Label urlLabel;
	private Label uidLabel;
	private Label typeLabel;
	private Label stateLabel;

	private Text idText;
	private Text appText;
	private Text nameText;
	private Text urlText;
	private Text uidText;
	private Combo typeCom;
	private Button stateBut;

	protected HashMap<String, Object> keyMap;
	private String selectId;


	protected NewCollWizardDetailPage(NewCollWizard wizard) {
		super(PAGE_NAME);
		setTitle(PAGE_NAME);
		setDescription(PAGE_DESC);
		collObj=wizard.coll;
		//ErlLogger.debug("keySet"+keySet.size());
		this.keyMap = wizard.keyMap;
		this.selectId = wizard.selectId;
		if(collObj==null)
			ErlLogger.debug("collection null!");
	}

	@Override
	public void createControl(Composite parent) {

		Composite parentcomposite = new Composite(parent, SWT.NONE);

		parentcomposite.setLayout(new FormLayout());
		Group composite = new Group(parentcomposite, SWT.BORDER);
		composite.setText("Collection Detail:");
		FormData comsite_form = new FormData();
		comsite_form.left = new FormAttachment(0,5);
		comsite_form.right = new FormAttachment(100,-5);
		comsite_form.top = new FormAttachment(0,3);
		comsite_form.bottom = new FormAttachment(0, 220);
		composite.setLayoutData(comsite_form);
		composite.setLayout(new FormLayout());

		Listener tmpListener = setTextListener();

		idLabel = new Label(composite, SWT.NULL);
		idLabel.setText("id*:");
		idLabel.setLayoutData(setLabelLayout(0));
		idText = new Text(composite, SWT.BORDER);
		idText.setLayoutData(setTextLayout(0));

		appLabel = new Label(composite, SWT.NULL);
		appLabel.setText("app*:");
		appLabel.setLayoutData(setLabelLayout(1));
		appText = new Text(composite, SWT.BORDER);
		appText.setLayoutData(setTextLayout(1));

		nameLabel = new Label(composite, SWT.NULL);
		nameLabel.setText("name*:");
		nameLabel.setLayoutData(setLabelLayout(2));
		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(setTextLayout(2));

		urlLabel = new Label(composite, SWT.NULL);
		urlLabel.setText("url:");
		urlLabel.setLayoutData(setLabelLayout(3));
		urlText = new Text(composite, SWT.BORDER);
		urlText.setLayoutData(setTextLayout(3));

		uidLabel = new Label(composite, SWT.NULL);
		uidLabel.setText("user_id:");
		uidLabel.setLayoutData(setLabelLayout(4));
		uidText = new Text(composite, SWT.BORDER);
		uidText.setLayoutData(setTextLayout(4));

		typeLabel = new Label(composite, SWT.NULL);
		typeLabel.setText("type*:");
		typeLabel.setLayoutData(setLabelLayout(5));

		typeCom = new Combo(composite, SWT.BORDER|SWT.READ_ONLY);
		typeCom.setItems(new String[]{"Collection","Collection总结点"});
		typeCom.select(0);
		typeCom.setLayoutData(setTextLayout(5));
		setComboListener(typeCom);

		stateLabel = new Label(composite, SWT.NONE);
		stateLabel.setText("State:");
		stateLabel.setLayoutData(setLabelLayout(6));
		stateBut= new Button(composite, SWT.BORDER|SWT.CHECK);
		stateBut.setSelection(true);
		stateBut.setText("开启");
		stateBut.setLayoutData(setTextLayout(6));
		setCheckButtonListener(stateBut);

		idText.addListener(SWT.Modify, tmpListener);
		appText.addListener(SWT.Modify, tmpListener);
		nameText.addListener(SWT.Modify, tmpListener);
		urlText.addListener(SWT.Modify, tmpListener);
		uidText.addListener(SWT.Modify, tmpListener);
		setControl(parentcomposite);
		if (collObj.coll_id !=""){
			initial_text();
			setPageComplete(true);
		} else{
			initial_default();
			setPageComplete(false);
		}
	}

	private FormData setLabelLayout(int i){
		FormData comsite_form = new FormData();
		comsite_form.left = new FormAttachment(0,5);
		comsite_form.right = new FormAttachment(0, 100);
		comsite_form.top = new FormAttachment(0,5+i*28);
		return comsite_form;
	}

	private FormData setTextLayout(int i){
		FormData comsite_form = new FormData();
		comsite_form.left = new FormAttachment(0,107);
		comsite_form.right = new FormAttachment(100, -10);
		comsite_form.top = new FormAttachment(0,5+i*28);
		return comsite_form;
	}

	private void initial_text(){
		ErlLogger.debug("initial text:"+collObj.coll_id);
		if (collObj.coll_id !=""){
			idText.setText(collObj.coll_id);
			appText.setText(collObj.coll_app);
			nameText.setText(collObj.coll_name);
			urlText.setText(collObj.coll_url);
			uidText.setText(collObj.coll_userid);

			typeCom.select(get_entry_index(collObj.coll_type));
			if (get_state_type(collObj.coll_state)){
				stateBut.setSelection(true);
				stateBut.setText("开启");
			} else {
				stateBut.setSelection(false);
				stateBut.setText("未开启");
			}
		}
	}

	public void initial_default(){
		collObj.coll_type = "0";
		collObj.coll_state = "1";
	}

	private int get_entry_index(String flag){
		if (flag .equalsIgnoreCase("0"))
			return 0;
		else
			return 1;
	}

	private boolean get_state_type(String state){
		if (state.equalsIgnoreCase("1"))
			return true;
		else
			return false;
	}


	// set listener

	private void setComboListener(final Combo combo){
		combo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ErlLogger.debug("selection :"+typeCom.getSelectionIndex());
				if (typeCom.getSelectionIndex() == 0)
					collObj.coll_type = "0";
				else
					collObj.coll_type = "1";
				ErlLogger.debug("cid:"+keyMap.containsKey(collObj.coll_id));
				if (keyMap.containsKey(collObj.coll_id)) {
					EwpCollections tmpObj = (EwpCollections) keyMap.get(collObj.coll_id);
					ErlLogger.debug("tmpObj.coll_type:"+tmpObj.coll_type+"|collObj.coll_type:"+collObj.coll_type);
					if (tmpObj.coll_type.equalsIgnoreCase(collObj.coll_type)){
						setErrorNotice("同类型Collection已经存在!");
					} else {
						setErrorNotice(null);
					}
				}

			}
		});
	}

	private void setCheckButtonListener(final Button button){
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if (button.getSelection()){
					button.setText("开启");
					collObj.coll_state="1";
				} else {
					button.setText("未开启");
					collObj.coll_state="0";
				}
			}
		});
	}

	//@FIXME add params verify
	public Listener setTextListener(){
		Listener listener = new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if(event.widget ==null || !(event.widget instanceof Text))
					return;

				String string = ((Text) event.widget).getText();
				String tmpstr = string.replace(" ", "");
				if(event.widget==idText){
					msg =null;
					//ErlLogger.debug("id:"+tmpstr +"|selectId:" +selectId);
					if (tmpstr.equalsIgnoreCase(selectId)){
						collObj.coll_id=string;
					} else if (keyMap.containsKey(tmpstr)) {
						EwpCollections tmpObj = (EwpCollections) keyMap.get(tmpstr);
						//ErlLogger.debug("tmpObj.coll_type:"+tmpObj.coll_type+"|collObj.coll_type:"+collObj.coll_type);

						if (tmpObj.coll_type.equalsIgnoreCase(collObj.coll_type)){
							collObj.coll_id=string;
							msg = "Input ID already exists.";
						} else {
							collObj.coll_id=string;
						}
					} else {
						collObj.coll_id=string;
					}
				}else if(event.widget==appText){
					ErlLogger.debug("app");
					collObj.coll_app=string;
				}else if(event.widget==nameText){
					ErlLogger.debug("name");
					collObj.coll_name=string;
				}else if(event.widget==urlText){
					ErlLogger.debug("url");
					collObj.coll_url=string;
				}else if(event.widget==uidText){
					ErlLogger.debug("uid");
					collObj.coll_userid=string;
				}
				//ErlLogger.debug("complete flag:"+collObj.checkValue());
				setErrorNotice(msg);
			}

		};
		return listener;
	}

	private void setErrorNotice(String msg){
		setErrorMessage(msg);
		if (msg != null){
			setPageComplete(false);
		} else if(collObj.checkValue())
			setPageComplete(true);
		else
			setPageComplete(false);
	}


/*
	for(int i=0;i<labelId.length;i++){
		if (collFlag.get(labelId[i])==null||collFlag.get(labelId[i])!=true)
			new Label(composite, SWT.NULL).setText(labelId[i]+":");
		else
			new Label(composite, SWT.NULL).setText(labelId[i]+"*:");
		Text newText = new Text(composite, SWT.NONE|SWT.BORDER);
		newText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		collText.put(labelId[i], newText);
		newText.addListener(SWT.Modify, tmpListener);

	}*/

/*	private Map<String, Text> map = null;
	private Iterator iter = null;
	public Listener setTextListener(){
		Listener listener = new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if(event.widget ==null || !(event.widget instanceof Text))
					return;
				int completeFlag=1;
				String string = ((Text) event.widget).getText();
				map = collText;
				iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object obj = entry.getValue();

					Text tmp = (Text) obj;
					if(event.widget == tmp){
						collObj.store_value((String) key, string);
					}
					String textStr = tmp.getText();
					if(collFlag.get((String) key) !=null && (textStr.isEmpty()||textStr.replace(" ", "").isEmpty())){
						completeFlag=0;
					}
				}
				if (completeFlag==1)
					setPageComplete(true);
				else
					setPageComplete(false);
			}
		};
		return listener;
	}*/


}
