package slime.sfcparser;
import java_cup.runtime.Symbol;
  /**
    * initially provided by Marco Wendel <mwe@informatik.uni-kiel.de>
    * $Id: SFC.lex,v 1.3 2002-06-26 08:27:05 swprakt Exp $
    * -----
    * $Log: not supported by cvs2svn $
    * Revision 1.2  2002/06/26 06:50:57  swprakt
    * removed unused entries in SFC.lex
    * and removed wrong entry in SFC.cup "stmtblock" in "stmtlist"
    *
    * Revision 1.1  2002/06/25 15:02:51  swprakt
    * missing files added
    *
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
    *		- THIS HAS TO BE CHANGED TO GET A RUNNING SFCParser.class :)
    * %notunix	-	does not care about ^M :) (skips \r)
    * %init{
    * %init}

"++"       {return new Symbol(SFCSymbols.INC); }
"--"	   {return new Symbol(SFCSymbols.DEC); }


    **/
%%
%char
%line
%class SFCLex
%notunix
%ignorecase
%full
%public
%cup

c1      = ("//".*)
c2      = ("/*".*"*/")
c3      = ("(*".*"*)")
comment	= ({c1}|{c2}|{c3})
space	= [\ \r\t\f\n\b\015\012]+
alpha   = [A-Za-z]
extra   = ("!"|"$"|"%"|"/"|"("|")"|"="|"?"|"["|"]"|"{"|"}"|"\"|"_"|"."|":"|";"|"#"|"*")
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

{space}	   {}
{comment}  {return new Symbol(SFCSymbols.COMMENT);}
"+"        {return new Symbol(SFCSymbols.ADD); 	/* Expr.PLUS    =  0 */}
"-"        {return new Symbol(SFCSymbols.SUB); 	/* Expr.MINUS   =  1 */}
"*"        {return new Symbol(SFCSymbols.MUL); 	/* Expr.TIMES   =  2 */}
"/"        {return new Symbol(SFCSymbols.DIV); 	/* Expr.DIV     =  3 */}
"%"        {return new Symbol(SFCSymbols.MOD); 	/* Expr.MOD     =  undef */}
"&&"       {return new Symbol(SFCSymbols.AND); 	/* Expr.AND     =  4 */}
"||"       {return new Symbol(SFCSymbols.OR);  	/* Expr.OR      =  5 */}
"!"        {return new Symbol(SFCSymbols.NOT); 	/* Expr.NEG     =  6 */}
"=="	   {return new Symbol(SFCSymbols.EQ);   /* Expr.EQ      =  7 */}
"<"        {return new Symbol(SFCSymbols.LT);  	/* Expr.LESS    =  8 */}
">"        {return new Symbol(SFCSymbols.GT);  	/* Expr.GREATER =  9 */}
"<="       {return new Symbol(SFCSymbols.LEQ); 	/* Expr.LEQ     = 10 */}
">="       {return new Symbol(SFCSymbols.GEQ); 	/* Expr.GEQ     = 11 */}
"!="       {return new Symbol(SFCSymbols.NEQ); 	/* Expr.NEQ     = 12 */}
"true"     {return new Symbol(SFCSymbols.TRUE);	        /* */}
"false"    {return new Symbol(SFCSymbols.FALSE); 	/* */}
"int"      {return new Symbol(SFCSymbols.INTTYPE); 	/* Class IntType */}
"bool"     {return new Symbol(SFCSymbols.BOOLTYPE); 	/* Class BoolType */}
"="        {return new Symbol(SFCSymbols.ASSIGN); 	/* Class Assign */}
"skip"     {return new Symbol(SFCSymbols.SKIP); 	/* Class Skip */}
"("        {return new Symbol(SFCSymbols.LPAREN); 	/* */}
")"        {return new Symbol(SFCSymbols.RPAREN); 	/* */}
"{"        {return new Symbol(SFCSymbols.LPSET); 	/* */}
"}"        {return new Symbol(SFCSymbols.RPSET); 	/* */}
","        {return new Symbol(SFCSymbols.COMMA); 	/* */}
";"        {return new Symbol(SFCSymbols.SEMICOLON);	/* */}
"SFC"      {return new Symbol(SFCSymbols.SFCPRG);	/* Begin SFC-Source-File */}
"input"    {return new Symbol(SFCSymbols.INPUT); 	/* ***RFC*** syntax !? */}
"output"   {return new Symbol(SFCSymbols.OUTPUT); 	/* ***RFC*** syntax !? */}

"split"    {return new Symbol(SFCSymbols.SPLIT); 	/* */}
"join"     {return new Symbol(SFCSymbols.JOIN); 	/* */}
"process"  {return new Symbol(SFCSymbols.PROCESS); 	/* */}

"if"       {return new Symbol(SFCSymbols.IF); 		/* */}
"else"     {return new Symbol(SFCSymbols.ELSE); 	/* */}
"while"    {return new Symbol(SFCSymbols.LPSET); 	/* */}
"repeat"   {return new Symbol(SFCSymbols.REPEAT); 	/* */}
"until"    {return new Symbol(SFCSymbols.UNTIL); 	/* */}

{identif}  {return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
{number}   {return new Symbol(SFCSymbols.INTEGER, 
		   new Integer(yytext())); 		/* t INTEGER */}
.          {System.out.println( "Error during lexical analysis"+
	    "\nLine number = "  + yyline + 
	    "\nChar number = "  + yychar +
	    "\nText content = " + yytext() ); 		/* LexError */}













