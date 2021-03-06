package slime.sfcparser;
import java_cup.runtime.Symbol;
  /**
    *  <b>SFC.lex</b><br>
    *
    * initially provided by Marco Wendel <Marco.Wendel@gmx.info>
    * $Id: SFC.lex,v 1.16 2002-07-08 13:10:07 swprakt Exp $
    * -----
    */
   /* $Log: not supported by cvs2svn $
   /* Revision 1.15  2002/07/04 16:27:19  swprakt
   /* New Versions without the expressions-operations-precedence-error
   /* and with added support for doubles and for-loops.
   /*
   /* Revision 1.14  2002/07/02 15:23:50  swprakt
   /* you may look at example.procs.sfc and the output
   /* output.procs.txt for an idea what i am doing
   /* currently. The naming scheme for steps and actions has to
   /* be improved... (mwe)
   /*
   /* Revision 1.13  2002/07/02 13:09:53  swprakt
   /* "example.sfc" contains some constructs possible in this
   /* sfc-language. output.txt is the output of
   /* java slime.sfcparser.ParserTest example.sfc > output.txt.
   /* The last line was added by hand, you may use (if your os
   /* does support this) 2>&1 for outputting the last line into
   /* that file too.
   /* The error that occurs in an error within the
   /* slime.utils.PrettyPrint,
   /*
   /* I will try to fix this error now, but the fixed version
   /* will reside in slime/sfcparser, because i do not want to
   /* change someones code without a prior discussion.
   /* Btw. someone modified my SFC.lex with the result that
   /* the whilestatement did not function for a long period of
   /* time - thank you very much.
   /*
   /* My advice for the next semester would be to use LOGIN-
   /* accounts for cvs. In my humble opinion it was Thomas
   /* Richter who was the first to propose this. It makes the
   /* logs more useful and avoids problems with $CVS_RSH.
   /* (mwe)
   /*
   /* Revision 1.12  2002/07/02 12:29:48  swprakt
   /* Phase 1 completed, correct parsing of complex
   /* SFC programs into slime.absynt.absfc.SFCabtree.
   /*
   /* Phase 2 still in progress: today fixed some bug
   /* with PrettyPrint4Absfc - partially the print(SFCabtree)
   /* by simply outcommenting the lists for vars and decls.
   /*
   /* Left to do in phase 2:
   /* verifying the correct(and complete!) creation of
   /* the varlist, decllist for the slime.absynt.SFC.
   /*
   /* 02.07.2002 mwe@informatik.uni-kiel.de
   /*
   /* Revision 1.11  2002/06/28 20:30:50  swprakt
   /* updated package information
   /*
   /* Revision 1.10  2002/06/28 20:01:08  swprakt
   /* Modified PrettyPrint for use with slime.absfc.SFCabtree,
   /* may now use it to debug Absfc2SFCConverter. I will try
   /* to check in a fully functional SFCParser incl. nested
   /* statements within one week, so 'round about 05/07/2002.
   /* (mwe)
   /*
   /* Revision 1.9  2002/06/28 08:03:12  swprakt
   /* old versions did conflict with "global" Makefile
   /* in src/slime, albeight the GLOBAL Makefile should
   /* exist in src. (mwe)
   /*
   /* Revision 1.8  2002/06/27 20:20:18  swprakt
   /* The SFCSymbols.EOF symbol has is an int with value 0.
   /* The YYlex.EOF is a symbol with int value -1.
   /* SFCParser now correctly parses 1+1, 7*8.. (mwe)
   /*
   /* Revision 1.7  2002/06/27 19:39:37  swprakt
   /* slightly improved exceptionhandling
   /*
   /* Revision 1.6  2002/06/27 14:30:57  swprakt
   /* tried to remove Error 19: "_public ugliness",
   /* now header comments consist of two parts, one
   /* for javadoc and one containing CVS Versio log.
   /*
    * Revision 1.5  2002/06/26 10:39:48  swprakt
    * yy_eof muss nun noch irgendwie behandelt werden...
    *
    * Revision 1.4  2002/06/26 09:19:34  swprakt
    * Debug die 1.: von einer Expression 1+1 parst er nur
    * 1+ korrekt, danach Abbruch
    *
    * Revision 1.2  2002/06/26 06:50:57  swprakt
    * removed unused entries in SFC.lex
    * and removed wrong entry in SFC.cup "stmtblock" in "stmtlist"
    *
    * Revision 1.1  2002/06/25 15:02:51  swprakt
    * missing files added
    * -------------------------------------------------
    * Revision 1.3  2002/05/05 22:24:12  mwe
    * added something from the grammar
    * i think something like slime.absynt.process
    * might be useful. some constructors within
    * slime.absynt.constval have to be added if
    * double/string support is required.
    *
    * Revision 1.2  2002/05/01 11:35:08  mwe
    * first version of SFC.lex
    * ATTENTION: %function has to be changed or removed
    * you can set "scan with" and set the corresponding function
    * COMMENTS wellcome
    *
    * Revision 1.1  2002/05/01 11:06:27  mwe
    * *** empty log message ***
    *
    *
    * Comments:
    * %cup 	-	it should work together with cup
    * %char	-	yychar() is accessible in Symboldefinition
    * %line	-	yyline() contains the current line in src-file
    * %function - 	use instead of next_Token()
    * %notunix	-	does not care about ^M :) (skips \r)
    * %ignorecase
    * %init{
    * %init}
    * %yy_eof
    * %initthrow{ ....  see JLex Manual for more options
    **/
