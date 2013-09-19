/*******************************************************************************
 * Copyright (c) 2006, 2007 zovirax.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     zovirax  
 *******************************************************************************/

package com.rytong.ui.editor.lua;

import java.util.ArrayList;
import java.util.Iterator;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;

import com.rytong.ui.editor.lua.model.LuaDocument;
import com.rytong.ui.editor.lua.model.Section;
import com.rytong.ui.editor.lua.model.Symbol;


public class LuaReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension{

	private LuaEditor editor;
	
	 private IDocument luaDocument;

     /** holds the calculated positions */
     protected final ArrayList<Section> fPositions = new ArrayList<Section>();

     /** The offset of the next character to be read */
     protected int fOffset;

     /** The end offset of the range to be scanned */
     protected int fRangeEnd;

     /** Contains all the defined functions*/
     
     private LuaDocument ld;
	
	
     public LuaEditor getEditor() {
 		return editor;
 	}

 	public void setEditor(LuaEditor editor) {
 		this.editor = editor;
 	}
     
     /*
      * (non-Javadoc)
      *
      * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.eclipse.jface.text.IDocument)
      */
     public void setDocument(IDocument document) {
             this.luaDocument = document;

     }

     /*
      * (non-Javadoc)
      *
      * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.reconciler.DirtyRegion,
      *      org.eclipse.jface.text.IRegion)
      */
     public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
   
			System.out.println("reconciling dirty region");
     }

     /*
      * (non-Javadoc)
      *
      * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.IRegion)
      */
     public void reconcile(IRegion partition) {
             initialReconcile(); 
     }

	private void updateLuaModel() {
		Display.getDefault().asyncExec(new Runnable() {
            public void run() {
            	editor.setLuaDocument(ld);
                    
            }

    });
		
	}

//	protected void updateOutlineView() {
//		Display.getDefault().asyncExec(new Runnable() {
//            public void run() {
//            	if(editor.getLuaOutlinePage() != null){
//            		editor.getLuaOutlinePage().update();
//            		}
//                    
//            }
//
//    });
//	}

	public void initialReconcile() {
		 fOffset = 0;
         fRangeEnd = luaDocument.getLength();
         // parse the document
         ld = new LuaDocument();
 		 parse(luaDocument, ld);
         // update model
         updateLuaModel();
         // update outline view
        // updateOutlineView();
         // upadte folding sturucture
         //updateFoldingStructure();
		
	}

	public void setProgressMonitor(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	

	
//	protected void updateFoldingStructure() {
//		
//		// update folding structure
//		
//		fPositions.clear();
//		for (Iterator iter = ld.getFunctionList().iterator(); iter.hasNext();) {
//			Symbol sym = (Symbol) iter.next();
//			sym.getSection();
//			fPositions.add(sym.getSection());
//		}
//
//        Display.getDefault().asyncExec(new Runnable() {
//                public void run() {
//                        editor.updateFoldingStructure(fPositions);
//                        
//                }
//
//        });
//}
	
	protected void parse(final IDocument document, LuaDocument ld) {
		LuaAnalyser parser = null;
		try {
			LuaLexer lexer = new LuaLexer(new ANTLRStringStream(document
					.get()));
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			parser = new LuaAnalyser(tokens);
			parser.setEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Section.fDocument = document;

		try {
			parser.start(ld);
			for (Iterator iter = ld.getSymbolList().iterator(); iter.hasNext();) {
				Symbol sym = (Symbol) iter.next();
				try {
					document.addPosition(sym.getSection());
				} catch (BadLocationException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		for (Iterator iter = ld.getVariablesList().iterator(); iter.hasNext();) {
//			Symbol sym = (Symbol) iter.next();
//			System.out.println(sym.toString());
//			}
	}

}
