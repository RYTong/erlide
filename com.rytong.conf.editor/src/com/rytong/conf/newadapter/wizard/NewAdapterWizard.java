package com.rytong.conf.newadapter.wizard;

import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.adapter.editor.AdapterEditorPage;
import com.rytong.conf.adapter.editor.AdapterEditorTreeComposite;
import com.rytong.conf.adapter.editor.EwpAdapter;
import com.rytong.conf.adapter.editor.EwpAdpaterList;
import com.rytong.conf.adapter.editor.EwpProcedure;
import com.rytong.conf.editor.pages.CollectionsPage;
import com.rytong.conf.editor.pages.EwpChannels;


public class NewAdapterWizard extends Wizard {

    private static String PAGE_TITLE="New Adapter Wizard";
    protected EwpChannels cha = new EwpChannels();
    private AdapterEditorTreeComposite parent=null;

    protected EwpAdpaterList tmpAdpList;
    protected Object g_tree_selection;
    private NewAdapterWizardComonPage g_wizard_page;
    private AdapterEditorPage parent_backend;

    public NewAdapterWizard NewAdapterWizard(){
        return this;
    }

    //@FIXME Add dialogSettings to initial the text area
    public void initial(AdapterEditorTreeComposite parent,EwpAdpaterList tmpAdpList){
        try{
            WizardDialog newWizardDialog= new WizardDialog(parent.getParentComposite().getShell(), new NewAdapterWizard(parent, tmpAdpList));

            newWizardDialog.create();
            Rectangle screenSize = Display.getDefault().getClientArea();
            Shell shell =newWizardDialog.getShell();
            shell.setSize(650, 700);
            shell.setLocation((screenSize.width - shell.getBounds().width) / 2,(
                    screenSize.height -shell.getBounds().height) / 2);
            newWizardDialog.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public NewAdapterWizard(AdapterEditorTreeComposite parentPage, EwpAdpaterList tmpAdpList){
        // add the wizard page
        super();
        setWindowTitle(PAGE_TITLE);
        this.tmpAdpList = tmpAdpList;
        parent=parentPage;
        if (parent != null){
            this.g_tree_selection = parent.getGlobalSelection();
            parent_backend = parent.getParentPage();
        }
    }


    public void addPages() {
        ErlLogger.debug("add page!");
        g_wizard_page = new NewAdapterWizardComonPage(this);
        addPage(g_wizard_page);
    }

    @Override
    public boolean performFinish() {
        // TODO Auto-generated method stub
        ErlLogger.debug("Channel Wizard finish!");
        Object m_tmp_obj = g_wizard_page.getSelection();
        if (m_tmp_obj instanceof EwpAdapter){
            ErlLogger.debug("ewpadapter !");
            EwpAdapter m_tmp_adp = (EwpAdapter) m_tmp_obj;
            parent_backend.erlBackend_addAdapter(m_tmp_adp);
            tmpAdpList.addAdapterList(m_tmp_adp);
        }else if (m_tmp_obj instanceof EwpProcedure){
            EwpProcedure m_tmp_pro = (EwpProcedure) m_tmp_obj;
            parent_backend.erlBackend_addProcedure(m_tmp_pro);
            tmpAdpList.addProcedureList(m_tmp_pro);
        }
        parent_backend.refreshTree();
        return true;
    }

    public boolean performCancel(){
        ErlLogger.debug("Channel Wizard cancel!");
        boolean cancelFlag = MessageDialog.openConfirm(getShell(), "Warning", "你是否要取消操作，如果取消，输入内容将全部消失。");
        if (cancelFlag)
            return true;
        else
            return false;
    }
}
