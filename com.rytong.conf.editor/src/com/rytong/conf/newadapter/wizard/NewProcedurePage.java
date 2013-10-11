package com.rytong.conf.newadapter.wizard;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.erlide.jinterface.ErlLogger;

public class NewProcedurePage extends WizardPage{

    private ISelection selection;

    private Label proc_id;
    private Label proc_adapter;
    private Label proc_return;
    private Label proc_path;
    private Label proc_log;
    private Label proc_code;
    private Label proc_sample;
    private Label proc_useSample;

    private Text text_id;
    private Text text_adapter;
    private Text text_path;

    private Text text_sample;

    private Combo text_return;
    private Combo combo_log;
    private Combo combo_code;
    private Combo combo_usesample;

    /**
     * 包含procedure 编辑页面的一个容器
     * @param selection
     */
    protected NewProcedurePage(ISelection selection) {
        super("New Procedure Wizard");
        setTitle("New a Procedure");
        setDescription("This wizard creates a new Procedure in adapter.conf.");
        this.selection = selection;
        // TODO Auto-generated constructor stub
    }

    /**
     * 初始化容器，在容器内添加procedure相关元素
     */
    public void createControl(Composite parent) {
        // TODO Auto-generated method stub
        ErlLogger.debug("test:"+selection.toString());

        Composite parentcomposite = new Composite(parent, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        parentcomposite.setLayout(formLayout);
        Group composite = new Group(parentcomposite, SWT.BORDER);
        composite.setText("Procedure Detail:");
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(100,-5);
        comsite_form.top = new FormAttachment(0,3);
        comsite_form.bottom = new FormAttachment(100, -5);
        composite.setLayoutData(comsite_form);
        composite.setLayout(new FormLayout());


        proc_id = new Label(composite, SWT.NONE);
        proc_id.setText("Id*:");
        proc_id.setLayoutData(setLabelLayout(0));
        text_id = new Text(composite, SWT.BORDER);
        text_id.setLayoutData(setTextLayout(0));

        proc_adapter = new Label(composite, SWT.NONE);
        proc_adapter.setText("Adapter*:");
        proc_adapter.setLayoutData(setLabelLayout(1));

        text_adapter = new Text(composite, SWT.BORDER);
        text_adapter.setLayoutData(setTextLayout(1));

        proc_path = new Label(composite, SWT.NONE);
        proc_path.setText("Id*:");
        proc_path.setLayoutData(setLabelLayout(2));
        text_path = new Text(composite, SWT.BORDER);
        text_path.setLayoutData(setTextLayout(2));

        proc_return = new Label(composite, SWT.NONE);
        proc_return.setText("Return Type*:");
        proc_return.setLayoutData(setLabelLayout(3));
        text_return = new Combo(composite, SWT.BORDER);
        text_return.setItems(new String[]{"xml", "json"});
        text_return.select(0);
        text_return.setLayoutData(setTextLayout(3));

        proc_code = new Label(composite, SWT.NONE);
        proc_code.setText("CallBack Code:");
        proc_code.setLayoutData(setLabelLayout(4));
        combo_code = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
        // if gen_code is true, the procedure id shoule be {fun, mod}
        combo_code.setItems(new String[]{"false", "true"});
        combo_code.select(0);
        combo_code.setLayoutData(setTextLayout(4));

        proc_log = new Label(composite, SWT.NONE);
        proc_log.setText("Log:");
        proc_log.setLayoutData(setLabelLayout(5));
        combo_log = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
        combo_log.setItems(new String[]{"true", "false"});
        combo_log.select(0);
        combo_log.setLayoutData(setTextLayout(5));

        proc_useSample = new Label(composite, SWT.NONE);
        proc_useSample.setText("Use Sample:");
        proc_useSample.setLayoutData(setLabelLayout(6));
        combo_usesample = new Combo(composite, SWT.BORDER| SWT.READ_ONLY);
        combo_usesample.setItems(new String[]{"true", "false"});
        combo_usesample.select(0);
        combo_usesample.setLayoutData(setTextLayout(6));

        proc_sample = new Label(composite, SWT.NONE);
        proc_sample.setText("Sample Data:");
        proc_sample.setLayoutData(setLabelLayout(7));
        text_sample = new Text(composite, SWT.BORDER);
        text_sample.setLayoutData(setTextLayout(7));

        ErlLogger.debug("shell:"+parent.getShell().getLocation());
        Point point = parent.getShell().getLocation();
        parent.getShell().setSize(500, 600);
        parent.getShell().setLocation(point.x, point.y);

        setControl(parentcomposite);
        setPageComplete(false);
    }

    /**
     * label 在容器内的布局计算
     * @param i
     * @return
     */
    private FormData setLabelLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(0, 100);
        comsite_form.top = new FormAttachment(0,5+i*29);
        return comsite_form;
    }

    /**
     * text 在容器内布局的计算
     * @param i
     * @return
     */
    private FormData setTextLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,107);
        comsite_form.right = new FormAttachment(100, -10);
        comsite_form.top = new FormAttachment(0,5+i*28);
        return comsite_form;
    }

}
