package com.rytong.ui.editor.lua;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import com.rytong.ui.editor.AtomDetector;
import com.rytong.ui.editor.ColorManager;
import com.rytong.ui.editor.NumberDetector;
import com.rytong.ui.editor.WhitespaceDetector;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class LuaScanner extends RuleBasedScanner {
	private static String[] fgKeywords= { "and", "break", "do", "else", "elseif", "end", "false", "for", "function", "if", "in", "local", "nil", "not", "or", "repeat", "return", "then", "true", "until", "while" };

	public LuaScanner(ColorManager colorManager) {
		super();
		IToken keyword= new Token(new TextAttribute(colorManager.getColor(LuaColorManager.KEYWORD), null, SWT.BOLD));
		IToken string = new Token(new TextAttribute(colorManager.getColor(LuaColorManager.STRING)));
		IToken number = new Token(new TextAttribute(colorManager.getColor(LuaColorManager.NUMBER)));
		IToken comment = new Token(new TextAttribute(colorManager.getColor(LuaColorManager.COMMENT)));
		IToken other = new Token(new TextAttribute(colorManager.getColor(LuaColorManager.DEFAULT)));
		
		List<IRule> rules= new LinkedList<IRule>();
        
		// Add word rule for keywords, types, and constants.
		WordRule wordRule= new WordRule(new AtomDetector(), other);
		for (int i= 0; i < fgKeywords.length; i++)
			wordRule.addWord(fgKeywords[i], keyword);
		
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
      rules.add(new LuaMultLineStringRule(string));
		WordRule numberRule= new WordRule(new NumberDetector(), number);
      rules.add(numberRule);
		rules.add(wordRule);
      rules.add(new EndOfLineRule("--", comment));
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		
		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

}
