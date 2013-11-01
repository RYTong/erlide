package com.rytong.ui.internal.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.rytong.ui.RytongUIConstants;
import com.rytong.ui.internal.RytongUIMessages;
import com.rytong.ui.internal.RytongUIPlugin;

public class EwpPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage, SelectionListener, ModifyListener {

    private Text ewp_path;


    /**
     * Creates composite control and sets the default layout data.
     *
     * @param parent  the parent of the new composite
     * @param numColumns  the number of columns for the new composite
     * @return the newly-created coposite
     */
    private Composite createComposite(Composite parent, int numColumns) {
        Composite composite = new Composite(parent, SWT.NULL);

        //GridLayout
        GridLayout layout = new GridLayout();
        layout.numColumns = numColumns;
        composite.setLayout(layout);

        //GridData
        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        composite.setLayoutData(data);
        return composite;
    }

    /** (non-Javadoc)
     * Method declared on PreferencePage
     */
    protected Control createContents(Composite parent) {
    	PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "com.rytong.ui.preference_page_context");

        //composite_textField << parent
        Composite composite_textField = createComposite(parent, 2);
        createLabel(composite_textField, RytongUIMessages.PreferencePage_Name); //$NON-NLS-1$
        ewp_path = createTextField(composite_textField);
        createPushButton(composite_textField, RytongUIMessages.PreferencePage_Change); //$NON-NLS-1$


        initializeValues();

        //font = null;
        return new Composite(parent, SWT.NULL);
    }

    /**
     * Utility method that creates a label instance
     * and sets the default layout data.
     *
     * @param parent  the parent for the new label
     * @param text  the text for the new label
     * @return the new label
     */
    private Label createLabel(Composite parent, String text) {
        Label label = new Label(parent, SWT.LEFT);
        label.setText(text);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = GridData.FILL;
        label.setLayoutData(data);
        return label;
    }

    /**
     * Utility method that creates a push button instance
     * and sets the default layout data.
     *
     * @param parent  the parent for the new button
     * @param label  the label for the new button
     * @return the newly-created button
     */
    private Button createPushButton(Composite parent, String label) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(label);
        button.addSelectionListener(this);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        button.setLayoutData(data);
        return button;
    }


    /**
     * Create a text field specific for this application
     *
     * @param parent  the parent of the new text field
     * @return the new text field
     */
    private Text createTextField(Composite parent) {
        Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
        text.addModifyListener(this);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.verticalAlignment = GridData.CENTER;
        data.grabExcessVerticalSpace = false;
        text.setLayoutData(data);
        return text;
    }

    /** 
     * The <code>EwpPreferencePage</code> implementation of this
     * <code>PreferencePage</code> method 
     * returns preference store that belongs to the our plugin.
     * This is important because we want to store
     * our preferences separately from the workbench.
     */
    protected IPreferenceStore doGetPreferenceStore() {
        //return ReadmePlugin.getDefault().getPreferenceStore();
    	return RytongUIPlugin.getRytongPreferenceStore();
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchPreferencePage
     */
    public void init(IWorkbench workbench) {
        // do nothing
    }

    /**
     * Initializes states of the controls using default values
     * in the preference store.
     */
    private void initializeDefaults() {
        IPreferenceStore store = getPreferenceStore();
        ewp_path.setText(store.getDefaultString(PreferenceConstants.EWP_PATH));
    }

    /**
     * Initializes states of the controls from the preference store.
     */
    private void initializeValues() {
        IPreferenceStore store = getPreferenceStore();
        ewp_path.setText(store.getString(PreferenceConstants.EWP_PATH));
    }

    /** (non-Javadoc)
     * Method declared on ModifyListener
     */
    public void modifyText(ModifyEvent event) {
        //Do nothing on a modification in this example
    }

    /* (non-Javadoc)
     * Method declared on PreferencePage
     */
    protected void performDefaults() {
        super.performDefaults();
        initializeDefaults();
    }

    /* (non-Javadoc)
     * Method declared on PreferencePage
     */
    public boolean performOk() {
        storeValues();
        RytongUIPlugin.getDefault().savePluginPreferences();
        //InstanceScope.INSTANCE.getNode(RytongUIPlugin.PLUGIN_ID);
        return true;
    }

    /**
     * Stores the values of the controls back to the preference store.
     */
    private void storeValues() {
        IPreferenceStore store = getPreferenceStore();
        store.setValue(PreferenceConstants.EWP_PATH, ewp_path.getText());
    }

    /** (non-Javadoc)
     * Method declared on SelectionListener
     */
    public void widgetDefaultSelected(SelectionEvent event) {
        //Handle a default selection. Do nothing in this example
    }

    /** (non-Javadoc)
     * Method declared on SelectionListener
     */
    public void widgetSelected(SelectionEvent event) {
    	DirectoryDialog dlg = new DirectoryDialog(getShell());
    	String startdir = System.getProperty("user.home");
    	if (startdir == null) startdir = "/";
    	dlg.setFilterPath(startdir);
    	String ewp_path = dlg.open();
    	if (ewp_path != null) {
    		this.ewp_path.setText(ewp_path);
    	}
    	check_it_out();
    }

	private void check_it_out() {
	}
}
