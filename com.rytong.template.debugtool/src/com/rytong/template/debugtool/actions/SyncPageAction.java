package com.rytong.template.debugtool.actions;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
//import org.eclipse.ui.texteditor.ITextEditor;
import org.erlide.jinterface.ErlLogger;

import com.rytong.template.debugtool.Activator;
import com.rytong.template.debugtool.util.SyncSocket;


public class SyncPageAction implements IEditorActionDelegate {
    private Activator  parent;
    private IWorkbench workbench;
    public SyncSocket serverSocket = null;

    public SyncPageAction(){
        ErlLogger.debug("SyncPageAction con!");
        parent = Activator.getDefault();
        serverSocket = parent.getSyncServer();
        workbench = PlatformUI.getWorkbench();
        parent.setSyncPageAct(this);
        //workbench.getActiveWorkbenchWindow();
        IActionBars tmp = parent.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getActionBars();
        ErlLogger.debug("tmp  len:"+tmp.getToolBarManager().getItems().length);

    }

    @Override
    public void run(IAction action) {
        // TODO Auto-generated method stub
        ErlLogger.debug("SyncPageAction run!");
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();

        if (!serverSocket.getServerStatus()){
            showErrorMsg(window, "请启动同步服务~");
            return;
        }

        IEditorPart part = page.getActiveEditor();
        ErlLogger.debug("ss:"+part.getEditorSite().getPluginId());
        if (part == null ){
            showErrorMsg(window, "没有可同步的页面，请打开需要同步的页面");
            return;
        }

        IEditorInput input = part.getEditorInput();
        int send_result = 0;

        if (input instanceof TmpStringEditorInput){

            String tmp_str = ((TmpStringEditorInput) input).getContent();
            send_result = serverSocket.sendServerMsg(tmp_str.toString());
        } else {

            IDocument document = ((ITextEditor) part).getDocumentProvider()
                    .getDocument(part.getEditorInput());
            //
            IFile file = ((IFileEditorInput) input).getFile();
            String tmp_ext = file.getFileExtension();
            ErlLogger.debug("file extension:"+file.getFileExtension());

            String low_ext = tmp_ext.toLowerCase();

            if (low_ext.equalsIgnoreCase("xhtml") || low_ext.equalsIgnoreCase("cs")){
                send_result = serverSocket.sendServerMsg(document.get());
            } else {
                boolean tmp_msg = MessageDialog.openConfirm(window.getShell(), "Sync Content Notic", "您要同步的报文可能不是标准的EMP页面，是否要继续同步？");
                ErlLogger.debug("tmp_msg:"+tmp_msg);
                if (tmp_msg){
                    send_result = serverSocket.sendServerMsg(document.get());
                }
            }

        }

        if (send_result == 2){
            MessageDialog.openInformation(window.getShell(), "Notic", "没有与调试服务连接的设备，请启动客户端并与当前服务连接~");
        } else if (send_result == 1){
            MessageDialog.openInformation(window.getShell(), "Notic", "发送成功~");
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // TODO Auto-generated method stub
        ErlLogger.debug("SyncPageAction sel!");

    }

    @Override
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        // TODO Auto-generated method stub
        ErlLogger.debug("SyncPageAction set!");

    }

    public void showErrorMsg(IWorkbenchWindow window, String msg){
        MessageDialog.openError(window.getShell(),
                "Synchronal server!",
                msg);
    }

}
