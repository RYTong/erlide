package com.rytong.ui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.erlide.core.model.util.PluginUtils;
import org.erlide.jinterface.ErlLogger;

import com.rytong.ui.internal.RytongUIPlugin;
import com.rytong.ui.RytongUIConstants;

public class NewAppProjectWizard extends Wizard implements INewWizard {
	
	private WizardNewProjectCreationPage namePage;
	private ProjectPreferencesWizardPage buildPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		System.out.println("newappWizard init...");
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		System.out.println("newappWizard finish...");
      final IWorkspaceRoot root = ResourcesPlugin.getWorkspace() .getRoot();
        IProject project = root.getProject(namePage.getProjectName()); 
        IProjectDescription description = ResourcesPlugin.getWorkspace()
                .newProjectDescription(project.getName());
        if(!Platform.getLocation().equals(namePage.getLocationPath())) {
        	description.setLocation(namePage.getLocationPath());
        }
        try {
			project.create(description, null);
        project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
        String str = "public/channel/abc";
        String sep = ":";
        String[] v = str.split(sep);
        List<String> sresult = new ArrayList<String>(Arrays.asList(v));
        List<IPath> result = new ArrayList<IPath>();
        for (final String s : sresult) {
        	result.add(new Path(s));
        }
        buildPaths(root, project, result);
		return true;
	}

	private void buildPaths(IWorkspaceRoot root, IProject project,
			List<IPath> result) {
        if(result != null) {
            final IPath projectPath = project.getFullPath();
            System.out.println("project full path: " + projectPath.toString());
            for (final IPath pp : result) {
            	if (!pp.isAbsolute() && !pp.toString().equals(".") && !pp.isEmpty()) {
            		final IPath path = projectPath.append(pp);
                  final IFolder folder = root.getFolder(path);
              		createFolderHelper(folder);
            	}
            }
        }
		
	}

	private void createFolderHelper(IFolder folder) {
        if (!folder.exists()) {
            final IContainer parent = folder.getParent();
            if (parent instanceof IFolder && !((IFolder) parent).exists()) {
                createFolderHelper((IFolder) parent);
            }
            try {
				folder.create(false, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
        }
		
	}

	@Override
	public void addPages() {
        try {
            super.addPages();
            namePage = new WizardNewProjectCreationPage(
                    "NewAppProjectWizard");
            namePage.setTitle(RytongUIPlugin
                    .getResourceString("wizards.titles.newproject"));
            namePage.setDescription(RytongUIPlugin
                    .getResourceString("wizards.descs.newproject"));
            namePage.setImageDescriptor(RytongUIPlugin.getDefault()
                    .getImageDescriptor(
                            RytongUIConstants.IMG_NEW_PROJECT_WIZARD));
            addPage(namePage);

            buildPage = new ProjectPreferencesWizardPage(
                    "ProjectPreferencesWizardPage");
            buildPage.setTitle(RytongUIPlugin
                    .getResourceString("wizards.titles.buildprefs"));
            buildPage.setDescription(RytongUIPlugin
                    .getResourceString("wizards.descs.buildprefs"));
            buildPage.setImageDescriptor(RytongUIPlugin.getDefault()
                    .getImageDescriptor(
                            RytongUIConstants.IMG_NEW_PROJECT_WIZARD));
            addPage(buildPage);
        } catch (final Exception x) {
            reportError(x);
        }
    }

    private void reportError(final Exception x) {
        ErlLogger.error(x);
        ErrorDialog.openError(getShell(), RytongUIPlugin
                .getResourceString("wizards.errors.projecterrordesc"),
                RytongUIPlugin
                        .getResourceString("wizards.errors.projecterrortitle"),
                PluginUtils.makeStatus(x));
    }
    protected void createProject(final IProgressMonitor monitor) {
    }
}