%%
%char
%line
%class SFCLex
%implements java_cup.runtime.Scanner
%function next_token
%notunix
%full
%public
%eofval{
    return new Symbol(slime.sfcparser.SFCSymbols.EOF); //Symbol(-1);
%eofval}
%cup
%eof{
    // System.out.println("\nEOF");
%eof}
c1      = ("//".*)
c2      = ("/*".*"*/")
c3      = ("(*".*"*)")
comment	= ({c1}|{c2}|{c3})
space	= [\ \r\t\f\n\b]+
alpha   = [A-Za-z]
extra   = ( "!" | "$" | "%" | "/" | "(" | ")" | "=" | "?" | "[" | "]" | "{" | "}" | "\" | "_" | "." | ":" | ";" | "#" | "*" )
letter	= ({alpha}|"_")
digit	= [0-9]
sign    = [+-]?
exp     = ([eE]{sign}{digit}+)
number	= {digit}+ 
rnumber = {number}\.{number}
enumber = ({digit}+\.{digit}*{exp}?|{digit}*\.{digit}+{exp}?|{digit}+{exp})
identif = ({letter}({letter}|{digit})*)
string  = (\"({space}|{alpha}|{digit}|{sign}|{extra})*\")

%%  

{space}    { }
{comment}  { /* return new Symbol(slime.sfcparser.SFCSymbols.COMMENT); */ }
";"        {return new Symbol(slime.sfcparser.SFCSymbols.SEMICOLON);	/* */}
"{"        {return new Symbol(slime.sfcparser.SFCSymbols.LPSET); 	/* */}
"}"        {return new Symbol(slime.sfcparser.SFCSymbols.RPSET); 	/* */}
"["        {return new Symbol(slime.sfcparser.SFCSymbols.LPFIELD); 	/* */}
"]"        {return new Symbol(slime.sfcparser.SFCSymbols.RPFIELD); 	/* */}
"("        {return new Symbol(slime.sfcparser.SFCSymbols.LPAREN); 	/* */}
")"        {return new Symbol(slime.sfcparser.SFCSymbols.RPAREN); 	/* */}
"++"       {return new Symbol(slime.sfcparser.SFCSymbols.INC); 	        /* */}
"+"        {return new Symbol(slime.sfcparser.SFCSymbols.ADD); 	/* Expr.PLUS    =  0 */}
"--"       {return new Symbol(slime.sfcparser.SFCSymbols.DEC); 	        /* */}
"-"        {return new Symbol(slime.sfcparser.SFCSymbols.SUB); 	/* Expr.MINUS   =  1 */}
"*"        {return new Symbol(slime.sfcparser.SFCSymbols.MUL); 	/* Expr.TIMES   =  2 */}
"/"        {return new Symbol(slime.sfcparser.SFCSymbols.DIV); 	/* Expr.DIV     =  3 */}
"&&"       {return new Symbol(slime.sfcparser.SFCSymbols.AND); 	/* Expr.AND     =  4 */}
"||"       {return new Symbol(slime.sfcparser.SFCSymbols.OR);  	/* Expr.OR      =  5 */}
"!"        {return new Symbol(slime.sfcparser.SFCSymbols.NOT); 	/* Expr.NEG     =  6 */}
"=="	   {return new Symbol(slime.sfcparser.SFCSymbols.EQ);   /* Expr.EQ      =  7 */}
"<"        {return new Symbol(slime.sfcparser.SFCSymbols.LT);  	/* Expr.LESS    =  8 */}
">"        {return new Symbol(slime.sfcparser.SFCSymbols.GT);  	/* Expr.GREATER =  9 */}
"<="       {return new Symbol(slime.sfcparser.SFCSymbols.LEQ); 	/* Expr.LEQ     = 10 */}
">="       {return new Symbol(slime.sfcparser.SFCSymbols.GEQ); 	/* Expr.GEQ     = 11 */}
"!="       {return new Symbol(slime.sfcparser.SFCSymbols.NEQ); 	/* Expr.NEQ     = 12 */}
"%"        {return new Symbol(slime.sfcparser.SFCSymbols.MOD); 	/* Expr.MOD     = 13 */}
"^"        {return new Symbol(slime.sfcparser.SFCSymbols.POW); 	        /* Expr.POW = 14 */}
","        {return new Symbol(slime.sfcparser.SFCSymbols.COMMA); 	/* */}
"."        {return new Symbol(slime.sfcparser.SFCSymbols.DOT); 		/* */}
"#"        {return new Symbol(slime.sfcparser.SFCSymbols.HASH); 	/* */}
"SFC"      {return new Symbol(slime.sfcparser.SFCSymbols.SFCPRG);	/* Begin SFC-Source-File */}
"input"    {return new Symbol(slime.sfcparser.SFCSymbols.INPUT); 	/* syntax !? */}
"output"   {return new Symbol(slime.sfcparser.SFCSymbols.OUTPUT); 	/* syntax !? */}
"split"    {return new Symbol(slime.sfcparser.SFCSymbols.SPLIT); 	/* */}
"join"     {return new Symbol(slime.sfcparser.SFCSymbols.JOIN); 	/* */}
"process"  {return new Symbol(slime.sfcparser.SFCSymbols.PROCESS); 	/* */}
"if"       {return new Symbol(slime.sfcparser.SFCSymbols.IF); 		/* */}
"else"     {return new Symbol(slime.sfcparser.SFCSymbols.ELSE); 	/* */}
"while"    {return new Symbol(slime.sfcparser.SFCSymbols.WHILE); 	/* */}
"repeat"   {return new Symbol(slime.sfcparser.SFCSymbols.REPEAT); 	/* */}
"until"    {return new Symbol(slime.sfcparser.SFCSymbols.UNTIL); 	/* */}
"for"      {return new Symbol(slime.sfcparser.SFCSymbols.FOR); 	        /* */}
"skip"     {return new Symbol(slime.sfcparser.SFCSymbols.SKIP); 	/* Class Skip */}
"="        {return new Symbol(slime.sfcparser.SFCSymbols.ASSIGN); 	/* Class Assign */}
"int"      {return new Symbol(slime.sfcparser.SFCSymbols.INTTYPE); 	/* Class IntType */}
"bool"     {return new Symbol(slime.sfcparser.SFCSymbols.BOOLTYPE); 	/* Class BoolType */}
"double"   {return new Symbol(slime.sfcparser.SFCSymbols.DOUBLETYPE); 	/* Class DoubleType */}
"true"     {return new Symbol(slime.sfcparser.SFCSymbols.TRUE);	        /* */}
"false"    {return new Symbol(slime.sfcparser.SFCSymbols.FALSE); 	/* */}
{identif}  {return new Symbol(slime.sfcparser.SFCSymbols.IDENTIFIER, yytext()); /* t IDENTIFIER */}
{number}   {return new Symbol(slime.sfcparser.SFCSymbols.INTEGER, new Integer( yytext() ) );/* t INTEGER */}
{enumber}  {return new Symbol(slime.sfcparser.SFCSymbols.DOUBLE, new Double( yytext() ) );/* t DOUBLE */}
.          {System.out.println( "Error during lexical analysis"+
	    "\nLine number = "  + yyline + 
	    "\nChar number = "  + yychar +
	    "\nText content = " + yytext() ); 		/* LexError */}

