package com.rytong.conf.newchannel.wizard;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;

public class OldCallBackChannel {
	private NewChaWizard wizard;
	private NewChaWizardViewPage parent;
	private EwpChannels cha;
	private String selectId;


	private Label csFlag;
	private Button csBut;

	protected Composite composite;
	private Table table;

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
		csl_form.left = new FormAttachment(0,20);
		csl_form.right = new FormAttachment(0,120120);
		csl_form.top = new FormAttachment(0, 5);
		csFlag.setLayoutData(csl_form);
		csBut = new Button(composite, SWT.BORDER | SWT.CHECK);
		FormData csb_form = new FormData();
		csb_form.left = new FormAttachment(0,5);
		csb_form.right = new FormAttachment(0, 20);
		csb_form.top = new FormAttachment(0,5);
		csBut.setLayoutData(csb_form);

		draw_table(composite);


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
		table_form.right = new FormAttachment(0, 410);
		table_form.bottom = new FormAttachment(100, -20);
		table.setLayoutData(table_form);

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

	public void set_visiable(){
		composite.setVisible(true);
	}

	public void set_unvisiable(){
		composite.setVisible(false);
	}
}
