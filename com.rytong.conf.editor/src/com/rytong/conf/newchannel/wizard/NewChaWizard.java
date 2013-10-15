package com.rytong.conf.newchannel.wizard;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextEditor;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.rytong.conf.editor.pages.CollectionsPage;
import com.rytong.conf.editor.pages.EwpChannels;

public class NewChaWizard extends Wizard {
    private static String PAGE_TITLE="New Channel Wizard";
    protected EwpChannels cha = new EwpChannels();
    private CollectionsPage parent=null;

    protected HashMap<String, EwpChannels>  keyMap;
    protected String selectId;

    public NewChaWizard NewChaWizard(){
        return this;
    }

    //@FIXME Add dialogSettings to initial the text area
    public void initial(CollectionsPage parent, HashMap<String, EwpChannels>  keyMap, String selectId){
        try{
            //ErlLogger.debug("initial size"+tmpset.size());
            //ErlLogger.debug("initial skeySet ize"+keySet.size());
            WizardDialog newWizardDialog= new WizardDialog(parent.pagecomposite.getShell(), new NewChaWizard(parent, keyMap, selectId));
/*			WizardDialog newWizardDialog= new WizardDialog(parent.pagecomposite.getShell(), new NewChaWizard(parent, tmpset)){
                protected void configureShell(Shell newShell) {

                super.configureShell(newShell);
                newShell.setSize(450, 500);

                newShell.addHelpListener(new HelpListener() {
                    public void helpRequested(HelpEvent event) {
                        // call perform help on the current page
                        if (getCurrentPage() != null) {
                            getCurrentPage().performHelp();
                        }
                    }
                });
                }
            };*/
            newWizardDialog.create();
            Rectangle screenSize = Display.getDefault().getClientArea();
            Shell shell =newWizardDialog.getShell();
            shell.setSize(850, 600);
            shell.setLocation((screenSize.width - shell.getBounds().width) / 2,(
                    screenSize.height -shell.getBounds().height) / 2);
            newWizardDialog.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public NewChaWizard(CollectionsPage parentPage, HashMap<String, EwpChannels>  tmpMap, String selectId){
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
            cha = parent.ChaMap.get(selectId).clone();
            addPage(new NewChaWizardDetailPage(this));
            addPage(new NewChaWizardViewPage(this));

        } else {
            addPage(new NewChaWizardDetailPage(this));
            addPage(new NewChaWizardViewPage(this));
        }
        //addPage(new NewCollWizardItemsPage(parent, coll));
    }

    @Override
    public boolean performFinish() {
        // TODO Auto-generated method stub
        ErlLogger.debug("Channel Wizard finish!");
        if (selectId != null){
            parent.erlBackend_addCha(selectId, cha);
            parent.ChaMap.remove(selectId);
        } else{
            parent.erlBackend_addCha("", cha);
        }
        create_source_code(cha);
        parent.ChaMap.put(cha.cha_id, cha);

        //parent.ChaMap.remove(cha.cha_id);
        parent.cha_table.refreshTable();
        parent.setVisiable();
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

    private void create_source_code(EwpChannels tmpCha){

        if (tmpCha.add_view.csFlag){
            ErlLogger.debug("cs flag is true!");
            final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            final IResource resource = root.findMember(new Path("test.cs"));
         } else {
             ErlLogger.debug("cs flag is false");
         }
    }

    public TextEditor getTextEditor(){
        return parent.getEditor();
    }


}
