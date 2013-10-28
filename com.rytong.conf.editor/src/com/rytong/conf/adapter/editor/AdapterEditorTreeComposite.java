package com.rytong.conf.adapter.editor;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangTuple;
import com.rytong.conf.newadapter.wizard.NewAdapterWizard;

public class AdapterEditorTreeComposite {

    private Composite parentComposite;
    private AdapterEditorPage parent;
    private AdapterEditorProcedureComposite right_com;
    private AdapterEditorAdapterComposite adp_com;
    private AdapterEditorTreeComposite g_this;
    private Tree tree;

    private Button addBut;
    private Button removeBut;


    //private TableViewer viewer;
    private TreeViewer viewer;
    private Object g_tree_selection=null;


    public AdapterEditorTreeComposite(AdapterEditorPage parent){
        this.parentComposite = parent.getParent();
        this.parent = parent;
        this.right_com = parent.get_right();
        this.adp_com = parent.get_adapter_composite();
        g_this = this;
    }

    public void initial_left_com(){
        Composite composite_left = new Composite(parentComposite, SWT.BORDER);
        FormData leftcomsite_form = new FormData();
        leftcomsite_form.left = new FormAttachment(0,5);
        leftcomsite_form.right = new FormAttachment(40);
        leftcomsite_form.top = new FormAttachment(0,23);
        leftcomsite_form.bottom = new FormAttachment(100);
        composite_left.setLayoutData(leftcomsite_form);
        composite_left.setLayout(new FormLayout());


        viewer = new TreeViewer(composite_left ,SWT.BORDER);
        tree = (Tree) viewer.getControl();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);

/*		TreeColumn adpter_icon = new TreeColumn(tree, SWT.NONE);
        adpter_icon.setWidth(30);
        adpter_icon.setText("Status");*/

        TreeColumn adpter_col = new TreeColumn(tree, SWT.NONE);
        adpter_col.setWidth(110);
        adpter_col.setText("adapter");

        TreeColumn name = new TreeColumn(tree, SWT.NONE);
        name.setWidth(150);
        name.setText("name");

        FormData tree_form = new FormData();
        tree_form.left = new FormAttachment(0,5);
        tree_form.right = new FormAttachment(65);
        tree_form.top = new FormAttachment(0,10);
        tree_form.bottom = new FormAttachment(100, -5);
        tree.setLayoutData(tree_form);
        viewer.setContentProvider(new AdapterTreeContentProvider());
        viewer.setLabelProvider(new AdapterLabelProvider());


        /*		viewer = new TableViewer(composite_left ,SWT.BORDER);
        table = viewer.getTable();

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn adpter_col = new TableColumn(table, SWT.NONE);
        adpter_col.setWidth(110);
        adpter_col.setText("adapter");

        TableColumn name = new TableColumn(table, SWT.NONE);
        name.setWidth(150);
        name.setText("procedure");

        FormData table_form = new FormData();
        table_form.left = new FormAttachment(0,5);
        table_form.right = new FormAttachment(65);
        table_form.top = new FormAttachment(0,10);
        table_form.bottom = new FormAttachment(100, -5);
        table.setLayoutData(table_form);
        //setTablelistener();
        // set viewer
        viewer.setContentProvider(new AdapterContentProvider());
        viewer.setLabelProvider(new AdapterLabelProvider());
         */


        viewer.addSelectionChangedListener(new ISelectionChangedListener(){
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                ErlLogger.debug("size:"+selection.size());
                //EwpProcedure[] tmps = new EwpProcedure[selection.size()];
                Object[] tmpObj = new Object[selection.size()];
                if (tmpObj.length > 0){
                    removeBut.setEnabled(true);
                    Iterator iter = selection.iterator();
                    int i=0;
                    while(iter.hasNext()){
                        tmpObj[i] = iter.next();
                        i++;
                    }
                    setGlobalSelection(tmpObj[0]);
                    ErlLogger.debug((tmpObj[0] instanceof EwpProcedure)+"|"+(tmpObj[0] instanceof EwpAdapter));
                    if (tmpObj[0] instanceof EwpProcedure){
                        // add adapter edit page
                        setTableSelected();
                        adp_com.setAdapterEditorUnVisiable();
                        right_com.setProcedureValue((EwpProcedure)tmpObj[0]);
                        ErlLogger.debug("procedure:se"+((EwpProcedure) tmpObj[0]).getId());
                    } else {
                        right_com.setProcedureEditorUnVisiable();
                        adp_com.setAdapterValue((EwpAdapter)tmpObj[0]);
                        ErlLogger.debug("adapter item!");
                    }
                }
            }
        });

        tree.addMouseListener(new MouseAdapter(){
        });



        addBut= new Button(composite_left, SWT.CENTER);
        addBut.setText(" New...");
        addBut.setLayoutData(setButtonLayout(0));

        removeBut = new Button(composite_left, SWT.CENTER);
        removeBut.setText(" Remove");
        removeBut.setLayoutData(setButtonLayout(1));
        removeBut.setEnabled(false);
        setButtonListener();
    }

    public FormData setButtonLayout(int i){
        FormData tmp_form = new FormData();
        tmp_form.left = new FormAttachment(65, 10);
        tmp_form.right = new FormAttachment(100, -10);
        tmp_form.top = new FormAttachment(0, 10+i*30);
        return tmp_form;
    }

    public void setButtonListener(){
        addBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("add button!");
                NewAdapterWizard wizard = new NewAdapterWizard(null, null);
                wizard.initial(g_this, parent.getAdpList());
            }
        });
        removeBut.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
                ErlLogger.debug("remove button!");
                //parent_backend.erlBackend_addProcedure(m_tmp_pro);
                TreeItem[] eventItem = tree.getSelection();
                ErlLogger.debug("llllll:"+eventItem.length);
                if (eventItem.length >=1){
                    if (g_tree_selection instanceof EwpProcedure ){
                        EwpProcedure tmpPro = ((EwpProcedure) g_tree_selection);
                        ErlLogger.debug("procedure:"+tmpPro.getId());
                        parent.erlBackend_removeProcedure(tmpPro);
                        parent.getAdpList().removeProcedure(tmpPro);
                    } else if(g_tree_selection instanceof EwpAdapter ){
                        EwpAdapter tmpAdp = ((EwpAdapter) g_tree_selection);
                        ErlLogger.debug("EwpAdapter:"+tmpAdp.getName());
                        parent.erlBackend_removeAdapter(tmpAdp);
                        parent.getAdpList().removeAdapter(tmpAdp);
                    }
                }
                parent.refreshTree();
            }
        });
    }

    public TreeViewer getViewer(){
        return viewer;
    }

    public void setTableSelected(){
        right_com.setLeftTableSelected();
    }

    public void setTableDeSelected(){
        right_com.setLeftTableDeSelected();
    }

    public Composite getParentComposite(){
        return  parentComposite;
    }

    private void setGlobalSelection(Object tmpObj){
        g_tree_selection = tmpObj;
    }

    public Object getGlobalSelection(){
        return g_tree_selection;
    }

    public AdapterEditorPage getParentPage(){
        return parent;
    }



}

