/*******************************************************************************
 * Copyright (c) 2009, 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/

package com.rytong.template.editor.lua;

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

public class LuaCodeScanner extends AbstractScriptScanner {

    private static String[] fgKeywords = { "and", "break", "do", "else", "elseif", "end", "false", "for", "function", "if", "in", "local", "nil",
            "not", "or", "repeat", "return", "then", "true", "until", "while" };

    private static String[] fgTokenProperties = new String[] { 
    	ITemplateColorConstants.CS_TAG,
    	ITemplateColorConstants.LUA_STRING, 
    	ITemplateColorConstants.LUA_SINGLE_LINE_COMMENT,
    	ITemplateColorConstants.LUA_MULTI_LINE_COMMENT,
    	ITemplateColorConstants.LUA_NUMBER, 
    	ITemplateColorConstants.LUA_DEFAULT,
    	ITemplateColorConstants.LUA_SINGLE_QUOTE_STRING,
    	ITemplateColorConstants.LUA_KEYWORD };

    public LuaCodeScanner(IColorManager manager, IPreferenceStore store) {
        super(manager, store);
        this.initialize();
    }

    protected String[] getTokenProperties() {
        return fgTokenProperties;
    }

    protected List<IRule> createRules() {
        List<IRule> rules = new ArrayList<IRule>();
        IToken keyword = this.getToken(ITemplateColorConstants.LUA_KEYWORD);
        IToken comment = this.getToken(ITemplateColorConstants.LUA_SINGLE_LINE_COMMENT);
        IToken multiline = this.getToken(ITemplateColorConstants.LUA_MULTI_LINE_COMMENT);
        IToken numbers = this.getToken(ITemplateColorConstants.LUA_NUMBER);
        IToken other = this.getToken(ITemplateColorConstants.LUA_DEFAULT);
        
        IToken string = this.getToken(ITemplateColorConstants.LUA_STRING);
        IToken singleQuoteString = this.getToken(ITemplateColorConstants.LUA_SINGLE_QUOTE_STRING);
        IToken noparseString = this.getToken(ITemplateColorConstants.LUA_STRING);
        
        IToken csTag = this.getToken(ITemplateColorConstants.CS_TAG);
		rules.add(new MultiLineRule("<?cs", "?>", csTag));//$NON-NLS-1$ //$NON-NLS-2$
		
		IToken csTag1 = this.getToken(ITemplateColorConstants.CS_TAG);	
		rules.add(new MultiLineRule("#{cs", "}#", csTag1));//$NON-NLS-1$ //$NON-NLS-2$
        // Add rule for Strings
        rules.add(new MultiLineRule("\'", "\'", singleQuoteString, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$
        rules.add(new MultiLineRule("\"", "\"", string, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$
        rules.add(new MultiLineRule("[[", "]]", noparseString));
        
        // Add rule for multi-line comments
        rules.add(new MultiLineRule("--[[", "]]", multiline));

        // Add rule for single line comments.
        rules.add(new EndOfLineRule("--", comment));

        // Add generic whitespace rule.
        rules.add(new WhitespaceRule(new LuaWhitespaceDetector()));

        // Add word rule for keywords.
        WordRule wordRule = new WordRule(new LuaWordDetector(), other);
        for (int i = 0; i < fgKeywords.length; i++) {
            wordRule.addWord(fgKeywords[i], keyword);
        }
        rules.add(wordRule);

        // Add number recognition
        NumberRule numberRule = new LuaNumberRule(numbers);
        rules.add(numberRule);

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

    public class LuaWordDetector implements IWordDetector {
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

    public class LuaNumberRule extends NumberRule {
        public LuaNumberRule(IToken token) {
            super(token);
        }

        public IToken evaluate(ICharacterScanner scanner) {
            int c = scanner.read();
            int p = c;
            if (Character.isDigit((char) c) || c == '.' || c == '-') {
                boolean hex = false;
                if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {
                    do {
                        p = c;
                        c = scanner.read();
                        if (c == 'x' || c == 'X' && !hex) {
                            hex = true;
                            p = c;
                            c = scanner.read();
                        }
                    } while (Character.isDigit((char) c) || (hex && Character.digit((char) c, 16) != -1));
                    if (c != 'e' && c != 'E') {
                        scanner.unread();
                    }
                    if (p == '.') {
                        scanner.unread();
                        return Token.UNDEFINED;
                    }
                    return fToken;
                }
            }
            scanner.unread();
            return Token.UNDEFINED;
        }
    }
}

