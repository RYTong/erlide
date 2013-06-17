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

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import com.rytong.template.editor.template.ITemplatePartitions;
import com.rytong.template.editor.template.TemplateConstants;

public class LuaPartitionScanner extends RuleBasedPartitionScanner {

    public LuaPartitionScanner() {
        super();
        List<PatternRule> rules = new ArrayList<PatternRule>();

        /*
         * Deal with single and double quote multi lines strings
         */
        IToken string = new Token(ITemplatePartitions.LUA_STRING);
        IToken singleQuoteString = new Token(ITemplatePartitions.LUA_SINGLE_QUOTE_STRING);
        rules.add(new MultiLineRule("\'", "\'", singleQuoteString, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$
        rules.add(new MultiLineRule("\"", "\"", string, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$

        /*
         * Deal with comments
         */

        // Multi-line
        IToken multiLineComment = new Token(ITemplatePartitions.LUA_MULTI_LINE_COMMENT);
        rules.add(new MultiLineRule("--[[", "]]", multiLineComment));//$NON-NLS-1$ //$NON-NLS-2$

        // Single line
        IToken comment = new Token(ITemplatePartitions.LUA_COMMENT);
        rules.add(new EndOfLineRule(TemplateConstants.COMMENT_STRING, comment));

        // Apply rules
        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
    
}
