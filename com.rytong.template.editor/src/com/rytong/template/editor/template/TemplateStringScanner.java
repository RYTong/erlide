package com.rytong.template.editor.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

import com.rytong.template.editor.xml.XMLTagScanner.XMLWhitespaceDetector;

public class TemplateStringScanner extends AbstractScriptScanner {

	public TemplateStringScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		this.initialize();
	}
	private static String[] fgTokenProperties = new String[] { 
		ITemplateColorConstants.XML_STRING,
		ITemplatePartitions.CS
		 };
	
	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		IToken other = this.getToken(ITemplateColorConstants.XML_STRING);

		IToken csTag = new Token(ITemplatePartitions.CS);
		rules.add(new MultiLineRule("#{cs", "}#", csTag));//$NON-NLS-1$ //$NON-NLS-2$

		// Default case
		this.setDefaultReturnToken(other);
		return rules;
	}
	
	@Override
	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

}
