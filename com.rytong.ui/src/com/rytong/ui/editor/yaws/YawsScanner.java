package com.rytong.ui.editor.yaws;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import com.rytong.ui.editor.ColorManager;
import com.rytong.ui.editor.NumberDetector;

/**
 * @author lu.jingbo@rytong.com
 *
 */
public class YawsScanner extends RuleBasedScanner {

    /**
     * Detector for atom.
     */
	static class AtomDetector implements IWordDetector {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
		 */
		@Override
		public boolean isWordStart(char c) {
	    	return Character.isJavaIdentifierPart(c);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
		 */
		@Override
		public boolean isWordPart(char c) {
	    	return Character.isJavaIdentifierPart(c);
		}
	}
    
    /**
     * Detector for whitespace
     */
	static class WhitespaceDetector implements IWhitespaceDetector {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
		 */
		@Override
		public boolean isWhitespace(char c) {
	    	return Character.isWhitespace(c);
		}
	}

	public YawsScanner(ColorManager colorManager) {
		super();
        
		IToken tag = new Token(new TextAttribute(colorManager.getColor(YawsColorManager.TAG)));
		IToken value = new Token(new TextAttribute(colorManager.getColor(YawsColorManager.VALUE)));
		IToken bool = new Token(new TextAttribute(colorManager.getColor(YawsColorManager.BOOLEAN)));
		IToken number = new Token(new TextAttribute(colorManager.getColor(YawsColorManager.NUMBER)));
		IToken undefined = new Token(new TextAttribute(colorManager.getColor(YawsColorManager.UNDEFINED)));
		IToken comment = new Token(new TextAttribute(colorManager.getColor(YawsColorManager.COMMENT)));
		IToken other = new Token(new TextAttribute(colorManager.getColor(YawsColorManager.DEFAULT), null, SWT.BOLD));
		
		List<IRule> rules= new LinkedList<IRule>();
		
		rules.add(new SingleLineRule("<", ">", tag, '\\'));
		rules.add(new SingleLineRule("=", null, value, '\\', true));
		WordRule numberRule= new WordRule(new NumberDetector(), number);
		WordRule wordRule= new WordRule(new AtomDetector(), other);
		wordRule.addWord("true", bool);
      wordRule.addWord("false", bool);
      wordRule.addWord("undefined", undefined);
      rules.add(numberRule);
		rules.add(wordRule);
      rules.add(new EndOfLineRule("#", comment));
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		
		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

}
