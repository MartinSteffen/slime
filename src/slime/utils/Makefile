#----------------------------------------------------------------------
#	Utilities for the Slime Tool
#----------------------------------------------------------------------

LEXLOGFILE = JLex.log
PACKAGE    = utils


all:	TestParser.class $(patsubst %.java,%.class,$(wildcard *.java))
debug:	; make JAVACOPTS="-g" all

%.class:	%.java
		javac $(JAVACOPTS) $<

TestParser.class: Parser.class TestParser.java ExprParser.class DeclParser.class
	javac TestParser.java

Parser.class: Parser.java Yylex.java ParseException.java
	javac Parser.java sym.java Yylex.java ParseException.java

ExprParser.class: ExprParser.java ParseException.java
	javac ExprParser.java sym.java Yylex.java ParseException.java

DeclParser.class: DeclParser.java ParseException.java
	javac DeclParser.java sym.java Yylex.java ParseException.java

Yylex.java: SAP.lex
	java JLex.Main SAP.lex >$(LEXLOGFILE)
	mv SAP.lex.java Yylex.java

Parser.java: SAP.cup
	java java_cup.Main -symbols sym -parser Parser -nonterms -package $(PACKAGE) SAP.cup 

ExprParser.java: Expr.cup
	java java_cup.Main -symbols sym -parser ExprParser -nonterms -package $(PACKAGE) Expr.cup

DeclParser.java: Decl.cup
	java java_cup.Main -symbols sym -parser DeclParser -nonterms -package $(PACKAGE) Decl.cup

tests: 
	cd test; make all


clean:
	\rm -f *.class *~ Yylex.java Scanner.lex.java sym.java Parser.java DeclParser.java ExprParser.java $(LEXLOGFILE) $(CUPLOGFILE)




# ------------------------------------------------------------------
#	$Log: not supported by cvs2svn $
#	Revision 1.6  2002/07/03 16:34:26  swprakt
#	*** empty log message ***
#	
#	Revision 1.5  2002/06/26 15:43:10  swprakt
#	added declaration parser
#	
#	Revision 1.4  2002/06/10 14:41:57  swprakt
#	added expression parser
#	
#	Revision 1.3  2002/06/07 15:04:17  swprakt
#	added parser for SAP programs, added function printSAP() to PP
#	
# -------------------------------------------------------------------	









