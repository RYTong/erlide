package com.rytong.ui.editor.lua;

import org.antlr.runtime.*;

public class LuaLexer extends Lexer {
    public static final int T29=29;
    public static final int HexDigit=14;
    public static final int T36=36;
    public static final int T58=58;
    public static final int FLOAT=8;
    public static final int T35=35;
    public static final int T61=61;
    public static final int T45=45;
    public static final int T20=20;
    public static final int T34=34;
    public static final int NEWLINE=18;
    public static final int T64=64;
    public static final int T25=25;
    public static final int T37=37;
    public static final int EscapeSequence=11;
    public static final int INT=7;
    public static final int T26=26;
    public static final int T32=32;
    public static final int T51=51;
    public static final int T46=46;
    public static final int T38=38;
    public static final int T41=41;
    public static final int T24=24;
    public static final int T19=19;
    public static final int T39=39;
    public static final int T21=21;
    public static final int T62=62;
    public static final int T44=44;
    public static final int T55=55;
    public static final int T33=33;
    public static final int T22=22;
    public static final int T50=50;
    public static final int WS=17;
    public static final int STRING=5;
    public static final int T43=43;
    public static final int T23=23;
    public static final int T28=28;
    public static final int T42=42;
    public static final int T40=40;
    public static final int COMMENT=15;
    public static final int T63=63;
    public static final int T57=57;
    public static final int LINE_COMMENT=16;
    public static final int T65=65;
    public static final int EXP=9;
    public static final int T56=56;
    public static final int UnicodeEscape=12;
    public static final int T59=59;
    public static final int T48=48;
    public static final int T54=54;
    public static final int EOF=-1;
    public static final int T47=47;
    public static final int Tokens=66;
    public static final int T53=53;
    public static final int T60=60;
    public static final int T31=31;
    public static final int OctalEscape=13;
    public static final int T49=49;
    public static final int CHARSTRING=6;
    public static final int T27=27;
    public static final int T52=52;
    public static final int NAME=4;
    public static final int T30=30;
    public static final int HEX=10;
    public LuaLexer() {;} 
    public LuaLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g"; }

