package com.rytong.conf.adapter.editor;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.erlide.jinterface.ErlLogger;

public class AdapterTreeContentProvider implements ITreeContentProvider{

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object[] getElements(Object inputElement) {
        // TODO Auto-generated method stub
        Object[] tmp = ((EwpAdpaterList) inputElement).getAdapterArray();
        ErlLogger.debug("adapter length:"+tmp.length);
        //ErlLogger.debug(""+((EwpAdapter) tmp[0]).getChildrenArray().length);
        return tmp;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        // TODO Auto-generated method stub
        Object[] tmp = ((EwpAdapter) parentElement).getChildrenArray();
        ErlLogger.debug("Procedure length:"+tmp.length);
        return tmp;
    }

    @Override
    public Object getParent(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        // TODO Auto-generated method stub
        if (element instanceof EwpAdapter){
            return ((EwpAdapter) element).has_children();
        } else {
            return false;
        }

    }

}
