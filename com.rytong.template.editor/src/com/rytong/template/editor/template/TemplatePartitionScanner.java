package com.rytong.template.editor.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class TemplatePartitionScanner extends RuleBasedPartitionScanner {

	public TemplatePartitionScanner() {
        super();
        List<PatternRule> rules = new ArrayList<PatternRule>();


        // CS  contained in <!cs !> and #{cs }#
        IToken csCode = new Token(ITemplatePartitions.CS);
        IToken insindeCSCode = new Token(ITemplatePartitions.CS);
        rules.add(new MultiLineRule("<?cs", "?>", csCode, '\\', false));//$NON-NLS-1$ //$NON-NLS-2$
        rules.add(new MultiLineRule("#{cs", "}#", insindeCSCode, '\\', false));//$NON-NLS-1$ //$NON-NLS-2$
        
        // Lua script contained in <![CDATA[ ... ]>
        IToken luaCode = new Token(ITemplatePartitions.LUA);
        rules.add(new MultiLineRule("<![CDATA[", "]]>", luaCode, '\\', false));//$NON-NLS-1$ //$NON-NLS-2$
        
        
        // XML Tag  
        IToken tag = new Token(ITemplatePartitions.XML_TAG);
        rules.add(new MultiLineRule("<", ">", tag, '\\', false));

       
        
        // Apply rules
        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
    
}
