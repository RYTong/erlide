package com.rytong.conf.newcollection.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.erlide.jinterface.ErlLogger;

import com.rytong.conf.editor.pages.CollectionsPage;
import com.rytong.conf.editor.pages.EwpChannels;
import com.rytong.conf.editor.pages.EwpCollections;


//@FIXME difference the channel and collection
public class NewCollWizardItemsPage extends WizardPage{
	private static String PAGE_NAME="Selections";
	private static String PAGE_DESC="Select collections and channels as new collection items.";
	private CollectionsPage parent;
	protected EwpCollections collObj = null;
	protected HashMap<String, Integer> tableMapStore;

	private Table tablel;
	private Table tabler;

	private Button addBut ;
	private Button addAllBut ;
	private Button removeBut ;
	private Button removeAllBut ;
	private Button swapBut ;


	protected NewCollWizardItemsPage(CollectionsPage parent, EwpCollections coll) {

		super(PAGE_NAME);
		setDescription(PAGE_DESC);
		collObj=coll;
		this.parent=parent;
		if(collObj==null)
			ErlLogger.debug("collection null!");
	}

@Override
public void createControl(Composite parent) {

	Composite composite = new Composite(parent, SWT.NONE);
	FormLayout layout = new FormLayout();
	composite.setLayout(layout);

	FormData label_form = new FormData();
	label_form.left = new FormAttachment(0,5);
	label_form.top = new FormAttachment(0,2);

	Label label = new Label(composite, SWT.NONE);
	label.setText("&Collection Items Lists:");
	label.setLayoutData(label_form);


	tablel = new Table(composite, SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION|SWT.LEFT);
	TableColumn tableCol = new TableColumn(tablel, SWT.LEFT);
	int width = composite.getDisplay().getClientArea().width;
	tableCol.setWidth(220);

	FormData lvl_form = new FormData();
	lvl_form.left = new FormAttachment(0,5);
	lvl_form.right = new FormAttachment(50,-78);
	lvl_form.top = new FormAttachment(0,20);
	lvl_form.bottom = new FormAttachment(100,-2);
	tablel.setLayoutData(lvl_form);
	initial_items(tablel);


	tabler = new Table(composite, SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION);

	FormData lvr_form = new FormData();
	lvr_form.left = new FormAttachment(50,78);
	lvr_form.right = new FormAttachment(100,-5);
	lvr_form.top = new FormAttachment(0,20);
	lvr_form.bottom = new FormAttachment(100,-2);
	tabler.setLayoutData(lvr_form);
	setSelectListener(tablel, tabler);

	collObj.set_Wizard_Table(tabler, tableMapStore);

	addBut = new Button(composite, SWT.BORDER);
	addBut.setText("Add ->");
	FormData add_form = new FormData();
	add_form.left = new FormAttachment(50,-75);
	add_form.right = new FormAttachment(50,75);
	add_form.top = new FormAttachment(0,20);
	addBut.setLayoutData(add_form);
	addBut.setEnabled(false);

	addAllBut = new Button(composite, SWT.BORDER);
	addAllBut.setText("Add All ->");
	FormData addA_form = new FormData();
	addA_form.left = new FormAttachment(50,-75);
	addA_form.right = new FormAttachment(50,75);
	addA_form.top = new FormAttachment(0,60);
	addAllBut.setLayoutData(addA_form);

	removeBut = new Button(composite, SWT.BORDER);
	removeBut.setText("<- Remove");
	FormData rem_form = new FormData();
	rem_form.left = new FormAttachment(50,-75);
	rem_form.right = new FormAttachment(50,75);
	rem_form.top = new FormAttachment(0,100);
	removeBut.setLayoutData(rem_form);
	removeBut.setEnabled(false);

	removeAllBut = new Button(composite, SWT.BORDER);
	removeAllBut.setText("<- Remove All");
	FormData remA_form = new FormData();
	remA_form.left = new FormAttachment(50,-75);
	remA_form.right = new FormAttachment(50,75);
	remA_form.top = new FormAttachment(0,140);
	removeAllBut.setLayoutData(remA_form);
	removeAllBut.setEnabled(false);

	swapBut = new Button(composite, SWT.BORDER);
	swapBut.setText("<- Swap ->");
	FormData swap_form = new FormData();
	swap_form.left = new FormAttachment(50,-75);
	swap_form.right = new FormAttachment(50,75);
	swap_form.top = new FormAttachment(0,180);
	swapBut.setLayoutData(swap_form);
	setButtonListener();


	setControl(composite);
	setPageComplete(true);
}

//@FIXME add icons
public void initial_items(Table table){
	HashMap<String, Object> collMap = parent.CollMap;
	HashMap<String, EwpChannels> chaMap = parent.ChaMap;
	tableMapStore = new HashMap<String, Integer>();

	Map<String, Object> map = collMap;
	Iterator iter = map.entrySet().iterator();
	while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		Object key = entry.getKey();
		Object obj = entry.getValue();
		EwpCollections collObj = (EwpCollections) obj;

		if (!(collObj.coll_type.equalsIgnoreCase("1"))){
			tableMapStore.put(collObj.coll_id, 0);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(collObj.coll_id);
		}
	}

