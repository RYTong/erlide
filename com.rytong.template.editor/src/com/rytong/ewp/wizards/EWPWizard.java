package com.rytong.ewp.wizards;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.backend.runtimeinfo.RuntimeInfo;
import org.erlide.jinterface.ErlLogger;

/**
 * @since 0.16
 */
public class EWPWizard extends Wizard implements INewWizard {

    private EWPWizardPage fPage;
    
    private ISelection fSelection;
    
    public EWPWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New EWP Node");
        }

    /**
     * Adding the page to the wizard.
     */

    @Override
    public void addPages() {
        fPage = new EWPWizardPage(fSelection);
        addPage(fPage);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        fSelection = selection; 
    }

    @Override
    public boolean performFinish() {
        final String ewpName = fPage.getEWPName();
        final String cookie = fPage.getCookie();
        final String ewpEbin = fPage.getEWPEbinPath();
        final IBackend ideBackend = BackendCore.getBackendManager()
                .getIdeBackend();
        ErlLogger.debug("Starting regsiter ewp node %s with cookie %s and ebin is under %s", ewpName, cookie, ewpEbin);
        final RuntimeInfo info = ideBackend.getRuntimeInfo();
        info.setNodeName(ewpName);
        info.setCookie(cookie);
        if (null==BackendCore.getBackendManager().registerEWPBackend(info)){
            MessageDialog.openError(getShell(), "Error",
                    "can't find ewp node with name: " + ewpName + " and cookie: " + cookie);
            return false;
        }
        return true;
    }

}
