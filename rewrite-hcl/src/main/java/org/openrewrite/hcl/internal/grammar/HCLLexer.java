/*
 * Copyright 2022 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Generated from /Users/jon/Projects/github/openrewrite/rewrite/rewrite-hcl/src/main/antlr/HCLLexer.g4 by ANTLR 4.9.3
package org.openrewrite.hcl.internal.grammar;
import java.util.Stack;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class HCLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		FOR_BRACE=1, FOR_BRACK=2, IF=3, IN=4, LBRACE=5, RBRACE=6, ASSIGN=7, Identifier=8, 
		WS=9, COMMENT=10, LINE_COMMENT=11, NEWLINE=12, NumericLiteral=13, BooleanLiteral=14, 
		QUOTE=15, NULL=16, HEREDOC_START=17, PLUS=18, AND=19, EQ=20, LT=21, COLON=22, 
		LBRACK=23, LPAREN=24, MINUS=25, OR=26, NEQ=27, GT=28, QUESTION=29, RBRACK=30, 
		RPAREN=31, MUL=32, NOT=33, LEQ=34, DOT=35, DIV=36, GEQ=37, ARROW=38, COMMA=39, 
		MOD=40, ELLIPSIS=41, TILDE=42, TEMPLATE_INTERPOLATION_START=43, TemplateStringLiteral=44, 
		TemplateStringLiteralChar=45, HTemplateLiteral=46, HTemplateLiteralChar=47;
	public static final int
		TEMPLATE=1, HEREDOC_PREAMBLE=2, HEREDOC=3;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "TEMPLATE", "HEREDOC_PREAMBLE", "HEREDOC"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"FOR_BRACE", "FOR_BRACK", "IF", "IN", "LBRACE", "RBRACE", "ASSIGN", "StringLiteralChar", 
			"Identifier", "WS", "COMMENT", "LINE_COMMENT", "NEWLINE", "LetterOrDigit", 
			"Letter", "EscapeSequence", "HexDigit", "NumericLiteral", "ExponentPart", 
			"BooleanLiteral", "QUOTE", "NULL", "HEREDOC_START", "PLUS", "AND", "EQ", 
			"LT", "COLON", "LBRACK", "LPAREN", "MINUS", "OR", "NEQ", "GT", "QUESTION", 
			"RBRACK", "RPAREN", "MUL", "NOT", "LEQ", "DOT", "DIV", "GEQ", "ARROW", 
			"COMMA", "MOD", "ELLIPSIS", "TILDE", "TEMPLATE_INTERPOLATION_START", 
			"TemplateStringLiteral", "TemplateStringLiteralChar", "END_QUOTE", "HP_NEWLINE", 
			"HPIdentifier", "H_NEWLINE", "H_TEMPLATE_INTERPOLATION_START", "HTemplateLiteral", 
			"HTemplateLiteralChar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'if'", "'in'", "'{'", "'}'", "'='", null, null, null, 
			null, null, null, null, null, "'null'", null, "'+'", "'&&'", "'=='", 
			"'<'", "':'", "'['", "'('", "'-'", "'||'", "'!='", "'>'", "'?'", "']'", 
			"')'", "'*'", "'!'", "'<='", "'.'", "'/'", "'>='", "'=>'", "','", "'%'", 
			"'...'", "'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "FOR_BRACE", "FOR_BRACK", "IF", "IN", "LBRACE", "RBRACE", "ASSIGN", 
			"Identifier", "WS", "COMMENT", "LINE_COMMENT", "NEWLINE", "NumericLiteral", 
			"BooleanLiteral", "QUOTE", "NULL", "HEREDOC_START", "PLUS", "AND", "EQ", 
			"LT", "COLON", "LBRACK", "LPAREN", "MINUS", "OR", "NEQ", "GT", "QUESTION", 
			"RBRACK", "RPAREN", "MUL", "NOT", "LEQ", "DOT", "DIV", "GEQ", "ARROW", 
			"COMMA", "MOD", "ELLIPSIS", "TILDE", "TEMPLATE_INTERPOLATION_START", 
			"TemplateStringLiteral", "TemplateStringLiteralChar", "HTemplateLiteral", 
			"HTemplateLiteralChar"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	    private enum CurlyType {
	        INTERPOLATION,
	        OBJECT
	    }

	    private Stack<CurlyType> leftCurlyStack = new Stack<CurlyType>();
	    private Stack<String> heredocIdentifier = new Stack<String>();


	public HCLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "HCLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 4:
			LBRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 5:
			RBRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 48:
			TEMPLATE_INTERPOLATION_START_action((RuleContext)_localctx, actionIndex);
			break;
		case 53:
			HPIdentifier_action((RuleContext)_localctx, actionIndex);
			break;
		case 55:
			H_TEMPLATE_INTERPOLATION_START_action((RuleContext)_localctx, actionIndex);
			break;
		case 56:
			HTemplateLiteral_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void LBRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			    leftCurlyStack.push(CurlyType.OBJECT);

			break;
		}
	}
	private void RBRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:

			    if(!leftCurlyStack.isEmpty()) {
			        if(leftCurlyStack.pop() == CurlyType.INTERPOLATION) {
			            popMode();
			        } else {
			            // closing an object, stay in the default mode
			        }
			    }

			break;
		}
	}
	private void TEMPLATE_INTERPOLATION_START_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			leftCurlyStack.push(CurlyType.INTERPOLATION);
			break;
		}
	}
	private void HPIdentifier_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:

			    heredocIdentifier.push(getText());

			break;
		}
	}
	private void H_TEMPLATE_INTERPOLATION_START_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			leftCurlyStack.push(CurlyType.INTERPOLATION);
			break;
		}
	}
	private void HTemplateLiteral_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:

			  if(!heredocIdentifier.isEmpty() && getText().endsWith(heredocIdentifier.peek())) {
			      setType(Identifier);
			      heredocIdentifier.pop();
			      popMode();
			  }

			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\61\u01bc\b\1\b\1"+
		"\b\1\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t"+
		"\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21"+
		"\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30"+
		"\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37"+
		"\t\37\4 \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)"+
		"\4*\t*\4+\t+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63"+
		"\t\63\4\64\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;"+
		"\3\2\3\2\3\2\7\2~\n\2\f\2\16\2\u0081\13\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3"+
		"\3\3\3\3\7\3\u008c\n\3\f\3\16\3\u008f\13\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4"+
		"\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\5\t\u00a7"+
		"\n\t\3\n\3\n\3\n\7\n\u00ac\n\n\f\n\16\n\u00af\13\n\3\13\6\13\u00b2\n\13"+
		"\r\13\16\13\u00b3\3\13\3\13\3\f\3\f\3\f\3\f\7\f\u00bc\n\f\f\f\16\f\u00bf"+
		"\13\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\5\r\u00c9\n\r\3\r\7\r\u00cc\n\r"+
		"\f\r\16\r\u00cf\13\r\3\r\5\r\u00d2\n\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\17\3\17\5\17\u00de\n\17\3\20\3\20\3\20\3\20\5\20\u00e4\n\20\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\5\21\u00f8\n\21\3\22\3\22\3\23\6\23\u00fd\n\23\r\23"+
		"\16\23\u00fe\3\23\3\23\7\23\u0103\n\23\f\23\16\23\u0106\13\23\3\23\5\23"+
		"\u0109\n\23\3\23\6\23\u010c\n\23\r\23\16\23\u010d\3\23\3\23\6\23\u0112"+
		"\n\23\r\23\16\23\u0113\5\23\u0116\n\23\3\24\3\24\5\24\u011a\n\24\3\24"+
		"\6\24\u011d\n\24\r\24\16\24\u011e\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\5\25\u012a\n\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"+
		"\3\30\3\30\3\30\3\30\5\30\u0139\n\30\3\30\3\30\3\31\3\31\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3"+
		"!\3\"\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\3*\3*\3+"+
		"\3+\3,\3,\3,\3-\3-\3-\3.\3.\3/\3/\3\60\3\60\3\60\3\60\3\61\3\61\3\62\3"+
		"\62\3\62\3\62\3\62\3\62\3\62\3\63\6\63\u0180\n\63\r\63\16\63\u0181\3\64"+
		"\3\64\3\64\3\64\3\64\3\64\5\64\u018a\n\64\3\65\3\65\3\65\3\65\3\65\3\66"+
		"\3\66\3\66\3\66\3\66\3\67\3\67\3\67\7\67\u0199\n\67\f\67\16\67\u019c\13"+
		"\67\3\67\3\67\3\67\3\67\38\38\38\38\39\39\39\39\39\39\39\39\3:\6:\u01af"+
		"\n:\r:\16:\u01b0\3:\3:\3;\3;\3;\3;\3;\3;\5;\u01bb\n;\3\u00bd\2<\6\3\b"+
		"\4\n\5\f\6\16\7\20\b\22\t\24\2\26\n\30\13\32\f\34\r\36\16 \2\"\2$\2&\2"+
		"(\17*\2,\20.\21\60\22\62\23\64\24\66\258\26:\27<\30>\31@\32B\33D\34F\35"+
		"H\36J\37L N!P\"R#T$V%X&Z\'\\(^)`*b+d,f-h.j/l\2n\2p\2r\2t\2v\60x\61\6\2"+
		"\3\4\5\20\6\2\f\f\17\17$$&\'\5\2\13\13\16\17\"\"\4\2\f\f\17\17\3\2\62"+
		";\6\2&&C\\aac|\4\2\2\u0081\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001"+
		"\7\2$$^^ppttvv\5\2\62;CHch\4\2GGgg\4\2--//\3\2}}\5\2\f\f\17\17&\'\2\u01d8"+
		"\2\6\3\2\2\2\2\b\3\2\2\2\2\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2\2\2\20\3\2"+
		"\2\2\2\22\3\2\2\2\2\26\3\2\2\2\2\30\3\2\2\2\2\32\3\2\2\2\2\34\3\2\2\2"+
		"\2\36\3\2\2\2\2(\3\2\2\2\2,\3\2\2\2\2.\3\2\2\2\2\60\3\2\2\2\2\62\3\2\2"+
		"\2\2\64\3\2\2\2\2\66\3\2\2\2\28\3\2\2\2\2:\3\2\2\2\2<\3\2\2\2\2>\3\2\2"+
		"\2\2@\3\2\2\2\2B\3\2\2\2\2D\3\2\2\2\2F\3\2\2\2\2H\3\2\2\2\2J\3\2\2\2\2"+
		"L\3\2\2\2\2N\3\2\2\2\2P\3\2\2\2\2R\3\2\2\2\2T\3\2\2\2\2V\3\2\2\2\2X\3"+
		"\2\2\2\2Z\3\2\2\2\2\\\3\2\2\2\2^\3\2\2\2\2`\3\2\2\2\2b\3\2\2\2\2d\3\2"+
		"\2\2\3f\3\2\2\2\3h\3\2\2\2\3j\3\2\2\2\3l\3\2\2\2\4n\3\2\2\2\4p\3\2\2\2"+
		"\5r\3\2\2\2\5t\3\2\2\2\5v\3\2\2\2\5x\3\2\2\2\6z\3\2\2\2\b\u0088\3\2\2"+
		"\2\n\u0096\3\2\2\2\f\u0099\3\2\2\2\16\u009c\3\2\2\2\20\u009f\3\2\2\2\22"+
		"\u00a2\3\2\2\2\24\u00a6\3\2\2\2\26\u00a8\3\2\2\2\30\u00b1\3\2\2\2\32\u00b7"+
		"\3\2\2\2\34\u00c8\3\2\2\2\36\u00d7\3\2\2\2 \u00dd\3\2\2\2\"\u00e3\3\2"+
		"\2\2$\u00f7\3\2\2\2&\u00f9\3\2\2\2(\u0115\3\2\2\2*\u0117\3\2\2\2,\u0129"+
		"\3\2\2\2.\u012b\3\2\2\2\60\u012f\3\2\2\2\62\u0134\3\2\2\2\64\u013c\3\2"+
		"\2\2\66\u013e\3\2\2\28\u0141\3\2\2\2:\u0144\3\2\2\2<\u0146\3\2\2\2>\u0148"+
		"\3\2\2\2@\u014a\3\2\2\2B\u014c\3\2\2\2D\u014e\3\2\2\2F\u0151\3\2\2\2H"+
		"\u0154\3\2\2\2J\u0156\3\2\2\2L\u0158\3\2\2\2N\u015a\3\2\2\2P\u015c\3\2"+
		"\2\2R\u015e\3\2\2\2T\u0160\3\2\2\2V\u0163\3\2\2\2X\u0165\3\2\2\2Z\u0167"+
		"\3\2\2\2\\\u016a\3\2\2\2^\u016d\3\2\2\2`\u016f\3\2\2\2b\u0171\3\2\2\2"+
		"d\u0175\3\2\2\2f\u0177\3\2\2\2h\u017f\3\2\2\2j\u0189\3\2\2\2l\u018b\3"+
		"\2\2\2n\u0190\3\2\2\2p\u0195\3\2\2\2r\u01a1\3\2\2\2t\u01a5\3\2\2\2v\u01ae"+
		"\3\2\2\2x\u01ba\3\2\2\2z\177\7}\2\2{~\5\30\13\2|~\5\36\16\2}{\3\2\2\2"+
		"}|\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080\u0082\3"+
		"\2\2\2\u0081\177\3\2\2\2\u0082\u0083\7h\2\2\u0083\u0084\7q\2\2\u0084\u0085"+
		"\7t\2\2\u0085\u0086\3\2\2\2\u0086\u0087\5\30\13\2\u0087\7\3\2\2\2\u0088"+
		"\u008d\7]\2\2\u0089\u008c\5\30\13\2\u008a\u008c\5\36\16\2\u008b\u0089"+
		"\3\2\2\2\u008b\u008a\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d"+
		"\u008e\3\2\2\2\u008e\u0090\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0091\7h"+
		"\2\2\u0091\u0092\7q\2\2\u0092\u0093\7t\2\2\u0093\u0094\3\2\2\2\u0094\u0095"+
		"\5\30\13\2\u0095\t\3\2\2\2\u0096\u0097\7k\2\2\u0097\u0098\7h\2\2\u0098"+
		"\13\3\2\2\2\u0099\u009a\7k\2\2\u009a\u009b\7p\2\2\u009b\r\3\2\2\2\u009c"+
		"\u009d\7}\2\2\u009d\u009e\b\6\2\2\u009e\17\3\2\2\2\u009f\u00a0\7\177\2"+
		"\2\u00a0\u00a1\b\7\3\2\u00a1\21\3\2\2\2\u00a2\u00a3\7?\2\2\u00a3\23\3"+
		"\2\2\2\u00a4\u00a7\n\2\2\2\u00a5\u00a7\5$\21\2\u00a6\u00a4\3\2\2\2\u00a6"+
		"\u00a5\3\2\2\2\u00a7\25\3\2\2\2\u00a8\u00ad\5\"\20\2\u00a9\u00ac\5 \17"+
		"\2\u00aa\u00ac\7/\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00aa\3\2\2\2\u00ac\u00af"+
		"\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\27\3\2\2\2\u00af"+
		"\u00ad\3\2\2\2\u00b0\u00b2\t\3\2\2\u00b1\u00b0\3\2\2\2\u00b2\u00b3\3\2"+
		"\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5"+
		"\u00b6\b\13\4\2\u00b6\31\3\2\2\2\u00b7\u00b8\7\61\2\2\u00b8\u00b9\7,\2"+
		"\2\u00b9\u00bd\3\2\2\2\u00ba\u00bc\13\2\2\2\u00bb\u00ba\3\2\2\2\u00bc"+
		"\u00bf\3\2\2\2\u00bd\u00be\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be\u00c0\3\2"+
		"\2\2\u00bf\u00bd\3\2\2\2\u00c0\u00c1\7,\2\2\u00c1\u00c2\7\61\2\2\u00c2"+
		"\u00c3\3\2\2\2\u00c3\u00c4\b\f\4\2\u00c4\33\3\2\2\2\u00c5\u00c6\7\61\2"+
		"\2\u00c6\u00c9\7\61\2\2\u00c7\u00c9\7%\2\2\u00c8\u00c5\3\2\2\2\u00c8\u00c7"+
		"\3\2\2\2\u00c9\u00cd\3\2\2\2\u00ca\u00cc\n\4\2\2\u00cb\u00ca\3\2\2\2\u00cc"+
		"\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00d1\3\2"+
		"\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00d2\7\17\2\2\u00d1\u00d0\3\2\2\2\u00d1"+
		"\u00d2\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d4\7\f\2\2\u00d4\u00d5\3\2"+
		"\2\2\u00d5\u00d6\b\r\4\2\u00d6\35\3\2\2\2\u00d7\u00d8\7\f\2\2\u00d8\u00d9"+
		"\3\2\2\2\u00d9\u00da\b\16\4\2\u00da\37\3\2\2\2\u00db\u00de\5\"\20\2\u00dc"+
		"\u00de\t\5\2\2\u00dd\u00db\3\2\2\2\u00dd\u00dc\3\2\2\2\u00de!\3\2\2\2"+
		"\u00df\u00e4\t\6\2\2\u00e0\u00e4\n\7\2\2\u00e1\u00e2\t\b\2\2\u00e2\u00e4"+
		"\t\t\2\2\u00e3\u00df\3\2\2\2\u00e3\u00e0\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e4"+
		"#\3\2\2\2\u00e5\u00e6\7^\2\2\u00e6\u00f8\t\n\2\2\u00e7\u00e8\7^\2\2\u00e8"+
		"\u00e9\5&\22\2\u00e9\u00ea\5&\22\2\u00ea\u00eb\5&\22\2\u00eb\u00ec\5&"+
		"\22\2\u00ec\u00f8\3\2\2\2\u00ed\u00ee\7^\2\2\u00ee\u00ef\5&\22\2\u00ef"+
		"\u00f0\5&\22\2\u00f0\u00f1\5&\22\2\u00f1\u00f2\5&\22\2\u00f2\u00f3\5&"+
		"\22\2\u00f3\u00f4\5&\22\2\u00f4\u00f5\5&\22\2\u00f5\u00f6\5&\22\2\u00f6"+
		"\u00f8\3\2\2\2\u00f7\u00e5\3\2\2\2\u00f7\u00e7\3\2\2\2\u00f7\u00ed\3\2"+
		"\2\2\u00f8%\3\2\2\2\u00f9\u00fa\t\13\2\2\u00fa\'\3\2\2\2\u00fb\u00fd\t"+
		"\5\2\2\u00fc\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe"+
		"\u00ff\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\u0104\7\60\2\2\u0101\u0103\t"+
		"\5\2\2\u0102\u0101\3\2\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2\2\2\u0104"+
		"\u0105\3\2\2\2\u0105\u0108\3\2\2\2\u0106\u0104\3\2\2\2\u0107\u0109\5*"+
		"\24\2\u0108\u0107\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u0116\3\2\2\2\u010a"+
		"\u010c\t\5\2\2\u010b\u010a\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010b\3\2"+
		"\2\2\u010d\u010e\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0116\5*\24\2\u0110"+
		"\u0112\t\5\2\2\u0111\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0111\3\2"+
		"\2\2\u0113\u0114\3\2\2\2\u0114\u0116\3\2\2\2\u0115\u00fc\3\2\2\2\u0115"+
		"\u010b\3\2\2\2\u0115\u0111\3\2\2\2\u0116)\3\2\2\2\u0117\u0119\t\f\2\2"+
		"\u0118\u011a\t\r\2\2\u0119\u0118\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011c"+
		"\3\2\2\2\u011b\u011d\t\5\2\2\u011c\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011e"+
		"\u011c\3\2\2\2\u011e\u011f\3\2\2\2\u011f+\3\2\2\2\u0120\u0121\7v\2\2\u0121"+
		"\u0122\7t\2\2\u0122\u0123\7w\2\2\u0123\u012a\7g\2\2\u0124\u0125\7h\2\2"+
		"\u0125\u0126\7c\2\2\u0126\u0127\7n\2\2\u0127\u0128\7u\2\2\u0128\u012a"+
		"\7g\2\2\u0129\u0120\3\2\2\2\u0129\u0124\3\2\2\2\u012a-\3\2\2\2\u012b\u012c"+
		"\7$\2\2\u012c\u012d\3\2\2\2\u012d\u012e\b\26\5\2\u012e/\3\2\2\2\u012f"+
		"\u0130\7p\2\2\u0130\u0131\7w\2\2\u0131\u0132\7n\2\2\u0132\u0133\7n\2\2"+
		"\u0133\61\3\2\2\2\u0134\u0135\7>\2\2\u0135\u0136\7>\2\2\u0136\u0138\3"+
		"\2\2\2\u0137\u0139\7/\2\2\u0138\u0137\3\2\2\2\u0138\u0139\3\2\2\2\u0139"+
		"\u013a\3\2\2\2\u013a\u013b\b\30\6\2\u013b\63\3\2\2\2\u013c\u013d\7-\2"+
		"\2\u013d\65\3\2\2\2\u013e\u013f\7(\2\2\u013f\u0140\7(\2\2\u0140\67\3\2"+
		"\2\2\u0141\u0142\7?\2\2\u0142\u0143\7?\2\2\u01439\3\2\2\2\u0144\u0145"+
		"\7>\2\2\u0145;\3\2\2\2\u0146\u0147\7<\2\2\u0147=\3\2\2\2\u0148\u0149\7"+
		"]\2\2\u0149?\3\2\2\2\u014a\u014b\7*\2\2\u014bA\3\2\2\2\u014c\u014d\7/"+
		"\2\2\u014dC\3\2\2\2\u014e\u014f\7~\2\2\u014f\u0150\7~\2\2\u0150E\3\2\2"+
		"\2\u0151\u0152\7#\2\2\u0152\u0153\7?\2\2\u0153G\3\2\2\2\u0154\u0155\7"+
		"@\2\2\u0155I\3\2\2\2\u0156\u0157\7A\2\2\u0157K\3\2\2\2\u0158\u0159\7_"+
		"\2\2\u0159M\3\2\2\2\u015a\u015b\7+\2\2\u015bO\3\2\2\2\u015c\u015d\7,\2"+
		"\2\u015dQ\3\2\2\2\u015e\u015f\7#\2\2\u015fS\3\2\2\2\u0160\u0161\7>\2\2"+
		"\u0161\u0162\7?\2\2\u0162U\3\2\2\2\u0163\u0164\7\60\2\2\u0164W\3\2\2\2"+
		"\u0165\u0166\7\61\2\2\u0166Y\3\2\2\2\u0167\u0168\7@\2\2\u0168\u0169\7"+
		"?\2\2\u0169[\3\2\2\2\u016a\u016b\7?\2\2\u016b\u016c\7@\2\2\u016c]\3\2"+
		"\2\2\u016d\u016e\7.\2\2\u016e_\3\2\2\2\u016f\u0170\7\'\2\2\u0170a\3\2"+
		"\2\2\u0171\u0172\7\60\2\2\u0172\u0173\7\60\2\2\u0173\u0174\7\60\2\2\u0174"+
		"c\3\2\2\2\u0175\u0176\7\u0080\2\2\u0176e\3\2\2\2\u0177\u0178\7&\2\2\u0178"+
		"\u0179\7}\2\2\u0179\u017a\3\2\2\2\u017a\u017b\b\62\7\2\u017b\u017c\3\2"+
		"\2\2\u017c\u017d\b\62\b\2\u017dg\3\2\2\2\u017e\u0180\5j\64\2\u017f\u017e"+
		"\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u017f\3\2\2\2\u0181\u0182\3\2\2\2\u0182"+
		"i\3\2\2\2\u0183\u018a\n\2\2\2\u0184\u0185\7&\2\2\u0185\u018a\n\16\2\2"+
		"\u0186\u0187\7\'\2\2\u0187\u018a\n\16\2\2\u0188\u018a\5$\21\2\u0189\u0183"+
		"\3\2\2\2\u0189\u0184\3\2\2\2\u0189\u0186\3\2\2\2\u0189\u0188\3\2\2\2\u018a"+
		"k\3\2\2\2\u018b\u018c\7$\2\2\u018c\u018d\3\2\2\2\u018d\u018e\b\65\t\2"+
		"\u018e\u018f\b\65\n\2\u018fm\3\2\2\2\u0190\u0191\7\f\2\2\u0191\u0192\3"+
		"\2\2\2\u0192\u0193\b\66\13\2\u0193\u0194\b\66\f\2\u0194o\3\2\2\2\u0195"+
		"\u019a\5\"\20\2\u0196\u0199\5 \17\2\u0197\u0199\7/\2\2\u0198\u0196\3\2"+
		"\2\2\u0198\u0197\3\2\2\2\u0199\u019c\3\2\2\2\u019a\u0198\3\2\2\2\u019a"+
		"\u019b\3\2\2\2\u019b\u019d\3\2\2\2\u019c\u019a\3\2\2\2\u019d\u019e\b\67"+
		"\r\2\u019e\u019f\3\2\2\2\u019f\u01a0\b\67\16\2\u01a0q\3\2\2\2\u01a1\u01a2"+
		"\7\f\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01a4\b8\13\2\u01a4s\3\2\2\2\u01a5"+
		"\u01a6\7&\2\2\u01a6\u01a7\7}\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9\b9\17"+
		"\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\b9\20\2\u01ab\u01ac\b9\b\2\u01acu\3"+
		"\2\2\2\u01ad\u01af\5x;\2\u01ae\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0"+
		"\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2\u01b3\b:"+
		"\21\2\u01b3w\3\2\2\2\u01b4\u01bb\n\17\2\2\u01b5\u01b6\7&\2\2\u01b6\u01bb"+
		"\n\16\2\2\u01b7\u01b8\7\'\2\2\u01b8\u01bb\n\16\2\2\u01b9\u01bb\5$\21\2"+
		"\u01ba\u01b4\3\2\2\2\u01ba\u01b5\3\2\2\2\u01ba\u01b7\3\2\2\2\u01ba\u01b9"+
		"\3\2\2\2\u01bby\3\2\2\2%\2\3\4\5}\177\u008b\u008d\u00a6\u00ab\u00ad\u00b3"+
		"\u00bd\u00c8\u00cd\u00d1\u00dd\u00e3\u00f7\u00fe\u0104\u0108\u010d\u0113"+
		"\u0115\u0119\u011e\u0129\u0138\u0181\u0189\u0198\u019a\u01b0\u01ba\22"+
		"\3\6\2\3\7\3\2\3\2\7\3\2\7\4\2\3\62\4\7\2\2\t\21\2\6\2\2\t\16\2\4\5\2"+
		"\3\67\5\t\n\2\39\6\t-\2\3:\7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}