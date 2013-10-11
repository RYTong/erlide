package com.rytong.conf.adapter.editor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.util.ConfParams;

public class AdapterEditorProcedureParamsDialog extends Dialog {
		private String dialogTitle="";
		private String dialogText="";
		private Button okBut;

		private Label keyLabel;
		private Label valueLabel;
		private Text dialogTexKey;
		private Text dialogTexValue;

		private boolean editFlag;
		private ConfParams param;


		public AdapterEditorProcedureParamsDialog(Shell parent, ConfParams param, boolean flag)
		{
			super(parent);
			editFlag = flag;
			this.param = param;
			if (editFlag){
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
			FormData msg_form = new FormData();
			msg_form.left = new FormAttachment(0,5);
			msg_form.right = new FormAttachment(100, -5);
			msg_form.top = new FormAttachment(0,5);
			msg.setLayoutData(msg_form);

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
			if (!editFlag){
				dialogTexKey.setText(param.getKey());
				dialogTexValue.setText(param.getValue());
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


		protected void configureShell(Shell newShell){
			ErlLogger.debug("configureShell ok!");
			super.configureShell(newShell);
			newShell.setText(dialogTitle);
		}

		protected Point getInitialSize() {
			ErlLogger.debug("getInitialSize ok!");
			okBut = getButton(IDialogConstants.OK_ID);
			if (editFlag){
				okBut.setEnabled(false);
			}
			return new Point(400,200);
		}

		private void setTextListener(){
			dialogTexKey.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent e) {
					// TODO Auto-generated method stub
					param.setKey(dialogTexKey.getText());
					//ErlLogger.debug("dialogKeyStr:"+dialogKeyStr);
					setOkButton();

				}
			});
			dialogTexValue.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent e) {
					// TODO Auto-generated method stub
					param.setValue(dialogTexValue.getText());
					//@FIXME check the type of input params
					//ErlLogger.debug("dialogKeyStr index:"+dialogValueStr.indexOf("\""));
					setOkButton();
				}
			});
		}

		private void setOkButton(){
			if (param.getKey().replace(" ", "").isEmpty()	|| param.getValue().replace(" ", "").isEmpty())
				okBut.setEnabled(false);
			else
				okBut.setEnabled(true);
		}


}
