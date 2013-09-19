package com.rytong.ui.editor.conf;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.forms.*;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.*;

import com.rytong.ui.RytongUIConstants;
import com.rytong.ui.editor.conf.model.Element;
import com.rytong.ui.internal.RytongUIPlugin;

/**
 *
 */
public class ScrolledPropertiesBlock extends MasterDetailsBlock {
	private FormPage page;

	public ScrolledPropertiesBlock(FormPage page) {
		this.page = page;
	}

	/**
	 * @param id
	 * @param title
	 */
	class MasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof SimpleFormEditorInput) {
				// SimpleFormEditorInput input = (SimpleFormEditorInput) page
				// .getEditor().getEditorInput();
				SimpleFormEditorInput input = new SimpleFormEditorInput("name");
				return input.getModel().getContents();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return obj.toString();
		}

		public Image getColumnImage(Object obj, int index) {
			if (obj instanceof TypeOne) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_ELEMENT);
			}
			if (obj instanceof TypeTwo) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_FILE);
			}
			return null;
		}
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {
		// final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		section.setText(Messages.getString("ScrolledPropertiesBlock.sname")); //$NON-NLS-1$
		section.setDescription(Messages
				.getString("ScrolledPropertiesBlock.sdesc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		Table t = toolkit.createTable(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(client);
		Button b = toolkit.createButton(client,
				Messages.getString("ScrolledPropertiesBlock.add"), SWT.PUSH); //$NON-NLS-1$
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		b.setLayoutData(gd);
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		TableViewer viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setInput(new SimpleFormEditorInput("test"));
		// viewer.setInput(page.getEditor().getEditorInput());
	}

	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		Action haction = new Action("hor", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(true);
		haction.setToolTipText(Messages
				.getString("ScrolledPropertiesBlock.horizontal")); //$NON-NLS-1$
		haction.setImageDescriptor(RytongUIPlugin.getDefault()
				.getImageRegistry()
				.getDescriptor(RytongUIConstants.IMG_HORIZONTAL));
		Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText(Messages
				.getString("ScrolledPropertiesBlock.vertical")); //$NON-NLS-1$
		vaction.setImageDescriptor(RytongUIPlugin.getDefault()
				.getImageRegistry()
				.getDescriptor(RytongUIConstants.IMG_VERTICAL));
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.setPageLimit(0);
		detailsPart.setPageProvider(new PageProvider());
		// detailsPart.registerPage(TypeOne.class, new TypeOneDetailsPage());
		// detailsPart.registerPage(TypeTwo.class, new TypeTwoDetailsPage());
	}

	class PageProvider implements IDetailsPageProvider {
    	private TypeOne element;

		@Override
		public Object getPageKey(Object object) {
			if (object instanceof TypeOne) {
            	this.element = (TypeOne)object;
				RytongUIConstants.s = !RytongUIConstants.s;
				if (RytongUIConstants.s)
					return "conf";
				return "conf2";
			}
			return "conf";
		}

		@Override
		public IDetailsPage getPage(Object key) {
			String k = (String) key;
			if ("conf".equals(k) || "conf2".equals(k)) {
				return new ConfDetailsPage(page, element);
			}
			return new ConfDetailsPage(page, element);
		}
	}

}