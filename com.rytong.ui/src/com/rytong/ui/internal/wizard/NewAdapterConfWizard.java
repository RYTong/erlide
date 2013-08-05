package com.rytong.ui.internal.wizard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.erlide.jinterface.ErlLogger;

import com.rytong.ui.RytongUIConstants;
import com.rytong.ui.internal.RytongUIMessages;
import com.rytong.ui.internal.RytongUIPlugin;

public class NewAdapterConfWizard extends Wizard implements INewWizard {

	private CreateAdapterConfPage adapterPage;
	private ISelection fSelection;

	public NewAdapterConfWizard() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(RytongUIMessages.NewAdapterConfWizard_title);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.fSelection = selection;
	}

	@Override
	public boolean performFinish() {
		String name = adapterPage.getName();
		String protocol = adapterPage.getProtocol();
		String skeleton = createSkeleton(name, protocol);
		String fileName = "adapter.conf";
		// System.out.println("Generating adapter: " + name + "#" + protocol);

		// create a sample adapter file
		final String containerName = getContainerName(fSelection);
		// System.out.println("containerName : " + containerName);
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IResource resource = root.findMember(new Path(containerName));
		if (resource == null || !resource.exists()
				|| !(resource instanceof IContainer)) {
			ErlLogger.error("Container \"" + containerName
					+ "\" does not exist.");
			return false;
		}
		final IContainer container = (IContainer) resource;
		IPath path = new Path(fileName);
		final IFile file = container.getFile(path);
		try {
			final InputStream stream = new ByteArrayInputStream(
					skeleton.getBytes());
			if (file.exists()) {
				file.setContents(stream, true, true, null);
			} else {
				file.create(stream, true, null);
			}
			stream.close();
		} catch (final IOException | CoreException e) {
			ErlLogger.error("failed to create adapter conf file");
			e.printStackTrace();
		}
		final IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, file, true);
		} catch (final PartInitException e) {
		}
		return true;
	}

	private String createSkeleton(String name, String protocol) {
		return "%% Adapters and Procedures Definition file.\n\n{adapter, [{name, \""
				+ name
				+ "\"},\n\t{host, \"localhost\"},\n\t{protocol, "
				+ protocol + "}]\n}. ";
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
	private String getContainerName(ISelection selection) {
		if (fSelection != null && !fSelection.isEmpty()
				&& fSelection instanceof IStructuredSelection) {
			final IStructuredSelection ssel = (IStructuredSelection) fSelection;
			if (ssel.size() > 1) {
				return null;
			}
			final Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				final IResource resource = (IResource) obj;
				IContainer container;
				if (resource instanceof IContainer) {
					container = (IContainer) resource;
				} else {
					container = resource.getParent();
				}
				String containerName = container.getFullPath().toString()
						+ "/config";
				return containerName;
			}
		}
		return null;
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
