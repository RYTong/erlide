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
	protected EwpCollections coll = null;
	private CollectionsPage parent=null;

	protected Set<String> keySet = null;

	public NewCollWizard NewCollWizard(){
		return this;
	}

	//@FIXME Add dialogSettings to initial the text area
	public void initial(CollectionsPage parent, Set<String> tmpset){
		try{
			ErlLogger.debug("initial size"+tmpset.size());

			//ErlLogger.debug("initial skeySet ize"+keySet.size());
			WizardDialog newWizard= new WizardDialog(parent.pagecomposite.getShell(), new NewCollWizard(parent, tmpset));
			newWizard.setBlockOnOpen(true);
			newWizard.open();


		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public NewCollWizard(CollectionsPage parentPage, Set<String> tmpset){
		// add the wizard page
		super();
		setWindowTitle(PAGE_TITLE);
		keySet = tmpset;
		parent=parentPage;
		if (parent != null)
	    	coll = parent.newEwpCollections();
	}

    public void addPages() {
    	ErlLogger.debug("add page!");
		addPage(new NewCollWizardDetailPage(coll, keySet));
		addPage(new NewCollWizardItemsPage(parent, coll));
    }

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub


		OtpErlangObject result = parent.erlBackend_addColl(coll);
		parent.setDocument(result);
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
