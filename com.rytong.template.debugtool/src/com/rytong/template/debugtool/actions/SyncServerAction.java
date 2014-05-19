package com.rytong.template.debugtool.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
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
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.erlide.jinterface.ErlLogger;

import com.rytong.template.debugtool.Activator;
import com.rytong.template.debugtool.util.SyncSocket;

public class SyncServerAction implements IEditorActionDelegate{

    private Activator  parent;
    private IWorkbench workbench;

    public SyncSocket serverSocket = null;
    public String port_str = "8080";

    public SyncServerAction(){
        ErlLogger.debug("SyncSocketAction cons!");
        parent = Activator.getDefault();
        serverSocket = parent.getSyncServer();
        workbench = PlatformUI.getWorkbench();
        parent.setSyncServerAct(this);
        //IViewPart[] tmp = parent.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews();
        //ErlLogger.debug("SyncServerAction  len:"+tmp.length);

//        IActionBars tmp1 = parent.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getActionBars();
//        IContributionItem[] tmp = tmp1.getToolBarManager().getItems();
//        ErlLogger.debug("tmp  len:"+tmp1.getToolBarManager().getItems().length);
//        IContributionItem tmp2 = tmp1.getToolBarManager().find("com.rytong.template.debugtool.SyncPageAction");
//        ErlLogger.debug(" id:"+tmp2.getId()+ "   :"+tmp2.isEnabled());
//        ErlLogger.debug(" id:"+tmp2.getId()+ "   :"+tmp2.isVisible());
//        tmp2.setVisible(false);

    }

    public void init(IViewSite view, IMemento memento){
        ErlLogger.debug("SyncSocketAction init!:"+view.getId());
    }

    @Override
    public void run(IAction action) {
        // TODO Auto-generated method stub
        ErlLogger.debug("SyncSocketAction run!");
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
//        IWorkbenchPage page = window.getActivePage();

        //取得当前处于活动状态的编辑器窗口
//        IEditorPart part = page.getActiveEditor();
//         IActionBars tmp = part.getEditorSite().getActionBars();
//
//         IContributionItem tmp1 = tmp.getToolBarManager().find("com.rytong.template.debugtool.SyncPageAction");
//         tmp.getToolBarManager().

        ErlLogger.debug("port:" + serverSocket.port);
        AddDiaolog newDialog = new AddDiaolog(window.getShell());
        newDialog.open();
        ErlLogger.debug("dialog result :"+newDialog.getReturnCode());
        if (newDialog.getReturnCode()==Window.OK){
            // start over
            if (!serverSocket.getServerStatus()){
                port_str = newDialog.dialogKeyStr;
                ErlLogger.debug("port to :"+Integer.valueOf(port_str));
                serverSocket.CreateSocket(Integer.valueOf(port_str));
                //tmp_page.setEnable();
                //window.getWorkbench().getWorkbenchWindows().get
                //IActionSetDescriptor.setInitiallyVisible();
                SyncPageAction tmp_act = parent.getSyncPageAct();

                //tmp_act.
            } else {
                serverSocket.CloseSocket();
                //serverSocket.sendServerMsg(document.get());
            }

        }

}

@Override
public void selectionChanged(IAction action, ISelection selection) {
    // TODO Auto-generated method stub
    ErlLogger.debug("SyncSocketAction changed!");

}

@Override
public void setActiveEditor(IAction action, IEditorPart targetEditor) {
    // TODO Auto-generated method stub
    ErlLogger.debug("SyncSocketAction editor!");

}

public void showErrorMsg(IWorkbenchWindow window, String msg){
    MessageDialog.openError(window.getShell(),
            "Synchronal server!",
            msg);
}


//
class AddDiaolog extends Dialog {
    private String dialogTitle="";
    private String dialogText="";
    private Button okBut;


    private Label keyLabel;
    private Text dialogTexKey;
    public String dialogKeyStr;

    public AddDiaolog(Shell parent)
    {
        super(parent);
        dialogTitle="Start socket server...";
        dialogText = " Start a socket server.";
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
        msg.setLayoutData(setTitleLayout());

        if (!serverSocket.getServerStatus()){
            keyLabel = new Label(composite, SWT.NULL);
            keyLabel.setText("Port:");
            keyLabel.setLayoutData(setLabelLayout(1));
            dialogTexKey = new Text(composite, SWT.BORDER);
            dialogTexKey.setLayoutData(setTextLayout(1));
            dialogTexKey.setText(port_str);
            dialogKeyStr = port_str;

            Label notic = new Label(composite, SWT.WRAP);
            notic.setText("注意：本处输入需要输入Socket的端口号~");
            FormData notic_form = new FormData();
            notic_form.left = new FormAttachment(0,5);
            notic_form.right = new FormAttachment(100, -5);
            notic_form.top = new FormAttachment(0,5+2*29);
            notic.setLayoutData(notic_form);
            setTextListener();
        } else {
            Label notic = new Label(composite, SWT.WRAP);
            notic.setText("Socket server 已经启动~");
            FormData notic_form = new FormData();
            notic_form.left = new FormAttachment(0,5);
            notic_form.right = new FormAttachment(100, -5);
            notic_form.top = new FormAttachment(0,34);
            notic.setLayoutData(notic_form);
        }
        return composite;
    }

    private FormData setTitleLayout(){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(0, 200);
        comsite_form.top = new FormAttachment(0,5);
        return comsite_form;
    }

    private FormData setLabelLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,5);
        comsite_form.right = new FormAttachment(0, 35);
        comsite_form.top = new FormAttachment(0,5+i*29);
        return comsite_form;
    }

    private FormData setTextLayout(int i){
        FormData comsite_form = new FormData();
        comsite_form.left = new FormAttachment(0,45);
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
        if (serverSocket.getServerStatus())
            okBut.setText("close");
        return new Point(400,200);
    }

    private void setTextListener(){
        dialogTexKey.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e) {
                // TODO Auto-generated method stub
                dialogKeyStr = dialogTexKey.getText();
                //ErlLogger.debug("dialogKeyStr:"+dialogKeyStr);
                setOkButton();
            }
        });
    }

    private void setOkButton(){
        if (dialogKeyStr.replace(" ", "").isEmpty())
            okBut.setEnabled(false);
        else
            okBut.setEnabled(true);
    }
}


}
