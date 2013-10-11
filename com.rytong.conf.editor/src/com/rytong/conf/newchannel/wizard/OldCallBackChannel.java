package com.rytong.conf.newchannel.wizard;


import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.newchannel.wizard.NewChaWizardDetailPage.AddDiaolog;

public class OldCallBackChannel {
	private NewChaWizard wizard;
	private NewChaWizardViewPage parent;
	private EwpChannels cha;
	private String selectId;


	private Label csFlag;
	private Button csBut;

	protected Composite composite;
	private Table table;
	private Button addBut;
	private Button editBut;
	private Button removeBut;
	private Button remAllBut;

	private boolean addFlagGlobal = true;

	public OldCallBackChannel(NewChaWizard wizard, NewChaWizardViewPage parent){
		this.wizard = wizard;
		this.parent = parent;
		cha = wizard.cha;
		this.selectId = wizard.selectId;
	}

	public Composite initial_composite(){

		composite = new Group(parent.parentcomposite, SWT.BORDER);

		FormData template_form = new FormData();
		template_form.left = new FormAttachment(0);
		template_form.right = new FormAttachment(100,-5);
		template_form.top = new FormAttachment(0,30);
		template_form.bottom = new FormAttachment(100, -3);
		composite.setLayoutData(template_form);
		composite.setLayout(new FormLayout());
		//templateGroup.setText("Template List");

		csFlag = new Label(composite, SWT.NONE);
		csFlag.setText("生成CS模板");
		FormData csl_form = new FormData();
		csl_form.left = new FormAttachment(0,40);
		csl_form.right = new FormAttachment(100, -10);
		csl_form.top = new FormAttachment(0, 10);
		csFlag.setLayoutData(csl_form);
		csBut = new Button(composite, SWT.BORDER | SWT.CHECK);
		FormData csb_form = new FormData();
		csb_form.left = new FormAttachment(0,20);
		csb_form.right = new FormAttachment(0, 40);
		csb_form.top = new FormAttachment(0,10);
		csBut.setLayoutData(csb_form);

		draw_table(composite);
		if (selectId != null){
			intialTable();
			initial_button();
		}

		ErlLogger.debug("entry:"+cha.cha_entry);

		return composite;
	}


	public void draw_table(Composite composite){
		table = new Table(composite, SWT.BORDER | SWT.MULTI);
		TableColumn tranColumn = new TableColumn(table, SWT.NONE);
		tranColumn.setText("tranCode");
		tranColumn.setWidth(200);
		TableColumn viewColumn = new TableColumn(table, SWT.NONE);
		viewColumn.setText("viewName");
		viewColumn.setWidth(200);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		FormData table_form = new FormData();
		table_form.top = new FormAttachment(0, 40);
		table_form.left = new FormAttachment(0, 20);
		table_form.right = new FormAttachment(100, -170);
		table_form.bottom = new FormAttachment(100, -20);
		table.setLayoutData(table_form);

		addBut = new Button(composite, SWT.BORDER | SWT.CENTER);
		addBut.setText("Add...");
		addBut.setLayoutData(setButtonLayout(0));

		editBut = new Button(composite, SWT.BORDER | SWT.CENTER);
		editBut.setText("Edit");
		editBut.setLayoutData(setButtonLayout(1));

		removeBut = new Button(composite, SWT.BORDER | SWT.CENTER);
		removeBut.setText("Remove");
		removeBut.setLayoutData(setButtonLayout(2));

		remAllBut = new Button(composite, SWT.BORDER | SWT.CENTER);
		remAllBut.setText("Remove All");
		remAllBut.setLayoutData(setButtonLayout(3));

		setTableListener();
		setButtonListener();


	}

	private FormData setButtonLayout(int i){
		FormData comsite_form = new FormData();
		comsite_form.left = new FormAttachment(100, -150);
		comsite_form.right = new FormAttachment(100, -20);
		comsite_form.top = new FormAttachment(0, 45+i*35);
		return comsite_form;
	}

	public void initial_button(){
		editBut.setEnabled(false);
		removeBut.setEnabled(false);
		if (table.getItemCount() == 0)
			remAllBut.setEnabled(false);
	}

	public void intialTable(){

		Iterator<OldCallbackParams> tmpIt = cha.add_view.initialOldList().iterator();
		while(tmpIt.hasNext()){
			OldCallbackParams viewParam = tmpIt.next();
			addParamsItem(table, viewParam);
		}
	}

