
all:	$(patsubst %.java,%.class,$(wildcard *.java))


test:  
	java slime.checks.tests.TypecheckTest

%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java


######################################################
## $Id: Makefile,v 1.3 2002-07-05 17:38:32 swprakt Exp $
######################################################