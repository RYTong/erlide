package com.rytong.conf.newchannel.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;

public class NewChaWizardViewPage extends WizardPage {
	private static String PAGE_NAME = "Create a Channel";
	private static String PAGE_DESC = "为新Channel添加代码及页面支持.";
	private EwpChannels cha;
	private NewChaWizard wizard;
	private WizarParams  cha_view;

	private String selectId;
	private Composite parent;
	private boolean addFlagGlobal = true;


	protected NewChaWizardViewPage(NewChaWizard wizard) {
		super(PAGE_NAME);
		setTitle(PAGE_NAME);
		setDescription(PAGE_DESC);
		this.wizard = wizard;
		this.cha = wizard.cha;
		cha_view = cha.add_view;
		this.selectId = wizard.selectId;
		// TODO Auto-generated constructor stub
	}
	protected Composite parentcomposite ;
	protected OldCallBackChannel oldCallBack;

	private Group offGroup;
	private Group templateGroup;

	private Label separateLine;
	private Label entryLabel;
	private Label templateFlag;
	private Label csFlag;
	private Label offFlag;

	private Button templateBut;
	private Button csBut;
	private Button offBut;
	private Combo chaEntryCom;

	private Label imagesLabel;
	private Label cssLabel;
	private Label luaLabel;
	private Label xhtmlLabel;
	private Label channelLabel;

	private Button imagesBut;
	private Button cssBut;
	private Button luaBut;
	private Button xhtmlBut;
	private Button channelBut;

	private Label plateLabel;
	private Label resolutionLabel;

	private Combo plateCom;
	private Combo resolutionCom;
	private static String[] plateforms = {"IPhone", "Android", "WinPhone"};
	private static String[] iPhoneRes = {"320*480", "640*960", "640*1136"};
	private static String[] androidRes = {"320*480", "480*800", "640*960", "320*240", "600*1024", "1280*720", "1280*800"};
	private static String[] winRes = {};


	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		parentcomposite = new Composite(parent, SWT.NONE);
		FormLayout formLayout = new FormLayout();
		parentcomposite.setLayout(formLayout);

		entryLabel  = new Label(parentcomposite, SWT.NONE);
		entryLabel.setText("Entry:");
		FormData entry_form = new FormData();
		entry_form.left = new FormAttachment(0,5);
		entry_form.right = new FormAttachment(0, 50);
		entry_form.top = new FormAttachment(0, 2);
		entryLabel.setLayoutData(entry_form);

		int flag = EwpChannels.get_entry_index(cha.cha_entry);
		chaEntryCom = new Combo(parentcomposite, SWT.BORDER|SWT.READ_ONLY);
		chaEntryCom.setItems(new String[]{"适配","新回调","旧回调"});
		chaEntryCom.select(flag);
		FormData com_form = new FormData();
		com_form.left = new FormAttachment(0,50);
		com_form.right = new FormAttachment(100, -5);
		com_form.top = new FormAttachment(0);

		chaEntryCom.setLayoutData(com_form);
		setComboListener(chaEntryCom);

		separateLine = new Label(parentcomposite, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BORDER);
		FormData seprate_form = new FormData();
		seprate_form.left = new FormAttachment(0,5);
		seprate_form.right = new FormAttachment(100, -5);
		seprate_form.top = new FormAttachment(0, 27);
		separateLine.setLayoutData(seprate_form);

		initial_adapter_group();
		// initial old callback composit
		oldCallBack = new OldCallBackChannel(wizard, this);
		oldCallBack.initial_composite();

		if (selectId !=null){
			if (flag == 0){
				set_visiable(flag);
				initial_text();
			} else if(flag == 1){

			} else {

			}
		} else{
			initial_default();
		}

