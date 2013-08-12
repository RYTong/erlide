package com.rytong.conf.editor.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.editors.text.TextEditor;
import org.erlide.jinterface.ErlLogger;

import com.ericsson.otp.erlang.OtpErlangBinary;

public class ChannelTable {

	private TextEditor editor;
	private Composite pagecomposite;
	private Composite cha_table;
	private IDocument document;
	private CollectionsPage parent;

	private String filePath;
	private OtpErlangBinary confCon=null;

	private Table table=null;
	private HashMap<String, TableItem> tableMap=null;
	private HashMap<String, EwpChannels> tableMapStore = null;
	private int chaIndex=1;


	public ChannelTable ChannelTable(){
		ErlLogger.debug("CollectionTable initial!");
		return this;
	}

	public ChannelTable getPage(){
		return this;
	}

	public void setParent(CollectionsPage obj) {
		parent=obj;
		editor = parent.editor;
		filePath=parent.filePathStr;
		confCon=parent.confCon;
		document = editor.getDocumentProvider()
				.getDocument(editor.getEditorInput());
	}

	public void initialCollectionsComposite(Composite maincomposite){
		pagecomposite = maincomposite;
		//right composite
		if (cha_table != null ) {
			cha_table.dispose();
		}
		cha_table = new Composite(pagecomposite, SWT.BORDER);
		FormData rightcomsite_form = new FormData();
		rightcomsite_form.left = new FormAttachment(50,5);
		rightcomsite_form.right = new FormAttachment(99);
		rightcomsite_form.top = new FormAttachment(50);
		rightcomsite_form.bottom = new FormAttachment(100);
		cha_table.setLayoutData(rightcomsite_form);

		GridLayout layout_right = new GridLayout();
		layout_right.numColumns = 6;
		layout_right.verticalSpacing=10;
		cha_table.setLayout(layout_right);

		GridData label_gd = new GridData(GridData.BEGINNING);
		label_gd.horizontalSpan = 10;

		Label label_all = new Label(cha_table, 0);
		label_all.setText("Channel Table");
		label_all.setLayoutData(label_gd);

		table = new Table(cha_table, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,8,9));

/*		Transfer[] Types = new Transfer[]{TextTransfer.getInstance()};
		int operations = DND.DROP_MOVE|DND.DROP_COPY|DND.DROP_LINK;

		final DragSource source = new DragSource(table, operations);
		source.setTransfer(Types);
		setDragListener(source);
*/

		TableColumn collId = new TableColumn(table, SWT.NONE);
		collId.setWidth(200);
		collId.setText("Channel Id");
		TableColumn collName = new TableColumn(table, SWT.NONE);
		collName.setWidth(100);
		collName.setText("Channel Name");

		//collStateText.addModifyListener(listener);
		//cha_table.setVisible(false);
		setTableListener(table);

	}

	public void setTable(HashMap<String, EwpChannels> chaMap){
		table.removeAll();
		tableMap = new HashMap<String, TableItem>();
		tableMapStore = new HashMap<String, EwpChannels>();
		Map<String, EwpChannels> map = chaMap;
		Iterator chaiter = map.entrySet().iterator();

		while (chaiter.hasNext()) {
			Map.Entry entry = (Map.Entry) chaiter.next();
			Object key = entry.getKey();
			Object obj = entry.getValue();
			//ErlLogger.debug("cha key:"+key);
			EwpChannels chaObj = (EwpChannels) obj;
			TableItem chaTable = tableMap.get(chaObj.cha_id);
			if (chaTable!=null){
				// do not create a new tree item
				//ErlLogger.debug("tree:"+chaObj.cha_id+" existed!");
			} else {
				TableItem tableId = newTableItem();
				setTableText(tableId, chaObj.cha_id, chaObj.cha_name);
				tableMap.put(chaObj.cha_id, tableId);
				// store the object into hashmap
				tableMapStore.put(chaObj.cha_id, chaObj);
			}
		}


	}

	public TableItem newTableItem(){
		TableItem tableId = new TableItem(table, SWT.NONE);
		return tableId;
	}

	public void setTableText(TableItem tableId, String id, String name){
		tableId.setText(new String[]{id, name});
	}

	public void setTableListener(final Table table) {
		table.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent event) {
				if (event.getSource() != null ){
					parent.setTableDeselect(chaIndex);
					Table eventTab = (Table) event.getSource();
					int Len = eventTab.getItemCount();
					TableItem[] eventItem = eventTab.getSelection();

					ErlLogger.debug("selectX:"+event.x+"  Y:"+event.y+"  H:"+Len*17);
					if (eventItem.length==1 && (event.y < Len*17) ){
						selectionPage(eventItem[0].getText());
					} else
					{
						eventTab.deselectAll();
						parent.setVisiable();
					}
				}else {
					ErlLogger.debug("select2 null :!");
				}
			}
		});
	}

	public void selectionPage(String key){
			EwpChannels chaObj = tableMapStore.get(key);
			ErlLogger.debug("select chaObj:"+chaObj.cha_id);

			parent.setUnVisiable();
			parent.collPage.setTextEmpty("");
			parent.chaPage.setText(chaObj);
	}

	public void setTableDeSelect(){
		table.deselectAll();
	}

}