    // $ANTLR start T19
    public void mT19() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T19;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:4:7: ( ';' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:4:7: ';'
            {
            match(';'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T19

    // $ANTLR start T20
    public void mT20() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T20;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:5:7: ( '=' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:5:7: '='
            {
            match('='); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T20

    // $ANTLR start T21
    public void mT21() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T21;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:6:7: ( 'while' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:6:7: 'while'
            {
            match("while"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T21

    // $ANTLR start T22
    public void mT22() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T22;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:7:7: ( 'repeat' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:7:7: 'repeat'
            {
            match("repeat"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T22

    // $ANTLR start T23
    public void mT23() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T23;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:8:7: ( 'until' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:8:7: 'until'
            {
            match("until"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T23

    // $ANTLR start T24
    public void mT24() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T24;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:9:7: ( 'if' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:9:7: 'if'
            {
            match("if"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T24

    // $ANTLR start T25
    public void mT25() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T25;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:10:7: ( 'then' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:10:7: 'then'
            {
            match("then"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T25

    // $ANTLR start T26
    public void mT26() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T26;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:11:7: ( 'elseif' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:11:7: 'elseif'
            {
            match("elseif"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T26

    // $ANTLR start T27
    public void mT27() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T27;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:12:7: ( 'else' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:12:7: 'else'
            {
            match("else"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T27

    // $ANTLR start T28
    public void mT28() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T28;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:13:7: ( 'end' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:13:7: 'end'
            {
            match("end"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T28

    // $ANTLR start T29
    public void mT29() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T29;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:14:7: ( 'for' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:14:7: 'for'
            {
            match("for"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T29

    // $ANTLR start T30
    public void mT30() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T30;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:15:7: ( ',' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:15:7: ','
            {
            match(','); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T30

    // $ANTLR start T31
    public void mT31() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T31;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:16:7: ( 'in' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:16:7: 'in'
            {
            match("in"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T31

    // $ANTLR start T32
    public void mT32() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T32;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:17:7: ( 'do' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:17:7: 'do'
            {
            match("do"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T32

    // $ANTLR start T33
    public void mT33() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T33;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:18:7: ( 'local' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:18:7: 'local'
            {
            match("local"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T33

    // $ANTLR start T34
    public void mT34() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T34;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:19:7: ( 'function' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:19:7: 'function'
            {
            match("function"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T34

    // $ANTLR start T35
    public void mT35() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T35;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:20:7: ( 'return' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:20:7: 'return'
            {
            match("return"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T35

    // $ANTLR start T36
    public void mT36() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T36;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:21:7: ( 'break' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:21:7: 'break'
            {
            match("break"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T36

    // $ANTLR start T37
    public void mT37() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T37;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:22:7: ( '.' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:22:7: '.'
            {
            match('.'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T37

    // $ANTLR start T38
    public void mT38() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T38;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:23:7: ( ':' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:23:7: ':'
            {
            match(':'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T38

    // $ANTLR start T39
    public void mT39() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T39;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:24:7: ( 'nil' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:24:7: 'nil'
            {
            match("nil"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T39

    // $ANTLR start T40
    public void mT40() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T40;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:25:7: ( 'false' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:25:7: 'false'
            {
            match("false"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T40

    // $ANTLR start T41
    public void mT41() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T41;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:26:7: ( 'true' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:26:7: 'true'
            {
            match("true"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T41

    // $ANTLR start T42
    public void mT42() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T42;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:27:7: ( '...' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:27:7: '...'
            {
            match("..."); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T42

    // $ANTLR start T43
    public void mT43() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T43;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:28:7: ( '(' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:28:7: '('
            {
            match('('); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T43

    // $ANTLR start T44
    public void mT44() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T44;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:29:7: ( ')' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:29:7: ')'
            {
            match(')'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T44

    // $ANTLR start T45
    public void mT45() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T45;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:30:7: ( '[' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:30:7: '['
            {
            match('['); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T45

    // $ANTLR start T46
    public void mT46() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T46;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:31:7: ( ']' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:31:7: ']'
            {
            match(']'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T46

    // $ANTLR start T47
    public void mT47() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T47;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:32:7: ( '..' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:32:7: '..'
            {
            match(".."); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T47

    // $ANTLR start T48
    public void mT48() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T48;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:33:7: ( '{' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:33:7: '{'
            {
            match('{'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T48

    // $ANTLR start T49
    public void mT49() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T49;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:34:7: ( '}' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:34:7: '}'
            {
            match('}'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T49

    // $ANTLR start T50
    public void mT50() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T50;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:35:7: ( '+' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:35:7: '+'
            {
            match('+'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T50

    // $ANTLR start T51
    public void mT51() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T51;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:36:7: ( '-' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:36:7: '-'
            {
            match('-'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T51

    // $ANTLR start T52
    public void mT52() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T52;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:37:7: ( '*' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:37:7: '*'
            {
            match('*'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T52

    // $ANTLR start T53
    public void mT53() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T53;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:38:7: ( '/' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:38:7: '/'
            {
            match('/'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T53

    // $ANTLR start T54
    public void mT54() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T54;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:39:7: ( '^' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:39:7: '^'
            {
            match('^'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T54

    // $ANTLR start T55
    public void mT55() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T55;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:40:7: ( '%' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:40:7: '%'
            {
            match('%'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T55

    // $ANTLR start T56
    public void mT56() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T56;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:41:7: ( '<' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:41:7: '<'
            {
            match('<'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T56

    // $ANTLR start T57
    public void mT57() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T57;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:42:7: ( '<=' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:42:7: '<='
            {
            match("<="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T57

    // $ANTLR start T58
    public void mT58() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T58;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:43:7: ( '>' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:43:7: '>'
            {
            match('>'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T58

    // $ANTLR start T59
    public void mT59() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T59;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:44:7: ( '>=' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:44:7: '>='
            {
            match(">="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T59

    // $ANTLR start T60
    public void mT60() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T60;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:45:7: ( '==' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:45:7: '=='
            {
            match("=="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T60

    // $ANTLR start T61
    public void mT61() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T61;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:46:7: ( '~=' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:46:7: '~='
            {
            match("~="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T61

    // $ANTLR start T62
    public void mT62() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T62;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:7: ( 'and' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:7: 'and'
            {
            match("and"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T62

    // $ANTLR start T63
    public void mT63() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T63;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:48:7: ( 'or' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:48:7: 'or'
            {
            match("or"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T63

    // $ANTLR start T64
    public void mT64() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T64;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:49:7: ( 'not' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:49:7: 'not'
            {
            match("not"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T64

    // $ANTLR start T65
    public void mT65() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T65;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:50:7: ( '#' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:50:7: '#'
            {
            match('#'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T65

    // $ANTLR start NAME
    public void mNAME() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = NAME;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:189:7: ( ('a'..'z'|'A'..'Z'|'_') ( options {greedy=true; } : 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:189:7: ('a'..'z'|'A'..'Z'|'_') ( options {greedy=true; } : 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:189:30: ( options {greedy=true; } : 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop1:
            do {
                int alt1=5;
                switch ( input.LA(1) ) {
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    alt1=1;
                    break;
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                    alt1=2;
                    break;
                case '_':
                    alt1=3;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    alt1=4;
                    break;

                }

                switch (alt1) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:189:54: 'a' .. 'z'
            	    {
            	    matchRange('a','z'); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:189:63: 'A' .. 'Z'
            	    {
            	    matchRange('A','Z'); 

            	    }
            	    break;
            	case 3 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:189:72: '_'
            	    {
            	    match('_'); 

            	    }
            	    break;
            	case 4 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:189:76: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end NAME

    // $ANTLR start INT
    public void mINT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = INT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:192:7: ( ( '0' .. '9' )+ )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:192:7: ( '0' .. '9' )+
            {
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:192:7: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);
                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:192:8: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end INT

    // $ANTLR start FLOAT
    public void mFLOAT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = FLOAT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:194:9: ( INT '.' INT )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:194:9: INT '.' INT
            {
            mINT(); 
            match('.'); 
            mINT(); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end FLOAT

    // $ANTLR start EXP
    public void mEXP() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = EXP;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:196:7: ( FLOAT ('E'|'e') ( '-' )? INT )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:196:7: FLOAT ('E'|'e') ( '-' )? INT
            {
            mFLOAT(); 
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:196:23: ( '-' )?
            int alt3=2;
            int LA3_0 = input.LA(1);
            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:196:24: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            mINT(); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end EXP

    // $ANTLR start HEX
    public void mHEX() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = HEX;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:198:6: ( '0x' ( ('0'..'9'|'a'..'f'))+ )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:198:6: '0x' ( ('0'..'9'|'a'..'f'))+
            {
            match("0x"); 

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:198:11: ( ('0'..'9'|'a'..'f'))+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);
                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='a' && LA4_0<='f')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:198:12: ('0'..'9'|'a'..'f')
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end HEX

    // $ANTLR start STRING
    public void mSTRING() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = STRING;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:203:8: ( '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:203:8: '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"'
            {
            match('\"'); 
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:203:12: ( EscapeSequence | ~ ('\\\\'|'\"'))*
            loop5:
            do {
                int alt5=3;
                int LA5_0 = input.LA(1);
                if ( (LA5_0=='\\') ) {
                    alt5=1;
                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='!')||(LA5_0>='#' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFE')) ) {
                    alt5=2;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:203:14: EscapeSequence
            	    {
            	    mEscapeSequence(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:203:31: ~ ('\\\\'|'\"')
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('\"'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end STRING

    // $ANTLR start CHARSTRING
    public void mCHARSTRING() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = CHARSTRING;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:207:6: ( '\\'' ( EscapeSequence | ~ ('\\''|'\\\\'))* '\\'' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:207:6: '\\'' ( EscapeSequence | ~ ('\\''|'\\\\'))* '\\''
            {
            match('\''); 
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:207:11: ( EscapeSequence | ~ ('\\''|'\\\\'))*
            loop6:
            do {
                int alt6=3;
                int LA6_0 = input.LA(1);
                if ( (LA6_0=='\\') ) {
                    alt6=1;
                }
                else if ( ((LA6_0>='\u0000' && LA6_0<='&')||(LA6_0>='(' && LA6_0<='[')||(LA6_0>=']' && LA6_0<='\uFFFE')) ) {
                    alt6=2;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:207:13: EscapeSequence
            	    {
            	    mEscapeSequence(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:207:30: ~ ('\\''|'\\\\')
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            match('\''); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end CHARSTRING

    // $ANTLR start EscapeSequence
    public void mEscapeSequence() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:212:9: ( '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\') | UnicodeEscape | OctalEscape )
            int alt7=3;
            int LA7_0 = input.LA(1);
            if ( (LA7_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    alt7=1;
                    break;
                case 'u':
                    alt7=2;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    alt7=3;
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("210:1: fragment EscapeSequence : ( '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\') | UnicodeEscape | OctalEscape );", 7, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("210:1: fragment EscapeSequence : ( '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\') | UnicodeEscape | OctalEscape );", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:212:9: '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\')
                    {
                    match('\\'); 
                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:213:9: UnicodeEscape
                    {
                    mUnicodeEscape(); 

                    }
                    break;
                case 3 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:214:9: OctalEscape
                    {
                    mOctalEscape(); 

                    }
                    break;

            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end EscapeSequence

    // $ANTLR start OctalEscape
    public void mOctalEscape() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:9: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt8=3;
            int LA8_0 = input.LA(1);
            if ( (LA8_0=='\\') ) {
                int LA8_1 = input.LA(2);
                if ( ((LA8_1>='0' && LA8_1<='3')) ) {
                    int LA8_2 = input.LA(3);
                    if ( ((LA8_2>='0' && LA8_2<='7')) ) {
                        int LA8_4 = input.LA(4);
                        if ( ((LA8_4>='0' && LA8_4<='7')) ) {
                            alt8=1;
                        }
                        else {
                            alt8=2;}
                    }
                    else {
                        alt8=3;}
                }
                else if ( ((LA8_1>='4' && LA8_1<='7')) ) {
                    int LA8_3 = input.LA(3);
                    if ( ((LA8_3>='0' && LA8_3<='7')) ) {
                        alt8=2;
                    }
                    else {
                        alt8=3;}
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("217:1: fragment OctalEscape : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 8, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("217:1: fragment OctalEscape : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:14: ( '0' .. '3' )
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:15: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:25: ( '0' .. '7' )
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:36: ( '0' .. '7' )
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:219:37: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:220:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:220:14: ( '0' .. '7' )
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:220:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:220:25: ( '0' .. '7' )
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:220:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:221:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:221:14: ( '0' .. '7' )
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:221:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;

            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end OctalEscape

    // $ANTLR start UnicodeEscape
    public void mUnicodeEscape() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:226:9: ( '\\\\' 'u' HexDigit HexDigit HexDigit HexDigit )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:226:9: '\\\\' 'u' HexDigit HexDigit HexDigit HexDigit
            {
            match('\\'); 
            match('u'); 
            mHexDigit(); 
            mHexDigit(); 
            mHexDigit(); 
            mHexDigit(); 

            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end UnicodeEscape

    // $ANTLR start HexDigit
    public void mHexDigit() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:230:12: ( ('0'..'9'|'a'..'f'|'A'..'F'))
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:230:12: ('0'..'9'|'a'..'f'|'A'..'F')
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end HexDigit

    // $ANTLR start COMMENT
    public void mCOMMENT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = COMMENT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:234:9: ( '--[[' ( options {greedy=false; } : . )* ']]' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:234:9: '--[[' ( options {greedy=false; } : . )* ']]'
            {
            match("--[["); 

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:234:16: ( options {greedy=false; } : . )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);
                if ( (LA9_0==']') ) {
                    int LA9_1 = input.LA(2);
                    if ( (LA9_1==']') ) {
                        alt9=2;
                    }
                    else if ( ((LA9_1>='\u0000' && LA9_1<='\\')||(LA9_1>='^' && LA9_1<='\uFFFE')) ) {
                        alt9=1;
                    }


                }
                else if ( ((LA9_0>='\u0000' && LA9_0<='\\')||(LA9_0>='^' && LA9_0<='\uFFFE')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:234:44: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match("]]"); 

            skip();

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start LINE_COMMENT
    public void mLINE_COMMENT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = LINE_COMMENT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:238:7: ( '--' (~ ('\\n'|'\\r'))* ( '\\r' )? '\\n' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:238:7: '--' (~ ('\\n'|'\\r'))* ( '\\r' )? '\\n'
            {
            match("--"); 

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:238:12: (~ ('\\n'|'\\r'))*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);
                if ( ((LA10_0>='\u0000' && LA10_0<='\t')||(LA10_0>='\u000B' && LA10_0<='\f')||(LA10_0>='\u000E' && LA10_0<='\uFFFE')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:238:12: ~ ('\\n'|'\\r')
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:238:26: ( '\\r' )?
            int alt11=2;
            int LA11_0 = input.LA(1);
            if ( (LA11_0=='\r') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:238:26: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            skip();

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LINE_COMMENT

    // $ANTLR start WS
    public void mWS() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = WS;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:242:8: ( (' '|'\\t'|'\\u000C'))
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:242:8: (' '|'\\t'|'\\u000C')
            {
            if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            skip();

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end WS

    // $ANTLR start NEWLINE
    public void mNEWLINE() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = NEWLINE;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:245:11: ( ( '\\r' )? '\\n' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:245:11: ( '\\r' )? '\\n'
            {
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:245:11: ( '\\r' )?
            int alt12=2;
            int LA12_0 = input.LA(1);
            if ( (LA12_0=='\r') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:245:12: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            skip();

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end NEWLINE

    public void mTokens() throws RecognitionException {
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:10: ( T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | T45 | T46 | T47 | T48 | T49 | T50 | T51 | T52 | T53 | T54 | T55 | T56 | T57 | T58 | T59 | T60 | T61 | T62 | T63 | T64 | T65 | NAME | INT | FLOAT | EXP | HEX | STRING | CHARSTRING | COMMENT | LINE_COMMENT | WS | NEWLINE )
        int alt13=58;
        alt13 = dfa13.predict(input);
        switch (alt13) {
            case 1 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:10: T19
                {
                mT19(); 

                }
                break;
            case 2 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:14: T20
                {
                mT20(); 

                }
                break;
            case 3 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:18: T21
                {
                mT21(); 

                }
                break;
            case 4 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:22: T22
                {
                mT22(); 

                }
                break;
            case 5 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:26: T23
                {
                mT23(); 

                }
                break;
            case 6 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:30: T24
                {
                mT24(); 

                }
                break;
            case 7 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:34: T25
                {
                mT25(); 

                }
                break;
            case 8 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:38: T26
                {
                mT26(); 

                }
                break;
            case 9 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:42: T27
                {
                mT27(); 

                }
                break;
            case 10 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:46: T28
                {
                mT28(); 

                }
                break;
            case 11 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:50: T29
                {
                mT29(); 

                }
                break;
            case 12 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:54: T30
                {
                mT30(); 

                }
                break;
            case 13 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:58: T31
                {
                mT31(); 

                }
                break;
            case 14 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:62: T32
                {
                mT32(); 

                }
                break;
            case 15 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:66: T33
                {
                mT33(); 

                }
                break;
            case 16 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:70: T34
                {
                mT34(); 

                }
                break;
            case 17 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:74: T35
                {
                mT35(); 

                }
                break;
            case 18 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:78: T36
                {
                mT36(); 

                }
                break;
            case 19 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:82: T37
                {
                mT37(); 

                }
                break;
            case 20 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:86: T38
                {
                mT38(); 

                }
                break;
            case 21 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:90: T39
                {
                mT39(); 

                }
                break;
            case 22 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:94: T40
                {
                mT40(); 

                }
                break;
            case 23 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:98: T41
                {
                mT41(); 

                }
                break;
            case 24 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:102: T42
                {
                mT42(); 

                }
                break;
            case 25 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:106: T43
                {
                mT43(); 

                }
                break;
            case 26 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:110: T44
                {
                mT44(); 

                }
                break;
            case 27 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:114: T45
                {
                mT45(); 

                }
                break;
            case 28 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:118: T46
                {
                mT46(); 

                }
                break;
            case 29 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:122: T47
                {
                mT47(); 

                }
                break;
            case 30 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:126: T48
                {
                mT48(); 

                }
                break;
            case 31 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:130: T49
                {
                mT49(); 

                }
                break;
            case 32 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:134: T50
                {
                mT50(); 

                }
                break;
            case 33 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:138: T51
                {
                mT51(); 

                }
                break;
            case 34 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:142: T52
                {
                mT52(); 

                }
                break;
            case 35 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:146: T53
                {
                mT53(); 

                }
                break;
            case 36 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:150: T54
                {
                mT54(); 

                }
                break;
            case 37 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:154: T55
                {
                mT55(); 

                }
                break;
            case 38 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:158: T56
                {
                mT56(); 

                }
                break;
            case 39 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:162: T57
                {
                mT57(); 

                }
                break;
            case 40 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:166: T58
                {
                mT58(); 

                }
                break;
            case 41 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:170: T59
                {
                mT59(); 

                }
                break;
            case 42 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:174: T60
                {
                mT60(); 

                }
                break;
            case 43 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:178: T61
                {
                mT61(); 

                }
                break;
            case 44 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:182: T62
                {
                mT62(); 

                }
                break;
            case 45 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:186: T63
                {
                mT63(); 

                }
                break;
            case 46 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:190: T64
                {
                mT64(); 

                }
                break;
            case 47 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:194: T65
                {
                mT65(); 

                }
                break;
            case 48 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:198: NAME
                {
                mNAME(); 

                }
                break;
            case 49 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:203: INT
                {
                mINT(); 

                }
                break;
            case 50 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:207: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 51 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:213: EXP
                {
                mEXP(); 

                }
                break;
            case 52 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:217: HEX
                {
                mHEX(); 

                }
                break;
            case 53 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:221: STRING
                {
                mSTRING(); 

                }
                break;
            case 54 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:228: CHARSTRING
                {
                mCHARSTRING(); 

                }
                break;
            case 55 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:239: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 56 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:247: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;
            case 57 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:260: WS
                {
                mWS(); 

                }
                break;
            case 58 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:1:263: NEWLINE
                {
                mNEWLINE(); 

                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    public static final String DFA13_eotS =
        "\2\uffff\1\53\7\43\1\uffff\3\43\1\74\1\uffff\1\43\7\uffff\1\100"+
        "\4\uffff\1\102\1\104\1\uffff\2\43\2\uffff\2\110\6\uffff\3\43\1\116"+
        "\1\117\7\43\1\127\2\43\1\133\1\uffff\2\43\6\uffff\1\43\1\141\3\uffff"+
        "\4\43\2\uffff\2\43\1\151\3\43\1\155\1\uffff\2\43\2\uffff\1\160\1"+
        "\161\2\uffff\1\163\1\uffff\1\164\4\43\1\172\1\173\1\uffff\1\175"+
        "\2\43\1\uffff\2\43\6\uffff\1\u0086\2\43\1\u0089\2\uffff\1\43\1\uffff"+
        "\1\u008b\1\43\1\u008d\1\u008e\2\uffff\1\137\2\uffff\1\u0091\1\u0092"+
        "\1\uffff\1\u0093\1\uffff\1\43\2\uffff\1\u0090\4\uffff\1\43\1\u0096"+
        "\1\uffff";
    public static final String DFA13_eofS =
        "\u0097\uffff";
    public static final String DFA13_minS =
        "\1\11\1\uffff\1\75\1\150\1\145\1\156\1\146\1\150\1\154\1\141\1\uffff"+
        "\2\157\1\162\1\56\1\uffff\1\151\7\uffff\1\55\4\uffff\2\75\1\uffff"+
        "\1\156\1\162\2\uffff\2\56\6\uffff\1\151\1\160\1\164\2\60\1\165\1"+
        "\145\1\144\1\163\1\154\1\156\1\162\1\60\1\143\1\145\1\56\1\uffff"+
        "\1\164\1\154\1\0\5\uffff\1\144\1\60\2\uffff\1\60\1\154\1\165\1\145"+
        "\1\151\2\uffff\1\145\1\156\1\60\1\145\1\163\1\143\1\60\1\uffff\2"+
        "\141\2\uffff\2\60\1\0\1\uffff\1\60\1\uffff\1\60\1\145\1\162\1\141"+
        "\1\154\2\60\1\uffff\1\60\1\145\1\164\1\uffff\1\154\1\153\2\uffff"+
        "\1\0\3\uffff\1\60\1\156\1\164\1\60\2\uffff\1\146\1\uffff\1\60\1"+
        "\151\2\60\4\0\1\uffff\2\60\1\uffff\1\60\1\uffff\1\157\2\uffff\1"+
        "\0\4\uffff\1\156\1\60\1\uffff";
    public static final String DFA13_maxS =
        "\1\176\1\uffff\1\75\1\150\1\145\2\156\1\162\1\156\1\165\1\uffff"+
        "\2\157\1\162\1\56\1\uffff\1\157\7\uffff\1\55\4\uffff\2\75\1\uffff"+
        "\1\156\1\162\2\uffff\1\170\1\71\6\uffff\1\151\2\164\2\172\1\165"+
        "\1\145\1\144\1\163\1\154\1\156\1\162\1\172\1\143\1\145\1\56\1\uffff"+
        "\1\164\1\154\1\ufffe\5\uffff\1\144\1\172\2\uffff\1\71\1\154\1\165"+
        "\1\145\1\151\2\uffff\1\145\1\156\1\172\1\145\1\163\1\143\1\172\1"+
        "\uffff\2\141\2\uffff\2\172\1\ufffe\1\uffff\1\172\1\uffff\2\145\1"+
        "\162\1\141\1\154\2\172\1\uffff\1\172\1\145\1\164\1\uffff\1\154\1"+
        "\153\2\uffff\1\ufffe\3\uffff\1\172\1\156\1\164\1\172\2\uffff\1\146"+
        "\1\uffff\1\172\1\151\2\172\4\ufffe\1\uffff\2\172\1\uffff\1\172\1"+
        "\uffff\1\157\2\uffff\1\ufffe\4\uffff\1\156\1\172\1\uffff";
    public static final String DFA13_acceptS =
        "\1\uffff\1\1\10\uffff\1\14\4\uffff\1\24\1\uffff\1\31\1\32\1\33\1"+
        "\34\1\36\1\37\1\40\1\uffff\1\42\1\43\1\44\1\45\2\uffff\1\53\2\uffff"+
        "\1\57\1\60\2\uffff\1\65\1\66\1\71\1\72\1\52\1\2\20\uffff\1\23\3"+
        "\uffff\1\41\1\47\1\46\1\51\1\50\2\uffff\1\64\1\61\5\uffff\1\15\1"+
        "\6\7\uffff\1\16\2\uffff\1\30\1\35\3\uffff\1\70\1\uffff\1\55\7\uffff"+
        "\1\12\3\uffff\1\13\2\uffff\1\56\1\25\1\uffff\1\54\1\62\1\63\4\uffff"+
        "\1\27\1\7\1\uffff\1\11\10\uffff\1\3\2\uffff\1\5\1\uffff\1\26\1\uffff"+
        "\1\17\1\22\1\uffff\1\67\1\21\1\4\1\10\2\uffff\1\20";
    public static final String DFA13_specialS =
        "\u0097\uffff}>";
    public static final String[] DFA13_transition = {
        "\1\50\1\51\1\uffff\1\50\1\51\22\uffff\1\50\1\uffff\1\46\1\42\1\uffff"+
        "\1\34\1\uffff\1\47\1\21\1\22\1\31\1\27\1\12\1\30\1\16\1\32\1\44"+
        "\11\45\1\17\1\1\1\35\1\2\1\36\2\uffff\32\43\1\23\1\uffff\1\24\1"+
        "\33\1\43\1\uffff\1\40\1\15\1\43\1\13\1\10\1\11\2\43\1\6\2\43\1\14"+
        "\1\43\1\20\1\41\2\43\1\4\1\43\1\7\1\5\1\43\1\3\3\43\1\25\1\uffff"+
        "\1\26\1\37",
        "",
        "\1\52",
        "\1\54",
        "\1\55",
        "\1\56",
        "\1\60\7\uffff\1\57",
        "\1\62\11\uffff\1\61",
        "\1\64\1\uffff\1\63",
        "\1\65\15\uffff\1\67\5\uffff\1\66",
        "",
        "\1\70",
        "\1\71",
        "\1\72",
        "\1\73",
        "",
        "\1\76\5\uffff\1\75",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "\1\77",
        "",
        "",
        "",
        "",
        "\1\101",
        "\1\103",
        "",
        "\1\105",
        "\1\106",
        "",
        "",
        "\1\111\1\uffff\12\45\76\uffff\1\107",
        "\1\111\1\uffff\12\45",
        "",
        "",
        "",
        "",
        "",
        "",
        "\1\112",
        "\1\114\3\uffff\1\113",
        "\1\115",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\1\120",
        "\1\121",
        "\1\122",
        "\1\123",
        "\1\124",
        "\1\125",
        "\1\126",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\1\130",
        "\1\131",
        "\1\132",
        "",
        "\1\134",
        "\1\135",
        "\133\137\1\136\uffa3\137",
        "",
        "",
        "",
        "",
        "",
        "\1\140",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "",
        "",
        "\12\142",
        "\1\143",
        "\1\144",
        "\1\145",
        "\1\146",
        "",
        "",
        "\1\147",
        "\1\150",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\1\152",
        "\1\153",
        "\1\154",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "",
        "\1\156",
        "\1\157",
        "",
        "",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\133\137\1\162\uffa3\137",
        "",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "",
        "\12\142\13\uffff\1\165\37\uffff\1\165",
        "\1\166",
        "\1\167",
        "\1\170",
        "\1\171",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\10\43\1\174\21\43",
        "\1\176",
        "\1\177",
        "",
        "\1\u0080",
        "\1\u0081",
        "",
        "",
        "\12\u0085\1\u0084\2\u0085\1\u0083\117\u0085\1\u0082\uffa1\u0085",
        "",
        "",
        "",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\1\u0087",
        "\1\u0088",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "",
        "",
        "\1\u008a",
        "",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\1\u008c",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\12\u0085\1\u0084\2\u0085\1\u0083\117\u0085\1\u008f\uffa1\u0085",
        "\12\u0090\1\u0084\ufff4\u0090",
        "\uffff\u0090",
        "\12\u0085\1\u0084\2\u0085\1\u0083\117\u0085\1\u0082\uffa1\u0085",
        "",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        "",
        "\1\u0094",
        "",
        "",
        "\12\u0085\1\u0084\2\u0085\1\u0083\117\u0085\1\u008f\uffa1\u0085",
        "",
        "",
        "",
        "",
        "\1\u0095",
        "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
        ""
    };

    class DFA13 extends DFA {
        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA.unpackEncodedString(DFA13_eotS);
            this.eof = DFA.unpackEncodedString(DFA13_eofS);
            this.min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
            this.max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
            this.accept = DFA.unpackEncodedString(DFA13_acceptS);
            this.special = DFA.unpackEncodedString(DFA13_specialS);
            int numStates = DFA13_transition.length;
            this.transition = new short[numStates][];
            for (int i=0; i<numStates; i++) {
                transition[i] = DFA.unpackEncodedString(DFA13_transition[i]);
            }
        }
        public String getDescription() {
            return "1:1: Tokens : ( T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | T45 | T46 | T47 | T48 | T49 | T50 | T51 | T52 | T53 | T54 | T55 | T56 | T57 | T58 | T59 | T60 | T61 | T62 | T63 | T64 | T65 | NAME | INT | FLOAT | EXP | HEX | STRING | CHARSTRING | COMMENT | LINE_COMMENT | WS | NEWLINE );";
        }
    }
 

}