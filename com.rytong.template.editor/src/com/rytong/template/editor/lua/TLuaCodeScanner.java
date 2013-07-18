

package com.rytong.template.editor.lua;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class TLuaCodeScanner extends AbstractScriptScanner {

	@SuppressWarnings("nls")
	private static String[] fgKeywords = { "and", "break", "do", "else", "elseif", "end", "false", "for", "function", "if", "in", "local", "nil",
			"not", "or", "repeat", "return", "then", "true", "until", "while" };

	private static String[] fgTokenProperties = new String[] { LuaColorConstants.LUA_NUMBER, LuaColorConstants.LUA_DEFAULT,
		LuaColorConstants.LUA_KEYWORD };

	public TLuaCodeScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		this.initialize();
	}

	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	protected List<IRule> createRules() {
		final List<IRule> rules = new ArrayList<IRule>();
		final IToken keyword = this.getToken(LuaColorConstants.LUA_KEYWORD);
		final IToken numbers = this.getToken(LuaColorConstants.LUA_NUMBER);
		final IToken other = this.getToken(LuaColorConstants.LUA_DEFAULT);

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new LuaWhitespaceDetector()));

		// Add word rule for keywords.
		final WordRule wordRule = new WordRule(new LuaWordDetector(), other);
		for (int i = 0; i < fgKeywords.length; i++) {
			wordRule.addWord(fgKeywords[i], keyword);
		}
		rules.add(wordRule);

		// Add number recognition
		final NumberRule numberRule = new LuaNumberRule(numbers);
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
	public static class LuaWhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}

	public static class LuaWordDetector implements IWordDetector {
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

	public static class LuaNumberRule extends NumberRule {
		public LuaNumberRule(final IToken token) {
			super(token);
		}

		@Override
		public IToken evaluate(final ICharacterScanner scanner) {
			if (eatNumber(scanner) > 0) {
				return fToken;
			}
			return Token.UNDEFINED;
		}

		private static int eatExponential(final ICharacterScanner scanner) {

			// Find 'e' or 'E'
			char current = (char) scanner.read();
			if (current == 'e' || current == 'E') {

				// 'e' is followed by digits it is an exponential notation
				if (followedByDigit(scanner)) {
					return eatDecimalDigits(scanner) + 1;
				} else {

					// Check for optional sign
					current = (char) scanner.read();
					if ((current == '-' || current == '+') && followedByDigit(scanner)) {

						// There is a digit after 'e' and sign
						return eatDecimalDigits(scanner) + 2;

					} else {
						// Last characters red are not an exponential notation
						scanner.unread();
					}
				}
			}
			scanner.unread();
			return 0;
		}

		private static int eatDecimalDigitsFromDot(final ICharacterScanner scanner) {
			// Handle '.'
			if (scanner.read() == '.') {
				return eatDecimalDigits(scanner) + 1;
			}
			scanner.unread();
			return 0;
		}

		private static int eatDecimalDigits(final ICharacterScanner scanner) {
			int digits = 0;
			while (Character.isDigit((char) scanner.read())) {
				digits++;
			}
			scanner.unread();
			return digits;
		}

		private static int eatNumber(final ICharacterScanner scanner) {
			final char current = (char) scanner.read();
			final int result;
			switch (current) {
			case '.':
				result = eatDecimalDigits(scanner);
				if (result > 0) {
					return result + eatExponential(scanner) + 1;
				}
				break;
			case '0':
				// Check hexadecimal
				if (followedByChar(scanner, 'x') || followedByChar(scanner, 'X')) {
					result = eatHexaecimalDigits(scanner);
					if (result > 0) {
						return result + eatExponential(scanner) + 1;
					}
				} else {
					// Regular numbers
					return eatDecimalDigits(scanner) + eatDecimalDigitsFromDot(scanner) + eatExponential(scanner) + 1;
				}
				break;
			default:
				if (Character.isDigit(current)) {
					return eatDecimalDigits(scanner) + eatDecimalDigitsFromDot(scanner) + eatExponential(scanner) + 1;
				}
			}
			scanner.unread();
			return 0;
		}

		private static int eatHexaecimalDigits(final ICharacterScanner scanner) {

			// Find 'x'
			int digits = 0;
			final char current = (char) scanner.read();
			if ((current == 'x' || current == 'X') && followedByHexadecimal(scanner)) {
				digits++;
			} else {
				scanner.unread();
				return digits;
			}

			// Loop over hexadecimal digits
			while (Character.digit((char) scanner.read(), 16) != -1) {
				digits++;
			}
			scanner.unread();
			return digits;
		}

		private static boolean followedByChar(final ICharacterScanner scanner, final char character) {
			final boolean result = character == (char) scanner.read();
			scanner.unread();
			return result;
		}

		private static boolean followedByDigit(final ICharacterScanner scanner) {
			final boolean result = Character.isDigit((char) scanner.read());
			scanner.unread();
			return result;
		}

		private static boolean followedByHexadecimal(final ICharacterScanner scanner) {
			final boolean result = Character.digit((char) scanner.read(), 16) != -1;
			scanner.unread();
			return result;
		}
	}
}
