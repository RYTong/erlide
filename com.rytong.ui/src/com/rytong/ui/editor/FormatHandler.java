package com.rytong.ui.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;

import com.rytong.ui.formatter.FormatUtil;
import com.rytong.ui.formatter.IFormatter;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class FormatHandler extends AbstractHandler{

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        IFormatter formatter = FormatUtil.getFormatter(event);
        if(formatter!=null){
            IDocument document = formatter.getDocument();
            String source = document.get();
            formatter.format(source, 0, source.length());
        }
		return null;
	}

}
