package com.rytong.conf.newadapter.wizard;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.adapter.editor.EwpAdapter;
import com.rytong.conf.adapter.editor.EwpAdpaterList;
import com.rytong.conf.adapter.editor.EwpProcedure;
import com.rytong.conf.editor.pages.EwpChannels;

public class NewAdapterWizardComonPage extends WizardPage {

    private static String PAGE_NAME = "Create a Adapter";
    private static String PAGE_DESC = "创建一个新的Adapter.";
    private NewAdapterWizard parent;
    private Composite pareng_composite;
    private static String[] ADAPTER_TYPE = new String[]{"Adapter", "Procedure"};

    private NewAdapterWizardAdapterComposite adapter_composite;
    private NewAdapterWizardProcedureComposite procedure_composite;
    private EwpAdapter g_tmp_adapter;
    private EwpProcedure g_tmp_procedure;
    private Object g_tmp_obj;

    protected NewAdapterWizardComonPage(NewAdapterWizard parent) {
        super(PAGE_NAME);
        setTitle(PAGE_NAME);
        setDescription(PAGE_DESC);
        this.parent = parent;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createControl(Composite composite) {
        // TODO Auto-generated method stub
        pareng_composite = composite;
        Composite parentcomposite = new Composite(composite, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        parentcomposite.setLayout(formLayout);
        addAdapterDetail(parentcomposite);
        addProcedureDetail(parentcomposite);
        addCombox(parentcomposite);

        setControl(parentcomposite);
        setPageComplete(true);
    }

    public void addCombox(Composite composite){
        Label m_choic_label = new Label(composite, SWT.NONE);
        m_choic_label.setText("选择添加类型:");

        FormData label_form = new FormData();
        label_form.left = new FormAttachment(0, 5);
        label_form.right = new FormAttachment(0, 100);
        label_form.top = new FormAttachment(0, 8);
        label_form.bottom = new FormAttachment(0, 40);
        m_choic_label.setLayoutData(label_form);

        final Combo m_choic_combo = new Combo(composite, SWT.BORDER|SWT.READ_ONLY);
        m_choic_combo.setItems(ADAPTER_TYPE);
        FormData combo_form = new FormData();
        combo_form.left = new FormAttachment(0, 105);
        combo_form.right = new FormAttachment(100, -10);
        combo_form.top = new FormAttachment(0, 5);
        combo_form.bottom = new FormAttachment(0, 35);
        m_choic_combo.setLayoutData(combo_form);

        Object p_selection = parent.g_tree_selection;
        if (p_selection instanceof EwpProcedure){
            m_choic_combo.select(1);
            setProcedureVisiable();
            g_tmp_obj=g_tmp_procedure;
        } else {
            m_choic_combo.select(0);
            setAdapterVisiable();
            g_tmp_obj=g_tmp_adapter;
        }
        m_choic_combo.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                int m_selection_index = m_choic_combo.getSelectionIndex();
                ErlLogger.debug("type selection:"+m_choic_combo.getSelectionIndex());
                if (m_selection_index == 0){
                    g_tmp_obj=g_tmp_adapter;
                    setAdapterVisiable();
                } else {
                    g_tmp_obj=g_tmp_procedure;
                    setProcedureVisiable();
                }
            }
        });}



    public void addAdapterDetail(Composite composite){
        adapter_composite = new NewAdapterWizardAdapterComposite();
        g_tmp_adapter = adapter_composite.initial_composite(this, composite);
    }

    public void addProcedureDetail(Composite composite){
        procedure_composite = new NewAdapterWizardProcedureComposite();
        g_tmp_procedure = procedure_composite.initial_composite(this, composite);
    }

    private void setAdapterVisiable(){
        adapter_composite.setCompositeVisiable();
        procedure_composite.setCompositeUnVisiable();
    }

    private void setProcedureVisiable(){
        adapter_composite.setCompositeUnVisiable();
        procedure_composite.setCompositeVisiable();
    }

    protected void setErrorNotice(String msg, boolean state){
        setErrorMessage(msg);
        if (msg != null){
            setPageComplete(false);
        } else
            setPageCompleteState(state);
    }

    protected void setPageCompleteState(boolean state){
        setPageComplete(state);
    }

    public EwpAdpaterList getAdpList(){
        return parent.tmpAdpList;
    }

    public Composite getComposite(){
        return pareng_composite;
    }

    protected Object getSelection(){
        return g_tmp_obj;
    }
}
