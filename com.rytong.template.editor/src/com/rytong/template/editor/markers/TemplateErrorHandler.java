package com.rytong.template.editor.markers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.erlide.jinterface.ErlLogger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;



public class TemplateErrorHandler extends DefaultHandler {

	public static final String ERROR_MARKER_ID = "com.rytong.template.editor.templateError";
	
	private IFile file;

	private Map<Object, Object> map;
	public TemplateErrorHandler(IFile file) {
		this.file = file;
		this.map = new HashMap<Object, Object>();
	}
	
	public void removeExistingMarkers()
	{
		try
		{
			file.deleteMarkers(ERROR_MARKER_ID, true, IResource.DEPTH_ZERO);
		}
		catch (CoreException e1)
		{
			e1.printStackTrace();
		}
	}

	private void addMarker(SAXParseException e, int severity) {
		addMarker(e.getLineNumber(), e.getMessage(), severity);
		
	}
	
	private void addMarker(int lineNumber, String message, int severity) {
		ErlLogger.debug("the message : " + message);
		MarkerUtilities.setLineNumber(map, lineNumber);
		MarkerUtilities.setMessage(map, message);
		map.put(IMarker.LOCATION, file.getFullPath().toString());
		map.put(IMarker.SEVERITY, new Integer(severity));
		try
		{
			MarkerUtilities.createMarker(file, map, ERROR_MARKER_ID);
		}
		catch (CoreException ee)
		{
			ee.printStackTrace();
		}
	}

	public void error(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_ERROR);
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_ERROR);
	}

	public void warning(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_WARNING);
	}

	public void addCSError(int lineNumber, String message) {
		addMarker(lineNumber, message, IMarker.SEVERITY_ERROR);
		
	}
}
