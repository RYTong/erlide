package com.rytong.ui.editor.lua.model;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

public class Section extends Position {
    public static IDocument fDocument; // TODO static is evil!
    int startLine=0;
    
    public void setStart(int line) {
        if (fDocument == null) return;
        try {
//            setOffset(fDocument.getLineOffset(line-1) + column - 1);
        	startLine = line;
            setOffset(fDocument.getLineOffset(line-1));
//            System.out.print(line-1 + " Offset: " + getOffset());
        }
        catch (BadLocationException e) {
        }
    }
    
    public void setEnd(int line) {
        if (fDocument == null) return;
        try {
//            int o1 = getOffset();
//            int o2 = fDocument.getLineOffset(line-1) + column - 1;
//            setLength(o2-o1+1);
        	int length = fDocument.getLineLength(line-1) + fDocument.getLineOffset(line-1) - getOffset();
        	if(length>0){
        		setLength(length);
        	}else{
        		setLength(0);
        	}
//        	System.out.println(" Length: " + getLength());
        }
        catch (BadLocationException e) {
        }
    }
    
    public String toString() {
        //        return "<" + fStartLine + "," + fEndLine + "> ";
        return "";
    }
}
