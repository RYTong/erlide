package com.rytong.template.editor.cs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;


import com.rytong.template.editor.template.ITemplateColorConstants;
import com.rytong.template.editor.template.ITemplatePartitions;

public class CSCodeScanner extends AbstractScriptScanner {

	private static String[] fgKeywords = { 
		"var", "evar", "lvar", "include", 
		"linclude", "set", "name",
		"if", "else", "elif", "alt",
		"each", "loop", "with",
		"def", "call"};

	private static String[] fgTokenProperties = new String[] { 
		ITemplateColorConstants.CS_STRING,  
		ITemplateColorConstants.CS_DEFAULT,
		ITemplateColorConstants.CS_SINGLE_QUOTE_STRING,
		ITemplateColorConstants.CS_KEYWORD };

	public CSCodeScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		this.initialize();
	}

	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		IToken keyword = this.getToken(ITemplateColorConstants.CS_KEYWORD);
		
		IToken other = this.getToken(ITemplateColorConstants.CS_TAG);

		IToken string = this.getToken(ITemplateColorConstants.CS_STRING);
		IToken singleQuoteString = this.getToken(ITemplateColorConstants.CS_SINGLE_QUOTE_STRING);
		
		
		// Add rule for Strings
		rules.add(new MultiLineRule("\'", "\'", singleQuoteString, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new MultiLineRule("\"", "\"", string, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$


		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new LuaWhitespaceDetector()));

		// Add word rule for keywords.
		WordRule wordRule = new WordRule(new CSWordDetector(), other);
		for (int i = 0; i < fgKeywords.length; i++) {
			wordRule.addWord(fgKeywords[i], keyword);
		}
		rules.add(wordRule);



		// Default case
		this.setDefaultReturnToken(other);
		return rules;
	}

	/**
	 * Indicates if argument is a white space
	 * 
	 * @param char Tested character
	 */
	public class LuaWhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}

	

	public class CSWordDetector implements IWordDetector {
		/**
		 * Indicates if argument is part of a word
		 * 
		 * @param char Tested character
		 */
		public boolean isWordPart(char character) {
			return Character.isJavaIdentifierPart(character);
		}

		/**
		 * Indicates if argument starts of a word
		 * 
		 * @param char Tested character
		 */
		public boolean isWordStart(char character) {
			return Character.isJavaIdentifierStart(character);
		}
	}



}
