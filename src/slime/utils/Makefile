#----------------------------------------------------------------------
#	Utilities for the Slime Tool
#----------------------------------------------------------------------

LEXLOGFILE = JLex.log
PACKAGE    = utils


all:	TestParser.class $(patsubst %.java,%.class,$(wildcard *.java))
debug:	; make JAVACOPTS="-g" all

%.class:	%.java
		javac $(JAVACOPTS) $<

TestParser.class: Parser.class TestParser.java
	javac TestParser.java

Parser.class:	Parser.java Yylex.java ParseException.java
	javac Parser.java sym.java Yylex.java ParseException.java

Yylex.java:		SAP.lex
	java JLex.Main SAP.lex >$(LEXLOGFILE)
	mv SAP.lex.java Yylex.java

Parser.java:	SAP.cup
	java java_cup.Main -symbols sym -parser Parser -nonterms -package $(PACKAGE) SAP.cup 

tests: 
	cd test; make all

clean:
	\rm -f *.class *~ Yylex.java Scanner.lex.java sym.java Parser.java $(LEXLOGFILE) $(CUPLOGFILE)


localdoc:	parser.java	sym.java
	javadoc  -version -author -d $(LOCALDOCDIR) *.java

www:
	cp $(LOCALDOCDIR)/*.html $(WEBDIR)


# ------------------------------------------------------------------
#	$Log: not supported by cvs2svn $
# -------------------------------------------------------------------	









