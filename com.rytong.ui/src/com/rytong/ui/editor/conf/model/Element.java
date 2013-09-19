package com.rytong.ui.editor.conf.model;

/**
 * @author lu.jingbo@rytong.com
 * 
 */
public class Element {

	private Annotation anot;
	private Element[] children;
    private String value = "undefined"; // String representation of ERLANG value
    private String dirtyValue;
    
    
    public Element(Annotation anot) {
    	this.anot = anot;
    }
    
    public Element(Annotation anot, Element[] children) {
    	this.anot = anot;
    	this.children = children;
    }
    public Element(Annotation anot, Element[] children, String value) {
    	this.anot = anot;
    	this.children = children;
    	this.value = value;
        this.dirtyValue= value;
    }
	/**
	 * @return the anot
	 */
	public Annotation getAnot() {
		return anot;
	}
	/**
	 * @param anot the anot to set
	 */
	public void setAnot(Annotation anot) {
		this.anot = anot;
	}
	/**
	 * @return the children
	 */
	public Element[] getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(Element[] children) {
		this.children = children;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
        this.dirtyValue= value;
	}

	/**
	 * @return the unsavedValue
	 */
	public String getDirtyValue() {
		return dirtyValue;
	}

	/**
	 * @param unsavedValue the unsavedValue to set
	 */
	public void setDirtyValue(String dirtyValue) {
		this.dirtyValue = dirtyValue;
	}

}
