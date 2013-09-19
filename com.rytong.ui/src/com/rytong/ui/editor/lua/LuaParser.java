package com.rytong.ui.editor.lua;
import com.rytong.ui.editor.lua.model.*;


import org.antlr.runtime.*;
import java.util.HashMap;
public class LuaParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NAME", "STRING", "CHARSTRING", "INT", "FLOAT", "EXP", "HEX", "EscapeSequence", "UnicodeEscape", "OctalEscape", "HexDigit", "COMMENT", "LINE_COMMENT", "WS", "NEWLINE", "';'", "'='", "'while'", "'repeat'", "'until'", "'if'", "'then'", "'elseif'", "'else'", "'end'", "'for'", "','", "'in'", "'do'", "'local'", "'function'", "'return'", "'break'", "'.'", "':'", "'nil'", "'false'", "'true'", "'...'", "'('", "')'", "'['", "']'", "'..'", "'{'", "'}'", "'+'", "'-'", "'*'", "'/'", "'^'", "'%'", "'<'", "'<='", "'>'", "'>='", "'=='", "'~='", "'and'", "'or'", "'not'", "'#'"
    };
    public static final int UnicodeEscape=12;
    public static final int HexDigit=14;
    public static final int EscapeSequence=11;
    public static final int INT=7;
    public static final int WS=17;
    public static final int EOF=-1;
    public static final int STRING=5;
    public static final int FLOAT=8;
    public static final int OctalEscape=13;
    public static final int COMMENT=15;
    public static final int CHARSTRING=6;
    public static final int LINE_COMMENT=16;
    public static final int NAME=4;
    public static final int HEX=10;
    public static final int EXP=9;
    public static final int NEWLINE=18;

        public LuaParser(TokenStream input) {
            super(input);
            ruleMemo = new HashMap[82+1];
         }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "/Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g"; }


     
    public LuaDocument ld = new LuaDocument();



     public Section createSection(Token t) {
    		Section s = new Section();
    		s.setStart(t.getLine());
    		return s;
    	}

     public void endSection(Section s, Token t) {
    		s.setEnd(t.getLine());
    	}
    	



    // $ANTLR start start
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:35:1: start[LuaDocument ld] : chunk[ld] EOF ;
    public void start(LuaDocument ld) throws RecognitionException {   
        int start_StartIndex = input.index();
         this.ld = ld;
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:37:4: ( chunk[ld] EOF )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:37:4: chunk[ld] EOF
            {
            pushFollow(FOLLOW_chunk_in_start71);
            chunk(ld);
            _fsp--;
            if (failed) return ;
            match(input,EOF,FOLLOW_EOF_in_start74); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 1, start_StartIndex); }
            if ( backtracking==0 ) {
              /* System.out.println("Parsing done!"); */
            }
       }
        return ;
    }
    // $ANTLR end start


    // $ANTLR start block
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:40:1: block[Block currentBlock] : chunk[b] ;
    public void block(Block currentBlock) throws RecognitionException {   
        int block_StartIndex = input.index();
        Section s = new Section();Block b = new Block(); 
        				currentBlock.addSymbol(b);
        				b.setSection(s);
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 2) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:44:4: ( chunk[b] )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:44:4: chunk[b]
            {
            pushFollow(FOLLOW_chunk_in_block100);
            chunk(b);
            _fsp--;
            if (failed) return ;
            if ( backtracking==0 ) {
              s.setStart(input.LT(1).getLine());
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 2, block_StartIndex); }
            if ( backtracking==0 ) {
              s.setEnd(input.LT(-1).getLine());
            }
       }
        return ;
    }
    // $ANTLR end block


    // $ANTLR start chunk
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:46:1: chunk[Block block] : ( ( stat[block] ( ( ';' )=> ';' )? )=> stat[block] ( ( ';' )=> ';' )? )* ( ( laststat ( ( ';' )=> ';' )? )=> laststat ( ( ';' )=> ';' )? )? ;
    public void chunk(Block block) throws RecognitionException {   
        int chunk_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 3) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:4: ( ( ( stat[block] ( ( ';' )=> ';' )? )=> stat[block] ( ( ';' )=> ';' )? )* ( ( laststat ( ( ';' )=> ';' )? )=> laststat ( ( ';' )=> ';' )? )? )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:4: ( ( stat[block] ( ( ';' )=> ';' )? )=> stat[block] ( ( ';' )=> ';' )? )* ( ( laststat ( ( ';' )=> ';' )? )=> laststat ( ( ';' )=> ';' )? )?
            {
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:4: ( ( stat[block] ( ( ';' )=> ';' )? )=> stat[block] ( ( ';' )=> ';' )? )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);
                if ( (LA2_0==NAME||(LA2_0>=21 && LA2_0<=22)||LA2_0==24||LA2_0==29||(LA2_0>=32 && LA2_0<=34)||LA2_0==43) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:5: ( stat[block] ( ( ';' )=> ';' )? )=> stat[block] ( ( ';' )=> ';' )?
            	    {
            	    pushFollow(FOLLOW_stat_in_chunk113);
            	    stat(block);
            	    _fsp--;
            	    if (failed) return ;
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:17: ( ( ';' )=> ';' )?
            	    int alt1=2;
            	    int LA1_0 = input.LA(1);
            	    if ( (LA1_0==19) ) {
            	        alt1=1;
            	    }
            	    switch (alt1) {
            	        case 1 :
            	            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:18: ( ';' )=> ';'
            	            {
            	            match(input,19,FOLLOW_19_in_chunk117); if (failed) return ;

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:27: ( ( laststat ( ( ';' )=> ';' )? )=> laststat ( ( ';' )=> ';' )? )?
            int alt4=2;
            int LA4_0 = input.LA(1);
            if ( ((LA4_0>=35 && LA4_0<=36)) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:29: ( laststat ( ( ';' )=> ';' )? )=> laststat ( ( ';' )=> ';' )?
                    {
                    pushFollow(FOLLOW_laststat_in_chunk126);
                    laststat();
                    _fsp--;
                    if (failed) return ;
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:38: ( ( ';' )=> ';' )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);
                    if ( (LA3_0==19) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:47:39: ( ';' )=> ';'
                            {
                            match(input,19,FOLLOW_19_in_chunk129); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 3, chunk_StartIndex); }
       }
        return ;
    }
    // $ANTLR end chunk


    // $ANTLR start stat
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );
    public void stat(Block block) throws RecognitionException {   
        int stat_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 4) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:50:4: ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] )
            int alt8=11;
            switch ( input.LA(1) ) {
            case 43:
                switch ( input.LA(2) ) {
                case 39:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 10, input);

                        throw nvae;
                    }
                    break;
                case 40:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 11, input);

                        throw nvae;
                    }
                    break;
                case 41:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 12, input);

                        throw nvae;
                    }
                    break;
                case INT:
                case FLOAT:
                case EXP:
                case HEX:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 13, input);

                        throw nvae;
                    }
                    break;
                case STRING:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 14, input);

                        throw nvae;
                    }
                    break;
                case CHARSTRING:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 15, input);

                        throw nvae;
                    }
                    break;
                case 42:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 16, input);

                        throw nvae;
                    }
                    break;
                case 34:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 17, input);

                        throw nvae;
                    }
                    break;
                case 48:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 18, input);

                        throw nvae;
                    }
                    break;
                case 43:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 19, input);

                        throw nvae;
                    }
                    break;
                case NAME:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 20, input);

                        throw nvae;
                    }
                    break;
                case 51:
                case 64:
                case 65:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 21, input);

                        throw nvae;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 1, input);

                    throw nvae;
                }

                break;
            case NAME:
                switch ( input.LA(2) ) {
                case 45:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 22, input);

                        throw nvae;
                    }
                    break;
                case 37:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 23, input);

                        throw nvae;
                    }
                    break;
                case 38:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 24, input);

                        throw nvae;
                    }
                    break;
                case 43:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 25, input);

                        throw nvae;
                    }
                    break;
                case 48:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 26, input);

                        throw nvae;
                    }
                    break;
                case STRING:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 27, input);

                        throw nvae;
                    }
                    break;
                case CHARSTRING:
                    if ( (synpred5()) ) {
                        alt8=1;
                    }
                    else if ( (synpred6()) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 28, input);

                        throw nvae;
                    }
                    break;
                case 20:
                case 30:
                    alt8=1;
                    break;
                case EOF:
                case NAME:
                case 19:
                case 21:
                case 22:
                case 23:
                case 24:
                case 26:
                case 27:
                case 28:
                case 29:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                    alt8=2;
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 2, input);

                    throw nvae;
                }

                break;
            case 32:
                alt8=3;
                break;
            case 21:
                alt8=4;
                break;
            case 22:
                alt8=5;
                break;
            case 24:
                alt8=6;
                break;
            case 29:
                int LA8_7 = input.LA(2);
                if ( (LA8_7==NAME) ) {
                    if ( (synpred13()) ) {
                        alt8=7;
                    }
                    else if ( (synpred16()) ) {
                        alt8=8;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 47, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 7, input);

                    throw nvae;
                }
                break;
            case 34:
                alt8=9;
                break;
            case 33:
                int LA8_9 = input.LA(2);
                if ( (LA8_9==34) ) {
                    alt8=10;
                }
                else if ( (LA8_9==NAME) ) {
                    alt8=11;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 9, input);

                    throw nvae;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("49:1: stat[Block block] : ( ( varlist1 '=' explist1 )=> varlist1 '=' explist1 | ( functioncall )=> functioncall | ( doBlockEnd[block] )=> doBlockEnd[block] | ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block] | ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp | ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' | ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block] | ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block] | ( functionDeclaration[block] )=> functionDeclaration[block] | ( localFunctionDeclaration )=> localFunctionDeclaration | localVariableDeclaration[block] );", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:50:4: ( varlist1 '=' explist1 )=> varlist1 '=' explist1
                    {
                    pushFollow(FOLLOW_varlist1_in_stat143);
                    varlist1();
                    _fsp--;
                    if (failed) return ;
                    match(input,20,FOLLOW_20_in_stat145); if (failed) return ;
                    pushFollow(FOLLOW_explist1_in_stat147);
                    explist1();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:51:4: ( functioncall )=> functioncall
                    {
                    pushFollow(FOLLOW_functioncall_in_stat153);
                    functioncall();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:52:4: ( doBlockEnd[block] )=> doBlockEnd[block]
                    {
                    pushFollow(FOLLOW_doBlockEnd_in_stat159);
                    doBlockEnd(block);
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:53:4: ( 'while' exp doBlockEnd[block] )=> 'while' exp doBlockEnd[block]
                    {
                    match(input,21,FOLLOW_21_in_stat165); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_stat167);
                    exp();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_doBlockEnd_in_stat169);
                    doBlockEnd(block);
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:54:4: ( 'repeat' block[block] 'until' exp )=> 'repeat' block[block] 'until' exp
                    {
                    match(input,22,FOLLOW_22_in_stat175); if (failed) return ;
                    pushFollow(FOLLOW_block_in_stat177);
                    block(block);
                    _fsp--;
                    if (failed) return ;
                    match(input,23,FOLLOW_23_in_stat180); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_stat182);
                    exp();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:55:4: ( 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end' )=> 'if' exp 'then' block[block] ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )* ( ( 'else' block[block] )=> 'else' block[block] )? 'end'
                    {
                    match(input,24,FOLLOW_24_in_stat187); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_stat189);
                    exp();
                    _fsp--;
                    if (failed) return ;
                    match(input,25,FOLLOW_25_in_stat191); if (failed) return ;
                    pushFollow(FOLLOW_block_in_stat193);
                    block(block);
                    _fsp--;
                    if (failed) return ;
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:55:33: ( ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block] )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);
                        if ( (LA5_0==26) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:55:34: ( 'elseif' exp 'then' block[block] )=> 'elseif' exp 'then' block[block]
                    	    {
                    	    match(input,26,FOLLOW_26_in_stat197); if (failed) return ;
                    	    pushFollow(FOLLOW_exp_in_stat199);
                    	    exp();
                    	    _fsp--;
                    	    if (failed) return ;
                    	    match(input,25,FOLLOW_25_in_stat201); if (failed) return ;
                    	    pushFollow(FOLLOW_block_in_stat203);
                    	    block(block);
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:55:69: ( ( 'else' block[block] )=> 'else' block[block] )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);
                    if ( (LA6_0==27) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:55:70: ( 'else' block[block] )=> 'else' block[block]
                            {
                            match(input,27,FOLLOW_27_in_stat209); if (failed) return ;
                            pushFollow(FOLLOW_block_in_stat211);
                            block(block);
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,28,FOLLOW_28_in_stat216); if (failed) return ;

                    }
                    break;
                case 7 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:56:4: ( 'for' NAME '=' )=> 'for' NAME '=' exp ',' exp ( ( ',' exp )=> ',' exp )? doBlockEnd[block]
                    {
                    match(input,29,FOLLOW_29_in_stat231); if (failed) return ;
                    match(input,NAME,FOLLOW_NAME_in_stat233); if (failed) return ;
                    match(input,20,FOLLOW_20_in_stat235); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_stat237);
                    exp();
                    _fsp--;
                    if (failed) return ;
                    match(input,30,FOLLOW_30_in_stat239); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_stat241);
                    exp();
                    _fsp--;
                    if (failed) return ;
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:56:51: ( ( ',' exp )=> ',' exp )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);
                    if ( (LA7_0==30) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:56:52: ( ',' exp )=> ',' exp
                            {
                            match(input,30,FOLLOW_30_in_stat244); if (failed) return ;
                            pushFollow(FOLLOW_exp_in_stat246);
                            exp();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    pushFollow(FOLLOW_doBlockEnd_in_stat250);
                    doBlockEnd(block);
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 8 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:57:4: ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )=> 'for' namelist 'in' explist1 doBlockEnd[block]
                    {
                    match(input,29,FOLLOW_29_in_stat269); if (failed) return ;
                    pushFollow(FOLLOW_namelist_in_stat271);
                    namelist();
                    _fsp--;
                    if (failed) return ;
                    match(input,31,FOLLOW_31_in_stat273); if (failed) return ;
                    pushFollow(FOLLOW_explist1_in_stat275);
                    explist1();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_doBlockEnd_in_stat277);
                    doBlockEnd(block);
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 9 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:58:5: ( functionDeclaration[block] )=> functionDeclaration[block]
                    {
                    pushFollow(FOLLOW_functionDeclaration_in_stat284);
                    functionDeclaration(block);
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 10 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:59:5: ( localFunctionDeclaration )=> localFunctionDeclaration
                    {
                    pushFollow(FOLLOW_localFunctionDeclaration_in_stat291);
                    localFunctionDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 11 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:60:5: localVariableDeclaration[block]
                    {
                    pushFollow(FOLLOW_localVariableDeclaration_in_stat297);
                    localVariableDeclaration(block);
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 4, stat_StartIndex); }
       }
        return ;
    }
    // $ANTLR end stat


    // $ANTLR start doBlockEnd
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:64:1: doBlockEnd[Block currentBlock] : 'do' block[b] 'end' ;
    public void doBlockEnd(Block currentBlock) throws RecognitionException {   
        int doBlockEnd_StartIndex = input.index();
        Block b = new Block(); currentBlock.addSymbol(b);
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 5) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:66:4: ( 'do' block[b] 'end' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:66:4: 'do' block[b] 'end'
            {
            match(input,32,FOLLOW_32_in_doBlockEnd317); if (failed) return ;
            pushFollow(FOLLOW_block_in_doBlockEnd322);
            block(b);
            _fsp--;
            if (failed) return ;
            match(input,28,FOLLOW_28_in_doBlockEnd328); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 5, doBlockEnd_StartIndex); }
       }
        return ;
    }
    // $ANTLR end doBlockEnd


    // $ANTLR start localVariableDeclaration
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:70:1: localVariableDeclaration[Block block] : 'local' namelist ( ( '=' explist1 )=> '=' explist1 )? ;
    public void localVariableDeclaration(Block block) throws RecognitionException {   
        int localVariableDeclaration_StartIndex = input.index();
        explist1_return explist11 = null;

        namelist_return namelist2 = null;


        Section s = null;String explist = null;
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 6) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:73:4: ( 'local' namelist ( ( '=' explist1 )=> '=' explist1 )? )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:73:4: 'local' namelist ( ( '=' explist1 )=> '=' explist1 )?
            {
            match(input,33,FOLLOW_33_in_localVariableDeclaration348); if (failed) return ;
            if ( backtracking==0 ) {
              s = new Section(); s.setStart(input.LT(1).getLine());
            }
            pushFollow(FOLLOW_namelist_in_localVariableDeclaration354);
            namelist2=namelist();
            _fsp--;
            if (failed) return ;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:74:12: ( ( '=' explist1 )=> '=' explist1 )?
            int alt9=2;
            int LA9_0 = input.LA(1);
            if ( (LA9_0==20) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:74:13: ( '=' explist1 )=> '=' explist1
                    {
                    match(input,20,FOLLOW_20_in_localVariableDeclaration357); if (failed) return ;
                    pushFollow(FOLLOW_explist1_in_localVariableDeclaration359);
                    explist11=explist1();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                      explist =  input.toString(explist11.start,explist11.stop);
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {

              					   String[] varlist = input.toString(namelist2.start,namelist2.stop).split(",");
              					   String[] exparray = {null};
              					   if(explist != null){
              					   	exparray = explist.split(",");
              					   }
              					   for(int i=0; i<varlist.length;i++){
              					   	Symbol sym = null;
              					   	if(explist != null && varlist.length == exparray.length && exparray[i].startsWith("function(")){
              					   		sym = new Function(varlist[i], true);
              					   	}else{
              							sym = new Variable(varlist[i], true);
              						}
              						sym.setSection(s);
              						block.addSymbol(sym);
              					   }
              					  
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 6, localVariableDeclaration_StartIndex); }
            if ( backtracking==0 ) {
               s.setEnd(input.LT(-1).getLine());
            }
       }
        return ;
    }
    // $ANTLR end localVariableDeclaration


    // $ANTLR start localFunctionDeclaration
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:93:1: localFunctionDeclaration : 'local' 'function' NAME funcbody[function] 'end' ;
    public void localFunctionDeclaration() throws RecognitionException {   
        int localFunctionDeclaration_StartIndex = input.index();
        Function function = new Function();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 7) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:94:4: ( 'local' 'function' NAME funcbody[function] 'end' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:94:4: 'local' 'function' NAME funcbody[function] 'end'
            {
            match(input,33,FOLLOW_33_in_localFunctionDeclaration381); if (failed) return ;
            match(input,34,FOLLOW_34_in_localFunctionDeclaration383); if (failed) return ;
            match(input,NAME,FOLLOW_NAME_in_localFunctionDeclaration385); if (failed) return ;
            pushFollow(FOLLOW_funcbody_in_localFunctionDeclaration387);
            funcbody(function);
            _fsp--;
            if (failed) return ;
            match(input,28,FOLLOW_28_in_localFunctionDeclaration390); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 7, localFunctionDeclaration_StartIndex); }
       }
        return ;
    }
    // $ANTLR end localFunctionDeclaration


    // $ANTLR start laststat
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:98:1: laststat : ( ( 'return' ( ( explist1 )=> explist1 )? )=> 'return' ( ( explist1 )=> explist1 )? | 'break' );
    public void laststat() throws RecognitionException {   
        int laststat_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 8) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:98:12: ( ( 'return' ( ( explist1 )=> explist1 )? )=> 'return' ( ( explist1 )=> explist1 )? | 'break' )
            int alt11=2;
            int LA11_0 = input.LA(1);
            if ( (LA11_0==35) ) {
                alt11=1;
            }
            else if ( (LA11_0==36) ) {
                alt11=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("98:1: laststat : ( ( 'return' ( ( explist1 )=> explist1 )? )=> 'return' ( ( explist1 )=> explist1 )? | 'break' );", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:98:12: ( 'return' ( ( explist1 )=> explist1 )? )=> 'return' ( ( explist1 )=> explist1 )?
                    {
                    match(input,35,FOLLOW_35_in_laststat401); if (failed) return ;
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:98:21: ( ( explist1 )=> explist1 )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);
                    if ( ((LA10_0>=NAME && LA10_0<=HEX)||LA10_0==34||(LA10_0>=39 && LA10_0<=43)||LA10_0==48||LA10_0==51||(LA10_0>=64 && LA10_0<=65)) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:98:22: ( explist1 )=> explist1
                            {
                            pushFollow(FOLLOW_explist1_in_laststat404);
                            explist1();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:99:5: 'break'
                    {
                    match(input,36,FOLLOW_36_in_laststat413); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 8, laststat_StartIndex); }
       }
        return ;
    }
    // $ANTLR end laststat


    // $ANTLR start functionDeclaration
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:102:1: functionDeclaration[Block block] : 'function' funcname funcbody[function] 'end' ;
    public void functionDeclaration(Block block) throws RecognitionException {   
        int functionDeclaration_StartIndex = input.index();
        funcname_return funcname3 = null;


        Function function = null; Section s = null;
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 9) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:105:6: ( 'function' funcname funcbody[function] 'end' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:105:6: 'function' funcname funcbody[function] 'end'
            {
            if ( backtracking==0 ) {
              s = new Section();
              	   		   s.setStart(input.LT(1).getLine());
            }
            match(input,34,FOLLOW_34_in_functionDeclaration446); if (failed) return ;
            pushFollow(FOLLOW_funcname_in_functionDeclaration448);
            funcname3=funcname();
            _fsp--;
            if (failed) return ;
            if ( backtracking==0 ) {
              function = new Function(); function.setName(input.toString(funcname3.start,funcname3.stop));function.setSection(s);
            }
            pushFollow(FOLLOW_funcbody_in_functionDeclaration454);
            funcbody(function);
            _fsp--;
            if (failed) return ;
            match(input,28,FOLLOW_28_in_functionDeclaration457); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 9, functionDeclaration_StartIndex); }
            if ( backtracking==0 ) {
               s.setEnd(input.LT(-1).getLine()); block.addSymbol(function);
            }
       }
        return ;
    }
    // $ANTLR end functionDeclaration

    public static class funcname_return extends ParserRuleReturnScope {
    };

    // $ANTLR start funcname
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:111:1: funcname : NAME ( ( '.' NAME )=> '.' NAME )* ( ( ':' NAME )=> ':' NAME )? ;
    public funcname_return funcname() throws RecognitionException {   
        funcname_return retval = new funcname_return();
        retval.start = input.LT(1);
        int funcname_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:111:12: ( NAME ( ( '.' NAME )=> '.' NAME )* ( ( ':' NAME )=> ':' NAME )? )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:111:12: NAME ( ( '.' NAME )=> '.' NAME )* ( ( ':' NAME )=> ':' NAME )?
            {
            match(input,NAME,FOLLOW_NAME_in_funcname468); if (failed) return retval;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:111:17: ( ( '.' NAME )=> '.' NAME )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);
                if ( (LA12_0==37) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:111:18: ( '.' NAME )=> '.' NAME
            	    {
            	    match(input,37,FOLLOW_37_in_funcname471); if (failed) return retval;
            	    match(input,NAME,FOLLOW_NAME_in_funcname473); if (failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:111:29: ( ( ':' NAME )=> ':' NAME )?
            int alt13=2;
            int LA13_0 = input.LA(1);
            if ( (LA13_0==38) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:111:31: ( ':' NAME )=> ':' NAME
                    {
                    match(input,38,FOLLOW_38_in_funcname479); if (failed) return retval;
                    match(input,NAME,FOLLOW_NAME_in_funcname481); if (failed) return retval;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);
            if ( backtracking>0 ) { memoize(input, 10, funcname_StartIndex); }
       }
        return retval;
    }
    // $ANTLR end funcname


    // $ANTLR start varlist1
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:114:1: varlist1 : var ( ( ',' var )=> ',' var )* ;
    public void varlist1() throws RecognitionException {   
        int varlist1_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 11) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:114:12: ( var ( ( ',' var )=> ',' var )* )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:114:12: var ( ( ',' var )=> ',' var )*
            {
            pushFollow(FOLLOW_var_in_varlist1494);
            var();
            _fsp--;
            if (failed) return ;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:114:16: ( ( ',' var )=> ',' var )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);
                if ( (LA14_0==30) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:114:17: ( ',' var )=> ',' var
            	    {
            	    match(input,30,FOLLOW_30_in_varlist1497); if (failed) return ;
            	    pushFollow(FOLLOW_var_in_varlist1499);
            	    var();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 11, varlist1_StartIndex); }
       }
        return ;
    }
    // $ANTLR end varlist1

    public static class namelist_return extends ParserRuleReturnScope {
    };

    // $ANTLR start namelist
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:117:1: namelist : NAME ( ( ',' NAME )=> ',' NAME )* ;
    public namelist_return namelist() throws RecognitionException {   
        namelist_return retval = new namelist_return();
        retval.start = input.LT(1);
        int namelist_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:117:12: ( NAME ( ( ',' NAME )=> ',' NAME )* )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:117:12: NAME ( ( ',' NAME )=> ',' NAME )*
            {
            match(input,NAME,FOLLOW_NAME_in_namelist512); if (failed) return retval;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:117:17: ( ( ',' NAME )=> ',' NAME )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);
                if ( (LA15_0==30) ) {
                    int LA15_20 = input.LA(2);
                    if ( (LA15_20==NAME) ) {
                        alt15=1;
                    }


                }


                switch (alt15) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:117:18: ( ',' NAME )=> ',' NAME
            	    {
            	    match(input,30,FOLLOW_30_in_namelist515); if (failed) return retval;
            	    match(input,NAME,FOLLOW_NAME_in_namelist517); if (failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);
            if ( backtracking>0 ) { memoize(input, 12, namelist_StartIndex); }
       }
        return retval;
    }
    // $ANTLR end namelist

    public static class explist1_return extends ParserRuleReturnScope {
    };

    // $ANTLR start explist1
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:120:1: explist1 : exp ( ( ',' exp )=> ',' exp )* ;
    public explist1_return explist1() throws RecognitionException {   
        explist1_return retval = new explist1_return();
        retval.start = input.LT(1);
        int explist1_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:120:12: ( exp ( ( ',' exp )=> ',' exp )* )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:120:12: exp ( ( ',' exp )=> ',' exp )*
            {
            pushFollow(FOLLOW_exp_in_explist1533);
            exp();
            _fsp--;
            if (failed) return retval;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:120:16: ( ( ',' exp )=> ',' exp )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);
                if ( (LA16_0==30) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:120:17: ( ',' exp )=> ',' exp
            	    {
            	    match(input,30,FOLLOW_30_in_explist1536); if (failed) return retval;
            	    pushFollow(FOLLOW_exp_in_explist1538);
            	    exp();
            	    _fsp--;
            	    if (failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);
            if ( backtracking>0 ) { memoize(input, 13, explist1_StartIndex); }
       }
        return retval;
    }
    // $ANTLR end explist1


    // $ANTLR start exp
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:123:1: exp : ( ( 'nil' )=> 'nil' | ( 'false' )=> 'false' | ( 'true' )=> 'true' | ( number )=> number | ( STRING )=> STRING | ( CHARSTRING )=> CHARSTRING | ( '...' )=> '...' | ( function )=> function | ( tableconstructor )=> tableconstructor | ( prefixexp )=> prefixexp | unop exp ) ( ( binop exp )=> binop exp )* ;
    public void exp() throws RecognitionException {   
        int exp_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 14) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:124:5: ( ( ( 'nil' )=> 'nil' | ( 'false' )=> 'false' | ( 'true' )=> 'true' | ( number )=> number | ( STRING )=> STRING | ( CHARSTRING )=> CHARSTRING | ( '...' )=> '...' | ( function )=> function | ( tableconstructor )=> tableconstructor | ( prefixexp )=> prefixexp | unop exp ) ( ( binop exp )=> binop exp )* )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:124:5: ( ( 'nil' )=> 'nil' | ( 'false' )=> 'false' | ( 'true' )=> 'true' | ( number )=> number | ( STRING )=> STRING | ( CHARSTRING )=> CHARSTRING | ( '...' )=> '...' | ( function )=> function | ( tableconstructor )=> tableconstructor | ( prefixexp )=> prefixexp | unop exp ) ( ( binop exp )=> binop exp )*
            {
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:124:5: ( ( 'nil' )=> 'nil' | ( 'false' )=> 'false' | ( 'true' )=> 'true' | ( number )=> number | ( STRING )=> STRING | ( CHARSTRING )=> CHARSTRING | ( '...' )=> '...' | ( function )=> function | ( tableconstructor )=> tableconstructor | ( prefixexp )=> prefixexp | unop exp )
            int alt17=11;
            switch ( input.LA(1) ) {
            case 39:
                alt17=1;
                break;
            case 40:
                alt17=2;
                break;
            case 41:
                alt17=3;
                break;
            case INT:
            case FLOAT:
            case EXP:
            case HEX:
                alt17=4;
                break;
            case STRING:
                alt17=5;
                break;
            case CHARSTRING:
                alt17=6;
                break;
            case 42:
                alt17=7;
                break;
            case 34:
                alt17=8;
                break;
            case 48:
                alt17=9;
                break;
            case NAME:
            case 43:
                alt17=10;
                break;
            case 51:
            case 64:
            case 65:
                alt17=11;
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("124:5: ( ( 'nil' )=> 'nil' | ( 'false' )=> 'false' | ( 'true' )=> 'true' | ( number )=> number | ( STRING )=> STRING | ( CHARSTRING )=> CHARSTRING | ( '...' )=> '...' | ( function )=> function | ( tableconstructor )=> tableconstructor | ( prefixexp )=> prefixexp | unop exp )", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:125:5: ( 'nil' )=> 'nil'
                    {
                    match(input,39,FOLLOW_39_in_exp562); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:126:5: ( 'false' )=> 'false'
                    {
                    match(input,40,FOLLOW_40_in_exp569); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:127:5: ( 'true' )=> 'true'
                    {
                    match(input,41,FOLLOW_41_in_exp576); if (failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:128:6: ( number )=> number
                    {
                    pushFollow(FOLLOW_number_in_exp584);
                    number();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:129:6: ( STRING )=> STRING
                    {
                    match(input,STRING,FOLLOW_STRING_in_exp592); if (failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:130:5: ( CHARSTRING )=> CHARSTRING
                    {
                    match(input,CHARSTRING,FOLLOW_CHARSTRING_in_exp599); if (failed) return ;

                    }
                    break;
                case 7 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:131:5: ( '...' )=> '...'
                    {
                    match(input,42,FOLLOW_42_in_exp605); if (failed) return ;

                    }
                    break;
                case 8 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:132:5: ( function )=> function
                    {
                    pushFollow(FOLLOW_function_in_exp611);
                    function();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 9 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:133:12: ( tableconstructor )=> tableconstructor
                    {
                    pushFollow(FOLLOW_tableconstructor_in_exp624);
                    tableconstructor();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 10 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:134:12: ( prefixexp )=> prefixexp
                    {
                    pushFollow(FOLLOW_prefixexp_in_exp637);
                    prefixexp();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 11 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:135:6: unop exp
                    {
                    pushFollow(FOLLOW_unop_in_exp644);
                    unop();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_exp_in_exp646);
                    exp();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:135:16: ( ( binop exp )=> binop exp )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);
                if ( (LA18_0==47||(LA18_0>=50 && LA18_0<=63)) ) {
                    if ( (synpred37()) ) {
                        alt18=1;
                    }


                }


                switch (alt18) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:135:17: ( binop exp )=> binop exp
            	    {
            	    pushFollow(FOLLOW_binop_in_exp650);
            	    binop();
            	    _fsp--;
            	    if (failed) return ;
            	    pushFollow(FOLLOW_exp_in_exp652);
            	    exp();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 14, exp_StartIndex); }
       }
        return ;
    }
    // $ANTLR end exp

    public static class prefixexp_return extends ParserRuleReturnScope {
    };

    // $ANTLR start prefixexp
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:138:1: prefixexp : ( ( '(' exp ')' )=> '(' exp ')' | NAME ) ( ( '[' exp ']' )=> '[' exp ']' | ( '.' NAME )=> '.' NAME | ( ( ( ':' NAME )=> ':' NAME )? args )=> ( ( ':' NAME )=> ':' NAME )? args )* ;
    public prefixexp_return prefixexp() throws RecognitionException {   
        prefixexp_return retval = new prefixexp_return();
        retval.start = input.LT(1);
        int prefixexp_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 15) ) { return retval; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:139:7: ( ( ( '(' exp ')' )=> '(' exp ')' | NAME ) ( ( '[' exp ']' )=> '[' exp ']' | ( '.' NAME )=> '.' NAME | ( ( ( ':' NAME )=> ':' NAME )? args )=> ( ( ':' NAME )=> ':' NAME )? args )* )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:139:7: ( ( '(' exp ')' )=> '(' exp ')' | NAME ) ( ( '[' exp ']' )=> '[' exp ']' | ( '.' NAME )=> '.' NAME | ( ( ( ':' NAME )=> ':' NAME )? args )=> ( ( ':' NAME )=> ':' NAME )? args )*
            {
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:139:7: ( ( '(' exp ')' )=> '(' exp ')' | NAME )
            int alt19=2;
            int LA19_0 = input.LA(1);
            if ( (LA19_0==43) ) {
                alt19=1;
            }
            else if ( (LA19_0==NAME) ) {
                alt19=2;
            }
            else {
                if (backtracking>0) {failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("139:7: ( ( '(' exp ')' )=> '(' exp ')' | NAME )", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:139:9: ( '(' exp ')' )=> '(' exp ')'
                    {
                    match(input,43,FOLLOW_43_in_prefixexp669); if (failed) return retval;
                    pushFollow(FOLLOW_exp_in_prefixexp670);
                    exp();
                    _fsp--;
                    if (failed) return retval;
                    match(input,44,FOLLOW_44_in_prefixexp671); if (failed) return retval;

                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:139:21: NAME
                    {
                    match(input,NAME,FOLLOW_NAME_in_prefixexp675); if (failed) return retval;

                    }
                    break;

            }

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:9: ( ( '[' exp ']' )=> '[' exp ']' | ( '.' NAME )=> '.' NAME | ( ( ( ':' NAME )=> ':' NAME )? args )=> ( ( ':' NAME )=> ':' NAME )? args )*
            loop21:
            do {
                int alt21=4;
                switch ( input.LA(1) ) {
                case 43:
                    switch ( input.LA(2) ) {
                    case 39:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 40:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 41:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case INT:
                    case FLOAT:
                    case EXP:
                    case HEX:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case STRING:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case CHARSTRING:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 42:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 34:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 48:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 43:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case NAME:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 51:
                    case 64:
                    case 65:
                        if ( (synpred42()) ) {
                            alt21=3;
                        }


                        break;
                    case 44:
                        alt21=3;
                        break;

                    }

                    break;
                case 45:
                    alt21=1;
                    break;
                case 37:
                    alt21=2;
                    break;
                case STRING:
                case CHARSTRING:
                case 38:
                case 48:
                    alt21=3;
                    break;

                }

                switch (alt21) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:11: ( '[' exp ']' )=> '[' exp ']'
            	    {
            	    match(input,45,FOLLOW_45_in_prefixexp689); if (failed) return retval;
            	    pushFollow(FOLLOW_exp_in_prefixexp690);
            	    exp();
            	    _fsp--;
            	    if (failed) return retval;
            	    match(input,46,FOLLOW_46_in_prefixexp691); if (failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:23: ( '.' NAME )=> '.' NAME
            	    {
            	    match(input,37,FOLLOW_37_in_prefixexp695); if (failed) return retval;
            	    match(input,NAME,FOLLOW_NAME_in_prefixexp697); if (failed) return retval;

            	    }
            	    break;
            	case 3 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:34: ( ( ( ':' NAME )=> ':' NAME )? args )=> ( ( ':' NAME )=> ':' NAME )? args
            	    {
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:34: ( ( ':' NAME )=> ':' NAME )?
            	    int alt20=2;
            	    int LA20_0 = input.LA(1);
            	    if ( (LA20_0==38) ) {
            	        alt20=1;
            	    }
            	    switch (alt20) {
            	        case 1 :
            	            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:35: ( ':' NAME )=> ':' NAME
            	            {
            	            match(input,38,FOLLOW_38_in_prefixexp702); if (failed) return retval;
            	            match(input,NAME,FOLLOW_NAME_in_prefixexp704); if (failed) return retval;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_args_in_prefixexp708);
            	    args();
            	    _fsp--;
            	    if (failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);
            if ( backtracking>0 ) { memoize(input, 15, prefixexp_StartIndex); }
       }
        return retval;
    }
    // $ANTLR end prefixexp


    // $ANTLR start var
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:143:1: var : prefixexp ;
    public void var() throws RecognitionException {   
        int var_StartIndex = input.index();
        prefixexp_return prefixexp4 = null;


        Variable var = null; /*Section s = new Section();s.setStart(input.LT(1).getLine());*/
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 16) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:145:3: ( prefixexp )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:145:3: prefixexp
            {
            pushFollow(FOLLOW_prefixexp_in_var736);
            prefixexp4=prefixexp();
            _fsp--;
            if (failed) return ;
            if ( backtracking==0 ) {
              var = new Variable(input.toString(prefixexp4.start,prefixexp4.stop), false);/*currentBlock.addSymbol(var);var.setSection(s);*/
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 16, var_StartIndex); }
            if ( backtracking==0 ) {
              /* s.setEnd(input.LT(-1).getLine());*/
            }
       }
        return ;
    }
    // $ANTLR end var


    // $ANTLR start functioncall
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:148:1: functioncall : prefixexp ;
    public void functioncall() throws RecognitionException {   
        int functioncall_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 17) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:149:4: ( prefixexp )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:149:4: prefixexp
            {
            pushFollow(FOLLOW_prefixexp_in_functioncall749);
            prefixexp();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 17, functioncall_StartIndex); }
       }
        return ;
    }
    // $ANTLR end functioncall


    // $ANTLR start args
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:1: args : ( ( '(' ( ( explist1 )=> explist1 )? ')' )=> '(' ( ( explist1 )=> explist1 )? ')' | ( tableconstructor )=> tableconstructor | ( STRING )=> STRING | CHARSTRING );
    public void args() throws RecognitionException {   
        int args_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 18) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:8: ( ( '(' ( ( explist1 )=> explist1 )? ')' )=> '(' ( ( explist1 )=> explist1 )? ')' | ( tableconstructor )=> tableconstructor | ( STRING )=> STRING | CHARSTRING )
            int alt23=4;
            switch ( input.LA(1) ) {
            case 43:
                alt23=1;
                break;
            case 48:
                alt23=2;
                break;
            case STRING:
                alt23=3;
                break;
            case CHARSTRING:
                alt23=4;
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("152:1: args : ( ( '(' ( ( explist1 )=> explist1 )? ')' )=> '(' ( ( explist1 )=> explist1 )? ')' | ( tableconstructor )=> tableconstructor | ( STRING )=> STRING | CHARSTRING );", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:8: ( '(' ( ( explist1 )=> explist1 )? ')' )=> '(' ( ( explist1 )=> explist1 )? ')'
                    {
                    match(input,43,FOLLOW_43_in_args759); if (failed) return ;
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:11: ( ( explist1 )=> explist1 )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);
                    if ( ((LA22_0>=NAME && LA22_0<=HEX)||LA22_0==34||(LA22_0>=39 && LA22_0<=43)||LA22_0==48||LA22_0==51||(LA22_0>=64 && LA22_0<=65)) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:12: ( explist1 )=> explist1
                            {
                            pushFollow(FOLLOW_explist1_in_args761);
                            explist1();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,44,FOLLOW_44_in_args764); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:29: ( tableconstructor )=> tableconstructor
                    {
                    pushFollow(FOLLOW_tableconstructor_in_args769);
                    tableconstructor();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:48: ( STRING )=> STRING
                    {
                    match(input,STRING,FOLLOW_STRING_in_args773); if (failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:152:57: CHARSTRING
                    {
                    match(input,CHARSTRING,FOLLOW_CHARSTRING_in_args777); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 18, args_StartIndex); }
       }
        return ;
    }
    // $ANTLR end args


    // $ANTLR start function
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:155:1: function : 'function' funcbody[function] 'end' ;
    public void function() throws RecognitionException {   
        int function_StartIndex = input.index();
        Function function = new Function();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 19) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:156:4: ( 'function' funcbody[function] 'end' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:156:4: 'function' funcbody[function] 'end'
            {
            match(input,34,FOLLOW_34_in_function792); if (failed) return ;
            pushFollow(FOLLOW_funcbody_in_function794);
            funcbody(function);
            _fsp--;
            if (failed) return ;
            match(input,28,FOLLOW_28_in_function797); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 19, function_StartIndex); }
       }
        return ;
    }
    // $ANTLR end function


    // $ANTLR start funcbody
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:158:1: funcbody[Function function] : '(' ( ( parlist1 )=> parlist1 )? ')' chunk[function] ;
    public void funcbody(Function function) throws RecognitionException {   
        int funcbody_StartIndex = input.index();
        parlist1_return parlist15 = null;


        try {
            if ( backtracking>0 && alreadyParsedRule(input, 20) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:158:31: ( '(' ( ( parlist1 )=> parlist1 )? ')' chunk[function] )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:158:31: '(' ( ( parlist1 )=> parlist1 )? ')' chunk[function]
            {
            match(input,43,FOLLOW_43_in_funcbody806); if (failed) return ;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:158:34: ( ( parlist1 )=> parlist1 )?
            int alt24=2;
            int LA24_0 = input.LA(1);
            if ( (LA24_0==NAME||LA24_0==42) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:158:35: ( parlist1 )=> parlist1
                    {
                    pushFollow(FOLLOW_parlist1_in_funcbody808);
                    parlist15=parlist1();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                      function.addFunctionParameter(input.toString(parlist15.start,parlist15.stop));
                    }

                    }
                    break;

            }

            match(input,44,FOLLOW_44_in_funcbody814); if (failed) return ;
            pushFollow(FOLLOW_chunk_in_funcbody817);
            chunk(function);
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 20, funcbody_StartIndex); }
       }
        return ;
    }
    // $ANTLR end funcbody

    public static class parlist1_return extends ParserRuleReturnScope {
    };

    // $ANTLR start parlist1
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:161:1: parlist1 : ( ( namelist ( ( ',' '..' )=> ',' '..' )? )=> namelist ( ( ',' '..' )=> ',' '..' )? | '...' );
    public parlist1_return parlist1() throws RecognitionException {   
        parlist1_return retval = new parlist1_return();
        retval.start = input.LT(1);
        int parlist1_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 21) ) { return retval; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:161:12: ( ( namelist ( ( ',' '..' )=> ',' '..' )? )=> namelist ( ( ',' '..' )=> ',' '..' )? | '...' )
            int alt26=2;
            int LA26_0 = input.LA(1);
            if ( (LA26_0==NAME) ) {
                alt26=1;
            }
            else if ( (LA26_0==42) ) {
                alt26=2;
            }
            else {
                if (backtracking>0) {failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("161:1: parlist1 : ( ( namelist ( ( ',' '..' )=> ',' '..' )? )=> namelist ( ( ',' '..' )=> ',' '..' )? | '...' );", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:161:12: ( namelist ( ( ',' '..' )=> ',' '..' )? )=> namelist ( ( ',' '..' )=> ',' '..' )?
                    {
                    pushFollow(FOLLOW_namelist_in_parlist1831);
                    namelist();
                    _fsp--;
                    if (failed) return retval;
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:161:21: ( ( ',' '..' )=> ',' '..' )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);
                    if ( (LA25_0==30) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:161:22: ( ',' '..' )=> ',' '..'
                            {
                            match(input,30,FOLLOW_30_in_parlist1834); if (failed) return retval;
                            match(input,47,FOLLOW_47_in_parlist1836); if (failed) return retval;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:161:35: '...'
                    {
                    match(input,42,FOLLOW_42_in_parlist1842); if (failed) return retval;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);
            if ( backtracking>0 ) { memoize(input, 21, parlist1_StartIndex); }
       }
        return retval;
    }
    // $ANTLR end parlist1


    // $ANTLR start tableconstructor
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:164:1: tableconstructor : '{' ( ( fieldlist )=> fieldlist )? '}' ;
    public void tableconstructor() throws RecognitionException {   
        int tableconstructor_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 22) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:165:4: ( '{' ( ( fieldlist )=> fieldlist )? '}' )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:165:4: '{' ( ( fieldlist )=> fieldlist )? '}'
            {
            match(input,48,FOLLOW_48_in_tableconstructor855); if (failed) return ;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:165:8: ( ( fieldlist )=> fieldlist )?
            int alt27=2;
            int LA27_0 = input.LA(1);
            if ( ((LA27_0>=NAME && LA27_0<=HEX)||LA27_0==34||(LA27_0>=39 && LA27_0<=43)||LA27_0==45||LA27_0==48||LA27_0==51||(LA27_0>=64 && LA27_0<=65)) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:165:9: ( fieldlist )=> fieldlist
                    {
                    pushFollow(FOLLOW_fieldlist_in_tableconstructor858);
                    fieldlist();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,49,FOLLOW_49_in_tableconstructor862); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 22, tableconstructor_StartIndex); }
       }
        return ;
    }
    // $ANTLR end tableconstructor


    // $ANTLR start fieldlist
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:168:1: fieldlist : field ( ( fieldsep field )=> fieldsep field )* ( ( fieldsep )=> fieldsep )? ;
    public void fieldlist() throws RecognitionException {   
        int fieldlist_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 23) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:169:4: ( field ( ( fieldsep field )=> fieldsep field )* ( ( fieldsep )=> fieldsep )? )
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:169:4: field ( ( fieldsep field )=> fieldsep field )* ( ( fieldsep )=> fieldsep )?
            {
            pushFollow(FOLLOW_field_in_fieldlist874);
            field();
            _fsp--;
            if (failed) return ;
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:169:10: ( ( fieldsep field )=> fieldsep field )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);
                if ( (LA28_0==19||LA28_0==30) ) {
                    int LA28_1 = input.LA(2);
                    if ( ((LA28_1>=NAME && LA28_1<=HEX)||LA28_1==34||(LA28_1>=39 && LA28_1<=43)||LA28_1==45||LA28_1==48||LA28_1==51||(LA28_1>=64 && LA28_1<=65)) ) {
                        alt28=1;
                    }


                }


                switch (alt28) {
            	case 1 :
            	    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:169:11: ( fieldsep field )=> fieldsep field
            	    {
            	    pushFollow(FOLLOW_fieldsep_in_fieldlist877);
            	    fieldsep();
            	    _fsp--;
            	    if (failed) return ;
            	    pushFollow(FOLLOW_field_in_fieldlist879);
            	    field();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);

            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:169:28: ( ( fieldsep )=> fieldsep )?
            int alt29=2;
            int LA29_0 = input.LA(1);
            if ( (LA29_0==19||LA29_0==30) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:169:29: ( fieldsep )=> fieldsep
                    {
                    pushFollow(FOLLOW_fieldsep_in_fieldlist884);
                    fieldsep();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 23, fieldlist_StartIndex); }
       }
        return ;
    }
    // $ANTLR end fieldlist


    // $ANTLR start field
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:172:1: field : ( ( '[' exp ']' '=' exp )=> '[' exp ']' '=' exp | ( NAME '=' exp )=> NAME '=' exp | exp );
    public void field() throws RecognitionException {   
        int field_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 24) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:172:9: ( ( '[' exp ']' '=' exp )=> '[' exp ']' '=' exp | ( NAME '=' exp )=> NAME '=' exp | exp )
            int alt30=3;
            switch ( input.LA(1) ) {
            case 45:
                alt30=1;
                break;
            case NAME:
                int LA30_2 = input.LA(2);
                if ( (LA30_2==20) ) {
                    alt30=2;
                }
                else if ( (LA30_2==EOF||(LA30_2>=STRING && LA30_2<=CHARSTRING)||LA30_2==19||LA30_2==30||(LA30_2>=37 && LA30_2<=38)||LA30_2==43||LA30_2==45||(LA30_2>=47 && LA30_2<=63)) ) {
                    alt30=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("172:1: field : ( ( '[' exp ']' '=' exp )=> '[' exp ']' '=' exp | ( NAME '=' exp )=> NAME '=' exp | exp );", 30, 2, input);

                    throw nvae;
                }
                break;
            case STRING:
            case CHARSTRING:
            case INT:
            case FLOAT:
            case EXP:
            case HEX:
            case 34:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 48:
            case 51:
            case 64:
            case 65:
                alt30=3;
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("172:1: field : ( ( '[' exp ']' '=' exp )=> '[' exp ']' '=' exp | ( NAME '=' exp )=> NAME '=' exp | exp );", 30, 0, input);

                throw nvae;
            }

            switch (alt30) {
                case 1 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:172:9: ( '[' exp ']' '=' exp )=> '[' exp ']' '=' exp
                    {
                    match(input,45,FOLLOW_45_in_field896); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_field897);
                    exp();
                    _fsp--;
                    if (failed) return ;
                    match(input,46,FOLLOW_46_in_field898); if (failed) return ;
                    match(input,20,FOLLOW_20_in_field900); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_field902);
                    exp();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:172:29: ( NAME '=' exp )=> NAME '=' exp
                    {
                    match(input,NAME,FOLLOW_NAME_in_field906); if (failed) return ;
                    match(input,20,FOLLOW_20_in_field908); if (failed) return ;
                    pushFollow(FOLLOW_exp_in_field910);
                    exp();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:172:44: exp
                    {
                    pushFollow(FOLLOW_exp_in_field914);
                    exp();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 24, field_StartIndex); }
       }
        return ;
    }
    // $ANTLR end field


    // $ANTLR start fieldsep
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:175:1: fieldsep : (','|';');
    public void fieldsep() throws RecognitionException {   
        int fieldsep_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 25) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:175:9: ( (','|';'))
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:175:11: (','|';')
            {
            if ( input.LA(1)==19||input.LA(1)==30 ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_fieldsep924);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 25, fieldsep_StartIndex); }
       }
        return ;
    }
    // $ANTLR end fieldsep


    // $ANTLR start binop
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:177:1: binop : ('+'|'-'|'*'|'/'|'^'|'%'|'..'|'<'|'<='|'>'|'>='|'=='|'~='|'and'|'or');
    public void binop() throws RecognitionException {   
        int binop_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 26) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:177:7: ( ('+'|'-'|'*'|'/'|'^'|'%'|'..'|'<'|'<='|'>'|'>='|'=='|'~='|'and'|'or'))
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:177:9: ('+'|'-'|'*'|'/'|'^'|'%'|'..'|'<'|'<='|'>'|'>='|'=='|'~='|'and'|'or')
            {
            if ( input.LA(1)==47||(input.LA(1)>=50 && input.LA(1)<=63) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_binop937);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 26, binop_StartIndex); }
       }
        return ;
    }
    // $ANTLR end binop


    // $ANTLR start unop
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:181:1: unop : ('-'|'not'|'#');
    public void unop() throws RecognitionException {   
        int unop_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 27) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:181:6: ( ('-'|'not'|'#'))
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:181:8: ('-'|'not'|'#')
            {
            if ( input.LA(1)==51||(input.LA(1)>=64 && input.LA(1)<=65) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_unop1006);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 27, unop_StartIndex); }
       }
        return ;
    }
    // $ANTLR end unop


    // $ANTLR start number
    // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:185:1: number : (INT|FLOAT|EXP|HEX);
    public void number() throws RecognitionException {   
        int number_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 28) ) { return ; }
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:185:8: ( (INT|FLOAT|EXP|HEX))
            // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:185:10: (INT|FLOAT|EXP|HEX)
            {
            if ( (input.LA(1)>=INT && input.LA(1)<=HEX) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_number1024);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 28, number_StartIndex); }
       }
        return ;
    }
    // $ANTLR end number

    // $ANTLR start synpred5
    public void synpred5_fragment() throws RecognitionException {   
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:50:4: ( varlist1 '=' explist1 )
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:50:4: varlist1 '=' explist1
        {
        pushFollow(FOLLOW_varlist1_in_synpred5143);
        varlist1();
        _fsp--;
        if (failed) return ;
        match(input,20,FOLLOW_20_in_synpred5145); if (failed) return ;
        pushFollow(FOLLOW_explist1_in_synpred5147);
        explist1();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred5

    // $ANTLR start synpred6
    public void synpred6_fragment() throws RecognitionException {   
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:51:4: ( functioncall )
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:51:4: functioncall
        {
        pushFollow(FOLLOW_functioncall_in_synpred6153);
        functioncall();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred6

    // $ANTLR start synpred13
    public void synpred13_fragment() throws RecognitionException {   
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:56:4: ( 'for' NAME '=' )
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:56:5: 'for' NAME '='
        {
        match(input,29,FOLLOW_29_in_synpred13222); if (failed) return ;
        match(input,NAME,FOLLOW_NAME_in_synpred13224); if (failed) return ;
        match(input,20,FOLLOW_20_in_synpred13226); if (failed) return ;

        }
    }
    // $ANTLR end synpred13

    // $ANTLR start synpred16
    public void synpred16_fragment() throws RecognitionException {   
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:57:4: ( 'for' NAME ( ( ',' NAME )=> ',' NAME )? )
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:57:5: 'for' NAME ( ( ',' NAME )=> ',' NAME )?
        {
        match(input,29,FOLLOW_29_in_synpred16257); if (failed) return ;
        match(input,NAME,FOLLOW_NAME_in_synpred16259); if (failed) return ;
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:57:16: ( ( ',' NAME )=> ',' NAME )?
        int alt35=2;
        int LA35_0 = input.LA(1);
        if ( (LA35_0==30) ) {
            alt35=1;
        }
        switch (alt35) {
            case 1 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:57:17: ( ',' NAME )=> ',' NAME
                {
                match(input,30,FOLLOW_30_in_synpred16262); if (failed) return ;
                match(input,NAME,FOLLOW_NAME_in_synpred16264); if (failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred16

    // $ANTLR start synpred37
    public void synpred37_fragment() throws RecognitionException {   
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:135:17: ( binop exp )
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:135:17: binop exp
        {
        pushFollow(FOLLOW_binop_in_synpred37650);
        binop();
        _fsp--;
        if (failed) return ;
        pushFollow(FOLLOW_exp_in_synpred37652);
        exp();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred37

    // $ANTLR start synpred42
    public void synpred42_fragment() throws RecognitionException {   
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:34: ( ( ( ':' NAME )=> ':' NAME )? args )
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:34: ( ( ':' NAME )=> ':' NAME )? args
        {
        // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:34: ( ( ':' NAME )=> ':' NAME )?
        int alt37=2;
        int LA37_0 = input.LA(1);
        if ( (LA37_0==38) ) {
            alt37=1;
        }
        switch (alt37) {
            case 1 :
                // /Users/nm/Projects/eclipse_workspaces/lunareclipse/net.sf.lunareclipse/grammar/Lua.g:140:35: ( ':' NAME )=> ':' NAME
                {
                match(input,38,FOLLOW_38_in_synpred42702); if (failed) return ;
                match(input,NAME,FOLLOW_NAME_in_synpred42704); if (failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_args_in_synpred42708);
        args();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred42

    public boolean synpred16() {
        backtracking++;
        int start = input.mark();
        try {
            synpred16_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public boolean synpred37() {
        backtracking++;
        int start = input.mark();
        try {
            synpred37_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public boolean synpred5() {
        backtracking++;
        int start = input.mark();
        try {
            synpred5_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public boolean synpred6() {
        backtracking++;
        int start = input.mark();
        try {
            synpred6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public boolean synpred13() {
        backtracking++;
        int start = input.mark();
        try {
            synpred13_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public boolean synpred42() {
        backtracking++;
        int start = input.mark();
        try {
            synpred42_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_chunk_in_start71 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_start74 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_chunk_in_block100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stat_in_chunk113 = new BitSet(new long[]{0x0000081F21680012L});
    public static final BitSet FOLLOW_19_in_chunk117 = new BitSet(new long[]{0x0000081F21600012L});
    public static final BitSet FOLLOW_laststat_in_chunk126 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_chunk129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varlist1_in_stat143 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_stat145 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_explist1_in_stat147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functioncall_in_stat153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doBlockEnd_in_stat159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_stat165 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_stat167 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_doBlockEnd_in_stat169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_stat175 = new BitSet(new long[]{0x0000081F21E00010L});
    public static final BitSet FOLLOW_block_in_stat177 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_stat180 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_stat182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_stat187 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_stat189 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_stat191 = new BitSet(new long[]{0x0000081F3D600010L});
    public static final BitSet FOLLOW_block_in_stat193 = new BitSet(new long[]{0x000000001C000000L});
    public static final BitSet FOLLOW_26_in_stat197 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_stat199 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_stat201 = new BitSet(new long[]{0x0000081F3D600010L});
    public static final BitSet FOLLOW_block_in_stat203 = new BitSet(new long[]{0x000000001C000000L});
    public static final BitSet FOLLOW_27_in_stat209 = new BitSet(new long[]{0x0000081F31600010L});
    public static final BitSet FOLLOW_block_in_stat211 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_stat216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_stat231 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_stat233 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_stat235 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_stat237 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_stat239 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_stat241 = new BitSet(new long[]{0x0000000140000000L});
    public static final BitSet FOLLOW_30_in_stat244 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_stat246 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_doBlockEnd_in_stat250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_stat269 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_namelist_in_stat271 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_stat273 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_explist1_in_stat275 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_doBlockEnd_in_stat277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDeclaration_in_stat284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localFunctionDeclaration_in_stat291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_stat297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_doBlockEnd317 = new BitSet(new long[]{0x0000081F31600010L});
    public static final BitSet FOLLOW_block_in_doBlockEnd322 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_doBlockEnd328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_localVariableDeclaration348 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_namelist_in_localVariableDeclaration354 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_20_in_localVariableDeclaration357 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_explist1_in_localVariableDeclaration359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_localFunctionDeclaration381 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_localFunctionDeclaration383 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_localFunctionDeclaration385 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_funcbody_in_localFunctionDeclaration387 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_localFunctionDeclaration390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_laststat401 = new BitSet(new long[]{0x00090F84000007F2L,0x0000000000000003L});
    public static final BitSet FOLLOW_explist1_in_laststat404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_laststat413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_functionDeclaration446 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_funcname_in_functionDeclaration448 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_funcbody_in_functionDeclaration454 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_functionDeclaration457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_funcname468 = new BitSet(new long[]{0x0000006000000002L});
    public static final BitSet FOLLOW_37_in_funcname471 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_funcname473 = new BitSet(new long[]{0x0000006000000002L});
    public static final BitSet FOLLOW_38_in_funcname479 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_funcname481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_varlist1494 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_varlist1497 = new BitSet(new long[]{0x0000080000000010L});
    public static final BitSet FOLLOW_var_in_varlist1499 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_NAME_in_namelist512 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_namelist515 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_namelist517 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_exp_in_explist1533 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_explist1536 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_explist1538 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_39_in_exp562 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_40_in_exp569 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_41_in_exp576 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_number_in_exp584 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_STRING_in_exp592 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_CHARSTRING_in_exp599 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_42_in_exp605 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_function_in_exp611 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_tableconstructor_in_exp624 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_prefixexp_in_exp637 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_unop_in_exp644 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_exp646 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_binop_in_exp650 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_exp652 = new BitSet(new long[]{0xFFFC800000000002L});
    public static final BitSet FOLLOW_43_in_prefixexp669 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_prefixexp670 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_prefixexp671 = new BitSet(new long[]{0x0001286000000062L});
    public static final BitSet FOLLOW_NAME_in_prefixexp675 = new BitSet(new long[]{0x0001286000000062L});
    public static final BitSet FOLLOW_45_in_prefixexp689 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_prefixexp690 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_prefixexp691 = new BitSet(new long[]{0x0001286000000062L});
    public static final BitSet FOLLOW_37_in_prefixexp695 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_prefixexp697 = new BitSet(new long[]{0x0001286000000062L});
    public static final BitSet FOLLOW_38_in_prefixexp702 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_prefixexp704 = new BitSet(new long[]{0x0001080000000060L});
    public static final BitSet FOLLOW_args_in_prefixexp708 = new BitSet(new long[]{0x0001286000000062L});
    public static final BitSet FOLLOW_prefixexp_in_var736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prefixexp_in_functioncall749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_args759 = new BitSet(new long[]{0x00091F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_explist1_in_args761 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_args764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tableconstructor_in_args769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_args773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHARSTRING_in_args777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_function792 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_funcbody_in_function794 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_function797 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_funcbody806 = new BitSet(new long[]{0x0000140000000010L});
    public static final BitSet FOLLOW_parlist1_in_funcbody808 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_funcbody814 = new BitSet(new long[]{0x0000081F21600012L});
    public static final BitSet FOLLOW_chunk_in_funcbody817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_namelist_in_parlist1831 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_parlist1834 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_parlist1836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_parlist1842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_tableconstructor855 = new BitSet(new long[]{0x000B2F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_fieldlist_in_tableconstructor858 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_tableconstructor862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_field_in_fieldlist874 = new BitSet(new long[]{0x0000000040080002L});
    public static final BitSet FOLLOW_fieldsep_in_fieldlist877 = new BitSet(new long[]{0x00092F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_field_in_fieldlist879 = new BitSet(new long[]{0x0000000040080002L});
    public static final BitSet FOLLOW_fieldsep_in_fieldlist884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_field896 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_field897 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_field898 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_field900 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_field902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_field906 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_field908 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_field910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exp_in_field914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_fieldsep924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_binop937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_unop1006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_number1024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varlist1_in_synpred5143 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_synpred5145 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_explist1_in_synpred5147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functioncall_in_synpred6153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_synpred13222 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_synpred13224 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_synpred13226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_synpred16257 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_synpred16259 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_synpred16262 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_synpred16264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_binop_in_synpred37650 = new BitSet(new long[]{0x00090F84000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exp_in_synpred37652 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_synpred42702 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NAME_in_synpred42704 = new BitSet(new long[]{0x0001080000000060L});
    public static final BitSet FOLLOW_args_in_synpred42708 = new BitSet(new long[]{0x0000000000000002L});

}