	Map<String, EwpChannels> mapcha= chaMap;
	iter = mapcha.entrySet().iterator();

	while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		Object key = entry.getKey();
		Object obj = entry.getValue();
		ErlLogger.debug("cha key:"+key);
		EwpChannels chaObj = (EwpChannels) obj;
		tableMapStore.put(chaObj.cha_id, 1);
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(chaObj.cha_id);

	}
}



public void setSelectListener(final Table tablel, final Table tabler){
	tablel.addSelectionListener(new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e) {
			//int[] index= tablel.getSelectionIndices();
			int	flag = tablel.getSelectionCount();
			if(flag!=0)
				addBut.setEnabled(true);
			else
				addBut.setEnabled(false);

		}
		public void widgetDefaultSelected(SelectionEvent e) {
			int[] indeces= tablel.getSelectionIndices();
			ErlLogger.debug("tablel:"+indeces.length);
			for(int i=0;i<indeces.length;i++)
			{
				TableItem tmpItem = tablel.getItem(indeces[i]);
				new TableItem(tabler, SWT.NONE).setText(tmpItem.getText());
			}
			tablel.remove(indeces);

			addEvent(indeces[0]);
		}
	});

	tabler.addSelectionListener(new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e) {
			int flag = tabler.getSelectionCount();
			if (flag != 0)
				removeBut.setEnabled(true);
			else
				removeBut.setEnabled(false);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			int[] indeces= tabler.getSelectionIndices();
			for(int i=0;i<indeces.length;i++)
			{
				TableItem tmpItem = tabler.getItem(indeces[i]);
				new TableItem(tablel, SWT.NONE).setText(tmpItem.getText());
			}
			tabler.remove(indeces);
			removeEvent(indeces[0]);
		}
	});
}




