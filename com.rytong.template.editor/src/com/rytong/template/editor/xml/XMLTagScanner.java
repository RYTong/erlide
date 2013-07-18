package com.rytong.template.editor.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.*;
import com.rytong.template.editor.template.ITemplateColorConstants;

public class XMLTagScanner extends AbstractScriptScanner {


	public XMLTagScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		this.initialize();
	}

	private static String[] fgTokenProperties = new String[] {
		ITemplateColorConstants.CS_TAG,
		ITemplateColorConstants.XML_STRING,
		ITemplateColorConstants.XML_SINGLE_QUOTE_STRING,
		ITemplateColorConstants.XML_TAG };

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		IToken other = this.getToken(ITemplateColorConstants.XML_TAG);
		IToken string = this.getToken(ITemplateColorConstants.XML_STRING);
		IToken singleQuoteString = this.getToken(ITemplateColorConstants.XML_STRING);

		IToken csTag = this.getToken(ITemplateColorConstants.CS_TAG);
		rules.add(new MultiLineRule("#{cs", "}#", csTag));//$NON-NLS-1$ //$NON-NLS-2$
		// Add rule for Strings
		rules.add(new MultiLineRule("\'", "\'", singleQuoteString, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new MultiLineRule("\"", "\"", string, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$


		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new XMLWhitespaceDetector()));


		// Default case
		this.setDefaultReturnToken(other);
		return rules;
	}

	public class XMLWhitespaceDetector implements IWhitespaceDetector {

		public boolean isWhitespace(char c) {
			return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
		}
	}

	@Override
	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}
}
