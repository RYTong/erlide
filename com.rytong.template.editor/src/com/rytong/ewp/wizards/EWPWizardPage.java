package com.rytong.ewp.wizards;


import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * @since 0.16
 */
public class EWPWizardPage extends WizardPage {
    
    private Text ewpNodeText;
    private Text cookieText;
    private Text ewpEbinText;

    private final ISelection fSelection;
   // private final Template[] moduleTemplates;
   // private final ModifyListener fModifyListener;
    
    public EWPWizardPage(final ISelection selection) {
        super("wizardPage");
        setTitle("EWP Node");
        setDescription("This wizard registers a new ewp node.");
        fSelection = selection;
    }


    @Override
    public void createControl(Composite parent) {
        final Composite container = new Composite(parent, SWT.NULL);

        final GridLayout grid = new GridLayout(1, true);
        container.setLayout(grid);

        final Composite filePanel = new Composite(container, SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        filePanel.setLayoutData(gd);
        final GridLayout layout = new GridLayout();
        filePanel.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;

        Label label = new Label(filePanel, SWT.NULL);
        label.setText("&EWP node name:");

        ewpNodeText = new Text(filePanel, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, true);
        ewpNodeText.setLayoutData(gd);

        label = new Label(filePanel, SWT.NULL);

        label = new Label(filePanel, SWT.NULL);
        label.setText("&Cookie:");

        cookieText = new Text(filePanel, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, true);
        cookieText.setLayoutData(gd);
        //containerText.addModifyListener(fModifyListener);

        label = new Label(filePanel, SWT.NULL);

        label = new Label(filePanel, SWT.NULL);
        label.setText("&EWP ebin:");
        ewpEbinText = new Text(filePanel, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, true);
        ewpEbinText.setLayoutData(gd);
        final Button button = new Button(filePanel, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                handleBrowse();
            }
        });

        label = new Label(filePanel, SWT.NULL);


        initialize();
        setControl(container);
    }
    
    public String getEWPName() {
        return ewpNodeText.getText();
    }

    public String getCookie() {
        return cookieText.getText();
    }

    public String getEWPEbinPath() {
        return ewpEbinText.getText();
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */

    private void initialize() {
        if (fSelection != null && !fSelection.isEmpty()
                && fSelection instanceof IStructuredSelection) {
            
        }
        ewpNodeText.setText("ewp");
        cookieText.setText("ewpcool");
        
    }
    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */
    void handleBrowse() {
        final DirectoryDialog dialog = new DirectoryDialog(getShell());
        ewpEbinText.setText(dialog.open());
    }
    
}
