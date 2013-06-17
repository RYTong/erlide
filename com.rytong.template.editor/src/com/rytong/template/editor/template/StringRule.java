package com.rytong.template.editor.template;

import org.eclipse.jface.text.rules.*;

public class StringRule extends MultiLineRule {

	public StringRule(String start, String end, IToken token) {
		super(start, end, token);
	}
	protected boolean sequenceDetected(
			ICharacterScanner scanner,
			char[] sequence,
			boolean eofAllowed) {
		int i = 0;
		int res = 0 ;
		if (sequence[0] == '"' || sequence[0] == '\'') {
			char tag[] = {'#', '{','c','s'};
			for (i= 0; i < tag.length -1; i++) {
				int c = scanner.read();
				if (c != tag[i]) {
					// Non-matching character detected, rewind the scanner back to the start.
					// Do not unread the first character.
					res = 0;
					break;
				} 
				
				res = -1;
				System.out.println("the res === " + res);
			}			
		}
		
		

		for (int j= i; j > 0; j--)
			scanner.unread();
		if (res == 0) {
			return super.sequenceDetected(scanner, sequence, eofAllowed);
		} else {
			return false;	
		}
	}
}