	public TableItem addParamsItem(Table table, OldCallbackParams tmpParams){
		TableItem tmpItem = new TableItem(table, SWT.NONE);
		tmpItem.setText(new String[] {tmpParams.tranCode, tmpParams.viewName});
		return tmpItem;
	}

	public Listener setCheckButListener(){
		Listener listener = new Listener(){
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				Button tmpBut = (Button) event.widget;

				if (tmpBut == csBut) {
					cha.add_view.csFlag = parent.getFlag(tmpBut.getSelection());
				} else {
					ErlLogger.debug("else button!");
				}
			}

		};
		return listener;
	}

	private static int[] table_item;

	public void setTableListener(){
		table.addMouseListener(new MouseAdapter(){
			 public void mouseDown(MouseEvent event) {
					ErlLogger.debug("table event.");
					if (event.getSource() != null){
						table_item = table.getSelectionIndices();
						if (table_item.length == 1){
							ErlLogger.debug("table event:"+table_item[0]);
							editBut.setEnabled(true);
							removeBut.setEnabled(true);
						} else if (table_item.length > 1){
							ErlLogger.debug("table event:"+table_item.length);
							editBut.setEnabled(false);
							removeBut.setEnabled(true);
							remAllBut.setEnabled(true);
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

	public void setButtonListener(){
		addBut.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ErlLogger.debug("add button!");
				OldCallbackParams tmpOld = new OldCallbackParams();
				viewDiaolog newDialog = new viewDiaolog(parent.getShell(), addFlagGlobal, tmpOld);
				newDialog.open();
				ErlLogger.debug("dialog result :"+newDialog.getReturnCode());
				if (newDialog.getReturnCode()==Window.OK){
					TableItem tmpItem = new TableItem(table, SWT.BORDER);
					tmpOld.viewName = tmpOld.viewName.replace(" ", "");
					tmpItem.setText(new String[]{tmpOld.tranCode, tmpOld.viewName});
					cha.add_view.addOldView(tmpOld);
					ErlLogger.debug("old list:"+cha.add_view.oldList.size());
					remAllBut.setEnabled(true);
				}


			}
		});

		editBut.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ErlLogger.debug("edit button!");
				OldCallbackParams tmpOld = cha.add_view.getOldView(table_item[0]);
				ErlLogger.debug("edit button:"+tmpOld.tranCode);
				viewDiaolog newDialog = new viewDiaolog(parent.getShell(), !addFlagGlobal, tmpOld);
				newDialog.open();
				ErlLogger.debug("dialog result :"+newDialog.getReturnCode());
				if (newDialog.getReturnCode()==Window.OK){
					TableItem tmpItem = table.getItem(table_item[0]);

					tmpOld.viewName = tmpOld.viewName.replace(" ", "");
					tmpItem.setText(new String[]{tmpOld.tranCode, tmpOld.viewName});
					cha.add_view.refreshOldView(table_item[0], tmpOld);
					ErlLogger.debug("old list:"+cha.add_view.oldList.size());
				}

			}
		});

		removeBut.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ErlLogger.debug("remove button:"+table_item[0]);
				if (table.getItemCount() != 0){
					table.remove(table_item[0]);
					cha.add_view.removeOldView(table_item[0]);
					remAllBut.setEnabled(false);
					removeBut.setEnabled(false);
				}
			}
		});

		remAllBut.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ErlLogger.debug("remove all button!");
				table.removeAll();
				cha.add_view.clearOldView();
				removeBut.setEnabled(false);
				remAllBut.setEnabled(false);
			}
		});
	}


	public void set_visiable(){
		composite.setVisible(true);
	}

	public void set_unvisiable(){
		composite.setVisible(false);
	}


	class viewDiaolog extends Dialog {
		private String dialogTitle="";
		private String dialogText="";
		private Button okBut;

		private Label keyLabel;
		private Label valueLabel;
		private Text dialogTexKey;
		private Text dialogTexValue;

		private boolean addFlag;
		private OldCallbackParams oldParams;


		public viewDiaolog(Shell parent, Boolean addFlag, OldCallbackParams oldParams)
		{
			super(parent);
			this.addFlag = addFlag;
			this.oldParams = oldParams;
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

		private void setOkButton(){

			if (oldParams.tranCode.replace(" ", "").isEmpty() || oldParams.viewName.replace(" ", "").isEmpty())
				okBut.setEnabled(false);
			else
				okBut.setEnabled(true);
		}

	}



}