public void setButtonListener(){
	addBut.addSelectionListener(new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e) {
			int[] indeces= tablel.getSelectionIndices();
			ErlLogger.debug("tablel:"+indeces.length);
			for(int i=0;i<indeces.length;i++)
			{
				TableItem tmpItem = tablel.getItem(indeces[i]);
				new TableItem(tabler, SWT.NONE).setText(tmpItem.getText());

			}
			tablel.remove(indeces);
			addEvent(indeces[0]);
		}
	});

	addAllBut.addSelectionListener(new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e) {
			int len= tablel.getItemCount();
			ErlLogger.debug("add all tablel len:"+len);
			for(int i=0;i<len;i++)
			{
				TableItem tmpItem = tablel.getItem(i);
				new TableItem(tabler, SWT.NONE).setText(tmpItem.getText());

			}
			tablel.removeAll();
			addBut.setEnabled(false);
			addAllBut.setEnabled(false);
			removeAllBut.setEnabled(true);
		}
	});

	removeBut.addSelectionListener(new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e) {
			int[] indeces= tabler.getSelectionIndices();
			for(int i=0;i<indeces.length;i++)
			{
				TableItem tmpItem = tabler.getItem(indeces[i]);
				new TableItem(tablel, SWT.NONE).setText(tmpItem.getText());
			}
			tabler.remove(indeces);
			removeEvent(indeces[0]);
		}
	});

	removeAllBut.addSelectionListener(new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e) {
			int len= tabler.getItemCount();
			ErlLogger.debug("add all tablel len:"+len);
			for(int i=0;i<len;i++)
			{
				TableItem tmpItem = tabler.getItem(i);
				new TableItem(tablel, SWT.NONE).setText(tmpItem.getText());

			}
			tabler.removeAll();
			removeBut.setEnabled(false);
			removeAllBut.setEnabled(false);
			addAllBut.setEnabled(true);
		}
	});

	swapBut.addSelectionListener(new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e) {
			int[] index= tabler.getSelectionIndices();
			int lenr= tabler.getItemCount();
			int lenl= tablel.getItemCount();

			for(int i=0;i<lenr;i++)
			{
				TableItem tmpItem = tabler.getItem(0);
				new TableItem(tablel, SWT.NONE).setText(tmpItem.getText());
				tabler.remove(0);
			}
			for(int i=0;i<lenl;i++)
			{
				TableItem tmpItem = tablel.getItem(0);
				new TableItem(tabler, SWT.NONE).setText(tmpItem.getText());
				tablel.remove(0);
			}
			addBut.setEnabled(false);
			removeBut.setEnabled(false);
			if (lenl==0 && lenr !=0){
				addAllBut.setEnabled(true);
				removeAllBut.setEnabled(false);
			} else if (lenl !=0 && lenr ==0){
				addAllBut.setEnabled(false);
				removeAllBut.setEnabled(true);
			}

		}
	});

}


public void addEvent(int index){
	if (removeAllBut.getEnabled() == false)
		removeAllBut.setEnabled(true);

	int len = tablel.getItemCount();
	if (len==0){
		addBut.setEnabled(false);
		addAllBut.setEnabled(false);
	}else if (tablel.getItem(index) != null) {
		tablel.select(index);
	} else {
		tablel.select(len-1);
	}
}

public void removeEvent(int index){
	if (addAllBut.getEnabled() == false)
		addAllBut.setEnabled(true);

	int len = tabler.getItemCount();
	if (len==0){
		removeBut.setEnabled(false);
		removeAllBut.setEnabled(false);
	}else if (tabler.getItem(index) != null) {
		tabler.select(index);
	} else {
		tabler.select(len-1);
	}
}

/*
public static class Item{

	public String name;
	public Object obj;

	public Item(String name, Object obj){
		this.name = name;
		this.obj = obj;
	}
}*/

/*public static class Items{
	public ArrayList<Object> items;

	public Items(){
	items = new ArrayList();
	items.add(new Item("test", collObj));
	items.add(new Item("test12312312312", collObj));
	}

	public List<Object> getItems(){
		return Collections.unmodifiableList(items);
	}
	public ArrayList<Object> getItems(){
		return items;
	}

}

public void setConProvider(ListViewer lv){
	lv.setContentProvider(new IStructuredContentProvider(){

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			ErlLogger.debug("struct dispose!");

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			ErlLogger.debug("struct change!");

		}

		@Override
		public Object[] getElements(Object element) {
			// TODO Auto-generated method stub
			return ((Items) element).getItems().toArray();
		}

	});
}

public void setLabelProvider(ListViewer lv){
	lv.setLabelProvider(new LabelProvider(){
		public String getText(Object element){
			return ((Item) element).name;
		}
	});
}



public void addSelectedChanged(ListViewer lv){
	lv.addSelectionChangedListener(new ISelectionChangedListener(){

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// TODO Auto-generated method stub
			ErlLogger.debug("select changes:");

		}

	});
}

public void addFilter(ListViewer lv){
	lv.addFilter(new ViewerFilter(){

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			// TODO Auto-generated method stub
			ErlLogger.debug("viewer filter!");
			return false;
		}

	});
}*/

}
