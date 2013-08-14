package com.rytong.conf.newcollection.wizard;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpCollections;

public class NewCollWizardDetailPage extends WizardPage{
	private static String PAGE_NAME="Create a Collection";
	private static String PAGE_DESC="创建一个新的Collection，并输入详细信息！";

	protected EwpCollections collObj = null;

	private Text idText;
	private Text appText;
	private Text nameText;
	private Text urlText;
	private Text uidText;
	private Text typeText;
	private Text stateText;

	private Set keySet = null;


	protected NewCollWizardDetailPage(EwpCollections coll, Set set) {
		super(PAGE_NAME);
		setDescription(PAGE_DESC);
		collObj=coll;
		//ErlLogger.debug("keySet"+keySet.size());
		keySet = set;
		if(collObj==null)
			ErlLogger.debug("collection null!");
	}


	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 10;
		Label label = new Label(composite, SWT.NONE);
		label.setText("&Collection Detail:");
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true,true,2,1));


		Listener tmpListener = setTextListener();

		new Label(composite, SWT.NULL).setText("id*:");
		idText = new Text(composite, SWT.NONE|SWT.BORDER);
		idText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("app*:");
		appText = new Text(composite, SWT.NONE|SWT.BORDER);
		appText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("name*:");
		nameText = new Text(composite, SWT.NONE|SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("url:");
		urlText = new Text(composite, SWT.NONE|SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("user_id:");
		uidText = new Text(composite, SWT.NONE|SWT.BORDER);
		uidText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("type*:");
		typeText = new Text(composite, SWT.NONE|SWT.BORDER);
		typeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NULL).setText("state*:");
		stateText = new Text(composite, SWT.NONE|SWT.BORDER);
		stateText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		idText.addListener(SWT.Modify, tmpListener);
		appText.addListener(SWT.Modify, tmpListener);
		nameText.addListener(SWT.Modify, tmpListener);
		urlText.addListener(SWT.Modify, tmpListener);
		uidText.addListener(SWT.Modify, tmpListener);
		typeText.addListener(SWT.Modify, tmpListener);
		stateText.addListener(SWT.Modify, tmpListener);
		setControl(composite);
		setPageComplete(true);
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
					ErlLogger.debug("id");
					if (keySet.contains(tmpstr)) {
						collObj.coll_id="";
						setErrorMessage("Input ID already exists.");
					} else {
						setErrorMessage(null);
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
				}else if(event.widget==typeText){
					ErlLogger.debug("type");
					if((tmpstr.equalsIgnoreCase("0"))||(tmpstr.equalsIgnoreCase("1"))){
						setErrorMessage(null);
						collObj.coll_type=string;
					}else if(string.isEmpty()){
						collObj.coll_type="";
						setErrorMessage(null);
					}else{
						collObj.coll_type="";
						setErrorMessage("Invalid input params of \"type*\", a valid input should be 0 or 1.");
					}

				}else if(event.widget==stateText){
					ErlLogger.debug("state");
					if((tmpstr.equalsIgnoreCase("0"))||(tmpstr.equalsIgnoreCase("1"))){
						//setErrorMessage(null);
						collObj.coll_state=string;
					}else if(string.isEmpty()){
						collObj.coll_state="";
						setErrorMessage(null);
					}else{
						setErrorMessage("Invalid input params of \"state*\", a valid input should be 0 or 1.");
						collObj.coll_state="";
					}
				}
				//ErlLogger.debug("complete flag:"+collObj.checkValue());
				if (collObj.checkValue())
					setPageComplete(true);
				else
					setPageComplete(false);
			}

		};
		return listener;
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
