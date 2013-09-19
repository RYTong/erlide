package com.rytong.ui.editor.conf;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.*;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.*;

import com.rytong.ui.editor.conf.model.Annotation;
import com.rytong.ui.editor.conf.model.Element;

public class ConfDetailsPage implements IDetailsPage {
	private IManagedForm mform;
	private TypeOne input;
    private FormPage page;
	private Button [] choices;
	private Button flag;
	private Text text0;
    public String str;
	private static final String RTEXT_DATA =
			"<form><p>An example of a free-form text that should be "+ //$NON-NLS-1$
			"wrapped below the section with widgets.</p>"+ //$NON-NLS-1$
			"<p>It can contain simple tags like <a>links</a> and <b>bold text</b>.</p></form>"; //$NON-NLS-1$
	
	public ConfDetailsPage(FormPage page, TypeOne input) {
    	this.page = page;
    	this.input = input;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm mform) {
		this.mform = mform;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);
        
        Element adapter = input.getElement();
        Annotation anot = adapter.getAnot();

		FormToolkit toolkit = mform.getToolkit();
		Section s1 = toolkit.createSection(parent, Section.DESCRIPTION|Section.TITLE_BAR);
		s1.marginWidth = 10;
		s1.setText(anot.getName()); //$NON-NLS-1$
		s1.setDescription(anot.getDocstring()); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		s1.setLayoutData(td);
        
		Composite client = toolkit.createComposite(s1);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);
        
        for(final Element child:adapter.getChildren()){
        	Annotation a = child.getAnot();
            if(a.getValue()==Annotation.Type.STRING) {
        		createSpacer(toolkit, client, 2);
        		toolkit.createLabel(client, a.getName()+":"); //$NON-NLS-1$
        		final Text text = toolkit.createText(client, child.getDirtyValue(), SWT.BORDER_SOLID); //$NON-NLS-1$
        		text.addModifyListener(new ModifyListener() {
        			public void modifyText(ModifyEvent e) {
        				if (input!=null){
                        	markDirty();
                        	child.setDirtyValue(text.getText());
        				}
        			}
        		});
        		GridData gd = new GridData(GridData.FILL_HORIZONTAL|GridData.VERTICAL_ALIGN_BEGINNING);
        		gd.widthHint = 10;
        		text.setLayoutData(gd);
                    	
                    }
        }
		
//		SelectionListener choiceListener = new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				Integer value = (Integer)e.widget.getData();
//				if (input!=null) {
//					input.setChoice(value.intValue());
//				}
//			}
//		};
//		GridData gd;
//		choices = new Button[TypeOne.CHOICES.length];
//		for (int i=0; i<TypeOne.CHOICES.length; i++) {
//			choices[i] = toolkit.createButton(client, TypeOne.CHOICES[i], SWT.RADIO);
//			choices[i].setData(new Integer(i));
//			choices[i].addSelectionListener(choiceListener);
//			gd = new GridData();
//			gd.horizontalSpan = 2;
//			choices[i].setLayoutData(gd);
//		}
//		createSpacer(toolkit, client, 2);
//		flag = toolkit.createButton(client, Messages.getString("TypeOneDetailsPage.check"), SWT.CHECK); //$NON-NLS-1$
//		flag.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				if (input!=null)
//					input.setFlag(flag.getSelection());
//			}
//		});
//		gd = new GridData();
//		gd.horizontalSpan = 2;
//		flag.setLayoutData(gd);
//		createSpacer(toolkit, client, 2);
//		
//		toolkit.createLabel(client, Messages.getString("TypeOneDetailsPage.label")); //$NON-NLS-1$
//		text = toolkit.createText(client, "", SWT.SINGLE); //$NON-NLS-1$
//		text.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				if (input!=null)
//					input.setText(text.getText());
//			}
//		});
//		gd = new GridData(GridData.FILL_HORIZONTAL|GridData.VERTICAL_ALIGN_BEGINNING);
//		gd.widthHint = 10;
//		text.setLayoutData(gd);
//		
//		createSpacer(toolkit, client, 2);
//		
//		FormText rtext = toolkit.createFormText(parent, true);
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		String dt = df.format(new Date());// new Date()为获取当前系统时间
//		//rtext.setText(RTEXT_DATA, true, false);
//		rtext.setText("<form><p>"+dt+"</p></form>", true, false);
//		td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
//		td.grabHorizontal = true;
//		rtext.setLayoutData(td);
		
		toolkit.paintBordersFor(s1);
		s1.setClient(client);
	}
	private void createSpacer(FormToolkit toolkit, Composite parent, int span) {
		Label spacer = toolkit.createLabel(parent, ""); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		spacer.setLayoutData(gd);
	}
	private void update() {
//		for (int i=0; i<TypeOne.CHOICES.length; i++) {
//			choices[i].setSelection(input!=null && input.getChoice()==i);
//		}
//		flag.setSelection(input!=null && input.getFlag());
//		text.setText(input!=null && input.getText()!=null?input.getText():""); //$NON-NLS-1$
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#inputChanged(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection ssel = (IStructuredSelection)selection;
		if (ssel.size()==1) {
			input = (TypeOne)ssel.getFirstElement();
		}
		else
			input = null;
		update();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#commit()
	 */
	public void commit(boolean onSave) {
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#setFocus()
	 */
	public void setFocus() {
		choices[0].setFocus();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#dispose()
	 */
	public void dispose() {
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}
	public boolean isStale() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IDetailsPage#refresh()
	 */
	public void refresh() {
		update();
	}
	public boolean setFormInput(Object input) {
		return false;
	}
    
    public void markDirty() {
    	((ConfEditor)page.getEditor()).markDirty();
    }
}
