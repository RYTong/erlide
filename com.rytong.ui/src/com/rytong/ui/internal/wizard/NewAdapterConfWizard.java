package com.rytong.ui.internal.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.rytong.ui.RytongUIConstants;
import com.rytong.ui.internal.RytongUIMessages;
import com.rytong.ui.internal.RytongUIPlugin;

public class NewAdapterConfWizard extends Wizard implements INewWizard {

	private CreateAdapterConfPage adapterPage;

	public NewAdapterConfWizard() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(RytongUIMessages.NewAdapterConfWizard_title);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
      String name = adapterPage.getName();
      String protocol = adapterPage.getProtocol();
      System.out.println("Generating adapter: " + name + "#" + protocol);
		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		adapterPage = new CreateAdapterConfPage("main");
		adapterPage.setTitle(RytongUIMessages.CreateAdapterConfPage_title);
		adapterPage.setDescription(RytongUIMessages.CreateAdapterConfPage_desc);
		adapterPage.setImageDescriptor(RytongUIPlugin.getDefault()
				.getImageDescriptor(RytongUIConstants.IMG_NEW_ADAPTER_WIZARD));
		addPage(adapterPage);
	}

}
