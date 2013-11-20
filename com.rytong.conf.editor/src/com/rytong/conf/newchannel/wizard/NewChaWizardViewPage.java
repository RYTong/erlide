package com.rytong.conf.newchannel.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.util.ChannelConfUtil;

public class NewChaWizardViewPage extends WizardPage {
    private ChannelAdapterTemplate createTmpUtil;
    private static String PAGE_NAME = "Create a Channel";
    private static String PAGE_DESC = "为新Channel添加代码及页面支持.";
    private EwpChannels cha;
    private NewChaWizard wizard;
    private WizarParams  cha_view;

    private String selectId;
    private Composite parent;
    protected ChannelConfUtil confUtil;


    protected NewChaWizardViewPage(NewChaWizard wizard) {
        super(PAGE_NAME);
        setTitle(PAGE_NAME);
        setDescription(PAGE_DESC);
        this.wizard = wizard;
        this.cha = wizard.cha;
        cha_view = cha.add_view;
        this.selectId = wizard.selectId;
        createTmpUtil = wizard.getCsTmpUtil();
        confUtil = new ChannelConfUtil();
        // TODO Auto-generated constructor stub
    }
    protected Composite parentcomposite ;
    protected OldCallBackChannel oldCallBack;
    protected AdapterChannel adapterCallBack;
    protected NewCallBackChannel newCallBack;


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
        adapterCallBack.initial_composite();
        // initial old callback composit
        oldCallBack = new OldCallBackChannel(wizard, this);
        oldCallBack.initial_composite();

        newCallBack = new NewCallBackChannel(wizard, this);
        newCallBack.initial_composite();

        if (selectId !=null){
            set_visiable(flag);
            if (flag == 0){
                adapterCallBack.initial_text();
            } else if(flag == 1){
                newCallBack.initial_text();
            } else {
                oldCallBack.initial_text();
            }
        } else{
            initial_default();
        }

        setControl(parentcomposite);
        setPageComplete(true);
    }

    public void set_visiable(int flag){
        if (flag == 0){

            adapterCallBack.set_visiable();
            oldCallBack.set_unvisiable();
            newCallBack.set_unvisiable();
        } else if (flag == 1) {
            adapterCallBack.set_unvisiable();
            oldCallBack.set_unvisiable();
            newCallBack.set_visiable();
        }else {
            adapterCallBack.set_unvisiable();
            oldCallBack.set_visiable();
            newCallBack.set_unvisiable();
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
