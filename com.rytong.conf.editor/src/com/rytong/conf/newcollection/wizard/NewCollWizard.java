package com.rytong.conf.newcollection.wizard;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;
import org.erlide.utils.Util;

import com.ericsson.otp.erlang.OtpErlangBinary;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.editor.pages.CollectionsPage;
import com.rytong.conf.editor.pages.EwpCollections;


public class NewCollWizard extends Wizard {

	private static String PAGE_TITLE="New Collection Wizard";
	protected EwpCollections coll = new EwpCollections();
	protected CollectionsPage parent=null;

	protected Set<String> keySet = null;
	protected HashMap<String, Object> keyMap;
	protected String selectId;

	public NewCollWizard NewCollWizard(){
		return this;
	}

	//@FIXME Add dialogSettings to initial the text area
	public void initial(CollectionsPage parent, HashMap<String, Object> tableMapStore, String selectId){
		try{
			ErlLogger.debug("initial size"+tableMapStore.size());

			//ErlLogger.debug("initial skeySet ize"+keySet.size());
			WizardDialog newWizardDialog= new WizardDialog(parent.pagecomposite.getShell(), new NewCollWizard(parent, tableMapStore, selectId));

			newWizardDialog.create();
			Rectangle screenSize = Display.getDefault().getClientArea();
			Shell shell =newWizardDialog.getShell();
			shell.setSize(600, 600);
			shell.setLocation((screenSize.width - newWizardDialog.getShell().getBounds().width) / 2,(
					screenSize.height -newWizardDialog.getShell().getBounds().height) / 2);
			newWizardDialog.open();

		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public NewCollWizard(CollectionsPage parentPage, HashMap<String, Object> tmpMap, String selectId){
		// add the wizard page
		super();
		setWindowTitle(PAGE_TITLE);
		keyMap = tmpMap;
		parent=parentPage;
		this.selectId = selectId;
	}

	public void addPages() {
		ErlLogger.debug("add page!");
		if (selectId != null){
			EwpCollections tmp = (EwpCollections) parent.CollMap.get(selectId);
			coll = tmp.clone();
			addPage(new NewCollWizardDetailPage(this));
			addPage(new NewCollWizardItemsPage(this));
		}else {
			addPage(new NewCollWizardDetailPage(this));
			addPage(new NewCollWizardItemsPage(this));
		}
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		if (selectId != null){
			parent.erlBackend_addColl(selectId, coll);
			parent.CollMap.remove(selectId);
		} else
			parent.erlBackend_addColl("", coll);

		parent.CollMap.put(coll.coll_id, coll);
		parent.coll_table.refreshTable();
		parent.refreshTreePage();
		return true;
	}

	public boolean performCancel(){
		ErlLogger.debug("Collection Wizard cancel!");
		boolean cancelFlag = MessageDialog.openConfirm(getShell(), "Warning", "你是否要取消操作，如果取消，输入内容将全部消失。");
		if (cancelFlag)
			return true;
		else
			return false;
	}





}
