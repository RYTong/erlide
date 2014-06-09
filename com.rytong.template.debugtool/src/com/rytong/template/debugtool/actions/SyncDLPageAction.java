package com.rytong.template.debugtool.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import org.erlide.jinterface.ErlLogger;

import com.rytong.template.debugtool.Activator;
import com.rytong.template.debugtool.util.SyncSocket;

public class SyncDLPageAction implements IEditorActionDelegate {
    private Activator  parent;
    private IWorkbench workbench;
    private String projectPath;
    private IWorkbenchWindow window;
    private IProject tmpProject;
    public SyncSocket serverSocket = null;

    public SyncDLPageAction(){
        ErlLogger.debug("SyncPageAction con!");
        parent = Activator.getDefault();
        serverSocket = parent.getSyncServer();
        workbench = PlatformUI.getWorkbench();
        //parent.setSyncPageAct(this);
        //workbench.getActiveWorkbenchWindow();
        IActionBars tmp = parent.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getActionBars();
        ErlLogger.debug("tmp  len:"+tmp.getToolBarManager().getItems().length);

        this.window = workbench.getActiveWorkbenchWindow();



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
        String send_result = "";

        ErlLogger.debug("send_result:"+send_result);

        send_result = serverSocket.getClientMsg();

        if (send_result == null){
            MessageDialog.openInformation(window.getShell(), "Notic", "没有与调试服务连接的设备，请启动客户端并与当前服务连接~");
        } else if (send_result.equalsIgnoreCase("")){
            MessageDialog.openInformation(window.getShell(), "Notic", "没有可接受的报文~");
        } else {

            boolean con_res = MessageDialog.openConfirm(window.getShell(), "Notic", "是否要创建新的编辑窗口~？");

            if (con_res){
                try {
                    //page.openEditor(new TmpStringEditorInput(send_result), "com.rytong.editors.TemplateEditor", true);
                    IEditorPart editor  =  PlatformUI.getWorkbench()
                            .getActiveWorkbenchWindow().getActivePage().getActiveEditor();

                    IFile tmp_file =  check_tmp_folder();
                    ErlLogger.debug("naem:"+editor.getEditorInput().getName());
                    ErlLogger.debug("tmp_file:"+tmp_file.exists());
                    try {
                        tmp_file.refreshLocal(IResource.DEPTH_ZERO,null);
                        if (!tmp_file.exists()){
                            tmp_file.create(new ByteArrayInputStream(send_result.getBytes()), false, null);

                        } else {
                            tmp_file.setContents(new ByteArrayInputStream(send_result.getBytes()), false, true, null);
                        }
                        ErlLogger.debug("tmp_file:"+tmp_file.exists());

                    } catch (CoreException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    IDE.openEditor(page, tmp_file, "com.rytong.editors.TemplateEditor", true);
                } catch (PartInitException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private IFile check_tmp_folder(){
        this.tmpProject = ((FileEditorInput) window.getActivePage().getActiveEditor().getEditorInput()).getFile().getProject();
        projectPath = tmpProject.getLocation().toString();
        try {
            String tmp_str = ((FileEditorInput) window.getActivePage().getActiveEditor().getEditorInput()).getFile().getPersistentProperty(IDE.EDITOR_KEY);
            ErlLogger.debug("tmp_str:"+tmp_str);
        } catch (CoreException e1) {
            // TODO Auto-generated catch blockz
            e1.printStackTrace();
        }
        ErlLogger.debug("projectPath:"+projectPath);
        IFolder tmp_foldor = tmpProject.getFolder("/tmp");
        if (!tmp_foldor.exists()){
            try {
                tmp_foldor.create(true, true, null);
            } catch (CoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        File newFile = new File(projectPath+"/tmp/eclipse_tmp.xhtml");

        if (!newFile.exists()){
            // InputStream inputStreamJava = new ByteArrayInputStream("this is a tmp file~".getBytes());
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ErlLogger.debug("new file failed~");
            }

        }
        IFile tmp_file = tmpProject.getFile(new Path("/tmp/eclipse_tmp.xhtml"));
        return tmp_file;
    }


    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        // TODO Auto-generated method stub

    }


    public void showErrorMsg(IWorkbenchWindow window, String msg){
        MessageDialog.openError(window.getShell(),
                "Synchronal server!",
                msg);
    }
}
