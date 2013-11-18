package com.rytong.conf.newchannel.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import org.eclipse.swt.widgets.Combo;
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
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.erlide.core.model.root.ErlModelManager;
import org.erlide.core.model.root.IErlProject;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.ChannelConfPlugin;
import com.rytong.conf.editor.pages.EwpChannels;

public class NewChaWizardViewPage extends WizardPage {
    private ChannelAdapterTemplate createTmpUtil;
    private static String PAGE_NAME = "Create a Channel";
    private static String PAGE_DESC = "为新Channel添加代码及页面支持.";
    private EwpChannels cha;
    private NewChaWizard wizard;
    private WizarParams  cha_view;

    private String selectId;
    private Composite parent;

    private Group templateGroup;


    protected NewChaWizardViewPage(NewChaWizard wizard) {
        super(PAGE_NAME);
        setTitle(PAGE_NAME);
        setDescription(PAGE_DESC);
        this.wizard = wizard;
        this.cha = wizard.cha;
        cha_view = cha.add_view;
        this.selectId = wizard.selectId;
        createTmpUtil = wizard.getCsTmpUtil();
        // TODO Auto-generated constructor stub
    }
    protected Composite parentcomposite ;
    protected OldCallBackChannel oldCallBack;
    protected AdapterChannel adapterCallBack;


    private Label separateLine;
    private Label entryLabel;
    private Label chaEntryLabel;


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
        ErlLogger.debug("flag:"+flag);
        chaEntryLabel = new Label(parentcomposite, SWT.NONE);
        chaEntryLabel.setText(cha.getEntryName());
        FormData com_form = new FormData();
        com_form.left = new FormAttachment(0,50);
        com_form.right = new FormAttachment(100, -5);
        com_form.top = new FormAttachment(0, 2);

        chaEntryLabel.setLayoutData(com_form);
//        chaEntryCom = new Combo(parentcomposite, SWT.BORDER|SWT.READ_ONLY);
//        chaEntryCom.setItems(new String[]{"适配","新回调","旧回调"});
//        chaEntryCom.select(flag);
//        FormData com_form = new FormData();
//        com_form.left = new FormAttachment(0,50);
//        com_form.right = new FormAttachment(100, -5);
//        com_form.top = new FormAttachment(0);
//
//        chaEntryCom.setLayoutData(com_form);
//        setComboListener(chaEntryCom);

        separateLine = new Label(parentcomposite, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BORDER);
        FormData seprate_form = new FormData();
        seprate_form.left = new FormAttachment(0,5);
        seprate_form.right = new FormAttachment(100, -5);
        seprate_form.top = new FormAttachment(0, 27);
        separateLine.setLayoutData(seprate_form);

        //initial_adapter_group();
        adapterCallBack = new AdapterChannel(wizard, this);
        templateGroup = adapterCallBack.initial_composite();
        // initial old callback composit
        oldCallBack = new OldCallBackChannel(wizard, this);
        oldCallBack.initial_composite();

        if (selectId !=null){
            if (flag == 0){
                set_visiable(flag);
                adapterCallBack.initial_text();
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
        chaEntryLabel.setText(cha.getEntryName());
    }



    public void initial_default(){
        cha.cha_entry = EwpChannels.CHANNEL_ADAPTER;
        cha.cha_state = "1";
        set_visiable(EwpChannels.get_entry_index(cha.cha_entry));
    }


    public void tmpTest(){
        createTmpUtil.createCsTemplate(cha);
    }

    public void offTest(){
        createTmpUtil.createOffTemplate(cha);
    }

    public void erlTest(){
        createTmpUtil.createAdpErlTemplate(cha);
    }

}