		setControl(parentcomposite);
		setPageComplete(true);
	}

	public void set_visiable(int flag){
		if (flag == 0){
			templateGroup.setVisible(true);
			oldCallBack.set_unvisiable();
		} else if (flag == 1) {
			templateGroup.setVisible(false);
			oldCallBack.set_unvisiable();
		}else {
			templateGroup.setVisible(false);
			oldCallBack.set_visiable();
		}
	}

	public void initial_adapter_group(){
		adapter_group(parentcomposite);
		templateGroup.setVisible(false);
	}

	public void adapter_group(Composite parentcomposite){

		templateGroup = new Group(parentcomposite, SWT.BORDER);

		FormData template_form = new FormData();
		template_form.left = new FormAttachment(0);
		template_form.right = new FormAttachment(100,-5);
		template_form.top = new FormAttachment(0,30);
		template_form.bottom = new FormAttachment(100, -3);
		templateGroup.setLayoutData(template_form);
		templateGroup.setLayout(new FormLayout());
		//templateGroup.setText("Template List");


		templateFlag = new Label(templateGroup, SWT.NONE);
		templateFlag.setText("生成辅助代码");
		templateFlag.setLayoutData(setFlagLableForm( 0));
		templateBut = new Button(templateGroup, SWT.BORDER | SWT.CHECK);
		templateBut.setLayoutData(setFlagButForm(0));

		csFlag = new Label(templateGroup, SWT.NONE);
		csFlag.setText("生成CS模板");
		csFlag.setLayoutData(setFlagLableForm(1));
		csBut = new Button(templateGroup, SWT.BORDER | SWT.CHECK);
		csBut.setLayoutData(setFlagButForm(1));


		offFlag = new Label(templateGroup, SWT.NONE);
		offFlag.setText("生成离线资源文件");
		offFlag.setLayoutData(setFlagLableForm(2));
		offBut = new Button(templateGroup, SWT.BORDER | SWT.CHECK);
		offBut.setLayoutData(setFlagButForm(2));
		offline_group(templateGroup);

		templateBut.addListener(SWT.Selection, setCheckButListener());
		csBut.addListener(SWT.Selection, setCheckButListener());
		offBut.addListener(SWT.Selection, setCheckButListener());



		ErlLogger.debug("entry:"+cha.cha_entry);
		//if (cha.cha_entry.equalsIgnoreCase(EwpChannels.CHANNEL_ADAPTER)){
		adapter_table(templateGroup);
		setGroupStatus(false);
		setInitialOffConf();
	}


	public void offline_group(Group templateGroup){
		offGroup = new Group(templateGroup, SWT.BORDER);
		FormData off_form = new FormData();
		off_form.left = new FormAttachment(0,3);
		off_form.right = new FormAttachment(100,-3);
		off_form.top = new FormAttachment(0,28);
		off_form.bottom = new FormAttachment(0, 90);
		offGroup.setLayoutData(off_form);
		offGroup.setLayout(new FormLayout());


		plateLabel = new Label(offGroup, SWT.NONE);
		plateLabel.setText("平台:");
		plateLabel.setLayoutData(setComLableForm(0));
		plateCom = new Combo(offGroup, SWT.BORDER);
		plateCom.setItems(plateforms);
		plateCom.setLayoutData(setComButForm(0));

		resolutionLabel = new Label(offGroup, SWT.NONE);
		resolutionLabel.setText("分辨率:");
		resolutionLabel.setLayoutData(setComLableForm(1));
		resolutionCom = new Combo(offGroup, SWT.BORDER);
		resolutionCom.setItems(iPhoneRes);
		resolutionCom.setLayoutData(setComButForm(1));


		imagesLabel = new Label(offGroup, SWT.NONE);
		imagesLabel.setText("images");
		imagesLabel.setLayoutData(setOffLableForm(0));
		imagesBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
		imagesBut.setLayoutData(setOffButForm(0));

		cssLabel = new Label(offGroup, SWT.NONE);
		cssLabel.setText("css");
		cssLabel.setLayoutData(setOffLableForm(1));
		cssBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
		cssBut.setLayoutData(setOffButForm(1));

		luaLabel = new Label(offGroup, SWT.NONE);
		luaLabel.setText("lua");
		luaLabel.setLayoutData(setOffLableForm(2));
		luaBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
		luaBut.setLayoutData(setOffButForm(2));

		xhtmlLabel = new Label(offGroup, SWT.NONE);
		xhtmlLabel.setText("xhtml");
		xhtmlLabel.setLayoutData(setOffLableForm(3));
		xhtmlBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
		xhtmlBut.setLayoutData(setOffButForm(3));

		channelLabel = new Label(offGroup, SWT.NONE);
		channelLabel.setText("channel");
		channelLabel.setLayoutData(setOffLableForm(4));
		channelBut = new Button(offGroup, SWT.BORDER | SWT.CHECK);
		channelBut.setLayoutData(setOffButForm(4));

		plateCom.addListener(SWT.Modify , setComboListener());
		resolutionCom.addListener(SWT.Modify, setComboListener());
		imagesBut.addListener(SWT.Selection, setOffListener());
		cssBut.addListener(SWT.Selection, setOffListener());
		luaBut.addListener(SWT.Selection, setOffListener());
		xhtmlBut.addListener(SWT.Selection, setOffListener());
		channelBut.addListener(SWT.Selection, setOffListener());

		imagesBut.setSelection(true);
		cssBut.setSelection(true);
		luaBut.setSelection(true);
		xhtmlBut.setSelection(true);
		channelBut.setSelection(true);

		//offGroup.setEnabled(false);

	}


	private Table adapter_table;
	private Table params_table;
	private Button addBut;
	private Button editBut;
	private Button removeBut;
	private Button remAllBut;
	private Group treeGroup;
	public void adapter_table(Composite parent){

		treeGroup = new Group(parent, SWT.BORDER);
		//treeGroup.setText("Views");
		treeGroup.setLayout(new FormLayout());
		FormData tGroup_form = new FormData();
		tGroup_form.left = new FormAttachment(0,5);
		tGroup_form.right = new FormAttachment(100,-5);
		tGroup_form.top = new FormAttachment(0,93);
		tGroup_form.bottom = new FormAttachment(100);
		treeGroup.setLayoutData(tGroup_form);


		adapter_table = new Table(treeGroup, SWT.BORDER| SWT.MULTI);
		TableColumn tranCodeColumn =	new TableColumn(adapter_table, SWT.NONE);
		tranCodeColumn.setText("TranCode");
		tranCodeColumn.setWidth(100);
		TableColumn viewNameColumn = new TableColumn(adapter_table, SWT.NONE);
		viewNameColumn.setText("ViewName");
		viewNameColumn.setWidth(100);
		TableColumn adapterColumn = new TableColumn(adapter_table, SWT.NONE);
		adapterColumn.setText("Adapter");
		adapterColumn.setWidth(100);
		TableColumn procedureColumn = new TableColumn(adapter_table, SWT.NONE);
		procedureColumn.setText("Procedure");
		procedureColumn.setWidth(100);

		adapter_table.setLinesVisible(true);
		adapter_table.setHeaderVisible(true);


		FormData adapte_form = new FormData();
		adapte_form.left = new FormAttachment(0,5);
		adapte_form.right = new FormAttachment(0,405);
		adapte_form.top = new FormAttachment(0,5);
		adapte_form.bottom = new FormAttachment(100, -5);
		adapter_table.setLayoutData(adapte_form);

		addBut = new Button(treeGroup, SWT.NONE | SWT.CENTER);
		addBut.setText("Add...");
		addBut.setLayoutData(setButtonLayout(0));

		editBut = new Button(treeGroup, SWT.NONE | SWT.CENTER);
		editBut.setText("Edit");
		editBut.setLayoutData(setButtonLayout(1));
		editBut.setEnabled(false);

		removeBut = new Button(treeGroup, SWT.NONE | SWT.CENTER);
		removeBut.setText("Remove");
		removeBut.setLayoutData(setButtonLayout(2));
		removeBut.setEnabled(false);

		remAllBut = new Button(treeGroup, SWT.NONE | SWT.CENTER);
		remAllBut.setText("Remove All");
		remAllBut.setLayoutData(setButtonLayout(3));
		remAllBut.setEnabled(false);
		// add button listener
		setParamsListener();

		// create a parmas table
		params_table = new Table(treeGroup, SWT.BORDER | SWT.MULTI);
		FormData params_form = new FormData();
		params_form.left = new FormAttachment(0,510);
		params_form.right = new FormAttachment(100,-5);
		params_form.top = new FormAttachment(0,5);
		params_form.bottom = new FormAttachment(100, -5);
		params_table.setLayoutData(params_form);

		TableColumn getKeyColumn =	new TableColumn(params_table, SWT.NONE);
		getKeyColumn.setText("GetKey");
		getKeyColumn.setWidth(100);
		TableColumn requestKeyColumn = new TableColumn(params_table, SWT.NONE);
		requestKeyColumn.setText("RequestKey");
		requestKeyColumn.setWidth(100);
		TableColumn getTypeColumn = new TableColumn(params_table, SWT.NONE);
		getTypeColumn.setText("GetType");
		getTypeColumn.setWidth(100);


		params_table.setLinesVisible(true);
		params_table.setHeaderVisible(true);

		setTableListener();
		//ErlLogger.debug("main:"+
		//treeGroup.isFocusControl()+" el:"+adapter_table.isFocusControl() +"  "+params_table.isFocusControl());

	}


	public FormData setFlagLableForm(int i){
		FormData tmp_form = new FormData();
		tmp_form.left = new FormAttachment(0,20+i*120);
		tmp_form.right = new FormAttachment(0,120+i*120);
		tmp_form.top = new FormAttachment(0, 5);
		return tmp_form;
	}

	public FormData setFlagButForm(int i){
		FormData tmp_form = new FormData();
		tmp_form.left = new FormAttachment(0,5+i*120);
		tmp_form.right = new FormAttachment(0, 20+i*120);
		tmp_form.top = new FormAttachment(0,5);
		return tmp_form;
	}

	public FormData setComLableForm(int i){
		FormData tmp_form = new FormData();
		tmp_form.left = new FormAttachment(0,3+i*350);
		tmp_form.right = new FormAttachment(0,50+i*350);
		tmp_form.top = new FormAttachment(0, 5);
		return tmp_form;
	}

	public FormData setComButForm(int i){
		FormData tmp_form = new FormData();
		tmp_form.left = new FormAttachment(0,53+i*350);
		tmp_form.right = new FormAttachment(0, 300+i*350);
		tmp_form.top = new FormAttachment(0,5);
		return tmp_form;
	}


	public FormData setOffLableForm(int i ){
		FormData tmp_form = new FormData();
		tmp_form.left = new FormAttachment(0,25+i*100);
		tmp_form.right = new FormAttachment(0,100+i*100);
		tmp_form.top = new FormAttachment(0, 35);
		return tmp_form;
	}

	public FormData setOffButForm(int i){
		FormData tmp_form = new FormData();
		tmp_form.left = new FormAttachment(0,5+i*100);
		tmp_form.right = new FormAttachment(0, 25+i*100);
		tmp_form.top = new FormAttachment(0,35);
		return tmp_form;
	}

	private FormData setLabelLayout(int i){
		FormData comsite_form = new FormData();
		comsite_form.left = new FormAttachment(0,5);
		comsite_form.right = new FormAttachment(0, 100);
		comsite_form.top = new FormAttachment(0,5+i*29);
		return comsite_form;
	}

	private FormData setButtonLayout(int i){
		FormData comsite_form = new FormData();
		comsite_form.left = new FormAttachment(0, 405);
		comsite_form.right = new FormAttachment(0, 510);
		comsite_form.top = new FormAttachment(0,5+i*28);
		return comsite_form;
	}



	// set event listener
	private void setComboListener(final Combo combo){
		combo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ErlLogger.debug("selection :"+chaEntryCom.getSelectionIndex());
				int index = chaEntryCom.getSelectionIndex();
				if (index == 0){
					cha.cha_entry = EwpChannels.CHANNEL_ADAPTER;
				}else if(index == 1){
					cha.cha_entry = EwpChannels.NEW_CALLBACK;
				}else {
					cha.cha_entry = EwpChannels.CHANNEL_CALLBACK;
				}
				set_visiable(index);
			}
		});
	}

	private void initial_text(){
		//ErlLogger.debug("initial text:"+cha.cha_id);
		Iterator<AdapterView> tmpIt = cha.add_view.storeList.iterator();
		ErlLogger.debug("tmpMap:"+cha.add_view.storeList.size());
		cha.add_view.removeMapAll();

		while(tmpIt.hasNext()){
			AdapterView tmpVIews= tmpIt.next();
			TableItem tmpItem = addAdaptItem(tmpVIews);
			cha_view.viewMap.put(tmpItem, tmpVIews);
			remAllBut.setEnabled(true);
		}
	}

	public void initial_default(){
		cha.cha_entry = EwpChannels.CHANNEL_ADAPTER;
		cha.cha_state = "1";
	}

	// add listener for adapter table
	public TableItem addAdaptItem(AdapterView tmpView){
		TableItem tmpItem = new TableItem(adapter_table, SWT.NONE);
		tmpItem.setText(new String[] {tmpView.tranCode, tmpView.viewName, tmpView.adapter, tmpView.procedure});
		return tmpItem;
	}
	public TableItem updateAdaptItem(TableItem tmpItem , AdapterView tmpView){
		tmpItem.setText(new String[] {tmpView.tranCode, tmpView.viewName, tmpView.adapter, tmpView.procedure});
		return tmpItem;
	}


	public TableItem addParamsItem(Table table, AdapterParams tmpParams){
		TableItem tmpItem = new TableItem(table, SWT.NONE);
		tmpItem.setText(new String[] {tmpParams.getKey, tmpParams.requestKey, tmpParams.GetFrom});
		return tmpItem;
	}

	private int focusFlag;
	private void setParamsListener(){
		addBut.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ErlLogger.debug("add button!");
				if (focusFlag == 1){
					AdapterView tmpView = new AdapterView();
					AddDiaolog addLog = new AddDiaolog(parent.getShell(), addFlagGlobal, tmpView);
					addLog.open();
					if (addLog.getReturnCode() == Window.OK) {
						TableItem tmpItem = addAdaptItem(tmpView);
						adapter_table.setSelection(tmpItem);
						cha_view.viewMap.put(tmpItem, tmpView);
						remAllBut.setEnabled(true);
						refresh_parase_table(tmpView);
					}
				} else {
					AdapterView tmpViews = cha_view.viewMap.get(adapt_item[0]);
					AdapterParams tmpParam = new AdapterParams();
					paramsDiaolog addLog = new paramsDiaolog(parent.getShell(), addFlagGlobal, tmpParam);
					addLog.open();
					if (addLog.getReturnCode() == Window.OK) {
						addParamsItem(params_table, tmpParam);
						tmpViews.paramsList.add(tmpParam);
					}
				}

			}
		});

		editBut.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ErlLogger.debug("editBut button!");
				if (focusFlag == 1){
					AdapterView tmpView = cha_view.viewMap.get(adapt_item[0]);
					AddDiaolog addLog = new AddDiaolog(parent.getShell(), !addFlagGlobal, tmpView);
					addLog.open();
					if (addLog.getReturnCode() == Window.OK) {
						cha_view.viewMap.remove(adapt_item[0]);
						cha_view.viewMap.put(updateAdaptItem(adapt_item[0], tmpView), tmpView);
					}
				}
			}
		});

		removeBut.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ErlLogger.debug("remove button!");
				if (focusFlag == 1){
					int[] indeces= adapter_table.getSelectionIndices();
					adapter_table.remove(indeces);
					cha_view.viewMap.remove(adapt_item[0]);
				}
			}
		});

		remAllBut.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ErlLogger.debug("remove all button!");
				if (focusFlag == 1){
					cha_view.removeMapAll();
					adapter_table.removeAll();
				}
				editBut.setEnabled(false);
				removeBut.setEnabled(false);
				remAllBut.setEnabled(false);
			}
		});
	}
	//set table listenr
	private static TableItem[] adapt_item;
	private static TableItem[] param_item;

	private void setTableListener(){

		adapter_table.addFocusListener(new FocusAdapter(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ErlLogger.debug("main:"+" el:"+adapter_table.isFocusControl() +"  "+params_table.isFocusControl());
				focusFlag = 1;
				Table tmp= (Table) e.getSource();
				ErlLogger.debug("co:"+tmp.getSelectionCount());
				addBut.setEnabled(true);
				if (tmp.getItemCount() >0 )
					remAllBut.setEnabled(true);
				else
					remAllBut.setEnabled(false);
			}
		});
		adapter_table.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent event) {
				ErlLogger.debug("table event.");
				if (event.getSource() != null){
					adapt_item = adapter_table.getSelection();
					ErlLogger.debug("table event:"+adapt_item.length);
					if (adapt_item.length == 1){
						editBut.setEnabled(true);
						removeBut.setEnabled(true);
						 refresh_parase_table(cha_view.viewMap.get(adapt_item[0]));
					} else if (adapt_item.length > 1){
						editBut.setEnabled(false);
						removeBut.setEnabled(true);
						refresh_parase_table(cha_view.viewMap.get(adapt_item[0]));
					} else {
						editBut.setEnabled(false);
						removeBut.setEnabled(false);
						refresh_parase_table(null);
					}


				} else {
					ErlLogger.debug("table event: null");
				}
			}
		});

		//params_table.addListener(SWT.SELECTED, newListener());

		params_table.addFocusListener(new FocusAdapter(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ErlLogger.debug(" adl:"+adapter_table.isFocusControl() +"  "+params_table.isFocusControl());
				focusFlag = 0;
				if (adapter_table.getSelection().length >0){
					Table tmp= (Table) e.getSource();
					ErlLogger.debug("co:"+tmp.getSelectionCount());
					addBut.setEnabled(true);
					if (tmp.getItemCount() >0 )
						remAllBut.setEnabled(true);
					else
						remAllBut.setEnabled(false);
				} else {
					addBut.setEnabled(false);
					editBut.setEnabled(false);
					removeBut.setEnabled(false);
					remAllBut.setEnabled(false);
				}

			}
		});

		params_table.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent event) {
				ErlLogger.debug("table event.");
				if (event.getSource() != null){
					param_item = params_table.getSelection();
					ErlLogger.debug("table event:"+param_item.length);
					if (param_item.length == 1){
						editBut.setEnabled(true);
						removeBut.setEnabled(true);
					} else if (param_item.length > 1){
						editBut.setEnabled(false);
						removeBut.setEnabled(true);
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

	public void refresh_parase_table( AdapterView tmpViews){
		if (tmpViews != null){
			ArrayList<AdapterParams> tmpList = tmpViews.paramsList;
			if (tmpList.size() == 0)
				params_table.removeAll();
			else {
				Iterator<AdapterParams> tmpIt = tmpList.iterator();
				while(tmpIt.hasNext()){
					AdapterParams tmpParams = tmpIt.next();
					addParamsItem(params_table, tmpParams);
				}
			}
		} else {
			params_table.removeAll();
		}

	}

	public Listener setCheckButListener(){
		Listener listener = new Listener(){
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				Button tmpBut = (Button) event.widget;

				if (tmpBut == templateBut) {
					cha_view.sourceFlag = getFlag(tmpBut.getSelection());
				} else if (tmpBut == csBut) {
					cha_view.csFlag = getFlag(tmpBut.getSelection());
				} else if (tmpBut == offBut ){
					Boolean tmpFlag = tmpBut.getSelection();
					cha_view.offFileFlag = getFlag(tmpFlag);
					setGroupStatus(tmpFlag);
				} else {
					ErlLogger.debug("else button!");
				}
			}

		};
		return listener;
	}

	public Listener setComboListener(){
		Listener listener = new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				Combo tmp = (Combo) event.widget;
				if (tmp == plateCom) {
					cha_view.plateForm = tmp.getText();
					ErlLogger.debug("p text:"+tmp.getText());
				} else if (tmp == resolutionCom) {
					cha_view.resoulction = tmp.getText();
					ErlLogger.debug("r text:"+tmp.getText());
				} else {
					ErlLogger.debug("unkown combox!");
				}
			}
		};
		return listener;
	}

	private static String imagesStr = "images";
	private static String cssStr = "css";
	private static String luaStr = "lua";
	private static String xhtmlStr = "xhtml";
	private static String channelStr = "channel";

	public Listener setOffListener(){
		Listener listener = new Listener(){
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				Button tmpBut = (Button) event.widget;
				ErlLogger.debug("off But:"+cha_view.offDir.size());
				if (tmpBut == imagesBut) {
					if (tmpBut.getSelection())
						cha_view.offDir.add(imagesStr);
					else
						cha_view.offDir.remove(imagesStr);
				} else if(tmpBut == cssBut ){
					if (tmpBut.getSelection())
						cha_view.offDir.add(cssStr);
					else
						cha_view.offDir.remove(cssStr);
				} else if (tmpBut == luaBut){
					if (tmpBut.getSelection())
						cha_view.offDir.add(luaStr);
					else
						cha_view.offDir.remove(luaStr);
				} else if(tmpBut == xhtmlBut) {
					if (tmpBut.getSelection())
						cha_view.offDir.add(xhtmlStr);
					else
						cha_view.offDir.remove(xhtmlStr);
				} else if (tmpBut == channelBut){
					if (tmpBut.getSelection())
						cha_view.offDir.add(channelStr);
					else
						cha_view.offDir.remove(channelStr);
				} else {
					ErlLogger.debug("unkown off button");
				}
			}
		};
		return listener;
	}


	public String getFlag(Boolean flag){
		if (flag == true)
			return "1";
		else
			return "0";
	}

	private void setInitialOffConf(){
		cha_view.offDir.add(imagesStr);
		cha_view.offDir.add(cssStr);
		cha_view.offDir.add(luaStr);
		cha_view.offDir.add(xhtmlStr);
		cha_view.offDir.add(channelStr);
	}

	private void setGroupStatus(Boolean flag){
		if (flag == true){
			imagesBut.setEnabled(true);
			cssBut.setEnabled(true);
			luaBut.setEnabled(true);
			xhtmlBut.setEnabled(true);
			channelBut.setEnabled(true);
			plateCom.setEnabled(true);
			resolutionCom.setEnabled(true);
			treeGroup.setEnabled(true);
		} else {
			imagesBut.setEnabled(false);
			cssBut.setEnabled(false);
			luaBut.setEnabled(false);
			xhtmlBut.setEnabled(false);
			channelBut.setEnabled(false);
			plateCom.setEnabled(false);
			resolutionCom.setEnabled(false);
			addBut.setEnabled(false);
			editBut.setEnabled(false);
			removeBut.setEnabled(false);
			remAllBut.setEnabled(false);
			treeGroup.setEnabled(false);
		}

	}

	// add params dialog
	class AddDiaolog extends Dialog {
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


		public AddDiaolog(Shell parent, Boolean addFlag, AdapterView view){
			super(parent);
			this.addFlag = addFlag;
			this.view = view;
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
					ErlLogger.debug("text listener!");
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
						paramsDiaolog addLog = new paramsDiaolog(parent.getShell(), addFlagGlobal, tmpParam);
						addLog.open();
						if (addLog.getReturnCode() == Window.OK) {
							//@FIXME error table
							addParamsItem(dparamsTable, tmpParam);
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
					view.adapter.replace(" ", "").isEmpty()||
					view.procedure.replace(" ", "").isEmpty()||
					view.viewName.replace(" ", "").isEmpty())
				okBut.setEnabled(false);
			else
				okBut.setEnabled(true);
		}

	}

	// -------------------------------------------------
	// add params dialog
	class paramsDiaolog extends Dialog {
		private String dialogTitle="";
		private String dialogText="";
		private Button okBut;

		private Label dkeyLabel;
		private Label drkeyLabel;
		private Label dvalueLabel;

		private Text dkeyText;
		private Text drkeyText;
		private Combo dfromCom;
		private  final String[] gettype = {"param", "arg", "session"};

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
			dfromCom.setItems(gettype);


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


}
