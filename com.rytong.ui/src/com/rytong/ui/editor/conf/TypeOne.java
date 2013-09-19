package com.rytong.ui.editor.conf;

import com.rytong.ui.editor.conf.model.Element;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class TypeOne extends NamedObject {
	public static final String P_CHOICE="choice"; //$NON-NLS-1$
	public static final String P_FLAG="flag"; //$NON-NLS-1$
	public static final String P_TEXT="text"; //$NON-NLS-1$
	public static final String [] CHOICES = {
			Messages.getString("TypeOne.c1"), Messages.getString("TypeOne.c2"), Messages.getString("TypeOne.c3"), Messages.getString("TypeOne.c4") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private int choice=0;
	private String text;
	private boolean flag;
    private Element element;
    
    public TypeOne(String name, Element element) {
    	super(name);
    	this.element = element;
    }

	/**
	 * @param name
	 */
	public TypeOne(String name, int choice, boolean flag, String text) {
		super(name);
		this.flag = flag;
		this.text = text;
		this.choice = choice;
	}
	public TypeOne(String name, Element element, int choice, boolean flag, String text) {
		super(name);
        this.element = element;
		this.flag = flag;
		this.text = text;
		this.choice = choice;
	}
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
		model.fireModelChanged(new Object[] {this}, IModelListener.CHANGED, P_CHOICE);
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
		model.fireModelChanged(new Object[] {this}, IModelListener.CHANGED, P_FLAG);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		model.fireModelChanged(new Object[] {this}, IModelListener.CHANGED, P_TEXT);
	}
	/**
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}
	/**
	 * @param element the element to set
	 */
	public void setElement(Element element) {
		this.element = element;
	}
    
}