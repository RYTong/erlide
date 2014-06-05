package com.rytong.template.debugtool.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class TmpStorage implements IStorage {

    private String input_str;

    public TmpStorage(String input_str){
        this.input_str = input_str;
    }


    @Override
    public Object getAdapter(Class adapter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getContents() throws CoreException {
        // TODO Auto-generated method stub
        return new ByteArrayInputStream(input_str.getBytes());
    }

    @Override
    public IPath getFullPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "Temp Storage";
    }

    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

}
