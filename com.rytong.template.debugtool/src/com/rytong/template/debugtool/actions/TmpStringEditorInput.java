package com.rytong.template.debugtool.actions;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class TmpStringEditorInput implements IStorageEditorInput{
    private final String inputString;


    public TmpStringEditorInput(String inputString){
        this.inputString = inputString;
    }


    @Override
    public boolean exists() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "Temp Input";
    }

    @Override
    public IPersistableElement getPersistable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getToolTipText() {
        // TODO Auto-generated method stub
        return "Temp input from client~";
    }

    @Override
    public Object getAdapter(Class adapter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IStorage getStorage() throws CoreException {
        // TODO Auto-generated method stub
        return new TmpStorage();
    }

    private final class TmpStorage implements IStorage {
        public InputStream getContents() throws CoreException {
            return new StringBufferInputStream(inputString);
        }

        public IPath getFullPath() {
            return null;
        }

        public String getName() {
            return TmpStringEditorInput.this.getName();
        }

        public boolean isReadOnly() {
            return false;
        }

        public Object getAdapter(Class adapter) {
            return null;
        }
    }

}
